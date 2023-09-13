package com.chen.mybatis.mapping;


import lombok.Getter;

import java.util.Map;

/**
 * 绑定的SQL, 是从SqlSource而来，将动态内容都处理完成得到的SQL语句字符串，其中包括?,还有绑定的参数
 */
@Getter
public class BoundSql {

    private String sql;
    private Map<Integer,String> parameterMapping;
    private String parameterType;
    private String resultType;

    public BoundSql(String sql, Map<Integer, String> parameterMapping, String parameterType, String resultType) {
        this.sql = sql;
        this.parameterMapping = parameterMapping;
        this.parameterType = parameterType;
        this.resultType = resultType;
    }


}
