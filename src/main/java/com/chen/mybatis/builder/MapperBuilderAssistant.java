package com.chen.mybatis.builder;

import com.chen.mybatis.executor.keygen.KeyGenerator;
import com.chen.mybatis.mapping.*;
import com.chen.mybatis.reflection.MetaClass;
import com.chen.mybatis.scripting.LanguageDriver;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.TypeHandler;

import java.util.ArrayList;
import java.util.List;

public class MapperBuilderAssistant extends BaseBuilder {


    private String currentNamespace;

    private String resource;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }


    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }


    public ResultMapping buildResultMapping(Class<?> resultType, String property, String column, List<ResultFlag> flags) {
        Class<?> javaTypeClass = resolveResultJavaType(resultType, property, null);
        TypeHandler<?> typeHandlerInstance = resolveTypeHandler(javaTypeClass, null);
        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, property, column, javaTypeClass);
        builder.typeHandler(typeHandlerInstance);
        builder.flags(flags);
        return builder.build();
    }

    private Class<?> resolveResultJavaType(Class<?> resultType, String property, Class<?> javaType) {
        if (javaType == null && property != null) {
            try {
                MetaClass metaResultType = MetaClass.forClass(resultType);
                javaType = metaResultType.getSetterType(property);
            } catch (Exception ignore) {

            }
        }
        if (javaType == null) {
            javaType = Object.class;
        }
        return javaType;
    }

    public String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference) {
            if (base.contains(".")) {
                return base;
            }
        } else {
            if (base.startsWith(currentNamespace + ".")) {
                return base;
            }
            if (base.contains(".")) {
                throw new RuntimeException("Dots are not allowed in element names, please remove it from " + base);
            }
        }
        return currentNamespace + "." + base;
    }

    /**
     * 添加映射语句
     * @param id
     * @param sqlSource
     * @param sqlCommandType
     * @param parameterType
     * @param resultMap
     * @param resultType
     * @param keyGenerator
     * @param keyProperty
     * @param lang
     * @return
     */
    public MappedStatement addMappedStatement(String id,
                                              SqlSource sqlSource,
                                              SqlCommandType sqlCommandType,
                                              Class<?> parameterType,
                                              String resultMap,
                                              Class<?> resultType,
                                              KeyGenerator keyGenerator,
                                              String keyProperty,
                                              LanguageDriver lang) {

        // 给id 加上namespace前缀，
        id = applyCurrentNamespace(id, false);
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlCommandType, sqlSource, resultType);

        statementBuilder.resource(resource);
        statementBuilder.keyGenerator(keyGenerator);
        statementBuilder.keyProperty(keyProperty);

        // 结果 映射，给MappedStatement#resultMaps
        setStatementResult(resultMap, resultType, statementBuilder);


        MappedStatement statement = statementBuilder.build();
        // 映射语句信息，建造完成存放到配置项中
        configuration.addMappedStatement(statement);

        return statement;

    }

    private void setStatementResult(String resultMap, Class<?> resultType, MappedStatement.Builder statementBuilder) {
        // 因为暂时还没有在Mapper XML 中配置 Map 返回结果， 所以这里返回的是 null
        resultMap = applyCurrentNamespace(resultMap, true);

        List<ResultMap> resultMaps = new ArrayList<>();

        if (resultMap != null) {
            String[] resultMapNames = resultMap.split(",");
            for (String resultMapName : resultMapNames) {
                resultMaps.add(configuration.getResultMap(resultMapName.trim()));
            }
        } else if (resultType != null) {
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration, statementBuilder.id() + "-Inline",
                    resultType, new ArrayList<>());

            resultMaps.add(inlineResultMapBuilder.build());
        }

        statementBuilder.resultMaps(resultMaps);

    }


    public ResultMap addResultMap(String id, Class<?> type, List<ResultMapping> resultMappings) {
        // fix 补全ID 全路径 如：com.chen.mybatis.test.dao.IActivityDao + activityMap
        id = applyCurrentNamespace(id, false);

        ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration,
                id,
                type,
                resultMappings);

        ResultMap resultMap = inlineResultMapBuilder.build();
        configuration.addResultMap(resultMap);
        return resultMap;

    }


}
