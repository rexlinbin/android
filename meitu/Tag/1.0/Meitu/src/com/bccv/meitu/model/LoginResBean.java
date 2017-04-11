package com.bccv.meitu.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class LoginResBean extends NetResBean {
	
	protected int user_id;
	protected String user_name;
	protected String user_icon;
	protected String backdrop;
	
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

	@Override
	public String toString() {
		return "LoginResBean [user_id=" + user_id + ", user_name=" + user_name
				+ ", user_icon=" + user_icon + ", backdrop=" + backdrop
				+ ", success=" + success + ", error=" + error + "]";
	}

	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				user_id = JsonObjectUitl.getInt(jsonObject, "user_id");
				user_name = JsonObjectUitl.getString(jsonObject, "user_name");
				user_icon = JsonObjectUitl.getString(jsonObject, "user_icon");
				backdrop = JsonObjectUitl.getString(jsonObject, "backdrop");
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
	}
}
