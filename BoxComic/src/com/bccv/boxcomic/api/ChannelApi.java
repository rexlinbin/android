package com.bccv.boxcomic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.boxcomic.modal.Channel;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class ChannelApi extends AppApi {
	public List<Channel> getChannelList(){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result = util.sendGet(Url.ChannelList_url, params);
		Logger.e("getChannelList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						List<Channel> list = null;
						list = JSON.parseArray(rtnStr, Channel.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<Channel> getEbookChannelList(){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result = util.sendGet(Url.BookChannelList_url, params);
		Logger.e("getChannelList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						List<Channel> list = null;
						list = JSON.parseArray(rtnStr, Channel.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public List<Comic> getCatList(String key, String secret, int p,int num, String keyword) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
Logger.e("getCatList", keyword);
		params.put("key", key);
		params.put("secret", secret);
		params.put("p", p + "");
		params.put("num", num + "");
		params.put("id", keyword);
		String result = util.sendGet(Url.CatList_url, params);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
