package com.bccv.strategy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class MyScrollView extends HorizontalScrollView {

	private OnScrollChangedListener listener;
	
	public interface OnScrollChangedListener{
		public void onScrollChanged(int l, int t, int oldl, int oldt);
	}
	
	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context) {
		super(context);
	}
	
	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		this.listener = listener;	
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (listener != null) {
			listener.onScrollChanged(l, t, oldl, oldt);
		}
	}
}
