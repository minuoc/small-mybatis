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
        register(Long.class,new LongTypeHandler());
        register(long.class,new LongTypeHandler());
        register(String.class,new StringTypeHandler());
        register(String.class,JdbcType.CHAR,new StringTypeHandler());
        register(String.class,JdbcType.VARCHAR,new StringTypeHandler());
    }

    private <T> void register(Type javaType, TypeHandler<? extends T> typeHandler) {
        register(javaType,null,typeHandler);
    }

    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> typeHandler) {
        if(null != javaType) {
            Map<JdbcType,TypeHandler<?>> map = TYPE_HANDLER_MAP.computeIfAbsent(javaType,k->new HashMap<>());
            map.put(jdbcType,typeHandler);
        }
        ALL_TYPE_HANDLER_MAP.put(typeHandler.getClass(),typeHandler);
    }


    public <T> TypeHandler<T> getTypeHandler(Class<T> type, JdbcType jdbType) {
        return getTypeHandler((Type) type, jdbType);
    }

    public boolean hasTypeHandler(Class<?> javaType) {
        return hasTypeHandler(javaType, null);
    }

    public boolean hasTypeHandler(Class<?> javaType, JdbcType jdbcType) {
        return javaType != null && getTypeHandler(javaType, jdbcType)!= null;
    }


    private <T> TypeHandler<T> getTypeHandler(Type type, JdbcType jdbType) {
        Map<JdbcType,TypeHandler<?>> jdbcHandlerMap = TYPE_HANDLER_MAP.get(type);
        TypeHandler<?> handler = null;
        if (jdbcHandlerMap!= null) {
            handler = jdbcHandlerMap.get(jdbType);
            if (handler == null) {
                handler = jdbcHandlerMap.get(null);
            }
        }
        // type drives generics here;
        return (TypeHandler<T>) handler;
    }
}
