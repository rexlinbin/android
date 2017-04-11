package com.boxuu.gamebox.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.boxuu.gamebox.network.NetResBean;
import com.boxuu.gamebox.utils.JsonObjectUitl;

public class GameList extends NetResBean{

	private List<GameInfo> list ;
	private int total;
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	
	public List<GameInfo> getList() {
		return list;
	}

	public void setList(List<GameInfo> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		if (list != null && list.size() > 0) {
			return "GameList [list.size : "+list.size()+"]"+"[list=" + list + "]";
		}else {
			return "list is null";
		}
	}

	@Override
	public void parseData(String data) {
//		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				String str = JsonObjectUitl.getString(jsonObject, "data");
				total = JsonObjectUitl.getInt(jsonObject, "total");
				status = JsonObjectUitl.getInt(jsonObject, "status");
				if (str != null && !str.equals("")) {
					list = JSON.parseArray(str, GameInfo.class);
				}
			} catch (JSONException e) {
				success = false;
				Log.e(TAG, "parseData"+ e.getMessage());
			}
//		}
	}
}
