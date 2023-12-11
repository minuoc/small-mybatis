package com.chen.mybatis.parsing;

/**
 * 记号处理器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/11
 */
public interface TokenHandler {
    String handleToken(String content);
}
