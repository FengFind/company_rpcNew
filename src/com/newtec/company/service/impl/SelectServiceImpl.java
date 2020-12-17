package com.newtec.company.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.newtec.company.service.SelectService;
import com.newtec.reflect.annotation.RpcClass;
import com.newtec.reflect.annotation.RpcMethod;
import com.newtec.router.request.FetchWebRequest;


@RpcClass(value = "selectService",http = true)
public class SelectServiceImpl implements SelectService {
    
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> getSelect(FetchWebRequest<Map<String, String>> fetchWebReq) {
		
		// TODO Auto-generated method stub
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> reMap = fetchWebReq.getData();
		//数据库类型
		String dbType = reMap.get("dbType");
		// 数据库地址
		String dbUrl = reMap.get("dbUrl");
		//数据库库名
		String dbName = reMap.get("dbName");
		//数据库字符集
		String dbCharSet = reMap.get("dbCharSet");
		// 数据库用户名
		String dbUname = reMap.get("dbUname");
		//数据库用户密码
		String dbPswd = reMap.get("dbPswd");
		
		Connection connection = null;
		if(dbType.equals("0")) {
			
			 try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				try {
					connection= DriverManager.getConnection("jdbc:oracle:thin:@"+dbUrl+":"+dbName,dbUname,dbPswd);
					
				} catch (SQLException e) {
					
					e.printStackTrace();
				}finally {
					try {
						connection.close();
					} catch (SQLException e) {
						
						e.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
		}
			if(dbType.equals("1")) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					try {
						connection = DriverManager.getConnection("jdbc:mysql://"+dbUrl+"/"+dbName+"?useUnicode=true&characterEncoding="+dbCharSet,dbUname,dbPswd);
					} catch (SQLException e) {
						
						e.printStackTrace();
					}finally {
						try {
							connection.close();
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
					}
				} catch (ClassNotFoundException e) {//jdbc:oracle:thin:@localhost:1521:SID
					                    // jdbc:mysql://192.168.104.9:3306/mydb?useUnicode=true&characterEncoding=utf8
					
					e.printStackTrace();
				}
			}
		 
		//验证穿过来的数据
		if(dbType == null || dbType.equals("")  || dbUrl == null || dbUrl.equals("")||dbName == null || dbName.equals("") || dbCharSet == null || dbCharSet.equals("")
				|| dbUname ==null || dbUname.equals("") || dbPswd ==null || dbPswd.equals("")) {
			map.put("status", -1);
			map.put("msg","参数有误，数据库连接不成功");
		}else {
			
			map.put("status",0);
			map.put("msg","数据库连接成功");
		}
		return map;
	}
    
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> getSelectByTableName(FetchWebRequest<Map<String, String>> fetchWebReq) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> reMap = fetchWebReq.getData();
		//数据库类型
		String dbType = reMap.get("dbType");
		// 数据库地址
		String dbUrl = reMap.get("dbUrl");
		//数据库库名
		String dbName = reMap.get("dbName");
		//数据库字符集
		String dbCharSet = reMap.get("dbCharSet");
		// 数据库用户名
		String dbUname = reMap.get("dbUname");
		//数据库用户密码
		String dbPswd = reMap.get("dbPswd");
		
		Connection connection = null;
		if(dbType.equals("0")) {
		 try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			try {
				connection= DriverManager.getConnection("jdbc:oracle:thin:@"+dbUrl+":"+dbName,dbUname,dbPswd);
				 DatabaseMetaData bData = connection.getMetaData();
			        ResultSet tables = bData.getTables(null, null, null,  new String[] { "TABLE" });
			        JSONArray tArray = new JSONArray();
			        while (tables.next()) {
			           //获得表名
			             String table_name = tables.getString("TABLE_NAME");

			            tArray.add(table_name);

			        }
			        map.put("tableName",tArray);
			        
			} catch (SQLException e) {
				
				e.printStackTrace();
			}finally {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		}
		if(dbType.equals("1")) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				try {
					connection = DriverManager.getConnection("jdbc:mysql://"+dbUrl+"/"+dbName+"?useUnicode=true&characterEncoding="+dbCharSet,dbUname,dbPswd);
					//获得元数据
			        DatabaseMetaData metaData = connection.getMetaData();
			        //获得表信息
			        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
			        JSONArray tArray = new JSONArray();
			        while(tables.next()) {
			        	//获得表名
			        	String tableName = tables.getString("TABLE_NAME");
			        	tArray.add(tableName);
			        }
			        map.put("tableName",tArray);
				} catch (SQLException e) {
					
					e.printStackTrace();
				}finally {
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
		}
		
        return map;  
	}
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> getSelectByTableFile(FetchWebRequest<Map<String, String>> fetchWebReq) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> reMap = fetchWebReq.getData();
		//数据库类型
		String dbType = reMap.get("dbType");
		// 数据库地址
		String dbUrl = reMap.get("dbUrl");
		//数据库库名
		String dbName = reMap.get("dbName");
		//数据库字符集
		String dbCharSet = reMap.get("dbCharSet");
		// 数据库用户名
		String dbUname = reMap.get("dbUname");
		//数据库用户密码
		String dbPswd = reMap.get("dbPswd");
		//数据库表名
		String tableName = reMap.get("tableName");
		
		Connection connection = null;
		if(dbType.equals("0")) {
			 try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				try {
					connection= DriverManager.getConnection("jdbc:oracle:thin:@"+dbUrl+":"+dbName,dbUname,dbPswd);
					//获得元数据
			        DatabaseMetaData metaData = connection.getMetaData();
			        //获得表信息
			        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
			        while(tables.next()) {
			        	JSONArray columArray = new JSONArray();
			        	//获取所有字段名
			        	ResultSet columns =metaData.getColumns(null, null, tableName.toUpperCase(), "%");
			        	while(columns.next()) {
			        		//获得字段名
			        		String columnName = columns.getString("COLUMN_NAME");
			        
			        		columArray.add(columnName);
			        	}
			        	map.put("data",columArray);
			        }
				} catch (SQLException e) {
					
					e.printStackTrace();
				}finally {
					try {
						connection.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			}
			}
			if(dbType.equals("1")) {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					try {
						connection = DriverManager.getConnection("jdbc:mysql://"+dbUrl+"/"+dbName+"?useUnicode=true&characterEncoding="+dbCharSet,dbUname,dbPswd);
						//获得元数据
				        DatabaseMetaData metaData = connection.getMetaData();
				        //获得表信息
				        ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
				        while(tables.next()) {
				        	JSONArray columArray = new JSONArray();
				        	//获取所有字段名
				        	ResultSet columns =metaData.getColumns(null, null, tableName.toUpperCase(), "%");
				        	while(columns.next()) {
				        		//获得字段名
				        		String columnName = columns.getString("COLUMN_NAME");
				        
				        		columArray.add(columnName);
				        	}
				        	map.put("data",columArray);
				        }
					} catch (SQLException e) {
						
						e.printStackTrace();
					}finally {
						try {
							connection.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (ClassNotFoundException e) {
					
					e.printStackTrace();
				}
			}
			
		        return map;
	}

}
