package com.boxuu.gamebox.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

import com.boxuu.gamebox.sns.bean.ShareInfo;
import com.boxuu.gamebox.ui.view.HTML5WebView;
import com.boxuu.gamebox.ui.view.HTML5WebView.Js2JavaListener;
import com.boxuu.gamebox.ui.view.SharePopwindow;
import com.boxuu.gamebox.utils.L;

public class GameActivity extends BaseActivity {

	public static final String URL_KEY = "url_key";
	
	private HTML5WebView mWebView;
	
	private SharePopwindow mSharePopwindow;
	private Js2JavaListener js2JavaListener;
	private View shadowView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWebView = new HTML5WebView(this);
		setFullScreen();
		initListener();
		
		shadowView = new View(mContext);
		LayoutParams layoutParams = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		shadowView.setLayoutParams(layoutParams);
		shadowView.setBackgroundColor(0Xcc222222);
		shadowView.setVisibility(View.GONE);
		mWebView.getLayout().addView(shadowView);
		
		//添加布局
		setContentView(mWebView.getLayout());
		
		if (savedInstanceState != null) {
			mWebView.restoreState(savedInstanceState);
		} else {
			// mWebView.loadUrl("http://freebsd.csie.nctu.edu.tw/~freedom/html5/");
			// mWebView.loadUrl("file:///android_asset/about.html");
			// mWebView.loadUrl("file:///android_asset/2048/index.html");
//			mWebView.loadUrl("http://m.cgame.cn");
			String url = iniData();
			if(!TextUtils.isEmpty(url)){
				L.v(TAG, "onCreate", "url : " + url);
				mWebView.loadUrl(url);
			}
		}

	}

	
	private void initListener(){
		js2JavaListener = new Js2JavaListener() {
			
			@Override
			public void showShare() {
				
				//TODO 封装 shareInfo对象
				ShareInfo shareInfo = new ShareInfo();
				
				showSharePop(shareInfo);
			}
		};
		mWebView.setJs2JavaListener(js2JavaListener);
	}
	
	private String iniData(){
		Intent intent = getIntent();
		String url = intent.getStringExtra(URL_KEY);
		if(TextUtils.isEmpty(url)){
			showShortToast("数据错误");
			finish();
		}
		return url;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mWebView.saveState(outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		mWebView.stopLoading();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.inCustomView()) {
				 mWebView.hideCustomView();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void showSharePop(ShareInfo shareInfo){
		if(mSharePopwindow==null){
			mSharePopwindow = new SharePopwindow(this,shadowView);
		}
		if(!mSharePopwindow.isShowing()){
			mSharePopwindow.show(mWebView, shareInfo);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWebView.stopLoading();
		mWebView.clearCache(true);
		mWebView.getLayout().removeAllViews();
		if(mSharePopwindow!=null){
			mSharePopwindow.clearViews();
		}
		mSharePopwindow = null;
	}
	
}
