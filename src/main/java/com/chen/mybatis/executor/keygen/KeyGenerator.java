package com.chen.mybatis.executor.keygen;

import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.mapping.MappedStatement;

import java.sql.Statement;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2024/1/9
 */
public interface KeyGenerator {

    void processBefore(Executor executor, MappedStatement mappedStatement, Statement stmt, Object parameter);


    void processAfter(Executor executor, MappedStatement mappedStatement,Statement statement, Object parameter);
}
