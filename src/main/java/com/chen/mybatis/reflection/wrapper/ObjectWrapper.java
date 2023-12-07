package com.chen.mybatis.reflection.wrapper;

import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.reflection.factory.ObjectFactory;
import com.chen.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/6
 */
public interface ObjectWrapper {

    Object get(PropertyTokenizer prop);

    void set(PropertyTokenizer prop, Object value);

    String findProperty(String name, boolean useCamelCaseMapping);

    String[] getGetterNames();

    String[] getSetterNames();

    Class<?> getSetterType(String name);

    Class<?> getGetterType(String name);

    boolean hasSetter(String name);

    boolean hasGetter(String name);

    /**
     * 实例化属性
     * @param name
     * @param prop
     * @param objectFactory
     * @return
     */
    MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory);


    boolean isCollection();

    void add(Object element);

    <E> void addAll(List<E> element);

}
