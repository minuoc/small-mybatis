package com.chen.mybatis.reflection;

import com.chen.mybatis.reflection.factory.DefaultObjectFactory;
import com.chen.mybatis.reflection.factory.ObjectFactory;
import com.chen.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.chen.mybatis.reflection.wrapper.ObjectWrapperFactory;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/7
 */
public class SystemMetaObject {


    public static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    public static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    public static final MetaObject NULL_META_OBJECT = MetaObject.forObject(NullObject.class, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);

    private SystemMetaObject() {
        // Prevent Instantiation of Static Class
    }

    /**
     * 空对象
     */
    private static class NullObject {
    }

    public static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }
}
