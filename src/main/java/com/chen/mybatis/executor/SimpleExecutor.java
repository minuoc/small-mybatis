package com.chen.mybatis.executor;

import com.chen.mybatis.executor.statement.StatementHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.ResultHandler;
import com.chen.mybatis.session.RowBounds;
import com.chen.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 简单执行器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public class SimpleExecutor extends BaseExecutor {

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        Statement stmt = null;
        try {
            Configuration configuration = ms.getConfiguration();
            // 新建一个StatementHandler
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
            // 准备语句
            stmt = prepareStatement(handler);

            return  handler.update(stmt);
        } finally {
            closeStatement(stmt);
        }

    }


    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds,ResultHandler resultHandler, BoundSql boundSql) {
        Statement stmt;
        try {
            Configuration configuration = ms.getConfiguration();
            StatementHandler handler = configuration.newStatementHandler(this, ms, parameter,rowBounds, resultHandler, boundSql);
            stmt = prepareStatement(handler);
            // 返回结果
            return handler.query(stmt, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }


    private Statement prepareStatement(StatementHandler handler) throws SQLException {
        Statement stmt;
        Connection connection = transaction.getConnection();
        // 准备语句
        stmt = handler.prepare(connection);
        handler.parameterize(stmt);
        return stmt;
    }



}
