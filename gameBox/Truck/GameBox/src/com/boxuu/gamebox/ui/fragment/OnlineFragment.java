package com.boxuu.gamebox.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.boxuu.gamebox.ApplicationManager;
import com.boxuu.gamebox.common.GlobalConstants;
import com.boxuu.gamebox.ui.activity.GameActivity;

public class OnlineFragment extends Fragment {

	public static final String TAG = "OnlineFragment";
	
	private WebView webView;
	
	private String currentUrl;
	
	public static OnlineFragment newInstance(){
		
		OnlineFragment onlineFragment = new OnlineFragment();
		
//		Bundle bundle = new Bundle();
//		bundle.putString("key", "value");
//		onlineFragment.setArguments(bundle);
		
		return onlineFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		initView();
		return webView;
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "SdCardPath" })
	private void initView(){
		webView = new MyWebView(ApplicationManager.getGlobalContext());
		
		webView.setWebViewClient(new MyWebViewClient());
		webView.setWebChromeClient(new MyWebChromeClient());
		
//		WebSettings s = webView.getSettings();
//		s.setBuiltInZoomControls(true);
//		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//		s.setUseWideViewPort(true);
//		s.setLoadWithOverviewMode(true);
//		s.setSavePassword(true);
//		s.setSaveFormData(true);
//		s.setJavaScriptEnabled(true);
//
//		// enable navigator.geolocation
//		s.setGeolocationEnabled(true);
//		String packageName = ApplicationManager.getGlobalContext().getPackageName();
//		s.setGeolocationDatabasePath("/data/data/" + packageName + "/databases/");
//
//		// enable Web Storage: localStorage, sessionStorage
//		s.setDomStorageEnabled(true);
		
		   WebSettings settings = webView.getSettings();
		   settings.setJavaScriptEnabled(true);// 设置可以运行JS脚本
		   settings.setUseWideViewPort(true); // 打开页面时， 自适应屏幕
		   settings.setLoadWithOverviewMode(true);// 打开页面时， 自适应屏幕
		   settings.setSupportZoom(false);// 用于设置webview放大
		   settings.setBuiltInZoomControls(false);
		   settings.setDomStorageEnabled(true);//设置可以使用localStorage
		   settings.setDomStorageEnabled( true );//设置可以使用数据库
		   settings.setAllowFileAccess(true);//设置可以操作文件
		   settings.setAppCacheEnabled(true);//设置使用缓存
		   settings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M
		   String appCacheDir = ApplicationManager.getGlobalContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		   settings.setAppCachePath(appCacheDir);//设置缓存路径
		   settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//使用缓存模式
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		webView.loadUrl(GlobalConstants.GAME_HOST_URL);
	}
	
	private class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
		}
		
	}
	
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(TAG, "shouldOverrideUrlLoading: " + url);
			
			if(url.contains(GlobalConstants.GAME_URL)){
				Intent intent = new Intent(getActivity(), GameActivity.class);
				intent.putExtra(GameActivity.URL_KEY, url);
				getActivity().startActivity(intent);
				return true;
			}
			view.loadUrl(url);
			currentUrl = url;
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
		}
		
	}
	
	
	private class MyWebView extends WebView{

		public MyWebView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {

//							|| currentUrl.equals(GlobalConstants.GAME_HOST_URL_HOT)
//							|| currentUrl.equals(GlobalConstants.GAME_HOST_URL_RANK))
				if (currentUrl!=null 
						&& !(currentUrl.equals(GlobalConstants.GAME_HOST_URL))
						&&canGoBack()) {
					goBack();
					currentUrl = getUrl();
					return true;
				}
			}
			return super.onKeyDown(keyCode, event);
		}
		
	}
	
	
}
