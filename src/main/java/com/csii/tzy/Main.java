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
        DatabaseConfig config = new DatabaseConfig("","scott");
        long startTime = System.currentTimeMillis();
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);
        long endTime = System.currentTimeMillis();
        System.out.println("得到所需要的表的时间"+ (endTime - startTime)/1000 + "s");
        dbMetadataUtils.sortTables(list);

        FtUtil ftUtil = new FtUtil();
        Map<String,Object> map =getFtUtilMap(list);
        ftUtil.generateFile("/freemaker",
                "数据字典.xml", map,
                "E:\\IdeaProjects\\dbMetadata\\src\\main\\resources\\files",
                "eip_autotest-6.5_数据字典.doc"
        );
    }

    private static Map<String,Object> getFtUtilMap(List<IntrospectedTable> list) throws Exception{
        //表集合
        List<Map<String, Object>> tableList = new ArrayList<>();
        //不同类型的表的数量和包含的字段数量
        int tableCount=0;                       int columnCount=0;
        int versionTableCount=0;                int versionColumnCount=0;
        int actTableCount=0;                    int actColumnCount=0;
        //不同类型的表，没有字段注释表数量和字段数量
        int noColumnRemarkTableCount=0;         int noColumnRemarkCountAll=0;
        int versionNoColumnRemarkTableCount=0;  int versionNoColumnRemarkCountAll=0;
        int actNoColumnRemarkTableCount=0;      int actNoColumnRemarkCountAll=0;
        //不同类型的表，没有表注释的表数量
        int noTableRemarkCount=0;
        int versionNoTableRemarkCount=0;
        int actNoTableRemarkCount=0;
        //特定类型字段的的字段数量
        int blobCountAll=0;
        int actBlobCountAll=0;
        int versionBlobCountAll=0;
        //
        for (IntrospectedTable table : list) {
            Map<String, Object> tb = new HashMap<>();
            System.out.println("表名称："+table.getName()+"  表注释："+table.getRemarks()+"  表类型："+table.getType());
            //表类型
            if(table.getType()!=null){
                tb.put("type",table.getType());
            } else{
                tb.put("type","");
            }
            //表名称
            tb.put("name", table.getName());
            //不同类型的表---总数统计
            tableCount++;
            if (table.getName().contains("ACT_")||table.getName().contains("act_")){
                actTableCount++;
            }
            if (table.getName().contains("_version")){
                versionTableCount++;
            }
            //表备注
            if (table.getRemarks()!=null){
                tb.put("remarks",table.getRemarks());
                if (table.getRemarks().replaceAll(" ","").equals("")){
                    //不同类型的表---没有表备注的表数量统计
                    noTableRemarkCount++;
                    if (table.getName().contains("ACT_")||table.getName().contains("act_")){
                        actNoTableRemarkCount++;
                    }
                    if (table.getName().contains("_version")){
                        versionNoTableRemarkCount++;
                    }
                }
            }else{
                tb.put("remarks","");
                //不同类型的表---没有表备注的表数量统计
                noTableRemarkCount++;
                if (table.getName().contains("ACT_")||table.getName().contains("act_")){
                    actNoTableRemarkCount++;
                }
                if (table.getName().contains("_version")){
                    versionNoTableRemarkCount++;
                }
            }
            //表主键
            String primaryKey="";
            for (IntrospectedColumn column : table.getPrimaryKeyColumns()) {
                primaryKey+=column.getName()+" ";
            }
            tb.put("primaryKey",primaryKey);
            //表外键
            String foreignKey="";
            for (IntrospectedColumn column : table.getForeignKeyColumns()) {
                foreignKey+=column.getName()+" ";
            }
            tb.put("foreignKey",foreignKey);
            //表索引
            String inFo="";
            for (IndexInFo column : table.getInFoColumns()) {
                inFo+=column.getColumnName()+" ";
            }
            tb.put("inFo",inFo);
            //表中字段
            List<Map<String, Object>> columnList = new ArrayList<>();
            //表中特定字段的类型的字段集合。
            //字段类型是blob的字段集合
            String blob="";   int blobCount=0;
            //表中字段个数
            int columnCountTemp=0;
            //表中没有字段注释的字段个数
            int noColumnRemarkCount=0;
            //表中没有字段描述的字段集合
            String noColumnRemarks="";
            //统计缺少字段注释的表的标记
            boolean noColumnRemarkTableCountFlag=false;
            boolean versionNoColumnRemarkTableCountFlag=false;
            boolean actNoColumnRemarkTableCountFlag=false;
            for (IntrospectedColumn column : table.getAllColumns()) {
                System.out.println(column.getJdbcTypeName()+"---"+column.getSqlTypes());
                columnCountTemp++;
                Map<String, Object> field = new HashMap<>();
                //字段名称
                field.put("name", column.getName());
                //字段注释
                if (column.getRemarks()!=null){
                    if (column.getRemarks().replaceAll(" ","").equals("")){
                        noColumnRemarks+=column.getName()+",  ";
                        noColumnRemarkCount++;
                        noColumnRemarkTableCountFlag=true;
                        if (table.getName().contains("ACT_")||table.getName().contains("act_")){
                            actNoColumnRemarkTableCountFlag=true;
                        }
                        if (table.getName().contains("_version")){
                            versionNoColumnRemarkTableCountFlag=true;
                        }
                    }

                    field.put("remarks",column.getRemarks());
                }else{
                    field.put("remarks","");
                    noColumnRemarks+=column.getName()+",  ";
                    noColumnRemarkCount++;
                    noColumnRemarkTableCountFlag=true;
                    if (table.getName().contains("ACT_")||table.getName().contains("act_")){
                        actNoColumnRemarkTableCountFlag=true;
                    }
                    if (table.getName().contains("_version")){
                        versionNoColumnRemarkTableCountFlag=true;
                    }
                }
                //字段类型-------------------------------------
                //field.put("type", column.getJdbcTypeName());
                field.put("type", column.getSqlTypes());
                if (field.get("type").equals("BLOB")){
                    blob+=column.getName()+", ";
                    blobCount++;
                }
                //字段长度
                if (field.get("type").toString().equals("NUMBER")){
                    String s="("+column.getLength()+","+column.getScale()+")";
                    field.put("length",s);
                }else if (field.get("type").toString().contains("TIMESTAMP")){
                    field.put("length",column.getScale());
                }else {
                    field.put("length",column.getLength());
                }
                //字段是否为空
                if (column.isNullable()){
                    field.put("nullable","Y");
                }else {
                    field.put("nullable","N");
                }
                //字段默认值
                if (column.getDefaultValue()!=null){
                    field.put("default",""+column.getDefaultValue());
                }else
                    field.put("default","");
                columnList.add(field);
            }
            System.out.println();
            System.out.println();

            if (noColumnRemarkTableCountFlag)
                noColumnRemarkTableCount++;
            if (actNoColumnRemarkTableCountFlag)
                actNoColumnRemarkTableCount++;
            if (versionNoColumnRemarkTableCountFlag)
                versionNoColumnRemarkTableCount++;
            //表中字段的集合
            tb.put("COLUMNS", columnList);
            //表中没有字段描述的字段集合
            tb.put("noColumnRemarks",noColumnRemarks);
            //表中没有字段描述的字段数量
            tb.put("noColumnRemarkCount",noColumnRemarkCount);
            //表中字段总数
            tb.put("columnCountTemp",columnCountTemp);
            //表中特定字段类型的字段集合
            tb.put("blob",blob);  tb.put("blobCount",blobCount);
            //不同类型的表的字段数量
            columnCount+=columnCountTemp;
            if (table.getName().contains("ACT_")||table.getName().contains("act_")){
                actColumnCount+=columnCountTemp;
            }
            if (table.getName().contains("_version")){
                versionColumnCount+=columnCountTemp;
            }
            //不同类型的表的没有字段注释的字段数量
            noColumnRemarkCountAll+=noColumnRemarkCount;
            if (table.getName().contains("ACT_")||table.getName().contains("act_")){
                actNoColumnRemarkCountAll+=noColumnRemarkCount;
            }
            if (table.getName().contains("_version")){
                versionNoColumnRemarkCountAll+=noColumnRemarkCount;
            }
            //不同类型的表的含有特定类型的字段的总数量。
            blobCountAll+=blobCount;
            if (table.getName().contains("ACT_")){
                actBlobCountAll+=blobCount;
            }
            if (table.getName().contains("_version")){
                versionBlobCountAll+=blobCount;
            }

            tableList.add(tb);
//            //缺少字段注释或者没有表注释的表集合
//                if(noColumnRemarks.length()>1||table.getRemarks()==null){
//                    tableList.add(tb);
//                }
//            if(!table.getName().contains("_version")){
//                tableList.add(tb);
//            }
//            //去除掉工作流表和版本控制表的含有特定特性的字段的表统计结果。
//            if (!table.getName().contains("ACT_")&&!table.getName().contains("_version")&&blob.length()>1){
//                tableList.add(tb);
//           }
        }

        Map<String,Object> map =new HashMap<>();
        map.put("table", tableList);
        //不同类型的表---没有表描述的表的数量
        map.put("noTableRemarkCount",noTableRemarkCount);
        map.put("versionNoTableRemarkCount",versionNoTableRemarkCount);
        map.put("actNoTableRemarkCount",actNoTableRemarkCount);
        //不同类型的表---没有字段描述字段的数量
        map.put("noColumnRemarkCount",noColumnRemarkCountAll);
        map.put("versionNoColumnRemarkCount",versionNoColumnRemarkCountAll);
        map.put("actNoColumnRemarkCount",actNoColumnRemarkCountAll);
        //不同类型的表---缺少字段描述的表的数量
        map.put("noColumnRemarkTableCount",noColumnRemarkTableCount);
        map.put("versionNoColumnRemarkTableCount",versionNoColumnRemarkTableCount);
        map.put("actNoColumnRemarkTableCount",actNoColumnRemarkTableCount);
        //不同类型的表---表的数量
        map.put("tableCount",tableCount);
        map.put("versionTableCount",versionTableCount);
        map.put("actTableCount",actTableCount);
        //不同类型的表---字段的数量
        map.put("columnCount",columnCount);
        map.put("versionColumnCount",versionColumnCount);
        map.put("actColumnCount",actColumnCount);
        //不同类型的表的含有特定类型的字段的总数量。
        map.put("blobCountAll",blobCountAll);
        map.put("versionBlobCountAll",versionBlobCountAll);
        map.put("actBlobCountAll",actBlobCountAll);
        //数据库中的其他信息描述--数据库名或者schema
        map.put("user","eip_autotest");
        return map;
    }
}