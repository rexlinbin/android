package com.bccv.meitu.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bccv.meitu.ApplicationManager;
import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.model.DoSpecialResBean;
import com.bccv.meitu.model.GetAttendedList;
import com.bccv.meitu.model.GetAuthorCommentResBean;
import com.bccv.meitu.model.GetAuthorinfoResBean;
import com.bccv.meitu.model.GetSpecialResBean;
import com.bccv.meitu.model.GetSpecialpicResBean;
import com.bccv.meitu.model.GetTaginfoResBean;
import com.bccv.meitu.model.GetTaglistResBean;
import com.bccv.meitu.model.GetlistResBean;
import com.bccv.meitu.model.GetuserinfoResBean;
import com.bccv.meitu.model.GetuserlistResBean;
import com.bccv.meitu.model.LoginResBean;
import com.bccv.meitu.model.ReportInfoBean;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.model.UpdateuserinfoResBean;
import com.bccv.meitu.model.Version;
import com.bccv.meitu.model.VersionInfo;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.HttpReq.ReqestMethod;
import com.bccv.meitu.network.HttpReqImpl;
import com.bccv.meitu.network.HttpService;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.utils.Base64;
import com.bccv.meitu.utils.Des3;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.utils.UserInfoUtil;

public class NetWorkAPI {
	
	/**
	 *  获取首页专辑列表
	 *  
	 * @param context
	 * @param type 1 最热  2 最新  3 关注
	 * @param page 获取数据的页号
	 * @param callBack 回调
	 */
	public static void getlist(Context context,int type,int page,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", String.valueOf(type));
		params.put("page", String.valueOf(page));
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/getlist?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetlistResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 *  获取专辑详情
	 *  
	 * @param context
	 * @param special_id 专辑ID
	 * @param callBack 回调
	 */
	public static void getSpecial(Context context,int special_id,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(special_id));
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/getspecial?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetSpecialResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 *  获取tag列表
	 *  
	 * @param context
	 * @param callBack 回调
	 */
	public static void getTaglist(Context context,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/gettaglist?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetTaglistResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 *  获取专辑图片
	 *  
	 * @param context
	 * @param special_id	专辑ID
	 * @param callBack 		回调
	 */
	public static void getSpecialpic(Context context,int special_id,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(special_id));
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/getspecialpic?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetSpecialpicResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 *  获取tag详情
	 *  
	 * @param context
	 * @param tag_id	tagID
	 * @param page		请求的页号
	 * @param callBack 回调
	 */
	public static void getTaginfo(Context context,int tag_id,int page,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("tag_id", String.valueOf(tag_id));
		params.put("page", String.valueOf(page));
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/gettaginfo?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetTaginfoResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 *  获取作者空间详情
	 *  
	 * @param context
	 * @param author_id	作者ID
	 * @param page		获取数据的页号
	 * @param callBack 	回调
	 */
	public static void getAuthorinfo(Context context,int author_id,int page,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("author_id", String.valueOf(author_id));
		params.put("page", String.valueOf(page));
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/getauthorinfo?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetAuthorinfoResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
		
	}
	
	/**
	 *  获取作者评论
	 *  
	 * @param context
	 * @param author_id	作者ID
	 * @param page		获取数据的页号
	 * @param callBack 	回调
	 */
	public static void getauthorcomment(Context context,int author_id,int page,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("author_id", String.valueOf(author_id));
		params.put("page", String.valueOf(page));
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/getauthorcomment?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetAuthorCommentResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
		
	}
	
	/**
	 * 添加标签
	 * 
	 * @param context
	 * @param special_id	专辑id
	 * @param tag_name		标签内容
	 * @param callBack
	 */
	public static void addTag(Context context,int special_id, String tag_name, HttpCallback callBack){
		
		if(!TextUtils.isEmpty(tag_name)){
			tag_name = new String( Base64.encode(tag_name.toString().getBytes(), Base64.URL_SAFE | Base64.NO_WRAP));
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(special_id));
		params.put("tag_name", String.valueOf(tag_name));
		params = addUserId(params);
		params = doDes3(params);
		
		String url = GlobalConstants.HOST+"/addtag?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 用户登陆
	 * 
	 * @param context
	 * @param user_name		用户名
	 * @param user_icon		用户头像地址
	 * @param user_openid	用户openid
	 * @param user_token	用户token
	 * @param user_type		用户类型
	 */
	public static void login(Context context, String user_name, String user_icon, String user_openid,
								String user_token, UserType user_type, HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_name", user_name);
		params.put("user_icon", user_icon);
		params.put("user_openid", user_openid);
		params.put("user_token", user_token);
		params.put("user_type", user_type.name());
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/login?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new LoginResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 获取用户信息
	 * @param context
	 * @param callBack
	 */
	public static void getuserinfo(Context context, HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/getuserinfo?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetuserinfoResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	public static void getversion(Context context,HttpCallback callBack) {
		String url = GlobalConstants.HOST+"/getversion";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setReportBean(new Version());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 获取用户关联信息
	 * 
	 * @param context
	 * @param type	 2我喜欢的    5我的专属   7我的专辑
	 * @param page
	 * @param callBack
	 */
	public static void getuserlist(Context context,int type,int page,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", String.valueOf(type));
		params.put("page", String.valueOf(page));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/getuserlist?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetuserlistResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 获取关注的列表
	 * 
	 * @param context
	 * @param page
	 * @param callBack
	 */
	public static void getAttendedList(Context context,int page,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", "6");
		params.put("page", String.valueOf(page));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/getuserlist?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetAttendedList());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
//	/**
//	 * 收藏专辑
//	 * 
//	 * @param context
//	 * @param special_id	专辑id
//	 * @param is_care		1  收藏   2 取消收藏
//	 * @param callBack	
//	 */
//	public static void collect(Context context,int special_id,int is_care,HttpCallback callBack){
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		params.put("special_id", String.valueOf(special_id));
//		params.put("is_care", String.valueOf(is_care));
//		params = addUserId(params);
//		params = doDes3(params);
//		String url = GlobalConstants.HOST+"/alloperate?";
//		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
//		reportReq.setParams(params);
//		HttpService.getInstance().addImmediateReq(reportReq);
//	}
	
	/**
	 * 专辑点赞
	 * 
	 * @param context
	 * @param special_id	专辑id
	 * @param is_zan		1 赞   2 取消赞
	 * @param callBack	
	 */
	public static void zan(Context context,int special_id,int is_zan,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(special_id));
		params.put("is_zan", String.valueOf(is_zan));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/alloperate?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 取消赞
	 * @param context
	 * @param special_id
	 * @param callBack
	 */
	public static void unzan(Context context,List<Special> specials,HttpCallback callBack){
		
		StringBuilder sb =new StringBuilder();
		for (Special special : specials) {
			sb.append(special.getSpecial_id());
			sb.append(",");
		}
		if(sb.length()>1){
			sb.deleteCharAt(sb.length()-1);
		}
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(sb.toString()));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/unzan?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 专辑分享成功上报
	 * 
	 * @param context
	 * @param special_id	专辑id
	 * @param callBack	
	 */
	public static void share(Context context,int special_id,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(special_id));
		params.put("is_share", String.valueOf(1));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/alloperate?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 专辑评论
	 * @param context
	 * @param special_id	专辑id
	 * @param comment		评论内容
	 * @param callBack	
	 */
	public static void comment(Context context,int special_id,List<String> commentList,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(special_id));
		params.put("is_comment", String.valueOf(1));
		ArrayList<String> dataList = new ArrayList<String>();
		
		if(commentList!=null){
			for (int i = 0; i < commentList.size(); i++) {
				String encodeStr = Base64.encodeToString(commentList.get(i).getBytes(), Base64.DEFAULT);
				dataList.add(encodeStr);
			}
		}
		
		JSONArray jsonArray = new JSONArray();
		jsonArray.addAll(dataList);
		String comments = jsonArray.toJSONString();
		
		params = addUserId(params);
		params = doDes3(params);
		params.put("comment", comments);
		String url = GlobalConstants.HOST+"/alloperate?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 关注作者
	 * @param context
	 * @param author_id	作者id
	 * @param type	1 关注  2 取消关注
	 * @param callBack
	 */
	public static void careauthor(Context context,int author_id,int type,HttpCallback callBack){
		
		// 关注作者
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("author_id", String.valueOf(author_id));
		params.put("type", String.valueOf(type));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/careauthor?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
		
	}
	
	/**
	 * 更改用户名
	 * 
	 * @param context
	 * @param new_name
	 * @param callBack
	 */
	public static void updateUserName(Context context,String new_name,HttpCallback callBack){

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("new_name", new_name);
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/updateuserinfo?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new UpdateuserinfoResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 更新头像
	 * 
	 * @param context
	 * @param headpic	头像bitmap		 无则为null
	 * @param bgpic		空间背景bitmap 	无则为null
	 * @param callBack
	 */
	public static void updateUserPic(Context context,Bitmap headpic,Bitmap bgpic,HttpCallback callBack){
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = addUserId(params);
		params = doDes3(params);
		
		if(headpic!=null){
			String headPicString = UserInfoUtil.getPicString(headpic);
			params.put("pic", headPicString);
		}
		if(bgpic!=null){
			String backdropString = UserInfoUtil.getPicString(bgpic);
			params.put("backdrop", backdropString);
		}
		
		String url = GlobalConstants.HOST+"/updateuserinfo?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new UpdateuserinfoResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 取消多个关注
	 * 
	 * @param context
	 * @param author_id
	 * @param callBack
	 */
	public static void nocareauthors(Context context,List<String> author_id,HttpCallback callBack){
	
		StringBuilder sb =new StringBuilder();
		for (String id : author_id) {
			sb.append(id);
			sb.append(",");
		}
		if(sb.length()>1){
			sb.deleteCharAt(sb.length()-1);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("author_id", sb.toString());
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/nocareauthors?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
		
	}
	
	/**
	 * 购买专辑
	 * @param context
	 * @param special_id	专辑id 
	 * @param callBack
	 * 
	 * 返回至 error 说明   8001 购买成功	8002 金币不够	8003 购买失败
	 * 
	 */
	public static void buy(Context context,int special_id,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_id", String.valueOf(special_id));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/buy?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 绑定用户信息
	 * @param context
	 * @param user_openid	被绑定的open_id
	 * @param UserType		账户类型
	 * @param callBack
	 * 
	 * 返回至 error 说明   
	 */
	public static void uniteaccount(Context context,String user_openid,UserType user_type,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_openid", user_openid);
		params.put("user_type", user_type.name());
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/uniteaccount?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 解绑用户信息
	 * @param context
	 * @param user_openid	被绑定的open_id
	 * @param UserType		账户类型
	 * @param callBack
	 * 
	 * 返回至 error 说明   
	 */
	public static void offuniteaccount(Context context,UserType user_type,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_type", user_type.name());
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/offuniteaccount?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	

	/**
	 * 获取本地关注作者列表
	 * 
	 * @param context
	 * @param authorIDs
	 * @param callBack
	 */
	public static void getLocalAttentionAuthorList(Context context,List<String> authorIDs,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", "1");
		params.put("stypestr", packageData(authorIDs));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/getlocallist?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetAttendedList());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 获取本地首页关注列表
	 * 
	 * @param context
	 * @param authorIDs
	 * @param callBack
	 */
	public static void getLocalAttentionAlbumList(Context context,List<String> authorIDs,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", "2");
		params.put("stypestr", packageData(authorIDs));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/getlocallist?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetlistResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 获取本地喜欢列表
	 * 
	 * @param context
	 * @param authorIDs
	 * @param callBack
	 */
	public static void getLocalLikeList(Context context,List<String> specialIDs,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", "3");
		params.put("stypestr", packageData(specialIDs));
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/getlocallist?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new GetuserlistResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	/**
	 * 创建相册
	 * 
	 * @param context
	 * @param authorIDs
	 * @param callBack
	 */
	public static void dospecial(Context context,String special_names,HttpCallback callBack){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("special_names", special_names);
		params = addUserId(params);
		params = doDes3(params);
		String url = GlobalConstants.HOST+"/dospecial?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new DoSpecialResBean());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	
	/**
	 * 数据上报
	 * @param context
	 * @param callBack
	 */
	public static void report(Context context,HttpCallback callBack){
		
		ReportInfoBean reportInfoBean = new ReportInfoBean();
		
		PackageInfo packageInfo = SystemUtil.getPackageInfo(ApplicationManager.getGlobalContext());
		
		if(packageInfo!=null){
			reportInfoBean.setApp_name("fe084214a01f940c");
			reportInfoBean.setApp_ver(packageInfo.versionName);
			reportInfoBean.setApp_upver(String.valueOf(packageInfo.versionCode));
			reportInfoBean.setApp_os(SystemUtil.getSDKVersion());
			reportInfoBean.setApp_imei(SystemUtil.getPhoneUdid());
			reportInfoBean.setApp_pname(SystemUtil.getPhoneBrand());
			reportInfoBean.setApp_channel(SystemUtil.getChannelCode());
		
			int currentNetType = SystemUtil.getCurrentNetType(ApplicationManager.getGlobalContext());
			switch (currentNetType) {
			case 1://wifi
				reportInfoBean.setApp_net("wifi");
				break;
			case 2://2g
				reportInfoBean.setApp_net("2g");
				break;
			case 3://3g
				reportInfoBean.setApp_net("3g");
				break;
			case 0://无网络
				//TODO 没有网络  不做上报
				if(callBack!=null){
					callBack.onError(" net is not connected !!! ");
				}
				return;
			default:
				break;
			}
		}
		String jsonString = JSON.toJSONString(reportInfoBean);

		//  上报
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("code", jsonString);
		params = addUserId(params);
		String url = GlobalConstants.REPOST_HOST+"/put?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new VersionInfo());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
	
	/**
	 * 将集合转化为  1,2,3 格式
	 * 
	 * @param dataList
	 * @return
	 */
	private static String packageData(List<String> dataList){
		
		if(dataList == null || dataList.size()==0){
			return "";
		}
		
		StringBuilder sb =new StringBuilder();
		for (String id : dataList) {
			sb.append(id);
			sb.append(",");
		}
		if(sb.length()>1){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	
	/**
	 * 添加用户信息
	 * @param params
	 * @return
	 */
	public static HashMap<String, Object> addUserId(HashMap<String, Object> params){
		// 如果用户登陆  则传入user_id
		if(UserInfoManager.isLogin()){
			params.put("user_id", String.valueOf(UserInfoManager.getUserId()));
		}
		return params;
	}
	
	public static HashMap<String, Object> doDes3(HashMap<String, Object> params){
		
		String encode = "";
		if (params != null && params.size() > 0) {
			String paramString = "{";
			for (Map.Entry<String, Object> item : params.entrySet()) {
				paramString += "\""+((String)item.getKey())+"\":" + "\""+((String)item.getValue())+"\",";
			}
			paramString = paramString.substring(0, paramString.length() - 2) + "\"}";
			try {
				encode = Des3.encode(paramString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			params.clear();
		}
		params.put("secret", encode);
		
		return params;
		
	}
	
	public enum UserType{
		QQ,TENCENT,SINA,WEIXIN,UNKNOW
	}
	
}
