package com.bccv.bangyangapp.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

//	private boolean noScroll = true;
	 
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public MyViewPager(Context context) {
        super(context);
    }
 
    public void setNoScroll(boolean noScroll) {
//        this.noScroll = noScroll;
    }
 
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }
 
    
    float x = 0;
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = arg0.getX();
			break;
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_UP:
			float xUp = arg0.getX();
			if (xUp - x < 0) {
				if (getCurrentItem() == getChildCount() -2) {
					return false;
				}else if (Math.abs(xUp - x) >= 300) {
					setCurrentItem(getCurrentItem()+1);
					return false;
				}
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(arg0);
    }
    
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//        if (!noScroll)
//            return false;
//        else
//            return super.onInterceptTouchEvent(arg0);
//    }
 
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
 
    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}
