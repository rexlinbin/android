package com.bccv.ebook.view;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.bccv.ebook.ApplicationManager;
import com.bccv.ebook.ui.adapter.PageAdapter;
import com.bccv.ebook.ui.adapter.ScanViewAdapter.Holder;
import com.bccv.ebook.ui.utils.BookPageFactory;
import com.bccv.ebook.utils.L;

public class PageWidget extends View {

	private static final String TAG = "PageWidget";

	public boolean m_isfirstPage, m_islastPage; // 是否第一页 或 最后一页

	private BookPageFactory bookPageFactory;

	private Bitmap countbitmap;
	private Canvas mCurPageCanvas;

	public PageWidget(Context context, BookPageFactory bookPageFactory,
			int index, Holder tag) {
		super(context);
		this.bookPageFactory = bookPageFactory;
		if (countbitmap != null) {
			countbitmap.recycle();
			countbitmap = null;
		}
		countbitmap = Bitmap.createBitmap(ApplicationManager.SCREEN_WIDTH, ApplicationManager.SCREEN_HEIGHT,
				Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(countbitmap);
		reset(index,tag);
	}
	
	
	public void reset(int index,Holder tag){
		
		
		
		try {
			
			// TODO  待处理
			switch (index) {
			case PageAdapter.PREPAGE:
				this.bookPageFactory.prePage(tag.end_index);
				this.bookPageFactory.draw(mCurPageCanvas);
				break;
			case PageAdapter.CURRENTPAGE:
				this.bookPageFactory.nextPage(0);
				this.bookPageFactory.draw(mCurPageCanvas);
				break;
			case PageAdapter.NEXTPAGE:
				this.bookPageFactory.nextPage(tag.start_index);
				this.bookPageFactory.draw(mCurPageCanvas);
				break;

			default:
				break;
			}

			tag.start_index = this.bookPageFactory.getBiginIndex();
			tag.end_index = this.bookPageFactory.getEndIndex();

			m_isfirstPage = this.bookPageFactory.isFirstPage();
			m_islastPage = this.bookPageFactory.isLastPage();
			tag.m_isfirstPage = m_isfirstPage;
			tag.m_islastPage = m_islastPage;

		} catch (IOException e) {
			L.e(TAG,"PageWidget", e.getMessage());
		}
		
	}

	public PageWidget(Context context) {
		super(context);
	}

	public void setBookPageFactory(BookPageFactory bookPageFactory) {
		this.bookPageFactory = bookPageFactory;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (countbitmap != null) {
			canvas.drawBitmap(countbitmap, 0, 0, null);
		}
	}

}
