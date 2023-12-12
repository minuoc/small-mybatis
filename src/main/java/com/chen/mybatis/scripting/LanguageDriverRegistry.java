package com.chen.mybatis.scripting;

import java.util.HashMap;
import java.util.Map;

/**
 * 脚本语言注册器
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/11
 */
public class LanguageDriverRegistry {

    //map
    private final Map<Class<?>, LanguageDriver> LANGUAGE_DRIVER_MAP = new HashMap<>();

    private Class<?> defaultDriverClass = null;


    public void register(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("null is not a valid language Driver");
        }
        if (!LanguageDriver.class.isAssignableFrom(cls)) {
            throw new RuntimeException(cls.getName() + " does not implements " + LanguageDriver.class.getName());
        }
        // 如果没注册过，再去注册
        LanguageDriver driver = LANGUAGE_DRIVER_MAP.get(cls);
        if (driver == null) {
            try {
                // 单例模式
                driver = (LanguageDriver) cls.newInstance();
                LANGUAGE_DRIVER_MAP.put(cls, driver);
            } catch (Exception e) {
                throw new RuntimeException("Fail to load language driver for " + cls.getName(), e);
            }
        }
    }

    public LanguageDriver getDriver(Class<?> cls) {
        return LANGUAGE_DRIVER_MAP.get(cls);
    }

    public LanguageDriver getDefaultDriver() {
        return getDriver(getDefaultDriverClass());
    }

    private Class<?> getDefaultDriverClass() {
        return defaultDriverClass;
    }

    /**
     * configuration() 有调用，默认的为 XMLLanguageDriver
     * @param defaultDriverClass
     */
    public void setDefaultDriverClass(Class<?> defaultDriverClass) {
        register(defaultDriverClass);
        this.defaultDriverClass = defaultDriverClass;
    }

}
