package com.newtec.company.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

public class HaiguanUtils {

	public final static String DATABASE_URL = "jdbc:oracle:thin:@192.168.130.12:1521:orcl";
	public final static String USERNAME = "SOU_CERC";
	public final static String PASSWORD = "SOU_CERC";

	/**
	 * 返回oracle的数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		return DBOrcal.getConnection(DATABASE_URL, USERNAME, PASSWORD);
	}

	/**
	 * 返回结果集
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String sql) {
		ResultSet rs = null;
		PreparedStatement pre = null;
		try {
			pre = conn.prepareStatement(sql);
			rs = pre.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public static List<JSONArray> changeRsToList(ResultSet rs) {
		List<JSONArray> result = new ArrayList<JSONArray>();

		try {
			while (rs.next()) {
				JSONArray ja = new JSONArray();

				ja.add(rs.getString(1));
				ja.add(rs.getString(2));
				ja.add(rs.getString(3));
				ja.add(rs.getString(4));
				ja.add(rs.getString(5));
				ja.add(rs.getString(6));

				result.add(ja);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}

	public static List<JSONArray> findData(String sql) {
		return changeRsToList(getResultSet(getConnection(), sql));
	}
	
	public static List<JSONArray> findFileAndAttachedFileData() {
		// 查询 T_FILE
		String sqlFile = "select a.*, rownum from( " + "select * from T_FILE  " + ") a " + "where rownum < 10"
				+ "  union "
				+ "select a.*, rownum from( " + "select * from T_ATTACHED_FILE  " + ") a "
				+ "where rownum < 10";
		return findData(sqlFile);
	}

	public static List<JSONArray> findFileData() {
		// 查询 T_FILE
//		String sqlFile = "select a.*, rownum from( " + "select * from T_FILE  " + ") a " + "where rownum < 100";
		String sqlFile = "select a.*, rownum from( " + "select * from T_FILE  " + ") a ";
		return findData(sqlFile);
	}

	public static List<JSONArray> findAttachedFileData() {
		// 查询 T_ATTACHED_FILE
		String sqlAtta = "select a.*, rownum from( " + "select * from T_ATTACHED_FILE  " + ") a "
				+ "where rownum < 100000";
		return findData(sqlAtta);
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

	/**
	 * 从网络Url中下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static String downLoadByUrl(String urlStr, String savePath) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为50秒
		conn.setConnectTimeout(50 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("Host", "cef-open.ccic.com");
		conn.setRequestProperty("x-qys-accesstoken", "lVWRJJclae");
//		conn.setRequestProperty("x-qys-signature", MD5("YcF3gIMeIS"+"yvruw7bdSP91gTDgZWUrs4to0fMkIi"+0).toLowerCase());
		conn.setRequestProperty("x-qys-signature", "f08a2e4eb6a0ea54bf8903b6bea56c46");
		conn.setRequestProperty("x-qys-timestamp", "0");
		conn.setRequestProperty("Content-Type", "multipart/form-data");
		conn.setRequestProperty("Accept-Charset", "UTF-8");
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
				
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		
		// 获取 Content-Disposition
		String headerField = conn.getHeaderField("Content-Disposition");
		String flieName = URLDecoder.decode( headerField.substring(headerField.indexOf("fileName=")+9),"UTF-8");
		
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);
		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + flieName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
		System.out.println("info:" + url + " download success");
		return flieName;
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}
	
	public static List<JSONArray> getPDFAndSave(String path, String urlStr) {
		List<JSONArray> fileData = null;
		try {
			fileData = findFileAndAttachedFileData();
			
			for (int i = 0; i < fileData.size(); i++) {	
				// 下载文件的地址
				String url = urlStr+fileData.get(i).getString(0);
				// 保存后的文件名称
				String fileName = downLoadByUrl(url, path);
				
				// 将文件转换成base64编码 并添加到jsoaarray
//				fileData.get(i).add(PDFBinaryConvert.getPDFBinary(new File(path+ File.separator + fileName)));
				// 将文件原名称 并添加到jsoaarray
//				fileData.get(i).add(fileName);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fileData;
	}	

	public static void main(String[] args) {
		
		// 下载文件的地址
		String urlStr = "https://cef-open.ccic.com/document/download?documentId=";
		// 保存文件的路径
		String path = "F:/hgcsbw/OutBox";
		
//		HaiguanUtils.getPDFAndSave(path, urlStr);
		
		// 保存文件的路径
		String dest = "F:/hgcsbw";
		String id = "2709945542562722407";
		
		try {
			System.out.println(HaiguanUtils.downLoadByUrl(urlStr+id, dest));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
