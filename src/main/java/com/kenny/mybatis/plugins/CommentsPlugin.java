package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehui on 2015/1/9.
 */
public class CommentsPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<String> docLines = initFieldDocLines(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
        field.getJavaDocLines().clear();
        field.getJavaDocLines().addAll(docLines);
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<String> docLines = initSetterMethodDocLines(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
        method.getJavaDocLines().clear();
        method.getJavaDocLines().addAll(docLines);
        return super.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<String> docLines = initGetterMethodDocLines(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
        method.getJavaDocLines().clear();
        method.getJavaDocLines().addAll(docLines);
        return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<String> docLines = initJavaMapperDocLines(interfaze, topLevelClass, introspectedTable);
        List<Method> methods = interfaze.getMethods();
        for (Method method : methods) {
            method.getJavaDocLines().clear();
            method.getJavaDocLines().addAll(docLines);
        }
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<String> docLines = initExampleDocLines(topLevelClass, introspectedTable);
        topLevelClass.getJavaDocLines().clear();
        topLevelClass.getJavaDocLines().addAll(docLines);
        List<Field> fields = topLevelClass.getFields();
        for (Field field : fields) {
            field.getJavaDocLines().clear();
            field.getJavaDocLines().addAll(docLines);
        }
        List<Method> methods = topLevelClass.getMethods();
        for (Method method : methods) {
            method.getJavaDocLines().clear();
            method.getJavaDocLines().addAll(docLines);
        }
        List<InnerClass> innerClasses = topLevelClass.getInnerClasses();
        for (InnerClass innerClass : innerClasses) {
            innerClass.getJavaDocLines().clear();
            innerClass.getJavaDocLines().addAll(docLines);
        }
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 字段注释
     * @param field
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    public List<String> initFieldDocLines(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<String> javaDocLines = new ArrayList<>();
        javaDocLines.add("/**");
        javaDocLines.add(" * 该字段对应数据库表中的字段" + introspectedTable.getTableConfiguration().getTableName() + "."
                + introspectedColumn.getActualColumnName());
        javaDocLines.add(" * 字段描述：" + introspectedColumn.getRemarks());
        javaDocLines.add(" * 字段数据库类型：" + introspectedColumn.getJdbcTypeName());
        javaDocLines.add(" * 字段长度：" + introspectedColumn.getLength());
        javaDocLines.add(" * @mbggenerated");
        javaDocLines.add(" */");
        return javaDocLines;
    }

    /**
     * 生成setter方法注释
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    public List<String> initSetterMethodDocLines(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<String> javaDocLines = new ArrayList<>();
        javaDocLines.add("/* @mbggenerated */");
        return javaDocLines;
    }

    /**
     * 生成getter方法注释
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    public List<String> initGetterMethodDocLines(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<String> javaDocLines = new ArrayList<>();
        javaDocLines.add("/* @mbggenerated */");
        return javaDocLines;
    }

    /**
     * mapper接口注释
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    public List<String> initJavaMapperDocLines(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<String> javaDocLines = new ArrayList<>();
        javaDocLines.add("/* @mbggenerated */");
        return javaDocLines;
    }

    /**
     * example注释
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    public List<String> initExampleDocLines(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<String> javaDocLines = new ArrayList<>();
        javaDocLines.add("/* @mbggenerated */");
        return javaDocLines;
    }

    /**
     * xml注释
     * @param field
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
    public List<String> initSqlMapperDocLines(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<String> xmlDocLines = new ArrayList<>();
        xmlDocLines.add("<!--");
        xmlDocLines.add("  WARNING - @mbggenerated");
        xmlDocLines.add("  该元素由mybatis-generator自动生成, 请勿修改");
        xmlDocLines.add("-->");
        return xmlDocLines;
    }
}
