 package com.bccv.meitu.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
		 
 
/** 
 * 截取圆形图片工具类
 * 
 * @author JasonZue
 * @since 2014-4-30
 */

public class RoundImageUtil {

	
	private static final String TAG = "RoundImageUtil";

	/** 
     * 转换图片成圆形 
     *  
     * @param bitmap 
     *            传入Bitmap对象 
     * @return 
     */  
    public static Bitmap toRoundBitmap(Bitmap bitmap) {  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        float roundPx;  
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;  
        if (width <= height) {  
            roundPx = width / 2;  
            left = 0;  
            top = 0;  
            right = width;  
            bottom = width;  
            height = width;  
            dst_left = 0;  
            dst_top = 0;  
            dst_right = width;  
            dst_bottom = width;  
        } else {  
            roundPx = height / 2;  
            float clip = (width - height) / 2;  
            left = clip;  
            right = width - clip;  
            top = 0;  
            bottom = height;  
            width = height;  
            dst_left = 0;  
            dst_top = 0;  
            dst_right = height;  
            dst_bottom = height;  
        }  
  
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
  
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);  
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);  
        @SuppressWarnings("unused")
		final RectF rectF = new RectF(dst);  
  
        paint.setAntiAlias(true);// 设置画笔无锯齿  
  
        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas  
        paint.setColor(color);  
  
        // 以下有两种方法画圆,drawRounRect和drawCircle  
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。  
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);  
  
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452  
        canvas.drawBitmap(bitmap, src, dst, paint); //以Mode.SRC_IN模式合并bitmap和已经draw了的Circle  
          
        return output;  
    }  
    
    /**
	 * 圆角方形图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerSquareBitmap(Bitmap bitmap) {
		try {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			// 正方形长度
			int squareLength;
			Bitmap output;
			// 判断是否裁剪
			if (height != width) {
				squareLength = (height > width) ? width : height;
				bitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2,
						squareLength, squareLength);
			}
			output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
					Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = 15;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}
    
    /** 
     * 从资源文件中获取bitmap
     * 
     * @param act
     * @param resId
     * @return 
     * @return Bitmap
     * @throws 
     */
    public static Bitmap getBitmapFromResources(Activity act, int resId) {
    	Resources res = act.getResources();
    	return BitmapFactory.decodeResource(res, resId);
    	}
}

