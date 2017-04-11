package com.bccv.meitu.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class Version extends NetResBean{
	private String version;
	private int version_code;
	private String download;
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getVersion_code() {
		return version_code;
	}
	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	@Override
	public String toString() {
		return "Version [version=" + version + ", version_code=" + version_code
				+ ", download=" + download + "]";
	}
	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				version_code = JsonObjectUitl.getInt(jsonObject, "version_code");
				version = JsonObjectUitl.getString(jsonObject, "version");
				download = JsonObjectUitl.getString(jsonObject, "download");
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
	}
}
