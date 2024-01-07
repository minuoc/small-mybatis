package com.chen.mybatis.scripting;

import com.chen.mybatis.executor.parameter.ParameterHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Element;


public interface LanguageDriver {

    /**
     * 创建SQL 源码(mapper xml 方式)
     * @param configuration
     * @param script
     * @param parameterType
     * @return
     */
    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);


    /**
     * 创建SQL 源码(annotation 方式)
     * @param configuration
     * @param script
     * @param parameterType
     * @return
     */
    SqlSource createSqlSource(Configuration configuration, String script, Class<?> parameterType);

    /**
     * 创建参数处理器
     * @param mappedStatement
     * @param parameterObject
     * @param boundSql
     * @return
     */
    ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql);
}
