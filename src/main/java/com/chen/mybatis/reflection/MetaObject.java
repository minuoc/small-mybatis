package com.chen.mybatis.reflection;

import com.chen.mybatis.reflection.factory.ObjectFactory;
import com.chen.mybatis.reflection.property.PropertyTokenizer;
import com.chen.mybatis.reflection.wrapper.*;

import java.util.Collection;
import java.util.Map;

/**
 * 元对象
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/6
 */
public class MetaObject {
    /**
     * 原对象
     */
    private Object originalObject;
    /**
     * 对象包装器
     */
    private ObjectWrapper objectWrapper;

    /**
     * 对象工厂
     */
    private ObjectFactory objectFactory;

    /**
     * 对象包装器工厂
     */
    private ObjectWrapperFactory objectWrapperFactory;


    private MetaObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        this.originalObject = object;
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;

        if (object instanceof ObjectWrapper) {
            //如果对象本身已经是ObjectWrapper类型，
            this.objectWrapper = (ObjectWrapper) object;
        } else if(objectWrapperFactory.hasWrapperFor(object)) {
            //如果有包装器，调用ObjectWrapperFactory.getWrapperFor
            this.objectWrapper = objectWrapperFactory.getWrapperFor(this,object);
        } else if (object instanceof Map) {
//            如果是Map型，返回MapWrapper
            this.objectWrapper = new MapWrapper(this, (Map) object);
        } else if (object instanceof Collection) {

            this.objectWrapper = new CollectionWrapper(this, (Collection) object);
        } else {
            this.objectWrapper = new BeanWrapper(this,object);
        }


    }


    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    /* --------以下方法都是委派给 ObjectWrapper------ */
    // 查找属性
    public String findProperty(String propName, boolean useCamelCaseMapping) {
        return objectWrapper.findProperty(propName, useCamelCaseMapping);
    }

    // 取得getter的名字列表
    public String[] getGetterNames() {
        return objectWrapper.getGetterNames();
    }

    // 取得setter的名字列表
    public String[] getSetterNames() {
        return objectWrapper.getSetterNames();
    }

    // 取得setter的类型列表
    public Class<?> getSetterType(String name) {
        return objectWrapper.getSetterType(name);
    }

    // 取得getter的类型列表
    public Class<?> getGetterType(String name) {
        return objectWrapper.getGetterType(name);
    }

    //是否有指定的setter
    public boolean hasSetter(String name) {
        return objectWrapper.hasSetter(name);
    }

    // 是否有指定的getter
    public boolean hasGetter(String name) {
        return objectWrapper.hasGetter(name);
    }


    /**
     * 取得值
     * @param name
     * @return
     */
    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObjectForProperty(prop.getName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                return null;
            } else {
                return metaValue.getValue(prop.getChildren());
            }
        } else {
            return objectWrapper.get(prop);
        }
    }

    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaObject metaValue = metaObjectForProperty(prop.getName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT) {
                if (value == null && prop.getChildren() != null) {
                    // 如果上层就是  null了，还得看有没有儿子，没有 那就结束
                    return;
                } else {
                    // 否则还得new 一个, 委派给 ObjectWrapper.instantiatePropertyValue
                    metaValue = objectWrapper.instantiatePropertyValue(name,prop,objectFactory);
                }
                // 递归调用setValue
                metaValue.setValue(prop.getChildren(), value);

            } else {
                //到了最后一层了，所以委派给 ObjectWrapper.set
                objectWrapper.set(prop,value);
            }
        }
    }


    /**
     * 为属性生成元对象
     * @param name
     * @return
     */
    public MetaObject metaObjectForProperty(String name) {
        //递归调用
        Object value = getValue(name);
        return MetaObject.forObject(value, objectFactory, objectWrapperFactory);
    }

    public static MetaObject forObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        if (object == null) {
            //处理一下 null,将null 包装起来
            return SystemMetaObject.NULL_META_OBJECT;
        } else {
            return new MetaObject(object,objectFactory,objectWrapperFactory);
        }
    }
}
