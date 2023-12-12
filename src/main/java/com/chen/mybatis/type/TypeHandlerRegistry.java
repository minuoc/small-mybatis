package com.chen.mybatis.type;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型处理注册机
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/12
 */
public final class TypeHandlerRegistry {
    private final Map<JdbcType, TypeHandler<?>> JDBC_TYPE_HANDLER_MAP = new EnumMap<>(JdbcType.class);
    private final Map<Type,Map<JdbcType, TypeHandler<?>>> TYPE_HANDLER_MAP = new HashMap<>();
    private Map<Class<?>,TypeHandler<?>> ALL_TYPE_HANDLER_MAP = new HashMap<>();

    public TypeHandlerRegistry() {
    }

    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> typeHandler) {
        if(null != javaType) {
            Map<JdbcType,TypeHandler<?>> map = TYPE_HANDLER_MAP.computeIfAbsent(javaType,k->new HashMap<>());
            map.put(jdbcType,typeHandler);
        }
        ALL_TYPE_HANDLER_MAP.put(typeHandler.getClass(),typeHandler);
    }
}
