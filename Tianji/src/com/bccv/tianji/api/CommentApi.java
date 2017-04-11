package com.bccv.tianji.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bccv.tianji.model.Comment;
import com.bccv.tianji.model.CommentReply;
import com.bccv.tianji.model.Reply;
import com.utils.net.HttpClientUtil;
import com.utils.tools.StringUtils;

public class CommentApi extends AppApi {
	public List<Comment> getMyCommentsList(String user_id, String page,
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "get_usercomment");
		params.put("user_id", user_id);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getMyCommentsList", result);
		} else {
			Log.e("getMyCommentsList", "null");
		}
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
	
	public List<Reply> getReplyList(String comment_id, String page,
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "get_reply");
		params.put("comment_id", comment_id);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getMyReplyList", result);
		} else {
			Log.e("getMyReplyList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Reply> list = null;
						list = JSON.parseArray(rtnStr, Reply.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	/**
	 * 
	 * 详情界面评论
	 * 
	 * **/
	
	public List<CommentReply> getCommentsList(String game_id, String page,
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "get_comment");
		params.put("game_id",game_id);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getCommentsList", result);
		} else {
			Log.e("getCommentsList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<CommentReply> list = null;
						list = JSON.parseArray(rtnStr, CommentReply.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	
	
	
	/**
	 * 
	 * 详情界面回复
	 * 
	 * **/
	public List<Reply> setReplyList(String comment_id, String page,
			String count) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "do_reply");
		params.put("comment_id", comment_id);
		params.put("page", page);
		params.put("count", count);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getReplyList", result);
		} else {
			Log.e("getReplyList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("data");

					if (!StringUtils.isEmpty(rtnStr) && !rtnStr.equals("null")) {

						List<Reply> list = null;
						list = JSON.parseArray(rtnStr, Reply.class);

						return list;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	/**
	 * 用户评论游戏 给游戏评星
	 *  point 和 comment两参数不可同时为空！
	 * 
	 * **/
	
	public String  setReplyStarList(String game_id, String point,
			String comment,String user_id) {
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "do_rr");
		params.put("game_id", game_id);
		params.put("point", point);
		params.put("comment", comment);
		params.put("user_id", user_id);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("getReplyList", result);
		} else {
			Log.e("getReplyList", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("status");
					return rtnStr;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	/**
	 * 用户回复评论
	 *  point 和 comment两参数不可同时为空！
	 * 
	 * **/
	
	public String ReplyList(String game_id, String comment_id,
			String comment,String user_id) {
		Log.e("参数", "comment_id"+comment_id+"game_id"+game_id+"comment"+comment);
		
		HttpClientUtil util = new HttpClientUtil();
		Map<String, String> params = new HashMap<String, String>();
		params.put("opt", "do_reply");
		params.put("game_id", game_id);
		params.put("comment_id", comment_id);
		params.put("comment", comment);
		params.put("user_id", user_id);
		String result = util.sendGet(Url.Host_url, params);
		if (result != null) {
			Log.e("do_reply", result);
		} else {
			Log.e("do_reply", "null");
		}
		if (!StringUtils.isEmpty(result)) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (checkResponse(jsonObject)) {
					String rtnStr = jsonObject.getString("status");
					return rtnStr;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	
	
}
