package com.bccv.zhuiyingzhihanju.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.zhuiyingzhihanju.model.Collect;
import com.bccv.zhuiyingzhihanju.model.Comment;
import com.utils.net.HttpClientUtil;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

import android.util.Log;
import android.widget.Toast;

public class CommentApi extends AppApi {
	public List<Comment> getCommentList(String movie_id, String type_id, String page, String count){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("video_id", movie_id);
		params.put("video_type", type_id);
		params.put("page", page);
		params.put("perpage", count);
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		String result = util.sendGet(UserUrl.dolist, params);
		if (result != null) {
			Log.e("getCommentList", result);
		} else {
			Log.e("getCommentList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getJSONObject("data").getString("items");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Comment> list = null;
						list = JSON.parseArray(rtnStr, Comment.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public List<Comment> getThemeCommentList(String theme_id, String page, String count){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("cat_id", "2");
		params.put("topic_id", theme_id);
		params.put("page", page);
		params.put("perpage", count);
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		String result = util.sendGet(UserUrl.dolist, params);
		if (result != null) {
			Log.e("getThemeCommentList", result);
		} else {
			Log.e("getThemeCommentList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getJSONObject("data").getString("items");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {
						List<Comment> list = null;
						list = JSON.parseArray(rtnStr, Comment.class);
						if (list == null) {
							list = new ArrayList<>();
						}
						return list;
					}else{
						return new ArrayList<>();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	public void commentTheme(String comment, String user_id, String theme_id, String from_id, String token, String platform){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		params.put("cat_id", "2");
		params.put("topic_id", theme_id);
		params.put("platform", platform);
		params.put("token", token);
		try {
			comment = URLEncoder.encode(comment, "UTF-8");
			params.put("content", comment);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		if (!StringUtils.isEmpty(from_id)) {
			params.put("fid", from_id);
		}
		
		
		String result = util.sendPostNoSecret(UserUrl.dosend, params);
		if (result != null) {
			Log.e("comment", result);
		} else {
			Log.e("comment", "null");
		}
	}
	
	public void comment(String comment, String user_id, String video_id, String type_id, String from_id, String token,String platform){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("uid", user_id);
		params.put("video_id", video_id);
		params.put("video_type", type_id);
		params.put("token", token);
		params.put("platform", platform);
		try {
			comment = URLEncoder.encode(comment, "UTF-8");
			params.put("content", comment);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		if (!StringUtils.isEmpty(from_id)) {
			params.put("fid", from_id);
		}
		
		
		String result = util.sendPostNoSecret(UserUrl.dosend, params);
		if (result != null) {
			Log.e("comment", result);
		} else {
			Log.e("comment", "null");
		}
	}
	
	public void digg(String comment_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("id", comment_id);
		
		String result = util.sendGet(UserUrl.Dodigg, params);
		if (result != null) {
			Log.e("digg", result);
		} else {
			Log.e("digg", "null");
		}
	}
	
	public void report(String comment_id, String user_id){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("apptype", GlobalParams.apptype);
		params.put("appid", GlobalParams.appid);
		params.put("comment_id", comment_id);
		params.put("uid", user_id);
		
		String result = util.sendGet(UserUrl.Fdosend, params);
		if (result != null) {
			Log.e("report", result);
		} else {
			Log.e("report", "null");
		}
	}
}
