package com.bccv.bangyangapp.api;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.bccv.bangyangapp.common.GlobalConstants;
import com.bccv.bangyangapp.model.AnswerListResBean;
import com.bccv.bangyangapp.model.AppDetailsBean;
import com.bccv.bangyangapp.model.CategoryAppListResBean;
import com.bccv.bangyangapp.model.CategoryResBean;
import com.bccv.bangyangapp.model.CollectListResBean;
import com.bccv.bangyangapp.model.CommentInfoResBean;
import com.bccv.bangyangapp.model.HomeResBean;
import com.bccv.bangyangapp.model.HotFreshListResBean;
import com.bccv.bangyangapp.model.LoginResBean;
import com.bccv.bangyangapp.model.MoreDiggiconResBean;
import com.bccv.bangyangapp.model.QuestionInfoListResBean;
import com.bccv.bangyangapp.model.Search4ShareListResBean;
import com.bccv.bangyangapp.model.SearchAppListResBean;
import com.bccv.bangyangapp.model.SearchRecommendListResBean;
import com.bccv.bangyangapp.model.UserAnswer2QListResBean;
import com.bccv.bangyangapp.model.UserComment2appListResBean;
import com.bccv.bangyangapp.model.UserInfoBean;
import com.bccv.bangyangapp.model.VersionBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.HttpReq.ReqestMethod;
import com.bccv.bangyangapp.network.HttpReqImpl;
import com.bccv.bangyangapp.network.HttpService;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.utils.UserInfoUtil;

public class NetWorkAPI {

	public static final String TAG = "NetWorkAPI";

	/**
	 * 获取首页列表
	 * 
	 * @param context
	 * @param callBack
	 *            回调
	 */
	public static void app_index(Context context, HttpCallback callBack) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_index";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new HomeResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取分类列表
	 * 
	 * @param context
	 * @param callBack
	 *            回调
	 */
	public static void app_nav(Context context, HttpCallback callBack) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_nav";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new CategoryResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取指定分类app列表
	 * 
	 * @param context
	 * @param id
	 *            分类id
	 * @param p
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void app_cat(Context context, String id, int p,
			HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_cat";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new CategoryAppListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取最新列表
	 * 
	 * @param context
	 * @param p
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void app_new(Context context, int p, HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_new";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new HotFreshListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取最热列表
	 * 
	 * @param context
	 * @param p
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void app_hot(Context context, int p, HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_hot";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new HotFreshListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取疑问列表
	 * 
	 * @param context
	 * @param p
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void app_question(Context context, int p,
			HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_question";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new QuestionInfoListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取收藏列表
	 * 
	 * @param context
	 * @param p
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void user_like(Context context, int p, HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/user_like";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new CollectListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 获取本地收藏列表
	 * 
	 * @param context
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void getlocallist(Context context,List<String> appIds,HttpCallback callBack) {
		
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < appIds.size(); i++) {
			stringBuilder.append(appIds.get(i));
			stringBuilder.append(",");
		}
		if(stringBuilder.length()>0){
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("app_ids", stringBuilder.toString());
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/getlocallist";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new CollectListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取疑问回答列表
	 * 
	 * @param context
	 * @param p
	 *            请求页下标
	 * @param id
	 *            问题id
	 * @param callBack
	 *            回调
	 */
	public static void app_answer(Context context, int p, String id,
			HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_answer";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new AnswerListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取用户评论app列表
	 * 
	 * @param context
	 * @param p
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void user_comment(Context context, int p,
			HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/user_comment";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new UserComment2appListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取用户回复问题列表
	 * 
	 * @param context
	 * @param p
	 *            请求页下标
	 * @param callBack
	 *            回调
	 */
	public static void user_answer(Context context, int p, HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", String.valueOf(p));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/user_answer";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new UserAnswer2QListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取应用详情
	 * 
	 * @param context
	 * @param callback
	 */
	public static void getAppDetails(Context context, int id, int p,int num,
			HttpCallback callback) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", String.valueOf(p));
		params.put("id", String.valueOf(id));
		params.put("num", String.valueOf(num));
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/app_info";
		HttpReqImpl reqImpl = new HttpReqImpl(context, url, ReqestMethod.GET,
				callback);
		reqImpl.setParams(params);
		reqImpl.setReportBean(new AppDetailsBean());
		HttpService.getInstance().addImmediateReq(reqImpl);
	}

	/**
	 * 意见反馈
	 * 
	 * @param context
	 * @param message
	 *            内容
	 * @param callback
	 */
	public static void feedback(Context context, String message,
			HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("message", message);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/feedback";
		HttpReqImpl reportReq = new HttpReqImpl(context, url,
				ReqestMethod.POST, callback);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 提交疑问
	 * 
	 * @param context
	 * @param message
	 *            内容
	 * @param callback
	 */
	public static void question(Context context, String message,
			HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("question", message);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/question";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callback);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 回答疑问
	 * 
	 * @param context
	 * @param message
	 *            内容
	 * @param callback
	 */
	public static void answer(Context context, String answer,
			String question_id, HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("question_id", question_id);
		params.put("answer", answer);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/answer";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callback);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取搜索推荐列表
	 * 
	 * @param context
	 * @param callback
	 */
	public static void search_list(Context context, HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/search_list";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callback);
		reportReq.setParams(params);
		reportReq.setReportBean(new SearchRecommendListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 获取搜索推荐列表
	 * 
	 * @param context
	 * @param callback
	 */
	public static void search(Context context, String keyword,
			HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("keyword", keyword);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/search";
		HttpReqImpl reportReq = new HttpReqImpl(context, url,
				ReqestMethod.POST, callback);
		reportReq.setParams(params);
		reportReq.setReportBean(new SearchAppListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 更改用户信息
	 * 
	 * @param context
	 * @param callback
	 */
	public static void updateUserInfo(Context context, String name,String introduce,
			String pic_type, Bitmap headpic,HttpCallback callback) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", name);
		params.put("introduce", introduce);
		params.put("pic_type", pic_type);
		params = addUserId(params);
		
		if (headpic != null) {
			String headPicString = UserInfoUtil.getPicString(headpic);
			params.put("pic", headPicString);
		}else {
			params.put("pic", "");
		}

		String url = GlobalConstants.HOST + "/updateuserinfo";
		HttpReqImpl reqImpl = new HttpReqImpl(context, url, ReqestMethod.POST,
				callback);
		reqImpl.setParams(params);
		reqImpl.setReportBean(new LoginResBean());
		HttpService.getInstance().addImmediateReq(reqImpl);
	}

	/**
	 * 获取用户分享的应用列表
	 * 
	 * @param context
	 * @param user_ids
	 * @param p
	 * @param num
	 * @param callback
	 */
	public static void getUserSharedApp(Context context, String user_ids, int p,
			int num, HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", p);
		params.put("num", num);
		params.put("user_ids", user_ids);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/user_share";
		HttpReqImpl reqImpl = new HttpReqImpl(context, url, ReqestMethod.GET,
				callback);
		reqImpl.setParams(params);
		reqImpl.setReportBean(new CollectListResBean());
		HttpService.getInstance().addImmediateReq(reqImpl);
	}

	/**
	 * 获取用户疑问列表
	 * 
	 * @param context
	 * @param user_ids
	 * @param p
	 * @param num
	 * @param callback
	 */
	public static void getUserQuestion(Context context, String user_ids, int p,
			int num, HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", p);
		params.put("num", num);
		params.put("user_ids", user_ids);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/user_question";
		HttpReqImpl reqImpl = new HttpReqImpl(context, url, ReqestMethod.GET,
				callback);
		reqImpl.setParams(params);
		reqImpl.setReportBean(new QuestionInfoListResBean());
		HttpService.getInstance().addImmediateReq(reqImpl);
	}

	/**
	 * 获取搜索推荐列表
	 * 
	 * @param context
	 * @param callback
	 */
	public static void search4share(Context context, String keyword,
			HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("app_name", keyword);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/add_app2";
		HttpReqImpl reportReq = new HttpReqImpl(context, url,
				ReqestMethod.POST, callback);
		reportReq.setParams(params);
		reportReq.setReportBean(new Search4ShareListResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 查看某条评论
	 * 
	 * @param context
	 * @param comment_id
	 *            评论id
	 * @param callback
	 */
	public static void find_comment(Context context, String comment_id,
			HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("comment_id", comment_id);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/find_comment";
		HttpReqImpl reportReq = new HttpReqImpl(context, url,
				ReqestMethod.GET, callback);
		reportReq.setParams(params);
		reportReq.setReportBean(new CommentInfoResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 用户登陆
	 * 
	 * @param context
	 * @param user_name
	 *            用户名
	 * @param user_icon
	 *            用户头像地址
	 * @param user_openid
	 *            用户openid
	 * @param user_token
	 *            用户token
	 * @param user_type
	 *            用户类型
	 */
	public static void login(Context context, String user_name, String icon,
			String openid, UserType type, HttpCallback callBack) {

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", user_name);
		params.put("icon", icon);
		params.put("openid", openid);
		params.put("type", type.name());
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/login";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new LoginResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	public enum UserType {
		QQ, TENCENT, SINA, WEIXIN, UNKNOW
	}

	/**
	 * 发布应用的接口
	 * 
	 * @param context
	 * @param itunes_id
	 * @param country
	 * @param comment
	 * @param callBack
	 */
	public static void appRelease(Context context, String itunes_id,
			String country, String comment, HttpCallback callBack) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("itunes_id", itunes_id);
		params.put("country", country);
		params.put("comment", comment);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/add_app1";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST,
				callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(null);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 用户顶评论
	 * @param context
	 * @param comment_id
	 * @param callback
	 */
	public static void digg(Context context ,String comment_id ,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("comment_id", comment_id);
		params = addUserId(params);

		String url = GlobalConstants.HOST + "/digg";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(null);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	/**
	 * 用户顶应用
	 * @param context
	 * @param comment_id
	 * @param callback
	 */
	public static void digg_app(Context context ,String app_id ,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("app_id", app_id);
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/digg_app";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(null);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 用户收藏应用
	 * @param context
	 * @param app_id
	 * @param callback
	 */
	public static void like(Context context ,String app_id ,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("app_id", app_id);
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/like";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(null);
		HttpService.getInstance().addImmediateReq(reportReq);
	}

	/**
	 * 用户顶疑问回答
	 * @param context
	 * @param answer_id
	 * @param callback
	 */
	public static void digg_answer(Context context ,String answer_id ,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("answer_id", answer_id);
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/digg_answer";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(null);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 用户评论应用
	 * app_id-------appid  
	 * comment------回复内容  
     * reply--------是否是回复 如果是回复 则添加所回复的评论ID  如果不是回复 则为空
	 */
	public static void comment(Context context,String app_id,String comment,String reply,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("app_id", app_id);
		params.put("comment", comment);
		params.put("reply", reply);
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/comment";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(null);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 用户分享action上报
	 * app_id-------appid 
	 * other_id------所分享的第三方ID   1-新浪  2-朋友圈  3-QQ  4-微信
	 */
	public static void comment(Context context,String app_id,String other_id,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("app_id", app_id);
		params.put("other_id", other_id);
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/share";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(null);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 获取版本号
	 * @param context
	 * @param callback
	 */
	public static void getVersion(Context context,HttpCallback callback) {
		//http://appsapi.bccv.com/an/pretty/getversion
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/getversion";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(new VersionBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 获取赞列表
	 * @param p				当前页数
	 * @param comment_id	评论id
	 * @param context
	 * @param callback
	 */
	public static void more_diggicon(Context context,int p,String comment_id,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("p", p);
		params.put("comment_id", comment_id);
		params = addUserId(params);
		
		String url = GlobalConstants.HOST + "/more_diggicon";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(new MoreDiggiconResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 根据userid获取用户信息
	 * @param context
	 * @param user_ids
	 * @param callback
	 */
	public static void getUserInfo(Context context,String user_ids,HttpCallback callback) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_ids", user_ids);
		
		String url = GlobalConstants.HOST + "/get_userinfo";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.GET,	callback);
		reportReq.setParams(params);
		reportReq.setReportBean(new UserInfoBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 添加用户信息
	 * 
	 * @param params
	 * @return
	 */
	public static HashMap<String, Object> addUserId(
			HashMap<String, Object> params) {
		// 如果用户登陆 则传入user_id
		if (UserInfoManager.isLogin()) {
			params.put("user_id", String.valueOf(UserInfoManager.getUserId()));
//			params.put("user_id", "1");
		}
		
		return params;
	}

}