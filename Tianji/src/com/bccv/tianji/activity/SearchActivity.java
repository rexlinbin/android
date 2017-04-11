package com.bccv.tianji.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bccv.tianji.R;
import com.bccv.tianji.adapter.ClassifyListAdapter;
import com.bccv.tianji.adapter.HotSearchAdapter;
import com.bccv.tianji.adapter.MyDownloadAdapter;
import com.bccv.tianji.api.SearchApi;
import com.bccv.tianji.api.TcpApi;
import com.bccv.tianji.model.Game;
import com.bccv.tianji.model.HotSearch;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;
import com.utils.views.WordWrapView;

public class SearchActivity extends BaseActivity {
	private ListView listView;
	private List<String> historyList;
	private List<HotSearch> hotSearchList, getHotSearchList;
	private HotSearchAdapter adapter;
	private TextView clearTextView, searchTextView;
	private WordWrapView wordWrapView;
	private EditText searchEditText;
	private LinearLayout searchInfoLayout;
	private ListView searchListView;
	private List<Game> searchList, getSearchList;
	private ClassifyListAdapter searchAdapter;
	private ImageView imageView;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setBack();
		setView();
		getData();
	}

	private ImageView backTextView;

	private void setBack() {
		backTextView = (ImageView) findViewById(R.id.back_textView);
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setView() {
		searchInfoLayout = (LinearLayout) findViewById(R.id.searchInfo_layout);
		listView = (ListView) findViewById(R.id.listView);
		hotSearchList = new ArrayList<HotSearch>();
		adapter = new HotSearchAdapter(getApplicationContext(), hotSearchList);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(android.R.color.transparent));
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String searchString = hotSearchList.get(position).getKeyword();
				searchInfoLayout.setVisibility(View.GONE);
				searchListView.setVisibility(View.VISIBLE);
				getSearchData(searchString);
			}
		});

		historyList = SerializationUtil
				.readSelectHistoryCache(getApplicationContext());
		if (historyList == null) {
			historyList = new ArrayList<String>();
		}

		searchEditText = (EditText) findViewById(R.id.search_editText);
		searchTextView = (TextView) findViewById(R.id.search_textView);
		clearTextView = (TextView) findViewById(R.id.clear_textView);
		wordWrapView = (WordWrapView) findViewById(R.id.wordWrapView);
		for (final String string : historyList) {
			TextView textView = new TextView(getApplicationContext());
			textView.setBackgroundResource(R.drawable.selector_searchhistory);
			textView.setGravity(Gravity.CENTER);
			textView.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			textView.setText(string);
			textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String searchString = string;
					searchInfoLayout.setVisibility(View.GONE);
					searchListView.setVisibility(View.VISIBLE);
					getSearchData(searchString);
				}
			});
			wordWrapView.addView(textView);
		}
		searchTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchString = searchEditText.getText().toString();
				if (!StringUtils.isEmpty(searchString)) {
					historyList.add(searchString);
					TextView textView = new TextView(getApplicationContext());
					textView.setText(searchString);
					wordWrapView.addView(textView);
					SerializationUtil
							.wirteSelectHistorySerialization(
									getApplicationContext(),
									(Serializable) historyList);
				}
				searchInfoLayout.setVisibility(View.GONE);
				searchListView.setVisibility(View.VISIBLE);
				getSearchData(searchEditText.getText().toString());
			}
		});

		clearTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				historyList.clear();
				wordWrapView.removeAllViews();
				SerializationUtil.wirteSelectHistorySerialization(
						getApplicationContext(), (Serializable) historyList);
			}
		});

		searchEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int actionId,
					KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String searchString = searchEditText.getText().toString();
					if (!StringUtils.isEmpty(searchString)) {
						historyList.add(searchString);
						TextView textView = new TextView(getApplicationContext());
						textView.setText(searchString);
						wordWrapView.addView(textView);
						SerializationUtil
								.wirteSelectHistorySerialization(
										getApplicationContext(),
										(Serializable) historyList);
					}
					searchInfoLayout.setVisibility(View.GONE);
					searchListView.setVisibility(View.VISIBLE);
					getSearchData(searchEditText.getText().toString());
				}
				return false;
			}
		});

		searchListView = (ListView) findViewById(R.id.search_listView);
		searchList = new ArrayList<Game>();
		searchAdapter = new ClassifyListAdapter(getApplicationContext(),
				searchList);
		searchListView.setAdapter(searchAdapter);
		searchListView.setVisibility(View.GONE);
		searchListView.setSelector(new ColorDrawable(android.R.color.transparent));
		searchListView.setDividerHeight(0);
		searchListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent aItent = new Intent(SearchActivity.this,
						DetailsActivity.class);

				aItent.putExtra("gameID", searchList.get(position).getGame_id());

				startActivity(aItent);
				
			}
		});

		imageView = (ImageView) findViewById(R.id.none_imageView);
		imageView.setVisibility(View.GONE);
	}

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getHotSearchList != null) {
					hotSearchList.addAll(getHotSearchList);
					adapter.notifyDataSetChanged();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				SearchApi searchApi = new SearchApi();
				getHotSearchList = searchApi.getHotSearchList("10");
				return null;
			}
		}.execute("");
	}

	private void getSearchData(final String string) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getSearchList != null) {
					searchList.addAll(getSearchList);
					searchAdapter.notifyDataSetChanged();
				}

				if (searchList.size() <= 0) {
					imageView.setVisibility(View.VISIBLE);
				} else {
					imageView.setVisibility(View.GONE);
					if (timer != null) {
						timer.cancel();
						timer = new Timer();
					}else{
						timer = new Timer();
					}
					TimerTask task = new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							TcpApi tcpApi = new TcpApi();
							if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
								tcpApi.getDownloadGameListState(GlobalParams.user.getUser_id(), GlobalParams.auth_id);
							}
							
						}
					};
					
					if (GlobalParams.user != null && !StringUtils.isEmpty(GlobalParams.auth_id)) {
						timer.schedule(task, 1000, 10000);
					}
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				GlobalParams.tcpClientHelper.setHandler(handler);
				SearchApi searchApi = new SearchApi();
				String useridString = "";
				if (GlobalParams.user != null) {
					useridString = GlobalParams.user.getUser_id();
				}
				getSearchList = searchApi.getSearchList(string, "1", "100000",
						useridString);
				return null;
			}
		}.execute("");
	}
	
	private Handler handler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				String data = (String) msg.obj;
				Logger.e("data111", data);
				if (!StringUtils.isEmpty(data)) {
					try {
						JSONObject jsonObject = JSON.parseObject(data);
						String flag = jsonObject.getString("flag");
						if (flag.equals("task_list")) {
							JSONArray jsonArray = jsonObject.getJSONArray("info");
							for (int i = 0; i < searchList.size(); i++) {
								Game game = searchList.get(i);
								for (int j = 0; j < jsonArray.size(); j++) {
									JSONObject jsonObject2 = jsonArray.getJSONObject(j);
									if (game.getGame_id().equals(jsonObject2.getString("game_id"))) {
										game.setStatus(jsonObject2.getString("download_status"));
										game.setDownload_size(jsonObject2.getFloatValue("download_size"));
									}
								}
							}
							
							
							searchAdapter.notifyDataSetChanged();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}
	};
}
