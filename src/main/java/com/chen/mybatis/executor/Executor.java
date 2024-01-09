package com.chen.mybatis.executor;

import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.ResultHandler;
import com.chen.mybatis.session.RowBounds;
import com.chen.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 执行器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    int update(MappedStatement ms, Object parameter) throws SQLException;

    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException;
    <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException;

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);



}
