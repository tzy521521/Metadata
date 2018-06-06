package com.csii.tzy.database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IntrospectedTable extends IntrospectedBase {
    //表所在的数据库
    private String catalog;
    //表所在的schema
    private String schema;
    //增加表的类型
    private String type;
    //表的主键信息
    protected List<IntrospectedColumn> primaryKeyColumns;
    //表的外键信息
    protected List<IntrospectedColumn> foreignKeyColumns;
    //表的索引信息
    protected List<IndexInFo> inFoColumns;
    //表的所有字段的信息
    protected List<IntrospectedColumn> baseColumns;

    public IntrospectedTable() {
        super();
        primaryKeyColumns = new ArrayList<>();
        foreignKeyColumns = new ArrayList<>();
        inFoColumns = new ArrayList<>();
        baseColumns = new ArrayList<>();
    }

    public IntrospectedTable(String catalog, String schema, String name) {
        this();
        this.catalog = catalog;
        this.schema = schema;
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<IntrospectedColumn> getPrimaryKeyColumns() {
        return primaryKeyColumns;
    }

    public boolean hasPrimaryKeyColumns() {
        return primaryKeyColumns.size() > 0;
    }

    public List<IntrospectedColumn> getForeignKeyColumns() {
        return foreignKeyColumns;
    }

    public boolean hasForeignKeyColumns(List<IntrospectedColumn> foreignKeyColumns) {
        return primaryKeyColumns.size() >0;
    }

    public List<IndexInFo> getInFoColumns() {
        return inFoColumns;
    }

    public boolean hasInFoColumns(List<IntrospectedColumn> inFoColumns) {
        return inFoColumns.size() > 0;
    }

    public List<IntrospectedColumn> getBaseColumns() {
        return baseColumns;
    }

    public boolean hasBaseColumns() {
        return baseColumns.size() > 0;
    }

    public List<IntrospectedColumn> getAllColumns() {
        return baseColumns;
    }

    public boolean hasAnyColumns() {
        return baseColumns.size() > 0;
    }

    //添加字段-----
    public void addColumn(IntrospectedColumn introspectedColumn) {
        baseColumns.add(introspectedColumn);
//        introspectedColumn.setIntrospectedTable(this);
    }

    //添加主键列表
    public void addPrimaryKeyColumn(String columnName) {
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getName().equals(columnName)) {
                introspectedColumn.setPk(true);
                primaryKeyColumns.add(introspectedColumn);
                break;
            }
        }
    }

    //添加外键列表
    public void addForeignKeyColumn(String columnName) {
        Iterator<IntrospectedColumn> iter = baseColumns.iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            if (introspectedColumn.getName().equals(columnName)) {
                foreignKeyColumns.add(introspectedColumn);
                break;
            }
        }
    }

    //添加索引
    public void addInFoKeyColumn(IndexInFo indexInFo){
        inFoColumns.add(indexInFo);
    }

    //获得表中某个字段的信息
    public IntrospectedColumn getColumn(String columnName) {
        if (columnName == null) {
            return null;
        } else {
            for (IntrospectedColumn introspectedColumn : baseColumns) {
                if (introspectedColumn.isColumnNameDelimited()) {
                    if (introspectedColumn.getName().equals(columnName)) {
                        return introspectedColumn;
                    }
                } else {
                    if (introspectedColumn.getName().equalsIgnoreCase(columnName)) {
                        return introspectedColumn;
                    }
                }
            }
            return null;
        }
    }
    //
    public boolean hasJDBCTimeColumns() {
        boolean rc = false;
        for (IntrospectedColumn introspectedColumn : baseColumns) {
            if (introspectedColumn.isJDBCTimeColumn()) {
                rc = true;
                break;
            }
        }
        return rc;
    }
    //
    public boolean hasJDBCDateColumns() {
        boolean rc = false;
        for (IntrospectedColumn introspectedColumn : baseColumns) {
            if (introspectedColumn.isJDBCDateColumn()) {
                rc = true;
                break;
            }
        }
        return rc;
    }


    //增加了表的类型，需不需要修改
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntrospectedTable)) return false;

        IntrospectedTable that = (IntrospectedTable) o;

        if (catalog != null ? !catalog.equals(that.catalog) : that.catalog != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (catalog != null ? catalog.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IntrospectedTable{" +
                "catalog='" + catalog + '\'' +
                ", schema='" + schema + '\'' +
                ", name='" + name + '\'' +
                ", remarks='" + remarks + '\'' +
                ", type='" + type + '\'' +
                ", primaryKeyColumns=" + primaryKeyColumns +
                ", foreignKeyColumns=" + foreignKeyColumns +
                ", inFoColumns=" + inFoColumns +
                ", baseColumns=" + baseColumns +
                '}';
    }
}