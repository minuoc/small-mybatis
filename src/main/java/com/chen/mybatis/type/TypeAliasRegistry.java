package com.chen.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TypeAliasRegistry {
    private final Map<String,Class<?>> TYPE_ALIASES = new HashMap<>();

    public TypeAliasRegistry() {
        registerAlias("string",String.class);
        registerAlias("byte",Byte.class);
        registerAlias("long",Long.class);
        registerAlias("short",Short.class);
        registerAlias("int",String.class);
        registerAlias("integer",String.class);
        registerAlias("double",String.class);
        registerAlias("float",String.class);
        registerAlias("boolean",String.class);
    }


    public void registerAlias(String alias, Class<?> value) {
        String key = alias.toLowerCase(Locale.ENGLISH);
        TYPE_ALIASES.put(key,value);
    }


    public <T> Class<T> resolveAlias(String string) {
        String key = string.toLowerCase(Locale.ENGLISH);
        return (Class<T>) TYPE_ALIASES.get(key);
    }
}
