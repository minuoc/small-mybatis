package com.chen.mybatis.builder.xml;

import com.chen.mybatis.builder.BaseBuilder;
import com.chen.mybatis.mapping.SqlCommandType;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.Locale;


public class XMLStatementBuilder extends BaseBuilder {

    private String currentNameSpace;

    private Element element;

    public XMLStatementBuilder(Configuration configuration, Element element, String currentNameSpace) {
        super(configuration);
        this.element = element;
        this.currentNameSpace = currentNameSpace;
    }


    public void parseStatementNode(){

        String id = element.attributeValue("id");

        // 参数类型
        String parameterType = element.attributeValue("parameterType");

        Class<?> parameterTypeClass = resolveAlias(parameterType);

        // 结果类型
        String resultType = element.attributeValue("resultType");

        Class<?> resultTypeClass = resolveAlias(resultType);

        // 获取命令类型
        String nodeName = element.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

        //获取默认语言驱动器
        Class<?> langClass =


    }

}
