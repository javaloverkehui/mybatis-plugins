package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 * 给xml中添加分页代码
 */
public class PagingPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        addLimit(element, introspectedTable);
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        addLimit(element, introspectedTable);
        return super.sqlMapSelectByExampleWithBLOBsElementGenerated(element, introspectedTable);
    }

    /**
     * 分页代码
     * @param element
     * @param introspectedTable
     */
    public void addLimit(XmlElement element, IntrospectedTable introspectedTable) {
        element.addElement(new TextElement("<if test=\"limit!=null\">"));
        element.addElement(new TextElement("limit"));
        element.addElement(new TextElement("<if test=\"start!=null\">"));
        element.addElement(new TextElement(" #{start},"));
        element.addElement(new TextElement(" </if>"));
        element.addElement(new TextElement(" #{limit}"));
        element.addElement(new TextElement(" </if>"));
    }
}
