package com.kenny.mybatis.plugins;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 * 替换pojo里的字段类型,同时修改setter getter方法里的返回类型和参数类型
 */
public class ReTypePlugin extends PluginAdapter {

    private final static String FROM_FIELDS_KEY = "fromFields";
    private final static String TO_FIELDS_KEY = "toFields";

    private String fromFields;
    private String toFields;
    @Override
    public boolean validate(List<String> warnings) {
        //如从java.lang.Integer变为java.lang.Byte
        fromFields = this.properties.getProperty(FROM_FIELDS_KEY);//从某一批类型  (类型以全称表示,以逗号隔开)
        toFields = this.properties.getProperty(TO_FIELDS_KEY);//变成某一批类型  (类型以全称表示,以逗号隔开)
        //两个字符串, split之后长度一致, 源类型与替换后的类型, 位置保持一致
        return fromFields.split(",").length == toFields.split(",").length;
    }

    /**
     * 添加字段注释, 并替换字段类型
     * @param field
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
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
                //类型一致,则替换
                if (fieldType.equals(field.getType().getFullyQualifiedName())) {
                    FullyQualifiedJavaType fullyQualifiedJavaType = new FullyQualifiedJavaType(tfields[i]);
                    field.setType(fullyQualifiedJavaType);
                }
            }
        }
        return super.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
    }

    /**
     * 替换get方法的返回类型
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
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

    /**
     * 替换set方法的参数类型
     * @param method
     * @param topLevelClass
     * @param introspectedColumn
     * @param introspectedTable
     * @param modelClassType
     * @return
     */
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
