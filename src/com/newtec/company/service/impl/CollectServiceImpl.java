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

import com.alibaba.fastjson.JSONObject;
import com.newtec.company.entity.collect.CollectInfo;
import com.newtec.company.service.CollectService;
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
			int count = 1;
			
			if((ch_name == null || ch_name.equals("")) 
					&& (sou_name == null || sou_name.equals(""))
					&& (target_name == null || target_name.equals(""))) {
				count = getCount(connection,"select count(1) as count from db_mapper where state = '0'");
			}
			
			Integer pageNum = StringUtils.isStrNull(strPageNum)?1:Integer.valueOf(strPageNum);
			Integer ps = StringUtils.isStrNull(strPageSize)?pageSize:Integer.valueOf(strPageSize);
			List<CollectInfo> list = getCollectInfo(connection, pageNum, count, sql, ps);
			map.put("pageNum", pageNum);
			map.put("data", list);
			
			// 设置分页数据
			Map m = new HashMap();
			
			// 总页数
			m.put("total",  (count % pageSize == 0) ? count/pageSize : (count/pageSize + 1));
			
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
		public static List<CollectInfo> getCollectInfo(Connection conn,Integer pageNum,int count,String sql, int ps)throws Exception{
			PreparedStatement pre = null;
			ResultSet rs = null;
			List<CollectInfo> list = new ArrayList<CollectInfo>();
			String ssql = "select * from ("
					+ " select dc.*,dl.sysname as sou_sys_name, dlt.sysname as tar_sys_name,dm.task_cron as task_cron "
					+ "	from db_collect dc "
					+ " inner join db_limit dl on dl.id=dc.sou_id "
					+ " inner join db_limit dlt on dlt.id=dc.target_id "
					+ " inner join db_mapper dm on dm.ch_name=dc.ch_name and dm.tar_name=dc.target_name and dm.sou_name=dc.sou_name "
					+ " group by dc.ch_name order by create_time desc limit ?) as a limit ?,?";
			
			if(sql != null && !sql.equals("")) {
				ssql = "select * from ("
						+ " select b.*,dl.sysname as sou_sys_name, dl.sysname as tar_sys_name,dm.task_cron as task_cron "
						+ " from ( "
						+ sql.replaceAll("\n", "")
						+ " ) as b"
						+ " inner join db_limit dl on dl.id=b.sou_id "
						+ " inner join db_limit dlt on dlt.id=b.target_id "
						+ " inner join db_mapper dm on dm.ch_name=dc.ch_name and dm.tar_name=dc.target_name and dm.sou_name=dc.sou_name "
						+ " group by b.ch_name order by b.create_time desc limit ? ) as a limit ?,?";
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
					
					// 加入 定时器执行状态
					c.setTask_status(DataViewMain.checkTaskRunStatus(rs.getString("target_name"))+"");
					// 加入 定时器 运行cron表达式
					c.setTask_cron(rs.getString("task_cron") == null || rs.getString("task_cron").equals("") ? "* * * ? * 2 *" : rs.getString("task_cron"));
					
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
				
				map.put("status", "1");
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
				map.put("status", "1");
			} catch (Exception e) {
				e.printStackTrace();
				map.put("status", "-1");
				map.put("msg", "系统异常！");
			}
			
			return map;
		}
}
