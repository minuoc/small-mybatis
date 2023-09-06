package com.chen.mybatis.builder;

import com.chen.mybatis.io.Resources;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.SqlCommandType;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlConfigBuilder extends BaseBuilder{
    private Element root;

    public XmlConfigBuilder(Reader reader) {
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
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    private void mapperElement(Element mappers) throws IOException, DocumentException, ClassNotFoundException {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList){
            // mapper 文件路径
            String resource = e.attributeValue("resource");

            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();
            // 命名空间
            String namespace = root.attributeValue("namespace");

            // Select
            List<Element> selectNodes = root.elements("select");
            for (Element node : selectNodes){
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();
                //  匹配
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                // 匹配 #{}
                Matcher matcher  = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++){
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i,g2);
                    sql = sql.replace(g1,"?");
                }

                String msId = namespace + "." + id;
                String nodeName = node.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
                MappedStatement mappedStatement = new MappedStatement.Builder(configuration,msId,sqlCommandType,
                        parameterType,resultType,sql,parameter).build();

                // 添加解析 SQL
                configuration.addMappedStatement(mappedStatement);
            }
            // 注册Mapper映射器
            configuration.addMapper(Resources.classForName(namespace));


        }


    }
}
