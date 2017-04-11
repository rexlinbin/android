package com.bccv.ebook.ui.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.ebook.ApplicationManager;
import com.bccv.ebook.model.BookInfo;
import com.bccv.ebook.model.Bookmark;
import com.bccv.ebook.model.Bookmarks;
import com.bccv.ebook.model.ChapterInfo;
import com.bccv.ebook.ui.adapter.ScanViewAdapter;
import com.bccv.ebook.ui.adapter.ScanViewAdapter.Holder;
import com.bccv.ebook.ui.utils.BookPageFactory;
import com.bccv.ebook.utils.L;
import com.bccv.ebook.utils.PreferenceHelper;
import com.bccv.ebook.utils.ScreenUtil;
import com.bccv.ebook.utils.SerializationUtil;
import com.bccv.ebook.view.ScanView;
import com.bccv.ebook.view.ScanView.onMoveListener;
import com.boxuu.ebookjy.R;

public class PageActivity extends BaseActivity {

	private static final String TAG = "PageActivity";

	public static final String BOOK_INFO_KEY = "book_info";
	public static final String TEXT_SIZE_KEY = "text_size_key";
	public static final String BOOKMARK_KEY = "bookmark_key";
	public static final String BOOKNAME_KEY = "bookname_key";
	public static final String BOOKINFO_KEY = "bookinfo_key";
	public static final String CURRENTPAGE_KEY = "currentpage_key";

	
	public View root,title, menu;
	public ImageView leftBtn, rightBtn;
	public TextView titleName;
	public View bookmarks_view, magnify_text_size, decrease_text_size, add_bookmark;
	public View next_chapter, pre_chapter;

	public ScanView scanview;
	public ScanViewAdapter adapter;

	public BookPageFactory pagefactory;

	private boolean isMenuShowing = false;
	private Animation menuIn, menuOut, titleIn, titleOut, addBookmark;

	private static final int MIN_TEXT_SIZE = 30;
	private static final int MAX_TEXT_SIZE = 50;
	private int currentTextSize = 40;

	private BookInfo bookInfo;
	private Bookmark readProgress;
	private ChapterInfo currentChapter;
	private Bookmarks bookmarks;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 去掉系统消息栏
		if(android.os.Build.VERSION.SDK_INT>=16){
			setFullScreen();
		}
		// 保持屏幕常亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_page);
		root = findViewById(R.id.root);
		
		final DisplayMetrics dm = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(dm);

		initView();
		initData();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Bookmark bookmark = (Bookmark) intent.getSerializableExtra(BOOKMARK_KEY);
		if(bookmark!=null){
			openBookMark(bookmark);
		}
		getBookMarks();
	}
	
	private void initView() {
		title = findViewById(R.id.common_title);
		
		if(android.os.Build.VERSION.SDK_INT>=16){
			RelativeLayout.LayoutParams lp = 
					new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
							RelativeLayout.LayoutParams.WRAP_CONTENT);  
			lp.setMargins(0, ScreenUtil.dp2px(mContext, 24), 0, 0);
			title.setLayoutParams(lp);
		}
		
		menu = findViewById(R.id.menu_pop);
		leftBtn = (ImageView) title.findViewById(R.id.common_title_leftbtn);
		leftBtn.setImageResource(R.drawable.back);
		rightBtn =(ImageView) title.findViewById(R.id.common_title_rightbtn);
		rightBtn.setImageResource(R.drawable.zhangjie);
		titleName = (TextView) title.findViewById(R.id.tv_title);

		bookmarks_view = menu.findViewById(R.id.bookmarks);
		magnify_text_size = menu.findViewById(R.id.magnify_text_size);
		decrease_text_size = menu.findViewById(R.id.decrease_text_size);
		add_bookmark = menu.findViewById(R.id.add_bookmark);

		next_chapter = findViewById(R.id.next_chapter);
		pre_chapter = findViewById(R.id.pre_chapter);

		title.setOnClickListener(this);
		menu.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		bookmarks_view.setOnClickListener(this);
		magnify_text_size.setOnClickListener(this);
		decrease_text_size.setOnClickListener(this);
		add_bookmark.setOnClickListener(this);
		pre_chapter.setOnClickListener(this);
		next_chapter.setOnClickListener(this);

		scanview = (ScanView) findViewById(R.id.scanview);
		scanview.setOnClickListener(this);

	}

	private void initData() {

//		final DisplayMetrics dm = new DisplayMetrics();
//		WindowManager windowManager = (WindowManager) this
//				.getSystemService(Context.WINDOW_SERVICE);
//		windowManager.getDefaultDisplay().getMetrics(dm);
		// Rect frame = new Rect();
		// getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

		int statusBarHeight = 50;
		int readHeight = ApplicationManager.SCREEN_HEIGHT - statusBarHeight;

		// TODO 获取图书信息
		Intent intent = getIntent();
		bookInfo = (BookInfo) intent.getSerializableExtra(BOOK_INFO_KEY);

		titleName.setText(bookInfo.getName());

		String progressInfo = PreferenceHelper.getString(bookInfo.getId() + "",
				null);
		L.v(TAG, "initData", "progressInfo : " + progressInfo);

		if (!TextUtils.isEmpty(progressInfo)) {
			// 格式 ： ChapterId_ChapterName_CharsetIndex
			String[] values = progressInfo.split("#");
			if (values.length == 3) {
				readProgress = new Bookmark();
				readProgress.setChapterId(Integer.valueOf(values[0]));
				readProgress.setChapterName(values[1]);
				readProgress.setCharsetIndex(Integer.valueOf(values[2]));
			} else {
				PreferenceHelper.putString(bookInfo.getId() + "", "");
			}
		}

		if (readProgress == null) {
			readProgress = new Bookmark();
			ChapterInfo chapterInfo = bookInfo.getChapterInfos().get(0);
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getName());
			readProgress.setCharsetIndex(0);
		}

		currentChapter = findChapterInfoById(readProgress.getChapterId());

		if (currentChapter == null
				&& TextUtils.isEmpty(currentChapter.getPath())) {
			showShortToast("图书不存在");
			finish();
			return;
		}

		// TODO 读取sp中的字体大小
		currentTextSize = PreferenceHelper.getInt(TEXT_SIZE_KEY, 40);
		BookPageFactory.setFontSize(currentTextSize);
		pagefactory = BookPageFactory.build(ApplicationManager.SCREEN_WIDTH, readHeight);
		try {
			pagefactory.openbook(currentChapter.getPath());
		} catch (IOException e) {
			showShortToast("图书不存在");
			finish();
			return;
		}

		adapter = new ScanViewAdapter(this, pagefactory);

		// TODO 跳转到历史记录
		scanview.setAdapter(adapter, readProgress.getCharsetIndex());

		menuIn = AnimationUtils.loadAnimation(this, R.anim.menu_pop_in);
		menuOut = AnimationUtils.loadAnimation(this, R.anim.menu_pop_out);
		
		if(android.os.Build.VERSION.SDK_INT>=16){
			titleOut = new TranslateAnimation(
	                Animation.ABSOLUTE, 0, Animation.ABSOLUTE,
	                0, Animation.ABSOLUTE, 0,
	                Animation.ABSOLUTE, -ScreenUtil.dp2px(mContext, 48+24));
			titleOut.setDuration(400);
	        		
	        titleIn = new TranslateAnimation(
    				Animation.ABSOLUTE, 0, Animation.ABSOLUTE,
    				0, Animation.ABSOLUTE, -ScreenUtil.dp2px(mContext, 48+24),
    				Animation.ABSOLUTE, 0);
	        titleIn.setDuration(400);
		}else{
			titleIn = AnimationUtils.loadAnimation(this, R.anim.title_in);
			titleOut = AnimationUtils.loadAnimation(this, R.anim.title_out);
		}
		
		addBookmark = AnimationUtils.loadAnimation(this, R.anim.add_bookmark);
		

		titleOut.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				title.setVisibility(View.GONE);
			}
		});
		
		

		menuOut.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				menu.setVisibility(View.GONE);
			}
		});

		scanview.setOnMoveListener(new onMoveListener() {

			@Override
			public void onMoveEvent() {
				if (isMenuShowing) {
					hideMenu();
				}
			}

			@Override
			public void onPageChanged(View currPage) {
				Holder tag = (Holder) currPage.getTag();
				readProgress.setCharsetIndex(tag.start_index);
				showChaptersBtn(tag);
			}
		});

		View currPage = scanview.getCurrPage();
		Holder tag = (Holder) currPage.getTag();
		showChaptersBtn(tag);
		
		//TODO 获取该书的书签
		getBookMarks();
		
	}
	
	@SuppressLint("UseSparseArrays")
	private void getBookMarks(){
		bookmarks = SerializationUtil.readSerializationBookmarks(mContext, bookInfo.getName());
		if(bookmarks==null){
			bookmarks = new Bookmarks();
		}
		if(bookmarks.getBookmarks()==null){
			bookmarks.setBookmarks(new HashMap<Integer, ArrayList<Bookmark>>());
		}
	}

	
	private void showChaptersBtn(Holder tag) {
		if (tag.m_isfirstPage && currentChapter.getIndex() > 0) {
			pre_chapter.setVisibility(View.VISIBLE);
			next_chapter.setVisibility(View.GONE);
		} else if (tag.m_islastPage
				&& currentChapter.getIndex() < bookInfo.getChapterInfos()
						.size()-1) {
			pre_chapter.setVisibility(View.GONE);
			next_chapter.setVisibility(View.VISIBLE);
		} else {
			pre_chapter.setVisibility(View.GONE);
			next_chapter.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.common_title_leftbtn:
			// 返回
			finish();
			break;
		case R.id.common_title_rightbtn:
			// 跳转章节列表
			Intent in = new Intent(this, CatalogueActivity.class);
			in.putExtra(BOOKINFO_KEY, bookInfo);
			in.putExtra(CURRENTPAGE_KEY, currentChapter.getIndex());
			startActivity(in);
			break;
		case R.id.bookmarks:
			Intent itIntent = new Intent(this, BookMarksActivity.class);
			itIntent.putExtra(BOOKNAME_KEY, bookInfo.getName());
			startActivity(itIntent);
			// 点击书签
			break;
		case R.id.magnify_text_size:
			if (currentTextSize + 2 <= MAX_TEXT_SIZE) {
				currentTextSize += 2;
				changeTextSize();
			} else {
				showShortToast("已达最大字体");
			}

			// 增大字体
			break;
		case R.id.decrease_text_size:
			if (currentTextSize - 2 >= MIN_TEXT_SIZE) {
				currentTextSize -= 2;
				changeTextSize();
			} else {
				showShortToast("已达最小字体");
			}
			// 减小字体
			break;
		case R.id.add_bookmark:
			add_bookmark.startAnimation(addBookmark);
			addBookmark();
			break;
		case R.id.pre_chapter:
			currentChapter = bookInfo.getChapterInfos().get(
					currentChapter.getIndex() - 1);
			if (openBook(currentChapter.getPath())) {
				pagefactory.reset();
				int beginIndex = pagefactory.measureBeginIndex(pagefactory
						.getFileLength());
				scanview.setAdapter(adapter, beginIndex);
				View currPage = scanview.getCurrPage();
				Holder tag = (Holder) currPage.getTag();
				showChaptersBtn(tag);
				updataProgress(tag);
			} else {
				//TODO 图书不存在
			}
			// 添加书签
			break;
		case R.id.next_chapter:
			// 添加书签
			currentChapter = bookInfo.getChapterInfos().get(
					currentChapter.getIndex() + 1);
			if (openBook(currentChapter.getPath())) {
				pagefactory.reset();
				scanview.setAdapter(adapter, 0);
				View currPage = scanview.getCurrPage();
				Holder tag = (Holder) currPage.getTag();
				showChaptersBtn(tag);
				updataProgress(tag);
			} else {
				//TODO 图书不存在
			}
			break;
		case R.id.scanview:
			// 跳转章节列表
			// menuPopwindow.show(title);
			if (isMenuShowing) {
				hideMenu();
			} else {
				showMenu();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 打开书签
	 * @param bookMark
	 */
	private void openBookMark(Bookmark bookmark){
		
		currentChapter = findChapterInfoById(bookmark.getChapterId());
		if (openBook(currentChapter.getPath())) {
			pagefactory.reset();
			scanview.setAdapter(adapter, bookmark.getCharsetIndex());
			View currPage = scanview.getCurrPage();
			Holder tag = (Holder) currPage.getTag();
			showChaptersBtn(tag);
			updataProgress(tag);
		} else {
			//TODO 图书不存在
		}
		
	}
	
	private boolean openBook(String path) {

		if (TextUtils.isEmpty(path)) {
			showShortToast("图书不存在");
			return false;
		}
		File file = new File(path);
		if (file.exists() && file.isFile() && file.length() > 0) {
			try {
				pagefactory.openbook(path);
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	private void changeTextSize() {
		Holder holder = (Holder) scanview.getCurrPage().getTag();
		BookPageFactory.setFontSize(currentTextSize);
		pagefactory.reset();
		scanview.setAdapter(adapter, holder.start_index);
		PreferenceHelper.putInt(TEXT_SIZE_KEY, currentTextSize);
	}

	private void showMenu() {
		if(android.os.Build.VERSION.SDK_INT>=16){
			quitFullScreen();
		}
		title.setVisibility(View.VISIBLE);
		title.startAnimation(titleIn);
		menu.setVisibility(View.VISIBLE);
		menu.startAnimation(menuIn);
		isMenuShowing = true;
	}

	private void hideMenu() {
		if(android.os.Build.VERSION.SDK_INT>=16){
			setFullScreen();
		}
		title.startAnimation(titleOut);
		menu.startAnimation(menuOut);
		isMenuShowing = false;
	}

	private ChapterInfo findChapterInfoById(int ChapterId) {

		for (ChapterInfo info : bookInfo.getChapterInfos()) {
			if (info.getId() == ChapterId) {
				return info;
			}
		}
		return null;
	}

	private void updataProgress(Holder tag) {
		readProgress.setChapterId(currentChapter.getId());
		readProgress.setChapterName(currentChapter.getName());
		readProgress.setCharsetIndex(tag.start_index);
	}

	private void addBookmark(){
		//TODO 添加书签
		ArrayList<Bookmark> markList = bookmarks.getBookmarks().get(currentChapter.getId());
		if(markList==null){
			markList = new ArrayList<Bookmark>();
			bookmarks.getBookmarks().put(currentChapter.getId(), markList);
		}
		
		Holder tag = (Holder) scanview.getCurrPage().getTag();
		
		Bookmark bookmark = new Bookmark();
		bookmark.setChapterId(currentChapter.getId());
		bookmark.setChapterName(currentChapter.getName());
		bookmark.setCharsetIndex(tag.start_index);
		bookmark.setTime(System.currentTimeMillis());
		
		if(markList.contains(bookmark)){
			markList.remove(bookmark);
			markList.add(0, bookmark);
			SerializationUtil.wirteSerializationBookmarks(mContext, bookmarks, bookInfo.getName());
			return;
		}
		
		while(markList.size()>=20){
			markList.remove(markList.size()-1);
		}
		markList.add(0, bookmark);
		SerializationUtil.wirteSerializationBookmarks(mContext, bookmarks, bookInfo.getName());
	}
	
	/**
	 * 在ontouch事件中用到的不同时期变量必须定义成全局变量
	 */
	int downX;
	boolean isClick = false;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		// if (ev.getAction() == MotionEvent.ACTION_DOWN) {
		// downX = (int) ev.getX();
		// }else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
		// int moveX = (int) ev.getX();
		// if (Math.abs(moveX - downX) < 10) {//通过绝对值保证滑动距离的判定
		// isClick = true;
		// return true;
		// }else {
		// isClick = false;//不能省略
		// }
		// }else if (ev.getAction() == MotionEvent.ACTION_UP) {
		// // 其实这里不需要做处理,在move事件中也可做点击处理
		// if (isClick) {
		// isClick = false;
		// return true;
		// }
		// }
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isMenuShowing) {
			hideMenu();
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 格式 ： ChapterId_ChapterName_CharsetIndex
		if (readProgress != null) {
			String progressInfo = readProgress.getChapterId() + "#"
					+ readProgress.getChapterName() + "#"
					+ readProgress.getCharsetIndex();
			PreferenceHelper.putString(bookInfo.getId() + "", progressInfo);

			L.v(TAG, "onDestroy", "progressInfo : " + progressInfo);

			readProgress = null;
		}
		if (pagefactory != null) {
			pagefactory.close();
			pagefactory = null;
		}

	}

	

	private void setFullScreen(){
	     getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        getWindow().setAttributes(lp);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}
	
	@SuppressLint({ "NewApi", "InlinedApi" })
	private void quitFullScreen(){
	      final WindowManager.LayoutParams attrs = getWindow().getAttributes();
	      attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
	      getWindow().setAttributes(attrs);
	      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
	
}
