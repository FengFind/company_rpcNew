package com.newtec.company.utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.newtec.company.entity.collect.MapperInfo;
import com.newtec.thread.CollectThread;

public class DataViewTask implements Job {
	
	private static final String targetID = "tar_customerInfo";//目标表
	private static final String souID = "sou_customerInfo";//来源表
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
			
			System.out.println(" chName ---- "+chName+"         souName ----  " + souName +"       tarName ------- "+tarName);
//			System.out.println("schedulerName ------------"+schedulerName);
			
			// 来源表数量 
//			sou_Conn = findConnectionByMsg("sou_customerInfo");
//			int sou_Count = CollectThread.getCount(sou_Conn, souName);
//			// 目标表数量
//			tar_Conn = findConnectionByMsg("tar_customerInfo");
//			int tar_Count= CollectThread.getCount(tar_Conn, tarName);
//			System.out.println("对比表并插入  【"+chName+"("+souName+")】  信息");
//			MapperInfo mapper = new MapperInfo();
//			
//			mapper.setCh_name(chName);
//			mapper.setSou_name(souName);
//			mapper.setTar_name(tarName);
//			
//			CollectThread.insertData(mapper, sou_Count, tar_Count);
			// 休眠1秒 防制插入数据失败
//			Thread.sleep(1000);
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
