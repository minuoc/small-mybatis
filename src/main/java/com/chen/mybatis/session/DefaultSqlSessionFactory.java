package com.chen.mybatis.session;

import com.chen.mybatis.binding.MapperRegistry;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private final MapperRegistry mapperRegistry;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegistry);
    }
}
