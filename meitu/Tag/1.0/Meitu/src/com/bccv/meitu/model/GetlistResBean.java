package com.bccv.meitu.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetlistResBean extends NetResBean {

	private int page;
	private int page_total;
	private List<Special> list;
	
	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public int getPage_total() {
		return page_total;
	}


	public void setPage_total(int page_total) {
		this.page_total = page_total;
	}

	
	public List<Special> getList() {
		return list;
	}


	public void setList(List<Special> list) {
		this.list = list;
	}


//	public class Specials{
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
//		
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
//		
//		@Override
//		public String toString() {
//			return "Specials [special_id=" + special_id + ", special_name="
//					+ special_name + ", special_pic=" + special_pic
//					+ ", care_num=" + care_num + ", zan_num=" + zan_num
//					+ ", comment_num=" + comment_num + ", special_pic_h="
//					+ special_pic_h + ", special_pic_w=" + special_pic_w
//					+ ", author_id=" + author_id + ", author_name="
//					+ author_name + ", author_icon=" + author_icon + "]";
//		}
//		
//	}
	
	@Override
	public String toString() {
		return "GetlistResBean [page=" + page + ", page_total=" + page_total
				+ ", list=" + list + ", success=" + success + ", error=" + error
				+ "]";
	}


	@Override
	public void parseData(String data) {
		
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				page = JsonObjectUitl.getInt(jsonObject, "page");
				page_total = JsonObjectUitl.getInt(jsonObject, "page_total");
				String listStr = JsonObjectUitl.getString(jsonObject, "list");
				if(!TextUtils.isEmpty(listStr)){
					list = JSON.parseArray(listStr, Special.class);
				}
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
		
	}
	
}
