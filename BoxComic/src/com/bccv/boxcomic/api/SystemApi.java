package com.bccv.boxcomic.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.boxcomic.modal.InfoAnalysis;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class SystemApi extends AppApi {
	public void getSystemInfo(){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		String result = httpClientUtil.sendGet(Url.SystemInfo, params);
		Logger.e("getSystemInfo", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						JSONObject dataJsonObject = new JSONObject(rtnStr);
						GlobalParams.imageHeadUrlString = StringUtils.isEmpty(dataJsonObject.getString("api_comicpic_url"))?"http://dmappimg.boxuu.com/mhimg/":dataJsonObject.getString("api_comicpic_url");
						GlobalParams.imageUrlString = StringUtils.isEmpty(dataJsonObject.getString("api_titlepic_url"))?"http://img-comic.boxuu.com/":dataJsonObject.getString("api_titlepic_url");
						GlobalParams.BookImagepic = StringUtils.isEmpty(dataJsonObject.getString("api_booktitlepic_url"))?"http://img-comic.boxuu.com/xs/":dataJsonObject.getString("api_booktitlepic_url");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendLosePage(String bookid, String chapterid, String page){
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("comic_id", bookid);
		params.put("chapter_id", chapterid);
		params.put("page", page);
		String result = httpClientUtil.sendGet(Url.SendLosePage, params);
		Logger.e("sendLosePage", result);
		
	}
}
