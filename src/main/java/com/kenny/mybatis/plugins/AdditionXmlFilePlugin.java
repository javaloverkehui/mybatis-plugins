package com.kenny.mybatis.plugins;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 */
public class AdditionXmlFilePlugin extends PluginAdapter {
    private static final String SEARCH_STR_KEY = "search";
    private static final String REPLACE_STR_KEY = "replace";
    private static final String TARGET_PACKEGE_KEY = "targetPackage";

    private String targetPackage;
    private String search;
    private String replace;

    public AdditionXmlFilePlugin() {
        super();

    }

    @Override
    public boolean validate(List<String> warnings) {
        search = this.properties.getProperty(SEARCH_STR_KEY);
        replace = this.properties.getProperty(REPLACE_STR_KEY);
        targetPackage = this.properties.getProperty(TARGET_PACKEGE_KEY);
        return true;
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> generatedXmlFiles = introspectedTable.getGeneratedXmlFiles();
        if (CollectionUtils.isEmpty(generatedXmlFiles))
            return super.contextGenerateAdditionalXmlFiles(introspectedTable);
        List<GeneratedXmlFile> additionXmlFiles = new ArrayList<>();
        for (GeneratedXmlFile generatedXmlFile : generatedXmlFiles) {
            Document document = new Document(
                    XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                    XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);

            XmlElement root = new XmlElement("mapper");
            root.addAttribute(new Attribute("namespace", introspectedTable.getMyBatis3SqlMapNamespace()));
            document.setRootElement(root);
            GeneratedXmlFile gxf = new GeneratedXmlFile(
                    document,
                    generatedXmlFile.getFileName().replaceAll(search, replace),
                    targetPackage,
                    generatedXmlFile.getTargetProject(),
                    false, context.getXmlFormatter());
            additionXmlFiles.add(gxf);
        }

        return additionXmlFiles;
    }

}
