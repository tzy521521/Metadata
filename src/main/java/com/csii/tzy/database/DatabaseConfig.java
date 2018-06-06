package com.csii.tzy.database;

import java.util.Arrays;

/**
 * 数据库查询配置
 * @author lipu@csii.com.cn
 */
public class DatabaseConfig {
    //要查询的数据库
    private String catalog;
    //要查询的匹配的schema
    private String schemaPattern;
    //要查询的表名称
    private String tableNamePattern;
    //增加查询特定字段名称的功能。
    private String columnNamePattern;
    //增加查询数据表的的类型功能
    private String[] types;

    public DatabaseConfig() {
        this(null, null);
    }

    public DatabaseConfig(String catalog, String schemaPattern) {
        this(catalog, schemaPattern, "%");
    }

    public DatabaseConfig(String catalog, String schemaPattern,String tableNamePattern) {
        this(catalog, schemaPattern, "%",null);
    }

    public DatabaseConfig(String catalog, String schemaPattern,String[] types) {
        this(catalog, schemaPattern, "%",null,types);
    }

    public DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern,String columnNamePattern) {
        this(catalog, schemaPattern, "%",null,null);
    }

    public DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern,String[] types) {
        this.catalog = catalog;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
        this.columnNamePattern = columnNamePattern;
        this.types = types;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchemaPattern() {
        return schemaPattern;
    }

    public void setSchemaPattern(String schemaPattern) {
        this.schemaPattern = schemaPattern;
    }

    public String getTableNamePattern() {
        return tableNamePattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public String getColumnNamePattern() {
        return columnNamePattern;
    }

    public void setColumnNamePattern(String columnNamePattern) {
        this.columnNamePattern = columnNamePattern;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }
    //
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseConfig that = (DatabaseConfig) o;

        if (catalog != null ? !catalog.equals(that.catalog) : that.catalog != null) return false;
        if (schemaPattern != null ? !schemaPattern.equals(that.schemaPattern) : that.schemaPattern != null)
            return false;
        if (tableNamePattern != null ? !tableNamePattern.equals(that.tableNamePattern) : that.tableNamePattern != null)
            return false;

        return true;
    }
    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + (schemaPattern != null ? schemaPattern.hashCode() : 0);
        result = 31 * result + (tableNamePattern != null ? tableNamePattern.hashCode() : 0);
        result = 31 * result + (columnNamePattern != null ? columnNamePattern.hashCode() : 0);
        result = 31 * result + (types !=null ? Arrays.hashCode(types) : 0);
        return result;
    }
    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "catalog='" + catalog + '\'' +
                ", schemaPattern='" + schemaPattern + '\'' +
                ", tableNamePattern='" + tableNamePattern + '\'' +
                ", types=" + Arrays.toString(types) +
                '}';
    }
}