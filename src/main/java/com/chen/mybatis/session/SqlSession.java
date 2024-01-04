package com.chen.mybatis.session;

import java.util.List;

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
     * Retrieve a list of mapped objects from the statement key and parameter.
     * 获取多条记录，这个方法容许我们可以传递一些参数
     *
     * @param <E>       the returned list element type
     * @param statement Unique identifier matching the statement to use.
     * @param parameter A parameter object to pass to the statement.
     * @return List of mapped object
     */
    <E> List<E> selectList(String statement, Object parameter);


    /**
     * Execute an insert statement with the given parameter object. Any generated
     * autoincrement values or selectKey entries will modify the given parameter
     * object properties. Only the number of rows affected will be returned.
     * 插入记录，容许传入参数。
     *
     * @param statement Unique identifier matching the statement to execute.
     * @param parameter A parameter object to pass to the statement.
     * @return int The number of rows affected by the insert. 注意返回的是受影响的行数
     */
    int insert(String statement, Object parameter);



    /**
     * Execute an update statement. The number of rows affected will be returned.
     * 更新记录
     *
     * @param statement Unique identifier matching the statement to execute.
     * @param parameter A parameter object to pass to the statement.
     * @return int The number of rows affected by the update. 返回的是受影响的行数
     */
    int update(String statement, Object parameter);


    /**
     * Execute a delete statement. The number of rows affected will be returned.
     * 删除记录
     *
     * @param statement Unique identifier matching the statement to execute.
     * @param parameter A parameter object to pass to the statement.
     * @return int The number of rows affected by the delete. 返回的是受影响的行数
     */
    Object delete(String statement, Object parameter);


    void commit();

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
