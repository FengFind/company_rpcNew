package com.newtec.company.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.newtec.company.entity.collect.CollectInfo;
import com.newtec.company.service.CollectService;
import com.newtec.company.utils.DBLimit;
import com.newtec.myqdp.server.utils.StringUtils;
import com.newtec.reflect.annotation.RpcClass;
import com.newtec.reflect.annotation.RpcMethod;
import com.newtec.router.request.FetchWebRequest;

@RpcClass(value = "collectService",http = true)
public class CollectServiceImpl implements CollectService{

	private final static Integer pageSize = 20;
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> getCollect(FetchWebRequest<Map<String, String>> fetchWebReq) {
		// TODO Auto-generated method stub
		
		Map<String,Object> map = new HashMap<String, Object>();
		Connection connection = DBLimit.getConnection();
		try{
			//共需要对比的表的数量
			// 对比的表的数量，Map<String,Object> map = new HashMap<String,Object>();
			int count = getCount(connection);
			Map<String, String> resMap = fetchWebReq.getData();
			String strPageNum = resMap.get("pageNum");
			Integer pageNum = StringUtils.isStrNull(strPageNum)?1:Integer.valueOf(strPageNum);
			List<CollectInfo> list = getCollectInfo(connection, pageNum, count);
			map.put("pageNum", pageNum);
			map.put("data", list);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			
			DBLimit.closeConnection(connection);
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> getCollectByName(FetchWebRequest<Map<String, String>> fetchWebReq) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		Connection connection = DBLimit.getConnection();
		
		try {
			Map<String, String> resMap = fetchWebReq.getData();
			String strPageNum = resMap.get("pageNum");
			String tableName = resMap.get("name");
			Integer pageNum = StringUtils.isStrNull(strPageNum)?1:Integer.valueOf(strPageNum);
			List<CollectInfo> list = getCollectInfoByName(connection, pageNum, tableName);
			map.put("pageNum", pageNum);
			map.put("data", list);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			DBLimit.closeConnection(connection);
		}
		return map;
	}
	
		/**
		 * 统计数量
		 * @param conn
		 * @param tableName
		 * @return
		 */
		public static int getCount(Connection conn) {
			String sql = "select count(1) as count from db_mapper where state = '0'";
			ResultSet rs = null;
			PreparedStatement pre = null;
			int rowCount = 0;
			try {
				pre = conn.prepareStatement(sql);
				rs = pre.executeQuery();
				while(rs.next())
					rowCount = Integer.valueOf(rs.getString("count"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				
					try {
						if(rs != null)
							rs.close();
						if(pre != null) {
							pre.close();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
			return rowCount;
		}
		/**
		 * 	分页获取数据
		 * @param conn
		 * @param pageNum
		 * @param count
		 * @return
		 */
		public static List<CollectInfo> getCollectInfo(Connection conn,Integer pageNum,int count){
			PreparedStatement pre = null;
			ResultSet rs = null;
			List<CollectInfo> list = new ArrayList<CollectInfo>();
			String sql = "select * from ("
					+ " select dc.*,dl.sysname as sou_sys_name, dlt.sysname as tar_sys_name"
					+ "	from db_collect dc "
					+ " inner join db_limit dl on dl.id=dc.sou_id "
					+ " inner join db_limit dlt on dlt.id=dc.target_id "
					+ " order by create_time desc limit ?) as a limit ?,?";
			try {
				pre = conn.prepareStatement(sql);
				pre.setInt(1, count);
				pre.setInt(2, (pageNum - 1) * pageSize);
				pre.setInt(3, pageSize);
				rs = pre.executeQuery();
				CollectInfo c = null;
				while(rs.next()) {
					c = new CollectInfo();
					c.setCh_name(rs.getString("ch_name"));
					c.setCreate_time(rs.getString("create_time"));
					c.setId(rs.getString("id"));
					c.setSame(rs.getString("same"));
					c.setSou_count(rs.getInt("sou_count"));
					c.setTarget_count(rs.getInt("target_count"));
					c.setSou_id(rs.getString("sou_id"));
					c.setSou_name(rs.getString("sou_name"));
					c.setTarget_name(rs.getString("target_name"));
					c.setTarget_id(rs.getString("target_id"));
					c.setSou_sys_name(rs.getString("sou_sys_name"));
					c.setTar_sys_name(rs.getString("tar_sys_name"));
					list.add(c);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}
		/**
		 * 	分页获取数据，根据表名查
		 * @param conn
		 * @param pageNum
		 * @param count
		 * @return
		 */
		public static List<CollectInfo> getCollectInfoByName(Connection conn,Integer pageNum,String tableName){
			PreparedStatement pre = null;
			ResultSet rs = null;
			List<CollectInfo> list = new ArrayList<CollectInfo>();
			String sql = "select * from db_collect where target_name = ? ORDER BY create_time desc limit ?,?";
			try {
				pre = conn.prepareStatement(sql);
				pre.setString(1, tableName);
				pre.setInt(2, (pageNum - 1) * pageSize);
				pre.setInt(3, pageSize);
				rs = pre.executeQuery();
				CollectInfo c = null;
				while(rs.next()) {
					c = new CollectInfo();
					c.setCh_name(rs.getString("ch_name"));
					c.setCreate_time(rs.getString("create_time"));
					c.setId(rs.getString("id"));
					c.setSame(rs.getString("same"));
					c.setSou_count(rs.getInt("sou_count"));
					c.setTarget_count(rs.getInt("target_count"));
					c.setSou_id(rs.getString("sou_id"));
					c.setSou_name(rs.getString("sou_name"));
					c.setTarget_name(rs.getString("target_name"));
					c.setTarget_id(rs.getString("target_id"));
					list.add(c);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}

		@Override
		public Map<String, Object> getCollectBySql(FetchWebRequest<Map<String, String>> fetchWebReq) {
			// TODO Auto-generated method stub
			return null;
		}
}
