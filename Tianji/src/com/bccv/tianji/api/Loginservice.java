package com.bccv.tianji.api;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

import com.utils.net.HttpClientUtil;
import com.utils.tools.MD5Util;
import com.utils.tools.UserInfoUtil;


public class Loginservice {
	
	//注册
	public static String reg(String user_phone,String phone_key,String pwd)
	{
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "reg");
		params.put("mob_num", user_phone);
		params.put("key", phone_key);
		pwd=MD5Util.string2MD5(pwd);
		params.put("pwd", pwd);
		
		
		return util.sendGet(Url.Host_url, params);
	}
	//登录
	public static String Login(String user_phone,String pwd)
	{
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "login");
		params.put("user_name",user_phone);
		pwd=MD5Util.string2MD5(pwd);
		Log.e("pwd", pwd);
		params.put("pwd", pwd);		
		return util.sendGet(Url.Host_url, params);
	}
	
	
	//更改用户信息
	public static String setUpdateUser(String user_id,String nick_name,Bitmap icon)
	{
		HttpClientUtil util = new HttpClientUtil();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt","updateuserinfo");
		params.put("user_id",user_id);
		params.put("nick_name",nick_name);
		String headPicString = UserInfoUtil.getPicString(icon);
	
		params.put("icon", headPicString);
		
		
		return util.sendPostNoSecret(Url.Host_url, params);
	}
	
	
		
				//忘记密码
				
				public static String forgetPwd(String user_phone,String code,String pwd)
				{
					HttpClientUtil util = new HttpClientUtil();
					Map<String, String> params = new HashMap<String, String>();
					params.put("opt", "getpassword");
					params.put("mob_num", user_phone);
					params.put("key", code);
					pwd=MD5Util.string2MD5(pwd);
					params.put("pwd", pwd);
					
					return util.sendGet(Url.Host_url, params);
				}
				
			/***
			 * 获取短信验证码
			 *   mob_num----手机号sshoujihap 必填 
      type-------1 获取注册短信验证码       
                 2 获取找回密码短信验证码  

			 * */

				
				public static String getCode(String mob_num,String type)
				{
					HttpClientUtil util = new HttpClientUtil();
					Map<String, String> params = new HashMap<String, String>();
					params.put("opt", "send_msg");
					params.put("mob_num", mob_num);
					
					params.put("type", type);
					
					return util.sendGet(Url.Host_url, params);
				}	
				
				
				
				
				
				
				
				
				
} 
