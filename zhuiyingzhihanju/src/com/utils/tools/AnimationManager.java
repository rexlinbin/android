package com.utils.tools;

import android.animation.Animator.AnimatorListener;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

public class AnimationManager {

	/**
	 * 
	 * View渐隐动画效果
	 * 
	 * 
	 */

	public static void setHideAnimation(View view, int duration,
			AnimationListener listener) {

		if (null == view || duration < 0) {

			return;

		}

		AlphaAnimation mHideAnimation = new AlphaAnimation(1.0f, 0.0f);

		mHideAnimation.setDuration(duration);

		mHideAnimation.setFillAfter(true);
		if (listener != null) {
			mHideAnimation.setAnimationListener(listener);
		}

		view.startAnimation(mHideAnimation);

	}

	/**
	 * 
	 * View渐现动画效果
	 * 
	 * 
	 */

	public static void setShowAnimation(View view, int duration,
			AnimationListener listener) {

		if (null == view || duration < 0) {

			return;

		}

		AlphaAnimation mShowAnimation = new AlphaAnimation(0.0f, 1.0f);

		mShowAnimation.setDuration(duration);

		mShowAnimation.setFillAfter(true);
		if (listener != null) {
			mShowAnimation.setAnimationListener(listener);
		}

		view.startAnimation(mShowAnimation);

	}
	
	public static void setRotateAnimation(View view, int fromDegree, int toDegree, int duration, int repeatCount, 
			AnimationListener listener){
		RotateAnimation rotateAnimation = new RotateAnimation(fromDegree, toDegree,Animation.RELATIVE_TO_SELF, 
				0.5f,Animation.RELATIVE_TO_SELF,0.5f);
		rotateAnimation.setDuration(duration);
		rotateAnimation.setFillAfter(true);
		rotateAnimation.setRepeatCount(repeatCount);
		if (listener != null) {
			rotateAnimation.setAnimationListener(listener);
		}
		view.startAnimation(rotateAnimation);
	}
	
	public static void setTranslateAnimation(View view, int fromX, int toX, int fromY, int toY, int duration, AnimationListener listener){
		TranslateAnimation translateAnimation = new TranslateAnimation(fromX, toX, fromY, toY);
		translateAnimation.setDuration(duration);
		translateAnimation.setFillAfter(true);
		if (listener != null) {
			translateAnimation.setAnimationListener(listener);
		}
		view.startAnimation(translateAnimation);
	}
}
