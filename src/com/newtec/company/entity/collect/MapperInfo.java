package com.newtec.company.entity.collect;

public class MapperInfo {

	private String ch_name;
	private String sou_name;
	private String tar_name;
	private String state;
	public String getCh_name() {
		return ch_name;
	}
	public void setCh_name(String ch_name) {
		this.ch_name = ch_name;
	}
	public String getSou_name() {
		return sou_name;
	}
	public void setSou_name(String sou_name) {
		this.sou_name = sou_name;
	}
	public String getTar_name() {
		return tar_name;
	}
	public void setTar_name(String tar_name) {
		this.tar_name = tar_name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public MapperInfo() {
		super();
	}
	public MapperInfo(String ch_name, String sou_name, String tar_name, String state) {
		super();
		this.ch_name = ch_name;
		this.sou_name = sou_name;
		this.tar_name = tar_name;
		this.state = state;
	}
	
}
