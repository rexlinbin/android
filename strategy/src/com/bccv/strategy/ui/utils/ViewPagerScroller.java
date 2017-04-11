package com.bccv.strategy.ui.utils;

import java.lang.reflect.Field;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.bccv.strategy.ui.view.HomePageViewPager;

public class ViewPagerScroller extends Scroller {
    private int mScrollDuration = 600;             // 滑动速度
    
    private HomePageViewPager mViewPager;
    
    /**
     * 设置速度速度
     * @param duration
     */
    public void setScrollDuration(int duration){
        this.mScrollDuration = duration;
    }
     
    public ViewPagerScroller(Context context) {
        super(context);
    }
 
    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }
 
    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
 
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
    	
    	
    	if(mViewPager.setItemByUser){
    		int width = mViewPager.getWidth();
    		int factor = (int)(Math.abs(((float)dx)/((float)width))+0.5f);
    		int mDuration = (int) (mScrollDuration*((factor-1)*0.5f + 1));
    		if(mDuration>1500){
    			mDuration = 1500;
    		}
    		
     	    ObjectAnimator anim = ObjectAnimator//  
    	            .ofFloat(mViewPager, "zhy", 1.0F,  0.0F)//  
    	            .setDuration(mDuration);//  
    	    anim.addUpdateListener(new AnimatorUpdateListener() {  
    	        @Override  
    	        public void onAnimationUpdate(ValueAnimator animation) {  
    	            float cVal = (Float) animation.getAnimatedValue();  
    	            if(cVal<0.1f){
    	            	
    	            	
    	            }
    	        }  
    	    }); 
    		
    		super.startScroll(startX, startY, dx, dy, mDuration);
    	    anim.start();
    		
    	}else{
    		super.startScroll(startX, startY, dx, dy, duration);
    	}
    	
    }
 
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy);
    }
 
    @Override
    public boolean computeScrollOffset() {
    	boolean returnValue = super.computeScrollOffset();
    	if(isFinished()){
//    		System.out.println("##################  4444444444");
    	}
    	return returnValue;
    }
    
    @Override
    public void abortAnimation() {
    	super.abortAnimation();
    	if(isFinished()){
//    		System.out.println("##################  555555555");
    	}
    }
     
     
    public void initViewPagerScroll(ViewPager viewPager) {
    	mViewPager = (HomePageViewPager) viewPager;
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
            
            mViewPager.mViewPagerScroller = this;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };
}
