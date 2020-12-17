package com.newtec.company.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.newtec.router.request.FetchWebRequest;

public interface StudentService {

	public Map<String, Object> findStudentInfo(FetchWebRequest<Map<String, String>> fetchWebReq);

	public JSONArray findStudentInfoArray(FetchWebRequest<Map<String, String>> fetchWebReq);
}
