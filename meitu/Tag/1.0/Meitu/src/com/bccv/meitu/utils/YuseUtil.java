package com.bccv.meitu.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.api.DownLoadAPI;
import com.bccv.meitu.model.Version;

public class YuseUtil {

	private final static String TAG = "YuseUtil";
	
	
	/**
	 * 解析升级信息 显示升级对话框
	 * 
	 * @param data		升级信息
	 * @param context	
	 */
	public static void showUpdateDialog(Version data,final Context context){
		
			if (data != null) {
				final int new_version = data.getVersion_code();
				final String downloadUrl = data.getDownload();
				final String version_name = data.getVersion();
				int versionCode = SystemUtil.getVersionCode(context);
					if (versionCode > 0 && new_version > 0) {
							if (versionCode < new_version) {
							//此处直接new一个Dialog对象出来，在实例化的时候传入主题
							final Dialog dialog = new Dialog(ActivityQueue.popIndex(ActivityQueue.getSize()-1), R.style.MyDialog);
							//设置它的ContentView
							View view = LayoutInflater.from(context).inflate(R.layout.custome_dialog, null);
							TextView tv = (TextView) view.findViewById(R.id.dialog_message);
							TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
							TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
							dialog_enter.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.cancel();
									if (downloadUrl != null && !downloadUrl.equals("")) {
										DownLoadAPI.downLoadApk(downloadUrl, context.getResources().getString(R.string.app_name), 
												context.getPackageName(), new_version, true, true);
									}else{
										Logger.e(TAG, "showUpdateDialog", " 下载路径出错  downloadUrl : " + downloadUrl);
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
							view.setMinimumWidth(600);
							dialog.setContentView(view);
							dialog.show();
					}else{
						Logger.v(TAG, "showUpdateDialog", " 当前是最新版本 ");
					}
				}else{
					Logger.v(TAG, "showUpdateDialog", " new_version : " + new_version);
				}
			}
	}
	
}
