package com.bccv.meitu.api;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.model.GetAuthorinfoResBean;
import com.bccv.meitu.model.GetSpecialResBean;
import com.bccv.meitu.model.GetSpecialpicResBean;
import com.bccv.meitu.model.GetTaginfoResBean;
import com.bccv.meitu.model.GetTaglistResBean;
import com.bccv.meitu.model.GetlistResBean;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.HttpReq.ReqestMethod;
import com.bccv.meitu.network.HttpReqImpl;
import com.bccv.meitu.network.HttpService;
import com.bccv.meitu.utils.Base64;
import com.bccv.meitu.utils.Des3;

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
	 * 添加用户信息
	 * @param params
	 * @return
	 */
	private static HashMap<String, Object> addUserId(HashMap<String, Object> params){
		//TODO 如果用户登陆  则传入user_id
		
		return params;
	}
	
	
	private static HashMap<String, Object> doDes3(HashMap<String, Object> params){
		
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
	
}
