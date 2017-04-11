package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class MyGameListResBean extends NetResBean {

	private int now_p; //当前页
	private int total_p; //总共页
	private ArrayList<GameInfoBean> appInfoItemBeans;
	
	public int getNow_p() {
		return now_p;
	}

	public void setNow_p(int now_p) {
		this.now_p = now_p;
	}

	public int getTotal_p() {
		return total_p;
	}

	public void setTotal_p(int total_p) {
		this.total_p = total_p;
	}

	public ArrayList<GameInfoBean> getAppInfoItemBeans() {
		return appInfoItemBeans;
	}

	public void setAppInfoItemBeans(ArrayList<GameInfoBean> appInfoItemBeans) {
		this.appInfoItemBeans = appInfoItemBeans;
	}
	
	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				now_p = jsonObject.optInt("now_p");
				total_p = jsonObject.optInt("total_p");
				JSONArray jsonArray = jsonObject.optJSONArray("result");
				if (jsonArray == null) {
					jsonArray = jsonObject.optJSONArray("data");
				}
				appInfoItemBeans = new ArrayList<GameInfoBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					GameInfoBean appInfoItemBean = new GameInfoBean();
					appInfoItemBean.parse(dataObject);	
					appInfoItemBeans.add(appInfoItemBean);
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
