package com.bccv.meitu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.sns.UserInfoManager;

public class ScoreActivity extends BaseActivity{

	private ImageView score_back;
	private TextView score_title;
	private WebView wv_score;
	private ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		initView();
	}
	private void initView() {
		score_back = (ImageView) findViewById(R.id.score_back);
		score_title = (TextView) findViewById(R.id.score_title);
		wv_score = (WebView) findViewById(R.id.wv_score);
		pb = (ProgressBar) findViewById(R.id.webview_progress);
		wv_score.getSettings().setJavaScriptEnabled(true);
		wv_score.getSettings().setSupportZoom(false);
		wv_score.getSettings().setBuiltInZoomControls(false);
		wv_score.setWebChromeClient(new WebViewClient());
		wv_score.getSettings()
				.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		
		wv_score.setVerticalScrollBarEnabled(false);
		wv_score.setVerticalScrollbarOverlay(false);
		wv_score.setHorizontalScrollBarEnabled(false);
		wv_score.setHorizontalScrollbarOverlay(false);
		score_back.setOnClickListener(this);
		
		wv_score.loadUrl(GlobalConstants.TASK_URL+UserInfoManager.getUserId());
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
