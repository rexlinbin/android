package com.utils.views;

import com.bccv.zhuiying.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
public class ShareDialog {

	
	/**
	 * 分享给朋友
	 * 
	 * @param data
	 *            粘贴信息
	 * @param context
	 */
	public void showUpdateDialog(final String masseage,final String url, final Context context, final Activity activity) {

		if (masseage != null) {
			
			final Dialog dialog = new Dialog(activity, R.style.MyDialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			// 设置它的ContentView
			View view = LayoutInflater.from(context).inflate(R.layout.tofriend_dialog, null);
			TextView tv = (TextView) view.findViewById(R.id.dialog_message);
			TextView urltv = (TextView) view.findViewById(R.id.dialog_url);
			TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
			TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
			dialog_enter.setSelected(true);
		
			dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					
					// 得到剪贴板管理器  
					ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
					cmb.setText(masseage.trim()+url.trim());  	
					
					dialog.cancel();
					
					
					
					
					
					
					
				}
			});
			
			dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
				
					dialog.cancel();
				}
			});
		

			tv.setText(masseage);
			urltv.setText(url);
			view.setMinimumWidth(600);
			dialog.setContentView(view);
			dialog.show();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
