package com.bccv.tianji.model;

public class User {
	/**
	 * 
	 * status----------系统状态码 data------------用户信息 user_id---------用户ID
	 * user_name-------用户名称 nick_name-------用户昵称 user_icon-------用户头像
	 * 
	 * 
	 * 
	 * 
	 * **/

	String user_id;
	String user_name;
	String user_icon;
	String nick_name;
String userErr;
	public String getUserErr() {
	return userErr;
}

public void setUserErr(String userErr) {
	this.userErr = userErr;
}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_icon() {
		return user_icon;
	}

	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_name=" + user_name
				+ ", user_icon=" + user_icon + ", nick_name=" + nick_name + "]";
	}

}
