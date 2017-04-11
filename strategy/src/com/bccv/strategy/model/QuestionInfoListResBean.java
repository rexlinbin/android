package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class QuestionInfoListResBean extends NetResBean {

	private int now_p; //当前页
	private int total_p; //总共页
	private ArrayList<QuestionInfoBean> questionInfoBeans;
	
	
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

	public ArrayList<QuestionInfoBean> getQuestionInfoBeans() {
		return questionInfoBeans;
	}

	public void setQuestionInfoBeans(ArrayList<QuestionInfoBean> questionInfoBeans) {
		this.questionInfoBeans = questionInfoBeans;
	}

	@Override
	public String toString() {
		return "QuestionInfoListResBean [now_p=" + now_p + ", total_p="
				+ total_p + ", questionInfoBeans=" + questionInfoBeans
				+ ", success=" + success + ", status_code=" + status_code + "]";
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
				questionInfoBeans = new ArrayList<QuestionInfoBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					QuestionInfoBean questionInfoBean = new QuestionInfoBean();
					questionInfoBean.setId(dataObject.optString("id"));
					questionInfoBean.setUser_id(dataObject.optString("user_id"));
					questionInfoBean.setUser_name(dataObject.optString("user_name"));
					questionInfoBean.setUser_icon(dataObject.optString("user_icon"));
					questionInfoBean.setContent(dataObject.optString("content"));
					questionInfoBean.setReply_num(dataObject.optString("reply_num"));
					questionInfoBean.setTimes(dataObject.optLong("times")*1000l);
					questionInfoBeans.add(questionInfoBean);
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
