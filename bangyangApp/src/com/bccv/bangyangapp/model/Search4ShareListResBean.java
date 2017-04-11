package com.bccv.bangyangapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.L;

public class Search4ShareListResBean extends NetResBean {

	private ArrayList<InstallAppInfo> appInfoItemBeans;
	

	public ArrayList<InstallAppInfo> getAppInfoItemBeans() {
		return appInfoItemBeans;
	}

	public void setAppInfoItemBeans(ArrayList<InstallAppInfo> appInfoItemBeans) {
		this.appInfoItemBeans = appInfoItemBeans;
	}
	
	@Override
	public String toString() {
		return "SearchAppListResBean [appInfoItemBeans=" + appInfoItemBeans
				+ "]";
	}

	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				appInfoItemBeans = new ArrayList<InstallAppInfo>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					InstallAppInfo appInfo = new InstallAppInfo();
					appInfo.setPackageName(dataObject.optString("itunes_id"));
					appInfo.setAppName(dataObject.optString("title"));
					appInfo.setIconUrl(dataObject.optString("icon"));
					appInfo.setIntroduce(dataObject.optString("introduce"));
					appInfoItemBeans.add(appInfo);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				success = false;
				e.printStackTrace();
				L.e(TAG, "parseData", e.getMessage());
			}
		}
	}
	
	
	
}
