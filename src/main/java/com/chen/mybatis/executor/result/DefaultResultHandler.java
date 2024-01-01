package com.chen.mybatis.executor.result;

import com.chen.mybatis.reflection.factory.ObjectFactory;
import com.chen.mybatis.session.ResultContext;
import com.chen.mybatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultResultHandler implements ResultHandler {


    private final List<Object> list;


    public DefaultResultHandler(){
        this.list = new ArrayList<>();
    }

    /**
     * 通过 ObjectFactory 反射工具类，产生特定的list
     * @param objectFactory
     */
    public DefaultResultHandler(ObjectFactory objectFactory) {
        this.list = objectFactory.create(List.class);
    }


    @Override
    public void handleResult(ResultContext resultContext) {
        list.add(resultContext.getResultObject());
    }

    public List<Object> getResultList(){
        return list;
    }
}
