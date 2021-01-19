package com.newtec.company.utils;

import java.io.BufferedReader;
import java.io.FileReader;

import com.alibaba.fastjson.JSONArray;

public class CreatePsiCertAndCliDoc {

	public static void main(String[] args) {
		createPsiCertCliDoc();
//		PDFBinaryConvert.writeContentToTxt("F:/hgcsbw/ids.txt", "123");
	}
	
	public static void createPsiCertCliDoc() {
		// 先获取 委托关系编号
		JSONArray wtgx = findWtgx();
		// 先生成 doc 附件报文
		DataConvertToXmlZip.dataToZipWtgx("F:/工作内容/海关数据对接/测试报文样例/CliPsiEdoc_last.xml", "F:/hgcsbw/CliDoc", wtgx);
		// 再生成 psi 证书报文
		PsiIumeCertDataConvertToXmlZip.dataToZipWtgx("F:/工作内容/海关数据对接/测试报文样例/PsiIumeCert.xml", "F:/hgcsbw/PsiCert", wtgx);
	}
	
	public static JSONArray findWtgx() {
		JSONArray res = new JSONArray();
		// 文件地址
		String fliePath = "F:\\hgcsbw\\委托关系编号.txt";
		try {
			BufferedReader bf= new BufferedReader(new FileReader(fliePath));
			String s = null;
			while((s = bf.readLine())!=null){//使用readLine方法，一次读一行
			    if (s.trim() != null && !s.trim().equals("")) {
//			    	System.out.println(s.trim());
					res.add(s.trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(res.size());
		return res;
	}
}
