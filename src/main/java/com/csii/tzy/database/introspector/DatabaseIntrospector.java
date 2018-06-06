package com.csii.tzy.database.introspector;

import com.csii.tzy.database.*;
import com.csii.tzy.utils.DBMetadataUtils;
import com.csii.tzy.utils.JavaBeansUtil;

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

/**
 * 数据库查询配置
 * @author lipu@csii.com.cn
 */
public class DatabaseIntrospector {

    protected static final Map<Integer, JdbcTypeInformation> typeMap = new HashMap<>();
    static {
        //java.sql.Type中的有39个,Types.DECIMAL:Types.NUMERIC:另做处理
        typeMap.put(Types.ARRAY, new JdbcTypeInformation("ARRAY",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.BIGINT, new JdbcTypeInformation("BIGINT",
                new FullyQualifiedJavaType(Long.class.getName())));
        typeMap.put(Types.BINARY, new JdbcTypeInformation("BINARY",
                new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.BIT, new JdbcTypeInformation("BIT",
                new FullyQualifiedJavaType(Boolean.class.getName())));
        typeMap.put(Types.BLOB, new JdbcTypeInformation("BLOB",
                new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.BOOLEAN, new JdbcTypeInformation("BOOLEAN",
                new FullyQualifiedJavaType(Boolean.class.getName())));
        typeMap.put(Types.CHAR, new JdbcTypeInformation("CHAR",
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.CLOB, new JdbcTypeInformation("CLOB",
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.DATALINK, new JdbcTypeInformation("DATALINK",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.DATE, new JdbcTypeInformation("DATE",
                new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.DISTINCT, new JdbcTypeInformation("DISTINCT",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.DOUBLE, new JdbcTypeInformation("DOUBLE",
                new FullyQualifiedJavaType(Double.class.getName())));
        typeMap.put(Types.FLOAT, new JdbcTypeInformation("FLOAT",
                new FullyQualifiedJavaType(Double.class.getName())));
        typeMap.put(Types.INTEGER, new JdbcTypeInformation("INTEGER",
                new FullyQualifiedJavaType(Integer.class.getName())));
        typeMap.put(Types.JAVA_OBJECT, new JdbcTypeInformation("JAVA_OBJECT",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.LONGNVARCHAR, new JdbcTypeInformation("LONGNVARCHAR",
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.LONGVARBINARY, new JdbcTypeInformation("LONGVARBINARY",
                new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.LONGVARCHAR, new JdbcTypeInformation("LONGVARCHAR",
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NCHAR, new JdbcTypeInformation("NCHAR",
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NCLOB, new JdbcTypeInformation("NCLOB",
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NVARCHAR, new JdbcTypeInformation("NVARCHAR",
                new FullyQualifiedJavaType(String.class.getName())));
        typeMap.put(Types.NULL, new JdbcTypeInformation("NULL",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.OTHER, new JdbcTypeInformation("OTHER",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.REAL, new JdbcTypeInformation("REAL",
                new FullyQualifiedJavaType(Float.class.getName())));
        typeMap.put(Types.REF, new JdbcTypeInformation("REF",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.SMALLINT, new JdbcTypeInformation("SMALLINT",
                new FullyQualifiedJavaType(Short.class.getName())));
        typeMap.put(Types.STRUCT, new JdbcTypeInformation("STRUCT",
                new FullyQualifiedJavaType(Object.class.getName())));
        typeMap.put(Types.TIME, new JdbcTypeInformation("TIME",
                new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP",
                new FullyQualifiedJavaType(Date.class.getName())));
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT",
                new FullyQualifiedJavaType(Byte.class.getName())));
        typeMap.put(Types.VARBINARY, new JdbcTypeInformation("VARBINARY",
                new FullyQualifiedJavaType("byte[]")));
        typeMap.put(Types.VARCHAR, new JdbcTypeInformation("VARCHAR",
                new FullyQualifiedJavaType(String.class.getName())));
    }

    protected DBMetadataUtils dbMetadataUtils;
    protected boolean forceBigDecimals;
    protected boolean useCamelCase;

    public DatabaseIntrospector(DBMetadataUtils dbMetadataUtils) {
        this(dbMetadataUtils, false, true);
    }

    public DatabaseIntrospector(DBMetadataUtils dbMetadataUtils, boolean forceBigDecimals, boolean useCamelCase) {
        this.dbMetadataUtils = dbMetadataUtils;
        this.forceBigDecimals = forceBigDecimals;
        this.useCamelCase = useCamelCase;
    }

    public LetterCase getLetterCase() throws SQLException {
        DatabaseMetaData databaseMetaData = dbMetadataUtils.getDatabaseMetaData();
        if (databaseMetaData.storesLowerCaseIdentifiers()) {
            return LetterCase.LOWER;
        } else if (databaseMetaData.storesUpperCaseIdentifiers()) {
            return LetterCase.UPPER;
        } else {
            return LetterCase.NORMAL;
        }
    }

    public List<String> getCatalogs() throws SQLException {
        ResultSet rs = dbMetadataUtils.getDatabaseMetaData().getCatalogs();
        List<String> catalogs = new ArrayList<>();
        while (rs.next()) {
            catalogs.add(rs.getString("TABLE_CAT"));
        }
        closeResultSet(rs);
        return catalogs;
    }

    public List<String> getSchemas() throws SQLException {
        ResultSet rs = dbMetadataUtils.getDatabaseMetaData().getSchemas();
        List<String> schemas = new ArrayList<>();
        while (rs.next()) {
            //返回的结果中还有一个属性-TABLE_CATALOG String => 类别名称（可为 null）
            schemas.add(rs.getString("TABLE_SCHEM"));
        }
        closeResultSet(rs);
        return schemas;
    }

    public List<String> getTableTypes() throws SQLException{
        ResultSet rs = dbMetadataUtils.getDatabaseMetaData().getTableTypes();
        List<String> tableType = new ArrayList<>();
        while (rs.next()) {
            tableType.add(rs.getString("TABLE_TYPE"));
        }
        closeResultSet(rs);
        return tableType;
    }
    /**
     * 获取表信息
     * @param config
     * @return List<IntrospectedTable>
     * @throws SQLException
     */
    public List<IntrospectedTable> introspectTables(DatabaseConfig config) throws SQLException {
        List<IntrospectedTable> introspectedTables = null;
        DatabaseConfig localConfig = getLocalDatabaseConfig(config);

        //处理完字段的时间
        long startTime = System.currentTimeMillis();
        Map<IntrospectedTable, List<IntrospectedColumn>> columns = getColumns(localConfig);
        long endTime = System.currentTimeMillis();
        System.out.println("处理完字段运行时间:" + (endTime - startTime)/1000 + "s");

        if (columns.isEmpty()) {
            introspectedTables = new ArrayList<>(0);
        } else {
            //处理表的运行时间
            long startTime1 = System.currentTimeMillis();
            introspectedTables = calculateIntrospectedTables(localConfig, columns);
            long endTime1 = System.currentTimeMillis();
            System.out.println("处理完所有的表的运行时间:" + (endTime1 - startTime1)/1000 + "s");

            Iterator<IntrospectedTable> iter = introspectedTables.iterator();
            while (iter.hasNext()) {
                IntrospectedTable introspectedTable = iter.next();
                //去掉没有字段的表
                if (!introspectedTable.hasAnyColumns()) {
                    iter.remove();
                }
            }
        }
        return introspectedTables;
    }
    /**
     * 根据数据库转换配置
     * 数据库--将未用双引号引起来的大小写混合的 SQL 标识符存储的形式---mysql中用小写
     * @param config
     * @return DatabaseConfig
     * @throws SQLException
     */
    protected DatabaseConfig getLocalDatabaseConfig(DatabaseConfig config) throws SQLException {
        String localCatalog;
        String localSchema;
        String localTableName;
        String localColumnName;
        String[] types;
        if (dbMetadataUtils.getDatabaseMetaData().storesLowerCaseIdentifiers()) {
            localCatalog = config.getCatalog() == null ? null : config.getCatalog().toLowerCase();
            localSchema = config.getSchemaPattern() == null ? null : config.getSchemaPattern().toLowerCase();
            localTableName = config.getTableNamePattern() == null ? null : config.getTableNamePattern().toLowerCase();
            localColumnName = config.getColumnNamePattern() == null ? null : config.getColumnNamePattern().toLowerCase();
        } else if (dbMetadataUtils.getDatabaseMetaData().storesUpperCaseIdentifiers()) {
            localCatalog = config.getCatalog() == null ? null : config.getCatalog().toUpperCase();
            localSchema = config.getSchemaPattern() == null ? null : config.getSchemaPattern().toUpperCase();
            localTableName = config.getTableNamePattern() == null ? null : config.getTableNamePattern().toUpperCase();
            localColumnName = config.getColumnNamePattern() == null ? null : config.getColumnNamePattern().toUpperCase();
        } else {
            localCatalog = config.getCatalog();
            localSchema = config.getSchemaPattern();
            localTableName = config.getTableNamePattern();
            localColumnName = config.getColumnNamePattern();
        }
        types=config.getTypes();
        return new DatabaseConfig(localCatalog, localSchema, localTableName,localColumnName,types);
    }
    /**
     * 获取全部的表和字段--------获得表和字段的对应关系（Map）
     * @param config
     * @return Map<IntrospectedTable, List<IntrospectedColumn>>
     * @throws SQLException
     */
    protected Map<IntrospectedTable, List<IntrospectedColumn>> getColumns(DatabaseConfig config) throws SQLException {
        Map<IntrospectedTable, List<IntrospectedColumn>> answer = new HashMap<>();
        /**
         * 在这里修改获得的字段的属性详情。
         */
        ResultSet rs = dbMetadataUtils.getDatabaseMetaData().getColumns(
                config.getCatalog(),
                config.getSchemaPattern(),
                config.getTableNamePattern(),
                config.getColumnNamePattern()
        );
        long i=1L;
        while (rs.next()) {
            IntrospectedColumn column = new IntrospectedColumn();
            column.setTableName(rs.getString("TABLE_NAME"));
            column.setName(rs.getString("COLUMN_NAME"));
            column.setRemarks(rs.getString("REMARKS"));
            column.setJdbcType(rs.getInt("DATA_TYPE"));
            column.setSqlTypes(rs.getString("TYPE_NAME"));
            column.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
            column.setLength(rs.getInt("COLUMN_SIZE"));
            column.setScale(rs.getInt("DECIMAL_DIGITS"));
            column.setDefaultValue(rs.getString("COLUMN_DEF"));
            //处理每一个字段都会生成一个临时的表对象。
            IntrospectedTable table = new IntrospectedTable(
                    /**
                     * 根据字段的获得所在的表的一些信息
                     */
                    rs.getString("TABLE_CAT"),
                    rs.getString("TABLE_SCHEM"),
                    rs.getString("TABLE_NAME")
            );
            List<IntrospectedColumn> columns = answer.get(table);
            if (columns == null) {
                columns = new ArrayList<>();
                answer.put(table, columns);
            }
            columns.add(column);
            //处理了几个字段
            System.out.println(i++);
        }
        closeResultSet(rs);
        return answer;
    }
    /**
     * 处理表
     * @param config
     * @param columns
     * @return List<IntrospectedTable>
     * @throws SQLException
     */
    protected List<IntrospectedTable> calculateIntrospectedTables(DatabaseConfig config, Map<IntrospectedTable, List<IntrospectedColumn>> columns) throws SQLException {
        List<IntrospectedTable> answer = new ArrayList<>();
        Map<String, String> tableCommentsMap = getTableComments(config);
        Map<String, String> tableTypesMap = getTableTypes(config);
        Map<String, Map<String, String>> tableColumnCommentsMap = getColumnComments(config);
        Map<String, String> columnTypes =  getColumnTypes(config);
        for (Map.Entry<IntrospectedTable, List<IntrospectedColumn>> entry : columns.entrySet()) {
            //处理表
            long startTime1 = System.currentTimeMillis();

            IntrospectedTable table = entry.getKey();
            //给表添加表类型
            if (tableTypesMap != null && tableTypesMap.containsKey(table.getName())) {
                table.setType(tableTypesMap.get(table.getName()));
            }
            //给表添加表注释
            if (tableCommentsMap != null && tableCommentsMap.containsKey(table.getName())) {
                table.setRemarks(tableCommentsMap.get(table.getName()));
            }
            //得到字段和字段注释的对应关系，方便添加字段注释。
            Map<String, String> columnCommentsMap = null;
            if (tableColumnCommentsMap != null && tableColumnCommentsMap.containsKey(table.getName())) {
                columnCommentsMap = tableColumnCommentsMap.get(table.getName());
            }
            //给字段添加类型和注释，不同的数据库，用数据库元数据类的方法可能获取不到字段类型和注释。
            for (IntrospectedColumn introspectedColumn : entry.getValue()) {
                FullyQualifiedJavaType fullyQualifiedJavaType = calculateJavaType(introspectedColumn);
                if (fullyQualifiedJavaType != null) {
                    introspectedColumn.setFullyQualifiedJavaType(fullyQualifiedJavaType);
                    if (columnTypes!=null){
                        introspectedColumn.setJdbcTypeName(columnTypes.get(introspectedColumn.getName()));
                    }else {
                        introspectedColumn.setJdbcTypeName(calculateJdbcTypeName(introspectedColumn));
                    }
                }
                //转换为驼峰形式
                if (useCamelCase) {
                    introspectedColumn.setJavaProperty(JavaBeansUtil.getCamelCaseString(introspectedColumn.getName(), false));
                } else {
                    introspectedColumn.setJavaProperty(JavaBeansUtil.getValidPropertyName(introspectedColumn.getName()));
                }
                if (columnCommentsMap != null && columnCommentsMap.containsKey(introspectedColumn.getName())) {
                    introspectedColumn.setRemarks(columnCommentsMap.get(introspectedColumn.getName()));
                }
                table.addColumn(introspectedColumn);
            }
            //添加表的主键信息
            calculatePrimaryKey(config, table);
            //表的外键-----
            calculateForeignKey(config,table);
            //表的索引-----
            calculateInFoColumns(config,table);
            answer.add(table);
//            //在这里增加根据表类型获取像相应的表信息
//            if (config.getTypes()==null){
//                answer.add(table);
//            }else {
//                for (String type:config.getTypes()){
//                    //数据库元数据获得表的类型的大小写形式和数据库是否有关。
//                    if (table.getType().equals(type.toUpperCase()))
//                        answer.add(table);
//                }
//            }

            long endTime1 = System.currentTimeMillis();
            System.out.println("处理完表"+table.getName()+" 的时间:" + (endTime1 - startTime1)+ "ms");
        }
        return answer;
    }
    /**
     * 获取表名和类型映射（Map）
     * @param config
     * @return Map<String, String>
     * @throws SQLException
     */
    protected Map<String, String> getTableTypes(DatabaseConfig config) throws SQLException {
        //这里注意，对于mysql来说获得表注释的话-----
        ResultSet rs = dbMetadataUtils.getDatabaseMetaData().getTables(
                config.getCatalog(),
                config.getSchemaPattern(),
                config.getTableNamePattern(),
                null
        );
        Map<String, String> answer = new HashMap<>();
        while (rs.next()) {
            answer.put(rs.getString("TABLE_NAME"), rs.getString("TABLE_TYPE"));
        }
        closeResultSet(rs);
        return answer;
    }
    /**
     * 获取表名和注释映射（Map）
     * @param config
     * @return Map<String, String>
     * @throws SQLException
     */
    protected Map<String, String> getTableComments(DatabaseConfig config) throws SQLException {
        //这里注意，对于mysql来说获得表注释的话-----
        ResultSet rs = dbMetadataUtils.getDatabaseMetaData().getTables(
                config.getCatalog(),
                config.getSchemaPattern(),
                config.getTableNamePattern(),
                null
        );
        Map<String, String> answer = new HashMap<>();
        while (rs.next()) {
            answer.put(rs.getString("TABLE_NAME"), rs.getString("REMARKS"));
        }
        closeResultSet(rs);
        return answer;
    }
    /**
     * 获取表名和(列名-注)释映射
     * @param config
     * @return
     * @throws SQLException
     */
    protected Map<String, Map<String, String>> getColumnComments(DatabaseConfig config) throws SQLException {
        return null;
    }
    /**
     * 字段-类型映射
     * @param config
     * @return
     * @throws SQLException
     */
    protected Map<String, String> getColumnTypes(DatabaseConfig config) throws SQLException {
        return null;
    }
    /**
     * javabean的类型？
     * @param introspectedColumn
     * @return FullyQualifiedJavaType
     */
    protected FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
        FullyQualifiedJavaType answer;
        JdbcTypeInformation jdbcTypeInformation = typeMap.get(introspectedColumn.getJdbcType());
        if (jdbcTypeInformation == null) {
            switch (introspectedColumn.getJdbcType()) {
                case Types.DECIMAL:
                case Types.NUMERIC:
                    if (introspectedColumn.getScale() > 0 || introspectedColumn.getLength() > 18 || forceBigDecimals) {
                        answer = new FullyQualifiedJavaType(BigDecimal.class.getName());
                    } else if (introspectedColumn.getLength() > 9) {
                        answer = new FullyQualifiedJavaType(Long.class.getName());
                    } else if (introspectedColumn.getLength() > 4) {
                        answer = new FullyQualifiedJavaType(Integer.class.getName());
                    } else {
                        answer = new FullyQualifiedJavaType(Short.class.getName());
                    }
                    break;
                default:
                    answer = null;
                    break;
            }
        } else {
            answer = jdbcTypeInformation.getFullyQualifiedJavaType();
        }
        return answer;
    }
    /***
     *
     * @param introspectedColumn
     * @return
     */
    protected String calculateJdbcTypeName(IntrospectedColumn introspectedColumn) {
        String answer;
        JdbcTypeInformation jdbcTypeInformation = typeMap.get(introspectedColumn.getJdbcType());

        if (jdbcTypeInformation == null) {
            switch (introspectedColumn.getJdbcType()) {
                case Types.DECIMAL:
                    answer = "DECIMAL";
                    break;
                case Types.NUMERIC:
                    answer = "NUMERIC";
                    break;
                default:
                    answer = null;
                    break;
            }
        } else {
            answer = jdbcTypeInformation.getJdbcTypeName();
        }

        return answer;
    }
    /**
     * 表主键-字段集合
     * @param config
     * @param introspectedTable
     */
    protected void calculatePrimaryKey(DatabaseConfig config, IntrospectedTable introspectedTable) {
        ResultSet rs = null;
        try {
            rs = dbMetadataUtils.getDatabaseMetaData().getPrimaryKeys(
                    config.getCatalog(),
                    config.getSchemaPattern(),
                    introspectedTable.getName()
            );
        } catch (SQLException e) {
            closeResultSet(rs);
            return;
        }
        try {
            Map<Short, String> keyColumns = new TreeMap<>();
            while (rs.next()) {
                //主键列名
                String columnName = rs.getString("COLUMN_NAME");
                //主键顺序
                short keySeq = rs.getShort("KEY_SEQ");
                keyColumns.put(keySeq, columnName);
            }

            for (String columnName : keyColumns.values()) {
                introspectedTable.addPrimaryKeyColumn(columnName);
            }
        } catch (SQLException e) {
        } finally {
            closeResultSet(rs);
        }
    }
    /**
     * 表外键
     * @param config
     * @param introspectedTable
     */
    protected void calculateForeignKey(DatabaseConfig config, IntrospectedTable introspectedTable){
        ResultSet rs = null;
        try {
            rs = dbMetadataUtils.getDatabaseMetaData().getImportedKeys(
                    config.getCatalog(),
                    config.getSchemaPattern(),
                    introspectedTable.getName()
            );
        } catch (SQLException e) {
            closeResultSet(rs);
            return;
        }
        try {
            Map<Short, String> keyColumns = new TreeMap<>();
            while (rs.next()) {
                //外键列名
                String columnName = rs.getString("FKCOLUMN_NAME");
                //外键顺序
                short keySeq = rs.getShort("KEY_SEQ");
                keyColumns.put(keySeq, columnName);
            }
            for (String columnName : keyColumns.values()) {
                introspectedTable.addForeignKeyColumn(columnName);
            }
        } catch (SQLException e) {
        } finally {
            closeResultSet(rs);
        }
    }
    /**
     * 表索引
     * @param config
     * @param introspectedTable
     */
    protected void calculateInFoColumns(DatabaseConfig config, IntrospectedTable introspectedTable){
        ResultSet rs = null;
        try {
            rs = dbMetadataUtils.getDatabaseMetaData().getIndexInfo(
                    config.getCatalog(),
                    config.getSchemaPattern(),
                    introspectedTable.getName(),
                    false,
                    false
            );
        } catch (SQLException e) {
            closeResultSet(rs);
            return;
        }
        try {
            //db2.Oracle中会有一些问题。。。表中如果没有索引，总会有一个结果null。
            while (rs.next()) {
                IndexInFo indexInFo=new IndexInFo();
                if (rs.getString("COLUMN_NAME")==null)
                    continue;
                //外键的一些信息
                indexInFo.setColumnName(rs.getString("COLUMN_NAME"));
                indexInFo.setName(rs.getString("INDEX_NAME"));

                introspectedTable.addInFoKeyColumn(indexInFo);
            }
        } catch (SQLException e) {
        } finally {
            closeResultSet(rs);
        }
    }
    /**
     * 关闭ResultSet
     * @param rs
     */
    protected void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
    }
}