package com.bccv.boxcomic.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class FeedbackApi extends AppApi {
	public boolean seedFeedback(String user_id, String content){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", user_id);
		params.put("message", content);

		String result = util.sendGet(Url.Feedback_url, params);
		Logger.e("seedFeedback", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					return true;
				}else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
