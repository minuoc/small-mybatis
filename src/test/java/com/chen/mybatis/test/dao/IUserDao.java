package com.chen.mybatis.test.dao;

import com.chen.mybatis.test.po.User;

public interface IUserDao {



    User queryUserInfoById(long id);


    User queryUserInfo(User req);
}
