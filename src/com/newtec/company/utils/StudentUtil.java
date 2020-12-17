package com.newtec.company.utils;

import java.sql.Connection;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtec.http.utils.HttpclientUtils;

public class StudentUtil {

	public static void main(String[] args) {
		// url 
		String url = "http://127.0.0.1:8089/CompanyWebServlet";
		// params
		JSONObject jo = new JSONObject();
		
		jo.put("url", url);
		jo.put("operServiceId", "studentService");
		jo.put("operId", "findStudentInfoArray");
		jo.put("data", "");
		jo.put("endRow", "30");
		jo.put("startRow", "0");
		jo.put("totalRow", "30");
		jo.put("ds", "person");
		jo.put("operType", "fetch");
		jo.put("clientType", "web");
		
		url =url + "?params=" + jo.toString();
		System.out.println(url);
		testApiGet(url);
		
	}
	
	/**
	 * 查询学生信息并返回jsonarray
	 * @param sql
	 * @return
	 */
	public static JSONArray findStudentInfo(String sql) {
		return StudentDBUtil.querySqlForJSONArray(sql);
	}
	
	/**
	 *	post 请求 
	 * @param httpUrl
	 * @param params
	 */
	public static void testApiPost(String httpUrl, String params) {
		// 进行post请求
		System.out.println(HttpClient.doPost(httpUrl, params));
	}
	
	/**
	 * get请求
	 * @param httpUrl
	 */
	public static void testApiGet(String httpUrl) {
		// 进行post请求
		System.out.println(HttpClient.doGet(httpUrl));
	}
}
