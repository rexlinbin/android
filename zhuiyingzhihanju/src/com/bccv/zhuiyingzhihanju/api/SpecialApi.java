package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.MovieNews;
import com.bccv.zhuiyingzhihanju.model.Special;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

import android.util.Log;

public class SpecialApi extends AppApi{

	
	
	
	
	public MovieNews getThemList(String flag,String page, String count) {

		HttpClientUtil util = new HttpClientUtil();

		Map<String, String> params = new HashMap<String, String>();

		params.put("flag", flag);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Theme_list, params);
		
		if (!StringUtils.isEmpty(result)) {
			Log.e("Specialresult", result.toString());
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
	
	
	
	

	public List<Special> getThemList(String page, String count) {

		HttpClientUtil util = new HttpClientUtil();

		Map<String, String> params = new HashMap<String, String>();
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Theme, params);

		if (!StringUtils.isEmpty(result)) {

			try {
				JSONObject jsonObject = new JSONObject(result);

				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Special> list = null;
						list = JSON.parseArray(rtnStr, Special.class);
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
