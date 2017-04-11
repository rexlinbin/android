package com.bccv.zhuiyingzhihanju.api;

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
import com.bccv.zhuiyingzhihanju.model.User;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

public class UserAPi extends AppApi {
	private String rtnStr;
	Context context;

	public UserAPi(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 
	 * 注册
	 * 
	 * 
	 **/
	public User Register(String phone, String password,String code) {
		String result = Loginservice.reg(phone, password, "1", code);
		if(result!=null){
			Log.e("注册参数", phone + "  code   " + code + "     password" + password);
			Log.e("注册结果", result);
		}
	
		if (!StringUtils.isEmpty(result)) {
			try {
				User user = new User();
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr1 = jsonObject.getString("data");
					JSONObject jsonObject1 = new JSONObject(rtnStr1);
					String rtnStr=  jsonObject1.getString("items");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						user = JSON.parseObject(rtnStr, User.class);
						return user;
					}

				} else {String errorString = jsonObject.getString("msg");
				user.setUserErr(errorString);}

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
	 **/
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
					String rtnStr1 = jsonObject.getString("data");
					JSONObject jsonObject1 = new JSONObject(rtnStr1);
					String rtnStr=  jsonObject1.getString("items");
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						user = JSON.parseObject(rtnStr, User.class);
						return user;
					}
				} else {
					String errorString = jsonObject.getString("msg");
					user.setUserErr(errorString);
//				String errorString = "登录失败";
//					switch (errorNum) {
//					case 806:
//						errorString = "手机号已经被注册";
//
//					
//
//						break;
//					case 803:
//						errorString = "验证码错误";
//
//						user.setUserErr(errorString);
//						break;
//
//					case 805:
//						errorString = "验证码失效";
//
//						user.setUserErr(errorString);
//						break;
//
//					default:
//						user.setUserErr(errorString);
//						break;
//
//					}

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
	 **/
	public User LoninUpdateInfo(String userID, String nick_name, Bitmap icon, String pwd, String new_pwd,String msg,String sex,String token,String phone) {

		String result = Loginservice.setUpdateUser(userID, nick_name, icon, pwd, new_pwd,msg,sex,token, phone);
		Log.v("LoninUpdateInfo", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				User user = new User();
				if (checkResponse(jsonObject)) {
					String rtnStr1 = jsonObject.getString("data");
					JSONObject jsonObject1 = new JSONObject(rtnStr1);
					String rtnStr=  jsonObject1.getString("items");

					
					
					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
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
	 * 重置密码
	 * 
	 * 
	 **/
	public String forget(String phone, String password,String code) {
		String errorString = "";
		String result = Loginservice.reg(phone, password, "2", code);
		Log.v("forget", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
			
				if (checkResponse(jsonObject)) {	
					String rtnStr = jsonObject.getString("status");
				
				String rtnSt = jsonObject.getString("msg");
			
				return rtnSt;
				} else {
				 errorString = jsonObject.getString("msg");
				
					
//					switch (errorNum) {
//					case 808:
//						errorString = "未找到相应账号";
//
//						user.setUserErr(errorString);
//
//						break;
//					case 807:
//						errorString = "更改失败";
//
//						user.setUserErr(errorString);
//						break;
//
//					case 805:
//						errorString = "验证码失效";
//
//						user.setUserErr(errorString);
//						break;
//
//					default:
//						user.setUserErr(errorString);
//						break;
//
//					}

				}

				return errorString;

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
	 **/

	public String getCode(String mob_num, String type) {
		String errorString = "";
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
//		params.put("opt", "send_msg");
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("phone", mob_num);
		params.put("type", type);
		
		String result = util.sendGet(UserUrl.ScodeUrl, params);
			Log.e("Coderesult", result);
		
		if (!StringUtils.isEmpty(result)) {

			try {
				JSONObject json = new JSONObject(result);
				if (checkResponse(json)) {
					String rtnStr = json.getString("status");
				
					String rtnSt = json.getString("msg");
				
					return rtnSt;
				} else {
					
					errorString=json.getString("msg");


				}

				return errorString;

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

		return "";

	}
	/**
	 * 获取短信验证码
	 * 
	 * 
	 **/

	public String getCodeMsg(String mob_num, String type,String code) {
		String errorString = "";
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
//		params.put("opt", "send_msg");
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("phone", mob_num);
		params.put("type", type);
		params.put("code", code);
		String result = util.sendGet(UserUrl.docheck, params);
			Log.e("Coderesult", result);
		
		if (!StringUtils.isEmpty(result)) {

			try {
				JSONObject json = new JSONObject(result);
				if (checkResponse(json)) {
					String rtnStr = json.getString("status");
					
					String rtnSt = json.getString("msg");
				
					return rtnSt;
				} else {
					
					errorString=json.getString("msg");

					
//					switch (errorNum) {
//					case 803:
//						rtnStr = "超过当天获取次数限制";
//
//						break;
//					case 804:
//						rtnStr = "一分钟内不可重复获取";
//
//						break;
//
//					default:
//						break;
//
//					}
				}

				return errorString;

			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return "";

	}

}
