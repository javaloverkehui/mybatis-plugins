package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 * 为example添加一个父类, 用于分页或者其它
 */
public class ExamplesPlugin extends PluginAdapter {

    private String parentExample;

    public ExamplesPlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        this.parentExample = this.properties.getProperty("parentExample");//父类的全称
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.setSuperClass(parentExample);//设置父类
        topLevelClass.addImportedType(parentExample);//添加父类的引入
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }
}
