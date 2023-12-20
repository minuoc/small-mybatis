package com.chen.mybatis.session;

import com.chen.mybatis.builder.xml.XMLConfigBuilder;
import com.chen.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * 构建SqlSessionFactory的工厂
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader){
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration config){
        return new DefaultSqlSessionFactory(config);
    }
}
