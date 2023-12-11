package com.chen.mybatis.builder;

import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.ParameterMapping;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.session.Configuration;

import java.util.List;

/**
 * 静态 SQL 源码
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/11
 */
public class StaticSqlSource implements SqlSource {
    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Configuration configuration;

    public StaticSqlSource(Configuration configuration, String sql) {
        this(configuration, sql, null);
    }

    public StaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }


    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(configuration, sql, parameterMappings, parameterObject);
    }
}
