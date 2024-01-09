package com.chen.mybatis;

import com.alibaba.fastjson.JSON;
import com.chen.mybatis.io.Resources;
import com.chen.mybatis.session.SqlSession;
import com.chen.mybatis.session.SqlSessionFactory;
import com.chen.mybatis.session.SqlSessionFactoryBuilder;
import com.chen.mybatis.test.dao.IActivityDao;
import com.chen.mybatis.test.dao.IUserDao;
import com.chen.mybatis.test.po.Activity;
import com.chen.mybatis.test.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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
    public void test_insert() {
        IActivityDao dao = sqlSession.getMapper(IActivityDao.class);
        Activity activity = new Activity();
        activity.setActivityId(10004L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("测试数据插入");
        activity.setCreator("xiaobai");

        Integer res = dao.insert(activity);
        sqlSession.commit();
        logger.info("测试结果: count:{} idx:{}",res,JSON.toJSONString(activity.getId()));

    }

}
