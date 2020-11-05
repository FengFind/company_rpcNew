package com.newtec.company.utils;

import java.io.File;

import com.alibaba.fastjson.JSONObject;

public class FindLostResponse {

	public static void main(String[] args) {
//		FindLostResponse.findLostFileNames("F:/hgcsbw/fujian/SentBox/2020-10-28", "F:/hgcsbw/fujian/InBox", "F:/工作内容/海关数据对接/20201028没有回执文件的附件报文");
//		FindLostResponse.findFailedSourceFile("F:/hgcsbw/fujian/InBox", "F:/hgcsbw/fujian/SentBox/2020-10-28", "F:/工作内容/海关数据对接/20201028没有回执文件的附件报文");
//		System.out.println("SI_ZHIQIANG 18JAN2020 BUD WAW - globalairgood@gmail.com - Gmail(1).pdf".getBytes().length);
//		FindLostResponse.findFileByEdocID("1620150200400013-003", "F:/hgcsbw/fujian/SentBox/2020-10-26", "F:/工作内容/海关数据对接/证书信息failed对应的附件报文");
//		FindLostResponse.findFjFileByFailedZsinfo("F:/hgcsbw/zhengshu/InBox", "F:/hgcsbw/fujian/SentBox/2020-10-26", "F:/工作内容/海关数据对接/证书信息failed对应的附件报文");
		System.out.println("WAW - globalairgood@gmail.com - Gmail(1).pdf".getBytes().length);
	}
	
	/**
	 * 根据证书信息的failed回执报文 查找是否存在对应的附件信息
	 * @param failzs
	 * @param sendFj
	 * @param dest
	 */
	public static void findFjFileByFailedZsinfo(String failzs, String sendFj, String dest) {
		try {
			// 证书信息failed报文文件夹
			File fzs = new File(failzs);
			// 发送的附件信息列表文件夹
			File sfj = new File(sendFj);
			// 证书信息failed报文文件列表
			File[] fzsl = fzs.listFiles();
			// 附件文件列表
			File[] sfjl = sfj.listFiles();
			// 计算
			int js = 0;
			for (int i = 0; i < fzsl.length; i++) {
				// 如果不带failed的 直接跳下一个
				if(fzsl[i].getName().indexOf("Failed") < 0) {
					continue;
				}
				
				JSONObject jo = XmlBase64ToFile.findFileNameAndBase64ByFile(fzsl[i]);
				
				if(jo.getString("ResultInfo") == null || jo.getString("ResultInfo").equals("")) {
					continue;
				}
				
				// 获取edocID
				String resultInfo = jo.getString("ResultInfo");
				String edocID = resultInfo.substring(resultInfo.indexOf("EdocID为")+7, resultInfo.indexOf("的附件未上传"));
				
				// 打印
				System.out.println("----------------EdocID为"+edocID+"-----------------");
				System.out.println("-----------查找第"+(++js)+"个failed报文对应的附件报文-----------");
				
				// 根据edocID查找对应的附件信息
				findFileByEdocID(edocID, sendFj, dest);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据EdocID 查找对应附件报文
	 * @param edocID 
	 * @param source 附件报文文件夹
	 * @param dest 存放的目标文件夹
	 */
	public static void findFileByEdocID(String edocID, String source, String dest) {
		File xml = XmlBase64ToFile.findFileNameByEdocID(source, edocID);
	
		if(xml == null) {
			System.out.println(" ------------找不到    "+edocID+"  对应的附件 -------------- ");
		}
		
		if(xml != null && xml.exists()) {
			PDFBinaryConvert.copyFileUsingFileChannels(xml, new File(dest+File.separator+xml.getName()));
		}
	}
	
	/**
	 * 查找failed回执对应的源文件
	 * @param fail failed回执文件的文件夹
	 * @param send 发送的文件夹
	 * @param dest 存放failed回执文件对应源文件的文件夹
	 */
	public static void findFailedSourceFile(String fail, String send, String dest) {
		// failed 文件
		File f = new File(fail);
		// 发送的文件
		File s = new File(send);
		// failed 文件列表
		File[] fl = f.listFiles();
		// 发送的文件列表
		File[] sl = s.listFiles();
		// 计数
		int js = 0;
		// 循环failed文件列表
		for (int i = 0; i < fl.length; i++) {
			// 如果文件名称不包含 failed 直接跳过
			if(fl[i].getName().indexOf("Failed") < 0) {
				continue;
			}
			
			// failed文件的名称
			String fname = fl[i].getName().substring(fl[i].getName().indexOf("_")+1, fl[i].getName().lastIndexOf("_"));
			// 循环发送文件列表
			for (int j = 0; j < sl.length; j++) {
				// 文件名称
				String sname = sl[j].getName().substring(0, sl[j].getName().indexOf("_"));
				if(fname.equals(sname)) {
					System.out.println("------"+(++js)+"-------");
					PDFBinaryConvert.copyFileUsingFileChannels(sl[j], new File(dest+File.separator+sl[j].getName()));
				}
			}
		}
	}
	
	/**
	 * 查找缺少回执文件的文件
	 * @param send 发送的文件夹
	 * @param response 接收回执文件的文件夹
	 * @param dest 存放没有回执文件的文件夹
	 */
	public static void findLostFileNames(String send, String response, String dest){
		// 发送的文件
		File s = new File(send);
		// 接收的文件
		File r = new File(response);
		// 发送文件列表
		File[] sl = s.listFiles();
		// 接收的文件列表
		File[] rl = r.listFiles();
		// 计数
		int js = 0;
		// 循环发送文件列表
		for (int i = 0; i < sl.length; i++) {
			// 源文件文件名称
			String sname = sl[i].getName().substring(0, sl[i].getName().lastIndexOf("_"));
			// 标志
			boolean flag = true;
			// 查接收文件是否存在对应的回执
			for (int j = 0; j < rl.length; j++) {
				// 回执文件中 源文件名称
				String rname = rl[j].getName().substring(rl[j].getName().indexOf("_")+1, rl[j].getName().lastIndexOf("_"));
				
				if(rname.equals(sname)) {
					flag = false;
					break;
				}
			}
			
			// 如果没有找到 将文件复制到指定文件夹
			if(flag) {
				System.out.println("------"+(++js)+"-------");
				PDFBinaryConvert.copyFileUsingFileChannels(sl[i], new File(dest+File.separator+sl[i].getName()));
			}
		}
		
	}
}
