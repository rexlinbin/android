package com.bccv.tianji.activity.dialog;

import com.bccv.tianji.R;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class ReplyDialog {
	
	Context context;
	Dialog dialog;
	 RatingBar replyStar;
	 
	EditText edit;
	Button cancleBtn,sureBtn;
	ReplayDialogcallback dialogcallback;
	int point=0;
	public boolean isReply;
	public String commentId;
	public boolean isReply() {
		return isReply;
	}

	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}

	public ReplyDialog(Context context) {
		super();
		this.context = context;
		
		dialog=new Dialog(context, R.style.dialog);
		
		dialog.setContentView(R.layout.dialog_replystar);
		
		
		edit=(EditText) dialog.findViewById(R.id.reply_edit);
		cancleBtn=(Button) dialog.findViewById(R.id.reply_cancleBtn);
		sureBtn=(Button) dialog.findViewById(R.id.reply_sureBtn);
		
		
		replyStar=(RatingBar) dialog.findViewById(R.id.reply_star);
		
		sureBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialogcallback.dialogdo(edit.getText().toString().trim(),point, isReply,getCommentId());
				dismiss();
			}
		});
		cancleBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		replyStar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar arg0, float arg1, boolean arg2) {
				// TODO Auto-generated method stub
				point=(int) arg1;
				
			
			}
		});
	}
	
	public interface ReplayDialogcallback {
		public void dialogdo(String replyContent,int arg1,boolean isReply,String commentID);
	}

	public void setDialogCallback(ReplayDialogcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}
	
	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}

	public void dismiss() {
		dialog.dismiss();

	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	
	

}
