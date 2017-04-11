package com.utils.views;

import com.bccv.zhuiyingzhihanju.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FeedBackDialog {

	
	Context context;
	EditText edit;
	Button sureBtn;
	
	
	Dialog dialog;
	
	FeedDialogcallback dialogcallback;
	
	
	
	
	
	public interface FeedDialogcallback{
		
		
		public void BackLog(String text);
		
		
	}





	public FeedDialogcallback getDialogcallback() {
		return dialogcallback;
	}





	public void setDialogcallback(FeedDialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}





	public FeedBackDialog(Context context) {
		super();
		this.context = context;
		
		
		dialog=new Dialog(context,R.style.MyDialog);
		dialog.show();
		
		dialog.setContentView(R.layout.dialog_feedback);
		
		
		edit=(EditText) dialog.findViewById(R.id.Feedback_edit);
		
		
		
		sureBtn=(Button) dialog.findViewById(R.id.Feedback_btn);
		
		
		sureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				dialogcallback.BackLog(edit.getText().toString().trim());
				
			}
		});
		
	}
	
	
	
	
	
	
	
	
}
