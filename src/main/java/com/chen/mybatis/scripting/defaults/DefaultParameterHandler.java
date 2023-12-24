package com.chen.mybatis.scripting.defaults;

import com.alibaba.fastjson.JSON;
import com.chen.mybatis.executor.parameter.ParameterHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.ParameterMapping;
import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.type.JdbcType;
import com.chen.mybatis.type.TypeHandler;
import com.chen.mybatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


/**
 * 默认参数处理器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/21
 */
public class DefaultParameterHandler implements ParameterHandler {

    private Logger logger = LoggerFactory.getLogger(DefaultParameterHandler.class);

    private final TypeHandlerRegistry typeHandlerRegistry;

    private final MappedStatement mappedStatement;
    private final Object parameterObject;
    private BoundSql boundSql;
    private Configuration configuration;

    public DefaultParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        this.mappedStatement = mappedStatement;
        this.configuration = mappedStatement.getConfiguration();
        this.typeHandlerRegistry = mappedStatement.getConfiguration().getTypeHandlerRegistry();
        this.parameterObject = parameterObject;
        this.boundSql = boundSql;
    }

    @Override
    public Object getParameterObject() {
        return parameterObject;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (null != parameterMappings) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                String propertyName = parameterMapping.getProperty();
                Object value;
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    value = parameterObject;
                } else {
                    MetaObject metaObject = configuration.newMetaObject(parameterObject);
                    value = metaObject.getValue(propertyName);
                }
                JdbcType jdbcType = parameterMapping.getJdbcType();
                //
                logger.info("根据每个ParameterMapping中的TypeHandler设置对应的参数信息 value:{}", JSON.toJSONString(value));

                // 设置参数
                TypeHandler typeHandler = parameterMapping.getTypeHandler();
                typeHandler.setParameter(ps, i + 1, value, jdbcType);
            }
        }
    }
}
