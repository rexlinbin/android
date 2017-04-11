package com.bccv.threedimensionalworld.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.view.MyTitleView;

public class AppClassificationActivity extends BaseActivity {

	private Button videoBtn, childBtn, musicBtn, educationBtn, lifeBtn,
			ebookBtn, newsBtn, healthBtn;
	private Button videoBtn1, childBtn1, musicBtn1, educationBtn1, lifeBtn1,
			ebookBtn1, newsBtn1, healthBtn1;

	private RelativeLayout leftLayout, rightLayout;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_appclassification);
		rightLayout = (RelativeLayout) findViewById(R.id.right_relativeLayout);
		leftLayout = (RelativeLayout) findViewById(R.id.left_relativeLayout);
		setStatus();
		setLeft();
		setRight();

	}

	private void setLeft() {
		// TODO Auto-generated method stub

		videoBtn = (Button) findViewById(R.id.app_video);
		childBtn = (Button) findViewById(R.id.app_child);
		musicBtn = (Button) findViewById(R.id.app_music);
		educationBtn = (Button) findViewById(R.id.app_education);

		lifeBtn = (Button) findViewById(R.id.app_life);
		ebookBtn = (Button) findViewById(R.id.app_ebook);
		newsBtn = (Button) findViewById(R.id.app_news);
		healthBtn = (Button) findViewById(R.id.app_health);

		videoBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					videoBtn.bringToFront();
					videoBtn.setSelected(true);
					videoBtn1.setSelected(true);
					videoBtn1.bringToFront();

					LayoutParams params = (LayoutParams) videoBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					videoBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					videoBtn1.setSelected(false);
					videoBtn.setSelected(false);

					LayoutParams params = (LayoutParams) videoBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					videoBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		childBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					childBtn.bringToFront();
					childBtn.setSelected(true);
					childBtn1.setSelected(true);
					childBtn1.bringToFront();

					LayoutParams params = (LayoutParams) childBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					childBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					childBtn1.setSelected(false);
					childBtn.setSelected(false);

					LayoutParams params = (LayoutParams) childBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					childBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		musicBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					musicBtn.bringToFront();
					musicBtn.setSelected(true);
					musicBtn1.setSelected(true);
					musicBtn1.bringToFront();

					LayoutParams params = (LayoutParams) musicBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					musicBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					musicBtn1.setSelected(false);
					musicBtn.setSelected(false);

					LayoutParams params = (LayoutParams) musicBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					musicBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		educationBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					educationBtn.bringToFront();
					educationBtn.setSelected(true);
					educationBtn1.setSelected(true);
					educationBtn1.bringToFront();

					LayoutParams params = (LayoutParams) educationBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					educationBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					educationBtn1.setSelected(false);
					educationBtn.setSelected(false);

					LayoutParams params = (LayoutParams) educationBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					educationBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		lifeBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					lifeBtn.bringToFront();
					lifeBtn.setSelected(true);
					lifeBtn1.setSelected(true);
					lifeBtn1.bringToFront();

					LayoutParams params = (LayoutParams) lifeBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					lifeBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					lifeBtn1.setSelected(false);
					lifeBtn.setSelected(false);

					LayoutParams params = (LayoutParams) lifeBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					lifeBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		ebookBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ebookBtn.bringToFront();
					ebookBtn.setSelected(true);
					ebookBtn1.setSelected(true);
					ebookBtn1.bringToFront();

					LayoutParams params = (LayoutParams) ebookBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					ebookBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					ebookBtn1.setSelected(false);
					ebookBtn.setSelected(false);

					LayoutParams params = (LayoutParams) ebookBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					ebookBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		newsBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					newsBtn.bringToFront();
					newsBtn.setSelected(true);
					newsBtn1.setSelected(true);
					newsBtn1.bringToFront();

					LayoutParams params = (LayoutParams) newsBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					newsBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					newsBtn1.setSelected(false);
					newsBtn.setSelected(false);

					LayoutParams params = (LayoutParams) newsBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					newsBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		healthBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					healthBtn.bringToFront();
					healthBtn.setSelected(true);
					healthBtn1.setSelected(true);
					healthBtn1.bringToFront();

					LayoutParams params = (LayoutParams) healthBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					healthBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					healthBtn1.setSelected(false);
					healthBtn.setSelected(false);

					LayoutParams params = (LayoutParams) healthBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					healthBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

	}

	private void setRight() {
		// TODO Auto-generated method stub

		videoBtn1 = (Button) findViewById(R.id.app_video1);
		childBtn1 = (Button) findViewById(R.id.app_child1);
		musicBtn1 = (Button) findViewById(R.id.app_music1);
		educationBtn1 = (Button) findViewById(R.id.app_education1);

		lifeBtn1 = (Button) findViewById(R.id.app_life1);
		ebookBtn1 = (Button) findViewById(R.id.app_ebook1);
		newsBtn1 = (Button) findViewById(R.id.app_news1);
		healthBtn1 = (Button) findViewById(R.id.app_health1);

		videoBtn1.setFocusable(false);
		videoBtn1.setFocusableInTouchMode(false);
		childBtn1.setFocusable(false);
		childBtn1.setFocusableInTouchMode(false);
		musicBtn1.setFocusable(false);
		musicBtn1.setFocusableInTouchMode(false);
		educationBtn1.setFocusable(false);
		educationBtn1.setFocusableInTouchMode(false);
		lifeBtn1.setFocusable(false);
		lifeBtn1.setFocusableInTouchMode(false);
		ebookBtn1.setFocusable(false);
		ebookBtn1.setFocusableInTouchMode(false);

		newsBtn1.setFocusable(false);
		newsBtn1.setFocusableInTouchMode(false);

		healthBtn1.setFocusable(false);
		healthBtn1.setFocusableInTouchMode(false);

	}

	public void onVedioClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", false);
		intent.putExtra("type_id", "1");
		startActivity(intent);
	}

	public void onMusicClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", false);
		intent.putExtra("type_id", "2");
		startActivity(intent);
	}

	public void onLifeClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", false);
		intent.putExtra("type_id", "6");
		startActivity(intent);
	}

	public void onChildClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", false);
		intent.putExtra("type_id", "7");
		startActivity(intent);
	}

	public void onEducationClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", false);
		intent.putExtra("type_id", "5");
		startActivity(intent);
	}

	public void onHealClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", false);
		intent.putExtra("type_id", "8");
		startActivity(intent);
	}

	public void onNewsClick(View view) {
		Intent intent = new Intent(getApplicationContext(), MyAppOrGameActivity.class);
		intent.putExtra("isGame", false);
		startActivity(intent);
	}

	public void onEbookClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", false);
		intent.putExtra("type_id", "3");
		startActivity(intent);
	}

	private MyTitleView leftMyTitleView, rightMyTitleView;

	private void setStatus() {
		leftMyTitleView = (MyTitleView) findViewById(R.id.left_myTitleView);
		rightMyTitleView = (MyTitleView) findViewById(R.id.right_myTitleView);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (leftMyTitleView != null && rightMyTitleView != null) {
			leftMyTitleView.pauseView();
			rightMyTitleView.pauseView();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (leftMyTitleView != null && rightMyTitleView != null) {
			leftMyTitleView.resumeView();
			rightMyTitleView.resumeView();
		}

	}

}
