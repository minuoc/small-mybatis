package com.chen.mybatis.session.defaults;

import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.Environment;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return this.selectOne(statement,null);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
       MappedStatement ms = configuration.getMappedStatement(statement);
       List<T> list = executor.query(ms,parameter,Executor.NO_RESULT_HANDLER,ms.getSqlSource().getBoundSql(parameter));
       return list.get(0);
    }



    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }


    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
