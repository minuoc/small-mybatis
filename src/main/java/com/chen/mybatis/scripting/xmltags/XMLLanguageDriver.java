package com.chen.mybatis.scripting.xmltags;

import com.chen.mybatis.executor.parameter.ParameterHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.scripting.LanguageDriver;
import com.chen.mybatis.scripting.defaults.DefaultParameterHandler;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Element;


public class XMLLanguageDriver implements LanguageDriver {
    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        XMLScriptBuilder builder = new XMLScriptBuilder(configuration,script,parameterType);
        return builder.parseScriptNode();
    }

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement,parameterObject,boundSql);
    }
}
