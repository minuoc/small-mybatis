package com.chen.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 类型处理器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/12
 */
public interface TypeHandler<T> {

    /**
     * 设置参数
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     */
    void setParameter(PreparedStatement ps, int i , T parameter, JdbcType jdbcType) throws SQLException;

    /**
     * 获得结果
     * @param resultSet
     * @param columnName
     * @return
     */
    T getResult(ResultSet resultSet, String columnName) throws SQLException;
}
