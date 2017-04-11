package com.bccv.zhuiyingzhihanju.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.Text;

import com.alibaba.fastjson.JSONObject;
import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnNetworkSpeedListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.DownloadEpisodeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.InfoCommentAdapter;
import com.bccv.zhuiyingzhihanju.adapter.InfoEpisodeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.InfoEpisodeNumAdapter;
import com.bccv.zhuiyingzhihanju.adapter.MovieLikeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.PlayerEpisodeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.PlayerHDAdapter;
import com.bccv.zhuiyingzhihanju.adapter.PlayerSourceAdapter;
import com.bccv.zhuiyingzhihanju.api.CommentApi;
import com.bccv.zhuiyingzhihanju.api.MovieInfoApi;
import com.bccv.zhuiyingzhihanju.api.MovieUrlApi;
import com.bccv.zhuiyingzhihanju.R.anim;
import com.bccv.zhuiyingzhihanju.R.drawable;
import com.bccv.zhuiyingzhihanju.R.id;
import com.bccv.zhuiyingzhihanju.R.layout;
import com.bccv.zhuiyingzhihanju.R.style;
import com.bccv.zhuiyingzhihanju.model.Comment;
import com.bccv.zhuiyingzhihanju.model.HD;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;
import com.bccv.zhuiyingzhihanju.model.MovieEpisodeNum;
import com.bccv.zhuiyingzhihanju.model.MovieInfo;
import com.bccv.zhuiyingzhihanju.model.MovieSource;
import com.bccv.zhuiyingzhihanju.model.MovieUrl;
import com.bccv.zhuiyingzhihanju.model.RealUrl;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.utils.headset.HeadSetUtil;
import com.utils.net.TcpServerHelper;
import com.utils.net.WebViewInterJS;
import com.utils.share.ShareSDK;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.M3U8Utils;
import com.utils.tools.PromptManager;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;
import com.utils.tools.SystemUtils;
import com.utils.views.HorizontalListView;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.TrafficStats;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "HandlerLeak", "Wakelock", "DefaultLocale", "ClickableViewAccessibility", "InflateParams" })
public class Video2DPlayerAndroidActivity extends BaseActivity implements OnPreparedListener, OnCompletionListener,
		OnErrorListener, OnInfoListener, OnPlayingBufferCacheListener, OnTouchListener {
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
	private String movie_id, type_id, episodes_id, image_url, movie_title;
	private Movie movie;
	private int hd = 2, currSourceNum = 0, maxColumn;
	private boolean isPortrait = true, isComment = true;

	InputMethodManager imm;

	private BVideoView mVV = null;
	private LinearLayout mViewHolder = null;

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
	private boolean isEpisode = false;
	private boolean isEnd = true;
	View main;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		main = View.inflate(getApplicationContext(), R.layout.activity_video2dandroidplayer, null);
		// main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		// main.setOnSystemUiVisibilityChangeListener(new
		// OnSystemUiVisibilityChangeListener() {
		//
		// @Override
		// public void onSystemUiVisibilityChange(int visibility) {
		// // TODO Auto-generated method stub
		// if (visibility != View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
		// if (isLock && !isPortrait) {
		// if (!isLockVisible) {
		// isLockVisible = true;
		// lockButton.setVisibility(View.VISIBLE);
		// }
		// return;
		// }
		// isViewcontrollerShow = true;
		// showViewController();
		// }
		// }
		// });
		setContentView(main);
		// PowerManager pm = (PowerManager)
		// getSystemService(Context.POWER_SERVICE);
		// mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
		// PowerManager.ON_AFTER_RELEASE, POWER_LOCK);

		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		screenWidth = wm.getDefaultDisplay().getWidth();
		screenHeight = wm.getDefaultDisplay().getHeight();

		maxColumn = SystemUtils.getMaxActivityColumn(this);

		mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
		movie_id = getIntent().getStringExtra("movie_id");
		type_id = getIntent().getStringExtra("type_id");
		episodes_id = getIntent().getStringExtra("episodes_id");

		isEpisode = getIntent().getBooleanExtra("isEpisode", false);
		// movie_id = "1";
		// type_id = "8";
		// movie = (Movie) getIntent().getSerializableExtra("movie");
		//
		// titleString = movie.getTitle();
		// movie_id = movie.getId();
		// type_id = movie.getType_id();
		// episodes_id = movie.getEpisode_id();

		// getMovieInfoData();

		if (!StringUtils.isEmpty(episodes_id)) {
			isEpisode = true;
			titleString += "第" + episodes_id + "集";
			isEnd = false;
		}
		initUI();
		// getSource();
		// getEpisode();

		mHandlerThread = new HandlerThread("event handler thread", Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());
		// historyList =
		// SerializationUtil.readHistoryCache(getApplicationContext());
		// if (historyList != null && historyList.size() > 0 &&
		// historyList.get(0).getPlay_Time() > 0) {
		// showWifiDialog(this);
		// } else {
		getData();
		// }

	}

	private int currEpisodeNum = 0;
	private List<MovieEpisode> getEpisodeList = new ArrayList<>();
	private List<MovieSource> movieSourceList = new ArrayList<>();

	private void getSource() {
		if (getSourceList == null) {
			return;
		}

		for (int i = 0; i < movieSourceList.size(); i++) {
			MovieSource movieSource = movieSourceList.get(i);
			if (movieSource.getEpisodes_id().equals(episodes_id)) {
				List<MovieUrl> sourceTextList = movieSource.getSource_text();
				for (int j = 0; j < sourceTextList.size(); j++) {
					if (sourceTextList.get(j).getSource_id().equals(getSourceList.get(currSourceNum).getSource_id())) {
						currSourceNum = j;
						sourceTextList.get(j).setSelect(true);
					} else {
						sourceTextList.get(j).setSelect(false);
					}
				}
				getSourceList = sourceTextList;
				break;
			}
		}

		if (getSourceList != null && getSourceList.size() > 0) {
			sourceList.clear();
			sourceList.addAll(getSourceList);
			currSourceTextView.setText(sourceList.get(currSourceNum).getSource_name());
			playerSourceAdapter.notifyDataSetChanged();
		}
	}

	private void getEpisode() {
		getEpisodeList.clear();
		for (int i = 0; i < movieSourceList.size(); i++) {
			MovieSource movieSource = movieSourceList.get(i);
			if (movieSource.getSource_text().contains(sourceList.get(currSourceNum))) {
				MovieEpisode movieEpisode = new MovieEpisode();
				movieEpisode.setId(movieSourceList.get(i).getEpisodes_id());
				movieEpisode.setDes(movieSourceList.get(i).getDes());
				movieEpisode.setPeriods(movieSourceList.get(i).getPeriods());
				
				if (!StringUtils.isEmpty(movieSourceList.get(i).getPeriods())) {
					gridViewNum = 2;
				}else if (!StringUtils.isEmpty(movieSourceList.get(i).getDes())) {
					gridViewNum = 3;
				}else{
					gridViewNum = 6;
				}
				
				if (movieSourceList.get(i).getEpisodes_id().equals(episodes_id)) {
					movieEpisode.setSelect(true);
					currEpisodeNum = i;
				} else {
					movieEpisode.setSelect(false);
				}
				getEpisodeList.add(movieEpisode);
			}
		}
		if (currEpisodeNum == getEpisodeList.size() - 1) {
			isEnd = true;
		}

		if (!isEnd) {
			nextButton.setVisibility(View.VISIBLE);
		}

		episodeList.clear();
		episodeList.addAll(getEpisodeList);
		playerEpisodeAdapter.notifyDataSetChanged();
		
		downloadList.clear();
		for (int i = 0; i < getEpisodeList.size(); i++) {

			MovieEpisode movieEpisode = new MovieEpisode();
			movieEpisode.setId(getEpisodeList.get(i).getId());
			movieEpisode.setDes(getEpisodeList.get(i).getDes());
			movieEpisode.setPeriods(getEpisodeList.get(i).getPeriods());
			if (i == 0) {
				movieEpisode.setSelect(false);

			} else {
				movieEpisode.setSelect(false);
			}
			downloadList.add(movieEpisode);
		}

		episodeDownAdapter.notifyDataSetChanged();
	}

	private List<Movie> historyList;
	private List<MovieUrl> sourceList, getSourceList;

	private void getData() {
		isCatch = false;
		// if (sourceList != null && sourceList.size() > 0) {
		// MovieUrl movieUrl = sourceList.get(currSourceNum);
		// source_id = movieUrl.getSource_id();
		// getUrl(movieUrl.getSource_id());
		// }

		showLoading();
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub

				if (getSourceList != null && getSourceList.size() > 0) {
					getSource();
					getEpisode();
					hideLoading();
					
					if (movieInfo != null) {
						setInfoData();
					}
					
					getUrl(getSourceList.get(currSourceNum).getSource_url());

				} else {
					mVideoSource = "";
					Toast.makeText(getApplicationContext(), "解析失败", Toast.LENGTH_SHORT).show();
					// if (currSourceNum == list.size() - 1) {
					// currSourceNum = 0;
					// } else {
					// currSourceNum++;
					// }
					// sourceHandler.sendEmptyMessage(0);
					hideLoading();
				}

			}
		};
		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieUrlApi movieUrlApi = new MovieUrlApi();
				currSourceNum = 0;
				if (isEpisode) {
					movieSourceList = movieUrlApi.getMovieEpidsodeUrlList(movie_id, type_id);
					if (movieSourceList != null && movieSourceList.size() > 0) {
						getSourceList = movieSourceList.get(0).getSource_text();
						for (int i = 1; i < movieSourceList.size(); i++) {
							List<MovieUrl> movieUrls = movieSourceList.get(i).getSource_text();
							if (!getSourceList.containsAll(movieUrls)) {
								for (int j = 0; j < movieUrls.size(); j++) {
									if (!getSourceList.contains(movieUrls.get(j))) {
										getSourceList.add(movieUrls.get(j));
									}
								}
							}
						}
					}

				} else {
					getSourceList = movieUrlApi.getMovieUrlList(movie_id, type_id);
				}

				MovieInfoApi movieInfoApi = new MovieInfoApi();
				movieInfo = movieInfoApi.getMovieInfo(movie_id, type_id);

				CommentApi commentApi = new CommentApi();
				getCommentList = commentApi.getCommentList(movie_id, type_id, "1", "5");
				return null;
			}
		}.execute("");

	}
private List<Comment> getCommentList;
	private void getNext() {
		isCatch = false;
		showLoading();
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(mVideoSource)) {
					mLastPos = 0;
					playMovie();
				} else {
					Toast.makeText(getApplicationContext(), "解析失败", Toast.LENGTH_SHORT).show();
					// if (currSourceNum == list.size() - 1) {
					// currSourceNum = 0;
					// } else {
					// currSourceNum++;
					// }
					// sourceHandler.sendEmptyMessage(0);
				}
				hideLoading();
			}
		};
		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieUrlApi movieUrlApi = new MovieUrlApi();
				getSourceList = movieUrlApi.getMovieUrlList(movie_id, type_id);
				currSourceNum = 0;
				if (getSourceList != null && getSourceList.size() > 0) {

					RealUrl realUrl = movieUrlApi.getUrl(getSourceList.get(0).getSource_id(), hd + "", false);
					if (realUrl != null) {
						mVideoSource = realUrl.getUrl();
						ua = realUrl.getUseragent();
					} else {
						mVideoSource = "";
					}

				} else {
					mVideoSource = "";
				}

				return null;
			}
		}.execute("");
	}

	private DataAsyncTask getUrlTask;
	private String ua = "";

	private void getUrl(final String url) {
		isCatch = false;
		showLoading();
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(mVideoSource)) {
					// mLastPos = (int) historyList.get(0).getPlay_Time();
					mLastPos = 0;

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
				RealUrl realUrl = movieUrlApi.getUrl(url, hd + "", false);
				if (realUrl != null) {
					mVideoSource = realUrl.getUrl();
					ua = realUrl.getUseragent();
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

	private LinearLayout holder;

	/**
	 * 初始化界面
	 */
	private void initUI() {
		mViewHolder = (LinearLayout) findViewById(R.id.videoview_holder);
		holder = (LinearLayout) findViewById(R.id.holder);
		/**
		 * 设置ak及sk的前16位
		 */
		BVideoView.setAKSK(AK, SK);

		/**
		 * 创建BVideoView和BMediaController
		 */
		mVV = new BVideoView(this);
		mViewHolder.addView(mVV);
		mVV.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

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

	private LinearLayout bottomLayout, topLayout, portBottomLayout, portTopLayout, sourceLayout, episodeLayout,
			hdLayout, loadingLayout, commentLayout, commentClickLayout, shareLayout, downloadLayout;
	private Button sendButton;
	private EditText commentEditText;

	private GridView episodeGridView;
	private DownloadEpisodeAdapter episodeDownAdapter;
	private List<MovieEpisode> downloadList;
	private int episodeSelectNum;

	private ImageButton leftplayButton, leftbackButton, nextButton, lockButton, portPlayButton, portBackButton;
	private SeekBar leftBar, portSeekBar;
	private TextView leftcurrTextView, leftdurationTextView, portcurrTextView, portdurationTextView, lefttitleTextView,
			leftstateButton, brightTextView, columnTextView, scrollTextView, loadingTextView, episodeTextView,
			currSourceTextView, commentClickEditText, cancelTextView, downloadSure, downloadCancel;
	private ImageView leftLoadingImageView, portFullImageView, shareQQ, shareQZ, shareWX, shareWXC, shareSina;
	private ListView sourceListView, hdListView, episodeListView;
	private List<HD> hdList;
	private List<MovieEpisode> episodeList;
	private PlayerEpisodeAdapter playerEpisodeAdapter;
	private PlayerHDAdapter playerHDAdapter;
	private PlayerSourceAdapter playerSourceAdapter;

	private boolean isPlaying = true;
	private boolean isViewcontrollerShow = false, isHdSelectShow = false;
	private boolean isSeeking = false, isLock = false;

	private void initViewController() {
		topLayout = (LinearLayout) findViewById(R.id.top_layout);
		bottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		portTopLayout = (LinearLayout) findViewById(R.id.port_top_layout);
		portBottomLayout = (LinearLayout) findViewById(R.id.port_bottom_layout);

		portPlayButton = (ImageButton) findViewById(R.id.port_play_button);
		portBackButton = (ImageButton) findViewById(R.id.port_back_button);
		portcurrTextView = (TextView) findViewById(R.id.port_curr_textView);
		portdurationTextView = (TextView) findViewById(R.id.port_duration_textView);
		portSeekBar = (SeekBar) findViewById(R.id.port_seekBar);
		portFullImageView = (ImageView) findViewById(R.id.port_change_imageView);
		portBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		portFullImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				portTopLayout.setVisibility(View.GONE);
				isViewcontrollerShow = false;
				hideViewController();
				isPortrait = false;
				holder.setVisibility(View.GONE);
				Video2DPlayerAndroidActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
		});

		commentLayout = (LinearLayout) findViewById(R.id.comment_layout);
		commentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commentLayout.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		commentEditText = (EditText) findViewById(R.id.comment_editText);
		sendButton = (Button) findViewById(R.id.send_button);
		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// if
				// (!StringUtils.isEmpty(commentEditText.getText().toString()))
				// {
				// webView.loadUrl("javascript:commentCallback(" +
				// commentJsonObject.toJSONString() + ")");
				// }
				commentMovie(commentEditText.getText().toString());
				commentLayout.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});

		commentClickLayout = (LinearLayout) findViewById(R.id.commentclick_layout);
		commentClickEditText = (TextView) findViewById(R.id.commentclick_editText);
		commentClickEditText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commentClickLayout.setVisibility(View.GONE);
				showComment();
			}
		});

		shareLayout = (LinearLayout) findViewById(R.id.share_layout);
		cancelTextView = (TextView) findViewById(R.id.cancel_textView);
		cancelTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.GONE);
			}
		});

		shareQQ = (ImageView) findViewById(R.id.share_QQ);
		shareQQ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareQQ(shareObject.getString("title"), shareObject.getString("content"),
							shareObject.getString("thumb"), shareObject.getString("url"));
				}
			}
		});
		shareQZ = (ImageView) findViewById(R.id.share_QQspace);
		shareQZ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareQZone(shareObject.getString("title"), shareObject.getString("content"),
							shareObject.getString("thumb"), shareObject.getString("url"));
				}
			}
		});
		shareWX = (ImageView) findViewById(R.id.share_weixin);
//		shareWX.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if (shareSDK != null) {
//					shareSDK.shareWeiXin(shareObject.getString("title"), shareObject.getString("content"),
//							shareObject.getString("thumb"), shareObject.getString("url"));
//				}
//			}
//		});
		shareWXC = (ImageView) findViewById(R.id.share_winxinquan);
		shareWXC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareWeiXinCircle(shareObject.getString("title"), shareObject.getString("content"),
							shareObject.getString("thumb"), shareObject.getString("url"));
				}
			}
		});
		shareSina = (ImageView) findViewById(R.id.share_sina);
		shareSina.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareSina(shareObject.getString("title"), shareObject.getString("content"),
							shareObject.getString("thumb"), shareObject.getString("url"));
				}
			}
		});

		downloadLayout = (LinearLayout) findViewById(R.id.download_layout);
		downloadSure = (TextView) findViewById(R.id.download_sure_textView);
		downloadCancel = (TextView) findViewById(R.id.download_cancel_textView);
		episodeGridView = (GridView) findViewById(R.id.episode_gridView);
		downloadList = new ArrayList<>();
		episodeDownAdapter = new DownloadEpisodeAdapter(getApplicationContext(), downloadList);
		episodeGridView.setAdapter(episodeDownAdapter);
		episodeGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		episodeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				boolean isSelect = downloadList.get(position).isSelect();
				downloadList.get(position).setSelect(!isSelect);
				if (isSelect) {
					episodeSelectNum--;
				} else {
					episodeSelectNum++;
				}

				if (episodeSelectNum > 0) {
					downloadSure.setSelected(true);
				} else {
					downloadSure.setSelected(false);
				}
				episodeDownAdapter.notifyDataSetChanged();
			}
		});

		downloadCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideDownload();
			}
		});

		downloadSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (downloadSure.isSelected()) {
					startDownload();
				}
			}
		});

		loadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
		loadingTextView = (TextView) findViewById(R.id.loading_textView);

		scrollTextView = (TextView) findViewById(R.id.scroll_textView);
		brightTextView = (TextView) findViewById(R.id.bright_textView);
		columnTextView = (TextView) findViewById(R.id.column_textView);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenH = dm.heightPixels;

		sourceLayout = (LinearLayout) findViewById(R.id.source_layout);
		ViewGroup.LayoutParams params = sourceLayout.getLayoutParams();
		params.height = screenH - topLayout.getLayoutParams().height - bottomLayout.getLayoutParams().height;
		sourceLayout.setLayoutParams(params);
		sourceList = new ArrayList<MovieUrl>();
		sourceListView = (ListView) findViewById(R.id.source_listView);
		sourceListView.setDividerHeight(0);
		sourceListView.setSelector(new ColorDrawable(android.R.color.transparent));
		playerSourceAdapter = new PlayerSourceAdapter(getApplicationContext(), sourceList);
		sourceListView.setAdapter(playerSourceAdapter);
		sourceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				sourceList.get(currSourceNum).setSelect(false);
				sourceList.get(position).setSelect(true);
				currSourceNum = position;
				if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
					mLastPos = mVV.getCurrentPosition();
					historyList.get(0).setPlay_Time(mLastPos);
				}
				mVV.stopPlayback();
				playerSourceAdapter.notifyDataSetChanged();
				currSourceTextView.setText(sourceList.get(position).getSource_name());
				source_id = sourceList.get(position).getSource_url();
				if (isEpisode) {
					getEpisode();
				}
				getUrl(source_id);
			}
		});
		currSourceTextView = (TextView) findViewById(R.id.currSource_textView);
		currSourceTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				if (isSourceShow) {
					hideSourceSelect();
				} else {
					showSourceSelect();
				}
			}
		});

		hdLayout = (LinearLayout) findViewById(R.id.hd_layout);
		ViewGroup.LayoutParams params1 = hdLayout.getLayoutParams();
		params1.height = screenH - topLayout.getLayoutParams().height - bottomLayout.getLayoutParams().height;
		hdLayout.setLayoutParams(params1);
		leftstateButton = (TextView) findViewById(R.id.hd_textView);
		leftstateButton.setVisibility(View.VISIBLE);
		leftstateButton.setText("标清");
		hdListView = (ListView) findViewById(R.id.hd_listView);
		hdListView.setDividerHeight(0);
		hdListView.setSelector(new ColorDrawable(android.R.color.transparent));
		hdList = new ArrayList<>();

		HD hd0 = new HD();
		hd0.setId("流畅");
		hd0.setSelect(false);
		hdList.add(hd0);

		HD hd1 = new HD();
		hd1.setId("标清");
		hd1.setSelect(true);
		hdList.add(hd1);

		HD hd2 = new HD();
		hd2.setId("高清");
		hd2.setSelect(false);
		hdList.add(hd2);

		playerHDAdapter = new PlayerHDAdapter(getApplicationContext(), hdList);
		hdListView.setAdapter(playerHDAdapter);
		hdListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				hdList.get(hd - 1).setSelect(false);
				hdList.get(position).setSelect(true);
				leftstateButton.setText(hdList.get(position).getId());
				hd = position + 1;
				if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
					mLastPos = mVV.getCurrentPosition();
					historyList.get(0).setPlay_Time(mLastPos);
				}
				mVV.stopPlayback();
				playerHDAdapter.notifyDataSetChanged();
				getUrl(source_id);
			}
		});

		episodeLayout = (LinearLayout) findViewById(R.id.episode_layout);
		ViewGroup.LayoutParams params2 = episodeLayout.getLayoutParams();
		params2.height = screenH - topLayout.getLayoutParams().height - bottomLayout.getLayoutParams().height;
		episodeLayout.setLayoutParams(params2);
		episodeListView = (ListView) findViewById(R.id.episode_listView);
		episodeListView.setDividerHeight(0);
		episodeListView.setSelector(new ColorDrawable(android.R.color.transparent));
		episodeList = new ArrayList<>();
		playerEpisodeAdapter = new PlayerEpisodeAdapter(getApplicationContext(), episodeList);
		episodeListView.setAdapter(playerEpisodeAdapter);
		episodeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				episodeList.get(currEpisodeNum).setSelect(false);
				episodeList.get(position).setSelect(true);
				episodes_id = episodeList.get(position).getId();
				titleString = "第" + episodes_id + "集";
				lefttitleTextView.setText(titleString);
				currEpisodeNum = position;
				if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
					mLastPos = 0;
					// historyList.get(0).setPlay_Time(mLastPos);
				}
				mVV.stopPlayback();
				playerEpisodeAdapter.notifyDataSetChanged();
				getSource();
				source_id = sourceList.get(currSourceNum).getSource_url();
				getUrl(source_id);
			}
		});
		episodeTextView = (TextView) findViewById(R.id.episode_textView);
		if (!isEpisode) {
			episodeTextView.setVisibility(View.GONE);
		}
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

		leftplayButton = (ImageButton) findViewById(R.id.play_button);
		lockButton = (ImageButton) findViewById(R.id.lock_button);
		lockButton.setSelected(false);
		leftbackButton = (ImageButton) findViewById(R.id.back_button);
		leftBar = (SeekBar) findViewById(R.id.seekBar);
		leftcurrTextView = (TextView) findViewById(R.id.curr_textView);
		leftdurationTextView = (TextView) findViewById(R.id.duration_textView);
		lefttitleTextView = (TextView) findViewById(R.id.title_textView);

		leftLoadingImageView = (ImageView) findViewById(R.id.loading_imageView);

		topLayout.setVisibility(View.GONE);
		bottomLayout.setVisibility(View.GONE);

		leftBar.setEnabled(false);
		portSeekBar.setEnabled(false);

		leftbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideViewController();
				portTopLayout.setVisibility(View.VISIBLE);
				isPortrait = true;
				holder.setVisibility(View.VISIBLE);
				Video2DPlayerAndroidActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});

		lefttitleTextView.setText(titleString);

		nextButton = (ImageButton) findViewById(R.id.next_button);
		if (!isEnd) {
			nextButton.setVisibility(View.VISIBLE);
			nextButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startHideViewControllerTimer();
					if (isLoading) {
						return;
					}
					int curr1 = Integer.parseInt(episodes_id);
					curr1++;
					episodes_id = curr1 + "";

					mVV.stopPlayback();
					nextHandler.sendEmptyMessage(0);
				}
			});
		}

		initInfoView();

	}

	private LinearLayout infoEpisodeLayout;
	private TextView infoTitle, infoCollect, infoShare, infoDownload, infoIntro;
	private ImageView infoDrop;
	private HorizontalListView infoEpisodeNumListView, infoLikeListView;
	private ListView infoCommentListView;
	private GridView infoEpisodeGridView;

	private InfoEpisodeNumAdapter infoEpisodeNumAdapter;
	private InfoEpisodeAdapter infoEpisodeAdapter;
	private InfoCommentAdapter infoCommentAdapter;
	private MovieLikeAdapter movieLikeAdapter;

	private int gridViewNum = 6;
	
	private MovieInfo movieInfo;
	private List<MovieEpisodeNum> infoEpisodeNumList;
	private List<Movie> infoLikeList;
	private List<MovieEpisode> infoEpisodeList;
	private List<Comment> infoCommentList;

	private void initInfoView() {
		infoEpisodeLayout = (LinearLayout) findViewById(R.id.info_episode_layout);
		
		infoTitle = (TextView) findViewById(R.id.info_title_textView);
		infoCollect = (TextView) findViewById(R.id.info_collect_textView);
		infoShare = (TextView) findViewById(R.id.info_share_textView);
		infoDownload = (TextView) findViewById(R.id.info_download_textView);
		infoIntro = (TextView) findViewById(R.id.info_intro_textView);
		infoDrop = (ImageView) findViewById(R.id.info_drop_imageView);

		infoEpisodeNumListView = (HorizontalListView) findViewById(R.id.info_episode_horizontalListView);
		infoEpisodeGridView = (GridView) findViewById(R.id.info_episode_gridView);
		infoCommentListView = (ListView) findViewById(R.id.info_comment_listView);
		infoLikeListView = (HorizontalListView) findViewById(R.id.info_like_horizontalListView);

		infoEpisodeNumList = new ArrayList<>();
		infoEpisodeNumAdapter = new InfoEpisodeNumAdapter(getApplicationContext(), infoEpisodeNumList);
		infoEpisodeNumListView.setAdapter(infoEpisodeNumAdapter);

		infoEpisodeList = new ArrayList<>();
		infoEpisodeAdapter = new InfoEpisodeAdapter(getApplicationContext(), infoEpisodeList);
		infoEpisodeGridView.setAdapter(infoEpisodeAdapter);
		infoEpisodeGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		infoEpisodeGridView.setNumColumns(6);

		infoLikeList = new ArrayList<>();
		movieLikeAdapter = new MovieLikeAdapter(this, infoLikeList);
		infoLikeListView.setAdapter(movieLikeAdapter);

		infoCommentList = new ArrayList<>();
		infoCommentAdapter = new InfoCommentAdapter(this, infoCommentList);
		infoCommentListView.setAdapter(infoCommentAdapter);
		infoCommentListView.setSelector(new ColorDrawable(android.R.color.transparent));
	}

	private void setInfoData() {
		infoTitle.setText(movieInfo.getTitle());
		infoIntro.setText(movieInfo.getSummary());
		
		infoLikeList.addAll(movieInfo.getMore());
		movieLikeAdapter.notifyDataSetChanged();
		
		int gridPageNum = gridViewNum * 2;
		int num = getEpisodeList.size() / gridPageNum;
		for (int i = 0; i < num + 1; i++) {
			MovieEpisodeNum movieEpisodeNum = new MovieEpisodeNum();
			movieEpisodeNum.setId(i*gridPageNum + 1 + "-" + (i*gridPageNum + gridPageNum));
			if (i == 0) {
				movieEpisodeNum.setSelect(true);
			}else{
				movieEpisodeNum.setSelect(false);
			}
			infoEpisodeNumList.add(movieEpisodeNum);
		}
		
		getEpisodeData(0);
		
		infoEpisodeNumAdapter.notifyDataSetChanged();
		infoEpisodeGridView.setNumColumns(gridViewNum);
		infoEpisodeAdapter.notifyDataSetChanged();
		
		if (getCommentList != null) {
			infoCommentList.addAll(getCommentList);
			infoCommentAdapter.notifyDataSetChanged();
		}
		

	}

	private void getEpisodeData(int num){
		infoEpisodeList.clear();
		for (int i = num * gridViewNum * 2; i < (num+1) * gridViewNum * 2; i++) {
			if (i < getEpisodeList.size()) {
				MovieEpisode movieEpisode = new MovieEpisode();
				movieEpisode.setId(getEpisodeList.get(i).getId());
				movieEpisode.setDes(getEpisodeList.get(i).getDes());
				movieEpisode.setPeriods(getEpisodeList.get(i).getPeriods());
				if (i == 0) {
					movieEpisode.setSelect(false);

				} else {
					movieEpisode.setSelect(false);
				}
				infoEpisodeList.add(movieEpisode);
			}
			
		}
	}
	
	private void setView() {
		leftstateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				if (isHdSelectShow) {
					isHdSelectShow = false;
					hideHdSelect();
				} else {
					isHdSelectShow = true;
					showHdSelect();
				}

				// restartMovie();
			}
		});

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

		portPlayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startHideViewControllerTimer();
				if (isPlaying) {
					mVV.pause();
					portPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.port_play));
					isPlaying = false;
				} else {
					if (mPlayerStatus == PLAYER_STATUS.PLAYER_IDLE) {
						mEventHandler.sendEmptyMessage(EVENT_PLAY);
					} else {
						mVV.resume();
					}
					portPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.port_stop));
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
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if (isSeeking) {
					startHideViewControllerTimer();
					mVV.seekTo(progress);
					String timeString = toTime(progress);
					leftcurrTextView.setText(timeString);
				}

			}
		});

		leftBar.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					isSeeking = true;
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					isSeeking = false;
				}

				return false;
			}
		});

		portSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

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
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				// TODO Auto-generated method stub
				if (isSeeking) {
					startHideViewControllerTimer();
					mVV.seekTo(progress);
					String timeString = toTime(progress);
					portcurrTextView.setText(timeString);
				}

			}
		});

		portSeekBar.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					isSeeking = true;
				} else if (event.getAction() == KeyEvent.ACTION_UP) {
					isSeeking = false;
				}

				return false;
			}
		});
	}

	private String source_id;

	private boolean isPause = false;

	@SuppressWarnings("deprecation")
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.v(TAG, "onPause");

		portPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.port_play));
		leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_start));
		if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
			mLastPos = mVV.getCurrentPosition();
		}
		if (mVV != null) {
			mVV.stopPlayback();
		}

		isPause = true;
		try {
			if (isEpisode) {
				historyList.get(0).setEpisode_id(episodes_id);
			}
			historyList.get(0).setPlay_Time(mLastPos);
			SerializationUtil.wirteHistorySerialization(getApplicationContext(), (Serializable) historyList);
		} catch (Exception e) {
			// TODO: handle exception
			Logger.e("SerializationUtil", e.getMessage());
		}

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
			portPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.port_stop));
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

		currSourceNum = -1;
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

	private Handler nextHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			titleString = "第" + episodes_id + "集";
			lefttitleTextView.setText(titleString);
			getNext();
		};
	};

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
						if (portSeekBar.getMax() > 0) {
							portSeekBar.setEnabled(true);
						}
						portSeekBar.setMax(duration);
						portSeekBar.setProgress(curr);
						portdurationTextView.setText(durationString);
						portcurrTextView.setText(timeString);
						if (leftBar.getMax() > 0) {
							leftBar.setEnabled(true);
						}
						leftBar.setMax(duration);
						leftBar.setProgress(curr);
						leftdurationTextView.setText(durationString);
						leftcurrTextView.setText(timeString);
					}

				}

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
				portPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.port_stop));
			} else if (msg.what == 5) {
				leftplayButton.setImageDrawable(getResources().getDrawable(R.drawable.player_start));
				portPlayButton.setImageDrawable(getResources().getDrawable(R.drawable.port_play));
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
		if (isPortrait) {
			portBottomLayout.clearAnimation();
			TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 140, 0);
			bottomtranslateAnimation.setDuration(300);
			bottomtranslateAnimation.setFillAfter(true);

			portBottomLayout.startAnimation(bottomtranslateAnimation);

			startHideViewControllerTimer();
		} else {
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
		if (isPortrait) {
			main.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			portBottomLayout.clearAnimation();
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
					portBottomLayout.clearAnimation();
					portBottomLayout.setVisibility(View.GONE);
				}
			});

			portBottomLayout.startAnimation(bottomtranslateAnimation);
		} else {
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
	}

	private void showHdSelect() {

		hdLayout.setVisibility(View.VISIBLE);

		hdLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(550, 0, 0, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		hdLayout.startAnimation(bottomtranslateAnimation);

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

	private void showSourceSelect() {
		if (isSourceShow) {
			return;
		} else {
			isSourceShow = true;
		}
		isLockVisible = false;
		lockButton.setVisibility(View.GONE);
		sourceLayout.setVisibility(View.VISIBLE);

		sourceLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(-550, 0, 0, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		sourceLayout.startAnimation(bottomtranslateAnimation);

	}

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
		msg.what = 100;
		if (netSpeed < 1000) {
			msg.obj = String.valueOf(netSpeed) + " b/s";
		} else if (netSpeed < 1000 * 1000) {
			msg.obj = String.valueOf(((int) (netSpeed / 1000.0 * 100)) / 100.0) + " kb/s";
		} else if (netSpeed < 1000 * 1000 * 1000) {
			msg.obj = String.valueOf(((int) (netSpeed / 1000.0 / 1000 * 100)) / 100.0) + " mb/s";
		} else {
			msg.obj = String.valueOf(((int) (netSpeed / 1000.0 / 1000 / 1000 * 100)) / 100.0) + " gb/s";
		}

		speedHandler.sendMessage(msg);// 更新界面
	}

	private Handler speedHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			if (loadingTextView != null) {
				loadingTextView.setText((CharSequence) msg.obj);
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
				loadingTextView.setText("0kb/s");
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
			currColumn = SystemUtils.getCurrentActivityColumn(Video2DPlayerAndroidActivity.this);

			currBright = tempBright;
			isMove = 0;
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			if (isPortrait) {
				return true;
			}
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
					if (leftBar.getMax() > 0) {
						isMove = 1;
						scrollTextView.setVisibility(View.VISIBLE);
						if (leftBar.getProgress() + distanceX > leftBar.getMax()) {
							distanceX = leftBar.getMax() - leftBar.getProgress();
						} else if (leftBar.getProgress() + distanceX < 0) {
							distanceX = 0 - leftBar.getProgress();
						}
						int progress = (int) (leftBar.getProgress() + distanceX);
						String timeString = toTime(progress);
						scrollTextView.setText(timeString);
					} else {
						isMove = 2;
					}

				} else {
					isMove = 2;
				}
			}

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (isPortrait) {
				if (!isViewcontrollerShow) {
					isViewcontrollerShow = true;
					showViewController();
				} else {
					isViewcontrollerShow = false;
					hideViewController();

				}
				return true;
			}
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
				float distanceX = event.getX() - startX;
				mVV.seekTo(leftBar.getProgress() + distanceX);
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
		SystemUtils.setCurrentActivityBrightness(Video2DPlayerAndroidActivity.this, num);
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

		SystemUtils.setCurrentActivityColumn(Video2DPlayerAndroidActivity.this, num, 0);
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

	public void openComment() {
		commentClickLayout.setVisibility(View.VISIBLE);
	}

	public void closeComment() {
		commentClickLayout.setVisibility(View.GONE);
	}

	private void showComment() {
		isComment = true;
		commentJsonObject = new JSONObject();
		commentJsonObject.put("user_id", GlobalParams.user.getUid());
		commentEditText.setText("");
		commentLayout.setVisibility(View.VISIBLE);
		commentEditText.requestFocus();
		imm.showSoftInput(commentEditText, 0);
	}

	private void commentMovie(String comment) {

	}

	private JSONObject commentJsonObject;

	public void showReply(JSONObject jsonObject) {
		isComment = false;
		commentJsonObject = jsonObject;
		closeComment();
		commentLayout.setVisibility(View.VISIBLE);
		commentEditText.requestFocus();
		imm.showSoftInput(commentEditText, 0);
		commentEditText.setHint("回复" + jsonObject.getString("from_nickname"));
	}

	public void changeEpisode(String changeEpisode_id) {
		startHideViewControllerTimer();
		if (getUrlTask != null) {
			getUrlTask.cancel(true);
		}
		int position = currEpisodeNum;
		if (episodeList.size() > currEpisodeNum) {
			episodeList.get(currEpisodeNum).setSelect(false);
			for (int i = 0; i < episodeList.size(); i++) {
				if (episodeList.get(i).getId().equals(changeEpisode_id)) {
					position = i;
					break;
				}
			}

			episodeList.get(position).setSelect(true);
			episodes_id = episodeList.get(position).getId();
			titleString = "第" + episodes_id + "集";
			lefttitleTextView.setText(titleString);
			currEpisodeNum = position;
			if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
				mLastPos = 0;
				// historyList.get(0).setPlay_Time(mLastPos);
			}
			mVV.stopPlayback();
			playerEpisodeAdapter.notifyDataSetChanged();
			getSource();
			source_id = sourceList.get(currSourceNum).getSource_url();
			getUrl(source_id);
		} else {
			Toast.makeText(getApplicationContext(), "获取视频失败", Toast.LENGTH_SHORT).show();
		}

	}

	private JSONObject shareObject;
	private ShareSDK shareSDK;

	public void showShare(JSONObject jsonObject) {
		shareObject = jsonObject;
		if (shareSDK == null) {
			shareSDK = new ShareSDK(this);
		}
		shareLayout.setVisibility(View.VISIBLE);
	}

	public void showDownload(String image_url, String movie_title) {
		// isDownloadShow = true;
		this.image_url = image_url;
		this.movie_title = movie_title;
		downloadLayout.setVisibility(View.VISIBLE);

		// downloadLayout.clearAnimation();
		// TranslateAnimation bottomtranslateAnimation = new
		// TranslateAnimation(0, 0, 1500, 0);
		// bottomtranslateAnimation.setDuration(300);
		// bottomtranslateAnimation.setFillAfter(true);
		//
		// downloadLayout.startAnimation(bottomtranslateAnimation);
	}

	private void hideDownload() {
		if (downloadLayout.getVisibility() == View.GONE) {
			// isDownloadShow = false;
			return;
		}
		// isDownloadShow = false;
		// downloadLayout.clearAnimation();
		// TranslateAnimation bottomtranslateAnimation = new
		// TranslateAnimation(0, 0, 0, 1500);
		// bottomtranslateAnimation.setDuration(300);
		// bottomtranslateAnimation.setFillAfter(true);
		// bottomtranslateAnimation.setAnimationListener(new AnimationListener()
		// {
		//
		// @Override
		// public void onAnimationStart(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation) {
		// // TODO Auto-generated method stub
		// downloadLayout.clearAnimation();
		// downloadLayout.setVisibility(View.GONE);
		// }
		// });
		//
		// downloadLayout.startAnimation(bottomtranslateAnimation);
		downloadLayout.setVisibility(View.GONE);

	}

	private int downloadNum = 0;
	private M3U8Utils m3u8Utils;

	private void startDownload() {
		downloadNum = 0;
		m3u8Utils = new M3U8Utils(getApplicationContext(), movie_id, type_id, image_url, movie_title, isEpisode,
				sourceList.get(currSourceNum).getSource_name(), sourceList.get(currSourceNum).getSource_url(), hd);
		if (isEpisode) {
			PromptManager.showCancelProgressDialog(Video2DPlayerAndroidActivity.this);
			for (int i = 0; i < downloadList.size(); i++) {
				MovieEpisode movieEpisode = downloadList.get(i);
				if (movieEpisode.isSelect()) {
					List<MovieUrl> list = movieSourceList.get(Integer.parseInt(movieEpisode.getId()) - 1)
							.getSource_text();
					for (int j = 0; j < list.size(); j++) {
						MovieUrl movieUrl = list.get(j);
						if (movieUrl.getSource_id().equals(sourceList.get(currSourceNum).getSource_id())) {
							downloadNum++;
							getDownloadUrl(movieUrl.getSource_id(), Integer.parseInt(movieEpisode.getId()));
						}
					}

				}
			}
		} else {
			downloadNum = 1;
			PromptManager.showCancelProgressDialog(Video2DPlayerAndroidActivity.this);
			getDownloadUrl(sourceList.get(currSourceNum).getSource_id(), 0);
		}
	}

	private void getDownloadUrl(final String url, final int episode_id) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(result)) {
					Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
					;
				} else {
					m3u8Utils.download(episode_id);
				}
				downloadNum--;
				if (downloadNum <= 0) {
					// saveDb();
					hideDownload();
					PromptManager.closeCancelProgressDialog();
				}

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (shareSDK != null) {
			shareSDK.onResult(requestCode, resultCode, data);
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
				historyList.get(0).setPlay_Time(0);
				getData();
				dialog.cancel();
			}
		});
		tv.setText("存在历史播放记录，是否继续播放？");
		view.setMinimumWidth(600);
		dialog.setContentView(view);
		dialog.show();

	}
}
