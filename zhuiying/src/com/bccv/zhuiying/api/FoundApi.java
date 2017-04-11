package com.bccv.zhuiying.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiying.model.Found;
import com.bccv.zhuiying.model.Movie;
import com.bccv.zhuiying.model.MovieType;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

import android.util.Log;

public class FoundApi extends AppApi {

	
	
	

	public List<Found> getTypeList() {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		String result = util.sendGet(Url.TypeList_url, params);
		if (result != null) {
			Log.e("TypeList_url", result);
		} else {
			Log.e("TypeList_url", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Found> list = null;
						list = JSON.parseArray(rtnStr, Found.class);
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	
	public MovieType getFindTypeList(String type_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type_id", type_id);
		String result = util.sendGet(Url.VideoFindList, params);
		if (result != null) {
			Log.e("getFindTypeList", result);
		} else {
			Log.e("getFindTypeList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						MovieType list = null;
						list = JSON.parseObject(rtnStr, MovieType.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public List<Movie> getFindList(String type_id, String area, String genres_id, String year, String page, String count){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type_id", type_id);
		params.put("countries", area);
		params.put("year", year);
		params.put("genres_id", genres_id);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.VideoFind, params);
		if (result != null) {
			Log.e("getFindList", result);
		} else {
			Log.e("getFindList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Movie> list = null;
						list = JSON.parseArray(rtnStr, Movie.class);

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
