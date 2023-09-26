package com.chen.mybatis.binding;

import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.SqlCommandType;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * description 映射器方法
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/26
 */
public class MapperMethod {

    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface,Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration,mapperInterface,method);
    }

    public Object execute(SqlSession sqlSession,Object[] args){
        Object result = null;
        switch (command.getType()) {
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getName(),args);
                break;
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
}
