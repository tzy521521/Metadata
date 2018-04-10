package com.csii.tzy;

import com.csii.tzy.database.*;
import com.csii.tzy.utils.DBMetadataUtils;
import com.csii.tzy.utils.FtUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{

         SimpleDataSource dataSource=new SimpleDataSource(
                Dialect.ORACLE,
                "jdbc:oracle:thin:@//localhost:1521/orcl",
                "scott",
                "scott"
        );
        /**
         * 创建完DBMetadataUtils之后，断开数据库连接。
         * 仅仅初始化了dataSource、letterCase、dialect、introspector(DatabaseIntrospector)、catalogs和schemas。
         * connection和databaseMetaData设置为了空？
         * 这样做的目的？？？？
         * 应该是如果没有清空databaseMetaData，在源代码里还继续使用databaseMetaData，那么万一再创建了这个databaseMetaData
         * 之后，数据库的信息修改了，之前赋值的databaseMetaData就没有意义。
         */
        DBMetadataUtils dbMetadataUtils =new DBMetadataUtils(dataSource);
        DatabaseConfig config = new DatabaseConfig("orcl","scott");
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);

        FtUtil ftUtil = new FtUtil();
        ftUtil.generateFile("/", "demo.xml", list, "D:/", "scott.doc");

    }
}