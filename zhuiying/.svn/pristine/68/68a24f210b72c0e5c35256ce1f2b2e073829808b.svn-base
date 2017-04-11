package com.bccv.zhuiying.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.cyberplayer.utils.s;
import com.bccv.zhuiying.R;
import com.bccv.zhuiying.adapter.DownloadAdapter;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler.State;
import com.tendcloud.tenddata.TCAgent;
import com.utils.download.DownloadInfo;
import com.utils.download.DownloadManager;
import com.utils.download.DownloadService;
import com.utils.model.DownloadMovie;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.DimensionPixelUtil;
import com.utils.tools.FileUtils;
import com.utils.tools.SerializationUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DownloadActivity extends BaseActivity {
	private LinearLayout editLayout;
	private ImageView noneImageView;
	private ListView listView;
	private TextView editTextView, selectTextView, deleteTextView;
	private DownloadManager downloadManager;
	private List<DownloadMovie> list, getList, selectList;
	private DownloadAdapter adapter;
	private boolean isEditShow = false;
	private float editHeight = 0;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TCAgent.onPageStart(getApplicationContext(), "DownloadActivity");
		setContentView(R.layout.activity_download);
		downloadManager = DownloadService.getDownloadManager(getApplicationContext());
		editHeight = DimensionPixelUtil.dip2px(getApplicationContext(), 60);
		selectList = new ArrayList<DownloadMovie>();
		setTitle();
		initView();
//		getData();
//		getDataWithThread();
	}

	private void setTitle() {
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
		listView = (ListView) findViewById(R.id.pList);
		editLayout = (LinearLayout) findViewById(R.id.edit_layout);
		selectTextView = (TextView) findViewById(R.id.select_textView);
		selectTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < list.size(); i++) {
					DownloadMovie downloadMovie = list.get(i);
					downloadMovie.setSelect(true);
					if (!selectList.contains(downloadMovie)) {
						selectList.add(downloadMovie);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		deleteTextView = (TextView) findViewById(R.id.delete_textView);
		deleteTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (timer != null) {
					timer.cancel();
				}
				deleteData();
			}
		});
		list = new ArrayList<DownloadMovie>();
		adapter = new DownloadAdapter(getApplicationContext(), list);
		listView.setAdapter(adapter);
		// SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		//
		// @Override
		// public void create(SwipeMenu menu) {
		// // TODO Auto-generated method stub
		// SwipeMenuItem item = new SwipeMenuItem(getApplicationContext());
		// item.setBackground(new ColorDrawable(Color.parseColor("#ff3b30")));
		// item.setWidth((int)
		// DimensionPixelUtil.dip2px(getApplicationContext(), 90));
		// item.setTitle("删除");
		// item.setTitleColor(Color.WHITE);
		// item.setTitleSize(15);
		// menu.addMenuItem(item);
		// }
		// };
		// listView.setMenuCreator(swipeMenuCreator);
		//
		// listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		//
		// @Override
		// public boolean onMenuItemClick(int position, SwipeMenu menu, int
		// index) {
		// // TODO Auto-generated method stub
		// deleteMovieData(list.get(position));
		// return false;
		// }
		// });
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
					if (downloadMovie.getModel_id().equals("abc")) {
						Intent intent = new Intent(getApplicationContext(), DownloadMovieActivity.class);
						intent.putExtra("isEpisode", false);
						startActivity(intent);
					} else if (downloadMovie.isEpisode()) {
						Intent intent = new Intent(getApplicationContext(), DownloadMovieActivity.class);
						intent.putExtra("isEpisode", true);
						intent.putExtra("movie_id", downloadMovie.getModel_id());
						intent.putExtra("type_id", downloadMovie.getModelType_id());
						intent.putExtra("title", downloadMovie.getTitle());
						startActivity(intent);
					} else {
						Intent intent = new Intent(getApplicationContext(), VideoMagnetPlayerActivity.class);
						intent.putExtra("url", downloadMovie.getLocalUrl());
						intent.putExtra("title", downloadMovie.getTitle());
						startActivity(intent);
					}
				}
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
					editTextView.setEnabled(true);
					noneImageView.setVisibility(View.GONE);
					list.clear();
					list.addAll(getList);
					if (isEditShow) {
						for (int i = 0; i < list.size(); i++) {
							list.get(i).setEdit(true);
							if (selectList.contains(list.get(i))) {
								list.get(i).setSelect(true);
							} else {
								list.get(i).setSelect(false);
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
					editTextView.setEnabled(false);
					if (timer != null) {
						timer.cancel();
					}
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				if (getList == null) {
					getList = new ArrayList<DownloadMovie>();
				} else {
					getList.clear();
				}
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

				boolean hasDownload = false;

				for (int i = 0; i < keyList.size(); i++) {
					List<DownloadInfo> list = map.get(keyList.get(i));
					boolean isSuccess = true;
					long totalSize = 0;
					long lastSize = 0;
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
							File file = new File(downloadInfo.getFileSavePath());
							long length = file.length();
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
							hasDownload = true;

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
						downloadMovie.setFileNum(fileSize);

						downloadMovie.setFileSize(fileTotalSize);
						downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());

						movieMap.put(keyList.get(i), downloadMovie);
					}
				}

				DownloadMovie downloadingMovie = new DownloadMovie();
				downloadingMovie.setDownloading(true);
				downloadingMovie.setTitle("正在下载");
				downloadingMovie.setModel_id("abc");
				downloadingMovie.setModelType_id("def");
				downloadingMovie.setEpisode_id(0);
				downloadingMovie.setM3U8(true);
				boolean isPause = true;
				for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
					DownloadMovie downloadMovie = entry.getValue();
					if (downloadMovie.isSuccess()) {
						if (!getList.contains(downloadMovie)) {
							if (downloadMovie.isEpisode()) {
								DownloadMovie newDownloadMovie = new DownloadMovie();
								newDownloadMovie.setDownloading(false);
								newDownloadMovie.setEpisode(downloadMovie.isEpisode());
								newDownloadMovie.setEpisode_id(0);
								newDownloadMovie.setImageUrl(downloadMovie.getImageUrl());
								newDownloadMovie.setModel_id(downloadMovie.getModel_id());
								newDownloadMovie.setModelType_id(downloadMovie.getModelType_id());
								newDownloadMovie.setTitle(downloadMovie.getTitle());
								newDownloadMovie.setSpeed("完成");
								newDownloadMovie.setSize(downloadMovie.getSize());
								newDownloadMovie.setLocalUrl(downloadMovie.getLocalUrl());
								newDownloadMovie.setSuccess(true);
								getList.add(newDownloadMovie);
							} else {
								getList.add(downloadMovie);
							}

						}

					} else {
						downloadingMovie.setSize(downloadMovie.getTitle());
						hasDownload = true;
						if (downloadMovie.isDownloading()) {
							isPause = false;
						}
						downloadingMovie.setFileNum(downloadingMovie.getFileNum() + downloadMovie.getFileNum());
						downloadingMovie.setFileSize(downloadingMovie.getFileSize() + downloadMovie.getFileSize());
						downloadingMovie.setSpeedNum(downloadingMovie.getSpeedNum() + downloadMovie.getSpeedNum());
					}
				}
				if (hasDownload) {
					if (!isPause) {
						downloadingMovie.setSpeed(FileUtils.formatFileSize(downloadingMovie.getSpeedNum()) + "/S");
						if (downloadingMovie.getFileSize() > 0) {
							downloadingMovie
									.setProgress(downloadingMovie.getFileNum() * 100 / downloadingMovie.getFileSize());
						} else {
							downloadingMovie.setProgress(0);
						}

					} else {
						downloadingMovie.setSpeed("暂停");
					}
					getList.add(0, downloadingMovie);
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
				Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
				if (getList == null) {
					getList = new ArrayList<DownloadMovie>();
				} else {
					getList.clear();
				}
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

				boolean hasDownload = false;

				for (int i = 0; i < keyList.size(); i++) {
					List<DownloadInfo> list = map.get(keyList.get(i));
					boolean isSuccess = true;
					long totalSize = 0;
					long lastSize = 0;
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
							File file = new File(downloadInfo.getFileSavePath());
							long length = file.length();
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
							hasDownload = true;

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
						downloadMovie.setFileNum(fileSize);

						downloadMovie.setFileSize(fileTotalSize);
						downloadMovie.setLocalUrl(downloadInfo.getLocalUrl());

						movieMap.put(keyList.get(i), downloadMovie);
					}
				}

				DownloadMovie downloadingMovie = new DownloadMovie();
				downloadingMovie.setDownloading(true);
				downloadingMovie.setTitle("正在下载");
				downloadingMovie.setModel_id("abc");
				downloadingMovie.setModelType_id("def");
				downloadingMovie.setEpisode_id(0);
				downloadingMovie.setM3U8(true);
				boolean isPause = true;
				for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
					DownloadMovie downloadMovie = entry.getValue();
					if (downloadMovie.isSuccess()) {
						if (!getList.contains(downloadMovie)) {
							if (downloadMovie.isEpisode()) {
								DownloadMovie newDownloadMovie = new DownloadMovie();
								newDownloadMovie.setDownloading(false);
								newDownloadMovie.setEpisode(downloadMovie.isEpisode());
								newDownloadMovie.setEpisode_id(0);
								newDownloadMovie.setImageUrl(downloadMovie.getImageUrl());
								newDownloadMovie.setModel_id(downloadMovie.getModel_id());
								newDownloadMovie.setModelType_id(downloadMovie.getModelType_id());
								newDownloadMovie.setTitle(downloadMovie.getTitle());
								newDownloadMovie.setSpeed("完成");
								newDownloadMovie.setSize(downloadMovie.getSize());
								newDownloadMovie.setLocalUrl(downloadMovie.getLocalUrl());
								newDownloadMovie.setSuccess(true);
								getList.add(newDownloadMovie);
							} else {
								getList.add(downloadMovie);
							}

						}

					} else {
						downloadingMovie.setSize(downloadMovie.getTitle());
						hasDownload = true;
						if (downloadMovie.isDownloading()) {
							isPause = false;
						}
						downloadingMovie.setFileNum(downloadingMovie.getFileNum() + downloadMovie.getFileNum());
						downloadingMovie.setFileSize(downloadingMovie.getFileSize() + downloadMovie.getFileSize());
						downloadingMovie.setSpeedNum(downloadingMovie.getSpeedNum() + downloadMovie.getSpeedNum());
					}
				}
				if (hasDownload) {
					if (!isPause) {
						downloadingMovie.setSpeed(FileUtils.formatFileSize(downloadingMovie.getSpeedNum()) + "/S");
						if (downloadingMovie.getFileSize() > 0) {
							downloadingMovie
									.setProgress(downloadingMovie.getFileNum() * 100 / downloadingMovie.getFileSize());
						} else {
							downloadingMovie.setProgress(0);
						}

					} else {
						downloadingMovie.setSpeed("暂停");
					}
					getList.add(0, downloadingMovie);
				}

				SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				getDataHandler.sendEmptyMessage(0);
			}
		}).start();
	}
	
	Handler getDataHandler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			if (getList != null && getList.size() > 0) {
				editTextView.setEnabled(true);
				noneImageView.setVisibility(View.GONE);
				list.clear();
				list.addAll(getList);
				if (isEditShow) {
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(true);
						if (selectList.contains(list.get(i))) {
							list.get(i).setSelect(true);
						} else {
							list.get(i).setSelect(false);
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

				editTextView.setEnabled(false);
				if (timer != null) {
					timer.cancel();
				}
			}
		};
	};
	
	private void refreshData() {
//		getDataWithThread();
		getData();
	}

	private void deleteData() {
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
					if (downloadMovie.getModel_id().equals("abc")) {
						for (int i = 0; i < keyList.size(); i++) {
							List<DownloadInfo> list = map.get(keyList.get(i));
							for (int k = 0; k < list.size(); k++) {
								if (list.get(k).getState() != State.SUCCESS) {
									try {
										downloadManager.removeDownloadAll(list);
									} catch (DbException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;
								}
							}
						}
						List<String> deleteList = new ArrayList<String>();
						for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
							DownloadMovie newDownloadMovie = entry.getValue();
							if (!newDownloadMovie.isSuccess()) {
								deleteList.add(entry.getKey());
							}
						}

						for (String string : deleteList) {
							movieMap.remove(string);
						}
					} else {
						if (downloadMovie.isEpisode()) {

							for (int i = 0; i < keyList.size(); i++) {
								List<DownloadInfo> list = map.get(keyList.get(i));
								for (int k = 0; k < list.size(); k++) {
									if (list.get(k).getModel_id().equals(downloadMovie.getModel_id())
											&& list.get(k).getModelType_id().equals(downloadMovie.getModelType_id())) {
										try {
											downloadManager.removeDownloadAll(list);
										} catch (DbException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										break;
									}
								}
							}
							List<String> deleteList = new ArrayList<String>();
							for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
								DownloadMovie newDownloadMovie = entry.getValue();
								if (newDownloadMovie.getModel_id().equals(downloadMovie.getModel_id())
										&& newDownloadMovie.getModelType_id().equals(downloadMovie.getModelType_id())) {
									deleteList.add(entry.getKey());
									String url = newDownloadMovie.getLocalUrl();

									int index = url.lastIndexOf("/");
									if (index >= 0) {
										url = url.substring(0, index);
									}
									FileUtils.deleteFile(url);
								}
							}

							for (String string : deleteList) {
								movieMap.remove(string);
							}

						} else {
							String key = downloadMovie.getModel_id() + "," + downloadMovie.getModelType_id() + ","
									+ downloadMovie.getEpisode_id();
							List<DownloadInfo> list = map.get(key);
							if (list != null) {
								try {
									downloadManager.removeDownloadAll(list);
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							movieMap.remove(key);
							String url = downloadMovie.getLocalUrl();

							int index = url.lastIndexOf("/");
							if (index >= 0) {
								url = url.substring(0, index);
							}
							FileUtils.deleteFile(url);
						}

					}

				}
				SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				return null;
			}
		}.execute("");

	}

	private void deleteMovieData(final DownloadMovie delete) {
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
					// hideEdit();
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
				if (downloadMovie.getModel_id().equals("abc")) {
					for (int i = 0; i < keyList.size(); i++) {
						List<DownloadInfo> list = map.get(keyList.get(i));
						for (int k = 0; k < list.size(); k++) {
							if (list.get(k).getState() != State.SUCCESS) {
								try {
									downloadManager.removeDownloadAll(list);
								} catch (DbException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								break;
							}
						}
					}
					List<String> deleteList = new ArrayList<String>();
					for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
						DownloadMovie newDownloadMovie = entry.getValue();
						if (!newDownloadMovie.isSuccess()) {
							deleteList.add(entry.getKey());
						}
					}

					for (String string : deleteList) {
						movieMap.remove(string);
					}
				} else {
					if (downloadMovie.isEpisode()) {

						for (int i = 0; i < keyList.size(); i++) {
							List<DownloadInfo> list = map.get(keyList.get(i));
							for (int k = 0; k < list.size(); k++) {
								if (list.get(k).getModel_id().equals(downloadMovie.getModel_id())
										&& list.get(k).getModelType_id().equals(downloadMovie.getModelType_id())) {
									try {
										downloadManager.removeDownloadAll(list);
									} catch (DbException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									break;
								}
							}
						}
						List<String> deleteList = new ArrayList<String>();
						for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
							DownloadMovie newDownloadMovie = entry.getValue();
							if (newDownloadMovie.getModel_id().equals(downloadMovie.getModel_id())
									&& newDownloadMovie.getModelType_id().equals(downloadMovie.getModelType_id())) {
								deleteList.add(entry.getKey());
								String url = newDownloadMovie.getLocalUrl();

								int index = url.lastIndexOf("/");
								if (index >= 0) {
									url = url.substring(0, index);
								}
								FileUtils.deleteFile(url);
							}
						}

						for (String string : deleteList) {
							movieMap.remove(string);
						}

					} else {
						String key = downloadMovie.getModel_id() + "," + downloadMovie.getModelType_id() + ","
								+ downloadMovie.getEpisode_id();
						List<DownloadInfo> list = map.get(key);
						if (list != null) {
							try {
								downloadManager.removeDownloadAll(list);
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						movieMap.remove(key);
						String url = downloadMovie.getLocalUrl();

						int index = url.lastIndexOf("/");
						if (index >= 0) {
							url = url.substring(0, index);
						}
						FileUtils.deleteFile(url);
					}

				}

				SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
				return null;
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
		isEditShow = false;
		selectList.clear();
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (timer != null) {
			timer.cancel();
		}
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

		timer.schedule(task, 100, 1000);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "DownloadActivity");
	}

}
