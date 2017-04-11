package com.bccv.zhuiyingzhihanju.api;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.MD5Util;
import com.utils.tools.UserInfoUtil;

public class Loginservice {

	// 注册
	// 忘记密码 type		验证码类型  1 注册  2  找回密码 3 修改信息
	public static String reg(String user_phone, String pwd,String type,String code) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
//		params.put("opt", "doreg");
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("phone", user_phone);
		params.put("type", type);
		params.put("code", code);
		
//		params.put("nick_name", nick_name);
//		params.put("key", phone_key);
		pwd = MD5Util.string2MD5(pwd);
		params.put("pwd", pwd);
		params.put("getui_clientid", GlobalParams.client_id);

		return util.sendGet(UserUrl.doreg, params);
	}

	// 登录
	public static String Login(String user_phone, String pwd) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
//		params.put("opt", "login");
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("phone", user_phone);
		pwd = MD5Util.string2MD5(pwd);
		// Log.e("pwd", pwd);
		params.put("pwd", pwd);
		params.put("getui_clientid", GlobalParams.client_id);
		return util.sendGet(UserUrl.dologin, params);
	}

	// 更改用户信息
	public static String setUpdateUser(String user_id, String nick_name, Bitmap icon, String pwd, String new_pwd,
			String msg, String sex, String token,String phone) {
		
		Log.e("setUpdateUser", "Loginservice [user_id=" + user_id + ", nick_name=" + nick_name + ", icon=" + icon + ", pwd=" + pwd
				+ ", new_pwd=" + new_pwd + ", msg=" + msg + ", sex=" + sex + ", token=" + token + ", phone=" + phone
				+ "]");
		
		
		HttpClientUtil sutil = new HttpClientUtil();

		Map<String, String> params = new HashMap<String, String>();
//		params.put("opt", "updateuserinfo");
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		
		params.put("token", token);

		String headPicString = UserInfoUtil.getPicString(icon);

		params.put("avatars", headPicString);
		params.put("phone", phone);
		params.put("nick_name", nick_name);
//		pwd=MD5Util.string2MD5(pwd);
//		params.put("pwd", pwd);
		// new_pwd=MD5Util.string2MD5(new_pwd);
//		params.put("new_pwd", new_pwd);
		params.put("des", msg);
		params.put("sex", sex);
		return sutil.sendPostNoSecret(UserUrl.infoEdit, params);
	}

//	// 忘记密码
//
//	public static String forgetPwd(String user_phone, String code, String pwd) {
//		HttpClientUtil util = new HttpClientUtil();
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("opt", "getpassword");
//		params.put("apptype", GlobalParams.apptype);
//		params.put("appid", GlobalParams.appid);
//		params.put("mob_num", user_phone);
//		params.put("key", code);
//		pwd = MD5Util.string2MD5(pwd);
//		params.put("pwd", pwd);
//
//		return util.sendGet(Url.Host_url, params);
//	}

	

	/***
	 * 获取短信验证码 mob_num----手机号sshoujihap 必填 type-------1 获取注册短信验证码 2 获取找回密码短信验证码
	 * 
	 */

	public static String getCode(String mob_num, String type) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
//		params.put("opt", "send_msg");
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("phone", mob_num);

		params.put("type", type);

		return util.sendGet(UserUrl.ScodeUrl, params);
	}

}
