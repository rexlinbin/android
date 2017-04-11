package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.Msg;
import com.bccv.zhuiyingzhihanju.model.MyMsg;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.StringUtils;

import android.util.Log;

public class MSGApi extends AppApi {
	public List<Msg> getSysMSgList(String user_id, String page, String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", user_id);
		params.put("page", page);
		params.put("count", count);
		params.put("token", GlobalParams.user.getToken());
		String result = util.sendGet(Url.SysMsg_url, params);
		if (result != null) {
			Log.v("getSysMSgList", result);
		} else {
			Log.v("getSysMSgList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Msg> list = null;
						list = JSON.parseArray(rtnStr, Msg.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	public List<Msg> getUserMSgList(String user_id, String page, String count,String token) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		params.put("page", page);
		params.put("count", count);
		params.put("token", token);
		String result = util.sendGet(UserUrl.CommentsDoshow, params);
		if (result != null) {
			Log.e("getUserMSgList", result);
		} else {
			Log.e("getUserMSgList", "null"); 
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr1 = jsonObject.getString("data");
					JSONObject jsonObject1 = new JSONObject(rtnStr1);
					String rtnStr=  jsonObject1.getString("items");


					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Msg> list = null;
						list = JSON.parseArray(rtnStr, Msg.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	
	

	public boolean sendDelect( String user_id, String msg_id, String typ_id,String token){
//		Log.e("send", user_id+"              "+msg_id+"          "+typ_id);
		
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "del_msg");
		params.put("user_id", user_id);
		params.put("msg_id", msg_id);
		params.put("typ_id", typ_id);
		params.put("token", token);
		String result = util.sendGet(Url.Host_url, params);
		Log.v("sendDelect", result);
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
	
	
	
	
	
	public MyMsg getCheckMSgList(String user_id) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "check_msg");
		params.put("user_id", user_id);
		MyMsg msg=new MyMsg();
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.v("getCheckMSgList", result);
		} else {
			Log.v("getCheckMSgList", "null");
		}
		
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

					
						msg = JSON.parseObject(rtnStr, MyMsg.class);

						return msg;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	
	public int getCode() {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "version");
		
	
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.v("getCode", result);
		} else {
			Log.v("getCode", "null");
		}
		
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					int rtnStr = jsonObject.getInt("data");


						return rtnStr;
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
}