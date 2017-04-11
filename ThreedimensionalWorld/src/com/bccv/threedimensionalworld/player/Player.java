package com.bccv.threedimensionalworld.player;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bccv.threedimensionalworld.activity.MusicActivity;
import com.bccv.threedimensionalworld.model.MusicBean;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 歌曲播放器
 * 
 * 
 */
public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		OnPreparedListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MediaPlayer mediaPlayer; // 媒体播放器
	private SeekBar seekBar, seekBar1; // 拖动条
	public Timer mTimer = new Timer(); // 计时器
	private TextView textNowPlayTime, textNowPlayTime1;
	private TextView textTotalPlayTime, textTotalPlayTime1;
	private Context context;
	private int currentListItem = 0;
	private boolean isLocal;
	private List<MusicBean> data;

	public int getCurrentListItem() {
		return currentListItem;
	}

	public void setCurrentListItem(int currentListItem) {
		this.currentListItem = currentListItem;
	}

	public List<MusicBean> getData() {
		return data;
	}

	public void setData(List<MusicBean> data) {
		this.data = data;
	}

	// 初始化播放器
	public Player(SeekBar seekBar, SeekBar seekBar1, Context context,
			boolean isLocal) {
		super();
		this.seekBar = seekBar;
		this.seekBar1 = seekBar1;
		textNowPlayTime = new TextView(context);
		textTotalPlayTime = new TextView(context);
		textNowPlayTime1 = new TextView(context);
		textTotalPlayTime1 = new TextView(context);
		this.context = context;
		this.isLocal = isLocal;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
//		mediaPlayer.prepareAsync();
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 每一秒触发一次
	mTimer.schedule(timerTask, 0, 1000);
//		new Thread(thread).start();
	}
//
//	// 计时器
	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && seekBar.isPressed() == false) {
				handler.sendEmptyMessage(0); // 发送消息
			}
		}
	};
	
//	private Runnable thread = new Runnable()
//	{
//		@Override
//		public void run()
//		{
//		
//			handler.postDelayed(thread, 1000);
//		}
//	};
	
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();
		
			if (duration > 0) {
				// 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
				long pos = seekBar.getMax() * position / duration;

				seekBar.setProgress((int) pos);

				seekBar1.setProgress((int) pos);
//			Log.e("进度", position / 1000 / 60 + ":" + position
//					/ 1000 % 60);
				
				
				
				textNowPlayTime.setText(position / 1000 / 60 + ":" + position
						/ 1000 % 60);

				textNowPlayTime1.setText(position / 1000 / 60 + ":" + position
						/ 1000 % 60);

				textTotalPlayTime.setText(duration / 1000 / 60 + ":" + duration
						/ 1000 % 60);
				textTotalPlayTime1.setText(duration / 1000 / 60 + ":"
						+ duration / 1000 % 60);
			}
		};
	};

	public void play() {

		try {

			
			mediaPlayer.prepare();
			mediaPlayer.start();

		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param url
	 *            url地址
	 */
	public void playUrl(String url) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url); // 设置数据源
			mediaPlayer.prepare(); // prepare自动播放

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 暂停
	public void pause() {
		mediaPlayer.pause();
	}

	// 继续
	public void start() {
		mediaPlayer.start();
	}

	// 停止
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// play.setImageResource(R.drawable.song_detail_play);
		Log.e("mediaPlayer", "onCompletion");

		Intent intent = new Intent();
		if (isLocal) {

			intent.setAction("isLocal");

		} else {

			intent.setAction("next");

		}

		context.sendBroadcast(intent);

	}

	/**
	 * 缓冲更新
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		seekBar.setSecondaryProgress(percent);
		seekBar1.setSecondaryProgress(percent);
		int currentProgress = seekBar.getMax()*mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();

		Log.e(currentProgress + "% play", percent + " buffer");
	}

	/**
	 * 获取播放状态
	 */
	public boolean getIsPlaying() {
		try {
			return mediaPlayer.isPlaying();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;

	}

	/**
	 * 设置拖动条
	 */
	public void setSeekbar(SeekBar seekBar) {
		this.seekBar = seekBar;
	}

	/**
	 * 设置显示当前播放时间和总时间
	 * 
	 * @param allTimeText
	 * @param allTimeText2
	 */
	public void setPlayTimeTextView(TextView textNowPlayTime,
			TextView allTimeText, TextView textTotalPlayTime,
			TextView allTimeText2) {
		this.textNowPlayTime = textNowPlayTime;
		this.textTotalPlayTime = textTotalPlayTime;
		this.textNowPlayTime1 = allTimeText;
		this.textTotalPlayTime1 = allTimeText2;
	}

	/**
	 * 重新获取mediaplayer
	 */
	public void initMediaPlayer() {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.prepareAsync();
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
public void CanleTimer(){
	
	if(mTimer!=null){
		mTimer.cancel();
	}
	
	
	
}
	
	
	
	// /**
	// * 下一首
	// */
	// public void nextSong() {
	//
	// if (++currentListItem >= data.size()) {
	// currentListItem = 0;
	// } else {
	// playUrl(data.get(currentListItem).getUrl());
	// }
	//
	// }

	// /**
	// * 上一首
	// */
	// public void perSong() {
	//
	// if (currentListItem != 0) {
	//
	// if (--currentListItem >= 0) {
	// currentListItem = data.size();
	// } else {
	// playUrl(data.get(currentListItem).getUrl());
	// }
	// } else {
	// playUrl(data.get(currentListItem).getUrl());
	// }
	//
	// }

}
