package com.chen.mybatis.type;

import com.chen.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/21
 */
public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    protected Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        // 定义抽象方法，由子类实现不同类型的属性设置
        setNonNullParameter(ps, i, parameter, jdbcType);
    }

    public T getResult(ResultSet rs, String columnName) throws SQLException {
        return getNullableResult(rs,columnName);
    }

    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getNullableResult(rs,columnIndex);
    }



    protected abstract void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;


    protected abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;


    protected abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

}
