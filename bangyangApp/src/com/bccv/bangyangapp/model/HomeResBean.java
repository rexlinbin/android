package com.bccv.bangyangapp.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.utils.L;

public class HomeResBean extends NetResBean {

	private ArrayList<AppInfoItemBean> HomeItemList;
	
//	public class AppInfoItemBean {
////        id--------------APP ID
////        title-----------APP名称
////        ftitle----------副标题
////        introduce-------介绍
////        icon------------图标
////        pic-------------推荐图
////        choice----------是否推荐 1为推荐
////        times-----------发布时间
////        creator_id------创建者ID
////        creator_name----创建者名称
////        creator_icon----创建者头像
////        comment---------创建者评论
//		
//		private String id;
//		private String title;
//		private String ftitle;
//		private String introduce;
//		private String icon;
//		private String pic;
//		private int choice;
//		private long times;
//		private String creator_id;
//		private String creator_name;
//		private String creator_icon;
//		private String comment;
//		public String getId() {
//			return id;
//		}
//		public void setId(String id) {
//			this.id = id;
//		}
//		public String getTitle() {
//			return title;
//		}
//		public void setTitle(String title) {
//			this.title = title;
//		}
//		public String getFtitle() {
//			return ftitle;
//		}
//		public void setFtitle(String ftitle) {
//			this.ftitle = ftitle;
//		}
//		public String getIntroduce() {
//			return introduce;
//		}
//		public void setIntroduce(String introduce) {
//			this.introduce = introduce;
//		}
//		public String getIcon() {
//			return icon;
//		}
//		public void setIcon(String icon) {
//			this.icon = icon;
//		}
//		public String getPic() {
//			return pic;
//		}
//		public void setPic(String pic) {
//			this.pic = pic;
//		}
//		public int getChoice() {
//			return choice;
//		}
//		public void setChoice(int choice) {
//			this.choice = choice;
//		}
//		public long getTimes() {
//			return times;
//		}
//		public void setTimes(long times) {
//			this.times = times;
//		}
//		public String getCreator_id() {
//			return creator_id;
//		}
//		public void setCreator_id(String creator_id) {
//			this.creator_id = creator_id;
//		}
//		public String getCreator_name() {
//			return creator_name;
//		}
//		public void setCreator_name(String creator_name) {
//			this.creator_name = creator_name;
//		}
//		public String getCreator_icon() {
//			return creator_icon;
//		}
//		public void setCreator_icon(String creator_icon) {
//			this.creator_icon = creator_icon;
//		}
//		public String getComment() {
//			return comment;
//		}
//		public void setComment(String comment) {
//			this.comment = comment;
//		}
//		@Override
//		public String toString() {
//			return "AppInfoItemBean [id=" + id + ", title=" + title + ", ftitle="
//					+ ftitle + ", introduce=" + introduce + ", icon=" + icon
//					+ ", pic=" + pic + ", choice=" + choice + ", times="
//					+ times + ", creator_id=" + creator_id + ", creator_name="
//					+ creator_name + ", creator_icon=" + creator_icon
//					+ ", comment=" + comment + "]";
//		}
//
//	}
	
	public ArrayList<AppInfoItemBean> getHomeItemList() {
		return HomeItemList;
	}

	public void setHomeItemList(ArrayList<AppInfoItemBean> homeItemList) {
		HomeItemList = homeItemList;
	}

	@Override
	public String toString() {
		return "HomeResBean [HomeItemList=" + HomeItemList + ", success="
				+ success + ", status_code=" + status_code + "]";
	}

	@Override
	public void parseData(String jsonData) {
		L.v(TAG, "parseData", "jsonData: " + jsonData);
		if (success) {
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				HomeItemList = new ArrayList<AppInfoItemBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject dataObject = jsonArray.getJSONObject(i);
					AppInfoItemBean homeItemBean = new AppInfoItemBean();
					homeItemBean.setId(dataObject.optString("id"));
					homeItemBean.setTitle(dataObject.optString("title"));
					homeItemBean.setFtitle(dataObject.optString("ftitle"));
					homeItemBean.setIntroduce(dataObject.optString("introduce"));
					homeItemBean.setIcon(dataObject.optString("icon"));
					homeItemBean.setPic(dataObject.optString("pic"));
					homeItemBean.setChoice(dataObject.optInt("choice"));
					homeItemBean.setTimes(dataObject.optLong("times")*1000l);
					homeItemBean.setCreator_id(dataObject.optString("creator_id"));
					homeItemBean.setCreator_name(dataObject.optString("creator_name"));
					homeItemBean.setCreator_icon(dataObject.optString("creator_icon"));
					homeItemBean.setComment(dataObject.optString("comment"));
					HomeItemList.add(homeItemBean);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				L.e(TAG, "parseData", e.getMessage());
			}
		}
	}

}
