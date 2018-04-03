package com.csii.tzy.database;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 数据源
 */
public final class SimpleDataSource implements DataSource {
    private Dialect dialect;
    private String url;
    private String user;
    private String pwd;
    private DataSource delegate;

    public SimpleDataSource(Dialect dialect, DataSource dataSource) {
        this.dialect = dialect;
        this.delegate = dataSource;
        try {
            Class.forName(dialect.getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到指定的数据库驱动:" + dialect.getDriverClass());
        }
    }

    public SimpleDataSource(Dialect dialect, String url, String user, String pwd) {
        this.dialect = dialect;
        this.url = url;
        this.user = user;
        this.pwd = pwd;
        this.delegate = this;
        try {
            Class.forName(dialect.getDriverClass());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到指定的数据库驱动:" + dialect.getDriverClass());
        }
    }

    public Dialect getDialect() {
        return dialect;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public Connection getConnection() throws SQLException {
        if (delegate instanceof SimpleDataSource) {
            //如果是mysql那么需要设置里可以返回mysql的备注信息,应该只是一部分。
            if (this.dialect.equals(Dialect.MYSQL)){
                Properties props =new Properties();
                props.setProperty("user", user);
                props.setProperty("password", pwd);
                props.setProperty("remarks", "true"); //设置可以获取remarks信息
                props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
                return DriverManager.getConnection(url,props);
            }
            return DriverManager.getConnection(url, user, pwd);
        } else {
            return delegate.getConnection();
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        if (delegate instanceof SimpleDataSource) {
            return DriverManager.getConnection(url, username, password);
        } else {
            return delegate.getConnection(username, password);
        }
    }

    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    public void setLoginTimeout(int seconds) throws SQLException {

    }

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }
    
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
