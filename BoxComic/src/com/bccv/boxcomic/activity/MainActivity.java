package com.bccv.boxcomic.activity;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.fragment.CollectFragment;
import com.bccv.boxcomic.fragment.MainFragment;
import com.bccv.boxcomic.fragment.OptionFragment;
import com.bccv.boxcomic.fragment.SearchFragment;
import com.bccv.boxcomic.tool.AppManager;
import com.bccv.boxcomic.tool.DialogUtils;
import com.bccv.boxcomic.tool.Logger;
import com.tencent.stat.StatService;

public class MainActivity extends TabActivity implements OnClickListener {
	private RelativeLayout homeRe, collectRe, searchRe, setRe;

	private Boolean isHome, isCollect, isSearch, isSet;
	private String TAG = "MainActivity";
	private ImageView collectIm, homeIm, searchIm, setIm;
	// 用TabHost加載控件
	private TabHost tabHost;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AppManager.getAppManager().addActivity(this);
		homeRe = (RelativeLayout) findViewById(R.id.main_bottom_home);
		collectRe = (RelativeLayout) findViewById(R.id.main_bottom_collect);
		searchRe = (RelativeLayout) findViewById(R.id.main_bottom_search);
		setRe = (RelativeLayout) findViewById(R.id.main_botton_set);
		homeIm = (ImageView) findViewById(R.id.main_bootom_homeIm);
		collectIm = (ImageView) findViewById(R.id.main_bootom_collectIm);
		searchIm = (ImageView) findViewById(R.id.main_bootom_searchIm);
		setIm = (ImageView) findViewById(R.id.main_bootom_setIm);
		homeRe.setOnClickListener(this);
		collectRe.setOnClickListener(this);
		searchRe.setOnClickListener(this);
		setRe.setOnClickListener(this);
		// 设置默认的选项
		setDefaultFragment();

	}

	private void setDefaultFragment() {
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("home").setIndicator("home")
				.setContent(new Intent(this, MainFragment.class)));
		tabHost.addTab(tabHost.newTabSpec("collect").setIndicator("collect")
				.setContent(new Intent(this, CollectFragment.class)));
		tabHost.addTab(tabHost.newTabSpec("search").setIndicator("search")
				.setContent(new Intent(this, SearchFragment.class)));
		tabHost.addTab(tabHost.newTabSpec("setting").setIndicator("setting")
				.setContent(new Intent(this, OptionFragment.class)));

		homeRe.setSelected(true);
		homeIm.setSelected(true);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.main_bottom_home:

			isHome = true;
			isSearch = false;
			isCollect = false;
			isSet = false;
			tabHost.setCurrentTabByTag("home");

			break;
		case R.id.main_bottom_collect:
			tabHost.setCurrentTabByTag("collect");
			isHome = false;
			isCollect = true;
			isSearch = false;
			isSet = false;

			break;

		case R.id.main_bottom_search:
			tabHost.setCurrentTabByTag("search");
			isHome = false;
			isCollect = false;
			isSearch = true;
			isSet = false;

			break;
		case R.id.main_botton_set:
			tabHost.setCurrentTabByTag("setting");
			isHome = false;
			isCollect = false;
			isSearch = false;
			isSet = true;

			break;
		}

		if (isHome) {
			Logger.v(TAG, "----主页------");
			homeRe.setSelected(true);
			homeIm.setSelected(true);
		} else {
			homeRe.setSelected(false);
			homeIm.setSelected(false);
		}
		if (isCollect) {
			Logger.v(TAG, "----收藏------");
			collectRe.setSelected(true);
			collectIm.setSelected(true);
		} else {
			collectRe.setSelected(false);
			collectIm.setSelected(false);
		}

		if (isSearch) {
			Logger.v(TAG, "----搜索------");
			searchRe.setSelected(true);
			searchIm.setSelected(true);
		} else {
			searchRe.setSelected(false);
			searchIm.setSelected(false);
		}

		if (isSet) {
			Logger.v(TAG, "----设置------");
			setIm.setSelected(true);
			setRe.setSelected(true);
		} else {
			setRe.setSelected(false);
			setIm.setSelected(false);
		}

	}

	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getRepeatCount() == 0) {
				DialogUtils.showDeleteDialog(MainActivity.this,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								finish();
								AppManager.getAppManager().finishAllActivity();
								System.exit(0);

							}

						}, "离开", "你真的要狠心离开么？");

			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);
		
		StatService.trackCustomBeginEvent(this, "MainActivity", "level");	
		
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		StatService.onPause(this);
		StatService.trackCustomEndEvent(this, "MainActivity", "level");
	}

}
