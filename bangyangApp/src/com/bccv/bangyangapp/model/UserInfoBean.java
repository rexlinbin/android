package com.bccv.bangyangapp.model;

import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.JsonObjectUitl;

public class UserInfoBean extends NetResBean{

	/*
	 *  "introduce": "巴拉巴拉",
        "user_icon": "http://http://appsapi.bccv.com/res/icon/2015-04-21/yk6wwir570.jpeg",
        "user_id": "8",
        "user_name": "测试"
	 */
	
	private String introduce;
	private String user_icon;
	private String user_id;
	private String user_name;
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public String getUser_icon() {
		return user_icon;
	}
	public void setUser_icon(String user_icon) {
		this.user_icon = user_icon;
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
	@Override
	public void parseData(String jsonData) {
		try {
			JSONObject object = new JSONObject(JsonObjectUitl.getString(new JSONObject(jsonData), "data"));
			setUser_icon(JsonObjectUitl.getString(object, "user_icon"));
			setIntroduce(JsonObjectUitl.getString(object, "introduce"));
			setUser_id(JsonObjectUitl.getString(object, "user_id"));
			setUser_name(JsonObjectUitl.getString(object, "user_name"));
		} catch (Exception e) {
		}
	}
}
