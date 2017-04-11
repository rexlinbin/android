package com.bccv.boxcomic.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bccv.boxcomic.ebook.BookInfo;
import com.bccv.boxcomic.ebook.ChapterInfo;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class EbookApi extends AppApi {
	public String getText(String id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		String result = util.sendGet(Url.EbookText, params);
		Logger.e("getChannelList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						List<String> list = null;
						list = JSON.parseArray(rtnStr, String.class);
						if (list != null && list.size() > 0) {
							String string = list.get(0);
							JSONObject jsonObject2 = new JSONObject(string);
							String newString = jsonObject2.getString("text");
							return newString;
						}
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public ArrayList<ChapterInfo> getChapterInfos(String id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		String result = util.sendGet(Url.EbookChapters, params);
		Logger.e("getChannelList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					
					JSONObject dataJsonObject = jsonObject.getJSONObject("data");
					String rtnStr = dataJsonObject.getString("chapter_info");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						ArrayList<ChapterInfo> list = null;
						list = (ArrayList<ChapterInfo>) JSON.parseArray(rtnStr, ChapterInfo.class);
						
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public List<BookInfo> getUpdate(String key, String secret, int p, int num) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		params.put("key", key);
		params.put("secret", secret);
		params.put("p", p + "");
		params.put("num", num + "");

		String result = util.sendGet(Url.EbookContent, params);
//Log.e("BookInfo", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<BookInfo> list = null;
						list = JSON.parseArray(rtnStr, BookInfo.class);
						return list;

					} else {
						List<BookInfo> list = new ArrayList<BookInfo>();
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
