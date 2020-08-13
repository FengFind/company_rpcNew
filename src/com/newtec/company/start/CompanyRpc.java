package com.newtec.company.start;

import com.newtec.company.common.common.CompanyRParam;
import com.newtec.myqdp.server.utils.MyqdpEncryptImpl;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.rpc.encrypt.NodeEncrypt;

/**
 * @author 
 * @Description  中检客户中心启动类
 * @date  2019年8月27日
 * @version 1.0
 */
public class CompanyRpc {

	
	static public void start(String path) throws CustomException {		
		NodeEncrypt.start();
		CompanyRParam.initParam(path);
		MyqdpEncryptImpl.getMyqdpServerHttpAddr();
//		BPMInit.initProcess("siteProcess:1");
	}
	
}
