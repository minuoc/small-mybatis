package com.chen.mybatis.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC类型枚举
 */
public enum JdbcType {

    INTEGER(Types.INTEGER),
    FLOAT(Types.FLOAT),
    DOUBLE(Types.DOUBLE),
    DECIMAL(Types.DECIMAL),
    VARCHAR(Types.VARCHAR),
    TIMESTAMP(Types.TIMESTAMP);

    private final int TYPE_CODE;
    private static Map<Integer,JdbcType> codeLookup = new HashMap<>();

    static {
        for(JdbcType type : JdbcType.values()) {
            codeLookup.put(type.TYPE_CODE,type);
        }
    }

    JdbcType(int type_code) {
        TYPE_CODE = type_code;
    }

    public static JdbcType forCode(int code){
        return codeLookup.get(code);
    }
}
