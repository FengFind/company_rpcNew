package com.newtec.company.entity.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 客户产品线实体
 * @author Administrator
 *
 */
@Entity
@Table(name = "customer_product_line")
public class CustomerProductLine {

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
	 * 产品线名称
	 */
	@Column(name = "product_name")
	private String productName;
	
	/*
	 * 人员id
	 */
	@Column(name = "staff_id")
	private String staffId;
	
	/*
	 * 部门id
	 */
	@Column(name = "dept_id")
	private String deptId;

	/*
	 * 
	 */
	@Column(name = "contacts_uuid")
	private String contactsUUID;
	
	/*
	 * 
	 */
	@Column(name = "contacts_name")
	private String contactsName;
	
	/*
	 * 
	 */
	@Column(name = "create_time")
	private String createTime;

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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	


	public String getContactsUUID() {
		return contactsUUID;
	}

	public void setContactsUUID(String contactsUUID) {
		this.contactsUUID = contactsUUID;
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}

	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public CustomerProductLine(String customerId, String productId, String productName, String staffId, String deptId,
			String contactsUUID, String contactsName) {
		super();
		this.customerId = customerId;
		this.productId = productId;
		this.productName = productName;
		this.staffId = staffId;
		this.deptId = deptId;
		this.contactsUUID = contactsUUID;
		this.contactsName = contactsName;
	}

	public CustomerProductLine() {
		super();
	}
	
}
