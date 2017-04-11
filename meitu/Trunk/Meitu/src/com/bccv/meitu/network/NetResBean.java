package com.bccv.meitu.network;

import org.json.JSONObject;

import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;



public abstract class NetResBean {

	protected String TAG = this.getClass().getSimpleName();
	
	private String data;
	public boolean success;
	public int error;
	
	/**
	 * 解析数据抽象方法  具体逻辑子类实现
	 * 
	 * @param data
	 */
	public abstract void parseData(String data);
	
	public final void setData(String data){
		this.data = data;
		Logger.v(TAG, "setData", "data : " + data);
		
		try {
			JSONObject jsonObject = new JSONObject(data);
				success = ("ok".equals(jsonObject.getString("request")));
				error = JsonObjectUitl.getInt(jsonObject, "error");
				Logger.v(TAG, "setData", "success : " + String.valueOf(success) + " error : " + error);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.v(TAG, "setData", e.getMessage());
			success = false;
			error = -1;
		}
		
		parseData(this.data);
	}

	@Override
	public String toString() {
		return this.data;
	}
	
}
