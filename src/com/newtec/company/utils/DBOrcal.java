package com.newtec.company.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DBOrcal {
	private static  String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static  String SQL = "SELECT * FROM ";// 数据库操作
	

	
	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("数据库不能连接");
		}
	}
	
	
	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static  Connection getConnection(String DATABASE_URL,String USERNAME,String PASSWORD) {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("获取连接失败");
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("关闭数据库失败");
			}
		}
	}
}
