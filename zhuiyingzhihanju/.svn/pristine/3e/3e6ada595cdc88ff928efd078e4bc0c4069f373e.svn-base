package com.utils.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class ScrollRefresh extends SwipeRefreshLayout {
	private CanChildScrollUpCallback mCanChildScrollUpCallback;

	public interface CanChildScrollUpCallback {
		boolean canSwipeRefreshChildScrollUp();
	}

	public ScrollRefresh(Context context) {
		super(context, null);
	}
	public ScrollRefresh(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setCanChildScrollUpCallback(CanChildScrollUpCallback canChildScrollUpCallback) {
		mCanChildScrollUpCallback = canChildScrollUpCallback;
	}

	@Override
	public boolean canChildScrollUp() {
		if (mCanChildScrollUpCallback != null) {
			return mCanChildScrollUpCallback.canSwipeRefreshChildScrollUp();
		}
		return super.canChildScrollUp();
	}

}
