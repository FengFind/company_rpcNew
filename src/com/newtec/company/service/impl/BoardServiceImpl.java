package com.newtec.company.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newtec.company.service.BoardService;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.reflect.annotation.RpcClass;
import com.newtec.reflect.annotation.RpcMethod;
import com.newtec.router.request.FetchWebRequest;
import com.newtec.rpc.db.DBManager;

import oracle.net.aso.l;
/**
 * 看板数据对接service接口实现类
 * @author 王鹏
 *
 */
@RpcClass(value = "boardService",http = true)
public class BoardServiceImpl implements BoardService {

	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findWtdTotalMoney(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select  " + 
                "      case  " + 
                "        when a.年数量 >= 10000 and a.年数量 < 100000000 " + 
                "          then concat(round(a.年数量/10000,2),'万') " + 
                "        when a.年数量 >= 100000000 " + 
                "          then concat(round(a.年数量/100000000,2),'亿') " + 
                "        else concat(a.年数量,'') " + 
                "      end  " + 
                "from ( " + 
                "select sum(count(distinct(order_uuid))) 年数量 from view_order_server " + 
                "        where product_line_name is not null " + 
                "        and substr(create_time,0,4)=to_char(sysdate,'yyyy') " + 
                "        group by product_line_name_st1,product_line_name " + 
                ")a  ";
		
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalMoney", obj);
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findWtdTodayNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select  " + 
                "      case  " + 
                "        when a.数量 >= 10000 and a.数量 < 100000000 " + 
                "          then concat(round(a.数量/10000,2),'万') " + 
                "        when a.数量 >= 100000000 " + 
                "          then concat(round(a.数量/100000000,2),'亿') " + 
                "        else concat(a.数量,'') " + 
                "      end  " + 
                "from ( " + 
                "select sum(nvl(count(distinct(order_uuid)),0)) 数量 from view_order_server " + 
                "        where product_line_name is not null " + 
                "        and substr(create_time,0,10)=to_char(sysdate,'yyyy-mm-dd') " + 
                "        group by product_line_name_st1,product_line_name " + 
                ")a";
		
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("todayNumber", obj);
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTotalMoneyAmount(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select  " + 
                "      case  " + 
                "        when a.年开票 >= 10000 and a.年开票 < 100000000 " + 
                "          then concat(round(a.年开票/10000,2),'万') " + 
                "        when a.年开票 >= 100000000 " + 
                "          then concat(round(a.年开票/100000000,2),'亿') " + 
                "        else concat(a.年开票,'') " + 
                "      end  " + 
                "from ( " + 
                "select sum(sum(system_tax_price)) 年开票 from view_server_invoice " + 
                "                  where substr(invoice_date,0,4)=to_char(sysdate,'yyyy') " + 
                "                  group by product_line_name_st1,product_name " + 
                ")a";
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalMoneyAmount", obj);
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTodayMoneyAmount(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		String sql =" select  " + 
                "      case  " + 
                "        when a.天开票 >= 10000 and a.天开票 < 100000000 " + 
                "          then concat(round(a.天开票/10000,2),'万') " + 
                "        when a.天开票 >= 100000000 " + 
                "          then concat(round(a.天开票/100000000,2),'亿') " + 
                "        else concat(a.天开票,'') " + 
                "      end  " + 
                "from ( " + 
                "select sum(sum(system_tax_price)) 天开票 " + 
                "  from view_server_invoice " + 
                " where substr(invoice_date, 0, 10) = to_char(sysdate, 'yyyy-mm-dd') " + 
                " group by product_line_name_st1, product_name " + 
                ") a";
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("todayMoneyAmount", obj);
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTotalCercNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		String sql = "select  " + 
                "      case  " + 
                "        when a.年证书 >= 10000 and a.年证书 < 100000000 " + 
                "          then concat(round(a.年证书/10000,2),'万') " + 
                "        when a.年证书 >= 100000000 " + 
                "          then concat(round(a.年证书/100000000,2),'亿') " + 
                "        else concat(a.年证书,'') " + 
                "      end  " + 
                "from (         " + 
                "select count(MID) 年证书 " + 
                "        from VIEW_ORDER_CERC " + 
                "        where   " + 
                "        substr(fictional_date,0,4)=to_char(sysdate,'yyyy') " + 
                "        and state='已完成'    " + 
                ") a";
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCercNumber", obj);
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTodayCercNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		String sql = "select " + 
                "      case " + 
                "        when a.日证书 >= 10000 and a.日证书 < 100000000" + 
                "          then concat(round(a.日证书/10000,2),'万')" + 
                "        when a.日证书 >= 100000000" + 
                "          then concat(round(a.日证书/100000000,2),'亿')" + 
                "        else concat(a.日证书,'')" + 
                "      end " + 
                "from (" + 
                "select count(MID) 日证书" + 
                "        from VIEW_ORDER_CERC" + 
                "        where  " + 
                "        substr(fictional_date,0,10)=to_char(sysdate,'yyyy-mm-dd')" + 
                "        and state='已完成' " + 
                ") a";
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("todayCercNumber", obj);
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTotalCustomNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		String sql = "select " + 
                "      case " + 
                "        when a.年客户 >= 10000 and a.年客户 < 100000000" + 
                "          then concat(round(a.年客户/10000,2),'万')" + 
                "        when a.年客户 >= 100000000" + 
                "          then concat(round(a.年客户/100000000,2),'亿')" + 
                "        else concat(a.年客户,'')" + 
                "      end " + 
                "from (        " + 
                "select count(distinct(CUSTOMER_name)) 年客户" + 
                "                  from view_order_crm" + 
                "                  where   substr(CREATE_TIME_order,0,4)=to_char(sysdate,'yyyy')  " + 
                ") a";
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalCustomNumber", obj);
		return map;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public Map<String, Object> findTodayCustomNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		String sql = "select " + 
                "      case " + 
                "        when a.日客户 >= 10000 and a.日客户 < 100000000" + 
                "          then concat(round(a.日客户/10000,2),'万')" + 
                "        when a.日客户 >= 100000000" + 
                "          then concat(round(a.日客户/100000000,2),'亿')" + 
                "        else concat(a.日客户,'')" + 
                "      end " + 
                "from (" + 
                "select count(distinct(CUSTOMER_name)) 日客户" + 
                "                  from view_order_crm" + 
                "                  where  substr(CREATE_TIME_order,0,10)= to_char(sysdate,'yyyy-mm-dd')" + 
                ") a";
		Object obj = DBManager.get("kabBan").createNativeQuery(sql).getSingleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("todayCustomNumber", obj);
		return map;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public List<Map<String, Object>> findWtdEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select a.product_line_name_st1 wtdName, " + 
				"      case " + 
				"        when a.wtdNum >= 10000 and a.wtdNum < 100000000 " + 
				"          then concat(round(a.wtdNum/10000,2),'万') " + 
				"        when a.wtdNum >= 100000000 " + 
				"          then concat(round(a.wtdNum/100000000,2),'亿') " + 
				"        else concat(a.wtdNum,'') " + 
				"      end wtdNum " + 
				"from ( " + 
				"select b.product_line_name_st1, sum(b.数量) wtdNum " + 
				"from (" + 
				"select product_line_name_st1, count(distinct(order_uuid)) 数量 from view_order_server " + 
				"        where product_line_name is not null " + 
				"        and substr(create_time,0,4)=to_char(sysdate,'yyyy') " + 
				"        group by product_line_name_st1,product_line_name  " + 
				") b " + 
				"where " + 
				"  b.product_line_name_st1 in ('大宗贸易','农食安全及溯源','工业','消费品','政府与机构业务','认证服务与企业优化') " + 
				"group by b.product_line_name_st1 " + 
				") a";
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		
		List<Map<String,Object>> arrayList = new ArrayList<Map<String, Object>>();
		
		Map<String,Object> map = null;
		if(list != null && list.size() > 0) {
			for(Object[] obj:list) {
				map = new HashMap<String, Object>();
				map.put("name", obj[0]);
				map.put("value",obj[1]);
				arrayList.add(map);
			}
		}
		return arrayList;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public List<Map<String, Object>> findTodayMoneyEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select a.product_line_name_st1 wtdName, " + 
                "      case  " + 
                "        when a.wtdNum >= 10000 and a.wtdNum < 10000000 " + 
                "          then concat(round(a.wtdNum/10000,2),'万') " + 
                "        when a.wtdNum >= 10000000 and a.wtdNum < 100000000 " + 
                "          then concat(round(a.wtdNum/10000000,2),'千万') " + 
                "        when a.wtdNum >= 100000000 " + 
                "          then concat(round(a.wtdNum/100000000,2),'亿') " + 
                "        else concat(a.wtdNum,'') " + 
                "      end wtdNum " + 
                "from ( " + 
                "select b.product_line_name_st1, sum(b.日开票) wtdNum " + 
                "from (                    " + 
                "select product_line_name_st1,sum(system_tax_price) 日开票 from view_server_invoice " + 
                "                  where substr(invoice_date, 0, 10) = to_char(sysdate, 'yyyy-mm-dd') " + 
                "                  group by product_line_name_st1,product_name " + 
                ") b  " + 
                "where  " + 
                "  b.product_line_name_st1 in ('大宗贸易','农食安全及溯源','工业','消费品','政府与机构业务','认证服务与企业优化') " + 
                "group by b.product_line_name_st1 " + 
                ")a  ";
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		
		List<Map<String,Object>> arrayList = new ArrayList<Map<String, Object>>();
		
		Map<String,Object> map = null;
		if(list != null && list.size() > 0) {
			for(Object[] obj:list) {
				map = new HashMap<String, Object>();
				map.put("name", obj[0]);
				map.put("value",obj[1]);
				arrayList.add(map);
			}
		}
		return arrayList;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public List<Map<String, Object>> findTotalMoneyEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		String sql = "select a.product_line_name_st1 wtdName, " + 
                "      case  " + 
                "        when a.wtdNum >= 10000 and a.wtdNum < 10000000 " + 
                "          then concat(round(a.wtdNum/10000,2),'万') " + 
                "        when a.wtdNum >= 10000000 and a.wtdNum < 100000000 " + 
                "          then concat(round(a.wtdNum/10000000,2),'千万') " + 
                "        when a.wtdNum >= 100000000 " + 
                "          then concat(round(a.wtdNum/100000000,2),'亿') " + 
                "        else concat(a.wtdNum,'') " + 
                "      end wtdNum " + 
                "from ( " + 
                "select b.product_line_name_st1, sum(b.年开票) wtdNum " + 
                "from (                    " + 
                "select product_line_name_st1,sum(system_tax_price) 年开票 from view_server_invoice " + 
                "                  where substr(invoice_date,0,4)=to_char(sysdate,'yyyy') " + 
                "                  group by product_line_name_st1,product_name " + 
                ") b  " + 
                "where  " + 
                "  b.product_line_name_st1 in ('大宗贸易','农食安全及溯源','工业','消费品','政府与机构业务','认证服务与企业优化') " + 
                "group by b.product_line_name_st1 " + 
                ")a ";
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		List<Map<String,Object>> arrayList = new ArrayList<Map<String, Object>>();
		Map<String,Object> map = null;
		if(list != null && list.size() > 0) {
			for(Object[] obj:list) {
				map = new HashMap<String, Object>();
				map.put("name", obj[0]);
				map.put("value",obj[1]);
				arrayList.add(map);
			}
		}
		return arrayList;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public JSONObject findTotalEveryTypeMoneyAndWtd(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select a.wtdName,a.wtd2name,a.wtdNum,b.tmoney from ( " + 
                "select e.product_line_name_st1 wtdName,e.product_line_name wtd2name, " + 
                "      case  " + 
                "        when e.twtdNum >= 10000 and e.twtdNum < 100000000 " + 
                "          then concat(round(e.twtdNum/10000,2),'万') " + 
                "        when e.twtdNum >= 100000000 " + 
                "          then concat(round(e.twtdNum/100000000,2),'亿') " + 
                "        else concat(e.twtdNum,'') " + 
                "      end wtdNum " + 
                "from ( " + 
                " " + 
                "select c.product_line_name_st1,c.product_line_name, sum(c.wtdNum) twtdNum   " + 
                "from ( " + 
                "select product_line_name_st1,product_line_name,count(distinct(order_uuid)) wtdNum from view_order_server " + 
                "        where product_line_name is not null " + 
                "        and substr(create_time,0,4)=to_char(sysdate,'yyyy') " + 
                "        group by product_line_name_st1,product_line_name " + 
                ") c       " + 
                "where  " + 
                "  c.product_line_name_st1 in ('大宗贸易','农食安全及溯源','工业','消费品','政府与机构业务','认证服务与企业优化') " + 
                "group by c.product_line_name_st1,c.product_line_name   " + 
                "order by c.product_line_name_st1 " + 
                ") e " + 
                " " + 
                ") a " + 
                "inner join          " + 
                "(        " + 
                "select f.product_line_name_st1 wtdName, f.product_name wtd2Name, " + 
                "      case  " + 
                "        when f.tmoney >= 10000 and f.tmoney < 10000000 " + 
                "          then concat(round(f.tmoney/10000,2),'万') " + 
                "        when f.tmoney >= 10000000 and f.tmoney < 100000000 " + 
                "          then concat(round(f.tmoney/10000000,2),'千万') " + 
                "        when f.tmoney >= 100000000 " + 
                "          then concat(round(f.tmoney/100000000,2),'亿') " + 
                "        else concat(f.tmoney,'') " + 
                "      end tmoney " + 
                "from ( " + 
                " " + 
                "select d.product_line_name_st1,d.product_name,sum(d.年开票) tmoney " + 
                "from (  " + 
                "select product_line_name_st1,product_name,sum(system_tax_price) 年开票 from view_server_invoice " + 
                "       where substr(invoice_date,0,4)=to_char(sysdate,'yyyy') " + 
                "       group by product_line_name_st1,product_name         " + 
                ") d " + 
                "where  " + 
                "  d.product_line_name_st1 in ('大宗贸易','农食安全及溯源','工业','消费品','政府与机构业务','认证服务与企业优化') " + 
                "group by d.product_line_name_st1,d.product_name   " + 
                "order by d.product_line_name_st1   " + 
                ") f " + 
                "     " + 
                ") b   " + 
                "on a.wtdName = b.wtdName and a.wtd2Name = b.wtd2Name";
		
		 JSONObject jo = new JSONObject();
         
         // 大宗贸易
         JSONArray dzmy = new JSONArray();        
         // 农食安全及溯源
         JSONArray nsaq = new JSONArray();
         // 工业
         JSONArray gy = new JSONArray();                
         // 消费品
         JSONArray xfp = new JSONArray();                
         // 政府与机构业务
         JSONArray zfjg = new JSONArray();                
         // 认证服务与企业优化
         JSONArray rzqy = new JSONArray();
         List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
         if(list != null && list.size() > 0) { 
                 for (int i = 0; i < list.size(); i++) {
                         Object[] o = list.get(i); 
                         JSONObject jot = new JSONObject();
                         
                         jot.put("wtd2Name", o[1]); 
                         jot.put("wtdNum", o[2]);
                         jot.put("tmoney", o[3]);
                         
                         if(o[0] != null && String.valueOf(o[0]).toString().equals("大宗贸易")) {
                                 dzmy.add(jot);
                         }else if(o[0] != null && String.valueOf(o[0]).toString().equals("农食安全及溯源")) {
                                 nsaq.add(jot);
                         }else if(o[0] != null && String.valueOf(o[0]).toString().equals("工业")) {
                                 gy.add(jot);
                         }else if(o[0] != null && String.valueOf(o[0]).toString().equals("消费品")) {
                                 xfp.add(jot);
                         }else if(o[0] != null && String.valueOf(o[0]).toString().equals("政府与机构业务")) {
                                 zfjg.add(jot);
                         }else if(o[0] != null && String.valueOf(o[0]).toString().equals("认证服务与企业优化")) {
                                 rzqy.add(jot);
                         }
                 }
         }
         
         jo.put("dzmy", dzmy);
         jo.put("nsaq", nsaq);
         jo.put("gy", gy);
         jo.put("xfp", xfp);
         jo.put("zfjg", zfjg);
         jo.put("rzqy", rzqy);
         
         return jo;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findTodayWtdEveryTypeNumber(FetchWebRequest<Map<String, String>> fetchWebReq)
			throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select a.product_line_name_st1 wtdName, " + 
                "      case  " + 
                "        when a.wtdNum >= 10000 and a.wtdNum < 100000000 " + 
                "          then concat(round(a.wtdNum/10000,2),'万') " + 
                "        when a.wtdNum >= 100000000 " + 
                "          then concat(round(a.wtdNum/100000000,2),'亿') " + 
                "        else concat(a.wtdNum,'') " + 
                "      end wtdNum " + 
                "from ( " + 
                "select b.product_line_name_st1, sum(b.数量) wtdNum " + 
                "from (  " + 
                "select product_line_name_st1,product_line_name,nvl(count(distinct(order_uuid)),0) 数量 from view_order_server " + 
                "        where product_line_name is not null " + 
                "        and substr(create_time,0,10)=to_char(sysdate,'yyyy-mm-dd') " + 
                "        group by product_line_name_st1,product_line_name " + 
                ") b  " + 
                "where  " + 
                "  b.product_line_name_st1 in ('大宗贸易','农食安全及溯源','工业','消费品','政府与机构业务','认证服务与企业优化') " + 
                "group by b.product_line_name_st1 " + 
                ")a";
		
		
		 List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		  JSONArray ja = new JSONArray();
          
          if(list != null && list.size() > 0) {
        	  for (int i = 0; i < list.size(); i++) {
                  Object[]  o =  list.get(i); 
                  JSONObject jo = new JSONObject();
                  jo.put("name", String.valueOf(o[0])); 
                  jo.put("value", String.valueOf(o[1]));
                  
                  ja.add(jo); } 
          }
          return ja;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findFirst5CercNum(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub

		String sql = "select 服务大类 name, 服务证书数 cercNum from view_cert_main order by 服务证书数 desc";
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			// 先计算总和 然后计算百分比
			Integer total = 0;
			for (int i = 0; i < list.size(); i++) {
				Object[] o = list.get(i);
				total += o[1] == null ? 0 : Integer.parseInt(String.valueOf(o[1]));
			}
			for (int i = 0; i < list.size(); i++) {
				Object[] o = list.get(i);
				JSONObject jo = new JSONObject();
				jo.put("name", o[0]);
				Double cert = (double) (Double.parseDouble(String.valueOf(o[1])) / total);
				DecimalFormat df = new DecimalFormat(".00");
				jo.put("value", cert * 100 < 1 ? "0" + df.format(cert * 100) : df.format(cert * 100));
				ja.add(jo);
			}
		}

		return ja;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findMapCountryMsg(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub

		String sql = "select east_and_west_channels 经度, " + 
				"       north_latitude         纬度, " + 
				"       公司, " + 
				"       国家, " + 
				"       委托单数 " + 
				"  from (select sorg_level3 公司, " + 
				"               BUSINESS_COUNTRY 国家, " + 
				"               f_mmj(count(distinct(order_uuid))) 委托单数 " + 
				"          from VIEW_ORDER_SERVER " + 
				"         where substr(create_time, 0, 4) = 2020 " + 
				"           and BUSINESS_COUNTRY is not null " + 
				"           and BUSINESS_COUNTRY <> '中国' " + 
				"         group by sorg_level3, BUSINESS_COUNTRY) a " + 
				" inner join INFOSHAR_1237384602 b " + 
				"    on a.国家 = b.country " + 
				" where a.国家 is not null " + 
				"   and b.east_and_west_channels is not null " + 
				" order by 国家";

		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] o = list.get(i);
				JSONObject jo = new JSONObject();

				jo.put("name", o[3]);
				jo.put("wtdNum", o[4]);
				jo.put("jd", Double.parseDouble(String.valueOf(o[0])));
				jo.put("wd", Double.parseDouble(String.valueOf(o[1])));
				jo.put("company", o[2]);
				ja.add(jo);
			}
		}

		return ja;
	}

	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findMapProvinceMsg(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub

		String sql = "select a.公司 company," + 
				"       a.省市 name," + 
				"       a.完工金额 act," + 
				"       (case" + 
				"         when a.完工金额 >= 10000 and a.完工金额 < 10000000 then " + 
				"          concat(round(a.完工金额 / 10000, 2), '万') " + 
				"         when a.完工金额 >= 10000000 and a.完工金额 < 100000000 then " + 
				"          concat(round(a.完工金额 / 10000000, 2), '千万')" + 
				"         when a.完工金额 >= 100000000 and a.完工金额 < 1000000000 then " + 
				"          concat(round(a.完工金额 / 100000000, 2), '亿') " + 
				"         else " + 
				"          concat(a.完工金额, '') " + 
				"       end) money " + 
				"  from (select sorg_level3 公司,business_provinces 省市, sum(sys_cur_total_amount) 完工金额 " + 
				"          from view_order_server " + 
				"         where substr(work_finish_time, 0, 4) = to_char(sysdate, 'yyyy') " + 
				"           and BUSINESS_COUNTRY = '中国' " + 
				"           and business_provinces is not null " + 
				"         group by sorg_level3,business_provinces) a order by 省市";
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] o = list.get(i);
				JSONObject jo = new JSONObject();
				jo.put("company", o[0]);
				jo.put("name", o[1]);
				jo.put("value", o[3]);
				jo.put("money", o[2]);
				ja.add(jo);
			}
		}
         return ja;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findMapInvoice(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		
		String sql = "select aaa.委托单号, " + 
				"       aaa.审核状态, " + 
				"       aaa.客户名称, " + 
				"       aaa.产品线名称, " + 
				"       aaa.收入, " + 
				"       aaa.支出, " + 
				"       aaa.业务状态, " + 
				"       aaa.国家, " + 
				"       aaa.省份, " + 
				"       substr(aaa.创建时间, 0, 19)  创建时间, " + 
				"       bbb.east_and_west_channels 经度, " + 
				"       bbb.north_latitude         纬度 " + 
				"  from (select order_uuid               委托单号,  " + 
				"               audit_state              审核状态, " + 
				"               customer_name            客户名称, " + 
				"               product_line_name        产品线名称, " + 
				"               system_total_revenue     收入, " + 
				"               system_total_expenditure 支出, " + 
				"               business_state           业务状态, " + 
				"               business_country         国家, " + 
				"               business_provinces       省份, " + 
				"               create_time              创建时间 " + 
				"          from (select aaa.order_uuid," + 
				"                       aaa.audit_state, " + 
				"                       bbb.customer_name, " + 
				"                       aaa.product_line_name, " + 
				"                       aaa.system_total_revenue, " + 
				"                       nvl(to_number(aaa.system_total_expenditure), 0) system_total_expenditure," + 
				"                       (case " + 
				"                         when aaa.business_state = '完工' then " + 
				"                          '已完工' " + 
				"                         when aaa.business_state = '开工' then " + 
				"                          '已开工' " + 
				"                         else " + 
				"                          aaa.business_state " + 
				"                       end) business_state, " + 
				"                       aaa.business_country," + 
				"                       aaa.business_provinces, " + 
				"                       aaa.create_time " + 
				"                  from infoshar_1756611273 aaa, infoshar_1293038837 bbb " + 
				"                 where aaa.customer_id = bbb.mid " + 
				"                   and to_number(aaa.system_total_revenue) <> 0 " + 
				"                   and bbb.group_type = '02' " + 
				"                   and aaa.data_state = '正常' " + 
				"					and (to_date(substr(aaa.create_time, 0, 19), 'yyyy-mm-dd hh24:mi:ss') between (sysdate - 3/24) and sysdate) "+
				"                   and substr(aaa.create_time, 0, 10) = " + 
				"                       to_char(sysdate, 'yyyy-mm-dd') " + 
				"                   and length(aaa.customer_name) > 3 " + 
				"                   and aaa.product_line_name is not null) " + 
				"         where (business_state = '未开工' and system_total_expenditure = '0') " + 
				"            or business_state <> '未开工') aaa " + 
				" inner join INFOSHAR_1237384602 bbb " + 
				"    on aaa.国家 = bbb.country " + 
				" where aaa.国家 is not null " + 
				"   and bbb.east_and_west_channels is not null " + 
				" order by 国家";
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] o = list.get(i);
				JSONObject jo = new JSONObject();
				jo.put("wtdNo", o[0]);//委托单号
				jo.put("shState", o[1]);//审核状态
				jo.put("khName", o[2]);//客户名称
				jo.put("productName", o[3]);//产品线名称
				jo.put("revenue", o[4]);//收入
				jo.put("expenditure", o[5]);//支出
				jo.put("ywState", o[6]);//业务状态
				jo.put("gj", o[7]);//国家
				jo.put("sf", o[8]);//省份
				jo.put("cTime", o[9]);//创建时间
				jo.put("jd", o[10]);//经度
				jo.put("wd", o[11]);//纬度
				jo.put("companyName", "江西公司"+i);//公司名称
				jo.put("isGood", i % 2 == 0?0:1);//0普通客户，1优质客户
				jo.put("serviceName", "服务项"+i);//服务项名称
				ja.add(jo);
			}
		}
		return ja;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findPrductLine(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		String sql = "select product_line_name, sum(sys_cur_total_amount) sys_cur_total_amount " + 
				"   from view_order_server " + 
				"  where substr(create_time_order, 0, 4) = to_char(sysdate, 'yyyy') " + 
				"    and product_line_name is not null " + 
				"  group by product_line_name";
		List<Object[]> list = DBManager.get("kabBan").createNativeQuery(sql).getResultList();
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Object[] o = list.get(i);
				JSONObject jo = new JSONObject();
				jo.put("name", o[0]);//审核状态
				jo.put("amount", o[1]);//客户名称
				ja.add(jo);
			}
		}
		return ja;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findCompanyInfo(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		
		
		String[] info = {"广东公司","广州粤信科技有限公司","1","矿产品","开发项目","30000.00"};
		String[] info1 = {"天津公司","天津质跃科技有限公司","0","体系服务认证","研发飞机","40000.00"};
		String[] info2 = {"内蒙古公司","牛羊肉出品有限公司","1","肉产品","放羊","50000.00"};
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(info);
		list.add(info1);
		list.add(info2);
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String[] o = list.get(i);
				JSONObject jo = new JSONObject();
				jo.put("companyName", o[0]);//公司名称
				jo.put("customerName", o[1]);//客户名称
				jo.put("isGood", o[2]);//优质客户（0：普通，1：优质）
				jo.put("productName", o[3]);//产品线名称
				jo.put("serviceName", o[4]);//服务项名称
				jo.put("money", o[5]);//服务项名称
				ja.add(jo);
			}
		}
		return ja;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findProductInfo(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		ArrayList<String[]> list = new ArrayList<String[]>();
		String[] info1 = {"农食安全","中检南方","中国","内蒙古自治区","1563","100000","2000单","235万","3200张","1800"};
		String[] info = null;
		for(int i = 0;i < 20;i ++) {
			info = new String[10];
			for(int j = 0;j < 10;j ++) {
				info[j] = info1[j] + i;
			}
			list.add(info);
		}
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String[] o = list.get(i);
				JSONObject jo = new JSONObject();
				jo.put("productLine", o[0]);//产品线
				jo.put("fzjg", o[1]);//分支机构
				jo.put("gj", o[2]);//国家
				jo.put("sf", o[3]);//省份
				jo.put("count", o[4]);//人数
				jo.put("yearWtd", o[5]);//年委托单数
				jo.put("monthWtd", o[6]);//月委托单数
				jo.put("monthSr", o[7]);//月收入
				jo.put("monthCz", o[8]);//月出证
				jo.put("addCustomer", o[9]);//新增客户
				ja.add(jo);
			}
		}
		return ja;
	}
	
	@RpcMethod(loginValidate = false)
	@Override
	public JSONObject findOrganInfo(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		
		String[] info = {"500","100单/天","400单/天","100证书/天"};
		String[] infos = {"体系服务认证","研发飞机","40000.00","下降"};
		ArrayList<String[]> list = new ArrayList<String[]>();
		for(int i = 0;i < 20;i ++) {
			String[] info1 = new String[4];
			for(int j = 0;j < 4;j ++) {
				info1[j] = infos[j] + i;
			}
			list.add(info1);
		}
		JSONObject ja = new JSONObject();
		JSONArray ja1 = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("st", info[0]);//产品线
		jo.put("jd", info[1]);//接单
		jo.put("pd", info[2]);//派单
		jo.put("fz", info[3]);//发证
		ja.put("organ",jo);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String[] o = list.get(i);
				JSONObject jos = new JSONObject();
				jos.put("productName", o[0]);//产品线名称
				jos.put("serviceName", o[1]);//服务项名称
				jos.put("cb", o[2]);//成本
				jos.put("state", o[3]);//状态
				ja1.add(jos);
			}
		}
		ja.put("proList", ja1);//发证
		return ja;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public JSONObject findStaffInfo(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		String[] info = {"15141","10063"};
		ArrayList<String[]> list = new ArrayList<String[]>();
		for(int i = 0;i < 20;i ++) {
			String[] info1 = new String[4];
			for(int j = 0;j < 4;j ++) {
				info1[j] = i+"";
			}
			list.add(info1);
		}
		JSONObject ja = new JSONObject();
		JSONArray ja1 = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("staffCount", info[0]);//人员总数
		jo.put("hyCount", info[1]);//活跃人员数
		ja.put("staffInfo",jo);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String[] o = list.get(i);
				JSONObject jos = new JSONObject();
				jos.put("per", o[0]);//人/组
				jos.put("ds", o[1]);//本月单数
				jos.put("fh", o[2]);//并行负荷
				jos.put("zl", o[3]);//环比增量
				ja1.add(jos);
			}
		}
		ja.put("staffList", ja1);//人员情况
		return ja;
	}
	@RpcMethod(loginValidate = false)
	@Override
	public JSONArray findOrderInfo(FetchWebRequest<Map<String, String>> fetchWebReq) throws CustomException {
		// TODO Auto-generated method stub
		ArrayList<String[]> list = new ArrayList<String[]>();
		String[] info = null;
		for(int i = 0;i < 20;i ++) {
			info = new String[6];
			for(int j = 0;j < 6;j ++) {
				info[j] = i + "";
				if(j == 2) {
					info[j] = "审核通过";
				}
				if(j == 3 || j == 4 || j == 5) {
					info[j] = "1";
				}
			}
			list.add(info);
		}
		JSONArray ja = new JSONArray();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				String[] o = list.get(i);
				JSONObject jo = new JSONObject();
				jo.put("cname", o[0]);//客户名
				jo.put("product", o[1]);//产品线
				jo.put("state", o[2]);//业务状态
				jo.put("jd", o[3]);//接单预警
				jo.put("pd", o[4]);//派单预警
				jo.put("cb", o[5]);//成本预警
				ja.add(jo);
			}
		}
		return ja;
	}
}
