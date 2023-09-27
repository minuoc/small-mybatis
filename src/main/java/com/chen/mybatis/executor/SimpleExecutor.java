package com.chen.mybatis.executor;

import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.session.ResultHandler;
import com.chen.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 简单执行器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/27
 */
public class SimpleExecutor extends BaseExecutor{

    protected SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        try {
            Configuration configuration = ms.getConfiguration();
            
            return null;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
