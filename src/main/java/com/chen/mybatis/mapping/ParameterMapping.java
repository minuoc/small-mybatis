package com.chen.mybatis.mapping;

import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.JdbcType;
import com.chen.mybatis.type.TypeHandler;
import com.chen.mybatis.type.TypeHandlerRegistry;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 参数映射 #{property,javaType=int,jdbcType=NUMERIC}
 *
 */
@NoArgsConstructor
@Getter
public class ParameterMapping {

    private Configuration configuration;

    private String property;
    // javaType = int
    private Class<?> javaType = Object.class;
    // jdbcType = NUMERIC
    private JdbcType jdbcType;

    private TypeHandler<?> typeHandler;

    public static class Builder {
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property, Class<?> javaType) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
            parameterMapping.javaType = javaType;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

        public ParameterMapping build(){
            if (parameterMapping.typeHandler == null && parameterMapping.jdbcType!= null) {
                Configuration configuration = parameterMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                parameterMapping.typeHandler = typeHandlerRegistry.getTypeHandler(parameterMapping.javaType, parameterMapping.jdbcType);
            }
            return parameterMapping;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

}
