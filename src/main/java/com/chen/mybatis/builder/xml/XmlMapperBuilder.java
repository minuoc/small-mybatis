package com.chen.mybatis.builder.xml;

import com.chen.mybatis.builder.BaseBuilder;
import com.chen.mybatis.io.Resources;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XmlMapperBuilder extends BaseBuilder {

    private Element element;

    private String resource;

    private String currentNameSpace;


    public XmlMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream),configuration,resource);
    }

    public XmlMapperBuilder(Document document,Configuration configuration, String resource) {
        super(configuration);
        this.element = document.getRootElement();
        this.resource = resource;
    }



    public void parse() throws Exception {
        // 如果当前资源没有加载过再加载，防止重复加载
        if (!configuration.isResourceLoaded(resource)) {
            configurationElement(element);
            // 标记一下，已经加载过了
            configuration.addLoadedResource(resource);
            // 绑定映射器 到 namespace
            configuration.addMapper(Resources.classForName(currentNameSpace));
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
        currentNameSpace = element.attributeValue("namespace");
        if (currentNameSpace.equals("")) {
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }
        // 2. 配置select | insert | update | delete
        buildStatementFromContext(element.elements("select"));

    }

    private void buildStatementFromContext(List<Element> list) {
        for (Element element : list) {
            final XMLStatementBuilder statementParser = new XMLStatementBuilder(configuration,element,currentNameSpace);
            statementParser.parseStatementNode();
        }
    }


}
