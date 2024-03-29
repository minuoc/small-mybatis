package com.chen.mybatis.executor.statement;

import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.executor.parameter.ParameterHandler;
import com.chen.mybatis.executor.resultset.ResultSetHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.ResultHandler;
import com.chen.mybatis.session.RowBounds;

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

    protected final ParameterHandler parameterHandler;

    protected final RowBounds rowBounds;
    protected BoundSql boundSql;

    protected BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        // 邢增判断， 因为update 不会传入boundSql 参数， 所以这里要做初始化处理
        if (boundSql == null) {
            boundSql = mappedStatement.getBoundSql(parameterObject);
        }
        this.boundSql = boundSql;

        // 参数和 结果集
        this.parameterObject = parameterObject;
        this.parameterHandler = configuration.newParameterHandler(mappedStatement,parameterObject,boundSql);
        this.resultSetHandler = configuration.newResultSetHandler(executor,mappedStatement,rowBounds,resultHandler, boundSql);
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
