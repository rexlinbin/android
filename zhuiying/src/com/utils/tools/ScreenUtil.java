package com.utils.tools;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {
	/**
	 * 获取屏幕的大小
	 * 
	 * @param context
	 *            当前上下文
	 * @return 屏幕尺寸对象
	 */
	public static Screen getScreenPix(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return new Screen(dm.widthPixels, dm.heightPixels);
	}

	/**
	 * 获取屏幕的宽
	 * 
	 * @param context
	 *            当前上下文
	 * @return 屏幕宽
	 */
	public static int getScreenWidthPix(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕的高
	 * 
	 * @param context
	 *            当前上下文
	 * @return 屏幕高
	 */
	public static int getScreenHeightPix(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 屏幕信息
	 * 
	 * @author wangyang
	 * 
	 */
	public static class Screen {
		// 屏幕宽
		public int widthPixels;
		// 屏幕高
		public int heightPixels;

		public Screen() {
		}

		public Screen(int widthPixels, int heightPixels) {
			this.widthPixels = widthPixels;
			this.heightPixels = heightPixels;
		}
	}

	/**
	 * 获取屏幕密度
	 * 
	 * @return float
	 * @throws
	 */
	public static float getDensity() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = MyApplication.getApplicationInstance().getResources().getDisplayMetrics();
		return dm.density;
	}

	/**
	 * dp转px
	 * 
	 * @param dpValue
	 *            dp
	 * @return int px
	 * @throws
	 */
	public static int dp2px(float dpValue) {
		return (int) (dpValue * getDensity() + 0.5f);
	}

	/**
	 * px 转 dp
	 * 
	 * @param pxValue
	 *            px
	 * @return int dp
	 * @throws
	 */
	public static int px2dp(float pxValue) {
		return (int) (pxValue / getDensity() + 0.5f);
	}

	/**
	 * 获取状态栏高度
	 * 
	 * @return
	 * @return int
	 * @throws
	 */
	public static int getStatusBarHeight() {
		return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
	}
}
