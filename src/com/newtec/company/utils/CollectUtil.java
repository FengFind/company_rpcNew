package com.newtec.company.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtec.thread.CollectThread;

public class CollectUtil {

	/**
	 * 保存dblimit信息
	 * @param type 数据库类型
	 * @param url 数据库ip:端口号
	 * @param name 数据库名称
	 * @param charSet 字符集
	 * @param uname 用户名
	 * @param pswd 密码
	 * @param sysname 来源系统
	 * @return
	 */
	public static JSONObject saveDbLimitByDbMsg(String type, String url, String name,
			String charSet, String uname, String pswd, String sysname) {
		JSONObject res = new JSONObject();
		
		// 根据数据类型组装数据库连接
		StringBuffer dburl = new StringBuffer();
		
		if(type.toLowerCase().equals("1")) {
			// Mysql
			dburl.append("jdbc:mysql://").append(url).append("/").append(name).append("?useUnicode=true&characterEncoding=").append(charSet);
		}else if(type.toLowerCase().equals("0")) {
			// Oracle
			dburl.append("jdbc:oracle:thin:@").append(url).append(":").append(name);
		}
		
		// 获取connect
		Connection conn = DBLimit.getConnection();
		
		try {
			// 存入之前 先查看是否已经存在相同的数据源
			String search = " select * from db_limit where name='"+name+"' "
								 + " and url = '"+dburl.toString()+"' "
								 + " and type ='"+(type.equals("1") ? "mysql" : "oracle")+"' "
								 + " and username = '"+uname+"' "
								 + " and password ='"+pswd+"' "
								 + " and sysname = '"+sysname+"' ";
			System.out.println("search : "+search);
			PreparedStatement pre = conn.prepareStatement(search);
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				res.put("id", rs.getString("id"));
				res.put("url", rs.getString("url"));
				res.put("type", rs.getString("type"));
				res.put("username", rs.getString("username"));
				res.put("password", rs.getString("password"));
				res.put("sysname", rs.getString("sysname"));
				
				return res;
			}
			
			// 保存sql
			String sql = "insert into db_limit(id,name,url,type,username,password,sysname)values(?,?,?,?,?,?,?)";
			
			pre = conn.prepareStatement(sql);
			String id = System.currentTimeMillis()+"";
			pre.setString(1, id);
			pre.setString(2, name);
			pre.setString(3, dburl.toString());
			pre.setString(4, (type.equals("1") ? "mysql" : "oracle"));
			pre.setString(5, uname);
			pre.setString(6, pswd);
			pre.setString(7, sysname);
			
			int update = pre.executeUpdate();
			
			if(update == 0) {
				res = null;
			}else {
				res.put("id", id);
				res.put("url", dburl.toString());
				res.put("type", (type.equals("1") ? "mysql" : "oracle"));
				res.put("username", uname);
				res.put("password", pswd);
				res.put("sysname", sysname);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	/**
	 * 保存 dbmapper 数据
	 * @param chName 中文名称
	 * @param souName 来源 英文表名 
	 * @param tarName 目标 英文表名
	 * @param souDbId 来源 数据库信息id
	 * @param tarDbId 目标 数据库信息id
	 * @param souTableColumns 来源 表 字段字符串
	 * @param tarTableColumns 目标 表 字段字符串
	 * @param souTableCond 来源 表 查询条件
	 * @param tarTableCond 目标 表 查询条件
	 */
	@SuppressWarnings("resource")
	public static boolean saveDbMapperMsg(String chName, String souName, String tarName, 
			String souDbId, String tarDbId, String souTableColumns, String tarTableColumns, 
			String souTableCond, String tarTableCond, String operType) {
		boolean res = true;
		
		// 获取connect
		Connection conn = DBLimit.getConnection();
		
		try {
			// 判断 operType 的值
			if(operType != null && !operType.equals("") && operType.equals("add")) {
				// 值为 add 表示 新增
				// 先查询是否已经存在相同的数据
				String search = " select * from db_mapper "
									 + " where  "
									 + " ch_name = '"+chName+"' "
									 + " and sou_name = '"+souName+"' "
									 + " and tar_name = '"+tarName+"' "
									 + " and sou_db_id = '"+souDbId+"' "
									 + " and tar_db_id = '"+tarDbId+"' ";
				System.out.println("search : "+search);
				PreparedStatement pre = conn.prepareStatement(search);
				ResultSet rs = pre.executeQuery();
				
				if(rs.next()) {
					// 存在 更新 
					String update = " update db_mapper set "
										  + " sou_table_columns = '"+souTableColumns+"', "
										  + " tar_table_columns = '"+tarTableColumns+"', "
										  + " sou_table_cond = '"+souTableCond+"', "
										  + " tar_table_cond = '"+tarTableCond+"' "
										  + " where  "
										  + " ch_name = '"+chName+"' "
										  + " and sou_name = '"+souName+"' "
										  + " and tar_name = '"+tarName+"' "
										  + " and sou_db_id = '"+souDbId+"' "
										  + " and tar_db_id = '"+tarDbId+"' ";
					System.out.println("update : "+update);
					pre = conn.prepareStatement(update);
					int upd = pre.executeUpdate();
					if(upd == 0) {
						res = false;
					}
				}else {
					// 不存在 插入新记录
					// 保存sql
					String sql = "insert into db_mapper(ch_name,sou_name,tar_name,state,task_cron,sou_db_id,tar_db_id,sou_table_columns,tar_table_columns,sou_table_cond,tar_table_cond,create_time,task_status)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
					System.out.println("sql : "+sql);
					pre = conn.prepareStatement(sql);
					pre.setString(1, chName);
					pre.setString(2, souName);
					pre.setString(3, tarName);
					pre.setInt(4, 0);
					pre.setString(5, "");
					pre.setString(6, souDbId);
					pre.setString(7, tarDbId);
					pre.setString(8, souTableColumns);
					pre.setString(9, tarTableColumns);
					pre.setString(10, souTableCond);
					pre.setString(11, tarTableCond);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					pre.setString(12, sdf.format(new Date()));
					pre.setString(13, "0");
					
					int update = pre.executeUpdate();
					
					if(update == 0) {
						res = false;
					}
					
				}
			}else if(operType != null && !operType.equals("") && operType.equals("set")) {
				// 值为 set 表示 修改
				String update = " update db_mapper set "
									  + " sou_table_columns = '"+souTableColumns+"', "
									  + " tar_table_columns = '"+tarTableColumns+"', "
									  + " sou_table_cond = '"+souTableCond+"', "
									  + " tar_table_cond = '"+tarTableCond+"', "
									  + " ch_name = '"+chName+"' "
									  + " where  "
									  + " sou_name = '"+souName+"' "
									  + " and tar_name = '"+tarName+"' "
									  + " and sou_db_id = '"+souDbId+"' "
									  + " and tar_db_id = '"+tarDbId+"' ";
				System.out.println("更新sql : "+update);
				PreparedStatement pre = conn.prepareStatement(update);
				int upd = pre.executeUpdate();
				if(upd == 0) {
					res = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	/**
	 * 测试是否数据库信息正确
	 * @param type 数据库类型
	 * @param url 数据库ip:端口号
	 * @param name 数据库名称
	 * @param charSet 字符集
	 * @param uname 用户名
	 * @param pswd 密码
	 * @return
	 */
	public static boolean testDbMsgConnect(String type, String url, String name,
			String charSet, String uname, String pswd) {
		boolean res = true;
		// 获取connect
		Connection conn = null;
		try {
			// 根据数据类型组装数据库连接
			StringBuffer dburl = new StringBuffer();
			if(type.toLowerCase().equals("1")) {
				// Mysql
				dburl.append("jdbc:mysql://").append(url).append("/").append(name).append("?useUnicode=true&characterEncoding=").append(charSet);
				conn = DBMySql.getConnection(dburl.toString(), uname, pswd);
			}else if(type.toLowerCase().equals("0")) {
				// Oracle
				dburl.append("jdbc:oracle:thin:@").append(url).append(":").append(name);
				conn = DBOrcal.getConnection(dburl.toString(), uname, pswd);
			}
			
			if(conn == null) {
				res = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("获取连接失败");
			res = false;
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	public static void main(String[] args) {
		
	}
	
	/**
	 * 根据id查询数据库连接信息
	 * @param id
	 * @return
	 */
	public static JSONObject findDbInfoById(String id) {
		JSONObject res = new JSONObject();
		
		// 获得connect
		Connection conn = DBLimit.getConnection();
		try {
			// sql
			String sql = " select * from db_limit where id='"+id+"' ";
			
			// 查询
			PreparedStatement pre = conn.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				res.put("id", rs.getObject("id") == null ? "" : rs.getString("id"));
				res.put("name", rs.getObject("name") == null ? "" : rs.getString("name"));
				res.put("type", rs.getObject("type") == null ? "" : rs.getString("type"));
				res.put("username", rs.getObject("username") == null ? "" : rs.getString("username"));
				res.put("password", rs.getObject("password") == null ? "" : rs.getString("password"));
				res.put("sysname", rs.getObject("sysname") == null ? "" : rs.getString("sysname"));
				
				String url = rs.getObject("url") == null ? "" : rs.getString("url");
				if(url != null && !url.equals("")) {
					// Mysql 
					if(res.get("type") != null && !res.get("type").equals("") && res.getString("type").equals("mysql")) {
						// ip:端口
						String url1 = url.substring("jdbc:mysql://".length());
						String dbUrl = url1.substring(0, url1.indexOf("/"));
						res.put("sourceDBUrl", dbUrl);
						
						url1 = url1.substring(dbUrl.length()+1);
						// 数据库名称
						String dbName = url1.substring(0, url1.indexOf("?"));
						res.put("sourceDBName", dbName);
						
						url1 = url1.substring(dbName.length()+1);
						// charset
						String charSet = url1.substring(url1.lastIndexOf("=")+1);
						res.put("sourceDBChar", charSet);
						// 设置类型
						res.put("sourceDBType", "1");
					}else if(res.get("type") != null && !res.get("type").equals("") && res.getString("type").equals("oracle")) {
						// ip:端口
						String url1 = url.substring("jdbc:oracle:thin:@".length());
						String dbUrl = url1.substring(0, url1.lastIndexOf(":"));
						res.put("sourceDBUrl", dbUrl);
						
						url1 = url1.substring(dbUrl.length()+1);
						// 数据库名称
						String dbName = url1;
						res.put("sourceDBName", dbName);
						
						// charset
						res.put("sourceDBChar", "utf8");
						// 设置类型
						res.put("sourceDBType", "0");
					}
					
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	/**
	 * 根据id查询数据库连接信息
	 * @param id
	 * @return
	 */
	public static JSONObject findMapperInfoById(String chName, String souName, String souDbId,
			String tarName, String tarDbId) {
		JSONObject res = new JSONObject();
		
		// 获得connect
		Connection conn = DBLimit.getConnection();
		try {
			// sql
			String sql = " SELECT * FROM db_mapper  WHERE ch_name = '"+chName+"' and sou_name='"+souName+"' and sou_db_id = '"+souDbId+"' and tar_name = '"+tarName+"' and tar_db_id = '"+tarDbId+"' ";
			System.out.println("sql : "+sql);
			// 查询
			PreparedStatement pre = conn.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				res.put("sou_table_columns", rs.getObject("sou_table_columns") == null ? "" : rs.getString("sou_table_columns"));
				res.put("tar_table_columns", rs.getObject("tar_table_columns") == null ? "" : rs.getString("tar_table_columns"));
				res.put("sou_table_cond", rs.getObject("sou_table_cond") == null ? "" : rs.getString("sou_table_cond"));
				res.put("tar_table_cond", rs.getObject("tar_table_cond") == null ? "" : rs.getString("tar_table_cond"));
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	/**
	 * 立即执行一次插入数据任务
	 * @param chName 中文表名
	 * @param souName 来源英文表名
	 * @param souDbId 来源数据源id
	 * @param tarName 目标英文表名
	 * @param tarDbId 目标数据源id
	 * @return
	 */
	public static JSONObject doOnceTask(String chName, String souName, String souDbId,
			String tarName, String tarDbId) {
		JSONObject res = new JSONObject();
		
		// 获得connect
		Connection conn = DBLimit.getConnection();
		try {
			// 来源数据库 信息
			String souUrl = "";
			String souType = "";
			String souUname = "";
			String souPswd = "";
//			String souTableColumns = "";
			String souTableCond = "";
			// 目标数据库 信息
			String tarUrl = "";
			String tarType = "";
			String tarUname = "";
			String tarPswd = "";
//			String tarTableColumns = "";
			String tarTableCond = "";
			// 标识 当三个标识都为true时 才执行插入数据操作
			boolean mf = false, lsf = false, ltf = false;
			
			// sql
			String sql = " SELECT * FROM db_mapper  WHERE ch_name = '"+chName+"' and sou_name='"+souName+"' and sou_db_id = '"+souDbId+"' and tar_name = '"+tarName+"' and tar_db_id = '"+tarDbId+"' ";
			System.out.println("sql : "+sql);
			// 查询
			PreparedStatement pre = conn.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			
			if(rs.next()) {
				mf = true;
//				souTableColumns = rs.getObject("sou_table_columns") == null ? "" : rs.getString("sou_table_columns");
				souTableCond = rs.getObject("sou_table_cond") == null ? "" : rs.getString("sou_table_cond");
//				tarTableColumns = rs.getObject("tar_table_columns") == null ? "" : rs.getString("tar_table_columns");
				tarTableCond = rs.getObject("tar_table_cond") == null ? "" : rs.getString("tar_table_cond");
				
				// 查询 来源数据库 信息
				String souSql = " select * from db_limit where id = '"+souDbId+"' ";
				System.out.println("souSql : "+souSql);
				pre = conn.prepareStatement(souSql);
				rs = pre.executeQuery();
				
				if(rs.next()) {
					lsf = true;
					souUrl = rs.getObject("url") == null ? "" : rs.getString("url");
					souType = rs.getObject("type") == null ? "" : rs.getString("type");
					souUname = rs.getObject("username") == null ? "" : rs.getString("username");
					souPswd = rs.getObject("password") == null ? "" : rs.getString("password");
				}
				
				// 目标数据库 信息
				String tarSql = " select * from db_limit where id = '"+tarDbId+"' ";
				System.out.println("tarSql : "+tarSql);
				pre = conn.prepareStatement(tarSql);
				rs = pre.executeQuery();
				
				if(rs.next()) {
					ltf = true;
					tarUrl = rs.getObject("url") == null ? "" : rs.getString("url");
					tarType = rs.getObject("type") == null ? "" : rs.getString("type");
					tarUname = rs.getObject("username") == null ? "" : rs.getString("username");
					tarPswd = rs.getObject("password") == null ? "" : rs.getString("password");
				}
			}
			
			if(mf && lsf && ltf) {
				Connection sou_Conn = null, tar_Conn = null;
				try {
					// 来源表数量 
					sou_Conn = DataViewTask.findConnectionByMsg(souUrl, souType, souUname, souPswd);
					int sou_Count = DataViewTask.getCount(sou_Conn, souName, souTableCond);
					// 目标表数量
					tar_Conn = DataViewTask.findConnectionByMsg(tarUrl, tarType, tarUname, tarPswd);
					int tar_Count= DataViewTask.getCount(tar_Conn, tarName, tarTableCond);
					
					System.out.println("对比表并插入  【"+souName+"("+tarName+")】  信息");
					JSONObject mapper = new JSONObject();
					
					mapper.put("tar_name", tarName);
					mapper.put("tar_db_id", tarDbId);
					mapper.put("sou_name", souName);
					mapper.put("sou_db_id", souDbId);
					mapper.put("ch_name", chName);
					
					CollectThread.insertData(mapper, sou_Count, tar_Count);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					if(sou_Conn != null) {
						try {
							sou_Conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(tar_Conn != null) {
						try {
							tar_Conn.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	/**
	 * 更新db_mapper 的定时器状态
	 * @param chName
	 * @param souName
	 * @param tarName
	 * @return
	 */
	public static int updateDbMapperTaskStatus(String chName, String souName, String tarName, String taskStatus) {
		int res = -1;
		
		// 获得connect
		Connection conn = DBLimit.getConnection();
		try {
			// sql
			String sql = " update db_mapper set task_status = '"+taskStatus+"' WHERE ch_name = '"+chName+"' and sou_name='"+souName+"' and tar_name = '"+tarName+"' ";
			System.out.println("sql : "+sql);
			// 查询
			PreparedStatement pre = conn.prepareStatement(sql);
			res = pre.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
	
	/**
	 * 根据数据库信息查询库中表
	 * @param dbType 数据库类型
	 * @param dbUrl 数据库url
	 * @param dbName 数据库名 
	 * @param dbCharSet 编码格式
	 * @param dbUname 用户名
	 * @param dbPswd 密码
	 * @return
	 */
	public static JSONArray selectTableNames(String dbType, String dbUrl, String dbName, 
			String dbCharSet, String dbUname, String dbPswd) {
		// 数据库url
		StringBuffer url = new StringBuffer();
		// 数据库连接
		Connection conn = null;
		// 查询sql
		String sql = "";
		
		if(dbType.toLowerCase().equals("1")) {
			// Mysql
			url.append("jdbc:mysql://").append(dbUrl).append("/").append(dbName).append("?useUnicode=true&characterEncoding=").append(dbCharSet);
			conn = DBMySql.getConnection(url.toString(), dbUname, dbPswd);
			sql = "select table_name from INFORMATION_SCHEMA.TABLES where table_schema='"+dbName+"'";
		}else if(dbType.toLowerCase().equals("0")) {
			// Oracle
			url.append("jdbc:oracle:thin:@").append(dbUrl).append(":").append(dbName);
			conn = DBOrcal.getConnection(url.toString(), dbUname, dbPswd);
			sql = "select t.table_name from user_tables t";
		}
		
		// 查询
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			JSONArray ja = new JSONArray();
			while(rs.next()) {
				ja.add(rs.getObject(1));
			}
			
			return ja;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 根据数据库信息查询库中表
	 * @param dbType 数据库类型
	 * @param dbUrl 数据库url
	 * @param dbName 数据库名 
	 * @param dbCharSet 编码格式
	 * @param dbUname 用户名
	 * @param dbPswd 密码
	 * @param tableName 表名
	 * @return
	 */
	public static JSONArray selectColumnsNamesByTable(String dbType, String dbUrl, String dbName, 
			String dbCharSet, String dbUname, String dbPswd, String tableName) {
		// 数据库url
		StringBuffer url = new StringBuffer();
		// 数据库连接
		Connection conn = null;
		// 查询sql
		String sql = "";
		
		if(dbType.toLowerCase().equals("1")) {
			// Mysql
			url.append("jdbc:mysql://").append(dbUrl).append("/").append(dbName).append("?useUnicode=true&characterEncoding=").append(dbCharSet);
			conn = DBMySql.getConnection(url.toString(), dbUname, dbPswd);
			sql = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '"+tableName+"' AND table_schema = '"+dbName+"'";
		}else if(dbType.toLowerCase().equals("0")) {
			// Oracle
			url.append("jdbc:oracle:thin:@").append(dbUrl).append(":").append(dbName);
			conn = DBOrcal.getConnection(url.toString(), dbUname, dbPswd);
			sql = "select utc.COLUMN_NAME from user_tab_columns utc where table_name = upper('"+tableName+"')";
		}
		
		// 查询
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			JSONArray ja = new JSONArray();
			while(rs.next()) {
				ja.add(rs.getObject(1));
			}
			
			return ja;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 查询出 sou 和 tar 对应的数据源名称数组
	 * @return
	 */
	public static JSONObject findDbAry() {
		// 返回的jsonobject
		JSONObject res = new JSONObject();
		// 数据库连接
		Connection conn = DBLimit.getConnection();
		// 查询sql
		String sql = "SELECT dl.id, dl.`name`, 1 as type,dl.url FROM db_mapper dm " + 
				"inner join db_limit dl on dm.sou_db_id=dl.id " + 
				"GROUP BY dl.id " + 
				"UNION " + 
				"SELECT dl.id, dl.`name`, 2 as type,dl.url FROM db_mapper dm " + 
				"inner join db_limit dl on dm.tar_db_id=dl.id " + 
				"GROUP BY dl.id";
		
		// 查询
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			ResultSet rs = pre.executeQuery();
			// source
			JSONArray sou = new JSONArray();
			// target
			JSONArray tar = new JSONArray();
			while(rs.next()) {
				// 类型
				int t = rs.getInt("type");
				// id
				String id = rs.getString("id");
				// name 
				String name = rs.getObject("name") == null ? "" : rs.getString("name") ;
				// url
				String url = rs.getString("url");
				// obj
				JSONObject jobj = new JSONObject();
				
				jobj.put("id", id);
				jobj.put("name", name);
				jobj.put("url", url);
				jobj.put("choosed", false);
				
				if (t == 1) {
					sou.add(jobj);
				}else if(t == 2) {
					tar.add(jobj);
				}
			}
			
			res.put("souDbAry", sou);
			res.put("tarDbAry", tar);
			res.put("status", "0");
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", "-1");
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return res;
	}
}
