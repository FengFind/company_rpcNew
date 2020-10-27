package com.newtec.company.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("unchecked")
public class XmlBase64ToFile {

	public static void main(String[] args) {
		try {
//			findBase64AndConvertToFile("F:/hgcsbw/loaded/test", "F:/hgcsbw/loaded/testpdf");
			findBase64AndSaveToTxt("F:/hgcsbw/loaded/OutBox", "F:/hgcsbw/loaded/txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * 返回 CertNo 和 FileName 信息
	 * @param source 文件夹地址
	 * @return
	 */
	public static File findFileNameByEdocID(String source, String edocID) {
		try {
			// 文件夹对象
			File dir = new File(source);
			// xml列表
			File[] files = dir.listFiles();
			// 循环files
			for (int i = 0; i < files.length; i++) {
//				System.out.println("  ----------  "+i+" ------------ ");
				// 判断文件名是否包含certNo
				String fileName = files[i].getAbsolutePath();
				// 对xml中内容进行循环查找出文件名称和base64编码
				JSONObject jo = findFileNameAndBase64ByFile(files[i]);
				// 如果jo中FileName 为空 则继续进行下一个文件
				if(jo.getString("UuId") == null || jo.getString("UuId").equals("")) {
					continue ;
				}
				
				if(edocID.equals(jo.getString("UuId"))) {
					System.out.println(jo.getString("UuId"));
					return files[i];
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 返回 CertNo 和 FileName 信息
	 * @param source 文件夹地址
	 * @return
	 */
	public static JSONObject isExistPdf(String source, String certNo) {
		JSONObject ls = new JSONObject();
		
		try {
			// 文件夹对象
			File dir = new File(source);
			// xml列表
			File[] files = dir.listFiles();
			// 循环files
			for (int i = 0; i < files.length; i++) {
//				System.out.println("  ----------  "+i+" ------------ ");
				// 判断文件名是否包含certNo
				String fileName = files[i].getName();
				if(fileName.indexOf(certNo) > 0) {
					ls.put("FileName", files[i].getName().substring(0, files[i].getName().lastIndexOf("_")));
					ls.put("base64", PDFBinaryConvert.findStringFromTxt(files[i].getAbsolutePath()));
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ls;
	}

	/**
	 * 将source文件夹内的xml中的base64编码转换成文件
	 * @param source 文件夹路径
	 * @param dest 目标文件夹路径
	 */
	public static void findBase64AndSaveToTxt(String source, String dest) throws Exception {
		// 文件夹对象
		File dir = new File(source);
		// xml列表
		File[] files = dir.listFiles();
		// 循环files
		for (int i = 0; i < files.length; i++) {
			// xml
			File xml = files[i];
			// 对xml中内容进行循环查找出文件名称和base64编码
			JSONObject jo = findFileNameAndBase64ByFile(xml);
			// 如果jo中FileName 为空 则继续进行下一个文件
			if(jo.getString("FileName") == null || jo.getString("FileName").equals("")) {
				continue ;
			}
			// 将信息写入txt文件中 
			writeMsgToTxt(jo, dest);
//			System.out.println(jo.getString("FileName") + "                           " +  jo.getString("CertNo"));
			System.out.println("-----------  第"+(i+1)+"个转换完成  ");
		}
	}
	
	/**
	 * 将信息存入txt
	 * @param jo
	 * @param dest
	 */
	public static void writeMsgToTxt(JSONObject jo, String dest) {
		// 拼接文件名称 以FileName_CertNo为文件名称
		String txtName = jo.getString("FileName")+"_"+jo.getString("CertNo")+".txt";
		// base64 
		String base64 = jo.getString("base64");
		
		PDFBinaryConvert.writeContentToTxt(dest + File.separator + txtName, base64);
	}
	
	/**
	 * 将source文件夹内的xml中的base64编码转换成文件
	 * @param source 文件夹路径
	 * @param dest 目标文件夹路径
	 */
	public static void findBase64AndConvertToFile(String source, String dest) throws Exception {
		// 文件夹对象
		File dir = new File(source);
		// xml列表
		File[] files = dir.listFiles();
		// 循环files
		for (int i = 0; i < files.length; i++) {
			// xml
			File xml = files[i];
			// 对xml中内容进行循环查找出文件名称和base64编码
			JSONObject jo = findFileNameAndBase64ByFile(xml);
			// 如果jo中FileName 为空 则继续进行下一个文件
			if(jo.getString("FileName") == null || jo.getString("FileName").equals("")) {
				continue ;
			}
			// 对base64进行转换
			convertBase64ToFile(jo, dest);
//			System.out.println(jo.getString("FileName") + "                           " +  jo.getString("CertNo"));
			System.out.println("-----------  第"+(i+1)+"个转换完成  ");
		}
	}
	
	/**
	 * 跟file文件查询出文件名称和base64编码
	 * @param file
	 * @return
	 */
	public static JSONObject findFileNameAndBase64ByFile(File file) throws Exception {
		JSONObject jo = new JSONObject();
		
		// 先将standalone="true" 去掉
		PDFBinaryConvert.writeContentToTxt(file.getAbsolutePath(), PDFBinaryConvert.findStringFromTxt(file.getAbsolutePath()).replace("standalone=\"true\"", ""));
		
		// xml 对应 document 对象
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		
		if(doc == null) {
			return jo;
		}
		
		// 对document 的子节点 进行循环 
		// 如果子节点有children 继续循环
		Element root = doc.getRootElement();
		Iterator<Element> firstIterator = root.elementIterator();
		jo = findDocumentAttrFileNameAndBase64(root, firstIterator, jo);				
		
		return jo;
	}
	
	/**
	 * 循环document中的子节点 查找出 FileName 和 File
	 * @param r
	 * @param firstIterator
	 * @return
	 */
	public static JSONObject findDocumentAttrFileNameAndBase64(Element r, Iterator<Element> firstIterator, JSONObject jo) {
		boolean flag = true;
		
		while(firstIterator.hasNext()) {
			flag = false;
			Element e= firstIterator.next();
			Iterator<Element> siet = e.elementIterator();
			
			findDocumentAttrFileNameAndBase64(e, siet, jo);
		}
		
		if(flag) {
			if(r.getName().equals("FileName")) {
				jo.put("FileName", r.getText());
			}else if(r.getName().equals("File")) {
				jo.put("base64", r.getText());
			}else if(r.getName().equals("CertNo")) {
				jo.put("CertNo", r.getText());
			}else if(r.getName().equals("UuId")) {
				jo.put("UuId", r.getText());
			}else if(r.getName().equals("ResultInfo")) {
				jo.put("ResultInfo", r.getText());
			}
		}
		
		return jo;
	}
	
	public static void convertBase64ToFile(JSONObject jo, String dest) {
		// 文件名称
		String fileName = jo.getString("FileName");
		// base64
		String base64 = jo.getString("base64");
		// 转换
		PDFBinaryConvert.base64StringToPDF(base64, dest+File.separator+fileName);
		
		// 设置 pdf的分辨率
		
	}
}
