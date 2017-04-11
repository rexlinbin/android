package com.bccv.meitu.view;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.Scroller;

import com.bccv.meitu.R;

@SuppressWarnings("deprecation")
public class SwipeBackLayout extends FrameLayout {
	private static final String TAG = SwipeBackLayout.class.getSimpleName();
	private View mContentView;
	private int mTouchSlop;
	private int downX;
	private int downY;
	private int tempX;
	private Scroller mScroller;
	private int viewWidth;
	private boolean isSilding;
	private boolean isFinish;
	private Drawable mShadowDrawable;
	private Activity mActivity;
	private List<ViewPager> mViewPagers = new LinkedList<ViewPager>();
	private List<Gallery> mGallerys = new LinkedList<Gallery>();
//	private List<MyImageView> mImageViews = new LinkedList<MyImageView>();
	
	public SwipeBackLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeBackLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//获取使控件移动的最小距离
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mScroller = new Scroller(context);

		mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
	}
	
	
	public void attachToActivity(Activity activity) {
		mActivity = activity;
		TypedArray a = activity.getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.windowBackground });
		int background = a.getResourceId(0, 0);
		a.recycle();
		//在Activity中获得根视图
		ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		decorChild.setBackgroundResource(background);
		decor.removeView(decorChild);
		addView(decorChild);
		setContentView(decorChild);
		decor.addView(this);
	}

	private void setContentView(View decorChild) {
		mContentView = (View) decorChild.getParent();
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		Log.e(TAG, "mViewPager size = " + mViewPagers.size());
//		ViewPager mViewPager = getTouchViewPager(mViewPagers, ev);
//		Log.e(TAG, "mViewPager = " + mViewPager);
//		if(mViewPager != null && mViewPager.getCurrentItem() != 0){
//			return super.onInterceptTouchEvent(ev);
//		}
		
		if(mViewPagers != null && mViewPagers.size() > 0){
//			Log.e(TAG, "come in this method");
			ViewPager mViewPager2 = mViewPagers.get(0);
			int[] location = new int[2];
			findViewById(R.id.pro_line_right).getLocationOnScreen(location);
//			Log.e(TAG, "ev.getY() = " + ev.getY() + "\r\n "+"findViewById(R.id.pro_line_right).getY();"
//			+location[1]);
			if(ev.getY() > location[1] && 
					mViewPager2 != null && mViewPager2.getCurrentItem() != 0){
				return super.onInterceptTouchEvent(ev);//相当于返回false,向子view的dispatchTouchEvent分发消息.
			}
		}
		
		if (mGallerys != null && mGallerys.size() > 0) {
			Gallery mGallery = mGallerys.get(0);
			if(mGallery != null && mGallery.getFirstVisiblePosition() != 0){
				return super.onInterceptTouchEvent(ev);
			}else{
				View view = mGallery.getSelectedView();
				if (view instanceof MyImageView) {
					MyImageView imageView = (MyImageView) view;
//					Log.e("!!!!!!!!!!!", "mImage.getScale() : "+imageView.getScale() +
//							"\r\n "+"mImage.getMiniZoom() : "+imageView.getMiniZoom());
					if (imageView.getScale() > imageView.getMiniZoom()) {
//						imageView.zoomTo(imageView.getMiniZoom());
						return super.onInterceptTouchEvent(ev);
					} 
//					else {
//						imageView.zoomTo(imageView.getMaxZoom());
//					}
				}
			}
		}
//		Log.e(TAG, "mImageView size : "+mImageViews.size());
//		if (mImageViews != null && mImageViews.size() > 0) {
//			MyImageView mImage = mImageViews.get(0);
////			Log.e(TAG, "mImage.getScale() : "+mImage.getScale() + "\r\n "+"mImage.getMiniZoom() : "+mImage.getMiniZoom());
//			if(mImage != null && mImage.getScale() > mImage.getMiniZoom()){
//				return super.onInterceptTouchEvent(ev);
//			}
//		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = tempX = (int) ev.getRawX();
			downY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getRawX();
			if (moveX - downX > mTouchSlop//判断当前X反向滑动的距离是否超过最小滑动距离
					&& Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {//判断当前Y反向滑动的距离是否超过最小滑动距离
				//如果X方向超过滑动距离,而Y方向没有超过,则是横向滑动事件.用来解决listView和scrollView的滑动事件.Math.abs()是返回绝对值.
				//如果确定是右划回退,则截断滑动事件(消费)
				return true;
			}
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) event.getRawX();
			int deltaX = tempX - moveX;
			tempX = moveX;
			if (moveX - downX > mTouchSlop
					&& Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
				isSilding = true;
			}

			if (moveX - downX >= 0 && isSilding) {
				mContentView.scrollBy(deltaX, 0);
			}
			break;
		case MotionEvent.ACTION_UP:
			isSilding = false;
			if (mContentView.getScrollX() <= -viewWidth / 2) {
				isFinish = true;
				scrollRight();
			} else {
				scrollOrigin();
				isFinish = false;
			}
			break;
		}

		return true;
	}
	
	private void getAlLViewPager(List<ViewPager> mViewPagers, ViewGroup parent){
		int childCount = parent.getChildCount();
		for(int i=0; i<childCount; i++){
			View child = parent.getChildAt(i);
			if(child instanceof ViewPager){
				mViewPagers.add((ViewPager)child);
			}else if(child instanceof ViewGroup){
				getAlLViewPager(mViewPagers, (ViewGroup)child);
			}
		}
	}
	
	private void getAllGallery(List<Gallery> mGallerys, ViewGroup parent){
		int childCount = parent.getChildCount();
		for(int i=0; i<childCount; i++){
			View child = parent.getChildAt(i);
			if(child instanceof Gallery){
				mGallerys.add((Gallery)child);
			}else if(child instanceof ViewGroup){
				getAllGallery(mGallerys, (ViewGroup)child);
			}
		}
	}
//	@SuppressWarnings("deprecation")
//	private void getAllMyImageView(List<MyImageView> mImageViews, ViewGroup parent){
//		int childCount = parent.getChildCount();
//		for(int i=0; i<childCount; i++){
//			View child = parent.getChildAt(i);
//			if(child instanceof MyImageView){
//				mImageViews.add((MyImageView)child);
//			}else if(child instanceof ViewGroup){
//				getAllMyImageView(mImageViews,(ViewGroup)child);
//			}
//		}
//	}
	
	private ViewPager getTouchViewPager(List<ViewPager> mViewPagers, MotionEvent ev){
		if(mViewPagers == null || mViewPagers.size() == 0){
			return null;
		}
		Rect mRect = new Rect();
		for(ViewPager v : mViewPagers){
			v.getHitRect(mRect);
			
			if(mRect.contains((int)ev.getX(), (int)ev.getY())){
				return v;
			}
		}
		return null;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			viewWidth = this.getWidth();
			
			getAlLViewPager(mViewPagers, this);
			getAllGallery(mGallerys, this);
//			System.out.println("ViewPager size = " + mViewPagers.size());
//			Log.e(TAG, "ViewPager size = " + mGallerys.size());
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mShadowDrawable != null && mContentView != null) {

			int left = mContentView.getLeft()
					- mShadowDrawable.getIntrinsicWidth();
			int right = left + mShadowDrawable.getIntrinsicWidth();
			int top = mContentView.getTop();
			int bottom = mContentView.getBottom();

			mShadowDrawable.setBounds(left, top, right, bottom);
			mShadowDrawable.draw(canvas);
		}

	}

	/**
	 * 向右滑动
	 */
	private void scrollRight() {
		final int delta = (viewWidth + mContentView.getScrollX());
		mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
				Math.abs(delta));
		postInvalidate();
	}
	/**
	 * 滑回起点
	 */
	private void scrollOrigin() {
		int delta = mContentView.getScrollX();
		mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
				Math.abs(delta));
		postInvalidate();
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();

			if (mScroller.isFinished() && isFinish) {
				mActivity.finish();
			}
		}
	}


}
