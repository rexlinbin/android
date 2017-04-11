package com.bccv.threedimensionalworld.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bccv.threedimensionalworld.Net.HttpClientUtil;
import com.bccv.threedimensionalworld.model.MovieClassification;
import com.bccv.threedimensionalworld.model.MusicBean;
import com.bccv.threedimensionalworld.tool.StringUtils;

public class MusicApi extends AppApi{

	public List<MusicBean> getMusicList() {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		
//		params.put("app_name", "radio_desktop_win");
//		params.put("version", "100");
//		params.put("type", "n");
//		params.put("channel", "0");
		String result = util.sendGet(Url.music_url, params);
		
		Log.e("数据列表", result.toString());
		
		
		if (!StringUtils.isEmpty(result)) {
			
			try {
				
				JSONObject jsonObject = new JSONObject(result);
				
				
					String rtnStr = jsonObject.getString("data");
					Log.e("数据数据", rtnStr);
					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<MusicBean> list = null;
						list =JSON.parseArray(rtnStr,MusicBean.class);

						return list;
					}
				
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("数据", e.toString());
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
}
