package com.chen.mybatis.datasource.unpooled;

import com.chen.mybatis.datasource.DataSourceFactory;
import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.reflection.SystemMetaObject;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/26
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {
    
    
    protected DataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        MetaObject metaObject = SystemMetaObject.forObject(dataSource);
        for (Object key : properties.keySet()) {
            String propertyName = (String)key;
            if (metaObject.hasSetter(propertyName)){
                String value = (String) properties.getProperty(propertyName);
                Object convertValue = convertValue(metaObject,propertyName,value);
                metaObject.setValue(propertyName,convertValue);
            }
        }
    }



    @Override
    public DataSource getDataSource() {
        return dataSource;
    }



    private Object convertValue(MetaObject metaObject, String propertyName, String value) {
        Object convertedValue = value;
        Class<?> targetType = metaObject.getSetterType(propertyName);
        if (targetType == Integer.class || targetType == int.class) {
            convertedValue = Integer.valueOf(value);
        }else if (targetType == Long.class || targetType == long.class) {
            convertedValue = Long.valueOf(value);
        }else if (targetType == Boolean.class || targetType == boolean.class){
            convertedValue = Boolean.valueOf(value);
        }
        return convertedValue;
    }
}
