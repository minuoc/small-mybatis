package com.chen.mybatis.session.defaults;

import com.chen.mybatis.binding.MapperRegistry;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;


    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
       return (T) ("你被代理了！" + statement );
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("你被代理了！" + "方法：" + statement + " 入参：" + parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type,this);
    }


    public Configuration getConfiguration(){
        return configuration;
    }
}
