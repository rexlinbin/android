package com.bccv.zhuiyingzhihanju.activity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.LoadingApi;
import com.bccv.zhuiyingzhihanju.model.Ad;
import com.bccv.zhuiyingzhihanju.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.stat.StatService;
import com.tendcloud.tenddata.TCAgent;
import com.utils.download.DownloadManager;
import com.utils.download.DownloadUtil;
import com.utils.model.AppInfo;
import com.utils.model.DownloadMovie;
import com.utils.model.UpdateInfo;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.SerializationUtil;
import com.utils.tools.Statistics;
import com.utils.tools.StringUtils;
import com.utils.updatedownload.DownLoadAPI;
import com.utils.views.RoundProgressBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class LoadingActivity extends BaseActivity {
	private ImageView loading;
	private Button button;
	private TextView titleTextView, introTextView, goTextView;
	private DataAsyncTask dataAsyncTask;
	private AppInfo appInfo;
	private RelativeLayout adLayout;
	private List<Ad> list;
	private RoundProgressBar r;
private LinearLayout LI;
private void tcStart(){
	TCAgent.onPageStart(getApplicationContext(), "LoadingActivity");
}

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	TCAgent.onPageEnd(getApplicationContext(), "LoadingActivity");
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_loading);
		StatService.trackCustomBeginEvent(this, "playTime", "app");
		setView();
		initDownload();
		getData();
	}

	private void setView() {
		adLayout = (RelativeLayout) findViewById(R.id.ad_layout);
		adLayout.setVisibility(View.INVISIBLE);
		loading = (ImageView) findViewById(R.id.loading_imageView);
		button = (Button) findViewById(R.id.button);
		button.setVisibility(View.GONE);
		titleTextView = (TextView) findViewById(R.id.title_textView);
		introTextView = (TextView) findViewById(R.id.intro_textView);
		introTextView.setVisibility(View.GONE);
		goTextView = (TextView) findViewById(R.id.go_textView);
		r = (RoundProgressBar) findViewById(R.id.roundProgressBar1);
		
		LI=(LinearLayout) findViewById(R.id.linearLayout1);

		if (AppConfig.getPrefUserInfo() != null) {
			GlobalParams.user = AppConfig.getPrefUserInfo();
			GlobalParams.hasLogin = true;

		} else {

			User userInfo = new User();

			GlobalParams.user = userInfo;
			GlobalParams.hasLogin = false;

		}
		
	}

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (list != null && list.size() > 0) {
					adLayout.setVisibility(View.VISIBLE);
					Ad ad = list.get(0);
					ImageLoader imageLoader = ImageLoader.getInstance();
					imageLoader.displayImage(ad.getTitlepic(), loading, GlobalParams.loadingOptions);
					titleTextView.setText(ad.getTitle());
					introTextView.setText(ad.getDes());
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							goMainActivity();
						}
					});
					r.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							goMainActivity();
						}
					});
					goTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (StringUtils.isEmpty(list.get(0).getLink())) {
								if (timer != null) {
									timer.cancel();
								}
//								Intent intent = new Intent(getApplicationContext(), Video2DPlayerActivity.class);
//								intent.putExtra("movie_id", list.get(0).getVideo_id());
//								intent.putExtra("type_id", list.get(0).getType_id());
//								intent.putExtra("episodes_id", "1");
//								intent.putExtra("fromLoading", true);
//								startActivity(intent);
								Bundle bundle = new Bundle();
								bundle.putString("movie_id", list.get(0).getVideo_id());
								bundle.putString("type_id", list.get(0).getType_id());
								bundle.putString("episodes_id", "0");
								bundle.putBoolean("fromLoading", true);
								startActivityWithSlideAnimation(VideoInfoActivity.class, bundle);
								finish();
							}else{
								if (timer != null) {
									timer.cancel();
								}
								Intent intent = new Intent(getApplicationContext(), AdActivity.class);
								intent.putExtra("fromLoading", true);
								intent.putExtra("url", list.get(0).getLink());
								startActivity(intent);
								overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
								finish();
							}
							
						}
					});
					
					
					LI.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (StringUtils.isEmpty(list.get(0).getLink())) {
								if (timer != null) {
									timer.cancel();
								}
								Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
								intent.putExtra("movie_id", list.get(0).getVideo_id());
								intent.putExtra("type_id", list.get(0).getType_id());
								intent.putExtra("episodes_id", "0");
								intent.putExtra("fromLoading", true);
								startActivity(intent);
								overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
								finish();
							}else{
								if (timer != null) {
									timer.cancel();
								}
								Intent intent = new Intent(getApplicationContext(), AdActivity.class);
								intent.putExtra("fromLoading", true);
								intent.putExtra("url", list.get(0).getLink());
								startActivity(intent);
								overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
								finish();
							}
							
						}
					});
					
					
					
					skipTimer();
					if (appInfo != null) {
						if (appInfo.getStatus() == 0) {

						} else {
							GlobalParams.isNewest = false;
							if (timer != null) {
								timer.cancel();
							}
							
							showUpdateDialog(appInfo.getData(), getApplicationContext(), LoadingActivity.this);
						}
					}
					
				}else{
					if (appInfo == null) {
						goMainActivity();
//						Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT).show();
					} else {
						
						if (appInfo.getStatus() == 0) {
							goMainActivity();
						} else {
							GlobalParams.isNewest = false;
							if (timer != null) {
								timer.cancel();
							}
							
							showUpdateDialog(appInfo.getData(), getApplicationContext(), LoadingActivity.this);
						}
					}
				}
				
			}
		};

		dataAsyncTask = new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				LoadingApi loadingApi = new LoadingApi();
				String channel = Statistics.getChannelCode();
		
				String upver = Statistics.getPackageInfo(getApplicationContext()).versionCode + "";
				appInfo = loadingApi.getAppInfo(channel, upver);
				list = loadingApi.getAdInfo(channel, upver);
				
				return null;
			}
		};
		dataAsyncTask.execute("");

	}

	/**
	 * 解析升级信息 显示升级对话框
	 * 
	 * @param data
	 *            升级信息
	 * @param context
	 */
	public void showUpdateDialog(final UpdateInfo data, final Context context, final Activity activity) {

		if (data != null) {
			final String downloadUrl = data.getDown_url();
			final String version_name = data.getDes_ver();
			String desString = data.getDes();
			final int new_version = data.getUpver();
			final Dialog dialog = new Dialog(activity, R.style.MyDialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			// 设置它的ContentView
			View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.custome_dialog, null);
			TextView tv = (TextView) view.findViewById(R.id.dialog_message);
			TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
			dialog_enter.setSelected(true);
			TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
			dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
					if (downloadUrl != null && !downloadUrl.equals("")) {
						DownLoadAPI.downLoadApk(downloadUrl,
								activity.getApplicationContext().getResources().getString(R.string.app_name),
								activity.getApplicationContext().getPackageName(), new_version, true, true);
					} else {
						Logger.e("showUpdateDialog", " 下载路径出错  downloadUrl : " + downloadUrl);
					}
					goMainActivity();
				}
			});
			
			
			dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					goMainActivity();
					dialog.cancel();
				}
			});
			desString = desString.replace("\\n", "\n");

			tv.setText("新版本: " + version_name + "\n" + desString);
			view.setMinimumWidth(600);
			dialog.setContentView(view);
			dialog.show();
		}
	}

	private Timer timer;

	public void goMainActivity() {
		if (timer != null) {
			timer.cancel();
		}
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		finish();

	}
	private int timei;
	public void skipTimer(){
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
		timer.schedule(task, list.get(0).getSec() * 10, list.get(0).getSec() * 10);
	}
	
	private Handler handler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			timei++;
			r.setProgress(timei);
			if (timei == 100) {
				goMainActivity();
			}
		};
	};
	
	private void initDownload(){
		DownloadManager downloadManager = DownloadUtil.getDownloadManager();
		Map<String, DownloadMovie> movieMap = downloadManager.getMovieMap();
		
		for (Map.Entry<String, DownloadMovie> entry : movieMap.entrySet()) {
			DownloadMovie list = entry.getValue();
			if (!list.isSuccess()) {
				list.setDownloading(false);
				list.setSpeed("暂停");
				list.setSpeedNum(0);
			}else{
				list.setSpeed("完成");
			}

		}

		if (movieMap.size() > 0) {
			downloadManager.setMovieMap(movieMap);
			SerializationUtil.wirteDownloadSerialization(getApplicationContext(), (Serializable) movieMap);
		}
	}
}
