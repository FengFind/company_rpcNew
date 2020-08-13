package com.newtec.company.entity.collect;

public class Limit {

	private String id;
	private String name;
	private String url;
	private String password;
	private String username;
	private String type;
	private String sysname;
	public String getSysname() {
		return sysname;
	}
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Limit(String id, String name, String url, String password, String username, String type, String sysname) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
		this.password = password;
		this.username = username;
		this.type = type;
		this.sysname = sysname;
	}
	public Limit() {
		super();
	}
	
	
	
}
