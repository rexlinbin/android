package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.StarInfo;
import com.bccv.zhuiyingzhihanju.model.StarWork;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

import android.util.Log;

public class StarApi extends AppApi {
	public StarInfo getStarInfo(String star_id) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("sid", star_id);
		// params.put("page", page);
		// params.put("count", count);

		String result = util.sendGet(Url.StarInfourl, params);
		if (result != null) {
			Log.e("getStarInfo", result);
		} else {
			Log.e("getStarInfo", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						JSONObject data = new JSONObject(rtnStr);
						StarInfo starInfo = null;
						starInfo = JSON.parseObject(data.getString("starInfo"), StarInfo.class);

						StarWork works = JSON.parseObject(data.getString("works"), StarWork.class);
						StarWork news = JSON.parseObject(data.getString("news"), StarWork.class);

						starInfo.setWorks(works);
						starInfo.setNews(news);
						return starInfo;

					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
