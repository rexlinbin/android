package com.bccv.ebook.api;

import java.util.HashMap;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.alibaba.fastjson.JSON;
import com.bccv.ebook.ApplicationManager;
import com.bccv.ebook.common.GlobalConstants;
import com.bccv.ebook.model.ReportInfoBean;
import com.bccv.ebook.model.VersionInfo;
import com.bccv.ebook.network.HttpCallback;
import com.bccv.ebook.network.HttpReq.ReqestMethod;
import com.bccv.ebook.network.HttpReqImpl;
import com.bccv.ebook.network.HttpService;
import com.bccv.ebook.utils.SystemUtil;

public class NetWorkAPI {

	
	/**
	 * 数据上报
	 * @param context
	 * @param callBack
	 */
	public static void report(Context context,HttpCallback callBack){
		
		ReportInfoBean reportInfoBean = new ReportInfoBean();
		
		PackageInfo packageInfo = SystemUtil.getPackageInfo(ApplicationManager.getGlobalContext());
		
		if(packageInfo!=null){
			reportInfoBean.setApp_name("040190bc6e2b555e");
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
		String url = GlobalConstants.REPOST_HOST+"/put?";
		HttpReqImpl reportReq = new HttpReqImpl(context, url, ReqestMethod.POST, callBack);
		reportReq.setParams(params);
		reportReq.setReportBean(new VersionInfo());
		HttpService.getInstance().addImmediateReq(reportReq);
	}
	
}
