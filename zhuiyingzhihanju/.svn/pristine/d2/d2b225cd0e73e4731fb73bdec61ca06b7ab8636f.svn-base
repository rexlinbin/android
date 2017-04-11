package com.bccv.zhuiyingzhihanju.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.DownloadAdapter;
import com.bccv.zhuiyingzhihanju.api.MovieUrlApi;
import com.bccv.zhuiyingzhihanju.model.Collect;
import com.bccv.zhuiyingzhihanju.model.RealUrl;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler.State;
import com.tendcloud.tenddata.TCAgent;
import com.utils.download.DownloadInfo;
import com.utils.download.DownloadManager;
import com.utils.download.DownloadUtil;
import com.utils.model.DownloadMovie;
import com.utils.swipemenulistview.SwipeMenuListView;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.DimensionPixelUtil;
import com.utils.tools.FileUtils;
import com.utils.tools.M3U8Utils;
import com.utils.tools.PromptManager;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DownloadMovieActivity extends BaseActivity {
	private LinearLayout editLayout;
	private ImageView noneImageView;
	private SwipeMenuListView listView;
	private TextView editTextView, selectTextView, deleteTextView;
	private DownloadManager downloadManager;
	private List<DownloadMovie> list, getList, selectList;
	private DownloadAdapter adapter;
	private boolean isEditShow = false, isSelectAll = false;
	private float editHeight = 0;
	private boolean isEpisode;
	private String movie_id, type_id, title;
	private Timer timer;
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "DownloadMovieActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "DownloadMovieActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.activity_download);
		downloadManager = DownloadUtil.getDownloadManager();
		editHeight = DimensionPixelUtil.dip2px(getApplicationContext(), 60);
		selectList = new ArrayList<DownloadMovie>();
		isEpisode = getIntent().getBooleanExtra("isEpisode", false);
		if (isEpisode) {
			movie_id = getIntent().getStringExtra("movie_id");
			type_id = getIntent().getStringExtra("type_id");
			title = getIntent().getStringExtra("title");
		}
		setTitle();
		initView();
		// getData();
	}

	private void setTitle() {
		if (isEpisode) {
			TextView textView = (TextView) findViewById(R.id.titleName_textView);
			textView.setText(title);
		}

		ImageButton imageButton = (ImageButton) findViewById(R.id.titel_back);
		imageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		editTextView = (TextView) findViewById(R.id.titleEdit_textView);
		editTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isEditShow) {
					editTextView.setText("编辑");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(false);
					}
					adapter.notifyDataSetChanged();
					hideEdit();
				} else {
					editTextView.setText("完成");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(true);
						list.get(i).setSelect(false);
					}
					adapter.notifyDataSetChanged();
					showEdit();
				}
			}
		});
	}

	private void initView() {
		noneImageView = (ImageView) findViewById(R.id.none_imageView);
		listView = (SwipeMenuListView) findViewById(R.id.pList);
		editLayout = (LinearLayout) findViewById(R.id.edit_layout);
		selectTextView = (TextView) findViewById(R.id.select_textView);
		selectTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSelectAll) {
					isSelectAll = false;
					for (int i = 0; i < list.size(); i++) {
						DownloadMovie downloadMovie = list.get(i);
						downloadMovie.setSelect(false);
					}
					selectList.clear();
					selectTextView.setText("全选");
				}else{
					isSelectAll = true;
					for (int i = 0; i < list.size(); i++) {
						DownloadMovie downloadMovie = list.get(i);
						downloadMovie.setSelect(true);
						if (selectList.size() == 0) {
							selectList.add(downloadMovie);
							continue;
						}
						for (int j = 0; j < selectList.size(); j++) {
							if (selectList.get(j).getModel_id().equals(downloadMovie.getModel_id()) && selectList.get(j).getModelType_id().equals(downloadMovie.getModelType_id()) && selectList.get(j).getEpisode_id() == downloadMovie.getEpisode_id()) {
								continue;
							}else{
								selectList.add(downloadMovie);
								break;
							}
						}
						
					}
					selectTextView.setText("取消全选");
				}
				
				adapter.notifyDataSetChanged();
			}
		});
		deleteTextView = (TextView) findViewById(R.id.delete_textView);
		deleteTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteData();
			}
		});
		list = new ArrayList<DownloadMovie>();
		adapter = new DownloadAdapter(getApplicationContext(), list);
		listView.setAdapter(adapter);
		
//SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
//			
//			@Override
//			public void create(SwipeMenu menu) {
//				// TODO Auto-generated method stub
//				SwipeMenuItem item = new SwipeMenuItem(
//                        getApplicationContext());
//                item.setBackground(new ColorDrawable(Color.parseColor("#ff3b30")));
//                item.setWidth((int) DimensionPixelUtil.dip2px(getApplicationContext(), 90));
//                item.setTitle("删除");
//                item.setTitleColor(Color.WHITE);
//                item.setTitleSize(15);
//                menu.addMenuItem(item);
//			}
//		};
//		listView.setMenuCreator(swipeMenuCreator);
//		
//		listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
//			
//			@Override
//			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//				// TODO Auto-generated method stub
//				deleteMovieData(list.get(position));
//				return false;
//			}
//		});
		
		listView.setSelector(new ColorDrawable(android.R.color.transparent));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				DownloadMovie downloadMovie = list.get(position);
				if (isEditShow) {

					if (downloadMovie.isSelect()) {
						selectList.remove(downloadMovie);
					} else {
						selectList.add(downloadMovie);
					}
					downloadMovie.setSelect(!downloadMovie.isSelect());
					adapter.notifyDataSetChanged();
				} else {
					if (isEpisode) {
						Intent intent = new Intent(getApplicationContext(), VideoMagnetPlayerActivity.class);
						intent.putExtra("url", downloadMovie.getLocalUrl());
						intent.putExtra("title", downloadMovie.getTitle());
						intent.putExtra("jishu", downloadMovie.getEpisode_id());
						startActivity(intent);
					} else {
						if (downloadMovie.getSpeed().equals("暂停") || downloadMovie.getSpeed().equals("失败")) {
							deleteMovie(downloadMovie);
						} else {
							pauseMovie(downloadMovie);
						}
					}
				}
				
				
				Log.e("数据 ", downloadMovie.getEpisode_id()+"");
			}
		});
	}

	Map<String, List<DownloadInfo>> map = new HashMap<String, List<DownloadInfo>>();
	List<String> keyList = new ArrayList<String>();

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null && getList.size() > 0) {
					noneImageView.setVisibility(View.GONE);
					list.clear();
					list.addAll(getList);
					if (isEditShow) {
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setEdit(true);
							DownloadMovie downloadMovie = list.get(i);
							list.get(i).setSelect(false);
							for (int j = 0; j < selectList.size(); j++) {
								if (selectList.get(j).getModel_id().equals(downloadMovie.getModel_id()) && selectList.get(j).getModelType_id().equals(downloadMovie.getModelType_id()) && selectList.get(j).getEpisode_id() == downloadMovie.getEpisode_id()) {
									list.get(i).setSelect(true);
								}
							}
						}
					} else {
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setEdit(false);
						}
					}
					adapter.notifyDataSetChanged();
				} else {
					list.clear();
					adapter.notifyDataSetChanged();
					noneImageView.setVisibility(View.VISIBLE);
					finish();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				if (getList == null) {
					getList = new ArrayList<DownloadMovie>();
				} else {
					getList.clear();
				}
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				if (isEpisode) {
					for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
						DownloadMovie downloadMovie = entry.getValue();
						if (downloadMovie.getModel_id().equals(movie_id)
								&& downloadMovie.getModelType_id().equals(type_id)) {
							if (downloadMovie.isSuccess()) {
								getList.add(downloadMovie);
							}
						}
					}

				} else {
					map.clear();
					keyList.clear();

					for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
						DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
						String key = downloadInfo.getModel_id() + "," + downloadInfo.getModelType_id() + ","
								+ downloadInfo.getEpisode_id();
						if (map.containsKey(key)) {
							map.get(key).add(downloadInfo);
						} else {
							List<DownloadInfo> list = new ArrayList<DownloadInfo>();
							list.add(downloadInfo);
							map.put(key, list);
							keyList.add(key);
						}

					}

					for (int i = 0; i < keyList.size(); i++) {
						List<DownloadInfo> list = map.get(keyList.get(i));
						boolean isSuccess = true;
						long lastSize = 0;
						long totalSize = 0;
						long downloadTotalSize = 0;
						long downloadSize = 0;
						int fileTotalSize = 0;
						int fileSize = 0;
						boolean isWaiting = true;
						boolean isFailure = true;
						boolean isPause = true;

						for (int j = 0; j < list.size(); j++) {

							DownloadInfo downloadInfo = list.get(j);
							if (downloadInfo.getState() == State.SUCCESS) {
								
								long length = FileUtils.getFileSize(downloadInfo.getFileSavePath());
								totalSize += length;
								downloadTotalSize += length;
								downloadSize += length;
								lastSize += downloadInfo.getLastProgress();
								downloadInfo.setLastProgress(length);
								fileTotalSize += 1;
								fileSize += 1;
							} else {
								if (downloadInfo.getState() != State.CANCELLED) {
									isPause = false;
								}
								isSuccess = false;
								
								long length = downloadInfo.getProgress();
								downloadTotalSize += downloadInfo.getFileLength();
								if (downloadInfo.getFileLength() == 0) {
									downloadTotalSize += length;
								}
								downloadSize += length;
								lastSize += downloadInfo.getLastProgress();
								downloadInfo.setLastProgress(length);
								fileTotalSize += 1;
							}
							if (downloadInfo.getState() != State.WAITING) {
								isWaiting = false;
							}
							if (downloadInfo.getState() != State.FAILURE && downloadInfo.getState() != State.SUCCESS) {
								isFailure = false;
							}
						}
						DownloadInfo downloadInfo = list.get(0);
						if (isSuccess) {
							DownloadMovie downloadMovie = new DownloadMovie();
							downloadMovie.setDownloading(false);
							downloadMovie.setEpisode(downloadInfo.isEpisode());
							downloadMovie.setEpisode_id(downloadInfo.getEpisode_id());
							downloadMovie.setImageUrl(downloadInfo.getModelImage_url());
							downloadMovie.setModel_id(downloadInfo.getModel_id());
							downloadMovie.setModelType_id(downloadInfo.getModelType_id());
							downloadMovie.setTitle(downloadInfo.getModel_title());
							downloadMovie.setSpeed("完成");
							downloadMovie.setSize(FileUtils.formatFileSize(totalSize));
							downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());
							downloadMovie.setSuccess(true);
							movieMap.put(keyList.get(i), downloadMovie);
						} else {
							DownloadMovie downloadMovie = new DownloadMovie();
							downloadMovie.setEpisode(downloadInfo.isEpisode());
							downloadMovie.setEpisode_id(downloadInfo.getEpisode_id());
							downloadMovie.setImageUrl(downloadInfo.getModelImage_url());
							downloadMovie.setModel_id(downloadInfo.getModel_id());
							downloadMovie.setModelType_id(downloadInfo.getModelType_id());
							downloadMovie.setTitle(downloadInfo.getModel_title());
							downloadMovie.setHd(downloadInfo.getHd());
							downloadMovie.setSource_id(downloadInfo.getSource_id());
							downloadMovie.setSource_name(downloadInfo.getSource_name());
							downloadMovie.setSuccess(false);
							downloadMovie.setM3U8(downloadInfo.isM3U8());
							downloadMovie.setSpeedNum(0);
							if (isFailure) {
								downloadMovie.setDownloading(false);
								downloadMovie.setSpeed("失败");
							} else if (isWaiting) {
								downloadMovie.setDownloading(false);
								downloadMovie.setSpeed("等待");
							} else if (isPause) {
								downloadMovie.setDownloading(false);
								downloadMovie.setSpeed("暂停");
							} else {
								downloadMovie.setDownloading(true);
								if (downloadTotalSize != 0) {
									long speed = downloadSize - lastSize;
									downloadMovie.setSpeed(FileUtils.formatFileSize(speed) + "/S");
									downloadMovie.setSpeedNum(speed);
									downloadMovie.setProgress(fileSize * 100 / fileTotalSize);
									downloadMovie.setLastSize(downloadSize * 100 / downloadTotalSize);
								} else {
									downloadMovie.setSpeed("0KB/S");
									downloadMovie.setProgress(0);
									downloadMovie.setLastSize(0);
									downloadMovie.setSpeedNum(0);
								}
							}

							downloadMovie.setSize(FileUtils.formatFileSize(downloadTotalSize));
							if (downloadInfo.isM3U8()) {
								downloadMovie.setFileNum(fileTotalSize);
							} else {
								downloadMovie.setFileNum(1);
							}
							downloadMovie.setFileSize(downloadTotalSize);
							downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());

							movieMap.put(keyList.get(i), downloadMovie);
						}
					}
					for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
						DownloadMovie downloadMovie = entry.getValue();
						if (!downloadMovie.isSuccess()) {
							getList.add(downloadMovie);
						}
					}
				}
				SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				return null;
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}

	private void getDataWithThread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (getList == null) {
					getList = new ArrayList<DownloadMovie>();
				} else {
					getList.clear();
				}
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				if (isEpisode) {
					for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
						DownloadMovie downloadMovie = entry.getValue();
						if (downloadMovie.getModel_id().equals(movie_id)
								&& downloadMovie.getModelType_id().equals(type_id)) {
							if (downloadMovie.isSuccess()) {
								getList.add(downloadMovie);
							}
						}
					}

				} else {
					map.clear();
					keyList.clear();

					for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
						DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
						String key = downloadInfo.getModel_id() + "," + downloadInfo.getModelType_id() + ","
								+ downloadInfo.getEpisode_id();
						if (map.containsKey(key)) {
							map.get(key).add(downloadInfo);
						} else {
							List<DownloadInfo> list = new ArrayList<DownloadInfo>();
							list.add(downloadInfo);
							map.put(key, list);
							keyList.add(key);
						}

					}

					for (int i = 0; i < keyList.size(); i++) {
						List<DownloadInfo> list = map.get(keyList.get(i));
						boolean isSuccess = true;
						long lastSize = 0;
						long totalSize = 0;
						long downloadTotalSize = 0;
						long downloadSize = 0;
						int fileTotalSize = 0;
						int fileSize = 0;
						boolean isWaiting = true;
						boolean isFailure = true;
						boolean isPause = true;

						for (int j = 0; j < list.size(); j++) {

							DownloadInfo downloadInfo = list.get(j);
							if (downloadInfo.getState() == State.SUCCESS) {
								
								long length = FileUtils.getFileSize(downloadInfo.getFileSavePath());
								totalSize += length;
								downloadTotalSize += length;
								downloadSize += length;
								lastSize += downloadInfo.getLastProgress();
								downloadInfo.setLastProgress(length);
								fileTotalSize += 1;
								fileSize += 1;
							} else {
								if (downloadInfo.getState() != State.CANCELLED) {
									isPause = false;
								}
								isSuccess = false;
								
								long length = downloadInfo.getProgress();
								downloadTotalSize += downloadInfo.getFileLength();
								if (downloadInfo.getFileLength() == 0) {
									downloadTotalSize += length;
								}
								downloadSize += length;
								lastSize += downloadInfo.getLastProgress();
								downloadInfo.setLastProgress(length);
								fileTotalSize += 1;
							}
							if (downloadInfo.getState() != State.WAITING) {
								isWaiting = false;
							}
							if (downloadInfo.getState() != State.FAILURE && downloadInfo.getState() != State.SUCCESS) {
								isFailure = false;
							}
						}
						DownloadInfo downloadInfo = list.get(0);
						if (isSuccess) {
							DownloadMovie downloadMovie = new DownloadMovie();
							downloadMovie.setDownloading(false);
							downloadMovie.setEpisode(downloadInfo.isEpisode());
							downloadMovie.setEpisode_id(downloadInfo.getEpisode_id());
							downloadMovie.setImageUrl(downloadInfo.getModelImage_url());
							downloadMovie.setModel_id(downloadInfo.getModel_id());
							downloadMovie.setModelType_id(downloadInfo.getModelType_id());
							downloadMovie.setTitle(downloadInfo.getModel_title());
							downloadMovie.setSpeed("完成");
							downloadMovie.setSize(FileUtils.formatFileSize(totalSize));
							downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());
							downloadMovie.setSuccess(true);
							movieMap.put(keyList.get(i), downloadMovie);
						} else {
							DownloadMovie downloadMovie = new DownloadMovie();
							downloadMovie.setEpisode(downloadInfo.isEpisode());
							downloadMovie.setEpisode_id(downloadInfo.getEpisode_id());
							downloadMovie.setImageUrl(downloadInfo.getModelImage_url());
							downloadMovie.setModel_id(downloadInfo.getModel_id());
							downloadMovie.setModelType_id(downloadInfo.getModelType_id());
							downloadMovie.setTitle(downloadInfo.getModel_title());
							downloadMovie.setHd(downloadInfo.getHd());
							downloadMovie.setSource_id(downloadInfo.getSource_id());
							downloadMovie.setSource_name(downloadInfo.getSource_name());
							downloadMovie.setSuccess(false);
							downloadMovie.setM3U8(downloadInfo.isM3U8());
							downloadMovie.setSpeedNum(0);
							if (isFailure) {
								downloadMovie.setDownloading(false);
								downloadMovie.setSpeed("失败");
							} else if (isWaiting) {
								downloadMovie.setDownloading(false);
								downloadMovie.setSpeed("等待");
							} else if (isPause) {
								downloadMovie.setDownloading(false);
								downloadMovie.setSpeed("暂停");
							} else {
								downloadMovie.setDownloading(true);
								if (downloadTotalSize != 0) {
									long speed = downloadSize - lastSize;
									downloadMovie.setSpeed(FileUtils.formatFileSize(speed) + "/S");
									downloadMovie.setSpeedNum(speed);
									downloadMovie.setProgress(fileSize * 100 / fileTotalSize);
									downloadMovie.setLastSize(downloadSize * 100 / downloadTotalSize);
								} else {
									downloadMovie.setSpeed("0KB/S");
									downloadMovie.setProgress(0);
									downloadMovie.setLastSize(0);
									downloadMovie.setSpeedNum(0);
								}
							}

							downloadMovie.setSize(FileUtils.formatFileSize(downloadTotalSize));
							if (downloadInfo.isM3U8()) {
								downloadMovie.setFileNum(fileTotalSize);
							} else {
								downloadMovie.setFileNum(1);
							}
							downloadMovie.setFileSize(downloadTotalSize);
							downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());

							movieMap.put(keyList.get(i), downloadMovie);
						}
					}
					for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
						DownloadMovie downloadMovie = entry.getValue();
						if (!downloadMovie.isSuccess()) {
							getList.add(downloadMovie);
						}
					}
				}
				SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				getDataHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	Handler getDataHandler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			if (getList != null && getList.size() > 0) {
				noneImageView.setVisibility(View.GONE);
				list.clear();
				list.addAll(getList);
				if (isEditShow) {
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(true);
						DownloadMovie downloadMovie = list.get(i);
						list.get(i).setSelect(false);
						for (int j = 0; j < selectList.size(); j++) {
							if (selectList.get(j).getModel_id().equals(downloadMovie.getModel_id()) && selectList.get(j).getModelType_id().equals(downloadMovie.getModelType_id()) && selectList.get(j).getEpisode_id() == downloadMovie.getEpisode_id()) {
								list.get(i).setSelect(true);
							}
						}
					}
				} else {
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(false);
					}
				}
				adapter.notifyDataSetChanged();
			} else {
				list.clear();
				adapter.notifyDataSetChanged();
				noneImageView.setVisibility(View.VISIBLE);
				finish();
			}
		};
	};
	
	private void refreshData() {
//		getDataWithThread();
		getData();
	}

	private void deleteData() {
		if (timer != null) {
			timer.cancel();
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (isEditShow) {
					editTextView.setText("编辑");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(false);
					}
					adapter.notifyDataSetChanged();
					hideEdit();
				}
				startTimer();
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				for (int j = 0; j < selectList.size(); j++) {
					DownloadMovie downloadMovie = selectList.get(j);
					String key = downloadMovie.getModel_id() + "," + downloadMovie.getModelType_id() + ","
							+ downloadMovie.getEpisode_id();
					
					List<DownloadInfo> list = map.get(key);
					if (list != null) {
						DownloadInfo downloadInfo = list.get(0);
						if (downloadInfo.getModel_id().equals(downloadMovie.getModel_id())
								&& downloadInfo.getModelType_id().equals(downloadMovie.getModelType_id())
								&& downloadInfo.getEpisode_id() == downloadMovie.getEpisode_id()) {

							try {
								downloadManager.removeDownloadAll(list);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
					movieMap.remove(key);
					SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				}
				return null;
			}
		}.execute("");

	}

	private void deleteMovieData(final DownloadMovie delete) {
		if (timer != null) {
			timer.cancel();
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (isEditShow) {
					editTextView.setText("编辑");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(false);
					}
					adapter.notifyDataSetChanged();
					hideEdit();
				}
				startTimer();
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				
					DownloadMovie downloadMovie = delete;
					String key = downloadMovie.getModel_id() + "," + downloadMovie.getModelType_id() + ","
							+ downloadMovie.getEpisode_id();
					List<DownloadInfo> list = map.get(key);
					if (list != null) {
						DownloadInfo downloadInfo = list.get(0);
						if (downloadInfo.getModel_id().equals(downloadMovie.getModel_id())
								&& downloadInfo.getModelType_id().equals(downloadMovie.getModelType_id())
								&& downloadInfo.getEpisode_id() == downloadMovie.getEpisode_id()) {

							try {
								downloadManager.removeDownloadAll(list);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
					movieMap.remove(key);
					SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				
				return null;
			}
		}.execute(""); 

	}
	
	private void deleteMovie(final DownloadMovie downloadMovie) {
		if (timer != null) {
			timer.cancel();
		}
		PromptManager.showCancelProgressDialog(DownloadMovieActivity.this);
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				startDownload(downloadMovie);
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				String key = downloadMovie.getModel_id() + "," + downloadMovie.getModelType_id() + ","
						+ downloadMovie.getEpisode_id();
				List<DownloadInfo> list = map.get(key);
				if (list != null) {
					DownloadInfo downloadInfo = list.get(0);
					if (downloadInfo.getModel_id().equals(downloadMovie.getModel_id())
							&& downloadInfo.getModelType_id().equals(downloadMovie.getModelType_id())
							&& downloadInfo.getEpisode_id() == downloadMovie.getEpisode_id()) {

						try {
							downloadManager.removeDownloadAll(list);
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}else{
					List<DownloadInfo> deletelist = new ArrayList<>();
					for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
						DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
						if (downloadInfo.getModel_id().equals(downloadMovie.getModel_id())
								&& downloadInfo.getModelType_id().equals(downloadMovie.getModelType_id())
								&& downloadInfo.getEpisode_id() == downloadMovie.getEpisode_id()) {

							try {
								deletelist.add(downloadInfo);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
					if (deletelist.size() > 0) {
						try {
							downloadManager.removeDownloadAll(deletelist);
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
				}
				movieMap.remove(key);
				SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				return null;
			}
		}.execute("");
	}

	private void pauseMovie(final DownloadMovie downloadMovie) {
		if (timer != null) {
			timer.cancel();
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				startTimer();
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				String key = downloadMovie.getModel_id() + "," + downloadMovie.getModelType_id() + ","
						+ downloadMovie.getEpisode_id();
				List<DownloadInfo> list = map.get(key);
				if (list != null) {
					try {
						downloadManager.removeDownloadAll(list);
					} catch (DbException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				DownloadMovie newDownloadMovie = movieMap.get(key);
				if (newDownloadMovie != null) {
					newDownloadMovie.setDownloading(false);
					newDownloadMovie.setSpeed("暂停");
				}

				SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				return null;
			}
		}.execute("");
	}

	private M3U8Utils m3u8Utils;

	private void startDownload(DownloadMovie downloadMovie) {
		m3u8Utils = new M3U8Utils(getApplicationContext(), downloadMovie.getModel_id(), downloadMovie.getModelType_id(),
				downloadMovie.getImageUrl(), downloadMovie.getTitle(), downloadMovie.isEpisode(),
				downloadMovie.getSource_name(), downloadMovie.getSource_id(), downloadMovie.getHd());
		getUrl(downloadMovie.getSource_id(), downloadMovie.getEpisode_id(), downloadMovie.getHd());
	}

	private void getUrl(final String url, final int episode_id, final int hd) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(result)) {
					Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
				} else {
					if (timer != null) {
						timer.cancel();
					}
					m3u8Utils.download(episode_id);
				}
				startTimer();
				PromptManager.closeCancelProgressDialog();
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieUrlApi movieUrlApi = new MovieUrlApi();
				RealUrl realUrl = movieUrlApi.getUrl(url, hd + "", true);
				String downloadUrl = "";
				boolean isM3U8 = false;
				String useragent = "";
				if (realUrl != null) {
					downloadUrl = realUrl.getUrl();
					if (realUrl.getFormat().equals("m3u8")) {
						isM3U8 = true;
					}
					useragent = realUrl.getUseragent();
				}
				if (!StringUtils.isEmpty(downloadUrl)) {
					// downloadUrl =
					// "http://vapi.saaser.cn/v1/youtu/sm3u8/id/MDAwMDAwMDAwMJKruJKUptWsfZi82ZmNbGSaZ7rZumhtrYFsjJjEZZvXldyf0ouon4-Fu7W2gaFkeI6Iq7O3fZdpiWl3a7GGaNCVqpfVgZTAr5OXk52XsHioiHeV3q2Kga6Cim9v_3.m3u8";
					m3u8Utils.initDownload(downloadUrl, episode_id, isM3U8, useragent);
				}
				return downloadUrl;
			}
		}.execute("");
	}

	private void showEdit() {
		isEditShow = true;
		editLayout.setVisibility(View.VISIBLE);

		editLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, editHeight, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		editLayout.startAnimation(bottomtranslateAnimation);
	}

	private void hideEdit() {
		selectList.clear();
		isEditShow = false;
		editLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 0, editHeight);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);
		bottomtranslateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				editLayout.clearAnimation();
				editLayout.setVisibility(View.GONE);
			}
		});

		editLayout.startAnimation(bottomtranslateAnimation);

	}

	class KeyComparator implements Comparator<String> {

		@Override
		public int compare(String str1, String str2) {

			int num1 = Integer.parseInt(str1);
			int num2 = Integer.parseInt(str2);

			if (num1 > num2) {
				return 1;
			} else if (num1 < num2) {
				return -1;
			}

			return 0;
		}
	}

	private Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			refreshData();
		};
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startTimer();
	}

	private void startTimer() {
		if (timer != null) {
			timer.cancel();
		}

		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(0);
			}
		};

		timer.schedule(task, 0, 1000);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (timer != null) {
			timer.cancel();
		}
	}
}
