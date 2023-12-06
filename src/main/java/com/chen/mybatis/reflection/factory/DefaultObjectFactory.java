package com.chen.mybatis.reflection.factory;

import java.io.Serializable;
import java.util.*;

/**
 * 默认对象工厂
 */
public class DefaultObjectFactory implements ObjectFactory, Serializable {

    @Override
    public void setProperties(Properties properties) {
        // 默认无属性可设置
    }

    @Override
    public <T> T create(Class<T> type) {

        return create(type,null,null);
    }

    @Override
    public <T> T create(Class<?> type,Class<T> constructArgTypes, List<Object> constructArgs) {
        //1.解析接口
        Class<?> classToCreate = resolveInterface(type);
        //2.类实例化
        return (T) instantiateClass(classToCreate,constructArgTypes,constructArgs);
    }

    private <T> Object instantiateClass(Class<?> classToCreate, Class<T> constructArgTypes, List<Object> constructArgs) {
        try {

        } catch (Exception e) {

        }
    }


    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }


    private Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            classToCreate = Arrays.class;
        } else if(type == Map.class) {
            classToCreate = HashMap.class;
        } else if (type == SortedMap.class) {
            classToCreate = TreeSet.class;
        } else if (type == Set.class) {
            classToCreate = HashSet.class;
        } else {
            classToCreate = type;
        }
        return classToCreate;
    }
}
