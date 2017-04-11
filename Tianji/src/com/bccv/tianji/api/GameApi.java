package com.bccv.tianji.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bccv.tianji.model.Game;
import com.bccv.tianji.model.Gameinfo;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

public class GameApi extends AppApi {
	public List<Game> getMyDownloadList(String user_id, String page,
			String count, String status) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "get_userdownload");
		params.put("user_id", user_id);
		params.put("page", page);
		params.put("count", count);
		params.put("status", status);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getMyDownloadList", result);
		} else {
			Log.e("getMyDownloadList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Game> list = null;
						list = JSON.parseArray(rtnStr, Game.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public List<Game> getClassifyListList(String type_id, String page,
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "game_typelist");
		params.put("type_id", type_id);
		params.put("page", page);
		params.put("count", count);
		if (GlobalParams.user != null) {
			if (!StringUtils.isEmpty(GlobalParams.user.getUser_id())) {
				params.put("user_id", GlobalParams.user.getUser_id());
			}
		}
		
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getClassifyListList", result);
		} else {
			Log.e("getClassifyListList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Game> list = null;
						list = JSON.parseArray(rtnStr, Game.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/***
	 * 主界面数据
	 * 
	 * 
	 */

	public List<Game> getGameListList(String user_id, String page,
			String count, String request) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", request);
		params.put("user_id", user_id);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getGameListList", result);
		} else {
			Log.e("getGameListList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Game> list = null;
						list = JSON.parseArray(rtnStr, Game.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 
	 * 详情介绍
	 * 
	 * 
	 * **/

	public Gameinfo getGameInfoList(String user_id, String game_id) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "game_info");
		params.put("user_id", user_id);
		params.put("game_id", game_id);

		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getGameinfoList", result);
		} else {
			Log.e("getGameinfoList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
				
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
					
						Gameinfo list = null;
						list = JSON.parseObject(rtnStr, Gameinfo.class);
int a=0;
a=1;
						return list;
					}
				

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
