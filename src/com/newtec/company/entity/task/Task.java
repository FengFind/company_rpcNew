package com.newtec.company.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author
 * @Description 跟进任务信息
 * @date 2019年9月2日
 * @version 1.0
 *
 */

@Entity
@Table(name = "bus_opp_followup")
public class Task {

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
	 * 客户名称
	 */
	@Column(name = "customer_name")
	private String customerName;
	
	/*
	 * 商机ID
	 */
	@Column(name = "bus_id")
	private String busId;	
	
	/*
	 * 商机任务
	 */
	@Column(name = "opp_name")
	private String oppName;	
	/*
	 * 跟进任务
	 */
	@Column(name = "followup_task")
	private String followupTask;
	/*
	 * 跟进人id
	 */
	@Column(name = "followup_person_id")
	private String followupPersonId;
	/*
	 * 跟进人
	 */
	@Column(name = "followup_person")
	private String followupPerson;
	/*
	 * 提醒间隔
	 */
	@Column(name = "remind_interval")
	private String remindInterval;
	/*
	 * 提醒方式
	 */
	@Column(name = "remind_way")
	private String remindWay;
	/*
	 * 商机阶段
	 */
	@Column(name = "business_stage")
	private String businessStage;
	/*
	 * 跟进任务状态
	 */
	@Column(name = "task_status")
	private String taskStatus;
	/*
	 * 紧要程度
	 */
	@Column(name = "urgency")
	private String urgency;

	/*
	 * 记录日期
	 */
	@Column(name = "record_date")
	private String recordDate;
	

	/*
	 * 联系人
	 */
	@Column(name = "contact")
	private String contact;
	/*
	 * 联系电话
	 */
	@Column(name = "contact_phone")
	private String contactPhone;
	/*
	 * 跟进时间
	 */
	@Column(name = "occurrencetime")
	private String occurrencetime;
	/*
	 * 创建人Id
	 */
	@Column(name = "staff_id")
	private String staffId;
	/*
	 * 创建人名称
	 */
	@Column(name = "staff_name")
	private String staffName;
	/*
	 * 负责人名称
	 */
	@Column(name = "responsible_id")
	private String responsibleId;
	/*
	 * 负责人名称
	 */
	@Column(name = "responsible")
	private String responsible;
	
	/*
	 * 跟进内容
	 */
	@Column(name = "follow_text")
	private String followText;
	
	/*
	 * 联系人id
	 */
	@Column(name = "concat_id")
	private String concatId;
	
	/*
	 * 任务是否已经被跟进
	 */
	@Column(name = "is_follow")
	private String hasFollow;

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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getOppName() {
		return oppName;
	}

	public void setOppName(String oppName) {
		this.oppName = oppName;
	}

	public String getFollowupTask() {
		return followupTask;
	}

	public void setFollowupTask(String followupTask) {
		this.followupTask = followupTask;
	}

	public String getFollowupPersonId() {
		return followupPersonId;
	}

	public void setFollowupPersonId(String followupPersonId) {
		this.followupPersonId = followupPersonId;
	}

	public String getFollowupPerson() {
		return followupPerson;
	}

	public void setFollowupPerson(String followupPerson) {
		this.followupPerson = followupPerson;
	}

	public String getRemindInterval() {
		return remindInterval;
	}

	public void setRemindInterval(String remindInterval) {
		this.remindInterval = remindInterval;
	}

	public String getRemindWay() {
		return remindWay;
	}

	public void setRemindWay(String remindWay) {
		this.remindWay = remindWay;
	}

	public String getBusinessStage() {
		return businessStage;
	}

	public void setBusinessStage(String businessStage) {
		this.businessStage = businessStage;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getOccurrencetime() {
		return occurrencetime;
	}

	public void setOccurrencetime(String occurrencetime) {
		this.occurrencetime = occurrencetime;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getFollowText() {
		return followText;
	}

	public void setFollowText(String followText) {
		this.followText = followText;
	}

	public String getConcatId() {
		return concatId;
	}

	public void setConcatId(String concatId) {
		this.concatId = concatId;
	}

	public String getHasFollow() {
		return hasFollow;
	}

	public void setHasFollow(String hasFollow) {
		this.hasFollow = hasFollow;
	}

	public String getResponsibleId() {
		return responsibleId;
	}

	public void setResponsibleId(String responsibleId) {
		this.responsibleId = responsibleId;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
}
