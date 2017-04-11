package com.utils.views;

import com.tencent.smtt.sdk.WebView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

@SuppressLint("ClickableViewAccessibility")
public class MYWebView extends WebView {

	public MYWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	  public MYWebView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	      }

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
	    Log.d("DEBUG ::: ", "in onTouchEvent() ---> ACTION_DOWN");

	    int temp_ScrollY = getScrollY();

	    int temp_ScrollX = getScrollX();
	    if(temp_ScrollX > 0) {
	        Log.d("RESET X COORD: WAS STARTING AT ", Integer.toString(temp_ScrollX));
	        setScrollX(0);
	    }
	    scrollTo(getScrollX(), getScrollY() + 1);
	    scrollTo(getScrollX(), temp_ScrollY);
	    Log.d("  DEBUG COORDS::: ", "X=" + Integer.toString(getScrollX()) + " Y=" + Integer.toString(getScrollY()) + " Y2=" + Integer.toString(temp_ScrollY));
	}
		return super.onTouchEvent(event);
}

}