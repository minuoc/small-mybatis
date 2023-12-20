package com.chen.mybatis.builder.xml;

import com.chen.mybatis.builder.BaseBuilder;
import com.chen.mybatis.datasource.DataSourceFactory;
import com.chen.mybatis.io.Resources;
import com.chen.mybatis.mapping.Environment;
import com.chen.mybatis.session.Configuration;
import com.chen.mybatis.transaction.TransactionFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * XML配置构建器，建造者模式
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/11
 */
public class XMLConfigBuilder extends BaseBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        //调用弗雷初始化 Configuration
        super(new Configuration());
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public Configuration parse() {
        try {
            // 环境
            environmentsElement(root.element("environments"));
            //解析映射器
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    private void environmentsElement(Element context) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String environment = context.attributeValue("default");
        List<Element> environmentList = context.elements("environment");
        for (Element e : environmentList) {
            String id = e.attributeValue("id");
            if (environment.equals(id)){
                // 事务管理器
                TransactionFactory txFactory = (TransactionFactory) typeAliasRegistry.resolveAlias(e.element("transactionManager").attributeValue("type")).newInstance();

                // 数据源
                Element dataSourceElement = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) typeAliasRegistry.resolveAlias(dataSourceElement.attributeValue("type")).newInstance();
                List<Element> propertyList = dataSourceElement.elements("property");
                Properties props = new Properties();
                for (Element property : propertyList) {
                    props.setProperty(property.attributeValue("name"), property.attributeValue("value"));
                }
                dataSourceFactory.setProperties(props);
                DataSource dataSource = dataSourceFactory.getDataSource();

                // 构建环境
                Environment.Builder environmentBuilder = new Environment.Builder(id)
                        .transactionFactory(txFactory)
                        .dataSource(dataSource);

                configuration.setEnvironment(environmentBuilder.build());

            }
        }

    }

    /*
     * <mappers>
     *	 <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
     *	 <mapper resource="org/mybatis/builder/PostMapper.xml"/>
     * </mappers>
     */
    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList){
            String resource = e.attributeValue("resource");
            InputStream inputStream = Resources.getResourceAsStream(resource);
            // 在for 循环里每个mapper 都重新new一个XMLMapperBuilder,来解析
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource);
            mapperParser.parse();

        }
    }
}
