package com.bccv.tianji.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bccv.tianji.model.Classification;
import com.bccv.tianji.model.HotSearch;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

public class ClassificationApi extends AppApi {
	public List<Classification> getClassificationList() {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "type_list");
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getClassificationList", result);
		} else {
			Log.e("getClassificationList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Classification> list = null;
						list = JSON.parseArray(rtnStr, Classification.class);

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
