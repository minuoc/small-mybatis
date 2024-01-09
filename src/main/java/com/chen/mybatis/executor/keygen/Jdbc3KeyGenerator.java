package com.chen.mybatis.executor.keygen;

import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.TypeHandler;
import com.chen.mybatis.type.TypeHandlerRegistry;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 使用 JDBC3 Statement.getGeneratedKeys
 */
public class Jdbc3KeyGenerator implements KeyGenerator{
    @Override
    public void processBefore(Executor executor, MappedStatement mappedStatement, Statement stmt, Object parameter) {

    }

    @Override
    public void processAfter(Executor executor, MappedStatement mappedStatement, Statement statement, Object parameter) {

    }

    public void processBatch(MappedStatement ms, Statement stmt, List<Object> parameters) {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            final Configuration configuration = ms.getConfiguration();
            final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            final String[] keyProperties = ms.getKeyProperties();
            final ResultSetMetaData rsmd = rs.getMetaData();
            TypeHandler<?>[] typeHandlers = null;
            if (keyProperties != null && rsmd.getColumnCount() >= keyProperties.length) {
                for (Object parameter : parameters) {
                    if (!rs.next()) {
                        break;
                    }
                    final MetaObject metaParam = configuration.newMetaObject(parameter);
                    if (typeHandlers == null) {
                        //先取得类型处理器
                        typeHandlers = getTypeHandlers(typeHandlerRegistry,metaParam,keyProperties);
                    }
                    //填充
                    populateKeys(rs,metaParam,keyProperties,typeHandlers);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    private TypeHandler<?>[] getTypeHandlers(TypeHandlerRegistry typeHandlerRegistry, MetaObject metaParam, String[] keyProperties) {
        TypeHandler<?>[] typeHandlers = new TypeHandler[keyProperties.length];
        for (int i = 0; i < keyProperties.length; i++) {
            if (metaParam.hasSetter(keyProperties[i])) {
                Class<?> keyPropertyType = metaParam.getSetterType(keyProperties[i]);
                TypeHandler<?> typeHandler = typeHandlerRegistry.getTypeHandler(keyPropertyType,null);
                typeHandlers[i] = typeHandler;
            }
        }
        return typeHandlers;
    }



    private void populateKeys(ResultSet rs, MetaObject metaParam, String[] keyProperties, TypeHandler<?>[] typeHandlers) throws SQLException {
        for (int i = 0; i < keyProperties.length; i++) {
            TypeHandler<?> typeHandler = typeHandlers[i];
            if (typeHandler != null) {
                Object value = typeHandler.getResult(rs, i+1);
                metaParam.setValue(keyProperties[i],value);
            }

        }
    }
}
