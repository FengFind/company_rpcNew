package com.newtec.company.entity.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * cqc产品线实体
 * @author 王鹏
 *	
 */
@Entity
@Table(name = "cqc_product_line")
public class CqcProductLine {
 
	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;
	
	/*
	 * 客户id
	 */
	@Column(name = "customer_id")
	private String customerId;
	
	/*
	 * 产品线id
	 */
	@Column(name = "product_id")
	private String productId;
	
	/*
	 * 人员id
	 */
	@Column(name = "staff_id")
	private String staffId;
	
	/*
	 * 创建人部门Id
	 */
	@Column(name = "dept_id")
	private String deptId;

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public CqcProductLine(String customerId, String productId, String staffId,String deptId) {
		super();
		this.customerId = customerId;
		this.productId = productId;
		this.staffId = staffId;
		this.deptId = deptId;
	}

	public CqcProductLine() {
		super();
	}
	
	
	
}
