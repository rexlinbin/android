package com.bccv.boxcomic.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.ant.liao.GifView;
import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ReadComicActivity.BatteryBroadcastReceiver;
import com.bccv.boxcomic.activity.ReadComicActivity.LosePageCallback;
import com.bccv.boxcomic.activity.ReadComicActivity.OnFragmentClickListener;
import com.bccv.boxcomic.adapter.ComicAdapter;
import com.bccv.boxcomic.adapter.ReadComicPagerAdapter;
import com.bccv.boxcomic.api.ComicApi;
import com.bccv.boxcomic.api.CommentApi;
import com.bccv.boxcomic.api.SystemApi;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.modal.ComicPic;
import com.bccv.boxcomic.modal.Comment;
import com.bccv.boxcomic.photoview.log.Logger;
import com.bccv.boxcomic.tool.AppConfig;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.BaseFragmentActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.DialogUtils;
import com.bccv.boxcomic.tool.DimensionPixelUtil;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.ImageUtils;
import com.bccv.boxcomic.tool.MyComparator;
import com.bccv.boxcomic.tool.SerializationUtil;
import com.bccv.boxcomic.tool.StringUtils;
import com.bccv.boxcomic.tool.SystemUtils;
import com.bccv.boxcomic.view.MyDigitalClock;
import com.lidroid.xutils.http.HttpHandler.State;

public class ReadComicLocalActivity extends BaseFragmentActivity {
	private RelativeLayout readComicRelativeLayout;

	private LinearLayout comicTitleLayout;
	private TextView pagesTextView;
	private ProgressBar batteryImageView;
	private TextView batteryTextView;
	private TextView netStateTextView;
	private LinearLayout comicMenuLayout;
	private SeekBar seekBar;

	private RelativeLayout readingLayout;

	private View blankView;
	private TextView pageChangeTextView;

	private boolean isShowingMenu = true;
	private boolean canDanmu = false;
	private boolean isLandscape = false;

	private List<Comment> comments;
	private int danmuNum = 0;
	private Timer timer = new Timer();

	private List<ComicPic> list;

	private List<ComicPic> getList;

	private ListView comicListView;
	private ComicAdapter adapter;

	private ViewPager viewPager;
	private ReadComicPagerAdapter pagerAdapter;

	private boolean isPage = true;
	private boolean isHigh = true;
	private boolean isTranslateStart = false;
	private boolean isTouch = false;
	private int pageNum = 0;
	private int currPageNum = 0;

	private Comic comic;
	private Chapter chapter;

	private List<Comic> bookmarkComicList;
	private List<Chapter> bookmarkList;

	private boolean canNext = true;
	private boolean canPre = true;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		GlobalParams.canDoubleClick = false;

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		GlobalParams.readComicLocalActivity = this;

		setContentView(R.layout.activity_readcomic);

		readComicRelativeLayout = (RelativeLayout) findViewById(R.id.readComic_relativeLayout);

		comic = (Comic) getIntent().getExtras().getSerializable("comic");
		chapter = (Chapter) getIntent().getExtras().getSerializable("chapter");

		blankView = findViewById(R.id.blank_view);
		pageChangeTextView = (TextView) findViewById(R.id.pageChange_textView);
		pageChangeTextView.setVisibility(View.GONE);

		readingLayout = (RelativeLayout) findViewById(R.id.reading_layout);
		readingLayout.setVisibility(View.VISIBLE);

		GifView gifView = (GifView) findViewById(R.id.gifView);
		gifView.setGifImage(R.drawable.loading_center);

		if (savedInstanceState != null) {
			isTranslateStart = true;
			currPageNum = savedInstanceState.getInt("listNum");
			pageNum = savedInstanceState.getInt("pageNum");
			isPage = savedInstanceState.getBoolean("isPage");
			canDanmu = savedInstanceState.getBoolean("canDanmu");
			isHigh = savedInstanceState.getBoolean("isHigh");
			isLandscape = savedInstanceState.getBoolean("isLandscape");
			list = (List<ComicPic>) savedInstanceState.getSerializable("list");
		} else {
			pageNum = chapter.getChapter_count();
			if (pageNum == 0) {
				currPageNum = -1;
			} else {
				currPageNum = chapter.getBookmarkNum();
			}
		}

		comicTitleLayout = (LinearLayout) findViewById(R.id.comicTitle_layout);
		comicMenuLayout = (LinearLayout) findViewById(R.id.comicMenu_layout);

		setTitle();

		setMenu();

		if (isLandscape) {
			setListView();

		} else {
			setViewPager();

		}

		blankView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (list != null && list.size() > 0) {
					hideMenu();
				}

			}
		});

		if (GlobalParams.isLanscape != isLandscape) {
			isLandscape = GlobalParams.isLanscape;
			if (!GlobalParams.isLanscape) {
				ReadComicLocalActivity.this
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else {
				ReadComicLocalActivity.this
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}

		} else {

			setData();

		}

	}

	public void setClick(float x, float y) {
		WindowManager wm = this.getWindowManager();

		final int width = wm.getDefaultDisplay().getWidth();

		if (x > width / 4 && x < width * 3 / 4) {
			if (isShowingMenu) {
				hideMenu();
			} else {
				showMenu();
			}
		} else if (x < width / 4) {
			if (currPageNum != 0) {
				viewPager.setCurrentItem(currPageNum - 1);
			} else {
				if (!StringUtils.isEmpty(GlobalParams.preComicString)) {
					getPre();
				} else {
					Toast.makeText(getApplicationContext(), "已经是第一章了", 1)
							.show();
				}
			}

		} else if (x > width * 3 / 4) {
			if (currPageNum != pageNum - 1) {
				viewPager.setCurrentItem(currPageNum + 1);
			} else {
				if (!StringUtils.isEmpty(GlobalParams.nextComicString)) {
					getNext();
				} else {
					Toast.makeText(getApplicationContext(), "已经是最后一章了", 1)
							.show();
				}
			}
		}
	}

	private void setListView() {
		comicListView = (ListView) findViewById(R.id.comic_listView);
		comicListView
				.setSelector(new ColorDrawable(android.R.color.transparent));
		comicListView.setDivider(new ColorDrawable(R.color.gray));
		comicListView.setDividerHeight((int) DimensionPixelUtil.dip2px(
				getApplicationContext(), 5));

		if (list == null) {
			list = new ArrayList<ComicPic>();
		}

		LosePageCallback losePageCallback = new LosePageCallback() {

			@Override
			public void callback(String page) {
				// TODO Auto-generated method stub
				sendLosePage(comic.getComic_id(), chapter.getChapter_id(), page);
			}
		};

		adapter = new ComicAdapter(list, getApplicationContext(),
				new OnTouchListener() {
					boolean isMove = false;

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_UP) {
							setListClick(event.getX(), event.getY());
						} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
							isMove = true;
						}

						return true;
					}
				}, losePageCallback, true);
		comicListView.setAdapter(adapter);

		comicListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					if (!isTouch) {
						if (isLastItemVisible()) {
							// getNext();
							currPageNum = view.getLastVisiblePosition();
							pagesTextView.setText((currPageNum + 1) + "/"
									+ pageNum);
							seekBar.setProgress(currPageNum);
						} else if (isFirstItemVisible()) {
							// getPre();
						} else {
							currPageNum = view.getFirstVisiblePosition();
							pagesTextView.setText((currPageNum + 1) + "/"
									+ pageNum);
							seekBar.setProgress(currPageNum);
						}

					}

					break;

				default:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});

		comicListView.setEnabled(false);
	}

	public void setListClick(float x, float y) {
		WindowManager wm = this.getWindowManager();

		final int height = wm.getDefaultDisplay().getHeight();

		if (y > height / 4 && y < height * 3 / 4) {
			if (isShowingMenu) {
				hideMenu();
			} else {
				showMenu();
			}
		} else if (y < height / 4
				&& comicListView.getFirstVisiblePosition() == 0) {

			if (!StringUtils.isEmpty(GlobalParams.preComicString)) {
				getPre();
			} else {
				Toast.makeText(getApplicationContext(), "已经是第一章了", 1).show();
			}

		} else if (y > height * 3 / 4
				&& (comicListView.getLastVisiblePosition() == list.size() - 1)) {

			if (!StringUtils.isEmpty(GlobalParams.nextComicString)) {
				getNext();
			} else {
				Toast.makeText(getApplicationContext(), "已经是最后一章了", 1).show();
			}

		}
	}

	/**
	 * 判断第一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = comicListView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		int mostTop = (comicListView.getChildCount() > 0) ? comicListView
				.getChildAt(0).getTop() : 0;
		if (mostTop >= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 判断最后一个child是否完全显示出来
	 * 
	 * @return true完全显示出来，否则false
	 */
	private boolean isLastItemVisible() {
		final Adapter adapter = comicListView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		final int lastItemPosition = adapter.getCount() - 1;
		final int lastVisiblePosition = comicListView.getLastVisiblePosition();

		/**
		 * This check should really just be: lastVisiblePosition ==
		 * lastItemPosition, but ListView internally uses a FooterView which
		 * messes the positions up. For me we'll just subtract one to account
		 * for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition >= lastItemPosition - 1) {
			final int childIndex = lastVisiblePosition
					- comicListView.getFirstVisiblePosition();
			final int childCount = comicListView.getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = comicListView.getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= comicListView
						.getBottom();
			}
		}

		return false;
	}

	private void setViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		OnFragmentClickListener onFragmentClickListener = new OnFragmentClickListener() {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				if (isShowingMenu) {
					hideMenu();
				} else {
					showMenu();
				}
			}
		};

		if (list == null) {
			list = new ArrayList<ComicPic>();
		}
		pagerAdapter = new ReadComicPagerAdapter(getSupportFragmentManager(),
				list, onFragmentClickListener, true);
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (currPageNum == arg0 && arg0 == pageNum - 1) {
					// getNext();
				} else if (currPageNum == arg0 && arg0 == 0) {
					// getPre();
				} else {
					currPageNum = arg0;
					pagesTextView.setText((currPageNum + 1) + "/" + pageNum);
					seekBar.setProgress(currPageNum);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		viewPager.setCurrentItem(currPageNum);

	}

	private void registerBattery() {
		registerReceiver(batteryBroadcastReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	private void unregisterBattery() {
		unregisterReceiver(batteryBroadcastReceiver);
	}

	private BatteryBroadcastReceiver batteryBroadcastReceiver = new BatteryBroadcastReceiver();

	/** 接受电量改变广播 */
	class BatteryBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

				int level = intent.getIntExtra("level", 0);
				batteryImageView.setProgress(level);
				batteryTextView.setText(level + "%");
			}
		}
	}

	private void setData() {
		canShowWebDialog = false;
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					list.clear();
					list.addAll(getList);
				}
				if (isLandscape) {
					adapter.notifyDataSetChanged();
				} else {
					pagerAdapter.notifyDataSetChanged();
				}
				pageNum = list.size();
				if (pageNum > 0) {
					if (!isTranslateStart) {
						if (currPageNum == -1) {
							currPageNum = 0;
						} else {
							currPageNum = chapter.getBookmarkNum();
						}
					}

					pagesTextView.setText((currPageNum + 1) + "/" + pageNum);

					if (isLandscape) {
						comicListView.setSelection(currPageNum);
					} else {
						viewPager.setCurrentItem(currPageNum);
					}
				}
				readingLayout.setVisibility(View.GONE);
			}
		};
		new DataAsyncTask(callback, false) {
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				if (list.size() != 0) {
					getList = null;
					return null;
				}

				String FileNameString = chapter.getChapter_id();

				String localPathString = GlobalParams.localComicPathString
						+ "/" + comic.getComic_id() + "/";

				List<File> chapterFiles = FileUtils
						.listPathDirs(localPathString);
				int position = 0;
				for (int i = 0; i < chapterFiles.size(); i++) {
					File file = chapterFiles.get(i);
					if (file.getName().equals(FileNameString)) {
						position = i;
						break;
					}
				}
				if (position + 1 < chapterFiles.size()) {
					GlobalParams.nextComicString = chapterFiles.get(
							position + 1).getName();
				} else {
					GlobalParams.nextComicString = "";
				}
				if (position - 1 >= 0) {
					GlobalParams.preComicString = chapterFiles
							.get(position - 1).getName();
				} else {
					GlobalParams.preComicString = "";
				}

				List<File> files = FileUtils.listPathFiles(localPathString
						+ FileNameString);

				MyComparator myComparator = new MyComparator();
				Collections.sort(files, myComparator);
				try {
					getList = new ArrayList<ComicPic>();
					for (int i = 0; i < files.size(); i++) {
						File file = files.get(i);
						Uri uri = Uri.fromFile(file);
						Bitmap bitmap = ImageUtils.getBitmapByFile(file);
						ComicPic comicPic = new ComicPic();
						comicPic.setUrl(uri.toString());
						comicPic.setHeight(bitmap.getHeight());
						comicPic.setWidth(bitmap.getWidth());
						comicPic.setLocal(true);
						getList.add(comicPic);
						bitmap.recycle();
						bitmap = null;
					}

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				if (comments == null) {
					comments = new ArrayList<Comment>();
				}
				return null;
			}
		}.executeProxy("");
	}

	public void fragmentSend(String page) {
		sendLosePage(comic.getComic_id(), chapter.getChapter_id(), page);
	}

	private void sendLosePage(final String bookid, final String chapterid,
			final String page) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				SystemApi systemApi = new SystemApi();
				systemApi.sendLosePage(bookid, chapterid, page);
				return null;
			}
		}.execute("");
	}

	private void getNext() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position = 0;
				Chapter preChapter = new Chapter();
				List<Chapter> getcChapters = comic.getChapters();
				for (int i = 0; i < getcChapters.size(); i++) {
					preChapter = getcChapters.get(i);
					if (preChapter.getChapter_id().equals(
							GlobalParams.nextComicString)) {
						position = i;
						break;
					}
				}

				List<Comic> chapterList = SerializationUtil
						.readNewestHistoryCache(getApplicationContext());
				if (chapterList == null) {
					chapterList = new ArrayList<Comic>();
				}

				comic.setHistoryChapter(position);
				comic.setHistoryChapterTitleString(preChapter
						.getChapter_title());
				boolean hasComic = true;
				int bookmark = 0;
				for (int i = 0; i < chapterList.size(); i++) {
					Comic historyComic = chapterList.get(i);
					if (historyComic.getComic_id().equals(comic.getComic_id())) {
						if (historyComic.getHistoryChapter() != position) {
							historyComic.setHistoryChapter(position);
							historyComic.setHistoryChapterPage(0);
							historyComic
									.setHistoryChapterTitleString(preChapter
											.getChapter_title());
						} else {
							bookmark = historyComic.getHistoryChapterPage();
						}
						hasComic = false;
					}
				}
				if (hasComic) {
					chapterList.add(0, comic);
				}

				SerializationUtil.wirteNewestHistorySerialization(
						getApplicationContext(), (Serializable) chapterList);

				preChapter.setBookmarkNum(bookmark);
				Intent intent = new Intent(ReadComicLocalActivity.this,
						ReadComicLocalActivity.class);
				intent.putExtra("comic", comic);
				intent.putExtra("chapter", preChapter);
				startActivity(intent);
				finish();
			}
		};

		DialogUtils
				.showNextDialog(ReadComicLocalActivity.this, onClickListener);
	}

	private void getPre() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position = 0;
				Chapter preChapter = new Chapter();
				List<Chapter> getcChapters = comic.getChapters();
				for (int i = 0; i < getcChapters.size(); i++) {
					preChapter = getcChapters.get(i);
					if (preChapter.getChapter_id().equals(
							GlobalParams.preComicString)) {
						position = i;
						break;
					}
				}

				List<Comic> chapterList = SerializationUtil
						.readNewestHistoryCache(getApplicationContext());
				if (chapterList == null) {
					chapterList = new ArrayList<Comic>();
				}

				comic.setHistoryChapter(position);
				comic.setHistoryChapterTitleString(preChapter
						.getChapter_title());
				boolean hasComic = true;
				int bookmark = 0;
				for (int i = 0; i < chapterList.size(); i++) {
					Comic historyComic = chapterList.get(i);
					if (historyComic.getComic_id().equals(comic.getComic_id())) {
						if (historyComic.getHistoryChapter() != position) {
							historyComic.setHistoryChapter(position);
							historyComic.setHistoryChapterPage(0);
							historyComic
									.setHistoryChapterTitleString(preChapter
											.getChapter_title());
						} else {
							bookmark = historyComic.getHistoryChapterPage();
						}

						hasComic = false;
					}
				}
				if (hasComic) {
					chapterList.add(0, comic);
				}

				SerializationUtil.wirteNewestHistorySerialization(
						getApplicationContext(), (Serializable) chapterList);

				preChapter.setBookmarkNum(bookmark);
				Intent intent = new Intent(ReadComicLocalActivity.this,
						ReadComicLocalActivity.class);
				intent.putExtra("comic", comic);
				intent.putExtra("chapter", preChapter);
				startActivity(intent);
				finish();
			}
		};

		DialogUtils.showPreDialog(ReadComicLocalActivity.this, onClickListener);
	}

	private void showMenu() {
		blankView.setVisibility(View.VISIBLE);
		if (!isShowingMenu) {
			isShowingMenu = true;
			if (isLandscape) {
				comicListView.setEnabled(false);
			} else {
				viewPager.setEnabled(false);
			}

			comicMenuLayout.setVisibility(View.VISIBLE);
			comicTitleLayout.setVisibility(View.VISIBLE);
			comicTitleLayout.clearAnimation();
			comicMenuLayout.clearAnimation();
			TranslateAnimation translateAnimationUp = new TranslateAnimation(0,
					0, -comicTitleLayout.getMeasuredHeight(), 0);
			translateAnimationUp.setDuration(100);
			translateAnimationUp.setFillAfter(true);

			comicTitleLayout.startAnimation(translateAnimationUp);

			TranslateAnimation translateAnimationDown = new TranslateAnimation(
					0, 0, comicMenuLayout.getMeasuredHeight(), 0);
			translateAnimationDown.setDuration(100);
			translateAnimationDown.setFillAfter(true);

			comicMenuLayout.startAnimation(translateAnimationDown);

		}
	}

	private void hideMenu() {
		blankView.setVisibility(View.GONE);
		if (isShowingMenu) {
			isShowingMenu = false;
			comicTitleLayout.clearAnimation();
			comicMenuLayout.clearAnimation();
			TranslateAnimation translateAnimationUp = new TranslateAnimation(0,
					0, 0, -comicTitleLayout.getMeasuredHeight());
			translateAnimationUp.setDuration(100);
			translateAnimationUp.setFillAfter(true);
			translateAnimationUp.setAnimationListener(new AnimationListener() {

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
					comicTitleLayout.clearAnimation();
					comicTitleLayout.setVisibility(View.GONE);
					if (isLandscape) {
						comicListView.setEnabled(true);
					} else {
						viewPager.setEnabled(true);
					}

				}
			});
			comicTitleLayout.startAnimation(translateAnimationUp);

			TranslateAnimation translateAnimationDown = new TranslateAnimation(
					0, 0, 0, comicMenuLayout.getMeasuredHeight());
			translateAnimationDown.setDuration(100);
			translateAnimationDown.setFillAfter(true);
			translateAnimationDown
					.setAnimationListener(new AnimationListener() {

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
							comicMenuLayout.clearAnimation();
							comicMenuLayout.setVisibility(View.GONE);

						}
					});
			comicMenuLayout.startAnimation(translateAnimationDown);
		}
	}

	private void setTitle() {
		TextView titleTextView = (TextView) findViewById(R.id.chapter_textView);
		titleTextView.setText(chapter.getChapter_title());

		batteryImageView = (ProgressBar) findViewById(R.id.battery_imageView);
		batteryTextView = (TextView) findViewById(R.id.battery_textView);
		MyDigitalClock myDigitalClock = (MyDigitalClock) findViewById(R.id.digitalClock);
		LinearLayout batteryLayout = (LinearLayout) findViewById(R.id.battery_layout);

		if (!GlobalParams.canShowTime) {
			batteryLayout.setVisibility(View.GONE);
			batteryImageView.setVisibility(View.GONE);
			batteryTextView.setVisibility(View.GONE);
			myDigitalClock.setVisibility(View.GONE);
		}

		netStateTextView = (TextView) findViewById(R.id.netState_textView);
		netStateTextView.setText(SystemUtils
				.getNetState(getApplicationContext()));

		pagesTextView = (TextView) findViewById(R.id.pageNum_textView);
		pagesTextView.setText((currPageNum + 1) + "/" + pageNum);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowingMenu) {
					setResult(1);
					finish();
				}
			}
		});

		final TextView highTextView = (TextView) findViewById(R.id.high_textView);
		if (isHigh) {
			highTextView.setText("高清");
			GlobalParams.imageHighString = "";
		} else {
			highTextView.setText("标清");
			GlobalParams.imageHighString = "!800";
		}
		highTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isHigh) {
					highTextView.setText("标清");
				} else {
					highTextView.setText("高清");
				}
				isHigh = !isHigh;
				if (isHigh) {
					GlobalParams.imageHighString = "";
				} else {
					GlobalParams.imageHighString = "!800";
				}
				if (isLandscape) {
					adapter.setIsHigh(isHigh);
					adapter.notifyDataSetChanged();
				} else {
					pagerAdapter.setIsHigh(isHigh);
					pagerAdapter.notifyDataSetChanged();
				}

			}
		});
	}

	private void setMenu() {

		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setMax(pageNum - 1);
		seekBar.setProgress(currPageNum);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int progress = seekBar.getProgress();
				if (isTouch) {
					if (isPage) {
						currPageNum = progress;
						pagesTextView.setText((progress + 1) + "/" + pageNum);

						if (isLandscape) {
							comicListView.setSelection(currPageNum);
						} else {
							viewPager.setCurrentItem(currPageNum);
						}
						pageChangeTextView.setVisibility(View.GONE);
					}
				}
				pageChangeTextView.setVisibility(View.GONE);
				isTouch = false;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				isTouch = true;
				pageChangeTextView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (isPage) {
					if (isTouch) {
						pageChangeTextView.setText((progress + 1) + "/"
								+ pageNum);
					}

				} else {
					SystemUtils.setCurrentActivityBrightness(
							ReadComicLocalActivity.this, (float) progress
									/ pageNum);
				}

			}
		});

		final ImageView leftImageView = (ImageView) findViewById(R.id.left_imageView);
		final ImageView rightImageView = (ImageView) findViewById(R.id.right_imageView);
		if (isPage) {
			leftImageView.setBackgroundResource(R.drawable.jian);
			rightImageView.setBackgroundResource(R.drawable.jia);
		} else {
			leftImageView.setBackgroundResource(R.drawable.liangdu1);
			rightImageView.setBackgroundResource(R.drawable.liangdu2);
		}

		final TextView lightTextView = (TextView) findViewById(R.id.light_textView);

		final TextView pageTextView = (TextView) findViewById(R.id.page_textView);
		pageTextView.setSelected(true);
		pageTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowingMenu) {
					if (!isPage) {
						leftImageView.setBackgroundResource(R.drawable.jian);
						rightImageView.setBackgroundResource(R.drawable.jia);
						isPage = true;
						pageTextView.setSelected(true);
						lightTextView.setSelected(false);
						seekBar.setProgress(currPageNum);
					}
				}
			}
		});

		TextView bookmarkTextView = (TextView) findViewById(R.id.bookmark_textView);
		bookmarkTextView.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				bookmarkComicList = SerializationUtil
						.readBookmarkComicCache(getApplicationContext());
				bookmarkList = SerializationUtil.readBookmarkCache(
						getApplicationContext(), comic.getComic_id());
				if (bookmarkComicList == null) {
					bookmarkComicList = new ArrayList<Comic>();
				}
				if (bookmarkList == null) {
					bookmarkList = new ArrayList<Chapter>();
				}
				final AlertDialog alertDialog = new AlertDialog.Builder(
						ReadComicLocalActivity.this).create();
				alertDialog.setCanceledOnTouchOutside(true);
				alertDialog.show();

				Window window = alertDialog.getWindow();
				window.setContentView(R.layout.dialog_bookmark);
				window.setDimAmount(0.6f);

				TextView addTextView = (TextView) window
						.findViewById(R.id.addbookmark_textView);
				addTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						boolean hasComic = false;
						for (Comic bComic : bookmarkComicList) {
							if (bComic.getComic_id()
									.equals(comic.getComic_id())) {
								hasComic = true;
							}
						}
						if (!hasComic) {
							bookmarkComicList.add(0, comic);
							SerializationUtil.wirteBookmarkComicSerialization(
									getApplicationContext(),
									(Serializable) bookmarkComicList);
						}

						boolean hasBookmark = false;
						for (Chapter bChapter : bookmarkList) {
							if (bChapter.getBookmarkNum() == currPageNum) {
								hasBookmark = true;
							}
						}
						if (!hasBookmark) {
							chapter.setBookmarkNum(currPageNum);
							bookmarkList.add(0, chapter);
							SerializationUtil.wirteBookmarkSerialization(
									getApplicationContext(),
									(Serializable) bookmarkList,
									comic.getComic_id());
							Toast.makeText(getApplicationContext(), "添加书签成功", 1)
									.show();
						} else {
							Toast.makeText(getApplicationContext(), "已添加", 1)
									.show();
						}

						alertDialog.dismiss();

					}
				});

				TextView myTextView = (TextView) window
						.findViewById(R.id.mybookmarks_textView);
				myTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ReadComicLocalActivity.this,
								BookmarkActivity.class);
						intent.putExtra("comic", comic);

						startActivityForResult(intent, 1);
						alertDialog.dismiss();
					}
				});
			}
		});

		lightTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowingMenu) {
					if (isPage) {
						leftImageView
								.setBackgroundResource(R.drawable.liangdu1);
						rightImageView
								.setBackgroundResource(R.drawable.liangdu2);
						isPage = false;
						lightTextView.setSelected(true);
						pageTextView.setSelected(false);
						seekBar.setProgress(pageNum);
					}

				}
			}
		});

		final TextView screenTextView = (TextView) findViewById(R.id.screen_textView);
		if (isLandscape) {
			screenTextView.setText("竖屏");
		} else {
			screenTextView.setText("横屏");
		}
		screenTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowingMenu) {
					if (isLandscape) {
						ReadComicLocalActivity.this
								.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						if (timer != null) {
							timer.cancel();
						}
					} else {
						ReadComicLocalActivity.this
								.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
						if (timer != null) {
							timer.cancel();
						}

					}
					isLandscape = !isLandscape;
					GlobalParams.isLanscape = isLandscape;
					AppConfig.setScreen(isLandscape);
				}
			}
		});

		final TextView danmuTextView = (TextView) findViewById(R.id.danmu_textView);
		danmuTextView.setSelected(canDanmu);
		danmuTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isShowingMenu) {
					danmuTextView.setSelected(!danmuTextView.isSelected());
					canDanmu = danmuTextView.isSelected();
					// Toast.makeText(getApplicationContext(), "暂未开放",
					// 1).show();
					if (comments != null) {
						startDanmu();
					}

				}
			}
		});

		if (canDanmu && comments != null) {
			startDanmu();
		}
	}

	private void startDanmu() {
		if (canDanmu) {
			if (timer == null) {
				timer = new Timer();
			}
			TimerTask timerTask = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (canDanmu) {
						if (danmuNum == comments.size()) {
							danmuNum = 0;
							timer.cancel();
							timer = null;
							return;
						}
						handler.sendEmptyMessage(0);
					} else {
						timer.cancel();
						return;
					}
				}
			};

			timer.schedule(timerTask, 100, 2000);
		} else {
			if (timer != null) {
				timer.cancel();
				timer = null;
			}

		}

	}

	private Handler handler = new Handler() {
		@SuppressLint("ResourceAsColor")
		public void handleMessage(android.os.Message msg) {
			if (danmuNum >= comments.size()) {
				return;
			}
			final TextView textView = new TextView(getApplicationContext());
			textView.setText(comments.get(danmuNum).getComment_content());
			textView.setTextSize(30);
			textView.setShadowLayer(1, 1, 1, 0xff000000);
			int color = 0;

			int rand = (int) (Math.random() * 4);

			switch (rand) {
			case 0:
				color = 0xffe08e6d;
				break;
			case 1:
				color = 0xffDA4453;
				break;
			case 2:
				color = 0xff4EC793;
				break;
			case 3:
				color = 0xff0080fc;
				break;
			default:
				break;
			}

			textView.setTextColor(color);

			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			int width = 0;
			int height = 0;
			if (isLandscape) {
				width = GlobalParams.screenHeight;
				height = GlobalParams.screenWidth;
			} else {
				width = GlobalParams.screenWidth;
				height = GlobalParams.screenHeight;
			}

			int num = danmuNum % 4;

			int textWidth = 0;

			TextPaint textPaint = textView.getPaint();
			textWidth = (int) textPaint.measureText(comments.get(danmuNum)
					.getComment_content());

			layoutParams.topMargin = height / 6 * num
					+ textView.getMeasuredHeight() + height / 12;

			textView.setLayoutParams(layoutParams);

			readComicRelativeLayout.addView(textView);

			TranslateAnimation translateAnimationUp = new TranslateAnimation(
					-textWidth, width, 0, 0);
			translateAnimationUp.setDuration(5000);
			translateAnimationUp.setFillAfter(true);
			translateAnimationUp.setAnimationListener(new AnimationListener() {

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
					textView.clearAnimation();
					textView.setVisibility(View.GONE);

				}
			});
			textView.startAnimation(translateAnimationUp);

			danmuNum++;
		};
	};

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (netStateTextView != null) {
			netStateTextView.setText(SystemUtils
					.getNetState(getApplicationContext()));
		}

		if (GlobalParams.canShowTime) {
			registerBattery();
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (GlobalParams.canShowTime) {
			unregisterBattery();
		}

		List<Comic> chapterList = SerializationUtil
				.readNewestHistoryCache(getApplicationContext());
		for (int i = 0; i < chapterList.size(); i++) {
			Comic historyComic = chapterList.get(i);
			if (historyComic.getComic_id().equals(comic.getComic_id())) {
				historyComic.setHistoryChapterPage(currPageNum);
			}
		}

		SerializationUtil.wirteNewestHistorySerialization(
				getApplicationContext(), (Serializable) chapterList);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("listNum", currPageNum);
		outState.putBoolean("canDanmu", canDanmu);
		outState.putBoolean("isPage", isPage);
		outState.putBoolean("isHigh", isHigh);
		outState.putBoolean("isLandscape", isLandscape);
		outState.putInt("pageNum", pageNum);
		outState.putSerializable("list", (Serializable) list);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) {
			if (data != null) {
				currPageNum = data.getIntExtra("bookmark", 0);
				pageNum = data.getIntExtra("pageCount", 0);
				pagesTextView.setText((currPageNum + 1) + "/" + pageNum);

				if (isLandscape) {
					comicListView.smoothScrollToPosition(currPageNum);
				} else {
					viewPager.setCurrentItem(currPageNum);
				}

				seekBar.setProgress(currPageNum);
			}
			
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			setResult(1);
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

}