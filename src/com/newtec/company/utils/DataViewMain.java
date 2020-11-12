package com.newtec.company.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;

import com.newtec.company.entity.collect.MapperInfo;

public class DataViewMain {

	private static List<Scheduler> schedList;

	public static void run() {
		try {
			schedList = new ArrayList<Scheduler>();
			
			// MapperList 表列表
			List<MapperInfo> mapperList = DBLimit.getMapper();
			// 计数
			int js = 0;
			
			for(MapperInfo mapper : mapperList) {
				// 创建LzstoneTimeTask的定时任务
				JobDataMap jobDataMap = new JobDataMap();
				// 来源表英文名称
				jobDataMap.put("souName", mapper.getSou_name());
				// 目标表英文名称
				jobDataMap.put("tarName", mapper.getTar_name());
				// 中文名称
				jobDataMap.put("chName", mapper.getCh_name());
				// 定时任务scheduler的名称
				jobDataMap.put("schedulerName", mapper.getTar_name()+"Scheduler");
				
				JobDetail jobDetail = new JobDetail(mapper.getTar_name(), "dataView", DataViewTask.class);
//				JobDetail jobDetail = new JobDetail("dv"+(++js),"ddd", DataViewTask.class);
				
				jobDetail.setJobDataMap(jobDataMap);
				
				// 目标 创建任务计划
//				CronTrigger trigger = new CronTrigger(mapper.getTar_name()+"Trigger", mapper.getTar_name(), "10/5 * * * * ?");
				CronTrigger trigger = new CronTrigger(mapper.getTar_name()+"Trigger", mapper.getTar_name(), "* * * ? * 1-2 *");
				// 0 0 12 * * ? 代表每天的中午12点触发
				
				Scheduler sched = getSchedulerByName(mapper.getTar_name()+"Scheduler");
				sched.scheduleJob(jobDetail, trigger);
				sched.start();
				
				schedList.add(sched);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 停止
	 * 
	 * @throws Exception
	 */
	public static void stop(Scheduler sched) throws Exception {
		sched.shutdown();
	}

	public static List<Scheduler> getSchedList() {
		return schedList;
	}

	public static void setSchedList(List<Scheduler> schedList) {
		DataViewMain.schedList = schedList;
	}
	
	/**
	 * 根据目标英文表名 检测对应定时器的运行状态
	 * @param targetName 目标英文表名
	 * @return 1 表示正在运行 0 表示没有运行
	 */
	public static int checkTaskRunStatus(String targetName) {
		int res = 0;
		
		try {
			// 作业列表
			List<Scheduler> schedList = DataViewMain.getSchedList();
			// 循环作业列表 根据作业名称进行判断 
			for (Scheduler scheduler : schedList) {
				// 如果scheduler的名称与targetName+Scheduler 相等 
				// 表示 该scheduler是要找的作业 否则 继续循环
				if(!scheduler.getSchedulerName().equals(targetName+"Scheduler")) {
					continue;
				}
				
				res = 1;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	/**
	 * 根据时间类型和时间值 返回状态及提示信息或者时间规则
	 * @param type 时间类型
	 * @param val 时间值
	 * */
	public static Map<String,Object> checkSetVal(int type, String val) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		switch(type) {
			case  0:
				//对小时数进行判断 如果不是整数 直接返回错误信息
				try {
					int h = Integer.parseInt(val);
					if(h < 0 || h > 23) {
						map.put("status", "-2");
						map.put("msg", "小时数不能大于23且是正整数，请重新输入");
					}else {
						map.put("status", "1");
						map.put("gz", "0 0 */"+h+" * * ?");
					}
				} catch (Exception e) {
					map.put("status", "-1");
					map.put("msg", "小时数不是整数，请重新输入");
				}
				break;
			case  1:
				//对分数进行判断 如果不是整数 直接返回错误信息
				try {
					int h = Integer.parseInt(val);
					if(h < 0 || h > 59) {
						map.put("status", "-2");
						map.put("msg", "分 数不能大于59且是正整数，请重新输入");
					}else {
						map.put("status", "1");
						map.put("gz", "0 */"+h+" * * * ?");
					}
				} catch (Exception e) {
					map.put("status", "-1");
					map.put("msg", "分数不是整数，请重新输入");
				}
				break;
			case  2:
				//对秒数进行判断 如果不是整数 直接返回错误信息
				try {
					int h = Integer.parseInt(val);
					if(h < 0 || h > 59) {
						map.put("status", "-2");
						map.put("msg", "秒数不能大于59且是正整数，请重新输入");
					}else {
						map.put("status", "1");
						map.put("gz", "*/"+h+" * * * * ?");
					}
				} catch (Exception e) {
					map.put("status", "-1");
					map.put("msg", "秒数不是整数，请重新输入");
				}
				break;
			case  3:
				//对天数进行判断 如果不是整数 直接返回错误信息
				try {
					int h = Integer.parseInt(val);
					if(h < 1 || h > 31) {
						map.put("status", "-2");
						map.put("msg", "天数不能大于31且是正整数，请重新输入");
					}else {
						map.put("status", "1");
						map.put("gz", "0 0 0 */"+h+" * ?");
					}
				} catch (Exception e) {
					map.put("status", "-1");
					map.put("msg", "天数不是整数，请重新输入");
				}
				break;
			case  4:
				//对月数进行判断 如果不是整数 直接返回错误信息
				try {
					int h = Integer.parseInt(val);
					if(h < 1 || h > 12) {
						map.put("status", "-2");
						map.put("msg", "月数不能大于12且是正整数，请重新输入");
					}else {
						map.put("status", "1");
						map.put("gz", "0 0 0 1 */"+h+" ?");
					}
				} catch (Exception e) {
					map.put("status", "-1");
					map.put("msg", "月数不是整数，请重新输入");
				}
				break;
			case  5:
				//对周数进行判断 如果不是整数 直接返回错误信息
				try {
					int h = Integer.parseInt(val);
					if(h < 1 || h > 7) {
						map.put("status", "-2");
						map.put("msg", "周数不能大于7且是正整数，请重新输入");
					}else {
						map.put("status", "1");
						map.put("gz", "0 0 0 ? * "+h);
					}
				} catch (Exception e) {
					map.put("status", "-1");
					map.put("msg", "周数不是整数，请重新输入");
				}
				break;
		}
		
		return map;
	}
	
	/**
	 * 返回名字为name的Scheduler 
	 * @param name Scheduler的名称
	 * @return
	 */
	public static Scheduler getSchedulerByName(String name) {
		Scheduler  scheduler = null;
		try {
			StdSchedulerFactory sf =new StdSchedulerFactory();
			Properties props = new Properties();
			
			props.put("org.quartz.scheduler.instanceName", name);
			props.put("org.quartz.scheduler.rmi.export", false);
			props.put("org.quartz.scheduler.rmi.proxy", false);
			props.put("org.quartz.scheduler.wrapJobExecutionInUserTransaction", false);
			props.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
			props.put("org.quartz.threadPool.threadCount", " 10");
			props.put("org.quartz.threadPool.threadPriority", " 5");
			props.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", " true");
			props.put("org.quartz.jobStore.misfireThreshold", " 60000");
			props.put("org.quartz.jobStore.class", " org.quartz.simpl.RAMJobStore");
			
			sf.initialize(props);
			scheduler = sf.getScheduler();
			System.out.println("name 为 "+scheduler.getSchedulerName()+" 的Scheduler 创建成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return scheduler;
	}

	public static void main(String[] args) {
		try {
			DataViewMain.run();
//			Thread.sleep(20*1000);
//			// 创建LzstoneTimeTask的定时任务
//			JobDataMap jobDataMap = new JobDataMap();
//			// 来源表英文名称
//			jobDataMap.put("souName", "souName新");
//			// 目标表英文名称
//			jobDataMap.put("tarName", "tarName新");
//			// 中文名称
//			jobDataMap.put("chName", "chName新");
			
//			JobDetail jobDetail = new JobDetail("dataViewJob", "souName新@tarName新", DataViewTask.class);
			
//			jobDetail.setJobDataMap(jobDataMap);
			
			// 目标 创建任务计划
//			CronTrigger trigger = new CronTrigger("dataViewTrigger-1", "dataView-1", "* * * * * ?");
			// 0 0 12 * * ? 代表每天的中午12点触发
						
//			Scheduler sched = DataViewMain.schedList.get(0);
//			System.out.println("--------------------"+sched.getTriggerGroupNames().length+"----------------------");
//			
//			for(String str : sched.getTriggerGroupNames()) {
//				System.out.println(str + "-----------------");
//			}
			
//			sched.interrupt(, arg1)
			
//			JobDetail xx = sched.getJobDetail("dv1", "ddd");
			
//			System.out.println(xx.getJobClass()+" -- "+xx.getFullName()+" --- "+xx.getName());
			
//			sched.deleteJob("dv1", "ddd");
//			sched.scheduleJob(jobDetail, trigger);
//			sched.start();
			
//			DataViewMain.schedList.get(0).shutdown();
//			System.out.println("-------------------"+DataViewMain.schedList.get(0).getSchedulerName()+"-------------------");
//			SchedulerRepository schedRep = SchedulerRepository.getInstance();
//			schedRep.remove(DataViewMain.schedList.get(0).getSchedulerName());
//			DataViewMain.schedList.remove(DataViewMain.schedList.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		LocalDateTime now = LocalDateTime.now();
//		String createTime = now.toLocalDate()+" "+now.toLocalTime();
//		System.out.println(createTime);
	}
}
