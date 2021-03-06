package com.csii.tzy.database;

/**
 * 数据库 - 驱动和连接示例
 * @author lipu@csii.com.cn
 */
public enum Dialect {
    ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@//localhost:1521/orcl"),
    MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test"),
    DB2("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://localhost:50000/SAMPLE"),
    HSQLDB("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:sample"),
    MARIADB("org.mariadb.jdbc.Driver", "jdbc:mariadb://localhost:3306/sample"),
    POSTGRESQL("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/sample"),
    SQLSERVER("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://localhost:1433/tempdb");

    private String clazz;
    private String sample;

    private Dialect(String clazz, String sample) {
        this.clazz = clazz;
        this.sample = sample;
    }

    public String getSample() {
        return sample;
    }

    public String getDriverClass() {
        return clazz;
    }

    /**
     * 驱动是否存在
     * @return
     */
    public boolean exists() {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}