package com.bccv.boxcomic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.modal.MainItem;
import com.bccv.boxcomic.modal.Search;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class SearchApi extends AppApi {

	public List<Comic> getSearchList(String key, String secret, int p,
			int num, String keyword) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		params.put("key", key);
		params.put("secret", secret);
		params.put("p", p + "");
		params.put("num", num + "");
		params.put("keyword", keyword);
		String result = util.sendGet(Url.Search, params);
		Logger.e("result", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Comic> list = null;
						list = JSON.parseArray(rtnStr, Comic.class);
						return list;

					} else {
						List<Comic> list = new ArrayList<Comic>();
						return list;

					}

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return null;

	}

	public List<Search> getSearch(String key, String secret) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		params.put("key", key);
		params.put("secret", secret);
		String result = util.sendGet(Url.listSearch_list, params);
		Logger.e("result", result);
		if (!StringUtils.isEmpty(result)) {
			try {
			
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
				
					String rtnStr  = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Search> list = null;
						list = JSON.parseArray(rtnStr, Search.class);
						return list;

					} else {
						List<Search> list = new ArrayList<Search>();
						return list;

					}

				}

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return null;

	}

}
