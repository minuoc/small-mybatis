package com.chen.mybatis.reflection.wrapper;

import com.chen.mybatis.reflection.MetaObject;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/7
 */
public class DefaultObjectWrapperFactory implements ObjectWrapperFactory{

    @Override
    public boolean hasWrapperFor(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper.");
    }
}
