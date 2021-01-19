package com.newtec.company.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Md5Test {

    public static void main(String[] args) throws IOException {
    	System.out.println(findMD5ByFilePath("F:\\hgcsbw\\#144-4 证书.pdf"));
    }

    /**
     * 根据文件路径 获取文件对应的MD5编码
     * @param path
     * @return
     */
    public static String findMD5ByFilePath(String path) {
    	try {
			File f = new File(path);
			if (!f.exists()) {
//			    throw new FileNotFoundException(path);
				return "NotFoundFile";
			}

			ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
			BufferedInputStream in = null;
			try {
			    in = new BufferedInputStream(new FileInputStream(f));
			    int buf_size = 1024;
			    byte[] buffer = new byte[buf_size];
			    int len = 0;
			    while (-1 != (len = in.read(buffer, 0, buf_size))) {
			        bos.write(buffer, 0, len);
			    }

			    //获取md5
			    byte[] fileBytes = bos.toByteArray();
//			    System.out.println("文件MD5为："+DigestUtils.md5Hex(fileBytes));
			    return DigestUtils.md5Hex(fileBytes);
			} catch (IOException e) {
			    e.printStackTrace();
			    throw e;
			} finally {
			    try {
			        in.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			    bos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
}
