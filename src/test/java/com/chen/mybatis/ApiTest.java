package com.chen.mybatis;

import com.alibaba.fastjson.JSON;
import com.chen.mybatis.io.Resources;
import com.chen.mybatis.session.SqlSession;
import com.chen.mybatis.session.SqlSessionFactory;
import com.chen.mybatis.session.SqlSessionFactoryBuilder;
import com.chen.mybatis.test.dao.IUserDao;
import com.chen.mybatis.test.po.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Slf4j
public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);
//    @Test
//    public void test_MapperProxyFactory() {
//        MapperProxyFactory<IUserDao> factory = new MapperProxyFactory<>(IUserDao.class);
//        Map<String,String> sqlSession = new HashMap<>();
//        sqlSession.put("com.chen.mybatis.dao.IUserDao.queryUserName","模拟执行 Mapper.xml 中SQL 语句的操作：查询用户姓名");
//        sqlSession.put("com.chen.mybatis.dao.IUserDao.queryUserAge","模拟执行 Mapper.xml 中 SQL 语句的操作: 查询用户年龄");
//        IUserDao userDao = factory.newInstance(sqlSession);
//        String res = userDao.queryUserName("10001");
//        log.info("测试结果:{}",res);
//
//    }

//    @Test
//    public void test_proxy_class(){
//        IUserDao userDao = (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
//                new Class[]{IUserDao.class},(proxy,method,args) -> "你的操作被代理了！");
//        String result = userDao.queryUserName("10001");
//        log.info("测试结果: " + result);
//    }

//    @Test
//    public void test_MapperProxyFactory(){
//        MapperRegistry registry =new MapperRegistry();
//        registry.addMappers("com.chen.mybatis.test.dao");
//        // 从SqlSession 工厂 获取session
////        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(registry);
//        SqlSession sqlSession = sqlSessionFactory.openSession();
//        // 获取映射器对象
//        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
//
//        // 测试验证
//        String res = userDao.queryUserName("10001");
//        log.info("测试结果:{}",res);
//    }

    @Test
    public void test_SqlSessionFactory() throws IOException {
        //
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(Resources.getResourceAsReader("mybatis-config-datasource.xml"));

        SqlSession sqlSession = sqlSessionFactory.openSession();

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        for (int i = 0; i < 50; i++) {
            User user = userDao.queryUserInfoById(1L);
            logger.info("测试结果:{}" ,JSON.toJSONString(user));
        }

    }
}
