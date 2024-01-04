package com.chen.mybatis.executor.statement;

import com.chen.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 语句处理器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public interface StatementHandler {

    /**
     * 准备语句
     * @param connection
     * @return
     */
    Statement  prepare(Connection connection) ;

    /**
     * 参数化
     * @param statement
     */
    void parameterize(Statement statement) throws SQLException;

    /**
     * 执行查询
     * @param statement
     * @param resultHandler
     * @param <E>
     * @return
     */
    <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

    /**
     * 执行更新
     * @param stmt
     * @return
     */
    int update(Statement stmt) throws SQLException;
}
