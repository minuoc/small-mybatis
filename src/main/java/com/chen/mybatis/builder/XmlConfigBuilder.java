package com.chen.mybatis.builder;

import com.chen.mybatis.session.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.xml.sax.InputSource;

import java.io.Reader;
import java.util.List;

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

    private void mapperElement(Element mappers) {
        List<Element> mapperList = mappers.elements("mapper");
        //TODO
    }
}
