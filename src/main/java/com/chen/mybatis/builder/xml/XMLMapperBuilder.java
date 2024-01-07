package com.chen.mybatis.builder.xml;

import com.chen.mybatis.builder.BaseBuilder;
import com.chen.mybatis.builder.MapperBuilderAssistant;
import com.chen.mybatis.builder.ResultMapResolver;
import com.chen.mybatis.io.Resources;
import com.chen.mybatis.mapping.ResultFlag;
import com.chen.mybatis.mapping.ResultMap;
import com.chen.mybatis.mapping.ResultMapping;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
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
        // 2.解析resultMap
        resultMapElements(element.elements("resultMap"));

        // 3. 配置select | insert | update | delete
        buildStatementFromContext(element.elements("select"),element.elements("insert"),
                element.elements("update"),
                element.elements("delete"));

    }

    private void resultMapElements(List<Element> list) {
        for (Element element : list) {
            try {
                resultMapElement(element, Collections.emptyList());
            } catch (Exception e) {

            }
        }

    }

    /**
     * <resultMap id="activityMap" type="cn.bugstack.mybatis.test.po.Activity">
     * <id column="id" property="id"/>
     * <result column="activity_id" property="activityId"/>
     * <result column="activity_name" property="activityName"/>
     * <result column="activity_desc" property="activityDesc"/>
     * <result column="create_time" property="createTime"/>
     * <result column="update_time" property="updateTime"/>
     * </resultMap>
     */
    private ResultMap resultMapElement(Element resultMapNode, List<ResultMapping> additionResultMappings) {
        String id = resultMapNode.attributeValue("id");
        String type = resultMapNode.attributeValue("type");
        Class<?> typeClass = resolveClass(type);

        List<ResultMapping> resultMappings = new ArrayList<>();
        resultMappings.addAll(additionResultMappings);
        List<Element> resultChildren = resultMapNode.elements();
        for (Element resultChild : resultChildren) {
            List<ResultFlag> flags = new ArrayList<>();
            if ("id".equals(resultChild.getName())){
                flags.add(ResultFlag.ID);
            }
            // 构建 ResultMapping
            resultMappings.add(buildStatementFromContext(resultChild,typeClass,flags));
        }

        // 创建结果映射解析器
        ResultMapResolver resultMapResolver = new ResultMapResolver(builderAssistant, id, typeClass, resultMappings);
        return resultMapResolver.resolve();

    }

    private ResultMapping buildStatementFromContext(Element context,Class<?> resultType, List<ResultFlag> flags) {
        String property = context.attributeValue("property");
        String column = context.attributeValue("column");
        return builderAssistant.buildResultMapping(resultType,property,column,flags);
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
