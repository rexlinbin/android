package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.FilterInfo;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.Search;
import com.bccv.zhuiyingzhihanju.model.SearchRecom;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

import android.util.Log;

public class FilterApi extends AppApi {
	public FilterInfo getFilterInfo(String type_id) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type_id", type_id);
		
		String result = util.sendGet(Url.FindList, params);
		if (result != null) {
			Log.e("getFilterInfo", result);
		} else {
			Log.e("getFilterInfo", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					
						String rtnStr = jsonObject.getString("data");
						
						if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
							
							FilterInfo search = null;
							search = JSON.parseObject(rtnStr, FilterInfo.class);
							
							return search;
						}
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	public List<Movie> getFilterList(String type_id, String order, String isFinish, String type, String year, String page, String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type_id", type_id);
		params.put("order", order);
		if (!StringUtils.isEmpty(isFinish)) {
			params.put("is_finish", isFinish);
		}
		params.put("genres_id", type);
		params.put("year", year);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Find, params);
		if (result != null) {
			Log.e("getFilterList", result);
		} else {
			Log.e("getFilterList", "null");
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
