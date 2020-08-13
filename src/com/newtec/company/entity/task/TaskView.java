package com.newtec.company.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "view_opp_task")
public class TaskView {
	
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
	 * 商机名称
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
	 * 负责Id
	 */
	@Column(name = "staff_id")
	private String staffId;
	/*
	 * 负责人
	 */
	@Column(name = "staff_name")
	private String staffName;
	/*
	 * 跟进内容
	 */
	@Column(name = "follow_text")
	private String followText;
	
		
	//联系目的
	@Column(name = "contact_purpose")
	private String contactPurpose;
	
	//反馈意见
	@Column(name = "follow_feedback")
	private String followFeedback;
	
	//访问时间
	@Column(name = "access_time")
	private String accessTime;
	
	//访问方式
	@Column(name = "access_mode")
	private String accessMode;
	
	/*
	 * 跟进人
	 */
	@Column(name = "followup_person")
	private String followupPerson;

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

	
	public String getFollowupPerson() {
		return followupPerson;
	}

	public void setFollowupPerson(String followupPerson) {
		this.followupPerson = followupPerson;
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

	public String getContactPurpose() {
		return contactPurpose;
	}

	public void setContactPurpose(String contactPurpose) {
		this.contactPurpose = contactPurpose;
	}

	public String getFollowFeedback() {
		return followFeedback;
	}

	public void setFollowFeedback(String followFeedback) {
		this.followFeedback = followFeedback;
	}

	public String getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(String accessTime) {
		this.accessTime = accessTime;
	}

	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}
	
}
