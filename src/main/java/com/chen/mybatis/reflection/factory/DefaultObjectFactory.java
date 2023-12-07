package com.chen.mybatis.reflection.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
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
        return create(type, null, null);
    }


    @Override
    public <T> T create(Class<?> type, List<Class<T>> constructArgTypes, List<Object> constructArgs) {
        //1.解析接口
        Class<?> classToCreate = resolveInterface(type);
        //2.类实例化
        return (T) instantiateClass(classToCreate, constructArgTypes, constructArgs);
    }

    private <T> Object instantiateClass(Class<?> type, List<Class<T>> constructArgTypes, List<Object> constructArgs) {
        try {
            Constructor<?> constructor;
            if (constructArgTypes == null || constructArgs == null) {
                constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            }
            constructor = type.getDeclaredConstructor(constructArgTypes.toArray(new Class[constructArgTypes.size()]));
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(constructArgs.toArray(new Object[constructArgs.size()]));
        } catch (Exception e) {
            // 如果出错，包装一下，重新抛出自己的异常
            StringBuilder argTypes = new StringBuilder();
            if (constructArgTypes != null) {
                for (Class<?> argType : constructArgTypes) {
                    argTypes.append(argType.getSimpleName()).append(",");
                }
            }
            StringBuilder argValues = new StringBuilder();
            if (constructArgs != null) {
                for (Object argValue : constructArgs) {
                    argValues.append(argValue.getClass().getSimpleName()).append(",");
                }
            }
            throw new RuntimeException("Could not instantiate class " + type.getName() + " with invalid types (" + argTypes + ") or values ( " + argValues + "). Cause:" + e, e);
        }
    }


    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }

    /**
     * 解析接口，将 interface 转换为实际的class 类
     * @param type
     * @return
     */
    private Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            classToCreate = Arrays.class;
        } else if (type == Map.class) {
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
