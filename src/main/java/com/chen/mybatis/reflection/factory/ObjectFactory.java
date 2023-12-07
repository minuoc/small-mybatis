package com.chen.mybatis.reflection.factory;

import java.util.List;
import java.util.Properties;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/6
 */
public interface ObjectFactory {

    void setProperties(Properties properties);

    <T> T create(Class<T> type);

    <T> T create(Class<?> type,List<Class<T>> constructArgTypes, List<Object> constructArgs);

    <T> boolean isCollection(Class<T> type);
}
