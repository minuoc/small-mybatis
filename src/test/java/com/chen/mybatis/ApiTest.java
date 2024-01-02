package com.chen.mybatis;

import com.alibaba.fastjson.JSON;
import com.chen.mybatis.io.Resources;
import com.chen.mybatis.session.SqlSession;
import com.chen.mybatis.session.SqlSessionFactory;
import com.chen.mybatis.session.SqlSessionFactoryBuilder;
import com.chen.mybatis.test.dao.IUserDao;
import com.chen.mybatis.test.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Slf4j
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    private SqlSession sqlSession;



    @Before
    public void init() throws IOException {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));
        sqlSession = sqlSessionFactory.openSession();


    }

    @Test
    public void test_queryUserInfoById() {
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        // 2. 测试验证：基本参数
        User user = userDao.queryUserInfoById(1L);
        logger.info("测试结果:{}",JSON.toJSONString(user));

    }

    @Test
    public void test_queryUserInfo(){
        // 1. 获取映射器对象
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        // 2. 测试验证：基本参数
        User user = userDao.queryUserInfo(new User());
        logger.info("测试结果:{}",JSON.toJSONString(user));
    }

}
