package com.chen.mybatis.builder.xml;

import com.chen.mybatis.builder.BaseBuilder;
import com.chen.mybatis.builder.MapperBuilderAssistant;
import com.chen.mybatis.io.Resources;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder extends BaseBuilder {

    private Element element;

    // 映射器构建助手
    private MapperBuilderAssistant builderAssistant;

    private String resource;



    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream),configuration,resource);
    }

    public XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration,resource);
        this.element = document.getRootElement();
        this.resource = resource;
    }



    public void parse() throws Exception {
        // 如果当前资源没有加载过再加载，防止重复加载
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(element);
            // 标记一下，已经加载过了
            configuration.addLoadedResource(resource);
            // 绑定映射器 到 namespace Mybatis 源码 方法名 -> bindMapperForNamespace
            configuration.addMapper(Resources.classForName(builderAssistant.getCurrentNamespace()));
        }
    }

    /**
     * 配置 mapper 元素
     *  // <mapper namespace="org.mybatis.example.BlogMapper">
     *     //   <select id="selectBlog" parameterType="int" resultType="Blog">
     *     //    select * from Blog where id = #{id}
     *     //   </select>
     *     // </mapper>
     * @param element
     */
    private void configurationElement(Element element) {
        // 1. 配置namespace
        String  currentNameSpace = element.attributeValue("namespace");
        if (currentNameSpace.equals("")) {
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }
        builderAssistant.setCurrentNamespace(currentNameSpace);
        // 2. 配置select | insert | update | delete
        buildStatementFromContext(element.elements("select"),element.elements("insert"),
                element.elements("update"),
                element.elements("delete"));

    }

    private void buildStatementFromContext(List<Element>... lists) {
        for (List<Element> list : lists) {
            for (Element element : list) {
                final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration,builderAssistant,element);
                statementParser.parseStatementNode();
            }
        }
    }


}
