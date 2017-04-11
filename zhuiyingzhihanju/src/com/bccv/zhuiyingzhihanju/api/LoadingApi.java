package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.Ad;
import com.utils.model.AppInfo;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

import android.util.Log;

public class LoadingApi extends AppApi {
	public AppInfo getAppInfo(String channel, String upver) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("channel", channel);
		params.put("upver", upver);
		params.put("appid", "4");

		String result = util.sendGet("http://log.zhuiying.me/update/?a=a", params);
		if (result != null) {
			Log.e("getAppInfo", result);
		} else {
			Log.e("getAppInfo", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				AppInfo list = null;
				list = JSON.parseObject(result, AppInfo.class);
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public List<Ad> getAdInfo(String channel, String upver) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		String result = util.sendGet(Url.loading, params);
		if (result != null) {
			Log.e("getAdInfo", result);
		} else {
			Log.e("getAdInfo", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
				List<Ad> list = null;
				list = JSON.parseArray(rtnStr, Ad.class);
				return list;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
