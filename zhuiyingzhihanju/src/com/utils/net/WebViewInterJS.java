package com.utils.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bccv.zhuiyingzhihanju.activity.StarInfoActivity;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
import com.bccv.zhuiyingzhihanju.fragment.SpecialFragment;
import com.igexin.sdk.PushBuildConfig;
import com.tencent.smtt.sdk.WebView;
import com.utils.tools.GlobalParams;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;

public class WebViewInterJS {
	private Activity activity;
	private WebView webView;

	public WebViewInterJS(Activity activity, WebView webView) {
		this.activity = activity;
		this.webView = webView;
	}
	/**
	 * 测试
	 * 
	 */
	@JavascriptInterface
	public void callNative(final String id, final String type_id) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(activity.getApplicationContext(), Video2DPlayerActivity.class);
				intent.putExtra("movie_id", id);
				intent.putExtra("type_id", type_id);
				activity.startActivity(intent);
			}
		});
	}
	/**
	 * 调用视频详情
	 * @param json
	 */
	@JavascriptInterface
	public void callPluralVideo(String json) {
		final JSONObject jsonObject = JSON.parseObject(json);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(activity.getApplicationContext(), Video2DPlayerActivity.class);
				intent.putExtra("movie_id", jsonObject.getString("video_id"));
				intent.putExtra("type_id", jsonObject.getString("type_id"));
				intent.putExtra("episodes_id", jsonObject.getString("episode_id"));
				intent.putExtra("isEpisode", true);
				activity.startActivity(intent);
			}
		});
	}
	/**
	 * 调用猜你喜欢
	 * @param json
	 */
	@JavascriptInterface
	public void callLikeVideo(String json) {
		final JSONObject jsonObject = JSON.parseObject(json);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(activity.getApplicationContext(), Video2DPlayerActivity.class);
				intent.putExtra("movie_id", jsonObject.getString("video_id"));
				intent.putExtra("type_id", jsonObject.getString("type_id"));
				intent.putExtra("episodes_id", jsonObject.getString("episode_id"));
				intent.putExtra("isEpisode", true);
				activity.startActivity(intent);
				activity.finish();
			}
		});
	}
	
	
	
	/**
	 * 调用视频详情
	 * @param json
	 */
	@JavascriptInterface
	public void callSingleVideo(String json) {
		final JSONObject jsonObject = JSON.parseObject(json);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(activity.getApplicationContext(), Video2DPlayerActivity.class);
				intent.putExtra("movie_id", jsonObject.getString("video_id"));
				intent.putExtra("type_id", jsonObject.getString("type_id"));
				intent.putExtra("isEpisode", false);
				activity.startActivity(intent);
			}
		});
	}

	/**
	 * 
	 * @param title 標題
	 * @param thumb 缩略图
	 * @param content 简介
	 * @param url 分享地址
	 */
	@JavascriptInterface
	public void callShare(String json){
		final JSONObject jsonObject = JSON.parseObject(json);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Video2DPlayerActivity)activity).showShare(jsonObject);;
			}
		});
	}
	/**
	 * 下载
	 * @param json {title:"",image:""}
	 */
	@JavascriptInterface
	public void callDownload(String json) {
//		String json = "";//
		final JSONObject jsonObject = JSON.parseObject(json);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Video2DPlayerActivity)activity).showDownload(jsonObject.getString("image"), jsonObject.getString("title"));
//				((Video2DPlayerActivity)activity).showDownload("","");
			}
		});

	}
	/**
	 * 调用明星主页
	 * @param json
	 */
	@JavascriptInterface
	public void callStar(String json) {
		final JSONObject jsonObject = JSON.parseObject(json);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(activity.getApplicationContext(), StarInfoActivity.class);
				intent.putExtra("star_id", jsonObject.getString("star_id"));
				activity.startActivity(intent);
			}
		});
		
	}
	
	/**
	 * 详情切换剧集
	 * @param json
	 */
	@JavascriptInterface
	public void callSwitchSpisode(String json) {
		final JSONObject jsonObject = JSON.parseObject(json);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Video2DPlayerActivity)activity).changeEpisode(jsonObject.getString("episode_id"));;
			}
		});

	}
	
	/**
	 * 打开评论框
	 * @param json
	 */
	@JavascriptInterface
	public void openCommonBox() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Video2DPlayerActivity)activity).openComment();
			}
		});
		
	}
	
	/**
	 * 回复
	 * @param json	{"user_id":"1", "from_id":"10", "from_nickname":"小明"}
	 */
	@JavascriptInterface
	public void replyCommentBox(String json) {
		final JSONObject jsonObject = JSON.parseObject(json);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Video2DPlayerActivity)activity).showReply(jsonObject);
			}
		});
	}
	

	
	/**
	 * 关闭评论框
	 * @param json
	 */
	@JavascriptInterface
	public void closeCommonBox() {
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((Video2DPlayerActivity)activity).closeComment();
			}
		});
	}
	
	/**
	 * 获取是否登录
	 * @return 登录信息
	 */
	@JavascriptInterface
	public String onlineUserInfo(){
		JSONObject jsonObject = new JSONObject();
		if (GlobalParams.hasLogin) {
			jsonObject.put("user_id", GlobalParams.user.getUid());
			jsonObject.put("user_name", GlobalParams.user.getPhone());
		}else{
			jsonObject.put("user_id", "");
			jsonObject.put("user_name", "");
		}
		
		return jsonObject.toJSONString();
	}
	
//	评论/回复回调
//	@return	@return {"user_id":"1",	"from_id":"10","content":"内容"} 回复
//			@return	@return {"user_id":"1",	"content":"内容"} 评论
//			pageDetail.commentCallback() 
	
	
	
	/**
	 * 跳转韩剧
	 * @param json
	 */
	@JavascriptInterface
	public void callMoreVideo(String json) {
		final JSONObject jsonObject = JSON.parseObject(json);
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(activity.getApplicationContext(), SpecialFragment.class);
				intent.putExtra("news", jsonObject.getString("news"));
				intent.putExtra("tv", jsonObject.getString("tv"));
				intent.putExtra("music", jsonObject.getString("music"));
				intent.putExtra("movie", jsonObject.getString("movie"));
				intent.putExtra("variety", jsonObject.getString("variety"));
				intent.putExtra("isSelect", false);
				
				activity.startActivity(intent);
			
			}
		});
	}
	
	
	
	
	
	
	
	
	
	
}
