package com.bccv.threedimensionalworld.tool;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;

public class MyGallery extends Gallery {

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGallery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		if (galleryListener != null) {
			galleryListener.onScroll(e1, e2, distanceX, distanceY);
		}
		
		return super.onScroll(e1, e2, distanceX, distanceY);
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (galleryListener != null) {
			galleryListener.onFling(e1, e2, velocityX, velocityY);
		}
		
		return super.onFling(e1, e2, velocityX, velocityY);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (galleryListener != null) {
			galleryListener.onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	 public void fixSelectedItemMetadata()
	  {
//	    View localView = getSelectedView();
//	    if (localView == null)
//	      return;
//	    localView.setSelected(true);
//	    localView.setFocusable(true);
	  }
	 protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
	  {
	    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
	    fixSelectedItemMetadata();
	  }
	
	public interface GalleryListener {

		void onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY);
		void onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY);
		void onKeyDown(int keyCode, KeyEvent event);
		
	}

	private GalleryListener galleryListener;

	/**
	 * 设置滚动监听 2014-12-7 下午3:59:51
	 * 
	 * @author DZC
	 * @return void
	 * @param listener
	 * @TODO
	 */
	public void setOnGalleyListener(GalleryListener listener) {
		this.galleryListener = listener;
	}

}
