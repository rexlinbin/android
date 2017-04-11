package com.bccv.boxcomic.fragment;

import java.util.List;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.SearchListActivity;
import com.bccv.boxcomic.api.SearchApi;
import com.bccv.boxcomic.modal.Search;
import com.bccv.boxcomic.tool.AppManager;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.view.KeywordsFlow;

@SuppressLint("NewApi")
public class SearchFragment extends FragmentActivity implements
		OnClickListener, OnTouchListener, OnGestureListener {
	private List<Search> list;
	public String[] keywords;
	private KeywordsFlow keywordsFlow;
	private Button btnIn, btnOut;
	private static final int FLING_MIN_DISTANCE = 100;// 移动最小距离
	private static final int FLING_MIN_VELOCITY = 80;// 移动最大速度
	private GestureDetector gDetector;
	private EditText edit;
	private ImageView toSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_search);
		AppManager.getAppManager().addActivity(this);
		init();
		keywordsFlow = (KeywordsFlow) findViewById(R.id.keywordsflow);
		keywordsFlow.setDuration(800l);
		keywordsFlow.setOnItemClickListener(this);
		keywordsFlow.setOnTouchListener(this);
		keywordsFlow.setFocusable(true);
		keywordsFlow.setClickable(true);

		// 构建手势探测器
		gDetector = new GestureDetector(this);
		new SearchTask().execute();
	}

	private void init() {
		// TODO Auto-generated method stub
		toSearch = (ImageView) findViewById(R.id.fragment_search_image);
		edit = (EditText) findViewById(R.id.fragment_search_edit);
		edit.setOnKeyListener(onKey);
		toSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String key = edit.getText().toString();
				Intent aIntent = new Intent(SearchFragment.this,
						SearchListActivity.class);
				aIntent.putExtra("type", "Search");
				aIntent.putExtra("key", key);
				aIntent.putExtra("textTitle", key);
				startActivity(aIntent);
			}
		});

	
		
		
	}
	OnKeyListener onKey=new OnKeyListener() {
		
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
		
			//修改回车键功能
			if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()== KeyEvent.ACTION_DOWN){
			// 先隐藏键盘
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(
						SearchFragment.this
				.getCurrentFocus()
				.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
			
			String key = edit.getText().toString();
			Intent aIntent = new Intent(SearchFragment.this,
					SearchListActivity.class);
			aIntent.putExtra("type", "Search");
			aIntent.putExtra("key", key);
			aIntent.putExtra("textTitle", key);
			startActivity(aIntent);
			return true;
			}
			
			
			return false;
		}
	};
	private void feedKeywordsFlow(KeywordsFlow keywordsFlow2, String[] keywords2) {
		// TODO Auto-generated method stub
		Random random = new Random();
		for (int i = 0; i < keywords2.length; i++) {
//			int ran = random.nextInt(keywords2.length);
			String tmp = keywords2[i];
			keywordsFlow.feedKeyword(tmp);
		}
	}

	// 往左滑动
	private void showLeftIn() {
		keywordsFlow.rubKeywords();
		// keywordsFlow.rubAllViews();
		feedKeywordsFlow(keywordsFlow, keywords);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
	}

	// 往右滑动
	private void showRightOut() {
		keywordsFlow.rubKeywords();
		// keywordsFlow.rubAllViews();
		feedKeywordsFlow(keywordsFlow, keywords);
		keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	class SearchTask extends AsyncTask<String, Void, List<Search>> {
		private int type;

		@Override
		protected List<Search> doInBackground(String... params) {

			SearchApi searchapi = new SearchApi();

			list = searchapi.getSearch("", "");

			return list;

		}

		@Override
		protected void onPostExecute(List<Search> list) {
			super.onPostExecute(list);

			try {

				keywords = new String[list.size()];

				for (int i = 0; i < list.size(); i++) {

					keywords[i] = list.get(i).getName();

				}

				feedKeywordsFlow(keywordsFlow, keywords);
				keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);

			} catch (Exception e) {
				// TODO: handle exception
				Logger.e("error", e.toString());
			}

		}
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float arg3) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			showRightOut();
		}
		// 向左翻图片
		if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			showLeftIn();
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		// TODO Auto-generated method stub
		return gDetector.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v instanceof TextView) {
			String keyword = ((TextView) v).getText().toString();

			Intent aIntent = new Intent(SearchFragment.this,
					SearchListActivity.class);
			aIntent.putExtra("type", "Search");
			aIntent.putExtra("key", keyword);
			aIntent.putExtra("textTitle", keyword);
			startActivity(aIntent);

		}
	}

}
