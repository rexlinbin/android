package com.bccv.boxcomic.tool;

import u.aly.di;

import com.bccv.boxcomic.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogUtils {

	@SuppressLint("NewApi")
	public static void showDeleteDialog(Activity activity,
			final OnClickListener onClickListener, String BtnString,
			String titleStrig) {

		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_delete);
		window.setDimAmount(0.6f);
		TextView text = (TextView) window.findViewById(R.id.content_textView);
		text.setText(titleStrig);
		Button sureButton = (Button) window.findViewById(R.id.sure_button);
		sureButton.setText(BtnString);
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickListener.onClick(v);
				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) window.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	@SuppressLint("NewApi")
	public static void showNextDialog(Activity activity,
			final OnClickListener onClickListener) {
		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_delete);
		window.setDimAmount(0.6f);

		TextView textView = (TextView) window
				.findViewById(R.id.content_textView);
		textView.setText("是否进入下一章");

		Button sureButton = (Button) window.findViewById(R.id.sure_button);
		sureButton.setText("确定");
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickListener.onClick(v);
				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) window.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	@SuppressLint("NewApi")
	public static void showPreDialog(Activity activity,
			final OnClickListener onClickListener) {
		final AlertDialog dialog = new AlertDialog.Builder(activity).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.dialog_delete);
		window.setDimAmount(0.6f);

		TextView textView = (TextView) window
				.findViewById(R.id.content_textView);
		textView.setText("是否进入上一章");

		Button sureButton = (Button) window.findViewById(R.id.sure_button);
		sureButton.setText("确定");
		sureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onClickListener.onClick(v);
				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) window.findViewById(R.id.cancel_button);
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	@SuppressLint("NewApi")
	public static void showDeleteDialog(Activity activity,
			final OnClickListener onClickListener) {
		showDeleteDialog(activity, onClickListener, "删除", "是否删除？");
	}
}
