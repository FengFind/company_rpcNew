package com.newtec.company.utils;

import java.security.MessageDigest;

public class StringUtil {

	public static void main(String[] args) {
		try {
			String str = StringUtil.MD5("123456");
			System.out.println(str.length()); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 判断str 是否为空 或者 ""
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkStringEmpty(String str) {
		return str == null || str.equals("");
	}

	/**
	 * 判断str 是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkStringNotEmpty(String str) {
		return str != null && !str.equals("");
	}

	/**
	 * 判断str 是否在长度区间范围内
	 * 
	 * @param str
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean checkStringLength(String str, int from, int to) {
		if (StringUtil.checkStringEmpty(str)) {
			return false;
		}

		return str.length() >= from && str.length() <= to;
	}

	public static String MD5(String data) throws Exception {
		java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();

		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}

		return sb.toString().toUpperCase();
	}
}
