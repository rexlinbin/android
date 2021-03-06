package com.bccv.zhuiying.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiying.model.HotSearch;
import com.bccv.zhuiying.model.Movie;
import com.bccv.zhuiying.model.Search;
import com.bccv.zhuiying.model.SearchInfo;
import com.bccv.zhuiying.model.SearchType;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

import android.util.Log;

public class SearchApi extends AppApi {
	public List<HotSearch> getHotSearchList(
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result = util.sendGet(Url.Search_list, params);
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
	public Search getSearchList(String keyword, String page,
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		try {
			keyword = URLEncoder.encode(keyword, "utf-8").replaceAll("\\+",
					 "%20");
			keyword = keyword.replaceAll("%3A", ":").replaceAll("%2F", "/");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		params.put("keyword", keyword);
		params.put("page", page);
		params.put("count", count);
		
		String result = util.sendGet(Url.Search, params);
		if (result != null) {
			Log.e("getSearchList", result);
		} else {
			Log.e("getSearchList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					int total = jsonObject.getInt("total");
					if (total > 0) {
						String rtnStr = jsonObject.getString("data");
						
						if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
							
							Search search = null;
							search = JSON.parseObject(rtnStr, Search.class);
							
							return search;
						}
					}
						

					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public SearchType getSearchMoreList(String keyword, String page,
			String type_id) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		try {
			keyword = URLEncoder.encode(keyword, "utf-8").replaceAll("\\+",
					 "%20");
			keyword = keyword.replaceAll("%3A", ":").replaceAll("%2F", "/");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		params.put("keyword", keyword);
		params.put("page", page);
		params.put("type_id", type_id);
		
		String result = util.sendGet(Url.SearchMore_list, params);
		if (result != null) {
			Log.e("getSearchMoreList", result);
		} else {
			Log.e("getSearchMoreList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					
						String rtnStr = jsonObject.getString("data");
						
						if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
							
							SearchType search = null;
							search = JSON.parseObject(rtnStr, SearchType.class);
							
							return search;
						}
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
