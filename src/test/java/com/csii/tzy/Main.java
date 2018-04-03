package com.csii.tzy;


import com.csii.tzy.database.*;
import com.csii.tzy.utils.DBMetadataUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{

         SimpleDataSource dataSource=new SimpleDataSource(
                Dialect.MYSQL,
                "jdbc:mysql://localhost:3306/test?useSSL=true",
                "root",
                "843460950"
        );
        //System.out.println(Dialect.MYSQL);
        /**
         * 创建完DBMetadataUtils之后，断开数据库连接。
         * 仅仅初始化了dataSource、letterCase、dialect、introspector(DatabaseIntrospector)、catalogs和schemas。
         * connection和databaseMetaData设置为了空？
         * 这样做的目的？？？？
         */
        DBMetadataUtils dbMetadataUtils =new DBMetadataUtils(dataSource);

        DatabaseConfig config = new DatabaseConfig("test","");
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);
        for (IntrospectedTable table : list) {
            System.out.println("表名称："+table.getName()+" 表备注："+table.getRemarks()+"表的类型"+table.getType());
            System.out.println(table.getPrimaryKeyColumns());
            System.out.println(table.getForeignKeyColumns());
            System.out.println(table.getInFoColumns());
            System.out.println("字段名称~~"+"字段注释~~"+"~~字段类型");
            for (IntrospectedColumn column : table.getAllColumns()) {
                System.out.println(column.getName() + " - "
                        +column.getRemarks()+ " - "
                        +column.getJdbcTypeName()+ " - "
                );
            }
            System.out.println();
        }
    }
}
