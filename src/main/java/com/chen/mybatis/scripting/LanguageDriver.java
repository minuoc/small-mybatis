package com.chen.mybatis.scripting;

import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.session.Configuration;

import javax.lang.model.element.Element;

public interface LanguageDriver {

    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);


}
