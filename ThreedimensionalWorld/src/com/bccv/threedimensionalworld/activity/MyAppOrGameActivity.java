package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.AppListAdapter;
import com.bccv.threedimensionalworld.api.GameApi;
import com.bccv.threedimensionalworld.download.DownloadInfo;
import com.bccv.threedimensionalworld.download.DownloadManager;
import com.bccv.threedimensionalworld.download.DownloadService;
import com.bccv.threedimensionalworld.model.App;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.SerializationUtil;
import com.bccv.threedimensionalworld.view.MyTitleView;
import com.lidroid.xutils.http.HttpHandler.State;

@SuppressLint("NewApi")
public class MyAppOrGameActivity extends BaseActivity {
	private GridView leftGridView, rightGridView;
	private List<App> list, getList;
	private AppListAdapter leftAdapter;
	private int currNum = 0;
	private Button leftBackButton, rightBackButton;
	private String type_id = "1";
	private boolean isGame = false;
	private int pageNum = 1, count = 30;
	private boolean isGettingData = false;
	private DownloadManager downloadManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apporgamelist);
		isGame = getIntent().getBooleanExtra("isGame", false);
		type_id = getIntent().getStringExtra("type_id");
		list = new ArrayList<App>();
		leftAdapter = new AppListAdapter(getApplicationContext(), list);
		// rightAdapter = new AppListAdapter(getApplicationContext(), list);
		setStatus();
		setLeft();
		setRight();
		setData();
	}

	private void setLeft() {
		if (isGame) {
			ImageView leftImageView = (ImageView) findViewById(R.id.left_myBg);
			leftImageView.setBackgroundResource(R.drawable.game_bg2);
		} else {
			ImageView leftImageView = (ImageView) findViewById(R.id.left_myBg);
			leftImageView.setBackgroundResource(R.drawable.app_bg);
		}

		leftGridView = (GridView) findViewById(R.id.left_gridView);
		leftGridView.requestFocus();
		leftGridView.setAdapter(leftAdapter);
		leftGridView.setVerticalScrollBarEnabled(false);
		leftGridView
				.setSelector(new ColorDrawable(android.R.color.transparent));
		// leftGridView.setSelector(getResources().getDrawable(
		// R.drawable.game_frame));
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
						AppInfoActivity.class);
				intent.putExtra("id", list.get(position).getId());
				intent.putExtra("isGame", isGame);
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
		if (isGame) {
			ImageView rightImageView = (ImageView) findViewById(R.id.right_myBg);
			rightImageView.setBackgroundResource(R.drawable.game_bg2);
		} else {
			ImageView rightImageView = (ImageView) findViewById(R.id.right_myBg);
			rightImageView.setBackgroundResource(R.drawable.app_bg);
		}

		rightGridView = (GridView) findViewById(R.id.right_gridView);
		rightGridView.setAdapter(leftAdapter);
		rightGridView.setVerticalScrollBarEnabled(false);
		// rightGridView.setSelector(getResources().getDrawable(
		// R.drawable.game_frame));
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
					// rightAdapter.notifyDataSetChanged();
					pageNum++;
				}
				isGettingData = false;
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				downloadManager = DownloadService
						.getDownloadManager(getApplicationContext());
				for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
					DownloadInfo downloadInfo = downloadManager
							.getDownloadInfo(i);
					if (downloadInfo.getState() == State.SUCCESS) {
						boolean isInstalled = checkIsInstalled(downloadInfo.getPackageString());
						if (isInstalled) {
							getList = new ArrayList<App>();
							App app = new App();
							app.setId(downloadInfo.getApp_idString());
							app.setName(downloadInfo.getApp_nameString());
							app.setApp_package(downloadInfo.getPackageString());
							app.setApp_score_interface(downloadInfo.getApp_score_interfaceString());
							app.setIntro(downloadInfo.getApp_introString());
							getList.add(app);
						}
					}
				}
				return null;
			}
		}.execute("");
	}

	private boolean checkIsInstalled(String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(packageName,
					0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (currNum > 0 && currNum < 6) {
				return true;
			}
		}
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
