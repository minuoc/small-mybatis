package com.chen.mybatis.test.dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTest {
    public static void main(String[] args) {
        String regEx = "(#\\{(.*?)})";
        Pattern pattern = Pattern.compile("(#\\{(.*?)})");
        // 匹配 #{}
        Matcher matcher  = pattern.matcher(" SELECT id, userId, userHead, createTime\n" +
                "        FROM user\n" +
                "        where id = #{id}");
        String sql = " SELECT id, userId, userHead, createTime\n" +
                "        FROM user\n" +
                "        where id = #{id}";
        for (int i = 1; matcher.find(); i++){
            String g1 = matcher.group(1);
            String g2 = matcher.group(2);

            sql = sql.replace(g1,"?");
        }
        System.out.println(sql);
    }
}
