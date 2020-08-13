/**   
* @Title: ExceptionUtils.java 
* @Package com.newtec.myqdp.server.utils 
* @Description: TODO(用一句话描述该文件做什么) 
* @author 王仕通 
* @date 2016-2-24 下午2:42:43 
* @version V1.0   
*/ 
package com.newtec.myqdp;

import java.util.HashMap;
import java.util.Map;

import com.newtec.myqdp.server.utils.StringUtils;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.myqdp.server.utils.exception.ExceptionKeyType;


/**
 * @author 王仕通
 * @date 2016-2-24 下午2:42:43 
 *@Description 异常工具类
 */
public class ExceptionUtils {

	
	public static void main(String[] args) {
		Map<String,String> exceptionMap = new HashMap<String,String>();
		exceptionMap.put("ORA-12899","字段长度不够");
		exceptionMap.put("唯一约束","无法添加站点，站点号或设备号已经存在了");
		exceptionMap.put("请确认在微信公众号摇一摇周边添加的","请确认在微信公众号摇一摇周边添加的设备信息与你所填的一致");
		dbExceptoin2CustomException(new CustomException("0-0-323232", "请确认在微信公众号摇一摇周边添加的设备信息与你所填的一致"), exceptionMap);
	}
	
	public static CustomException dbExceptoin2CustomException(Exception e) {
		return dbExceptoin2CustomException(e,"");
	}
	
	public static CustomException dbExceptoin2CustomException(Exception e1,Map<String,String> exceptionMap){
//		System.err.println("进入来了啊。。。。。。。。。。。。。。。。。");
		 CustomException  e = dbExceptoin2CustomException(e1);
//			System.err.println(e.getMessage()+">>exceptionMap==="+exceptionMap);
		 if(exceptionMap != null){
			 String message = e.getMessage();
			 System.err.println("==1"+message);
				for(String key : exceptionMap.keySet()){
					System.err.println("==2 "+key);
					if(message.contains(key) || (e.getDetailMessage()+"").contains(key)){
						return new CustomException(e.getKey(),exceptionMap.get(key),e.getDetailMessage());
					}
				}
		 }
		return e;
	}
	/**
	 * 方法说明：数据库异常转化成自定义异常
	 * @param e
	 * @return
	 */
	
	public static CustomException dbExceptoin2CustomException(Exception e,Object detailMessage) {
//		System.out.println("验证中的map:"+map);
		String message = e.getMessage();
//		System.err.println("----------------------start"+message);
			Throwable t = e.getCause();
			while(t != null){
				message = t.getMessage();
				//System.err.println("message======"+message);
				t= t.getCause();
			}
		String table = "";
		String field = "";
		if(StringUtils.isNull(detailMessage)) detailMessage = message+";"+e.getMessage();
		if(message.startsWith("ORA-00942")){
			return new CustomException(ExceptionKeyType.NotFoundTable,message,detailMessage);
		}else if(message.startsWith("ORA-01400") || message.startsWith("ORA-01407")){//出现空值异常，ORA-01400: 无法将 NULL 插入 ("SMS"."TEST2"."CODE")
//			String[] errors = message.split("\\.");
//			table = errors[1].substring(1,errors[1].length()-1);
//			field = errors[2].substring(1,errors[2].length()-3);
//			throw new CustomException("ORA-01400", "操作失败，["+key2value(table+"."+field, map)+"]不能为空！");
			
			String str = message.split("\\(")[1].split("\\)")[0].replaceAll("\"", "");
			System.err.println("="+str+"=");
			return new CustomException(str,message,detailMessage);
			
		}else if(message.startsWith("ORA-00904")){//字段不存在，ORA-00904: "SIGN2": 标识符无效
//			String errorField = message.substring(message.indexOf("\"")+1, message.lastIndexOf("\""));
			String errorField = message.substring(message.indexOf("\"")+1, message.lastIndexOf("\"")>0? message.lastIndexOf("\""):message.length());
			return new CustomException("ORA-00904","操作失败，不存在["+errorField+"]字段！");
		}else if(message.startsWith("ORA-00001")){//违反唯一约束，ORA-00001: 违反唯一约束条件 (SMS.CODE_UNIQUE)
			String str = message.split("\\(")[1].split("\\)")[0];
			System.err.println("="+str+"=");
			return new CustomException(ExceptionKeyType.Unique,message,detailMessage);
		}else if(message.startsWith("ORA-00957")){//ORA-00957: 重复的列名
			return new CustomException("ORA-00957","操作失败，出现重复的列名，请联系管理员",detailMessage);
		}else if(message.startsWith("ORA-02291")){//ORA-02291: 违反完整约束条件 (EXCEL.ASDF) - 未找到父项关键字,增加时违反外键约束
			return new CustomException(/*"ORA-02291"*/ExceptionKeyType.UniqueAdd,"操作失败，增加时违反外键约束，请联系管理员！",detailMessage);
		}else if(message.startsWith("ORA-02292")){//ORA-02292: 违反完整约束条件 (EXCEL.ASDF) - 已找到子记录,删除时违反外键约束
			return new CustomException("ORA-02292","操作失败，删除时违反外键约束，请联系管理员！",detailMessage);
		}else if(message.startsWith("ORA-12899")){//ORA-12899: 列 "FINANCE1"."TRAVEL"."PHONE" 的值太大 (实际值: 19, 最大值: 15)
			return new CustomException("ORA-12899","操作失败,字段值长度不够，请联系管理员！",message.replaceFirst("ORA-12899:", ""));
		}else{
			e.printStackTrace();
			return new CustomException(" ","操作失败，数据库未知错误！"+message);
		}
	}
	
}
