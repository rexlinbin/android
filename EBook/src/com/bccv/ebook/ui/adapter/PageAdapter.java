package com.bccv.ebook.ui.adapter;

import android.view.View;

public abstract class PageAdapter {
	/**
	 * @return 页面view
	 */
	public abstract View getView();

	/**
	 * 将内容添加到view中
	 * 
	 * @param view
	 *            包含内容的view
	 * @param index
	 *            页下标
	 */
	public abstract void addContent(View view, int index);

	public static final int PREPAGE = 1;
	public static final int CURRENTPAGE = 2;
	public static final int NEXTPAGE = 3;
	
}
