package com.chen.mybatis.reflection.invoker;

import java.lang.reflect.Field;

public class SetFieldInvoker implements Invoker{
    private Field field;

    @Override
    public Object invoke(Object target, Object[] args) throws Exception {
        field.set(target,args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return null;
    }
}
