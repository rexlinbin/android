package com.bccv.strategy.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class AutoRotateBitmapDisplayer implements BitmapDisplayer {

   protected final int margin;
	
   public AutoRotateBitmapDisplayer() {
        this(0);
    }

    public AutoRotateBitmapDisplayer(int margin) {
        this.margin = margin;
    }
	
	@Override
	public void display(Bitmap bitmap, ImageAware imageAware,
			LoadedFrom loadedFrom) {
		 if (!(imageAware instanceof ImageViewAware)) {
	            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
	        }
	        imageAware.setImageBitmap(getRotateBitmap(bitmap));
	}
	
    private Bitmap getRotateBitmap(Bitmap bitmap){
		if(bitmap.getWidth()>bitmap.getHeight()){
			// 定义矩阵对象  
			Matrix matrix = new Matrix();  
			// 缩放原图  
			matrix.postScale(1f, 1f); 
			// 向右旋转90度，参数为正则向右旋转  
			matrix.postRotate(90);  
			Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),  
					matrix, true);  
			return dstbmp;
		}else{
			return bitmap;
		}
    }

}
