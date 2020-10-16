package com.newtec.company.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtec.company.service.NewReportService;
import com.newtec.company.utils.NewUtils;
import com.newtec.company.utils.Util;
import com.newtec.reflect.annotation.RpcClass;
import com.newtec.reflect.annotation.RpcMethod;
import com.newtec.router.request.FetchWebRequest;
import com.newtec.rpc.db.DBManager;

@RpcClass(value = "NewReportService", http = true )
public class NewReportServiceImpl implements NewReportService {
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTotalByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = " select '总计' as a," + 
                "         case when sum(yysrbnlj) is null then 0 else sum(yysrbnlj) end as b," + 
                "         case when sum(yycbbnlj) is null then 0 else sum(yycbbnlj) end as c," + 
                "         case when sum(yylybnlj) is null then 0 else sum(yylybnlj) end as d," + 
                "         case when sum(lrzebnlj) is null then 0 else sum(lrzebnlj) end as e," + 
                "         case when sum(jlrbnlj)  is null then 0 else sum(jlrbnlj) end as f" + 
                "    from infoshar_202007kbsj " + 
                " where month=(select max(month) from infoshar_202007kbsj)"+ 
                "   and org_name_t is not null  ";
    	System.out.println(sql);
    List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {
			Object[] o = list.get(0);
			JSONArray jArray = new JSONArray();
			DecimalFormat dFormat = new DecimalFormat("#.00");
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业收入", o[1], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业成本", o[2], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业利润", o[3], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("利润总额", o[4], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("净利润", o[5], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg(null, null, null));
			map.put("tdata",jArray);
			
		}
		return map;
	}

    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTotalByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql ="select org_name_t as 公司名称," + 
                "         case when to_number(yysrbnlj) is null then 0 else to_number(yysrbnlj) end as 营业收入," + 
                "         case when to_number(yycbbnlj )is null then 0 else to_number(yycbbnlj) end as 营业成本," + 
                "         case when to_number(yylybnlj )is null then 0 else to_number(yylybnlj) end as 营业利润," + 
                "         case when to_number(lrzebnlj )is null then 0 else to_number(lrzebnlj) end as 利润总额," + 
                "         case when to_number(jlrbnlj ) is null then 0 else to_number(jlrbnlj ) end as 净利润," +
                "         month" + 
                "    from infoshar_202007kbsj " + 
                "    where month=(select max(month) from infoshar_202007kbsj)"+
                "   and org_name_t is not null " + 
                "   and org_name_t = '"+companyId+"'";
		System.out.println(sql);
    	List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {
			Object[] o = list.get(0);
			JSONArray jArray = new JSONArray();
			DecimalFormat dFormat = new DecimalFormat("#.00");
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业收入", o[1], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业成本", o[2], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业利润", o[3], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("利润总额", o[4], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("净利润", o[5], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg(null, null, null));
			map.put("tdata",jArray);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTotalByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " select '总计' as a,quyu," + 
                "         case when sum(yysrbnlj) is null then 0 else sum(yysrbnlj) end as b," + 
                "         case when sum(yycbbnlj) is null then 0 else sum(yycbbnlj) end as c," + 
                "         case when sum(yylybnlj) is null then 0 else sum(yylybnlj) end as d," + 
                "         case when sum(lrzebnlj) is null then 0 else sum(lrzebnlj) end as e," + 
                "         case when sum(jlrbnlj)  is null then 0 else sum(jlrbnlj) end as f" + 
                "    from infoshar_202007kbsj " + 
                " where month=(select max(month) from infoshar_202007kbsj)"+
                "   and org_name_t is not null " +
                "   and quyu='"+companyId+"'"+
                " group by quyu";  
    	System.out.println(sql);
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {
			Object[] o = list.get(0);
			JSONArray jArray = new JSONArray();
			DecimalFormat dFormat = new DecimalFormat("#.00");
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业收入", o[2], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业成本", o[3], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("营业利润", o[4], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("利润总额", o[5], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg("净利润", o[6], dFormat));
			jArray.add(NewUtils.returnTodayTotalDoubleMsg(null, null, null));
			map.put("tdata",jArray);
			
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYysrTbByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "select  to_number(substr(month, 6, 2))||'月' as month," + 
    			"       sum(yysrbnlj) 今年营业收入," + 
    			"       sum(yysrsntqljje) 去年营业收入," + 
    			"       round((sum(yysrbnlj) - sum(yysrsntqljje))/sum(yysrsntqljje) * 100,2) as 营业收入同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" + 
    			" group by substr(month, 6, 2) order by month";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxis.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxis.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYycbTbByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "select  to_number(substr(month, 6, 2))||'月' as month," + 
    			"       sum(yycbbnlj) 今年营业成本," + 
    			"       sum(yycbsntqljje) 去年营业成本," + 
    			"       round((sum(yycbbnlj) - sum(yycbsntqljje))/sum(yycbsntqljje) * 100,2) as 营业成本同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" + 
    			" group by substr(month, 6, 2) order by month";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxis.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxis.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYylrTbByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "select  to_number(substr(month, 6, 2))||'月' as month," + 
    			"       sum(yylybnlj) 今年营业利润," + 
    			"       sum(yylrssntqljje) 去年营业利润," + 
    			"       round((sum(yylybnlj) - sum(yylrssntqljje))/sum(yylrssntqljje) * 100,2) as 营业利润同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" + 
    			" group by substr(month, 6, 2) order by month";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxis.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxis.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYysrTbByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
    	String sql = "select substr(month, 7, 1)||'月' as month," + 
    			"       sum(yysrbnlj) 今年营业收入," + 
    			"       sum(yysrsntqljje) 去年营业收入," + 
    			"       round((sum(yysrbnlj) - sum(yysrsntqljje)) / (case when  sum(yysrsntqljje)='0.00' then 1 else sum(yysrsntqljje) end ) * 100,2) as 营业收入同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month,6,2) <= to_char(sysdate,'mm')" + 
    			"   and substr(month,1,4) = '2020'" + 
    			"   and org_name_t = '"+companyId+"'" + 
    			" group by substr(month,7,1)" + 
    			" order by month";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxsi = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxsi.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxsi.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxsi);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYycbTbByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "select  to_number(substr(month, 7, 1))||'月' as month," + 
    			"       sum(yycbbnlj) 今年营业成本," + 
    			"       sum(yycbsntqljje) 去年营业成本," + 
    			"       round((sum(yycbbnlj) - sum(yycbsntqljje))/(case when sum(yycbsntqljje)='0.00'then 1 else sum(yycbsntqljje) end) * 100,2) as 营业成本同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" +
    			" and org_name_t = '"+companyId+"'" +
    			" group by substr(month, 7, 1) order by month";
		System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxsi = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxsi.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxsi.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxsi);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYylrTbByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "select  to_number(substr(month, 7, 1))||'月' as month," + 
    			"       sum(yylybnlj) 今年营业利润," + 
    			"       sum(yylrssntqljje) 去年营业利润," + 
    			"       round((sum(yylybnlj) - sum(yylrssntqljje))/(case when sum(yylrssntqljje)='0.00'then 1 else sum(yylrssntqljje) end) * 100,2) as 营业利润同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" +
    			" and org_name_t = '"+companyId+"'" +
    			" group by substr(month, 7, 1) order by month";
		System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxsi = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxsi.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxsi.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxsi);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYysrTbByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
    	String sql = "select  to_number(substr(month, 6, 2))||'月' as month，" + 
    			"       sum(yysrbnlj) 今年营业收入," + 
    			"       sum(yysrsntqljje) 去年营业收入," + 
    			"       round((sum(yysrbnlj) - sum(yysrsntqljje))/sum(yysrsntqljje) * 100,2) as 营业收入同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" +
    			" and quyu = '"+companyId+"'" +
    			" group by substr(month, 6, 2) order by month";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxis.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxis.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYycbTbByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "select  to_number(substr(month, 6, 2))||'月' as month," + 
    			"       sum(yycbbnlj) 今年营业成本," + 
    			"       sum(yycbsntqljje) 去年营业成本," + 
    			"       round((sum(yycbbnlj) - sum(yycbsntqljje))/sum(yycbsntqljje) * 100,2) as 营业成本同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" +
    			" and quyu = '"+companyId+"'" +
    			" group by substr(month, 6, 2) order by month";
		System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxis.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxis.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYylrTbByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "select  to_number(substr(month, 6, 2))||'月' as month," + 
    			"       sum(yylybnlj) 今年营业利润," + 
    			"       sum(yylrssntqljje) 去年营业利润," + 
    			"       round((sum(yylybnlj) - sum(yylrssntqljje))/sum(yylrssntqljje) * 100,2) as 营业利润同比" + 
    			"  from infoshar_202007kbsj" + 
    			" where substr(month, 6, 2) <= to_char(sysdate, 'mm')  and  substr(month, 1, 4)='2020'" +
    			" and quyu = '"+companyId+"'" +
    			" group by substr(month, 6, 2) order by month";
		System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴月份
			JSONArray yueX = new JSONArray();
			//今年
			JSONArray curYear = new JSONArray();
			//去年
			JSONArray lastYear = new JSONArray();
			//左y轴
			JSONObject leftY = new JSONObject();
			//右y轴
			JSONObject rightY = new JSONObject();
			//同比
			JSONArray tongbi = new JSONArray();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double leftYMin = 0d, leftYMax = 0d, rightYMin = 0d, rightYMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				lastYear.add(ob[2]==null?0:ob[2]);
				tongbi.add(ob[3]==null?0:ob[3]);
				if(ob[1] != null && ob [1] != "") { 
					if(leftYMin > Double.parseDouble(ob[1].toString())) {
						leftYMin = Double.parseDouble(ob[1].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[1].toString())) {
						leftYMax = Double.parseDouble(ob[1].toString());
					}
				}
				if(ob[2] != null && ob [2] != "") { 
					if(leftYMin > Double.parseDouble(ob[2].toString())) {
						leftYMin = Double.parseDouble(ob[2].toString());
					}
					
					if(leftYMax < Double.parseDouble(ob[2].toString())) {
						leftYMax = Double.parseDouble(ob[2].toString());
					}
				}
				if(ob[3] != null && ob [3] != "") { 
					if(rightYMin > Double.parseDouble(ob[3].toString())) {
						rightYMin = Double.parseDouble(ob[3].toString());
					}
					
					if(rightYMax < Double.parseDouble(ob[3].toString())) {
						rightYMax = Double.parseDouble(ob[3].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			saceDate.add(lastYear);
			saceDate.add(tongbi);
			leftY.put("min",leftYMin);
			leftY.put("max",leftYMax);
			yAxis.add(leftY);
			rightY.put("min",rightYMin);
			rightY.put("max",rightYMax);
			yAxis.add(rightY);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyskByZhongjian(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		// TODO Auto-generated method stub
    	String sql = "select \"年委托单量\",\"日委托单量\",\"年开工单量\", \"日开工单量\",\"年完工单量\", \"日完工单量\",\"年委托金额\", \"日委托金额\",\"年预测收入\", \"日预测收入\",\"年客户数量\"," + 
    			"       \"日客户数量\"," + 
    			"       \"产品线活跃客户数量\"," + 
    			"       \"非产品线活跃客户\"," + 
    			"       \"非活跃客户数量\",\"年供应商数量\"," + 
    			"       \"日供应商数量\"," + 
    			"       \"非产品线活跃供应商\"," + 
    			"       \"产品线活跃供应商数量\"," + 
    			"       \"活跃供应商数量\"," + 
    			"       \"非活跃供应商数量\",\"年出证数量\"," + 
    			"       \"纸质报告+纸质证书\"+\"电子报告+电子证书\"," + 
    			"       \"纸质报告+纸质证书\"," + 
    			"       \"电子报告+电子证书\"," + 
    			"       \"其他工作成果物\"," + 
    			"       \"今日出证数量\"," + 
    			"       \"产品线毛利率\"" + 
    			"        from(" + 
    			" SELECT *" + 
    			"  FROM (SELECT COUNT(年委托单量) 年委托单量" + 
    			"          FROM (SELECT COUNT(1) 年委托单量" + 
    			"                  FROM T_ORDER_SERVER" + 
    			"                 WHERE  SUBSTR(CREATE_TIME_ORDER, 1, 4) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY')" + 
    			"                 GROUP BY ORDER_UUID))) A," + 
    			"       (SELECT COUNT(日委托单量) 日委托单量" + 
    			"          FROM (SELECT COUNT(1) 日委托单量" + 
    			"                  FROM T_ORDER_SERVER" + 
    			"                 WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY-MM-DD')" + 
    			"                 GROUP BY ORDER_UUID)) B " + 
    			" left join" + 
    			" (SELECT 年开工单量, 日开工单量" + 
    			"  FROM (SELECT COUNT(日开工单量) 日开工单量" + 
    			"          FROM (SELECT ORDER_UUID 日开工单量" + 
    			"                  FROM T_ORDER_SERVER" + 
    			"                 WHERE SUBSTR(JOB_START_TIME_ORDER, 1, 10) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY-MM-DD')" + 
    			"                   AND JOB_START_TIME_ORDER IS NOT NULL" + 
    			"                 GROUP BY ORDER_UUID)) A," + 
    			"       (SELECT COUNT(年开工单量) 年开工单量" + 
    			"          FROM (SELECT ORDER_UUID 年开工单量" + 
    			"                  FROM T_ORDER_SERVER" + 
    			"                 WHERE SUBSTR(JOB_START_TIME_ORDER, 1, 4) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY')" + 
    			"                   AND JOB_START_TIME_ORDER IS NOT NULL" + 
    			"                 GROUP BY ORDER_UUID)) B)on 1=1" + 
    			"  left join" + 
    			" (SELECT 年完工单量, 日完工单量" + 
    			"  FROM (SELECT COUNT(日完工单量) 日完工单量" + 
    			"          FROM (SELECT ORDER_UUID 日完工单量" + 
    			"                  FROM T_ORDER_SERVER" + 
    			"                 WHERE SUBSTR(work_finish_time, 1, 10) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY-MM-DD')" + 
    			"                   AND JOB_END_TIME_ORDER IS NOT NULL" + 
    			"                 GROUP BY ORDER_UUID)) A," + 
    			"       (SELECT COUNT(年完工单量) 年完工单量" + 
    			"          FROM (SELECT ORDER_UUID 年完工单量" + 
    			"                  FROM T_ORDER_SERVER" + 
    			"                 WHERE SUBSTR(work_finish_time, 1, 4) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY')" + 
    			"                 GROUP BY ORDER_UUID)) B)on 1=1" + 
    			" left join                " + 
    			" (SELECT 年委托金额, 日委托金额" + 
    			"  FROM (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年委托金额" + 
    			"          FROM T_ORDER_SERVER" + 
    			"         WHERE SUBSTR(CREATE_TIME_ORDER, 1, 4) = TO_CHAR(SYSDATE, 'YYYY')) A," + 
    			"       (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日委托金额" + 
    			"          FROM VIEW_ORDER_SERVER_TAB" + 
    			"         WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) =" + 
    			"               TO_CHAR(SYSDATE, 'YYYY-mm-dd')) B)on 1=1" + 
    			"  left join             " + 
    			" (SELECT 年预测收入, 日预测收入" + 
    			"  FROM (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年预测收入" + 
    			"          FROM T_ORDER_SERVER" + 
    			"         WHERE SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY')) A," + 
    			"       (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日预测收入" + 
    			"          FROM T_ORDER_SERVER" + 
    			"         WHERE SUBSTR(WORK_FINISH_TIME, 1, 10) =" + 
    			"               TO_CHAR(SYSDATE, 'YYYY-mm-dd')) B)on 1=1" + 
    			" left join              " + 
    			" (SELECT 年客户数量," + 
    			"       日客户数量," + 
    			"       产品线活跃客户数量 产品线活跃客户数量," + 
    			"       非产品线活跃客户," + 
    			"       年客户数量 - 活跃客户数量 非活跃客户数量" + 
    			"  FROM (SELECT COUNT(年客户数量) 年客户数量" + 
    			"          FROM (SELECT COUNT(1) 年客户数量" + 
    			"                  FROM VIEW_CUSTOMER_ALL" + 
    			"                 GROUP BY CUSTOMER_ID)) A," + 
    			"       (SELECT COUNT(日客户数量) 日客户数量" + 
    			"          FROM (SELECT COUNT(1) 日客户数量" + 
    			"                  FROM VIEW_CUSTOMER_ALL" + 
    			"                 WHERE SUBSTR(CREATE_TIME, 1, 10) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY-MM-DD')" + 
    			"                 GROUP BY CUSTOMER_NAME)) B," + 
    			"       (SELECT COUNT(distinct CUSTOMER_NAME) 活跃客户数量" + 
    			"          FROM (SELECT CUSTOMER_NAME" + 
    			"                  FROM T_ORDER_CRM" + 
    			"                 WHERE SUBSTR(CREATE_TIME_ORDER, 1, 7) >=" + 
    			"                       TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'YYYY-mm')" + 
    			"                 GROUP BY CUSTOMER_NAME)) C," + 
    			"       (SELECT COUNT(CUSTOMER_ID) 非产品线活跃客户" + 
    			"          FROM (SELECT DISTINCT CUSTOMER_ID, CUSTOMER_NAME" + 
    			"                  FROM T_ORDER_CRM" + 
    			"                 WHERE TYPE = '非产品线业务')) D," + 
    			"       (SELECT COUNT(CUSTOMER_ID) 产品线活跃客户数量" + 
    			"          FROM (SELECT DISTINCT CUSTOMER_ID, CUSTOMER_NAME" + 
    			"                  FROM T_ORDER_CRM" + 
    			"                 WHERE TYPE <> '非产品线业务')) E)on 1=1" + 
    			" left join                " + 
    			" (SELECT 年供应商数量," + 
    			"       日供应商数量," + 
    			"       非产品线活跃供应商," + 
    			"       产品线活跃供应商数量 产品线活跃供应商数量," + 
    			"       活跃供应商数量," + 
    			"       年供应商数量 - 活跃供应商数量 非活跃供应商数量" + 
    			"  FROM (SELECT COUNT(CODE) 年供应商数量" + 
    			"          FROM (SELECT CODE FROM INFOSHAR_618389493 GROUP BY CODE)) A," + 
    			"       (SELECT COUNT(CODE) 日供应商数量" + 
    			"          FROM (SELECT CODE" + 
    			"                  FROM INFOSHAR_618389493" + 
    			"                 WHERE SUBSTR(START_TIME, 1, 10) =" + 
    			"                       TO_CHAR(SYSDATE, 'YYYY-MM-DD')" + 
    			"                 GROUP BY CODE)) B," + 
    			"       (SELECT COUNT(SUB_PACKAGE_ID) 活跃供应商数量" + 
    			"          FROM (SELECT SUB_PACKAGE_ID" + 
    			"                  FROM T_ORDER_SRM" + 
    			"                 GROUP BY SUB_PACKAGE_ID)) C," + 
    			"       (SELECT COUNT(1) 非产品线活跃供应商" + 
    			"          FROM (SELECT SUB_PACKAGE_ID" + 
    			"                  FROM T_ORDER_SRM" + 
    			"                 WHERE TYPE = '非产品线业务'" + 
    			"                 GROUP BY SUB_PACKAGE_ID)) D," + 
    			"       (SELECT COUNT(1) 产品线活跃供应商数量" + 
    			"          FROM (SELECT SUB_PACKAGE_ID" + 
    			"                  FROM T_ORDER_SRM" + 
    			"                 WHERE TYPE <> '非产品线业务'" + 
    			"                 GROUP BY SUB_PACKAGE_ID)) E)on 1=1" + 
    			"  left join               " + 
    			" (SELECT 年出证数量," + 
    			"       \"纸质报告+纸质证书\"+\"电子报告+电子证书\"," + 
    			"       \"纸质报告+纸质证书\"," + 
    			"       \"电子报告+电子证书\"," + 
    			"       \"其他工作成果物\"," + 
    			"       今日出证数量" + 
    			"  FROM (SELECT COUNT(MID) AS 年出证数量," + 
    			"               SUM(CASE" + 
    			"                     WHEN CERTTYPE IN ('纸质报告', '纸质证书') THEN" + 
    			"                      1" + 
    			"                     ELSE" + 
    			"                      0" + 
    			"                   END) AS \"纸质报告+纸质证书\"," + 
    			"               SUM(CASE" + 
    			"                     WHEN CERTTYPE IN ('电子报告', '电子证书') THEN" + 
    			"                      1" + 
    			"                     ELSE" + 
    			"                      0" + 
    			"                   END) AS \"电子报告+电子证书\"," + 
    			"               SUM(CASE" + 
    			"                     WHEN CERTTYPE = '其他工作成果物' THEN" + 
    			"                      1" + 
    			"                     ELSE" + 
    			"                      0" + 
    			"                   END) AS \"其他工作成果物\"" + 
    			"          FROM T_ORDER_CERC WHERE SUBSTR(FICTIONAL_DATE,1,4) = to_char(SYSDATE,'YYYY'))," + 
    			"       (SELECT COUNT(MID) AS 今日出证数量" + 
    			"          FROM T_ORDER_CERC" + 
    			"         WHERE SUBSTR(FICTIONAL_DATE, 1, 10) =" + 
    			"               TO_CHAR(SYSDATE, 'YYYY-MM-DD'))) on 1=1 " + 
    			"  left join" + 
    			"  ( SELECT 年业务收入,业务成本,round((年业务收入 - 业务成本) / 年业务收入*100,2) 产品线毛利率" + 
    			"  FROM (SELECT  nvl(SUM(EX_TAX_PRICE),0) 年业务收入" + 
    			"          FROM T_COMP_INVOICE_ALL" + 
    			"         WHERE VOID_TIME IS NULL" + 
    			"           AND TYPE = '产品线业务'" + 
    			"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
    			"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
    			"         ) A," + 
    			" (SELECT SUM(TOTAL_COST) AS 业务成本" + 
    			"    FROM T_COMPANY_COST_ALL" + 
    			"   WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
    			"  ) B) on 1=1              ";
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 返回的jsonarray
			JSONArray ja = new JSONArray();
			JSONArray taArray = new JSONArray();
			JSONArray jArray1 = new JSONArray();
			JSONObject jObject = new JSONObject();
			JSONArray jArray3 = new JSONArray();
			JSONObject jObject2 = new JSONObject();
			JSONArray jArray5 = new JSONArray();
			JSONObject jObject3 = new JSONObject();
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			ja.add(NewUtils.returnYwjiqkIntMsg("委托单量", "今日", res[0], res[1], df));
			ja.add(NewUtils.returnYwjiqkIntMsg("开工单量","今日", res[2], res[3], df));
			ja.add(NewUtils.returnYwjiqkIntMsg("完工单量","今日", res[4], res[5], df));
			ja.add(NewUtils.returnYwjiqkDoubleMsg("委托金额", "今日",res[6], res[7], df));
            ja.add(NewUtils.returnYwjiqkDoubleMsg("预测收入","今日", res[8], res[9], df));
            ja.add(NewUtils.returnYwjiqkDoubleMsg("产品线毛利率", "", res[27], null, df));
            
            jObject = NewUtils.returnYwjiqkIntMsg("客户数量", "今日", res[10], res[11], df);
            jArray1.add(NewUtils.returnYwjiqkIntMsg1("产品线活跃客户", res[12], df));
            jArray1.add(NewUtils.returnYwjiqkIntMsg1("非产品线活跃客户", res[13], df));
            jArray1.add(NewUtils.returnYwjiqkIntMsg1("非活跃客户", res[14], df));
            jObject.put("detail",jArray1);
            taArray.add(jObject);
            
            jObject2 = NewUtils.returnYwjiqkIntMsg("供应商数量", "今日", res[15], res[16], df);
            jArray3.add(NewUtils.returnYwjiqkIntMsg1("产品线活跃供应商数量",res[18], df));
            jArray3.add(NewUtils.returnYwjiqkIntMsg1("非产品线活跃供应商数量",res[17], df));
            jArray3.add(NewUtils.returnYwjiqkIntMsg1("非活跃供应商数量",res[20], df));
            jObject2.put("detail",jArray3);
            taArray.add(jObject2);
            
            jObject3 = NewUtils.returnYwjiqkIntMsg("出证数量", "今日", res[21], res[26], df);
            jArray5.add(NewUtils.returnYwjiqkIntMsg1("证书数量",res[22], df));
            jArray5.add(NewUtils.returnYwjiqkIntMsg1("其他成果物数量",res[25], df));
            jObject3.put("detail",jArray5);
            taArray.add(jObject3);
//            jArray.add("产品线毛利率");
//            jArray.add(res[27]+"%");
//            ja.add(jArray);
//            ja.add(NewUtils.returnTodayTotalDoubleMsg1("客户数量", res[10], res[11], "产品线活跃客户数量", "非产品线活跃客户数量", "非活跃客户数量", res[12], res[13], res[14], df));
//            ja.add(NewUtils.returnTodayTotalDoubleMsg1("供应商数量", res[15], res[16], "产品线活跃供应商数量", "非产品线活跃供应商数量", "非活跃供应商数量", res[18], res[17], res[20], df));
//            ja.add(NewUtils.returnTodayTotalDoubleMsg1("出证数量", res[21], res[26], "证书数量", "其他成果物数量", null, res[22], res[25], null, df));
           
           map.put("ttuData",ja);
           map.put("ttdData", taArray);
           
		}
    	return map;
		
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyskByCompany(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyskByQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwsrByZj(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "SELECT SUM(EX_TAX_PRICE) 年业务收入," + 
    			"       SORG_LEVEL3 COMPANY_BUSI_ORG_NAME," + 
    			"       SORG_LEVEL3_ID COMPANY_BUSI_ORG_ID" + 
    			"  FROM T_COMP_INVOICE_ALL" + 
    			" WHERE VOID_TIME IS NULL" + 
    			"   AND SUBSTR(INVOICE_DATE, 1, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
    			"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
    			"   AND SORG_LEVEL2 = '在京单位'" + 
    			" GROUP BY SORG_LEVEL3, SORG_LEVEL3_ID" + 
    			" ORDER BY 1 DESC";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴单位名
			JSONArray yueX = new JSONArray();
			//业务收入
			JSONArray curYear = new JSONArray();
			//y轴
			JSONObject Y = new JSONObject();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double YMin = 0d, YMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[1]);
				curYear.add(ob[0]== null?0:ob[0]);
				if(ob[0] != null && ob [0] != "") { 
					if(YMin > Double.parseDouble(ob[0].toString())) {
						YMin = Double.parseDouble(ob[0].toString());
					}
					
					if(YMax < Double.parseDouble(ob[0].toString())) {
						YMax = Double.parseDouble(ob[0].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			Y.put("min",YMin);
			Y.put("max",YMax);
			yAxis.add(Y);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwcbByZj(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "SELECT SORG_LEVEL3," + 
    			"       SUM(payment_amount + i_payment_amount + apport_pay_amount + continu_payment_amount) AS 业务成本" + 
    			"  FROM T_COMPANY_COST_ALL" + 
    			" WHERE SUBSTR(pay_end_date, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
    			"   AND SORG_LEVEL2 = '在京单位'" + 
    			" GROUP BY SORG_LEVEL3" + 
    			" ORDER BY 2 DESC";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴单位名
			JSONArray yueX = new JSONArray();
			//业务收入
			JSONArray curYear = new JSONArray();
			//y轴
			JSONObject Y = new JSONObject();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double YMin = 0d, YMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				if(ob[1] != null && ob [1] != "") { 
					if(YMin > Double.parseDouble(ob[1].toString())) {
						YMin = Double.parseDouble(ob[1].toString());
					}
					
					if(YMax < Double.parseDouble(ob[1].toString())) {
						YMax = Double.parseDouble(ob[1].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			Y.put("min",YMin);
			Y.put("max",YMax);
			yAxis.add(Y);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwsrByGn(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "SELECT SUM(SYSTEM_EX_TAX_PRICE - VOID_SYSTEM_EX_TAX_PRICE +" + 
    			"           SYSTEM_EX_TAX_PRICE_YX + SYSTEM_EX_TAX_PRICE_FYW +" + 
    			"           SYSTEM_EX_TAX_PRICE_SY + SYSTEM_EX_TAX_PRICE_PSI +" + 
    			"           SYSTEM_EX_TAX_PRICE_CQC) 年业务收入," + 
    			"       SORG_LEVEL3 COMPANY_BUSI_ORG_NAME," + 
    			"       SORG_LEVEL3_ID COMPANY_BUSI_ORG_ID" + 
    			"  FROM T_COMP_INVOICE_ALL_MONTH" + 
    			" WHERE SUBSTR(INVOICE_DATE_MONTH, 1, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
    			"   AND INVOICE_DATE_MONTH <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
    			"   AND SORG_LEVEL2 = '地方公司'" + 
    			" GROUP BY SORG_LEVEL3, SORG_LEVEL3_ID" + 
    			" ORDER BY 1 desc";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴单位名
			JSONArray yueX = new JSONArray();
			//业务收入
			JSONArray curYear = new JSONArray();
			//y轴
			JSONObject Y = new JSONObject();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double YMin = 0d, YMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[1]);
				curYear.add(ob[0]== null?0:ob[0]);
				if(ob[0] != null && ob [0] != "") { 
					if(YMin > Double.parseDouble(ob[0].toString())) {
						YMin = Double.parseDouble(ob[0].toString());
					}
					
					if(YMax < Double.parseDouble(ob[0].toString())) {
						YMax = Double.parseDouble(ob[0].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			Y.put("min",YMin);
			Y.put("max",YMax);
			yAxis.add(Y);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwcbByGn(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "SELECT SORG_LEVEL3," + 
    			"       SUM(payment_amount + i_payment_amount + apport_pay_amount + continu_payment_amount) AS 业务成本" + 
    			"  FROM T_COMPANY_COST_ALL" + 
    			" WHERE SUBSTR(pay_end_date, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
    			"   AND SORG_LEVEL2 = '地方公司'" + 
    			" GROUP BY SORG_LEVEL3" + 
    			" ORDER BY 2 DESC";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴单位名
			JSONArray yueX = new JSONArray();
			//业务收入
			JSONArray curYear = new JSONArray();
			//y轴
			JSONObject Y = new JSONObject();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double YMin = 0d, YMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				if(ob[1] != null && ob [1] != "") { 
					if(YMin > Double.parseDouble(ob[1].toString())) {
						YMin = Double.parseDouble(ob[1].toString());
					}
					
					if(YMax < Double.parseDouble(ob[1].toString())) {
						YMax = Double.parseDouble(ob[1].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			Y.put("min",YMin);
			Y.put("max",YMax);
			yAxis.add(Y);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwsrByJw(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "SELECT SUM(SYSTEM_EX_TAX_PRICE - VOID_SYSTEM_EX_TAX_PRICE +" + 
    			"           SYSTEM_EX_TAX_PRICE_YX + SYSTEM_EX_TAX_PRICE_FYW +" + 
    			"           SYSTEM_EX_TAX_PRICE_SY + SYSTEM_EX_TAX_PRICE_PSI +" + 
    			"           SYSTEM_EX_TAX_PRICE_CQC) 年业务收入," + 
    			"       SORG_LEVEL3 COMPANY_BUSI_ORG_NAME," + 
    			"       SORG_LEVEL3_ID COMPANY_BUSI_ORG_ID" + 
    			"  FROM T_COMP_INVOICE_ALL_MONTH" + 
    			" WHERE SUBSTR(INVOICE_DATE_MONTH, 1, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
    			"   AND INVOICE_DATE_MONTH <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
    			"   AND SORG_LEVEL2 = '境外公司'" + 
    			" GROUP BY SORG_LEVEL3, SORG_LEVEL3_ID" + 
    			" ORDER BY 1 desc";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴单位名
			JSONArray yueX = new JSONArray();
			//业务收入
			JSONArray curYear = new JSONArray();
			//y轴
			JSONObject Y = new JSONObject();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double YMin = 0d, YMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[1]);
				curYear.add(ob[0]== null?0:ob[0]);
				if(ob[0] != null && ob [0] != "") { 
					if(YMin > Double.parseDouble(ob[0].toString())) {
						YMin = Double.parseDouble(ob[0].toString());
					}
					
					if(YMax < Double.parseDouble(ob[0].toString())) {
						YMax = Double.parseDouble(ob[0].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			Y.put("min",YMin);
			Y.put("max",YMax);
			yAxis.add(Y);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}
    @RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwcbByJw(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	String sql = "SELECT SORG_LEVEL3," + 
    			"       SUM(payment_amount + i_payment_amount + apport_pay_amount + continu_payment_amount) AS 业务成本" + 
    			"  FROM T_COMPANY_COST_ALL" + 
    			" WHERE SUBSTR(pay_end_date, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
    			"   AND SORG_LEVEL2 = '境外公司'" + 
    			" GROUP BY SORG_LEVEL3" + 
    			" ORDER BY 2 DESC";
    	System.out.println(sql);
    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
		Map<String, Object> map = new HashMap<String, Object>();
		if(reList !=null && reList.size()!= 0 ) {
			JSONArray saceDate = new JSONArray();
			//x轴单位名
			JSONArray yueX = new JSONArray();
			//业务收入
			JSONArray curYear = new JSONArray();
			//y轴
			JSONObject Y = new JSONObject();
			//y轴最大最小值
			JSONArray yAxis = new JSONArray();
			
			Double YMin = 0d, YMax = 0d;
			for (int i = 0; i < reList.size(); i++) {
				Object[] ob = reList.get(i);
				if(ob == null ||ob.length == 0) {
					continue;
				}
				yueX.add(ob[0]);
				curYear.add(ob[1]== null?0:ob[1]);
				if(ob[1] != null && ob [1] != "") { 
					if(YMin > Double.parseDouble(ob[1].toString())) {
						YMin = Double.parseDouble(ob[1].toString());
					}
					
					if(YMax < Double.parseDouble(ob[1].toString())) {
						YMax = Double.parseDouble(ob[1].toString());
					}
				}
				
			}
			saceDate.add(curYear);
			Y.put("min",YMin);
			Y.put("max",YMax);
			yAxis.add(Y);
			map.put("seriesData",saceDate);
			map.put("xAxisData",yueX);
			map.put("yAxis",yAxis);
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByCompany(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from (  " + 
				"select tcaam.sorg_level2 a,'父节点' as b, " + 
				"       tcaam.委托单量 c, " + 
				"       tos.年预测收入 d, " + 
				"       tcia.收入总额 e, " + 
				"       tcca.业务成本 f " + 
				"  from ( " + 
				"    select sorg_level2,sorg_level2_id, sum(ORDER_CREATE_COUNT) 委托单量 " + 
				"    from T_COMP_AMOUNT_ALL_MONTH " + 
				"    where SORG_LEVEL2 not like '%参股%' " + 
				"    group by sorg_level2, sorg_level2_id    " + 
				"  ) tcaam " + 
				"  inner join ( " + 
				"    SELECT SORG_LEVEL2, " + 
				"       NVL(SUM(SYS_CUR_TOTAL_AMOUNT), 0) 年预测收入 " + 
				"    FROM T_ORDER_SERVER " + 
				"    WHERE DATA_STATE_ORDER <> '已删除' " + 
				"      AND DATA_STATE_ORDER <> '草稿' " + 
				"      AND AUDIT_STATE_ORDER <> '草稿' " + 
				"      AND SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"    GROUP BY SORG_LEVEL2     " + 
				"  ) tos on tos.SORG_LEVEL2 = tcaam.sorg_level2 " + 
				"  inner join ( " + 
				"    select SORG_LEVEL2,SORG_LEVEL2_ID,sum(EX_TAX_PRICE) 收入总额    " + 
				"    from T_COMP_INVOICE_ALL " + 
				"    where VOID_TIME is null  " + 
				"    and SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"    and SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"    and SORG_LEVEL2 not like '%参股%' " + 
				"    group by SORG_LEVEL2,SORG_LEVEL2_ID " + 
				"  ) tcia on tcia.SORG_LEVEL2_ID = tcaam.sorg_level2_id " + 
				"  inner join ( " + 
				"    select SORG_LEVEL2,SORG_LEVEL2_ID,SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本  " + 
				"    from T_COMPANY_COST_ALL  " + 
				"    where SORG_LEVEL2 not like '%参股%' " + 
				"    group by SORG_LEVEL2,SORG_LEVEL2_ID " + 
				"  ) tcca on tcca.sorg_level2_id = tcaam.sorg_level2_id   " + 
				" union " + 
				" select  " + 
				"  tcaam.SORG_LEVEL2 a,tcaam.SORG_LEVEL3 as b, " + 
				"  tcaam.委托单量 c, " + 
				"  tos.年预测收入 d, " + 
				"  tcia.收入总额 e, " + 
				"  tcca.业务成本 f " + 
				" from ( " + 
				"   select SORG_LEVEL2,SORG_LEVEL3,SORG_LEVEL3_ID, sum(ORDER_CREATE_COUNT) 委托单量     " + 
				"   from T_COMP_AMOUNT_ALL_MONTH  " + 
				"   where SORG_LEVEL2 not like '%参股%'    " + 
				"   group by SORG_LEVEL2,SORG_LEVEL3,SORG_LEVEL3_ID " + 
				" ) tcaam  " + 
				" inner join ( " + 
				"  SELECT SORG_LEVEL3, " + 
				"       NVL(SUM(SYS_CUR_TOTAL_AMOUNT), 0) 年预测收入 " + 
				"  FROM T_ORDER_SERVER " + 
				"  WHERE DATA_STATE_ORDER <> '已删除' " + 
				"   AND DATA_STATE_ORDER <> '草稿' " + 
				"   AND AUDIT_STATE_ORDER <> '草稿' " + 
				"   AND SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"  GROUP BY SORG_LEVEL3      " + 
				" ) tos on tos.SORG_LEVEL3 = tcaam.SORG_LEVEL3 " + 
				" inner join ( " + 
				"  SELECT SORG_LEVEL3_ID,SUM(EX_TAX_PRICE) 收入总额 " + 
				"  FROM T_COMP_INVOICE_ALL " + 
				"  WHERE VOID_TIME IS NULL " + 
				"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"   GROUP BY SORG_LEVEL3,SORG_LEVEL3_ID      " + 
				" ) tcia on tcia.SORG_LEVEL3_ID = tcaam.SORG_LEVEL3_ID " + 
				" inner join ( " + 
				"  SELECT SORG_LEVEL3_ID, SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本 " + 
				"  FROM T_COMPANY_COST_ALL " + 
				"  GROUP BY SORG_LEVEL3, SORG_LEVEL3_ID      " + 
				" ) tcca on tcca.SORG_LEVEL3_ID = tcaam.SORG_LEVEL3_ID    " + 
				" ) aa  " + 
				" order by aa.a asc, aa.d desc, aa.c desc");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			// 计算合计
			JSONObject hj = NewUtils.returnHeji(resultList, "父节点");
			// 进行json格式化
			JSONArray ja = NewUtils.returnCompanyMsg(resultList, "父节点");
			// 按照预测收入进行排序
			JSONArray res = NewUtils.paixuForJsonArray(hj, ja);			
			
			map.put("tableData", res);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByCompanyYh(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select SORG_LEVEL2, " + 
				"       SORG_LEVEL3, " + 
				"       nvl(sum(委托单量), 0) 委托单量, " + 
				"       nvl(sum(年预测收入), 0) 年预测收入, " + 
				"       nvl(sum(收入总额), 0) 收入总额, " + 
				"       nvl(sum(业务成本), 0) 业务成本, " + 
				"       sorg_level3_id " + 
				"  from (select SORG_LEVEL2, " + 
				"               sorg_level3_id, " + 
				"               SORG_LEVEL3, " + 
				"               sum(ORDER_CREATE_COUNT) 委托单量, " + 
				"               sum(server_end_amount) 年预测收入, " + 
				"               0 as 收入总额, " + 
				"               0 as 业务成本 " + 
				"          from T_COMP_AMOUNT_ALL_MONTH " + 
				"         where SORG_LEVEL2 not like '%参股%' " + 
				"           and substr(data_stats_month, 0, 4) = to_char(sysdate, 'YYYY') " + 
				"         group by SORG_LEVEL2, sorg_level3_id,SORG_LEVEL3 " + 
				"        union all " + 
				"        SELECT SORG_LEVEL2, " + 
				"               sorg_level3_id, " + 
				"               SORG_LEVEL3, " + 
				"               0 委托单量, " + 
				"               0 年预测收入, " + 
				"               SUM(invoice_all_ex_tax_price) 收入总额, " + 
				"               0 业务成本 " + 
				"          FROM T_COMP_INVOICE_ALL_MONTH " + 
				"         WHERE SUBSTR(invoice_date_month, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           AND SUBSTR(invoice_date_month, 0, 7) <= " + 
				"               TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"           AND SORG_LEVEL2 not like '%参股%' " + 
				"         GROUP BY SORG_LEVEL2,sorg_level3_id, SORG_LEVEL3 " + 
				"        union all " + 
				"        SELECT SORG_LEVEL2, " + 
				"               sorg_level3_id, " + 
				"               SORG_LEVEL3, " + 
				"               0 委托单量, " + 
				"               0 年预测收入, " + 
				"               0 as 收入总额, " + 
				"               SUM(total_cost) 业务成本 " + 
				"          FROM T_COMPANY_COST_ALL " + 
				"         where SORG_LEVEL2 not like '%参股%' " + 
				"         GROUP BY SORG_LEVEL2,sorg_level3_id, SORG_LEVEL3) " + 
				" group by SORG_LEVEL2, sorg_level3_id, SORG_LEVEL3 " + 
				" order by 年预测收入 desc");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			// 进行json格式化
			JSONArray res = NewUtils.newReturnCompanyMsg(resultList);			
			
			map.put("tableData", res);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByArea(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from ( " + 
				"select tcaam.BUSINESS_REGION a,'父节点' as b, " + 
				"       tcaam.委托单量 c, " + 
				"       tcaam.预测收入 d, " + 
				"       tcia.收入总额 e, " + 
				"       tcca.业务成本 f " + 
				"from (        " + 
				"select BUSINESS_REGION, " + 
				"       sum(ORDER_CREATE_COUNT) 委托单量, " + 
				"       sum(SERVER_END_AMOUNT) 预测收入 " + 
				"  from T_COMP_AMOUNT_ALL_MONTH " + 
				"  where BUSINESS_REGION is not null " + 
				" group by BUSINESS_REGION " + 
				") tcaam  " + 
				"inner join ( " + 
				"  SELECT SUM(EX_TAX_PRICE) 收入总额,BUSINESS_REGION " + 
				"  FROM T_COMP_INVOICE_ALL " + 
				" WHERE VOID_TIME IS NULL " + 
				"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"   AND BUSINESS_REGION IS NOT NULL " + 
				"   GROUP BY BUSINESS_REGION         " + 
				") tcia on tcia.BUSINESS_REGION = tcaam.BUSINESS_REGION " + 
				"inner join ( " + 
				"  SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本, BUSINESS_REGION " + 
				"  FROM T_COMPANY_COST_ALL " + 
				"  GROUP BY BUSINESS_REGION " + 
				") tcca on tcca.BUSINESS_REGION = tcaam.BUSINESS_REGION " + 
				"union " + 
				"select  " + 
				"  tcaam.BUSINESS_REGION a,tcaam.SORG_LEVEL3 as b, " + 
				"  tcaam.委托单量 c, " + 
				"  tcaam.预测收入 d, " + 
				"  tcia.收入总额 e, " + 
				"  tcca.业务成本 f " + 
				"from ( " + 
				"  select BUSINESS_REGION,SORG_LEVEL3, " + 
				"       sum(ORDER_CREATE_COUNT) 委托单量, " + 
				"       sum(SERVER_END_AMOUNT) 预测收入 " + 
				"  from T_COMP_AMOUNT_ALL_MONTH " + 
				"  where BUSINESS_REGION is not null " + 
				" group by BUSINESS_REGION,SORG_LEVEL3 " + 
				") tcaam " + 
				"inner join ( " + 
				"  SELECT SUM(EX_TAX_PRICE) 收入总额,BUSINESS_REGION,SORG_LEVEL3 " + 
				"  FROM T_COMP_INVOICE_ALL " + 
				" WHERE VOID_TIME IS NULL " + 
				"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"   AND BUSINESS_REGION IS NOT NULL " + 
				"   GROUP BY BUSINESS_REGION,SORG_LEVEL3 " + 
				") tcia on tcia.SORG_LEVEL3 = tcaam.SORG_LEVEL3 " + 
				"inner join ( " + 
				"  SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本, SORG_LEVEL3, BUSINESS_REGION " + 
				"  FROM T_COMPANY_COST_ALL " + 
				"  where BUSINESS_REGION IS NOT NULL " + 
				"  GROUP BY BUSINESS_REGION, SORG_LEVEL3 " + 
				") tcca on tcca.SORG_LEVEL3 = tcaam.SORG_LEVEL3 " + 
				") aa  " + 
				" order by aa.a asc, aa.d desc,aa.b asc");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			// 计算合计
			JSONObject hj = NewUtils.returnHeji(resultList, "父节点");
			// 进行json格式化
			JSONArray ja = NewUtils.returnCompanyMsg(resultList, "父节点");
			// 按照预测收入进行排序
			JSONArray res = NewUtils.paixuForJsonArray(hj, ja);			
			
			map.put("tableData", res);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByAreaYh(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT BUSINESS_REGION, " + 
				"       SORG_LEVEL3, " + 
				"       SUM(委托单量) 委托单量, " + 
				"       SUM(预测收入) 预测收入, " + 
				"       SUM(年业务收入) 年业务收入, " + 
				"       SUM(业务成本) 业务成本, " + 
				"       sorg_level3_id " + 
				"  FROM (SELECT BUSINESS_REGION, " + 
				"               sorg_level3_id, " + 
				"               SORG_LEVEL3, " + 
				"               COUNT(DISTINCT(ORDER_UUID)) 委托单量, " + 
				"               0 AS 年业务收入, " + 
				"               0 AS 预测收入, " + 
				"               0 AS 业务成本 " + 
				"          FROM T_ORDER_SERVER " + 
				"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           AND BUSINESS_REGION IS NOT NULL " + 
				"         GROUP BY BUSINESS_REGION, sorg_level3_id, SORG_LEVEL3 " + 
				"        UNION ALL " + 
				"        SELECT BUSINESS_REGION, " + 
				"               sorg_level3_id, " + 
				"               SORG_LEVEL3, " + 
				"               0 AS 委托单量, " + 
				"               SUM(EX_TAX_PRICE) 年业务收入, " + 
				"               0 AS 预测收入, " + 
				"               0 AS 业务成本 " + 
				"          FROM T_COMP_INVOICE_ALL " + 
				"         WHERE VOID_TIME IS NULL " + 
				"           AND BUSINESS_REGION IS NOT NULL " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"         GROUP BY BUSINESS_REGION, sorg_level3_id, SORG_LEVEL3 " + 
				"        UNION ALL " + 
				"        SELECT BUSINESS_REGION, " + 
				"               sorg_level3_id, " + 
				"               SORG_LEVEL3, " + 
				"               0 AS 委托单量, " + 
				"               0 AS 年业务收入, " + 
				"               SUM(SYS_CUR_TOTAL_AMOUNT) 预测收入, " + 
				"               0 AS 业务成本 " + 
				"          FROM T_ORDER_SERVER " + 
				"         WHERE SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           AND BUSINESS_REGION IS NOT NULL " + 
				"         GROUP BY BUSINESS_REGION, sorg_level3_id, SORG_LEVEL3 " + 
				"        UNION ALL " + 
				"        SELECT BUSINESS_REGION, " + 
				"               sorg_level3_id, " + 
				"               SORG_LEVEL3, " + 
				"               0 AS 委托单量, " + 
				"               0 AS 年业务收入, " + 
				"               0 AS 预测收入, " + 
				"               SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本 " + 
				"          FROM T_COMPANY_COST_ALL " + 
				"         WHERE BUSINESS_REGION IS NOT NULL " + 
				"         GROUP BY BUSINESS_REGION,sorg_level3_id, SORG_LEVEL3) " + 
				" GROUP BY BUSINESS_REGION, sorg_level3_id, SORG_LEVEL3 " + 
				" order by 预测收入 desc");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			// 进行json格式化
			JSONArray ja = NewUtils.newReturnCompanyMsg(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByCpx(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT PRODUCT_LINE_NAME_ST1, " + 
				"       PRODUCT_LINE_NAME, " + 
				"       sum(年委托单) 委托单量, " + 
				"       SUM(预测收入) 预测收入, " + 
				"       SUM(年业务收入) 年业务收入, " + 
				"       SUM(业务成本) 业务成本 " + 
				"  FROM (select PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               count(distinct(order_uuid)) 年委托单, " + 
				"               0 as 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               0 as 业务成本 " + 
				"          from T_ORDER_SERVER " + 
				"         where substr(create_time_order, 0, 4) = to_char(sysdate, 'yyyy') " + 
				"           and data_state_order <> '草稿' " + 
				"           and data_state_order <> '草稿' " + 
				"           and audit_state_order <> '已删除' " + 
				"         group by PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               SUM(EX_TAX_PRICE) 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               0 as 业务成本 " + 
				"          FROM T_COMP_INVOICE_ALL " + 
				"         WHERE VOID_TIME IS NULL " + 
				"           AND TYPE = '产品线业务' " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               0 as 年业务收入, " + 
				"               SUM(SYS_CUR_TOTAL_AMOUNT) 预测收入, " + 
				"               0 as 业务成本 " + 
				"          FROM T_ORDER_SERVER " + 
				"         WHERE SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME  " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               product_name PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               0 as 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               SUM(total_cost) AS 业务成本 " + 
				"          FROM T_COMPANY_COST_ALL " + 
				"         WHERE PRODUCT_NAME IS NOT NULL " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, product_name) " + 
				" GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				" order by 预测收入 desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			// 进行json格式化
			JSONArray ja = NewUtils.returnCompanyMsgByCpx(resultList, "父节点");
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCompanyMsgByCpxName(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		// 获取当前产品线名称
		Map<String, String> resMap = fetchWebReq.getData();
		String cpxName = resMap.get("cpxName");
		
		sql.append("select SORG_LEVEL3, " + 
				"       nvl(sum(委托单量), 0) 委托单量, " + 
				"       nvl(sum(年预测收入), 0) 年预测收入, " + 
				"       nvl(sum(收入总额), 0) 收入总额, " + 
				"       nvl(sum(业务成本), 0) 业务成本, " + 
				"       company_busi_org_id " + 
				"from (select company_busi_org_id, " + 
				"             SORG_LEVEL2, " + 
				"             SORG_LEVEL3, " + 
				"             sum(ORDER_CREATE_COUNT) 委托单量, " + 
				"             sum(server_end_amount) 年预测收入, " + 
				"             0 as 收入总额, " + 
				"             0 as 业务成本 " + 
				"      from T_COMP_AMOUNT_ALL_MONTH " + 
				"      where SORG_LEVEL2 not like '%参股%' " + 
				"      and product_line_name = '"+cpxName+"' " + 
				"      and substr(data_stats_month, 0, 4) = to_char(sysdate, 'YYYY') " + 
				"      group by company_busi_org_id, SORG_LEVEL2, SORG_LEVEL3 " + 
				"      union all " + 
				"      SELECT company_busi_org_id, " + 
				"             SORG_LEVEL2, " + 
				"             SORG_LEVEL3, " + 
				"             0 委托单量, " + 
				"             0 年预测收入, " + 
				"             SUM(invoice_all_ex_tax_price) 收入总额, " + 
				"             0 业务成本 " + 
				"      FROM T_COMP_INVOICE_ALL_MONTH " + 
				"      WHERE SUBSTR(invoice_date_month, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"      and product_line_name = '"+cpxName+"' " + 
				"      AND SUBSTR(invoice_date_month, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"      GROUP BY company_busi_org_id, SORG_LEVEL2, SORG_LEVEL3 " + 
				"      union all " + 
				"      SELECT company_busi_org_id, " + 
				"             SORG_LEVEL2, " + 
				"             SORG_LEVEL3, " + 
				"             0 委托单量, " + 
				"             0 年预测收入, " + 
				"             0 as 收入总额, " + 
				"             SUM(total_cost) 业务成本 " + 
				"      FROM T_COMPANY_COST_ALL " + 
				"      where product_name = '"+cpxName+"' " + 
				"      GROUP BY company_busi_org_id, SORG_LEVEL2, SORG_LEVEL3) " + 
				"group by company_busi_org_id, SORG_LEVEL3 " + 
				"order by 年预测收入 desc " + 
				"");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
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
		String cpxName = resMap.get("cpxName");
		
		sql.append("select * " + 
				"from (select 服务技术大类 as a, " + 
				"             '父节点' as b, " + 
				"             sum(委托金额) as c, " + 
				"             sum(开票金额) as d, " + 
				"             sum(成本总额) as e " + 
				"      from (SELECT SERVICE_MAINCLASS_NAME 服务技术大类, " + 
				"                   SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
				"                   SUM(SYS_CUR_TOTAL_AMOUNT) 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_ORDER_SERVER " + 
				"            WHERE SERVICE_MEDIUMCLASS_NAME IS NOT NULL " + 
				"            AND DATA_STATE_ORDER <> '已删除' " + 
				"            AND DATA_STATE_ORDER <> '草稿' " + 
				"            AND AUDIT_STATE_ORDER <> '草稿' " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            AND SUBSTR(CREATE_TIME_ORDER, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            GROUP BY SERVICE_MEDIUMCLASS_NAME, SERVICE_MAINCLASS_NAME " + 
				"            union all " + 
				"            SELECT SERVICE_MAINCLASS_NAME 服务技术大类, " + 
				"                   SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
				"                   0 as 委托金额, " + 
				"                   SUM(EX_TAX_PRICE) 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SERVICE_MAINCLASS_NAME IS NOT NULL " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            GROUP BY SERVICE_MEDIUMCLASS_NAME, SERVICE_MAINCLASS_NAME " + 
				"            union all " + 
				"            SELECT SERVICE_MAINCLASS_NAME 服务技术大类, " + 
				"                   SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
				"                   0 as 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   nvl(SUM(total_cost), 0) 成本总额 " + 
				"            FROM T_COMPANY_COST_ALL " + 
				"            WHERE SERVICE_MAINCLASS_NAME IS NOT NULL " + 
				"            AND PRODUCT_NAME = '"+cpxName+"' " + 
				"            GROUP BY SERVICE_MAINCLASS_NAME, SERVICE_MEDIUMCLASS_NAME) " + 
				"      group by 服务技术大类 " + 
				"      union " + 
				"      select 服务技术大类 as a, " + 
				"             服务技术中类 as b, " + 
				"             sum(委托金额) as c, " + 
				"             sum(开票金额) as d, " + 
				"             sum(成本总额) as e " + 
				"      from (SELECT SERVICE_MAINCLASS_NAME 服务技术大类, " + 
				"                   SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
				"                   SUM(SYS_CUR_TOTAL_AMOUNT) 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_ORDER_SERVER " + 
				"            WHERE SERVICE_MEDIUMCLASS_NAME IS NOT NULL " + 
				"            AND DATA_STATE_ORDER <> '已删除' " + 
				"            AND DATA_STATE_ORDER <> '草稿' " + 
				"            AND AUDIT_STATE_ORDER <> '草稿' " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            AND SUBSTR(CREATE_TIME_ORDER, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            GROUP BY SERVICE_MEDIUMCLASS_NAME, SERVICE_MAINCLASS_NAME " + 
				"            union all " + 
				"            SELECT SERVICE_MAINCLASS_NAME 服务技术大类, " + 
				"                   SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
				"                   0 as 委托金额, " + 
				"                   SUM(EX_TAX_PRICE) 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SERVICE_MAINCLASS_NAME IS NOT NULL " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            GROUP BY SERVICE_MEDIUMCLASS_NAME, SERVICE_MAINCLASS_NAME " + 
				"            union all " + 
				"            SELECT SERVICE_MAINCLASS_NAME 服务技术大类, " + 
				"                   SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
				"                   0 as 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   nvl(SUM(total_cost), 0) 成本总额 " + 
				"            FROM T_COMPANY_COST_ALL " + 
				"            WHERE SERVICE_MAINCLASS_NAME IS NOT NULL " + 
				"            AND PRODUCT_NAME = '"+cpxName+"' " + 
				"            GROUP BY SERVICE_MAINCLASS_NAME, SERVICE_MEDIUMCLASS_NAME) " + 
				"      group by 服务技术大类, 服务技术中类) a " + 
				"order by a.a desc，a.c desc, a.b desc " + 
				"");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgForCpxJs(resultList, "父节点");
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCpxMsgByDxfl(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		// 获取当前查询jsid
		Map<String, String> resMap = fetchWebReq.getData();
		String cpxName = resMap.get("cpxName");
		
		sql.append("select * " + 
				"from (select 服务对象大类 as a, " + 
				"             '父节点' as b， sum(委托金额) as c, " + 
				"             sum(开票金额) as d, " + 
				"             sum(成本总额) as e " + 
				"      from (SELECT GOODS_MAINCLASS_NAME 服务对象大类, " + 
				"                   GOODS_MEDIUMCLASS_NAME 服务对象中类, " + 
				"                   SUM(SYS_CUR_TOTAL_AMOUNT) 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_ORDER_SERVER " + 
				"            WHERE GOODS_MAINCLASS_NAME IS NOT NULL " + 
				"            AND DATA_STATE_ORDER <> '已删除' " + 
				"            AND DATA_STATE_ORDER <> '草稿' " + 
				"            AND AUDIT_STATE_ORDER <> '草稿' " + 
				"            AND SUBSTR(CREATE_TIME_ORDER, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            GROUP BY GOODS_MAINCLASS_NAME, GOODS_MEDIUMCLASS_NAME " + 
				"            union all " + 
				"            SELECT GOODS_MAINCLASS_NAME 服务对象, " + 
				"                   GOODS_MEDIUMCLASS_NAME 服务对象中类, " + 
				"                   0 as 委托金额, " + 
				"                   SUM(EX_TAX_PRICE) 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND GOODS_MAINCLASS_NAME IS NOT NULL " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            GROUP BY GOODS_MAINCLASS_NAME, GOODS_MEDIUMCLASS_NAME " + 
				"            union all " + 
				"            SELECT GOODS_MAINCLASS_NAME 服务对象大类, " + 
				"                   GOODS_MEDIUMCLASS_NAME 服务对象中类, " + 
				"                   0 as 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   SUM(TOTAL_COST) 成本总额 " + 
				"            FROM T_COMPANY_COST_ALL " + 
				"            WHERE GOODS_MAINCLASS_NAME IS NOT NULL " + 
				"            and product_name = '"+cpxName+"' " + 
				"            AND PAY_END_DATE IS NOT NULL " + 
				"            GROUP BY GOODS_MAINCLASS_NAME, GOODS_MEDIUMCLASS_NAME) " + 
				"      group by 服务对象大类 " + 
				"      union " + 
				"      select 服务对象大类 as a, " + 
				"             服务对象中类 as b， sum(委托金额) as c, " + 
				"             sum(开票金额) as d, " + 
				"             sum(成本总额) as e " + 
				"      from (SELECT GOODS_MAINCLASS_NAME 服务对象大类, " + 
				"                   GOODS_MEDIUMCLASS_NAME 服务对象中类, " + 
				"                   SUM(SYS_CUR_TOTAL_AMOUNT) 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_ORDER_SERVER " + 
				"            WHERE GOODS_MAINCLASS_NAME IS NOT NULL " + 
				"            AND DATA_STATE_ORDER <> '已删除' " + 
				"            AND DATA_STATE_ORDER <> '草稿' " + 
				"            AND AUDIT_STATE_ORDER <> '草稿' " + 
				"            AND SUBSTR(CREATE_TIME_ORDER, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            GROUP BY GOODS_MAINCLASS_NAME, GOODS_MEDIUMCLASS_NAME " + 
				"            union all " + 
				"            SELECT GOODS_MAINCLASS_NAME 服务对象, " + 
				"                   GOODS_MEDIUMCLASS_NAME 服务对象中类, " + 
				"                   0 as 委托金额, " + 
				"                   SUM(EX_TAX_PRICE) 开票金额, " + 
				"                   0 as 成本总额 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND GOODS_MAINCLASS_NAME IS NOT NULL " + 
				"            AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"            GROUP BY GOODS_MAINCLASS_NAME, GOODS_MEDIUMCLASS_NAME " + 
				"            union all " + 
				"            SELECT GOODS_MAINCLASS_NAME 服务对象大类, " + 
				"                   GOODS_MEDIUMCLASS_NAME 服务对象中类, " + 
				"                   0 as 委托金额, " + 
				"                   0 as 开票金额, " + 
				"                   SUM(TOTAL_COST) 成本总额 " + 
				"            FROM T_COMPANY_COST_ALL " + 
				"            WHERE GOODS_MAINCLASS_NAME IS NOT NULL " + 
				"            and product_name = '"+cpxName+"' " + 
				"            AND PAY_END_DATE IS NOT NULL " + 
				"            GROUP BY GOODS_MAINCLASS_NAME, GOODS_MEDIUMCLASS_NAME) " + 
				"      group by 服务对象大类，服务对象中类) a " + 
				"order by a.a desc，a.c desc, a.b desc " + 
				"");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgForCpxJs(resultList, "父节点");
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findCpxTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Map<String, String> resMap = fetchWebReq.getData();
		String cpxName = resMap.get("cpxName");
		String type = resMap.get("type");
		
		sql.append(" select * " + 
				"from (select aa.*, rownum as rm " + 
				"      from ( " + 
				"           select 客户名, " + 
				"                   sum(委托单数量) 委托单量, " + 
				"                   sum(证书) 出证数量, " + 
				"                   sum(委托金额) 委托金额, " + 
				"                   sum(实收) 已收金额, " + 
				"                   内外部 " + 
				"            from (SELECT CUSTOMER_NAME 客户名, " + 
				"                         GROUP_TYPE 内外部, " + 
				"                         COUNT(DISTINCT(ORDER_UUID)) 委托单数量, " + 
				"                         nvl(sum(SYS_CUR_TOTAL_AMOUNT), 0) 委托金额, " + 
				"                         0 as 实收, " + 
				"                         0 as 证书 " + 
				"                  FROM T_ORDER_SERVER " + 
				"                  WHERE PRODUCT_LINE_NAME_ST1 IS NOT NULL " + 
				"                  AND DATA_STATE_ORDER <> '已删除' " + 
				"                  AND DATA_STATE_ORDER <> '草稿' " + 
				"                  AND AUDIT_STATE_ORDER <> '草稿' " + 
				"                  AND PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"                  AND SUBSTR(CREATE_TIME_ORDER, 0, 4) = " + 
				"                        TO_CHAR(SYSDATE, 'yyyy') " + 
				"                  GROUP BY CUSTOMER_NAME, GROUP_TYPE " + 
				"                  union all " + 
				"                  SELECT CUSTOMER_NAME 客户名, " + 
				"                         GROUP_TYPE 内外部, " + 
				"                         0 as 委托单数量, " + 
				"                         0 as 委托金额, " + 
				"                         SUM(SYSTEM_TOTAL_AMOUNT) 实收, " + 
				"                         0 as 证书 " + 
				"                  FROM T_SERVER_INCOME " + 
				"                  WHERE product_line_name = '"+cpxName+"' " + 
				"                  AND SUBSTR(match_date, 0, 4) = TO_CHAR(SYSDATE, 'yyyy') " + 
				"                  GROUP BY CUSTOMER_NAME, group_type " + 
				"                  union all " + 
				"                  SELECT CUSTOMER_NAME 客户名, " + 
				"                         group_type 内外部, " + 
				"                         0 as 委托单数量, " + 
				"                         0 as 委托金额, " + 
				"                         0 as 实收, " + 
				"                         COUNT(MID) 证书 " + 
				"                  FROM T_ORDER_CERC " + 
				"                  WHERE PRODUCT_LINE_NAME = '"+cpxName+"' " + 
				"                  AND SUBSTR(FICTIONAL_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy') " + 
				"                  GROUP BY CUSTOMER_NAME, group_type) " + 
				"            group by 客户名, 内外部 ) aa " + 
				"where rownum < 100 " + 
				"      and aa.内外部 = '"+type+"' " + 
				"      order by aa.委托金额 desc) bb " + 
				"where rm > 0 " + 
				"");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsTableKh(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Map<String, String> resMap = fetchWebReq.getData();
		String gsName = resMap.get("gsName");
		String type = resMap.get("type");
		
		sql.append(" select * from ( ")
		.append("       select aa.*, rownum as rm from ( ")
		.append("             select customer_name, count(order_uuid) 委托单数, sum(order_cerc) 出证数量, sum(sys_total_amount) 委托金额, sum(system_total_amount_income) 已收金额 ")
		.append("             from T_ORDER_CRM ")
		.append("             where group_type='").append(type).append("' ")
		.append("             and company_busi_org_name='").append(gsName).append("' ")
		.append("             group by customer_name order by 委托金额 desc  ")
		.append("       ) aa where rownum < 100")
		.append(" )bb where rm > 0");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsTableKhQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Map<String, String> resMap = fetchWebReq.getData();
		String gsName = resMap.get("gsName");
		String type = resMap.get("type");
		
		sql.append(" select * from ( ")
		.append("       select aa.*, rownum as rm from ( ")
		.append("             select customer_name, count(order_uuid) 委托单数, sum(order_cerc) 出证数量, sum(sys_total_amount) 委托金额, sum(system_total_amount_income) 已收金额 ")
		.append("             from T_ORDER_CRM ")
		.append("             where group_type='").append(type).append("' ")
		.append("             and BUSINESS_REGION='").append(gsName).append("' ")
		.append("             group by customer_name order by 委托金额 desc  ")
		.append("       ) aa where rownum < 100")
		.append(" )bb where rm > 0");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsTableGs(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Map<String, String> resMap = fetchWebReq.getData();
		String gsName = resMap.get("gsName");
		
		sql.append("  select company_busi_org_name, " + 
				"        nvl(sum(委托单量), 0) 委托单量, " + 
				"        nvl(sum(年预测收入), 0) 年预测收入, " + 
				"        nvl(sum(收入总额), 0) 收入总额, " + 
				"        nvl(sum(业务成本), 0) 业务成本, " + 
				"        company_busi_org_id " + 
				" from (select company_busi_org_id, " + 
				"              SORG_LEVEL2, " + 
				"              SORG_LEVEL3, " + 
				"              company_busi_org_name, " + 
				"              sum(ORDER_CREATE_COUNT) 委托单量, " + 
				"              sum(server_end_amount) 年预测收入, " + 
				"              0 as 收入总额, " + 
				"              0 as 业务成本 " + 
				"       from T_COMP_AMOUNT_ALL_MONTH " + 
				"       where SORG_LEVEL2 not like '%参股%' " + 
				"       and SORG_LEVEL3 = '"+gsName+"' " + 
				"       and substr(data_stats_month, 0, 4) = to_char(sysdate, 'YYYY') " + 
				"       group by company_busi_org_id, " + 
				"                SORG_LEVEL2, " + 
				"                SORG_LEVEL3, " + 
				"                company_busi_org_name " + 
				"       union all " + 
				"       SELECT company_busi_org_id, " + 
				"              SORG_LEVEL2, " + 
				"              SORG_LEVEL3, " + 
				"              company_busi_org_name, " + 
				"              0 委托单量, " + 
				"              0 年预测收入, " + 
				"              SUM(invoice_all_ex_tax_price) 收入总额, " + 
				"              0 业务成本 " + 
				"       FROM T_COMP_INVOICE_ALL_MONTH " + 
				"       WHERE SUBSTR(invoice_date_month, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"       and SORG_LEVEL3 = '"+gsName+"' " + 
				"       AND SUBSTR(invoice_date_month, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"       GROUP BY company_busi_org_id, " + 
				"                SORG_LEVEL2, " + 
				"                SORG_LEVEL3, " + 
				"                company_busi_org_name " + 
				"       union all " + 
				"       SELECT company_busi_org_id, " + 
				"              SORG_LEVEL2, " + 
				"              SORG_LEVEL3, " + 
				"              company_busi_org_name, " + 
				"              0 委托单量, " + 
				"              0 年预测收入, " + 
				"              0 as 收入总额, " + 
				"              SUM(total_cost) 业务成本 " + 
				"       FROM T_COMPANY_COST_ALL " + 
				"       where SORG_LEVEL3 = '"+gsName+"' " + 
				"       GROUP BY company_busi_org_id, " + 
				"                SORG_LEVEL2, " + 
				"                SORG_LEVEL3, " + 
				"                company_busi_org_name) " + 
				" group by company_busi_org_id, company_busi_org_name " + 
				" order by 年预测收入 desc " + 
				" ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsTableGsQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Map<String, String> resMap = fetchWebReq.getData();
		String gsName = resMap.get("gsName");
		
		sql.append(" select company_busi_org_name, " + 
				"       nvl(sum(委托单量), 0) 委托单量, " + 
				"       nvl(sum(年预测收入), 0) 年预测收入, " + 
				"       nvl(sum(收入总额), 0) 收入总额, " + 
				"       nvl(sum(业务成本), 0) 业务成本, " + 
				"       company_busi_org_id " + 
				"  from (select SORG_LEVEL2, " + 
				"               SORG_LEVEL3, " + 
				"               company_busi_org_id, " + 
				"               company_busi_org_name, " + 
				"               sum(ORDER_CREATE_COUNT) 委托单量, " + 
				"               sum(server_end_amount) 年预测收入, " + 
				"               0 as 收入总额, " + 
				"               0 as 业务成本 " + 
				"          from T_COMP_AMOUNT_ALL_MONTH " + 
				"         where SORG_LEVEL2 not like '%参股%' " + 
				"           and BUSINESS_REGION = '"+gsName+"' " + 
				"           and substr(data_stats_month, 0, 4) = to_char(sysdate, 'YYYY') " + 
				"         group by SORG_LEVEL2, " + 
				"                  SORG_LEVEL3, " + 
				"                  company_busi_org_id, " + 
				"                  company_busi_org_name " + 
				"        union all " + 
				"        SELECT SORG_LEVEL2, " + 
				"               SORG_LEVEL3, " + 
				"               company_busi_org_id, " + 
				"               company_busi_org_name, " + 
				"               0 委托单量, " + 
				"               0 年预测收入, " + 
				"               SUM(invoice_all_ex_tax_price) 收入总额, " + 
				"               0 业务成本 " + 
				"          FROM T_COMP_INVOICE_ALL_MONTH " + 
				"         WHERE SUBSTR(invoice_date_month, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           and BUSINESS_REGION = '"+gsName+"' " + 
				"           AND SUBSTR(invoice_date_month, 0, 7) <= " + 
				"               TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"         GROUP BY SORG_LEVEL2, " + 
				"                  SORG_LEVEL3, " + 
				"                  company_busi_org_id, " + 
				"                  company_busi_org_name " + 
				"        union all " + 
				"        SELECT SORG_LEVEL2, " + 
				"               SORG_LEVEL3, " + 
				"               company_busi_org_id, " + 
				"               company_busi_org_name, " + 
				"               0 委托单量, " + 
				"               0 年预测收入, " + 
				"               0 as 收入总额, " + 
				"               SUM(total_cost) 业务成本 " + 
				"          FROM T_COMPANY_COST_ALL " + 
				"         where BUSINESS_REGION = '"+gsName+"' " + 
				"         GROUP BY SORG_LEVEL2, " + 
				"                  SORG_LEVEL3, " + 
				"                  company_busi_org_id, " + 
				"                  company_busi_org_name) " + 
				" group by company_busi_org_id,company_busi_org_name " + 
				" order by 年预测收入 desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsTableCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Map<String, String> resMap = fetchWebReq.getData();
		String gsName = resMap.get("gsName");
		
		sql.append(" SELECT PRODUCT_LINE_NAME, " + 
				"       sum(年委托单) 委托单量, " + 
				"       SUM(预测收入) 预测收入, " + 
				"       SUM(年业务收入) 年业务收入, " + 
				"       SUM(业务成本) 业务成本 " + 
				"  FROM (select PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               count(distinct(order_uuid)) 年委托单, " + 
				"               0 as 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               0 as 业务成本 " + 
				"          from T_ORDER_SERVER " + 
				"         where substr(create_time_order, 0, 4) = to_char(sysdate, 'yyyy') " + 
				"         and sorg_level3 ='"+gsName+"' " + 
				"           and data_state_order <> '草稿' " + 
				"           and data_state_order <> '草稿' " + 
				"           and audit_state_order <> '已删除' " + 
				"         group by PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               SUM(EX_TAX_PRICE) 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               0 as 业务成本 " + 
				"          FROM T_COMP_INVOICE_ALL " + 
				"         WHERE VOID_TIME IS NULL " + 
				"         and sorg_level3 ='"+gsName+"' " + 
				"           AND TYPE = '产品线业务' " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               0 as 年业务收入, " + 
				"               SUM(SYS_CUR_TOTAL_AMOUNT) 预测收入, " + 
				"               0 as 业务成本 " + 
				"          FROM T_ORDER_SERVER " + 
				"         WHERE SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"         and sorg_level3 ='"+gsName+"' " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME  " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               product_name PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               0 as 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               SUM(total_cost) AS 业务成本 " + 
				"          FROM T_COMPANY_COST_ALL " + 
				"         WHERE PRODUCT_NAME IS NOT NULL " + 
				"         and sorg_level3 ='"+gsName+"' " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, product_name) " + 
				" GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				" order by 预测收入 desc ");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findJtgsTableCpxQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		Map<String, String> resMap = fetchWebReq.getData();
		String gsName = resMap.get("gsName");
		
		sql.append(" SELECT PRODUCT_LINE_NAME, " + 
				"       sum(年委托单) 委托单量, " + 
				"       SUM(预测收入) 预测收入, " + 
				"       SUM(年业务收入) 年业务收入, " + 
				"       SUM(业务成本) 业务成本 " + 
				"  FROM (select PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               count(distinct(order_uuid)) 年委托单, " + 
				"               0 as 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               0 as 业务成本 " + 
				"          from T_ORDER_SERVER " + 
				"         where substr(create_time_order, 0, 4) = to_char(sysdate, 'yyyy') " + 
				"         and BUSINESS_REGION ='"+gsName+"' " + 
				"           and data_state_order <> '草稿' " + 
				"           and data_state_order <> '草稿' " + 
				"           and audit_state_order <> '已删除' " + 
				"         group by PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               SUM(EX_TAX_PRICE) 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               0 as 业务成本 " + 
				"          FROM T_COMP_INVOICE_ALL " + 
				"         WHERE VOID_TIME IS NULL " + 
				"         and BUSINESS_REGION ='"+gsName+"' " + 
				"           AND TYPE = '产品线业务' " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               0 as 年业务收入, " + 
				"               SUM(SYS_CUR_TOTAL_AMOUNT) 预测收入, " + 
				"               0 as 业务成本 " + 
				"          FROM T_ORDER_SERVER " + 
				"         WHERE SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"         and BUSINESS_REGION ='"+gsName+"' " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME  " + 
				"        union all " + 
				"        SELECT PRODUCT_LINE_NAME_ST1, " + 
				"               product_name PRODUCT_LINE_NAME, " + 
				"               0 as 委托单量, " + 
				"               0 as 年业务收入, " + 
				"               0 as 预测收入, " + 
				"               SUM(total_cost) AS 业务成本 " + 
				"          FROM T_COMPANY_COST_ALL " + 
				"         WHERE PRODUCT_NAME IS NOT NULL " + 
				"         and BUSINESS_REGION ='"+gsName+"' " + 
				"         GROUP BY PRODUCT_LINE_NAME_ST1, product_name) " + 
				" GROUP BY PRODUCT_LINE_NAME_ST1, PRODUCT_LINE_NAME " + 
				" order by 预测收入 desc");
		System.out.println(sql.toString());
		List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			JSONArray ja = NewUtils.returnCompanyMsgByCpxName(resultList);
			
			map.put("tableData", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
   	@Override
   	public Map<String, Object> findYyjyqk(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
   		// TODO Auto-generated method stub
       	String sql1 = " select * from (      " + 
       			"  SELECT SUM(EX_TAX_PRICE) as 年业务收入" + 
       			"  FROM T_COMP_INVOICE_ALL" + 
       			" WHERE VOID_TIME IS NULL" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM'))" + 
       			"   left join(" + 
       			"   " + 
       			"   SELECT SUM(EX_TAX_PRICE) as 日开票收入" + 
       			"          FROM T_COMP_INVOICE_ALL" + 
       			"         WHERE SUBSTR(INVOICE_DATE, 0, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-DD'))on 1=1" + 
       			"    left join(     " + 
       			"   SELECT SUM(EX_TAX_PRICE) AS 产品线业务" + 
       			"  FROM T_COMP_INVOICE_ALL" + 
       			" WHERE TYPE in ('产品线业务')" + 
       			"   AND VOID_TIME IS NULL" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM'))on 1=1" + 
       			"   left join(" + 
       			"    " + 
       			"   SELECT SUM(EX_TAX_PRICE) AS 非产品线业务" + 
       			"  FROM T_COMP_INVOICE_ALL" + 
       			" WHERE TYPE = '非产品线业务'" + 
       			"   AND VOID_TIME IS NULL" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM'))on 1=1" + 
       			"   left join(" + 
       			"   " + 
       			"   SELECT SUM(EX_TAX_PRICE) AS 待分类业务" + 
       			"  FROM T_COMP_INVOICE_ALL" + 
       			" WHERE TYPE = '待分类业务'" + 
       			"   AND VOID_TIME IS NULL" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM'))on 1=1" + 
       			"   left join(" + 
       			"   " + 
       			"   SELECT SUM(EX_TAX_PRICE) AS 其他业务" + 
       			"  FROM T_COMP_INVOICE_ALL" + 
       			" WHERE TYPE = '其他业务'" + 
       			"   AND VOID_TIME IS NULL" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
       			"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM'))on 1=1" + 
       			"   left join(" + 
       			"   SELECT SUM(业务成本) 业务成本," + 
       			"       SUM(直接成本) 直接成本," + 
       			"       SUM(间接成本) 间接成本," + 
       			"       SUM(今日业务成本) 今日业务成本," + 
       			"       SUM(今日直接成本) 今日直接成本," + 
       			"       SUM(今日间接成本) 今日间接成本" + 
       			"  FROM (SELECT COMPANY_BUSI_ORG_ID 公司ID," + 
       			"               SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本," + 
       			"               SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT +" + 
       			"                   CONTINU_PAYMENT_AMOUNT) 直接成本," + 
       			"               SUM(APPORT_PAY_AMOUNT) 间接成本," + 
       			"               0 AS 今日业务成本," + 
       			"               0 AS 今日直接成本," + 
       			"               0 AS 今日间接成本," + 
       			"               SORG_LEVEL3 PARENT_SORG_NAME," + 
       			"               BUSINESS_REGION AREA_COMPANY" + 
       			"          FROM T_COMPANY_COST_ALL" + 
       			"         GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION" + 
       			"        UNION ALL" + 
       			"        SELECT COMPANY_BUSI_ORG_ID 公司ID," + 
       			"               0 AS 业务成本," + 
       			"               0 AS 直接成本," + 
       			"               0 AS 间接成本," + 
       			"               SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 今日业务成本," + 
       			"               SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT +" + 
       			"                   CONTINU_PAYMENT_AMOUNT) 今日直接成本," + 
       			"               SUM(APPORT_PAY_AMOUNT) 今日间接成本," + 
       			"               SORG_LEVEL3 PARENT_SORG_NAME," + 
       			"               BUSINESS_REGION AREA_COMPANY" + 
       			"          FROM T_COMPANY_COST_ALL" + 
       			"         WHERE SUBSTR(PAY_END_DATE, 1, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-DD')" + 
       			"         GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION))on 1=1";		
       	System.out.println(sql1);
   		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
   		Map<String, Object> map = new HashMap<String, Object>();
   		if(list != null && list.size() != 0) {
   			Object[] o = list.get(0);
   			JSONObject ljArray = new JSONObject();
   			JSONObject ljArray1 = new JSONObject();
   			JSONArray mArray = new JSONArray();
   			JSONArray rArray = new JSONArray();
   			JSONArray rArray1 = new JSONArray();
   			JSONObject ywsrObject = new JSONObject();
   			JSONObject ywcbObject = new JSONObject();
   			DecimalFormat dFormat = new DecimalFormat("#.00");
   			ljArray = NewUtils.returnYwjiqkDoubleMsg("收入总额", "今日", o[0], o[1],dFormat);
   			ljArray1 = NewUtils.returnYwjiqkDoubleMsg("业务成本", "今日", o[6], o[9],dFormat);
   			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("产品线业务收入", o[2], dFormat));
   			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("非产品线业务收入", o[3], dFormat));
   			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("待分类业务收入", o[4], dFormat));
   			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("其他业务收入", o[5], dFormat));
   			mArray.add(NewUtils.returnYwjiqkDoubleMsg1("直接成本", o[7], dFormat));
   			mArray.add(NewUtils.returnYwjiqkDoubleMsg1("间接成本", o[8], dFormat));
   			rArray1.add(NewUtils.returnYwjiqkDoubleMsg2("今日", o[10], dFormat));
   			rArray1.add(NewUtils.returnYwjiqkDoubleMsg2("今日", o[11], dFormat));
   			ywsrObject.put("left", ljArray);
   			ywsrObject.put("right",rArray);
   			ywcbObject.put("left",ljArray1);
   			ywcbObject.put("mid",mArray);
   			ywcbObject.put("right",rArray1);
   			map.put("ywsr",ywsrObject);
   			map.put("ywcb",ywcbObject);
   		}
   		return map;
   	}
	
	@RpcMethod(loginValidate = false)
   	@Override
   	public Map<String, Object> findYyjyqkYhSrzeYear(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
       	String sql1 = " SELECT SUM(EX_TAX_PRICE) as 年业务收入 " + 
       			"       FROM T_COMP_INVOICE_ALL " + 
       			"       WHERE VOID_TIME IS NULL " + 
       			"       AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
       			"       AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')";		
       	System.out.println(sql1);
   		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
   		Map<String, Object> map = new HashMap<String, Object>();
   		if(list != null && list.size() != 0) {   			
   			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
   		}
   		return map;
   	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhSrzeDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(EX_TAX_PRICE) as 日开票收入 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE SUBSTR(INVOICE_DATE, 0, 10) = " + 
				"                  TO_CHAR(SYSDATE, 'YYYY-MM-DD')";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhSrzeCpxywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 产品线业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE in ('产品线业务') " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhSrzeFcpxywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 非产品线业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '非产品线业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhSrzeDflywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 待分类业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '待分类业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhSrzeQtywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 其他业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '其他业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhYwcbYear(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(业务成本) 业务成本 " + 
				"            FROM (SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本 " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhYwcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(今日业务成本) 今日业务成本 " + 
				"            FROM ( " + 
				"            SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 今日业务成本 " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                        TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhYwcbZjcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(直接成本) 直接成本 " + 
				"            FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                             CONTINU_PAYMENT_AMOUNT) 直接成本 " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhYwcbJjcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(间接成本) 间接成本 " + 
				"            FROM (SELECT SUM(APPORT_PAY_AMOUNT) 间接成本 " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhYwcbZjcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(今日直接成本) 今日直接成本 " + 
				"            FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                             CONTINU_PAYMENT_AMOUNT) 今日直接成本 " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                        TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkYhYwcbJjcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		String sql1 = " SELECT SUM(今日间接成本) 今日间接成本 " + 
				"            FROM (SELECT SUM(APPORT_PAY_AMOUNT) 今日间接成本 " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                        TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgs(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = "  select * " + 
				" from (SELECT SUM(EX_TAX_PRICE) as 年业务收入 " + 
				"       FROM T_COMP_INVOICE_ALL " + 
				"       WHERE VOID_TIME IS NULL " + 
				"       AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"       AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"       AND SORG_LEVEL3 = '"+companyId+"') " + 
				" left join (SELECT SUM(EX_TAX_PRICE) as 日开票收入 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE SUBSTR(INVOICE_DATE, 0, 10) = " + 
				"                  TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"            AND SORG_LEVEL3 = '"+companyId+"' ) " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 产品线业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE in ('产品线业务') " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND SORG_LEVEL3 = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 非产品线业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '非产品线业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND SORG_LEVEL3 = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 待分类业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '待分类业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND SORG_LEVEL3 = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 其他业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '其他业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND SORG_LEVEL3 = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(业务成本) 业务成本, " + 
				"                   SUM(直接成本) 直接成本, " + 
				"                   SUM(间接成本) 间接成本, " + 
				"                   SUM(今日业务成本) 今日业务成本, " + 
				"                   SUM(今日直接成本) 今日直接成本, " + 
				"                   SUM(今日间接成本) 今日间接成本 " + 
				"            FROM (SELECT COMPANY_BUSI_ORG_ID 公司ID, " + 
				"                         SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本, " + 
				"                         SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                             CONTINU_PAYMENT_AMOUNT) 直接成本, " + 
				"                         SUM(APPORT_PAY_AMOUNT) 间接成本, " + 
				"                         0 AS 今日业务成本, " + 
				"                         0 AS 今日直接成本, " + 
				"                         0 AS 今日间接成本, " + 
				"                         SORG_LEVEL3 PARENT_SORG_NAME, " + 
				"                         BUSINESS_REGION AREA_COMPANY " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  where SORG_LEVEL3 = '"+companyId+"' " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION " + 
				"                  UNION ALL " + 
				"                  SELECT COMPANY_BUSI_ORG_ID 公司ID, " + 
				"                         0 AS 业务成本, " + 
				"                         0 AS 直接成本, " + 
				"                         0 AS 间接成本, " + 
				"                         SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 今日业务成本, " + 
				"                         SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                             CONTINU_PAYMENT_AMOUNT) 今日直接成本, " + 
				"                         SUM(APPORT_PAY_AMOUNT) 今日间接成本, " + 
				"                         SORG_LEVEL3 PARENT_SORG_NAME, " + 
				"                         BUSINESS_REGION AREA_COMPANY " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                        TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                  AND SORG_LEVEL3 = '"+companyId+"' " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)) " + 
				" on 1 = 1 " + 
				"";		
		System.out.println(sql1);
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {
			Object[] o = list.get(0);
			JSONObject ljArray = new JSONObject();
			JSONObject ljArray1 = new JSONObject();
			JSONArray mArray = new JSONArray();
			JSONArray rArray = new JSONArray();
			JSONArray rArray1 = new JSONArray();
			JSONObject ywsrObject = new JSONObject();
			JSONObject ywcbObject = new JSONObject();
			DecimalFormat dFormat = new DecimalFormat("#.00");
			ljArray = NewUtils.returnYwjiqkDoubleMsg("收入总额", "今日", o[0], o[1],dFormat);
			ljArray1 = NewUtils.returnYwjiqkDoubleMsg("业务成本", "今日", o[6], o[9],dFormat);
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("产品线业务收入", o[2], dFormat));
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("非产品线业务收入", o[3], dFormat));
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("待分类业务收入", o[4], dFormat));
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("其他业务收入", o[5], dFormat));
			mArray.add(NewUtils.returnYwjiqkDoubleMsg1("直接成本", o[7], dFormat));
			mArray.add(NewUtils.returnYwjiqkDoubleMsg1("间接成本", o[8], dFormat));
			rArray1.add(NewUtils.returnYwjiqkDoubleMsg2("今日", o[10], dFormat));
			rArray1.add(NewUtils.returnYwjiqkDoubleMsg2("今日", o[11], dFormat));
			ywsrObject.put("left", ljArray);
			ywsrObject.put("right",rArray);
			ywcbObject.put("left",ljArray1);
			ywcbObject.put("mid",mArray);
			ywcbObject.put("right",rArray1);
			map.put("ywsr",ywsrObject);
			map.put("ywcb",ywcbObject);
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
   	@Override
   	public Map<String, Object> findYyjyqkJtgsYhSrzeYear(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
       	String sql1 = " SELECT SUM(EX_TAX_PRICE) as 年业务收入 " + 
       			"        FROM T_COMP_INVOICE_ALL " + 
       			"        WHERE VOID_TIME IS NULL " + 
       			"        AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
       			"        AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
       			"        AND SORG_LEVEL3 = '"+companyId+"'";		
       	System.out.println(sql1);
   		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
   		Map<String, Object> map = new HashMap<String, Object>();
   		if(list != null && list.size() != 0) {   			
   			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
   		}
   		return map;
   	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhSrzeDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) as 日开票收入 " + 
				"             FROM T_COMP_INVOICE_ALL " + 
				"             WHERE SUBSTR(INVOICE_DATE, 0, 10) = " + 
				"                   TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"             AND SORG_LEVEL3 = '"+companyId+"'";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhSrzeCpxywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 产品线业务 " + 
				"             FROM T_COMP_INVOICE_ALL " + 
				"             WHERE TYPE in ('产品线业务') " + 
				"             AND VOID_TIME IS NULL " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"             AND SORG_LEVEL3 = '"+companyId+"'";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhSrzeFcpxywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 非产品线业务 " + 
				"             FROM T_COMP_INVOICE_ALL " + 
				"             WHERE TYPE = '非产品线业务' " + 
				"             AND VOID_TIME IS NULL " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"             AND SORG_LEVEL3 = '"+companyId+"'";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhSrzeDflywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 待分类业务 " + 
				"             FROM T_COMP_INVOICE_ALL " + 
				"             WHERE TYPE = '待分类业务' " + 
				"             AND VOID_TIME IS NULL " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"             AND SORG_LEVEL3 = '"+companyId+"'";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhSrzeQtywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 其他业务 " + 
				"             FROM T_COMP_INVOICE_ALL " + 
				"             WHERE TYPE = '其他业务' " + 
				"             AND VOID_TIME IS NULL " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"             AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"             AND SORG_LEVEL3 = '"+companyId+"'";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhYwcbYear(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(业务成本) 业务成本 " + 
				"             FROM (SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本 " + 
				"                   FROM T_COMPANY_COST_ALL " + 
				"                   where SORG_LEVEL3 = '"+companyId+"' " + 
				"                   GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhYwcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(今日业务成本) 今日业务成本 " + 
				"             FROM (SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 今日业务成本 " + 
				"                   FROM T_COMPANY_COST_ALL " + 
				"                   WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                         TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                   AND SORG_LEVEL3 = '"+companyId+"' " + 
				"                   GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhYwcbZjcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(直接成本) 直接成本 " + 
				"             FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                              CONTINU_PAYMENT_AMOUNT) 直接成本 " + 
				"                   FROM T_COMPANY_COST_ALL " + 
				"                   where SORG_LEVEL3 = '"+companyId+"' " + 
				"                   GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION " + 
				"                   )";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhYwcbJjcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(间接成本) 间接成本 " + 
				"             FROM (SELECT SUM(APPORT_PAY_AMOUNT) 间接成本 " + 
				"                   FROM T_COMPANY_COST_ALL " + 
				"                   where SORG_LEVEL3 = '"+companyId+"' " + 
				"                   GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION " + 
				"                   )";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhYwcbZjcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(今日直接成本) 今日直接成本 " + 
				"             FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                              CONTINU_PAYMENT_AMOUNT) 今日直接成本 " + 
				"                   FROM T_COMPANY_COST_ALL " + 
				"                   WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                         TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                   AND SORG_LEVEL3 = '"+companyId+"' " + 
				"                   GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsYhYwcbJjcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(今日间接成本) 今日间接成本 " + 
				"             FROM (SELECT SUM(APPORT_PAY_AMOUNT) 今日间接成本 " + 
				"                   FROM T_COMPANY_COST_ALL " + 
				"                   WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                         TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                   AND SORG_LEVEL3 = '"+companyId+"' " + 
				"                   GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyu(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = "   select * " + 
				" from (SELECT SUM(EX_TAX_PRICE) as 年业务收入 " + 
				"       FROM T_COMP_INVOICE_ALL " + 
				"       WHERE VOID_TIME IS NULL " + 
				"       AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"       AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"       AND BUSINESS_REGION = '"+companyId+"') " + 
				" left join (SELECT SUM(EX_TAX_PRICE) as 日开票收入 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE SUBSTR(INVOICE_DATE, 0, 10) = " + 
				"                  TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"            AND BUSINESS_REGION = '"+companyId+"' ) " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 产品线业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE in ('产品线业务') " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND BUSINESS_REGION = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 非产品线业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '非产品线业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND BUSINESS_REGION = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 待分类业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '待分类业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND BUSINESS_REGION = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(EX_TAX_PRICE) AS 其他业务 " + 
				"            FROM T_COMP_INVOICE_ALL " + 
				"            WHERE TYPE = '其他业务' " + 
				"            AND VOID_TIME IS NULL " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"            AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"            AND BUSINESS_REGION = '"+companyId+"') " + 
				" on 1 = 1 " + 
				" left join (SELECT SUM(业务成本) 业务成本, " + 
				"                   SUM(直接成本) 直接成本, " + 
				"                   SUM(间接成本) 间接成本, " + 
				"                   SUM(今日业务成本) 今日业务成本, " + 
				"                   SUM(今日直接成本) 今日直接成本, " + 
				"                   SUM(今日间接成本) 今日间接成本 " + 
				"            FROM (SELECT COMPANY_BUSI_ORG_ID 公司ID, " + 
				"                         SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本, " + 
				"                         SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                             CONTINU_PAYMENT_AMOUNT) 直接成本, " + 
				"                         SUM(APPORT_PAY_AMOUNT) 间接成本, " + 
				"                         0 AS 今日业务成本, " + 
				"                         0 AS 今日直接成本, " + 
				"                         0 AS 今日间接成本, " + 
				"                         SORG_LEVEL3 PARENT_SORG_NAME, " + 
				"                         BUSINESS_REGION AREA_COMPANY " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  where BUSINESS_REGION = '"+companyId+"' " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION " + 
				"                  UNION ALL " + 
				"                  SELECT COMPANY_BUSI_ORG_ID 公司ID, " + 
				"                         0 AS 业务成本, " + 
				"                         0 AS 直接成本, " + 
				"                         0 AS 间接成本, " + 
				"                         SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 今日业务成本, " + 
				"                         SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                             CONTINU_PAYMENT_AMOUNT) 今日直接成本, " + 
				"                         SUM(APPORT_PAY_AMOUNT) 今日间接成本, " + 
				"                         SORG_LEVEL3 PARENT_SORG_NAME, " + 
				"                         BUSINESS_REGION AREA_COMPANY " + 
				"                  FROM T_COMPANY_COST_ALL " + 
				"                  WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                        TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                  AND BUSINESS_REGION = '"+companyId+"' " + 
				"                  GROUP BY COMPANY_BUSI_ORG_ID, SORG_LEVEL3, BUSINESS_REGION)) " + 
				" on 1 = 1 ";		
		System.out.println(sql1);
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {
			Object[] o = list.get(0);
			JSONObject ljArray = new JSONObject();
			JSONObject ljArray1 = new JSONObject();
			JSONArray mArray = new JSONArray();
			JSONArray rArray = new JSONArray();
			JSONArray rArray1 = new JSONArray();
			JSONObject ywsrObject = new JSONObject();
			JSONObject ywcbObject = new JSONObject();
			DecimalFormat dFormat = new DecimalFormat("#.00");
			ljArray = NewUtils.returnYwjiqkDoubleMsg("收入总额", "今日", o[0], o[1],dFormat);
			ljArray1 = NewUtils.returnYwjiqkDoubleMsg("业务成本", "今日", o[6], o[9],dFormat);
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("产品线业务收入", o[2], dFormat));
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("非产品线业务收入", o[3], dFormat));
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("待分类业务收入", o[4], dFormat));
			rArray.add(NewUtils.returnYwjiqkDoubleMsg1("其他业务收入", o[5], dFormat));
			mArray.add(NewUtils.returnYwjiqkDoubleMsg1("直接成本", o[7], dFormat));
			mArray.add(NewUtils.returnYwjiqkDoubleMsg1("间接成本", o[8], dFormat));
			rArray1.add(NewUtils.returnYwjiqkDoubleMsg2("今日", o[10], dFormat));
			rArray1.add(NewUtils.returnYwjiqkDoubleMsg2("今日", o[11], dFormat));
			ywsrObject.put("left", ljArray);
			ywsrObject.put("right",rArray);
			ywcbObject.put("left",ljArray1);
			ywcbObject.put("mid",mArray);
			ywcbObject.put("right",rArray1);
			map.put("ywsr",ywsrObject);
			map.put("ywcb",ywcbObject);
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
   	@Override
   	public Map<String, Object> findYyjyqkJtgsQuyuYhSrzeYear(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
       	String sql1 = " SELECT SUM(EX_TAX_PRICE) as 年业务收入 " + 
       			"         FROM T_COMP_INVOICE_ALL " + 
       			"         WHERE VOID_TIME IS NULL " + 
       			"         AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
       			"         AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
       			"         AND BUSINESS_REGION = '"+companyId+"' ";		
       	System.out.println(sql1);
   		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
   		Map<String, Object> map = new HashMap<String, Object>();
   		if(list != null && list.size() != 0) {   			
   			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
   		}
   		return map;
   	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhSrzeDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) as 日开票收入 " + 
				"              FROM T_COMP_INVOICE_ALL " + 
				"              WHERE SUBSTR(INVOICE_DATE, 0, 10) = " + 
				"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"              AND BUSINESS_REGION = '"+companyId+"' ";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhSrzeCpxywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 产品线业务 " + 
				"              FROM T_COMP_INVOICE_ALL " + 
				"              WHERE TYPE in ('产品线业务') " + 
				"              AND VOID_TIME IS NULL " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"              AND BUSINESS_REGION = '"+companyId+"' ";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhSrzeFcpxywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 非产品线业务 " + 
				"              FROM T_COMP_INVOICE_ALL " + 
				"              WHERE TYPE = '非产品线业务' " + 
				"              AND VOID_TIME IS NULL " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"              AND BUSINESS_REGION = '"+companyId+"' ";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhSrzeDflywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 待分类业务 " + 
				"              FROM T_COMP_INVOICE_ALL " + 
				"              WHERE TYPE = '待分类业务' " + 
				"              AND VOID_TIME IS NULL " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"              AND BUSINESS_REGION = '"+companyId+"' ";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhSrzeQtywsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(EX_TAX_PRICE) AS 其他业务 " + 
				"              FROM T_COMP_INVOICE_ALL " + 
				"              WHERE TYPE = '其他业务' " + 
				"              AND VOID_TIME IS NULL " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
				"              AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
				"              AND BUSINESS_REGION = '"+companyId+"' ";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhYwcbYear(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(业务成本) 业务成本 " + 
				"              FROM (SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 业务成本 " + 
				"                    FROM T_COMPANY_COST_ALL " + 
				"                    where BUSINESS_REGION = '"+companyId+"' " + 
				"                    GROUP BY COMPANY_BUSI_ORG_ID, " + 
				"                             SORG_LEVEL3, " + 
				"                             BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhYwcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(今日业务成本) 今日业务成本 " + 
				"              FROM (SELECT SUM(APPORT_PAY_AMOUNT + DIRECT_COST) 今日业务成本 " + 
				"                    FROM T_COMPANY_COST_ALL " + 
				"                    WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                          TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                    AND BUSINESS_REGION = '"+companyId+"' " + 
				"                    GROUP BY COMPANY_BUSI_ORG_ID, " + 
				"                             SORG_LEVEL3, " + 
				"                             BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhYwcbZjcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(直接成本) 直接成本 " + 
				"              FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + " + 
				"                               CONTINU_PAYMENT_AMOUNT) 直接成本 " + 
				"                    FROM T_COMPANY_COST_ALL " + 
				"                    where BUSINESS_REGION = '"+companyId+"' " + 
				"                    GROUP BY COMPANY_BUSI_ORG_ID, " + 
				"                             SORG_LEVEL3, " + 
				"                             BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhYwcbJjcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(间接成本) 间接成本 " + 
				"              FROM (SELECT SUM(APPORT_PAY_AMOUNT) 间接成本 " + 
				"                    FROM T_COMPANY_COST_ALL " + 
				"                    where BUSINESS_REGION = '"+companyId+"' " + 
				"                    GROUP BY COMPANY_BUSI_ORG_ID, " + 
				"                             SORG_LEVEL3, " + 
				"                             BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result",NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhYwcbZjcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(今日直接成本) 今日直接成本 " + 
				"              FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + CONTINU_PAYMENT_AMOUNT) 今日直接成本 " + 
				"                    FROM T_COMPANY_COST_ALL " + 
				"                    WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                          TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                    AND BUSINESS_REGION = '"+companyId+"' " + 
				"                    GROUP BY COMPANY_BUSI_ORG_ID,SORG_LEVEL3,BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYyjyqkJtgsQuyuYhYwcbJjcbDay(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		
		String sql1 = " SELECT SUM(今日间接成本) 今日间接成本 " + 
				"              FROM (SELECT SUM(APPORT_PAY_AMOUNT) 今日间接成本 " + 
				"                    FROM T_COMPANY_COST_ALL " + 
				"                    WHERE SUBSTR(PAY_END_DATE, 1, 10) = " + 
				"                          TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"                    AND BUSINESS_REGION = '"+companyId+"' " + 
				"                    GROUP BY COMPANY_BUSI_ORG_ID, " + 
				"                             SORG_LEVEL3, " + 
				"                             BUSINESS_REGION)";		
		System.out.println(sql1);
		List<BigDecimal> list = DBManager.get("kabBan").createNativeQuery(sql1).getResultList();
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() != 0) {   			
			map.put("result","+"+NewUtils.returnYiOrWanWtdl(list.get(0), 2));
		}
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwskByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select \"年委托单量\",\"日委托单量\",\"年开工单量\", \"日开工单量\"," + 
				"      \"年完工单量\", \"日完工单量\",\"年客户\"," + 
				"       \"日客户\"," + 
				"      \"年供应商\"," + 
				"       \"日供应商\"," + 
				"       \"年证书\"," + 
				"       \"日证书\"" + 
				"        from(" + 
				"    select " + 
				" sum(ORDER_CREATE_COUNT)年委托单量," + 
				" sum(ORDER_START_COUNT)年开工单量," + 
				" sum(ORDER_END_COUNT)年完工单量" + 
				" from T_COMP_AMOUNT_ALL_MONTH " + 
				" where  PRODUCT_LINE_NAME ='"+companyId+"')" + 
				" left join(select count(distinct(order_uuid)) 日委托单量" + 
				"          from T_ORDER_SERVER" + 
				"         where substr(create_time_order, 0, 10) =to_char(sysdate,'yyyy-mm-dd')" + 
				"           and data_state_order <> '草稿'" + 
				"           and data_state_order <> '草稿'" + 
				"           and audit_state_order <> '已删除'" + 
				"           and PRODUCT_LINE_NAME ='"+companyId+"')on 1=1" + 
				" left join(SELECT COUNT(DISTINCT(ORDER_UUID)) 日开工单量" + 
				"  FROM T_ORDER_SERVER" + 
				" WHERE DATA_STATE_ORDER <> '草稿'" + 
				"   AND DATA_STATE_ORDER <> '草稿'" + 
				"   AND AUDIT_STATE_ORDER <> '已删除'" + 
				"   AND PRODUCT_LINE_NAME ='"+companyId+"'" + 
				"   AND SUBSTR(JOB_START_TIME_ORDER, 0, 10) = TO_CHAR(SYSDATE, 'yyyy-mm-dd'))on 1=1" + 
				" left join( select " + 
				"         count(distinct(order_uuid)) 日完工单量" + 
				"        from T_ORDER_SERVER" + 
				"        where  " + 
				"        substr(work_finish_time,0,10)=to_char(sysdate,'yyyy-mm-dd')" + 
				"         and PRODUCT_LINE_NAME='"+companyId+"')on 1=1" + 
				" left join(SELECT 年客户, 日客户" + 
				"  FROM (SELECT COUNT(DISTINCT(CUSTOMER_NAME)) 年客户" + 
				"          FROM T_ORDER_CRM" + 
				"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 4) = to_char(SYSDATE,'YYYY')" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"') A," + 
				"       (SELECT COUNT(DISTINCT(CUSTOMER_NAME)) 日客户" + 
				"          FROM T_ORDER_CRM" + 
				"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 10) = to_char(SYSDATE,'YYYY')" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"'))on 1=1" + 
				" left join(select 日供应商,年供应商" + 
				"        from (select " + 
				"       count(distinct(SUB_PACKAGE_NAME)) 年供应商" + 
				"        from T_ORDER_SRM " + 
				"        where  " + 
				"        substr(create_time_order,0,4)= TO_CHAR(SYSDATE,'YYYY') " + 
				"        and PRODUCT_NAME = '"+companyId+"') a, (" + 
				"        select " + 
				"       count(distinct(SUB_PACKAGE_NAME)) 日供应商" + 
				"        from T_ORDER_SRM" + 
				"        where  " + 
				"        substr(create_time_order,0,10)=TO_CHAR(SYSDATE,'YYYY-MM-DD') " + 
				"        and PRODUCT_NAME = '"+companyId+"'))on 1=1" + 
				" left join(SELECT 年证书, 日证书" + 
				"  FROM (SELECT COUNT(MID) 年证书" + 
				"          FROM T_ORDER_CERC" + 
				"         WHERE SUBSTR(FICTIONAL_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
				"           AND STATE = '已完成'" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"') A," + 
				"       (SELECT COUNT(MID) 日证书" + 
				"          FROM T_ORDER_CERC" + 
				"         WHERE SUBSTR(FICTIONAL_DATE, 0, 10) =" + 
				"               TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"           AND STATE = '已完成'" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"'))on 1=1" ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 返回的jsonarray
			JSONArray ja = new JSONArray();
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			ja.add(NewUtils.returnTodayTotalIntMsg("委托单量",  res[0], res[1], df));
			
			ja.add(NewUtils.returnTodayTotalIntMsg("开工单量",  res[2], res[3], df));
			
			ja.add(NewUtils.returnTodayTotalIntMsg("完工单量",  res[4], res[5], df));
			
			ja.add(NewUtils.returnTodayTotalIntMsg("客户数量",  res[6], res[7], df));
			
			ja.add(NewUtils.returnTodayTotalIntMsg("供应商数量",  res[8], res[9], df));
			
			ja.add(NewUtils.returnTodayTotalIntMsg("出证数量",  res[10], res[11], df));
			
			map.put("ttdata", ja);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwskByCpxYhWtdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " select * from (" + 
				"select sum(ORDER_CREATE_COUNT) 年委托单量" + 
				"      from T_COMP_AMOUNT_ALL_MONTH" + 
				"      where PRODUCT_LINE_NAME = '"+companyId+"')," + 
				"(select count(distinct(order_uuid)) 日委托单量" + 
				"           from T_ORDER_SERVER" + 
				"           where substr(create_time_order, 0, 10) =" + 
				"                 to_char(sysdate, 'yyyy-mm-dd')" + 
				"           and data_state_order <> '草稿'" + 
				"           and data_state_order <> '草稿'" + 
				"           and audit_state_order <> '已删除'" + 
				"           and PRODUCT_LINE_NAME = '"+companyId+"') " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");		
			
			map.put("result", NewUtils.returnTodayTotalIntMsg("委托单量",  res[0], res[1], df));
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwskByCpxYhKgdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " select * from (" + 
				"select sum(ORDER_START_COUNT) 年开工单量" + 
				"      from T_COMP_AMOUNT_ALL_MONTH" + 
				"      where PRODUCT_LINE_NAME = '"+companyId+"')," + 
				"(SELECT COUNT(DISTINCT(ORDER_UUID)) 日开工单量" + 
				"           FROM T_ORDER_SERVER" + 
				"           WHERE DATA_STATE_ORDER <> '草稿'" + 
				"           AND DATA_STATE_ORDER <> '草稿'" + 
				"           AND AUDIT_STATE_ORDER <> '已删除'" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"'" + 
				"           AND SUBSTR(JOB_START_TIME_ORDER, 0, 10) =" + 
				"                 TO_CHAR(SYSDATE, 'yyyy-mm-dd'))   " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");		
			
			map.put("result", NewUtils.returnTodayTotalIntMsg("开工单量",  res[0], res[1], df));
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwskByCpxYhWgdl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " select * from (" + 
				"select sum(ORDER_END_COUNT) 年完工单量" + 
				"      from T_COMP_AMOUNT_ALL_MONTH" + 
				"      where PRODUCT_LINE_NAME = '"+companyId+"')," + 
				"(select count(distinct(order_uuid)) 日完工单量" + 
				"           from T_ORDER_SERVER" + 
				"           where substr(work_finish_time, 0, 10) =" + 
				"                 to_char(sysdate, 'yyyy-mm-dd')" + 
				"           and PRODUCT_LINE_NAME = '"+companyId+"') " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");		
			
			map.put("result", NewUtils.returnTodayTotalIntMsg("完工单量",  res[0], res[1], df));
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwskByCpxYhKhsl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " SELECT SUM(年客户数量) 年客户数量, " + 
				"       SUM(NVL(日客户数量, 0)) 日客户数量 " + 
				"  FROM (SELECT product_line_name, COUNT(DISTINCT CUSTOMER_NAME) 年客户数量 " + 
				"          FROM T_ORDER_CRM " + 
				"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
				"               TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
				"           AND product_line_name IS NOT NULL " + 
				"         GROUP BY product_line_name) A " + 
				"  LEFT JOIN (SELECT product_line_name, COUNT(DISTINCT CUSTOMER_NAME) 日客户数量 " + 
				"               FROM T_ORDER_CRM " + 
				"              WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
				"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"              GROUP BY product_line_name) B " + 
				"    ON A.product_line_name = B.product_line_name " + 
				"  LEFT JOIN (SELECT COUNT(CUSTOMER_NAME) 非产品线活跃客户, product_line_name " + 
				"               FROM (SELECT DISTINCT CUSTOMER_NAME, product_line_name " + 
				"                       FROM T_ORDER_CRM T1 " + 
				"                      WHERE TYPE <> '产品线业务') " + 
				"              GROUP BY product_line_name) D " + 
				"    ON A.product_line_name = D.product_line_name " + 
				" WHERE A.product_line_name = '"+companyId+"' " + 
				" GROUP BY A.product_line_name " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");		
			
			map.put("result", NewUtils.returnTodayTotalIntMsg("客户数量",  res[0], res[1], df));
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwskByCpxYhGyssl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " SELECT SUM(年供应商数量) 年供应商数量, " + 
				"       SUM(NVL(日供应商数量, 0)) 日供应商数量 " + 
				"  FROM (SELECT PRODUCT_NAME, COUNT(DISTINCT SUB_PACKAGE_NAME) 年供应商数量 " + 
				"          FROM T_ORDER_SRM " + 
				"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
				"               TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
				"           AND PRODUCT_NAME IS NOT NULL " + 
				"         GROUP BY PRODUCT_NAME) A " + 
				"  LEFT JOIN (SELECT PRODUCT_NAME, " + 
				"                    COUNT(DISTINCT SUB_PACKAGE_NAME) 日供应商数量 " + 
				"               FROM T_ORDER_SRM " + 
				"              WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
				"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
				"              GROUP BY PRODUCT_NAME) B " + 
				"    ON A.PRODUCT_NAME = B.PRODUCT_NAME " + 
				"WHERE A.PRODUCT_NAME = '"+companyId+"' " + 
				" GROUP BY A.PRODUCT_NAME " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");		
			
			map.put("result", NewUtils.returnTodayTotalIntMsg("供应商数量",  res[0], res[1], df));
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findYwskByCpxYhCzsl(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " select * from (" + 
				"SELECT 年证书, 日证书" + 
				"           FROM (SELECT COUNT(MID) 年证书" + 
				"                 FROM T_ORDER_CERC" + 
				"                 WHERE SUBSTR(FICTIONAL_DATE, 0, 4) =" + 
				"                       TO_CHAR(SYSDATE, 'yyyy')" + 
				"                 AND STATE = '已完成'" + 
				"                 AND PRODUCT_LINE_NAME = '"+companyId+"') A," + 
				"                (SELECT COUNT(MID) 日证书" + 
				"                 FROM T_ORDER_CERC" + 
				"                 WHERE SUBSTR(FICTIONAL_DATE, 0, 10) =" + 
				"                       TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"                 AND STATE = '已完成'" + 
				"                 AND PRODUCT_LINE_NAME = '"+companyId+"'))   " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");		
			
			map.put("result", NewUtils.returnTodayTotalIntMsg("出证数量",  res[0], res[1], df));
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select \"年委托金额\",\"日委托金额\",\"年开工金额\", \"日开工金额\"," + 
				"      \"年预测收入\", \"日预测收入\",\"年业务收入\"," + 
				"       \"日业务收入\"," + 
				"      \"年业务成本\"," + 
				"       \"日业务成本\"," + 
				"       \"毛利率\"，" + 
				"       \"年直接成本\"，" + 
				"       \"日直接成本\"，" + 
				"       \"年间接成本\"，" + 
				"       \"日间接成本\" " + 
				"        from(" + 
				"    select" + 
				"    sum(ORDER_CREATE_AMOUNT) 年委托金额，" + 
				"    sum(SERVER_END_AMOUNT) 年预测收入" + 
				"    from T_COMP_AMOUNT_ALL_MONTH " + 
				"    where  PRODUCT_LINE_NAME ='"+companyId+"')" + 
				"    left join(SELECT NVL(SUM(SYS_CUR_TOTAL_AMOUNT), 0) 日委托金额" + 
				"          FROM T_ORDER_SERVER" + 
				"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 10) =" + 
				"               TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"           AND DATA_STATE_ORDER <> '已删除'" + 
				"           AND DATA_STATE_ORDER <> '草稿'" + 
				"           AND AUDIT_STATE_ORDER <> '草稿'" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"')on 1=1" + 
				"    left join(SELECT 年开工金额,NVL(日开工金额,0) 日开工金额" + 
				"  FROM (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年开工金额" + 
				"          FROM T_ORDER_SERVER" + 
				"         WHERE JOB_START_TIME_ORDER IS NOT NULL" + 
				"           AND SUBSTR(JOB_START_TIME_ORDER, 1, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"') A," + 
				"       (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日开工金额" + 
				"          FROM T_ORDER_SERVER" + 
				"         WHERE JOB_START_TIME_ORDER IS NOT NULL" + 
				"           AND SUBSTR(JOB_START_TIME_ORDER, 1, 10) =" + 
				"               TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"'))on 1=1  " + 
				"   left join(SELECT NVL(SUM(SYS_CUR_TOTAL_AMOUNT), 0) 日预测收入" + 
				"          FROM T_ORDER_SERVER" + 
				"         WHERE SUBSTR(WORK_FINISH_TIME, 0, 10) =" + 
				"               TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"'" + 
				"           AND DATA_STATE_ORDER <> '已删除'" + 
				"           AND DATA_STATE_ORDER <> '草稿'" + 
				"           AND AUDIT_STATE_ORDER <> '草稿')on 1=1" + 
				"   left join(SELECT " + 
				"       SUM(EX_TAX_PRICE) AS 年业务收入" + 
				"  FROM T_COMP_INVOICE_ALL" + 
				" WHERE TYPE IN ('产品线业务')" + 
				"   AND VOID_TIME IS NULL" + 
				"   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
				"   AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
				"   AND PRODUCT_LINE_NAME = '"+companyId+"')on 1=1" + 
				" left join(SELECT SUM(EX_TAX_PRICE) AS 日业务收入" + 
				"  FROM T_COMP_INVOICE_ALL" + 
				" WHERE TYPE IN ('产品线业务')" + 
				"   AND VOID_TIME IS NULL" + 
				"   AND SUBSTR(INVOICE_DATE, 0, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-dd')" + 
				"   and PRODUCT_LINE_NAME ='"+companyId+"')on 1=1" + 
				"   left join(SELECT 年业务成本,nvl(日业务成本,0) 日业务成本" + 
				"  FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + APPORT_PAY_AMOUNT +" + 
				"                   CONTINU_PAYMENT_AMOUNT) AS 年业务成本" + 
				"          FROM T_COMPANY_COST_ALL" + 
				"         WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
				"           AND PRODUCT_NAME IS NOT NULL" + 
				"           AND PRODUCT_NAME = '"+companyId+"') A," + 
				"       (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT + APPORT_PAY_AMOUNT +" + 
				"                   CONTINU_PAYMENT_AMOUNT) AS 日业务成本" + 
				"          FROM T_COMPANY_COST_ALL" + 
				"         WHERE SUBSTR(PAY_END_DATE, 0, 10) = TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"           AND PRODUCT_NAME IS NOT NULL" + 
				"           AND PRODUCT_NAME = '"+companyId+"'))on 1=1" + 
				"    left join(SELECT 业务收入,业务成本,(round((业务收入 - 业务成本) / 业务收入*100,2)) 毛利率" + 
				"  FROM (SELECT  nvl(SUM(EX_TAX_PRICE),0) 业务收入" + 
				"          FROM T_COMP_INVOICE_ALL" + 
				"         WHERE VOID_TIME IS NULL" + 
				"           AND TYPE = '产品线业务'" + 
				"           and PRODUCT_LINE_NAME='"+companyId+"'" + 
				"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
				"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
				"         ) A," + 
				" (SELECT SUM(TOTAL_COST) AS 业务成本" + 
				"    FROM T_COMPANY_COST_ALL" + 
				"   WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
				"   and PRODUCT_NAME='"+companyId+"'" + 
				"  ) B)on 1=1" + 
				"   left join(select 年直接成本, 日直接成本" + 
				"  from (select nvl(sum(direct_cost),0) 年直接成本" + 
				"          from T_COMPANY_COST_ALL" + 
				"         where substr(pay_end_date, 0,4) =to_char(sysdate,'yyyy')" + 
				"          and product_name='"+companyId+"') a," + 
				"       (select nvl(sum(direct_cost),0) 日直接成本" + 
				"          from T_COMPANY_COST_ALL" + 
				"         where substr(pay_end_date, 0, 10) =to_char(sysdate,'yyyy-mm-dd')" + 
				"         and product_name='"+companyId+"'))on 1=1" + 
				"   left join(select 年间接成本, 日间接成本" + 
				"  from (select nvl(sum(APPORT_PAY_AMOUNT),0) 年间接成本" + 
				"          from T_COMPANY_COST_ALL" + 
				"         where substr(PAY_END_DATE, 0,4) =to_char(sysdate,'yyyy')" + 
				"         and PRODUCT_NAME='"+companyId+"') a," + 
				"       (select nvl(sum(APPORT_PAY_AMOUNT),0) 日间接成本" + 
				"          from T_COMPANY_COST_ALL" + 
				"         where substr(PAY_END_DATE, 0, 10) =to_char(sysdate,'yyyy-mm-dd')" + 
				"         and PRODUCT_NAME='"+companyId+"'))on 1=1" ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 返回的jsonarray
			JSONArray ja = new JSONArray();
			JSONArray ja1 = new JSONArray();
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			ja.add(NewUtils.returnTodayTotalDoubleMsg("委托金额",  res[0], res[1], df));
			
			ja.add(NewUtils.returnTodayTotalDoubleMsg("开工金额",  res[2], res[3], df));
			
			ja.add(NewUtils.returnTodayTotalDoubleMsg("预测收入",  res[4], res[5], df));
			
			ja.add(NewUtils.returnTodayTotalDoubleMsg("收入总额",  res[6], res[7], df));
			
			ja.add(NewUtils.returnTodayTotalDoubleMsg("业务成本",  res[8], res[9], df));
			
			ja.add(NewUtils.returnTodayTotalDoubleMsg("毛利率",  res[10],null , df));
			
			ja1.add(NewUtils.returnYwjiqkDoubleMsg1("直接成本", res[11], df));
			
			ja1.add(NewUtils.returnYwjiqkDoubleMsg1("间接成本", res[13], df));
			map.put("ttuData", ja);
			map.put("ttdData", ja1);
		}
		
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpxYhWtje(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select * from (" + 
				"select sum(ORDER_CREATE_AMOUNT) 年委托金额" + 
				"      from T_COMP_AMOUNT_ALL_MONTH" + 
				"      where PRODUCT_LINE_NAME = '"+companyId+"')," + 
				"(SELECT NVL(SUM(SYS_CUR_TOTAL_AMOUNT), 0) 日委托金额" + 
				"           FROM T_ORDER_SERVER" + 
				"           WHERE SUBSTR(CREATE_TIME_ORDER, 0, 10) =" + 
				"                 TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"           AND DATA_STATE_ORDER <> '已删除'" + 
				"           AND DATA_STATE_ORDER <> '草稿'" + 
				"           AND AUDIT_STATE_ORDER <> '草稿'" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"')   " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			map.put("result", NewUtils.returnTodayTotalDoubleMsgCpx("委托金额",  res[0], res[1], df));
		}
		
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpxYhKgje(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select * from (" + 
				"SELECT 年开工金额, NVL(日开工金额, 0) 日开工金额" + 
				"           FROM (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年开工金额" + 
				"                 FROM T_ORDER_SERVER" + 
				"                 WHERE JOB_START_TIME_ORDER IS NOT NULL" + 
				"                 AND SUBSTR(JOB_START_TIME_ORDER, 1, 4) =" + 
				"                       TO_CHAR(SYSDATE, 'yyyy')" + 
				"                 AND PRODUCT_LINE_NAME = '"+companyId+"') A," + 
				"                (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日开工金额" + 
				"                 FROM T_ORDER_SERVER" + 
				"                 WHERE JOB_START_TIME_ORDER IS NOT NULL" + 
				"                 AND SUBSTR(JOB_START_TIME_ORDER, 1, 10) =" + 
				"                       TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"                 AND PRODUCT_LINE_NAME = '"+companyId+"'))  " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			map.put("result", NewUtils.returnTodayTotalDoubleMsgCpx("开工金额",  res[0], res[1], df));
		}
		
		return map;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpxYhYcsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select * from (" + 
				"select sum(SERVER_END_AMOUNT) 年预测收入" + 
				"      from T_COMP_AMOUNT_ALL_MONTH" + 
				"      where PRODUCT_LINE_NAME = '"+companyId+"')," + 
				"(SELECT NVL(SUM(SYS_CUR_TOTAL_AMOUNT), 0) 日预测收入" + 
				"           FROM T_ORDER_SERVER" + 
				"           WHERE SUBSTR(WORK_FINISH_TIME, 0, 10) =" + 
				"                 TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"'" + 
				"           AND DATA_STATE_ORDER <> '已删除'" + 
				"           AND DATA_STATE_ORDER <> '草稿'" + 
				"           AND AUDIT_STATE_ORDER <> '草稿')     " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			map.put("result", NewUtils.returnTodayTotalDoubleMsgCpx("预测收入",  res[0], res[1], df));
		}
		
		return map;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpxYhYwsr(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select * from (" + 
				"SELECT SUM(EX_TAX_PRICE) AS 年业务收入" + 
				"           FROM T_COMP_INVOICE_ALL" + 
				"           WHERE TYPE IN ('产品线业务')" + 
				"           AND VOID_TIME IS NULL" + 
				"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
				"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
				"           AND PRODUCT_LINE_NAME = '"+companyId+"')," + 
				"(SELECT SUM(EX_TAX_PRICE) AS 日业务收入" + 
				"           FROM T_COMP_INVOICE_ALL" + 
				"           WHERE TYPE IN ('产品线业务')" + 
				"           AND VOID_TIME IS NULL" + 
				"           AND SUBSTR(INVOICE_DATE, 0, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-dd')" + 
				"           and PRODUCT_LINE_NAME = '"+companyId+"')     " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			map.put("result", NewUtils.returnTodayTotalDoubleMsgCpx("收入总额",  res[0], res[1], df));
		}
		
		return map;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpxYhYwcb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select * from (SELECT 年业务成本, nvl(日业务成本, 0) 日业务成本" + 
				"           FROM (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT +" + 
				"                            APPORT_PAY_AMOUNT + CONTINU_PAYMENT_AMOUNT) AS 年业务成本" + 
				"                 FROM T_COMPANY_COST_ALL" + 
				"                 WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
				"                 AND PRODUCT_NAME IS NOT NULL" + 
				"                 AND PRODUCT_NAME = '"+companyId+"') A," + 
				"                (SELECT SUM(PAYMENT_AMOUNT + I_PAYMENT_AMOUNT +" + 
				"                            APPORT_PAY_AMOUNT + CONTINU_PAYMENT_AMOUNT) AS 日业务成本" + 
				"                 FROM T_COMPANY_COST_ALL" + 
				"                 WHERE SUBSTR(PAY_END_DATE, 0, 10) =" + 
				"                       TO_CHAR(SYSDATE, 'yyyy-mm-dd')" + 
				"                 AND PRODUCT_NAME IS NOT NULL" + 
				"                 AND PRODUCT_NAME = '"+companyId+"'))  " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			map.put("result", NewUtils.returnTodayTotalDoubleMsgCpx("业务成本",  res[0], res[1], df));
		}
		
		return map;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpxYhMll(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = " SELECT  case when 业务收入 is null or 业务收入=0 then 0 else (round((业务收入 - 业务成本) / 业务收入 * 100, 2)) end 毛利率 " + 
				"           FROM (SELECT nvl(SUM(EX_TAX_PRICE), 0) 业务收入" + 
				"                 FROM T_COMP_INVOICE_ALL" + 
				"                 WHERE VOID_TIME IS NULL" + 
				"                 AND TYPE = '产品线业务'" + 
				"                 and PRODUCT_LINE_NAME = '"+companyId+"'" + 
				"                 AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
				"                 AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')) A," + 
				"                (SELECT SUM(TOTAL_COST) AS 业务成本" + 
				"                 FROM T_COMPANY_COST_ALL" + 
				"                 WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')" + 
				"                 and PRODUCT_NAME = '"+companyId+"') B  " ;
    	System.out.println("----------"+sql.toString());
    	List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			BigDecimal res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			
			map.put("result", NewUtils.returnTodayTotalDoubleMsgCpx("毛利率",  res, null, df));
		}
		
		return map;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findSzskByCpxYhCb(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
		// TODO Auto-generated method stub
    	Map<String, String>reMap = fetchWebReq.getData();
		String companyId = reMap.get("companyId");
		String sql = "  select * from (select nvl(sum(direct_cost), 0) 年直接成本" + 
				"                 from T_COMPANY_COST_ALL" + 
				"                 where substr(pay_end_date, 0, 4) = to_char(sysdate, 'yyyy')" + 
				"                 and product_name = '"+companyId+"')," + 
				"(select nvl(sum(APPORT_PAY_AMOUNT), 0) 年间接成本" + 
				"                 from T_COMPANY_COST_ALL" + 
				"                 where substr(PAY_END_DATE, 0, 4) = to_char(sysdate, 'yyyy')" + 
				"                 and PRODUCT_NAME = '"+companyId+"')   " ;
    	System.out.println("----------"+sql.toString());
    	List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		if(resultList != null && resultList.size() > 0) {
			Object[] res = resultList.get(0);
			
			// 四舍五入保留2位小数
			DecimalFormat df = new DecimalFormat("#.00");
			JSONArray ja1 = new JSONArray();
			ja1.add(NewUtils.returnYwjiqkDoubleMsg1("直接成本", res[0], df));
			ja1.add(NewUtils.returnYwjiqkDoubleMsg1("间接成本", res[1], df));
			map.put("ttdData", ja1);
		}
		
		return map;
	}
	
	 @RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findSzByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
			// TODO Auto-generated method stub
	    	Map<String, String>reMap = fetchWebReq.getData();
			String companyId = reMap.get("companyId");
			String sql = "SELECT SUM(A.收入) OVER(ORDER BY A.月份) 收入," + 
					"       SUM(B.成本) OVER(ORDER BY A.月份) 成本," + 
					"       substr(A.月份, 7, 1)||'月'" + 
					"  FROM (SELECT SUM(EX_TAX_PRICE) AS 收入," + 
					"               SUBSTR(INVOICE_DATE, 1, 7) 月份" + 
					"          FROM T_COMP_INVOICE_ALL" + 
					"         WHERE TYPE IN ('产品线业务')" + 
					"           AND VOID_TIME IS NULL" + 
					"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY')" + 
					"           AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')" + 
					"           AND PRODUCT_LINE_NAME = '"+companyId+"'" + 
					"         GROUP BY PRODUCT_LINE_NAME, SUBSTR(INVOICE_DATE, 1, 7)) A" + 
					"  LEFT JOIN (SELECT SUM(TOTAL_COST) 成本, SUBSTR(PAY_END_DATE, 1, 7) 月份" + 
					"               FROM T_COMPANY_COST_ALL" + 
					"              WHERE PAY_END_DATE IS NOT NULL" + 
					"                AND PRODUCT_NAME = '"+companyId+"'" + 
					"              GROUP BY SUBSTR(PAY_END_DATE, 1, 7)) B" + 
					"    ON A.月份 = B.月份";
			System.out.println(sql);
	    	List<Object[]> reList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();	
			Map<String, Object> map = new HashMap<String, Object>();
			if(reList !=null && reList.size()!= 0 ) {
				JSONArray saceDate = new JSONArray();
				//x轴月份
				JSONArray yueX = new JSONArray();
				//收入
				JSONArray curYear = new JSONArray();
				//成本
				JSONArray lastYear = new JSONArray();
				//左y轴
				JSONObject leftY = new JSONObject();
				//y轴最大最小值
				JSONArray yAxsi = new JSONArray();
				
				Double leftYMin = 0d, leftYMax = 0d;
				for (int i = 0; i < reList.size(); i++) {
					Object[] ob = reList.get(i);
					if(ob == null ||ob.length == 0) {
						continue;
					}
					yueX.add(ob[2]);
					curYear.add(ob[0]== null?0:ob[0]);
					lastYear.add(ob[1]==null?0:ob[1]);
					if(ob[1] != null && ob [1] != "") { 
						if(leftYMin > Double.parseDouble(ob[1].toString())) {
							leftYMin = Double.parseDouble(ob[1].toString());
						}
						
						if(leftYMax < Double.parseDouble(ob[1].toString())) {
							leftYMax = Double.parseDouble(ob[1].toString());
						}
					}
					if(ob[0] != null && ob [0] != "") { 
						if(leftYMin > Double.parseDouble(ob[0].toString())) {
							leftYMin = Double.parseDouble(ob[0].toString());
						}
						
						if(leftYMax < Double.parseDouble(ob[0].toString())) {
							leftYMax = Double.parseDouble(ob[0].toString());
						}
					}
					
					
				}
				saceDate.add(curYear);
				saceDate.add(lastYear);
				leftY.put("min",leftYMin);
				leftY.put("max",leftYMax);
				yAxsi.add(leftY);
				map.put("seriesData",saceDate);
				map.put("xAxisData",yueX);
				map.put("yAxis",yAxsi);
			}
			return map;
		}
	 
	 @SuppressWarnings("unchecked")
	@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findFwjsByCpx(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
			// TODO Auto-generated method stub
	    	Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			String sql = "SELECT * " + 
					"  FROM (SELECT SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
					"               NVL(SUM(EX_TAX_PRICE), 0) 开票收入 " + 
					"          FROM T_COMP_INVOICE_ALL " + 
					"         WHERE VOID_TIME IS NULL " + 
					"           AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy') " + 
					"           AND SERVICE_MEDIUMCLASS_NAME IS NOT NULL " + 
					"        AND PRODUCT_LINE_NAME = '"+companyId+"' " + 
					"         GROUP BY SERVICE_MEDIUMCLASS_NAME " + 
					"         ORDER BY SUM(SYSTEM_TAX_PRICE) DESC) " + 
					" WHERE ROWNUM <= 5 " + 
					"UNION ALL " + 
					"SELECT '其他' AS 服务技术中类, NVL(SUM(开票收入), 0) 开票收入 " + 
					"  FROM (SELECT A.*, ROWNUM RN " + 
					"          FROM (SELECT SERVICE_MEDIUMCLASS_NAME 服务技术中类, " + 
					"                       SUM(EX_TAX_PRICE) 开票收入 " + 
					"                  FROM T_COMP_INVOICE_ALL " + 
					"                 WHERE VOID_TIME IS NULL " + 
					"                   AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy') " + 
					"                AND PRODUCT_LINE_NAME = '"+companyId+"' " + 
					"                 GROUP BY SERVICE_MEDIUMCLASS_NAME " + 
					"                 ORDER BY 开票收入 DESC) A) B " + 
					" WHERE RN > 5 ";
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();

			Map<String, Object> map = new HashMap<String, Object>();

			if (resultList != null && resultList.size() > 0) {
				JSONArray ja = NewUtils.returnSrpieCpx(resultList);
				map.put("tableData", ja);
			}

			return map;
		}
	 
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findCompanyMsgForChange(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" select * " + 
					"from (select tcaam.sorg_level2 a, " + 
					"             '父节点' as b, " + 
					"             tcaam.委托单量 c, " + 
					"             tcaam.年预测收入 d, " + 
					"             tcaam.收入总额 e, " + 
					"             tcaam.业务成本 f " + 
					"      from (select sorg_level2, " + 
					"                   sorg_level2_id, " + 
					"                   sum(0) 委托单量, " + 
					"                   sum(0) 年预测收入, " + 
					"                   sum(0) 收入总额, " + 
					"                   sum(0) 业务成本 " + 
					"            from T_COMP_AMOUNT_ALL_MONTH " + 
					"            where SORG_LEVEL2 not like '%参股%' " + 
					"            group by sorg_level2, sorg_level2_id) tcaam " + 
					"       " + 
					"      union " + 
					"      select tcaam.SORG_LEVEL2 a, " + 
					"             tcaam.SORG_LEVEL3 as b, " + 
					"             tcaam.委托单量 c, " + 
					"             tcaam.年预测收入 d, " + 
					"             tcaam.收入总额 e, " + 
					"             tcaam.业务成本 f " + 
					"      from (select SORG_LEVEL2, " + 
					"                   SORG_LEVEL3, " + 
					"                   SORG_LEVEL3_ID, " + 
					"                   sum(0) 委托单量, " + 
					"                   sum(0) 年预测收入, " + 
					"                   sum(0) 收入总额, " + 
					"                   sum(0) 业务成本 " + 
					"            from T_COMP_AMOUNT_ALL_MONTH " + 
					"            where SORG_LEVEL2 not like '%参股%' " + 
					"            group by SORG_LEVEL2, SORG_LEVEL3, SORG_LEVEL3_ID) tcaam " + 
					") aa " + 
					"order by aa.a asc, aa.d desc, aa.c desc " + 
					" ");
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
		public Map<String, Object> findYwskMsgSql1(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" select sum(委托单数量) 委托单数量, " + 
					"       sum(委托金额) 委托金额, " + 
					"       sum(开工数量) 开工数量, " + 
					"       sum(完工数量) 完工数量, " + 
					"       sum(预测收入) 预测收入, " + 
					"       sum(今日委托单数量) 今日委托单数量, " + 
					"       sum(今日委托金额) 今日委托金额, " + 
					"       sum(今日开工数量) 今日开工数量, " + 
					"       sum(今日完工数量) 今日完工数量, " + 
					"       sum(今日预测收入) 今日预测收入 " + 
					"  from (select sum(ORDER_CREATE_COUNT) 委托单数量, " + 
					"               sum(ORDER_CREATE_AMOUNT) 委托金额, " + 
					"               sum(ORDER_START_COUNT) 开工数量, " + 
					"               sum(ORDER_END_COUNT) 完工数量, " + 
					"               sum(SERVER_END_AMOUNT) 预测收入, " + 
					"               0 as 今日委托单数量, " + 
					"               0 as 今日委托金额, " + 
					"               0 as 今日开工数量, " + 
					"               0 as 今日完工数量, " + 
					"               0 as 今日预测收入 " + 
					"          from T_COMP_AMOUNT_ALL_MONTH " +
					"          where SORG_LEVEL3 = '"+companyId+"'  and substr(data_stats_month,0,4) = to_char(sysdate,'yyyy') " +
					"        union all " + 
					"        select 0 as 委托单数量, " + 
					"               0 as 委托金额, " + 
					"               0 as 开工数量, " + 
					"               0 as 完工数量, " + 
					"               0 as 预测收入, " + 
					"               sum(ORDER_CREATE_COUNT) 今日委托单数量, " + 
					"               sum(ORDER_CREATE_AMOUNT) 今日委托金额, " + 
					"               sum(ORDER_START_COUNT) 今日开工数量, " + 
					"               sum(ORDER_END_COUNT) 今日完工数量, " + 
					"               sum(SERVER_END_AMOUNT) 今日预测收入 " + 
					"          from T_COMP_AMOUNT_ALL_DAY " +
					"          where SORG_LEVEL3 = '"+companyId+"'  ) ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
				
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONArray ja = NewUtils.returnYwskMsgSql1(resultList);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql2(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append("  SELECT  " + 
					"  case " + 
					"    when 年业务收入 is null or 年业务收入=0 then 0 " + 
					"    else round((年业务收入 - 业务成本) / 年业务收入 * 100, 2)    " + 
					"  end 产品线毛利率 " + 
					" FROM (SELECT nvl(SUM(EX_TAX_PRICE), 0) 年业务收入 " + 
					"       FROM T_COMP_INVOICE_ALL " + 
					"       WHERE VOID_TIME IS NULL " + 
					"       and SORG_LEVEL3 = '"+companyId+"' " + 
					"       AND TYPE = '产品线业务' " + 
					"       AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
					"       AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')) A, " + 
					"      (SELECT SUM(TOTAL_COST) AS 业务成本 " + 
					"       FROM T_COMPANY_COST_ALL " + 
					"       WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy') " + 
					"       and SORG_LEVEL3 = '"+companyId+"') B " + 
					" ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql2(resultList);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" SELECT SUM(年客户数量) 年客户数量, " + 
					"       SUM(NVL(日客户数量, 0)) 日客户数量, " + 
					"       SUM(年客户数量 - NVL(非产品线活跃客户, 0)) 产品线活跃客户数量, " + 
					"       SUM(NVL(非产品线活跃客户, 0)) 非产品线活跃客户, " + 
					"       SUM(NVL(年客户数量, 0)) 活跃客户数量, " + 
					"       A.SORG_LEVEL3 " + 
					"  FROM (SELECT SORG_LEVEL3, COUNT(DISTINCT CUSTOMER_NAME) 年客户数量 " + 
					"          FROM T_ORDER_CRM " + 
					"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"               TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"           AND SORG_LEVEL3 IS NOT NULL " + 
					"         GROUP BY SORG_LEVEL3) A " + 
					"  LEFT JOIN (SELECT SORG_LEVEL3, COUNT(DISTINCT CUSTOMER_NAME) 日客户数量 " + 
					"               FROM T_ORDER_CRM " + 
					"              WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
					"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"              GROUP BY SORG_LEVEL3) B " + 
					"    ON A.SORG_LEVEL3 = B.SORG_LEVEL3 " + 
					"  LEFT JOIN (SELECT COUNT(CUSTOMER_NAME) 非产品线活跃客户, SORG_LEVEL3 " + 
					"               FROM (SELECT DISTINCT CUSTOMER_NAME, SORG_LEVEL3 " + 
					"                       FROM T_ORDER_CRM T1 " + 
					"                      WHERE TYPE <> '产品线业务') " + 
					"              GROUP BY SORG_LEVEL3) D " + 
					"    ON A.SORG_LEVEL3 = D.SORG_LEVEL3 " + 
					" WHERE A.SORG_LEVEL3 = '"+companyId+"' " + 
					" GROUP BY A.SORG_LEVEL3 ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
				
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql3(resultList,1);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql4(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" SELECT SUM(年供应商数量) 供应商数量, " + 
					"       SUM(业务供应商数量 - NVL(非产品线活跃供应商, 0)) 产品线活跃供应商数量, " + 
					"       SUM(NVL(非产品线活跃供应商, 0)) 非产品线活跃供应商, " + 
					"       SUM(NVL(日供应商数量, 0)) 日供应商数量, " + 
					"       A.SORG_LEVEL3 " + 
					"  FROM (SELECT SORG_LEVEL3, COUNT(DISTINCT SUB_PACKAGE_NAME) 年供应商数量 " + 
					"          FROM T_ORDER_SRM " + 
					"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"               TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"           AND SORG_LEVEL3 IS NOT NULL " + 
					"         GROUP BY SORG_LEVEL3) A " + 
					"  LEFT JOIN (SELECT SORG_LEVEL3, " + 
					"                    COUNT(DISTINCT SUB_PACKAGE_NAME) 日供应商数量 " + 
					"               FROM T_ORDER_SRM " + 
					"              WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
					"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"              GROUP BY SORG_LEVEL3) B " + 
					"    ON A.SORG_LEVEL3 = B.SORG_LEVEL3 " + 
					"  LEFT JOIN (SELECT SORG_LEVEL3, " + 
					"                    COUNT(DISTINCT SUB_PACKAGE_ID) 业务供应商数量 " + 
					"               FROM T_ORDER_SRM " + 
					"              WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"                    TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"              GROUP BY SORG_LEVEL3) C " + 
					"    ON A.SORG_LEVEL3 = C.SORG_LEVEL3 " + 
					"  LEFT JOIN (SELECT COUNT(SUB_PACKAGE_NAME) 非产品线活跃供应商, SORG_LEVEL3 " + 
					"               FROM (SELECT DISTINCT SUB_PACKAGE_NAME, SORG_LEVEL3 " + 
					"                       FROM T_ORDER_SRM T1 " + 
					"                      WHERE TYPE <> '产品线业务') " + 
					"              GROUP BY SORG_LEVEL3) D " + 
					"    ON A.SORG_LEVEL3 = D.SORG_LEVEL3 " + 
					"WHERE A.SORG_LEVEL3 = '"+companyId+"' " + 
					" GROUP BY A.SORG_LEVEL3 " + 
					" ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql4(resultList,1);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql5(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" SELECT 年出证数量,今日出证数量, " + 
					"       \"纸质报告+纸质证书\"+\"电子报告+电子证书\", " + 
					"       \"其他工作成果物\" " + 
					"        " + 
					"  FROM (SELECT COUNT(MID) AS 年出证数量, " + 
					"               SUM(CASE " + 
					"                     WHEN CERTTYPE IN ('纸质报告', '纸质证书') THEN " + 
					"                      1 " + 
					"                     ELSE " + 
					"                      0 " + 
					"                   END) AS \"纸质报告+纸质证书\", " + 
					"               SUM(CASE " + 
					"                     WHEN CERTTYPE IN ('电子报告', '电子证书') THEN " + 
					"                      1 " + 
					"                     ELSE " + 
					"                      0 " + 
					"                   END) AS \"电子报告+电子证书\", " + 
					"               SUM(CASE " + 
					"                     WHEN CERTTYPE = '其他工作成果物' THEN " + 
					"                      1 " + 
					"                     ELSE " + 
					"                      0 " + 
					"                   END) AS \"其他工作成果物\" " + 
					"          FROM T_ORDER_CERC WHERE SUBSTR(FICTIONAL_DATE,1,4) = to_char(SYSDATE,'YYYY') " + 
					"         and sorg_level3 ='"+companyId+"'), " + 
					"       (SELECT COUNT(MID) AS 今日出证数量 " + 
					"          FROM T_ORDER_CERC " + 
					"         WHERE SUBSTR(FICTIONAL_DATE, 1, 10) =TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"               and sorg_level3 ='"+companyId+"') ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql5(resultList);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWtdl(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT * from (SELECT * " + 
					"      FROM (SELECT COUNT(年委托单量) 年委托单量 " + 
					"            FROM (SELECT COUNT(1) 年委托单量 " + 
					"                  FROM T_ORDER_SERVER " + 
					"                  WHERE SUBSTR(CREATE_TIME_ORDER, 1, 4) = " + 
					"                        TO_CHAR(SYSDATE, 'YYYY') " + 
					"                  GROUP BY ORDER_UUID))) A, " + 
					"     (SELECT COUNT(日委托单量) 日委托单量 " + 
					"      FROM (SELECT COUNT(1) 日委托单量 " + 
					"            FROM T_ORDER_SERVER " + 
					"            WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
					"                  TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"            GROUP BY ORDER_UUID)) B ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql1Jt(resultList, "委托单量");
				map.put("result", ja);
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWtdlYear(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT COUNT(年委托单量) 年委托单量 " + 
					"              FROM (SELECT COUNT(*) 年委托单量 " + 
					"                    FROM T_ORDER_SERVER " + 
					"                    WHERE SUBSTR(CREATE_TIME_ORDER, 1, 4) = " + 
					"                          TO_CHAR(SYSDATE, 'YYYY') " + 
					"                    GROUP BY ORDER_UUID) ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 2));
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWtdlDay(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT COUNT(日委托单量) 日委托单量" + 
					"        FROM (SELECT COUNT(1) 日委托单量" + 
					"              FROM T_ORDER_SERVER" + 
					"              WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) =" + 
					"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD')" + 
					"              GROUP BY ORDER_UUID) ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result","+"+NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtKgdl(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT 年开工单量, 日开工单量 " + 
					"           FROM (SELECT COUNT(日开工单量) 日开工单量 " + 
					"                 FROM (SELECT ORDER_UUID 日开工单量 " + 
					"                       FROM T_ORDER_SERVER " + 
					"                       WHERE SUBSTR(JOB_START_TIME_ORDER, 1, 10) = " + 
					"                             TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"                       AND JOB_START_TIME_ORDER IS NOT NULL " + 
					"                       GROUP BY ORDER_UUID)) A, " + 
					"                (SELECT COUNT(年开工单量) 年开工单量 " + 
					"                 FROM (SELECT ORDER_UUID 年开工单量 " + 
					"                       FROM T_ORDER_SERVER " + 
					"                       WHERE SUBSTR(JOB_START_TIME_ORDER, 1, 4) = " + 
					"                             TO_CHAR(SYSDATE, 'YYYY') " + 
					"                       AND JOB_START_TIME_ORDER IS NOT NULL " + 
					"                       GROUP BY ORDER_UUID)) B ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql1Jt(resultList, "开工单量");
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtKgdlYear(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT COUNT(年开工单量) 年开工单量 " + 
					"       FROM (SELECT ORDER_UUID 年开工单量 " + 
					"             FROM T_ORDER_SERVER " + 
					"             WHERE SUBSTR(JOB_START_TIME_ORDER, 1, 4) = " + 
					"                   TO_CHAR(SYSDATE, 'YYYY') " + 
					"             AND JOB_START_TIME_ORDER IS NOT NULL " + 
					"             GROUP BY ORDER_UUID) ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 2));
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtKgdlDay(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT COUNT(日开工单量) 日开工单量 " + 
					"       FROM (SELECT ORDER_UUID 日开工单量 " + 
					"             FROM T_ORDER_SERVER " + 
					"             WHERE SUBSTR(JOB_START_TIME_ORDER, 1, 10) = " + 
					"                   TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"             AND JOB_START_TIME_ORDER IS NOT NULL " + 
					"             GROUP BY ORDER_UUID) ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result","+"+NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWgdl(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT 年完工单量, 日完工单量 " + 
					"           FROM (SELECT COUNT(日完工单量) 日完工单量 " + 
					"                 FROM (SELECT ORDER_UUID 日完工单量 " + 
					"                       FROM T_ORDER_SERVER " + 
					"                       WHERE SUBSTR(JOB_END_TIME_ORDER, 1, 10) = " + 
					"                             TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"                       AND JOB_END_TIME_ORDER IS NOT NULL " + 
					"                       GROUP BY ORDER_UUID)) A, " + 
					"                (SELECT COUNT(年完工单量) 年完工单量 " + 
					"                 FROM (SELECT ORDER_UUID 年完工单量 " + 
					"                       FROM T_ORDER_SERVER " + 
					"                       WHERE SUBSTR(JOB_END_TIME_ORDER, 1, 4) = " + 
					"                             TO_CHAR(SYSDATE, 'YYYY') " + 
					"                       GROUP BY ORDER_UUID)) B ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql1Jt(resultList, "完工单量");
				map.put("result", ja);
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWgdlYear(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT COUNT(年完工单量) 年完工单量 " + 
					"       FROM (SELECT ORDER_UUID 年完工单量 " + 
					"             FROM T_ORDER_SERVER " + 
					"             WHERE SUBSTR(JOB_END_TIME_ORDER, 1, 4) = " + 
					"                   TO_CHAR(SYSDATE, 'YYYY') " + 
					"             GROUP BY ORDER_UUID) ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWgdlDay(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT COUNT(日完工单量) 日完工单量 " + 
					"       FROM (SELECT ORDER_UUID 日完工单量 " + 
					"             FROM T_ORDER_SERVER " + 
					"             WHERE SUBSTR(JOB_END_TIME_ORDER, 1, 10) = " + 
					"                   TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"             AND JOB_END_TIME_ORDER IS NOT NULL " + 
					"             GROUP BY ORDER_UUID) ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result","+"+NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWtje(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT 年委托金额, NVL(日委托金额, 0) 日委托金额  " + 
					"  FROM (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年委托金额 " + 
					"          FROM T_ORDER_SERVER " + 
					"         WHERE SUBSTR(CREATE_TIME_ORDER, 1, 4) = TO_CHAR(SYSDATE, 'YYYY')) A, " + 
					"       (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日委托金额 " + 
					"          FROM T_ORDER_SERVER " + 
					"         WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
					"               TO_CHAR(SYSDATE, 'YYYY-mm-dd')) B");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql1Jt(resultList, "委托金额");
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWtjeYear(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年委托金额 " + 
					"       FROM T_ORDER_SERVER " + 
					"       WHERE SUBSTR(CREATE_TIME_ORDER, 1, 4) = TO_CHAR(SYSDATE, 'YYYY')");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtWtjeDay(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日委托金额 " + 
					"       FROM T_ORDER_SERVER " + 
					"       WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
					"             TO_CHAR(SYSDATE, 'YYYY-mm-dd') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result","+"+NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtYcsr(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT 年预测收入, 日预测收入 " + 
					"           FROM (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年预测收入 " + 
					"                 FROM T_ORDER_SERVER " + 
					"                 WHERE SUBSTR(WORK_FINISH_TIME, 1, 4) = " + 
					"                       TO_CHAR(SYSDATE, 'YYYY')) A, " + 
					"                (SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日预测收入 " + 
					"                 FROM T_ORDER_SERVER " + 
					"                 WHERE SUBSTR(WORK_FINISH_TIME, 1, 10) = " + 
					"                       TO_CHAR(SYSDATE, 'YYYY-mm-dd')) B ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql1Jt(resultList, "预测收入");
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtYcsrYear(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 年预测收入 " + 
					"       FROM T_ORDER_SERVER " + 
					"       WHERE SUBSTR(WORK_FINISH_TIME, 1, 4) = TO_CHAR(SYSDATE, 'YYYY') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1JtYcsrDay(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT SUM(SYS_CUR_TOTAL_AMOUNT) 日预测收入 " + 
					"       FROM T_ORDER_SERVER " + 
					"       WHERE SUBSTR(WORK_FINISH_TIME, 1, 10) = " + 
					"             TO_CHAR(SYSDATE, 'YYYY-mm-dd') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql2Jt(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT  " + 
					"  case " + 
					"    when 年业务收入 is null or 年业务收入 = 0 then 0 " + 
					"    else round((年业务收入 - 业务成本) / 年业务收入 * 100, 2)   " + 
					"  end 产品线毛利率  " + 
					" FROM (SELECT nvl(SUM(EX_TAX_PRICE), 0) 年业务收入 " + 
					"       FROM T_COMP_INVOICE_ALL " + 
					"       WHERE VOID_TIME IS NULL " + 
					"       AND TYPE = '产品线业务' " + 
					"       AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
					"       AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM')) A, " + 
					"      (SELECT SUM(TOTAL_COST) AS 业务成本 " + 
					"       FROM T_COMPANY_COST_ALL " + 
					"       WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy')) B " + 
					" ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql2(resultList);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3Jt(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append("  SELECT 年客户数量, " + 
					"       NVL(日客户数量, 0) 日客户数量, " + 
					"       产品线活跃客户数量, " + 
					"       NVL(非产品线活跃客户, 0) 非产品线活跃客户, " + 
					"       0, " + 
					"       年客户数量 - 活跃客户数量 非活跃客户数量 " + 
					"  FROM (SELECT COUNT(DISTINCT CUSTOMER_NAME) 年客户数量 " + 
					"          FROM VIEW_CUSTOMER_ALL) A, " + 
					"       (SELECT COUNT(DISTINCT CUSTOMER_NAME) 活跃客户数量 FROM T_ORDER_CRM) B, " + 
					"       (SELECT COUNT(DISTINCT CUSTOMER_NAME) 日客户数量 " + 
					"          FROM VIEW_CUSTOMER_ALL " + 
					"         WHERE SUBSTR(CREATE_TIME, 1, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-DD')) C, " + 
					"       (SELECT COUNT(DISTINCT CUSTOMER_NAME) 产品线活跃客户数量 " + 
					"          FROM T_ORDER_CRM " + 
					"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"               TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"           AND TYPE = '产品线业务') D, " + 
					"       (SELECT COUNT(CUSTOMER_NAME) 非产品线活跃客户 " + 
					"          FROM (SELECT DISTINCT CUSTOMER_NAME " + 
					"                  FROM T_ORDER_CRM T1 " + 
					"                 WHERE TYPE <> '产品线业务')) E ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql3(resultList,0);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3JtKhslYear(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT COUNT(DISTINCT CUSTOMER_NAME) 年客户数量 " + 
					"        FROM VIEW_CUSTOMER_ALL ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3JtKhslDay(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT COUNT(DISTINCT CUSTOMER_NAME) 日客户数量 " + 
					"        FROM VIEW_CUSTOMER_ALL " + 
					"        WHERE SUBSTR(CREATE_TIME, 1, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-DD') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3JtKhslCpxhy(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT COUNT(DISTINCT CUSTOMER_NAME) 产品线活跃客户数量 " + 
					"        FROM T_ORDER_CRM " + 
					"        WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"              TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"        AND TYPE = '产品线业务' ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3JtKhslFcpxhy(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("  SELECT COUNT(CUSTOMER_NAME) 非产品线活跃客户 " + 
					"        FROM (SELECT DISTINCT CUSTOMER_NAME " + 
					"              FROM T_ORDER_CRM T1 " + 
					"              WHERE TYPE <> '产品线业务') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3JtKhslFhy(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append("    SELECT 年客户数量 - 活跃客户数量 非活跃客户数量 " + 
					"  FROM (SELECT COUNT(DISTINCT CUSTOMER_NAME) 年客户数量 " + 
					"        FROM VIEW_CUSTOMER_ALL) A, " + 
					"       (SELECT COUNT(DISTINCT CUSTOMER_NAME) 活跃客户数量 FROM T_ORDER_CRM) B");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYwjiqkIntMsg1("非活跃客户数量", resultList.get(0), new DecimalFormat("#.00")));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql4Jt(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" SELECT 年供应商数量 年供应商数量, " + 
					"       活跃供应商数量 - NVL(非产品线活跃供应商, 0) 产品线活跃供应商数量, " + 
					"       NVL(非产品线活跃供应商, 0) 非产品线活跃供应商, " + 
					"       NVL(日供应商数量, 0) 日供应商数量, " + 
					"       年供应商数量 - 活跃供应商数量 非活跃供应商数量 " + 
					"FROM (SELECT COUNT(DISTINCT NAME) 年供应商数量 FROM INFOSHAR_618389493) A, " + 
					"     (SELECT COUNT(DISTINCT NAME) 日供应商数量 " + 
					"      FROM INFOSHAR_618389493 " + 
					"      WHERE SUBSTR(START_TIME, 1, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-DD')) B, " + 
					"     (SELECT COUNT(DISTINCT SUB_PACKAGE_ID) 活跃供应商数量 " + 
					"      FROM T_ORDER_SRM " + 
					"      WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"            TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm')) C, " + 
					"     (SELECT COUNT(SUB_PACKAGE_NAME) 非产品线活跃供应商, SORG_LEVEL3 " + 
					"      FROM (SELECT DISTINCT SUB_PACKAGE_NAME, SORG_LEVEL3 " + 
					"            FROM T_ORDER_SRM T1 " + 
					"            WHERE TYPE <> '产品线业务')) D " + 
					" ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql4(resultList,0);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql5Jt(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" SELECT 年出证数量,今日出证数量, " + 
					"                  \"纸质报告+纸质证书\" + \"电子报告+电子证书\", " + 
					"                  \"其他工作成果物\" " + 
					"           FROM (SELECT COUNT(MID) AS 年出证数量, " + 
					"                        SUM(CASE " + 
					"                              WHEN CERTTYPE IN ('纸质报告', '纸质证书') THEN " + 
					"                               1 " + 
					"                              ELSE " + 
					"                               0 " + 
					"                            END) AS \"纸质报告+纸质证书\", " + 
					"                        SUM(CASE " + 
					"                              WHEN CERTTYPE IN ('电子报告', '电子证书') THEN " + 
					"                               1 " + 
					"                              ELSE " + 
					"                               0 " + 
					"                            END) AS \"电子报告+电子证书\", " + 
					"                        SUM(CASE " + 
					"                              WHEN CERTTYPE = '其他工作成果物' THEN " + 
					"                               1 " + 
					"                              ELSE " + 
					"                               0 " + 
					"                            END) AS \"其他工作成果物\" " + 
					"                 FROM T_ORDER_CERC " + 
					"                 WHERE SUBSTR(FICTIONAL_DATE, 1, 4) = " + 
					"                       to_char(SYSDATE, 'YYYY')), " + 
					"                (SELECT COUNT(MID) AS 今日出证数量 " + 
					"                 FROM T_ORDER_CERC " + 
					"                 WHERE SUBSTR(FICTIONAL_DATE, 1, 10) = " + 
					"                       TO_CHAR(SYSDATE, 'YYYY-MM-DD')) ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql5(resultList);
				map.put("result", ja);
			}
			
			return map;
		}

		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql5JtCzslYear(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT COUNT(MID) AS 年出证数量 " + 
					"       FROM T_ORDER_CERC " + 
					"       WHERE SUBSTR(FICTIONAL_DATE, 1, 4) = to_char(SYSDATE, 'YYYY') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql5JtCzslDay(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT COUNT(MID) AS 今日出证数量 " + 
					"       FROM T_ORDER_CERC " + 
					"       WHERE SUBSTR(FICTIONAL_DATE, 1, 10) = TO_CHAR(SYSDATE, 'YYYY-MM-DD') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result","+"+NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql5JtCzslZssl(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT SUM(CASE " + 
					"                    WHEN CERTTYPE IN ('纸质报告', '纸质证书') THEN " + 
					"                     1 " + 
					"                    ELSE " + 
					"                     0 " + 
					"                  END) + " + 
					"              SUM(CASE " + 
					"                    WHEN CERTTYPE IN ('电子报告', '电子证书') THEN " + 
					"                     1 " + 
					"                    ELSE " + 
					"                     0 " + 
					"                  END) AS \"出证数量\" " + 
					"       FROM T_ORDER_CERC " + 
					"       WHERE SUBSTR(FICTIONAL_DATE, 1, 4) = to_char(SYSDATE, 'YYYY') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql5JtCzslQtcgw(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			
			sql.append(" SELECT SUM(CASE " + 
					"                    WHEN CERTTYPE = '其他工作成果物' THEN " + 
					"                     1 " + 
					"                    ELSE " + 
					"                     0 " + 
					"                  END) AS \"其他工作成果物\" " + 
					"       FROM T_ORDER_CERC " + 
					"       WHERE SUBSTR(FICTIONAL_DATE, 1, 4) = to_char(SYSDATE, 'YYYY') ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("result",NewUtils.returnYiOrWanWtdlInt(resultList.get(0), 0));
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql1Quyu(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" select sum(委托单数量) 委托单数量, " + 
					"       sum(委托金额) 委托金额, " + 
					"       sum(开工数量) 开工数量, " + 
					"       sum(完工数量) 完工数量, " + 
					"       sum(预测收入) 预测收入, " + 
					"       sum(今日委托单数量) 今日委托单数量, " + 
					"       sum(今日委托金额) 今日委托金额, " + 
					"       sum(今日开工数量) 今日开工数量, " + 
					"       sum(今日完工数量) 今日完工数量, " + 
					"       sum(今日预测收入) 今日预测收入 " + 
					"  from (select sum(ORDER_CREATE_COUNT) 委托单数量, " + 
					"               sum(ORDER_CREATE_AMOUNT) 委托金额, " + 
					"               sum(ORDER_START_COUNT) 开工数量, " + 
					"               sum(ORDER_END_COUNT) 完工数量, " + 
					"               sum(SERVER_END_AMOUNT) 预测收入, " + 
					"               0 as 今日委托单数量, " + 
					"               0 as 今日委托金额, " + 
					"               0 as 今日开工数量, " + 
					"               0 as 今日完工数量, " + 
					"               0 as 今日预测收入 " + 
					"          from T_COMP_AMOUNT_ALL_MONTH " + 
					"			where BUSINESS_REGION = '"+companyId+"' and substr(data_stats_month,0,4) = to_char(sysdate,'yyyy') " +
					"        union all " + 
					"        select 0 as 委托单数量, " + 
					"               0 as 委托金额, " + 
					"               0 as 开工数量, " + 
					"               0 as 完工数量, " + 
					"               0 as 预测收入, " + 
					"               sum(ORDER_CREATE_COUNT) 今日委托单数量, " + 
					"               sum(ORDER_CREATE_AMOUNT) 今日委托金额, " + 
					"               sum(ORDER_START_COUNT) 今日开工数量, " + 
					"               sum(ORDER_END_COUNT) 今日完工数量, " + 
					"               sum(SERVER_END_AMOUNT) 今日预测收入 " + 
					"          from T_COMP_AMOUNT_ALL_DAY " +
					"			where BUSINESS_REGION = '"+companyId+"'  ) ");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONArray ja = NewUtils.returnYwskMsgSql1(resultList);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql2Quyu(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" SELECT case when 年业务收入 is null or 年业务收入=0 then 0 else round((年业务收入 - 业务成本) / 年业务收入*100,2) end 产品线毛利率 " + 
					"            FROM (SELECT  nvl(SUM(EX_TAX_PRICE),0) 年业务收入 " + 
					"                    FROM T_COMP_INVOICE_ALL " + 
					"                   WHERE VOID_TIME IS NULL and BUSINESS_REGION ='"+companyId+"' " + 
					"                     AND TYPE = '产品线业务' " + 
					"                     AND SUBSTR(INVOICE_DATE, 0, 4) = TO_CHAR(SYSDATE, 'YYYY') " + 
					"                     AND SUBSTR(INVOICE_DATE, 0, 7) <= TO_CHAR(SYSDATE, 'YYYY-MM') " + 
					"                   ) A, " + 
					"           (SELECT SUM(TOTAL_COST) AS 业务成本 " + 
					"              FROM T_COMPANY_COST_ALL " + 
					"          WHERE SUBSTR(PAY_END_DATE, 0, 4) = TO_CHAR(SYSDATE, 'yyyy') and BUSINESS_REGION ='"+companyId+"' " + 
					"          )B ");
			System.out.println(sql.toString());
			List<BigDecimal> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql2(resultList);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql3Quyu(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append(" " + 
					"SELECT SUM(年客户数量) 年客户数量, " + 
					"       SUM(NVL(日客户数量, 0)) 日客户数量, " + 
					"       SUM(年客户数量 - NVL(非产品线活跃客户, 0)) 产品线活跃客户数量, " + 
					"       SUM(NVL(非产品线活跃客户, 0)) 非产品线活跃客户, " + 
					"       SUM(NVL(年客户数量, 0)) 活跃客户数量, " + 
					"       A.BUSINESS_REGION  " + 
					"  FROM (SELECT BUSINESS_REGION, COUNT(DISTINCT CUSTOMER_NAME) 年客户数量 " + 
					"          FROM T_ORDER_CRM " + 
					"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"               TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"           AND BUSINESS_REGION IS NOT NULL " + 
					"         GROUP BY BUSINESS_REGION) A " + 
					"  LEFT JOIN (SELECT BUSINESS_REGION, COUNT(DISTINCT CUSTOMER_NAME) 日客户数量 " + 
					"               FROM T_ORDER_CRM " + 
					"              WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
					"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"              GROUP BY BUSINESS_REGION) B " + 
					"    ON A.BUSINESS_REGION = B.BUSINESS_REGION " + 
					"  LEFT JOIN (SELECT COUNT(CUSTOMER_NAME) 非产品线活跃客户, BUSINESS_REGION " + 
					"               FROM (SELECT DISTINCT CUSTOMER_NAME, BUSINESS_REGION " + 
					"                       FROM T_ORDER_CRM T1 " + 
					"                      WHERE TYPE <> '产品线业务') " + 
					"              GROUP BY BUSINESS_REGION) D " + 
					"    ON A.BUSINESS_REGION = D.BUSINESS_REGION " + 
					" WHERE A.BUSINESS_REGION = '"+companyId+"' " + 
					" GROUP BY A.BUSINESS_REGION");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql3(resultList,3);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql4Quyu(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append("  SELECT SUM(年供应商数量) 供应商数量, " + 
					"       SUM(业务供应商数量 - NVL(非产品线活跃供应商, 0)) 产品线活跃供应商数量, " + 
					"       SUM(NVL(非产品线活跃供应商, 0)) 非产品线活跃供应商, " + 
					"       SUM(NVL(日供应商数量, 0)) 日供应商数量, " + 
					"       A.BUSINESS_REGION " + 
					"  FROM (SELECT BUSINESS_REGION, COUNT(DISTINCT SUB_PACKAGE_NAME) 年供应商数量 " + 
					"          FROM T_ORDER_SRM " + 
					"         WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"               TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"           AND BUSINESS_REGION IS NOT NULL " + 
					"         GROUP BY BUSINESS_REGION) A " + 
					"  LEFT JOIN (SELECT BUSINESS_REGION, " + 
					"                    COUNT(DISTINCT SUB_PACKAGE_NAME) 日供应商数量 " + 
					"               FROM T_ORDER_SRM " + 
					"              WHERE SUBSTR(CREATE_TIME_ORDER, 1, 10) = " + 
					"                    TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"              GROUP BY BUSINESS_REGION) B " + 
					"    ON A.BUSINESS_REGION = B.BUSINESS_REGION " + 
					"  LEFT JOIN (SELECT BUSINESS_REGION, " + 
					"                    COUNT(DISTINCT SUB_PACKAGE_ID) 业务供应商数量 " + 
					"               FROM T_ORDER_SRM " + 
					"              WHERE SUBSTR(CREATE_TIME_ORDER, 0, 7) >= " + 
					"                    TO_CHAR(ADD_MONTHS(SYSDATE, -12), 'yyyy-mm') " + 
					"              GROUP BY BUSINESS_REGION) C " + 
					"    ON A.BUSINESS_REGION = C.BUSINESS_REGION " + 
					"  LEFT JOIN (SELECT COUNT(SUB_PACKAGE_NAME) 非产品线活跃供应商, BUSINESS_REGION " + 
					"               FROM (SELECT DISTINCT SUB_PACKAGE_NAME, BUSINESS_REGION " + 
					"                       FROM T_ORDER_SRM T1 " + 
					"                      WHERE TYPE <> '产品线业务') " + 
					"              GROUP BY BUSINESS_REGION) D " + 
					"    ON A.BUSINESS_REGION = D.BUSINESS_REGION " + 
					"WHERE A.BUSINESS_REGION = '"+companyId+"' " + 
					" GROUP BY A.BUSINESS_REGION");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql4(resultList,3);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findYwskMsgSql5Quyu(FetchWebRequest<Map<String, String>> fetchWebReq)
				throws Exception {
			StringBuffer sql = new StringBuffer();
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			
			sql.append("  SELECT 年出证数量,今日出证数量, " + 
					"       \"纸质报告+纸质证书\"+\"电子报告+电子证书\", " + 
					"       \"其他工作成果物\" " + 
					"        " + 
					"  FROM (SELECT COUNT(MID) AS 年出证数量, " + 
					"               SUM(CASE " + 
					"                     WHEN CERTTYPE IN ('纸质报告', '纸质证书') THEN " + 
					"                      1 " + 
					"                     ELSE " + 
					"                      0 " + 
					"                   END) AS \"纸质报告+纸质证书\", " + 
					"               SUM(CASE " + 
					"                     WHEN CERTTYPE IN ('电子报告', '电子证书') THEN " + 
					"                      1 " + 
					"                     ELSE " + 
					"                      0 " + 
					"                   END) AS \"电子报告+电子证书\", " + 
					"               SUM(CASE " + 
					"                     WHEN CERTTYPE = '其他工作成果物' THEN " + 
					"                      1 " + 
					"                     ELSE " + 
					"                      0 " + 
					"                   END) AS \"其他工作成果物\" " + 
					"          FROM T_ORDER_CERC WHERE SUBSTR(FICTIONAL_DATE,1,4) = to_char(SYSDATE,'YYYY') " + 
					"         and business_region='"+companyId+"'), " + 
					"       (SELECT COUNT(MID) AS 今日出证数量 " + 
					"          FROM T_ORDER_CERC " + 
					"         WHERE SUBSTR(FICTIONAL_DATE, 1, 10) =TO_CHAR(SYSDATE, 'YYYY-MM-DD') " + 
					"               and business_region='"+companyId+"')");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = NewUtils.returnYwskMsgSql5(resultList);
				map.put("result", ja);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findJtgsSrbar(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
			StringBuffer sql = new StringBuffer();
			// 获取当前公司id
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			sql.append(" select * " + 
					"  from (select GOODS_MEDIUMCLASS_NAME 服务对象中类, " + 
					"               sum(EX_tax_price) 开票收入 " + 
					"          from T_COMP_INVOICE_ALL " + 
					"         where VOID_TIME IS NULL " + 
					"           AND substr(invoice_date,0,4)=to_char(sysdate,'yyyy') " + 
					"           and GOODS_MEDIUMCLASS_NAME is not null " + 
					"           and PRODUCT_LINE_NAME='"+companyId+"' " + 
					"         group by GOODS_MEDIUMCLASS_NAME " + 
					"         order by sum(EX_tax_price) desc) " + 
					" where rownum <= 5 " + 
					"       order by 开票收入 desc");
			System.out.println(sql.toString());
			List<Object[]> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
				
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				JSONObject ja = Util.returnJtgsSrbarOption(resultList);
				
				map.put("srbarOption", ja);
			}else {
				JSONObject jo = new JSONObject();
				JSONObject xAxis = new JSONObject();
				
				xAxis.put("min", 0);
				xAxis.put("max", 0);
				
				jo.put("xAxis", xAxis);
				jo.put("yData", new JSONArray());
				
				jo.put("sdata", new JSONArray());
				
				map.put("srbarOption", jo);
			}
			
			return map;
		}
		
		@RpcMethod(loginValidate = false)
		@Override
		public Map<String, Object> findKhmcByOrgId(FetchWebRequest<Map<String, String>> fetchWebReq) throws Exception {
			StringBuffer sql = new StringBuffer();
			// 获取当前公司id
			Map<String, String> resMap = fetchWebReq.getData();
			String companyId = resMap.get("companyId");
			sql.append(" select sorg_fullname from T_ORG_TREE where x_unifycode = '"+companyId+"'");
			System.out.println(sql.toString());
			List<String> resultList = DBManager.get("kabBan").createNativeQuery(sql.toString()).getResultList();
			
			Map<String,Object> map = new HashMap<String,Object>();
			
			if(resultList != null && resultList.size() > 0) {
				map.put("khmc", resultList.get(0));
			}else {
				map.put("khmc", "");
			}
			
			return map;
		}
}
