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

public class AnalysisApi extends AppApi {
	public List<InfoAnalysis> getInfoAnalysisList(String comic_id, String pageString, String countString){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("comic_id", comic_id);
		params.put("p", pageString);
		params.put("num", countString);
		String result = util.sendGet(Url.InfoAnalysisList_url, params);
		Logger.e("getInfoAnalysisList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						List<InfoAnalysis> list = null;
						list = JSON.parseArray(rtnStr, InfoAnalysis.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public InfoAnalysis getInfoAnalysis(String advices_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("advices_id", advices_id);
		String result = util.sendGet(Url.InfoAnalysis_url, params);
		Logger.e("getInfoAnalysis", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						
						InfoAnalysis infoAnalysis = null;
						infoAnalysis = JSON.parseObject(rtnStr, InfoAnalysis.class);

						return infoAnalysis;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
