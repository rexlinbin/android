package com.bccv.bangyangapp.ui.view;

import java.util.ArrayList;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.ui.adapter.IRoundTabAdapter;
import com.bccv.bangyangapp.utils.ScreenUtil;

public class RoundTab extends ViewGroup {

	private static double ANGLE_STATUS_LEFT;

	private static double ANGLE_STATUS_1;
	private static double ANGLE_STATUS_2;
	private static double ANGLE_STATUS_3;
	private static double ANGLE_STATUS_4;
	private static double ANGLE_STATUS_5;

	private static double ANGLE_STATUS_RIGHT;

	public static final double W = 360.0; // 720屏幕宽度的一半
	public static final double D = 46.0; // 720屏幕弧形高度

	public static final long ANIM_TOTLE_DURATION = 130l;// 动画执行总时间

	private double SCREEN_W;// 屏幕宽度
	
	private Paint paint;
//	private Paint tagPaint;

	private ArrayList<View> removeViewArray; // 移除屏幕的view
	private ArrayList<DataView> showViewArray; // 当前显示的view
	private ArrayList<View> showViewTagArray; // 当前显示的view

	private IRoundTabAdapter mAdapter;

	protected int VISIBLE_VIEWS = 5;
	
	private PaintFlagsDrawFilter mDrawFilter;

	private int paddingLeft;
	private int paddingRight;
	private int paddingTop;
	private int paddingBottom;

	private int firstIndext = -3; // 第一个孩子的下标
	private int endIndext = -4; // 最后一个孩子的下标

	int widthSize; // 控件原本宽
	int heightSize;// 控件原本高

	int avaiblableWidth; // 控件可见宽
	int avaiblableHeight;// 控件可见高

	int itemWidth; // tab标签的宽度
	int itemHeight;// tab标签的高度
	
	int tagWidth; // tag标签的宽度
	int tagHeight;// tag标签的高度

	private double w; //  控件显示的宽度的一半
	private double D_Y;// 圆顶距控件顶部的距离
	private double r;//   圆的半径
	private double d;//	    拱形高
	
	private double angle;

	private int currentPosition = 0;

	private OnItemClickListener onItemClickListener;
	
//	private Bitmap viewTag,selectedViewTag;

	public RoundTab(Context context) {
		super(context);
		initResouce();
		init();
		// TODO Auto-generated constructor stub
	}

	public RoundTab(Context context, AttributeSet attrs) {
		super(context, attrs);
		initResouce();
		init();
		// TODO Auto-generated constructor stub
	}

	public RoundTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initResouce();
		init();
		// TODO Auto-generated constructor stub
	}

	private int dotSize;
	private int dotOff;
	
	private void init() {
		
		dotSize = ScreenUtil.dp2px(getContext(), 10);
		dotOff = ScreenUtil.dp2px(getContext(), 1);
				
		removeAllViews();
		setWillNotDraw(false);
		setClickable(true);
		
		movePercent = 0.0;
		
		firstIndext = -3; // 第一个孩子的下标
		endIndext = -4; 
		currentPosition = 0;
		
		
		if (showViewArray == null) {
			showViewArray = new ArrayList<DataView>();
		} else {
			showViewArray.clear();
		}
		if (removeViewArray == null) {
			removeViewArray = new ArrayList<View>();
		} else {
			removeViewArray.clear();
		}
		if (showViewTagArray == null) {
			showViewTagArray = new ArrayList<View>();
		} else {
			showViewTagArray.clear();
		}

		for (int i = 0; i < VISIBLE_VIEWS + 2 && mAdapter != null; ++i) {

			View convertView = null;
			if (removeViewArray.size() > 0) {
				convertView = removeViewArray.remove(0);
			}
			++endIndext;
			View view = mAdapter.getView(getActuallyPosition(endIndext), convertView, this);
			view.setOnClickListener(new MyClickListener());
			DataView dataView = new DataView(view, endIndext);
			showViewArray.add(dataView);
			addView(view);
			
//			View tag = View.inflate(getContext(), R.layout.item_view_tag, null);
			View tag = new View(getContext());
			tag.setBackgroundResource(R.drawable.dot);
			LayoutParams layoutParams = new LayoutParams(dotSize, dotSize);
			tag.setLayoutParams(layoutParams);
			
			showViewTagArray.add(tag);
			addView(tag);
			
		}
		
		if(showViewTagArray.size()>0){
			View tag = showViewTagArray.get(showViewTagArray.size()/2);
			tag.setBackgroundResource(R.drawable.dot_light);
		}
		requestLayout();

	}

	private void initResouce(){
		paint = new Paint();
		// 设置画笔颜色为红色
		paint.setColor(0X32ffffff);
        paint.setAntiAlias(true); //消除锯齿  
        paint.setStyle(Paint.Style.STROKE); //绘制空心圆  
        paint.setStrokeWidth(3.0f);
        
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
        		| Paint.FILTER_BITMAP_FLAG);
        
//        tagPaint = new Paint();
//        // 设置画笔颜色为红色
//        tagPaint.setColor(Color.WHITE);
//        tagPaint.setAntiAlias(true); //消除锯齿  
//        tagPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        
        
        
//        Bitmap decodeResource = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_launcher);
//        
//        viewTag = Bitmap.createScaledBitmap(decodeResource, 30, 30, true);
//        selectedViewTag = viewTag;
//        
//        decodeResource.recycle();
        
//        viewTag,selectedViewTag
        
//        BitmapFactory.decodeResource(getContext().getResources(), id)
        
	}
	
	public void setAdapter(IRoundTabAdapter mAdapter) {
		this.mAdapter = mAdapter;
		
		animList.clear();
		if(isAnimRuning){
			anim.cancel();
			isAnimRuning = false;
		}
		init();
	}

	public IRoundTabAdapter getAdapter() {
		return mAdapter;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	boolean isff = true; // 是否是第一次测量

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (mAdapter == null || showViewArray.size() <= 0) {
			return;
		}

		if (isff) {

			SCREEN_W = getScreenWidthPix(getContext());

			paddingLeft = getPaddingLeft();
			paddingRight = getPaddingRight();
			paddingTop = getPaddingTop();
			paddingBottom = getPaddingBottom();

			widthSize = MeasureSpec.getSize(widthMeasureSpec);
			heightSize = MeasureSpec.getSize(heightMeasureSpec);

			// 控件高度
			avaiblableHeight = heightSize - paddingTop - paddingBottom;
			avaiblableWidth = widthSize - paddingLeft - paddingRight;
			measureLayoutValues();

			isff = false;

			// postDelayed(new Runnable() {
			//
			// @Override
			// public void run() {
			// // TODO Auto-generated method stub
			//
			// // rotateyAnimRun(RoundTab.this);
			// moveToTarget(1);
			// // moveToTarget(0);
			// // moveToTarget(-2);
			// }
			// }, 1000);
			// //

		}

		for (int i = 0; i < showViewArray.size(); i++) {
			View child = showViewArray.get(i).view;
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
			itemWidth = child.getMeasuredWidth();
			itemHeight = child.getMeasuredHeight();
		}
		
		for (int i = 0; i < showViewTagArray.size(); i++) {
			View tag = showViewTagArray.get(i);
			measureChild(tag, widthMeasureSpec, heightMeasureSpec);
			tagWidth = tag.getMeasuredWidth();
			tagHeight = tag.getMeasuredHeight();
		}

	}

	private void measureLayoutValues() {

		angle = Math.asin((2 * D * W) / (Math.pow(W, 2) + Math.pow(D, 2)));

		ANGLE_STATUS_LEFT = Math.PI / 2.0 + (angle / 10 * 12);

		ANGLE_STATUS_1 = Math.PI / 2.0 + (angle / 10 * 8);
		ANGLE_STATUS_2 = Math.PI / 2.0 + (angle / 10 * 4);
		ANGLE_STATUS_3 = Math.PI / 2.0;
		ANGLE_STATUS_4 = Math.PI / 2.0 - (angle / 10 * 4);
		ANGLE_STATUS_5 = Math.PI / 2.0 - (angle / 10 * 8);

		ANGLE_STATUS_RIGHT = Math.PI / 2.0 - (angle / 10 * 12);

		w = ((double) avaiblableWidth) / 2.0f;
		r = ((Math.pow(W, 2) + Math.pow(D, 2)) * w) / (2 * D * W);
		
		d = r - Math.sqrt(Math.pow(r, 2)-Math.pow(w, 2));
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
		D_Y = itemHeight / 5.0 *2.0;
		mearsureChildViewsLayout();
	}

	private void mearsureChildViewsLayout() {
		if (mAdapter == null || mAdapter.getCount() <= 0
				|| showViewArray.size() <= 0) {
			return;
		}
		for (int i = 0; i < showViewArray.size(); i++) {
			double angle = mearsureAngle(i);
			View child = showViewArray.get(i).view;
			setItemLayout(child, angle);
			showViewArray.get(i).angle = angle;
			
			View tag = showViewTagArray.get(i);
			setTagLayout(tag, angle);
//			
		}
	}

	public double movePercent = 0.0;// 移动角度百分比

	private double mearsureAngle(int Indext) {
		double angle = 0;

		if (movePercent <= 0) {

			switch (Indext) {
			case 0:
				angle = ANGLE_STATUS_LEFT
						+ (movePercent * (ANGLE_STATUS_LEFT - ANGLE_STATUS_1));
				break;
			case 1:
				angle = ANGLE_STATUS_1
						+ (movePercent * (ANGLE_STATUS_1 - ANGLE_STATUS_2));
				break;
			case 2:
				angle = ANGLE_STATUS_2
						+ (movePercent * (ANGLE_STATUS_2 - ANGLE_STATUS_3));
				break;
			case 3:
				angle = ANGLE_STATUS_3
						+ (movePercent * (ANGLE_STATUS_3 - ANGLE_STATUS_4));
				break;
			case 4:
				angle = ANGLE_STATUS_4
						+ (movePercent * (ANGLE_STATUS_4 - ANGLE_STATUS_5));
				break;
			case 5:
				angle = ANGLE_STATUS_5
						+ (movePercent * (ANGLE_STATUS_5 - ANGLE_STATUS_RIGHT));
				break;
			case 6:
				angle = ANGLE_STATUS_RIGHT
						+ (movePercent * (ANGLE_STATUS_RIGHT - 0));
				break;

			default:
				break;
			}
		} else {

			switch (Indext) {
			case 0:
				angle = ANGLE_STATUS_LEFT
						+ (movePercent * (Math.PI - ANGLE_STATUS_LEFT));
				break;
			case 1:
				angle = ANGLE_STATUS_1
						+ (movePercent * (ANGLE_STATUS_LEFT - ANGLE_STATUS_1));
				break;
			case 2:
				angle = ANGLE_STATUS_2
						+ (movePercent * (ANGLE_STATUS_1 - ANGLE_STATUS_2));
				break;
			case 3:
				angle = ANGLE_STATUS_3
						+ (movePercent * (ANGLE_STATUS_2 - ANGLE_STATUS_3));
				break;
			case 4:
				angle = ANGLE_STATUS_4
						+ (movePercent * (ANGLE_STATUS_3 - ANGLE_STATUS_4));
				break;
			case 5:
				angle = ANGLE_STATUS_5
						+ (movePercent * (ANGLE_STATUS_4 - ANGLE_STATUS_5));
				break;
			case 6:
				angle = ANGLE_STATUS_RIGHT
						+ (movePercent * (ANGLE_STATUS_5 - ANGLE_STATUS_RIGHT));
				break;

			default:
				break;
			}
		}

		return angle;
	}

	private void setItemLayout(View child, double angle) {

		double x = SCREEN_W / 2.0 + r * Math.cos(angle);

		double y = r + D_Y - r * Math.sin(angle);

//		child.layout(0, 0, itemWidth, itemHeight);

		double translateX = x - itemWidth / 2.0;
//		double translateY = y + itemHeight / 2.0;
		double translateY = y;

		child.layout((int) translateX, (int) translateY,
				(int) (translateX + itemWidth), (int) (translateY + itemHeight));

		double alpha = 1 - Math.abs(angle - Math.PI / 2) / this.angle;

		child.setAlpha((float) alpha);

		// child.setTranslationX((float) translateX);
		// child.setTranslationY((float) translateY);
	}
	
	private void setTagLayout(View tag, double angle) {
		
		double x = SCREEN_W / 2.0 + r * Math.cos(angle);
		
		double y = r - r * Math.sin(angle);
		
		double translateX = x - tagWidth / 2.0;
		double translateY = y + (getMeasuredHeight() - getPaddingBottom() -d +3) - tagHeight/2.0;
		
		if(angle==ANGLE_STATUS_1 || angle == ANGLE_STATUS_5){
			translateY-=dotOff;
		}
		
		tag.layout((int) translateX, (int) translateY,
				(int) (translateX + tagWidth), (int) (translateY + tagHeight));
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.setDrawFilter(mDrawFilter);
		canvas.drawCircle((float)(SCREEN_W / 2.0),(float)(getMeasuredHeight() - getPaddingBottom() -d +3 + r), (float)r, paint);
//		drawChildViewsTagBitmap(canvas);
	}
	
	
//	private void drawChildViewsTagBitmap(Canvas canvas) {
//		if (mAdapter == null || mAdapter.getCount() <= 0
//				|| showViewArray.size() <= 0) {
//			return;
//		}
//		for (int i = 0; i < showViewArray.size(); i++) {
//			DataView dataView = showViewArray.get(i);
//			drawChildViewTag(dataView.angle,canvas);
//		}
//	}
//	
//	private void drawChildViewTag(double angle,Canvas canvas){
//		double x = SCREEN_W / 2.0 + r * Math.cos(angle);
//		double y = getMeasuredHeight() - getPaddingBottom() -d +3 + r - r * Math.sin(angle);
//		
//		canvas.drawBitmap(viewTag, (float)(x-viewTag.getWidth()/2.0), (float)(y-viewTag.getHeight()/2.0), tagPaint);
//	}

	
	/**
	 * 刷新数据  单签Item position
	 * @param currentPosition
	 */
	public synchronized void notifyDataSetChanged(int currentPosition){
		
		animList.clear();
		if(isAnimRuning){
			anim.cancel();
		}
		
		removeAllViews();
		
		this.currentPosition = currentPosition;
		movePercent = 0.0;
		
		firstIndext = currentPosition-3; // 第一个孩子的下标
		endIndext = currentPosition-4; 
		
		if (showViewArray == null) {
			showViewArray = new ArrayList<DataView>();
		} else {
			showViewArray.clear();
		}
		if (removeViewArray == null) {
			removeViewArray = new ArrayList<View>();
		} else {
			removeViewArray.clear();
		}
		if (showViewTagArray == null) {
			showViewTagArray = new ArrayList<View>();
		} else {
			showViewTagArray.clear();
		}
		
		for (int i = 0; i < VISIBLE_VIEWS + 2 && mAdapter != null; ++i) {

			View convertView = null;
			if (removeViewArray.size() > 0) {
				convertView = removeViewArray.remove(0);
			}
			++endIndext;
			View view = mAdapter.getView(getActuallyPosition(endIndext), convertView, this);
			view.setOnClickListener(new MyClickListener());
			DataView dataView = new DataView(view, endIndext);
			showViewArray.add(dataView);
			addView(view);
			
			
//			View tag = View.inflate(getContext(), R.layout.item_view_tag, null);
			View tag = new View(getContext());
			tag.setBackgroundResource(R.drawable.dot);
			LayoutParams layoutParams = new LayoutParams(dotSize, dotSize);
			tag.setLayoutParams(layoutParams);
			
			
			showViewTagArray.add(tag);
			addView(tag);
			
		}
		
		if(showViewTagArray.size()>0){
			View tag = showViewTagArray.get(showViewTagArray.size()/2);
			tag.setBackgroundResource(R.drawable.dot_light);
		}
		requestLayout();
		
		isAnimRuning = false;
		
	}
	
	
	boolean isAnimRuning = false;
	int targetPosition = 0;

	ArrayList<Integer> animList = new ArrayList<Integer>();

	public synchronized void moveToTarget(int position) {

		if (isAnimRuning) {
			animList.add(position);
			return;
		}

		if (position == currentPosition) {
			return;
		}
		
		View tag = showViewTagArray.get(showViewTagArray.size()/2);
		tag.setBackgroundResource(R.drawable.dot);

		isAnimRuning = true;

		targetPosition = position;

		int distancePosition = position - currentPosition;

		long duration = ANIM_TOTLE_DURATION / Math.abs(distancePosition);

		if (duration < 65l) {
			duration = 65l;
		}

		int sign = position - currentPosition > 0 ? 1 : -1;

		rotateyAnimRun(this, duration, sign);

	}

	private void moveToTargetIgnoreAnimRuning(int position) {
		if (position == currentPosition) {
			if (animList.size() > 0) {
				moveToTargetIgnoreAnimRuning(animList.remove(0));
			}
			return;
		}
		isAnimRuning = true;

		targetPosition = position;

		int distancePosition = position - currentPosition;

		long duration = ANIM_TOTLE_DURATION / Math.abs(distancePosition);

		if (duration < 65l) {
			duration = 65l;
		}

		int sign = position - currentPosition > 0 ? 1 : -1;

		rotateyAnimRun(this, duration, sign);
	}

	private ObjectAnimator anim ;
	private void rotateyAnimRun(final View view, final long duration,
			final int sign) {

		anim = ObjectAnimator//
				.ofFloat(view, "zhy", 0.0f, 1.0f)//
				.setDuration(duration);//
		anim.start();
		anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();

				// 计算角度变化量百分比
				movePercent = cVal * sign;
				requestLayout();
				
				if (cVal == 1.0) {
					// TODO updataItem 重置movePercent
					movePercent = 0.0;

					if (sign > 0) {
						showRight2CurrentItem();
					} else {
						showLeft2currentItem();
					}

					if (currentPosition != targetPosition) {
						rotateyAnimRun(view, duration, sign);
					} else if (animList.size() > 0) {
						moveToTargetIgnoreAnimRuning(animList.remove(0));
					} else {
						
						View tag = showViewTagArray.get(showViewTagArray.size()/2);
						tag.setBackgroundResource(R.drawable.dot_light);
						isAnimRuning = false;
					}
				}
			}
		});
	}

	/**
	 * 将右边条目 置为当前条目
	 */
	private void showRight2CurrentItem() {

		// TODO 左边item 移除 右边item 添加
		DataView dataView = showViewArray.remove(0);
		removeViewArray.add(dataView.view);
		removeView(dataView.view);
		++firstIndext;
		dataView.view = null;

		View convertView = null;
		if (removeViewArray.size() > 0) {
			convertView = removeViewArray.remove(0);
		}
		View view = mAdapter.getView(getActuallyPosition(endIndext + 1), convertView, this);
		view.setOnClickListener(new MyClickListener());
		dataView.view = view;
		dataView.position = (++endIndext);
		showViewArray.add(dataView);
		addView(view);

		View tag = showViewTagArray.remove(0);
		showViewTagArray.add(tag);
		
		currentPosition++;

		movePercent = 0.0;
		// requestLayout();
		mearsureChildViewsLayout();
		invalidate();

	}

	/**
	 * 将左边条目 置为当前条目
	 */
	private void showLeft2currentItem() {

		// TODO 右边item 移除 左边item 添加
		DataView dataView = showViewArray.remove(showViewArray.size() - 1);
		removeViewArray.add(dataView.view);
		removeView(dataView.view);
		--endIndext;
		dataView.view = null;

		View convertView = null;
		if (removeViewArray.size() > 0) {
			convertView = removeViewArray.remove(0);
		}
		View view = mAdapter.getView(getActuallyPosition(firstIndext - 1), convertView, this);
		view.setOnClickListener(new MyClickListener());
		dataView.view = view;
		dataView.position = (--firstIndext);
		showViewArray.add(0, dataView);
		addView(view);

		View tag = showViewTagArray.remove(showViewTagArray.size()-1);
		showViewTagArray.add(0,tag);
		
		currentPosition--;

		movePercent = 0.0;
		// requestLayout();
		mearsureChildViewsLayout();
		invalidate();

	}

	private int getActuallyPosition(int position){
		if(mAdapter==null||mAdapter.getCount()==0){
			return position;
		}
		
		if(position>=0){
			return position % mAdapter.getCount();
		}else{
			if(position % mAdapter.getCount()==0){
				return 0;
			}
			return position % mAdapter.getCount() + mAdapter.getCount();
		}
	}
	
	private class DataView {
		public DataView(View view, int position) {
			this.view = view;
			this.position = position;
		};

		public View view;
		public double angle;
		public int position;

		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub

			if (o instanceof View) {
				View item = (View) o;
				return view == item;
			}

			return super.equals(o);
		}

	}

	private class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int onclickItemPosition = getOnclickItemPosition(v);
			if (onItemClickListener != null) {
				onItemClickListener.onItemClick(v, onclickItemPosition);
			}
		}

	}

	private int getOnclickItemPosition(View v) {

		for (int i = 0; i < showViewArray.size(); i++) {
			DataView dataView = showViewArray.get(i);
			if (dataView.view == v) {
				return dataView.position;
			}
		}
		return -1;
	}

	public interface OnItemClickListener {
		void onItemClick(View v, int position);
	}

	private static int getScreenWidthPix(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

}
