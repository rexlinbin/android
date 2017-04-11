package com.bccv.zhuiyingzhihanju.activity;

import com.bccv.zhuiyingzhihanju.R;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tendcloud.tenddata.TCAgent;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.utils.net.WebViewInterJS;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class HanJuMiAgreeActivity extends Activity {

	private WebView mWebView;
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "HanJuMiAgreeActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "HanJuMiAgreeActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agree);
tcStart();
		TextView text = (TextView) findViewById(R.id.titleName_textView);
		text.setVisibility(View.VISIBLE);
		text.setText("韩剧迷条款");
		LinearLayout li = (LinearLayout) findViewById(R.id.title);
		li.setBackgroundColor(getResources().getColor(R.color.white));

		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mWebView = (WebView) findViewById(R.id.agree_webView);

		WebSettings webSetting = mWebView.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);

		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(GlobalParams.canWebCache);
		if (GlobalParams.canWebCache) {
			webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		}
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
		webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);
		mWebView.addJavascriptInterface(new WebViewInterJS(this, mWebView), "HJ");
		mWebView.loadUrl("http://hanju.zhuiying.me/registerprotocol.html");

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);

				return true;
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
				// TODO Auto-generated method stub

				Logger.e("should", "request.getUrl().toString() is " + request.getUrl().toString());

				return super.shouldInterceptRequest(view, request);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				// refreshLayout.setRefreshing(false);
				// PromptManager.closeProgressDialog();
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// PromptManager.showProgressDialog(FoundFragment.this);
			}

		});

	}

}
