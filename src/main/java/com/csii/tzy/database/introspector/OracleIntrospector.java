package com.csii.tzy.database.introspector;

import com.csii.tzy.database.DatabaseConfig;
import com.csii.tzy.database.IndexInFo;
import com.csii.tzy.database.IntrospectedTable;
import com.csii.tzy.utils.DBMetadataUtils;
import com.csii.tzy.utils.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class OracleIntrospector extends DatabaseIntrospector {

    public OracleIntrospector(DBMetadataUtils dbMetadataUtils) {
        super(dbMetadataUtils);
    }

    public OracleIntrospector(DBMetadataUtils dbMetadataUtils, boolean forceBigDecimals, boolean useCamelCase) {
        super(dbMetadataUtils, forceBigDecimals, useCamelCase);
    }

    @Override
    protected void calculateInFoColumns(DatabaseConfig config, IntrospectedTable introspectedTable) {
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
            //Oracle中会有一些问题。。。表中如果没有索引，总会有一个结果。
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
     * 获取表名和注释映射
     *
     * @param config
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, String> getTableComments(DatabaseConfig config) throws SQLException {
        Map<String, String> answer = new HashMap<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("select table_name tname,comments from all_tab_comments where comments is not null ");
            if (StringUtils.isNotEmpty(config.getSchemaPattern())) {
                sqlBuilder.append(" and owner like :1 ");
            }
            sqlBuilder.append("order by tname ");
            PreparedStatement preparedStatement = dbMetadataUtils.getConnection().prepareStatement(sqlBuilder.toString());
            if (StringUtils.isNotEmpty(config.getSchemaPattern())) {
                preparedStatement.setString(1, config.getSchemaPattern());
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                answer.put(rs.getString(dbMetadataUtils.convertLetterByCase("TNAME")), rs.getString(dbMetadataUtils.convertLetterByCase("COMMENTS")));
            }
            closeResultSet(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

    /**
     * 获取表字段注释
     *
     * @param config
     * @return
     * @throws SQLException
     */
    @Override
    protected Map<String, Map<String, String>> getColumnComments(DatabaseConfig config) throws SQLException {
        Map<String, Map<String, String>> answer = new HashMap<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("select table_name tname,column_name cname,comments from all_col_comments ");
            sqlBuilder.append("where comments is not null ");
            if (StringUtils.isNotEmpty(config.getSchemaPattern())) {
                sqlBuilder.append(" and owner like :1 ");
            }
            sqlBuilder.append("order by table_name,column_name ");

            PreparedStatement preparedStatement = dbMetadataUtils.getConnection().prepareStatement(sqlBuilder.toString());
            if (StringUtils.isNotEmpty(config.getSchemaPattern())) {
                preparedStatement.setString(1, config.getSchemaPattern());
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String tname = rs.getString(dbMetadataUtils.convertLetterByCase("TNAME"));
                if (!answer.containsKey(tname)) {
                    answer.put(tname, new HashMap<>());
                }
                answer.get(tname).put(rs.getString(dbMetadataUtils.convertLetterByCase("CNAME")), rs.getString(dbMetadataUtils.convertLetterByCase("COMMENTS")));
            }
            closeResultSet(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }
}