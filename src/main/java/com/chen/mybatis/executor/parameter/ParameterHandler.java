package com.chen.mybatis.executor.parameter;

import java.sql.SQLException;

/**
 * 参数处理器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/21
 */
public interface ParameterHandler {

    /**
     * 获取参数
     * @return
     */
    Object getParameterObject();

    /**
     * 设置参数
     * @param parameterObject
     * @throws SQLException
     */
    void setParameters(Object parameterObject) throws SQLException;
}
