package com.bccv.meitu.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetAttendedList extends NetResBean {
	
	
	private int page;
	private int page_total;
	private List<AuthorInfo> list;
	

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

	public List<AuthorInfo> getList() {
		return list;
	}

	public void setList(List<AuthorInfo> list) {
		this.list = list;
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
					list = JSON.parseArray(listStr, AuthorInfo.class);
				}
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
	}

}
