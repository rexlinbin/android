package com.bccv.ebook.utils;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.ebook.api.DownLoadAPI;
import com.bccv.ebook.model.VersionInfo;
import com.boxuu.ebookjy.R;

public class EBookUtil {

	private final static String TAG = "YuseUtil";
	
	/**
	 * 解析升级信息 显示升级对话框
	 * 
	 * @param data		升级信息
	 * @param context	
	 */
	public static void showUpdateDialog(VersionInfo data,final Activity activity,boolean isAlert){
		if (data != null) {
			if (data.isHasNewVer()) {
				final String downloadUrl = data.getApp_url();
				final int new_version = data.getApp_upver();
				String version_name = data.getApp_ver();
				String version_desS = data.getApp_des();
				int versionCode = SystemUtil.getVersionCode(activity.getApplicationContext());
				if (versionCode > 0 && new_version > 0) {
					if (versionCode < new_version) {
						//此处直接new一个Dialog对象出来，在实例化的时候传入主题
						final Dialog dialog = new Dialog(activity, R.style.MyDialog);
						//设置它的ContentView
						View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.custome_dialog, null);
						TextView des = (TextView) view.findViewById(R.id.description);
						TextView tv = (TextView) view.findViewById(R.id.dialog_message);
						TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
						TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
						dialog_enter.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.cancel();
								if (downloadUrl != null && !downloadUrl.equals("")) {
									DownLoadAPI.downLoadApk(downloadUrl, activity.getApplicationContext().getResources().getString(R.string.app_name), 
											activity.getApplicationContext().getPackageName(), new_version, true, true);
									Toast.makeText(activity.getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
								}else{
									Toast.makeText(activity.getApplicationContext(),"下载路径出错", Toast.LENGTH_SHORT).show();
								}
							}
						});
						dialog_cancle.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.cancel();
							}
						});
						tv.setText("悦色提示: 新版本: "+version_name);
						des.setText(Html.fromHtml(version_desS));
						view.setMinimumWidth(600);
						dialog.setContentView(view);
						dialog.show();
					}else if(isAlert){
						Toast.makeText(activity.getApplicationContext(),"当前是最新版本", Toast.LENGTH_SHORT).show();
					}
				}else if(isAlert){
					Toast.makeText(activity.getApplicationContext(),"获取版本失败", Toast.LENGTH_SHORT).show();
				}
			}else if(isAlert){
				Toast.makeText(activity.getApplicationContext(),"当前是最新版本", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
}
