package com.chen.mybatis.scripting.xmltags;

import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.scripting.LanguageDriver;
import com.chen.mybatis.session.Configuration;

import javax.lang.model.element.Element;

public class XMLLanguageDriver implements LanguageDriver {
    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        XMLScriptBuilder builder
        return null;
    }
}
