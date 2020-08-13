package com.newtec.company.entity.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author
 * @Description 客户付款信息
 * @date 2019年9月3日
 * @version 1.0
 *
 */
@Entity
@Table(name = "invoice_payment")
public class CustomerPayment {

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	/*
	 * 客户ID
	 */
	@Column(name = "customer_id")
	private String customerId;
	
	/*
	 * 付款方
	 */
	@Column(name = "payer")
	private String payer;
	/*
	 * 开户银行
	 */
	@Column(name = "deposit_bank")
	private String depositBank;
	/*
	 * 银行账号
	 */
	@Column(name = "bank_account")
	private String bankAccount;
	/*
	 * 备注
	 */
	@Column(name = "remark")
	private String remark;
	/*
	 * 人员ID
	 */
	@Column(name = "staff_id")
	private String staffId;

	/*
	 * 记录日期
	 */
	@Column(name = "record_time")
	private String recordTime;
	
	
	/*
	 *活动状态
	 */
	@Column(name = "active_state")
	private String activeState;

	/*
	 * 创建人部门Id
	 */
	@Column(name = "dept_id")
	private String deptId;
	
	/*
	 * 公司id
	 */
	@Column(name = "cmpany_id")
	private String cmpanyId;
	
	/*
	 * 公司名称
	 */
	@Column(name = "cmpany_name")
	private String cmpanyName;
	
	/*
	 * 人员名称
	 */
	@Column(name = "staff_name")
	private String staffName;
	
	/*
	 * 部门名称
	 */
	@Column(name = "dept_name")
	private String deptName;
	
	public String getCmpanyId() {
		return cmpanyId;
	}

	public void setCmpanyId(String cmpanyId) {
		this.cmpanyId = cmpanyId;
	}

	public String getCmpanyName() {
		return cmpanyName;
	}

	public void setCmpanyName(String cmpanyName) {
		this.cmpanyName = cmpanyName;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

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


	public String getPayer() {
		return payer;
	}


	public void setPayer(String payer) {
		this.payer = payer;
	}


	public String getDepositBank() {
		return depositBank;
	}


	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}


	public String getBankAccount() {
		return bankAccount;
	}


	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getStaffId() {
		return staffId;
	}


	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}



	public String getRecordTime() {
		return recordTime;
	}


	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}


	public String getActiveState() {
		return activeState;
	}


	public void setActiveState(String activeState) {
		this.activeState = activeState;
	}
	
}
