package com.bccv.strategy.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

@SuppressWarnings("deprecation")
public class BackGroundView extends View {
	
	public static final String BACKGROUND_COLOR = "background_color";
	
	private LinearGradient gradinet;
	private Paint paint;
	private int width;
	private int height;
	private int start = 0XFFFF8080;
	private int end = 0XFF8080FF;
	
	//d35f53-db417a    385773-ab5046  613c56-385773  734d38-613c56 347a69-734d38 386371-347a69
	
	public BackGroundView(Context context) {
		super(context);
		getDisplay(context);
	}

	public BackGroundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getDisplay(context);
	}
	
	private void getDisplay(Context context) {
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
	}
	
	public void setGradient(int start,int end){
		this.start = start;
		this.end = end;
		gradinet = new LinearGradient(0, 0, width, height, start, end, Shader.TileMode.MIRROR);
		invalidate();
	}
	
	public int[] getGradientColor(){
		int[] colors = new int[]{start,end};
		return colors;
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (paint == null) {
			paint = new Paint();
		}
		if (gradinet == null) {
			gradinet = new LinearGradient(0, 0, width, height, start, end, Shader.TileMode.MIRROR);
		}
		paint.setShader(gradinet);
		canvas.drawRect(0, 0, width	, height, paint);
	}

}
