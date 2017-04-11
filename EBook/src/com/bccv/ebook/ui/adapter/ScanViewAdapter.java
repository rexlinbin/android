package com.bccv.ebook.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.bccv.ebook.ui.utils.BookPageFactory;
import com.bccv.ebook.view.PageWidget;
import com.boxuu.ebookjy.R;

public class ScanViewAdapter extends PageAdapter {
	Context context;
	List<String> items;
	AssetManager am;

	private BookPageFactory bookPageFactory;

	public static final int START_INDEX = 0;
	public static final int END_INDEX = 1;
	

	public ScanViewAdapter(Context context, BookPageFactory bookPageFactory) {
		this.context = context;
		this.bookPageFactory = bookPageFactory;
	}

	public void addContent(View view, int index) {

		// TODO 画出界面内容
		Holder tag = (Holder) view.getTag();
		RelativeLayout container = (android.widget.RelativeLayout) view
				.findViewById(R.id.book_container);
		container.removeAllViews();

		if(container.getChildCount()>0){
			PageWidget pageWidget = (PageWidget) container.getChildAt(0);
			pageWidget.reset(index, tag);
		}else{
			PageWidget pageWidget = new PageWidget(context, bookPageFactory, index,
					tag);
			container.addView(pageWidget, 0, new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT));
		}
		view.setTag(tag);
	}

	public View getView() {
		View view = LayoutInflater.from(context).inflate(R.layout.page_layout,
				null);
		Holder holder = new Holder();
		view.setTag(holder);
		return view;
	}

	public static class Holder {
		public int start_index = 0;
		public int end_index = 0;
		public boolean m_isfirstPage = false;
		public boolean m_islastPage = false;

		public void rest() {
			start_index = 0;
			end_index = 0;
			m_isfirstPage = false;
			m_islastPage = false;
		}
	}

}
