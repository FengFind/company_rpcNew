package com.newtec.rpc.call.client;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.newtec.myqdp.server.utils.StringUtils;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.reflect.meta.MethodMeta;
import com.newtec.rpc.core.RPCParam;
import com.newtec.rpc.http.support.HRequest;

/**
 * @author Yuexin
 * @Description  客户端发起的服务调用请求对象
 */
public class Request implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 请求id
	 */
	private String requestId;
	/**
	 * rpc调用用户凭证
	 */
	private String userKey;
	/**
	 * 服务名
	 */
	private String serviceName;
	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 参数值
	 * 目前支持格式：
	 * 1.json字符串
	 * 2.对象数组
	 * 3.键值对Map
	 */
	private Object value;
	
	/**
	 * 扩展值
	 */
	private Object extValue;
	
	/**
	 * 表服务编码
	 */
	private String tableServiceCode;	
//	/**
//	 * 是否是http调用
//	 */
//	transient private boolean httpCall = false;
	/** 
	 *  超时参数
	 * -1代表用不超时
	 */
	private int timeOut = RPCParam.getCallTimeOut() == 0?10 * 1000:RPCParam.getCallTimeOut();
	/** 调用时是否找本地方法  */
	transient private boolean findLocalMethod = RPCParam.isFindLocalMethod();
	/**
	 * 指定调用路由的主机，如有指定，所有的调用都往这台主机上发送，不使用负载均衡
	 */
	transient private String toHost;
	/**
	 * 指定调用路由的主机端口，如有指定，所有的调用都往这台主机端口上发送，不使用负载均衡
	 */
	transient private int toPort;
	/**
	 * 禁止方位的IP地址
	 */
	transient private Set<String> noAccessNode ;
	/** 要求服务端返回的结果转成json字符串 */
	private boolean resultJson = false;
	/** true:同步调用;false：异步调用 */
	transient private boolean sync = true;
	/**
	* 异步回调对象
	* null:服务端不需要发送通知消息
	* 非null:异步执行回调
	*/
	transient private AsyncCallback asyncCallback;
	/** 是否记录调用日志*/
	transient private boolean writeLog = true;
	
	
	/**
	 * 路由标记大于0才起作用，优先级比直接调用低。
	 * routerFlag与当前服务中能找到服务的总集群数  取余数 来路由（存在问题：集群中有新加入或移除时会导致前后路由数据不一致）
	 */
	transient private int routerFlag=-1;
	
	public Request() {}
	
	
	public Request(String userKey, String serviceName, String methodName) {
		this.userKey = userKey;
		this.serviceName = serviceName;
		this.methodName = methodName;
	}

	
	public Request(String userKey, String serviceName, String methodName, Object[] valueObjs) {
		this(userKey, serviceName, methodName);
		this.value = valueObjs;
	}
	
	public Request(String userKey, String serviceName, String methodName, String valueJson) {
		this(userKey, serviceName, methodName);
		this.value = valueJson;
	}

	public Request(String userKey, String serviceName, String methodName, LinkedHashMap<String, Object> valueMap) {
		this(userKey, serviceName, methodName);
		this.value = valueMap;
	}

	public Request(String userKey, String serviceName, String methodName, LinkedHashMap<String, Object> valueMap,boolean isRequestJson) {
		this(userKey, serviceName, methodName,valueMap);
		this.resultJson = isRequestJson;	
	}
	
	protected Request(Request request) {
		this.serviceName = request.getServiceName();
		this.methodName = request.getMethodName();
		this.value = request.getValue();
		this.extValue = request.getExtValue();
		this.timeOut = request.getTimeOut();
		this.asyncCallback = request.getAsyncCallback();
		this.sync = request.isSync();
		this.userKey = request.getActivateId();
		
		this.resultJson = request.isResultJson();
		
		if (!StringUtils.isNull(request.getTableServiceCode())) {
			this.tableServiceCode = request.getTableServiceCode();
		}

		if (StringUtils.isNull(request.getRequestId())) {
			this.requestId = buildCallRequestId();
		} else {
			this.requestId = request.getRequestId();
		}
	}

	public boolean isFindLocalMethod() {
		return findLocalMethod;
	}
	public Object getValue() {
		return value;
	}
	public int getRouterFlag() {
		return routerFlag;
	}
	public void setRouterFlag(int routerFlag) {
		this.routerFlag = routerFlag;
	}
	public void setFindLocalMethod(boolean findLocalMethod) {
		this.findLocalMethod = findLocalMethod;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getValueObjs() {
//		return valueObjs;
		return (Object[])value;
	}

	public void setValueObjs(Object... valueObjs) {
//		this.valueObjs = valueObjs;
//		this.valueType = RequestValueType.Objs;
		this.value = valueObjs;
	}

//	public Map<String, String> getValueJsonMaps() {
//		return valueJsonMaps;
//	}

//	public void setValueJsonMaps(Map<String, String> valueJsonMaps) {
//		this.valueJsonMaps = valueJsonMaps;
//		this.valueType = RequestValueType.JsonMap;
//	}

	public String getValueJson() {
//		return valueJson;
		return (String)value;
	}
	public int getValueCount(){
		if(value==null) return 0;
		if(value instanceof Object[]){
			return ((Object[]) value).length;
		}else if(value instanceof Map){
			return ((Map)value).size();
		}
		return -1;
	}
	
	public boolean isValueObjs(){
		return value==null || value instanceof Object[];
	}
	public boolean isValueMap(){
		return value instanceof Map;
	}
	public boolean isValueJson(){
		return value instanceof String;
	}
	
	public void setValueJson(String valueJson) {
//		this.valueJson = valueJson;
//		this.valueType = RequestValueType.Json;
		this.value = valueJson;
	}

	public Map<String, Object> getValueMap() {
		return (Map<String,Object>)value;
	}

	public void setValueMap(Map<String, Object> valueMap) {
//		this.valueMap = valueMap;
//		this.valueType = RequestValueType.Map;
		value = valueMap;
	}
	
	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

//	public RequestValueType getValueType() {
//		return valueType;
//	}
//
//	public void setValueType(RequestValueType valueType) {
//		this.valueType = valueType;
//	}

	public AsyncCallback getAsyncCallback() {
		return asyncCallback;
	}

	public void setAsyncCallback(AsyncCallback asyncCallback) {
		this.sync = false;
		this.asyncCallback = asyncCallback;
	}

//	public long getWaitTime() {
//		return waitTime;
//	}
//
//	public void setWaitTime(long waitTime) {
//		this.waitTime = waitTime;
//	}

	public boolean isSync() {
		return sync;
	}

	public void setSync(boolean sync) {
		this.sync = sync;
	}

	public String getToHost() {
		return toHost;
	}

	public void setToHost(String toHost) {
		if(toHost != null){
			toHost = toHost.trim();
		}
		this.toHost = toHost;
	}

	public int getToPort() {
		return toPort;
	}

	public void setToPort(int toPort) {
		this.toPort = toPort;
	}
	
	public String getActivateId() {
		return userKey;
	}

	public void setActivateId(String userKey) {
		this.userKey = userKey;
	}

	public String getTableServiceCode() {
		return tableServiceCode;
	}

	public void setTableServiceCode(String tableServiceCode) {
		this.tableServiceCode = tableServiceCode;
	}

	public void setTo(String toHost, int toPort) {
		this.setToHost(toHost);
		this.toPort = toPort;
	}

	public void setTo(String toHost, int toPort, int timeOut) {
		setTo(toHost, toPort);
		this.timeOut = timeOut;
	}

	public boolean isResultJson() {
		return resultJson;
	}

	public void setResultJson(boolean resultJson) {
		this.resultJson = resultJson;
	}

	public String getRequestInfo() {
		return "服务名 :"+serviceName+ "方法名:"+methodName ;
	}
	

	public void setNoAccessNode(String... addrs) {
		if(addrs==null || addrs.length==0) return ;
		this.noAccessNode = new HashSet<String>();
		for(String address : addrs) {
		   noAccessNode.add(address);
		}
	}
	
	public Set<String> getNoAccessNode() {
		return noAccessNode;
	}
	
	public boolean isWriteLog() {
		return writeLog;
	}

	public void setWriteLog(boolean writeLog) {
		this.writeLog = writeLog;
	}
	
//	public void setHttpCall(boolean httpCall) {
//		this.httpCall = httpCall;
//	}
//
	public boolean isHttpCall() {
//		return httpCall;
		return this instanceof HRequest;
	}
	/**
	 * 方法说明:  构建调用请求Id
	 */
	public String buildCallRequestId() {
		return "call_" + UUID.randomUUID();
	}
	public Object getExtValue() {
		return extValue;
	}
	public void setExtValue(Object extValue) {
		this.extValue = extValue;
	}
	
	public <T extends MethodMeta> void findMethodFish(List<T>methods)throws CustomException{}
}
