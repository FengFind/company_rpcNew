package com.newtec.rpc.start.test;

import com.newtec.company.start.CompanyRpc;
import com.newtec.myqdp.server.utils.MyqdpEncryptImpl;
import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.rpc.node.NodeUtils;
import com.newtec.thread.CollectThread;

/**
 * @author 
 * @Description
 * @date  2018年7月13日
 * @version 1.0
 */
public class RpcNodeTest {
	
	final static public String DEV_PATH                   =   "company-rpc-dev.properties";
	public static void main(String[] args) throws CustomException, InterruptedException {        
		CompanyRpc.start(DEV_PATH);
		NodeUtils.getLocHost(); 
		CollectThread c = new CollectThread();
		c.start();
		MyqdpEncryptImpl.getMyqdpServerHttpAddr();  
	}                        
}
