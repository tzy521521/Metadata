package com.csii.tzy.database;

import java.util.Arrays;

/**
 * 数据库查询配置
 * @author tzy
 */
public class DatabaseConfig {
    //要查询的数据库
    private String catalog;
    //要查询的匹配的shema.
    private String schemaPattern;
    //要查询的表名称
    private String tableNamePattern;
    //增加数据表的的类型功能~~~~~~~~~~~~~~~~~~~~,但是查询字段的时候不是这个参数。
    private String[] types;
    //暂时没啥用？
    private DatabaseProcess databaseProcess;

    public DatabaseConfig() {
        this(null, null);
    }

    public DatabaseConfig(String catalog, String schemaPattern) {
        this(catalog, schemaPattern, "%");
    }

    public DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern) {
        this.catalog = catalog;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
    }

    public DatabaseConfig(String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        this.catalog = catalog;
        this.schemaPattern = schemaPattern;
        this.tableNamePattern = tableNamePattern;
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

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public boolean hasProcess() {
        return databaseProcess != null;
    }

    public DatabaseProcess getDatabaseProcess() {
        return databaseProcess;
    }

    public void setDatabaseProcess(DatabaseProcess databaseProcess) {
        this.databaseProcess = databaseProcess;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "catalog='" + catalog + '\'' +
                ", schemaPattern='" + schemaPattern + '\'' +
                ", tableNamePattern='" + tableNamePattern + '\'' +
                ", types=" + Arrays.toString(types) +
                ", databaseProcess=" + databaseProcess +
                '}';
    }

    //加入查询类型是否要修改？
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

    //加入查询类型是否要修改？
    @Override
    public int hashCode() {
        int result = catalog != null ? catalog.hashCode() : 0;
        result = 31 * result + (schemaPattern != null ? schemaPattern.hashCode() : 0);
        result = 31 * result + (tableNamePattern != null ? tableNamePattern.hashCode() : 0);
        return result;
    }
}