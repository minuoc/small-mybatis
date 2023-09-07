package com.chen.mybatis.transaction;

import com.chen.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

public interface TransactionFactory {

    /**
     * 根据Connection 创建 Transaction
     * @param conn
     * @return
     */
    Transaction newTransaction(Connection conn);


    /**
     * 根据数据源和事务隔离级别创建 Transaction
     * @param dataSource
     * @param level
     * @param autoCommit
     * @return
     */
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);

}
