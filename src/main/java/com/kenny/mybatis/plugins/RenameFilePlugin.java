package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

/**
 * Created by kehui on 2015/1/6.
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
        priffix = properties.getProperty(PRIFFIX_STR_KEY);
        suffix = properties.getProperty(SUFFIX_STR_KEY);
        return true;
    }


    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getMyBatis3JavaMapperType();
        String oldPri = oldType.substring(0, oldType.lastIndexOf(".") + 1);
        String oldClassName = oldType.replace(oldPri, "");
        String newType = oldPri + priffix + oldClassName + suffix;
        introspectedTable.setMyBatis3JavaMapperType(newType);

        String oldXml = introspectedTable.getMyBatis3XmlMapperFileName();
        String newXml = priffix + oldXml.replace("\\.", suffix + ".");
        introspectedTable.setMyBatis3XmlMapperFileName(newXml);
        super.initialized(introspectedTable);
    }

}
