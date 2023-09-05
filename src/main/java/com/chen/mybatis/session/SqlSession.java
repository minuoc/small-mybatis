package com.chen.mybatis.session;

public interface SqlSession {

    /**
     * 根据指定的SqlId 获取一条记录的封装对象
     * @param statement
     * @return
     */
    <T> T selectOne(String statement);

    /**
     * 根据指定的sqlId 获取一条记录的封装对象，只不过这个方法容许我们给sql传递一些参数
     * 一般在实际使用中 这个传递的是pojo 或者map 或者 ImutableMap
     * @param statement
     * @param parameter
     * @return
     */
    <T> T selectOne(String statement,Object parameter);

    /**
     * 得到映射器 这个巧妙的使用了泛型，使得类型安全
     * @param type
     * @return
     */
    <T> T getMapper(Class<T> type);


    /**
     * 得到配置
     * @return Configuration
     */
    Configuration getConfiguration();

}
