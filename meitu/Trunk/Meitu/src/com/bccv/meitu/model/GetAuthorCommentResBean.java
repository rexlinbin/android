package com.bccv.meitu.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetAuthorCommentResBean extends NetResBean {

	
	private int page;
	private int page_total;
	private List<Comment> comment;
	
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

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}
	

	@Override
	public String toString() {
		return "GetAuthorCommentResBean [page=" + page + ", page_total="
				+ page_total + ", comment=" + comment + ", success=" + success
				+ ", error=" + error + "]";
	}


	@Override
	public void parseData(String data) {
		
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				page = JsonObjectUitl.getInt(jsonObject, "page");
				page_total = JsonObjectUitl.getInt(jsonObject, "page_total");
			
				String listStr = JsonObjectUitl.getString(jsonObject, "comment");
//				System.out.println("2222222222222222222"+listStr.toString());
				if(!TextUtils.isEmpty(listStr)){
					comment = JSON.parseArray(listStr, Comment.class);
				}
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}

	}

}
