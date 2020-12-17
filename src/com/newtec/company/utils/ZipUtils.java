package com.newtec.company.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Nemo
 * @version 1.0
 * @date 2019/11/5
 */
public class ZipUtils {
	private static final int BUFFER_SIZE = 2 * 1024;

	/**
	 * 压缩成ZIP 方法1
	 * 
	 * @param sourceFile       压缩文件夹路径
	 * @param out              压缩文件输出流
	 * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(File sourceFile, OutputStream out, boolean KeepDirStructure) throws RuntimeException {
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 压缩成ZIP 方法2
	 * 
	 * @param srcFiles 需要压缩的文件列表
	 * @param out      压缩文件输出流
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(List<File> srcFiles, OutputStream out) throws RuntimeException {
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(out);
			for (File srcFile : srcFiles) {
				byte[] buf = new byte[BUFFER_SIZE];
				zos.putNextEntry(new ZipEntry(srcFile.getName()));
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
				in.close();
			}
			long end = System.currentTimeMillis();
			System.out.println("压缩完成，耗时：" + (end - start) + " ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 递归压缩方法
	 * 
	 * @param sourceFile       源文件
	 * @param zos              zip输出流
	 * @param name             压缩后的名称
	 * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
			throws Exception {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (KeepDirStructure) {
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (KeepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
					} else {
						compress(file, zos, file.getName(), KeepDirStructure);
					}
				}
			}
		}
	}
	
	// 删除 文件夹下的文件
	public static void delDirFiles(String dir) {
		try {
			File dirFile = new File(dir);
			
			if(dirFile.exists() && dirFile.isDirectory()) {
				File[] cfile = dirFile.listFiles();
				
				if(cfile != null && cfile.length > 0) {
					for (int i = 0; i < cfile.length; i++) {
						if(cfile[i].exists()) {
							cfile[i].delete();
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void dirFilesToZip(String dirPath, String desPath) {
		// 循环压缩文件
		int size = 100;
		// 存放file
		List<File> fileList = new ArrayList<>();
		// 文件夹对象
		File dir = new File(dirPath);
		// 文件夹内文件 列表
		File[] dirFiles = dir.listFiles();
		
		// 循环 每隔100个 压缩一次
		int i = 0;
		for (; i < dirFiles.length; i++) {
			if(fileList.size() == 0) {
				fileList.add(dirFiles[i]);
				continue;
			}
			
			if(fileList.size() == 100) {
				// 压缩
				try {
					FileOutputStream fos2 = new FileOutputStream(new File(desPath+File.separator+System.currentTimeMillis()+"_"+i+".zip"));
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					System.out.println("第"+(i/100)+"个文件 压缩开始 -------- "+sdf.format(new Date()));
					ZipUtils.toZip(fileList, fos2);
					System.out.println("第"+(i/100)+"个文件 压缩结束 -------- "+sdf.format(new Date()));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				fileList = new ArrayList<>();
			}
			
			fileList.add(dirFiles[i]);
		}
		
		// 压缩
		try {
			FileOutputStream fos2 = new FileOutputStream(new File(desPath+File.separator+System.currentTimeMillis()+"_"+i+".zip"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("第"+(i/100)+"个文件 压缩开始 -------- "+sdf.format(new Date()));
			ZipUtils.toZip(fileList, fos2);
			System.out.println("第"+(i/100)+"个文件 压缩结束 -------- "+sdf.format(new Date()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		/** 测试压缩方法1 */
//		FileOutputStream fos1 = new FileOutputStream(new File("c:/mytest01.zip"));
//		ZipUtils.toZip(new File("D:/log"), fos1, true);
		/** 测试压缩方法2 */
//		List<File> fileList = new ArrayList<>();
//		fileList.add(new File("D:/Java/jdk1.7.0_45_64bit/bin/jar.exe"));
//		fileList.add(new File("D:/Java/jdk1.7.0_45_64bit/bin/java.exe"));
//		FileOutputStream fos2 = new FileOutputStream(new File("c:/mytest02.zip"));
//		ZipUtils.toZip(fileList, fos2);
		
		dirFilesToZip("F:/hgcsbw/1603189489646_附件信息/OutBox",  "F:/hgcsbw/1603189489646_附件信息");
	}
}