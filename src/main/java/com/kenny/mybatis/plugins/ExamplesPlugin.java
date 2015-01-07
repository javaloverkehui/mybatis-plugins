package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 */
public class ExamplesPlugin extends PluginAdapter {

    private String parentExample;

    public ExamplesPlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        this.parentExample = this.properties.getProperty("parentExample");
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.setSuperClass(parentExample);
        topLevelClass.addImportedType(parentExample);
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }
}
