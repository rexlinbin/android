package com.boxuu.gamebox.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.boxuu.gamebox.R;
import com.boxuu.gamebox.common.GlobalConstants;
import com.boxuu.gamebox.utils.L;

public class HTML5WebView extends WebView {

	private Context mContext;
	private MyWebChromeClient mWebChromeClient;
	private View mCustomView;
	private FrameLayout mCustomViewContainer;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;

	private FrameLayout mContentView;
	private FrameLayout mBrowserFrameLayout;
	private FrameLayout mLayout;

	private Js2JavaListener mJs2JavaListener;
	private Js2JavaModel mJs2JavaModel;

	static final String TAG = "HTML5WebView";

	@SuppressLint({ "SetJavaScriptEnabled", "SdCardPath" })
	private void init(Context context) {
		mContext = context;
		// Activity a = (Activity) mContext;

		mLayout = new FrameLayout(context);

		mBrowserFrameLayout = (FrameLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.custom_screen, null);
		mContentView = (FrameLayout) mBrowserFrameLayout
				.findViewById(R.id.main_content);
		mCustomViewContainer = (FrameLayout) mBrowserFrameLayout
				.findViewById(R.id.fullscreen_custom_content);

		mLayout.addView(mBrowserFrameLayout, COVER_SCREEN_PARAMS);

		mWebChromeClient = new MyWebChromeClient();
		setWebChromeClient(mWebChromeClient);

		setWebViewClient(new MyWebViewClient());

		// Configure the webview
		WebSettings s = getSettings();
		s.setBuiltInZoomControls(false);
		s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		s.setUseWideViewPort(true);
		s.setLoadWithOverviewMode(true);
		s.setSavePassword(true);
		s.setSaveFormData(true);
		s.setJavaScriptEnabled(true);

		// enable navigator.geolocation
		s.setGeolocationEnabled(true);
		String packageName = context.getPackageName();
		s.setGeolocationDatabasePath("/data/data/" + packageName
				+ "/databases/");

		// enable Web Storage: localStorage, sessionStorage
		s.setDomStorageEnabled(true);

		mContentView.addView(this);

		initJs2JavaModel();
	}

	private void initJs2JavaModel() {

		mJs2JavaModel = new Js2JavaModel() {
			
			@Override
			public void showShare(String params) {
				if (mJs2JavaListener != null) {
					mJs2JavaListener.showShare();
				}
			}
			
		};

		addJavascriptInterface(mJs2JavaModel, "gamebox");
	}

	public HTML5WebView(Context context) {
		super(context);
		init(context);
	}

	public HTML5WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public HTML5WebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FrameLayout getLayout() {
		return mLayout;
	}

	public boolean inCustomView() {
		return (mCustomView != null);
	}

	public void hideCustomView() {
		mWebChromeClient.onHideCustomView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if ((mCustomView == null) && canGoBack()) {
				goBack();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private class MyWebChromeClient extends WebChromeClient {
		private Bitmap mDefaultVideoPoster;
		private View mVideoProgressView;

		@Override
		public void onShowCustomView(View view,
				WebChromeClient.CustomViewCallback callback) {
			// Log.i(LOGTAG, "here in on ShowCustomView");
			HTML5WebView.this.setVisibility(View.GONE);

			// if a view already exists then immediately terminate the new one
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}

			mCustomViewContainer.addView(view);
			mCustomView = view;
			mCustomViewCallback = callback;
			mCustomViewContainer.setVisibility(View.VISIBLE);
		}

		@Override
		public void onHideCustomView() {

			if (mCustomView == null)
				return;

			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);

			// Remove the custom view from its container.
			mCustomViewContainer.removeView(mCustomView);
			mCustomView = null;
			mCustomViewContainer.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();

			HTML5WebView.this.setVisibility(View.VISIBLE);

			// Log.i(LOGTAG, "set it to webVew");
		}

		@Override
		public Bitmap getDefaultVideoPoster() {
			// Log.i(LOGTAG, "here in on getDefaultVideoPoster");
			if (mDefaultVideoPoster == null) {
				mDefaultVideoPoster = BitmapFactory.decodeResource(
						getResources(), R.drawable.default_video_poster);
			}
			return mDefaultVideoPoster;
		}

		@Override
		public View getVideoLoadingProgressView() {
			// Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

			if (mVideoProgressView == null) {
				LayoutInflater inflater = LayoutInflater.from(mContext);
				mVideoProgressView = inflater.inflate(
						R.layout.video_loading_progress, null);
			}
			return mVideoProgressView;
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			((Activity) mContext).setTitle(title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {

			L.v(TAG, "onProgressChanged", "newProgress: " + newProgress);

			((Activity) mContext).getWindow().setFeatureInt(
					Window.FEATURE_PROGRESS, newProgress * 100);
		}

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
				GeolocationPermissions.Callback callback) {
			callback.invoke(origin, true, false);
		}
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			L.v(TAG, "shouldOverrideUrlLoading", "url: " + url);
			if (url.equalsIgnoreCase(GlobalConstants.GAME_HOST_URL)) {
				((Activity) mContext).finish();
//				if (mJs2JavaListener != null) {
//					mJs2JavaListener.showShare();
//				}
			}
			view.loadUrl(url);
			return true;
		}
	}

	static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.MATCH_PARENT,
			ViewGroup.LayoutParams.MATCH_PARENT);

	public void setJs2JavaListener(Js2JavaListener js2JavaListener) {
		this.mJs2JavaListener = js2JavaListener;
	}

	public interface Js2JavaListener {

		public static final int SHARE_MESSAGE = 1;

		void showShare();

	}
	
}