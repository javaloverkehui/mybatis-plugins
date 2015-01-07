package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kehui on 2015/1/6.
 * 根据regex来替换文件名称
 */
public class RenameFileByPatternPlugin extends PluginAdapter {
    private static final String SEARCH_STR_KEY = "search";
    private static final String REPLACE_STR_KEY = "replace";
    private String searchString;
    private String replaceString;
    private Pattern pattern;


    public RenameFileByPatternPlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        searchString = properties.getProperty(SEARCH_STR_KEY);
        replaceString = properties.getProperty(REPLACE_STR_KEY);
        pattern = Pattern.compile(searchString);
        return true;
    }


    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        //替换java文件名称
        String oldType = introspectedTable.getMyBatis3JavaMapperType();
        Matcher matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        introspectedTable.setMyBatis3JavaMapperType(oldType);

        //替换xml文件名称
        String xml = introspectedTable.getMyBatis3XmlMapperFileName();
        Matcher xmlMatcher = pattern.matcher(xml);
        xml = xmlMatcher.replaceAll(replaceString);
        introspectedTable.setMyBatis3XmlMapperFileName(xml);
        super.initialized(introspectedTable);
    }
}
