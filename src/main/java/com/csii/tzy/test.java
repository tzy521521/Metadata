package com.csii.tzy;
import com.csii.tzy.database.*;
import com.csii.tzy.utils.DBMetadataUtils;
import com.csii.tzy.utils.FtUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) throws Exception{
        SimpleDataSource dataSource=new SimpleDataSource(
                Dialect.ORACLE,"jdbc:oracle:thin:@//115.182.90.203:15217/xe",
                "eip_study",
                "eip_study"
        );
        String [] types=new String[]{"table"};
        DBMetadataUtils dbMetadataUtils =new DBMetadataUtils(dataSource);
        System.out.println("Catalog:"+dbMetadataUtils.getCatalogs());
        System.out.println("Schema"+dbMetadataUtils.getSchemas());
        DatabaseConfig config = new DatabaseConfig(
                "xe",
                "BO_DEV",
                "%",
                types
        );
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);
        System.out.println("BO_DEV中数据库表个数："+list.size());


        //表集合
        List<Map<String, Object>> tableList = new ArrayList<>();

        for (IntrospectedTable table : list) {
            System.out.println("表名称："+table.getName()+"  表注释："+table.getRemarks()+" 空？"+"  表类型："+table.getType());
            //表
            Map<String, Object> tb = new HashMap<>();
            if(table.getType()!=null){
                tb.put("type",table.getType());
            }
            else
                tb.put("type","没有？");
            //表名称
            tb.put("name", table.getName());
            //表备注
            if (table.getRemarks()!=null){
                tb.put("remarks",table.getRemarks());
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
            System.out.println(table.getInFoColumns());
            String inFo="";
            for (IndexInFo column : table.getInFoColumns()) {
                inFo+=column.getColumnName()+" ";
            }
            tb.put("inFo",inFo);


            //字段集合
            List<Map<String, Object>> columnList = new ArrayList<>();
            for (IntrospectedColumn column : table.getAllColumns()) {
                System.out.println("字段注释"+column.getRemarks()+" 空？");
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
                if (column.getDefaultValue()==null){
                    field.put("default","");
                }else
                    field.put("default",""+column.getDefaultValue());
                columnList.add(field);
            }

            tb.put("COLUMNS", columnList);
            tableList.add(tb);
        }
        Map<String,Object> map =new HashMap<>();
        map.put("table", tableList);

        /**
         * FtUtil ftUtil = new FtUtil();
         ftUtil.generateFile("/", "demo.xml", map, "D:/", "demo.doc");
         */

        // step1 创建freeMarker配置实例
        Configuration configuration = new Configuration();
        Writer out = null;
        try {
            // step2 获取模版路径
            configuration.setDirectoryForTemplateLoading(new File("E:\\IdeaProjects\\Metadata\\src\\main\\resource\\freemaker"));
            // step4 加载模版文件
            Template template = configuration.getTemplate("demo.xml");
            // step5 生成数据
            File docFile = new File("D:\\BO_DEV.doc");
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            // step6 输出文件
            template.process(map, out);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^AutoCodeDemo.java 文件创建成功 !");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.flush();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}