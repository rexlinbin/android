package com.bccv.boxcomic.tool;


import com.bccv.boxcomic.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class MyGifView extends View {
	private long movieStart;
	private Movie movie;
	public MyGifView(Context context) {
		super(context);
		movie = Movie.decodeStream(getResources().openRawResource(
				R.drawable.loading_center));
	}

	public MyGifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		movie = Movie.decodeStream(getResources().openRawResource(
				R.drawable.loading_center));
	}

	public MyGifView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		movie = Movie.decodeStream(getResources().openRawResource(
				R.drawable.loading_center));
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		long curTime = android.os.SystemClock.uptimeMillis();
		// 第一次播放
		if (movieStart == 0) {
			movieStart = curTime;
		}
		if (movie != null) {
			int duraction = movie.duration();
			int relTime = (int) ((curTime - movieStart) % duraction);
			movie.setTime(relTime);
			movie.draw(canvas, 0, 0);
			// 强制重绘
			invalidate();
		}
		super.onDraw(canvas);
	}
}
