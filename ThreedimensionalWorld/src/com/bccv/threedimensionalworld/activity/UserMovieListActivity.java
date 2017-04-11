package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.MovieListAdapter;
import com.bccv.threedimensionalworld.api.MovieApi;
import com.bccv.threedimensionalworld.model.Movie;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;

public class UserMovieListActivity extends BaseActivity {
	private TextView leftStatusTextView;
	private TextView rightStatusTextView;
	private GridView leftGridView, rightGridView;
	private List<Movie> list, getList;
	private MovieListAdapter adapter;
	private int currNum = 0;
	private Button leftSearchButton, leftBackButton, rightSearchButton, rightBackButton;
	private String type_id = "1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movielist);
		type_id = getIntent().getStringExtra("type_id");
		list = new ArrayList<Movie>();
		adapter = new MovieListAdapter(getApplicationContext(), list);
		setStatus();
		
		setLeft();
		setRight();
		
		setData();
	}

	private void setStatus() {
		leftStatusTextView = (TextView) findViewById(R.id.left_status_textView);
		rightStatusTextView = (TextView) findViewById(R.id.right_status_textView);
	}

	private void setLeft() {
		leftGridView = (GridView) findViewById(R.id.left_gridView);
		leftGridView.requestFocus();
		leftGridView.setAdapter(adapter);
		leftGridView
				.setSelector(new ColorDrawable(android.R.color.transparent));
		leftGridView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				adapter.setSelect(position);
				currNum = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		leftGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
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
		
		leftBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setRight() {
		rightGridView = (GridView) findViewById(R.id.right_gridView);
		rightGridView.setAdapter(adapter);
		rightGridView
		.setSelector(new ColorDrawable(android.R.color.transparent));
		rightBackButton = (Button) findViewById(R.id.right_back_button);
		rightSearchButton = (Button) findViewById(R.id.right_search_button);
	}

	private void setData() {
		
		Callback callback = new Callback() {
			
			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					list.addAll(getList);
					adapter.notifyDataSetChanged();
				}
			}
		};
		
		new DataAsyncTask(callback, false) {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieApi movieApi = new MovieApi();
				getList = movieApi.getMovieList(type_id, 1, 1000000);
				return null;
			}
		}.execute("");
	}
}
