package com.bccv.threedimensionalworld.activity;

import java.util.Date;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.view.MyTitleView;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends BaseActivity {
	private Button leftlocalButton, leftmovieButton, leftmusicButton,
			leftgameButton, leftappButton, leftsettingButton;
	private ImageView rightlocalButton, rightmovieButton, rightmusicButton,
			rightgameButton, rightappButton, rightsettingButton;

	private RelativeLayout leftLayout, rightLayout;
	private TextView leftStatusTextView;
	private TextView rightStatusTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		leftLayout = (RelativeLayout) findViewById(R.id.left_relativeLayout);
		rightLayout = (RelativeLayout) findViewById(R.id.right_relativeLayout);
		setLeft();
		setRight();
		setStatus();
	}

	

	private void setLeft() {

		leftlocalButton = (Button) findViewById(R.id.left_local_Button);
		leftmovieButton = (Button) findViewById(R.id.left_movie_Button);
		leftmusicButton = (Button) findViewById(R.id.left_music_Button);
		leftgameButton = (Button) findViewById(R.id.left_game_Button);
		leftappButton = (Button) findViewById(R.id.left_app_Button);
		leftsettingButton = (Button) findViewById(R.id.left_setting_Button);

		leftmovieButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftmovieButton.bringToFront();
					rightmovieButton.setSelected(true);
					rightmovieButton.bringToFront();
					leftmovieButton.setSelected(true);
					LayoutParams params = new LayoutParams(292,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(1, 40, 0, 0);
					leftmovieButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(292,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 40, 0, 0);
					leftmovieButton.setLayoutParams(params);
					rightmovieButton.setSelected(false);
					leftmovieButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftmovieButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovie();
			}
		});
		
		leftgameButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftgameButton.bringToFront();
					leftgameButton.setSelected(true);
					rightgameButton.setSelected(true);
					rightgameButton.bringToFront();
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(230, 57, 0, 0);
					leftgameButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(229, 57, 0, 0);
					leftgameButton.setLayoutParams(params);
					rightgameButton.setSelected(false);
					leftgameButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftgameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startGame();
			}
		});
		leftappButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftappButton.bringToFront();
					leftappButton.setSelected(true);
					rightappButton.setSelected(true);
					rightappButton.bringToFront();
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(230, 293, 0, 0);
					leftappButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(229, 293, 0, 0);
					leftappButton.setLayoutParams(params);
					rightappButton.setSelected(false);
					leftappButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftappButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startApp();
			}
		});

		leftmusicButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftmusicButton.bringToFront();
					leftmusicButton.setSelected(true);
					rightmusicButton.setSelected(true);
					rightmusicButton.bringToFront();
					LayoutParams params = new LayoutParams(171,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(457, 40, 0, 0);
					leftmusicButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(171,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(456, 40, 0, 0);
					leftmusicButton.setLayoutParams(params);
					rightmusicButton.setSelected(false);
					leftmusicButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftmusicButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMusic();
			}
		});
		
		leftlocalButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftlocalButton.bringToFront();
					rightlocalButton.setSelected(true);
					leftlocalButton.setSelected(true);
					rightlocalButton.bringToFront();
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(580, 57, 0, 0);
					leftlocalButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(579, 57, 0, 0);
					leftlocalButton.setLayoutParams(params);
					rightlocalButton.setSelected(false);
					leftlocalButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftlocalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startLocal();
			}
		});

		leftsettingButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftsettingButton.bringToFront();
					leftsettingButton.setSelected(true);
					rightsettingButton.setSelected(true);
					rightsettingButton.bringToFront();
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(580, 293, 0, 0);
					leftsettingButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(579, 293, 0, 0);
					leftsettingButton.setLayoutParams(params);
					rightsettingButton.setSelected(false);
					leftsettingButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftsettingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSetting();
			}
		});

	}

	private void setRight() {

		rightlocalButton = (ImageView) findViewById(R.id.right_local_Button);
		rightmovieButton = (ImageView) findViewById(R.id.right_movie_Button);
		rightmusicButton = (ImageView) findViewById(R.id.right_music_Button);
		rightgameButton = (ImageView) findViewById(R.id.right_game_Button);
		rightappButton = (ImageView) findViewById(R.id.right_app_Button);
		rightsettingButton = (ImageView) findViewById(R.id.right_setting_Button);
		
		rightlocalButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startLocal();
			}
		});
		
		rightappButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startApp();
			}
		});
		
		rightgameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startGame();
			}
		});
		
		rightmovieButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovie();
			}
		});
		
		rightmusicButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMusic();
			}
		});
		
		rightsettingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSetting();
			}
		});
		
	}

	private void startMovie() {
		Intent intent = new Intent(getApplicationContext(),
				MovieClassificationActivity.class);
		startActivity(intent);
	}

	private void startGame() {
		Intent intent = new Intent(getApplicationContext(),
				GameClassificationActivity.class);
		startActivity(intent);
	}

	private void startApp() {
		Intent intent = new Intent(getApplicationContext(),
				AppClassificationActivity.class);
		startActivity(intent);
	}

	private void startMusic() {
		Intent intent = new Intent(MainActivity.this, MusicSelectActivity.class);
		startActivity(intent);
	}

	private void startLocal() {
		Intent intent = new Intent(MainActivity.this, LocalActivity.class);
		startActivity(intent);
	}

	private void startSetting() {
		Intent intent = new Intent(MainActivity.this, SettingActivity.class);
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
