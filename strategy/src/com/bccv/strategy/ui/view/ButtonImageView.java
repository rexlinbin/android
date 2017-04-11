package com.bccv.strategy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ButtonImageView extends ImageView {
	
	public ButtonImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ButtonImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ButtonImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setAlpha(0.96f);
			return true;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			setAlpha(1f);
			return true;
		default:
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
}
