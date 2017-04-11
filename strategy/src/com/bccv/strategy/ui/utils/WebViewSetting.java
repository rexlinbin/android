package com.bccv.strategy.ui.utils;

import java.util.regex.PatternSyntaxException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bccv.strategy.ApplicationManager;
import com.bccv.strategy.utils.SystemUtil;

public class WebViewSetting {
	private static Context context ; 
	@SuppressLint("SetJavaScriptEnabled")
	public static void setting(WebView webView,String TAG,View loadingView,Context con) {
		context = con;
		webView.setWebViewClient(new MyWebViewClient(TAG, loadingView));
		webView.setWebChromeClient(new MyWebChromeClient());
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);// 设置可以运行JS脚本
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
//		webView.setVerticalScrollBarEnabled(false);
//		webView.setVerticalScrollbarOverlay(false);
//		webView.setHorizontalScrollBarEnabled(false);
//		webView.setHorizontalScrollbarOverlay(false);
		
		settings.setUseWideViewPort(true); // 打开页面时， 自适应屏幕
		settings.setLoadWithOverviewMode(true);// 打开页面时， 自适应屏幕
		settings.setSupportZoom(false);// 用于设置webview放大
		settings.setBuiltInZoomControls(false);
		settings.setDomStorageEnabled(true);// 设置可以使用localStorage
		settings.setDomStorageEnabled(true);// 设置可以使用数据库
		settings.setAllowFileAccess(true);// 设置可以操作文件
		settings.setAppCacheEnabled(true);// 设置使用缓存
		settings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，设的是8M
		String appCacheDir = ApplicationManager.getGlobalContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		settings.setAppCachePath(appCacheDir);// 设置缓存路径
		if (SystemUtil.isNetOk(ApplicationManager.getGlobalContext())) {
			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 使用缓存模式
		}else {
			settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 使用缓存模式
		}
	}
	
	public static class MyWebViewClient extends WebViewClient {
		
		private String TAG;
		private View loadingView;
		
		public MyWebViewClient(String TAG,View loadingView){
			this.TAG = TAG;
			this.loadingView = loadingView;
		}
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(TAG, "shouldOverrideUrlLoading: " + url);
//			if (url.contains("fullscreen") ) {
//                Log.i("LOB", "FULLSCREEN " + url);
//
//             try {
//               url = url.replaceAll("(?im)fullscreen", "");
//                 } catch (PatternSyntaxException ex) {
//                 } catch (IllegalArgumentException ex) {
//                 } catch (IndexOutOfBoundsException ex) {
//               }
//
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.setDataAndType(Uri.parse(url), "video/mp4");
//                context.startActivity(intent);
//
//                   return true;
//            }else {
            	view.loadUrl(url);
//			}
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

	public static class MyWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
		}

	}
}
