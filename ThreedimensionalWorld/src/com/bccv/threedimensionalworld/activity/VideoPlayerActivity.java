package com.bccv.threedimensionalworld.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.baidu.cyberplayer.subtitle.SubtitleManager;
import com.baidu.cyberplayer.subtitle.utils.DownFinishCallback;
import com.baidu.cyberplayer.subtitle.utils.SubtitleError;
import com.baidu.cyberplayer.subtitle.utils.SubtitleErrorCallback;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.api.MovieApi;
import com.bccv.threedimensionalworld.model.MovieUrl;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.SrtManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

@SuppressLint("NewApi")
public class VideoPlayerActivity extends BaseActivity implements
		OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnPlayingBufferCacheListener {
	private final String TAG = "VideoViewPlayingActivity";

	/**
	 * 您的ak
	 */
	private String AK = GlobalParams.AK;
	/**
	 * //您的sk的前16位
	 */
	private String SK = GlobalParams.SK;

	private String mVideoSource = "";
	private String titleString = "";
	private String movie_id;
	private MovieUrl movieUrl;
	private int movieType = 1;
	private int changePosition = 0;
	private boolean isChanging = false;
	private boolean is2D = false;

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
				// mVideoSource =
				// "http://3dmovie.bj.bcebos.com/biaoqing/Captain.America.The.Winter.Soldier/index.m3u8";
				// mVideoSource =
				// "http://pl.youku.com/playlist/m3u8?vid=332503657&amp;type=flv&amp;ts=1441705462&amp;keyframe=0&amp;ep=ciaQG06NU8sA4iDWij8bYHizIXFaXP0J9BqFgtRnB9QmQOm%2F&amp;sid=0441705507392127dae6e&amp;token=2810&amp;ctype=12&amp;ev=1&amp;oip=3678980627";
				mHandler.sendEmptyMessage(4);

				/**
				 * 设置播放url
				 */
				mVV.setVideoPath(mVideoSource);

				/**
				 * 续播，如果需要如此
				 */
				if (mLastPos > 0) {

					mVV.seekTo(mLastPos);
					mLastPos = 0;
				}
				if (isChanging && changePosition > 0) {
					mVV.seekTo(changePosition);
					changePosition = 0;
					isChanging = false;
				}
				/**
				 * 显示或者隐藏缓冲提示
				 */
				mVV.showCacheInfo(false);
				if (mSubtitleManager != null) {
					mSubtitleManager.setIsShowSubtitle(false);
				}
				
				/**
				 * 开始播放
				 */
				mVV.start();

				mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoplayer);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

		mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
		titleString = getIntent().getStringExtra("title");
		movie_id = getIntent().getStringExtra("movie_id");
		is2D = getIntent().getBooleanExtra("is2D", false);
		initUI();
		// String url = getIntent().getExtras().getString("url");
		String url = "http://3d.cgame.tv/data/m3u8/b5e5e633e7530d3d2a377f0df7b70643_cache.m3u8";
		mVideoSource = url;

		// mHandlerThread = new HandlerThread("event handler thread",
		// Process.THREAD_PRIORITY_BACKGROUND);
		// mHandlerThread.start();
		// mEventHandler = new EventHandler(mHandlerThread.getLooper());
		// mEventHandler.sendEmptyMessage(EVENT_PLAY);

		getData();

	}

	private void getData() {
		showLoading();
		startAnalysis();
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (movieUrl != null) {
					if (is2D) {
						getUrl(movieUrl.getUrl_1());
					} else {
						initSubtitleSetting();
						movieType = 3;
						mVideoSource = movieUrl.getUrl_3();
						/**
						 * 开启后台事件处理线程
						 */
						mHandlerThread = new HandlerThread(
								"event handler thread",
								Process.THREAD_PRIORITY_BACKGROUND);
						mHandlerThread.start();
						mEventHandler = new EventHandler(
								mHandlerThread.getLooper());
						mEventHandler.sendEmptyMessage(EVENT_PLAY);
					}
				}
				// mVideoSource = Environment.getExternalStorageDirectory()
				// .getAbsolutePath() + "/movie/avatar_2015828134352.mkv";
				// /**
				// * 开启后台事件处理线程
				// */
				// mHandlerThread = new HandlerThread("event handler thread",
				// Process.THREAD_PRIORITY_BACKGROUND);
				// mHandlerThread.start();
				// mEventHandler = new EventHandler(mHandlerThread.getLooper());
				// mEventHandler.sendEmptyMessage(EVENT_PLAY);
			}
		};
		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieApi movieApi = new MovieApi();
				movieUrl = movieApi.getMovieUrl(movie_id, is2D);
//				movieUrl = movieApi.getFengXingMovieUrl("633626", true);
				return null;
			}
		}.execute("");
	}
	private Timer analysisTimer;
	private int analysisNum = 0;
	private void startAnalysis(){
		if (analysisTimer != null) {
			analysisTimer.cancel();
		}
		
		analysisTimer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (analysisNum < 3) {
					analysisHandler.sendEmptyMessage(analysisNum);
					analysisNum++;
				}else {
					analysisTimer.cancel();
				}
			}
		};
		analysisTimer.schedule(task, 100, 3000);
	}

	private Handler analysisHandler = new Handler(){
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 0:
				leftanalysisTextView.setText("正在为您选取最优线路");
				rightanalysisTextView.setText("正在为您选取最优线路");
				break;
			case 1:
				leftanalysisTextView.setText("正在为您选取最优线路\n智能解析视频源地址完成");
				rightanalysisTextView.setText("正在为您选取最优线路\n智能解析视频源地址完成");
				break;
			case 2:
				leftanalysisTextView.setText("正在为您选取最优线路\n智能解析视频源地址完成\n视频加载中,请稍后");
				rightanalysisTextView.setText("正在为您选取最优线路\n智能解析视频源地址完成\n视频加载中,请稍后");
				break;
			case 3:
				leftanalysisTextView.setText("");
				rightanalysisTextView.setText("");
				break;
			default:
				leftanalysisTextView.setText("正在为您选取最优线路\n智能解析视频源地址完成\n视频加载中,请稍后");
				rightanalysisTextView.setText("正在为您选取最优线路\n智能解析视频源地址完成\n视频加载中,请稍后");
				break;
			}
		};
	};
	
	@SuppressLint("JavascriptInterface")
	private void getUrl(String url) {
		WebView mWebView;
		mWebView = new WebView(this);
		initSettings(mWebView);
		mWebView.getSettings()
				.setUserAgentString(
						"Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D5145e Safari/9537.53");

		mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "js_method");// 添加java
																					// script接口
		mWebView.loadUrl(url);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initSettings(WebView mWebView) {
		WebSettings webSettings = mWebView.getSettings();
		// 开启java script的支持
		webSettings.setJavaScriptEnabled(true);

		// 启用localStorage 和 essionStorage
		webSettings.setDomStorageEnabled(true);

		// 开启应用程序缓存
		webSettings.setAppCacheEnabled(true);
		String appCacheDir = this.getApplicationContext()
				.getDir("cache", Context.MODE_PRIVATE).getPath();
		webSettings.setAppCachePath(appCacheDir);
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是10M
		webSettings.setAllowFileAccess(true);
		webSettings.setBlockNetworkImage(true);

		mWebView.setWebViewClient(mWebViewClient);
	}

	private WebViewClient mWebViewClient = new WebViewClient() {

		@Override
		public void onPageFinished(WebView view, String url) {
			view.loadUrl("javascript:window.js_method.showSource(document.getElementsByTagName('video')[0].src);"); // iqiyi
			// view.loadUrl("javascript:window.js_method.showSource('<head>'+" +
			// "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onPageFinished(view, url);
		}
	};
	public String realPlayUrl;
	private static boolean GETHTML5_COMPLETE = false;

	class InJavaScriptLocalObj {

		@JavascriptInterface
		// android4.2兼容问题
		public void showSource(String html5url) {
			if (html5url != null && !GETHTML5_COMPLETE) {
				GETHTML5_COMPLETE = true;
				realPlayUrl = html5url;
				mmEventHandler.sendEmptyMessage(0);
			}
			Log.i("conowen", "html5url=" + html5url);
			Log.e("asdddd", SystemClock.currentThreadTimeMillis() + "");
		}
	}

	private Handler mmEventHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mVideoSource = realPlayUrl;
			/**
			 * 开启后台事件处理线程
			 */
			mHandlerThread = new HandlerThread("event handler thread",
					Process.THREAD_PRIORITY_BACKGROUND);
			mHandlerThread.start();
			mEventHandler = new EventHandler(mHandlerThread.getLooper());

			mEventHandler.sendEmptyMessage(EVENT_PLAY);
		}
	};

	private void restartMovie() {
		isChanging = true;
		changePosition = mVV.getCurrentPosition();
		mVV.stopPlayback();
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

		/**
		 * 设置解码模式
		 */
		mVV.setDecodeMode(BVideoView.DECODE_MHW_AUTO);

		mViewHolder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (timer != null) {
					timer.cancel();
				}
				timer = new Timer();
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mHandler.sendEmptyMessage(1);
					}
				};
				timer.schedule(task, 5000);

				if (!isViewcontrollerShow) {
					isViewcontrollerShow = true;
					showViewController();
				}

			}
		});

		initViewController();

	}

	private SubtitleManager mSubtitleManager;

	private SrtManager srtManager = new SrtManager();

	private void initSubtitleSetting() {
		mSubtitleManager = mVV
				.getSubtitlePlayManger(new SubtitleErrorCallback() {

					@Override
					public void onSubtitleError(SubtitleError error) {
						Log.w("test_subtitle", "code: " + error.errorCode
								+ " msg: " + error.errorMsg);
					}
				});
		mSubtitleManager.setIsShowSubtitle(false);
		final String downloadPathString = Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/subtitle/Avatar.srt";
//		mSubtitleManager.startSubtitle(movieUrl.getLetter(),
//				downloadPathString, 0, new DownFinishCallback() {
//
//					@Override
//					public void onDownFinished(boolean arg0, String arg1) {
//						// TODO Auto-generated method stub
//						srtManager.loadSrtFile(downloadPathString);
//						mSubtitleManager.setIsShowSubtitle(false);
//					}
//				});

		mSubtitleManager.setIsShowSubtitle(false);
		// mSubtitleManager.startSubtitle(downloadPathString);
		//
		// mSubtitleManager.getSubtitleSettings().setDisplayFontSize(20);
		// mSubtitleManager.getSubtitleSettings().setDisplayColor(getResources().getColor(R.color.white));
		// mSubtitleManager.getSubtitleSettings().setDisplayLocation(0, 10);
	}

	private LinearLayout bottomLayout, topLayout, rightbottomLayout, righttopLayout;
	
	private Button leftplayButton, rightplayButton, leftbackButton,
			rightbackButton, leftstateButton, rightstateButton;
	private SeekBar leftBar, rightBar;
	private TextView leftcurrTextView, rightcurrTextView, leftdurationTextView,
			rightdurationTextView, lefttitleTextView, righttitleTextView,
			leftsubtitleTextView, rightsubtitleTextView, leftanalysisTextView, rightanalysisTextView;
	private ImageView leftLoadingImageView, rightLoadingImageView;

	private boolean isPlaying = true;
	private boolean isViewcontrollerShow = false;
	private boolean isSeeking = false;

	private void initViewController() {
		topLayout = (LinearLayout) findViewById(R.id.top_layout);
		bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		righttopLayout = (LinearLayout) findViewById(R.id.right_top_layout);
		rightbottomLayout = (LinearLayout) findViewById(R.id.right_bottom_layout);
		leftplayButton = (Button) findViewById(R.id.left_play_button);
		rightplayButton = (Button) findViewById(R.id.right_play_button);
		leftbackButton = (Button) findViewById(R.id.left_back_button);
		rightbackButton = (Button) findViewById(R.id.right_back_button);
		leftBar = (SeekBar) findViewById(R.id.left_seekBar);
		rightBar = (SeekBar) findViewById(R.id.right_seekBar);
		leftcurrTextView = (TextView) findViewById(R.id.left_curr_textView);
		rightcurrTextView = (TextView) findViewById(R.id.right_curr_textView);
		leftdurationTextView = (TextView) findViewById(R.id.left_duration_textView);
		rightdurationTextView = (TextView) findViewById(R.id.right_duration_textView);
		lefttitleTextView = (TextView) findViewById(R.id.left_title_textView);
		righttitleTextView = (TextView) findViewById(R.id.right_title_textView);

		leftsubtitleTextView = (TextView) findViewById(R.id.left_subtitle_textView);
		rightsubtitleTextView = (TextView) findViewById(R.id.right_subtitle_textView);

		leftsubtitleTextView.setScaleX(0.5f);
		rightsubtitleTextView.setScaleX(0.5f);
//		rightsubtitleTextView.setVisibility(View.INVISIBLE);

		leftanalysisTextView = (TextView) findViewById(R.id.left_analysis_textView);
		rightanalysisTextView = (TextView) findViewById(R.id.right_analysis_textView);
		leftanalysisTextView.setScaleX(0.5f);
		rightanalysisTextView.setScaleX(0.5f);
		
		leftLoadingImageView = (ImageView) findViewById(R.id.left_loading_imageView);
		rightLoadingImageView = (ImageView) findViewById(R.id.right_loading_imageView);

		leftstateButton = (Button) findViewById(R.id.left_state_button);
		rightstateButton = (Button) findViewById(R.id.right_state_button);
		
		if (is2D) {
//			righttopLayout.setVisibility(View.GONE);
//			rightbottomLayout.setVisibility(View.GONE);
		}
		
		leftstateButton.setScaleX(0.5f);
		rightstateButton.setScaleX(0.5f);
		if (is2D) {
			leftstateButton.setVisibility(View.GONE);
			rightstateButton.setVisibility(View.GONE);
		}
		// leftstateButton.setVisibility(View.GONE);
		// rightstateButton.setVisibility(View.GONE);
		leftstateButton.setText("高清");
		rightstateButton.setText("高清");
		leftstateButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightstateButton.setSelected(hasFocus);
			}
		});
		leftstateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (movieType == 1) {
					movieType = 2;
					leftstateButton.setText("标清");
					rightstateButton.setText("标清");
					mVideoSource = movieUrl.getUrl_2();
				} else if (movieType == 2) {
					movieType = 3;
					leftstateButton.setText("高清");
					rightstateButton.setText("高清");
					mVideoSource = movieUrl.getUrl_3();
				} else if (movieType == 3) {
					movieType = 1;
					leftstateButton.setText("流畅");
					rightstateButton.setText("流畅");
					mVideoSource = movieUrl.getUrl_1();
				}
//				else if (movieType == 4) {
//					movieType = 1;
//					leftstateButton.setText("流畅");
//					rightstateButton.setText("流畅");
//					mVideoSource = movieUrl.getUrl_1();
//				}

				restartMovie();
			}
		});

		topLayout.setVisibility(View.GONE);
		bottomLayout.setVisibility(View.GONE);

		leftbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		leftbackButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightbackButton.setSelected(hasFocus);
			}
		});

		lefttitleTextView.setScaleX(0.5f);
		righttitleTextView.setScaleX(0.5f);
		lefttitleTextView.setText(titleString);
		righttitleTextView.setText(titleString);

		leftcurrTextView.setScaleX(0.5f);
		rightcurrTextView.setScaleX(0.5f);
		leftdurationTextView.setScaleX(0.5f);
		rightdurationTextView.setScaleX(0.5f);

		leftplayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isPlaying) {
					mVV.pause();
					leftplayButton
							.setBackgroundResource(R.drawable.videoplayer_play);
					rightplayButton
							.setBackgroundResource(R.drawable.videoplayer_play);
					isPlaying = false;
				} else {
					if (mPlayerStatus == PLAYER_STATUS.PLAYER_IDLE) {
						mEventHandler.sendEmptyMessage(EVENT_PLAY);
					} else {
						mVV.resume();
					}
					leftplayButton
							.setBackgroundResource(R.drawable.videoplayer_pause);
					rightplayButton
							.setBackgroundResource(R.drawable.videoplayer_pause);
					isPlaying = true;
				}
			}
		});
		leftplayButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				rightplayButton.setSelected(hasFocus);
			}
		});

		leftBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				isSeeking = false;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				isSeeking = true;
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (isSeeking) {
					mVV.seekTo(progress);
					String timeString = toTime(progress);
					leftcurrTextView.setText(timeString);
					rightcurrTextView.setText(timeString);
				}

			}
		});

		leftBar.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				rightBar.onTouchEvent(event);
				return false;
			}
		});

		leftBar.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					isSeeking = true;
					rightBar.onKeyDown(keyCode, event);
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					isSeeking = false;
					rightBar.onKeyUp(keyCode, event);
				}

				return false;
			}
		});

		rightBar.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				leftBar.onKeyDown(keyCode, event);
				return false;
			}
		});

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause");
		leftplayButton.setBackgroundResource(R.drawable.videoplayer_play);
		rightplayButton.setBackgroundResource(R.drawable.videoplayer_play);
		mVV.stopPlayback();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.v(TAG, "onResume");
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**
		 * 结束后台事件处理线程
		 */
		mHandlerThread.quit();
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
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(2);
		if (percent >= 100) {
			mHandler.sendEmptyMessage(3);
		}
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
		if (isChanging) {
			mEventHandler.sendEmptyMessage(EVENT_PLAY);
		}
	}

	/**
	 * 播放准备就绪
	 */
	@Override
	public void onPrepared() {
		// TODO Auto-generated method stub
		Log.v(TAG, "onPrepared");
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
		if (analysisTimer != null) {
			analysisTimer.cancel();
		}
		analysisHandler.sendEmptyMessage(3);
		mHandler.sendEmptyMessage(3);
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

	private Timer timer = null;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(1);
			}
		};
		timer.schedule(task, 5000);
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (!isViewcontrollerShow) {
				isViewcontrollerShow = true;
				showViewController();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	};

	private Handler mHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			if (msg.what == 0) {
				// if (isChanging) {
				// isChanging = false;
				// mVV.pause();
				// mVV.resume();
				// }
				if (!isSeeking) {
					int curr = mVV.getCurrentPosition();
					int duration = mVV.getDuration();

					String timeString = toTime(curr);
					String durationString = toTime(duration);
					if (isViewcontrollerShow) {
						leftBar.setMax(duration);
						rightBar.setMax(duration);
						leftBar.setProgress(curr);
						rightBar.setProgress(curr);
						leftdurationTextView.setText(durationString);
						rightdurationTextView.setText(durationString);
						leftcurrTextView.setText(timeString);
						rightcurrTextView.setText(timeString);
					}

					if (srtManager.hasSrt() && !isLoading && isPlaying) {
						String subtitle = srtManager.getCurrStrText(curr);
						leftsubtitleTextView.setText(subtitle);
						rightsubtitleTextView.setText(subtitle);
					}
				}
				// if (isPlaying && !isChanging) {
				// mHandler.sendEmptyMessageDelayed(0, 200);
				// }

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
				leftplayButton
						.setBackgroundResource(R.drawable.videoplayer_pause);
				rightplayButton
						.setBackgroundResource(R.drawable.videoplayer_pause);
			} else if (msg.what == 5) {
				leftplayButton
						.setBackgroundResource(R.drawable.videoplayer_play);
				rightplayButton
						.setBackgroundResource(R.drawable.videoplayer_play);
			}

		};
	};

	private void showViewController() {

		bottomLayout.setVisibility(View.VISIBLE);
		topLayout.setVisibility(View.VISIBLE);

		bottomLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0,
				0, 140, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		bottomLayout.startAnimation(bottomtranslateAnimation);

		topLayout.clearAnimation();
		TranslateAnimation toptranslateAnimation = new TranslateAnimation(0, 0,
				-140, 0);
		toptranslateAnimation.setDuration(300);
		toptranslateAnimation.setFillAfter(true);

		topLayout.startAnimation(toptranslateAnimation);
	}

	private void hideViewController() {
		bottomLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0,
				0, 0, 140);
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
		TranslateAnimation toptranslateAnimation = new TranslateAnimation(0, 0,
				0, -140);
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

	private boolean isLoading = false;

	private void showLoading() {
		if (!isLoading) {
			isLoading = true;
			AnimationDrawable leftanimationDrawable = (AnimationDrawable) getResources()
					.getDrawable(R.drawable.videoplayer_loading);
			leftLoadingImageView.setBackground(leftanimationDrawable);
			leftLoadingImageView.setVisibility(View.VISIBLE);
			leftanimationDrawable.start();

			AnimationDrawable rightanimationDrawable = (AnimationDrawable) getResources()
					.getDrawable(R.drawable.videoplayer_loading);
			rightLoadingImageView.setBackground(rightanimationDrawable);
			rightLoadingImageView.setVisibility(View.VISIBLE);
			rightanimationDrawable.start();

		}
	}

	private void hideLoading() {
		if (isLoading) {
			isLoading = false;
			leftLoadingImageView.setVisibility(View.GONE);

			rightLoadingImageView.setVisibility(View.GONE);

		}
	}
}
