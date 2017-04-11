package com.bccv.meitu.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bccv.meitu.R;

public class CommonDialog implements OnClickListener  {
	
	private static final String TAG = "UpdateDialog";
	
	private Dialog dialog;
	
	private View root ;
	private TextView dialog_title;
	private TextView remind_message;
	private TextView affirm_worlds;
	private TextView dialog_cancle;
	private TextView dialog_enter;
	
	private onDialogBtnClickListener listener;

	public CommonDialog(Activity actvity){
		dialog = new Dialog(actvity, R.style.MyDialog);
		root = View.inflate(actvity.getApplicationContext(),R.layout.common_dialog, null);
		dialog_title = (TextView) root.findViewById(R.id.dialog_title);
		remind_message = (TextView) root.findViewById(R.id.remind_message);
		affirm_worlds = (TextView) root.findViewById(R.id.affirm_worlds);
		dialog_cancle = (TextView) root.findViewById(R.id.dialog_cancle);
		dialog_enter = (TextView) root.findViewById(R.id.dialog_enter);
		
		dialog_cancle.setOnClickListener(this);
		dialog_enter.setOnClickListener(this);
		
	}
	
	/**
	 * @param listener		按钮监听
	 * @param dialogTitle	dialog标题
	 * @param remindMessage	dialog提示内容
	 * @param affirmWorlds	dialog确认动作
	 * @param canCancel		是否可以取消
	 */
	public void show(onDialogBtnClickListener listener,String dialogTitle,String remindMessage,String affirmWorlds,boolean canCancel){
		
		this.listener = listener;
		
		dialog_title.setText(dialogTitle);
		remind_message.setText(remindMessage);
		affirm_worlds.setText(affirmWorlds);
		
		dialog.setCanceledOnTouchOutside(canCancel);
		dialog.setCancelable(canCancel);
		
		root.setMinimumWidth(600);
		dialog.setContentView(root);
		
		dialog.show();
	}
	
	public void cancel(){
		dialog.cancel();
	}
	
	public boolean isShowing(){
		return dialog.isShowing();
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.dialog_cancle:
			if(listener!=null){
				listener.onCancelClick();
			}
			break;
		case R.id.dialog_enter:
			if(listener!=null){
				listener.onEnterClick();
			}
			break;

		default:
			break;
		}
	}

	public interface onDialogBtnClickListener{
		void onCancelClick();
		void onEnterClick();
	}
	
}
