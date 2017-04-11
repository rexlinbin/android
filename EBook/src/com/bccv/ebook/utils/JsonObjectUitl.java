package com.bccv.ebook.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonObjectUitl {

	public static int getInt(JSONObject jsonObject, String name) {
		try {
			return jsonObject.getInt(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String getString(JSONObject jsonObject, String name) {
		try {
			return jsonObject.getString(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static JSONObject getJSONObject(JSONObject jsonObject, String name) {
		try {
			return jsonObject.getJSONObject(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JSONArray getJSONArray(JSONObject jsonObject, String name) {
		try {
			return jsonObject.getJSONArray(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
