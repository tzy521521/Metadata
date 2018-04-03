package com.csii.tzy;

import com.github.abel533.database.*;
import com.github.abel533.utils.DBMetadataUtils;
import sun.security.util.Length;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception{
        SimpleDataSource dataSource=new SimpleDataSource(Dialect.MYSQL,"jdbc:mysql://localhost:3306/test?useSSL=true","root","843460950");
        DBMetadataUtils dbMetadataUtils =new DBMetadataUtils(dataSource);
        DatabaseConfig config = new DatabaseConfig();
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);

        //表集合
        List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();

        for (IntrospectedTable table : list) {
            //表
            Map<String, Object> tb = new HashMap<String, Object>();

            System.out.println(table.getName() + ":");

            //表名称
            tb.put("NAME", table.getName());

            //字段集合
            List<Map<String, Object>> columnList = new ArrayList<Map<String, Object>>();

            for (IntrospectedColumn column : table.getAllColumns()) {
                //字段
                Map<String, Object> field = new HashMap<String, Object>();

                System.out.println(column.getName() + " - " + column.getJdbcTypeName());

                field.put("NAME", column.getName());
                field.put("TYPE", column.getJdbcTypeName());
                field.put("Length",column.getLength());
                if (column.isPk()){
                    field.put("isPK","TRUE");
                }else {
                    field.put("isPK","FALSE");
                }

                columnList.add(field);
            }


            tb.put("COLUMNS", columnList);

            tableList.add(tb);
        }

        Map<String,Object> map =new HashMap<String, Object>();
        map.put("table", tableList);
        FtUtil ftUtil = new FtUtil();
        ftUtil.generateFile("/", "1.xml", map, "D:/", "1_trs.doc");
    }
}
