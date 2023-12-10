package com.chen.mybatis.scripting.xmltags;

import java.util.List;

/**
 * 混合sql 节点
 */
public class MixedSqlNode implements SqlNode{
    // 组合模式 拥有一个SqlNode的list
    private List<SqlNode> contents;

    public MixedSqlNode(List<SqlNode> contents) {
        this.contents = contents;
    }

    @Override
    public boolean apply(DynamicContext context) {
        // 依次调用list 里面每个元素的apply
        contents.forEach(node -> node.apply(context));
        return true;
    }
}
