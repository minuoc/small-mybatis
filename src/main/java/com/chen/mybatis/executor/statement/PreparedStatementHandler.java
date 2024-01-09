package com.chen.mybatis.executor.statement;

import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.executor.keygen.KeyGenerator;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.ResultHandler;
import com.chen.mybatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public class PreparedStatementHandler extends BaseStatementHandler {

    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, rowBounds,resultHandler, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        parameterHandler.setParameters((PreparedStatement) statement);
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.<E>handleResultSets(ps);
    }

    @Override
    public int update(Statement statement) throws SQLException {
        // 执行 insert/delete/update 语句
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        int rows = ps.getUpdateCount();

        // 执行 selectKey 语句
        Object parameterObject = boundSql.getParameterObject();
        KeyGenerator keyGenerator = mappedStatement.getKeyGenerator();
        keyGenerator.processAfter(executor,mappedStatement,ps,parameterObject);
        return rows;
    }


}
