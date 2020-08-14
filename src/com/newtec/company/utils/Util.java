package com.newtec.company.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.alibaba.fastjson.JSONObject;

public class Util {

	public static void main(String[] args) {
		
	}

	public static JSONObject returnTodayTotalIntMsg(String name, String unit, int year, int day,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		JSONObject total = new JSONObject();
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("today", "+"+( day > yi ? df.format( (float)day/yi ) + "亿" : ( day > wan ? df.format( (float)day/wan ) + "万" : day) ));
		
		total.put("w", (year > yi ? "亿" : ( year > wan ? "万" : "" ))+ unit);
		total.put("n", year > yi ? df.format( (float)year/yi ) : ( year > wan ? df.format( (float)year/wan ) : year) );
		
		jo.put("total", total);
		
		return jo;
	}

	public static JSONObject returnTodayTotalDoubleMsg(String name, String unit, double year, double day,
			DecimalFormat df) {
		JSONObject jo = new JSONObject();
		JSONObject total = new JSONObject();
		// 亿 万
		int yi = 100000000, wan = 10000;
		
		jo.put("word", name);
		jo.put("today", "+"+( day > yi ? df.format( day/yi ) + "亿" : ( day > wan ? df.format( day/wan ) + "万" : day) ));
		
		total.put("w", (year > yi ? "亿" : ( year > wan ? "万" : "" ))+ unit);
		total.put("n", year > yi ? df.format( year/yi ) : ( year > wan ? df.format( year/wan ) : year) );
		
		jo.put("total", total);
		
		return jo;
	}
}
