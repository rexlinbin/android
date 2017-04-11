package com.bccv.bangyangapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.L;

public class SearchRecommendListResBean extends NetResBean {

	private ArrayList<String> recommendList;

	public ArrayList<String> getRecommendList() {
		return recommendList;
	}

	public void setRecommendList(ArrayList<String> recommendList) {
		this.recommendList = recommendList;
	}
	
	@Override
	public String toString() {
		return "SearchRecommendListResBean [recommendList=" + recommendList
				+ "]";
	}

	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				recommendList = new ArrayList<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					String name = dataObject.optString("name");
					recommendList.add(name);
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
