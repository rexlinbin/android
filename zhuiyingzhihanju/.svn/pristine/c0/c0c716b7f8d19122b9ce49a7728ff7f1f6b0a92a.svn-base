package com.utils.tools;

import com.bccv.zhuiyingzhihanju.R;
import com.utils.model.UpdateInfo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DialogUtils {
	/**
	 * 解析升级信息 显示升级对话框
	 * 
	 * @param data
	 *            升级信息
	 * @param context
	 */
	public static void showDialog(final Activity activity, String title, String titleString, String message) {

		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		// 设置它的ContentView
		View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.dialog, null);
		TextView tv = (TextView) view.findViewById(R.id.dialog_message);
		TextView titleTextView = (TextView) view.findViewById(R.id.dialog_title);
		TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
		TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
		dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();

			}
		});

		dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.cancel();
			}
		});

		titleTextView.setText(titleString);
		tv.setText(message);
		view.setMinimumWidth(600);
		dialog.setContentView(view);
		dialog.show();

	}
}
