package com.bccv.tianji.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bccv.tianji.model.Game;
import com.bccv.tianji.model.HotSearch;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

public class SearchApi extends AppApi {
	public List<HotSearch> getHotSearchList(
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "search_list");
		params.put("count", count);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getHotSearchList", result);
		} else {
			Log.e("getHotSearchList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<HotSearch> list = null;
						list = JSON.parseArray(rtnStr, HotSearch.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	public List<Game> getSearchList(String keyword, String page,
			String count, String user_id) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "search");
		params.put("keyword", keyword);
		params.put("page", page);
		params.put("count", count);
		if (!StringUtils.isEmpty(user_id)) {
			params.put("user_id", user_id);
		}
		
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getSearchList", result);
		} else {
			Log.e("getSearchList", "null");
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
	


}
