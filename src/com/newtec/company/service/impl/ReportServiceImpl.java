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
		sql.append("select * from "+tableName).append(" where SORG_LEVEL3= '").append(companyId).append("'").append(" order by 委托金额 asc ");;
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
		sql.append("select 公司NAME, ")
			.append(" case when 委托单数 is null then 0 else 委托单数 end as c, ")
			.append(" case when 开票 is null then 0 else 开票 end as d, ")
			.append(" case when 成本 is null then 0 else 成本 end as e, ")
			.append(" case when 证书 is null then 0 else 证书 end as f ")
			.append(" from ")
			.append(tableName)
			.append(" where SORG_LEVEL3= '")
			.append(companyId).append("'")
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
		sql.append("select * from "+tableName+" where SORG_LEVEL3 = '"+companyId+"'");
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
		
		sql.append(" select * from ( ")
			.append("       select aa.*, rownum as rm from ( ")
			.append("             select CUSTOMER_NAME,委托金额,开票,已到款,证书  ")
			.append("             from ").append(tableName)
			.append("             where SORG_LEVEL3 ='").append(companyId).append("' ")
			.append("              and GROUP_TYPE='").append(groupType).append("' ")
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

		sql.append("select * from " + tableName + " where SORG_LEVEL3 ='"+companyId+"'");
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
		
		sql.append("select  产品线NAME a,  " 
				+ "     (case when 委托单数 is null then 0 else 委托单数 end) as c,  "
				+ "     (case when 开票 is null then 0 else 开票 end) as d,  "
				+ "     (case when 成本 is null then 0 else 成本 end) as e,  "
				+ "     (case when 证书 is null then 0 else 证书 end) as f " 
				+ " from  " + tableName
				+ " where SORG_LEVEL3 ='"+companyId+"' ");
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
}
