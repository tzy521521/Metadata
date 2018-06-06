package com.csii.tzy;

import com.csii.tzy.database.*;
import com.csii.tzy.utils.DBMetadataUtils;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class zhushi {
    public static void main(String[] args) throws Exception {
        SimpleDataSource oracle_source = new SimpleDataSource(
                Dialect.ORACLE,
                "jdbc:oracle:thin:@//115.182.90.203:15217/xe",
                "zhengli",
                "zhengli"
        );
        DBMetadataUtils oracle_dbMetadataUtils = new DBMetadataUtils(oracle_source);
        DatabaseConfig oracle_config = new DatabaseConfig("", "zhengli");
        List<IntrospectedTable> oracle_list = oracle_dbMetadataUtils.introspectTables(oracle_config);
        oracle_dbMetadataUtils.sortTables(oracle_list);
        HashMap<String, HashMap<String, String>> zhushi = getZhushi(oracle_list);


        System.out.println("开始处mysql~~~~~~");
        SimpleDataSource mysql_dataSource = new SimpleDataSource(
                Dialect.MYSQL,
                "jdbc:mysql://115.182.90.203:33067/eip_autotest",
                "eibs",
                "eibs"
        );

        DBMetadataUtils mysql_dbMetadataUtils = new DBMetadataUtils(mysql_dataSource);
        DatabaseConfig mysql_config = new DatabaseConfig("eip_autotest", "");
        List<IntrospectedTable> mysql_list = mysql_dbMetadataUtils.introspectTables(mysql_config);
        mysql_dbMetadataUtils.sortTables(mysql_list);


        File Output_cPoular = new File("./根据eip_zhengli生成的mysql.sql");
        try (
                PrintWriter printWriter = new PrintWriter(Output_cPoular)

        ) {
            for (IntrospectedTable table : mysql_list) {
                HashMap<String, String> temp = zhushi.get(table.getName());
                if (temp!=null){
                    printWriter.println("alter table "+"`"+table.getName()+"`"+" comment "+"'"+temp.get(table.getName())+"';");
                    for (IntrospectedColumn column : table.getAllColumns()) {
                        String s = "";
                        //字段长度
                        if (column.getSqlTypes().equals("VARCHAR")) {
                            s = "(" + column.getLength() + ")";
                        }

                        if (table.getName().contains("_version")) {
                            printWriter.println("alter table " +"`"+table.getName()+"`"+ " modify column " +"`"+ column.getName()+"`" + " " + column.getSqlTypes() + s + " comment '" + temp.get(column.getName()) + "';");
                        } else {
                            printWriter.println("alter table " +"`"+table.getName()+"`"+ " modify column " +"`"+ column.getName() +"`"+ " " + column.getSqlTypes() + s + " comment '" + temp.get(column.getName().toUpperCase()) + "';");
                            System.out.println(s);
                        }
                    }
                    printWriter.println();
                    printWriter.println();
                }
            }
        }
    }

    private static HashMap<String, HashMap<String, String>> getZhushi(List<IntrospectedTable> list) {
        HashMap<String, HashMap<String, String>> oracle = new HashMap<>();
        for (IntrospectedTable table : list) {
            System.out.println("表名称：" + table.getName() + "  表注释：" + table.getRemarks() + "  表类型：" + table.getType());
            if (table.getName().contains("_version")) {
                HashMap<String, String> biao=new HashMap<>();
                biao.put(table.getName(),table.getRemarks());
                oracle.put(table.getName(), biao);
            } else {
                HashMap<String, String> biao=new HashMap<>();
                biao.put(table.getName().toLowerCase(),table.getRemarks());
                oracle.put(table.getName().toLowerCase(), biao);
            }
            for (IntrospectedColumn column : table.getAllColumns()) {
                if (table.getName().contains("_version")) {
                    HashMap<String, String> temp = oracle.get(table.getName());
                    temp.put(column.getName(), column.getRemarks());
                    oracle.put(table.getName(), temp);
                } else {
                    HashMap<String, String> temp = oracle.get(table.getName().toLowerCase());
                    temp.put(column.getName(), column.getRemarks());
                    oracle.put(table.getName().toLowerCase(), temp);
                }
            }
        }
        return oracle;
    }
}