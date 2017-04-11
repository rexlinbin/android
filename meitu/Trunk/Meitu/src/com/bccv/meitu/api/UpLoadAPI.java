package com.bccv.meitu.api;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.localphoto.bean.UpdatePhotoInfo;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.upload.UploadFileNetUtil.UploadListener;
import com.bccv.meitu.upload.UploadFileTask;
import com.bccv.meitu.utils.Des3;
import com.bccv.meitu.utils.Logger;

public class UpLoadAPI {
	private static final String TAG = "UpLoadAPI";
	
	private UploadFileTask currentTask;
	
	private UpLoadAPI(){}
	
	public static UpLoadAPI getInstance(){
		return new UpLoadAPI();
	}
	
	/**
	 * 上传文件
	 * 
	 * @param context
	 * @param file
	 * @param params
	 * @param listener
	 */
	public void upLoadFile(Context context,UpdatePhotoInfo photoInfo,int special_id,UploadListener listener){
		
		HashMap<String, String> params = new HashMap<String,String>();
		params.put("special_id", String.valueOf(special_id));
		if(photoInfo.isCover()){
			params.put("type", "2");
		}else{
			params.put("type", "1");
		}
		params = addUserId(params);
		String url = GlobalConstants.HOST+"/dospecialpic?";
		
		Logger.v(TAG, "upLoadFile", "url : " + url + " params :" + params);
		
		currentTask = new UploadFileTask(context, listener);
		currentTask.dpoExecute(photoInfo, url, params);
	}
	
	/**
	 * 取消当前任务
	 */
	public void cancelCurrentTask(){
		if(currentTask!=null){
			currentTask.cancel(true);
		}
	}
	
	/**
	 * 添加用户信息
	 * @param params
	 * @return
	 */
	public static HashMap<String, String> addUserId(HashMap<String, String> params){
		//TODO 如果用户登陆  则传入user_id
		if(UserInfoManager.isLogin()){
			params.put("user_id", String.valueOf(UserInfoManager.getUserId()));
		}
		return params;
	}
	
	public static HashMap<String, String> doDes3(HashMap<String, String> params){
		
		String encode = "";
		if (params != null && params.size() > 0) {
			String paramString = "{";
			for (Map.Entry<String, String> item : params.entrySet()) {
				paramString += "\""+(item.getKey())+"\":" + "\""+(item.getValue())+"\",";
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
