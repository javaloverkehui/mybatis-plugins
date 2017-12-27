package com.kenny.mybatis.plugins;

import org.apache.commons.collections.CollectionUtils;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 * 添加额外的xml文件
 * 文件为空, 项目中需要手写的一些sql实现,放在这些文件里面, 不要改MBG开头的文件
 */
public class AdditionXmlFilePlugin extends PluginAdapter {
    private static final String SEARCH_STR_KEY = "search";
    private static final String REPLACE_STR_KEY = "replace";
    private static final String TARGET_PACKEGE_KEY = "targetPackage";
    private static final String NAME_SPACE_SEARCH_KEY = "nameSpaceSearch";
    private static final String NAME_SPACE_REPLACE_KEY = "nameSpaceReplace";

    private String targetPackage;
    private String search;
    private String replace;
    private String nameSpaceReplace;
    private String nameSpaceSearch;

    public AdditionXmlFilePlugin() {
        super();

    }

    @Override
    public boolean validate(List<String> warnings) {
        search = this.properties.getProperty(SEARCH_STR_KEY);//查找名字内容
        replace = this.properties.getProperty(REPLACE_STR_KEY);//替换后的名字内容
        targetPackage = this.properties.getProperty(TARGET_PACKEGE_KEY);//目录, 以resource下面一级开始
        nameSpaceSearch = this.properties.getProperty(NAME_SPACE_SEARCH_KEY);//查找xml中的命名空间的内容
        nameSpaceReplace = this.properties.getProperty(NAME_SPACE_REPLACE_KEY);//替换xml命名空间后的内容
        return true;
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        List<GeneratedXmlFile> generatedXmlFiles = introspectedTable.getGeneratedXmlFiles();
        if (CollectionUtils.isEmpty(generatedXmlFiles)) {

            return super.contextGenerateAdditionalXmlFiles(introspectedTable);
        }
        List<GeneratedXmlFile> additionXmlFiles = new ArrayList<>();
        for (GeneratedXmlFile generatedXmlFile : generatedXmlFiles) {
            //新的文件名称
            String newFileName = generatedXmlFile.getFileName().replaceAll(search, replace);
            //如果该文件已生成则无需生成, 否则生成
            if (null != getClass().getResource("/" + targetPackage + "/" + newFileName))
                continue;
            //xml头信息
            Document document = new Document(
                    XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                    XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
            //顶级节点
            XmlElement root = new XmlElement("mapper");
            //命名空间
            root.addAttribute(new Attribute("namespace",
                    introspectedTable.getMyBatis3SqlMapNamespace()
                            .replaceAll(search, replace)
                            .replaceAll(nameSpaceSearch, nameSpaceReplace)));
            root.addElement(new TextElement("<!--")); //$NON-NLS-1$

            /*注释*/
            StringBuilder sb = new StringBuilder();
            sb.append(" mbg mapper's namespace = "); //$NON-NLS-1$
            sb.append(introspectedTable.getMyBatis3SqlMapNamespace());
            root.addElement(new TextElement(sb.toString()));
            root.addElement(new TextElement("-->")); //$NON-NLS-1$
            /*注释结束*/
            document.setRootElement(root);
            //初始化一个xml文件
            GeneratedXmlFile gxf = new GeneratedXmlFile(
                    document,
                    newFileName,
                    targetPackage,
                    generatedXmlFile.getTargetProject(),
                    false, context.getXmlFormatter());
            additionXmlFiles.add(gxf);
        }

        return additionXmlFiles;
    }



}
