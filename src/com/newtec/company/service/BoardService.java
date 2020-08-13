package com.newtec.company.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.router.request.FetchWebRequest;
/**
 * 看板数据对接service接口
 * @author 王鹏
 *
 */
public interface BoardService {

	//查找委托单总钱数
	public Map<String,Object> findWtdTotalMoney(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	//查找今日委托单总数
	public Map<String,Object> findWtdTodayNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public Map<String,Object> findTotalMoneyAmount(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public Map<String,Object> findTodayMoneyAmount(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public Map<String,Object> findTotalCercNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public Map<String,Object> findTodayCercNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public Map<String,Object> findTotalCustomNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public Map<String,Object> findTodayCustomNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public List<Map<String,Object>> findWtdEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	
	public List<Map<String,Object>> findTodayMoneyEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public List<Map<String,Object>> findTotalMoneyEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	//设置各类型的包含的总2级服务订单数和金额
	public JSONObject findTotalEveryTypeMoneyAndWtd(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public JSONArray  findTodayWtdEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException;
	
	public JSONArray findFirst5CercNum(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 
	 //查询 各个国家的经纬度以及委托单数
	 public JSONArray findMapCountryMsg(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 //中国各省完工金额
	 public JSONArray findMapProvinceMsg(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 
	 public JSONArray findMapInvoice(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	
	 public JSONArray findPrductLine(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 
	 
	 public JSONArray findCompanyInfo(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 
	 public JSONArray findProductInfo(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 
	 public JSONObject findOrganInfo(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 
	 public JSONObject findStaffInfo(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	 
	 public JSONArray findOrderInfo(FetchWebRequest<Map<String, String>> fetchWebReq)throws CustomException;
	
}
