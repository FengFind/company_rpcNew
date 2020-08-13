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
 * @Description 客户风险信息
 * @date 2019年9月2日
 * @version 1.0
 *
 */

@Entity
@Table(name = "customer_risk")
public class CustomerRisk {

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
	 * 风险编号
	 */
	@Column(name = "risk_no")
	private String riskNo;
	/*
	 * 风险等级
	 */
	@Column(name = "risk_level")
	private String riskLevel;
	/*
	 * 风险类型
	 */
	@Column(name = "risk_type")
	private String riskType;
	/*
	 * 风险来源
	 */
	@Column(name = "risk_source")
	private String riskSource;
	/*
	 * 影响范围
	 */
	@Column(name = "influence_shape")
	private String influenceShape;
	/*
	 * 影响地名
	 */
	@Column(name = "influence_add")
	private String influenceAdd;
	/*
	 * 发生时间
	 */
	@Column(name = "occurrence_time")
	private String occurrenceTime;
	/*
	 * 记录日期
	 */
	@Column(name = "record_date")
	private String recordDate;
	/*
	 * 风险说明
	 */
	@Column(name = "risk_explain")
	private String riskExplain;
	/*
	 * 输入模式
	 */
	@Column(name = "input_pattern")
	private String inputPattern;

	/*
	 * 人员ID
	 */
	@Column(name = "staff_id")
	private String staffId;

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

	public String getRiskNo() {
		return riskNo;
	}

	public void setRiskNo(String riskNo) {
		this.riskNo = riskNo;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getRiskSource() {
		return riskSource;
	}

	public void setRiskSource(String riskSource) {
		this.riskSource = riskSource;
	}

	public String getInfluenceShape() {
		return influenceShape;
	}

	public void setInfluenceShape(String influenceShape) {
		this.influenceShape = influenceShape;
	}

	public String getInfluenceAdd() {
		return influenceAdd;
	}

	public void setInfluenceAdd(String influenceAdd) {
		this.influenceAdd = influenceAdd;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getOccurrenceTime() {
		return occurrenceTime;
	}

	public void setOccurrenceTime(String occurrenceTime) {
		this.occurrenceTime = occurrenceTime;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public String getRiskExplain() {
		return riskExplain;
	}

	public void setRiskExplain(String riskExplain) {
		this.riskExplain = riskExplain;
	}

	public String getInputPattern() {
		return inputPattern;
	}

	public void setInputPattern(String inputPattern) {
		this.inputPattern = inputPattern;
	}
}
