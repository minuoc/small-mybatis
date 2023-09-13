package com.chen.mybatis.session;

public interface SqlSessionFactory {

    /**
     * 打开一个session
     * @return
     */
    SqlSession openSession();



}
