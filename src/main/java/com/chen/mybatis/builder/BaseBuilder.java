package com.chen.mybatis.builder;

import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.TypeAliasRegistry;

/**
 * 构建器基类
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration(){
        return configuration;
    }
}
