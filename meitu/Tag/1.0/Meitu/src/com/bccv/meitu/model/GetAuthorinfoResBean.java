package com.bccv.meitu.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetAuthorinfoResBean extends NetResBean {

	private int page;
	private int page_total;
	private String author_name;
	private String author_icon;
	private String backdrop;
	private int specialnum;
	private int is_care;
	private int comment_num;
	private List<Special> authorspecial;
	
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

	public String getAuthor_name() {
		return author_name;
	}


	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}


	public String getAuthor_icon() {
		return author_icon;
	}


	public void setAuthor_icon(String author_icon) {
		this.author_icon = author_icon;
	}

	public String getBackdrop() {
		return backdrop;
	}


	public void setBackdrop(String backdrop) {
		this.backdrop = backdrop;
	}


	public int getSpecialnum() {
		return specialnum;
	}


	public void setSpecialnum(int specialnum) {
		this.specialnum = specialnum;
	}


	public int getComment_num() {
		return comment_num;
	}


	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}


	public List<Special> getAuthorspecial() {
		return authorspecial;
	}


	public void setAuthorspecial(List<Special> authorspecial) {
		this.authorspecial = authorspecial;
	}

	
	public int getIs_care() {
		return is_care;
	}


	public void setIs_care(int is_care) {
		this.is_care = is_care;
	}

	@Override
	public String toString() {
		return "GetAuthorinfoResBean [page=" + page + ", page_total="
				+ page_total + ", author_name=" + author_name
				+ ", author_icon=" + author_icon + ", backdrop=" + backdrop
				+ ", specialnum=" + specialnum + ", is_care=" + is_care
				+ ", comment_num=" + comment_num + ", authorspecial="
				+ authorspecial + ", success=" + success + ", error=" + error
				+ "]";
	}

	@Override
	public void parseData(String data) {
		
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				page = JsonObjectUitl.getInt(jsonObject, "page");
				page_total = JsonObjectUitl.getInt(jsonObject, "page_total");
				author_name = JsonObjectUitl.getString(jsonObject, "author_name");
				author_icon = JsonObjectUitl.getString(jsonObject, "author_icon");
				backdrop = JsonObjectUitl.getString(jsonObject, "backdrop");
				specialnum = JsonObjectUitl.getInt(jsonObject, "specialnum");
				comment_num = JsonObjectUitl.getInt(jsonObject, "comment_num");
				is_care = JsonObjectUitl.getInt(jsonObject, "is_care");
				
				String listStr = JsonObjectUitl.getString(jsonObject, "authorspecial");
				if(!TextUtils.isEmpty(listStr)){
					authorspecial = JSON.parseArray(listStr, Special.class);
				}
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
		
	}
	
}
