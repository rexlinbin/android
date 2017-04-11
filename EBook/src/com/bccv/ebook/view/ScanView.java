package com.bccv.ebook.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;

import com.bccv.ebook.ui.adapter.PageAdapter;
import com.bccv.ebook.ui.adapter.ScanViewAdapter;
import com.bccv.ebook.ui.adapter.ScanViewAdapter.Holder;
import com.bccv.ebook.utils.L;

/**
 * @author liukai
 * @date 2015-1-5
 */
public class ScanView extends RelativeLayout {
	public static final String TAG = "ScanView";
	private boolean isInit = true;
	// 滑动的时候存在两页可滑动，要判断是哪一页在滑动
	private boolean isPreMoving = true, isCurrMoving = true;
	// 当前是第几页
	// private int index;
	private float lastX;
	private float lastY;
	// 前一页，当前页，下一页的左边位置
	private int prePageLeft = 0, currPageLeft = 0, nextPageLeft = 0;
	// 三张页面
	private View prePage, currPage, nextPage;
	// 页面状态
	private static final int STATE_MOVE = 0;
	private static final int STATE_STOP = 1;
	// 滑动的页面，只有前一页和当前页可滑
	private static final int PRE = 2;
	private static final int CURR = 3;
	private int state = STATE_STOP;
	// 正在滑动的页面右边位置，用于绘制阴影
	private float right;
	// 手指滑动的距离
	private float moveLenght;
	// 页面宽高
	private int mWidth, mHeight;
	// 获取滑动速度
	private VelocityTracker vt;
	// 防止抖动
	private float speed_shake = 100;
	// 当前滑动速度
	private float speed;
	private Timer timer;
	private MyTimerTask mTask;
	// 滑动动画的移动速度
	public static final int MOVE_SPEED = 10;
	// 页面适配器
	private ScanViewAdapter adapter;
	/**
	 * 过滤多点触碰的控制变量
	 */
	private int mEvents;

	public void setAdapter(ScanViewAdapter adapter, int currentStartIndext) {

		removeAllViews();
		this.adapter = adapter;
		prePage = adapter.getView();
		Holder prePageTag = (Holder) prePage.getTag();
		prePageTag.end_index = currentStartIndext;
		prePage.setTag(prePageTag);
		addView(prePage, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		if (prePageTag.end_index > 0) {
			adapter.addContent(prePage, PageAdapter.PREPAGE);
		}

		currPage = adapter.getView();
		Holder currPageTag = (Holder) currPage.getTag();
		// currPageTag.start_index = prePageTag.end_index;
		currPageTag.start_index = currentStartIndext;
		currPage.setTag(currPageTag);
		addView(currPage, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		if (currPageTag.start_index > 0) {
			adapter.addContent(currPage, PageAdapter.NEXTPAGE);
		} else {
			adapter.addContent(currPage, PageAdapter.CURRENTPAGE);
		}

		nextPage = adapter.getView();
		Holder nextPageTag = (Holder) nextPage.getTag();
		nextPageTag.start_index = currPageTag.end_index;

		prePage.setTag(prePageTag);
		addView(nextPage, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		adapter.addContent(nextPage, PageAdapter.NEXTPAGE);

	}

	/**
	 * 向左滑。注意可以滑动的页面只有当前页和前一页
	 * 
	 * @param which
	 */
	private void moveLeft(int which) {
		switch (which) {
		case PRE:
			prePageLeft -= MOVE_SPEED;
			if (prePageLeft < -mWidth)
				prePageLeft = -mWidth;
			right = mWidth + prePageLeft;
			break;
		case CURR:
			currPageLeft -= MOVE_SPEED;
			if (currPageLeft < -mWidth)
				currPageLeft = -mWidth;
			right = mWidth + currPageLeft;
			break;
		}
	}

	/**
	 * 向右滑。注意可以滑动的页面只有当前页和前一页
	 * 
	 * @param which
	 */
	private void moveRight(int which) {
		switch (which) {
		case PRE:
			prePageLeft += MOVE_SPEED;
			if (prePageLeft > 0)
				prePageLeft = 0;
			right = mWidth + prePageLeft;
			break;
		case CURR:
			currPageLeft += MOVE_SPEED;
			if (currPageLeft > 0)
				currPageLeft = 0;
			right = mWidth + currPageLeft;
			break;
		}
	}

	/**
	 * 当往回翻过一页时添加前一页在最左边
	 */
	private void addPrePage() {
		
		
		removeView(nextPage);

		Holder tag = (Holder) nextPage.getTag();
		tag.rest();
		Holder nextPageTag = (Holder) prePage.getTag();
		tag.end_index = nextPageTag.start_index;

		nextPage.setTag(tag);
		addView(nextPage, -1, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// 从适配器获取前一页内容
		adapter.addContent(nextPage, PageAdapter.PREPAGE);
		// 交换顺序
		View temp = nextPage;
		nextPage = currPage;
		currPage = prePage;
		prePage = temp;

		prePageLeft = -mWidth;

		// 当前页如果是首页 且 第一页尾坐标与第二页坐标不一致 则更新adapter
		Holder currPageTag = (Holder) currPage.getTag();
		nextPageTag = (Holder) nextPage.getTag();
		if (nextPageTag.start_index != currPageTag.end_index
				&& currPageTag.start_index == 0) {
			setAdapter(adapter, 0);
		}
		
		if(moveListener!=null){
			moveListener.onPageChanged(currPage);
		}

	}

	/**
	 * 当往前翻过一页时，添加一页在最底下
	 */
	private void addNextPage() {
		removeView(prePage);
		Holder tag = (Holder) prePage.getTag();
		tag.rest();
		Holder nextPageTag = (Holder) nextPage.getTag();
		tag.start_index = nextPageTag.end_index;

		prePage.setTag(tag);
		addView(prePage, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// 从适配器获取后一页内容
		adapter.addContent(prePage, PageAdapter.NEXTPAGE);
		// 交换顺序
		View temp = currPage;
		currPage = nextPage;
		nextPage = prePage;
		prePage = temp;
		currPageLeft = 0;
		if(moveListener!=null){
			moveListener.onPageChanged(currPage);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (state != STATE_MOVE)
				return;
			// 移动页面
			// 翻回，先判断当前哪一页处于未返回状态
			if (prePageLeft > -mWidth && speed <= 0) {
				// 前一页处于未返回状态
				moveLeft(PRE);
			} else if (currPageLeft < 0 && speed >= 0) {
				// 当前页处于未返回状态
				moveRight(CURR);
			} else if (speed < 0 && !((Holder) currPage.getTag()).m_islastPage) {
				// 向左翻，翻动的是当前页
				moveLeft(CURR);
				if (currPageLeft == (-mWidth)) {
					// 翻过一页，在底下添加一页，把最上层页面移除
					addNextPage();
				}
			} else if (speed > 0 && !((Holder) currPage.getTag()).m_isfirstPage) {
				// 向右翻，翻动的是前一页
				moveRight(PRE);
				if (prePageLeft == 0) {
					// 翻回一页，添加一页在最上层，隐藏在最左边
					addPrePage();
				}
			}
			if (right == 0 || right == mWidth) {
				releaseMoving();
				state = STATE_STOP;
				quitMove();
			}
			ScanView.this.requestLayout();
		}

	};

	public ScanView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ScanView(Context context) {
		super(context);
		init();
	}

	public ScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * 退出动画翻页
	 */
	public void quitMove() {
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
	}

	private void init() {
		timer = new Timer();
		mTask = new MyTimerTask(updateHandler);
	}

	/**
	 * 释放动作，不限制手滑动方向
	 */
	private void releaseMoving() {
		isPreMoving = true;
		isCurrMoving = true;
	}

	boolean isMove = false;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (adapter != null)
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				lastX = event.getX();
				lastY = event.getY();
				try {
					if (vt == null) {
						vt = VelocityTracker.obtain();
					} else {
						vt.clear();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				vt.addMovement(event);
				mEvents = 0;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
			case MotionEvent.ACTION_POINTER_UP:
				mEvents = -1;
				break;
			case MotionEvent.ACTION_MOVE:

				// 取消动画
				quitMove();
				L.v(TAG,"dispatchTouchEvent", "mEvents = " + mEvents + ", isPreMoving = "
						+ isPreMoving + ", isCurrMoving = " + isCurrMoving);
				vt.addMovement(event);
				vt.computeCurrentVelocity(500);// 设置时间单位 500毫秒
				speed = vt.getXVelocity();
				moveLenght = event.getX() - lastX;
				float distanceY = event.getY() - lastY;

				if (Math.abs(moveLenght) > 10 || Math.abs(distanceY) > 10) {
					isMove = true;
					if(moveListener!=null){
						moveListener.onMoveEvent();
					}
				}

				if ((moveLenght > 10 || !isCurrMoving) && isPreMoving
						&& mEvents == 0) {
					isPreMoving = true;
					isCurrMoving = false;
					if (((Holder) currPage.getTag()).m_isfirstPage) {
						// 第一页不能再往右翻，跳转到前一个activity
						state = STATE_MOVE;
						releaseMoving();
					} else {
						// 非第一页
						prePageLeft += (int) moveLenght;
						// 防止滑过边界
						if (prePageLeft > 0)
							prePageLeft = 0;
						else if (prePageLeft < -mWidth) {
							// 边界判断，释放动作，防止来回滑动导致滑动前一页时当前页无法滑动
							prePageLeft = -mWidth;
							releaseMoving();
						}
						right = mWidth + prePageLeft;
						state = STATE_MOVE;
					}
				} else if ((moveLenght < -10 || !isPreMoving) && isCurrMoving
						&& mEvents == 0) {
					isPreMoving = false;
					isCurrMoving = true;
					if (((Holder) currPage.getTag()).m_islastPage) {
						// 最后一页不能再往左翻
						state = STATE_STOP;
						releaseMoving();
					} else {
						currPageLeft += (int) moveLenght;
						// 防止滑过边界
						if (currPageLeft < -mWidth)
							currPageLeft = -mWidth;
						else if (currPageLeft > 0) {
							// 边界判断，释放动作，防止来回滑动导致滑动当前页是前一页无法滑动
							currPageLeft = 0;
							releaseMoving();
						}
						right = mWidth + currPageLeft;
						state = STATE_MOVE;
					}

				} else
					mEvents = 0;
				lastX = event.getX();
				lastY = event.getY();
				requestLayout();
				break;
			case MotionEvent.ACTION_UP:
				if (Math.abs(speed) < speed_shake)
					speed = 0;
				quitMove();
				mTask = new MyTimerTask(updateHandler);
				timer.schedule(mTask, 0, 5);
				try {
					vt.clear();
					vt.recycle();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (isMove) {
					isMove = false;
					return true;
				}
				break;
			default:
				break;
			}
		super.dispatchTouchEvent(event);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	/*
	 * （非 Javadoc） 在这里绘制翻页阴影效果
	 * 
	 * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (right == 0 || right == mWidth)
			return;
		RectF rectF = new RectF(right, 0, mWidth, mHeight);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		LinearGradient linearGradient = new LinearGradient(right, 0,
				right + 36, 0, 0xffbbbbbb, 0x00bbbbbb, TileMode.CLAMP);
		paint.setShader(linearGradient);
		paint.setStyle(Style.FILL);
		canvas.drawRect(rectF, paint);

		// RectF rectF = new RectF(right, 7.5f, right+5.5f, mHeight);
		// Paint paint = new Paint();
		// paint.setAntiAlias(true);
		// LinearGradient linearGradient = new LinearGradient(right, 0,
		// right +5.5f, 0, 0xdd333333, 0xdd333333, TileMode.CLAMP);
		// paint.setShader(linearGradient);
		// paint.setStyle(Style.FILL);
		// canvas.drawRect(rectF, paint);
		// paint.setColor(Color.BLACK);
		// paint.setShader(null);
		// canvas.drawLine(right, 0, right, mHeight, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		if (isInit) {
			// 初始状态，一页放在左边隐藏起来，两页叠在一块
			prePageLeft = -mWidth;
			currPageLeft = 0;
			nextPageLeft = 0;
			isInit = false;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (adapter == null)
			return;
		prePage.layout(prePageLeft, 0,
				prePageLeft + prePage.getMeasuredWidth(),
				prePage.getMeasuredHeight());
		currPage.layout(currPageLeft, 0,
				currPageLeft + currPage.getMeasuredWidth(),
				currPage.getMeasuredHeight());
		nextPage.layout(nextPageLeft, 0,
				nextPageLeft + nextPage.getMeasuredWidth(),
				nextPage.getMeasuredHeight());
		invalidate();
	}

	class MyTimerTask extends TimerTask {
		Handler handler;

		public MyTimerTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendMessage(handler.obtainMessage());
		}

	}

	
	private onMoveListener moveListener;
	public void setOnMoveListener(onMoveListener moveListener){
		this.moveListener = moveListener;
	}
	
	public interface onMoveListener{
		void onMoveEvent();
		void onPageChanged(View currPage);
	}
	
	// public View prePage, currPage, nextPage;
	public View getCurrPage() {
		return currPage;
	}
	
	

}
