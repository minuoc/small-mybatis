package com.chen.mybatis.mapping;

import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.JdbcType;
import com.chen.mybatis.type.TypeHandler;

public class ResultMapping {

    private Configuration configuration;

    private String property;


    private String column;

    private Class<?> javaType;

    private JdbcType jdbcType;


    private TypeHandler typeHandler;


    public ResultMapping() {
    }


    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();
    }
}
