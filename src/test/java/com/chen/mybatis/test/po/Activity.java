package com.chen.mybatis.test.po;

import lombok.Data;

import java.util.Date;

@Data
public class Activity {

    private Long id;

    private Long activityId;

    private String activityName;

    private String activityDesc;

    private String creator;

    private Date createTime;

    private Date updateTime;
}
