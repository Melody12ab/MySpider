package com.mbm.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
	public static Connection dbConn = null;
    public static String serverIP;
    public static String userName="sa";
    public static String userPwd="123456789";
    
	static {
		serverIP=ReadProperties.readValue("jdbc_ip");
		openConnection(serverIP,userName,userPwd);
	}
	
	public static boolean openConnection(String serverIP, String userName,
			String userPwd) {
		String driverName = "net.sourceforge.jtds.jdbc.Driver"; //sqlserver2005 jdbc驱动
		String dbURL = "jdbc:jtds:sqlserver://" + serverIP + ":1433/LOS"; // 服务器地址
		try {
			Class.forName(driverName);
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean closeConnection(Connection conn) {
		try {
			if (conn.isClosed()) {
				return true;
			} else {
				conn.close();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String args[]) throws Exception{
		openConnection("192.168.1.120","sa","123456789");
		
//		Statement stat=dbConn.createStatement();
//		String sql="select a108 from tb_A108";
//		ResultSet rs=stat.executeQuery(sql);
		String sql="call del_repeat_records_zel 'zel'";
		CallableStatement cmd = null;
		cmd = dbConn.prepareCall(sql);
	}
	
}
