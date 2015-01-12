package com.kenny.mybatis.api;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * Created by kehui on 2015/1/9.
 */
public class MyCommentGenerator extends DefaultCommentGenerator implements CommentGenerator {
    private Properties properties;
    private boolean suppressDate;  
    private boolean suppressAllComments;

    public MyCommentGenerator() {
        super();
        properties = new Properties();
        suppressDate = false;
        suppressAllComments = false;
    }

    public void addJavaFileComment(CompilationUnit compilationUnit) {
        if (suppressAllComments) {
            return;
        }
        compilationUnit.addFileCommentLine("/**");
        compilationUnit.addFileCommentLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
        compilationUnit.addFileCommentLine(" * @author kehui");
        compilationUnit.addFileCommentLine(" * " + getDateString());
        compilationUnit.addFileCommentLine(" */");
    }

    /**
     * Adds a suitable comment to warn users that the element was generated, and
     * when it was generated.
     */
    public void addComment(XmlElement xmlElement) {
        if (suppressAllComments) {
            return;
        }

        xmlElement.addElement(new TextElement("<!--")); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append("  WARNING - "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        xmlElement.addElement(new TextElement(sb.toString()));
        xmlElement.addElement(new TextElement("-->")); //$NON-NLS-1$
    }

    public void addRootComment(XmlElement rootElement) {
        if (suppressAllComments) {
            return;
        }

        rootElement.addElement(new TextElement("<!--")); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        sb.append("  WARNING - "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        sb.append(" 以下是由mybatis-generator自动生成,请勿修改! " + getDateString());
        rootElement.addElement(new TextElement(sb.toString()));
        rootElement.addElement(new TextElement("-->")); //$NON-NLS-1$
    }

    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));

        suppressAllComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
    }

    /**
     * This method adds the custom javadoc tag for. You may do nothing if you do
     * not wish to include the Javadoc tag - however, if you do not include the
     * Javadoc tag then the Java merge capability of the eclipse plugin will
     * break.
     *
     * @param javaElement
     *            the java element
     */
    protected void addJavadocTag(JavaElement javaElement,
                                 boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(" * "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" 请勿修改"); //$NON-NLS-1$
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    protected void addOnlyTag(JavaElement javaElement,
                                 boolean markAsDoNotDelete) {
        StringBuilder sb = new StringBuilder();
        sb.append("/* "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" 请勿修改"); //$NON-NLS-1$
        }
        sb.append(" */");
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * This method returns a formated date string to include in the Javadoc tag
     * and XML comments. You may return null if you do not want the date in
     * these documentation elements.
     *
     * @return a string representing the current timestamp, or null
     */
    protected String getDateString() {
        if (suppressDate) {
            return null;
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        }
    }

    public void addClassComment(InnerClass innerClass,
                                IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        innerClass.addJavaDocLine("/**"); //$NON-NLS-1$
        sb.append(" * 对应数据库表 "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());

        addJavadocTag(innerClass, false);

        innerClass.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addEnumComment(InnerEnum innerEnum,
                               IntrospectedTable introspectedTable) {
        super.addEnumComment(innerEnum, introspectedTable);
    }

    public void addFieldComment(Field field,
                                IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        field.addJavaDocLine("/**"); //$NON-NLS-1$
        sb.append(" * " + introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        field.addJavaDocLine(sb.toString());

        field.addJavaDocLine(" * 字段描述：" + introspectedColumn.getRemarks());
        field.addJavaDocLine(" * " + introspectedColumn.getJdbcTypeName() + "(" + introspectedColumn.getLength() + ")");
        addJavadocTag(field, false);
        field.addJavaDocLine(" */"); //$NON-NLS-1$
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        addOnlyTag(field, false);
    }

    public void addGeneralMethodComment(Method method,
                                        IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

        addOnlyTag(method, false);
    }

    public void addGetterComment(Method method,
                                 IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        addOnlyTag(method, false);
    }

    public void addSetterComment(Method method,
                                 IntrospectedTable introspectedTable,
                                 IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

        addOnlyTag(method, false);
    }

    public void addClassComment(InnerClass innerClass,
                                IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }

        addOnlyTag(innerClass, false);
    }
}
