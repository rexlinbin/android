package com.bccv.bangyangapp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bccv.bangyangapp.ApplicationManager;
import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.ui.view.BackGroundView;

public class AboutUsActivity extends BaseActivity {

	private static final String TAG = "AboutUsActivity";

	private BackGroundView mBackGroundView;
	private WebView mWebView;
	private View common_title_back_btn, common_title_right_btn;
	private TextView common_title_tv;
	private View loadingView;

	private int[] background;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		initView();
		initData();
	}

	private void initView() {
		mBackGroundView = (BackGroundView) findViewById(R.id.background_view);
		common_title_back_btn = findViewById(R.id.common_title_back_btn);
		common_title_right_btn = findViewById(R.id.common_title_right_btn);
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		mWebView = (WebView) findViewById(R.id.about_us_web);
		loadingView = findViewById(R.id.about_us_loading);

		common_title_tv.setText("关于我们");

		common_title_back_btn.setOnClickListener(this);
		common_title_right_btn.setOnClickListener(this);

		mBackGroundView.setGradient(background[0], background[1]);
		
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.setWebChromeClient(new MyWebChromeClient());

		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);// 设置可以运行JS脚本
		settings.setUseWideViewPort(true); // 打开页面时， 自适应屏幕
		settings.setLoadWithOverviewMode(true);// 打开页面时， 自适应屏幕
		settings.setSupportZoom(false);// 用于设置webview放大
		settings.setBuiltInZoomControls(false);
		settings.setDomStorageEnabled(true);// 设置可以使用localStorage
		settings.setDomStorageEnabled(true);// 设置可以使用数据库
		settings.setAllowFileAccess(true);// 设置可以操作文件
		settings.setAppCacheEnabled(true);// 设置使用缓存
		settings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M
		String appCacheDir = ApplicationManager.getGlobalContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		settings.setAppCachePath(appCacheDir);// 设置缓存路径
		settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 使用缓存模式

	}

	private void initData() {
		mWebView.loadUrl("http://appsapi.bccv.com/about.html");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.common_title_back_btn:
			finish();
			break;
		case R.id.common_title_right_btn:
			// TODO 跳转分享页
			Intent appReleaseIntent = new Intent(this, AppReleaseActivity.class);
			appReleaseIntent.putExtra(BackGroundView.BACKGROUND_COLOR, mBackGroundView.getGradientColor());
			startActivity(appReleaseIntent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;

		default:
			break;
		}

	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(TAG, "shouldOverrideUrlLoading: " + url);
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			loadingView.setVisibility(View.VISIBLE);
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			loadingView.setVisibility(View.GONE);
			super.onPageFinished(view, url);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
		}

	}

	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
