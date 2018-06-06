package com.csii.tzy.database;

/**
 * @author lipu@csii.com.cn
 */
public class JdbcTypeInformation {
    private String jdbcTypeName;
    private FullyQualifiedJavaType fullyQualifiedJavaType;

    public JdbcTypeInformation(String jdbcTypeName, FullyQualifiedJavaType fullyQualifiedJavaType) {
        this.jdbcTypeName = jdbcTypeName;
        this.fullyQualifiedJavaType = fullyQualifiedJavaType;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public FullyQualifiedJavaType getFullyQualifiedJavaType() {
        return fullyQualifiedJavaType;
    }
}
