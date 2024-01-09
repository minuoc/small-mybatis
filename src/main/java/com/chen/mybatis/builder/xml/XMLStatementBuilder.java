package com.chen.mybatis.builder.xml;

import com.chen.mybatis.builder.BaseBuilder;
import com.chen.mybatis.builder.MapperBuilderAssistant;
import com.chen.mybatis.executor.keygen.KeyGenerator;
import com.chen.mybatis.executor.keygen.NoKeyGenerator;
import com.chen.mybatis.executor.keygen.SelectKeyGenerator;
import com.chen.mybatis.mapping.MappedStatement;
import com.chen.mybatis.mapping.SqlCommandType;
import com.chen.mybatis.mapping.SqlSource;
import com.chen.mybatis.scripting.LanguageDriver;
import com.chen.mybatis.session.Configuration;
import org.dom4j.Element;

import java.util.List;
import java.util.Locale;


public class XMLStatementBuilder extends BaseBuilder {

    private MapperBuilderAssistant builderAssistant;

    private Element element;

    public XMLStatementBuilder(Configuration configuration,MapperBuilderAssistant mapperBuilderAssistant, Element element) {
        super(configuration);
        this.builderAssistant = mapperBuilderAssistant;
        this.element = element;

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
    //  SELECT * FROM PERSON WHERE ID = #{id}1·
    //</select>
    public void parseStatementNode(){

        String id = element.attributeValue("id");

        // 参数类型
        String parameterType = element.attributeValue("parameterType");
        Class<?> parameterTypeClass = resolveAlias(parameterType);

        //外部应用 resultMap
        String resultMap = element.attributeValue("resultMap");


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

        // 调用助手类
        builderAssistant.addMappedStatement(id,sqlSource,sqlCommandType,parameterTypeClass,resultMap,resultTypeClass,langDriver);

    }


    private void processSelectKeyNodes(String id, Class<?> parameterTypeClass, LanguageDriver languageDriver){
        List<Element> selectKeyNodes = element.elements("selectKey");
        parseSelectKeyNodes(id,selectKeyNodes,parameterTypeClass,languageDriver);
    }

    private void parseSelectKeyNodes(String parentId, List<Element> selectKeyNodes, Class<?> parameterTypeClass, LanguageDriver languageDriver) {
        for (Element nodeToHandle : selectKeyNodes) {
            String id = parentId + SelectKeyGenerator.SELECT_KEY_SUFFIX;
            parseSelectKeyNode(id,nodeToHandle,parameterTypeClass,languageDriver);
        }

    }

    /**
     * 解析selectKey节点
     * <selectKey keyProperty="id" order="AFTER" resultType="long">
     * SELECT LAST_INSERT_ID()
     * </selectKey>
     * @param id
     * @param nodeToHandle
     * @param parameterTypeClass
     * @param languageDriver
     */
    private void parseSelectKeyNode(String id, Element nodeToHandle, Class<?> parameterTypeClass, LanguageDriver languageDriver) {
        String resultType = nodeToHandle.attributeValue("resultType");
        Class<?> resultTypeClass = resolveClass(resultType);
        boolean executeBefore = "BEFORE".equals(nodeToHandle.attributeValue("order","AFTER"));
        String keyProperty = nodeToHandle.attributeValue("keyProperty");

        // default
        String resultMap = null;
        KeyGenerator keyGenerator = new NoKeyGenerator();

        // 解析成SqlSource, DynamicSqlSource/RawSqlSource
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, nodeToHandle, parameterTypeClass);
        SqlCommandType sqlCommandType = SqlCommandType.SELECT;

        // 调用助手类
        builderAssistant.addMappedStatement(id,
                sqlSource,
                sqlCommandType,
                parameterTypeClass,
                resultMap,
                resultTypeClass,
                languageDriver);

        id = builderAssistant.applyCurrentNamespace(id,false);
        MappedStatement keyStatement = configuration.getMappedStatement(id);
        configuration.addKeyGenerator(id,new SelectKeyGenerator(keyStatement,executeBefore));

    }
}
