package com.newtec.company.utils;

import java.io.File;
import java.security.MessageDigest;

import com.alibaba.fastjson.JSONObject;

public class StringUtil {

	public static void main(String[] args) {
		try {
//			String str = StringUtil.MD5(s);
//			System.out.println(str); 
//			PDFBinaryConvert.base64StringToPDF(s, "F:/hgcsbw/Microsoft Word - 报告-非工程机械.pdf");
//			System.out.println(Md5Test.findMD5ByFilePath("F:\\hgcsbw\\loaded\\pdf\\Microsoft Word - 报告-工程机械-2019-5-1.pdf"));
			// 获取 已经存在的 pdf文件名称和base64
//			String exist = "F:/hgcsbw/loaded/txt";
//			// 存放信息的jsonobject
//			JSONObject ejo = XmlBase64ToFile.isExistPdf(exist, "2726584847872307392");
//			PDFBinaryConvert.base64StringToPDF(ejo.getString("base64"), "F:/hgcsbw/123.pdf");
//			File pdf = new File("F:/hgcsbw/123.pdf");
//			System.out.println(" size === " + pdf.length());
			
//			System.out.println("5.旧机电装运前检验证书-AU2000003AUUM.pdf".length());
			
			// 生成18位随机数
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 18; j++) {
					String str = "";
					int d = (int)(Math.random()*10) ;
					switch(d) {
						case 0:
							str += 0;
							break;
						case 1:
							str += 1;
							break;
						case 2:
							str += 2;
							break;
						case 3:
							str += 3;
							break;
						case 4:
							str += 4;
							break;
						case 5:
							str += 5;
							break;
						case 6:
							str += 6;
							break;
						case 7:
							str += 7;
							break;
						case 8:
							str += 8;
							break;
						case 9:
							str += 9;
							break;
					}
					
					System.out.print(str);
				}
				System.out.println();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//2C55E4D37720129B61F54AF936CDAEA9
			//2C55E4D37720129B61F54AF936CDAEA9
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
