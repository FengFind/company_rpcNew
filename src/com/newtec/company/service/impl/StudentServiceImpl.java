package com.newtec.company.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.newtec.company.service.StudentService;
import com.newtec.company.utils.StudentUtil;
import com.newtec.reflect.annotation.RpcClass;
import com.newtec.reflect.annotation.RpcMethod;
import com.newtec.router.request.FetchWebRequest;

@RpcClass(value = "studentService",http = true)
public class StudentServiceImpl implements StudentService {

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findStudentInfo(FetchWebRequest<Map<String, String>> fetchWebReq) {
		Map<String, Object> res = new HashMap<String, Object>();
		
		// 查询学生信息
		JSONArray stu = StudentUtil.findStudentInfo("select * from student");
		
		res.put("info", stu);
		
		return res;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findStudentInfoArray(FetchWebRequest<Map<String, String>> fetchWebReq) {
		return StudentUtil.findStudentInfo("select * from student");
	}

}
