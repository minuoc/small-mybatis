package com.chen.mybatis.builder;

import com.chen.mybatis.mapping.ParameterMapping;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.parsing.GenericTokenParser;
import com.chen.mybatis.parsing.TokenHandler;
import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.session.Configuration;

import java.util.List;
import java.util.Map;

public class SqlSourceBuilder extends BaseBuilder {

    private static final String parameterProperties = "javaType,jdbcType,mode,numericScale,resultMap,typeHandler,jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, Map<String, Object> additionalParameters) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        // 返回静态SQL
        return new StaticSqlSource(configuration, sql, handler.getParameterMappings());

    }


    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {
        private List<ParameterMapping> parameterMappings;
        private Class<?> parameterType;
        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String,Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        /**
         * 构建 参数 映射
         *
         * @param content
         * @return
         */

        private ParameterMapping buildParameterMapping(String content) {
            // 先解析参数映射，就是转化成一个HashMap | #{favouriteSection,jdbtType = VARCHAR}
            Map<String, String> propertiesMap = new ParameterExpression(content);
            String property = propertiesMap.get("property");
            Class<?> propertyType = parameterType;
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
            return builder.build();
        }
    }


}
