package com.bccv.boxcomic.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.tool.BaseActivity;

public class TermsActivity extends BaseActivity{
	private WebView web;
	private ImageView back;
	private TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms);
		
		web=(WebView) findViewById(R.id.terms_web);
		web.loadUrl("http://comicapi.boxuu.com/gree.html");
		
		back=(ImageView) findViewById(R.id.acitvity_title_imageback);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finishActivityWithAnim();
			}
		});
		title=(TextView) findViewById(R.id.activity_title_text);
		title.setText("《盒子漫画》使用条款");
		
	}

}
