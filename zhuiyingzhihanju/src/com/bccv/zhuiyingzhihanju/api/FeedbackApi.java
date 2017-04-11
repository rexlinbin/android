package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.bccv.zhuiyingzhihanju.api.AppApi;
import com.bccv.zhuiyingzhihanju.api.Url;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.StringUtils;

public class FeedbackApi extends AppApi {
	public String seedFeedback( String version,String question,String user_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);

	params.put("version", version);
		params.put("question", question);
	params.put("uid", user_id);
		
		
		
		String result = util.sendGet(UserUrl.Fdosend, params);
	Logger.e("seedFeedback", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("status");
					
					String rtnSt = jsonObject.getString("msg");
				
					return rtnSt;
					
				
					
				}else {
					
					return jsonObject.getString("msg");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
