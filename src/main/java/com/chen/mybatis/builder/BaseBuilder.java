package com.chen.mybatis.builder;

import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.TypeAliasRegistry;
import com.chen.mybatis.type.TypeHandlerRegistry;

/**
 * 构建器基类
 */
public abstract class BaseBuilder {

    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;

    protected final TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration(){
        return configuration;
    }


    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }
}
