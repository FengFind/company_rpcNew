package com.newtec.company.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSONArray;

public class PsiIumeCertDataConvertToXmlZip {

	// 通过数据库查询出 要导出的数据 生成xml 并打包成zip
	public static void dataToZip(String source, String dest) {
		try {
			SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			System.out.println(" 开始时间 " + sdf.format(new Date()));
			// 先将对应文件进行下载,并返回对应的数据库信息
			List<List<Object>> result = findDataFromDbTFile(dest, source);
			
			System.out.println(" 结束时间 " + sdf.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 查询数据库中需要导出的数据 --- t_file
	public static List<List<Object>> findDataFromDbTFile(String dest, String source) throws Exception {
		List<List<Object>> result = new ArrayList<List<Object>>();
		
		// 获取数据
		List<JSONArray> db = HaiguanUtils.findFileData();
		// 日期格式
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 计数 
		int js = 0;
		
		// 获取附件对应的ids
//		String ids = PDFBinaryConvert.findStringFromTxt("F:/hgcsbw/ids.txt");
		
		// 安装海运传输的xml格式模拟数据
		for (int i = 0; i < db.size(); i++) {
//		for (int i = 0; i < 3000; i++) {
			// 数据库查出来的一条数据
			JSONArray dbi = db.get(i);
//			JSONArray dbi = new JSONArray();
			
			// 判断是否存在于ids中
//			if(ids.indexOf(dbi.get(0).toString()) < 0) {
//				continue;
//			}
			
			// 需要组装成xml中对应的格式
			List<Object> rut = new ArrayList<Object>();
			
			// 测试数据			
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i));
			rut.add("申请人名称"+i);
			rut.add("申请人地址"+i);
			rut.add("收货人名称"+i);
			rut.add("收货人地址"+i);
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i));
			rut.add("产品名称"+i);
			rut.add(i);
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i).substring(0, 8));
			rut.add("计量单位名称"+i);
			rut.add("产品金额"+i);
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i).substring(0, 8));
			rut.add("币制名称"+i);
			rut.add("装运前检验机构"+i);
			rut.add(sdf.format(new Date()).replace(" ", "T")+"Z");
			rut.add("检验地点"+i);
			rut.add(dbi.get(0).toString().length() > 40 ? dbi.get(0).toString().substring(0, 40) : dbi.get(0).toString());
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i).substring(0, 8));
			rut.add(sdf.format(sdf.parse(dbi.getString(6))).replace(" ", "T")+"Z");
			rut.add("授权签字人"+i);
			rut.add(sdf.format(new Date()).replace(" ", "T")+"Z");
			rut.add("1");
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i));
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i));
			rut.add(i);
			rut.add("产品名称"+i);
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i));			
			rut.add(i);
			rut.add("产地"+i);
			rut.add(sdf.format(new Date()).replace(" ", "T")+"Z");
			rut.add("0");			
			rut.add(dbi.get(4).toString().length() > 64 ? dbi.get(4).toString().substring(0, 64) : dbi.get(4).toString());
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i).substring(0, 8));
			rut.add(i%3+"");
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i));
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i).substring(0, 8));
			rut.add(StringUtil.MD5(System.currentTimeMillis()+""+i).substring(0, 8));
			rut.add(sdf.format(new Date()).substring(0, 10));
			rut.add("所属单位名称"+i);
			rut.add(i);
			
			result.add(rut);
			
			if(js == 100) {
				// 将查询出的数据 封装到xml中
				setDataToXml(result, source, dest);
				
				js =0;
				result = new ArrayList<List<Object>>();
				continue;
			}
			
			js++;
		}
		
		// 将查询出的数据 封装到xml中
		setDataToXml(result, source, dest);
		
		// 将xml打包成.zip文件
		convertXmlsToZip(dest);
		// 转换成功之后将xml文件删除
		ZipUtils.delDirFiles(dest);
		
		return null;
	}
	
	// 将查询出的数据 封装到xml中
	public static boolean setDataToXml(List<List<Object>> result, String source, String dest) throws Exception{
		boolean flag = true;
		
		if(result != null && result.size() > 0) {
			for (int i = 0; i < result.size(); i++) {
				// 将source对应的document
				Document sdoc = getDocumentByFilePath(source);
				
				if(sdoc == null) {
					return false;
				}
				
				// 根据source路径下的xml格式，将result中的一条数据存入一个xml并保存到dest目录中
				if(!saveDataToXml(result.get(i), sdoc, dest, i)) {
					flag = false;
					break;
				}
			}
		}
		
		return flag;
	}
	
	// 将xml打包成.zip文件
	public static boolean convertXmlsToZip(String dest) {
		boolean flag = true;
		
		try {
			// zip名称
			String fileName = dest.substring(0, dest.lastIndexOf("/")) + File.separator + System.currentTimeMillis() + ".zip";
			
			// 创建文件
			File file = new File(fileName);
			file.createNewFile();
			// 文件夹
			File dir = new File(dest);
			
			// 输出流
			FileOutputStream fos1 = new FileOutputStream(file);
			ZipUtils.toZip(dir, fos1, true);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 将每条数据根据document的格式保存到新的xml中
	 * @param list
	 * @param sdoc
	 * @param dest
	 * @param i
	 * @return
	 */
	private static boolean saveDataToXml(List<Object> list, Document sdoc, String dest, int i) {
		boolean flag = true;
		
		if(list == null || list.size() == 0) {
			return false;
		}
		
		if(!setDataToDocument(list, sdoc, 0)) {
			return false;
		}
		
		if(!saveDocumentToXml(sdoc, dest, i)) {
			flag = false;
		}
		
		return flag;
	}

	/**
	 * 将document保存到xml中
	 * @param sdoc
	 * @param dest
	 * @param i 
	 */
	private static boolean saveDocumentToXml(Document sdoc, String dest, int i) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			// ** 设置xml编码 格式**
            format.setEncoding("UTF-8");
            // ** 设置自动换行 **
            format.setNewlines(true);
            // ** 设置缩进 **
            format.setIndent(true);
            // 保留空格
            format.setTrimText(false);
            // xml 名称
            String flieName = dest + File.separator + StringUtil.MD5(System.currentTimeMillis()+" "+i)+".xml";
            //设置本地保存路径
			File file = new File(flieName);
			
			if(!file.exists()) {
				file.createNewFile();
			}
            
            StandaloneWriter writer = new StandaloneWriter (new FileWriter(flieName),format);
            writer.write(sdoc);
            writer.close();            
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	/**
	 * 将一条数据设置到document中
	 * @param list 数据list
	 * @param sdoc document对象
	 * @param idx 序号 记录list已经设置到哪个位置
	 * @return
	 */
	private static boolean setDataToDocument(List<Object> list, Document sdoc, int idx) {
		boolean flag = true;
		
		// document 的子节点 进行循环 
		// 如果子节点有children 继续循环
		Element root = sdoc.getRootElement();
		Iterator<Element> firstIterator = root.elementIterator();
		
		try {
			setDataToEveryLeaf(root, firstIterator, list, 0);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * 将数据设置到每个子节点
	 * @param firstIterator 子节点数据对象
	 * @param list 数据list
	 * @param i 序号 记录list已经设置到哪个位置
	 */
	@SuppressWarnings("unchecked")
	private static int setDataToEveryLeaf(Element r, Iterator<Element> firstIterator, List<Object> list, int i) {		
		boolean flag = true;
		
		while(firstIterator.hasNext()) {
			flag = false;
			Element e= firstIterator.next();
			Iterator<Element> siet = e.elementIterator();
			
			i = setDataToEveryLeaf(e, siet, list, i);
		}
		
		if(flag) {
			r.setText(list.get(i) == null ? "" : list.get(i).toString());
			i++;
		}
		
		return i;
	}

	/**
	 * 根据文件路径 返回 对应的document对象
	 * @param source 文件路径
	 * @return
	 */
	private static Document getDocumentByFilePath(String source) {
		Document doc = null;
		try {
			File file = new File(source);
			SAXReader reader = new SAXReader();
			doc = reader.read(file);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static void main(String[] args) {
		PsiIumeCertDataConvertToXmlZip.dataToZip("F:/工作内容/海关数据对接/测试报文样例/PsiIumeCert.xml", "F:/hgcsbw/OutBox");
	}

}
