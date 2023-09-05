package com.chen.mybatis.builder;

import com.chen.mybatis.session.Configuration;

/**
 * 构建器基类
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration(){
        return configuration;
    }
}
