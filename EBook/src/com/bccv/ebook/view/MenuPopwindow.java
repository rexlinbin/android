package com.bccv.ebook.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.boxuu.ebookjy.R;

public class MenuPopwindow implements OnClickListener {

	private Activity activity;

	private PopupWindow popuWindow;
	
	private View bookmarks;
	private View magnify_text_size;
	private View decrease_text_size;
	private View add_bookmark;
	
	
	public MenuPopwindow(Activity activity){
		this.activity = activity;
		initView();
	}
	
	private void initView(){
		Context context = activity.getApplicationContext();
		View footView = View.inflate(context, R.layout.menu_pop_view, null);
		bookmarks = footView.findViewById(R.id.bookmarks);
		magnify_text_size = footView.findViewById(R.id.magnify_text_size);
		decrease_text_size = footView.findViewById(R.id.decrease_text_size);
		add_bookmark = footView.findViewById(R.id.add_bookmark);
		
		bookmarks.setOnClickListener(this);
		magnify_text_size.setOnClickListener(this);
		decrease_text_size.setOnClickListener(this);
		add_bookmark.setOnClickListener(this);
		
		popuWindow = new PopupWindow(footView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popuWindow.setAnimationStyle(R.style.anim_share_select);
		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);
		popuWindow.setBackgroundDrawable(cd);
		
	}
	
	/**
	 * 弹出popwindow
	 * 
	 * @param view
	 */
	public void show(View view) {
		popuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
	}
	
	/**
	 * 隐藏popwindow
	 */
	public void dismiss() {
		if (popuWindow != null && popuWindow.isShowing()) {
			popuWindow.dismiss();
		}
	}
	
	public boolean isShowing() {
		if (popuWindow != null) {
			return popuWindow.isShowing();
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bookmarks:
			//TODO 我的书签
			break;
		case R.id.magnify_text_size:
			//TODO 增大字体
			break;
		case R.id.decrease_text_size:
			//TODO 减小字体
			break;
		case R.id.add_bookmark:
			//TODO 添加书签
			break;

		default:
			break;
		}
		
	}
	
}
