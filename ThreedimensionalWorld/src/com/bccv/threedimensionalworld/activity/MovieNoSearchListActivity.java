package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baidu.cyberplayer.core.BVideoView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.MovieListAdapter;
import com.bccv.threedimensionalworld.api.MovieApi;
import com.bccv.threedimensionalworld.model.Movie;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.PromptManager;
import com.bccv.threedimensionalworld.view.MyTitleView;
import com.bccv.threedimensionalworld.view.MyToast;

@SuppressLint("NewApi")
public class MovieNoSearchListActivity extends BaseActivity {
	
	private GridView leftGridView, rightGridView;
	private List<Movie> list, getList;
	private MovieListAdapter leftAdapter, rightAdapter;
	private Button leftBackButton, rightBackButton;
	private TextView leftnosearchTextView, rightnosearchTextView;

	private String type_id = "1";
	private String area = "";
	private String year = "";
	private String genres = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movienosearch);
		type_id = getIntent().getStringExtra("type_id");
		area = getIntent().getStringExtra("area");
		year = getIntent().getStringExtra("year");
		genres = getIntent().getStringExtra("genres");
		list = new ArrayList<Movie>();
		leftAdapter = new MovieListAdapter(getApplicationContext(), list, true);
		rightAdapter = new MovieListAdapter(getApplicationContext(), list, false);
		setStatus();

		setLeft();
		setRight();

		setData();
	}


	private void setLeft() {
		leftnosearchTextView = (TextView) findViewById(R.id.left_nosearch_textView); 
		leftnosearchTextView.setScaleX(0.5f);
		leftGridView = (GridView) findViewById(R.id.left_gridView);
		leftGridView.requestFocus();
		leftGridView.setAdapter(leftAdapter);
		leftGridView.setVerticalScrollBarEnabled(false);
		leftGridView
				.setSelector(new ColorDrawable(android.R.color.transparent));
		leftGridView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				leftAdapter.setSelect(position);
				rightAdapter.setSelect(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		leftGridView.setOnGenericMotionListener(new OnGenericMotionListener() {
			
			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		leftGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MovieInfoActivity.class);
				intent.putExtra("movie_id", list.get(position).getId());
				startActivity(intent);
			}
		});
		leftGridView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				rightGridView.onTouchEvent(event);
				return false;
			}
		});

		leftGridView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				rightGridView.onKeyDown(keyCode, event);
				return false;
			}
		});

		leftBackButton = (Button) findViewById(R.id.left_back_button);
		leftBackButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightBackButton.setSelected(hasFocus);
			}
		});

		leftBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setRight() {
		rightnosearchTextView = (TextView) findViewById(R.id.right_nosearch_textView); 
		rightnosearchTextView.setScaleX(0.5f);
		rightGridView = (GridView) findViewById(R.id.right_gridView);
		rightGridView.setAdapter(rightAdapter);
		rightGridView.setVerticalScrollBarEnabled(false);
		rightGridView
				.setSelector(new ColorDrawable(android.R.color.transparent));
		rightBackButton = (Button) findViewById(R.id.right_back_button);
		rightBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null && getList.size() > 0) {
					list.addAll(getList);
					leftAdapter.notifyDataSetChanged();
					rightAdapter.notifyDataSetChanged();
				}else {
//					MyToast.makeText(getApplicationContext(), "未搜索到相关影片", 1).show();
					leftnosearchTextView.setText("未搜索到相关影片");
					rightnosearchTextView.setText("未搜索到相关影片");
					leftnosearchTextView.setVisibility(View.VISIBLE);
					rightnosearchTextView.setVisibility(View.VISIBLE);
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieApi movieApi = new MovieApi();
				getList = movieApi.getSearchMovieList(type_id, area, year, genres);
				return null;
			}
		}.execute("");
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