package com.newtec.company.entity.task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "bus_opp_task_result")
public class TaskResult {
	@Id
	@GenericGenerator(name = "hibernate-uuid", strategy = "uuid")
	@GeneratedValue(generator = "hibernate-uuid")
	private String id;

	//任务id
	@Column(name = "bus_id")
	private String busId;
	
	//任务id
	@Column(name = "task_id")
	private String taskId;
	
	// 记录日期
	@Column(name = "record_date")
	private String recordDate;

	//联系人
	@Column(name = "contact")
	private String contact;
	
	//联系号码
	@Column(name = "contact_phone")
	private String contactPhone;
	
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
	
	//操作人id
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
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

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
	
}
