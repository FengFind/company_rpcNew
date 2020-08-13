package com.newtec.company.common.common;

import com.newtec.myqdp.server.utils.exception.CustomException;
import com.newtec.rpc.db.DBManager;
import com.newtec.rpc.db.DBexecute;
import com.newtec.rpc.db.DBexecuteVoid;
import javax.persistence.EntityManager;

public class CompanyDBManager {
	public static EntityManager get() throws CustomException {
		return DBManager.get(CompanyRParam.getCompanyDB());
	}

	public static void execute(DBexecuteVoid execute) throws CustomException {
		DBManager.execute(execute, CompanyRParam.getCompanyDB());
	}

	public static <T> T execute(DBexecute<T> execute) throws CustomException {
		return DBManager.execute(execute, CompanyRParam.getCompanyDB());
	}
}