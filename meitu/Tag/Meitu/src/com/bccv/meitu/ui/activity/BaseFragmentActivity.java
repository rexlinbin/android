package com.bccv.meitu.ui.activity;

import android.R.anim;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.utils.ActivityQueue;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.SwipeBackLayout;

/**
 * A basic Activity that have provided some common features. Anyone activity
 * that extends BaseFragmentActivity will be pushed into a ActivityQueue
 * automatically
 * 
 * @author liukai
 * @since 2014-11-7
 */
public abstract class BaseFragmentActivity extends FragmentActivity implements Stackable,OnClickListener{
	protected SwipeBackLayout layout;
	protected Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SystemUtil.SetFullScreen(this);
		
		if(!(this instanceof MainActivity)){//如果不是首页 添加右划回退
			layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
					R.layout.base, null);
			layout.attachToActivity(this);
		}
		mContext = getApplicationContext();
		push();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		pop();
	}

	@Override
	public void push() {
		ActivityQueue.pushActivity(this);
	}

	@Override
	public void pop() {
		ActivityQueue.popActivity(this);
	}
	
	/**
	 * Toast短显示
	 * @param int pResId
	 */
	protected void showShortToast(int pResId) {
		showShortToast(mContext.getString(pResId));
	}
	
	/**
	 * Toast短显示
	 * @param String pMsg
	 */
	protected void showShortToast(String pMsg) {
		Toast.makeText(mContext, pMsg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Toast长显示
	 * @param int pResId
	 */
	protected void showLongToast(int pResId) {
		showLongToast(mContext.getString(pResId));
	}
	
	/**
	 * Toast长显示
	 * @param String pMsg
	 */
	protected void showLongToast(String pMsg) {
		Toast.makeText(mContext, pMsg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(anim.slide_in_left, anim.slide_out_right);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(anim.slide_in_left, anim.slide_out_right);
	}
	
}
