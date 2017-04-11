package com.bccv.boxcomic.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.GlobalParams;

public class LocalWebActivity extends BaseActivity {
	WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localweb);

		RelativeLayout back = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivityWithAnim();
			}
		});

		webView = (WebView) findViewById(R.id.localweb);
		WebSettings wetSettins=webView.getSettings();
		wetSettins.setJavaScriptEnabled(true);
		wetSettins.setAllowFileAccess(true);
		wetSettins.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wetSettins.setUseWideViewPort(true); 
		wetSettins.setLoadWithOverviewMode(true);// 打开页面时， 自适应屏幕
		wetSettins.setSupportZoom(false);// 用于设置webview放大
		wetSettins.setBuiltInZoomControls(false);
		webView.setHorizontalScrollbarOverlay(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.loadUrl("http://comicapi.boxuu.com/helpan.html");

	}

}
