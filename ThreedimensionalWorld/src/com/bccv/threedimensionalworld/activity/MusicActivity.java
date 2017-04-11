package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.api.MusicApi;
import com.bccv.threedimensionalworld.model.MusicBean;
import com.bccv.threedimensionalworld.player.Player;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("NewApi")
public class MusicActivity extends BaseActivity {
	private TextView text, AllTimeText, timeText, Author;
	private TextView text1, AllTimeText1, timeText1, Author1;
	private Player player;
	SeekBar seekBar;
	SeekBar seekBar1;
	private String musicTitle, musicAuthor;
	private ImageView im;
	private ImageView im1;
	private String picString;
	ImageLoader imageLoader;
	DisplayImageOptions options;

	private Button preBtn, pauseBtn, nextBtn;
	List<MusicBean> data = new ArrayList<MusicBean>();
	private Button preBtn1, pauseBtn1, nextBtn1;
	String url = null;
	private boolean isLocal;
	private int position;
	Animation animation, animation1, animation3, animation4;
	private RelativeLayout muSicRE, muSicRE1;
	private boolean isLoading = false;
	private ImageView leftLoadingImageView, rightLoadingImageView;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		setContentView(R.layout.activity_music);
		setLeft();
		setRight();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration
				.createDefault(MusicActivity.this));
		isLocal = getIntent().getBooleanExtra("isLocal", false);
		player = new Player(seekBar, seekBar1, this, isLocal);
		leftLoadingImageView = (ImageView) findViewById(R.id.left_loading_imageView);
		rightLoadingImageView = (ImageView) findViewById(R.id.right_loading_imageView);
		if (isLocal) {

			data = (List<MusicBean>) getIntent().getSerializableExtra("list");
			position = getIntent().getIntExtra("position", -1);

			setLocalData(position);

			player.setData(data);
			player.setCurrentListItem(position);

		} else {
			setData();
		}

		IntentFilter filter = new IntentFilter();
		filter.addAction("next");
		filter.addAction("isLocal");
		MusicActivity.this.registerReceiver(mBroadcastReceiver, filter);

		// animation = AnimationUtils.loadAnimation(
		// MusicActivity.this, R.anim.back_scale);
		// animation1 = AnimationUtils.loadAnimation(
		// MusicActivity.this, R.anim.fornt_acale);
		//
		// animation3 = AnimationUtils.loadAnimation(
		// MusicActivity.this, R.anim.back_scale);
		// animation4 = AnimationUtils.loadAnimation(
		// MusicActivity.this, R.anim.fornt_acale);
	}

	private void setRight() {
		// TODO Auto-generated method stub
		text1 = (TextView) findViewById(R.id.music_name1);
		Author1 = (TextView) findViewById(R.id.music_author1);
		im1 = (ImageView) findViewById(R.id.music_image1);

		text1.setScaleX(0.5f);
		Author1.setScaleX(0.5f);

		AllTimeText1 = (TextView) findViewById(R.id.music_Alltime1);
		timeText1 = (TextView) findViewById(R.id.music_time1);

		AllTimeText1.setScaleX(0.5f);
		timeText1.setScaleX(0.5f);

		seekBar1 = (SeekBar) findViewById(R.id.music_seek1);

		preBtn1 = (Button) findViewById(R.id.music_pre1);
		pauseBtn1 = (Button) findViewById(R.id.music_pause1);
		nextBtn1 = (Button) findViewById(R.id.music_next1);
		seekBar1.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		preBtn1.setOnFocusChangeListener(new OnPreChangeListener());
		pauseBtn1.setOnFocusChangeListener(new OnPauseChangeListener());
		nextBtn1.setOnFocusChangeListener(new OnNextChangeListener());
		muSicRE1 = (RelativeLayout) findViewById(R.id.music_relative);

	}

	private void setLeft() {
		// TODO Auto-generated method stub
		preBtn = (Button) findViewById(R.id.music_pre);
		pauseBtn = (Button) findViewById(R.id.music_pause);
		nextBtn = (Button) findViewById(R.id.music_next);

		preBtn.setOnFocusChangeListener(new OnPreChangeListener());
		pauseBtn.setOnFocusChangeListener(new OnPauseChangeListener());
		nextBtn.setOnFocusChangeListener(new OnNextChangeListener());

		text = (TextView) findViewById(R.id.music_name);
		Author = (TextView) findViewById(R.id.music_author);
		im = (ImageView) findViewById(R.id.music_image);
		AllTimeText = (TextView) findViewById(R.id.music_Alltime);
		timeText = (TextView) findViewById(R.id.music_time);
		muSicRE = (RelativeLayout) findViewById(R.id.music_relative);
		seekBar = (SeekBar) findViewById(R.id.music_seek);
		seekBar.setOnSeekBarChangeListener(new SeekBarChangeEvent());

		text.setScaleX(0.5f);
		Author.setScaleX(0.5f);
		AllTimeText.setScaleX(0.5f);
		timeText.setScaleX(0.5f);
	}

	public void onPauseClick(View view) {

		if (player.getIsPlaying()) {

			player.pause();
			pauseBtn.setBackgroundResource(R.drawable.music_stopbtnselect);
			pauseBtn1.setBackgroundResource(R.drawable.music_stopbtnselect);
		} else {
			player.start();
			pauseBtn.setBackgroundResource(R.drawable.music_pausebtnselect);
			pauseBtn1.setBackgroundResource(R.drawable.music_pausebtnselect);
		}

	}

	public void onPreClick(View view) {

		if (player.getIsPlaying()) {
			player.CanleTimer();
			player.stop();
			player = new Player(seekBar, seekBar1, this, isLocal);
		}

		if (isLocal) {

			if (position-- <= 0) {
				position = data.size() - 1;
				setLocalData(position);
			} else {
				setLocalData(position);
			}

		} else {
			setData();
		}
	}

	public void onNextClick(View view) {

		next();
	}

	public void next() {
		if (player.getIsPlaying()) {
			player.CanleTimer();
			player.stop();

			player = null;
			player = new Player(seekBar, seekBar1, this, isLocal);
		}

		if (isLocal) {

			position++;
			Log.e("position", position + "");
			if (position >= data.size()) {
				position = 0;
				setLocalData(position);
			} else {
				setLocalData(position);
			}

		} else {
			setData();
		}

	}

	public void onCloseClick(View view) {
		if (player.getIsPlaying()) {
			player.CanleTimer();
			player.stop();

		}

		finish();

	}

	class OnPreChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) preBtn.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				preBtn.setLayoutParams(params);
				preBtn.setSelected(true);

				preBtn1.setSelected(true);

			} else {

				LayoutParams params = (LayoutParams) preBtn.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				preBtn.setLayoutParams(params);
				preBtn.setSelected(false);

				preBtn1.setSelected(false);

			}

		}

	}

	class OnNextChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) nextBtn.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				nextBtn.setLayoutParams(params);
				nextBtn.setSelected(true);
				nextBtn1.setSelected(true);
			} else {
				nextBtn.setSelected(false);

				nextBtn1.setSelected(false);
				LayoutParams params = (LayoutParams) nextBtn.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				nextBtn.setLayoutParams(params);
			}

		}

	}

	class OnPauseChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {
				LayoutParams params = (LayoutParams) pauseBtn.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				pauseBtn.setLayoutParams(params);
				pauseBtn.setSelected(true);

				pauseBtn1.setSelected(true);

			} else {
				pauseBtn.setSelected(false);

				pauseBtn1.setSelected(false);
				LayoutParams params = (LayoutParams) pauseBtn.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				pauseBtn.setLayoutParams(params);
			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.CanleTimer();
		player.stop();

	}

	public void setLocalData(int position2) {

		// url=getIntent().getStringExtra("url");
		// musicTitle=getIntent().getStringExtra("name");
		// musicAuthor=getIntent().getStringExtra("author");
		//

		url = data.get(position2).getPlay_url();
		musicTitle = data.get(position2).getName();
		musicAuthor = data.get(position2).getAuthor();

		player.playUrl(url);
		player.play();

		text.setText(musicTitle);
		Author.setText(musicAuthor);
		// imageLoader.displayImage(picString, im);
		player.setPlayTimeTextView(timeText, timeText1, AllTimeText1,
				AllTimeText);

		text1.setText(musicTitle);
		Author1.setText(musicAuthor);
		// imageLoader.displayImage(picString, im1);

	}

	public void setData() {
		// showLoading();
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				// hideLoading();
				player.playUrl(result);
				player.play();

				text.setText(musicTitle);
				Author.setText(musicAuthor);
				imageLoader.displayImage(picString, im);
				player.setPlayTimeTextView(timeText, timeText1, AllTimeText1,
						AllTimeText);

				text1.setText(musicTitle);
				Author1.setText(musicAuthor);
				imageLoader.displayImage(picString, im1);

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				MusicApi api = new MusicApi();

				data = api.getMusicList();
				for (int i = 0; i < data.size(); i++) {

					url = data.get(i).getPlay_url();
					musicAuthor = data.get(i).getAuthor();
					musicTitle = data.get(i).getName();
					picString = data.get(i).getImage();
					Log.e("url", url);
				}

				return url;
			}
		}.execute("");

	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) // onReceive函数不能做耗时的事情，参考值：10s以内
		{

			String action = intent.getAction();
			Log.e("收到", "isLocal");
			if (action.equals("next")) {
				player.CanleTimer();
				player.stop();
				player = new Player(seekBar, seekBar1, MusicActivity.this,
						isLocal);
				setData();
			} else {

				next();

			}
		}
	};

	/**
	 * 进度条监听
	 * 
	 * @author lijiang
	 * 
	 */
	class SeekBarChangeEvent implements OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			this.progress = progress * player.mediaPlayer.getDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			player.mediaPlayer.seekTo(progress);
		}

	}

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
