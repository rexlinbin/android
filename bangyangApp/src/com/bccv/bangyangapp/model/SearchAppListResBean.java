package com.bccv.bangyangapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.L;

public class SearchAppListResBean extends NetResBean {

	private ArrayList<AppInfoItemBean> appInfoItemBeans;
	

	public ArrayList<AppInfoItemBean> getAppInfoItemBeans() {
		return appInfoItemBeans;
	}

	public void setAppInfoItemBeans(ArrayList<AppInfoItemBean> appInfoItemBeans) {
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
				appInfoItemBeans = new ArrayList<AppInfoItemBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					AppInfoItemBean appInfoItemBean = new AppInfoItemBean();
					appInfoItemBean.setId(dataObject.optString("id"));
					appInfoItemBean.setTitle(dataObject.optString("title"));
					appInfoItemBean.setFtitle(dataObject.optString("ftitle"));
					appInfoItemBean.setIntroduce(dataObject.optString("introduce"));
					appInfoItemBean.setIcon(dataObject.optString("icon"));
					appInfoItemBean.setPic(dataObject.optString("pic"));
					appInfoItemBean.setChoice(dataObject.optInt("choice"));
					appInfoItemBean.setTimes(dataObject.optLong("times")*1000l);
					appInfoItemBean.setCreator_id(dataObject.optString("creator_id"));
					appInfoItemBean.setCreator_name(dataObject.optString("creator_name"));
					appInfoItemBean.setCreator_icon(dataObject.optString("creator_icon"));
					appInfoItemBean.setComment(dataObject.optString("comment"));
					appInfoItemBean.setLike_num(dataObject.optInt("like_num"));
//					appInfoItemBean.setComment_num(dataObject.optInt("comment_num"));
//					appInfoItemBean.setIs_first(dataObject.optInt("is_first"));
//					appInfoItemBean.setPrice(dataObject.optString("price"));
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
