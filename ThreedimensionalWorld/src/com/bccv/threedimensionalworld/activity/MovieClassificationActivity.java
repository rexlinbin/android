package com.bccv.threedimensionalworld.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.view.MyTitleView;

@SuppressLint("NewApi")
public class MovieClassificationActivity extends BaseActivity {
	private Button left3dfilmButton, left3dbeautyButton, leftexperienceButton,
			leftrecordButton, left3dmusicButton, left2dButton, leftuserButton;
	private Button right3dfilmButton, right3dbeautyButton,
			rightexperienceButton, rightrecordButton, right3dmusicButton,
			right2dButton, rightuserButton;
	private TextView leftStatusTextView;
	private TextView rightStatusTextView;
	private RelativeLayout leftLayout, rightLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movieclassification);
		rightLayout = (RelativeLayout) findViewById(R.id.right_relativeLayout);
		leftLayout = (RelativeLayout) findViewById(R.id.left_relativeLayout);
		setLeft();
		setRight();
		setStatus();
	}

	private void setLeft() {
		left3dfilmButton = (Button) findViewById(R.id.left_3dfilm_Button);
		left3dbeautyButton = (Button) findViewById(R.id.left_3dbeauty_Button);
		leftexperienceButton = (Button) findViewById(R.id.left_experience_Button);
		leftrecordButton = (Button) findViewById(R.id.left_record_Button);
		left3dmusicButton = (Button) findViewById(R.id.left_3dmusic_Button);
		left2dButton = (Button) findViewById(R.id.left_2d_Button);
		leftuserButton = (Button) findViewById(R.id.left_user_Button);

		left2dButton.setVisibility(View.GONE);
		leftuserButton.setVisibility(View.GONE);
		
		left3dfilmButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					left3dfilmButton.bringToFront();
					left3dfilmButton.setSelected(true);
					right3dfilmButton.setSelected(true);
					right3dfilmButton.bringToFront();

					LayoutParams params = (LayoutParams) left3dfilmButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					left3dfilmButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					right3dfilmButton.setSelected(false);
					left3dfilmButton.setSelected(false);

					LayoutParams params = (LayoutParams) left3dfilmButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					left3dfilmButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		left3dfilmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("1");
			}
		});

		left3dbeautyButton
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							left3dbeautyButton.bringToFront();
							left3dbeautyButton.setSelected(true);
							right3dbeautyButton.setSelected(true);
							right3dbeautyButton.bringToFront();

							LayoutParams params = (LayoutParams) left3dbeautyButton
									.getLayoutParams();
							params.leftMargin = params.leftMargin + 1;
							left3dbeautyButton.setLayoutParams(params);
							leftLayout.requestLayout();
							rightLayout.requestLayout();
						} else {
							right3dbeautyButton.setSelected(false);
							left3dbeautyButton.setSelected(false);

							LayoutParams params = (LayoutParams) left3dbeautyButton
									.getLayoutParams();
							params.leftMargin = params.leftMargin - 1;
							left3dbeautyButton.setLayoutParams(params);
							leftLayout.requestLayout();
							rightLayout.requestLayout();
						}
					}
				});

		left3dbeautyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("5");
			}
		});

		leftexperienceButton
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							leftexperienceButton.bringToFront();
							leftexperienceButton.setSelected(true);
							rightexperienceButton.setSelected(true);
							rightexperienceButton.bringToFront();

							LayoutParams params = (LayoutParams) leftexperienceButton
									.getLayoutParams();
							params.leftMargin = params.leftMargin + 1;
							leftexperienceButton.setLayoutParams(params);
							leftLayout.requestLayout();
							rightLayout.requestLayout();
						} else {
							rightexperienceButton.setSelected(false);
							leftexperienceButton.setSelected(false);

							LayoutParams params = (LayoutParams) leftexperienceButton
									.getLayoutParams();
							params.leftMargin = params.leftMargin - 1;
							leftexperienceButton.setLayoutParams(params);
							leftLayout.requestLayout();
							rightLayout.requestLayout();
						}
					}
				});

		leftexperienceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), MyMovieListActivity.class);
				intent.putExtra("isCollect", true);
				startActivity(intent);
			}
		});

		leftrecordButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftrecordButton.bringToFront();
					leftrecordButton.setSelected(true);
					rightrecordButton.setSelected(true);
					rightrecordButton.bringToFront();

					LayoutParams params = (LayoutParams) leftrecordButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					leftrecordButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					rightrecordButton.setSelected(false);
					leftrecordButton.setSelected(false);

					LayoutParams params = (LayoutParams) leftrecordButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					leftrecordButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		leftrecordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), MyMovieListActivity.class);
				intent.putExtra("isCollect", false);
				startActivity(intent);
			}
		});

		left3dmusicButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					left3dmusicButton.bringToFront();
					left3dmusicButton.setSelected(true);
					right3dmusicButton.setSelected(true);
					right3dmusicButton.bringToFront();

					LayoutParams params = (LayoutParams) left3dmusicButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					left3dmusicButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					right3dmusicButton.setSelected(false);
					left3dmusicButton.setSelected(false);

					LayoutParams params = (LayoutParams) left3dmusicButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					left3dmusicButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		left3dmusicButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startMovieList("5");
				startMovieList("4");
			}
		});

		left2dButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					left2dButton.bringToFront();
					left2dButton.setSelected(true);
					right2dButton.setSelected(true);
					right2dButton.bringToFront();

					LayoutParams params = (LayoutParams) left2dButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					left2dButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					right2dButton.setSelected(false);
					left2dButton.setSelected(false);

					LayoutParams params = (LayoutParams) left2dButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					left2dButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		left2dButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("4");
			}
		});

		leftuserButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftuserButton.bringToFront();
					leftuserButton.setSelected(true);
					rightuserButton.setSelected(true);
					rightuserButton.bringToFront();

					LayoutParams params = (LayoutParams) leftuserButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin + 1;
					leftuserButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					rightuserButton.setSelected(false);
					leftuserButton.setSelected(false);

					LayoutParams params = (LayoutParams) leftuserButton
							.getLayoutParams();
					params.leftMargin = params.leftMargin - 1;
					leftuserButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});

		leftuserButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void setRight() {
		right3dfilmButton = (Button) findViewById(R.id.right_3dfilm_Button);
		right3dbeautyButton = (Button) findViewById(R.id.right_3dbeauty_Button);
		rightexperienceButton = (Button) findViewById(R.id.right_experience_Button);
		rightrecordButton = (Button) findViewById(R.id.right_record_Button);
		right3dmusicButton = (Button) findViewById(R.id.right_3dmusic_Button);
		right2dButton = (Button) findViewById(R.id.right_2d_Button);
		rightuserButton = (Button) findViewById(R.id.right_user_Button);

		right2dButton.setVisibility(View.GONE);
		rightuserButton.setVisibility(View.GONE);
		
		right3dfilmButton.setFocusable(false);
		right3dfilmButton.setFocusableInTouchMode(false);
		right3dbeautyButton.setFocusable(false);
		right3dbeautyButton.setFocusableInTouchMode(false);
		rightexperienceButton.setFocusable(false);
		rightexperienceButton.setFocusableInTouchMode(false);
		rightrecordButton.setFocusable(false);
		rightrecordButton.setFocusableInTouchMode(false);
		right3dmusicButton.setFocusable(false);
		right3dmusicButton.setFocusableInTouchMode(false);
		
		right3dfilmButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("1");
			}
		});

		right3dbeautyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("5");
			}
		});

		rightexperienceButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("2");
			}
		});

		rightrecordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("6");
			}
		});

		right3dmusicButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startMovieList("5");
				startMovieList("4");
			}
		});

		right2dButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovieList("4");
			}
		});

		rightuserButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void startMovieList(String type_id) {
		Intent intent = new Intent(getApplicationContext(), MovieListActivity.class);
		intent.putExtra("type_id", type_id);
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
