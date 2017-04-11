package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnNetworkSpeedListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.MovieUrlApi;
import com.bccv.zhuiyingzhihanju.model.TV;
import com.bccv.zhuiyingzhihanju.model.TVUrl;
import com.bccv.zhuiyingzhihanju.model.TVchannel;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;
import com.utils.tools.SystemUtils;
import com.utils.views.HorizontalListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "HandlerLeak", "Wakelock", "DefaultLocale", "ClickableViewAccessibility", "InflateParams" })
public class VideoTVPlayerActivity extends BaseActivity implements OnPreparedListener, OnCompletionListener,
		OnErrorListener, OnInfoListener, OnPlayingBufferCacheListener, OnTouchListener {
	private final String TAG = "VideoViewPlayingActivity";

	/**
	 * 您的ak
	 */
	private String AK = GlobalParams.AK;
	/**
	 * 您的sk的前16位
	 */
	private String SK = GlobalParams.SK;

	private String mVideoSource = "";
	private String titleString = "";
	private TV tv;
	private List<TV> tvList;
	private int maxColumn, currTV;

	private BVideoView mVV = null;
	private RelativeLayout mViewHolder = null;

	private boolean mIsHwDecode = false;

	private EventHandler mEventHandler;
	private HandlerThread mHandlerThread;

	private final Object SYNC_Playing = new Object();

	private final int EVENT_PLAY = 0;

	private WakeLock mWakeLock = null;
	private static final String POWER_LOCK = "VideoPlayerActivity";

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

	/**
	 * 记录播放位置
	 */
	private int mLastPos = 0;

	class EventHandler extends Handler {
		public EventHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case EVENT_PLAY:
				/**
				 * 如果已经播放了，等待上一次播放结束
				 */
				if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
					synchronized (SYNC_Playing) {
						try {
							SYNC_Playing.wait();
							Log.v(TAG, "wait player status to idle");
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				isSeeking = false;
				mHandler.sendEmptyMessage(4);

				/**
				 * 设置播放url
				 */
				// mVideoSource =
				// "http://124.207.162.216/0f35f5a8f099e78ef8376eb253886e49.m3u8?type=web.cloudplay&k=e6f2ff234eb61b5432c91056471ea5e1-2b3b-1464863186&cpn=27565&ppyunid=158425217";
				if (!StringUtils.isEmpty(ua)) {
					mVV.setUserAgent(ua);
				}

				mVV.setVideoPath(mVideoSource);

				/**
				 * 续播，如果需要如此
				 */
				if (mLastPos > 0) {

					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}

				/**
				 * 显示或者隐藏缓冲提示
				 */
				mVV.showCacheInfo(false);

				/**
				 * 开始播放
				 */
				mVV.start();

				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				mHandler.sendEmptyMessage(6);
				isCatch = true;
				mHandler.sendEmptyMessage(2);
				break;
			default:
				break;
			}
		}
	}

	private int screenWidth, screenHeight;
	View main;

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		main = View.inflate(getApplicationContext(), R.layout.activity_videotvplayer, null);
		main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		main.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {

			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				// TODO Auto-generated method stub
				if (visibility != View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
					if (isLock) {
						if (!isLockVisible) {
							isLockVisible = true;
							lockButton.setVisibility(View.VISIBLE);
						}
						return;
					}
					isViewcontrollerShow = true;
					showViewController();
				}
			}
		});
		setContentView(main);
		// setContentView(R.layout.activity_video2dplayer);
		// Video2DPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();

		maxColumn = SystemUtils.getMaxActivityColumn(this);

		mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
		tvList = (List<TV>) getIntent().getSerializableExtra("tv");
		currTV = getIntent().getIntExtra("currTV", 0);
		if (tvList != null && tvList.size() > currTV) {
			tv = tvList.get(currTV);
			titleString = tv.getTitle();
			tv.setSelect(true);
			initUI();
			mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
			mHandlerThread.start();
			mEventHandler = new EventHandler(mHandlerThread.getLooper());
			getData();
		} else {
			titleString = "";
			Toast.makeText(getApplicationContext(), "获取直播信息失败！", Toast.LENGTH_SHORT).show();
		}
	}

	private void getData() {
		isCatch = false;
		source_id = tv.getUrl();
		getUrl();
	}

	private DataAsyncTask getUrlTask;
	private String ua = "";

	private void getUrl() {
		isCatch = false;
		showLoading();
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(mVideoSource)) {
					if (geTVchannels != null) {
						tVchannels.clear();
						tVchannels.addAll(geTVchannels);
						tvShowListAdapter.notifyDataSetChanged();
						startTVShowTimer();
					}
					playMovie();
				} else {
					Toast.makeText(getApplicationContext(), "解析失败", Toast.LENGTH_SHORT).show();

				}
				hideLoading();
			}
		};
		getUrlTask = new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieUrlApi movieUrlApi = new MovieUrlApi();
				TVUrl realUrl = movieUrlApi.getTVUrl(source_id);
				if (realUrl != null) {
					mVideoSource = realUrl.getPlayurl();
					geTVchannels = realUrl.getPlayinfo().getList();
					ua = "";
				} else {
					mVideoSource = "";
				}

				return null;
			}
		};
		getUrlTask.execute("");
	}

	private void playMovie() {
		/**
		 * 开启后台事件处理线程
		 */

		mEventHandler.sendEmptyMessage(EVENT_PLAY);
	}

	/**
	 * 初始化界面
	 */
	private void initUI() {
		mViewHolder = (RelativeLayout) findViewById(R.id.videoview_holder);

		/**
		 * 设置ak及sk的前16位
		 */
		BVideoView.setAKSK(AK, SK);

		/**
		 * 创建BVideoView和BMediaController
		 */
		mVV = new BVideoView(this);
		mViewHolder.addView(mVV);

		/**
		 * 注册listener
		 */
		mVV.setOnPreparedListener(this);
		mVV.setOnCompletionListener(this);
		mVV.setOnErrorListener(this);
		mVV.setOnInfoListener(this);
		mVV.setOnPlayingBufferCacheListener(this);
		mVV.setOnNetworkSpeedListener(new OnNetworkSpeedListener() {

			@Override
			public void onNetworkSpeedUpdate(int arg0) {
				// TODO Auto-generated method stub
				netSpeed = arg0;
			}
		});

		// /**
		// * 设置解码模式
		// */
		// mVV.setDecodeMode(BVideoView.DECODE_MHW_AUTO);

		/**
		 * 设置解码模式
		 */
		mVV.setDecodeMode(mIsHwDecode ? BVideoView.DECODE_HW : BVideoView.DECODE_SW);

		mViewHolder.setOnTouchListener(this);

		initViewController();

	}

	private LinearLayout bottomLayout, topLayout, sourceLayout, episodeLayout, hdLayout, loadingLayout;

	private ImageButton leftplayButton, leftbackButton, lockButton;
	private TextView lefttitleTextView, brightTextView, columnTextView, scrollTextView, loadingTextView,
			episodeTextView;
	private ImageView leftLoadingImageView;
	private ListView episodeListView;
	private MyTVListAdapter playerEpisodeAdapter;

	private HorizontalListView horizontalListView;
	private MyTVShowListAdapter tvShowListAdapter;
	private List<TVchannel> tVchannels = new ArrayList<>();
	private List<TVchannel> geTVchannels;

	private boolean isPlaying = true;
	private boolean isViewcontrollerShow = false, isHdSelectShow = false;
	private boolean isSeeking = false, isLock = false;

	private void initViewController() {
		topLayout = (LinearLayout) findViewById(R.id.top_layout);
		bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
		loadingTextView = (TextView) findViewById(R.id.loading_textView);

		scrollTextView = (TextView) findViewById(R.id.scroll_textView);
		brightTextView = (TextView) findViewById(R.id.bright_textView);
		columnTextView = (TextView) findViewById(R.id.column_textView);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenH = dm.heightPixels;

		episodeLayout = (LinearLayout) findViewById(R.id.episode_layout);
		ViewGroup.LayoutParams params2 = episodeLayout.getLayoutParams();
		params2.height = screenH - topLayout.getLayoutParams().height - bottomLayout.getLayoutParams().height;
		episodeLayout.setLayoutParams(params2);
		episodeListView = (ListView) findViewById(R.id.episode_listView);
		episodeListView.setDividerHeight(0);
		episodeListView.setSelector(new ColorDrawable(android.R.color.transparent));
		playerEpisodeAdapter = new MyTVListAdapter(tvList);
		episodeListView.setAdapter(playerEpisodeAdapter);
		episodeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				tvList.get(currTV).setSelect(false);
				tvList.get(position).setSelect(true);

				titleString = tvList.get(position).getTitle();
				lefttitleTextView.setText(titleString);
				currTV = position;
				if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
					mLastPos = 0;
				}
				mVV.stopPlayback();
				playerEpisodeAdapter.notifyDataSetChanged();
				source_id = tvList.get(position).getUrl();
				getUrl();
			}
		});
		episodeTextView = (TextView) findViewById(R.id.episode_textView);
		episodeTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				if (isEpisodeShow) {
					hideEpisodeSelect();
				} else {
					showEpisodeSelect();
				}

			}
		});

		horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
		tvShowListAdapter = new MyTVShowListAdapter(tVchannels);
		horizontalListView.setAdapter(tvShowListAdapter);

		leftplayButton = (ImageButton) findViewById(R.id.play_button);
		leftplayButton.setVisibility(View.GONE);
		lockButton = (ImageButton) findViewById(R.id.lock_button);
		lockButton.setSelected(false);
		leftbackButton = (ImageButton) findViewById(R.id.back_button);
		lefttitleTextView = (TextView) findViewById(R.id.title_textView);

		leftLoadingImageView = (ImageView) findViewById(R.id.loading_imageView);

		topLayout.setVisibility(View.GONE);
		bottomLayout.setVisibility(View.GONE);

		leftbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		lefttitleTextView.setText(titleString);

	}

	private void setView() {
		leftplayButton.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				if (isPlaying) {
					mVV.pause();
					leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_start));
					isPlaying = false;
				} else {
					if (mPlayerStatus == PLAYER_STATUS.PLAYER_IDLE) {
						mEventHandler.sendEmptyMessage(EVENT_PLAY);
					} else {
						mVV.resume();
					}
					leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));
					isPlaying = true;
				}
			}
		});

		lockButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLock = !lockButton.isSelected();
				lockButton.setSelected(isLock);
				if (isLock) {
					hideViewController();
				}
			}
		});

	}

	private Timer tvShowTimer;

	private void startTVShowTimer() {
		if (tvShowTimer != null) {
			tvShowTimer.cancel();
		}
		tvShowTimer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				tvShowHandler.sendEmptyMessage(0);
			}
		};
//		tvShowTimer.schedule(task, 100, 5000);
	}

	private void stopTVShowTimer() {
		if (tvShowTimer != null) {
			tvShowTimer.cancel();
		}
	}

	Handler tvShowHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			setTVShow();
		};
	};

	@SuppressWarnings("deprecation")
	private void setTVShow() {
		if (tVchannels.size() > 0) {
			Date date = new Date();
			int minutes = date.getHours() * 60 + date.getMinutes();
			int currShow = 0;
			if (tVchannels.size() == 1) {
				tVchannels.get(0).setSelect(true);
			} else {
				for (int i = 0; i < tVchannels.size() - 1; i++) {
					tVchannels.get(i).setSelect(false);
					TVchannel tVchannel = tVchannels.get(i);
					String[] tvDate = tVchannel.getTime().split(":");
					int tvMinutes = Integer.parseInt(tvDate[0]) * 60 + Integer.parseInt(tvDate[1]);

					TVchannel tVNextchannel = tVchannels.get(i + 1);
					String[] tvNextDate = tVNextchannel.getTime().split(":");
					int tvNextMinutes = Integer.parseInt(tvNextDate[0]) * 60 + Integer.parseInt(tvNextDate[1]);

					if (minutes >= tvMinutes && minutes < tvNextMinutes) {
						tVchannels.get(i).setSelect(true);
						currShow = i;
						break;
					} else if (minutes >= tvNextMinutes) {
						tVchannels.get(i + 1).setSelect(true);
						currShow = i + 1;
					}
				}
				final int arg = currShow;
				
				new Handler().postDelayed(new Runnable() {

					public void run() {
						horizontalListView.setSelection(arg);
					}
				}, 200);
			}
			tvShowListAdapter.notifyDataSetChanged();
		}
	}

	private String source_id;

	private boolean isPause = false;

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause");
		leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_start));
		if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
			mLastPos = mVV.getCurrentPosition();
		}
		if (mVV != null) {
			mVV.stopPlayback();
		}

		isPause = true;

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume");
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		if (isPause) {
			isPause = false;
			isCatch = false;
			showLoading();
			if (mEventHandler == null) {
				if (mHandlerThread != null) {
					mHandlerThread.quit();
				}
				mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
				mHandlerThread.start();
				mEventHandler = new EventHandler(mHandlerThread.getLooper());
			}
			mEventHandler.sendEmptyMessage(EVENT_PLAY);
			leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));
			isPlaying = true;
		}
		// if (mVV != null && leftplayButton != null) {
		// if (isPlaying) {
		// mVV.pause();
		// leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_start));
		// isPlaying = false;
		// } else {
		// if (mPlayerStatus == PLAYER_STATUS.PLAYER_IDLE) {
		// mEventHandler.sendEmptyMessage(EVENT_PLAY);
		// } else {
		// mVV.resume();
		// }
		// leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));
		// isPlaying = true;
		// }
		// }

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (speedTimer != null) {
			speedTimer.cancel();
		}
		stopTVShowTimer();
		/**
		 * 结束后台事件处理线程
		 */
		if (mHandlerThread != null) {
			mHandlerThread.quit();
		}
		// HeadSetUtil.getInstance().close(getApplicationContext());
		Log.v(TAG, "onDestroy");
	}

	@Override
	public boolean onInfo(int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
		/**
		 * 开始缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			isCatch = true;
			mHandler.sendEmptyMessage(2);
			break;
		/**
		 * 结束缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_END:
			mHandler.sendEmptyMessage(3);
			break;
		default:
			mHandler.sendEmptyMessage(3);
			break;
		}
		return false;
	}

	/**
	 * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
	 */
	@Override
	public void onPlayingBufferCache(int percent) {
		isCatch = true;
		// TODO Auto-generated method stub
		// mHandler.sendEmptyMessage(2);
		// if (percent >= 100) {
		// mHandler.sendEmptyMessage(3);
		// }
	}

	/**
	 * 播放出错
	 */
	@Override
	public boolean onError(int what, int extra) {
		// TODO Auto-generated method stub
		Log.v(TAG, "onError" + "what=" + what + ",extra=" + extra);
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		return true;
	}

	/**
	 * 播放完成
	 */
	@Override
	public void onCompletion() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onCompletion");
		mHandler.sendEmptyMessage(5);
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;

	}

	/**
	 * 播放准备就绪
	 */
	@Override
	public void onPrepared() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onPrepared");
		setView();
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
		mHandler.sendEmptyMessage(1);
		mHandler.sendEmptyMessage(3);
		mHandler.sendEmptyMessage(7);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				if (!isSeeking && isPlaying) {
					mHandler.sendEmptyMessage(0);
				}

			}
		};
		// mHandler.sendEmptyMessage(0);
		timer.schedule(task, 100, 200);
	}

	public String toTime(int time) {
		int minute = time / 60;
		int hour = minute / 60;
		int second = time % 60;
		minute %= 60;
		if (hour > 0) {
			return String.format("%02d:%02d:%02d", hour, minute, second);
		} else {
			return String.format("%02d:%02d", minute, second);
		}
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
	// if (!isViewcontrollerShow) {
	// isViewcontrollerShow = true;
	// showViewController();
	// return false;
	// }
	// }
	// return super.onKeyDown(keyCode, event);
	// };

	private Handler mHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void dispatchMessage(Message msg) {
			if (msg.what == 0) {

			} else if (msg.what == 1) {
				if (isViewcontrollerShow) {
					isViewcontrollerShow = false;
					hideViewController();
				}

			} else if (msg.what == 2) {
				showLoading();
			} else if (msg.what == 3) {
				hideLoading();
			} else if (msg.what == 4) {
				leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_stop));
			} else if (msg.what == 5) {
				leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_start));
			} else if (msg.what == 6) {
				if (!isViewcontrollerShow) {
					isViewcontrollerShow = true;
					showViewController();
				}
			}

		};
	};

	Timer viewControllerTimer;

	private void showViewController() {
		tvShowHandler.sendEmptyMessage(0);
		leftplayButton.setVisibility(View.VISIBLE);
		isLockVisible = true;
		lockButton.setVisibility(View.VISIBLE);

		bottomLayout.setVisibility(View.VISIBLE);
		topLayout.setVisibility(View.VISIBLE);

		bottomLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 140, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		bottomLayout.startAnimation(bottomtranslateAnimation);

		topLayout.clearAnimation();
		TranslateAnimation toptranslateAnimation = new TranslateAnimation(0, 0, -140, 0);
		toptranslateAnimation.setDuration(300);
		toptranslateAnimation.setFillAfter(true);

		topLayout.startAnimation(toptranslateAnimation);
		startHideViewControllerTimer();

	}

	private void startHideViewControllerTimer() {
		if (isViewcontrollerShow) {
			if (viewControllerTimer != null) {
				viewControllerTimer.cancel();
			}
			viewControllerTimer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mHandler.sendEmptyMessage(1);
				}
			};

			viewControllerTimer.schedule(task, 8000);

		}
	}

	private void hideViewController() {
		if (viewControllerTimer != null) {
			viewControllerTimer.cancel();
		}

		if (isHdSelectShow) {
			isHdSelectShow = false;
			hideHdSelect();
		}

		if (isEpisodeShow) {
			isEpisodeShow = false;
			hideEpisodeSelect();
		}

		if (isSourceShow) {
			isSourceShow = false;
			hideSourceSelect();
		}
		isLockVisible = false;
		lockButton.setVisibility(View.GONE);
		leftplayButton.setVisibility(View.GONE);
		main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		bottomLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 0, 140);
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
				bottomLayout.clearAnimation();
				bottomLayout.setVisibility(View.GONE);
			}
		});

		bottomLayout.startAnimation(bottomtranslateAnimation);

		topLayout.clearAnimation();
		TranslateAnimation toptranslateAnimation = new TranslateAnimation(0, 0, 0, -140);
		toptranslateAnimation.setDuration(300);
		toptranslateAnimation.setFillAfter(true);
		toptranslateAnimation.setAnimationListener(new AnimationListener() {

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
				topLayout.clearAnimation();
				topLayout.setVisibility(View.GONE);
			}
		});
 
		topLayout.startAnimation(toptranslateAnimation);
	}


	private void hideHdSelect() {
		hdLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 550, 0, 0);
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
				hdLayout.clearAnimation();
				hdLayout.setVisibility(View.GONE);
			}
		});

		hdLayout.startAnimation(bottomtranslateAnimation);

	}

	private boolean isEpisodeShow = false;

	private void showEpisodeSelect() {
		if (isEpisodeShow) {
			return;
		} else {
			isEpisodeShow = true;
		}
		episodeLayout.setVisibility(View.VISIBLE);

		episodeLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(550, 0, 0, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		episodeLayout.startAnimation(bottomtranslateAnimation);

	}

	private void hideEpisodeSelect() {
		isEpisodeShow = false;
		episodeLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 550, 0, 0);
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
				episodeLayout.clearAnimation();
				episodeLayout.setVisibility(View.GONE);
			}
		});

		episodeLayout.startAnimation(bottomtranslateAnimation);

	}

	private boolean isSourceShow = false;


	private void hideSourceSelect() {
		isLockVisible = true;
		lockButton.setVisibility(View.VISIBLE);
		isSourceShow = false;
		sourceLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, -550, 0, 0);
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
				sourceLayout.clearAnimation();
				sourceLayout.setVisibility(View.GONE);
			}
		});

		sourceLayout.startAnimation(bottomtranslateAnimation);

	}

	private boolean isLoading = false;
	private boolean isCatch = false;

	// private long lastTotalRxBytes = 0;
	// private long lastTimeStamp = 0;
	//
	// private long getTotalRxBytes() {
	// return TrafficStats.getUidRxBytes(getApplicationInfo().uid) ==
	// TrafficStats.UNSUPPORTED ? 0
	// : (TrafficStats.getTotalRxBytes() / 1024);// 转为KB
	// }

	Timer speedTimer = new Timer();
	private int netSpeed = 0;

	private void showNetSpeed() {

		// long nowTotalRxBytes = getTotalRxBytes();
		// long nowTimeStamp = System.currentTimeMillis();
		// long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 /
		// (nowTimeStamp - lastTimeStamp));// 毫秒转换
		// lastTimeStamp = nowTimeStamp;
		// lastTotalRxBytes = nowTotalRxBytes;

		Message msg = speedHandler.obtainMessage();
		if (netSpeed > 1024) {
			netSpeed = netSpeed / 1024;
			msg.what = 100;
			msg.obj = String.valueOf(netSpeed) + " kb/s";
		}else{
			msg.what = 100;
			msg.obj = String.valueOf(netSpeed) + " b/s";
		}
		

		speedHandler.sendMessage(msg);// 更新界面
	}

	private Handler speedHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			if (loadingTextView != null) {
//				loadingTextView.setText((CharSequence) msg.obj);
				loadingTextView.setText("拼命加载中...");
			}
		};
	};

	private void showLoading() {
		if (!isLoading) {
			isLoading = true;
			loadingLayout.setVisibility(View.VISIBLE);
			Animation operatingAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
			LinearInterpolator lin = new LinearInterpolator();
			operatingAnim.setInterpolator(lin);
			leftLoadingImageView.startAnimation(operatingAnim);
			if (isCatch) {
//				loadingTextView.setText("0kb/s");
				loadingTextView.setText("拼命加载中...");
				if (speedTimer != null) {
					speedTimer.cancel();
				}
				speedTimer = new Timer();
				TimerTask speedTask = new TimerTask() {
					@Override
					public void run() {
						showNetSpeed();
					}
				};
				speedTimer.schedule(speedTask, 100, 1000);
			} else {
				loadingTextView.setText("拼命加载中...");
				if (speedTimer != null) {
					speedTimer.cancel();
				}
			}

		}
	}

	private void hideLoading() {
		if (isLoading) {
			isLoading = false;
			leftLoadingImageView.clearAnimation();
			loadingLayout.setVisibility(View.GONE);
			if (speedTimer != null) {
				speedTimer.cancel();
			}
		}
	}

	private int isMove = 0;
	private float startX, startY;
	private float currColumn, currBright, tempBright = 1;
	private boolean isLockVisible = false;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (isLock) {
				return true;
			}
			if (viewControllerTimer != null) {
				viewControllerTimer.cancel();
			}
			startX = event.getX();
			startY = event.getY();
			currColumn = SystemUtils.getCurrentActivityColumn(VideoTVPlayerActivity.this);

			currBright = tempBright;
			isMove = 0;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (isLock) {
				return true;
			}
			float distanceX = event.getX() - startX;
			float distanceY = event.getY() - startY;

			if (startX > screenWidth * 7 / 8) {
				if (!isHdSelectShow) {
					if (distanceY > 10 || distanceY < -10) {
						showColumn();
						isMove = 3;

						float cb = currColumn + -distanceY * 28 / screenHeight;
						if (cb > maxColumn) {
							cb = maxColumn;
						}
						setColumn((int) cb);
						int percent = (int) (cb * 100 / maxColumn);

						if (percent > 100) {
							percent = 100;
						} else if (percent < 0) {
							percent = 0;
						}

						columnTextView.setText(percent + "%");
					}
				} else {
					isMove = 3;
				}

			} else if (startX < screenWidth / 8) {
				if (distanceY > 10 || distanceY < -10) {
					showBright();
					isMove = 3;
					float cb = currBright - distanceY * 4 / screenHeight;
					if (cb > 1) {
						cb = 1;
					} else if (cb < 0) {
						cb = 0;
					}
					tempBright = cb;
					setBright(cb);
					brightTextView.setText((int) (cb * 100) + "%");
				}

			} else {
				if (distanceX > 10 || distanceX < -10) {
					isMove = 2;

				} else {
					isMove = 2;
				}
			}

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (isLock) {
				if (isLockVisible) {
					isLockVisible = false;
					lockButton.setVisibility(View.GONE);
				} else {
					isLockVisible = true;
					lockButton.setVisibility(View.VISIBLE);
				}

				return true;
			}
			hideBright();
			hideColumn();
			scrollTextView.setVisibility(View.GONE);
			startHideViewControllerTimer();
			if (isMove == 1) {
				isMove = 0;
				scrollTextView.setVisibility(View.GONE);

			} else if (isMove == 2 || isMove == 0) {
				if (!isViewcontrollerShow) {
					isViewcontrollerShow = true;
					showViewController();
				} else {
					isViewcontrollerShow = false;
					hideViewController();

				}
			}
		}
		return true;
	}

	private void setBright(float num) {
		SystemUtils.setCurrentActivityBrightness(VideoTVPlayerActivity.this, num);
	}

	private boolean isBrightShow = false;

	private void showBright() {
		if (!isBrightShow) {
			isBrightShow = true;
			brightTextView.setVisibility(View.VISIBLE);
		}
	}

	private void hideBright() {
		if (isBrightShow) {
			isBrightShow = false;
			brightTextView.setVisibility(View.GONE);
		}
	}

	private void setColumn(int num) {

		SystemUtils.setCurrentActivityColumn(VideoTVPlayerActivity.this, num, 0);
	}

	private boolean isColumnShow = false;

	private void showColumn() {
		if (!isColumnShow) {
			isColumnShow = true;
			columnTextView.setVisibility(View.VISIBLE);
		}
	}

	private void hideColumn() {
		if (isColumnShow) {
			isColumnShow = false;
			columnTextView.setVisibility(View.GONE);
		}
	}

	public void showWifiDialog(final Activity activity) {

		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		// 设置它的ContentView
		View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.isgoon_dialog, null);
		TextView tv = (TextView) view.findViewById(R.id.dialog_message);
		TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
		TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
		dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				getData();
			}
		});

		dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getData();
				dialog.cancel();
			}
		});
		tv.setText("存在历史播放记录，是否继续播放？");
		view.setMinimumWidth(600);
		dialog.setContentView(view);
		dialog.show();

	}

	public class MyTVListAdapter extends BaseAdapter {
		private List<TV> list;

		public MyTVListAdapter(List<TV> list) {
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(getApplicationContext(), R.layout.listitem_tvtab, null);
				viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.textView.setText(list.get(position).getTitle());

			viewHolder.textView.setSelected(list.get(position).isSelect());

			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}

	} 

	public class MyTVShowListAdapter extends BaseAdapter {
		private List<TVchannel> list;

		public MyTVShowListAdapter(List<TVchannel> list) {
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub 
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(getApplicationContext(), R.layout.listitem_tvshow, null);
				viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
				viewHolder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.textView.setText(list.get(position).getTime());
			viewHolder.textView.setSelected(list.get(position).isSelect());
			viewHolder.textView1.setText(list.get(position).getTitle());
			viewHolder.textView1.setSelected(list.get(position).isSelect());

			return convertView;
		}

		class ViewHolder {
			TextView textView; 
			TextView textView1;
		}

	}
}
