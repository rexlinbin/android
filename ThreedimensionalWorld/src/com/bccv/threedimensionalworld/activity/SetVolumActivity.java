package com.bccv.threedimensionalworld.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.bccv.threedimensionalworld.R;

public class SetVolumActivity extends Activity {

	AudioManager mAudioManager;

	int maxVolume;
	int currentVolume;
	private boolean isClick = true;

	private SeekBar soundBar;
	private TextView text;
	private SeekBar soundBar1;
	private TextView text1;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setvolume);

		soundBar = (SeekBar) findViewById(R.id.setting_volumeSeekBar);
		soundBar.setOnSeekBarChangeListener(new OnSeekbar());

		text = (TextView) findViewById(R.id.setting_volume_textView);
		TextView startText = (TextView) findViewById(R.id.volume_start_text);
		startText.setScaleX(0.5f);

		TextView startText1 = (TextView) findViewById(R.id.volume_start_text1);
		startText1.setScaleX(0.5f);
		TextView endText = (TextView) findViewById(R.id.volume_end_text);
		endText.setScaleX(0.5f);
		TextView endText1 = (TextView) findViewById(R.id.volume_end_text1);
		endText1.setScaleX(0.5f);

		soundBar1 = (SeekBar) findViewById(R.id.setting_volumeSeekBar1);
		soundBar1.setOnSeekBarChangeListener(new OnSeekbar());

		text1 = (TextView) findViewById(R.id.setting_volume_textView1);
		// 音量控制,初始化定义
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// 最大音量
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		// 当前音量
		soundBar.setProgress(currentVolume);
		soundBar.setMax(maxVolume);
		text.setText(currentVolume * 100 / maxVolume + " %");
		Log.e("音量", currentVolume + "");
		Log.e("音量maxVolume", maxVolume + "");
		soundBar1.setProgress(currentVolume);
		soundBar1.setMax(maxVolume);
		text1.setText(currentVolume * 100 / maxVolume + " %");
		text.setScaleX(0.5f);
		text1.setScaleX(0.5f);
	}

	class OnSeekbar implements OnSeekBarChangeListener {
  
		@Override
		public void onProgressChanged(                                                               SeekBar arg0, int progress, boolean arg2) {
			// TODO Auto-generated method stub
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
					0);
			currentVolume = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
			soundBar.setProgress(currentVolume);
			text.setText(currentVolume * 100 / maxVolume + " %");
			soundBar1.setProgress(currentVolume);
			text1.setText(currentVolume * 100 / maxVolume + " %");
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

	}
}