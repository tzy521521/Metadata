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
        /**
         * 创建完DBMetadataUtils之后，断开数据库连接。
         * 仅仅初始化了dataSource、letterCase、dialect、introspector(DatabaseIntrospector)、catalogs和schemas。
         * connection和databaseMetaData设置为了空？
         * 这样做的目的？？？？
         * 应该是如果没有清空databaseMetaData，再源代码里还继续使用databaseMetaData，那么万一再创建了这个databaseMetaData
         * 之后，数据库的信息修改了，之前赋值的databaseMetaData就没有意义。
         */
        DBMetadataUtils dbMetadataUtils =new DBMetadataUtils(dataSource);
        /*
        System.out.println(dbMetadataUtils.testConnection());
        System.out.println(dbMetadataUtils.getConnection());
        System.out.println(dbMetadataUtils.getLetterCase());
        System.out.println(dbMetadataUtils.getDialect());
        System.out.println(dbMetadataUtils.getIntrospector());
        System.out.println(dbMetadataUtils.getDatabaseMetaData());
        System.out.println(dbMetadataUtils.getCatalogs());
        System.out.println(dbMetadataUtils.getCatalogSeparator());
        System.out.println(dbMetadataUtils.getSchemas());
        System.out.println(dbMetadataUtils.getTableTypes());
        System.out.println(dbMetadataUtils.getDefaultConfig());
         */
        DatabaseConfig config = new DatabaseConfig("test","");
        System.out.println(config);
        /*
        这样也是可以的。。。
        DatabaseIntrospector databaseIntrospector=dbMetadataUtils.getIntrospector();
        DatabaseIntrospector databaseIntrospector=new DatabaseIntrospector(dbMetadataUtils);
        List<IntrospectedTable> list = databaseIntrospector.introspectTables(config);
         */
         List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);
         for (IntrospectedTable table : list) {
         System.out.println("表名称："+table.getName()+" 表备注："+table.getRemarks()+" 表的类型："+table.getType());
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