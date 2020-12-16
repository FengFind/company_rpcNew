package com.newtec.company.entity.collect;

public class CollectInfo {

	
	private String id;
	private String target_name;
	private Integer target_count;
	private String target_id;
	private String create_time;
	private String sou_name;
	private Integer sou_count;
	private String sou_id;
	private String same;
	private String ch_name;
	private String sou_ch_name;
	private String tar_ch_name;
	private String sou_sys_name;
	private String tar_sys_name;
	private String task_status;
	private String task_cron;
	private String dmct;
	
	public String getDmct() {
		return dmct;
	}
	public void setDmct(String dmct) {
		this.dmct = dmct;
	}
	public String getTask_cron() {
		return task_cron;
	}
	public void setTask_cron(String task_cron) {
		this.task_cron = task_cron;
	}
	public String getTask_status() {
		return task_status;
	}
	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}
	public String getSou_sys_name() {
		return sou_sys_name;
	}
	public void setSou_sys_name(String sou_sys_name) {
		this.sou_sys_name = sou_sys_name;
	}
	public String getTar_sys_name() {
		return tar_sys_name;
	}
	public void setTar_sys_name(String tar_sys_name) {
		this.tar_sys_name = tar_sys_name;
	}
	public String getSou_ch_name() {
		return sou_ch_name;
	}
	public void setSou_ch_name(String sou_ch_name) {
		this.sou_ch_name = sou_ch_name;
	}
	public String getTar_ch_name() {
		return tar_ch_name;
	}
	public void setTar_ch_name(String tar_ch_name) {
		this.tar_ch_name = tar_ch_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTarget_name() {
		return target_name;
	}
	public void setTarget_name(String target_name) {
		this.target_name = target_name;
	}
	public Integer getTarget_count() {
		return target_count;
	}
	public void setTarget_count(Integer target_count) {
		this.target_count = target_count;
	}
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public String getSou_name() {
		return sou_name;
	}
	public void setSou_name(String sou_name) {
		this.sou_name = sou_name;
	}
	public Integer getSou_count() {
		return sou_count;
	}
	public void setSou_count(Integer sou_count) {
		this.sou_count = sou_count;
	}
	public String getSou_id() {
		return sou_id;
	}
	public void setSou_id(String sou_id) {
		this.sou_id = sou_id;
	}
	public String getSame() {
		return same;
	}
	public void setSame(String same) {
		this.same = same;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getCh_name() {
		return ch_name;
	}
	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}
	public CollectInfo() {
		super();
	}
	public CollectInfo(String id, String target_name, Integer target_count, String target_id, String create_time,
			String sou_name, Integer sou_count, String sou_id, String same, String ch_name, String sou_ch_name,
			String tar_ch_name, String sou_sys_name, String tar_sys_name, String task_status, String task_cron,
			String dmct) {
		super();
		this.id = id;
		this.target_name = target_name;
		this.target_count = target_count;
		this.target_id = target_id;
		this.create_time = create_time;
		this.sou_name = sou_name;
		this.sou_count = sou_count;
		this.sou_id = sou_id;
		this.same = same;
		this.ch_name = ch_name;
		this.sou_ch_name = sou_ch_name;
		this.tar_ch_name = tar_ch_name;
		this.sou_sys_name = sou_sys_name;
		this.tar_sys_name = tar_sys_name;
		this.task_status = task_status;
		this.task_cron = task_cron;
		this.dmct = dmct;
	}
	@Override
	public String toString() {
		return "CollectInfo [id=" + id + ", target_name=" + target_name + ", target_count=" + target_count
				+ ", target_id=" + target_id + ", create_time=" + create_time + ", sou_name=" + sou_name
				+ ", sou_count=" + sou_count + ", sou_id=" + sou_id + ", same=" + same + ", ch_name=" + ch_name
				+ ", sou_ch_name=" + sou_ch_name + ", tar_ch_name=" + tar_ch_name + ", sou_sys_name=" + sou_sys_name
				+ ", tar_sys_name=" + tar_sys_name + ", task_status=" + task_status + ", task_cron=" + task_cron
				+ ", dmct=" + dmct + "]";
	}
}
