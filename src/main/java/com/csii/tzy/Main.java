package com.csii.tzy;

import com.csii.tzy.database.*;
import com.csii.tzy.utils.DBMetadataUtils;
import com.csii.tzy.utils.FtUtil;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception{

        //jdbc:oracle:thin:@//115.182.90.203:15217/xe jdbc:oracle:thin:@//localhost:1521/orcl
        //user:eip_study   pwd:eip_study scott scott
         SimpleDataSource dataSource=new SimpleDataSource(
                Dialect.ORACLE,
                "jdbc:oracle:thin:@//localhost:1521/orcl",
                "scott",
                "scott"
        );
        DBMetadataUtils dbMetadataUtils =new DBMetadataUtils(dataSource);
        DatabaseConfig config = new DatabaseConfig("orcl","scott");
        List<IntrospectedTable> list = dbMetadataUtils.introspectTables(config);

        FtUtil ftUtil = new FtUtil();
        ftUtil.generateFile("/", "demo.xml", list, "D:/", "demo.doc");
    }
}