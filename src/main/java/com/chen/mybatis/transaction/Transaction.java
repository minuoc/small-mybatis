package com.chen.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务接口
 */
public interface Transaction {
    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    Connection getConnection() throws SQLException;


    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

}
