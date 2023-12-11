package com.chen.mybatis.parsing;

/**
 * description 普通几号解析器 处理#{} 和 ${} 参数
 *
 * @author : e-Lufeng.Chen
 * @create 2023/12/11
 */
public class GenericTokenParser {
    // 有一个开始和结束记号
    private final String openToken;
    private final String closeToken;
    // 记号处理器
    private final TokenHandler handler;

    public GenericTokenParser(String openToken, String closeToken, TokenHandler handler) {
        this.openToken = openToken;
        this.closeToken = closeToken;
        this.handler = handler;
    }


    public String parse(String text) {
        StringBuilder builder = new StringBuilder();
        if (text != null && text.length() > 0) {
            char[] src = text.toCharArray();
            int offset = 0;
            int start = text.indexOf(openToken, offset);

            // #{favouriteSection,jdbcType=VARCHAR}
            // 这里是循环解析参数 参考GenricTokenParserTest,比如可以解析${first_name} ${initial} ${last_name} reporting.这样的字符串，里面有三个${}
            while (start > -1) {
                // 判断一下 ${ 前面是否是 反斜杠,这个逻辑在老板的mybatis 中 (如3.1.0) 是没有的
                if (start > 0 && src[start - 1] == '\\') {
                    // 新版已经没有调用substring了，改为调用如下offset方式，提高了效率
                    builder.append(src, offset, start - offset - 1).append(openToken);
                    offset = start + openToken.length();
                } else {
                    int end = text.indexOf(closeToken, start);
                    if (end == -1) {
                        builder.append(src, offset, src.length - offset);
                        offset = src.length;
                    } else {
                        builder.append(src,offset,start - offset);
                        offset = start + openToken.length();
                        String content = new String(src, offset, end - offset);
                        // 得到一对大括号里的字符串后， 调用handler.handleToken,比如替换变量来这种功能
                        builder.append(handler.handleToken(content));
                        offset = end + closeToken.length();
                    }
                }
                start = text.indexOf(openToken, offset);

            }
            if (offset < src.length) {
                builder.append(src, offset, src.length - offset);
            }

        }
        return builder.toString();
    }

}
