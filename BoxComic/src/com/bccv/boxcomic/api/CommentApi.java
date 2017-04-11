package com.bccv.boxcomic.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.bccv.boxcomic.modal.Comment;
import com.bccv.boxcomic.net.HttpClientUtil;
import com.bccv.boxcomic.net.Url;
import com.bccv.boxcomic.tool.AppApi;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.StringUtils;

public class CommentApi extends AppApi {
	public List<Comment> getCommentList(String comic_id, String pageString, String countString){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("comic_id", comic_id);
		params.put("p", pageString);
		params.put("num", countString);
		String result = util.sendGet(Url.CommentList_url, params);
		Logger.e("getChannelList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
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
	
	public List<Comment> getMyCommentList(String user_id, String pageString, String countString){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("user_id", user_id);
		params.put("p", pageString);
		params.put("num", countString);
		String result = util.sendGet(Url.MyCommentList_url, params);
		Logger.e("getChannelList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
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
	
	public List<Comment> getReplyList(String comment_id, String pageString, String countString){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("comment_id", comment_id);
		params.put("p", pageString);
		params.put("num", countString);
		String result = util.sendGet(Url.ReplyList_url, params);
		Logger.e("getChannelList", result);
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");
					
					
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
	
	public boolean sendComment(String id, String content, String user_id, String report){
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("user_id", user_id);
		params.put("comment_content", content);
		params.put("report", report);
		String result = util.sendGet(Url.Comment_url, params);
		Logger.e("sendComment", result);
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
}
