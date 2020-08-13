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
 * @Description 客户开票信息
 * @date 2019年9月2日
 * @version 1.0
 *
 */
@Entity
@Table(name = "t_invoice")
public class CustomerInvoice {

	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;
	
	/*
	 * 国别
	 */
	@Column(name = "country")
	private String country;	
	
	/*
	 * 手机
	 */
	@Column(name = "phone")
	private String phone;

	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/*
	 * 客户ID
	 */
	@Column(name = "customer_id")
	private String customerId;
	
	/*
	 * 开票类型
	 */
	@Column(name = "invoice_type")
	private String invoiceType;
	/*
	 * 统一社会信用代码
	 */
	@Column(name = "company_no")
	private String companyNo;
	/*
	 * 统一社会信用代码
	 */
	@Column(name = "code_type")
	private String codeType;
	/*
	 * 发票抬头
	 */
	@Column(name = "invoice_title")
	private String invoiceTitle;
	/*
	 * 开户银行
	 */
	@Column(name = "open_bank")
	private String openBank;
	/*
	 * 银行账号
	 */
	@Column(name = "bank_account_no")
	private String bankAccountNo;
	/*
	 * 地址
	 */
	@Column(name = "company_addr")
	private String companyAddr;
	/*
	 * 电话
	 */
	@Column(name = "phone_no")
	private String phoneNo;
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
	@Column(name = "record_date")
	private String recordDate;

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

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getOpenBank() {
		return openBank;
	}

	public void setOpenBank(String openBank) {
		this.openBank = openBank;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
