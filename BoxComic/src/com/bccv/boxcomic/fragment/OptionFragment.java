package com.bccv.boxcomic.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.tool.AppConfig;
import com.bccv.boxcomic.tool.AppManager;
import com.bccv.boxcomic.tool.GlobalParams;
import com.igexin.sdk.PushManager;

@SuppressLint("NewApi")
public class OptionFragment extends FragmentActivity {
	ToggleButton tog, tog1, tog2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_option);
		AppManager.getAppManager().addActivity(this);
		TextView title = (TextView) findViewById(R.id.activity_title_text);
		title.setText("设置");

		ImageView imBack = (ImageView) findViewById(R.id.acitvity_title_imageback);
		imBack.setVisibility(View.INVISIBLE);
		tog = (ToggleButton) findViewById(R.id.option_tog);
		tog.setChecked(GlobalParams.isLanscape);
		tog.setBackgroundResource(GlobalParams.isLanscape ? R.drawable.set_open
				: R.drawable.set_close);
		tog.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

				tog.setChecked(arg1);
				tog.setBackgroundResource(arg1 ? R.drawable.set_open
						: R.drawable.set_close);
				GlobalParams.isLanscape = !GlobalParams.isLanscape;
				AppConfig.setScreen(GlobalParams.isLanscape);

			}
		});
		tog1 = (ToggleButton) findViewById(R.id.option_time);
		tog2 = (ToggleButton) findViewById(R.id.option_onClick);
		tog1.setChecked(GlobalParams.canShowTime);
		tog1.setBackgroundResource(GlobalParams.canShowTime ? R.drawable.set_open
				: R.drawable.set_close);
		tog1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				tog1.setChecked(arg1);
				tog1.setBackgroundResource(arg1 ? R.drawable.set_open
						: R.drawable.set_close);
				GlobalParams.canShowTime = !GlobalParams.canShowTime;
				AppConfig.setShowTime(GlobalParams.canShowTime);
			}
		});
		tog2.setChecked(GlobalParams.isTui);
		tog2.setBackgroundResource(GlobalParams.isTui ? R.drawable.set_open
				: R.drawable.set_close);
		tog2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				tog2.setChecked(arg1);
				tog2.setBackgroundResource(arg1 ? R.drawable.set_open
						: R.drawable.set_close);
				if (arg1) {
					// 开启
					PushManager.getInstance().turnOnPush(
							OptionFragment.this.getApplicationContext());

				} else {
					// 关闭
					PushManager.getInstance().turnOffPush(
							OptionFragment.this.getApplicationContext());
				}

				GlobalParams.isTui = !GlobalParams.isTui;
				AppConfig.setTui(GlobalParams.isTui);
			}
		});

	}

}
