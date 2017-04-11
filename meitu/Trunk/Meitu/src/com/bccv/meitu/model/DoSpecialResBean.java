package com.bccv.meitu.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class DoSpecialResBean extends NetResBean {

	private int special_id;
	
	public int getSpecial_id() {
		return special_id;
	}

	public void setSpecial_id(int special_id) {
		this.special_id = special_id;
	}

	@Override
	public void parseData(String data) {
		try {
			JSONObject jsonObject = new JSONObject(data);
			special_id = JsonObjectUitl.getInt(jsonObject, "special_id");
		} catch (JSONException e) {
			success = false;
			Logger.e(TAG, "parseData", e.getMessage());
		}
	}

}
