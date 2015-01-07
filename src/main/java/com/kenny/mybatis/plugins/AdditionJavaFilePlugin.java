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
        search = this.properties.getProperty(SEARCH_STR_KEY);
        replace = this.properties.getProperty(REPLACE_STR_KEY);
        targetPackage = this.properties.getProperty(TARGET_PACKEGE_KEY);
        return true;
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        List<GeneratedJavaFile> additionJavaFiles = new ArrayList<>();
        List<GeneratedJavaFile> generatedJavaFiles = introspectedTable.getGeneratedJavaFiles();
        for (GeneratedJavaFile generatedJavaFile : generatedJavaFiles){
            if (!generatedJavaFile.getCompilationUnit().isJavaInterface())
                continue;

            CompilationUnit originalCompilationUnit = generatedJavaFile.getCompilationUnit();
            String javaFileName = generatedJavaFile.getFileName().replaceAll(search, replace).replaceAll("\\..*", "");
            String fullJavaFileName = targetPackage + "." + javaFileName;
            Interface additionalJavaFileCompilationUnit = new Interface(fullJavaFileName);
            additionalJavaFileCompilationUnit.addSuperInterface(originalCompilationUnit.getType());
            additionalJavaFileCompilationUnit.setVisibility(JavaVisibility.PUBLIC);
            additionalJavaFileCompilationUnit.setStatic(false);
            additionalJavaFileCompilationUnit.addImportedType(originalCompilationUnit.getType());
            JavaFormatter formatter = new DefaultJavaFormatter();
            formatter.setContext(this.context);

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
