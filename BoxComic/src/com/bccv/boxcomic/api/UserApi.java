package com.bccv.boxcomic.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSONException;
import com.bccv.boxcomic.modal.User;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.sns.UserInfoManager.UserType;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.MD5Util;
import com.bccv.boxcomic.tool.StringUtils;

public class UserApi extends AppApi {

	public User login(UserType user_type, String user_name, String user_icon,
			String user_openid, String user_token) {

		Logger.e("UserApi", "type:" + user_type.name() + "," + " user_name:  "
				+ user_name + " user_icon:  " + user_icon + " user_openid: "
				+ user_openid + " user_token: " + user_token);

		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", user_type.name() + "");
		params.put("user_name", user_name + "");
		params.put("icon", user_icon + "");
		params.put("openid", user_openid + "");

		String result = util.sendGet(Url.Login_url, params);
		Logger.e("Login", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					if (checkResponse(jsonObject)) {
						GlobalParams.user.setUser_id(Integer
								.parseInt(jsonObject.getString("user_id")));
						GlobalParams.user.setUser_icon(jsonObject
								.getString("user_icon"));
						GlobalParams.user.setUser_name(jsonObject
								.getString("user_name"));

					}
					return GlobalParams.user;
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public User UpdateInfo(String user_name, String user_icon, String user_id,String pic_type, String pic) {

		Logger.e("UserApi", " user_name:  " + user_name + " user_icon:  "
				+ user_icon + " pic_type: " + pic_type + " pic_type: "
				+ pic_type);

		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_name", user_name + "");
		params.put("icon", user_icon + "");
		params.put("user_id", user_id + "");
		params.put("pic_type", pic_type + "");
		params.put("pic", pic + "");
		String result = util.sendPostNoSecret(Url.UpdateUser, params);
		Logger.e("Login", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					if (checkResponse(jsonObject)) {
						GlobalParams.user.setUser_id(Integer
								.parseInt(jsonObject.getString("user_id")));
						GlobalParams.user.setUser_icon(jsonObject
								.getString("user_icon"));
						GlobalParams.user.setUser_name(jsonObject
								.getString("user_name"));

					}
					return GlobalParams.user;
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public User loginOWn(String user_account, String user_password) {

//		Logger.e("UserApi", "type:" + user_type.name() + "," + " user_name:  "
//				+ user_name + " user_icon:  " + user_icon + " user_openid: "
//				+ user_openid + " user_token: " + user_token);

		
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_account", user_account);
		params.put("user_password", MD5Util.string2MD5(user_password));
		

		String result = util.sendGet("http://comicapi.boxuu.com/v1/comic/login_account/", params);
		Logger.e("Login", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					if (checkResponse(jsonObject)) {
						GlobalParams.user.setUser_id(Integer
								.parseInt(jsonObject.getString("user_id")));
						GlobalParams.user.setUser_icon(jsonObject
								.getString("user_icon"));
						GlobalParams.user.setUser_name(jsonObject
								.getString("user_name"));
						return GlobalParams.user;
					}
					
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public User Register(String user_account, String user_password) {

//		Logger.e("UserApi", "type:" + user_type.name() + "," + " user_name:  "
//				+ user_name + " user_icon:  " + user_icon + " user_openid: "
//				+ user_openid + " user_token: " + user_token);

		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_account", user_account);
		params.put("user_password", MD5Util.string2MD5(user_password));
		

		String result = util.sendGet("http://comicapi.boxuu.com/v1/comic/reg_account/", params);
		Logger.e("Register", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					if (checkResponse(jsonObject)) {
						GlobalParams.user.setUser_id(Integer
								.parseInt(jsonObject.getString("user_id")));
						GlobalParams.user.setUser_icon(jsonObject
								.getString("user_icon"));
						GlobalParams.user.setUser_name(jsonObject
								.getString("user_name"));
						return GlobalParams.user;
					}
				
				} catch (org.json.JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
