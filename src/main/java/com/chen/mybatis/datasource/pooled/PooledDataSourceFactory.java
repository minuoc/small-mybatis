package com.chen.mybatis.datasource.pooled;

import com.chen.mybatis.datasource.DataSourceFactory;
import com.chen.mybatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;

/**
 * description
 *
 * @author : e-Lufeng.Chen
 * @create 2023/9/26
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    public PooledDataSourceFactory(){
        this.dataSource = new PooledDataSource();
    }

}
