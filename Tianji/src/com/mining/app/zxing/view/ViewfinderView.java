/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mining.app.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.bccv.tianji.R;
import com.google.zxing.ResultPoint;
import com.mining.app.zxing.camera.CameraManager;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public final class ViewfinderView extends View {
	private static final String TAG = "log";
	/**
	 * 鍒烽敓閾版枻鎷烽敓鏂ゆ嫹閿熺粸鎲嬫嫹閿燂拷
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;

	/**
	 * 閿熶茎闈╂嫹閿熸枻鎷疯壊閿熺瑙掕鎷峰簲閿熶茎绛规嫹閿熸枻鎷�
	 */
	private int ScreenRate;
	
	/**
	 * 閿熶茎闈╂嫹閿熸枻鎷疯壊閿熺瑙掕鎷峰簲閿熶茎鍖℃嫹閿燂拷
	 */
	private static final int CORNER_WIDTH = 10;
	/**
	 * 鎵敓鏂ゆ嫹閿熸枻鎷锋閿熸枻鎷峰睉閿熸枻鎷峰彥鐩敓鏂ゆ嫹
	 */
	private static final int MIDDLE_LINE_WIDTH = 6;
	
	/**
	 * 鎵敓鏂ゆ嫹閿熸枻鎷锋閿熸枻鎷峰睉閿熸枻鎷峰彥閿熸枻鎷烽敓缂搭煉鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鎻殑纭锋嫹闅�
	 */
	private static final int MIDDLE_LINE_PADDING = 5;
	
	/**
	 * 閿熷彨纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋瘡閿熸枻鎷峰埛閿熸枻鎷烽敓鐙¤鎷烽敓渚ユ拝鎷烽敓鏂ゆ嫹
	 */
	private static final int SPEEN_DISTANCE = 5;
	
	/**
	 * 閿熻浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹骞曢敓鏉拌鎷�
	 */
	private static float density;
	/**
	 * 閿熸枻鎷烽敓鏂ゆ嫹閿熷彨锟�
	 */
	private static final int TEXT_SIZE = 16;
	/**
	 * 閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓缂搭煉鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鏈ㄩ敓鏂ゆ嫹閿燂拷
	 */
	private static final int TEXT_PADDING_TOP = 30;
	
	/**
	 * 閿熸枻鎷烽敓缁炶鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿燂拷
	 */
	private Paint paint;
	
	/**
	 * 閿熷彨闂存粦閿熸枻鎷烽敓绔鎷烽敓绛嬮《閿熸枻鎷蜂綅閿熸枻鎷�
	 */
	private int slideTop;
	
	/**
	 * 閿熷彨闂存粦閿熸枻鎷烽敓绔鎷烽敓鏂ゆ嫹閿ラ敓杞夸紮鎷烽敓锟�
	 */
	private int slideBottom;
	
	/**
	 * 閿熸枻鎷锋壂閿熸枻鎷蜂憨閿熻娇顒婃嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熺煫浼欐嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏉帮綇鎷烽敓鏂ゆ嫹鏃堕敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹
	 */
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	
	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	boolean isFirst;
	
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		density = context.getResources().getDisplayMetrics().density;
		//閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷疯浆閿熸枻鎷烽敓鏂ゆ嫹dp
		ScreenRate = (int)(20 * density);

		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
	}

	@Override
	public void onDraw(Canvas canvas) {
		//閿熷彨纭锋嫹閿熺即顭掓嫹閿熸枻鎷烽敓鏂ゆ嫹瑕侀敓鐫潻鎷锋壂閿熸枻鎷烽敓渚ヨ揪鎷峰皬閿熸枻鎷峰幓CameraManager閿熸枻鎷烽敓鏂ゆ嫹閿熺潾闈╂嫹
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		
		//閿熸枻鎷峰閿熸枻鎷烽敓鍙》鎷烽敓绔紮鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熻緝杈圭尨鎷烽敓鏂ゆ嫹閿熼摪鎲嬫嫹
		if(!isFirst){
			isFirst = true;
			slideTop = frame.top;
			slideBottom = frame.bottom;
		}
		
		//閿熸枻鎷峰彇閿熸枻鎷峰箷閿熶茎鍖℃嫹閫忛敓锟�
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		
		//閿熸枻鎷烽敓鏂ゆ嫹鎵敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹褰遍敓鏂ゆ嫹閿熻锝忔嫹閿熸枻鎷烽敓渚ラ潻鎷烽敓鏂ゆ嫹閿熻锝忔嫹鎵敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熻姤鍒伴敓鏂ゆ嫹骞曢敓鏂ゆ嫹閿熻姤锛屾壂閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鑺ュ埌閿熸枻鎷峰箷閿熸枻鎷烽敓鏂ゆ嫹
		//鎵敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷峰ù鏂ゆ嫹閿熶茎浼欐嫹閿熺锝忔嫹鎵敓鏂ゆ嫹閿熸枻鎷烽敓鎻竟纰夋嫹閿熸枻鎷峰箷閿熸彮鎲嬫嫹
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		
		

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			//閿熸枻鎷锋壂閿熸枻鎷烽敓鏂ゆ嫹閿熻緝鐨勮锝忔嫹閿熸澃鐧告嫹8閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
			paint.setColor(Color.GREEN);
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate,
					frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
					frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
					frame.right, frame.bottom, paint);

			
			//閿熸枻鎷烽敓鏂ゆ嫹閿熷彨纭锋嫹閿熸枻鎷烽敓锟�,姣忛敓鏂ゆ嫹鍒烽敓閾版枻鎷烽敓鑺ワ紝閿熷彨纭锋嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹璐敓绲奝EEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if(slideTop >= frame.bottom){
				slideTop = frame.top;
			}
			Rect lineRect = new Rect();  
            lineRect.left = frame.left;  
            lineRect.right = frame.right;  
            lineRect.top = slideTop;  
            lineRect.bottom = slideTop + 18;  
            canvas.drawBitmap(((BitmapDrawable)(getResources().getDrawable(R.drawable.qrcode_scan_line))).getBitmap(), null, lineRect, paint);  
			
			
			//閿熸枻鎷锋壂閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷�
			paint.setColor(Color.WHITE);    
			paint.setTextSize(TEXT_SIZE * density);    
			paint.setAlpha(0x40);    
			paint.setTypeface(Typeface.DEFAULT_BOLD);   
			String text = getResources().getString(R.string.scan_text);  
			float textWidth = paint.measureText(text);  
			  
			canvas.drawText(text, (width - textWidth)/2, (float) (frame.bottom + (float)TEXT_PADDING_TOP *density), paint);
			
			

			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}

			
			//鍙埛閿熸枻鎷锋壂閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鎹凤綇鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鎴嚖鎷烽敓鏂ゆ嫹鍒烽敓鏂ゆ嫹
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
			
		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
