package com.chen.mybatis.scripting.defaults;

import com.chen.mybatis.builder.SqlSourceBuilder;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.scripting.xmltags.DynamicContext;
import com.chen.mybatis.scripting.xmltags.MixedSqlNode;
import com.chen.mybatis.scripting.xmltags.SqlNode;
import com.chen.mybatis.session.Configuration;

import java.util.HashMap;

/**
 * 原始SQL 源码, 比 DynamicSqlSource 动态SQL 处理快
 */
public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceParser.parse(sql, clazz, new HashMap<>());
    }


    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext context = new DynamicContext(configuration, null);
        rootSqlNode.apply(context);
        return context.getSql();
    }
}
