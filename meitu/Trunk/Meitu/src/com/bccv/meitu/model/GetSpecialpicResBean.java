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

	
	private int is_free;	// 0 免费  
	private int free_num;	// 免费照片的张数
	private int pic_num;	// 照片总张数
	
	private List<PicInfo> list;
	private List<Comment> comment;
	
	
	public int getIs_free() {
		return is_free;
	}

	public void setIs_free(int is_free) {
		this.is_free = is_free;
	}

	public int getFree_num() {
		return free_num;
	}

	public void setFree_num(int free_num) {
		this.free_num = free_num;
	}

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


	@Override
	public String toString() {
		return "GetSpecialpicResBean [pic_num=" + pic_num + ", is_free="
				+ is_free + ", free_num=" + free_num + ", list=" + list
				+ ", comment=" + comment + ", success=" + success + "]";
	}

	@Override
	public void parseData(String data) {
		if(success){
			try {
				JSONObject jsonObject = new JSONObject(data);
				is_free = JsonObjectUitl.getInt(jsonObject, "is_free");
				free_num = JsonObjectUitl.getInt(jsonObject, "free_num");
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
