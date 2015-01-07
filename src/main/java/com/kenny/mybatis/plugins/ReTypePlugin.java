package com.kenny.mybatis.plugins;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 */
public class ReTypePlugin extends PluginAdapter {

    private final static String FROM_FIELDS_KEY = "fromFields";
    private final static String TO_FIELDS_KEY = "toFields";

    private String fromFields;
    private String toFields;
    @Override
    public boolean validate(List<String> warnings) {
        fromFields = this.properties.getProperty(FROM_FIELDS_KEY);
        toFields = this.properties.getProperty(TO_FIELDS_KEY);
        return fromFields.split(",").length == toFields.split(",").length;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        //添加数据库注释
        if (null != introspectedColumn.getRemarks() && !"".equals(introspectedColumn.getRemarks())) {
            String remark = " * " + introspectedColumn.getRemarks();
            field.getJavaDocLines().add(3, remark);
        }
        if (fromFields.contains(field.getType().getFullyQualifiedName())) {
            String[] ffields = fromFields.split(",");
            String[] tfields = toFields.split(",");
            for (int i = 0; i < ffields.length; i++) {
                String fieldType = ffields[i];
                if (fieldType.equals(field.getType().getFullyQualifiedName())) {
                    FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(tfields[i]);
                    field.setType(fullyQualifiedJavaType);
                }
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        if (fromFields.contains(method.getReturnType().getFullyQualifiedName())) {
            String[] ffields = fromFields.split(",");
            String[] tfields = toFields.split(",");
            for (int i = 0; i < ffields.length; i++) {
                String fieldType = ffields[i];
                if (fieldType.equals(method.getReturnType().getFullyQualifiedName())) {
                    FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(tfields[i]);
                    method.setReturnType(fullyQualifiedJavaType);
                }
            }
        }
        return super.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        List<Parameter> parameters = method.getParameters();
        if (CollectionUtils.isNotEmpty(parameters)) {
            for (int j = 0; j < parameters.size(); j++) {
                Parameter parameter = parameters.get(j);
                if (fromFields.contains(parameter.getType().getFullyQualifiedName())) {
                    String[] ffields = fromFields.split(",");
                    String[] tfields = toFields.split(",");
                    for (int i = 0; i < ffields.length; i++) {
                        String fieldType = ffields[i];
                        if (fieldType.equals(parameter.getType().getFullyQualifiedName())) {
                            FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(tfields[i]);
                            Parameter p = new Parameter(fullyQualifiedJavaType, parameter.getName());
                            parameters.remove(j);
                            method.addParameter(j, p);
                        }
                    }
                }
            }
        }
        return super.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }
}
