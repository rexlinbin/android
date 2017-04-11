package com.utils.views;

import com.bccv.zhuiying.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CiDialog {

	/**
	 * 分享给朋友
	 * 
	 * @param data
	 *            粘贴信息
	 * @param context
	 */
	private Cicallback Call;
	public void CiDialog(final String url, final Context context, final Activity activity) {

		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		// 设置它的ContentView
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_ci, null);

		final EditText urltv = (EditText) view.findViewById(R.id.dialog_ciurl);
		TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
		TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
		dialog_enter.setSelected(true);

		dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {

				dialog.cancel();
				Call.dialogdo(true,urltv.getText().toString().trim());
			}
		});

		dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.cancel();
			}
		});

		if (url == null) {
			urltv.setText("");
		} else {
			urltv.setText(url);
		}

		view.setMinimumWidth(600);
		dialog.setContentView(view);
		dialog.show();
	}

	
	
	
	
	
	
	
	
	

	public interface Cicallback {
		public void dialogdo(boolean is,String url);
	}


	public void setDialogCallback(Cicallback dissMissllback) {
		this.Call = dissMissllback;
	}










	
	
	
	
	
	
	
	
}
