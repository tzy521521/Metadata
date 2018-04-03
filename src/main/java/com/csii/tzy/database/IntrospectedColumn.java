package com.csii.tzy.database;

import java.sql.Types;

public class IntrospectedColumn extends IntrospectedBase {
    //字段
    protected int jdbcType;
    //字段类型名（数据库中的类型）
    protected String jdbcTypeName;
    //字段是否为空
    protected boolean nullable;
    //是否为主键
    protected boolean pk;
    //字段长度
    protected int length;
    //字段小数部分位数
    protected int scale;
    //
    protected String javaProperty;
    //
    protected FullyQualifiedJavaType fullyQualifiedJavaType;
    //是否是表中的特定？？字段。
    protected boolean isColumnNameDelimited;
    //字段所在的表
    protected IntrospectedTable introspectedTable;
    //
    private String type;
    //字段默认值
    protected String defaultValue;

    public IntrospectedColumn() {
        super();
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJdbcTypeName() {
        if (jdbcTypeName == null) {
            return "OTHER";
        }

        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public boolean isPk() {
        return pk;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getJavaProperty() {
        return javaProperty;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public FullyQualifiedJavaType getFullyQualifiedJavaType() {
        return fullyQualifiedJavaType;
    }

    public void setFullyQualifiedJavaType(FullyQualifiedJavaType fullyQualifiedJavaType) {
        this.fullyQualifiedJavaType = fullyQualifiedJavaType;
    }

    public boolean isColumnNameDelimited() {
        return isColumnNameDelimited;
    }

    public void setColumnNameDelimited(boolean isColumnNameDelimited) {
        this.isColumnNameDelimited = isColumnNameDelimited;
    }

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    //
    public boolean isBLOBColumn() {
        String typeName = getJdbcTypeName();

        return "BINARY".equals(typeName) || "BLOB".equals(typeName)  //$NON-NLS-2$
                || "CLOB".equals(typeName) || "LONGNVARCHAR".equals(typeName)  //$NON-NLS-2$
                || "LONGVARBINARY".equals(typeName) || "LONGVARCHAR".equals(typeName)  //$NON-NLS-2$
                || "NCLOB".equals(typeName) || "VARBINARY".equals(typeName);  //$NON-NLS-2$
    }

    //
    public boolean isStringColumn() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType
                .getStringInstance());
    }

    //
    public boolean isJdbcCharacterColumn() {
        return jdbcType == Types.CHAR || jdbcType == Types.CLOB
                || jdbcType == Types.LONGVARCHAR || jdbcType == Types.VARCHAR
                || jdbcType == Types.LONGNVARCHAR || jdbcType == Types.NCHAR
                || jdbcType == Types.NCLOB || jdbcType == Types.NVARCHAR;
    }

    //
    public boolean isJDBCDateColumn() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType
                .getDateInstance())
                && "DATE".equalsIgnoreCase(jdbcTypeName);
    }

    //
    public boolean isJDBCTimeColumn() {
        return fullyQualifiedJavaType.equals(FullyQualifiedJavaType
                .getDateInstance())
                && "TIME".equalsIgnoreCase(jdbcTypeName);
    }

    public String getTableName() {
        return getIntrospectedTable().getName();
    }

    @Override
    public String toString() {
        return "IntrospectedColumn{" +
                "name='" + name + '\'' +
                ", jdbcType=" + jdbcType +
                ", jdbcTypeName='" + jdbcTypeName + '\'' +
                ", nullable=" + nullable +
                ", length=" + length +
                ", scale=" + scale +
                ", javaProperty='" + javaProperty + '\'' +
                ", fullyQualifiedJavaType=" + fullyQualifiedJavaType +
                ", isColumnNameDelimited=" + isColumnNameDelimited +
                ", introspectedTable=" + introspectedTable +
                ", remarks='" + remarks + '\'' +
                ", type='" + type + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}