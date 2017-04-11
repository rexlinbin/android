package com.bccv.boxcomic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.modal.MainItem;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class HomeHotApi extends AppApi {
	public List<Comic> getHome(String key, String secret, int p, int num) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		params.put("key", key);
		params.put("secret", secret);
		params.put("p", p + "");
		params.put("num", num + "");

		String result = util.sendGet(Url.listHot_url, params);
//		Log.e("HomeHotApi", result);
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
	public List<Comic> getUpdate(String key, String secret, int p, int num) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		params.put("key", key);
		params.put("secret", secret);
		params.put("p", p + "");
		params.put("num", num + "");

		String result = util.sendGet(Url.listNew_url, params);

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
}
