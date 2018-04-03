package com.csii.tzy.utils;

import com.csii.tzy.database.*;
import com.csii.tzy.database.introspector.DatabaseIntrospector;
import com.csii.tzy.database.introspector.OracleIntrospector;
import com.csii.tzy.database.introspector.PGIntrospector;
import com.csii.tzy.database.introspector.SqlServerIntrospector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 数据库元数据操作
 * @author lipu@csii.com.cn
 */
public class DBMetadataUtils {
    //数据源
    private SimpleDataSource dataSource;
    //数据库连接
    private Connection connection;
    //获取数据库--将未用双引号引起来的大小写混合的 SQL 标识符存储的形式---mysql中用小写
    private LetterCase letterCase;
    //数据库驱动
    private Dialect dialect;
    //Java内省机制IntroSpector,????mybatis
    private DatabaseIntrospector introspector;
    //数据库元数据
    private DatabaseMetaData databaseMetaData;
    //数据库列表
    private List<String> catalogs;
    //获取此数据库用作类别和表名之间的分隔符的 String----新增....
    private String catalogSeparator;
    //schema列表
    private List<String> schemas;
    //数据库中所用的表类型----新增....
    private List<String> tableTypes;

    public DBMetadataUtils(SimpleDataSource dataSource) {
        this(dataSource, false, true);
    }

    public DBMetadataUtils(SimpleDataSource dataSource, boolean forceBigDecimals, boolean useCamelCase) {
        if (dataSource == null) {
            throw new NullPointerException("Argument dataSource can't be null!");
        }
        this.dataSource = dataSource;
        this.dialect = dataSource.getDialect();
        try {
            initLetterCase();
            this.introspector = getDatabaseIntrospector(forceBigDecimals, useCamelCase);
            this.catalogs = introspector.getCatalogs();
            this.schemas = introspector.getSchemas();
            this.tableTypes= introspector.getTableTypes();
            this.catalogSeparator=introspector.getCatalogSeparator();
            //断开链接。。。。。。
            closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 私有--在创建完DBMetadataUtils之后关闭链接数据库链接
     * 将connection和databaseMetaData设置为空,有什么用处。
     */
    private void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
                this.databaseMetaData = null;
            } catch (SQLException e) {
            }
        }
    }

    public boolean testConnection() {
        try {
            if (!getConnection().isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return false;
    }

    public Connection getConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        try {
            this.connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public LetterCase getLetterCase() {
        return letterCase;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public DatabaseIntrospector getIntrospector() {
        return introspector;
    }

    private DatabaseIntrospector getDatabaseIntrospector(boolean forceBigDecimals, boolean useCamelCase) {
        switch (dataSource.getDialect()) {
            case ORACLE:
                return new OracleIntrospector(this, forceBigDecimals, useCamelCase);
            case POSTGRESQL:
                return new PGIntrospector(this, forceBigDecimals, useCamelCase);
            case SQLSERVER:
                return new SqlServerIntrospector(this, forceBigDecimals, useCamelCase);
            case DB2:
            case HSQLDB:
            case MARIADB:
            case MYSQL:
            default:
                return new DatabaseIntrospector(this, forceBigDecimals, useCamelCase);
        }
    }

    public DatabaseMetaData getDatabaseMetaData() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            if (databaseMetaData != null) {
                return databaseMetaData;
            } else {
                databaseMetaData = connection.getMetaData();
                return databaseMetaData;
            }
        } else {
            databaseMetaData = getConnection().getMetaData();
            return databaseMetaData;
        }
    }

    public List<String> getCatalogs() throws SQLException {
        return catalogs;
    }

    public String getCatalogSeparator() {
        return catalogSeparator;
    }

    public List<String> getSchemas() throws SQLException {
        return schemas;
    }

    public List<String> getTableTypes() throws SQLException{
        return tableTypes;
    }

    public DatabaseConfig getDefaultConfig() throws SQLException {
        DatabaseConfig config = null;
        if (catalogs.size() == 1) {
            if (schemas.size() == 1) {
                config = new DatabaseConfig(catalogs.get(0), schemas.get(0));
            } else if (schemas.size() == 0) {
                config = new DatabaseConfig(catalogs.get(0), null);
            }
        } else if (catalogs.size() == 0) {
            if (schemas.size() == 1) {
                config = new DatabaseConfig(null, schemas.get(0));
            } else if (schemas.size() == 0) {
                config = new DatabaseConfig(null, null);
            }
        }
        if (config == null) {
            //switch (getDialect()) {
            switch (this.dialect){
                case DB2:
                case ORACLE:
                    config = new DatabaseConfig(null, dataSource.getUser());
                    break;
                case MYSQL:
                    if (schemas.size() > 0) {
                        break;
                    }
                    //String url = dataSource.getUrl();
                    String url = this.dialect.getSample();
                    if (url.indexOf('/') > 0) {
                        String dbName = url.substring(url.lastIndexOf('/') + 1);
                        for (String catalog : catalogs) {
                            if (dbName.equalsIgnoreCase(catalog)) {
                                config = new DatabaseConfig(catalog, null);
                                break;
                            }
                        }
                    }
                    break;
                case SQLSERVER:
                    String sqlserverUrl = dataSource.getUrl();
                    String sqlserverCatalog = null;
                    if (sqlserverUrl.indexOf('/') > 0) {
                        String dbName = sqlserverUrl.substring(sqlserverUrl.lastIndexOf('/') + 1);
                        for (String catalog : catalogs) {
                            if (dbName.equalsIgnoreCase(catalog)) {
                                sqlserverCatalog = catalog;
                                break;
                            }
                        }
                        if (sqlserverCatalog != null) {
                            for (String schema : schemas) {
                                if ("dbo".equalsIgnoreCase(schema)) {
                                    config = new DatabaseConfig(sqlserverCatalog, "dbo");
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case POSTGRESQL:
                    String postgreUrl = dataSource.getUrl();
                    String postgreCatalog = null;
                    if (postgreUrl.indexOf('/') > 0) {
                        String dbName = postgreUrl.substring(postgreUrl.lastIndexOf('/') + 1);
                        for (String catalog : catalogs) {
                            if (dbName.equalsIgnoreCase(catalog)) {
                                postgreCatalog = catalog;
                                break;
                            }
                        }
                        if (postgreCatalog != null) {
                            for (String schema : schemas) {
                                if ("public".equalsIgnoreCase(schema)) {
                                    config = new DatabaseConfig(postgreCatalog, "public");
                                    break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return config;
    }

    /**
     * 根据数据库查询配置，获得数据库中表的和表中字段的信息。
     * @param config
     * @return
     * @throws SQLException
     */
    public List<IntrospectedTable> introspectTables(DatabaseConfig config) throws SQLException {
        try {
            return introspector.introspectTables(config);
        } finally {
            closeConnection();
        }
    }

    public static void sortTables(List<IntrospectedTable> tables) {
        if (StringUtils.isNotEmpty(tables)) {
            Collections.sort(tables, new Comparator<IntrospectedTable>() {
                public int compare(IntrospectedTable o1, IntrospectedTable o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        }
    }

    public static void sortColumns(List<IntrospectedColumn> columns) {
        if (StringUtils.isNotEmpty(columns)) {
            Collections.sort(columns, new Comparator<IntrospectedColumn>() {
                public int compare(IntrospectedColumn o1, IntrospectedColumn o2) {
                    int result = o1.getTableName().compareTo(o2.getTableName());
                    if (result == 0) {
                        result = o1.getName().compareTo(o2.getName());
                    }
                    return result;
                }
            });
        }
    }

    private enum LetterCase {
        UPPER, LOWER, NORMAL
    }

    private void initLetterCase() {
        try {
            DatabaseMetaData databaseMetaData = getConnection().getMetaData();
            if (databaseMetaData.storesLowerCaseIdentifiers()) {
                this.letterCase = LetterCase.LOWER;
            } else if (databaseMetaData.storesUpperCaseIdentifiers()) {
                this.letterCase = LetterCase.UPPER;
            } else {
                this.letterCase = LetterCase.NORMAL;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertLetterByCase(String value) {
        if (value == null) {
            return null;
        }
        switch (letterCase) {
            case UPPER:
                return value.toUpperCase();
            case LOWER:
                return value.toLowerCase();
            default:
                return value;
        }
    }

    @Override
    public String toString() {
        return "DBMetadataUtils{" +
                "dataSource=" + dataSource +
                ", connection=" + connection +
                ", letterCase=" + letterCase +
                ", dialect=" + dialect +
                ", introspector=" + introspector +
                ", databaseMetaData=" + databaseMetaData +
                ", catalogs=" + catalogs +
                ", schemas=" + schemas +
                ", tableTypes=" + tableTypes +
                ", catalogSeparator='" + catalogSeparator + '\'' +
                '}';
    }
}