package com.newtec.company.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PDFBinaryConvert {
	/*
	 * BASE64Encoder和BASE64Decoder这两个方法是sun公司的内部方法，并没有在java api中公开过，所以使用这些方法是不安全的，
	 * 将来随时可能会从中去除，所以相应的应该使用替代的对象及方法，建议使用apache公司的API
	 */
	static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

	public static void main(String[] args) {
		// 将PDF格式文件转成base64编码
//		String path = "F:/hgcsbw/OutBox/#367-4 报告.pdf";
//		String base64String = getPDFBinary(new File(path));
//		System.out.println(base64String);
		// 将base64的编码转成PDF格式文件
//		String dest = "F:/hgcsbw/OutBox/tttt.pdf";
//		base64StringToPDF(base64String, dest);
		// 将 编码写入txt
//		String txt = "F:/hgcsbw/OutBox/tttt.txt";
//		writeContentToTxt(txt, base64String);
		
		PDFBinaryConvert.convertPdfToManyByPages("F:/hgcsbw/#367-4 报告.pdf", "F:/hgcsbw/fgpdf");
	}
	
	/**
	 * 将base64编码分割
	 */
	public static void fengeBase64() {
		String dest = "F:/hgcsbw/newttt.pdf";
		
		String base64 = PDFBinaryConvert.findStringFromTxt("F:/hgcsbw/pdf.txt");
		
		// 将base64进行分组 按3M 一组 
		// 1个字符1个字节 3M = 3 * 1024 *1024 
		int m3 = 3 * 1024 *1024 ;
		// 存放 分开的字符串
		List<String> strList = new ArrayList<String>();
		
		for (int i = 0; i < base64.length(); i+=m3) {
			strList.add(base64.substring(i, (i+m3) > base64.length() ? base64.length() : (i+m3) ));
		}
		
		System.out.println( "  strList  的 长度 " + strList.size() );
		
		String new64 = "";
		
		for (int j = 0; j < strList.size(); j++) {
			new64 += strList.get(j);
			// 将 字符串写入txt
			writeContentToTxt("F:/hgcsbw/txt/"+j+".txt", strList.get(j));
		}
		
		base64StringToPDF(new64, dest);
	}

	/**
	 * pdf 按页转换成多个pdf
	 * @param source 源文件地址
	 * @param dest 目标文件夹地址
	 */
	public static void convertPdfToManyByPages(String source, String dest) {
		try {
			PdfReader reader = new PdfReader(new FileInputStream(new File(source)));
			int n = reader.getNumberOfPages();
			for (int i = 0; i < n; i++) {
				File dir = new File(dest);
				
				if(!dir.exists()) {
					dir.mkdirs();
				}
				
				File file = new File(dest+File.separator+source.substring(source.lastIndexOf("/")+1, source.lastIndexOf("."))+"_"+i+".pdf");
				
				if(!file.exists()) {
					file.createNewFile();
				}
				
				Document document = new Document();
				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest+File.separator+source.substring(source.lastIndexOf("/")+1, source.lastIndexOf("."))+"_"+i+".pdf"));
				
				document.open();
				PdfContentByte cb = writer.getDirectContent();
				document.newPage();
				PdfImportedPage page = writer.getImportedPage(reader, i+1);
				cb.addTemplate(page, 0, 0);
				document.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 从txt中读取内容转换为字符串
	 * @param path
	 * @return
	 */
	public static String findStringFromTxt(String path) {
		StringBuffer buffer = new StringBuffer();
		
		try {
			BufferedReader bf= new BufferedReader(new FileReader(path));
			String s = null;
			while((s = bf.readLine())!=null){//使用readLine方法，一次读一行
			    buffer.append(s.trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return buffer.toString();
	}
	
	/**
	 * 将PDF转换成base64编码 1.使用BufferedInputStream和FileInputStream从File指定的文件中读取内容；
	 * 2.然后建立写入到ByteArrayOutputStream底层输出流对象的缓冲输出流BufferedOutputStream
	 * 3.底层输出流转换成字节数组，然后由BASE64Encoder的对象对流进行编码
	 */
	static String getPDFBinary(File file) {
		String ecode = "";
		FileInputStream fin = null;
		BufferedInputStream bin = null;
		ByteArrayOutputStream baos = null;
		BufferedOutputStream bout = null;
		try {
			// 建立读取文件的文件输出流
			fin = new FileInputStream(file);
			// 在文件输出流上安装节点流（更大效率读取）
			bin = new BufferedInputStream(fin);
			// 创建一个新的 byte 数组输出流，它具有指定大小的缓冲区容量
			baos = new ByteArrayOutputStream();
			// 创建一个新的缓冲输出流，以将数据写入指定的底层输出流
			bout = new BufferedOutputStream(baos);
			byte[] buffer = new byte[1024];
			int len = bin.read(buffer);
			while (len != -1) {
				bout.write(buffer, 0, len);
				len = bin.read(buffer);
			}
			// 刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
			bout.flush();
			byte[] bytes = baos.toByteArray();
			
			// sun公司的API
			ecode = encoder.encodeBuffer(bytes).trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fin.close();
				bin.close();
				// 关闭 ByteArrayOutputStream 无效。此类中的方法在关闭此流后仍可被调用，而不会产生任何 IOException
				// baos.close();
				bout.close();
				// 转换完成后删除file
				file.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ecode;
	}

	/**
     * 将base64编码转换成PDF
     * @param base64sString
     * 1.使用BASE64Decoder对编码的字符串解码成字节数组
     *  2.使用底层输入流ByteArrayInputStream对象从字节数组中获取数据；
     *  3.建立从底层输入流中读取数据的BufferedInputStream缓冲输出流对象；
     *  4.使用BufferedOutputStream和FileOutputSteam输出数据到指定的文件中
     */
    static void base64StringToPDF(String base64sString, String dest){
            BufferedInputStream bin = null;
            FileOutputStream fout = null;
            BufferedOutputStream bout = null;
            try {
                     //将base64编码的字符串解码成字节数组
                        byte[] bytes = decoder.decodeBuffer(base64sString);
                    //apache公司的API
                    //byte[] bytes = Base64.decodeBase64(base64sString);
                        //创建一个将bytes作为其缓冲区的ByteArrayInputStream对象
                        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                        //创建从底层输入流中读取数据的缓冲输入流对象
                        bin = new BufferedInputStream(bais);
                        //指定输出的文件
                        File file = new File(dest);
                        
                        if(!file.exists()) {
                        	file.createNewFile();
                        }
                        
                        //创建到指定文件的输出流
                        fout  = new FileOutputStream(file);
                        //为文件输出流对接缓冲输出流对象
                        bout = new BufferedOutputStream(fout);
                        
                        byte[] buffers = new byte[1024];
                        int len = bin.read(buffers);
                        while(len != -1){
                                bout.write(buffers, 0, len);
                                len = bin.read(buffers);
                        }
                        //刷新此输出流并强制写出所有缓冲的输出字节，必须这行代码，否则有可能有问题
                        bout.flush();
                        
                } catch (IOException e) {
                        e.printStackTrace();
                }finally{
                        try {
                                bin.close();
                                fout.close();
                                bout.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
    }
    
    /**
     * 向txt文件中写入content 
     * @param name
     * @param content
     * @return
     */
	public static boolean writeContentToTxt(String name,String content) {
		try {
			File file = new File(name);
			
			if(!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.flush();
			bw.close();
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}