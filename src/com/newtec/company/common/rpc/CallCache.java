package com.newtec.company.common.rpc;

import com.newtec.reflect.client.ProxyInstanceUtils;
import com.newtec.rpc.call.MyProxyHandler;
import com.newtec.rpc.core.RPCParam;

public class CallCache {
	public static final String TASK_MANAGER_NAME = "taskCacheManager";

	public static <T> T getInstance(Class<T> clazz, String serviceName) {
		return ProxyInstanceUtils.getInstance(clazz, new MyProxyHandler(RPCParam.MANAGER_KEY, serviceName));
	}

}