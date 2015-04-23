package com.melody.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.melody.pojos.Information;
import com.weibo.zel.utils.sina.ReadSpiderConfig;

public class JDBCTools {
    /**
     * 获得数据库连接
     * 
     * @return
     * @throws Exception
     */
    public static Connection getConnection() {
        Connection con = null;
        String driver=ReadSpiderConfig.getValue("driver");
  		String url=ReadSpiderConfig.getValue("url");
   		String user=ReadSpiderConfig.getValue("root");
  		String password=ReadSpiderConfig.getValue("password");
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(
            		url, user, password);
        } catch (Exception e) {
            System.out.print("MYSQL ERROR:" + e.getMessage());
        }
        return con;
    }

    public static void releaseSource(ResultSet rs, Statement statement,
            Connection conncetion) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (conncetion != null) {
            try {
                conncetion.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 关闭statement和conncetion
     * 
     * @param statement
     * @param conncetion
     */
    public static void releaseSource(Statement statement, Connection conncetion) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (conncetion != null) {
            try {
                conncetion.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 通用数据库更新的方法：包括INSERT、UPDATE、DELETE
     * 
     * @param sql
     */
    public static void update(String sql) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JDBCTools.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCTools.releaseSource(statement, connection);
        }
    }

    /**
     * 使用preparement升级update方法
     * 
     * @param sql
     * @param args
     */
    public static void update(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCTools.getConnection();
            ps = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertBatch(String sql, Information[] informations) {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final int batchSize = 15;
        int count = 0;
        for (Information information : informations) {
            try {
                ps.setInt(1, information.getId());
                ps.setString(2, information.getUid());
                ps.setString(3, information.getName());
                ps.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (++count % batchSize == 0) {
                try {
                    ps.executeBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            ps.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
