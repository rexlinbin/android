package com.bccv.bangyangapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.L;

public class UserComment2appListResBean extends NetResBean {
	
	private int now_p;
	private int total_p;
	private ArrayList<MyCommentItemBean> comments;
	
	public ArrayList<MyCommentItemBean> getComments() {
		return comments;
	}

	public void setComments(ArrayList<MyCommentItemBean> comments) {
		this.comments = comments;
	}

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

	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				now_p = jsonObject.optInt("now_p");
				total_p = jsonObject.optInt("total_p");
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				comments = new ArrayList<MyCommentItemBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					MyCommentItemBean commentItemBean = new MyCommentItemBean();
					commentItemBean.setId(dataObject.optString("id"));
					commentItemBean.setTitle(dataObject.optString("title"));
					commentItemBean.setComment(dataObject.optString("comment"));
					commentItemBean.setIcon(dataObject.optString("icon"));
					commentItemBean.setApp_id(dataObject.optString("app_id"));
					comments.add(commentItemBean);
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
