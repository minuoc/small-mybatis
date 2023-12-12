package com.chen.mybatis.type;

import java.sql.PreparedStatement;

/**
 * 类型处理器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/12
 */
public interface TypeHandler<T> {

    void setParamter(PreparedStatement ps, int i , T parameter, JdbcType jdbcType);
}
