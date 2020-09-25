package com.newtec.company.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class NewUtils {
	
	/**
	 * 今年 数据转换 double 类型
	 * @param name 类型名字
	 * @param y 年数据
	 * @param df 换算单位
	 * @return
	 */
	public static JSONObject returnTodayTotalDoubleMsg(String name, Object y,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		
		if(name == null) {
			return jo;
		}
		
		//JSONObject total = new JSONObject();
		double year =  y == null ? 0d : ((BigDecimal)y).doubleValue();
		// 亿 万
		int yi = 100000000,wan = 10000;
		
		jo.put("word", name);
		jo.put("unit", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		jo.put("num", year > yi ? df.format( year/yi ) : ( year > wan ? df.format(year/wan ) : year) );
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
		double year =  y == null ? 0d : ((BigDecimal) y).doubleValue(), 
				day =  d == null ? 0d : ((BigDecimal) d).doubleValue();
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
	 * 今年和今日 数据转换 int 类型
	 * @param name 类型名字
	 * @param unit 类型单位
	 * @param y 年数据
	 * @param d 日数据
	 * @param df 换算单位
	 * @return
	 */
	public static JSONObject returnTodayTotalIntMsg(String name,  Object y, Object d,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		int year =  y == null ? 0 : ((BigDecimal) y).intValue(), 
			day =  d == null ? 0 : ((BigDecimal) d).intValue();
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("bword", "今日");
		jo.put("bnum", "+"+( day > yi ? day/yi  + "亿" : ( day > wan ? day/wan + "万" : day) ));
		
		jo.put("unit", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		jo.put("num", year > yi ? df.format( (float)year/yi ) : ( year > wan ? df.format( (float)year/wan ) : year) );
		
		return jo;
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
	public static JSONObject returnTodayTotalIntMsg(String name,  Object y, Object d,String name1,String name2,String name3,
			Object a,Object b,Object c, DecimalFormat df) {
		JSONObject jo = new JSONObject();
		JSONObject total = new JSONObject();
		JSONObject j = new JSONObject();
		int year =  y == null ? 0 : ((BigDecimal) y).intValue(), 
			day =  d == null ? 0 : ((BigDecimal) d).intValue(),
			aay =  a == null ? 0 : ((BigDecimal) a).intValue(),
			bby =  b == null ? 0 : ((BigDecimal) b).intValue(),
			ccy = c == null ? 0 : ((BigDecimal) c).intValue();				;
		// 亿 万
		int yi = 100000000, wan = 10000;
		j.put("cpxhykh", name1);
		j.put("ncpxhykh",name2);
		j.put("nhykh",name3);
		j.put("cpxhykhsl",aay > yi ? aay/yi  + "亿" : ( aay > wan ? aay/wan + "万" : aay));
		j.put("ncpxhykhsl",bby > yi ? bby/yi  + "亿" : ( bby > wan ? bby/wan + "万" : bby));
		j.put("nhykhsl",ccy > yi ? ccy/yi  + "亿" : ( ccy > wan ? ccy/wan + "万" : ccy));
		jo.put("word", name);
		
		jo.put("today", "+"+( day > yi ? day/yi  + "亿" : ( day > wan ? day/wan + "万" : day) ));
		
		total.put("w", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		total.put("n", year > yi ? df.format( (float)year/yi ) : ( year > wan ? df.format( (float)year/wan ) : year) );
		
		jo.put("total", total);
		jo.put("cpx",j);
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
			String wtdl = NewUtils.returnYiOrWan((BigDecimal) rdata[2], 0);
			// 预测收入
			String ycsr = NewUtils.returnYiOrWan((BigDecimal) rdata[3], 0);
			// 收入总额
			String srze = NewUtils.returnYiOrWan((BigDecimal) rdata[4], 0);
			// 支出总额
			String zcze = NewUtils.returnYiOrWan((BigDecimal) rdata[5], 0);

			if(cur2.equals(company2) && company3.equals(gjz)) {
				parent.put("id", company2);
				parent.put("name", company2);
				parent.put("wtdl", wtdl);
				parent.put("ycsr", ycsr);
				parent.put("srze", srze);
				parent.put("zcze", zcze);
				
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
				parent.put("name", company2);
				parent.put("wtdl", wtdl);
				parent.put("ycsr", ycsr);
				parent.put("srze", srze);
				parent.put("zcze", zcze);
				
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
			child.put("name", company3);
			child.put("wtdl", wtdl);
			child.put("ycsr", ycsr);
			child.put("srze", srze);
			child.put("zcze", zcze);
			
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
	 * 计算合计
	 * @param res
	 * @param gjz
	 * @return
	 */
	public static JSONObject returnHeji(List<Object[]> res, String gjz) {
		JSONObject jo = new JSONObject();
		
		if(res == null || res.size() == 0) {
			return jo;
		}
		
		// 用于计算求和
		// 委托单量
		BigDecimal twtdl = new BigDecimal(0);
		// 预测收入 收入总额 支出总额
		BigDecimal tycsr = new BigDecimal(0), 
				tsrze = new BigDecimal(0), 
				tzcze = new BigDecimal(0);
		
		for (int i = 0; i < res.size(); i++) {
			Object[] r = res.get(i);
			
			// 获取第二列值
			String two = (String) r[1];
			
			if(two != null && two.equals("父节点")) {
				// 委托单量
				BigDecimal wtdl = (BigDecimal) r[2];
				// 预测收入
				BigDecimal ycsr = (BigDecimal) r[3];
				// 收入总额
				BigDecimal srze = (BigDecimal) r[4];
				// 支出总额
				BigDecimal zcze = (BigDecimal) r[5];
				
				twtdl = twtdl.add(wtdl);
				tycsr = tycsr.add(ycsr);
				tsrze = tsrze.add(srze);
				tzcze = tzcze.add(zcze);
			}
		}
		
		jo.put("id", "合计");
		jo.put("name", "合计");
		jo.put("wtdl", NewUtils.returnYiOrWan(twtdl, 0));
		jo.put("ycsr", NewUtils.returnYiOrWan(tycsr, 0));
		jo.put("srze", NewUtils.returnYiOrWan(tsrze, 0));
		jo.put("zcze", NewUtils.returnYiOrWan(tzcze, 0));
		
		return jo;
	}
	
	/**
	 * 对ja进行排序 并将合计添加到第一个
	 * @param hj 合计
	 * @param ja 需要排序的ja
	 * @return
	 */
	public static JSONArray paixuForJsonArray(JSONObject hj, JSONArray ja) {
		JSONArray res = new JSONArray();
		
		res.add(hj);
		
		if(ja != null && ja.size() > 0) {
			for (int i = 0; i < ja.size(); i++) {
				JSONObject jt = (JSONObject) ja.get(i);
				
				for (int j = i+1; j < ja.size(); j++) {
					JSONObject jaj = (JSONObject) ja.get(j);
					
					if(jaj.getString("ycsr") == null || jaj.getString("ycsr").equals("")
							|| ( jt.getString("ycsr").length() > 1 && jaj.getString("ycsr").length() == 1 )) {
						continue;
					}

					if(jt.getString("ycsr") == null || jt.getString("ycsr").equals("")
							|| ( jt.getString("ycsr").length() == 1 && jaj.getString("ycsr").length() > 1 )
							|| ( jt.getString("ycsr").length() == 1 && jaj.getString("ycsr").length() == 1 
									&& Double.parseDouble(jt.getString("ycsr")) < Double.parseDouble(jaj.getString("ycsr")) ) ) {
						JSONObject jh = (JSONObject) ja.get(j);
						ja.set(j, jt);
						jt = jh;
						
						continue;
					}
					
					// i
					// 单位
					String ycsri = jt.getString("ycsr");
					String dwi = jt.getString("ycsr").substring(jt.getString("ycsr").length() - 1);
					Integer kpsri = Integer.parseInt( ycsri.indexOf(".") > -1 ? ycsri.substring(0, ycsri.indexOf(".")) : ( dwi.equals("万") ? ycsri.substring(0, ycsri.length() - 1) : ycsri ) );
					
					// j
					// 单位
					String ycsrj = jaj.getString("ycsr");
					String dwj = jaj.getString("ycsr").substring(jaj.getString("ycsr").length() - 1);
					Integer kpsrj = Integer.parseInt( ycsrj.indexOf(".") > -1 ? ycsrj.substring(0, ycsrj.indexOf(".")) : ( dwi.equals("万") ? ycsrj.substring(0, ycsrj.length() - 1) : ycsrj ) );
					
					if(dwi == null || dwi.equals("") || dwj == null || dwj.equals("")
							|| ( !dwj.equals("亿")&&!dwj.equals("万")&&(dwi.equals("亿")||dwi.equals("万")) ) 
							|| ( !dwj.equals("亿")&&!dwj.equals("万")&&!dwi.equals("亿")&&!dwi.equals("万")
									&& kpsri.intValue() >= kpsrj.intValue()  )
							|| ( dwi.equals("亿") && dwj.equals("万") ) 
							|| ( dwi.equals("亿") && dwj.equals("亿") && kpsri.intValue() >= kpsrj.intValue()  )
							|| ( dwi.equals("万") && dwj.equals("万") && kpsri.intValue() >= kpsrj.intValue()  ) ) {
						continue;
					}else if( ( !dwi.equals("亿")&&!dwi.equals("万")&&(dwj.equals("亿")||dwj.equals("万")) ) 
							||  ( !dwj.equals("亿")&&!dwj.equals("万")&&!dwi.equals("亿")&&!dwi.equals("万")
									&& kpsri.intValue() < kpsrj.intValue() ) 
							|| ( dwj.equals("亿") && dwi.equals("万") ) 
							|| ( dwi.equals("亿") && dwj.equals("亿") && kpsri.intValue() < kpsrj.intValue()  )
							|| ( dwi.equals("万") && dwj.equals("万") && kpsri.intValue() < kpsrj.intValue()  ) ) {
						JSONObject jh = (JSONObject) ja.get(j);
						ja.set(j, jt);
						jt = jh;
					}
				}
				
				res.add(jt);
			}
		}
		
		return res;
	}
	
	/**
	 * 转换成亿或者万返回
	 * @param bd 数字
	 * @param ws 有效数字位数
	 * @return
	 */
	public static String returnYiOrWan(BigDecimal bd, int ws) {
		// 如果 ==0 或者 没带小数点 直接返回
		if(bd == null) {
			return "0";
		}
		
		BigDecimal yi = new BigDecimal(100000000);
		BigDecimal wan = new BigDecimal(10000);
		
		return bd.compareTo(yi)== 1 ? 
				NewUtils.returnStringForNumber( bd.divide(wan), ws) + "万" : NewUtils.returnStringForNumber(bd.divide(wan), 2);
	}
	
	/**
	 * 根据位数 四舍五入 返回字符串
	 * @param bd
	 * @param ws
	 * @return
	 */
	public static String returnStringForNumber(BigDecimal bd, int ws) {
		if(ws < 0 && bd == null) {
			return null;
		}
		
		if(ws == 0 && bd != null) {
			return bd.intValue()+"";
		}
		
		// 格式字符串
		StringBuffer gs = new StringBuffer("#.");
		
		for (int i = 0; i < ws; i++) {
			gs.append("0");
		}
		
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat(gs.toString());
		
		String res = df.format(bd); 
		
		return res.substring(0,1).equals(".") ? "0"+res : res;
	}
	
	public static JSONArray returnCompanyMsgByCpx(List<Object[]> res, String gjz) {
		JSONArray ja = new JSONArray();
		// 累计 求合计
		// 委托单量
		BigDecimal twtdl = new BigDecimal(0);
		// 预测收入
		BigDecimal tycsr = new BigDecimal(0);
		// 收入总额
		BigDecimal tsrze = new BigDecimal(0);
		// 支出总额
		BigDecimal tzcze = new BigDecimal(0);
		
		// 大宗贸易 ja
		JSONObject dzmyja = new JSONObject();
		// 大宗贸易 对应children
		JSONArray dzmyChildren = new JSONArray();
		// 农食安全及溯源 ja
		JSONObject nsaqja = new JSONObject();
		// 农食安全及溯源 对应children
		JSONArray nsaqChildren = new JSONArray();
		// 工业 ja
		JSONObject gyja = new JSONObject();
		// 工业 对应children
		JSONArray gyChildren = new JSONArray();
		// 消费品 ja
		JSONObject xfpja = new JSONObject();
		// 消费品 对应children
		JSONArray xfpChildren = new JSONArray();
		// 政府与机构业务 ja
		JSONObject zfjgja = new JSONObject();
		// 政府与机构业务 对应children
		JSONArray zfjgChildren = new JSONArray();
		// 认证服务与企业优化 ja
		JSONObject rzqyja = new JSONObject();
		// 认证服务与企业优化 对应children
		JSONArray rzqyChildren = new JSONArray();
		// 其他 ja
		JSONObject qtja = new JSONObject();
		// 其他 对应children
		JSONArray qtChildren = new JSONArray();
		// 其他支出项 ja
		JSONObject qtzcja = new JSONObject();
		// 其他支出项 对应children
		JSONArray qtzcChildren = new JSONArray();
		
		if(res != null && res.size() > 0) {
			for (int i = 0; i < res.size(); i++) {
				Object[] obj = res.get(i);
				
				// 一级产品线 名字
				String yjcpx = (String) obj[0];
				// 二级产品线名字
				String ejcpx = (String) obj[1];
				// 委托单量
				BigDecimal wtdl = (BigDecimal) obj[2];
				// 预测收入
				BigDecimal ycsr = (BigDecimal) obj[3];
				// 收入总额
				BigDecimal srze = (BigDecimal) obj[4];
				// 支出总额
				BigDecimal zcze = (BigDecimal) obj[5];
				
				JSONObject hang = setValueToJobj(ejcpx, wtdl, ycsr, srze, zcze);
				
				if(yjcpx.equals("大宗贸易")) {
					dzmyja = setParentJobj(dzmyja, hang);
					dzmyja.put("name", "大宗贸易");
					dzmyChildren.add(hang);
				}else if(yjcpx.equals("农食安全及溯源")) {
					nsaqja = setParentJobj(nsaqja, hang);
					nsaqja.put("name", "农食安全及溯源");
					nsaqChildren.add(hang);
				}else if(yjcpx.equals("工业")) {
					gyja = setParentJobj(gyja, hang);
					gyja.put("name", "工业");
					gyChildren.add(hang);
				}else if(yjcpx.equals("消费品")) {
					xfpja = setParentJobj(xfpja, hang);
					xfpja.put("name", "消费品");
					xfpChildren.add(hang);
				}else if(yjcpx.equals("政府与机构业务")) {
					zfjgja = setParentJobj(zfjgja, hang);
					zfjgja.put("name", "政府与机构业务");
					zfjgChildren.add(hang);
				}else if(yjcpx.equals("认证服务与企业优化")) {
					rzqyja = setParentJobj(rzqyja, hang);
					rzqyja.put("name", "认证服务与企业优化");
					rzqyChildren.add(hang);
				}else if(yjcpx.equals("其他")) {
					qtja = setParentJobj(qtja, hang);
					qtja.put("name", "其他");
					qtChildren.add(hang);
				}else if(yjcpx.equals("其他支出项")) {
					qtzcja = setParentJobj(qtzcja, hang);
					qtzcja.put("name", "其他支出项");
					qtzcChildren.add(hang);
				}
				
				twtdl = twtdl.add(wtdl);
				tycsr = twtdl.add(ycsr);
				tsrze = twtdl.add(srze);
				tzcze = twtdl.add(zcze);
			}
		}
		
		ja.add( setValueToJobjFinal ( setValueToJobj("合计", twtdl, tycsr, tsrze, tzcze)) );
		
		if(dzmyChildren.size() > 0) {
			for (int i = 0; i < dzmyChildren.size(); i++) {
				dzmyChildren.set(i, setValueToJobjFinal((JSONObject) dzmyChildren.get(i)));
			}
			
			dzmyja = setValueToJobjFinal(dzmyja);
			dzmyja.put("children", dzmyChildren);
			ja.add(dzmyja);
		}

		if(nsaqChildren.size() > 0) {
			for (int i = 0; i < nsaqChildren.size(); i++) {
				nsaqChildren.set(i, setValueToJobjFinal((JSONObject) nsaqChildren.get(i)));
			}

			nsaqja = setValueToJobjFinal(nsaqja);
			nsaqja.put("children", nsaqChildren);
			ja.add(nsaqja);
		}

		if(gyChildren.size() > 0) {
			for (int i = 0; i < gyChildren.size(); i++) {
				gyChildren.set(i, setValueToJobjFinal((JSONObject) gyChildren.get(i)));
			}

			gyja = setValueToJobjFinal(gyja);
			gyja.put("children", gyChildren);
			ja.add(gyja);
		}

		if(xfpChildren.size() > 0) {
			for (int i = 0; i < xfpChildren.size(); i++) {
				xfpChildren.set(i, setValueToJobjFinal((JSONObject) xfpChildren.get(i)));
			}

			xfpja = setValueToJobjFinal(xfpja);
			xfpja.put("children", xfpChildren);
			ja.add(xfpja);
		}

		if(zfjgChildren.size() > 0) {
			for (int i = 0; i < zfjgChildren.size(); i++) {
				zfjgChildren.set(i, setValueToJobjFinal((JSONObject) zfjgChildren.get(i)));
			}

			zfjgja = setValueToJobjFinal(zfjgja);
			zfjgja.put("children", zfjgChildren);
			ja.add(zfjgja);
		}

		if(rzqyChildren.size() > 0) {
			for (int i = 0; i < rzqyChildren.size(); i++) {
				rzqyChildren.set(i, setValueToJobjFinal((JSONObject) rzqyChildren.get(i)));
			}

			rzqyja = setValueToJobjFinal(rzqyja);
			rzqyja.put("children", rzqyChildren);
			ja.add(rzqyja);
		}

		if(qtChildren.size() > 0) {
			for (int i = 0; i < qtChildren.size(); i++) {
				qtChildren.set(i, setValueToJobjFinal((JSONObject) qtChildren.get(i)));
			}

			qtja = setValueToJobjFinal(qtja);
			qtja.put("children", qtChildren);
			ja.add(qtja);
		}

		if(qtzcChildren.size() > 0) {
			for (int i = 0; i < qtzcChildren.size(); i++) {
				qtzcChildren.set(i, setValueToJobjFinal((JSONObject) qtzcChildren.get(i)));
			}
			
			qtzcja = setValueToJobjFinal(qtzcja);
			qtzcja.put("children", qtzcChildren);
			ja.add(qtzcja);
		}
		
		return ja;
	}
	
	public static JSONObject setValueToJobj(String name, BigDecimal wtdl, BigDecimal ycsr,
			BigDecimal srze, BigDecimal zcze) {
		JSONObject res = new JSONObject();
		
		res.put("name", name);
		res.put("wtdl", wtdl);
		res.put("ycsr", ycsr);
		res.put("srze", srze);
		res.put("zcze", zcze);
		
		return res;
	}
	
	public static JSONObject setValueToJobjFinal(JSONObject jobj) {
		JSONObject res = new JSONObject();
		
		res.put("name", jobj.get("name"));
		res.put("wtdl", NewUtils.returnYiOrWan((BigDecimal) jobj.get("wtdl"), 0));
		res.put("ycsr", NewUtils.returnYiOrWan((BigDecimal) jobj.get("ycsr"), 0));
		res.put("srze", NewUtils.returnYiOrWan((BigDecimal) jobj.get("srze"), 0));
		res.put("zcze", NewUtils.returnYiOrWan((BigDecimal) jobj.get("zcze"), 0));
		
		return res;
	}
	
	public static JSONObject setParentJobj(JSONObject parent, JSONObject child) {
		JSONObject res = new JSONObject();
		if(parent.get("wtdl") == null || parent.get("wtdl").equals("")) {
			res.put("name", child.get("name"));
			res.put("wtdl", child.get("wtdl"));
			res.put("ycsr", child.get("ycsr"));
			res.put("srze", child.get("srze"));
			res.put("zcze", child.get("zcze"));
			return res;
		}
		
		// 委托单量
		BigDecimal pwtdl = new BigDecimal(parent.get("wtdl").toString());
		// 预测收入
		BigDecimal pycsr = new BigDecimal(parent.get("ycsr").toString());
		// 收入总额
		BigDecimal psrze = new BigDecimal(parent.get("srze").toString());
		// 支出总额
		BigDecimal pzcze = new BigDecimal(parent.get("zcze").toString());
		
		// 委托单量
		BigDecimal cwtdl = new BigDecimal(child.get("wtdl").toString());
		// 预测收入
		BigDecimal cycsr = new BigDecimal(child.get("ycsr").toString());
		// 收入总额
		BigDecimal csrze = new BigDecimal(child.get("srze").toString());
		// 支出总额
		BigDecimal czcze = new BigDecimal(child.get("zcze").toString());
		
		res.put("wtdl", pwtdl.add(cwtdl));
		res.put("ycsr", pycsr.add(cycsr));
		res.put("srze", psrze.add(csrze));
		res.put("zcze", pzcze.add(czcze));
		
		return res;
	}
	
	/**
	 * 根据查询结果 返回对应的公司信息列表
	 * @param res
	 * @return
	 */
	public static JSONArray returnCompanyMsgByCpxName(List<Object[]> res) {
		JSONArray ja = new JSONArray();
		
		// 累计 求合计
		// 委托单量
		BigDecimal twtdl = new BigDecimal(0);
		// 预测收入
		BigDecimal tycsr = new BigDecimal(0);
		// 收入总额
		BigDecimal tsrze = new BigDecimal(0);
		// 支出总额
		BigDecimal tzcze = new BigDecimal(0);
		
		ja.add(new JSONObject());
		
		for (int i =0; i < res.size(); i++) {
			Object[] reso = res.get(i);
			
			// 名称为空 直接下一条
			if(reso[0] == null || reso[0].toString().equals("")) {
				continue;
			}
			
			// 公司名称
			String gsname = reso[0].toString();
			// 委托单量
			BigDecimal wtdl = (BigDecimal) reso[1];
			// 预测收入
			BigDecimal ycsr = (BigDecimal) reso[2];
			// 收入总额
			BigDecimal srze = (BigDecimal) reso[3];
			// 支出总额
			BigDecimal zcze = (BigDecimal) reso[4];
			
			ja.add(setValueToJobjFinal( setValueToJobj(gsname, wtdl, ycsr, srze, zcze) ));
			
			twtdl = twtdl.add(wtdl == null ? new BigDecimal(0) : wtdl);
			tycsr = tycsr.add(ycsr == null ? new BigDecimal(0) : ycsr);
			tsrze = tsrze.add(srze == null ? new BigDecimal(0) : srze);
			tzcze = tzcze.add(zcze == null ? new BigDecimal(0) : zcze);
		}
		
		ja.set(0, setValueToJobjFinal( setValueToJobj("合计", twtdl, tycsr, tsrze, tzcze) ));
		
		return ja;
	}

	/**
	 * 根据关键字 返回公司信息json数组
	 * @param res 公司信息
	 * @param gjz 父节点关键字
	 * @return
	 */
	public static JSONArray returnCompanyMsgForCpxJs(List<Object[]> res, String gjz) {
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
			
			// 委托金额
			String wtje = NewUtils.returnYiOrWan((BigDecimal) rdata[2], 0);
			// 开票收入
			String kpsr = NewUtils.returnYiOrWan((BigDecimal) rdata[3], 0);
			// 成本总额
			String cbze = NewUtils.returnYiOrWan((BigDecimal) rdata[4], 0);

			if(cur2.equals(company2) && company3.equals(gjz)) {
				parent.put("id", company2);
				parent.put("name", company2);
				parent.put("wtje", wtje);
				parent.put("kpsr", kpsr);
				parent.put("cbze", cbze);				
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
				parent.put("name", company2);
				parent.put("wtje", wtje);
				parent.put("kpsr", kpsr);
				parent.put("cbze", cbze);					
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
			child.put("name", company3);
			child.put("wtje", wtje);
			child.put("kpsr", kpsr);
			child.put("cbze", cbze);	
			
			// 将child插入到父节点的children中
			children.add(child);
		}
		
		// 最后将parent和 children 加入
		parent.put("children", children);
		// 先将父节点add进数组ja
		ja.add(parent);
		
		return ja;
	}

	public static JSONObject returnYwjiqkIntMsg(String name, String name1, Object y, Object d,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		int year =  y == null ? 0 : ((BigDecimal) y).intValue(), 
			day =  d == null ? 0: ((BigDecimal) d).intValue();
		if(name ==null || name == "") {
			return jo;
		}
		
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("bword",name1);
		jo.put("bnum", "+"+( day > yi ? (day/yi)  + "亿" : ( day > wan ? (day/wan) + "万" : day) ));
		jo.put("unit", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		jo.put("num", year > yi ? df.format( (float)year/yi ) : ( year > wan ? df.format( (float)year/wan ) : year) );
		
		
		return jo;
	}
	public static JSONObject returnYwjiqkIntMsg1(String name,  Object y, 
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		int year =  y == null ? 0 : ((BigDecimal) y).intValue(); 
		if(name ==null || name == "") {
			return jo;
		}
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("unit", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		jo.put("num", year > yi ? df.format( (float)year/yi ) : ( year > wan ? df.format((float) year/wan ) : year) );
		
		
		return jo;
	}
	public static JSONObject returnYwjiqkDoubleMsg1(String name,  Object y, 
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		double year =  y == null ? 0d : ((BigDecimal) y).doubleValue(); 
		if(name ==null || name == "") {
			return jo;
		}
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("unit", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		jo.put("num", year > yi ? df.format( year/yi ) : ( year > wan ? df.format( year/wan ) : year) );
		
		
		return jo;
	}
	
	public static JSONObject returnYwjiqkDoubleMsg2(String name, Object d,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		double  day =  d == null ? 0 : ((BigDecimal) d).doubleValue();
		if(name ==null || name == "") {
			return jo;
		}
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		
		jo.put("bword",name);
		jo.put("bnum", "+"+( day > yi ? df.format(day/yi)  + "亿" : ( day > wan ? df.format(day/wan) + "万" : day) ));
		
		return jo;
	}
	
	/**
	 * 根据查询的list 返回 重点服务技术中类收入饼图
	 * @param res
	 * @return
	 */
	public static JSONArray returnSrpieCpx(List<Object[]> res) {
		JSONArray ja = new JSONArray();
		// 支取最多5个数据
		int len = res.size() > 5 ? 5 : res.size();
		
		for (int i = 0; i < len; i++) {
			Object[] reso = res.get(i);
			
			// 名称为空 直接下一条
			if(reso[0] == null || reso[0].toString().equals("")) {
				continue;
			}
			
			// 名称
			String name = reso[0].toString();
			// 委托单量
			BigDecimal value = (BigDecimal) reso[1];
			JSONObject child = new JSONObject();
			
			child.put("name", name);;
			child.put("value", value);
			
			ja.add(child);
		}
		
		return ja;
	}
	
	public static JSONObject returnYwjiqkDoubleMsg(String name, String name1, Object y, Object d,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		double year =  y == null ? 0d : ((BigDecimal) y).doubleValue(), 
			day =  d == null ? 0d : ((BigDecimal) d).doubleValue();
		if(name ==null || name == "") {
			return jo;
		}
		
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("bword",name1);
		jo.put("bnum", (name.equals("产品线毛利率")?"":"+"+( day > yi ? (int)(day/yi)  + "亿" : ( day > wan ? (int)(day/wan) + "万" : day) )) );
		jo.put("unit", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		jo.put("num", year > yi ? df.format( year/yi ) : ( year > wan ? df.format( year/wan ) : year) );
		
		
		return jo;
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
	public static JSONObject returnTodayTotalDoubleMsg(String name,  Object y, Object d,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		JSONObject total = new JSONObject();
		double year =  y == null ? 0 : ((BigDecimal) y).doubleValue(), 
			day =  d == null ? 0 : ((BigDecimal) d).doubleValue();
		if(name ==null || name == "") {
			return jo;
		}
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("bnum", (name.indexOf("毛利率") > -1?"":"+"+( day > yi ? (int)(day/yi)  + "亿" : ( day > wan ? (int)(day/wan) + "万" : day) )) );
		jo.put("unit", (year > yi ? "亿" : ( year > wan ? "万" : "" )));
		jo.put("num", year > yi ? df.format( year/yi ) : ( year > wan ? df.format( year/wan ) : year) );
		
		if(name.indexOf("毛利率") > -1) {
			jo.put("num", jo.get("num") + "%");
		}
		
		return jo;
	}
	
	public static JSONArray newReturnCompanyMsg(List<Object[]> list) {
		JSONArray res = new JSONArray();
		
		if(list == null || list.size() == 0) {
			return res;
		}
		
		// 存放父节点
		JSONArray parent = new JSONArray();
		// 存放父节点的子节点
		JSONArray sca = new JSONArray();
		// 用于累计合计
		// 委托单量
		BigDecimal twtdl = new BigDecimal(0);
		// 预测收入
		BigDecimal tycsr = new BigDecimal(0);
		// 收入总额
		BigDecimal tsrze = new BigDecimal(0);
		// 支出总额
		BigDecimal tzcze = new BigDecimal(0);
		
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = list.get(i);
			
			// 父节点名称 -- 二级公司名
			String pname = (String) obj[0];
			// 子节点名称 -- 三级公司名
			String cname = (String) obj[1];
			// 委托单量
			BigDecimal wtdl = (BigDecimal) obj[2];
			// 预测收入
			BigDecimal ycsr = (BigDecimal) obj[3];
			// 收入总额
			BigDecimal srze = (BigDecimal) obj[4];
			// 支出总额
			BigDecimal zcze = (BigDecimal) obj[5];
			
			if(parent.size() == 0) {
				JSONObject p = new JSONObject();
				
				p.put("id", pname);
				p.put("name", pname);
				
				parent.add(p);
				
				JSONArray pc = new JSONArray();
				JSONObject c = new JSONObject();
				
				c.put("id", cname);
				c.put("name", cname);
				c.put("wtdl", wtdl);
				c.put("ycsr", ycsr);
				c.put("srze", srze);
				c.put("zcze", zcze);
				
				pc.add(c);
				sca.add(pc);
			}else {
				boolean flag = true;
				for (int j = 0; j < parent.size(); j++) {
					if( ( (JSONObject)parent.get(j) ).get("name").equals(pname) ) {
						// 向其对应的子节点中添加节点
						JSONObject c = new JSONObject();
						
						c.put("id", cname);
						c.put("name", cname);
						c.put("wtdl", wtdl);
						c.put("ycsr", ycsr);
						c.put("srze", srze);
						c.put("zcze", zcze);
						
						( (JSONArray) sca.get(j) ).add(c);
						flag = false;
						break;
					}
				}
				
				if(flag) {
					JSONObject p = new JSONObject();
					
					p.put("id", pname);
					p.put("name", pname);
					
					parent.add(p);
					
					JSONArray pc = new JSONArray();
					JSONObject c = new JSONObject();
					
					c.put("id", cname);
					c.put("name", cname);
					c.put("wtdl", wtdl);
					c.put("ycsr", ycsr);
					c.put("srze", srze);
					c.put("zcze", zcze);
					
					pc.add(c);
					sca.add(pc);
				}
			}
			
			twtdl = twtdl.add(wtdl);
			tycsr = tycsr.add(ycsr);
			tsrze = tsrze.add(srze);
			tzcze = tzcze.add(zcze);
		}
		
		// 计算合计 并加入
		JSONObject hj = new JSONObject();
		
		hj.put("id", "合计");
		hj.put("name", "合计");
		hj.put("wtdl", NewUtils.returnYiOrWan(twtdl, 0));
		hj.put("ycsr", NewUtils.returnYiOrWan(tycsr, 0));
		hj.put("srze", NewUtils.returnYiOrWan(tsrze, 0));
		hj.put("zcze", NewUtils.returnYiOrWan(tzcze, 0));
		
		res.add(hj);
		
		// 循环parent 加入res
		for (int i = 0; i < parent.size(); i++) {
			JSONObject p = parent.getJSONObject(i);
			
			// 循环sca.getJSONArray(i) 计算合计
			// 委托单量
			twtdl = new BigDecimal(0);
			// 预测收入
			tycsr = new BigDecimal(0);
			// 收入总额
			tsrze = new BigDecimal(0);
			// 支出总额
			tzcze = new BigDecimal(0);
			
			for (int j = 0; j < sca.getJSONArray(i).size(); j++) {
				JSONObject c = sca.getJSONArray(i).getJSONObject(j);
				
				// 累加
				twtdl = twtdl.add((BigDecimal) c.get("wtdl"));
				tycsr = tycsr.add((BigDecimal) c.get("ycsr"));
				tsrze = tsrze.add((BigDecimal) c.get("srze"));
				tzcze = tzcze.add((BigDecimal) c.get("zcze"));
				
				// 设置单位
				c.put("wtdl", NewUtils.returnYiOrWan((BigDecimal) c.get("wtdl"), 0));
				c.put("ycsr", NewUtils.returnYiOrWan((BigDecimal) c.get("ycsr"), 0));
				c.put("srze", NewUtils.returnYiOrWan((BigDecimal) c.get("srze"), 0));
				c.put("zcze", NewUtils.returnYiOrWan((BigDecimal) c.get("zcze"), 0));
				
				sca.getJSONArray(i).set(j, c);
			}
			
			// 设置单位
			p.put("wtdl", NewUtils.returnYiOrWan(twtdl, 0));
			p.put("ycsr", NewUtils.returnYiOrWan(tycsr, 0));
			p.put("srze", NewUtils.returnYiOrWan(tsrze, 0));
			p.put("zcze", NewUtils.returnYiOrWan(tzcze, 0));
			
			// 用于排序
			p.put("ycsrpx", tycsr);
			
			p.put("children", sca.getJSONArray(i));
			
			res.add(p);
		}
		
		// 对res 根据ycsr排序
		for (int i = 1; i < res.size(); i++) {
			JSONObject ji = res.getJSONObject(i);
			for (int j = i+1; j < res.size(); j++) {
				JSONObject jj = res.getJSONObject(j);
				
				if( ( (BigDecimal) ji.get("ycsrpx") ).doubleValue() < ( (BigDecimal) jj.get("ycsrpx") ).doubleValue() ) {
					JSONObject ls = new JSONObject();
					
					ls = jj;
					
					res.set(j, ji);
					res.set(i, ls);
					ji = ls;
				}
			}
		}
		
		return res;
	}
	
	public static JSONArray returnYwskMsgSql1(List<Object[]> list) {
		JSONArray res = new JSONArray();
		
		if(list == null || list.size() == 0) {
			return res;
		}
		
		// 结果行 
		Object[] result = list.get(0);
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat("#.00");
		// 分为五组数据
		// 委托单量
		res.add(NewUtils.returnYwjiqkDoubleMsg("委托单量", "今日", result[0], result[5], df));
		// 开工单量
		res.add(NewUtils.returnYwjiqkDoubleMsg("开工单量", "今日", result[2], result[7], df));
		// 完工单量
		res.add(NewUtils.returnYwjiqkDoubleMsg("完工单量", "今日", result[3], result[8], df));
		// 委托金额
		res.add(NewUtils.returnYwjiqkDoubleMsg("委托金额", "今日", result[1], result[6], df));
		// 预测收入
		res.add(NewUtils.returnYwjiqkDoubleMsg("预测收入", "今日", result[4], result[9], df));
		
		return res;
	}
	
	public static JSONObject returnYwskMsgSql2(List<BigDecimal> list) {
		JSONObject res = new JSONObject();
		
		if(list == null || list.size() == 0) {
			return res;
		}
		
		// 结果行 
		BigDecimal result = list.get(0);
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat("#.00");
		// 分为五组数据
		// 产品毛利率
		res.put("word", "产品线毛利率");
		res.put("num", result+"%");
		res.put("unit", "");
		res.put("bword", "");
		res.put("bnum", "");
		
		return res;
	}
	
	public static JSONObject returnYwskMsgSql3(List<Object[]> list, int type) {
		JSONObject res = new JSONObject();
		
		if(list == null || list.size() == 0) {
			return res;
		}
		
		// 结果行 
		Object[] result = list.get(0);
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat("#.00");
		// 客户数量
		res = NewUtils.returnYwjiqkDoubleMsg("客户数量", "今日", result[0], result[1], df);
		// detail
		JSONArray dl = new JSONArray();
		
		dl.add(NewUtils.returnYwjiqkIntMsg1("产品线活跃客户数量", result[2], df));
		dl.add(NewUtils.returnYwjiqkIntMsg1("非产品线活跃客户数量", result[3], df));
		if(type == 0) {
			dl.add(NewUtils.returnYwjiqkIntMsg1("非活跃客户数量", result[5], df));
		}
		
		res.put("detail", dl);
		
		return res;
	}
	
	public static JSONObject returnYwskMsgSql4(List<Object[]> list, int type) {
		JSONObject res = new JSONObject();
		
		if(list == null || list.size() == 0) {
			return res;
		}
		
		// 结果行 
		Object[] result = list.get(0);
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat("#.00");
		// 供应商数量
		res = NewUtils.returnYwjiqkDoubleMsg("供应商数量", "今日", result[0], result[3], df);
		// detail
		JSONArray dl = new JSONArray();
		
		dl.add(NewUtils.returnYwjiqkIntMsg1("产品线活跃供应商数量", result[1], df));
		dl.add(NewUtils.returnYwjiqkIntMsg1("非产品线活跃供应商数量", result[2], df));
		if(type == 0) {
			dl.add(NewUtils.returnYwjiqkIntMsg1("非活跃供应商数量", result[4], df));
		}
		
		res.put("detail", dl);
		
		return res;
	}
	
	public static JSONObject returnYwskMsgSql5(List<Object[]> list) {
		JSONObject res = new JSONObject();
		
		if(list == null || list.size() == 0) {
			return res;
		}
		
		// 结果行 
		Object[] result = list.get(0);
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat("#.00");
		// 出证数量
		res = NewUtils.returnYwjiqkDoubleMsg("出证数量", "今日", result[0], result[1], df);
		// detail
		JSONArray dl = new JSONArray();
		
		dl.add(NewUtils.returnYwjiqkIntMsg1("证书数量", result[2], df));
		dl.add(NewUtils.returnYwjiqkIntMsg1("其他成果物数量", result[3], df));
		
		res.put("detail", dl);
		
		return res;
	}
	
	public static JSONObject returnYwskMsgSql1Jt(List<Object[]> list, String name) {
		if(list == null || list.size() == 0) {
			return new JSONObject();
		}
		
		// 结果行 
		Object[] result = list.get(0);
		// 四舍五入保留2位小数
		DecimalFormat df = new DecimalFormat("#.00");
		
		return NewUtils.returnYwjiqkDoubleMsg(name, "今日", result[0], result[1], df);
	}	
}
