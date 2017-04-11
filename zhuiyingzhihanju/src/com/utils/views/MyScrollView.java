package com.utils.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ScrollView;

@SuppressLint("NewApi")
public class MyScrollView extends ScrollView {
	private OnScrollListener onScrollListener;  

	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub

	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub

	}

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}
	
	 /** 
     * 设置滚动接口 
     * @param onScrollListener 
     */  
    public void setOnScrollListener(OnScrollListener onScrollListener) {  
        this.onScrollListener = onScrollListener;  
    }  
  
  
    
      
      
    @Override  
    public int computeVerticalScrollRange() {  
        return super.computeVerticalScrollRange();  
    }  
      
  
    @Override  
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {  
        super.onScrollChanged(l, t, oldl, oldt);  
        if(onScrollListener != null){  
            onScrollListener.onScroll(t);  
        }  
    }  
    
    /** 
     *  
     * 滚动的回调接口 
     *  
     * @author xiaanming 
     * 
     */  
    public interface OnScrollListener{  
        /** 
         * 回调方法， 返回MyScrollView滑动的Y方向距离 
         * @param scrollY 
         *              、 
         */  
        public void onScroll(int scrollY);  
    }  
      
      
  

	
	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {  
        //禁止scrollView内布局变化后自动滚动  
        return 0;  
    }
}
