package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.MovieListAdapter;
import com.bccv.threedimensionalworld.api.MovieApi;
import com.bccv.threedimensionalworld.model.Movie;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.view.MyTitleView;

@SuppressLint("NewApi")
public class MovieListActivity extends BaseActivity {
	private TextView leftStatusTextView;
	private TextView rightStatusTextView;
	private GridView leftGridView, rightGridView;
	private List<Movie> list, getList;
	private MovieListAdapter leftAdapter, rightAdapter;
	private int currNum = 0;
	private Button leftSearchButton, leftBackButton, rightSearchButton,
			rightBackButton, leftcreeningButton, rightcreeningButton;
	private boolean isSearch = false;
	private boolean isBack = false;
	private ImageView leftBg, RightBg;
	private String type_id = "1";
	private int count = 30, pageNum = 1;
	private boolean isGettingData = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_movielist);
		type_id = getIntent().getStringExtra("type_id");
		list = new ArrayList<Movie>();
		leftAdapter = new MovieListAdapter(getApplicationContext(), list, true);
		rightAdapter = new MovieListAdapter(getApplicationContext(), list,
				false);
		setStatus();

		setLeft();
		setRight();

		setData();
	}

	private void setLeft() {
		leftGridView = (GridView) findViewById(R.id.left_gridView);
		leftBg = (ImageView) findViewById(R.id.left_myBg);
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
				// leftAdapter.setSelect(position);
				// rightAdapter.setSelect(position);
				currNum = position;
				if (currNum > list.size() / 3 - 1) {
					setData();
				}
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
				Intent intent = new Intent(getApplicationContext(),
						MovieInfoActivity.class);
				intent.putExtra("movie_id", list.get(position).getId());
				if (type_id.equals("4")) {
					intent.putExtra("is2D", true);
				}
				startActivity(intent);
			}
		});

		leftGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem > list.size() / 3 - 1) {
					setData();
				}
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

		leftGridView.setOnGenericMotionListener(new OnGenericMotionListener() {

			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				rightGridView.onGenericMotionEvent(arg1);
				return false;
			}
		});

		leftcreeningButton = (Button) findViewById(R.id.left_creening_button);
		leftcreeningButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MovieSearchActivity.class);
				intent.putExtra("type_id", type_id);
				startActivity(intent);
			}
		});

		leftcreeningButton
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (!hasFocus) {
							rightcreeningButton.setSelected(false);
							isSearch = false;
						} else {
							rightcreeningButton.setSelected(true);
						}
					}
				});

		leftBackButton = (Button) findViewById(R.id.left_back_button);
		// leftBackButton.setFocusable(false);
		// leftBackButton.setFocusableInTouchMode(false);
		leftSearchButton = (Button) findViewById(R.id.left_search_button);
		leftSearchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MovieSearchActivity.class);
				intent.putExtra("type_id", type_id);
				startActivity(intent);
			}
		});
		leftSearchButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					leftSearchButton.setVisibility(View.GONE);
					rightSearchButton.setVisibility(View.GONE);
					isSearch = false;
				} else {
					rightSearchButton.setSelected(true);
				}
			}
		});
		leftSearchButton.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				rightSearchButton.onKeyDown(keyCode, event);
				return false;
			}
		});
		leftSearchButton.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				rightSearchButton.onTouchEvent(event);
				return false;
			}
		});

		leftBackButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightBackButton.setSelected(hasFocus);
				if (hasFocus) {
					isBack = true;
				} else {
					isBack = false;
				}
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
		rightGridView = (GridView) findViewById(R.id.right_gridView);
		RightBg = (ImageView) findViewById(R.id.right_myBg);
		rightGridView.setAdapter(rightAdapter);
		rightGridView.setVerticalScrollBarEnabled(false);
		rightGridView
				.setSelector(new ColorDrawable(android.R.color.transparent));

		rightGridView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				leftGridView.onTouchEvent(event);
				return false;
			}
		});

		rightGridView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				leftGridView.onKeyDown(keyCode, event);
				return false;
			}
		});

		rightGridView.setOnGenericMotionListener(new OnGenericMotionListener() {

			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				leftGridView.onGenericMotionEvent(arg1);
				return false;
			}
		});

		rightcreeningButton = (Button) findViewById(R.id.right_creening_button);
		rightcreeningButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						MovieSearchActivity.class);
				intent.putExtra("type_id", type_id);
				startActivity(intent);
			}
		});

		rightBackButton = (Button) findViewById(R.id.right_back_button);
		// rightBackButton.setFocusable(false);
		// rightBackButton.setFocusableInTouchMode(false);
		rightSearchButton = (Button) findViewById(R.id.right_search_button);
		rightBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void setData() {
		if (isGettingData) {
			return;
		}
		isGettingData = true;
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					list.addAll(getList);
					leftAdapter.notifyDataSetChanged();
					rightAdapter.notifyDataSetChanged();
					pageNum++;
				}
				isGettingData = false;
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieApi movieApi = new MovieApi();
				getList = movieApi.getMovieList(type_id, pageNum, count);
				return null;
			}
		}.execute("");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (currNum > 0 && currNum < 6) {
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BUTTON_Y) {
			Intent intent = new Intent(getApplicationContext(),
					MovieSearchActivity.class);
			intent.putExtra("type_id", type_id);
			startActivity(intent);
			return true;
		}

		 if (keyCode == KeyEvent.KEYCODE_BUTTON_X) {
		 Intent intent = new Intent(getApplicationContext(),
		 MovieSearchInputActivity.class);
		 intent.putExtra("type_id", type_id);
		 startActivity(intent);
		 return true;
		 }

		// if (!isBack) {
		// leftSearchButton.setVisibility(View.VISIBLE);
		// leftSearchButton.requestFocus();
		// rightSearchButton.setVisibility(View.VISIBLE);
		// isSearch = true;
		// return true;
		// }
		// }
		return super.onKeyDown(keyCode, event);
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
