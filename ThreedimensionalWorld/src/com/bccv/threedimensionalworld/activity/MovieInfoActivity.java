package com.bccv.threedimensionalworld.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.core.BVideoView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.api.MovieApi;
import com.bccv.threedimensionalworld.model.Movie;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.SerializationUtil;
import com.bccv.threedimensionalworld.view.MyTitleView;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class MovieInfoActivity extends BaseActivity {
	private ImageView leftImageView, rightImageView, bgImageView;
	private Button leftplayButton, leftdownloadButton, leftcollectButton,
			leftintroButton, rightplayButton, rightdownloadButton,
			rightcollectButton, rightintroButton;
	private RatingBar leftRatingBar, rightRatingBar;
	private TextView lefttitleTextView, leftscoreTextView, leftrolesTextView,
			leftdirectorTextView, lefttypeTextView, leftareaTextView,
			leftyearTextView, leftinfoTextView, leftstatusTextView,
			righttitleTextView, rightscoreTextView, rightrolesTextView,
			rightdirectorTextView, righttypeTextView, rightareaTextView,
			rightyearTextView, rightinfoTextView, rightstatusTextView;
	private LinearLayout leftinfoLayout, rightinfoLayout;

	private Movie movie;
	private String movie_id = "44";
	private boolean is2D = false;
	private List<Movie> collectList, historyList;
	private boolean isCollect = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movieinfo);
		movie_id = getIntent().getStringExtra("movie_id");
		is2D = getIntent().getBooleanExtra("is2D", false);
		isCollect = checkCollect();
		setLeftInfo();
		setRightInfo();
		initLeftButton();
		initRightButton();
		setStatus();
		getData();
	}

	private void setLeftInfo() {
		bgImageView = (ImageView) findViewById(R.id.bg_imageView);
		leftImageView = (ImageView) findViewById(R.id.left_movie_ImageView);

		leftRatingBar = (RatingBar) findViewById(R.id.left_ratingBar);
		leftRatingBar.setScaleX(0.5f);

		lefttitleTextView = (TextView) findViewById(R.id.left_title_textView);
		leftscoreTextView = (TextView) findViewById(R.id.left_score_textView);
		leftrolesTextView = (TextView) findViewById(R.id.left_roles_textView);
		leftdirectorTextView = (TextView) findViewById(R.id.left_director_textView);
		lefttypeTextView = (TextView) findViewById(R.id.left_type_textView);
		leftareaTextView = (TextView) findViewById(R.id.left_area_textView);
		leftyearTextView = (TextView) findViewById(R.id.left_year_textView);

		leftinfoLayout = (LinearLayout) findViewById(R.id.left_info_layout);
		leftinfoTextView = (TextView) findViewById(R.id.left_info_textView);

		AssetManager mgr = getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		leftscoreTextView.setTypeface(tf);// 设置字体
	}

	private void initLeftButton() {
		leftplayButton = (Button) findViewById(R.id.left_play_button);
		leftcollectButton = (Button) findViewById(R.id.left_collect_button);
		leftintroButton = (Button) findViewById(R.id.left_intro_button);
		leftplayButton.requestFocus();
	}

	private void setLeftButton() {

		leftplayButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightplayButton.setSelected(hasFocus);
			}
		});
		leftplayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setPlay();
			}
		});

		leftcollectButton.setSelected(isCollect);
		leftcollectButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (isCollect) {
					leftcollectButton.setSelected(true);
					rightcollectButton.setSelected(true);
				} else {
					rightcollectButton.setSelected(hasFocus);
				}

			}
		});
		leftcollectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isSelect = !leftcollectButton.isSelected();
				leftcollectButton.setSelected(isSelect);
				rightcollectButton.setSelected(isSelect);
				setCollect(isSelect);
				
				boolean hasFocus = false;
				leftintroButton.setSelected(hasFocus);
				rightintroButton.setSelected(hasFocus);
				if (hasFocus) {
					leftinfoLayout.setVisibility(View.VISIBLE);
					rightinfoLayout.setVisibility(View.VISIBLE);
				} else {
					leftinfoLayout.setVisibility(View.INVISIBLE);
					rightinfoLayout.setVisibility(View.INVISIBLE);
				}
			}
		});

		leftintroButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightintroButton.setSelected(hasFocus);
				if (hasFocus) {
					leftinfoLayout.setVisibility(View.VISIBLE);
					rightinfoLayout.setVisibility(View.VISIBLE);
				} else {
					leftinfoLayout.setVisibility(View.INVISIBLE);
					rightinfoLayout.setVisibility(View.INVISIBLE);
				}

			}
		});

		leftintroButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean hasFocus = !leftintroButton.isSelected();
				leftintroButton.setSelected(hasFocus);
				rightintroButton.setSelected(hasFocus);
				if (hasFocus) {
					leftinfoLayout.setVisibility(View.VISIBLE);
					rightinfoLayout.setVisibility(View.VISIBLE);
				} else {
					leftinfoLayout.setVisibility(View.INVISIBLE);
					rightinfoLayout.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void setRightInfo() {
		rightImageView = (ImageView) findViewById(R.id.right_movie_ImageView);

		rightRatingBar = (RatingBar) findViewById(R.id.right_ratingBar);
		rightRatingBar.setScaleX(0.5f);

		righttitleTextView = (TextView) findViewById(R.id.right_title_textView);
		rightscoreTextView = (TextView) findViewById(R.id.right_score_textView);
		rightrolesTextView = (TextView) findViewById(R.id.right_roles_textView);
		rightdirectorTextView = (TextView) findViewById(R.id.right_director_textView);
		righttypeTextView = (TextView) findViewById(R.id.right_type_textView);
		rightareaTextView = (TextView) findViewById(R.id.right_area_textView);
		rightyearTextView = (TextView) findViewById(R.id.right_year_textView);

		rightinfoLayout = (LinearLayout) findViewById(R.id.right_info_layout);
		rightinfoTextView = (TextView) findViewById(R.id.right_info_textView);

		AssetManager mgr = getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		rightscoreTextView.setTypeface(tf);// 设置字体
	}

	private void initRightButton() {
		rightplayButton = (Button) findViewById(R.id.right_play_button);
		rightcollectButton = (Button) findViewById(R.id.right_collect_button);
		rightintroButton = (Button) findViewById(R.id.right_intro_button);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
			}
		};
		timer.schedule(task, 10);
	}

	private void setRightButton() {

		rightplayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setPlay();
			}
		});
		rightcollectButton.setSelected(isCollect);
		// rightcollectButton.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// boolean isSelect = !rightcollectButton.isSelected();
		// leftcollectButton.setSelected(isSelect);
		// rightcollectButton.setSelected(isSelect);
		// setCollect(isSelect);
		// }
		// });
	}

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			if (leftplayButton.isFocused()) {
				rightplayButton.setSelected(true);
			}
		};
	};

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (movie != null) {
					setData();
				} else {
					movie = new Movie();
					setData();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieApi movieApi = new MovieApi();
				movie = movieApi.getMovieInfo(movie_id, is2D);
				return null;
			}
		}.execute("");
	}

	private void setData() {
		lefttitleTextView.setText(movie.getTitle());
		leftRatingBar.setStepSize(0.5f);
		leftRatingBar.setRating((movie.getRating() - 0.5f) / 2);
		leftscoreTextView.setText(movie.getRating() + "分");
		leftrolesTextView.setText("主演: " + movie.getCasts());
		leftdirectorTextView.setText("导演: " + movie.getDirectors());
		lefttypeTextView.setText("类型: " + movie.getGenres_name());
		leftareaTextView.setText("地区: " + movie.getCountries());
		leftyearTextView.setText("年代: " + movie.getYear());
		leftinfoTextView.setText("简介: \n" + movie.getSummary());

		lefttitleTextView.setScaleX(0.5f);
		leftRatingBar.setScaleX(0.5f);
		leftscoreTextView.setScaleX(0.5f);
		leftrolesTextView.setScaleX(0.5f);
		leftdirectorTextView.setScaleX(0.5f);
		lefttypeTextView.setScaleX(0.5f);
		leftareaTextView.setScaleX(0.5f);
		leftyearTextView.setScaleX(0.5f);
		leftinfoTextView.setScaleX(0.5f);

		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(movie.getImages(), leftImageView,
				GlobalParams.options);

		// imageLoader.displayImage(movie.getBackground(), bgImageView);

		righttitleTextView.setText(movie.getTitle());
		rightRatingBar.setStepSize(0.5f);
		rightRatingBar.setRating((movie.getRating() - 0.5f) / 2);
		rightscoreTextView.setText(movie.getRating() + "分");
		rightrolesTextView.setText("主演: " + movie.getCasts());
		rightdirectorTextView.setText("导演: " + movie.getDirectors());
		righttypeTextView.setText("类型: " + movie.getGenres_name());
		rightareaTextView.setText("地区: " + movie.getCountries());
		rightyearTextView.setText("年代: " + movie.getYear());
		rightinfoTextView.setText("简介: \n" + movie.getSummary());

		righttitleTextView.setScaleX(0.5f);
		rightRatingBar.setScaleX(0.5f);
		rightscoreTextView.setScaleX(0.5f);
		rightrolesTextView.setScaleX(0.5f);
		rightdirectorTextView.setScaleX(0.5f);
		righttypeTextView.setScaleX(0.5f);
		rightareaTextView.setScaleX(0.5f);
		rightyearTextView.setScaleX(0.5f);
		rightinfoTextView.setScaleX(0.5f);

		imageLoader.displayImage(movie.getImages(), rightImageView,
				GlobalParams.options);

		setLeftButton();
		setRightButton();

	}

	private void setPlay() {
		setHistory();
		if (is2D) {
			Intent intent = new Intent(getApplicationContext(),
					Video2DPlayerActivity.class);
			intent.putExtra("title", movie.getTitle());
			intent.putExtra("movie_id", movie_id);
			intent.putExtra("is2D", true);
			startActivity(intent);
		} else {
			Intent intent = new Intent(getApplicationContext(),
					VideoPlayerActivity.class);
			intent.putExtra("title", movie.getTitle());
			intent.putExtra("movie_id", movie_id);
			startActivity(intent);
		}

		finish();
	}

	private void setCollect(boolean isSelect) {
		collectList = SerializationUtil
				.readCollectCache(getApplicationContext());
		if (collectList == null) {
			collectList = new ArrayList<Movie>();
		}
		if (isSelect) {
			isCollect = true;
			movie.setIs2D(is2D);
			collectList.add(movie);
			SerializationUtil.wirteCollectSerialization(
					getApplicationContext(), (Serializable) collectList);
		} else {
			for (Movie iterable_element : collectList) {
				if (iterable_element.getId().equals(movie_id)
						&& iterable_element.isIs2D() == is2D) {
					collectList.remove(iterable_element);
					isCollect = false;
					break;
				}
			}
			SerializationUtil.wirteCollectSerialization(
					getApplicationContext(), (Serializable) collectList);
		}
	}

	private void setHistory() {
		historyList = SerializationUtil
				.readHistoryCache(getApplicationContext());
		if (historyList == null) {
			historyList = new ArrayList<Movie>();
		}
		movie.setIs2D(is2D);
		for (int i = 0; i < historyList.size(); i++) {
			Movie itemMovie = historyList.get(i);
			if (itemMovie.isIs2D() == is2D
					&& itemMovie.getId().equals(movie_id)) {
				historyList.remove(i);
				historyList.add(0, movie);
				SerializationUtil.wirteHistorySerialization(
						getApplicationContext(), (Serializable) historyList);
				return;
			}
		}
		if (historyList.size() >= 30) {
			historyList.remove(29);
		}
		historyList.add(0, movie);
		SerializationUtil.wirteHistorySerialization(getApplicationContext(),
				(Serializable) historyList);
	}

	private boolean checkCollect() {
		collectList = SerializationUtil
				.readCollectCache(getApplicationContext());
		if (collectList == null) {
			collectList = new ArrayList<Movie>();
		}

		for (Movie iterable_element : collectList) {
			if (iterable_element.getId().equals(movie_id)
					&& iterable_element.isIs2D() == is2D) {
				return true;
			}
		}

		return false;
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
