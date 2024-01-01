package com.chen.mybatis.builder;

import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.ResultMap;
import com.chen.mybatis.mapping.SqlCommandType;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.scripting.LanguageDriver;
import com.chen.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

public class MapperBuilderAssistant extends BaseBuilder{


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

    private String applyCurrentNamespace(String base, boolean isReference) {
        if (base == null) {
            return null;
        }
        if (isReference) {
            if (base.contains(".")) {
                return base;
            }
        }
        return  currentNamespace + "." + base;
    }

    public MappedStatement addMappedStatement(String id,
                                              SqlSource sqlSource,
                                              SqlCommandType sqlCommandType,
                                              Class<?> parameterType,
                                              String resultMap,
                                              Class<?> resultType,
                                              LanguageDriver lang) {

        // 给id 加上namespace前缀，
        id = applyCurrentNamespace(id,false);
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration,id,sqlCommandType,sqlSource,resultType);

        // 结果 映射，给MappedStatement#resultMaps
        setStatementResult(resultMap,resultType,statementBuilder);


        MappedStatement statement = statementBuilder.build();
        // 映射语句信息，建造完成存放到配置项中
        configuration.addMappedStatement(statement);

        return statement;

    }

    private void setStatementResult(String resultMap, Class<?> resultType, MappedStatement.Builder statementBuilder) {
        // 因为暂时还没有在Mapper XML 中配置 Map 返回结果， 所以这里返回的是 null
        resultMap = applyCurrentNamespace(resultMap,true);

        List<ResultMap> resultMaps = new ArrayList<>();

        if (resultMap != null){
            //TODO 暂无Map结果映射配置, 本章节 不添加此逻辑
        }

        else if (resultType != null) {
            ResultMap.Builder inlineResultMapBuilder = new ResultMap.Builder(configuration,statementBuilder.id() + "-Inline",
                    resultType,new ArrayList<>());

            resultMaps.add(inlineResultMapBuilder.build());
        }

        statementBuilder.resultMaps(resultMaps);

    }


}
