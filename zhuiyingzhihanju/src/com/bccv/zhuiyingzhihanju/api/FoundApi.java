package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.Found;
import com.bccv.zhuiyingzhihanju.model.FoundModel;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieNews;
import com.bccv.zhuiyingzhihanju.model.MovieType;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

import android.util.Log;

public class FoundApi extends AppApi {

	
	
	

	public FoundModel getTypeList() {
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
						FoundModel list = null;
					list = JSON.parseObject(rtnStr, FoundModel.class);
						
						JSONObject data = new JSONObject(rtnStr);
						MovieNews movie=JSON.parseObject(data.getString("movie"), MovieNews.class);
						List<Movie>slide=JSON.parseArray(data.getString("slide"), Movie.class);
						
						List<Movie>news=JSON.parseArray(data.getString("news"), Movie.class);
						
						List<Movie>tv=JSON.parseArray(data.getString("tv"), Movie.class);
						
						MovieNews variety=JSON.parseObject(data.getString("variety"), MovieNews.class);
						List<Movie>music=JSON.parseArray(data.getString("music"), Movie.class);
						
						list.setSlide(slide);
						list.setNews(news);
						list.setTv(tv);
						list.setVariety(variety);
						list.setMovie(movie);
						list.setMusic(music);
						
						
						
						
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
		params.put("area", area);
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
