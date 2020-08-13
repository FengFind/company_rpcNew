package com.newtec.http.proxy;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.newtec.http.utils.HttpclientUtils;
import com.newtec.http.utils.Testaa;

/**
 * @author 王仕通
 * @date 2017-8-8 下午3:41:50 
 * @Description rpc调用代理
 */
public class HttpProxy {

	public static void main(String[] args) {
		long t = System.currentTimeMillis();
		Testaa b = null;
		for(int i=0;i<1000000;i++){
			b = getInstance("http://123456adfadfs;asdf","activateId",Testaa.class,"serviceName");
		}
		System.err.println("是："+(System.currentTimeMillis()-t));
		
		System.err.println(b.te("张三")); 
	}
	
	static private Map<String,Object> instanceMap = new HashMap<String,Object>();
	static public <T> T getInstance(String activateId,Class<T> clazz,String serviceName){
		return getInstance(null, activateId, clazz, serviceName);
	}
	
	static public <T> T getInstance(String httpAddr,String activateId,Class<T> clazz,String serviceName){
		if(httpAddr==null||"".equals(httpAddr)){
			httpAddr = HttpclientUtils.getHttpAddr() ;
			if(httpAddr==null){HttpclientUtils.setHttpAddr(httpAddr);
				throw new RuntimeException("请在初始化的地方调用HttpclientUtils.setHttpAddr进行设置httpAddr");
			}
		}
		String path = httpAddr+serviceName;
		T obj = (T)instanceMap.get(path);
//		HttpProxyHandler.setHttpServicePath(httpAddr+serviceName);
		if(obj==null){
			HttpProxyHandler proxyHandler = new HttpProxyHandler(path);
			obj = (T) Proxy.newProxyInstance(clazz.getClassLoader(), 
				     new Class[]{clazz}, 
				     proxyHandler);
			instanceMap.put(path, obj);
		}
		return obj;
		
//		HttpProxyHandler proxyHandler = new HttpProxyHandler(httpAddr,activateId,serviceName);
//		return (T)Proxy.newProxyInstance(clazz.getClassLoader(), 
//			     new Class[]{clazz}, 
//			     proxyHandler);
	}
	
	
}
