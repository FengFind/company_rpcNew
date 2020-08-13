package com.newtec.company.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.newtec.company.common.CompanyRData;
import com.newtec.myqdp.print.utils.Print;
import com.newtec.myqdp.server.utils.exception.CustomException;

/**
 * @author 
 * @Description  Http调用客户端
 * @date  2018年7月9日
 * @version 1.0
 */
public class CompanyHttpUtils {

	static public Response doGet(String url, Map<String, String> headers, Map<String, String> params) throws Exception {
		return doGet(url, headers, params, false);
	}

	static public Response doGet(String url, Map<String, String> headers, Map<String, String> params, boolean file)
			throws Exception {
		Response response = null;
		if (params != null && !params.isEmpty()) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
			for (String key : params.keySet()) {
				pairs.add(new BasicNameValuePair(key, params.get(key)));
			}
			url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs), "UTF-8");
		}
		Print.debug(CompanyRData.COMPANY + " 请求的url：" + url);
		HttpGet httpGet = new HttpGet(url);
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpGet.addHeader(header.getKey(), header.getValue());
			}
		}
		HttpResponse httpResponse = gethttpClient().execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		int repStatus = httpResponse.getStatusLine().getStatusCode();
		if (repStatus == 200) {
			if (file) {
				byte[] resultByte = EntityUtils.toByteArray(httpEntity);
				response = new Response(repStatus, resultByte);
			} else {
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				response = new Response(repStatus, result);
				Print.debug(CompanyRData.COMPANY + " 返回的结果：" + result);
			}
			httpGet.releaseConnection();
			return response;
		}

		httpGet.releaseConnection();
		throw new CustomException("", "请求指定 " + url + " 失败，状态码：" + repStatus);

	}

	static public Response doPost(String url, Map<String, String> headers, String param) throws Exception {
		return doPost(url, headers, param, false);
	}

	static public Response doPost(String url, Map<String, String> headers, String param, boolean file)
			throws Exception {
		Response response = null;
		HttpPost httpPost = new HttpPost(url);
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpPost.addHeader(header.getKey(), header.getValue());
			}
		}
		StringEntity entity = new StringEntity(param, Charset.forName("UTF-8"));
		entity.setContentType("application/json; charset=utf-8");
		httpPost.setEntity(entity);
		HttpResponse httpResponse = gethttpClient().execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		int repStatus = httpResponse.getStatusLine().getStatusCode();
		if (repStatus == 200) {
			if (file) {
				byte[] resultByte = EntityUtils.toByteArray(httpEntity);
				response = new Response(repStatus, resultByte);
			} else {
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				response = new Response(repStatus, result);
				Print.debug(CompanyRData.COMPANY + " 返回的结果：" + result);
			}
			httpPost.releaseConnection();
			return response;
		}
		httpPost.releaseConnection();
		throw new CustomException("", "请求指定 " + url + " 失败，状态码：" + repStatus);
	}
	
	static public Response doPut(String url, Map<String, String> headers, String param)
			throws Exception {
		Response response = null;
		HttpPut httpPost = new HttpPut(url);
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpPost.addHeader(header.getKey(), header.getValue());
			}
		}
		StringEntity entity = new StringEntity(param, Charset.forName("UTF-8"));
		entity.setContentType("application/json; charset=utf-8");
		httpPost.setEntity(entity);
		HttpResponse httpResponse = gethttpClient().execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		int repStatus = httpResponse.getStatusLine().getStatusCode();
		if (repStatus == 200) {
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			response = new Response(repStatus, result);
			Print.debug(CompanyRData.COMPANY + " 返回的结果：" + result);
			httpPost.releaseConnection();
			return response;
		}
		httpPost.releaseConnection();
		throw new CustomException("", "请求指定 " + url + " 失败，状态码：" + repStatus);
	}

	static public Response doDelete(String url, Map<String, String> headers, String param)
			throws Exception {
		Response response = null;
		HttpDelete httpPost = new HttpDelete(url);
		if (headers != null) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				httpPost.addHeader(header.getKey(), header.getValue());
			}
		}
		StringEntity entity = new StringEntity(param, Charset.forName("UTF-8"));
		entity.setContentType("application/json; charset=utf-8");
		HttpResponse httpResponse = gethttpClient().execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		int repStatus = httpResponse.getStatusLine().getStatusCode();
		if (repStatus == 200) {
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			response = new Response(repStatus, result);
			Print.debug(CompanyRData.COMPANY + " 返回的结果：" + result);
			httpPost.releaseConnection();
			return response;
		}
		httpPost.releaseConnection();
		throw new CustomException("", "请求指定 " + url + " 失败，状态码：" + repStatus);
	}
	
	
	static public class Response {

		private int status;
		private String resultStr;
		private byte[] resultByte;

		public Response() {
		}

		public Response(int status, String resultStr) {
			this.status = status;
			this.resultStr = resultStr;
		}

		public Response(int status, byte[] resultByte) {
			this.status = status;
			this.resultByte = resultByte;
		}

		public int getStatus() {
			return status;
		}

		public byte[] getResultByte() {
			return resultByte;
		}

		public String getResultStr() {
			return resultStr;
		}

		@Override
		public String toString() {
			return "Response [status=" + status + ", result=" + resultStr + "]";
		}

	}

	static private HttpClient gethttpClient() {
		Registry<ConnectionSocketFactory> registry;
		registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", PlainConnectionSocketFactory.getSocketFactory())
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(5);
		connectionManager.setDefaultMaxPerRoute(5);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(8000).setConnectTimeout(8000)
				.setConnectionRequestTimeout(8000).build();

		return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager)
				.build();
	}
}
