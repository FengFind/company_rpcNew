package com.newtec.company.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.poi.ss.formula.functions.T;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StudentDBUtil {

	// 数据库驱动
	private static  String DRIVER = "com.mysql.jdbc.Driver";
	// 数据库url
	private static  String DATABASE_URL = "jdbc:mysql://47.94.241.212:3366/student?useUnicode=true&characterEncoding=utf8";
	// 用户名
	private static  String USERNAME = "root";
	// 密码
	private static  String PASSWORD = "tiger";
	
	// 加载驱动
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
	public static  Connection getConnection() {
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
	public static void closeConnection(Connection conn, PreparedStatement pre, ResultSet rs) {
		if (rs != null) {
			try {
//				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("关闭rs失败");
			}
		}
		
		if (pre != null) {
			try {
				pre.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("关闭pre失败");
			}
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.err.println("关闭数据库失败");
			}
		}
	}
	
	/**
	 * 根据查询sql 返回ResultSet 
	 * @param sql 已经加入参数的sql
	 * @return
	 */
	public static ResultSet querySql(String sql) {
		// 获取connect
		Connection conn = null;
		// prepared sql中查询条件不进行设置参数 参数应该已经包含在sql中
		PreparedStatement pre = null;
		// ResultSet
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			// prepared sql中查询条件不进行设置参数 参数应该已经包含在sql中
			pre = conn.prepareStatement(sql);
			// 查询并返回结果集
			rs = pre.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn, pre, rs);
		}
		
		return rs;
	}
	
	/**
	 * 根据sql查询出结果并返回jsonArray格式的数据
	 * @param sql 已经写入参数的sql
	 * @return
	 */
	public static JSONArray querySqlForJSONArray(String sql) {
		// 返回json对象
		JSONArray res = new JSONArray();
		// 获取connect
		Connection conn = null;
		// prepared sql中查询条件不进行设置参数 参数应该已经包含在sql中
		PreparedStatement pre = null;
		// ResultSet
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			// prepared sql中查询条件不进行设置参数 参数应该已经包含在sql中
			pre = conn.prepareStatement(sql);
			// 查询并返回结果集
			rs = pre.executeQuery();
			//获得列集
			ResultSetMetaData rsmd = rs.getMetaData();

			while(rs.next()) {
				JSONObject jobj = new JSONObject();
				
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					jobj.put(rsmd.getColumnName(i+1), rs.getObject(rsmd.getColumnName(i+1)));
				}
				
				res.add(jobj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn, pre, rs);
		}
		
		return res;
	}
}
