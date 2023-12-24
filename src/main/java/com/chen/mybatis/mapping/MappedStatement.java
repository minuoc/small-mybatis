package com.chen.mybatis.mapping;

import com.chen.mybatis.executor.parameter.ParameterHandler;
import com.chen.mybatis.scripting.LanguageDriver;
import com.chen.mybatis.session.Configuration;

import java.util.Map;

/**
 * 映射语句类
 */
public class MappedStatement {

    private Configuration configuration;

    private String id;

    private SqlCommandType sqlCommandType;

    private SqlSource sqlSource;

    Class<?> resultType;

    private LanguageDriver lang;

    public MappedStatement() {
    }




    public static class Builder {
        private MappedStatement mappedStatement = new MappedStatement();
        public Builder(Configuration configuration,String id,SqlCommandType sqlCommandType,SqlSource sqlSource,Class<?> resultType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.resultType = resultType;
            mappedStatement.lang = configuration.getDefaultScriptingLanguageInstance();
        }
        public MappedStatement build(){
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }


    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public SqlSource getSqlSource(){
        return sqlSource;
    }

    public Class<?> getResultType() {
        return resultType;
    }


    public LanguageDriver getLang() {
        return lang;
    }
}
