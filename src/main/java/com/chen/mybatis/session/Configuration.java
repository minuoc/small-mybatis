package com.chen.mybatis.session;

import com.chen.mybatis.binding.MapperRegistry;
import com.chen.mybatis.datasource.DruidDataSourceFactory;
import com.chen.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.chen.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.executor.SimpleExecutor;
import com.chen.mybatis.executor.keygen.KeyGenerator;
import com.chen.mybatis.executor.keygen.SelectKeyGenerator;
import com.chen.mybatis.executor.parameter.ParameterHandler;
import com.chen.mybatis.executor.resultset.DefaultResultSetHandler;
import com.chen.mybatis.executor.resultset.ResultSetHandler;
import com.chen.mybatis.executor.statement.PreparedStatementHandler;
import com.chen.mybatis.executor.statement.StatementHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.Environment;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.ResultMap;
import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.reflection.factory.DefaultObjectFactory;
import com.chen.mybatis.reflection.factory.ObjectFactory;
import com.chen.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.chen.mybatis.reflection.wrapper.ObjectWrapperFactory;
import com.chen.mybatis.scripting.LanguageDriver;
import com.chen.mybatis.scripting.LanguageDriverRegistry;
import com.chen.mybatis.scripting.xmltags.XMLLanguageDriver;
import com.chen.mybatis.transaction.Transaction;
import com.chen.mybatis.transaction.jdc.JdbcTransactionFactory;
import com.chen.mybatis.type.TypeAliasRegistry;
import com.chen.mybatis.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Configuration {

    /**
     * 环境
     */
    protected Environment environment;

    protected boolean useGeneratedKeys = false;
    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);


    /**
     * 映射的语句,存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 结果映射
     */
    protected final Map<String,ResultMap> resultMaps = new HashMap<>();

    protected final Map<String, KeyGenerator> keyGenerators = new HashMap<>();

    /**
     * 类型别名注册机
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();

    /**
     * 类型处理器注册机
     */
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    /**
     * 对象工厂 和 对象包装工厂
     */
    protected ObjectFactory objectFactory = new DefaultObjectFactory();

    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    protected final Set<String> loadedResources = new HashSet<>();

    protected String databaseId;

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }


    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public Object getDatabaseId() {
        return databaseId;
    }

    /**
     * 创建结果集处理器
     *
     * @param executor
     * @param mappedStatement
     * @param boundSql
     * @return
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement,RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, resultHandler,rowBounds,boundSql);
    }


    /**
     * 创建生产执行器
     *
     * @param tx
     * @return
     */
    public Executor newExecutor(Transaction tx) {
        return new SimpleExecutor(this, tx);
    }

    /**
     * 创建 语句处理器
     *
     * @param executor
     * @param ms
     * @param parameter
     * @param resultHandler
     * @param boundSql
     * @return
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement ms, Object parameter,RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, ms, parameter,rowBounds,resultHandler, boundSql);
    }


    /**
     * 创建元对象
     *
     * @param parameterObject
     * @return
     */
    public MetaObject newMetaObject(Object parameterObject) {
        return MetaObject.forObject(parameterObject, objectFactory, objectWrapperFactory);
    }

    /**
     * 类型处理器 注册机
     * @return
     */
    public TypeHandlerRegistry getTypeHandlerRegistry(){
        return typeHandlerRegistry;
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }


    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        // 创建参数处理器
        ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement,parameterObject,boundSql);
        // 插件的一些参数，也是在这里处理，暂时不添加这部分内容
        return parameterHandler;
    }

    public LanguageDriver getDefaultScriptingLanguageInstance(){
        return languageRegistry.getDefaultDriver();
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public ResultMap getResultMap(String id) {
        return resultMaps.get(id);
    }

    public void addResultMap(ResultMap resultMap) {
        resultMaps.put(resultMap.getId(),resultMap);
    }

    public void addKeyGenerator(String id, SelectKeyGenerator selectKeyGenerator) {
        keyGenerators.put(id,selectKeyGenerator);
    }

    public KeyGenerator getKeyGenerator(String id) {
        return keyGenerators.get(id);
    }

    public boolean hasKeyGenerator(String id) {
        return keyGenerators.containsKey(id);
    }

    public boolean isUseGeneratedKeys() {
        return useGeneratedKeys;
    }

    public void setUseGeneratedKeys(boolean useGeneratedKeys) {
        this.useGeneratedKeys = useGeneratedKeys;
    }
}
