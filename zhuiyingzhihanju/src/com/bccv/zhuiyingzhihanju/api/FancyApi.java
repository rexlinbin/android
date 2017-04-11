package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;

import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieModel;
import com.bccv.zhuiyingzhihanju.model.MovieNews;
import com.bccv.zhuiyingzhihanju.model.Star;
import com.bccv.zhuiyingzhihanju.model.StarInfo;
import com.utils.net.HttpClientUtil;
import com.utils.tools.Logger;
import com.utils.tools.StringUtils;

import android.util.Log;

public class FancyApi extends AppApi {

	public MovieModel getMovielist() {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();

		String result = util.sendGet(Url.Today_list, params);
		if (result != null) {
			Log.e("Today_list", result);
		} else {
			Log.e("Today_list", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						MovieModel list = null;
						list = JSON.parseObject(result, MovieModel.class);
						JSONObject data = new JSONObject(rtnStr);
						MovieNews coming=JSON.parseObject(data.getString("coming"), MovieNews.class);
						MovieNews sidelight=JSON.parseObject(data.getString("sidelight"), MovieNews.class);
						MovieNews high=JSON.parseObject(data.getString("high"), MovieNews.class);
						
						List<Movie>slide=JSON.parseArray(data.getString("slide"), Movie.class);
						List<Movie>hot=JSON.parseArray(data.getString("hot"), Movie.class);
						List<Star>star=JSON.parseArray(data.getString("star"), Star.class);
						list.setComing(coming);
						list.setSidelight(sidelight);
						list.setHigh(high);
						list.setHot(hot);
						list.setStar(star);
						list.setSlide(slide);
						
						
						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public MovieNews getMoreNews(String type_id){

		HttpClientUtil util = new HttpClientUtil();

		Map<String, String> params = new HashMap<String, String>();

		params.put("type_id", type_id);
		
		String result = util.sendGet(Url.change, params);
	
		if (!StringUtils.isEmpty(result)) {

			try {

				JSONObject jsonObject = new JSONObject(result);

				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						MovieNews list = null;
						list = JSON.parseObject(rtnStr, MovieNews.class);
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
	
	
	
	
	public List<Movie> getTopList() {

		HttpClientUtil util = new HttpClientUtil();

		Map<String, String> params = new HashMap<String, String>();

		String result = util.sendGet(Url.Top_list, params);
		Logger.e("Top_list", result);
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
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return null;

	}
	public List<Movie> getMoreList() {

		HttpClientUtil util = new HttpClientUtil();

		Map<String, String> params = new HashMap<String, String>();

		String result = util.sendGet(Url.Moreurl, params);
		Logger.e("wat_list", result);
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
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return null;

	}
}
