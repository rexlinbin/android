package com.boxuu.gamebox.network;

import org.json.JSONObject;

import com.boxuu.gamebox.utils.L;

public abstract class NetResBean {

	protected String TAG = this.getClass().getSimpleName();

	private String data;
	public boolean success;
//	public int error;

	/**
	 * 解析数据抽象方法 具体逻辑子类实现
	 * 
	 * @param data
	 */
	public abstract void parseData(String data);

	public final void setData(String data) {
		this.data = data;
		L.v(TAG, "setData", "data : " + data);

		try {
			JSONObject jsonObject = new JSONObject(data);
			success = ("1".equals(jsonObject.getString("status")));
			if (success) {
				parseData(this.data);
			}
//			error = jsonObject.optInt("error", -1);
//			error = JsonObjectUitl.getInt(jsonObject, "error");
//			L.v(TAG, "setData", "success : " + String.valueOf(success)
//					+ " error : " + error);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			L.v(TAG, "setData", e.getMessage());
			success = false;
//			error = -1;
		}
	}

	@Override
	public String toString() {
		return this.data;
	}

}
