package com.newtec.company.service;

import java.util.List;
import java.util.Map;

import com.newtec.router.request.FetchWebRequest;

public interface ReportService {

	public Map<String, Object> findInvoiceDataCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findInvoiceData(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

	public Map<String, Object> findTodayTotal(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

	public Map<String, Object> findCompanyMsgByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findCompanyMsgByArea(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findCompanyMsgByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findJtgsGxbar(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findJtgsCompanyMsg(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findFirstCompanyKp(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findJtgsTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findZjytTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findTotalCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findJTGSCompanyCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findTodayTotalCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findSrpieCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findJtgsSrbar(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findCpxGxbar(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findCpxMsgByJsfl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findCpxTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findCompanyMsgByCpxName(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findStationByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findGdmsgByCompanyId(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findGgmsgByCompanyId(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
	
	public Map<String, Object> findFzjgmsgByCompanyId(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
}
