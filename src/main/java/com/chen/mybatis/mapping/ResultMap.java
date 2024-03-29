package com.chen.mybatis.mapping;

import com.chen.mybatis.session.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ResultMap {
    private String id;

    private Class<?> type;

    private List<ResultMapping> resultMappings;


    private Set<String> mappedColumns;


    public ResultMap() {
    }

    public static class Builder {
        private ResultMap resultMap = new ResultMap();

        public Builder(Configuration configuration, String id, Class<?> type, List<ResultMapping> resultMappings) {
            resultMap.id = id;
            resultMap.type = type;
            resultMap.resultMappings = resultMappings;
        }

        public ResultMap build(){
            resultMap.mappedColumns = new HashSet<>();
            // step-14 新增加 添加mappedColumns字段
            for (ResultMapping resultMapping : resultMap.resultMappings) {
                final String column = resultMapping.getColumn();
                if (column != null) {
                    resultMap.mappedColumns.add(column.toUpperCase(Locale.ENGLISH));
                }
            }

            return resultMap;
        }
    }


    public String getId() {
        return id;
    }

    public Class<?> getType() {
        return type;
    }

    public List<ResultMapping> getResultMappings() {
        return resultMappings;
    }

    public Set<String> getMappedColumns() {
        return mappedColumns;
    }


    public List<ResultMapping> getPropertyResultMappings(){
        return resultMappings;
    }
}
