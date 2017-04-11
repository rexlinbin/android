package com.bccv.boxcomic.ebook;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ReadComicActivity;
import com.bccv.boxcomic.api.EbookApi;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.ebook.ScanView.onMoveListener;
import com.bccv.boxcomic.ebook.ScanViewAdapter.Holder;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.MyApplication;
import com.bccv.boxcomic.tool.MyPreferences;
import com.bccv.boxcomic.tool.SerializationUtil;
import com.bccv.boxcomic.tool.SystemUtils;
import com.lidroid.xutils.http.HttpHandler.State;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PageActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "PageActivity";

	public static final String BOOK_INFO_KEY = "book_info";
	public static final String TEXT_SIZE_KEY = "text_size_key";
	public static final String BOOKMARK_KEY = "bookmark_key";
	public static final String BOOKNAME_KEY = "bookname_key";
	public static final String BOOKINFO_KEY = "bookinfo_key";
	public static final String CURRENTPAGE_KEY = "currentpage_key";

	private Context mContext;

	private String content;

	public View root, title, menu, menu_setting;
	public ImageView leftBtn, rightBtn;
	public TextView titleName;
	public TextView dateView, magnify_text_size, decrease_text_size,
			settingView;
	public View next_chapter, pre_chapter;

	public ScanView scanview;
	public ScanViewAdapter adapter;

	public BookPageFactoryOnline pagefactory;

	private boolean isMenuShowing = false;
	private Animation menuIn, menuOut, titleIn, titleOut;

	private static final int MIN_TEXT_SIZE = 30;
	private static final int MAX_TEXT_SIZE = 50;
	private int currentTextSize = 40;

	private BookInfo bookInfo;
	private Bookmark readProgress;
	private ChapterInfo currentChapter;
	private Bookmarks bookmarks;

	private boolean isLocal = false;

	private SeekBar seekBar;
	private TextView normalTextView;
	private TextView huyanTextView;
	private TextView niupiTextView;

	private int dayBackColor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		GlobalParams.pageActivity = this;

		mContext = getApplicationContext();
		// 去掉系统消息栏
		if (android.os.Build.VERSION.SDK_INT >= 16) {
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
		if (isLocalData()) {
			getLocalData();
		} else {
			getData();
		}

		// initData();
	}

	private boolean isLocalData() {
		Intent intent = getIntent();
		bookInfo = (BookInfo) intent.getSerializableExtra(BOOK_INFO_KEY);

		Bookmark bookmark = (Bookmark) intent
				.getSerializableExtra(BOOKMARK_KEY);
		String progressInfo = "";
		if (bookmark == null) {
			progressInfo = PreferenceHelper.getString(bookInfo.getBook_id()
					+ "", null);
		} else {
			progressInfo = bookmark.getChapterId() + "#"
					+ bookmark.getChapterName() + "#"
					+ bookmark.getCharsetIndex();
		}

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
				PreferenceHelper.putString(bookInfo.getBook_id() + "", "");
			}
		}
		String fileName = "";
		if (readProgress != null) {
			fileName = readProgress.getChapterId() + "";
		}

		File file = new File(GlobalParams.ebookDownloadPathString + "/"
				+ bookInfo.getBook_id() + "/" + fileName + ".txt");
		if (file.exists()) {
			DownloadManager downloadManager = DownloadService
					.getDownloadManager(getApplicationContext());
			DownloadInfo downloadInfo = null;

			for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
				DownloadInfo aDownloadInfo = downloadManager.getDownloadInfo(j);
				if (aDownloadInfo.getApp_idString() != null
						&& aDownloadInfo.getApp_idString().equals(
								bookInfo.getBook_id() + "")) {
					downloadInfo = downloadManager.getDownloadInfo(j);
					if (downloadInfo.getChapter_id().equals(
							readProgress.getChapterId() + "")) {
						if (downloadInfo.getState() == State.SUCCESS) {
							isLocal = true;
							break;
						}
					}
				}
			}

		}

		return isLocal;
	}

	private void getLocalData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					content = result;
					initData();
				} else {
					Toast.makeText(getApplicationContext(), "读取漫画失败", 1).show();
					finish();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String fileName = readProgress.getChapterId() + "";
				String result = FileUtils.read(
						getApplicationContext(),
						GlobalParams.ebookDownloadPathString + "/"
								+ bookInfo.getBook_id() + "/" + fileName
								+ ".txt");
				List<ChapterInfo> list = getLocalChapters();
				bookInfo.setChapterInfos((ArrayList<ChapterInfo>) list);
				return result;
			}
		}.execute("");
	}

	private List<ChapterInfo> getLocalChapters() {
		List<ChapterInfo> list = SerializationUtil
				.readSerializationEbookChapters(mContext, bookInfo.getBook_id()
						+ "");
		return list;
	}

	private void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					content = result;
					initData();
				} else {
					Toast.makeText(getApplicationContext(), "读取漫画失败", 1).show();
					finish();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				EbookApi ebookApi = new EbookApi();
				ArrayList<ChapterInfo> chapterInfos = ebookApi
						.getChapterInfos(bookInfo.getBook_id() + "");
				if (chapterInfos != null) {
					SerializationUtil.wirteSerializationEbookChapters(mContext,
							(Serializable) chapterInfos, bookInfo.getBook_id()
									+ "");
					bookInfo.setChapterInfos(chapterInfos);
				}
				String result = null;
				if (readProgress != null && readProgress.getChapterId() != 0) {
					result = ebookApi.getText(readProgress.getChapterId() + "");
				} else {
					if (chapterInfos != null && chapterInfos.size() > 0) {
						result = ebookApi.getText(chapterInfos.get(0).getId()
								+ "");
					}
				}
				return result;
			}
		}.execute("");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Bookmark bookmark = (Bookmark) intent
				.getSerializableExtra(BOOKMARK_KEY);
		if (bookmark != null) {
			openBookMark(bookmark);
		}
		getBookMarks();
	}

	private void initView() {
		title = findViewById(R.id.common_title);

		if (android.os.Build.VERSION.SDK_INT >= 16) {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, ScreenUtil.dp2px(mContext, 24), 0, 0);
			title.setLayoutParams(lp);
		}
		menu_setting = findViewById(R.id.menu_setting);
		menu = findViewById(R.id.menu_pop);
		leftBtn = (ImageView) title.findViewById(R.id.common_title_leftbtn);
		leftBtn.setImageResource(R.drawable.back);
		rightBtn = (ImageView) title.findViewById(R.id.common_title_rightbtn);
		rightBtn.setImageResource(R.drawable.zhangjie);
		titleName = (TextView) title.findViewById(R.id.tv_title);

		dateView = (TextView) menu.findViewById(R.id.datemenu);
		magnify_text_size = (TextView) menu
				.findViewById(R.id.magnify_text_size);
		decrease_text_size = (TextView) menu
				.findViewById(R.id.decrease_text_size);
		settingView = (TextView) menu.findViewById(R.id.setting);

		next_chapter = findViewById(R.id.next_chapter);
		pre_chapter = findViewById(R.id.pre_chapter);

		title.setOnClickListener(this);
		menu.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		dateView.setOnClickListener(this);
		magnify_text_size.setOnClickListener(this);
		decrease_text_size.setOnClickListener(this);
		settingView.setOnClickListener(this);
		pre_chapter.setOnClickListener(this);
		next_chapter.setOnClickListener(this);

		scanview = (ScanView) findViewById(R.id.scanview);
		scanview.setOnClickListener(this);

		initSetting();
	}

	private void initSetting() {
		int backcolor = MyPreferences.getBackColor(getApplicationContext());

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		float brightness = SystemUtils.getCurrentActivityBrightness(PageActivity.this);
		seekBar.setProgress((int) (brightness * 255));
		seekBar.setMax(255);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				SystemUtils.setCurrentActivityBrightness(
						PageActivity.this, (float) progress / 255);
			}
		});
		
		
		normalTextView = (TextView) findViewById(R.id.normal_textView);
		huyanTextView = (TextView) findViewById(R.id.huyan_textView);
		niupiTextView = (TextView) findViewById(R.id.niupi_textView);
		switch (backcolor) {
		case 0:
			normalTextView.setSelected(true);
			huyanTextView.setSelected(false);
			niupiTextView.setSelected(false);
			dayBackColor = 0xfff0f0f0;
			break;
		case 1:
			normalTextView.setSelected(false);
			huyanTextView.setSelected(true);
			niupiTextView.setSelected(false);
			dayBackColor = 0xffcce3d4;
			break;
		case 2:
			normalTextView.setSelected(false);
			huyanTextView.setSelected(false);
			niupiTextView.setSelected(true);
			dayBackColor = 0xffe6d2b5;
			break;
		default:
			break;
		}

		normalTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				normalTextView.setSelected(true);
				huyanTextView.setSelected(false);
				niupiTextView.setSelected(false);
				MyPreferences.setBackColor(getApplicationContext(), 0);
				dayBackColor = 0xfff0f0f0;
				if (!dateView.isSelected()) {
					BookPageFactoryOnline.setBackGroundColor(0xfff0f0f0);
					//刷新
					Holder holder = (Holder) scanview.getCurrPage().getTag();
					pagefactory.reset();
					scanview.setAdapter(adapter, holder.start_index);
				}
			}
		});

		huyanTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				normalTextView.setSelected(false);
				huyanTextView.setSelected(true);
				niupiTextView.setSelected(false);
				MyPreferences.setBackColor(getApplicationContext(), 1);
				dayBackColor = 0xffcce3d4;
				//刷新
				if (!dateView.isSelected()) {
					BookPageFactoryOnline.setBackGroundColor(0xffcce3d4);
					//刷新
					Holder holder = (Holder) scanview.getCurrPage().getTag();
					pagefactory.reset();
					scanview.setAdapter(adapter, holder.start_index);
				}
			}
		});

		niupiTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				normalTextView.setSelected(false);
				huyanTextView.setSelected(false);
				niupiTextView.setSelected(true);
				MyPreferences.setBackColor(getApplicationContext(), 2);
				dayBackColor = 0xffe6d2b5;
				if (!dateView.isSelected()) {
					BookPageFactoryOnline.setBackGroundColor(0xffe6d2b5);
					//刷新
					Holder holder = (Holder) scanview.getCurrPage().getTag();
					pagefactory.reset();
					scanview.setAdapter(adapter, holder.start_index);
				}
			}
		});
		
		boolean isDay = MyPreferences.isDay(getApplicationContext());
		if (!isDay) {
			BookPageFactoryOnline.m_backColor = 0xff252525;
			BookPageFactoryOnline.m_textColor = 0xff555555;
			ScanView.shadowFromColor = 0xff000000;
			ScanView.shadowToColor = 0x00000000;
			dateView.setText("夜间");
			dateView.setSelected(true);
		} else {
			BookPageFactoryOnline.m_backColor = dayBackColor;
			BookPageFactoryOnline.m_textColor = 0xff333333;
			ScanView.shadowFromColor = 0xffbbbbbb;
			ScanView.shadowToColor = 0x00bbbbbb;
			dateView.setText("日间");
		}
		
	}

	private void initData() {

		// final DisplayMetrics dm = new DisplayMetrics();
		// WindowManager windowManager = (WindowManager) this
		// .getSystemService(Context.WINDOW_SERVICE);
		// windowManager.getDefaultDisplay().getMetrics(dm);
		// Rect frame = new Rect();
		// getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

		int statusBarHeight = 50;
		int readHeight = MyApplication.SCREEN_HEIGHT - statusBarHeight;

		// TODO 获取图书信息
		// Intent intent = getIntent();
		// bookInfo = (BookInfo) intent.getSerializableExtra(BOOK_INFO_KEY);

		titleName.setText(bookInfo.getBook_title());

		if (readProgress == null) {
			readProgress = new Bookmark();
			ChapterInfo chapterInfo = bookInfo.getChapterInfos().get(0);
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		}

		currentChapter = findChapterInfoById(readProgress.getChapterId());

		// if (currentChapter != null
		// && TextUtils.isEmpty(currentChapter.getPath())) {
		// showShortToast("图书不存在");
		// finish();
		// return;
		// }

		// TODO 读取sp中的字体大小
		currentTextSize = PreferenceHelper.getInt(TEXT_SIZE_KEY, 40);
		BookPageFactoryOnline.setFontSize(currentTextSize);
		pagefactory = BookPageFactoryOnline.build(MyApplication.SCREEN_WIDTH,
				readHeight);
		try {
			pagefactory.openBookWithContent(content);
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

		if (android.os.Build.VERSION.SDK_INT >= 16) {
			titleOut = new TranslateAnimation(Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, -ScreenUtil.dp2px(mContext, 48 + 24));
			titleOut.setDuration(400);

			titleIn = new TranslateAnimation(Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, 0, Animation.ABSOLUTE,
					-ScreenUtil.dp2px(mContext, 48 + 24), Animation.ABSOLUTE, 0);
			titleIn.setDuration(400);
		} else {
			titleIn = AnimationUtils.loadAnimation(this, R.anim.title_in);
			titleOut = AnimationUtils.loadAnimation(this, R.anim.title_out);
		}

		titleOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
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

		// TODO 获取该书的书签
		getBookMarks();

	}

	@SuppressLint("UseSparseArrays")
	private void getBookMarks() {
		bookmarks = SerializationUtil.readSerializationBookmarks(mContext,
				bookInfo.getBook_title());
		if (bookmarks == null) {
			bookmarks = new Bookmarks();
		}
		if (bookmarks.getBookmarks() == null) {
			bookmarks.setBookmarks(new HashMap<Integer, ArrayList<Bookmark>>());
		}
	}

	private void showChaptersBtn(Holder tag) {
		if (tag.m_isfirstPage
				&& currentChapter.getId() != bookInfo.getChapterInfos().get(0)
						.getId()) {
			pre_chapter.setVisibility(View.VISIBLE);
			next_chapter.setVisibility(View.GONE);
		} else if (tag.m_islastPage
				&& currentChapter.getId() != bookInfo.getChapterInfos()
						.get(bookInfo.getChapterInfos().size() - 1).getId()) {
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
			in.putExtra(CURRENTPAGE_KEY, currentChapter.getId());
			startActivity(in);
			break;
		case R.id.datemenu:// 白天黑夜
			MyPreferences.setIsDay(getApplicationContext(), v.isSelected());
			v.setSelected(!v.isSelected());
			if (v.isSelected()) {
				BookPageFactoryOnline.m_backColor = 0xff252525;
				BookPageFactoryOnline.m_textColor = 0xff555555;
				ScanView.shadowFromColor = 0xff000000;
				ScanView.shadowToColor = 0x00000000;
				dateView.setText("夜间");
			} else {
				BookPageFactoryOnline.m_backColor = dayBackColor;
				BookPageFactoryOnline.m_textColor = 0xff333333;
				ScanView.shadowFromColor = 0xffbbbbbb;
				ScanView.shadowToColor = 0x00bbbbbb;
				dateView.setText("日间");
			}
			Holder holder = (Holder) scanview.getCurrPage().getTag();
			pagefactory.reset();
			scanview.setAdapter(adapter, holder.start_index);
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
		case R.id.setting:
			settingView.setSelected(!settingView.isSelected());
			if (settingView.isSelected()) {
				menu_setting.setVisibility(View.VISIBLE);
			} else {
				menu_setting.setVisibility(View.GONE);
			}
			break;
		case R.id.pre_chapter:
			currentChapter = bookInfo.getChapterInfos().get(
					bookInfo.getChapterInfos().indexOf(currentChapter) - 1);
			getbookPreContent(currentChapter.getId() + "");

			// 添加书签
			break;
		case R.id.next_chapter:
			// 添加书签
			currentChapter = bookInfo.getChapterInfos().get(
					bookInfo.getChapterInfos().indexOf(currentChapter) + 1);
			getbookNextContent(currentChapter.getId() + "");
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
	 * 
	 * @param bookMark
	 */
	private void openBookMark(Bookmark bookmark) {

		currentChapter = findChapterInfoById(bookmark.getChapterId());
		if (openBook(currentChapter.getPath())) {
			pagefactory.reset();
			scanview.setAdapter(adapter, bookmark.getCharsetIndex());
			View currPage = scanview.getCurrPage();
			Holder tag = (Holder) currPage.getTag();
			showChaptersBtn(tag);
			updataProgress(tag);
		} else {
			// TODO 图书不存在
		}

	}

	private boolean getIsLocal(String chapterid) {
		String fileName = chapterid;

		File file = new File(GlobalParams.ebookDownloadPathString + "/"
				+ bookInfo.getBook_id() + "/" + fileName + ".txt");
		if (file.exists()) {
			DownloadManager downloadManager = DownloadService
					.getDownloadManager(getApplicationContext());
			DownloadInfo downloadInfo = null;

			for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
				DownloadInfo aDownloadInfo = downloadManager.getDownloadInfo(j);
				if (aDownloadInfo.getApp_idString() != null
						&& aDownloadInfo.getApp_idString().equals(
								bookInfo.getBook_id() + "")) {
					downloadInfo = downloadManager.getDownloadInfo(j);
					if (downloadInfo.getChapter_id().equals(
							readProgress.getChapterId() + "")) {
						if (downloadInfo.getState() == State.SUCCESS) {
							return true;
						}
					}
				}
			}

		}

		return false;
	}

	private void getbookPreContent(final String chapteridString) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					content = result;
					try {
						pagefactory.openBookWithContent(content);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pagefactory.reset();
					int beginIndex = pagefactory.measureBeginIndex(pagefactory
							.getFileLength());
					scanview.setAdapter(adapter, beginIndex);
					View currPage = scanview.getCurrPage();
					Holder tag = (Holder) currPage.getTag();
					showChaptersBtn(tag);
					updataProgress(tag);
				} else {
					Toast.makeText(getApplicationContext(), "读取漫画失败", 1).show();
					finish();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				EbookApi ebookApi = new EbookApi();
				String result = null;
				if (getIsLocal(chapteridString)) {
					result = FileUtils.read(getApplicationContext(),
							GlobalParams.ebookDownloadPathString + "/"
									+ bookInfo.getBook_id() + "/"
									+ chapteridString + ".txt");
				} else {
					result = ebookApi.getText(chapteridString);
				}

				return result;
			}
		}.execute("");
	}

	private void getbookNextContent(final String chapteridString) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					content = result;
					try {
						pagefactory.openBookWithContent(content);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					pagefactory.reset();

					scanview.setAdapter(adapter, 0);
					View currPage = scanview.getCurrPage();
					Holder tag = (Holder) currPage.getTag();
					showChaptersBtn(tag);
					updataProgress(tag);
				} else {
					Toast.makeText(getApplicationContext(), "读取漫画失败", 1).show();
					finish();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				EbookApi ebookApi = new EbookApi();
				String result = null;
				if (getIsLocal(chapteridString)) {
					result = FileUtils.read(getApplicationContext(),
							GlobalParams.ebookDownloadPathString + "/"
									+ bookInfo.getBook_id() + "/"
									+ chapteridString + ".txt");
				} else {
					result = ebookApi.getText(chapteridString);
				}
				return result;
			}
		}.execute("");
	}

	private boolean openBook(String path) {

		if (TextUtils.isEmpty(path)) {
			showShortToast("图书不存在");
			return false;
		}
		File file = new File(path);
		if (file.exists() && file.isFile() && file.length() > 0) {
			try {
				pagefactory.openBookWithContent(content);
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
		BookPageFactoryOnline.setFontSize(currentTextSize);
		pagefactory.reset();
		scanview.setAdapter(adapter, holder.start_index);
		PreferenceHelper.putInt(TEXT_SIZE_KEY, currentTextSize);
	}

	private void showMenu() {
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			quitFullScreen();
		}
		title.setVisibility(View.VISIBLE);
		title.startAnimation(titleIn);
		menu.setVisibility(View.VISIBLE);
		menu.startAnimation(menuIn);
		isMenuShowing = true;
	}

	private void hideMenu() {
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			setFullScreen();
		}
		menu_setting.setVisibility(View.GONE);
		settingView.setSelected(false);
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
		readProgress.setChapterName(currentChapter.getTitle());
		readProgress.setCharsetIndex(tag.start_index);
	}

	private void addBookmark() {
		// TODO 添加书签
		ArrayList<Bookmark> markList = bookmarks.getBookmarks().get(
				currentChapter.getId());
		if (markList == null) {
			markList = new ArrayList<Bookmark>();
			bookmarks.getBookmarks().put(currentChapter.getId(), markList);
		}

		Holder tag = (Holder) scanview.getCurrPage().getTag();

		Bookmark bookmark = new Bookmark();
		bookmark.setChapterId(currentChapter.getId());
		bookmark.setChapterName(currentChapter.getTitle());
		bookmark.setCharsetIndex(tag.start_index);
		bookmark.setTime(System.currentTimeMillis());

		if (markList.contains(bookmark)) {
			markList.remove(bookmark);
			markList.add(0, bookmark);
			SerializationUtil.wirteSerializationBookmarks(mContext, bookmarks,
					bookInfo.getBook_title());
			return;
		}

		while (markList.size() >= 20) {
			markList.remove(markList.size() - 1);
		}
		markList.add(0, bookmark);
		SerializationUtil.wirteSerializationBookmarks(mContext, bookmarks,
				bookInfo.getBook_title());
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
			PreferenceHelper
					.putString(bookInfo.getBook_id() + "", progressInfo);

			L.v(TAG, "onDestroy", "progressInfo : " + progressInfo);

			readProgress = null;
		}
		if (pagefactory != null) {
			pagefactory.close();
			pagefactory = null;
		}

	}

	private void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		// WindowManager.LayoutParams lp = getWindow().getAttributes();
		// lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// getWindow().setAttributes(lp);
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	@SuppressLint({ "NewApi", "InlinedApi" })
	private void quitFullScreen() {
		final WindowManager.LayoutParams attrs = getWindow().getAttributes();
		attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setAttributes(attrs);
		getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		root.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}

}
