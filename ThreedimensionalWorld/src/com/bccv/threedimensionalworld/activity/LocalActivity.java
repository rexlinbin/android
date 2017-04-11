package com.bccv.threedimensionalworld.activity;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;

public class LocalActivity extends BaseActivity {

	private Button MusicBtn, VedioBtn, PicBtn, UpanBtn;
	private Button MusicBtn1, VedioBtn1, PicBtn1, UpanBtn1;
	private LinearLayout upanLayout, upanLayout1;
	private String usbString = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local);

		setLeft();
		setRight();
	checkUpan();
	
	}

	private void checkUpan() {
		String path = "/mnt/usb_storage";
		// String path =
		// Environment.getExternalStorageDirectory().getAbsolutePath();
		File folderFile = new File(path);
		File[] files = folderFile.listFiles();
		if (files != null && files.length > 0) {
			File file = files[0];
			usbString = file.getAbsolutePath();
			File[] usbfiles = file.listFiles();

			if (usbfiles != null && usbfiles.length > 0) {
				upanLayout.setVisibility(View.VISIBLE);
				upanLayout1.setVisibility(View.VISIBLE);
			} else {
				upanLayout.setVisibility(View.GONE);
				upanLayout1.setVisibility(View.GONE);
			}
		}
	}

	private void setRight() {
		// TODO Auto-generated method stub
		MusicBtn1 = (Button) findViewById(R.id.local_music1);

		VedioBtn1 = (Button) findViewById(R.id.local_video1);

		PicBtn1 = (Button) findViewById(R.id.local_picture1);

		upanLayout1 = (LinearLayout) findViewById(R.id.upan_layout1);
		UpanBtn1 = (Button) findViewById(R.id.local_Upan1);

		MusicBtn1.setOnFocusChangeListener(new OnMusicChangeListener());

		VedioBtn1.setOnFocusChangeListener(new OnVideoChangeListener());
		PicBtn1.setOnFocusChangeListener(new OnPicChangeListener());
		UpanBtn1.setOnFocusChangeListener(new OnUpanChangeListener());
	}

	private void setLeft() {
		// TODO Auto-generated method stub
		MusicBtn = (Button) findViewById(R.id.local_music);

		VedioBtn = (Button) findViewById(R.id.local_video);

		PicBtn = (Button) findViewById(R.id.local_picture);

		upanLayout = (LinearLayout) findViewById(R.id.upan_layout);
		UpanBtn = (Button) findViewById(R.id.local_Upan);

		MusicBtn.setOnFocusChangeListener(new OnMusicChangeListener());

		VedioBtn.setOnFocusChangeListener(new OnVideoChangeListener());
		PicBtn.setOnFocusChangeListener(new OnPicChangeListener());
		UpanBtn.setOnFocusChangeListener(new OnUpanChangeListener());
	}

	class OnMusicChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				MusicBtn.setSelected(true);

				MusicBtn1.setSelected(true);

				LayoutParams params = (LayoutParams) MusicBtn.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				MusicBtn.setLayoutParams(params);

			} else {
				MusicBtn.setSelected(false);

				MusicBtn1.setSelected(false);

				LayoutParams params = (LayoutParams) MusicBtn.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				MusicBtn.setLayoutParams(params);

			}

		}

	}

	class OnVideoChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) VedioBtn.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				VedioBtn.setLayoutParams(params);

				VedioBtn.setSelected(true);

				VedioBtn1.setSelected(true);

			} else {
				VedioBtn.setSelected(false);

				VedioBtn1.setSelected(false);

				LayoutParams params = (LayoutParams) VedioBtn.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				VedioBtn.setLayoutParams(params);

			}

		}

	}

	class OnPicChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) PicBtn.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				PicBtn.setLayoutParams(params);
				PicBtn.setSelected(true);

				PicBtn1.setSelected(true);

			} else {
				PicBtn.setSelected(false);

				PicBtn1.setSelected(false);

				LayoutParams params = (LayoutParams) PicBtn.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				PicBtn.setLayoutParams(params);
			}

		}

	}

	class OnUpanChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) UpanBtn.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				UpanBtn.setLayoutParams(params);
				UpanBtn.setSelected(true);

				UpanBtn1.setSelected(true);

			} else {
				UpanBtn.setSelected(false);

				UpanBtn1.setSelected(false);

				LayoutParams params = (LayoutParams) UpanBtn.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				UpanBtn.setLayoutParams(params);
			}

		}

	}

	public void onVideoClick(View view) {

		Intent aIntent = new Intent(LocalActivity.this,
				LocalVideoActivity.class);
		startActivity(aIntent);

	}

	public void onMusicClick(View view) {

		Intent aIntent = new Intent(LocalActivity.this,
				LocalMusicActivity.class);
		startActivity(aIntent);

	}

	public void onPicClick(View view) {

		Intent aIntent = new Intent(LocalActivity.this, ImageActivity.class);
		startActivity(aIntent);

	}

	public void onUpanClick(View view) {
		Intent aIntent = new Intent(LocalActivity.this, UsbActivity.class);
		aIntent.putExtra("usbPath", usbString);
		startActivity(aIntent);

	}

}
