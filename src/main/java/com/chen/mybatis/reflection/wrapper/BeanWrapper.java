package com.chen.mybatis.reflection.wrapper;

import com.chen.mybatis.reflection.MetaClass;
import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.reflection.factory.ObjectFactory;
import com.chen.mybatis.reflection.invoker.Invoker;
import com.chen.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/7
 */
public class BeanWrapper extends BaseWrapper{

    /**
     * 原来的对象
     */
    private Object object;

    /**
     * 元类
     */
    private MetaClass metaClass;

    public BeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject);
        this.object= object;
        this.metaClass = MetaClass.forClass(object.getClass());
    }

    @Override
    public Object get(PropertyTokenizer prop) {
        //如果 index(有中括号) 说明是集合，那就要解析集合,调用的是 BaseWrapper.resolveCollection 和 getCollectionValue
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop,object);
            return getCollectionValue(prop,collection);
        }else {
            return getBeanProperty(prop,object);
        }
    }



    @Override
    public void set(PropertyTokenizer prop, Object value) {

    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return null;
    }

    @Override
    public String[] getGetterNames() {
        return new String[0];
    }

    @Override
    public String[] getSetterNames() {
        return new String[0];
    }

    @Override
    public Class<?> getSetterType(String name) {
        return null;
    }

    @Override
    public Class<?> getGetterType(String name) {
        return null;
    }

    @Override
    public boolean hasSetter(String name) {
        return false;
    }

    @Override
    public boolean hasGetter(String name) {
        return false;
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        return null;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public void add(Object element) {

    }

    @Override
    public <E> void addAll(List<E> element) {

    }

    private Object getBeanProperty(PropertyTokenizer prop, Object object) {
        try {
            // 得到getter方法,然后调用
            Invoker method = metaClass.getGetInvoker(prop.getName());
            return method.invoke(object,NO_ARGUMENTS);
        } catch (RuntimeException e) {
            throw e;
        }catch (Throwable e) {
            throw new RuntimeException("Could not get property '" + prop.getName() + "' from " + object.getClass() + ". Cause:" + e.toString(),e);
        }
    }

    private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
        try {
            // 得到setter方法,然后调用
            Invoker method = metaClass.getSetInvoker(prop.getName());
            Object[] param = {value};
            method.invoke(object,new Object[]{value});
        } catch (Throwable e) {
            throw new RuntimeException("Could not set property '" + prop.getName() + "' from " + object.getClass() + ". Cause:" + e.toString(),e);
        }
    }
}
