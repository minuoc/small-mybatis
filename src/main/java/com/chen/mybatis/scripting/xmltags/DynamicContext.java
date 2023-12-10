package com.chen.mybatis.scripting.xmltags;


import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.session.Configuration;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态上下文
 */
public class DynamicContext {

    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    public static final String DATABASE_ID_KEY = "_databaseId";


    static {
        // 定义属性 -> getter 方法映射， ContextMap 到 ContextAccessor的 映射， 注册到 ognl运行时
        // 参考http://commons.apache.org/proper/commons-ognl/developer-guide.html
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
        // 将传入的参数对象统一封装为ContextMap对象 （继承了 HashMap对象）
        // 然后Ognl运行时环境再动态计算Sql 语句时，
        // 会按照ContextAccessor 中描述的Map接口的方式来访问和读取ContextMap对象，获取计算过程中需要的参数。
        // ContextMap 对象内部可能封装了一个普通的POJO 对象，也可以是直接传递的Map 对象，当然从外部时看不出来的，因为时是使用Map的接口来读取数据。

    }

    private final ContextMap bindings;

    private final StringBuilder sqlBuilder = new StringBuilder();

    private int uniqueNumber = 0;


    public DynamicContext(Configuration configuration, Object parameterObject) {

        // 绝大多数调用的地方parameterObject 为 null;
        if (parameterObject != null && !(parameterObject instanceof Map)) {
            // 如果 时
            MetaObject metaObject = configuration.newMetaObject(parameterObject);
            bindings = new ContextMap(metaObject);
        } else {
            bindings = new ContextMap(null);
        }
        bindings.put(PARAMETER_OBJECT_KEY,parameterObject);
        bindings.put(DATABASE_ID_KEY,configuration.getDatabaseId());

    }

    public ContextMap getBindings() {
        return bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name,value);
    }


    public void appendSql(String sql){
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql(){
        return sqlBuilder.toString().trim();
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    static class ContextMap extends HashMap<String,Object>  {
        private MetaObject parameterMetaObject;

        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        public Object get(Object key) {
            String strKey = (String)key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }
            // 如果没有找到，再用 ongl 表达式去取值
            // 如果 person[0].birthday.year
            if (parameterMetaObject != null) {
                return parameterMetaObject.getValue(strKey);
            }
            return null;
        }


    }

    /**
     * 上下文访问器 静态内部类 实现 OGNL 的 PropertyAccessor
     */
    static class ContextAccessor implements PropertyAccessor {


        @Override
        public Object getProperty(Map context, Object target, Object name) throws OgnlException {
            Map map = (Map) target;
            Object result = map.get(name);
            if (result != null) {
                return result;
            }
            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }
            return null;
        }

        @Override
        public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
            Map<Object,Object> map = (Map<Object,Object>) target;
            map.put(name,value);
        }

        @Override
        public String getSourceAccessor(OgnlContext ognlContext, Object o, Object o1) {
            return null;
        }

        @Override
        public String getSourceSetter(OgnlContext ognlContext, Object o, Object o1) {
            return null;
        }
    }
}
