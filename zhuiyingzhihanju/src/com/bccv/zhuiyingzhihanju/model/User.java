package com.bccv.zhuiyingzhihanju.model;

public class User {
	/**
	 * 
	 * status----------系统状态码 data------------用户信息 user_id---------用户ID
	 * user_name-------用户名称 nick_name-------用户昵称 user_icon-------用户头像
	 * 
	 * 
	 * 
	 * 
	 **/
	String id;
	String uid;
	String name;
	String avatars;
	String nick_name;
	String userErr;
	String des;
	String sex;
	String token;
	String pwd;
	String phone;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatars() {
		return avatars;
	}
	public void setAvatars(String avatars) {
		this.avatars = avatars;
	}
	public String getNick_name() {
		return nick_name;
	}
	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}
	public String getUserErr() {
		return userErr;
	}
	public void setUserErr(String userErr) {
		this.userErr = userErr;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

}
