package com.newtec.company.service;

import java.util.Map;

import com.newtec.router.request.FetchWebRequest;

public interface CollectService {

	public Map<String, Object> getCollect(FetchWebRequest<Map<String, String>> fetchWebReq);
	
	public Map<String, Object> getCollectByName(FetchWebRequest<Map<String, String>> fetchWebReq);
	
	public Map<String, Object> getCollectBySql(FetchWebRequest<Map<String, String>> fetchWebReq);
	
	public Map<String, Object> checkSql(FetchWebRequest<Map<String, String>> fetchWebReq);
}
