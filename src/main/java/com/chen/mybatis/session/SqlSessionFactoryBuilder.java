package com.chen.mybatis.session;

import com.chen.mybatis.builder.XmlConfigBuilder;
import com.chen.mybatis.session.defaults.DefaultSqlSession;

import java.io.Reader;

/**
 * 构建SqlSessionFactory的工厂
 */
public class SqlSessionFactoryBuilder {

    public SqlSession build(Reader reader){
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSession build(Configuration config){
        return new DefaultSqlSession(config);
    }
}
