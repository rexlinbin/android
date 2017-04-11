package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class HotFreshListResBean extends NetResBean {

	private int now_p; //当前页
	private int total_p; //总共页
	private ArrayList<AppInfoItemBean> appInfoItemBeans;
	
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

	public ArrayList<AppInfoItemBean> getAppInfoItemBeans() {
		return appInfoItemBeans;
	}

	public void setAppInfoItemBeans(ArrayList<AppInfoItemBean> appInfoItemBeans) {
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
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				appInfoItemBeans = new ArrayList<AppInfoItemBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					AppInfoItemBean appInfoItemBean = new AppInfoItemBean();
					
//					appInfoItemBean.setId(dataObject.optString("cat_title"));
//					appInfoItemBean.setCreator_id(dataObject.optString("creator_id"));
//					appInfoItemBean.setCreator_name(dataObject.optString("creator_name"));
//					appInfoItemBean.setGame_icon(dataObject.optString("game_icon"));
//					appInfoItemBean.setGame_title(dataObject.optString("game_title"));
//					appInfoItemBean.setId(dataObject.optString("id"));
//					appInfoItemBean.setNews_click(dataObject.optString("news_click"));
//					appInfoItemBean.setNews_ctime(dataObject.optLong("news_ctime")*1000l);
//					appInfoItemBean.setCreator_id(dataObject.optString("news_digg"));
//					appInfoItemBean.setCreator_name(dataObject.optString("news_focus"));
//					appInfoItemBean.setNews_ftitle(dataObject.optString("news_ftitle"));
//					appInfoItemBean.setNews_good(dataObject.optString("news_good"));
//					appInfoItemBean.setNews_path(dataObject.optString("news_path"));
//					appInfoItemBean.setNews_title(dataObject.optString("news_title"));
//					appInfoItemBean.setNews_titlepic(dataObject.optString("news_titlepic"));
//					appInfoItemBean.setNews_introduce(dataObject.optString("news_introduce"));
//					appInfoItemBean.setNews_comment(dataObject.optString("news_comment"));
//					appInfoItemBean.setChoice(dataObject.optInt("choice"));
//					appInfoItemBean.setPrice(dataObject.optString("price"));
//					appInfoItemBean.setType_name(dataObject.optString("type_name"));
					
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
