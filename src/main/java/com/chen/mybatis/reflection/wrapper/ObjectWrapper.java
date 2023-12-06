package com.chen.mybatis.reflection.wrapper;

import com.chen.mybatis.reflection.property.PropertyTokenizer;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/6
 */
public interface ObjectWrapper {

    Object get(Object object);

    void set(PropertyTokenizer prop, Object value);

    String findProperty(String name, boolean useCamelCaseMapping);

    String[] getGetterNames();

    String[] getSetterType(String name);

    String[] getGetterType(String name);

    boolean hasSetter(String name);

    boolean hasGetter(String name);

    MetaObject instantiatePropertyValue()

}
