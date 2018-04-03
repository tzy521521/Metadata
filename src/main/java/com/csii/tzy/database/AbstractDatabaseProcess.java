package com.csii.tzy.database;

import java.util.List;

/**
 * 暂时没啥用
 * 内省数据库时的处理接口
 */
public abstract class AbstractDatabaseProcess implements DatabaseProcess {
    public void processStart() {

    }

    public void processColumn(IntrospectedTable table, IntrospectedColumn column) {

    }

    public void processTable(IntrospectedTable table) {

    }

    public void processComplete(List<IntrospectedTable> introspectedTables) {

    }
}