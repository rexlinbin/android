package com.bccv.meitu.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetTaginfoResBean extends NetResBean {


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

	
	@Override
	public String toString() {
		return "GetlistResBean [page=" + page + ", page_total=" + page_total
				+ ", list=" + list + ", success=" + success + ", error=" + error
				+ "]";
	}
	
	
	@Override
	public void parseData(String data) {
		// TODO Auto-generated method stub
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
