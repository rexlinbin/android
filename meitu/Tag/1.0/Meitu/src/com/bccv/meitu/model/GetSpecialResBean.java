package com.bccv.meitu.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetSpecialResBean extends NetResBean {

	private int isfavorite;
	private int iszan;
	private Special special;//专辑详情
	private List<Special> list;//相关专辑
	private List<Comment> comment;//最新评论
	private List<Comment> comment_hot;//最热评论
	
	public int getIsfavorite() {
		return isfavorite;
	}


	public void setIsfavorite(int isfavorite) {
		this.isfavorite = isfavorite;
	}


	public int getIszan() {
		return iszan;
	}


	public void setIszan(int iszan) {
		this.iszan = iszan;
	}


	public List<Special> getList() {
		return list;
	}


	public Special getSpecial() {
		return special;
	}


	public void setSpecial(Special special) {
		this.special = special;
	}


	public void setList(List<Special> list) {
		this.list = list;
	}


	public List<Comment> getComment() {
		return comment;
	}


	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}


	public List<Comment> getComment_hot() {
		return comment_hot;
	}


	public void setComment_hot(List<Comment> comment_hot) {
		this.comment_hot = comment_hot;
	}


//	public class Special{
//		
//		private int special_id;
//		private String special_name;
//		private String special_pic;
//		private int care_num;
//		private int zan_num;
//		private int comment_num;
//		private int special_pic_h;
//		private int special_pic_w;
//		private int author_id;
//		private String author_name;
//		private String author_icon;
//		private List<Tag> tag;
//		public int getSpecial_id() {
//			return special_id;
//		}
//		public void setSpecial_id(int special_id) {
//			this.special_id = special_id;
//		}
//		public String getSpecial_name() {
//			return special_name;
//		}
//		public void setSpecial_name(String special_name) {
//			this.special_name = special_name;
//		}
//		public String getSpecial_pic() {
//			return special_pic;
//		}
//		public void setSpecial_pic(String special_pic) {
//			this.special_pic = special_pic;
//		}
//		public int getCare_num() {
//			return care_num;
//		}
//		public void setCare_num(int care_num) {
//			this.care_num = care_num;
//		}
//		public int getZan_num() {
//			return zan_num;
//		}
//		public void setZan_num(int zan_num) {
//			this.zan_num = zan_num;
//		}
//		public int getComment_num() {
//			return comment_num;
//		}
//		public void setComment_num(int comment_num) {
//			this.comment_num = comment_num;
//		}
//		public int getSpecial_pic_h() {
//			return special_pic_h;
//		}
//		public void setSpecial_pic_h(int special_pic_h) {
//			this.special_pic_h = special_pic_h;
//		}
//		public int getSpecial_pic_w() {
//			return special_pic_w;
//		}
//		public void setSpecial_pic_w(int special_pic_w) {
//			this.special_pic_w = special_pic_w;
//		}
//		public int getAuthor_id() {
//			return author_id;
//		}
//		public void setAuthor_id(int author_id) {
//			this.author_id = author_id;
//		}
//		public String getAuthor_name() {
//			return author_name;
//		}
//		public void setAuthor_name(String author_name) {
//			this.author_name = author_name;
//		}
//		public String getAuthor_icon() {
//			return author_icon;
//		}
//		public void setAuthor_icon(String author_icon) {
//			this.author_icon = author_icon;
//		}
//		public List<Tag> getTag() {
//			return tag;
//		}
//		public void setTag(List<Tag> tag) {
//			this.tag = tag;
//		}
//	}
//	
//	public class Tag{
//		private int tag_id;
//		private String tag_name;
//		public int getTag_id() {
//			return tag_id;
//		}
//		public void setTag_id(int tag_id) {
//			this.tag_id = tag_id;
//		}
//		public String getTag_name() {
//			return tag_name;
//		}
//		public void setTag_name(String tag_name) {
//			this.tag_name = tag_name;
//		}
//		
//	}
	
	
//	public class Comment{//评论
//		private int id;
//		private String comments_content;
//		private int support;
//		private int ctime;
//		private String user_name;
//		private String user_icon;
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
//		
//	}
	
	@Override
	public String toString() {
		return "GetSpecialResBean [isfavorite=" + isfavorite + ", iszan="
				+ iszan + ", special=" + special + ", list=" + list
				+ ", comment=" + comment + ", comment_hot=" + comment_hot
				+ ", success=" + success + ", error=" + error + "]";
	}
	
	
	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				
				
				isfavorite = JsonObjectUitl.getInt(jsonObject, "isfavorite");
				iszan = JsonObjectUitl.getInt(jsonObject, "iszan");
				
				String listStr = JsonObjectUitl.getString(jsonObject, "list");
				list = JSON.parseArray(listStr, Special.class);
				
				String commentStr = JsonObjectUitl.getString(jsonObject, "comment");
				if(!TextUtils.isEmpty(commentStr)){
					comment = JSON.parseArray(commentStr, Comment.class);
				}
					
				String commentHotStr = JsonObjectUitl.getString(jsonObject, "comment_hot");
				if(!TextUtils.isEmpty(commentHotStr)){
					comment_hot = JSON.parseArray(commentHotStr, Comment.class);
				}
				
				JSONObject specialObject = jsonObject.getJSONObject("special");
				special  = new Special();
				
				special.setSpecial_id(JsonObjectUitl.getInt(specialObject, "special_id"));
				special.setSpecial_name(JsonObjectUitl.getString(specialObject, "special_name"));
				special.setSpecial_pic(JsonObjectUitl.getString(specialObject, "special_pic"));
				special.setCare_num(JsonObjectUitl.getInt(specialObject,"care_num"));
				special.setZan_num(JsonObjectUitl.getInt(specialObject,"zan_num"));
				special.setComment_num(JsonObjectUitl.getInt(specialObject,"comment_num"));
				special.setSpecial_pic_h(JsonObjectUitl.getInt(specialObject,"special_pic_h"));
				special.setSpecial_pic_w(JsonObjectUitl.getInt(specialObject,"special_pic_w"));
				special.setAuthor_id(JsonObjectUitl.getInt(specialObject,"author_id"));
				special.setAuthor_name(JsonObjectUitl.getString(specialObject,"author_name"));
				special.setAuthor_icon(JsonObjectUitl.getString(specialObject,"author_icon"));
				special.setCtime(JsonObjectUitl.getString(specialObject,"ctime"));
				special.setSpecial_names(JsonObjectUitl.getString(specialObject, "special_names"));
				
				
				String tagStr = JsonObjectUitl.getString(specialObject,"tag");
				if(!TextUtils.isEmpty(tagStr)){
					special.setTag(JSON.parseArray(tagStr, Tag.class));
				}
				
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
	}



}
