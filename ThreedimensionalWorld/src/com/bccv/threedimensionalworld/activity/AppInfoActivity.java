package com.bccv.threedimensionalworld.activity;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.cyberplayer.core.BVideoView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.api.GameApi;
import com.bccv.threedimensionalworld.download.DownloadInfo;
import com.bccv.threedimensionalworld.download.DownloadManager;
import com.bccv.threedimensionalworld.download.DownloadService;
import com.bccv.threedimensionalworld.model.App;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.StringUtils;
import com.bccv.threedimensionalworld.view.MyTitleView;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.http.HttpHandler.State;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi")
public class AppInfoActivity extends BaseActivity implements OnClickListener {
	private enum AppState {
		INSTALLED, DOWNLOADING, CANCELLED, FAILURE, UNINSTALL, UNDOWNLOAD
	};

	private Button leftbackButton, rightbackButton, leftinstallButton,
			rightinstallButton, leftuninstallButton, rightuninstallButton;
	private ProgressBar leftProgressBar, rightProgressBar;
	private ImageView leftIconImageView, rightIconImageView, leftImageView1,
			rightImageView1, leftImageView2, rightImageView2, leftImageView3,
			rightImageView3;
	private TextView lefttitleTextView, righttitleTextView, lefttypeTextView,
			righttypeTextView, leftinfoTextView, rightinfoTextView, leftrating,
			rightrating, leftprogressTextView, rightprogressTextView;
	private RatingBar leftRatingBar, rightRatingBar;
	private LinearLayout leftLayout, rightLayout;

	private App app;
	private boolean isGame, hasInit = false;
	private String id;
	private AppState appState = AppState.UNDOWNLOAD;
	private DownloadManager downloadManager;
	private DownloadInfo downloadInfo;
	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appinfo);
		isGame = getIntent().getBooleanExtra("isGame", false);
		id = getIntent().getStringExtra("id");

		setStatus();
		initBack();
		initLeftView();
		initRightView();
		getData();
	}

	private void initBack() {
		leftbackButton = (Button) findViewById(R.id.left_back_button);
		rightbackButton = (Button) findViewById(R.id.right_back_button);
		leftbackButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightbackButton.setSelected(hasFocus);
			}
		});

		leftbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		rightbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void initLeftView() {
		if (isGame) {
			leftLayout = (LinearLayout) findViewById(R.id.left_layout);
			leftLayout.setBackgroundResource(R.drawable.game_bg2);
		}else {
			leftLayout = (LinearLayout) findViewById(R.id.left_layout);
			leftLayout.setBackgroundResource(R.drawable.app_bg2);
		}
		
		leftinstallButton = (Button) findViewById(R.id.left_install_button);
		leftuninstallButton = (Button) findViewById(R.id.left_uninstall_button);
		leftProgressBar = (ProgressBar) findViewById(R.id.left_download_pb);
		leftRatingBar = (RatingBar) findViewById(R.id.left_ratingBar);
		leftIconImageView = (ImageView) findViewById(R.id.left_app_ImageView);
		leftImageView1 = (ImageView) findViewById(R.id.left_imageView1);
		leftImageView2 = (ImageView) findViewById(R.id.left_imageView2);
		leftImageView3 = (ImageView) findViewById(R.id.left_imageView3);
		lefttitleTextView = (TextView) findViewById(R.id.left_title_textView);
		leftrating = (TextView) findViewById(R.id.left_score_textView);
		lefttypeTextView = (TextView) findViewById(R.id.left_type_textView);
		leftinfoTextView = (TextView) findViewById(R.id.left_info_textView);
		leftprogressTextView = (TextView) findViewById(R.id.left_progress_textView);

		leftinstallButton.setScaleX(0.5f);
		leftuninstallButton.setScaleX(0.5f);
		leftProgressBar.setScaleX(0.5f);
		leftprogressTextView.setScaleX(0.5f);
		leftRatingBar.setScaleX(0.5f);
		lefttitleTextView.setScaleX(0.5f);
		leftrating.setScaleX(0.5f);
		lefttypeTextView.setScaleX(0.5f);
		leftinfoTextView.setScaleX(0.5f);

		AssetManager mgr = getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		leftrating.setTypeface(tf);// 设置字体

		leftinstallButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightinstallButton.setSelected(hasFocus);
			}
		});

		leftuninstallButton
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						rightuninstallButton.setSelected(hasFocus);
					}
				});

	}

	private void initRightView() {
		if (isGame) {
			rightLayout = (LinearLayout) findViewById(R.id.right_layout);
			rightLayout.setBackgroundResource(R.drawable.game_bg2);
		}else {
			rightLayout = (LinearLayout) findViewById(R.id.right_layout);
			rightLayout.setBackgroundResource(R.drawable.app_bg2);
		}
		rightinstallButton = (Button) findViewById(R.id.right_install_button);
		rightuninstallButton = (Button) findViewById(R.id.right_uninstall_button);
		rightProgressBar = (ProgressBar) findViewById(R.id.right_download_pb);
		rightRatingBar = (RatingBar) findViewById(R.id.right_ratingBar);
		rightIconImageView = (ImageView) findViewById(R.id.right_app_ImageView);
		rightImageView1 = (ImageView) findViewById(R.id.right_imageView1);
		rightImageView2 = (ImageView) findViewById(R.id.right_imageView2);
		rightImageView3 = (ImageView) findViewById(R.id.right_imageView3);
		righttitleTextView = (TextView) findViewById(R.id.right_title_textView);
		rightrating = (TextView) findViewById(R.id.right_score_textView);
		righttypeTextView = (TextView) findViewById(R.id.right_type_textView);
		rightinfoTextView = (TextView) findViewById(R.id.right_info_textView);
		rightprogressTextView = (TextView) findViewById(R.id.right_progress_textView);

		rightinstallButton.setScaleX(0.5f);
		rightuninstallButton.setScaleX(0.5f);
		rightProgressBar.setScaleX(0.5f);
		rightprogressTextView.setScaleX(0.5f);
		rightRatingBar.setScaleX(0.5f);
		righttitleTextView.setScaleX(0.5f);
		rightrating.setScaleX(0.5f);
		righttypeTextView.setScaleX(0.5f);
		rightinfoTextView.setScaleX(0.5f);

		AssetManager mgr = getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		rightrating.setTypeface(tf);// 设置字体

		rightProgressBar.setProgress(0);
		leftProgressBar.setProgress(0);
	}

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (app != null) {
					if (StringUtils.isEmpty(app.getApp_package())) {
						app.setApp_package("com.halfbrick.fruitninjafree");
					}
					setView();
				}
			}
		};

		new DataAsyncTask(callback, false) {
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				GameApi gameApi = new GameApi();
				app = gameApi.getAppInfo(id, isGame);
				return null;
			}
		}.execute("");
	}

	private void setView() {
		ImageLoader imageLoader = ImageLoader.getInstance();

		lefttitleTextView.setText(app.getName());
		leftRatingBar.setStepSize(0.5f);
		leftRatingBar.setRating((app.getApp_score_interface() - 0.5f) / 2);
		leftrating.setText(app.getApp_score_interface() + "分");
		lefttypeTextView.setText("类别:" + app.getGenres_name() + "        语言:"
				+ app.getLanguage() + "        大小:" + app.getApp_size());
		leftinfoTextView.setText(app.getIntro());
		imageLoader.displayImage(app.getImages(), leftIconImageView,
				GlobalParams.appIconOptions);
		if (app.getPics() != null) {
			switch (app.getPics().size()) {
			case 3:
				imageLoader.displayImage(app.getPics().get(2), leftImageView3,
						GlobalParams.appInfoOptions);
			case 2:
				imageLoader.displayImage(app.getPics().get(1), leftImageView2,
						GlobalParams.appInfoOptions);
			case 1:
				imageLoader.displayImage(app.getPics().get(0), leftImageView1,
						GlobalParams.appInfoOptions);
				break;
			default:
				break;
			}

		}

		righttitleTextView.setText(app.getName());
		rightRatingBar.setStepSize(0.5f);
		rightRatingBar.setRating((app.getApp_score_interface() - 0.5f) / 2);
		rightrating.setText(app.getApp_score_interface() + "分");
		righttypeTextView.setText("类别:" + app.getGenres_name() + "        语言:"
				+ app.getLanguage() + "        大小:" + app.getApp_size());
		rightinfoTextView.setText(app.getIntro());
		imageLoader.displayImage(app.getImages(), rightIconImageView,
				GlobalParams.appIconOptions);
		if (app.getPics() != null) {
			switch (app.getPics().size()) {
			case 3:
				imageLoader.displayImage(app.getPics().get(2), rightImageView3,
						GlobalParams.appInfoOptions);
			case 2:
				imageLoader.displayImage(app.getPics().get(1), rightImageView2,
						GlobalParams.appInfoOptions);
			case 1:
				imageLoader.displayImage(app.getPics().get(0), rightImageView1,
						GlobalParams.appInfoOptions);
				break;
			default:
				break;
			}

		}

		setButton();
		setState();
		refreshButton();
		if (appState == AppState.DOWNLOADING) {
			startProgress();
		} else if (appState == appState.CANCELLED) {
			if (downloadInfo != null && downloadInfo.getFileLength() > 0) {
				int progress = (int) (downloadInfo.getProgress() * 100 / downloadInfo
						.getFileLength());
				leftProgressBar.setProgress(progress);
				rightProgressBar.setProgress(progress);
				rightprogressTextView.setText(progress + "%");
				leftprogressTextView.setText(progress + "%");
				if (progress >= 100) {
					appState = AppState.UNINSTALL;
					refreshButton();
					stopProgress();
				}
			}
		}
	}

	private void setButton() {
		leftinstallButton.setOnClickListener(this);
		rightinstallButton.setOnClickListener(this);

		leftuninstallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uninstallApp(getApplicationContext(), app.getApp_package());
			}
		});

		rightuninstallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				uninstallApp(getApplicationContext(), app.getApp_package());
			}
		});
	}

	private void refreshButton() {
		hasInit = true;
		if (appState == AppState.INSTALLED) {
			if (downloadInfo != null) {
				File file = new File(downloadInfo.getFileSavePath());
				if (file.exists()) {
					file.delete();
					try {
						downloadManager.removeDownload(downloadInfo);
					} catch (DbException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			leftinstallButton.setText("打开");
			leftinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);
			rightinstallButton.setText("打开");
			rightinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);

			leftProgressBar.setVisibility(View.GONE);
			rightProgressBar.setVisibility(View.GONE);
			leftprogressTextView.setVisibility(View.GONE);
			rightprogressTextView.setVisibility(View.GONE);
			leftuninstallButton.setVisibility(View.VISIBLE);
			rightuninstallButton.setVisibility(View.VISIBLE);

		} else if (appState == AppState.DOWNLOADING) {
			leftinstallButton.setText("暂停");
			leftinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);
			rightinstallButton.setText("暂停");
			rightinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);

			leftProgressBar.setVisibility(View.VISIBLE);
			rightProgressBar.setVisibility(View.VISIBLE);
			leftprogressTextView.setVisibility(View.VISIBLE);
			rightprogressTextView.setVisibility(View.VISIBLE);
			leftuninstallButton.setVisibility(View.GONE);
			rightuninstallButton.setVisibility(View.GONE);
		} else if (appState == AppState.CANCELLED) {
			leftinstallButton.setText("继续");
			leftinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);
			rightinstallButton.setText("继续");
			rightinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);

			leftProgressBar.setVisibility(View.VISIBLE);
			rightProgressBar.setVisibility(View.VISIBLE);
			leftprogressTextView.setVisibility(View.VISIBLE);
			rightprogressTextView.setVisibility(View.VISIBLE);
			leftuninstallButton.setVisibility(View.GONE);
			rightuninstallButton.setVisibility(View.GONE);
		} else if (appState == AppState.FAILURE) {
			leftinstallButton.setText("重试");
			leftinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);
			rightinstallButton.setText("重试");
			rightinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);

			leftProgressBar.setVisibility(View.VISIBLE);
			rightProgressBar.setVisibility(View.VISIBLE);
			leftprogressTextView.setVisibility(View.VISIBLE);
			rightprogressTextView.setVisibility(View.VISIBLE);
			leftuninstallButton.setVisibility(View.GONE);
			rightuninstallButton.setVisibility(View.GONE);
		} else if (appState == AppState.UNINSTALL) {
			leftinstallButton.setText("安装");
			leftinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);
			rightinstallButton.setText("安装");
			rightinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_option);

			leftProgressBar.setVisibility(View.GONE);
			rightProgressBar.setVisibility(View.GONE);
			leftprogressTextView.setVisibility(View.GONE);
			rightprogressTextView.setVisibility(View.GONE);
			leftuninstallButton.setVisibility(View.GONE);
			rightuninstallButton.setVisibility(View.GONE);
		} else if (appState == AppState.UNDOWNLOAD) {
			leftinstallButton.setText("");
			leftinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_install);
			rightinstallButton.setText("");
			rightinstallButton
					.setBackgroundResource(R.drawable.appinfo_game_install);

			leftProgressBar.setVisibility(View.GONE);
			rightProgressBar.setVisibility(View.GONE);
			leftprogressTextView.setVisibility(View.GONE);
			rightprogressTextView.setVisibility(View.GONE);
			leftuninstallButton.setVisibility(View.GONE);
			rightuninstallButton.setVisibility(View.GONE);
		}
	}

	private void setState() {
		if (checkIsInstalled(app.getApp_package())) {
			appState = AppState.INSTALLED;
		} else {
			initDownload();
		}
	}

	private void initDownload() {
		downloadManager = DownloadService
				.getDownloadManager(getApplicationContext());
		for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
			DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
			String app_id;
			if (isGame) {
				app_id = app.getId() + "0";
			} else {
				app_id = app.getId() + "1";
			}
			if (downloadInfo.getApp_idString().equals(app_id)) {
				this.downloadInfo = downloadInfo;
				if (downloadInfo.getState() == State.SUCCESS) {
					File file = new File(downloadInfo.getFileSavePath());
					if (file.exists()) {
						appState = AppState.UNINSTALL;
					} else {
						appState = AppState.UNDOWNLOAD;
					}

				} else if (downloadInfo.getState() == State.CANCELLED) {
					appState = AppState.CANCELLED;
				} else if (downloadInfo.getState() == State.WAITING) {
					appState = AppState.DOWNLOADING;
				} else if (downloadInfo.getState() == State.STARTED) {
					appState = AppState.DOWNLOADING;
				} else if (downloadInfo.getState() == State.LOADING) {
					appState = AppState.DOWNLOADING;
				} else if (downloadInfo.getState() == State.FAILURE) {
					appState = AppState.FAILURE;
				}
				return;
			}
		}
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

	private void installApp(Context context, String path) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			String appName = pm.getApplicationLabel(appInfo).toString();
			String packageName = appInfo.packageName;
			PackageInfo packageInfo1;
			try {
				packageInfo1 = pm.getPackageInfo(packageName, 0);

			} catch (NameNotFoundException e) {
				packageInfo1 = null;
				e.printStackTrace();
			}
			if (packageInfo1 == null) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction(android.content.Intent.ACTION_VIEW);
				Uri uri = Uri.fromFile(new File(path));
				intent.setDataAndType(uri,
						"application/vnd.android.package-archive");
				context.startActivity(intent);
			} else {
				// File file = new File(path);
				// if (file.exists()) {
				// file.delete();
				// }
				Intent intent = pm.getLaunchIntentForPackage(packageName);
				context.startActivity(intent);
			}
		}
	}

	private void uninstallApp(Context context, String packageName) {
		Uri uri = Uri.parse("package:" + packageName);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		startActivity(intent);
	}

	private void downloadApp() {
		String url = app.getApp_download();

		String[] strings = url.split("/");
		String fileName = strings[strings.length - 1];

		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String app_id = app.getId();
		if (isGame) {
			path = path + "/Game/" + fileName;
			app_id = app_id + "0";
		} else {
			path = path + "/App/" + fileName;
			app_id = app_id + "1";
		}
		try {
			downloadInfo = downloadManager.addNewDownload(url, app.getName(),
					path, true, false, null, app_id, app.getImages(), app.getName(), app.getApp_package(), app.getApp_score_interface(), app.getIntro());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void startApp(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();

		PackageInfo packageInfo1;
		try {
			packageInfo1 = pm.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo1 = null;
			e.printStackTrace();
		}
		if (packageInfo1 != null) {
			Intent intent = pm.getLaunchIntentForPackage(packageName);
			context.startActivity(intent);
		}
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

		if (hasInit) {
			setState();
			refreshButton();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (appState) {
		case INSTALLED:
			startApp(getApplicationContext(), app.getApp_package());
			break;
		case DOWNLOADING:
			try {
				downloadManager.stopDownload(downloadInfo);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			appState = AppState.CANCELLED;
			stopProgress();
			break;
		case CANCELLED:
			try {
				downloadManager.resumeDownload(downloadInfo, null);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			appState = AppState.DOWNLOADING;
			startProgress();
			break;
		case FAILURE:
			try {
				downloadManager.resumeDownload(downloadInfo, null);
			} catch (DbException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			appState = AppState.DOWNLOADING;
			startProgress();
			break;
		case UNINSTALL:
			installApp(getApplicationContext(), downloadInfo.getFileSavePath());
			break;
		case UNDOWNLOAD:
			downloadApp();
			appState = AppState.DOWNLOADING;
			startProgress();
			break;
		default:
			break;
		}
		refreshButton();
	}

	private void startProgress() {
		if (timer == null) {
			timer = new Timer();
		} else {
			timer.cancel();
			timer = new Timer();
		}

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressHandler.sendEmptyMessage(0);
			}
		};

		timer.schedule(task, 100, 1000);
	}

	private void stopProgress() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private Handler progressHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			if (downloadInfo != null && downloadInfo.getFileLength() > 0) {
				int progress = (int) (downloadInfo.getProgress() * 100 / downloadInfo
						.getFileLength());
				leftProgressBar.setProgress(progress);
				rightProgressBar.setProgress(progress);
				rightprogressTextView.setText(progress + "%");
				leftprogressTextView.setText(progress + "%");
				if (progress >= 100) {
					appState = AppState.UNINSTALL;
					refreshButton();
					stopProgress();
				}
			}
		};
	};

}
