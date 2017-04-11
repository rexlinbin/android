package com.bccv.meitu.network;

import org.json.JSONObject;

import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;



public abstract class NetResBean {

	protected String TAG = this.getClass().getSimpleName();
	
	private String data;
	public boolean success;
	public String msg;
	
	/**
	 * 解析数据抽象方法  具体逻辑子类实现
	 * 
	 * @param data
	 */
	public abstract void parseData(String data);
	
	public final void setData(String data){
		this.data = data;
		
		try {
			JSONObject jsonObject = new JSONObject(data);
				success = ("ok".equals(jsonObject.getString("request")));
				msg = JsonObjectUitl.getString(jsonObject, "error");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Logger.v(TAG, "setData", e.getMessage());
			success = false;
			msg = " net data is error ! ";
		}
		
		parseData(this.data);
	}

	@Override
	public String toString() {
		return this.data;
	}
	
}
