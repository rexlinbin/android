package com.bccv.meitu.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetSpecialpicResBean extends NetResBean {

	private int pic_num;
	
	private List<PicInfo> list;
	private List<Comment> comment;
	
	public List<PicInfo> getList() {
		return list;
	}

	public void setList(List<PicInfo> list) {
		this.list = list;
	}

	public int getPic_num() {
		return pic_num;
	}

	public void setPic_num(int pic_num) {
		this.pic_num = pic_num;
	}
	public List<Comment> getComment() {
		return comment;
	}
	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}
//	public class PicInfo{
//		private String pic;
//		private int is_free;
//		private List<Comment> comment;
//		
//		public String getPic() {
//			return pic;
//		}
//		public void setPic(String pic) {
//			this.pic = pic;
//		}
//		public int getIs_free() {
//			return is_free;
//		}
//		public void setIs_free(int is_free) {
//			this.is_free = is_free;
//		}
//		public List<Comment> getComment() {
//			return comment;
//		}
//		public void setComment(List<Comment> comment) {
//			this.comment = comment;
//		}
//		
//		@Override
//		public String toString() {
//			return "PicInfos [pic=" + pic + ", is_free=" + is_free
//					+ ", comment=" + comment + "]";
//		}
//		
//	}
//	
//	public class Comment{
//		int id;
//		String comments_content;
//		int support;
//		int ctime;
//		String user_name;
//		String user_icon;
//		public int getId() {
//			return id;
//		}
//		public void setId(int id) {
//			this.id = id;
//		}
//		public String getComments_content() {
//			return comments_content;
//		}
//		public void setComments_content(String comments_content) {
//			this.comments_content = comments_content;
//		}
//		public int getSupport() {
//			return support;
//		}
//		public void setSupport(int support) {
//			this.support = support;
//		}
//		public int getCtime() {
//			return ctime;
//		}
//		public void setCtime(int ctime) {
//			this.ctime = ctime;
//		}
//		public String getUser_name() {
//			return user_name;
//		}
//		public void setUser_name(String user_name) {
//			this.user_name = user_name;
//		}
//		public String getUser_icon() {
//			return user_icon;
//		}
//		public void setUser_icon(String user_icon) {
//			this.user_icon = user_icon;
//		}
//		@Override
//		public String toString() {
//			return "Comments [id=" + id + ", comments_content="
//					+ comments_content + ", support=" + support + ", ctime="
//					+ ctime + ", user_name=" + user_name + ", user_icon="
//					+ user_icon + "]";
//		}
//		
//	}

	@Override
	public String toString() {
		return "GetSpecialpicResBean [pic_num=" + pic_num + ", list=" + list
				+ ", success=" + success + ", msg=" + msg + "]";
	}

	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				pic_num = JsonObjectUitl.getInt(jsonObject, "pic_num");
				String commentStr = JsonObjectUitl.getString(jsonObject,"comment") ;
				if(!TextUtils.isEmpty(commentStr)){
					comment = JSON.parseArray(commentStr, Comment.class);
				}
				JSONArray jsonArray = JsonObjectUitl.getJSONArray(jsonObject, "list");
				list = new ArrayList<PicInfo>();
				if(jsonArray!=null){
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = (JSONObject) jsonArray.get(i);
						PicInfo picInfo = new PicInfo();
						picInfo.setPic(JsonObjectUitl.getString(object, "pic"));
						picInfo.setIs_free(JsonObjectUitl.getInt(object,"is_free"));
						list.add(picInfo);
					}
				}
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
	}
}
