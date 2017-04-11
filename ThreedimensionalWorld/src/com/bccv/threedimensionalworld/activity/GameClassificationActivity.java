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

public class GameClassificationActivity extends BaseActivity {

	
	private RelativeLayout leftLayout, rightLayout;

	private Button ActionBtn, CarsBtn, ChildgameBtn, CosplayBtn,
			MotionsensingBtn, ReaxationBtn, Sports;

	private Button ActionBtn1, CarsBtn1, ChildgameBtn1, CosplayBtn1,
			MotionsensingBtn1, ReaxationBtn1, Sports1;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameclassification);
		rightLayout = (RelativeLayout) findViewById(R.id.right_relativeLayout);
	
		leftLayout = (RelativeLayout) findViewById(R.id.left_relativeLayout);
	
		setLeft();
		setRight();
		setStatus();
	}

	private void setLeft() {
		ActionBtn = (Button) findViewById(R.id.Game_action_button);
		CarsBtn = (Button) findViewById(R.id.Game_cars_button);
		ChildgameBtn = (Button) findViewById(R.id.Game_ChildGame_button);
		CosplayBtn = (Button) findViewById(R.id.Game_CosPlay_button);
		MotionsensingBtn = (Button) findViewById(R.id.Game_Motionsensing_button);
		ReaxationBtn = (Button) findViewById(R.id.Game_relaxtion_button);
		Sports = (Button) findViewById(R.id.Game_sports_button);

		ActionBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ActionBtn.bringToFront();
					ActionBtn.setSelected(true);
					ActionBtn1.setSelected(true);
					ActionBtn1.bringToFront();

					LayoutParams params = (LayoutParams) ActionBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					ActionBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					ActionBtn1.setSelected(false);
					ActionBtn.setSelected(false);

					LayoutParams params = (LayoutParams) ActionBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					ActionBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		CarsBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					CarsBtn.bringToFront();
					CarsBtn.setSelected(true);
					CarsBtn1.setSelected(true);
					CarsBtn1.bringToFront();

					LayoutParams params = (LayoutParams) CarsBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					CarsBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					CarsBtn1.setSelected(false);
					CarsBtn.setSelected(false);

					LayoutParams params = (LayoutParams) CarsBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					CarsBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		ChildgameBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ChildgameBtn.bringToFront();
					ChildgameBtn.setSelected(true);
					ChildgameBtn1.setSelected(true);
					ChildgameBtn1.bringToFront();

					LayoutParams params = (LayoutParams) ChildgameBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					ChildgameBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					ChildgameBtn1.setSelected(false);
					ChildgameBtn.setSelected(false);

					LayoutParams params = (LayoutParams) ChildgameBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					ChildgameBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		CosplayBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					CosplayBtn.bringToFront();
					CosplayBtn.setSelected(true);
					CosplayBtn1.setSelected(true);
					CosplayBtn1.bringToFront();

					LayoutParams params = (LayoutParams) CosplayBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					CosplayBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					CosplayBtn1.setSelected(false);
					CosplayBtn.setSelected(false);

					LayoutParams params = (LayoutParams) CosplayBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					CosplayBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		MotionsensingBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					MotionsensingBtn.bringToFront();
					MotionsensingBtn.setSelected(true);
					MotionsensingBtn1.setSelected(true);
					MotionsensingBtn1.bringToFront();

					LayoutParams params = (LayoutParams) MotionsensingBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					MotionsensingBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					MotionsensingBtn1.setSelected(false);
					MotionsensingBtn.setSelected(false);

					LayoutParams params = (LayoutParams) MotionsensingBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					MotionsensingBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		ReaxationBtn.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					ReaxationBtn.bringToFront();
					ReaxationBtn.setSelected(true);
					ReaxationBtn1.setSelected(true);
					ReaxationBtn1.bringToFront();

					LayoutParams params = (LayoutParams) ReaxationBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					ReaxationBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					ReaxationBtn1.setSelected(false);
					ReaxationBtn.setSelected(false);

					LayoutParams params = (LayoutParams) ReaxationBtn
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					ReaxationBtn.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		Sports.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Sports.bringToFront();
					Sports.setSelected(true);
					Sports1.setSelected(true);
					Sports1.bringToFront();

					LayoutParams params = (LayoutParams) Sports
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					Sports.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					Sports1.setSelected(false);
					Sports.setSelected(false);

					LayoutParams params = (LayoutParams) Sports
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					Sports.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

	}

	private void setRight() {
		ActionBtn1 = (Button) findViewById(R.id.Game_action_button1);
		CarsBtn1 = (Button) findViewById(R.id.Game_cars_button1);
		ChildgameBtn1 = (Button) findViewById(R.id.Game_ChildGame_button1);
		CosplayBtn1 = (Button) findViewById(R.id.Game_CosPlay_button1);
		MotionsensingBtn1 = (Button) findViewById(R.id.Game_Motionsensing_button1);
		ReaxationBtn1 = (Button) findViewById(R.id.Game_relaxtion_button1);
		Sports1 = (Button) findViewById(R.id.Game_sports_button1);
		
		
		ActionBtn1.setFocusable(false);
		ActionBtn1.setFocusableInTouchMode(false);
		CarsBtn1.setFocusable(false);
		CarsBtn1.setFocusableInTouchMode(false);
		ChildgameBtn1.setFocusable(false);
		ChildgameBtn1.setFocusableInTouchMode(false);
		CosplayBtn1.setFocusable(false);
		CosplayBtn1.setFocusableInTouchMode(false);
		MotionsensingBtn1.setFocusable(false);
		MotionsensingBtn1.setFocusableInTouchMode(false);
		ReaxationBtn1.setFocusable(false);
		ReaxationBtn1.setFocusableInTouchMode(false);
		
		Sports1.setFocusable(false);
		Sports1.setFocusableInTouchMode(false);
		
	}

	public void onActionClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", true);
		intent.putExtra("type_id", "1");
		startActivity(intent);
	}

	public void onSportsClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", true);
		intent.putExtra("type_id", "3");
		startActivity(intent);
	}

	public void onRelaxtionClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", true);
		intent.putExtra("type_id", "2");
		startActivity(intent);
	}

	public void onChildGameClick(View view) {
		Intent intent = new Intent(getApplicationContext(), MyAppOrGameActivity.class);
		intent.putExtra("isGame", true);
		intent.putExtra("type_id", "4");
		startActivity(intent);
	}

	public void onCosPlayClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", true);
		intent.putExtra("type_id", "5");
		startActivity(intent);
	}

	public void onCarsClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", true);
		intent.putExtra("type_id", "6");
		startActivity(intent);
	}

	public void onMotionsensingClick(View view) {
		Intent intent = new Intent(getApplicationContext(), AppListActivity.class);
		intent.putExtra("isGame", true);
		intent.putExtra("type_id", "7");
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
