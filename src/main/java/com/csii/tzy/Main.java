package com.csii.tzy;

import com.csii.tzy.database.*;
import com.csii.tzy.utils.DBMetadataUtils;
import com.csii.tzy.utils.FtUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception{

         SimpleDataSource dataSource=new SimpleDataSource(
                Dialect.ORACLE,
                "jdbc:oracle:thin:@//localhost:1521/orcl",
                "scott",
                "scott"
        );
        DBMetadataUtils dbMetadataUtils =new DBMetadataUtils(dataSource);
        DatabaseConfig config = new DatabaseConfig("orcl","scott");
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);


        //表集合
        List<Map<String, Object>> tableList = new ArrayList<>();
        for (IntrospectedTable table : list) {
            Map<String, Object> tb = new HashMap<>();
            System.out.println("表名称："+table.getName()+"  表注释："+table.getRemarks()+"  表类型："+table.getType());
            if(table.getType()!=null){
                tb.put("type",table.getType());
            }
            else
                tb.put("type","没有？");
            //表名称
            tb.put("name", table.getName());
            //表备注
            if (table.getRemarks()!=null){
                tb.put("remarks",table.getRemarks()+"。");
            }else
                tb.put("remarks"," ");
            //表主键
            String primaryKey="";
            for (IntrospectedColumn column : table.getPrimaryKeyColumns()) {
                primaryKey+=column.getName()+" ";
            }
            tb.put("primaryKey",primaryKey);
            //表外键
            String foreignKey="";
            for (IntrospectedColumn column : table.getForeignKeyColumns()) {
                primaryKey+=column.getName()+" ";
            }
            tb.put("foreignKey",foreignKey);
            //表索引
            String inFo="";
            for (IndexInFo column : table.getInFoColumns()) {
                inFo+=column.getColumnName()+" ";
            }
            tb.put("inFo",inFo);

            //字段集合
            List<Map<String, Object>> columnList = new ArrayList<>();
            for (IntrospectedColumn column : table.getAllColumns()) {
                System.out.println("字段注释"+column.getRemarks());
                //字段
                Map<String, Object> field = new HashMap<>();
                field.put("name", column.getName());

                if (column.getRemarks()!=null){
                    field.put("remarks",column.getRemarks());
                }else
                    field.put("remarks","");
                field.put("jdbcTypeName", column.getJdbcTypeName());
                field.put("length",column.getLength());
                if (column.isNullable()){
                    field.put("nullable","Y");
                }else {
                    field.put("nullable","N");
                }
                if (column.getDefaultValue()!=null){
                    field.put("default",""+column.getDefaultValue());
                }else
                    field.put("default","");
                columnList.add(field);
            }
            tb.put("COLUMNS", columnList);
            tableList.add(tb);
        }
        Map<String,Object> map =new HashMap<>();
        map.put("table", tableList);
        FtUtil ftUtil = new FtUtil();
        ftUtil.generateFile("/", "demo.xml", map, "D:/", "scott.doc");
    }
}