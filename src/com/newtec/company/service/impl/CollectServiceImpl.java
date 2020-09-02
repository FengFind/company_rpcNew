package com.newtec.company.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
			Map<String, String> resMap = fetchWebReq.getData();
			String strPageNum = resMap.get("pageNum");
			String sql = resMap.get("sql");
			String ch_name = resMap.get("ch_name");
			String sou_name = resMap.get("sou_name");
			String target_name = resMap.get("target_name");
			int count = 1;
			
			if((ch_name == null || ch_name.equals("")) 
					&& (sou_name == null || sou_name.equals(""))
					&& (target_name == null || target_name.equals(""))) {
				count = getCount(connection);
			}
			
			Integer pageNum = StringUtils.isStrNull(strPageNum)?1:Integer.valueOf(strPageNum);
			List<CollectInfo> list = getCollectInfo(connection, pageNum, count, sql);
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
	public Map<String, Object> checkSql(FetchWebRequest<Map<String, String>> fetchWebReq) {
		// TODO Auto-generated method stub
		
		Map<String,Object> map = new HashMap<String, Object>();
		Connection connection = DBLimit.getConnection();
		try{
			Map<String, String> resMap = fetchWebReq.getData();
			String checkSql = resMap.get("checkSql");
			// 验证传递过来的sql 
			if(checkSql == null || checkSql.equals("")) {
				map.put("status", -1);
				map.put("msg", "您输入的sql为null或者空");
			}else if(checkSql.indexOf("db_limit") == -1 && checkSql.indexOf("db_collect") == -1
					&& checkSql.indexOf("db_mapper") == -1) {
				map.put("status", -1);
				map.put("msg", "您输入的sql中必须包含数据库中的表名db_limit、db_collect、db_mapper其中的一个");
			}else if(checkSql.indexOf("delete") > -1 || checkSql.indexOf("update") > -1
					|| checkSql.indexOf("create") > -1) {
				map.put("status", -1);
				map.put("msg", "您输入的sql中不能包含delete、update、create等关键字");
			}else{
				System.out.println(checkSql.replaceAll("\n", ""));
				
				PreparedStatement pre = connection.prepareStatement(checkSql.replaceAll("\n", ""));
				ResultSet rs = pre.executeQuery();
				
				map.put("status", 0);
				map.put("msg", "测试通过");
			}
		}catch (Exception e) {
			e.printStackTrace();
			map.put("status", -1);
			map.put("msg", e.getMessage());
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
		public static List<CollectInfo> getCollectInfo(Connection conn,Integer pageNum,int count,String sql)throws Exception{
			PreparedStatement pre = null;
			ResultSet rs = null;
			List<CollectInfo> list = new ArrayList<CollectInfo>();
			String ssql = "select * from ("
					+ " select dc.*,dl.sysname as sou_sys_name, dlt.sysname as tar_sys_name"
					+ "	from db_collect dc "
					+ " inner join db_limit dl on dl.id=dc.sou_id "
					+ " inner join db_limit dlt on dlt.id=dc.target_id "
					+ " order by create_time desc limit ?) as a limit ?,?";
			
			if(sql != null && !sql.equals("")) {
				ssql = "select * from ("
						+ " select b.*,dl.sysname as sou_sys_name, dl.sysname as tar_sys_name"
						+ " from ( "
						+ sql.replaceAll("\n", "")
						+ " ) as b"
						+ " inner join db_limit dl on dl.id=b.sou_id "
						+ " inner join db_limit dlt on dlt.id=b.target_id "
						+ " order by b.create_time desc limit ? ) as a limit ?,?";
			}
			System.out.println(ssql);
			try {
				pre = conn.prepareStatement(ssql);
				pre.setInt(1, count);
				pre.setInt(2, (pageNum - 1) * pageSize);
				pre.setInt(3, pageSize);
				rs = pre.executeQuery();
				CollectInfo c = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while(rs.next()) {
					c = new CollectInfo();
					c.setCh_name(rs.getString("ch_name"));
					c.setSou_ch_name(rs.getString("ch_name"));
					c.setTar_ch_name(rs.getString("ch_name"));
					c.setCreate_time(sdf.format(sdf.parse(rs.getString("create_time"))));
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
		public static List<CollectInfo> getCollectInfoByName(Connection conn,Integer pageNum,String tableName) throws Exception{
			PreparedStatement pre = null;
			ResultSet rs = null;
			List<CollectInfo> list = new ArrayList<CollectInfo>();
			String sql = "select * from ("
					+ " select dc.*,dl.sysname as sou_sys_name, dlt.sysname as tar_sys_name"
					+ "	from db_collect dc "
					+ " inner join db_limit dl on dl.id=dc.sou_id "
					+ " inner join db_limit dlt on dlt.id=dc.target_id "
					+ " where target_name = ? "
					+ " order by create_time desc) as a limit ?,?";
			try {
				pre = conn.prepareStatement(sql);
				pre.setString(1, tableName);
				pre.setInt(2, (pageNum - 1) * pageSize);
				pre.setInt(3, pageSize);
				rs = pre.executeQuery();
				CollectInfo c = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while(rs.next()) {
					c = new CollectInfo();
					c.setCh_name(rs.getString("ch_name"));
					c.setSou_ch_name(rs.getString("ch_name"));
					c.setTar_ch_name(rs.getString("ch_name"));
					c.setCreate_time(sdf.format(sdf.parse(rs.getString("create_time"))));
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

		@Override
		public Map<String, Object> getCollectBySql(FetchWebRequest<Map<String, String>> fetchWebReq) {
			// TODO Auto-generated method stub
			return null;
		}
}
