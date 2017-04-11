package com.bccv.meitu.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.JsonObjectUitl;
import com.bccv.meitu.utils.Logger;

public class GetTaglistResBean extends NetResBean {

	private List<Tag> taglist ;
	
	public List<Tag> getTaglist() {
		return taglist;
	}

	public void setTaglist(List<Tag> taglist) {
		this.taglist = taglist;
	}

	
	@Override
	public String toString() {
		return "GettaglistResBean [taglist=" + taglist + ", success=" + success
				+ ", error=" + error + "]";
	}
	
	@Override
	public void parseData(String data) {
		// TODO Auto-generated method stub
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				String taglistStr = JsonObjectUitl.getString(jsonObject, "taglist");
				if(!TextUtils.isEmpty(taglistStr)){
					taglist = JSON.parseArray(taglistStr, Tag.class);
				}
				
			} catch (JSONException e) {
				success = false;
				Logger.e(TAG, "parseData", e.getMessage());
			}
		}
	}
}
