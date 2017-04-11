package com.bccv.boxcomic.ebook;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.api.EbookApi;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.ebook.ScanView.onMoveListener;
import com.bccv.boxcomic.ebook.ScanViewAdapter.Holder;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.MyApplication;
import com.bccv.boxcomic.tool.MyPreferences;
import com.bccv.boxcomic.tool.SerializationUtil;
import com.bccv.boxcomic.tool.SystemUtils;
import com.igexin.getuiext.a.i;
import com.lidroid.xutils.http.HttpHandler.State;

@SuppressLint("WrongCall")
public class EbookActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "PageActivity";

	public static final String BOOK_INFO_KEY = "book_info";
	public static final String TEXT_SIZE_KEY = "text_size_key";
	public static final String BOOKMARK_KEY = "bookmark_key";
	public static final String BOOKNAME_KEY = "bookname_key";
	public static final String BOOKINFO_KEY = "bookinfo_key";
	public static final String CURRENTPAGE_KEY = "currentpage_key";

	// 小说数据
	private BookInfo bookInfo;
	private Bookmark readProgress;
	private ChapterInfo currentChapter;
	private Bookmarks bookmarks;

	private boolean isLocal;
	private String content;
	private String preContent = null;
	private boolean hasPre = false;
	private String nextContent = null;
	private boolean hasNext = false;
	private static final int MIN_TEXT_SIZE = 30;
	private static final int MAX_TEXT_SIZE = 50;
	private int currentTextSize = 40;

	// 小说菜单
	public View root, title, menu, menu_setting;
	public ImageView leftBtn, rightBtn;
	public TextView titleName;
	public TextView dateView, magnify_text_size, decrease_text_size,
			settingView;
	private boolean isMenuShowing = false;
	private SeekBar seekBar;
	private TextView normalTextView;
	private TextView huyanTextView;
	private TextView niupiTextView;

	private Animation menuIn, menuOut, titleIn, titleOut;

	private int dayBackColor;

	// 小说阅读
	EbookView ebookView;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	BookPageFactory pagefactory;
	RelativeLayout scanView;
	private Context mContext;

	private int screenWidth;
	private int screenHeight;
	
	private int range = 3;

	private boolean canTouch = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		GlobalParams.pageActivity = this;

		mContext = getApplicationContext();

		// 去掉系统消息栏
		if (android.os.Build.VERSION.SDK_INT >= 16) {
			setFullScreen();
		}
		// 保持屏幕常亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.activity_ebook);

		setBookView();
		initView();
		initAnimation();

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

		String chapteridString = "";
		if (readProgress != null) {
			chapteridString = readProgress.getChapterId() + "";
		}
		if (isLocalData(chapteridString)) {
			getLocalData();
		} else {
			getData();
		}
	}

	private void setBookView() {
		ebookView = new EbookView(this);
		root = findViewById(R.id.root);
		scanView = (RelativeLayout) findViewById(R.id.scanview);
		scanView.addView(ebookView);

		screenWidth = MyApplication.SCREEN_WIDTH;
		screenHeight = MyApplication.SCREEN_HEIGHT;

		ebookView.setScreen(screenWidth, screenHeight);

		mCurPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);

		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		pagefactory = new BookPageFactory(screenWidth, screenHeight);

		int backcolor = MyPreferences.getBackColor(getApplicationContext());
		switch (backcolor) {
		case 0:
			dayBackColor = 0xfff0f0f0;
			break;
		case 1:
			dayBackColor = 0xffcce3d4;
			break;
		case 2:
			dayBackColor = 0xffe6d2b5;
			break;
		default:
			break;
		}

		boolean isDay = MyPreferences.isDay(getApplicationContext());
		if (!isDay) {
			pagefactory.setBackGroundColor(0xff252525);
			ebookView.backColor = 0xff252525;
			pagefactory.setTextColor(0xff555555);
		} else {
			pagefactory.setBackGroundColor(dayBackColor);
			ebookView.backColor = dayBackColor;
			pagefactory.setTextColor(0xff333333);
		}
		// TODO 读取sp中的字体大小
		currentTextSize = PreferenceHelper.getInt(TEXT_SIZE_KEY, 40);
		pagefactory.setFontSize(currentTextSize);

		pagefactory.reset();
		pagefactory.drawBlank(mCurPageCanvas);

		ebookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);

		ebookView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				// TODO Auto-generated method stub

				boolean ret = false;
				if (v == ebookView) {
					if (e.getAction() == MotionEvent.ACTION_DOWN) {
						if (e.getX() > screenWidth / range
								&& e.getX() < screenWidth * (range - 1) / range
								&& e.getY() > screenHeight / range
								&& e.getY() < screenHeight * (range - 1) / range) {
							return false;
						}
						if (isMenuShowing) {
							return false;
						}
						
						if (!canTouch) {
							return false;
						}
						
						ebookView.abortAnimation();
						ebookView.calcCornerXY(e.getX(), e.getY());
						if (pagefactory.isBlank) {
							pagefactory.drawBlank(mCurPageCanvas);
						} else {
							pagefactory.draw(mCurPageCanvas);
						}

						if (ebookView.DragToRight()) {
							try {
								if (pagefactory.isBlank) {

								} else {
									pagefactory.prePage();
								}

							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.isfirstPage()) {
								if (pagefactory.isBlank) {
									return false;
								}
								if (hasPre) {
									if (!initPreBook()) {
										canTouch = false;
										pagefactory.drawBlank(mNextPageCanvas);
										ScrollOverCallback scrollOverCallback = new ScrollOverCallback() {

											@Override
											public void callback() {
												// TODO Auto-generated method stub
												getbookPreContent();
												canTouch = true;
											}
										};
										ebookView
												.setScrollOverCallback(scrollOverCallback);
									}
									
								} else {
									canTouch = false;
									pagefactory.drawBlank(mNextPageCanvas);
									ScrollOverCallback scrollOverCallback = new ScrollOverCallback() {

										@Override
										public void callback() {
											// TODO Auto-generated method stub
											getbookPreContent();
											canTouch = true;
										}
									};
									ebookView
											.setScrollOverCallback(scrollOverCallback);
								}

							} else {
								pagefactory.draw(mNextPageCanvas);
							}

						} else {
							try {
								if (pagefactory.isBlank) {

								} else {
									pagefactory.nextPage();
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (pagefactory.islastPage()) {
								if (pagefactory.isBlank) {
									return false;
								}
								if (hasNext) {
									if (!initNextBook()) {
										canTouch = false;
										pagefactory.drawBlank(mNextPageCanvas);
										ScrollOverCallback scrollOverCallback = new ScrollOverCallback() {

											@Override
											public void callback() {
												// TODO Auto-generated method stub
												getbookNextContent();
												canTouch = true;
											}
										};
										ebookView
												.setScrollOverCallback(scrollOverCallback);
									}

								} else {
									canTouch = false;
									pagefactory.drawBlank(mNextPageCanvas);
									ScrollOverCallback scrollOverCallback = new ScrollOverCallback() {

										@Override
										public void callback() {
											// TODO Auto-generated method stub
											getbookNextContent();
											canTouch = true;
										}
									};
									ebookView
											.setScrollOverCallback(scrollOverCallback);
								}

							} else {
								pagefactory.draw(mNextPageCanvas);
							}
						}
						if (!pagefactory.isBlank) {
							if (readProgress != null) {
								readProgress.setCharsetIndex(pagefactory
										.getBeginIndex());
							}
						}
						ebookView.setBitmaps(mCurPageBitmap, mNextPageBitmap);
					}

					ret = ebookView.doTouchEvent(e);
					return ret;
				}
				return false;
			}

		});
	}

	public interface ScrollOverCallback {
		public void callback();
	}

	private void initBook(boolean isPre, boolean isNext) {
		titleName.setText(bookInfo.getBook_title());

		try {
			pagefactory.openBookWithContent(content);
			if (isPre) {
				int begin = pagefactory.measureBeginIndex(pagefactory
						.getFileLength());
				readProgress.setCharsetIndex(begin);
				pagefactory.setBeginIndex(begin);
				pagefactory.draw(mNextPageCanvas);
				ebookView.setBitmaps(mNextPageBitmap, mNextPageBitmap);
			} else if (isNext) {
				pagefactory.setBeginIndex(0);
				pagefactory.draw(mNextPageCanvas);
				ebookView.setBitmaps(mNextPageBitmap, mNextPageBitmap);
			} else {
				pagefactory.setBeginIndex(readProgress.getCharsetIndex());
				pagefactory.draw(mCurPageCanvas);
				ebookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);
			}

			ebookView.invalidate();
		} catch (IOException e) {
			showShortToast("图书不存在");
			finish();
			return;
		}

	}

	private boolean initPreBook() {
		int index = bookInfo.getChapterInfos().indexOf(currentChapter);
		if (index == 0) {
			return false;
		}
		currentChapter = bookInfo.getChapterInfos().get(index - 1);
		if (readProgress == null) {
			readProgress = new Bookmark();
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		} else {
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		}
		nextContent = content;
		hasNext = true;
		content = preContent;
		try {
			pagefactory.openBookWithContent(content);
			int begin = pagefactory.measureBeginIndex(pagefactory
					.getFileLength());
			readProgress.setCharsetIndex(begin);
			pagefactory.setBeginIndex(begin);
			pagefactory.draw(mNextPageCanvas);

		} catch (IOException e) {
			return false;
		}
		getPreContent();
		return true;
	}

	private boolean initNextBook() {
		int index = bookInfo.getChapterInfos().indexOf(currentChapter);
		if (index == bookInfo.getChapterInfos().size() - 1) {
			return false;
		}
		currentChapter = bookInfo.getChapterInfos().get(index + 1);
		if (readProgress == null) {
			readProgress = new Bookmark();
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		} else {
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		}
		preContent = content;
		hasPre = true;
		content = nextContent;
		try {
			pagefactory.openBookWithContent(content);
			pagefactory.setBeginIndex(0);
			pagefactory.draw(mNextPageCanvas);

		} catch (IOException e) {
			return false;
		}
		getNextContent();
		return true;
	}

	private ChapterInfo findChapterInfoById(int ChapterId) {

		for (ChapterInfo info : bookInfo.getChapterInfos()) {
			if (info.getId() == ChapterId) {
				return info;
			}
		}
		return null;
	}

	private void initAnimation() {
		menuIn = AnimationUtils.loadAnimation(this, R.anim.menu_pop_in);
		menuOut = AnimationUtils.loadAnimation(this, R.anim.menu_pop_out);

		if (android.os.Build.VERSION.SDK_INT >= 16) {
			titleOut = new TranslateAnimation(Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, -ScreenUtil.dp2px(mContext, 48 + 24));
			titleOut.setDuration(200);

			titleIn = new TranslateAnimation(Animation.ABSOLUTE, 0,
					Animation.ABSOLUTE, 0, Animation.ABSOLUTE,
					-ScreenUtil.dp2px(mContext, 48 + 24), Animation.ABSOLUTE, 0);
			titleIn.setDuration(200);
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
				ebookView.canDraw = true;
			}
		});
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

		title.setOnClickListener(this);
		menu.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		dateView.setOnClickListener(this);
		magnify_text_size.setOnClickListener(this);
		decrease_text_size.setOnClickListener(this);
		settingView.setOnClickListener(this);

		scanView.setOnClickListener(this);
		initSetting();
	}

	private void initSetting() {
		int backcolor = MyPreferences.getBackColor(getApplicationContext());

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		float brightness = SystemUtils
				.getCurrentActivityBrightness(EbookActivity.this);
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
				SystemUtils.setCurrentActivityBrightness(EbookActivity.this,
						(float) progress / 255);
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
					pagefactory.setBackGroundColor(0xfff0f0f0);
					ebookView.backColor = 0xfff0f0f0;
					// 刷新
					pagefactory.reset();
					pagefactory.draw(mCurPageCanvas);
					ebookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);
					ebookView.invalidate();
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
				// 刷新
				if (!dateView.isSelected()) {
					pagefactory.setBackGroundColor(0xffcce3d4);
					ebookView.backColor = 0xffcce3d4;
					// 刷新
					pagefactory.reset();
					pagefactory.draw(mCurPageCanvas);
					ebookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);
					ebookView.invalidate();
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
					pagefactory.setBackGroundColor(0xffe6d2b5);
					ebookView.backColor = 0xffe6d2b5;
					// 刷新
					pagefactory.reset();
					pagefactory.draw(mCurPageCanvas);
					ebookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);
					ebookView.invalidate();
				}
			}
		});

		boolean isDay = MyPreferences.isDay(getApplicationContext());
		if (!isDay) {
			pagefactory.setBackGroundColor(0xff252525);
			ebookView.backColor = 0xff252525;
			pagefactory.setTextColor(0xff555555);
			dateView.setText("夜间");
			dateView.setSelected(true);
		} else {
			pagefactory.setBackGroundColor(dayBackColor);
			ebookView.backColor = dayBackColor;
			pagefactory.setTextColor(0xff333333);
			dateView.setText("日间");
		}

	}

	private boolean isLocalData(String chapterid) {

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
					if (readProgress == null) {
						readProgress = new Bookmark();
						ChapterInfo chapterInfo = bookInfo.getChapterInfos()
								.get(0);
						readProgress.setChapterId(chapterInfo.getId());
						readProgress.setChapterName(chapterInfo.getTitle());
						readProgress.setCharsetIndex(0);
					}

					currentChapter = findChapterInfoById(readProgress
							.getChapterId());
					initBook(false, false);
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
					if (readProgress == null) {
						readProgress = new Bookmark();
						ChapterInfo chapterInfo = bookInfo.getChapterInfos()
								.get(0);
						readProgress.setChapterId(chapterInfo.getId());
						readProgress.setChapterName(chapterInfo.getTitle());
						readProgress.setCharsetIndex(0);
					}

					currentChapter = findChapterInfoById(readProgress
							.getChapterId());
					initBook(false, false);
					getNextContent();
					getPreContent();
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

	private void getPreContent() {
		preContent = null;
		hasPre = false;
		int index = bookInfo.getChapterInfos().indexOf(currentChapter);
		if (index == 0) {
			return;
		}
		final ChapterInfo chapter = bookInfo.getChapterInfos().get(index - 1);
		final String chapteridString = chapter.getId() + "";
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					if (!chapteridString.equals(currentChapter.getId() + "")) {
						preContent = result;
						hasPre = true;
					}

				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				EbookApi ebookApi = new EbookApi();
				String result = null;
				if (isLocalData(chapteridString)) {
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

	private void getNextContent() {
		nextContent = null;
		hasNext = false;
		int index = bookInfo.getChapterInfos().indexOf(currentChapter);
		if (index == bookInfo.getChapterInfos().size() - 1) {
			return;
		}
		ChapterInfo chapter = bookInfo.getChapterInfos().get(index + 1);
		final String chapteridString = chapter.getId() + "";
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					if (!chapteridString.equals(currentChapter.getId() + "")) {
						nextContent = result;
						hasNext = true;
					}
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				EbookApi ebookApi = new EbookApi();
				String result = null;
				if (isLocalData(chapteridString)) {
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

	private void getbookPreContent() {
		int index = bookInfo.getChapterInfos().indexOf(currentChapter);
		if (index == 0) {
			Toast.makeText(mContext, "没有上一章了", 1).show();
			return;
		}
		currentChapter = bookInfo.getChapterInfos().get(index - 1);
		if (readProgress == null) {
			readProgress = new Bookmark();
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		} else {
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		}
		final String chapteridString = currentChapter.getId() + "";

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					nextContent = content;
					hasNext = true;
					content = result;
					initBook(true, false);
					getPreContent();
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
				String result = null;
				EbookApi ebookApi = new EbookApi();

				if (isLocalData(chapteridString)) {
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

	private void getbookNextContent() {
		int index = bookInfo.getChapterInfos().indexOf(currentChapter);
		if (index == bookInfo.getChapterInfos().size() - 1) {
			Toast.makeText(mContext, "没有下一章了", 1).show();
			return;
		}
		currentChapter = bookInfo.getChapterInfos().get(index + 1);
		if (readProgress == null) {
			readProgress = new Bookmark();
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		} else {
			ChapterInfo chapterInfo = currentChapter;
			readProgress.setChapterId(chapterInfo.getId());
			readProgress.setChapterName(chapterInfo.getTitle());
			readProgress.setCharsetIndex(0);
		}
		final String chapteridString = currentChapter.getId() + "";
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result != null) {
					preContent = content;
					hasPre = true;
					content = result;
					initBook(false, true);
					getNextContent();
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
				String result = null;
				EbookApi ebookApi = new EbookApi();
				if (isLocalData(chapteridString)) {
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

	private void setFullScreen() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (pagefactory.isBlank) {
			return;
		}
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
				pagefactory.setBackGroundColor(0xff252525);
				ebookView.backColor = 0xff252525;
				pagefactory.setTextColor(0xff555555);
				dateView.setText("夜间");
			} else {
				pagefactory.setBackGroundColor(dayBackColor);
				ebookView.backColor = dayBackColor;
				pagefactory.setTextColor(0xff333333);
				dateView.setText("日间");
			}
			pagefactory.reset();
			pagefactory.draw(mCurPageCanvas);
			ebookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);
			ebookView.invalidate();
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

	private void changeTextSize() {
		pagefactory.setFontSize(currentTextSize);
		pagefactory.reset();
		pagefactory.draw(mCurPageCanvas);
		ebookView.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		ebookView.invalidate();
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
		ebookView.canDraw = false;
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
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
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (pagefactory != null) {
			pagefactory.close();
			pagefactory = null;
		}
	}
	
	
}
