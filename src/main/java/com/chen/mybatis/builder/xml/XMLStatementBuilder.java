package com.chen.mybatis.builder.xml;

import com.chen.mybatis.builder.BaseBuilder;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.SqlCommandType;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.scripting.LanguageDriver;
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


    //解析语句(select|insert|update|delete)
    //<select
    //  id="selectPerson"
    //  parameterType="int"
    //  parameterMap="deprecated"
    //  resultType="hashmap"
    //  resultMap="personResultMap"
    //  flushCache="false"
    //  useCache="true"
    //  timeout="10000"
    //  fetchSize="256"
    //  statementType="PREPARED"
    //  resultSetType="FORWARD_ONLY">
    //  SELECT * FROM PERSON WHERE ID = #{id}
    //</select>
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
        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        LanguageDriver langDriver = configuration.getLanguageRegistry().getDriver(langClass);

        SqlSource sqlSource = langDriver.createSqlSource(configuration,element,parameterTypeClass);
        MappedStatement mappedStatement =
                new MappedStatement.Builder(configuration,currentNameSpace + "." + id,
                sqlCommandType,sqlSource,resultTypeClass).build();

        // 添加解析 SQL
        configuration.addMappedStatement(mappedStatement);

    }

}
