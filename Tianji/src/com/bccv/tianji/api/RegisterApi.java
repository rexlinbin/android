package com.bccv.tianji.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bccv.tianji.model.User;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

public class RegisterApi extends AppApi {
	private String rtnStr;
	Context context;

	public RegisterApi(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 
	 * 注册
	 * 
	 * 
	 * **/
	public User Register(String phone, String code, String password) {
		String result = Loginservice.reg(phone, code, password);
		Log.e("注册参数", phone+"  code   "+code+ "     password"   + password); 
Log.e("注册结果", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				User user = new User();
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
				
				
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
				
						user = JSON.parseObject(rtnStr, User.class);
						return user;
					}
					
				}else {
						int errorNum = Integer.parseInt(jsonObject.getString("status_code"));
					
					
					String errorString = "";
					switch (errorNum) {
					case 803:
						errorString = "手机号已经被注册";
						
						user.setUserErr(errorString);
					
						break;
					case 806:
						errorString = "验证码错误";
					
						user.setUserErr(errorString);
						break;
					
					case 805:
						errorString = "验证码失效";
						
						user.setUserErr(errorString);
						break;
						
						
					default:
						break;
						
					}
					
					
					
				}

			
				return user;	
					
					
			
			} catch (JSONException e) {
				e.printStackTrace();
				
				Log.e("注册错误", e.toString());
			}
		}
		return null;
	}

	/**
	 * 登录
	 * 
	 * 
	 * **/
	public User Login(String phone, String password) {
		String result = Loginservice.Login(phone, password);
		
Log.e("phone", phone);
Log.e("password", password);	
Log.e("Loginresult", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				User user = new User();
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
					
						user = JSON.parseObject(rtnStr, User.class);
						return user;
					}
				} else {
					int errorNum = Integer.parseInt(jsonObject.getString("status_code"));
			
				String errorString = "";
				switch (errorNum) {
				case 806:
					errorString = "手机号已经被注册";
					
					user.setUserErr(errorString);
				
					break;
				case 803:
					errorString = "验证码错误";
				
					user.setUserErr(errorString);
					break;
				
				case 805:
					errorString = "验证码失效";
					
					user.setUserErr(errorString);
					break;
					
					
				default:
					break;
					
				}
				
				
				
			}

				return user;	
					
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 更改用户信息
	 * 
	 * 
	 * **/
	public User LoninUpdateInfo(String userID, String nick_name,Bitmap icon) {
		
		String result = Loginservice.setUpdateUser(userID, nick_name, icon);
		Log.v("LoninUpdateInfo", result);	
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
				
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						User user = new User();
						user = JSON.parseObject(rtnStr, User.class);
						return user;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 忘记密码
	 * 
	 * 
	 * **/
	public User forget(String phone, String code, String password) {

		String result = Loginservice.forgetPwd(phone, code, password);
		Log.v("forget", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {

					String rtnStr = jsonObject.getString("data");
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						User user = new User();
						user = JSON.parseObject(rtnStr, User.class);
						return user;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	
	/**
	 * 获取短信验证码
	 * 
	 * 
	 * **/
	
	public String getCode(String mob_num,String type){
		
	
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "send_msg");
		params.put("mob_num", mob_num);
		params.put("type", type);
		String result = util.sendGet(Url.Host_url, params);
		
		Log.e("获取短信", result);
		if(!StringUtils.isEmpty(result)){
			
		
			try {
				JSONObject json=new JSONObject(result);
				if (checkResponse(json)){
					String rtnStr= json.getString("status");
					return rtnStr;
				}
				else {
					int errorNum = Integer.parseInt(json.getString("status_code"));
			
				
				String errorString = "";
				switch (errorNum) {
				case  803:
					rtnStr = "超过当天获取次数限制";
					
					
				
					break;
				case 804:
					rtnStr = "一分钟内不可重复获取";
				
				
					break;
				
				default:
					break;
					
				}
				}
				
				
				return rtnStr;	
				
			
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			
			
		}
		
		
		return "false";
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
