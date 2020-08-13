package com.newtec.company.entity.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 假新增cqc客户
 * @author 王鹏
 *	
 */
@Entity
@Table(name = "t_share_customer")
public class CustomerShare {

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
	 * 人员id
	 */
	@Column(name = "staff_id")
	private String staffId;
	
	/*
	 * 创建人部门Id
	 */
	@Column(name = "dept_id")
	private String deptId;
	
	/*
	 * 记录日期
	 */
	@Column(name = "record_date")
	private String recordDate;

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

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public CustomerShare(String customerId, String staffId, String deptId, String recordDate) {
		super();
		this.customerId = customerId;
		this.staffId = staffId;
		this.deptId = deptId;
		this.recordDate = recordDate;
	}

	public CustomerShare() {
		super();
	}
	
	
}
