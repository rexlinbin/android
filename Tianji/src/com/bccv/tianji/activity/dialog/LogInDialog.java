package com.bccv.tianji.activity.dialog;

import com.bccv.tianji.R;
import com.bccv.tianji.activity.MainActivity;
import com.bccv.tianji.activity.RegisterPhoneActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LogInDialog {
	Context context;
	Dialog dialog;

	Button loginBtn;
	EditText userEdit, passWordit;
	Dialogcallback dialogcallback;
ImageView clear;
	@SuppressLint("NewApi")
	public LogInDialog(Context con) {

		this.context = con;

		dialog = new Dialog(context, R.style.logdialog);
		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_login);
		window.setDimAmount(0.3f);
		// dialog.setContentView(R.layout.dialog_login);

		userEdit = (EditText) window.findViewById(R.id.login_tellEdit);
		passWordit = (EditText) window.findViewById(R.id.login_passWordEdit);
TextView textPass=(TextView) window.findViewById(R.id.fogetPW);
		loginBtn = (Button) window.findViewById(R.id.login_btn);
		clear=(ImageView) window.findViewById(R.id.login_clear);
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userEdit.setText("");
			}
		});
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialogcallback.dialogdo(getUserText(), getPassWordText());
				dismiss();
			}
		});
		textPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(context,
						RegisterPhoneActivity.class);
				aIntent.putExtra("isReset", true);

				context.startActivity(aIntent);
			}
		});
	}

	public String getUserText() {
		return userEdit.getText().toString();
	}

	public String getPassWordText() {
		return passWordit.getText().toString();
	}

	public interface Dialogcallback {
		public void dialogdo(String UserName, String PassWord);
	}

	public void setDialogCallback(Dialogcallback dialogcallback) {
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

}
