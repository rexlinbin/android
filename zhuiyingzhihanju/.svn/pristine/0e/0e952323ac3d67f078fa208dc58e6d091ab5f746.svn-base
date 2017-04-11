package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.Collect;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

import android.util.Log;

public class CollectApi extends AppApi {
	public List<Collect> getCollectList(String user_id, String page, String count, String token){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		params.put("page", page);
		params.put("perpage", count);
		params.put("token", token);
		String result = util.sendGet(UserUrl.myfav, params);
		if (result != null) {
			Log.e("getCollectList", result);
	
		} else {
			Log.e("getCollectList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getJSONObject("data").getString("items");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Collect> list = null;
						list = JSON.parseArray(rtnStr, Collect.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public void deleteCollect(String user_id, String video_id, String type_id, String token){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		params.put("video_id", video_id);
		params.put("video_type", type_id);
		params.put("token", token);
		params.put("action", "del");
		String result = util.sendGet(UserUrl.favorite, params);
		if (result != null) {
			Log.e("deleteCollect", result);
		} else {
			Log.e("deleteCollect", "null");
		}
		
	}
	
	public void collect(String user_id, String video_id, String type_id, String token){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		params.put("video_id", video_id);
		params.put("video_type", type_id);
		params.put("token", token);
		params.put("action", "add");
		String result = util.sendGet(UserUrl.favorite, params);
		if (result != null) {
			Log.e("collect", result);
		} else {
			Log.e("collect", "null");
		}
	}
	
	public int isCollect(String user_id, String video_id, String type_id, String token){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		params.put("video_id", video_id);
		params.put("video_type", type_id);
		params.put("token", token);
		String result = util.sendGet(UserUrl.checkfav, params);
		if (result != null) {
			Log.e("collect", result);
		} else {
			Log.e("collect", "null");
		}
		
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				int res = jsonObject.getInt("status");
				return res;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return 0;
	}
	
}
