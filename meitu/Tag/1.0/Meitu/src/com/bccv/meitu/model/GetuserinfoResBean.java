package com.bccv.meitu.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetuserinfoResBean extends NetResBean {

	
	private int user_id;
	private String user_name;
	private String user_icon;
	private String backdrop;
	private String binds;
	private int user_money;
	private int user_score;
	private String user_group;
	private int version_code;
//	private String version;
	private String download;
	
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
	
	public String getBinds() {
		return binds;
	}

	public void setBinds(String binds) {
		this.binds = binds;
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
	
	public int getVersion_code() {
		return version_code;
	}

	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}

//	public String getVersion() {
//		return version;
//	}
//
//	public void setVersion(String version) {
//		this.version = version;
//	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				user_id = JsonObjectUitl.getInt(jsonObject, "user_id");
				user_name = JsonObjectUitl.getString(jsonObject, "user_name");
				user_icon = JsonObjectUitl.getString(jsonObject, "user_icon");
				binds = JsonObjectUitl.getString(jsonObject, "binds");
				backdrop = JsonObjectUitl.getString(jsonObject, "backdrop");
				user_money = JsonObjectUitl.getInt(jsonObject, "user_money");
				user_score = JsonObjectUitl.getInt(jsonObject, "user_score");
				user_group = JsonObjectUitl.getString(jsonObject, "user_group");
				version_code = JsonObjectUitl.getInt(jsonObject, "version_code");
//				version = JsonObjectUitl.getString(jsonObject, "version");
				download = JsonObjectUitl.getString(jsonObject, "download");
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
	}

	@Override
	public String toString() {
		return "GetuserinfoResBean [user_id=" + user_id + ", user_name="
				+ user_name + ", user_icon=" + user_icon + ", backdrop="
				+ backdrop + ", binds=" + binds + ", user_money=" + user_money
				+ ", user_score=" + user_score + ", user_group=" + user_group
				+ ", version_code=" + version_code +  ", download=" + download + ", success=" + success
				+ ", error=" + error + "]";
	}


}
