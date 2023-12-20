package com.chen.mybatis.session;

import com.chen.mybatis.binding.MapperRegistry;
import com.chen.mybatis.datasource.DruidDataSourceFactory;
import com.chen.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.chen.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.chen.mybatis.executor.Executor;
import com.chen.mybatis.executor.SimpleExecutor;
import com.chen.mybatis.executor.resultset.DefaultResultSetHandler;
import com.chen.mybatis.executor.resultset.ResultSetHandler;
import com.chen.mybatis.executor.statement.PrepareStatementHandler;
import com.chen.mybatis.executor.statement.StatementHandler;
import com.chen.mybatis.mapping.BoundSql;
import com.chen.mybatis.mapping.Environment;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.reflection.MetaObject;
import com.chen.mybatis.reflection.factory.DefaultObjectFactory;
import com.chen.mybatis.reflection.factory.ObjectFactory;
import com.chen.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import com.chen.mybatis.reflection.wrapper.ObjectWrapperFactory;
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

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry();


    /**
     * 映射的语句,存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

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
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
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
    public StatementHandler newStatementHandler(Executor executor, MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PrepareStatementHandler(executor, ms, parameter, resultHandler, boundSql);
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


    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }


    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }
}
