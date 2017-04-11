package com.bccv.strategy.model;

public class UserInfo {

	private int user_id = -1;
	private String user_name;
	private String user_icon;
	private String backdrop;
	private int user_type;
	private int user_money;
	private int user_score;
	private String user_group;
	private String user_binds;
	private String user_token;
	private String user_introduce;;
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	
	public int getUser_type() {
		return user_type;
	}

	public void setUser_type(int user_type) {
		this.user_type = user_type;
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

	public String getBackdrop() {
		return backdrop;
	}

	public void setBackdrop(String backdrop) {
		this.backdrop = backdrop;
	}

	public int getUser_money() {
		return user_money;
	}

	public void setUser_money(int user_money) {
		this.user_money = user_money;
	}

	public int getUser_score() {
		return user_score;
	}

	public void setUser_score(int user_score) {
		this.user_score = user_score;
	}

	public String getUser_group() {
		return user_group;
	}

	public void setUser_group(String user_group) {
		this.user_group = user_group;
	}
	
	public String getUser_binds() {
		return user_binds;
	}

	public void setUser_binds(String user_binds) {
		this.user_binds = user_binds;
	}
	
	public String getUser_token() {
		return user_token;
	}

	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}

	public String getUser_introduce() {
		return user_introduce;
	}

	public void setUser_introduce(String user_introduce) {
		this.user_introduce = user_introduce;
	}

	@Override
	public String toString() {
		return "UserInfo [user_id=" + user_id + ", user_name=" + user_name
				+ ", user_icon=" + user_icon + ", backdrop=" + backdrop
				+ ", user_type=" + user_type + ", user_money=" + user_money
				+ ", user_score=" + user_score + ", user_group=" + user_group
				+ ", user_binds=" + user_binds + ", user_token=" + user_token
				+ ", user_introduce=" + user_introduce + "]";
	}
	
}
