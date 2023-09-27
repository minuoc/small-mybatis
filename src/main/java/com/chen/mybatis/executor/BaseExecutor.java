package com.chen.mybatis.executor;

import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.ResultHandler;
import com.chen.mybatis.transaction.Transaction;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * 执行器 抽象基类
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public abstract class BaseExecutor implements Executor {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(BaseExecutor.class);

    protected Configuration configuration;
    protected Transaction transaction;
    protected Executor wrapper;

    private boolean closed;

    protected BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        if (closed) {
            throw new RuntimeException("Executor was closed");
        }
        return doQuery(ms,parameter,resultHandler,boundSql);
    }

    protected abstract <E> List<E> doQuery(MappedStatement ms,Object parameter,ResultHandler resultHandler,BoundSql boundSql);


    @Override
    public Transaction getTransaction() {
        if (closed){
            throw new RuntimeException("Executor was closed");
        }
        return transaction;
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed){
            throw new RuntimeException("Executor was closed");
        }
        if (required) {
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed){
            if (required) {
                transaction.rollback();
            }
        }

    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } finally {
                transaction.close();
            }
        } catch (SQLException e) {
            logger.warn("Unexpected exception on closing transaction. Cause: " + e);
        } finally {
            transaction = null;
            closed = true;
        }
    }
}
