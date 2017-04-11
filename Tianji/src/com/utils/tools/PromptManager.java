package com.utils.tools;



import java.util.logging.Logger;

import com.bccv.tianji.R;
import com.utils.tools.BaseActivity.DataAsyncTask;

import junit.runner.Version;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;




/**
 * 提示信息的管理
 */

public class PromptManager {
	private static AlertDialog dialog;
	private static AlertDialog cancelAlertDialog;
	
	@SuppressLint("NewApi")
	public static void showProgressDialog(Context context) {
		LinearLayout view = (LinearLayout) View.inflate(context, R.layout.dialog_progress, null);
		
		dialog = new AlertDialog.Builder(
				context).create();
//		dialog.setView(view, 0, 0, 0, 0);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_progress);
		window.setDimAmount(0.3f);
		ImageView loadingImageView = (ImageView) window.findViewById(R.id.loading_imageView);
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);  
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin); 
		loadingImageView.startAnimation(operatingAnim);
	}

	@SuppressLint("NewApi")
	public static void showCancelProgressDialog(Context context, final DataAsyncTask dataAsyncTask) {
		LinearLayout view = (LinearLayout) View.inflate(context, R.layout.dialog_progress, null);
		
		cancelAlertDialog = new AlertDialog.Builder(
				context).create();
//		dialog.setView(view, 0, 0, 0, 0);
		cancelAlertDialog.setCanceledOnTouchOutside(false);
		cancelAlertDialog.show();
		
		Window window = cancelAlertDialog.getWindow();
		window.setContentView(R.layout.dialog_progress);
		window.setDimAmount(0.3f);
		
		ImageView loadingImageView = (ImageView) window.findViewById(R.id.loading_imageView);
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);  
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin); 
		loadingImageView.startAnimation(operatingAnim);
		
		cancelAlertDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				dataAsyncTask.cancel(true);
			}
		});
	}
	
	public static void closeCancelProgressDialog() {
		if (cancelAlertDialog != null && cancelAlertDialog.isShowing()) {
			cancelAlertDialog.dismiss();
			cancelAlertDialog = null;
		}
	}
	
	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	
	
	/**
	 * 当判断当前手机没有网络时使用
	 * 
	 * @param context
	 */
	public static void showNoNetWork(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage("当前无网络,点击设置前往网络设置界面。").setPositiveButton("设置", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 跳转到系统的网络设置界面
						Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						context.startActivity(intent);

					}
				}).setNegativeButton("知道了", null).show();
	}

	/**
	 * 退出系统
	 * 
	 * @param context
	 */
	public static void showExitSystem(Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage("是否退出应用").setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						android.os.Process.killProcess(android.os.Process.myPid());
						// 多个Activity
						// 将所有用到的Activity都存起来，获取全部，干掉
					}
				}).setNegativeButton("取消", null).show();

	}

	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String msg) {
		new AlertDialog.Builder(context)//
				.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage(msg)//
				.setNegativeButton("确定", null)//
				.show();
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showToast(Context context, int msgResId) {
		Toast.makeText(context, msgResId, Toast.LENGTH_SHORT).show();
	}

	private static final boolean isShow = true;

	/**
	 * 测试用
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastTest(Context context, String msg) {
		if (isShow) {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 
	 * 提示对话框
	 * 
	 * @param promptMsg
	 *            提示信息。
	 * @param isToMain
	 *            是否处理确认键跳转主界面。
	 *//*
	public static void showAlertDialog(String promptMsg, final Activity activity) {
		builder = new AlertDialog.Builder(activity);
		builder.setIcon(R.drawable.icon);
		builder.setTitle(R.string.app_name);
		builder.setMessage(promptMsg);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(activity, SlidingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				GlobalParams.context.startActivity(intent);
				activity.finish();
				activity.overridePendingTransition(anim.slide_in_left, anim.slide_out_right);
			}
		}).setNegativeButton("取消", null);
		builder.show();
	}*/

	/**
	 * 解析升级信息 显示升级对话框
	 * 
	 * @param data		升级信息
	 * @param context	
	 */
//	public static void showUpdateDialog(Version data,final Context context, final Activity activity){
//		
//			if (data != null) {
//				final int new_version = data.getVersion_code();
//				final String downloadUrl = data.getDownload();
//				final String version_name = data.getVersion();
//				final String desString = data.getDesString();
//				int versionCode = Statistics.getPackageInfo().versionCode;
//					if (versionCode > 0 && new_version > 0) {
//							if (versionCode < new_version) {
//							//此处直接new一个Dialog对象出来，在实例化的时候传入主题
//							final Dialog dialog = new Dialog(activity, R.style.MyDialog);
//							//设置它的ContentView
//							View view = LayoutInflater.from(context).inflate(R.layout.custome_dialog, null);
//							TextView tv = (TextView) view.findViewById(R.id.dialog_message);
//							TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
//							TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
//							dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									dialog.cancel();
//									if (downloadUrl != null && !downloadUrl.equals("")) {
//										DownLoadAPI.downLoadApk(downloadUrl, activity.getApplicationContext().getResources().getString(R.string.app_name), 
//												activity.getApplicationContext().getPackageName(), new_version, true, true);
//									}else{
//										Logger.e("showUpdateDialog", " 下载路径出错  downloadUrl : " + downloadUrl);
//									}
//									if (GlobalParams.isLoadingActivity) {
//										((LoadingActivity)activity).goMainActivity();
//									}
//								}
//							});
//							dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									if (GlobalParams.isLoadingActivity) {
//										((LoadingActivity)activity).goMainActivity();
//									}
//									dialog.cancel();
//								}
//							});
//							tv.setText("新版本: "+version_name + "\n" + desString);
//							view.setMinimumWidth(600);
//							dialog.setContentView(view);
//							dialog.show();
//					}else{
//						Logger.v("showUpdateDialog", " 当前是最新版本 ");
//					}
//				}else{
//					Logger.v("showUpdateDialog", " new_version : " + new_version);
//				}
//			}
//	}
	
}
