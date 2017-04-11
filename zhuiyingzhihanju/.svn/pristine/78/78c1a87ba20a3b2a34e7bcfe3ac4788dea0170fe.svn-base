package com.bccv.zhuiyingzhihanju.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.Magnet;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

import android.util.Log;

public class MagnetApi extends AppApi {

	public Magnet getMagentInfo(String url) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		// params.put("channel", channel);
		// params.put("upver", upver);
		try {
			url = URLEncoder.encode(url, "utf-8");
			String result = util.sendGet(GlobalParams.videoUrl + "/v1/cili/anget.html?url=" + url, params);
			if (result != null) {
				Log.e("getMagentInfo", result);
			} else {
				Log.e("getMagentInfo", "null");
			}
			if (!StringUtils.isEmpty(result)) {

				Magnet list = null;
				list = JSON.parseObject(result, Magnet.class);
				return list;

			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

}
