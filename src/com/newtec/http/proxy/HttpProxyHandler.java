/**   
* @Title: My.java 
* @Package com.newtec.call 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 王仕通 
* @date 2017-8-8 下午3:45:45 
* @version V1.0   
*/
package com.newtec.http.proxy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.serialize.common.Objects;
import com.newtec.serialize.common.Response;
import com.newtec.serialize.utils.SerializationUtil;

/**
 * @author 王仕通
 * @date 2017-8-8 下午3:45:45 
 *@Description 用一句话描述该类做什么
 */
public class HttpProxyHandler implements InvocationHandler {
	
	public static final String OperSuccess = "10000";
	/**
	 * http地址+服务名构成的路径
	 */
//	private String httpServicePath;
//	static private ThreadLocal<String> httpServicePath = new ThreadLocal<String>(); 
	private String httpServiceSharPath ; 

	public HttpProxyHandler(/*String activateId, */String httpServiceSharPath) {
		this.httpServiceSharPath = httpServiceSharPath;
	}
//	static public void setHttpServicePath(String httpServicePath){
//		HttpProxyHandler.httpServicePath.set(httpServicePath);
//	}
//	static public void clearHttpServicePath(){
//		HttpProxyHandler.httpServicePath.remove();
//	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		byte[] params = null;
		if (args != null && args.length != 0) {
			params = SerializationUtil.serialize(new Objects(args));
		}else{
			params = new byte[0];
		}
		byte[] result;
		try {
//			String path = httpServicePath.get() ;
//			path = path==null?httpServiceSharPath:path;
			result = sendPostRequest(httpServiceSharPath+ "/" + method.getName(),
					params);
		} catch (Exception e) {
			if (e.getMessage().contains("Connection refused: connect")) {
				System.err.println("【HTTP】 "+httpServiceSharPath+"无法访问，请检查连接地址是否正确或者服务器是否正确启动 ");
				throw new CustomException("","【HTTP】 "+httpServiceSharPath+"无法访问，请检查连接地址是否正确或者服务器是否正确启动 ");
			} else {
				throw e;
			}
		}
		Response resp = SerializationUtil.deserialize(result, Response.class);
		if (resp == null) {
			return null;
		}
		String status = resp.getStatus();
		// 如果返回的结果不是操作成功，则抛出异常
		if(!status.equals(OperSuccess)) throw new CustomException(status,resp.getMessage() +"|"+resp.getResult());
		return resp.getResult();
	}

	/**
	 * 
	 * 方法说明:  发送Post请求
	 * @param 	url              请求的url
	 * @param 	params           传递的请求参数
	 * @return  String         	   请求结果
	 */
	private static byte[] sendPostRequest(String url, byte[] params) throws  CustomException {
		int HttpResult = 0;
		HttpURLConnection con = null;
		BufferedOutputStream wr = null;
		ByteArrayOutputStream swapStream = null;
		InputStream inStream = null;
		try {
			URL object = new URL(url);
			con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setReadTimeout(20 * 1000);
			con.setConnectTimeout(20 * 1000);
			con.setRequestMethod("POST");
			// OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr = new BufferedOutputStream(con.getOutputStream());
			wr.write(params);
			wr.flush();
			HttpResult = con.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				swapStream = new ByteArrayOutputStream();
				inStream = con.getInputStream();
				byte[] buff = new byte[100];
				int rc = 0;
				while ((rc = inStream.read(buff, 0, 100)) > 0) {
					swapStream.write(buff, 0, rc);
				}
				byte[] in2b = swapStream.toByteArray();
				return in2b;
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(inStream !=null){
				try {
					inStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(swapStream !=null){
				try {
					swapStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(wr !=null){
				try {
					wr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(con !=null){
				con.disconnect();
				con = null;
			}
		}
		throw new CustomException("", " 请求地址：" + url + " 请求失败,状态码：" + HttpResult);
	}
}
