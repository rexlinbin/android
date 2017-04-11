package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.Loader.ForceLoadContentObserver;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.MyDigitalClock;
import com.bccv.threedimensionalworld.view.MyTitleView;

@SuppressLint("NewApi")
public class MovieSearchActivity extends BaseActivity {
	LinearLayout leftLayout, rightLayout;

	private String[] areaStrings = { "全部", "中国大陆", "香港", "台湾", "日本", "韩国",
			"美国", "印度", "泰国", "欧洲" };
	private String[] typeStrings = { "全部", "院线", "动作", "冒险", "喜剧", "爱情", "战争",
			"恐怖", "犯罪", "悬疑", "惊悚", "武侠", "科幻", "音乐", "歌舞", "动画", "奇幻", "家庭",
			"剧情", "伦理", "记录", "历史", "传记" };
	private String[] yearStrings = { "全部", "2015", "2014", "2013", "2012",
			"2011", "2010", "2009", "2008", "2007", "2006", "更早" };

	private int[] leftAreaIds = { R.id.left_allarea_Button,
			R.id.left_china_Button, R.id.left_hongkong_Button,
			R.id.left_taiwan_Button, R.id.left_japan_Button,
			R.id.left_korea_Button, R.id.left_america_Button,
			R.id.left_india_Button, R.id.left_Thailand_Button,
			R.id.left_europe_Button };
	private int[] leftTypeIds = { R.id.left_alltype_Button,
			R.id.left_cinema_Button, R.id.left_action_Button,
			R.id.left_adventure_Button, R.id.left_comedy_Button,
			R.id.left_love_Button, R.id.left_war_Button,
			R.id.left_terror_Button, R.id.left_crime_Button,
			R.id.left_suspense_Button, R.id.left_panic_Button,
			R.id.left_emprise_Button, R.id.left_sciencefiction_Button,
			R.id.left_music_Button, R.id.left_dance_Button,
			R.id.left_cartoon_Button, R.id.left_strange_Button,
			R.id.left_family_Button, R.id.left_story_Button,
			R.id.left_ethic_Button, R.id.left_record_Button,
			R.id.left_history_Button, R.id.left_biography_Button };
	private int[] leftYearIds = { R.id.left_allyear_Button, R.id.left_0_Button,
			R.id.left_1_Button, R.id.left_2_Button, R.id.left_3_Button,
			R.id.left_4_Button, R.id.left_5_Button, R.id.left_6_Button,
			R.id.left_7_Button, R.id.left_8_Button, R.id.left_9_Button,
			R.id.left_10_Button };

	private List<Button> leftAreaList, leftTypeList, leftYearList;

	private int areaSelectNum = 0, typeSelectNum = 0, yearSelectNum = 0;

	private int[] rightAreaIds = { R.id.right_allarea_Button,
			R.id.right_china_Button, R.id.right_hongkong_Button,
			R.id.right_taiwan_Button, R.id.right_japan_Button,
			R.id.right_korea_Button, R.id.right_america_Button,
			R.id.right_india_Button, R.id.right_Thailand_Button,
			R.id.right_europe_Button };
	private int[] rightTypeIds = { R.id.right_alltype_Button,
			R.id.right_cinema_Button, R.id.right_action_Button,
			R.id.right_adventure_Button, R.id.right_comedy_Button,
			R.id.right_love_Button, R.id.right_war_Button,
			R.id.right_terror_Button, R.id.right_crime_Button,
			R.id.right_suspense_Button, R.id.right_panic_Button,
			R.id.right_emprise_Button, R.id.right_sciencefiction_Button,
			R.id.right_music_Button, R.id.right_dance_Button,
			R.id.right_cartoon_Button, R.id.right_strange_Button,
			R.id.right_family_Button, R.id.right_story_Button,
			R.id.right_ethic_Button, R.id.right_record_Button,
			R.id.right_history_Button, R.id.right_biography_Button };
	private int[] rightYearIds = { R.id.right_allyear_Button,
			R.id.right_0_Button, R.id.right_1_Button, R.id.right_2_Button,
			R.id.right_3_Button, R.id.right_4_Button, R.id.right_5_Button,
			R.id.right_6_Button, R.id.right_7_Button, R.id.right_8_Button,
			R.id.right_9_Button, R.id.right_10_Button };

	private List<Button> rightAreaList, rightTypeList, rightYearList;
	private TextView leftareaTextView, lefttypeTextView, leftyearTextView,
			rightareaTextView, righttypeTextView, rightyearTextView;
	private Button leftsureButton, rightsureButton;

	private String type_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moviesearch);
		type_id = getIntent().getStringExtra("type_id");
		Date curDate = new Date(System.currentTimeMillis());
		for (int i = 1; i < 11; i++) {
			yearStrings[i] = curDate.getYear() - i + "";
		}
		initBack();
		initStatus();
		initLeftButtons();
		initRightButtons();
		leftAreaList.get(0).requestFocus();
	}

	private void initBack() {
		Button leftbackButton = (Button) findViewById(R.id.left_back_button);
		final Button rightbackButton = (Button) findViewById(R.id.right_back_button);
		leftbackButton.setScaleX(0.5f);
		rightbackButton.setScaleX(0.5f);
		leftbackButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightbackButton.setSelected(hasFocus);
			}
		});
		
		
		leftbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rightbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		Button leftsearchButton = (Button) findViewById(R.id.left_search_button);
		final Button rightsearchButton = (Button) findViewById(R.id.right_search_button);
		leftsearchButton.setScaleX(0.5f);
		rightsearchButton.setScaleX(0.5f);
		leftsearchButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightsearchButton.setSelected(hasFocus);
			}
		});
		
		
		leftsearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startInputSearch();
			}
		});
		rightsearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startInputSearch();
			}
		});
	}

	private void initLeftButtons() {
		leftLayout = (LinearLayout) findViewById(R.id.left_layout);
		leftareaTextView = (TextView) findViewById(R.id.left_area_textView);
		lefttypeTextView = (TextView) findViewById(R.id.left_type_textView);
		leftyearTextView = (TextView) findViewById(R.id.left_year_textView);

		leftareaTextView.setScaleX(0.5f);
		lefttypeTextView.setScaleX(0.5f);
		leftyearTextView.setScaleX(0.5f);

		leftsureButton = (Button) findViewById(R.id.left_sure_button);
		leftsureButton.setScaleX(0.5f);
		leftsureButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightsureButton.setSelected(hasFocus);
			}
		});
		leftsureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String area = "";
				String year = "";
				String genres = "";
				if (areaSelectNum > 0) {
					area = areaStrings[areaSelectNum];
				}
				if (yearSelectNum > 0) {
					year = yearStrings[yearSelectNum];
				}
				if (typeSelectNum > 0) {
					genres = typeStrings[typeSelectNum];
				}

				Intent intent = new Intent(getApplicationContext(),
						MovieNoSearchListActivity.class);
				intent.putExtra("type_id", type_id);
				intent.putExtra("area", area);
				intent.putExtra("year", year);
				intent.putExtra("genres", genres);
				startActivity(intent);
				finish();
			}
		});

		leftAreaList = new ArrayList<Button>();
		leftTypeList = new ArrayList<Button>();
		leftYearList = new ArrayList<Button>();

		for (int i = 0; i < leftAreaIds.length; i++) {
			Button button = (Button) findViewById(leftAreaIds[i]);
			button.setScaleX(0.5f);

			leftAreaList.add(button);
			if (i == 0) {
				button.setBackgroundResource(R.drawable.movie_hover_select);
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				leftLayout.requestLayout();
			} else {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -38;
				button.setLayoutParams(params);
				leftLayout.requestLayout();
			}
			final int num = i;
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					leftAreaList.get(areaSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					v.setBackgroundResource(R.drawable.movie_hover_select);
					rightAreaList.get(areaSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					rightAreaList.get(num).setBackgroundResource(
							R.drawable.movie_hover_select);
					areaSelectNum = num;
				}
			});

			button.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					rightAreaList.get(num).setSelected(hasFocus);
				}
			});

		}

		for (int i = 0; i < leftTypeIds.length; i++) {
			Button button = (Button) findViewById(leftTypeIds[i]);
			button.setScaleX(0.5f);
			leftTypeList.add(button);
			if (i == 0) {
				button.setBackgroundResource(R.drawable.movie_hover_select);
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				leftLayout.requestLayout();
			} else if (i < 15) {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -38;
				button.setLayoutParams(params);
				leftLayout.requestLayout();
			} else if (i == 15) {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				leftLayout.requestLayout();
			}
			final int num = i;
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					leftTypeList.get(typeSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					v.setBackgroundResource(R.drawable.movie_hover_select);
					rightTypeList.get(typeSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					rightTypeList.get(num).setBackgroundResource(
							R.drawable.movie_hover_select);
					typeSelectNum = num;
				}
			});

			button.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					rightTypeList.get(num).setSelected(hasFocus);
				}
			});
		}

		for (int i = 0; i < leftYearIds.length; i++) {
			Button button = (Button) findViewById(leftYearIds[i]);
			button.setScaleX(0.5f);
			leftYearList.add(button);
			if (i == 0) {
				button.setBackgroundResource(R.drawable.movie_hover_select);
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				leftLayout.requestLayout();
			} else {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -38;
				button.setLayoutParams(params);
				leftLayout.requestLayout();
			}
			final int num = i;
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					leftYearList.get(yearSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					v.setBackgroundResource(R.drawable.movie_hover_select);
					rightYearList.get(yearSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					rightYearList.get(num).setBackgroundResource(
							R.drawable.movie_hover_select);
					yearSelectNum = num;
				}
			});
			button.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					rightYearList.get(num).setSelected(hasFocus);
				}
			});
		}

	}

	private void initRightButtons() {
		rightLayout = (LinearLayout) findViewById(R.id.right_layout);

		rightareaTextView = (TextView) findViewById(R.id.right_area_textView);
		righttypeTextView = (TextView) findViewById(R.id.right_type_textView);
		rightyearTextView = (TextView) findViewById(R.id.right_year_textView);

		rightareaTextView.setScaleX(0.5f);
		righttypeTextView.setScaleX(0.5f);
		rightyearTextView.setScaleX(0.5f);

		rightsureButton = (Button) findViewById(R.id.right_sure_button);
		rightsureButton.setScaleX(0.5f);
		rightsureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String area = "";
				String year = "";
				String genres = "";
				if (areaSelectNum > 0) {
					area = areaStrings[areaSelectNum];
				}
				if (yearSelectNum > 0) {
					if (yearSelectNum < 11) {
						year = yearStrings[yearSelectNum];
					} else {
						Date curDate = new Date(System.currentTimeMillis());
						year = curDate.getYear() - 11 + "";
					}

				}
				if (typeSelectNum > 0) {
					genres = typeStrings[typeSelectNum];
				}

				Intent intent = new Intent(getApplicationContext(),
						MovieNoSearchListActivity.class);
				intent.putExtra("type_id", type_id);
				intent.putExtra("area", area);
				intent.putExtra("year", year);
				intent.putExtra("genres", genres);
				startActivity(intent);
				finish();
			}
		});

		rightAreaList = new ArrayList<Button>();
		rightTypeList = new ArrayList<Button>();
		rightYearList = new ArrayList<Button>();

		for (int i = 0; i < rightAreaIds.length; i++) {
			Button button = (Button) findViewById(rightAreaIds[i]);
			button.setScaleX(0.5f);

			rightAreaList.add(button);
			if (i == 0) {
				button.setBackgroundResource(R.drawable.movie_hover_select);
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				rightLayout.requestLayout();
			} else {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -38;
				button.setLayoutParams(params);
				rightLayout.requestLayout();
			}
			final int num = i;
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					leftAreaList.get(areaSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					v.setBackgroundResource(R.drawable.movie_hover_select);
					rightAreaList.get(areaSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					leftAreaList.get(num).setBackgroundResource(
							R.drawable.movie_hover_select);
					areaSelectNum = num;
				}
			});
		}

		for (int i = 0; i < rightTypeIds.length; i++) {
			Button button = (Button) findViewById(rightTypeIds[i]);
			button.setScaleX(0.5f);
			rightTypeList.add(button);
			if (i == 0) {
				button.setBackgroundResource(R.drawable.movie_hover_select);
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				rightLayout.requestLayout();
			} else if (i < 15) {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -38;
				button.setLayoutParams(params);
				rightLayout.requestLayout();
			} else if (i == 15) {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				rightLayout.requestLayout();
			}
			final int num = i;
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					leftTypeList.get(typeSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					v.setBackgroundResource(R.drawable.movie_hover_select);
					rightTypeList.get(typeSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					leftTypeList.get(num).setBackgroundResource(
							R.drawable.movie_hover_select);
					typeSelectNum = num;
				}
			});

		}

		for (int i = 0; i < rightYearIds.length; i++) {
			Button button = (Button) findViewById(rightYearIds[i]);
			button.setScaleX(0.5f);
			rightYearList.add(button);
			if (i == 0) {
				button.setBackgroundResource(R.drawable.movie_hover_select);
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -25;
				button.setLayoutParams(params);
				rightLayout.requestLayout();
			} else {
				LayoutParams params = (LayoutParams) button.getLayoutParams();
				params.leftMargin = -38;
				button.setLayoutParams(params);
				rightLayout.requestLayout();
			}
			final int num = i;
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					leftYearList.get(yearSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					v.setBackgroundResource(R.drawable.movie_hover_select);
					rightYearList.get(yearSelectNum).setBackgroundResource(
							R.drawable.movie_hover);
					leftYearList.get(num).setBackgroundResource(
							R.drawable.movie_hover_select);
					yearSelectNum = num;
				}
			});

		}

	}

	private MyTitleView leftMyTitleView, rightMyTitleView;

	private void initStatus() {
		leftMyTitleView = (MyTitleView) findViewById(R.id.left_myTitleView);
		rightMyTitleView = (MyTitleView) findViewById(R.id.right_myTitleView);

	}

	private void startInputSearch() {
		Intent intent = new Intent(getApplicationContext(),
				MovieSearchInputActivity.class);
		intent.putExtra("type_id", type_id);
		startActivity(intent);
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
