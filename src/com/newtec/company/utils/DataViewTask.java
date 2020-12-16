package com.newtec.company.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSONObject;
import com.newtec.company.entity.collect.MapperInfo;
import com.newtec.thread.CollectThread;

public class DataViewTask implements Job {
	
//	private static final String targetID = "tar_customerInfo";//目标表
//	private static final String souID = "sou_customerInfo";//来源表
	private static Connection sou_Conn;//来源表链接
	private static Connection tar_Conn;//目标表链接

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
//			sou_Conn = DBMySql.getConnection(DBLimit.getLimitInfo(souID).getUrl(),DBLimit.getLimitInfo(souID).getUsername(),DBLimit.getLimitInfo(souID).getPassword());		
//			tar_Conn = DBOrcal.getConnection(DBLimit.getLimitInfo(targetID).getUrl(),DBLimit.getLimitInfo(targetID).getUsername(),DBLimit.getLimitInfo(targetID).getPassword());
			
			// 获取表信息
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			// 打印表信息
			// 表中文名称
			String chName = jobDataMap.getString("chName");
			// 来源表英文名称
			String souName = jobDataMap.getString("souName");
			// 目标表英文名称
			String tarName = jobDataMap.getString("tarName");
			// 定时任务scheduler的名称
			String schedulerName = jobDataMap.getString("schedulerName");
			// 来源数据库 信息
			String souUrl = jobDataMap.getString("souUrl");
			String souType = jobDataMap.getString("souType");
			String souUname = jobDataMap.getString("souUname");
			String souPswd = jobDataMap.getString("souPswd");
			String souTableColumns = jobDataMap.getString("souTableColumns");
			String souTableCond = jobDataMap.getString("souTableCond");
			String sou_db_id = jobDataMap.getString("sou_db_id");
			// 目标数据库 信息
			String tarUrl = jobDataMap.getString("tarUrl");
			String tarType = jobDataMap.getString("tarType");
			String tarUname = jobDataMap.getString("tarUname");
			String tarPswd = jobDataMap.getString("tarPswd");
			String tarTableColumns = jobDataMap.getString("tarTableColumns");
			String tarTableCond = jobDataMap.getString("tarTableCond");
			String tar_db_id = jobDataMap.getString("tar_db_id");
			
			
//			System.out.println(" chName ---- "+chName+"         souName ----  " + souName +"       tarName ------- "+tarName);
//			System.out.println("schedulerName ------------"+schedulerName);
			
			// 来源表数量 
			sou_Conn = findConnectionByMsg(souUrl, souType, souUname, souPswd);
			int sou_Count = getCount(sou_Conn, souName, souTableCond);
			// 目标表数量
			tar_Conn = findConnectionByMsg(tarUrl, tarType, tarUname, tarPswd);
			int tar_Count= getCount(tar_Conn, tarName, tarTableCond);
			
			System.out.println("对比表并插入  【"+souName+"("+tarName+")】  信息");
			JSONObject mapper = new JSONObject();
			
			mapper.put("tar_name", tarName);
			mapper.put("tar_db_id", tar_db_id);
			mapper.put("sou_name", souName);
			mapper.put("sou_db_id", sou_db_id);
			mapper.put("ch_name", chName);
			
			CollectThread.insertData(mapper, sou_Count, tar_Count);
			// 休眠1秒 防制插入数据失败
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {			
			if(tar_Conn != null) {
				try {
					tar_Conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(sou_Conn != null) {
				try {
					sou_Conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static int getCount(Connection sou_Conn2, String souName, String souTableCond) {
		String sql = "select count(1) as count from "+souName + ( (  souTableCond == null || souTableCond.equals("") ) ? "" : " where " + souTableCond );
		ResultSet rs = null;
		PreparedStatement pre = null;
		int rowCount = 0;
		try {
			pre = sou_Conn2.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = pre.executeQuery();
			while(rs.next())
				rowCount = Integer.valueOf(rs.getString("count"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("表"+souName+"信息对比失败");
		}
		finally {
			
				try {
					if(rs != null)
						rs.close();
					if(pre != null) {
						pre.close();
					}
					if(sou_Conn2 != null) {
						sou_Conn2.close();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return rowCount;
	}

	/**
	 * 根据类型 和 url name pswd 获得数据库连接
	 * @param souUrl 
	 * @param souType
	 * @param souUname
	 * @param souPswd
	 * @return
	 */
	public static Connection findConnectionByMsg(String souUrl, String souType, String souUname, String souPswd) {
		if(souType.equals("mysql")) {
			return DBMySql.getConnection(souUrl, souUname, souPswd);
		}else if(souType.equals("oracle")) {
			return DBOrcal.getConnection(souUrl, souUname, souPswd);
		}
		
		return null;
	}

	/**
	 * 获取Connection by Id
	 * @param id 数据库名称
	 * @return
	 * @throws Exception
	 */
	public static Connection findConnectionByMsg(String id) throws Exception {
		return DBMySql.getConnection(DBLimit.getLimitInfo(id).getUrl(),DBLimit.getLimitInfo(id).getUsername(),DBLimit.getLimitInfo(id).getPassword());
	}
}
