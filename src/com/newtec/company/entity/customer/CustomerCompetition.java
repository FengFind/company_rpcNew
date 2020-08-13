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
 * @Description 客户竞争信息
 * @date 2019年9月2日
 * @version 1.0
 *
 */

@Entity
@Table(name = "customer_competition")
public class CustomerCompetition {
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
	 * 公司等级
	 */
	@Column(name = "company_level")
	private String companyLevel;
	/*
	 * 公司评价
	 */
	@Column(name = "company_comment")
	private String companyComment;

	/*
	 * 营收
	 */
	@Column(name = "revenue")
	private String revenue;
	/*
	 * 利润
	 */
	@Column(name = "profit")
	private String profit;
	/*
	 * 授予折扣
	 */
	@Column(name = "discount")
	private String discount;
	/*
	 * 成长性
	 */
	@Column(name = "growth")
	private String growth;
	/*
	 * 忠诚度
	 */
	@Column(name = "loyalty")
	private String loyalty;
	/*
	 * 营业额预计
	 */
	@Column(name = "turn_over")
	private String turnOver;
	/*
	 * 结算周期预计
	 */
	@Column(name = "settlement_interval")
	private String settlementInterval;
	/*
	 * 开始合作时间
	 */
	@Column(name = "begin_cooperate")
	private String beginCooperate;
	/*
	 * 最近合作时间
	 */
	@Column(name = "recent_cooperate")
	private String recentCooperate;
	/*
	 * 竞争对手
	 */
	@Column(name = "competitor")
	private String competitor;
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

	public String getCompanyLevel() {
		return companyLevel;
	}

	public void setCompanyLevel(String companyLevel) {
		this.companyLevel = companyLevel;
	}

	public String getCompanyComment() {
		return companyComment;
	}

	public void setCompanyComment(String companyComment) {
		this.companyComment = companyComment;
	}

	public String getRevenue() {
		return revenue;
	}

	public void setRevenue(String revenue) {
		this.revenue = revenue;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getGrowth() {
		return growth;
	}

	public void setGrowth(String growth) {
		this.growth = growth;
	}

	public String getLoyalty() {
		return loyalty;
	}

	public void setLoyalty(String loyalty) {
		this.loyalty = loyalty;
	}

	public String getTurnOver() {
		return turnOver;
	}

	public void setTurnOver(String turnOver) {
		this.turnOver = turnOver;
	}

	public String getSettlementInterval() {
		return settlementInterval;
	}

	public void setSettlementInterval(String settlementInterval) {
		this.settlementInterval = settlementInterval;
	}

	public String getBeginCooperate() {
		return beginCooperate;
	}

	public void setBeginCooperate(String beginCooperate) {
		this.beginCooperate = beginCooperate;
	}

	public String getRecentCooperate() {
		return recentCooperate;
	}

	public void setRecentCooperate(String recentCooperate) {
		this.recentCooperate = recentCooperate;
	}

	public String getCompetitor() {
		return competitor;
	}

	public void setCompetitor(String competitor) {
		this.competitor = competitor;
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

}
