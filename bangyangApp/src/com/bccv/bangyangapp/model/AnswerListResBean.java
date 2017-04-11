package com.bccv.bangyangapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.L;

public class AnswerListResBean extends NetResBean {
	
	private int now_p; //当前页
	private int total_p; //总共页
	private ArrayList<ReplyBean> answerList;
	
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

	public ArrayList<ReplyBean> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(ArrayList<ReplyBean> answerList) {
		this.answerList = answerList;
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
				answerList = new ArrayList<ReplyBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					ReplyBean replyBean = new ReplyBean();
					replyBean.setId(dataObject.optString("id"));
					replyBean.setUser_id(dataObject.optInt("user_id"));
					replyBean.setUser_name(dataObject.optString("user_name"));
					replyBean.setUser_icon(dataObject.optString("user_icon"));
					replyBean.setComment(dataObject.optString("answer"));
					replyBean.setTimes(dataObject.optLong("times")*1000l);
					replyBean.setDigg(dataObject.optInt("digg"));
					answerList.add(replyBean);
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
