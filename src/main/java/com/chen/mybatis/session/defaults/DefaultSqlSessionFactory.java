package com.chen.mybatis.session.defaults;

import com.chen.mybatis.binding.MapperRegistry;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.SqlSession;
import com.chen.mybatis.session.SqlSessionFactory;
import com.chen.mybatis.session.defaults.DefaultSqlSession;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;


    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
