package com.bccv.threedimensionalworld.activity;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.SrtManager;
import com.bccv.threedimensionalworld.view.MyTitleView;

@SuppressLint("NewApi")
public class MainVideoBackActivity extends BaseActivity implements
		OnPreparedListener, OnCompletionListener, OnErrorListener,
		OnInfoListener, OnPlayingBufferCacheListener {
	private Button leftlocalButton, leftmovieButton, leftmusicButton,
			leftgameButton, leftappButton, leftsettingButton;
	private Button rightlocalButton, rightmovieButton, rightmusicButton,
			rightgameButton, rightappButton, rightsettingButton;

	private RelativeLayout leftLayout, rightLayout;
	private TextView leftStatusTextView;
	private TextView rightStatusTextView;
	private boolean isPause = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainvideoback);
		leftLayout = (RelativeLayout) findViewById(R.id.left_relativeLayout);
		rightLayout = (RelativeLayout) findViewById(R.id.right_relativeLayout);
		setLeft();
		setRight();
		setStatus();
		setVideo();
		// SrtManager strManager = new SrtManager();
		// strManager.loadSrtFile(Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/subtitle/Avatar.srt");
	}

	private void setLeft() {

		leftlocalButton = (Button) findViewById(R.id.left_local_Button);
		leftmovieButton = (Button) findViewById(R.id.left_movie_Button);
		leftmusicButton = (Button) findViewById(R.id.left_music_Button);
		leftgameButton = (Button) findViewById(R.id.left_game_Button);
		leftappButton = (Button) findViewById(R.id.left_app_Button);
		leftsettingButton = (Button) findViewById(R.id.left_setting_Button);

		leftmovieButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftmovieButton.bringToFront();
					rightmovieButton.setSelected(true);
					rightmovieButton.bringToFront();
					leftmovieButton.setSelected(true);
					LayoutParams params = new LayoutParams(292,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(1, 40, 0, 0);
					leftmovieButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(292,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(0, 40, 0, 0);
					leftmovieButton.setLayoutParams(params);
					rightmovieButton.setSelected(false);
					leftmovieButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftmovieButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovie();
			}
		});

		leftgameButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftgameButton.bringToFront();
					leftgameButton.setSelected(true);
					rightgameButton.setSelected(true);
					rightgameButton.bringToFront();
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(230, 57, 0, 0);
					leftgameButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(229, 57, 0, 0);
					leftgameButton.setLayoutParams(params);
					rightgameButton.setSelected(false);
					leftgameButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftgameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startGame();
			}
		});
		leftappButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftappButton.bringToFront();
					leftappButton.setSelected(true);
					rightappButton.setSelected(true);
					rightappButton.bringToFront();
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(230, 293, 0, 0);
					leftappButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(283,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(229, 293, 0, 0);
					leftappButton.setLayoutParams(params);
					rightappButton.setSelected(false);
					leftappButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftappButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startApp();
			}
		});

		leftmusicButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftmusicButton.bringToFront();
					leftmusicButton.setSelected(true);
					rightmusicButton.setSelected(true);
					rightmusicButton.bringToFront();
					LayoutParams params = new LayoutParams(171,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(457, 40, 0, 0);
					leftmusicButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(171,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(456, 40, 0, 0);
					leftmusicButton.setLayoutParams(params);
					rightmusicButton.setSelected(false);
					leftmusicButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftmusicButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMusic();
			}
		});

		leftlocalButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftlocalButton.bringToFront();
					rightlocalButton.setSelected(true);
					leftlocalButton.setSelected(true);
					rightlocalButton.bringToFront();
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(580, 57, 0, 0);
					leftlocalButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(579, 57, 0, 0);
					leftlocalButton.setLayoutParams(params);
					rightlocalButton.setSelected(false);
					leftlocalButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftlocalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startLocal();
			}
		});

		leftsettingButton.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					leftsettingButton.bringToFront();
					leftsettingButton.setSelected(true);
					rightsettingButton.setSelected(true);
					rightsettingButton.bringToFront();
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(580, 293, 0, 0);
					leftsettingButton.setLayoutParams(params);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				} else {
					LayoutParams params = new LayoutParams(166,
							LayoutParams.WRAP_CONTENT);
					params.setMargins(579, 293, 0, 0);
					leftsettingButton.setLayoutParams(params);
					rightsettingButton.setSelected(false);
					leftsettingButton.setSelected(false);
					leftLayout.requestLayout();
					rightLayout.requestLayout();
				}
			}
		});
		leftsettingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSetting();
			}
		});

	}

	private void setRight() {

		rightlocalButton = (Button) findViewById(R.id.right_local_Button);
		rightmovieButton = (Button) findViewById(R.id.right_movie_Button);
		rightmusicButton = (Button) findViewById(R.id.right_music_Button);
		rightgameButton = (Button) findViewById(R.id.right_game_Button);
		rightappButton = (Button) findViewById(R.id.right_app_Button);
		rightsettingButton = (Button) findViewById(R.id.right_setting_Button);

		rightlocalButton.setFocusable(false);
		rightlocalButton.setFocusableInTouchMode(false);
		rightmovieButton.setFocusable(false);
		rightmovieButton.setFocusableInTouchMode(false);
		rightmusicButton.setFocusable(false);
		rightmusicButton.setFocusableInTouchMode(false);
		rightgameButton.setFocusable(false);
		rightgameButton.setFocusableInTouchMode(false);
		rightappButton.setFocusable(false);
		rightappButton.setFocusableInTouchMode(false);
		rightsettingButton.setFocusable(false);
		rightsettingButton.setFocusableInTouchMode(false);

		rightlocalButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startLocal();
			}
		});

		rightmovieButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMovie();
			}
		});

		rightmusicButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startMusic();
			}
		});

		rightappButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startApp();
			}
		});

		rightgameButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startGame();
			}
		});

		rightsettingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startSetting();
			}
		});
	}

	private void startMovie() {
		Intent intent = new Intent(getApplicationContext(),
				MovieClassificationActivity.class);
		startActivity(intent);
	}

	private void startGame() {
		Intent intent = new Intent(getApplicationContext(),
				GameClassificationActivity.class);
		startActivity(intent);
	}

	private void startApp() {

		Intent intent = new Intent(MainVideoBackActivity.this,
				AppClassificationActivity.class);
		startActivity(intent);
	}

	private void startMusic() {
		Intent intent = new Intent(MainVideoBackActivity.this,
				MusicSelectActivity.class);
		startActivity(intent);
	}

	private void startLocal() {
		Intent intent = new Intent(MainVideoBackActivity.this,
				LocalActivity.class);
		startActivity(intent);
	}

	private void startSetting() {
		Intent intent = new Intent(MainVideoBackActivity.this,
				SettingActivity.class);
		startActivity(intent);
	}

	/**
	 * 您的ak
	 */
	private String AK = GlobalParams.AK;
	/**
	 * //您的sk的前16位
	 */
	private String SK = GlobalParams.SK;

	private String mVideoSource = "";
	private RelativeLayout videoHoldeRelativeLayout;
	private BVideoView mVV = null;
	private EventHandler mEventHandler;
	private HandlerThread mHandlerThread;

	private final Object SYNC_Playing = new Object();

	private final int EVENT_PLAY = 0;

	private WakeLock mWakeLock = null;

	/**
	 * 播放状态
	 */
	private enum PLAYER_STATUS {
		PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
	}

	private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
	private static final String POWER_LOCK = "MainVideoBackActivity";

	private void setVideo() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
		videoHoldeRelativeLayout = (RelativeLayout) findViewById(R.id.videoview_holder);

		/**
		 * 设置ak及sk的前16位
		 */
		BVideoView.setAKSK(AK, SK);

		/**
		 * 创建BVideoView和BMediaController
		 */
		mVV = new BVideoView(this);
		videoHoldeRelativeLayout.addView(mVV);

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
		mVV.setDecodeMode(BVideoView.DECODE_HW);

		mHandlerThread = new HandlerThread("event handler thread",
				Process.THREAD_PRIORITY_BACKGROUND);
		mHandlerThread.start();
		mEventHandler = new EventHandler(mHandlerThread.getLooper());
		mEventHandler.sendEmptyMessage(EVENT_PLAY);
	}

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
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				mVideoSource = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/movie/avatar_2015828134352.mkv";
				// mVideoSource =
				// "http://3d.cgame.tv/data/m3u8/faea21858713c8a1dd26ebd8c8e83976_cache.m3u8";
				try {
					String str[] = getAssets().list("fonts");
					int i = 0;
					i = 5;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/**
				 * 设置播放url
				 */
				mVV.setVideoPath(mVideoSource);

				/**
				 * 显示或者隐藏缓冲提示
				 */
				mVV.showCacheInfo(false);

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
	public void onPlayingBufferCache(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onInfo(int arg0, int arg1) {
		// TODO Auto-generated method stub
		switch (arg0) {
		/**
		 * 开始缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_START:
			break;
		/**
		 * 结束缓冲
		 */
		case BVideoView.MEDIA_INFO_BUFFERING_END:
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean onError(int arg0, int arg1) {
		// TODO Auto-generated method stub
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		return true;
	}

	@Override
	public void onCompletion() {
		// TODO Auto-generated method stub
		synchronized (SYNC_Playing) {
			SYNC_Playing.notify();
		}
		mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
		if (!isPause) {
			mEventHandler.sendEmptyMessage(EVENT_PLAY);
		}
	}

	@Override
	public void onPrepared() {
		// TODO Auto-generated method stub
		mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isPause = true;
		/**
		 * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
		 */
		// if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
		// mVV.stopPlayback();
		// }
		mVV.stopPlayback();
		
		if (leftMyTitleView != null && rightMyTitleView != null) {
			leftMyTitleView.pauseView();
			rightMyTitleView.pauseView();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isPause = false;
		if (null != mWakeLock && (!mWakeLock.isHeld())) {
			mWakeLock.acquire();
		}
		/**
		 * 发起一次播放任务,当然您不一定要在这发起
		 */
		if (mEventHandler != null) {
			mEventHandler.sendEmptyMessage(EVENT_PLAY);
		}

		if (leftMyTitleView != null && rightMyTitleView != null) {
			leftMyTitleView.resumeView();
			rightMyTitleView.resumeView();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		/**
		 * 结束后台事件处理线程
		 */
		mHandlerThread.quit();
	}

	private MyTitleView leftMyTitleView, rightMyTitleView;

	private void setStatus() {
		leftMyTitleView = (MyTitleView) findViewById(R.id.left_myTitleView);
		rightMyTitleView = (MyTitleView) findViewById(R.id.right_myTitleView);
	}

}
