package com.chen.mybatis.session;

public interface ResultContext {


    /**
     * 获取结果
     * @return
     */
    Object getResultObject();

    /**
     * 获取记录数
     * @return
     */
    int getResultCount();

    void nextResultObject(Object rowValue);
}
