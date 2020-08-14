package com.newtec.company.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtec.company.service.ReportService;
import com.newtec.company.utils.Util;
import com.newtec.reflect.annotation.RpcClass;
import com.newtec.reflect.annotation.RpcMethod;
import com.newtec.router.request.FetchWebRequest;
import com.newtec.rpc.db.DBManager;

@RpcClass(value = "reportService",http = true)
public class ReportServiceImpl implements ReportService{

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findInvoiceData(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select table_name_en from TABLE_SWITCH where table_uuid = 'V_INVOICE_NC_MM_ADD' and table_state = 1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		String whereCond = (companyId!= null && !companyId.equals("") && !companyId.equals("zj") ) ? " where COMPANY_BUSI_ORG_ID = "+ companyId: "";
		sql.append("select "
							+ "to_char(a.mon)||'月' as yue,a.curYear, a.lastYear, round(a.curYear/a.lastYear*100, 2) as tongbi  "
						+ "from ( " 
								+ "select  "
										+ "to_number(substr(INVOICE_DATE,6,7)) as mon,sum((SYSTEM_TAX_PRICE+TAXPRICE_YX+TAXPRICE_FYW)) as curYear, sum(YYSRSNTQLJ) as lastYear "
								+ "from  "
										+ tableName
								+ whereCond
								+	" group by INVOICE_DATE "
								+  " order by mon  "
						+ ")  a");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			// seriesData
			JSONArray seriesData = new JSONArray();
			// 去年
			JSONArray lastYear = new JSONArray();
			// 今年
			JSONArray curYear = new JSONArray();
			// 同比
			JSONArray tongbi = new JSONArray();
			// 月份
			JSONArray xAxisData = new JSONArray();
			// y轴最大最小值
			JSONArray yAxis = new JSONArray();
			// 左侧y轴
			JSONObject leftY = new JSONObject();
			// 右侧y轴
			JSONObject rightY = new JSONObject();
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d; 
						
			for (int i = 0; i < resultList.size(); i++) {
				Object[] ro = resultList.get(i);
				
				if(ro == null || ro.length == 0) {
					continue;
				}
				
				lastYear.add(ro[2] == null ? 0 : ro[2]);
				curYear.add(ro[1] == null ? 0 : ro[1]);
				tongbi.add(ro[3] == null ? 0 : ro[3]);
				xAxisData.add(ro[0]);
				
				if(ro[1] != null && ro[1] != "") { 
					if(leftYMin > Double.parseDouble(ro[1].toString())) {
						leftYMin = Double.parseDouble(ro[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ro[1].toString())) {
						leftYMax = Double.parseDouble(ro[1].toString());
					}
				}
				
				if(ro[2] != null && ro[2] != "") { 
					if(leftYMin > Double.parseDouble(ro[2].toString())) {
						leftYMin = Double.parseDouble(ro[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ro[2].toString())) {
						leftYMax = Double.parseDouble(ro[2].toString());
					}
				}
				
				if(ro[3] != null && ro[3] != "") { 
					if(rightYMin > Double.parseDouble(ro[3].toString())) {
						rightYMin = Double.parseDouble(ro[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ro[3].toString())) {
						rightYMax = Double.parseDouble(ro[3].toString());
					}
				}
			}
			
			seriesData.add(lastYear);    
			seriesData.add(curYear);
			seriesData.add(tongbi);

			leftY.put("min", leftYMin);
			leftY.put("max", leftYMax);
			
			yAxis.add(leftY);
			
			rightY.put("min", rightYMin);
			rightY.put("max", rightYMax);
			
			yAxis.add(rightY);
			
			map.put("seriesData", seriesData);
			map.put("xAxisData", xAxisData);
			map.put("yAxis", yAxis);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTodayTotal(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		sql.append("select * from "+tableName);
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 返回的jsonarray
			JSONArray ja = new JSONArray();
			// 亿 万
			int yi = 100000000, wan = 10000;
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			// 开工数量 4 年开工 5 日开工
			ja.add(Util.returnTodayTotalIntMsg("开工数量", "单", res[4], res[5], df));
			
			// 出证数量 14 年证书 15 日证书
			ja.add(Util.returnTodayTotalIntMsg("出证数量", "", res[14], res[15], df));
			
			// 客户数量 8 年客户 9 日客户
			ja.add(Util.returnTodayTotalIntMsg("客户数量", "个", res[8], res[9], df));
			
			// 供应商数量 3 年供应商 2 日供应商
			ja.add(Util.returnTodayTotalIntMsg("供应商数量", "个", res[3], res[2], df));

			// 委托金额 12 年委托金额 13 日委托金额
			ja.add(Util.returnTodayTotalDoubleMsg("委托金额", "元", res[12], res[13], df));

			// 完工金额 10 年完工金额 11 日完工金额 
			ja.add(Util.returnTodayTotalDoubleMsg("完工金额", "元", res[10], res[11], df));
			
			// 开票收入 6 年开票收入 7 日开票收入 
			ja.add(Util.returnTodayTotalDoubleMsg("开票收入", "元", res[6], res[7], df));
			
			// 成本总额 0 年成本总额 1 日成本总额 
			ja.add(Util.returnTodayTotalDoubleMsg("成本总额", "元", res[0], res[1], df));
			
			map.put("ttdata", ja);
		}
		
		return map;
	}
	
}
