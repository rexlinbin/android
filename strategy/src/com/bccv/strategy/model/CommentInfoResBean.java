package com.bccv.strategy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.utils.L;

public class CommentInfoResBean extends NetResBean {
	
//	private AppInfo app_info;
	private AppInfo raid_info;
	private CommentInfo comment_info;
	private int now_situation;
	
	public AppInfo getRaid_info() {
		return raid_info;
	}


	public void setRaid_info(AppInfo raid_info) {
		this.raid_info = raid_info;
	}


	public CommentInfo getComment_info() {
		return comment_info;
	}


	public void setComment_info(CommentInfo comment_info) {
		this.comment_info = comment_info;
	}


	public int getNow_situation() {
		return now_situation;
	}


	public void setNow_situation(int now_situation) {
		this.now_situation = now_situation;
	}


	public class CommentInfo{
		public boolean isBasic; // true 一级评论  false 二级评论
		public String id;
		public String user_id;
		public String comment;
		public String user_icon;
		public String user_name;
		public String raid_id;
//		public String app_id;
		public boolean is_digg;
		public int digg;
		public long times;
		public ArrayList<CommentInfo> reply;
		public ArrayList<Digg> digg_list;
		@Override
		public String toString() {
			return "CommentInfo [isBasic=" + isBasic + ", id=" + id
					+ ", user_id=" + user_id + ", comment=" + comment
					+ ", user_icon=" + user_icon + ", user_name=" + user_name
					+ ", raid_id=" + raid_id + ", digg=" + digg + ", times="
					+ times + ", reply=" + reply + ", digg_list=" + digg_list
					+ "]";
		}
		
	}
	
	public class Digg{
		public String user_id;
		public String user_icon;
		@Override
		public String toString() {
			return "Digg [user_id=" + user_id + ", user_icon=" + user_icon
					+ "]";
		}
		
	}

	public class AppInfo{
		public String id;
		public String news_title;
		public String news_digg;
		public String news_focues;
		public String news_comment;
		public String news_introduce;
		public String icon;
		public int like_num;
		public int comment_num;
		@Override
		public String toString() {
			return "AppInfo [id=" + id + ", news_title=" + news_title
					+ ", news_digg=" + news_digg + ", news_focues="
					+ news_focues + ", news_comment=" + news_comment
					+ ", news_introduce=" + news_introduce + ", icon=" + icon
					+ ", like_num=" + like_num + ", comment_num=" + comment_num
					+ "]";
		}
	}
	
	@Override
	public String toString() {
		return "CommentInfoResBean [raid_info=" + raid_info + ", comment_info="
				+ comment_info + ", now_situation=" + now_situation
				+ ", success=" + success + ", status_code=" + status_code + "]";
	}


	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONObject data = jsonObject.getJSONObject("data");
				
				now_situation = data.optInt("now_situation");
				
				JSONObject comment_info_data= data.optJSONObject("comment");
				comment_info = new CommentInfo();
				comment_info.isBasic = true;
				comment_info.id = comment_info_data.optString("id");
				comment_info.user_id = comment_info_data.optString("user_id");
				comment_info.comment = comment_info_data.optString("comment");
				comment_info.user_icon = comment_info_data.optString("user_icon");
				comment_info.user_name = comment_info_data.optString("user_name");
				comment_info.raid_id = comment_info_data.optString("raid_id");
				comment_info.digg = comment_info_data.optInt("digg");
				comment_info.times = comment_info_data.optLong("times")*1000l;
				comment_info.is_digg = comment_info_data.optInt("is_digg")==1;

				JSONArray replylist = comment_info_data.optJSONArray("reply");
				comment_info.reply = new ArrayList<CommentInfo>();
				for (int i = 0; replylist != null && i < replylist.length(); i++) {
					JSONObject replyInfo = replylist.getJSONObject(i);
					CommentInfo commentInfo = new CommentInfo();
					commentInfo.isBasic = false;
					commentInfo.id = replyInfo.optString("id");
					commentInfo.user_id = replyInfo.optString("user_id");
					commentInfo.user_icon = replyInfo.optString("user_icon");
					commentInfo.user_name = replyInfo.optString("user_name");
					commentInfo.comment = replyInfo.optString("comment");
					commentInfo.digg = replyInfo.optInt("digg");
					commentInfo.times = replyInfo.optLong("times")*1000l;
					commentInfo.is_digg = comment_info_data.optInt("is_digg")==1;
					comment_info.reply.add(commentInfo);
				}
				
				JSONArray digg_list = data.optJSONArray("digg_list");
				comment_info.digg_list = new ArrayList<Digg>();
				for (int i = 0; digg_list != null && i < digg_list.length(); i++) {
					JSONObject diggInfo = digg_list.getJSONObject(i);
					Digg digg = new Digg();
					digg.user_id = diggInfo.optString("user_id");
					digg.user_icon = diggInfo.optString("user_icon");
					comment_info.digg_list.add(digg);
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
