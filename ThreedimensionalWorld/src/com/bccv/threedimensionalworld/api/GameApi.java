package com.bccv.threedimensionalworld.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bccv.threedimensionalworld.Net.HttpClientUtil;
import com.bccv.threedimensionalworld.model.App;
import com.bccv.threedimensionalworld.tool.StringUtils;

public class GameApi extends AppApi {
	public List<App> getAppList(String type_id, boolean isGame, int page, int count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type_id", type_id);
		params.put("page", page + "");
		params.put("count", count + "");
		String result;
		if (isGame) {
			result = util.sendGet(Url.gameList_url, params);
		}else {
			result = util.sendGet(Url.appList_url, params);
		}
		
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<App> list = null;
						list = JSON.parseArray(rtnStr, App.class);
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public App getAppInfo(String id, boolean isGame) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result;
		if (isGame) {
			params.put("game_id", id);
			result = util.sendGet(Url.gameInfo_url, params);
		}else {
			params.put("app_id", id);
			result = util.sendGet(Url.appInfo_url, params);
		}
		
		if (result != null) {
			Log.e("getAppInfo", result);
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						App app = JSON.parseObject(rtnStr, App.class);
						return app;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
