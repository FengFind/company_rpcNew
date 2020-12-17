package com.newtec.company.service;

import java.sql.SQLException;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.newtec.router.request.FetchWebRequest;

public interface SelectService {
	//根据传递参数进行验证，是否连接成功
	public Map<String, Object> getSelect(FetchWebRequest<Map<String, String>> fetchWebReq);
	
	//根据传递参数返回数据库中的表名
	public Map<String, Object> getSelectByTableName(FetchWebRequest<Map<String, String>> fetchWebReq) throws ClassNotFoundException, SQLException;
	
	//根据表名返回所有字段
	public Map<String, Object> getSelectByTableFile(FetchWebRequest<Map<String, String>> fetchWebReq);
}
