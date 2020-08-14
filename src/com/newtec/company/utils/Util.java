package com.newtec.company.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Util {

	public static void main(String[] args) {
		DecimalFormat df = new DecimalFormat("#.000");
		
		System.out.println(df.format(new BigDecimal("1235.6978")));
	}
	
	/**
	 * 今年和今日 数据转换 int 类型
	 * @param name 类型名字
	 * @param unit 类型单位
	 * @param y 年数据
	 * @param d 日数据
	 * @param df 换算单位
	 * @return
	 */
	public static JSONObject returnTodayTotalIntMsg(String name, String unit, Object y, Object d,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		JSONObject total = new JSONObject();
		int year =  ((BigDecimal) y).intValue(), 
			day =  ((BigDecimal) d).intValue();
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("today", "+"+( day > yi ? day/yi  + "亿" : ( day > wan ? day/wan + "万" : day) ));
		
		total.put("w", (year > yi ? "亿" : ( year > wan ? "万" : "" ))+ unit);
		total.put("n", year > yi ? df.format( (float)year/yi ) : ( year > wan ? df.format( (float)year/wan ) : year) );
		
		jo.put("total", total);
		
		return jo;
	}

	/**
	 * 今年和今日 数据转换 double 类型
	 * @param name 类型名字
	 * @param unit 类型单位
	 * @param y 年数据
	 * @param d 日数据
	 * @param df 换算单位
	 * @return
	 */
	public static JSONObject returnTodayTotalDoubleMsg(String name, String unit, Object y, Object d,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		JSONObject total = new JSONObject();
		double year =  ((BigDecimal) y).doubleValue(), 
				day =  ((BigDecimal) d).doubleValue();
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("today", "+"+( day > yi ? (int) (day/yi) + "亿" : ( day > wan ? (int)( day/wan ) + "万" : day) ));
		
		total.put("w", (year > yi ? "亿" : ( year > wan ? "万" : "" ))+ unit);
		total.put("n", year > yi ? df.format( year/yi ) : ( year > wan ? df.format( year/wan ) : year) );
		
		jo.put("total", total);
		
		return jo;
	}
	
	/**
	 * 根据关键字 返回公司信息json数组
	 * @param res 公司信息
	 * @param gjz 父节点关键字
	 * @return
	 */
	public static JSONArray returnCompanyMsg(List<Object[]> res, String gjz) {
		JSONArray ja = new JSONArray();
		JSONObject parent = new JSONObject();
		JSONArray children = new JSONArray();
		// 当前2级公司名称
		String cur2 = "";
		
		for (int i = 0; i < res.size(); i++) {
			Object[] rdata = res.get(i);
			
			// 2级公司名称
			Object company0 = rdata[0];
			// 3级公司名称
			Object company1 = rdata[1];
			
			if(company0== null || company0.equals("")
					|| company1 == null || company1.equals("")
					|| company0.toString().indexOf("参股") > 0) {
				continue;
			}
			
			String company2 = company0.toString();
			String company3 = company1.toString();
			
			if(cur2.equals("")) {
				cur2 = company2;
			}
			
			// 委托单量
			String wtdl = Util.returnYiOrWan((BigDecimal) rdata[2], 2);
			// 开票收入
			String kpsr = Util.returnYiOrWan((BigDecimal) rdata[3], 2);
			// 成本总额
			String cbze = Util.returnYiOrWan((BigDecimal) rdata[4], 2);
			// 出证数量
			String czsl = Util.returnYiOrWan((BigDecimal) rdata[5], 2);

			if(cur2.equals(company2) && company3.equals(gjz)) {
				parent.put("id", company2);
				parent.put("gsname", company2);
				parent.put("wtdl", wtdl);
				parent.put("kpsr", kpsr);
				parent.put("cbze", cbze);
				parent.put("czsl", czsl);
				
				continue;
			}else if(!cur2.equals(company2) && company3.equals(gjz)) {
				parent.put("children", children);
				// 先将父节点add进数组ja
				ja.add(parent);
				
				// 然后将父子节点重新赋值
				parent = new JSONObject();
				children = new JSONArray();
				cur2 = company2;
				
				parent.put("id", company2);
				parent.put("gsname", company2);
				parent.put("wtdl", wtdl);
				parent.put("kpsr", kpsr);
				parent.put("cbze", cbze);
				parent.put("czsl", czsl);
				
				continue;
			}else if(!cur2.equals(company2) && !company3.equals(gjz)) {
				parent.put("children", children);
				// 先将父节点add进数组ja
				ja.add(parent);
				
				// 然后将父子节点重新赋值
				parent = new JSONObject();
				children = new JSONArray();
				cur2 = company2;
			}
			
			// 这里是子节点的数据添加
			JSONObject child = new JSONObject();
			
			child.put("id", company3);
			child.put("gsname", company3);
			child.put("wtdl", wtdl);
			child.put("kpsr", kpsr);
			child.put("cbze", cbze);
			child.put("czsl", czsl);
			
			// 将child插入到父节点的children中
			children.add(child);
		}
		
		// 最后将parent和 children 加入
		parent.put("children", children);
		// 先将父节点add进数组ja
		ja.add(parent);
		
		return ja;
	}
	
	/**
	 * 根据位数 四舍五入 返回字符串
	 * @param bd
	 * @param ws
	 * @return
	 */
	public static String returnStringForNumber(BigDecimal bd, int ws) {
		if(ws <= 0) {
			return null;
		}
		
		// 格式字符串
		StringBuffer gs = new StringBuffer("#.");
		
		for (int i = 0; i < ws; i++) {
			gs.append("0");
		}
		
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat(gs.toString());
		
		return df.format(bd);
	}
	
	/**
	 * 转换成亿或者万返回
	 * @param bd 数字
	 * @param ws 有效数字位数
	 * @return
	 */
	public static String returnYiOrWan(BigDecimal bd, int ws) {
		// 如果 ==0 或者 没带小数点 直接返回
		if(bd.compareTo(new BigDecimal(0)) == 0 
				|| (bd.toString().indexOf(".") == -1 && bd.compareTo(new BigDecimal(10000)) == -1)) {
			return bd.toString();
		}
		
		BigDecimal yi = new BigDecimal(100000000);
		BigDecimal wan = new BigDecimal(10000);
		
		return bd.compareTo(yi)== 1 ? 
				Util.returnStringForNumber(bd.divide(yi), ws) + "亿" :
					( bd.compareTo(wan)== 1 ? Util.returnStringForNumber(bd.divide(wan), ws) + "万"   : Util.returnStringForNumber(bd, ws) );
	}
}
