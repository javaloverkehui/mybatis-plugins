package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 * 给文件名加前置名称和后置名称
 * 如Test.java  变为 PriffixTestSuffix.java
 */
public class RenameFilePlugin extends PluginAdapter {
    private static final String PRIFFIX_STR_KEY = "priffix";
    private static final String SUFFIX_STR_KEY = "suffix";
    private String priffix;
    private String suffix;


    public RenameFilePlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        priffix = properties.getProperty(PRIFFIX_STR_KEY); //前置名称
        suffix = properties.getProperty(SUFFIX_STR_KEY);//后置名称
        return true;
    }


    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        //替换java文件
        String oldType = introspectedTable.getMyBatis3JavaMapperType();
        String oldPri = oldType.substring(0, oldType.lastIndexOf(".") + 1);
        String oldClassName = oldType.replace(oldPri, "");
        String newType = oldPri + priffix + oldClassName + suffix;
        introspectedTable.setMyBatis3JavaMapperType(newType);

        //替换xml文件
        String oldXml = introspectedTable.getMyBatis3XmlMapperFileName();
        String newXml = priffix + oldXml.replace("\\.", suffix + ".");
        introspectedTable.setMyBatis3XmlMapperFileName(newXml);

        super.initialized(introspectedTable);
    }

}
