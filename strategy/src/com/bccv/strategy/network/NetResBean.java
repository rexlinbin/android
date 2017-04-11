package com.bccv.strategy.network;

import org.json.JSONObject;

import com.bccv.strategy.utils.L;


public abstract class NetResBean {

	protected String TAG = this.getClass().getSimpleName();

	private String jsonData;
	public boolean success;
	public int status_code;

	/**
	 * 解析数据抽象方法 具体逻辑子类实现
	 * 
	 * @param data
	 */
	public abstract void parseData(String jsonData);

	public final void setData(String jsonData) {
		this.jsonData = jsonData;
		L.v(TAG, "setData", "jsonData : " + jsonData);

		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			success = (1==jsonObject.getInt("status"));
			status_code = jsonObject.optInt("status_code", -1);
			L.v(TAG, "setData", "success : " + String.valueOf(success)
					+ " status_code : " + status_code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			L.v(TAG, "setData", e.getMessage());
			success = false;
			status_code = -1;
		}

		parseData(this.jsonData);
	}

	@Override
	public String toString() {
		return this.jsonData;
	}

}
