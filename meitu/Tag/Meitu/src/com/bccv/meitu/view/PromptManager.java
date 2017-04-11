package com.bccv.meitu.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bccv.meitu.R;


/**
 * 提示信息的管理
 */

public class PromptManager {
	private static AlertDialog dialog;
	
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
	
	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
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



}
