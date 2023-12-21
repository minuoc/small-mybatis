package com.chen.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/21
 */
public class StringTypeHandler extends BaseTypeHandler<String>{
    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter);
    }
}
