package com.csii.tzy.database.introspector;

import com.csii.tzy.database.DatabaseConfig;
import com.csii.tzy.utils.DBMetadataUtils;
import com.csii.tzy.utils.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lipu@csii.com.cn
 */
public class MySQLIntrospector extends DatabaseIntrospector{

    public MySQLIntrospector(DBMetadataUtils dbMetadataUtils) {
        super(dbMetadataUtils);
    }

    public MySQLIntrospector(DBMetadataUtils dbMetadataUtils, boolean forceBigDecimals, boolean useCamelCase) {
        super(dbMetadataUtils, forceBigDecimals, useCamelCase);
    }
    @Override
    protected Map<String, String> getTableComments(DatabaseConfig config) throws SQLException {
        Map<String, String> answer = new HashMap<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("select Table_name,Table_comment from information_schema.tables where Table_comment is not null ");
            if (StringUtils.isNotEmpty(config.getCatalog())) {
                sqlBuilder.append(" and TABLE_SCHEMA like ? ");
            }
            sqlBuilder.append("order by Table_name ");
            PreparedStatement preparedStatement = dbMetadataUtils.getConnection().prepareStatement(sqlBuilder.toString());
            if (StringUtils.isNotEmpty(config.getCatalog())) {
                preparedStatement.setString(1, config.getCatalog());
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                answer.put(rs.getString(1), rs.getString(2));
            }
            closeResultSet(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }
    @Override
    protected Map<String, Map<String, String>> getColumnComments(DatabaseConfig config) throws SQLException {
        Map<String, Map<String, String>> answer = new HashMap<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("select Table_name,Column_name,Column_comment from information_schema.columns ");
            sqlBuilder.append("where Column_name is not null ");
            if (StringUtils.isNotEmpty(config.getCatalog())) {
                sqlBuilder.append(" and TABLE_SCHEMA like ? ");
            }
            sqlBuilder.append("order by Table_name,Column_name ");

            PreparedStatement preparedStatement = dbMetadataUtils.getConnection().prepareStatement(sqlBuilder.toString());
            if (StringUtils.isNotEmpty(config.getCatalog())) {
                preparedStatement.setString(1, config.getCatalog());
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                //String tname = rs.getString(dbMetadataUtils.convertLetterByCase("TNAME"));
                String tname = rs.getString(1);
                if (!answer.containsKey(tname)) {
                    answer.put(tname, new HashMap<>());
                }
                //answer.get(tname).put(rs.getString(dbMetadataUtils.convertLetterByCase("CNAME")), rs.getString(dbMetadataUtils.convertLetterByCase("COMMENTS")));
                answer.get(tname).put(rs.getString(1), rs.getString(2));
            }
            closeResultSet(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }
    @Override
    protected Map<String, String> getColumnTypes(DatabaseConfig config) throws SQLException {
        Map<String, String> answer = new HashMap<>();
        try {
            StringBuilder sqlBuilder = new StringBuilder("select Column_name,Data_type from information_schema.columns ");
            if (StringUtils.isNotEmpty(config.getCatalog())) {
                sqlBuilder.append(" where TABLE_SCHEMA like ? ");
            }
            sqlBuilder.append("order by Column_name,Data_type ");

            PreparedStatement preparedStatement = dbMetadataUtils.getConnection().prepareStatement(sqlBuilder.toString());
            if (StringUtils.isNotEmpty(config.getCatalog())) {
                preparedStatement.setString(1, config.getCatalog());
            }
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String tname = rs.getString(1);
                if (!answer.containsKey(tname)) {
                    answer.put(tname, rs.getString(2));
                }
            }
            closeResultSet(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return answer;
    }
}
