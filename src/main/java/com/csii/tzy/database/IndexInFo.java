package com.csii.tzy.database;

public class IndexInFo {
    protected String columnName;
    protected String name;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IndexInFo{" +
                "columnName='" + columnName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}