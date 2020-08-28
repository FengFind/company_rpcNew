package com.newtec.company.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
	public Map<String, Object> findInvoiceDataCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		
		sql.append("select sum(system_tax_price) 今年开票, " + 
				"                     to_number(substr(invoice_date, 6, 2))||'月' 今年月份 " + 
				"                  from VIEW_SERVER_INVOICE " + 
				"             where  substr(invoice_date,0,4)=to_char(sysdate,'yyyy') " + 
				"                    and PRODUCT_NAME='"+ companyId +"' " + 
				"                    group by substr(invoice_date, 6, 2) " + 
				"                    order by substr(invoice_date, 6, 2)");

		System.out.println(sql.toString());
		
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			// seriesData
			JSONArray seriesData = new JSONArray();
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
				
				// 开票
				BigDecimal kp = ro[0] == null ? new BigDecimal(0) : (BigDecimal) ro[0];
				
				curYear.add(kp.doubleValue());
				
				// 环比
				BigDecimal hb = new BigDecimal(0);
				
				if(i > 0) {
					// 本月开票
					BigDecimal bykp = ro[0] == null ? new BigDecimal(0) : (BigDecimal) ro[0];
					// 上月开票 
					Object[] ro1 = resultList.get(i-1);
					BigDecimal sykp = ro1[0] == null ? new BigDecimal(0) : (BigDecimal) ro1[0];
					// 环比计算公式 (本月开票 - 上月开票)/上月开票
					hb = new BigDecimal( ( bykp.doubleValue() -  sykp.doubleValue() ) / sykp.doubleValue() * 100 );
				}
				
				tongbi.add(Double.parseDouble(Util.returnStringForNumber(hb, 2)));
				
				xAxisData.add(ro[1]);
				
				if(ro[0] != null && ro[0] != "") { 
					if(leftYMin > Double.parseDouble(ro[0].toString())) {
						leftYMin = Double.parseDouble(ro[0].toString());
					}
					
					if(leftYMax < Double.parseDouble(ro[0].toString())) {
						leftYMax = Double.parseDouble(ro[0].toString());
					}
				}
				
				if(hb != null) { 
					if(rightYMin > hb.doubleValue()) {
						rightYMin = hb.doubleValue();
					}
					
					if(rightYMax < hb.doubleValue()) {
						rightYMax = hb.doubleValue();
					}
				}
			}
			
			seriesData.add(curYear);
			seriesData.add(tongbi);

			leftY.put("min", leftYMin);
			leftY.put("max", leftYMax);
			
			yAxis.add(leftY);
			
			rightY.put("min", Integer.parseInt(rightYMin.toString().substring(0,rightYMin.toString().indexOf("."))) - 5);
			rightY.put("max", Integer.parseInt(rightYMax.toString().substring(0,rightYMax.toString().indexOf("."))) + 5);
			
			yAxis.add(rightY);
			
			map.put("seriesData", seriesData);
			map.put("xAxisData", xAxisData);
			map.put("yAxis", yAxis);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findInvoiceData(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		
		if(companyId !=null && !companyId.equals("")
				&& ( companyId.equals("东南亚区域") || companyId.equals("欧洲区域") || companyId.equals("非洲区域") )) {
			// 区域公司
			sql.append("select " + 
					"  to_char(a.mon)||'月' as yue,a.curYear, a.lastYear, " + 
					"  case when a.lastYear=0 then 0 else round((a.curYear - a.lastYear)/a.lastYear*100, 2) end as tongbi  " + 
					"from " + 
					"( " + 
					"SELECT to_number(SUBSTR(INVOICE_DATE, 6, 2)) as mon, " + 
					"         NVL(SUM(SYSTEM_TAX_PRICE), 0) + NVL(SUM(TAXPRICE_YX), 0) + " + 
					"         NVL(SUM(TAXPRICE_FYW), 0) + NVL(SUM(SYSTEM_TAX_PRICE_SY), 0) + " + 
					"         NVL(SUM(SYSTEM_TAX_PRICE_PSI), 0) + " + 
					"         NVL(SUM(SYSTEM_TAX_PRICE_CQC), 0) as curYear, " + 
					"         SUM(YYSRBNLJ19) as lastYear " + 
					"    FROM ZJYTKP_TABLE " + 
					"   WHERE  business_region='"+companyId+"' " + 
					"  and SUBSTR(INVOICE_DATE, 6, 2) <= to_char(SYSDATE,'mm') " + 
					"   GROUP BY SUBSTR(INVOICE_DATE, 6, 2) " + 
					"   ORDER BY mon " + 
					") a");
		}else {
			// 单个公司
			sql.append(" select table_name_en from TABLE_SWITCH where table_uuid = 'V_INVOICE_NC_MM_ADD' and table_state = 1 ");
			
			Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
			
			if(tableName == null || tableName.toString().equals("")) {
				throw new Exception();
			}
			
			sql.delete(0, sql.length());		
			String whereCond = (companyId!= null && !companyId.equals("") && !companyId.equals("zj") ) ? " where COMPANY_BUSI_ORG_NAME = '"+ companyId + "'": "";
			sql.append("select "
								+ "to_char(a.mon)||'月' as yue,a.curYear, a.lastYear, "
								+ "case when a.lastYear=0 then 0 else round((a.curYear - a.lastYear)/a.lastYear*100, 2) end as tongbi  "
							+ "from ( " 
									+ "select  "
											+ "to_number(substr(INVOICE_DATE,6,7)) as mon,sum((SYSTEM_TAX_PRICE+TAXPRICE_YX+TAXPRICE_FYW)) as curYear, sum(YYSRSNTQLJ) as lastYear "
									+ "from  "
											+ tableName
									+ whereCond
									+	" group by INVOICE_DATE "
									+  " order by mon  "
							+ ")  a");
		}
		
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
			
			rightY.put("min", Integer.parseInt(rightYMin.toString().substring(0,rightYMin.toString().indexOf("."))) - 5);
			rightY.put("max", Integer.parseInt(rightYMax.toString().substring(0,rightYMax.toString().indexOf("."))) + 5);
			
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
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTodayTotalCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_PROD_DY' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		sql.append("select * from "+tableName+" where PRODUCT_LINE_NAME='"+companyId+"' ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 返回的jsonarray
			JSONArray ja = new JSONArray();
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			// 开工数量 5 年开工 6 日开工
			ja.add(Util.returnTodayTotalIntMsg("开工数量", "单", res[5], res[6], df));
			
			// 出证数量 15 年证书 16 日证书
			ja.add(Util.returnTodayTotalIntMsg("出证数量", "", res[15], res[16], df));
			
			// 客户数量 9 年客户 10 日客户
			ja.add(Util.returnTodayTotalIntMsg("客户数量", "个", res[9], res[10], df));
			
			// 供应商数量 4 年供应商 3 日供应商
			ja.add(Util.returnTodayTotalIntMsg("供应商数量", "个", res[4], res[3], df));

			// 委托金额 13 年委托金额 14 日委托金额
			ja.add(Util.returnTodayTotalDoubleMsg("委托金额", "元", res[13], res[14], df));

			// 完工金额 11 年完工金额 12 日完工金额 
			ja.add(Util.returnTodayTotalDoubleMsg("完工金额", "元", res[11], res[12], df));
			
			// 开票收入 7 年开票收入 8 日开票收入 
			ja.add(Util.returnTodayTotalDoubleMsg("开票收入", "元", res[7], res[8], df));
			
			// 成本总额 1 年成本总额 2 日成本总额 
			ja.add(Util.returnTodayTotalDoubleMsg("成本总额", "元", res[1], res[2], df));
			
			map.put("ttdata", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByCompany(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_SORG_LEVEL3' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		sql.append(" select * from ( " + 
				"select  " + 
				"      SORG_LEVEL2 as a, '父节点' as b, "
				+ " case when sum(委托单数) is null then 0 else sum(委托单数) end as c, "
				+ " case when sum(开票) is null then 0 else sum(开票) end as d, "
				+ " case when sum(成本) is null then 0 else sum(成本) end as e, "
				+ " case when sum(证书) is null then 0 else sum(证书) end as f " + 
				"from "+tableName+" aa " + 
				"group by SORG_LEVEL2  " + 
				" " + 
				"union   " + 
				" " + 
				"select  " + 
				"     SORG_LEVEL2 a, SORG_LEVEL3 b,  " + 
				"     (case when 委托单数 is null then 0 else 委托单数 end) as c,  " + 
				"     (case when 开票 is null then 0 else 开票 end) as d,  " + 
				"     (case when 成本 is null then 0 else 成本 end) as e,  " + 
				"     (case when 证书 is null then 0 else 证书 end) as f " + 
				"from  "
					+ tableName + 
				"  ) aa        " + 
				"order by aa.a asc, aa.d desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnCompanyMsg(resultList, "父节点");
			map.put("tableData", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByArea(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_AREA' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		sql.append(" select * from ( " + 
				"select  " + 
				"        BUSINESS_REGION as a, '父节点' as b, " + 
				"        case " + 
				"                when sum(委托单数) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(委托单数) " + 
				"              end as c, " + 
				"              case " + 
				"                when sum(开票) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(开票) " + 
				"              end as d, " + 
				"              case " + 
				"                when sum(成本) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(成本) " + 
				"              end as e, " + 
				"              case " + 
				"                when sum(证书) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(证书) " + 
				"              end as f  " + 
				" from  " + tableName + 
				" group by BUSINESS_REGION " + 
				" " + 
				"union  " + 
				" " + 
				"select  " + 
				"      BUSINESS_REGION as a, SORG_LEVEL3 b,  " + 
				"     (case when 委托单数 is null then 0 else 委托单数 end) as c,  " + 
				"     (case when 开票 is null then 0 else 开票 end) as d,  " + 
				"     (case when 成本 is null then 0 else 成本 end) as e,  " + 
				"     (case when 证书 is null then 0 else 证书 end) as f " + 
				"from  " + tableName + 
				" ) aa " + 
				" " + 
				"order by aa.a asc, aa.d desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnCompanyMsg(resultList, "父节点");
			map.put("tableData", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByCpx(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_PRODUCT' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		sql.append(" select * from ( " + 
				"select  " + 
				"        PRODUCT_LINE_NAME_ST1 as a, '父节点' as b, " + 
				"        case " + 
				"                when sum(委托单数) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(委托单数) " + 
				"              end as c, " + 
				"              case " + 
				"                when sum(开票) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(开票) " + 
				"              end as d, " + 
				"              case " + 
				"                when sum(成本) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(成本) " + 
				"              end as e, " + 
				"              case " + 
				"                when sum(证书) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(证书) " + 
				"              end as f  " + 
				"from  " + tableName+ 
				"        " + 
				"group by PRODUCT_LINE_NAME_ST1 " + 
				" " + 
				"union  " + 
				" " + 
				"select  " + 
				"      PRODUCT_LINE_NAME_ST1 as a, 产品线NAME b,  " + 
				"     (case when 委托单数 is null then 0 else 委托单数 end) as c,  " + 
				"     (case when 开票 is null then 0 else 开票 end) as d,  " + 
				"     (case when 成本 is null then 0 else 成本 end) as e,  " + 
				"     (case when 证书 is null then 0 else 证书 end) as f " + 
				"from  " + tableName+ 
				"        " + 
				") aa " + 
				" " + 
				"order by aa.a asc, aa.d desc " );
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnCompanyMsg(resultList, "父节点");
			JSONArray phx = new JSONArray();
			JSONArray type = new JSONArray();
			
			type.add("大宗贸易");
			type.add("农食安全及溯源");
			type.add("工业");
			type.add("消费品");
			type.add("政府与机构业务");
			type.add("认证服务与企业优化");
			type.add("其他");
			type.add("其他支出项");
			
			for (int i = 0; i < type.size(); i++) {
				for (int j = 0; j < ja.size(); j++) {
					if(type.get(i).toString().equals(ja.getJSONObject(j).getString("gsname"))) {
						phx.add(ja.getJSONObject(j));
						break;
					}
				}
			}
			
			map.put("tableData", phx);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsGxbar(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VEIW_ZJYT_SORG_GX' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		String cond = "select * from "+tableName+" where SORG_LEVEL3= '"+companyId+"' order by 委托金额 asc";
		
		if(companyId != null && !companyId.equals("") 
				&& ( companyId.equals("东南亚区域") || companyId.equals("欧洲区域") || companyId.equals("非洲区域")  ) ) {
			String childs = resMap.get("childs");
			cond = "select b.SORG_LEVEL3,b.CUSTOMER_NAME,b.委托金额,b.已到款 " + 
					"from ( " + 
					"select rownum rn,a.*  " + 
					"from (  " + 
					"select  " + 
					"  * " + 
					"from "+tableName+" " + 
					"where SORG_LEVEL3 in ("+childs.substring(0, childs.length() - 1)+") " + 
					"order by 委托金额 desc " + 
					") a  " + 
					") b where b.rn < 6  order by b.委托金额 asc";
		}			
		
		sql.append(cond);
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONObject ja = Util.returnJtgsGxbarOption(resultList);
			
			map.put("gxbarOption", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsCompanyMsg(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_SORG_COMPANY' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		String cond = " where SORG_LEVEL3= '"+companyId+"'";
		
		if(companyId != null && !companyId.equals("") 
				&& ( companyId.equals("东南亚区域") || companyId.equals("欧洲区域") || companyId.equals("非洲区域")  ) ) {
			String childs = resMap.get("childs");
			cond = " where SORG_LEVEL3 in ("+childs.substring(0, childs.length() - 1)+") ";
		}
		
		sql.append("select 公司NAME, ")
			.append(" case when 委托单数 is null then 0 else 委托单数 end as c, ")
			.append(" case when 开票 is null then 0 else 开票 end as d, ")
			.append(" case when 成本 is null then 0 else 成本 end as e, ")
			.append(" case when 证书 is null then 0 else 证书 end as f ")
			.append(" from ")
			.append(tableName)
			.append(cond)
			.append(" order by  d desc");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.findJtgsCompanyMsg(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String,Object> findFirstCompanyKp(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select TABLE_NAME_EN from table_zjyt where table_uuid = 'VIEW_ZJYT_SORG_INVOICE' and table_state=1");
		Object tableName = DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();

		if (tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}

		sql.delete(0, sql.length());
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		String cond = "select * from "+tableName+" where SORG_LEVEL3 = '"+companyId+"'";
		
		if(companyId != null && !companyId.equals("") 
				&& ( companyId.equals("东南亚区域") || companyId.equals("欧洲区域") || companyId.equals("非洲区域")  ) ) {
			String childs = resMap.get("childs");
			cond = "select  " + 
					"       '区域公司','"+companyId+"',PRODUCT_LINE_NAME_ST1, sum(开票)  " + 
					" from "+tableName+ 
					" where SORG_LEVEL3 in ("+childs.substring(0, childs.length() - 1)+") " + 
					" group by PRODUCT_LINE_NAME_ST1";
		}
		
		sql.append(cond);
		System.out.println("------------------" + sql.toString());
		List<Object[]> obj = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		JSONArray ja = new JSONArray();
		if (obj != null && obj.size() > 0) {
			for (Object[] o : obj) {
				JSONObject jo = new JSONObject();
				jo.put("name", o[2] == null ? "" : o[2]);
				jo.put("value", o[3] == null ? 0 : o[3]);
				ja.add(jo);
			}
		}
		
		map.put("zbpieOption",ja);
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_SORG_CUSTOMER' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		String groupType = resMap.get("groupType");
		String pageNum = resMap.get("pageNum");
		
		if(companyId != null && !companyId.equals("") 
				&& ( companyId.equals("东南亚区域") || companyId.equals("欧洲区域") || companyId.equals("非洲区域")  ) ) {
			String childs = resMap.get("childs");
			sql.append(" select * ")
				.append("       from (select aa.*, rownum as rm ")
				.append("             from (select CUSTOMER_NAME, NVL(委托金额,0) a, NVL(开票,0) b, NVL(已到款,0) c, NVL(证书,0) d  ")
				.append("             from ").append(tableName)
				.append("             where SORG_LEVEL3 in (").append(childs.substring(0, childs.length() - 1)).append(") ")
				.append("              and GROUP_TYPE='").append(groupType).append("' ")
				.append("             order by a desc  ")
				.append("       ) aa where rownum < ").append(Integer.parseInt(pageNum)*10+1)
				.append(" )bb where rm > ").append((Integer.parseInt(pageNum) - 1) * 10);
		}else {
			sql.append(" select * from ( ")
				.append("       select aa.*, rownum as rm from ( ")
				.append("             select CUSTOMER_NAME,NVL(委托金额,0) a,NVL(开票,0) b,NVL(已到款,0) c,NVL(证书,0) d  ")
				.append("             from ").append(tableName)
				.append("             where SORG_LEVEL3 ='").append(companyId).append("' ")
				.append("              and GROUP_TYPE='").append(groupType).append("' ")
				.append("             order by a desc  ")
				.append("       ) aa where rownum < ").append(Integer.parseInt(pageNum)*10+1)
				.append(" )bb where rm > ").append((Integer.parseInt(pageNum) - 1) * 10);
		}
		
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnTableKh(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findZjytTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_CUSTOMER' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String groupType = resMap.get("groupType");
		String pageNum = resMap.get("pageNum");
		
		sql.append(" select * from ( ")
			.append("       select aa.*, rownum as rm from ( ")
			.append("             select CUSTOMER_NAME,委托金额,已开票,已收款,出证数量 ")
			.append("             from ").append(tableName)
			.append("             where GROUP_TYPE='").append(groupType).append("' ")
			.append("             order by 委托金额 desc  ")
			.append("       ) aa where rownum < ").append(Integer.parseInt(pageNum)*10+1)
			.append(" )bb where rm > ").append((Integer.parseInt(pageNum) - 1) * 10);
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnTableKh(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTotalCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_SORG_DY' and TABLE_STATE=1 ");

		Object tableName = DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();

		if (tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}

		sql.delete(0, sql.length());
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		String cond = "select * from " + tableName + " where SORG_LEVEL3 ='"+companyId+"'";
		
		if(companyId != null && !companyId.equals("") 
				&& ( companyId.equals("东南亚区域") || companyId.equals("欧洲区域") || companyId.equals("非洲区域")  ) ) {
			String childs = resMap.get("childs");
			cond = " select '"+companyId+"' as comp, " + 
					"       sum(年成本) as 年成本, " + 
					"       sum(日成本) as 日成本, " + 
					"       sum(日供应商) as 日供应商, " + 
					"       sum(年供应商) as 年供应商, " + 
					"       sum(年开工) as 年开工, " + 
					"       sum(日开工) as 日开工, " + 
					"       sum(年开票) as 年开票, " + 
					"       sum(日开票) as 日开票, " + 
					"       sum(年客户) as 年客户, " + 
					"       sum(日客户) as 日客户, " + 
					"       sum(年完工金额) as 年完工金额, " + 
					"       sum(日完工金额) as 日完工金额, " + 
					"       sum(年委托金额) as 年委托金额, " + 
					"       sum(日委托金额) as 日委托金额, " + 
					"       sum(年证书) as 年证书, " + 
					"       sum(日证书) as 日证书 " + 
					" from " + tableName + 
					" where sorg_level3 in ("+childs.substring(0,childs.length() - 1)+")";
		}
		
		sql.append(cond);
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();

		Map<String, Object> map = new HashMap<String, Object>();
		// 返回的jsonarray
		JSONArray ja = new JSONArray();
		if (resultList != null && resultList.size() > 0) {

			Object[] res = resultList.get(0);

			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");

			// 开工数量 5年开工 6日开工
			ja.add(Util.returnTodayTotalIntMsg("开工数量", "单", res[5], res[6], df));

			// 出证数量 15 年证书 16 日证书
			ja.add(Util.returnTodayTotalIntMsg("出证数量", "", res[15], res[16], df));

			// 客户数量 9年客户 10日客户
			ja.add(Util.returnTodayTotalIntMsg("客户数量", "个", res[9], res[10], df));

			// 供应商数量 4 年供应商 3 日供应商
			ja.add(Util.returnTodayTotalIntMsg("供应商数量", "个", res[4], res[3], df));

			// 委托金额 13 年委托金额 14 日委托金额
			ja.add(Util.returnTodayTotalDoubleMsg("委托金额", "元", res[13], res[14], df));

			// 完工金额 11 年完工金额 12 日完工金额
			ja.add(Util.returnTodayTotalDoubleMsg("完工金额", "元", res[11], res[12], df));

			// 开票收入 7 年开票收入 8 日开票收入
			ja.add(Util.returnTodayTotalDoubleMsg("开票收入", "元", res[7], res[8], df));

			// 成本总额 1年成本总额 2 日成本总额
			ja.add(Util.returnTodayTotalDoubleMsg("成本总额", "元", res[1], res[2], df));
		}

		map.put("ttdata", ja);

		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJTGSCompanyCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_SORG_PRODUCT' and TABLE_STATE=1");
		Object tableName = DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();

		if (tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		sql.delete(0, sql.length());
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		String cond = "select  产品线NAME a,  " 
				+ "     (case when 委托单数 is null then 0 else 委托单数 end) as c,  "
				+ "     (case when 开票 is null then 0 else 开票 end) as d,  "
				+ "     (case when 成本 is null then 0 else 成本 end) as e,  "
				+ "     (case when 证书 is null then 0 else 证书 end) as f " 
				+ " from  " + tableName
				+ " where SORG_LEVEL3 ='"+companyId+"'  order by d desc";
		
		if(companyId != null && !companyId.equals("") 
				&& ( companyId.equals("东南亚区域") || companyId.equals("欧洲区域") || companyId.equals("非洲区域")  ) ) {
			String childs = resMap.get("childs");
			cond = "select 产品线NAME a, " + 
					"       (case " + 
					"         when sum(委托单数) is null then " + 
					"          0 " + 
					"         else " + 
					"          sum(委托单数) " + 
					"       end) as c, " + 
					"       (case " + 
					"         when sum(开票) is null then " + 
					"          0 " + 
					"         else " + 
					"          sum(开票) " + 
					"       end) as d, " + 
					"       (case " + 
					"         when sum(成本) is null then " + 
					"          0 " + 
					"         else " + 
					"          sum(成本) " + 
					"       end) as e, " + 
					"       (case " + 
					"         when sum(证书) is null then " + 
					"          0 " + 
					"         else " + 
					"          sum(证书) " + 
					"       end) as f " + 
					"from "+tableName+" " + 
					"where SORG_LEVEL3 in ("+childs.substring(0, childs.length() - 1)+") " + 
					"group by 产品线NAME " + 
					"order by d desc";
		}			
		sql.append(cond);
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();

		Map<String, Object> map = new HashMap<String, Object>();

		if (resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnJTGSCompanyCpx(resultList);
			map.put("tableData", ja);
		}

		return map;
	}  
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSrpieCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_PROD_SERVICE' and TABLE_STATE=1");
		Object tableName = DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();

		if (tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		sql.delete(0, sql.length());
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		
		sql.append(" select SERVICE_MEDIUMCLASS_NAME, "
				+ " case when sum(开票) is null then 0 else sum(开票) end as a "
				+ " from  " +  tableName
				+ " where PRODUCT_LINE_NAME='"+companyId+"' and SERVICE_MEDIUMCLASS_NAME is not null " 
				+ " group by SERVICE_MEDIUMCLASS_NAME"
				+ " order by a desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();

		Map<String, Object> map = new HashMap<String, Object>();

		if (resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnSrpieCpx(resultList);
			map.put("tableData", ja);
		}

		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsSrbar(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_PROD_GOODS' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		sql.append(" select GOODS_MEDIUMCLASS_NAME, "
				+ " case when sum(开票) is null then 0 else sum(开票) end as a "
				+ " from  " +  tableName
				+ " where PRODUCT_LINE_NAME='"+companyId+"' and GOODS_MEDIUMCLASS_NAME is not null " 
				+ " group by GOODS_MEDIUMCLASS_NAME "
				+ " order by a desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONObject ja = Util.returnJtgsSrbarOption(resultList);
			
			map.put("srbarOption", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCpxGxbar(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_PROD_GX' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String companyId = resMap.get("companyId");
		sql.append("select * from "+tableName).append(" where PRODUCT_LINE_NAME= '").append(companyId).append("'").append(" order by 委托金额 asc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONObject ja = Util.returnJtgsGxbarOption(resultList);
			
			map.put("gxbarOption", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCpxMsgByJsfl(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		// 获取当前查询jsid
		Map<String, String> resMap = fetchWebReq.getData();
		String jsid = resMap.get("jsid");
		String cpxname = resMap.get("cpxname");
		String cond = jsid.equals("1") ? "VIEW_ZJYT_PROD_SERVICE" : "VIEW_ZJYT_PROD_GOODS" ;
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='"+cond+"' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		String as = "SERVICE";
				
		if( jsid.equals("2")) {
			as = "GOODS";
		}
		
		sql.append(" select * from ( " + 
				"select  " + 
				"        "+as+"_MAINCLASS_NAME as a, '父节点' as b, " + 
				"        case " + 
				"                when sum(委托单数) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(委托单数) " + 
				"              end as c, " + 
				"              case " + 
				"                when sum(开票) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(开票) " + 
				"              end as d, " + 
				"              case " + 
				"                when sum(成本) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(成本) " + 
				"              end as e, " + 
				"              case " + 
				"                when sum(证书) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(证书) " + 
				"              end as f  " + 
				" from  " + tableName + 
				" where PRODUCT_LINE_NAME='"+cpxname+"' " +
				" and "+as+"_MAINCLASS_NAME is not null " +
				" group by "+as+"_MAINCLASS_NAME " + 
				" " + 
				"union  " + 
				" " + 
				"select  " + 
				"      "+as+"_MAINCLASS_NAME as a, "+as+"_MEDIUMCLASS_NAME b,  " + 
				"        case " + 
				"                when sum(委托单数) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(委托单数) " + 
				"              end as c, " + 
				"              case " + 
				"                when sum(开票) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(开票) " + 
				"              end as d, " + 
				"              case " + 
				"                when sum(成本) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(成本) " + 
				"              end as e, " + 
				"              case " + 
				"                when sum(证书) is null then " + 
				"                 0 " + 
				"                else " + 
				"                 sum(证书) " + 
				"              end as f  " + 
				"from  " + tableName + 
				" where PRODUCT_LINE_NAME='"+cpxname+"' " +
				" and "+as+"_MAINCLASS_NAME is not null " +
				" group by "+as+"_MAINCLASS_NAME, "+as+"_MEDIUMCLASS_NAME ) aa " + 
				" " + 
				"order by aa.a asc, aa.d desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnCompanyMsg(resultList, "父节点");
			map.put("tableData", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCpxTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_PROD_CUSTOMER' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前公司id
		Map<String, String> resMap = fetchWebReq.getData();
		String cpxName = resMap.get("cpxName");
		String groupType = resMap.get("groupType");
		String pageNum = resMap.get("pageNum");
		
		sql.append(" select * from ( ")
			.append("       select aa.*, rownum as rm from ( ")
			.append("             select CUSTOMER_NAME,委托金额,开票,已到款,证书 ")
			.append("             from ").append(tableName)
			.append("             where GROUP_TYPE='").append(groupType).append("' ")
			.append("             and PRODUCT_LINE_NAME='").append(cpxName).append("' ")
			.append("             order by 委托金额 desc  ")
			.append("       ) aa where rownum < ").append(Integer.parseInt(pageNum)*10+1)
			.append(" )bb where rm > ").append((Integer.parseInt(pageNum) - 1) * 10);
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnTableKh(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByCpxName(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select TABLE_NAME_EN from table_zjyt where TABLE_UUID='VIEW_ZJYT_PROD_COMPANY' and TABLE_STATE=1 ");
		
		Object tableName= DBManager.get("kabBan").createNativeQuery(sql.toString()).getSingleResult();
		
		if(tableName == null || tableName.toString().equals("")) {
			throw new Exception();
		}
		
		sql.delete(0, sql.length());
		
		// 获取当前产品线名称
		Map<String, String> resMap = fetchWebReq.getData();
		String cpxName = resMap.get("cpxName");
		
		sql.append("select * from ( " + 
				"select COMPANY_BUSI_ORG_NAME as a, " + 
				"      case " + 
				"        when 委托单数 is null then " + 
				"         0 " + 
				"        else " + 
				"         委托单数 " + 
				"      end as c, case " + 
				"        when 开票 is null then " + 
				"         0 " + 
				"        else " + 
				"         开票 " + 
				"      end as d, case " + 
				"        when 成本 is null then " + 
				"         0 " + 
				"        else " + 
				"         成本 " + 
				"      end as e, case " + 
				"        when 证书 is null then " + 
				"         0 " + 
				"        else " + 
				"         证书 " + 
				"      end as f  " + 
				"from " + tableName +" " +
				"where PRODUCT_LINE_NAME = '"+cpxName+"' and COMPANY_BUSI_ORG_NAME is not null " + 
				"union " + 
				"select '总计' as a, " + 
				"      case " + 
				"        when sum(委托单数) is null then " + 
				"         0 " + 
				"        else " + 
				"         sum(委托单数) " + 
				"      end as c, case " + 
				"        when sum(开票) is null then " + 
				"         0 " + 
				"        else " + 
				"         sum(开票) " + 
				"      end as d, case " + 
				"        when sum(成本) is null then " + 
				"         0 " + 
				"        else " + 
				"         sum(成本) " + 
				"      end as e, case " + 
				"        when sum(证书) is null then " + 
				"         0 " + 
				"        else " + 
				"         sum(证书) " + 
				"      end as f  " + 
				"from "  + tableName +" " + 
				"where PRODUCT_LINE_NAME = '"+cpxName+"' and COMPANY_BUSI_ORG_NAME is not null " + 
				"group by PRODUCT_LINE_NAME " + 
				") aa order by aa.d desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
    @Override
    public Map<String, Object> findStationByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
            // TODO Auto-generated method stub
            StringBuffer sql = new StringBuffer();
            Map<String, String> rem = fetchWebReq.getData();
            String companyId = rem.get("companyId");
            sql.append("select ENT_NAME, NVL(replace(LEGAL,'null'),'-'), " + 
                            " (select count(*) from infoshar_432395066" + 
                            "  where INFOSHAR_TIME is not null" + 
                            "  and LEGAL = aaa.LEGAL" + 
                            "  and ent_status = aaa.ent_status " + 
                            "  and BUSS_REGIS_NUM != '-'" + 
                            "  ) as comNum," + 
                            "  NVL(REGISTTIME,'-'),NVL(ENT_STATUS,'-'),NVL(REGISTFUND,'0人民币'),NVL(SJZB,'-'),NVL(UNI_SOC_CRE_CODE,'-')," + 
                            "  NVL(BUSS_REGIS_NUM,'-'), NVL(ORG_CODE,'-'), NVL(PAYTAXMAN,'-'),NVL(NSRZZ,'-'),NVL(COMPANY_TYPE,'-')," + 
                            "  NVL(INDUSTRY,'-'),NVL(replace(OPERATING_PERIOD ,'无固定期限','-'),'-'),NVL(RYGM,'-'),NVL(CBRS,'0'),NVL(ENNAME,'-')," + 
                            "  NVL(REGISTRATION_AUTHORITY,'-'),NVL(CHEAPPRDATE,'-'),NVL(replace(REG_PLC_ADDR, CHR(38)||'|'||CHR(38)||'附近公司'),'-')," + 
                            "  NVL(BIZ_SCOP,'-'),id" + 
                            " from infoshar_432395066 aaa" + 
                            " where INFOSHAR_TIME is not null" + 
                            " and ENT_NAME = '"+companyId+"'");
            System.out.println("-----------"+sql.toString());
            List<Object[]> resultList = DBManager.get("customerInfo2").createNativeQuery(sql.toString()).getResultList();
            Map<String, Object> map = new HashMap<String, Object>();
            sql.delete(0, sql.length());
            if(resultList != null && resultList.size() != 0) {
                    Object[] o = resultList.get(0);
                    JSONArray jArray = new JSONArray();
                    jArray.add(Util.returnStionCompany("法定代表人", "他有公司"));
                    jArray.add(Util.returnStionCompany(o[1], o[2]+"家"));
                    jArray.add(Util.returnStionCompany("成立日期", "经营状态"));
                    jArray.add(Util.returnStionCompany(o[3], o[4]));
                    jArray.add(Util.returnStionCompany("注册资本", "实缴资本"));
                    jArray.add(Util.returnStionCompany(o[5], o[6]));
                    jArray.add(Util.returnStionCompany("统一社会信用代码", ""));
                    jArray.add(Util.returnStionCompany(o[7], ""));
                    jArray.add(Util.returnStionCompany("工商注册号", "组织机构代码"));
                    jArray.add(Util.returnStionCompany(o[8], o[9]));
                    jArray.add(Util.returnStionCompany("纳税人识别号", "纳税人资质"));
                    jArray.add(Util.returnStionCompany(o[10], o[11]));
                    jArray.add(Util.returnStionCompany("企业类型", ""));
                    jArray.add(Util.returnStionCompany(o[12], ""));
                    jArray.add(Util.returnStionCompany("行业", ""));
                    jArray.add(Util.returnStionCompany(o[13], ""));
                    jArray.add(Util.returnStionCompany("营业期限", ""));
                    jArray.add(Util.returnStionCompany(o[14], ""));
                    jArray.add(Util.returnStionCompany("人员规模", "参保人数"));
                    jArray.add(Util.returnStionCompany(o[15], o[16]));
                    jArray.add(Util.returnStionCompany("英文名称", ""));
                    jArray.add(Util.returnStionCompany(o[17], ""));
                    jArray.add(Util.returnStionCompany("登记机关", ""));
                    jArray.add(Util.returnStionCompany(o[18], ""));
                    jArray.add(Util.returnStionCompany("核准日期", ""));
                    jArray.add(Util.returnStionCompany(o[19], ""));
                    jArray.add(Util.returnStionCompany("注册地址", ""));
                    jArray.add(Util.returnStionCompany(o[20], ""));
                    jArray.add(Util.returnStionCompany("经营范围", ""));
                    jArray.add(Util.returnStionCompany(o[21], ""));        
                    
                    map.put("tdata",jArray);
                    map.put("id",o[22]);
            }
            return map;
    }
	
	@RpcMethod(loginValidate = false)
    @Override
    public Map<String, Object> findGdmsgByCompanyId(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
            // TODO Auto-generated method stub
            StringBuffer sql = new StringBuffer();
            Map<String, String> rem = fetchWebReq.getData();
            String companyId = rem.get("companyId");
            sql.append("select  " + 
            		" NVL(SHAREHOLDERS,'-'),NVL(RATIO,'-'),NVL(CONTRIBUTIVE,'-') " + 
            		" from infoshar_536933980 " + 
            		" where id = '"+companyId+"' " + 
            		" order by SERIAL_NUMBER asc, length(SHAREHOLDERS) asc");
            System.out.println("-----------"+sql.toString());
            List<Object[]> resultList = DBManager.get("customerInfo2").createNativeQuery(sql.toString()).getResultList();
            Map<String, Object> map = new HashMap<String, Object>();
            sql.delete(0, sql.length());
            if(resultList != null && resultList.size() > 0) {
                    JSONArray ja = Util.returnGdMsg(resultList);
                    map.put("tdata",ja);
            }
            return map;
    }
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findGgmsgByCompanyId(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, String> rem = fetchWebReq.getData();
		String companyId = rem.get("companyId");
		sql.append("select NVL(NAME,'-'), NVL(DUTY,'-') from INFOSHAR_446650136 where id = '"+companyId+"'");
		System.out.println("-----------"+sql.toString());
		List<Object[]> resultList = DBManager.get("customerInfo2").createNativeQuery(sql.toString()).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		sql.delete(0, sql.length());
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnGgMsg(resultList);
			map.put("tdata",ja);
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findFzjgmsgByCompanyId(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		Map<String, String> rem = fetchWebReq.getData();
		String companyId = rem.get("companyId");
		sql.append("select  " + 
				"  NVL(ENT_NAME,'-'),NVL(LEGAL_REPRESENT,'-'),NVL(STATE,'-'),NVL(REGISTRATION_DATE,'-') " + 
				"from " + 
				"infoshar_666443020  " + 
				"where id = '"+companyId+"'");
		System.out.println("-----------"+sql.toString());
		List<Object[]> resultList = DBManager.get("customerInfo2").createNativeQuery(sql.toString()).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		sql.delete(0, sql.length());
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = Util.returnFzjgMsg(resultList);
			map.put("tdata",ja);
		}
		return map;
	}
}
