package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class MoreDiggiconResBean extends NetResBean {
	
	private int now_p; //当前页
	private int total_p; //总共页
	private ArrayList<Diggicon> diggicons;
	
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

	public ArrayList<Diggicon> getDiggicons() {
		return diggicons;
	}

	public void setDiggicons(ArrayList<Diggicon> diggicons) {
		this.diggicons = diggicons;
	}

	public class Diggicon{
		private String user_id;
		private String user_name;
		private String user_icon;
		public String getUser_id() {
			return user_id;
		}
		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}
		public String getUser_name() {
			return user_name;
		}
		public void setUser_name(String user_name) {
			this.user_name = user_name;
		}
		public String getUser_icon() {
			return user_icon;
		}
		public void setUser_icon(String user_icon) {
			this.user_icon = user_icon;
		}
		@Override
		public String toString() {
			return "Diggicon [user_id=" + user_id + ", user_name=" + user_name
					+ ", user_icon=" + user_icon + "]";
		}
		
	}


	@Override
	public String toString() {
		return "MoreDiggiconResBean [now_p=" + now_p + ", total_p=" + total_p
				+ ", diggicons=" + diggicons + ", success=" + success
				+ ", status_code=" + status_code + "]";
	}

	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				now_p = jsonObject.optInt("now_p");
				total_p = jsonObject.optInt("total_p");
				diggicons = new ArrayList<Diggicon>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					Diggicon diggicon = new Diggicon();
					diggicon.user_icon = dataObject.optString("user_icon");
					diggicon.user_id = dataObject.optString("user_id");
					diggicon.user_name = dataObject.optString("user_name");
					diggicons.add(diggicon);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				L.e(TAG, "parseData", e.getMessage());
			}
		}
	}

}
