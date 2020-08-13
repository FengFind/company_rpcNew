package com.newtec.rpc.monitor;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;

import com.newtec.json.utils.JsonUtil;
import com.newtec.myqdp.server.utils.StringUtils;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.rpc.call.client.Response;
import com.newtec.rpc.call.core.RpcRequest;
import com.newtec.rpc.call.core.RpcResponse;
import com.newtec.rpc.core.RPCParam;
import com.newtec.rpc.db.DBManager;
import com.newtec.rpc.db.DBexecuteVoid;
import com.newtec.rpc.utils.RpcUtils;

/**
 * @author Yuexin
 * @Description 调用日志监控管理
 * @date  2017年9月19日
 * @version 1.0
 */
public class CallLogManager {

	static private String tableName = "t_monitor_call_log";
	static private String tableErrorName = "t_monitor_call_error_log";
	/**
	 *  	下次更新表名的时间段，初始化为-1，需要进行更新
	 */
	static private long nextUpdateTableTime =  -1;
	
	static private int SQL_LENGTH = 2000;
//	static private Map<String, MonitorCallLogTime> logMap = new ConcurrentHashMap<String, MonitorCallLogTime>();
	/** 保存监控调用日志的队列*/
	static private ConcurrentLinkedQueue<MonitorCallLogTime> monitorCallLogQueue = new ConcurrentLinkedQueue<MonitorCallLogTime>();

//	static public Map<String, MonitorCallLogTime> getLogMap() {
//		return logMap;
//	}

//	/**
//	 * 方法说明: 生成监控调用日志 
//	 */
//	static public MonitorCallLogTime generatorStartLog(RpcRequest rpcRequest, /*String clazz, */String cpu, String mac)
//			throws CustomException {
//		if (!rpcRequest.isWriteLog() || tableName == null)
//			return null;// 本次请求不记录日志
//		MonitorCallLogTime monitorCallLog = new MonitorCallLogTime(rpcRequest, /*clazz,*/ cpu, mac);
////		logMap.put(monitorCallLog.getRequestId(), monitorCallLog);
//		return monitorCallLog;
//	}

	/**
	 * 方法说明:  更新监控调用日志
	 * @param requestId
	 * @param callTime
	 * @param receiverAddr
	 * @throws CustomException 
	 */
	static public MonitorCallLogTime addCallLog(RpcRequest rpcRequest,long localStartTime, String receiverCpu, String receiverMac, String receiverAddr, String errors,
			 RpcResponse resp) throws CustomException {
		return addCallLog(rpcRequest, localStartTime, receiverCpu, receiverMac, receiverAddr, errors, resp==null?-2:resp.getCallTime(), resp);
	}
	static public MonitorCallLogTime addCallLog(RpcRequest rpcRequest,long localStartTime, String receiverCpu, String receiverMac, String receiverAddr,
			String errors, long callTime,Response resp) {
//		MonitorCallLogTime monitorCallLog = logMap.remove(requestId);
		if (!rpcRequest.isWriteLog() || !isSaveLog(errors)) {
			return null;
		}
		MonitorCallLogTime monitorCallLog = new MonitorCallLogTime(localStartTime,rpcRequest, /*clazz,*/ receiverCpu, receiverMac);
		monitorCallLog.setEndTimeLong(System.currentTimeMillis());
		monitorCallLog.setEnd(1);
//		monitorCallLog.setEndTime(StringUtils.getCurrentTime());
//		monitorCallLog.setTotalTime(System.currentTimeMillis() - monitorCallLog.getCallTime());
		monitorCallLog.setCallTime(callTime);
		monitorCallLog.setReceiverAddr(receiverAddr);
		String request = monitorCallLog.getRequest();	
		if(!StringUtils.isNull(request) ) {
			if(request.contains("\\x")) {
				request = request.replace("\\x", "x");
			}	
			if(request.length() > SQL_LENGTH ) {
				request = request.substring(0, SQL_LENGTH);
			}
			monitorCallLog.setRequest(request);
		}
		if (!StringUtils.isNull(errors)) {
			if(errors.length() > SQL_LENGTH) {
				errors = errors.substring(0, SQL_LENGTH);
			}
			monitorCallLog.setErrors(errors);
		}
		if (resp != null && resp.getResult() != null) {
			Object result =  resp.getResult();
			String temp;
			try {
				if(result instanceof String) {
					temp = (String)result;
				}else {
					temp = JsonUtil.objecte2JsonString(result);
				}
			}catch (Throwable e) {
				temp = result.toString();
			}
			if (temp.length() > SQL_LENGTH) {
				temp = temp.substring(0, SQL_LENGTH);
			}
			monitorCallLog.setResponse(temp);
		}

		// 添加到队列中
		CallLogManager.addMonitorCallQueue(monitorCallLog);
		return monitorCallLog;
	}

	static private boolean isSaveLog(String error){
		CallLogLevel level = RPCParam.getCallLogLevel();
		if(level==CallLogLevel.No ||(level==CallLogLevel.Error && StringUtils.isNull(error))){
			return false;
		}
		return true;
	}
	/**
	 * 方法说明:  往监控调用日志 队列添加日志
	 */
	static private void addMonitorCallQueue(MonitorCallLogTime monitorCallLog) {
		monitorCallLogQueue.add(monitorCallLog);
	}

	/**
	 *   启动更新数据库
	 */
	static public void start() {
		// 初始化更新表名
		updateTable();
		
		if (tableName != null) {
			ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
			service.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					try {
						// 定时运行更新表名
						updateTable();
						
						while (!monitorCallLogQueue.isEmpty()) {
							final MonitorCallLogTime log = monitorCallLogQueue.poll();
							monitorCallDB(log);
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}, 1, RPCParam.getClusterUpdateTime(), TimeUnit.SECONDS);
		}
	}

	/**
	 *        判断是否需要更新表名并获取下次更新表的时间段
	 *  nextUpdateTableTime = 0 不需要更新表名，默认为不分表
	 *  nextUpdateTableTime = -1 初始化阶段，需要更新表名
	 *  nextUpdateTableTime > 当前毫秒数   已经到了需要更新表名的时间段
	 */
	static private void updateTable() {		
		if(nextUpdateTableTime == 0) {
			return;
		}
		if(nextUpdateTableTime != -1 && System.currentTimeMillis() < nextUpdateTableTime) {
			return;
		}
		CallLogLevel level = RPCParam.getCallLogLevel();
		switch (level) {
		case No:
			tableName = null;
			nextUpdateTableTime= 0;
			break;
		case All:
			tableName = "t_monitor_call_log";
			tableErrorName = "t_monitor_call_error_log";
			nextUpdateTableTime= 0;
			break;
		case Year:
			String currentYear = RpcUtils.getCurrentYear().replaceAll("-", "_");
			tableName = "t_monitor_call_log_" + currentYear;
			tableErrorName = "t_monitor_call_error_log_" + currentYear;
			nextUpdateTableTime = getNextYearTime();
			break;
		case Month:
			String curentMonth = RpcUtils.getCurrentMonth().replaceAll("-", "_");
			tableName = "t_monitor_call_log_" + curentMonth;
			tableErrorName = "t_monitor_call_error_log_" + curentMonth;
			nextUpdateTableTime = getNextMonthTime();
			break;
		case Day:
			String curentDay = RpcUtils.getCurrentDay().replaceAll("-", "_");
			tableName = "t_monitor_call_log_" + curentDay;
			tableErrorName = "t_monitor_call_error_log_" + curentDay;
			nextUpdateTableTime = getNextDayTime();
			break;
		default:
			tableName = "t_monitor_call_log";
			tableErrorName = "t_monitor_call_error_log";
			nextUpdateTableTime= 0;
			break;
		}
	}
		
	/**
	 * @Description: 获取第二天的时间
	 * @return  long
	 */
    static private long getNextDayTime() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);      
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
    
	/**
	 * @Description: 获取下个月第一天的时间
	 * @return  long
	 */
    static private long getNextMonthTime() {
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_MONTH,1);
        cal.set(Calendar.HOUR_OF_DAY, 0); 
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	cal.add(Calendar.MONTH, 1);
    	return cal.getTimeInMillis();
    }
    
	static private long getNextYearTime() {
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.DAY_OF_YEAR,1);
        cal.set(Calendar.HOUR_OF_DAY, 0); 
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
    	cal.add(Calendar.YEAR, 1);
    	return cal.getTimeInMillis();
	}
    
    /**
	 * 方法说明: 将监控调用日志存放到数据库
	 * @throws CustomException 
	 */
	static private void monitorCallDB(final MonitorCallLogTime monitorCallLog) throws CustomException {

		Object[] requestObjects = monitorCallLog.getRequestObjects();
		if(requestObjects != null && requestObjects.length >0) {
			String requestValue = monitorCallLog.getRequest();
			for(Object object : requestObjects) {
				try {
					requestValue = requestValue + JsonUtil.objecte2JsonString(object)+",";
				}catch (Exception e) {
				}
			}
			//判断是否大于数据库字段长度，避免太长，出现Data truncation: Data too long for column异常，无法插入数据库
			if(requestValue != null && requestValue.length() < SQL_LENGTH)
            monitorCallLog.setRequest(requestValue);		
		}
		
		final String insertField = " (request_id,sender_addr,sender_mac,sender_cpu,receiver_addr,receiver_mac,"+
							"receiver_cpu,service,method,clazz,request,response,errors,start_time,total_time,"+
							"call_time,end,table_service_code,system_time,user_name,user_ip,end_time) ";
		final String sql = StringUtils.isNull(monitorCallLog.getErrors())
				? "insert into " + tableName + insertField +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE(),?,?,?)"
				: "insert into " + tableErrorName + insertField +" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE(),?,?,?)";
		try {
			DBManager.execute(new DBexecuteVoid() {
				@Override
				public void execute(EntityManager manager) {
					monitorCallLog.setTime();
					manager.createNativeQuery(sql).setParameter(1, monitorCallLog.getRequestId())
							.setParameter(2, monitorCallLog.getSenderAddr())
							.setParameter(3, monitorCallLog.getSenderMac())
							.setParameter(4, monitorCallLog.getSenderCpu())
							.setParameter(5, monitorCallLog.getReceiverAddr())
							.setParameter(6, monitorCallLog.getReceiverMac())
							.setParameter(7, monitorCallLog.getReceiverCpu())
							.setParameter(8, monitorCallLog.getService()).setParameter(9, monitorCallLog.getMethod())
							.setParameter(10, monitorCallLog.getClazz()).setParameter(11, monitorCallLog.getRequest())
							.setParameter(12, monitorCallLog.getResponse()).setParameter(13, monitorCallLog.getErrors())
							.setParameter(14, monitorCallLog.getStartTime())
							.setParameter(15, monitorCallLog.getTotalTime())
							.setParameter(16, monitorCallLog.getCallTime())
							.setParameter(17, monitorCallLog.getEnd())
							.setParameter(18, monitorCallLog.getTableServiceCode())
							.setParameter(19, monitorCallLog.getUserName())
							.setParameter(20, monitorCallLog.getUserIp())
							.setParameter(21, monitorCallLog.getEndTime())
							.executeUpdate();
				}
			});
		} catch (Throwable e) {
			Throwable temp = e;
			while (temp != null) {
				String message = temp.getMessage();
				if (message != null && message.contains("doesn't exist")) {
					final String createTableSQL = StringUtils.isNull(monitorCallLog.getErrors())
							? "create table " + tableName + " select * from t_monitor_call_log where 1=2"
							: "create table " + tableErrorName + " select * from t_monitor_call_error_log where 1=2";
					DBManager.execute(new DBexecuteVoid() {
						@Override
						public void execute(EntityManager manager) throws CustomException {
							manager.createNativeQuery(createTableSQL).executeUpdate();
						}
					});
					monitorCallDB(monitorCallLog);
					return;
				}
				temp = temp.getCause();
			}
			e.printStackTrace();
			throw e;
		}

	}

	public static void main(String[] args) {
		Date date2=new Date();
		date2.setTime(getNextDayTime());
		System.err.println("获取第二天="+date2);
		Date date3=new Date();
		date3.setTime(getNextMonthTime());
		System.err.println("获取下个月="+date3);
		Date date4=new Date();
		date4.setTime(getNextYearTime());
		System.err.println("获取下个年="+date4);
	}
	

}