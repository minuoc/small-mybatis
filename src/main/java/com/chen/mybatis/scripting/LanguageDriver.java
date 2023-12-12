package com.chen.mybatis.scripting;

import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Element;


public interface LanguageDriver {

    SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType);

}
