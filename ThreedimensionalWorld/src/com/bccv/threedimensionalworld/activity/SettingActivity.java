package com.bccv.threedimensionalworld.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.RelativeLayout.LayoutParams;

import com.bccv.threedimensionalworld.R;

public class SettingActivity extends Activity {

	private Button setWifi, setVolume, setAbout;

	private Button setWifi1, setVolume1, setAbout1;
	private Button setBlue1, setBlue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		setLeft();
		setRight();

	}

	private void setRight() {
		// TODO Auto-generated method stub

		setBlue1 = (Button) findViewById(R.id.set_blueBtn1);
		setWifi1 = (Button) findViewById(R.id.set_wifiBtn1);
		setVolume1 = (Button) findViewById(R.id.set_volumeBtn1);
		setAbout1 = (Button) findViewById(R.id.set_AboutBtn1);
		setBlue1.setOnFocusChangeListener(new OnBlueChangeListener());
		setWifi1.setOnFocusChangeListener(new OnWifiChangeListener());
		setVolume1.setOnFocusChangeListener(new OnVolumChangeListener());
		setAbout1.setOnFocusChangeListener(new OnAboutChangeListener());
	}

	private void setLeft() {
		// TODO Auto-generated method stub

		setBlue = (Button) findViewById(R.id.set_blueBtn);
		setWifi = (Button) findViewById(R.id.set_wifiBtn);
		setVolume = (Button) findViewById(R.id.set_volumeBtn);
		setAbout = (Button) findViewById(R.id.set_AboutBtn);

		setBlue.setOnFocusChangeListener(new OnBlueChangeListener());
		setWifi.setOnFocusChangeListener(new OnWifiChangeListener());
		setVolume.setOnFocusChangeListener(new OnVolumChangeListener());
		setAbout.setOnFocusChangeListener(new OnAboutChangeListener());
	}

	class OnBlueChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {
				LayoutParams params = (LayoutParams) setBlue.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				setBlue.setLayoutParams(params);

				setBlue1.setSelected(true);

				setBlue.setSelected(true);

			} else {
				setBlue1.setSelected(false);

				setBlue.setSelected(false);
				LayoutParams params = (LayoutParams) setBlue.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				setBlue.setLayoutParams(params);
			}

		}
	}

	class OnAboutChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {
				LayoutParams params = (LayoutParams) setAbout.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				setAbout.setLayoutParams(params);
				setAbout.setSelected(true);

				setAbout1.setSelected(true);

			} else {
				LayoutParams params = (LayoutParams) setAbout.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				setAbout.setLayoutParams(params);

				setAbout.setSelected(false);

				setAbout1.setSelected(false);

			}

		}
	}

	class OnWifiChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {
				LayoutParams params = (LayoutParams) setWifi.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				setWifi.setLayoutParams(params);
				setWifi1.setSelected(true);

				setWifi.setSelected(true);

			} else {
				LayoutParams params = (LayoutParams) setWifi.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				setWifi.setLayoutParams(params);
				setWifi1.setSelected(false);

				setWifi.setSelected(false);

			}

		}
	}

	class OnVolumChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {
				LayoutParams params = (LayoutParams) setVolume
						.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				setVolume.setLayoutParams(params);
				setVolume1.setSelected(true);

				setVolume.setSelected(true);

			} else {
				LayoutParams params = (LayoutParams) setVolume
						.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				setVolume.setLayoutParams(params);
				setVolume1.setSelected(false);

				setVolume.setSelected(false);

			}

		}
	}

	public void onWifiClick(View view) {

		// Intent wifiIntent = new Intent(SettingActivity.this,
		// WifiActivity.class);
		// startActivity(wifiIntent);

		// Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
		// startActivity(intent);

		Intent wifiIntent = new Intent(SettingActivity.this, WifiActivity.class);
		startActivity(wifiIntent);

	}

	public void onBlueClick(View view) {

		// Intent wifiIntent = new Intent(SettingActivity.this,
		// BluetoothActivity.class);
		// startActivity(wifiIntent);
		// Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		// startActivity(intent);
	}

	public void onVolumeClick(View view) {

		Intent wifiIntent = new Intent(SettingActivity.this,
				SetVolumActivity.class);
		startActivity(wifiIntent);

	}

	public void onAboutClick(View view) {

//		Intent wifiIntent = new Intent(SettingActivity.this,
//				AboutAcitvity.class);
//		startActivity(wifiIntent);

	}

}
