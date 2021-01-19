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
import com.alibaba.fastjson.JSONObject;

/**
 * 通过数据库查询出 要导出的数据
 * 将每条数据封装成一个xml文件
 * 将生成的xml打包成.zip文件 方便传输到服务器
 * @author Administrator
 *
 */
public class DataConvertToXmlZip {

	// 通过数据库查询出 要导出的数据 生成xml 并打包成zip
	// 生成附件 对应xml 并打包
	// 加入 委托关系编号的逻辑
	public static void dataToZipWtgx(String source, String dest, JSONArray wtgx) {
		try {
			SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			System.out.println(" 开始时间 " + sdf.format(new Date()));
			// 下载文件的地址
			String path = "https://cef-open.ccic.com/document/download?documentId=";
			// 先将对应文件进行下载,并返回对应的数据库信息
			List<List<Object>> result = findDataFromDbTFile(dest, path, source, wtgx);
			
			System.out.println(" 结束时间 " + sdf.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 通过数据库查询出 要导出的数据 生成xml 并打包成zip
		// 生成附件 对应xml 并打包
		public static void dataToZip(String source, String dest) {
			try {
				SimpleDateFormat  sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				System.out.println(" 开始时间 " + sdf.format(new Date()));
				// 下载文件的地址
				String path = "https://cef-open.ccic.com/document/download?documentId=";
				// 先将对应文件进行下载,并返回对应的数据库信息
				List<List<Object>> result = findDataFromDbTFile(dest, path, source, new JSONArray());
				
				System.out.println(" 结束时间 " + sdf.format(new Date()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	// 查询数据库中需要导出的数据 --- t_file
	public static List<List<Object>> findDataFromDbTFile(String dest, String path, String source, JSONArray wtgx) throws Exception {
		List<List<Object>> result = new ArrayList<List<Object>>();
		
		// 获取数据
		List<JSONArray> db = HaiguanUtils.findFileData();
		// 日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 计数 
		int js = 0, jfs = 0;
		
		// 网络中断之后添加的代码
//		String idddd = "2751361106952589813";
//		boolean idf = false;
		
		// 用于存ids
		StringBuffer sb = new StringBuffer();
		
		// 安装海运传输的xml格式模拟数据
		for (int i = 0; i < db.size(); i++) {
			System.out.println("  ----------  "+i+" ------------ ");
			// 数据库查出来的一条数据
			JSONArray dbi = db.get(i);
			
//			//网络中断之后添加的代码
//			if(dbi.getString(0).equals(idddd)) {
//				idf = true;
//			}
//			
//			if(!idf) {
//				System.out.println(" idddd =  " + dbi.getString(0));
//				continue;
//			}
			
			// 存在的文件名称
			String fileName;
			// base64编码
			String ecode;
			// 文件大小
			int fileSize;
			// 获取 已经存在的 pdf文件名称和base64
			String exist = "F:/hgcsbw/loaded/txt";
			// 存放信息的jsonobject
			JSONObject ejo = XmlBase64ToFile.isExistPdf(exist, dbi.getString(0));
			System.out.println(ejo.getString("FileName"));
			if(ejo.getString("FileName") != null && !ejo.getString("FileName").equals("")) {
				fileName = ejo.getString("FileName");
				ecode = ejo.getString("base64");				
				fileSize = ejo.getIntValue("size");
			}else {
				// 下载文件的地址
				String url = path+dbi.getString(0);
				// 保存后的文件名称
				fileName = HaiguanUtils.downLoadByUrl(url, dest);				
				// 将文件转换成base64编码 并添加到jsoaarray
				File pdf = new File(dest+ File.separator + fileName);
				ecode = PDFBinaryConvert.getPDFBinary(pdf);
				fileSize = Integer.parseInt(pdf.length()+"");
			}
			
			// md5			
//			String fmd5 = StringUtil.MD5(ecode);
			// 先将base64转码为pdf 再获取md5 
			PDFBinaryConvert.base64StringToPDF(ejo.getString("base64"), dest+File.separator+fileName);
			String fmd5 = Md5Test.findMD5ByFilePath(dest+File.separator+fileName);
			// 将对应的pdf文件删除
			File pdf = new File(dest+File.separator+fileName);
			pdf.delete();
			
			if(fmd5 != null && fmd5.equals("NotFoundFile")) {
				// 给出提示 好进行查找没有对应文件的pdf信息
				System.out.println(" id 为 "+ dbi.getString(0) + "对应的   " + fileName+"   pdf文件未找到!  ");
			}
			
			// 20201222 过滤掉小于3M的文件
//			if(ecode.length() < 2 * 1024 * 1024) {
//				continue;
//			}
			
			// 判断ecode 是否大于3M 剩余1M 用来存放其他字符
			if(ecode.length() >= 2 * 1024 * 1024) {
				// 这里有可能需要转换成pdf
				PDFBinaryConvert.base64StringToPDF(ecode, "F:/hgcsbw/pdf/"+fileName);
				
				int maxel = ecode.length() % (2 * 1024 * 1024) > 0 ? ( ecode.length() / (2 * 1024 * 1024) + 1 ) : ( ecode.length() / (2 * 1024 * 1024) );
				for (int eln = 0; eln < maxel  ; eln++) {
					// 需要组装成xml中对应的格式
					List<Object> rut = new ArrayList<Object>();
					
					// 测试数据			
					rut.add("PSIUMEEDOC");
					rut.add(dbi.get(4).toString().length() > 64 ? dbi.get(4).toString().substring(0, 64) : dbi.get(4).toString() );
					// 判断文件名称是否超过64位
					if(fileName.length() > 64) {
						fileName = fileName.substring(0, 64 - fileName.substring(fileName.lastIndexOf(".")).length())+fileName.substring(fileName.lastIndexOf("."));
					}
					rut.add(fileName);
					rut.add("");
					rut.add("");
					rut.add(dbi.get(4).toString());
					rut.add("F");
					rut.add(sdf.format(new Date()));
					rut.add("中检集团证书中心");
					rut.add("");
					rut.add(ecode.substring(eln*2 * 1024 * 1024, ( eln == (maxel - 1) ? ecode.length() : (eln + 1) * 2 * 1024 * 1024)) );
					
					// 添加新的五个属性
					rut.add(fileSize);
					rut.add(fmd5);
					rut.add(1);
					rut.add(eln+1);
					rut.add(maxel);
					System.out.println(fileName + " 第 " + (eln+1) + " 个 共 "+maxel+" 个 ");
					rut.add(dbi.get(0).toString().length() > 40 ? dbi.get(0).toString().substring(0, 40) : dbi.get(0).toString() );
					if(wtgx != null && wtgx.size() > 0) {
						rut.add(wtgx.get(jfs));
					}else {
						rut.add("");
					}
					rut.add("");
					rut.add("");
					rut.add("");
					
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
				
				sb.append(dbi.get(0)+"#"+wtgx.get(jfs)+"@@");
				
				// 正在进行
				System.out.println(dbi.getString(0) + "  正在生成第"+(++jfs)+"个xml --------- ");
				
				if(wtgx != null && wtgx.size() > 0 && jfs == wtgx.size()) {
					break;
				}
				
				// 为了验证错误文件
//				if( dbi.get(0).toString().equals("2668181580532043959") ) {
//					break;
//				}
				
				continue;
			}
			
			// 需要组装成xml中对应的格式
			List<Object> rut = new ArrayList<Object>();
			
			// 测试数据			
			rut.add("PSIUMEEDOC");
			rut.add(dbi.get(4).toString().length() > 64 ? dbi.get(4).toString().substring(0, 64) : dbi.get(4).toString() );
			// 判断文件名称是否超过64位
			if(fileName.length() > 64) {
				fileName = fileName.substring(0, 64 - fileName.substring(fileName.lastIndexOf(".")).length())+fileName.substring(fileName.lastIndexOf("."));
			}
			rut.add(fileName);
			rut.add("");
			rut.add("");
			rut.add(dbi.get(4).toString());
			rut.add("F");
			rut.add(sdf.format(new Date()));
			rut.add("中检集团证书中心");
			rut.add("");
			rut.add(ecode);
			
			// 添加新的五个属性
			rut.add(fileSize);
			rut.add(fmd5);
			rut.add(0);
			rut.add(1);
			rut.add(1);
			
			rut.add(dbi.get(0).toString().length() > 40 ? dbi.get(0).toString().substring(0, 40) : dbi.get(0).toString() );
			if(wtgx != null && wtgx.size() > 0) {
				rut.add(wtgx.get(jfs));
			}else {
				rut.add("");
			}
			rut.add("");
			rut.add("");
			rut.add("");
			
			result.add(rut);
			
			sb.append(dbi.get(0)+"#"+wtgx.get(jfs)+"@@");
			
			// 正在进行
			System.out.println(dbi.getString(0) + "  正在生成第"+(++jfs)+"个xml --------- ");

			if(wtgx != null && wtgx.size() > 0 && jfs == wtgx.size()) {
				break;
			}
			
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
		
		// 向txt写入id
		PDFBinaryConvert.writeContentToTxt("F:/hgcsbw/ids.txt", sb.toString().substring(0, sb.toString().length() - 2));
		
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
			String fileName = dest.substring(0, dest.lastIndexOf("/")) + File.separator + System.currentTimeMillis() + "_" + ( dest.substring(dest.lastIndexOf("/")+1) ) + ".zip";
			
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
		DataConvertToXmlZip.dataToZip("F:/工作内容/海关数据对接/测试报文样例/CliPsiEdoc_last.xml", "F:/hgcsbw/OutBox");
	}
}
