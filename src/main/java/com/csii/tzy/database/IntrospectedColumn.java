package com.csii.tzy.database;

import java.sql.Types;

public class IntrospectedColumn extends IntrospectedBase {

    //字段所在的表的名称
    protected String tableName;
    //是否为主键
    protected boolean pk;
    //java.sql.Types 的 SQL 类型编号
    protected int jdbcType;
    //java.sql.Types 的 SQL 类型
    protected String sqlTypes ;
    //
    protected String jdbcTypeName;
    //字段是否为空
    protected boolean nullable;
    //字段长度
    protected int length;
    //字段小数部分位数
    protected int scale;
    //字段默认值
    protected String defaultValue;
    //
    protected String javaProperty;
    //
    protected FullyQualifiedJavaType fullyQualifiedJavaType;
    //
    protected boolean isColumnNameDelimited;
//    //
//    protected IntrospectedTable introspectedTable;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isPk() {
        return pk;
    }

    public void setPk(boolean pk) {
        this.pk = pk;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getSqlTypes() {
        return sqlTypes;
    }

    public void setSqlTypes(String sqlTypes) {
        this.sqlTypes = sqlTypes;
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

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
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

    public void setColumnNameDelimited(boolean columnNameDelimited) {
        isColumnNameDelimited = columnNameDelimited;
    }

//    public IntrospectedTable getIntrospectedTable() {
//        return introspectedTable;
//    }
//
//    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
//        this.introspectedTable = introspectedTable;
//    }

    //~~~~~
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

    @Override
    public String toString() {
        return "IntrospectedColumn{" +
                "tableName='" + tableName + '\'' +
                ", name='" + name + '\'' +
                ", remarks='" + remarks + '\'' +
                ", pk=" + pk +
                ", jdbcType=" + jdbcType +
                ", jdbcTypeName='" + jdbcTypeName + '\'' +
                ", type='" + sqlTypes + '\'' +
                ", nullable=" + nullable +
                ", length=" + length +
                ", defaultValue='" + defaultValue + '\'' +
                ", scale=" + scale +
                ", javaProperty='" + javaProperty + '\'' +
                ", fullyQualifiedJavaType=" + fullyQualifiedJavaType +
                ", isColumnNameDelimited=" + isColumnNameDelimited +
                '}';
    }
}