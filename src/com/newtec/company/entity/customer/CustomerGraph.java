package com.newtec.company.entity.customer;

import java.util.List;

/**
 *  	客户产品线映射实体
 * @author 王鹏
 *
 */
public class CustomerGraph {

	private Customer customer; //客户
	
	private List<String> list; //产品线集合

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}
	
	
	
}
