package com.chen.mybatis.executor.statement;

import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.executor.resultset.ResultSetHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public abstract class BaseStatementHandler implements StatementHandler{

    protected final Configuration configuration;
    protected final Executor executor;
    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;
    protected final ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    protected BaseStatementHandler( Executor executor, MappedStatement mappedStatement, Object parameterObject, ResultHandler resultHandler,BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;

        // 参数和 结果集
        this.parameterObject = parameterObject;
        this.resultSetHandler = configuration.newResultSetHandler(executor,mappedStatement,boundSql);
    }

    @Override
    public Statement prepare(Connection connection) {
        Statement statement = null;
        try {
            // 实例化 Statement
            statement = instantiateStatement(connection);
            // 参数设置, 可以被抽取 提供配置
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;

        } catch (Exception e) {
            throw new RuntimeException("Error preparing statement. Cause: " + e, e);
        }

    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;


}
