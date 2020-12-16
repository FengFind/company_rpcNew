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

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.SchedulerRepository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtec.company.entity.collect.CollectInfo;
import com.newtec.company.service.CollectService;
import com.newtec.company.utils.CollectUtil;
import com.newtec.company.utils.DBLimit;
import com.newtec.company.utils.DataViewMain;
import com.newtec.company.utils.DataViewTask;
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
			String strPageSize = resMap.get("pageSize");
			String sql = resMap.get("sql");
			String ch_name = resMap.get("ch_name");
			String sou_name = resMap.get("sou_name");
			String target_name = resMap.get("target_name");
			String souDbIds = resMap.get("souDbIds");
			String tarDbIds = resMap.get("tarDbIds");
			int count = 1;
			
			// 检验是否勾选了数据源
			String cond = "";
			
			if(souDbIds != null && !souDbIds.equals("")) {
				cond += " and dm.sou_db_id in (";
				String[] sda = souDbIds.split(",");
				for (int i = 0; i < sda.length; i++) {
					if (sda[i] != null && !sda[i].equals("")) {
						cond += "'"+sda[i]+"',";
					}
				}
				cond = cond.substring(0, cond.length() - 1)+") ";
			}
			
			if(tarDbIds != null && !tarDbIds.equals("")) {
				cond += " and dm.tar_db_id in (";
				String[] sda = tarDbIds.split(",");
				for (int i = 0; i < sda.length; i++) {
					if (sda[i] != null && !sda[i].equals("")) {
						cond += "'"+sda[i]+"',";
					}
				}
				cond = cond.substring(0, cond.length() - 1)+") ";
			}
			
			if((ch_name == null || ch_name.equals("")) 
					&& (sou_name == null || sou_name.equals(""))
					&& (target_name == null || target_name.equals(""))) {
				count = getCount(connection,"select count(1) as count from db_mapper dm where dm.state = '0'" + cond);
			}
			
			Integer pageNum = StringUtils.isStrNull(strPageNum)?1:Integer.valueOf(strPageNum);
			Integer ps = StringUtils.isStrNull(strPageSize)?pageSize:Integer.valueOf(strPageSize);
			List<CollectInfo> list = getCollectInfo(connection, pageNum, count, sql, ps, cond);
			map.put("pageNum", pageNum);
			map.put("data", list);
			
			// 设置分页数据
			Map m = new HashMap();
			
			// 总页数
			m.put("total",  (count % ps == 0) ? count/ps : (count/ps + 1));
			
			map.put("pageMsg", m);
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

			// 设置分页数据
			String sql = " select count(*) as count "
					+ "	from db_collect dc "
					+ " inner join db_limit dl on dl.id=dc.sou_id "
					+ " inner join db_limit dlt on dlt.id=dc.target_id "
					+ " where dc.target_name = '"+tableName+"' "
					+ " order by create_time desc";
			
			System.out.println(sql);
			
			int count = getCount(connection, sql);
			Map m = new HashMap();
			
			// 总页数
			m.put("total",  (count % pageSize == 0) ? count/pageSize : (count/pageSize + 1));
			
			map.put("pageMsg", m);
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
		public static int getCount(Connection conn, String sql) {
			ResultSet rs = null;
			PreparedStatement pre = null;
			int rowCount = 0;
			try {
				System.out.println(sql);
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
		public static List<CollectInfo> getCollectInfo(Connection conn,Integer pageNum,int count,String sql, int ps, 
				String cond)throws Exception{
			PreparedStatement pre = null;
			ResultSet rs = null;
			List<CollectInfo> list = new ArrayList<CollectInfo>();
//			String ssql = "select * from ("
//					+ " select dc.*,dl.sysname as sou_sys_name, dlt.sysname as tar_sys_name,dm.task_cron as task_cron "
//					+ "	from db_mapper dm "
//					+ " inner join db_collect dc on dm.ch_name=dc.ch_name and dm.tar_name=dc.target_name and dm.sou_name=dc.sou_name "
//					+ " inner join db_limit dl on dl.id=dc.sou_id "
//					+ " inner join db_limit dlt on dlt.id=dc.target_id "
//					+ " group by dc.ch_name order by create_time desc limit ?) as a limit ?,?";
			
			String ssql = "select * from ("
					+ " select "
					+ " dc.id,dc.target_count,max(dc.create_time) as create_time,dc.sou_count,dc.same, "
					+ " dm.tar_name,dm.sou_name,dm.sou_db_id,dm.tar_db_id,dm.create_time as dmct,dm.ch_name,dm.task_status, "
					+ " dl.sysname AS sou_sys_name, "
					+ " dlt.sysname AS tar_sys_name, "
					+ " dm.task_cron AS task_cron  "
					+ " from db_mapper dm "
					+ " INNER JOIN db_limit dl ON dl.id = dm.sou_db_id "
					+ " INNER JOIN db_limit dlt ON dlt.id = dm.tar_db_id "
					+ " LEFT JOIN db_collect dc ON dm.ch_name = dc.ch_name AND dm.tar_name = dc.target_name AND dm.sou_name = dc.sou_name "
					+ " where dm.state='0' "
					+ cond
					+ " group by dc.ch_name,dm.tar_name,dm.sou_name,dm.sou_db_id,dm.tar_db_id order by dmct desc, dc.create_time desc limit ?) as a limit ?,?";
			
			if(sql != null && !sql.equals("")) {
				ssql = "select * from ("
						+ " select "
						+ " dc.id,dc.target_count,max(dc.create_time) as create_time,dc.sou_count,dc.same, "
						+ " dm.tar_name,dm.sou_name,dm.sou_db_id,dm.tar_db_id,dm.create_time as dmct,dm.ch_name,dm.task_status, "
						+ " dl.sysname AS sou_sys_name, "
						+ " dlt.sysname AS tar_sys_name, "
						+ " dm.task_cron AS task_cron  "
						+ " from ( "
						+ sql.replaceAll("\n", "")
						+ " ) as b"
						+ " inner join db_limit dl on dl.id=b.sou_id "
						+ " inner join db_limit dlt on dlt.id=b.target_id "
						+ " inner join db_mapper dm on dm.ch_name=dc.ch_name and dm.tar_name=dc.target_name and dm.sou_name=dc.sou_name "
						+ " where 1=1 "
						+ cond
						+ " group by dc.ch_name,dm.tar_name,dm.sou_name,dm.sou_db_id,dm.tar_db_id order by dmct desc, dc.create_time desc limit ? ) as a limit ?,?";
			}
			System.out.println(ssql);
			try {
				pre = conn.prepareStatement(ssql);
				pre.setInt(1, count);
				pre.setInt(2, (pageNum - 1) * ps);
				pre.setInt(3, ps);
				rs = pre.executeQuery();
				CollectInfo c = null;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while(rs.next()) {
					c = new CollectInfo();
					c.setCh_name(rs.getObject("ch_name") == null ? "" : rs.getString("ch_name"));
					c.setSou_ch_name(rs.getObject("ch_name") == null ? "" : rs.getString("ch_name"));
					c.setTar_ch_name(rs.getObject("ch_name") == null ? "" : rs.getString("ch_name"));
					c.setCreate_time(rs.getObject("create_time") == null ? "" : sdf.format(sdf.parse(rs.getString("create_time"))) );
					c.setId(rs.getObject("id") == null ? "" : rs.getString("id"));
					c.setSame(rs.getObject("same") == null ? "" : rs.getString("same"));
					c.setSou_count(rs.getObject("sou_count") == null ? 0 : rs.getInt("sou_count"));
					c.setTarget_count(rs.getObject("target_count") == null ? 0 : rs.getInt("target_count"));
					c.setSou_id(rs.getObject("sou_db_id") == null ? "" : rs.getString("sou_db_id"));
					c.setSou_name(rs.getObject("sou_name") == null ? "" : rs.getString("sou_name"));
					c.setTarget_name(rs.getObject("tar_name") == null ? "" : rs.getString("tar_name"));
					c.setTarget_id(rs.getObject("tar_db_id") == null ? "" : rs.getString("tar_db_id"));
					c.setSou_sys_name(rs.getObject("sou_sys_name") == null ? "" : rs.getString("sou_sys_name"));
					c.setTar_sys_name(rs.getObject("tar_sys_name") == null ? "" : rs.getString("tar_sys_name"));
					c.setDmct(rs.getObject("dmct") == null ? "" : sdf.format(sdf.parse(rs.getString("dmct"))) );
					c.setTask_status(rs.getObject("task_status") == null ? "" : rs.getString("task_status"));
					
					// 加入 定时器执行状态
//					c.setTask_status(DataViewMain.checkTaskRunStatus(rs.getObject("tar_name") == null ? "" : rs.getString("tar_name"))+"");
					// 加入 定时器 运行cron表达式
					if(rs.getString("task_cron") == null || rs.getString("task_cron").equals("")) {
						c.setTask_cron("");
					}else {
						c.setTask_cron(rs.getString("task_cron"));
					}
					
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

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> dealCollectQuartz(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 目标英文表名
				String targetName = resMap.get("target_name");
				// 定时任务名称是根据目标英文表名设置的 
				// 命名规则为 targetName+Scheduler
				// 循环定时任务
				for (Scheduler scheduler : DataViewMain.getSchedList()) {
					// 如果scheduler的名称与targetName+Scheduler 相等 
					// 表示 该scheduler是要找的作业 否则 继续循环
					if(!scheduler.getSchedulerName().equals(targetName+"Scheduler")) {
						continue;
					}
					
					// 获取JobDetail
					JobDetail job = scheduler.getJobDetail(targetName, "dataView");					
					// 获取类型
//					String type = resMap.get("type");
					// 获取值
//					String val = resMap.get("val");
					
					//对数值进行判断 如果不是整数 直接返回错误信息
//					map = DataViewMain.checkSetVal(Integer.parseInt(type), val);
					
					// 如果map中的status 不是 1 表示数据有问题直接返回
//					if(!map.get("status").toString().equals("1")) {
//						break;
//					}
					
					// 时间规则
//					String gz = map.get("gz").toString();
					String gz = resMap.get("cron");
					System.out.println(" gz ----- " + gz);
					CronTrigger trigger = new CronTrigger(targetName+"Trigger", targetName, gz);
					scheduler.deleteJob(targetName, "dataView");
					scheduler.scheduleJob(job, trigger);
					scheduler.start();
					
					// 将gz 更新到数据库中
					Connection connection = DBLimit.getConnection();
					// sql
					String sql = "update db_mapper set task_cron=? where tar_name = ? ";
					PreparedStatement pstmt=connection.prepareStatement(sql);
					pstmt.setString(1, gz);
					pstmt.setString(2, targetName);
					int n=pstmt.executeUpdate();
					
					if(n == 1) {
						map.put("status", "1");
					}else {
						map.put("status", "-2");
						map.put("msg", "目标表"+targetName+"更新task_cron为"+gz+"失败");
					}
					
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常!");
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> stopCollectQuartz(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 目标英文表名
				String targetName = resMap.get("target_name");
				// 来源英文表名
				String souName = resMap.get("sou_name");
				// 中文表名
				String chName = resMap.get("ch_name");
				// 定时任务名称是根据目标英文表名设置的 
				// 命名规则为 targetName+Scheduler
				// 循环定时任务
				// 计数
				int js = 0;
				for (Scheduler scheduler : DataViewMain.getSchedList()) {
					js++;
					// 如果scheduler的名称与targetName+Scheduler 相等 
					// 表示 该scheduler是要找的作业 否则 继续循环
					if(!scheduler.getSchedulerName().equals(targetName+"Scheduler")) {
						continue;
					}
					
					// 先停掉定时任务 再将定时任务从列表中删除
					scheduler.shutdown();
					SchedulerRepository schedRep = SchedulerRepository.getInstance();
					schedRep.remove(scheduler.getSchedulerName());
					break;
				}
				
				DataViewMain.getSchedList().remove(--js);
				
				// 更新 dbmapper 数据状态
				int res = CollectUtil.updateDbMapperTaskStatus(chName, souName, targetName, "0");
				
				if(res < 0) {
					map.put("status", "-2");
					map.put("msg", "数据库更新异常！");
				}else {
					map.put("status", "1");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> startCollectQuartz(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 目标英文表名
				String targetName = resMap.get("target_name");
				// 目标英文表名
				String souName = resMap.get("sou_name");
				// 目标英文表名
				String chName = resMap.get("ch_name");
				// 作业列表
				List<Scheduler> schedList = DataViewMain.getSchedList();
				// 创建LzstoneTimeTask的定时任务
				JobDataMap jobDataMap = new JobDataMap();
				// 来源表英文名称
				jobDataMap.put("souName", souName);
				// 目标表英文名称
				jobDataMap.put("tarName", targetName);
				// 中文名称
				jobDataMap.put("chName", chName);
				
				JobDetail jobDetail = new JobDetail(targetName, "dataView", DataViewTask.class);
				
				jobDetail.setJobDataMap(jobDataMap);
				
				// 目标 创建任务计划
				CronTrigger trigger = new CronTrigger(targetName+"Trigger", targetName, "0 */1 * * * ?");
				// 0 0 12 * * ? 代表每天的中午12点触发
				
				Scheduler sched = DataViewMain.getSchedulerByName(targetName+"Scheduler");
				sched.scheduleJob(jobDetail, trigger);
				sched.start();
				
				schedList.add(sched);
				
				// 更新 dbmapper 数据状态
				int res = CollectUtil.updateDbMapperTaskStatus(chName, souName, targetName, "1");
				
				if(res < 0) {
					map.put("status", "-2");
					map.put("msg", "数据库更新异常！");
				}else {
					map.put("status", "1");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> saveNewCollectMsg(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 先获取来源表数据库信息 并保存数据库信息
				String souDbType = resMap.get("souDbType");
				String souDbUrl = resMap.get("souDbUrl");
				String souDbName = resMap.get("souDbName");
				String souDbChar = resMap.get("souDbChar");
				String souDbUname = resMap.get("souDbUname");
				String souDbPswd = resMap.get("souDbPswd");
				String souSysname = resMap.get("souSysname");
				
				JSONObject sou = CollectUtil.saveDbLimitByDbMsg(souDbType, souDbUrl, souDbName, souDbChar, souDbUname, souDbPswd, souSysname);
				
				if(sou == null) {
					map.put("status", "-1");
					map.put("msg", "系统异常！");
					return map;
				}
				
				// 再获取目标表数据库信息并保存数据库信息
				String tarDbType = resMap.get("tarDbType");
				String tarDbUrl = resMap.get("tarDbUrl");
				String tarDbName = resMap.get("tarDbName");
				String tarDbChar = resMap.get("tarDbChar");
				String tarDbUname = resMap.get("tarDbUname");
				String tarDbPswd = resMap.get("tarDbPswd");
				String tarSysname = resMap.get("tarSysname");
				
				JSONObject tar = CollectUtil.saveDbLimitByDbMsg(tarDbType, tarDbUrl, tarDbName, tarDbChar, tarDbUname, tarDbPswd, tarSysname);
				
				if(tar == null) {
					map.put("status", "-1");
					map.put("msg", "系统异常！");
					return map;
				}
				
				// 然后保存来源表和目标表信息
				// 中文表名
				String chName = resMap.get("chName");
				// 来源 英文表名
				String souName = resMap.get("souName");
				// 目标 英文表名
				String tarName = resMap.get("tarName");
				// 来源表 表中字段
				String souTableColumns = resMap.get("souTableColumns");
				// 目标表 表中字段
				String tarTableColumns = resMap.get("tarTableColumns");
				// 来源表 查询条件
				String souTableCond = resMap.get("souTableCond");
		        // 目标表 查询条件
		        String tarTableCond = resMap.get("tarTableCond");
		        // 类型
		        String operType = resMap.get("operType");
		        
		        boolean res = CollectUtil.saveDbMapperMsg(chName, souName, tarName, sou.getString("id"), tar.getString("id"), souTableColumns, tarTableColumns, souTableCond, tarTableCond, operType);
		        
		        if(!res) {
		        	map.put("status", "-1");
					map.put("msg", "系统异常！");
					return map;
		        }
		        
				map.put("status", "0");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> testDbMsgConnect(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 先获取来源表数据库信息 并保存数据库信息
				String dbType = resMap.get("dbType");
				String dbUrl = resMap.get("dbUrl");
				String dbName = resMap.get("dbName");
				String dbCharSet = resMap.get("dbCharSet");
				String dbUname = resMap.get("dbUname");
				String dbPswd = resMap.get("dbPswd");
				
				boolean res = CollectUtil.testDbMsgConnect(dbType, dbUrl, dbName, dbCharSet, dbUname, dbPswd);
				
				if(!res) {
					map.put("status", "-2");
					map.put("msg", "获取连接失败！您输入的信息有误，请查证后再测");
					return map;
				}
				
				map.put("status", "0");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findDbInfoById(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 先获取来源表数据库信息 并保存数据库信息
				String id = resMap.get("id");
				
				JSONObject res = CollectUtil.findDbInfoById(id);
				
				map.put("dbMsg", res);
				map.put("status", "0");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findMapperInfoById(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 先获取来源表数据库信息 并保存数据库信息
				String chName = resMap.get("chName"); 
				String souName = resMap.get("souName"); 
				String souDbId = resMap.get("souDbId");
				String tarName = resMap.get("tarName"); 
				String tarDbId = resMap.get("tarDbId");
				
				JSONObject res = CollectUtil.findMapperInfoById(chName, souName, souDbId, tarName, tarDbId);
				
				map.put("dbMsg", res);
				map.put("status", "0");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> doOnceTask(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 先获取来源表数据库信息 并保存数据库信息
				String chName = resMap.get("chName"); 
				String souName = resMap.get("souName"); 
				String souDbId = resMap.get("souDbId");
				String tarName = resMap.get("tarName"); 
				String tarDbId = resMap.get("tarDbId");
				
				JSONObject res = CollectUtil.doOnceTask(chName, souName, souDbId, tarName, tarDbId);
				
				map.put("dbMsg", res);
				map.put("status", "0");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> selectTableNames(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 先获取来源表数据库信息 并保存数据库信息
				//数据库类型
				String dbType = resMap.get("dbType");
				// 数据库地址
				String dbUrl = resMap.get("dbUrl");
				//数据库库名
				String dbName = resMap.get("dbName");
				//数据库字符集
				String dbCharSet = resMap.get("dbCharSet");
				// 数据库用户名
				String dbUname = resMap.get("dbUname");
				//数据库用户密码
				String dbPswd = resMap.get("dbPswd");
				
				JSONArray res = CollectUtil.selectTableNames(dbType, dbUrl, dbName, dbCharSet, dbUname, dbPswd);
				
				map.put("tableName", res);
				map.put("status", "0");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> selectColumnsNamesByTable(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				// 获取参数值
				Map<String, String> resMap = fetchWebReq.getData();
				// 先获取来源表数据库信息 并保存数据库信息
				//数据库类型
				String dbType = resMap.get("dbType");
				// 数据库地址
				String dbUrl = resMap.get("dbUrl");
				//数据库库名
				String dbName = resMap.get("dbName");
				//数据库字符集
				String dbCharSet = resMap.get("dbCharSet");
				// 数据库用户名
				String dbUname = resMap.get("dbUname");
				//数据库用户密码
				String dbPswd = resMap.get("dbPswd");
				//数据库表名
				String tableName = resMap.get("tableName");
				
				JSONArray res = CollectUtil.selectColumnsNamesByTable(dbType, dbUrl, dbName, dbCharSet, dbUname, dbPswd, tableName);
				
				map.put("data", res);
				map.put("status", "0");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findDbAry(FetchWebRequest<Map<String, String>> fetchWebReq) {
			Map<String,Object> map = new HashMap<String, Object>();
			try {
				JSONObject res = CollectUtil.findDbAry();
				
				if(res.getInteger("status") == 0) {
					map.put("souDbAry", res.get("souDbAry"));
					map.put("tarDbAry", res.get("tarDbAry"));
					map.put("status", "0");
				}else {
					map.put("status", "-2");
					map.put("msg", "查询异常！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}
}
