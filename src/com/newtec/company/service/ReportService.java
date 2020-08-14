package com.newtec.company.service;

import java.util.Map;

import com.newtec.router.request.FetchWebRequest;

public interface ReportService {

	public Map<String, Object> findInvoiceData(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

	public Map<String, Object> findTodayTotal(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;

	public Map<String, Object> findCompanyMsgByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception ;
}
