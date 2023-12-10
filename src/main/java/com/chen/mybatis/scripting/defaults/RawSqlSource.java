package com.chen.mybatis.scripting.defaults;

import com.chen.mybatis.scripting.xmltags.MixedSqlNode;
import com.chen.mybatis.session.Configuration;

/**
 * 原始SQL 源码, 比 DynamicSqlSource 动态SQL 处理快
 */
public class RawSqlSource {



    public RawSqlSource(Configuration configuration, MixedSqlNode rootSqlNode, Class<?> parameterType) {
    }
}
