package com.bccv.bangyangapp.model;

import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.JsonObjectUitl;

public class VersionBean extends NetResBean{

	/**
	 * "code": "1001",
            "download": "https://itunes.apple.com/cn/app/you-xi-gong-e-for-wen-yi-gong-si/id887825905?mt=8&uo=4",
            "id": "1",
            "name": "V1.2.1",
            "type": "1"
	 */
	
	private int code;
	private String download;
	private int id;
	private String name;
	private int type;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public void parseData(String jsonData) {
		try {
			
			JSONObject object = new JSONObject(JsonObjectUitl.getString(new JSONObject(jsonData), "data")); 
			setCode(JsonObjectUitl.getInt(object, "code"));
			setDownload(JsonObjectUitl.getString(object, "download"));
			setId(JsonObjectUitl.getInt(object, "id"));
			setName(JsonObjectUitl.getString(object, "name"));
			setType(JsonObjectUitl.getInt(object, "type"));
			
		} catch (Exception e) {
			e.printStackTrace();
			return ;
		}
	}
}
