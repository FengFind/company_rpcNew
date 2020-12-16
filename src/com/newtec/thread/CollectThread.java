package com.newtec.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.newtec.company.entity.collect.Limit;
import com.newtec.company.entity.collect.MapperInfo;
import com.newtec.company.utils.DBLimit;
import com.newtec.company.utils.DBMySql;
import com.newtec.company.utils.DBOrcal;

public class CollectThread  extends Thread {

	private static final long sleepTime = 60 * 60 * 1000;//定时执行的时间间隔，1个小时 
	
	private static final String targetID = "tar_customerInfo";//目标表
	
	private static final String souID = "sou_customerInfo";//来源表
	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("启动定时任务：");
				long sTime = System.currentTimeMillis();
//				collectInfo();
				getInfo();
				System.out.println("结束定时采集对比任务，共耗时："+(System.currentTimeMillis()-sTime));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void collectInfo() throws SQLException {
		Limit tarInfo = DBLimit.getLimitInfo(targetID);
		Limit souInfo = DBLimit.getLimitInfo(souID);
		getLimit(tarInfo,souInfo);
		
	}
	
	/**
	 * 获取来源表和目标表的连接
	 * @param tarInfo 目标
	 * @param souInfo 来源
	 */
	public static void getLimit(Limit tarInfo,Limit souInfo) {
		
		//来源表链接
		Connection sou_Conn = DBMySql.getConnection(souInfo.getUrl(),souInfo.getUsername(),souInfo.getPassword());
		
		//目标表链接
		Connection tar_Conn = DBOrcal.getConnection(tarInfo.getUrl(),tarInfo.getUsername(),tarInfo.getPassword());
//		getInfo(sou_Conn,tar_Conn);
	}
	
	/**
	 *  获取目标表和来源表的所有表数量，并进行插入
	 * @param conn
	 * @param conn1
	 */
	public static void getInfo() {
		List<JSONObject> mapperList = null;
		try {
			mapperList = DBLimit.getMapper();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.err.println("获取映射关系失败");
		}
		
		for(JSONObject mapper:mapperList) {
			try {
				// 根据数据库类型 选择不同的数据库连接
				Connection sou_Conn = null;
				Connection tar_Conn = null; 
				
				if(mapper.getString("souType").toLowerCase().equals("mysql")) {
					sou_Conn = DBMySql.getConnection(mapper.getString("souUrl"), mapper.getString("souUname"), mapper.getString("souPswd"));
				}else if(mapper.getString("souType").toLowerCase().equals("oracle")) {
					sou_Conn = DBOrcal.getConnection(mapper.getString("souUrl"), mapper.getString("souUname"), mapper.getString("souPswd"));
				}
				
				if(mapper.getString("tarType").toLowerCase().equals("mysql")) {
					tar_Conn = DBMySql.getConnection(mapper.getString("tarUrl"), mapper.getString("tarUname"), mapper.getString("tarPswd"));
				}else if(mapper.getString("tarType").toLowerCase().equals("oracle")) {
					tar_Conn = DBOrcal.getConnection(mapper.getString("tarUrl"), mapper.getString("tarUname"), mapper.getString("tarPswd"));
				}
				
				//去来源表查
				int sou_Count = getCount(sou_Conn,mapper.getString("sou_name"));
				int tar_Count= getCount(tar_Conn, mapper.getString("tar_name"));
				System.err.println("对比表并插入  【"+mapper.getString("ch_name")+"("+mapper.getString("sou_name")+")】  信息");
				insertData(mapper, sou_Count, tar_Count);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}		
	}
	
	
	/**
	 * 统计数量
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static int getCount(Connection conn,String tableName) {
		String sql = "select count(1) as count from "+tableName;
		ResultSet rs = null;
		PreparedStatement pre = null;
		int rowCount = 0;
		try {
			pre = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = pre.executeQuery();
			while(rs.next())
				rowCount = Integer.valueOf(rs.getString("count"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("表"+tableName+"信息对比失败");
		}
		finally {
			
				try {
					if(rs != null)
						rs.close();
					if(pre != null) {
						pre.close();
					}
					if(conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return rowCount;
	}
	
	/**
	 * 插入采集信息
	 * @param tableName
	 * @param stName
	 * @param tcount
	 * @param scount
	 */
	public static void insertData(JSONObject mapper,int scount,int tcount) {
		Connection conn = DBLimit.getConnection();
		UUID uuid = UUID.randomUUID();
		String id =  uuid.toString().replaceAll("-", "");
		LocalDateTime now = LocalDateTime.now();
		String createTime = now.toLocalDate()+" "+now.toLocalTime();
		String same = tcount == scount?"true":"false";
		String sql = "insert into db_collect(id,target_name,target_count,create_time,target_id,sou_name,sou_count,sou_id,same,ch_name) values(?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pre = conn.prepareStatement(sql);
			pre.setString(1, id);
			pre.setString(2, mapper.getString("tar_name"));
			pre.setInt(3, tcount);
			pre.setString(4, createTime);
			pre.setString(5, mapper.getString("tar_db_id"));
			pre.setString(6, mapper.getString("sou_name"));
			pre.setInt(7, scount);
			pre.setString(8, mapper.getString("sou_db_id"));
			pre.setString(9, same);
			pre.setString(10, mapper.getString("ch_name"));
			int update = pre.executeUpdate();
			if(update == 0)
				System.err.println("插入失败");
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
	}

	public static void main(String[] args) throws SQLException {
		collectInfo();
	}
}
