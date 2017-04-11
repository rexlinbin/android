package com.bccv.ebook.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.ebook.network.NetResBean;
import com.bccv.ebook.utils.JsonObjectUitl;
import com.bccv.ebook.utils.L;

public class VersionInfo extends NetResBean{

	private	boolean hasNewVer = false; // 是否有新版本
	private int app_up = 0;	 //是否有新版本
	private String app_ver ; //升级版本 名
	private int app_upver;	 //升级版本号
	private String app_url ; //下载url
	private String app_des ; //升级描述
	
	public boolean isHasNewVer() {
		return hasNewVer;
	}

	public void setHasNewVer(boolean hasNewVer) {
		this.hasNewVer = hasNewVer;
	}

	public int getApp_up() {
		return app_up;
	}

	public void setApp_up(int app_up) {
		this.app_up = app_up;
	}

	public String getApp_ver() {
		return app_ver;
	}

	public void setApp_ver(String app_ver) {
		this.app_ver = app_ver;
	}

	public int getApp_upver() {
		return app_upver;
	}

	public void setApp_upver(int app_upver) {
		this.app_upver = app_upver;
	}

	public String getApp_url() {
		return app_url;
	}

	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}

	public String getApp_des() {
		return app_des;
	}

	public void setApp_des(String app_des) {
		this.app_des = app_des;
	}

	@Override
	public String toString() {
		return "VersionInfo [hasNewVer=" + hasNewVer + ", app_up=" + app_up
				+ ", app_ver=" + app_ver + ", app_upver=" + app_upver
				+ ", app_url=" + app_url + ", app_des=" + app_des + "]";
	}

	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				app_up = JsonObjectUitl.getInt(jsonObject, "app_up");
				if(app_up==1){
					hasNewVer = true;
				}
				app_upver = JsonObjectUitl.getInt(jsonObject, "app_upver");
				app_ver = JsonObjectUitl.getString(jsonObject, "app_ver");
				app_url = JsonObjectUitl.getString(jsonObject, "app_url");
				app_des = JsonObjectUitl.getString(jsonObject, "app_des");
				
			} catch (JSONException e) {
				success = false;
				L.e(TAG, "parseData", e.getMessage());
			}
		}
	}
}
