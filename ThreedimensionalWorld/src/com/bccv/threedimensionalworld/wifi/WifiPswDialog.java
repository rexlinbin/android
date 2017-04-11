package com.bccv.threedimensionalworld.wifi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.view.MyKeyBoard;
import com.bccv.threedimensionalworld.view.MyKeyBoard.KeyBoardListener;

public class WifiPswDialog {
	private static Button cancelButton, cancelButton1;
	private static Button okButton, okButton1;
	private static Button pswEdit, pswEdit1;
	private static OnCustomDialogListener customDialogListener;
	private static AlertDialog dialog, dialog1;
	private static MyKeyBoard leftMyKeyBoard;
	private static MyKeyBoard rightMyKeyBoard;
	private static LinearLayout linear, linear1;

	@SuppressLint("NewApi")
	public static void showDialog(Context context,
			OnCustomDialogListener customListener) {
		// OnCancelListener cancelListener) {

		customDialogListener = customListener;

		// dialog = new AlertDialog.Builder(context).create();
		// dialog.setCanceledOnTouchOutside(true);
		// dialog.setCancelable(true);
		// dialog.show();

		// Log.e("type", type+"");

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();

		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setContentView(R.layout.wifi_config_dialog);
		// window.setDimAmount(0.8f);
		window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		// window.setGravity(Gravity.TOP);

		LayoutParams params = new LayoutParams();

		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 1); // 高度设置为屏幕的0.6
		lp.height = (int) (d.widthPixels * 0.5);

		window.setAttributes(lp);

		pswEdit = (Button) window.findViewById(R.id.wifiDialogPsw);
		pswEdit1 = (Button) window.findViewById(R.id.wifiDialogPsw1);
		linear = (LinearLayout) window.findViewById(R.id.dialog_linear);
		linear1 = (LinearLayout) window.findViewById(R.id.dialog_linear1);
		linear.setScaleX(0.5f);
		linear1.setScaleX(0.5f);
		cancelButton = (Button) window.findViewById(R.id.wifiDialogCancel);
		okButton = (Button) window.findViewById(R.id.wifiDialogCertain);
		cancelButton.setOnClickListener(buttonDialogListener);
		okButton.setOnClickListener(buttonDialogListener);

		cancelButton1 = (Button) window.findViewById(R.id.wifiDialogCancel1);
		okButton1 = (Button) window.findViewById(R.id.wifiDialogCertain1);
		cancelButton1.setOnClickListener(buttonDialogListener);
		okButton1.setOnClickListener(buttonDialogListener);

		pswEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {

					pswEdit1.setSelected(true);

					pswEdit.setSelected(true);

				} else {
					pswEdit1.setSelected(false);

					pswEdit.setSelected(false);

				}
			}
		});
		pswEdit1.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {

					pswEdit1.setSelected(true);

					pswEdit.setSelected(true);

				} else {
					pswEdit1.setSelected(false);

					pswEdit.setSelected(false);

				}
			}
		});
		okButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {

					okButton1.setSelected(true);

					okButton.setSelected(true);

				} else {
					okButton1.setSelected(false);

					okButton.setSelected(false);

				}
			}
		});
		okButton1.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {

					okButton1.setSelected(true);

					okButton.setSelected(true);

				} else {
					okButton1.setSelected(false);

					okButton.setSelected(false);

				}
			}
		});

		cancelButton1.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1) {

					cancelButton1.setSelected(true);

					cancelButton.setSelected(true);

				} else {
					cancelButton1.setSelected(false);

					cancelButton.setSelected(false);

				}
			}
		});
		
		cancelButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1) {

					cancelButton1.setSelected(true);

					cancelButton.setSelected(true);

				} else {
					cancelButton1.setSelected(false);

					cancelButton.setSelected(false);

				}
			}
		});

		leftMyKeyBoard = (MyKeyBoard) window.findViewById(R.id.left_myKeyBoard);
		rightMyKeyBoard = (MyKeyBoard) window
				.findViewById(R.id.right_myKeyBoard);
		leftMyKeyBoard.setKeyBoardListener(new KeyBoardListener() {

			@Override
			public void onKey(String text) {
				// TODO Auto-generated method stub
				pswEdit.setText(text);
				pswEdit1.setText(text);
			}

			@Override
			public void hideKeyBoard() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSure() {
				// TODO Auto-generated method stub

			}
		});
		leftMyKeyBoard.setRightButtons(
				rightMyKeyBoard.getRightInputKeyButtons(),
				rightMyKeyBoard.getRightFunctionKeyButtons());
		leftMyKeyBoard.setInputKeyListener();
		leftMyKeyBoard.setFunctionKeyButtons();

	}

	// 定义dialog的回调事件
	public interface OnCustomDialogListener {
		void back(String str);
	}

	private static View.OnClickListener buttonDialogListener = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			if (view.getId() == R.id.wifiDialogCancel
					|| view.getId() == R.id.wifiDialogCancel1) {
				pswEdit = null;
				customDialogListener.back(null);
				dialog.cancel();// 自动调用dismiss();
			} else {
				customDialogListener.back(pswEdit.getText().toString());
				dialog.dismiss();
			}
		}
	};

}
