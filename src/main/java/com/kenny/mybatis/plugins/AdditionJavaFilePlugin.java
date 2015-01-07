package com.kenny.mybatis.plugins;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kehui on 2015/1/6.
 * 添加额外的java文件
 * 文件为空, 并继承相应名称的mbg生成的mapper,项目中需要手写的一些方法,放在这些文件里面, 不要改MBG开头的文件
 */
public class AdditionJavaFilePlugin extends PluginAdapter {

    private static final String SEARCH_STR_KEY = "search";
    private static final String REPLACE_STR_KEY = "replace";
    private static final String TARGET_PACKEGE_KEY = "targetPackage";

    private String search;
    private String replace;
    private String targetPackage;

    public AdditionJavaFilePlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        search = this.properties.getProperty(SEARCH_STR_KEY);//查找文件名称的内容, 可为regex
        replace = this.properties.getProperty(REPLACE_STR_KEY);//文件名称中替换后的内容
        targetPackage = this.properties.getProperty(TARGET_PACKEGE_KEY);//将该文件置于该目录下
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> additionJavaFiles = new ArrayList<>();
        List<GeneratedJavaFile> generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
        for (GeneratedJavaFile generatedJavaFile : generatedJavaFiles){
            //只修改mapper
            if (!generatedJavaFile.getCompilationUnit().isJavaInterface())
                continue;
            //文件的描述信息
            CompilationUnit originalCompilationUnit = generatedJavaFile.getCompilationUnit();
            String javaFileName = generatedJavaFile.getFileName().replaceAll(search, replace).replaceAll("\\..*", "");

            try {
                //如果类不能被初始化, 则说明没有该文件,需要生成, 如果有该文件, 则直接跳过不生成
                Class.forName(targetPackage + "." + javaFileName);
                continue;
            } catch (ClassNotFoundException e) {
            }
            //文件的全称(包括包名), 用来初始化文件的类型
            String fullJavaFileName = targetPackage + "." + javaFileName;
            //初始化一个mapper, 设置文件内容
            Interface additionalJavaFileCompilationUnit = new Interface(fullJavaFileName);
            additionalJavaFileCompilationUnit.addSuperInterface(originalCompilationUnit.getType());
            additionalJavaFileCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
            additionalJavaFileCompilationUnit.setStatic(false);
            additionalJavaFileCompilationUnit.addImportedType(originalCompilationUnit.getType());
            JavaFormatter formatter = new DefaultJavaFormatter();
            formatter.setContext(this.context);
            //将这个mapper的信息初始化为一个java文件
            GeneratedJavaFile child = new GeneratedJavaFile(
                    additionalJavaFileCompilationUnit,
                    generatedJavaFile.getTargetProject(),
                    generatedJavaFile.getFileEncoding(),
                    formatter);

            additionJavaFiles.add(child);

        }
        return additionJavaFiles;
    }
}
