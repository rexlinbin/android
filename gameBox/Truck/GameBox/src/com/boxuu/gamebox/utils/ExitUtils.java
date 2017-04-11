package com.boxuu.gamebox.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * 退出时toast询问是否退出的工具类
 * 
 */
public class ExitUtils {

	private static String TAG = "ExitUtils";

	// 计时器
	private static MyCount mc;
	private static boolean isExit;
	private static boolean hasTask;

	public static boolean isExit(Activity activity) {
		if (isExit == false) {
			isExit = true;
			Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			if (!hasTask) {
				hasTask = true;
				mc = new MyCount(2000, 1000);
				mc.start();
			}
		} else {
			return true;
		}
		return false;
	}

	/* 定义一个倒计时的内部类 */
	static class MyCount extends CountDownTimer {
		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			L.v(TAG, "MyCount onFinish", "重置");
			isExit = false;
			hasTask = false;
		}

		@Override
		public void onTick(long millisUntilFinished) {

		}
	}
}
