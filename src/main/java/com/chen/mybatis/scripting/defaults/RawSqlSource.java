package com.chen.mybatis.scripting.defaults;

import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.scripting.xmltags.MixedSqlNode;
import com.chen.mybatis.session.Configuration;

/**
 * 原始SQL 源码, 比 DynamicSqlSource 动态SQL 处理快
 */
public class RawSqlSource implements SqlSource{

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, MixedSqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration,getSql(configuration,rootSqlNode),parameterType);
    }


    public RawSqlSource(Configuration configuration,String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceBuilder = new SqlSourceBuilder(configuration);

        sqlSource = sqlSourcePara
    }



    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return null;
    }

    private static MixedSqlNode getSql(Configuration configuration, MixedSqlNode rootSqlNode) {
        return null;
    }
}
