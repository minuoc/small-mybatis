package com.chen.mybatis.binding;

import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.SqlCommandType;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.*;

/**
 * description 映射器方法
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/26
 */
public class MapperMethod {

    private final SqlCommand command;

    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface,Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration,mapperInterface,method);
        this.method = new MethodSignature(configuration,method);
    }

    public Object execute(SqlSession sqlSession,Object[] args){
        Object result = null;
        switch (command.getType()) {
            case INSERT: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.insert(command.getName(), param);
                break;
            }
            case DELETE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.delete(command.getName(), param);
                break;
            }
            case UPDATE: {
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.update(command.getName(),param);
                break;
            }
            case SELECT: {
                Object param = method.convertArgsToSqlCommandParam(args);
                if (method.returnsMany) {
                    result = sqlSession.selectList(command.getName(),param);
                } else {
                    result = sqlSession.selectOne(command.getName(),param);
                }
                break;
            }
            default:
                throw new RuntimeException("Unknown execution method for:" + command.getName());
        }
        return result;
    }

    public static class SqlCommand {
        private final String name;
        private final SqlCommandType type;
        public SqlCommand(Configuration configuration,Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement ms = configuration.getMappedStatement(statementName);
            name = ms.getId();
            type = ms.getSqlCommandType();

        }
        public String getName() {
            return name;
        }
        public SqlCommandType getType() {
            return type;
        }
    }


    /**
     * 方法签名
     */
    public static class MethodSignature {

        private final boolean returnsMany;

        private final Class<?> returnType;
        private final SortedMap<Integer,String> params;

        public MethodSignature(Configuration configuration, Method method) {
            this.returnType = method.getReturnType();
            this.returnsMany = (configuration.getObjectFactory()).isCollection(this.returnType) || this.returnType.isArray();
            this.params = Collections.unmodifiableSortedMap(getParams(method));
        }

        public Object convertArgsToSqlCommandParam(Object[] args) {
            final int paramCount = args.length;
            if (args == null || paramCount == 0) {
                return null;
            } else if (paramCount == 1) {
                return args[params.keySet().iterator().next().intValue()];
            } else {
                // 否则，返回一个ParamMap，修改参数名，参数名就是其位置
                final Map<String,Object> param = new ParamMap<Object>();
                int i = 0;
                for (Map.Entry<Integer,String> entry : params.entrySet()) {
                    // 1.先加一个#{0},#{1},#{2}....参数
                    param.put(entry.getValue(),args[entry.getKey().intValue()]);

                    final String genericParamName = "param" + (i + 1);
                    if (!param.containsKey(genericParamName)) {
                        /**
                         * 2. 再加一个#{param1},#{param2}... 参数
                         * 你可以传递多个参数给一个映射器方法。
                         */
                        param.put(genericParamName,args[entry.getKey()]);
                    }
                    i++;
                }
                return param;
            }
        }

        private SortedMap<Integer,String> getParams(Method method) {
            // 用一个TreeMap, 这样就保证还是按参数的先后顺序
            final SortedMap<Integer,String> params = new TreeMap<>();
            final Class<?>[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                String paramName = String.valueOf(argTypes[i]);
                params.put(i,paramName);
            }
            return params;
        }

        public boolean returnsMany(){
            return returnsMany;
        }
    }

    /**
     * 参数map,静态内部类，更严格的get方法，如果没有相应的key,报错
     * @param <V>
     */
    public static class ParamMap<V> extends HashMap<String,V> {

        private static final long serialVersionUID = 1L;

        @Override
        public V get(Object key) {
            if (!super.containsKey(key)) {
                throw new RuntimeException("Parameter '" + key + "' not found. Available parameters are " + keySet());
            }
            return super.get(key);
        }

    }
}
