package com.chen.mybatis.reflection.wrapper;

import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;
import java.util.Map;

/**
 * 对象包装器抽象基类
 */
public abstract class BaseWrapper implements ObjectWrapper{

    protected static final Object[] NO_ARGUMENTS = new Object[0];
    protected MetaObject metaObject;


    protected BaseWrapper(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    /**
     * 解析集合
     * @param prop
     * @param object
     * @return
     */
    protected Object resolveCollection(PropertyTokenizer prop, Object object) {
        if ("".equals(prop.getName())){
            return object;
        } else {
            return metaObject.getValue(prop.getName());
        }
    }

    /**
     * 取集合中的值
     * 中括号有2个意思，一个是Map，一个是List或数组
     * @param prop
     * @param collection
     * @return
     */
    protected Object getCollectionValue(PropertyTokenizer prop, Object collection) {
        if (collection instanceof Map) {
            //map['name']
            return ((Map) collection).get(prop.getIndex());
        } else {
            int i = Integer.parseInt(prop.getIndex());
            if (collection instanceof List) {
                // list[0]
                return ((List) collection).get(i);
            } else if (collection instanceof Object[]){
                return ((Object[]) collection)[i];
            } else if(collection instanceof char[]) {
                return ((char[]) collection)[i];
            } else if (collection instanceof boolean[]){
                return ((boolean[]) collection)[i];
            } else if (collection instanceof byte[]){
                return ((byte[]) collection)[i];
            } else if (collection instanceof short[]){
                return ((short[]) collection)[i];
            } else if (collection instanceof int[]){
                return ((int[]) collection)[i];
            } else if (collection instanceof long[]){
                return ((long[]) collection)[i];
            } else if (collection instanceof float[]){
                return ((float[]) collection)[i];
            } else if (collection instanceof double[]){
                return ((double[]) collection)[i];
            } else {
                throw new RuntimeException("The '" + prop.getName() + "' property of " + collection + " is not a List or Array.");
            }
        }
    }

    protected void setCollectionValue(PropertyTokenizer prop, Object collection, Object value) {
        if (collection instanceof Map) {
            //map['name']
            ((Map) collection).put(prop.getIndex(), value);
        } else {
            int i = Integer.parseInt(prop.getIndex());
            if (collection instanceof List) {
                // list[0]
                ((List) collection).set(i, value);
            } else if (collection instanceof Object[]){
                ((Object[]) collection)[i] = value;
            } else if(collection instanceof char[]) {
                ((char[]) collection)[i] = (Character) value;
            } else if (collection instanceof boolean[]){
                ((boolean[]) collection)[i] = (Boolean) value;
            } else if (collection instanceof byte[]){
                ((byte[]) collection)[i] = (Byte) value;
            } else if (collection instanceof short[]){
                ((short[]) collection)[i] = (Short) value;
            } else if (collection instanceof int[]){
                ((int[]) collection)[i] = (Integer) value;
            } else if (collection instanceof long[]){
                ((long[]) collection)[i] = (Long) value;
            } else if (collection instanceof float[]){
                ((float[]) collection)[i] = (Float) value;
            } else if (collection instanceof double[]){
                ((double[]) collection)[i] = (Double) value;
            } else {
                throw new RuntimeException("The '" + prop.getName() + "' property of " + collection + " is not a List or Array.");
            }
        }
    }
}
