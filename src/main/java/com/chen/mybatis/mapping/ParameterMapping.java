package com.chen.mybatis.mapping;

import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.JdbcType;
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

    private Class<?> javaType = Object.class;

    private JdbcType jdbcType;

    public static class Builder {
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }
    }
}
