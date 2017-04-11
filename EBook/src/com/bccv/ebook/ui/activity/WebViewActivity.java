package com.bccv.ebook.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bccv.ebook.common.GlobalConstants;
import com.boxuu.ebookjy.R;

public class WebViewActivity extends BaseActivity{

	private ImageView score_back;
//	private TextView score_title;
	private WebView wv_score;
	private ProgressBar pb;
//	private String title;
	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
//		title = getIntent().getStringExtra("title");
		url = getIntent().getStringExtra("url");
		initView();
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		score_back = (ImageView) findViewById(R.id.score_back);
//		score_title = (TextView) findViewById(R.id.score_title);
//		if (title != null && !title.equals("")) {
//			score_title.setText(title);
//		}else{
//			score_title.setText("全部小说");
//		}
		wv_score = (WebView) findViewById(R.id.wv_score);
		pb = (ProgressBar) findViewById(R.id.webview_progress);
		wv_score.getSettings().setJavaScriptEnabled(true);
		wv_score.getSettings().setSupportZoom(false);
		wv_score.getSettings().setBuiltInZoomControls(false);
		wv_score.setWebChromeClient(new WebViewClient());
		wv_score.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		
		wv_score.setVerticalScrollBarEnabled(false);
		wv_score.setVerticalScrollbarOverlay(false);
		wv_score.setHorizontalScrollBarEnabled(false);
		wv_score.setHorizontalScrollbarOverlay(false);
		score_back.setOnClickListener(this);
		if (url != null && !url.equals("")) {
			wv_score.loadUrl(url);
		}else{
			wv_score.loadUrl(GlobalConstants.WEBURL);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.score_back:
			onBackPressed();
			break;
		default:
			break;
		}
	}
	
	private class WebViewClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			pb.setProgress(newProgress);
			if (newProgress == 100) {
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		wv_score.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		wv_score.onResume();
	}
}
