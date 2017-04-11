package com.bccv.boxcomic.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.api.AnalysisApi;
import com.bccv.boxcomic.modal.InfoAnalysis;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;

public class InfoAnalysisContentActivity extends BaseActivity {
	
	private WebView webView;
	
	private InfoAnalysis infoAnalysis;
	private String advices_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infoanalysiscontent);
		setBack();
		
		advices_id = getIntent().getExtras().getString("advices_id");
		webView = (WebView) findViewById(R.id.analysis_webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings()
		.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webView.loadUrl("http://192.168.0.240/comicapi/home/index/web_advices/id/" + advices_id);
//		setData();
	}
	
	private void setData(){
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (infoAnalysis != null) {
					webView.loadUrl("");
				}
			}
		};

		new DataAsyncTask(callback, false) {

			

			@Override
			protected String doInBackground(String... params) {
				AnalysisApi analysisApi = new AnalysisApi();
				infoAnalysis = analysisApi.getInfoAnalysis(advices_id);
				return "";
			}
		}.executeProxy("");
	}
	
	private void setBack(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}

}
