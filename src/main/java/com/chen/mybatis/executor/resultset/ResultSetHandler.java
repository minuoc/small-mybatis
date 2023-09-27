package com.chen.mybatis.executor.resultset;

import java.sql.Statement;
import java.util.List;

/**
 * 结果集处理器
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public interface ResultSetHandler {

    <E> List<E> handleResultSets(Statement stmt);

}
