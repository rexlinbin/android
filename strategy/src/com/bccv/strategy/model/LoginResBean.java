package com.bccv.strategy.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.JsonObjectUitl;
import com.bccv.strategy.utils.L;

public class LoginResBean extends NetResBean {
	
	protected int user_id;
	protected String user_name;
	protected String user_icon;
	protected String backdrop;
	protected String token;
	protected String introduce;
	
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
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
	
	public String getBackdrop() {
		return backdrop;
	}

	public void setBackdrop(String backdrop) {
		this.backdrop = backdrop;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Override
	public String toString() {
		return "LoginResBean [user_id=" + user_id + ", user_name=" + user_name
				+ ", user_icon=" + user_icon + ", backdrop=" + backdrop
				+ ", token=" + token + ", introduce=" + introduce + "]";
	}

	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				user_id = JsonObjectUitl.getInt(jsonObject, "user_id");
				user_name = JsonObjectUitl.getString(jsonObject, "user_name");
				user_icon = JsonObjectUitl.getString(jsonObject, "user_icon");
				introduce = JsonObjectUitl.getString(jsonObject, "introduce");
//				backdrop = JsonObjectUitl.getString(jsonObject, "backdrop");
//				token = JsonObjectUitl.getString(jsonObject, "token");
				
			} catch (JSONException e) {
				success = false;
				L.e(TAG, "parseData", e.getMessage());
			}
		}
	}
}
