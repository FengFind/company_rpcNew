package com.newtec.company.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.newtec.company.entity.collect.Limit;
import com.newtec.company.entity.collect.MapperInfo;

/**
 *  切换数据源的工具类
 * @author Administrator
 *
 */
public class DBLimit{
	private static  String DRIVER = "com.mysql.jdbc.Driver";
	private static  String DATABASE_URL = "jdbc:mysql://192.168.104.9:3306/mydb?useUnicode=true&characterEncoding=utf8";
	private static  String USERNAME = "root";
	private static  String PASSWORD = "password";
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
	
	/**
	 * 获取连接源
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static Limit  getLimitInfo(String id) throws SQLException {
		
		Connection conn = getConnection();
		
		String sql = "select * from db_limit where id = ?";
		
		PreparedStatement pre = conn.prepareStatement(sql);
		
		pre.setString(1, id);
		
		ResultSet rs = pre.executeQuery();
		Limit limit = null;
		List<Limit> list = new ArrayList<Limit>();
		while(rs.next()) {
			limit = new Limit(id,rs.getString("name"),rs.getString("url"),rs.getString("password"),rs.getString("username"),rs.getString("type"),rs.getString("sysname"));
		}
		closeConnection(conn);
		return limit;
	}
	
	/**
	 * 获取来源表和目标表的映射关系
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static List<MapperInfo>  getMapper() throws SQLException {
		
		Connection conn = getConnection();
		String sql = "select * from db_mapper where state = '0'";
		PreparedStatement pre = conn.prepareStatement(sql);
		ResultSet rs = pre.executeQuery();
		MapperInfo mapper = null;
		List<MapperInfo> list = new ArrayList<MapperInfo>();
		while(rs.next()) {
			mapper = new MapperInfo(rs.getString("ch_name"),rs.getString("sou_name"),rs.getString("tar_name"),rs.getString("state"));
			list.add(mapper);
		}
		closeConnection(conn);
		return list;
	}
	
}