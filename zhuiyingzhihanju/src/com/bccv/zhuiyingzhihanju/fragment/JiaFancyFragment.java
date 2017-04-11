package com.bccv.zhuiyingzhihanju.fragment;

import com.bccv.zhuiyingzhihanju.R;

import com.utils.tools.BaseActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JiaFancyFragment extends BaseActivity{
	WebView myWebView;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_jiafancy);
		
		  myWebView = (WebView) findViewById(R.id.jiaFancy_web);
		    WebSettings webSetting = myWebView.getSettings();
		    webSetting.setJavaScriptEnabled(true);
		    
		    webSetting.setAllowFileAccess(true);
		
			webSetting.setSupportZoom(true);
			webSetting.setBuiltInZoomControls(true);
			webSetting.setUseWideViewPort(true);
			webSetting.setSupportMultipleWindows(false);
			webSetting.setLoadWithOverviewMode(true);
			webSetting.setAppCacheEnabled(true);
			webSetting.setDatabaseEnabled(true);
			webSetting.setDomStorageEnabled(true);
			webSetting.setJavaScriptEnabled(true);
			webSetting.setGeolocationEnabled(true);
			webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
			webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
			webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
			webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
					.getPath());
			// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
			webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
			webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		    
		    
		    
		    
		    
		    
		    
		
		    myWebView.loadUrl("http://hanju.zhuiying.me/app/video/client");
		
		
		
		
	}

}
