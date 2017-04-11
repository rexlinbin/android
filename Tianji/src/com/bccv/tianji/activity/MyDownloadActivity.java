package com.bccv.tianji.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bccv.tianji.R;
import com.bccv.tianji.adapter.MyDownloadAdapter;
import com.bccv.tianji.api.GameApi;
import com.bccv.tianji.api.TcpApi;
import com.bccv.tianji.model.Game;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.StringUtils;

public class MyDownloadActivity extends BaseActivity {
	private TextView switchleftTextView, switchrightTextView, ediTextView;
	private ListView downloadingListView, completeListView;
	private MyDownloadAdapter downloadingAdapter, completeAdapter;
	private List<Game> downloadingList, downloadingGetList, completeList, completeGetList;
	private enum DownloadType {
		Downloading,
		Complete
	}
	private DownloadType downloadType = DownloadType.Downloading;
	private boolean isEdit = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mydownload);
		setBack();
		setView();
		getDownloadingData();
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
	
	private void setView(){
		ediTextView = (TextView) findViewById(R.id.edit_textView);
		ediTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEdit = !isEdit;
				if (isEdit) {
					ediTextView.setText("删除");
				}else {
					ediTextView.setText("编辑");
				}
				if (downloadType == DownloadType.Downloading) {
					if (!isEdit) {
						List<Game> deleteGames = new ArrayList<Game>();
						for (int i = 0; i < downloadingList.size(); i++) {
							final Game game = downloadingList.get(i);
							if (game.isSelect()) {
								deleteGames.add(game);
								Timer timer = new Timer();
								TimerTask task = new TimerTask() {

									@Override
									public void run() {
										// TODO Auto-generated method stub
										TcpApi tcpApi = new TcpApi();
										if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
											tcpApi.deleteDownload(game.getTask_id(), GlobalParams.user.getUser_id(), game.getGame_id(), GlobalParams.auth_id);
										}

									}
								};

								if (GlobalParams.user != null
										&& !StringUtils.isEmpty(GlobalParams.auth_id)) {
									timer.schedule(task, i * 10);
								}
							}
						}
						
						for (int i = 0; i < deleteGames.size(); i++) {
							downloadingList.remove(deleteGames.get(i));
						}
					}
					downloadingAdapter.isEdit(isEdit);
					downloadingAdapter.notifyDataSetChanged();
				}else {
					if (!isEdit) {
						List<Game> deleteGames = new ArrayList<Game>();
						for (int i = 0; i < completeList.size(); i++) {
							final Game game = completeList.get(i);
							if (game.isSelect()) {
								deleteGames.add(game);
								Timer timer = new Timer();
								TimerTask task = new TimerTask() {
									@Override
									public void run() {
										// TODO Auto-generated method stub-
										TcpApi tcpApi = new TcpApi();
										if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
											tcpApi.deleteDownload(game.getTask_id(), GlobalParams.user.getUser_id(), game.getGame_id(), GlobalParams.auth_id);
										}
									}
								};

								if (GlobalParams.user != null
										&& !StringUtils.isEmpty(GlobalParams.auth_id)) {
									timer.schedule(task, i * 10);
								}
							}
						}
						
						for (int i = 0; i < deleteGames.size(); i++) {
							completeList.remove(deleteGames.get(i));
						}
					}
					completeAdapter.isEdit(isEdit);
					completeAdapter.notifyDataSetChanged();
				}
			}
		});
		
		switchleftTextView = (TextView) findViewById(R.id.complete_textView);
		switchrightTextView = (TextView) findViewById(R.id.downloading_textView);
		switchrightTextView.setSelected(true);
		
		switchleftTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (switchrightTextView.isSelected()) {
					switchleftTextView.setSelected(true);
					switchrightTextView.setSelected(false);
					downloadingListView.setVisibility(View.GONE);
					completeListView.setVisibility(View.VISIBLE);
					getCompleteData();
				}
			}
		});
		
		switchrightTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (switchleftTextView.isSelected()) {
					switchleftTextView.setSelected(false);
					switchrightTextView.setSelected(true);
					downloadingListView.setVisibility(View.VISIBLE);
					completeListView.setVisibility(View.GONE);
					getDownloadingData();
				}
			}
		});
		
		downloadingListView = (ListView) findViewById(R.id.downloading_listView);
		completeListView = (ListView) findViewById(R.id.complete_listView);
		downloadingList = new ArrayList<Game>();
		completeList = new ArrayList<Game>();
		downloadingAdapter = new MyDownloadAdapter(getApplicationContext(), downloadingList);
		completeAdapter = new MyDownloadAdapter(getApplicationContext(), completeList);
		downloadingListView.setDividerHeight(0);
		completeListView.setDividerHeight(0);
		downloadingListView.setSelector(new ColorDrawable(android.R.color.transparent));
		completeListView.setSelector(new ColorDrawable(android.R.color.transparent));
		downloadingListView.setAdapter(downloadingAdapter);
		completeListView.setAdapter(completeAdapter);
	}
	
	private void getDownloadingData(){
		Callback callback = new Callback() {
			
			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (downloadingGetList != null) {
					downloadingList.clear();
					downloadingList.addAll(downloadingGetList);
					downloadingAdapter.notifyDataSetChanged();
					Timer timer = new Timer();
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
					
					timer.schedule(task, 1000, 10000);
				}
			}
		};
		
		new DataAsyncTask(callback, false) {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				GlobalParams.tcpClientHelper.setHandler(handler);
//				TcpApi tcpApi = new TcpApi();
//				tcpApi.login("2");
				GameApi gameApi = new GameApi();
				downloadingGetList = gameApi.getMyDownloadList(GlobalParams.user.getUser_id(), "1", "100000", "0");
				return null;
			}
		}.execute("");
	}
	
	private void getCompleteData(){
		Callback callback = new Callback() {
			
			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (completeGetList != null) {
					completeList.clear();
					completeList.addAll(completeGetList);
					completeAdapter.notifyDataSetChanged();
				}
			}
		};
		
		new DataAsyncTask(callback, false) {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				GameApi gameApi = new GameApi();
				completeGetList = gameApi.getMyDownloadList(GlobalParams.user.getUser_id(), "1", "100000", "1");
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
							List<Game> deleteGames = new ArrayList<Game>();
							for (int i = 0; i < downloadingList.size(); i++) {
								Game game = downloadingList.get(i);
								boolean isComplete = true;
								for (int j = 0; j < jsonArray.size(); j++) {
									JSONObject jsonObject2 = jsonArray.getJSONObject(j);
									if (game.getGame_id().equals(jsonObject2.getString("game_id"))) {
										game.setStatus(jsonObject2.getString("download_status"));
										game.setDownload_size(jsonObject2.getFloatValue("download_size"));
										isComplete = false;
									}
								}
								if (isComplete) {
									deleteGames.add(game);
								}
							}
							
							for (int t = 0; t < deleteGames.size(); t++) {
								downloadingList.remove(deleteGames.get(t));
							}
							
							downloadingAdapter.notifyDataSetChanged();
						}else if (flag.equals("login")){
							GlobalParams.auth_id = jsonObject.getJSONObject("info").getString("auth_id");
						}else if (flag.equals("task")) {
							Toast.makeText(getApplicationContext(), jsonObject.getString("message"), 1).show();
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
