package com.kenny.mybatis.plugins;

import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.config.MergeConstants.NEW_ELEMENT_TAG;

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
        List<String> docLines = initFieldDocLines(introspectedColumn, introspectedTable);
        field.getJavaDocLines().clear();
        field.getJavaDocLines().addAll(docLines);
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    /**
     * 字段注释
     * @param introspectedColumn
     * @param introspectedTable
     * @return
     */
    public List<String> initFieldDocLines(IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable) {
        List<String> javaDocLines = new ArrayList<>();
        javaDocLines.add("/**");
        javaDocLines.add(" * " + introspectedTable.getTableConfiguration().getTableName() + "."
                + introspectedColumn.getActualColumnName());
        if (StringUtils.isNotBlank(introspectedColumn.getRemarks())) {
            javaDocLines.add(" * " + introspectedColumn.getRemarks());
        }
        javaDocLines.add(" * " + introspectedColumn.getJdbcTypeName());
        javaDocLines.add(" * " + introspectedColumn.getLength());
        javaDocLines.add(" * " + NEW_ELEMENT_TAG);
        javaDocLines.add(" */");
        return javaDocLines;
    }


}
