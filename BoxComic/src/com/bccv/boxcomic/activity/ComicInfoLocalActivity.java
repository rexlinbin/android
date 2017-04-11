package com.bccv.boxcomic.activity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoActivity.OnChapterAll;
import com.bccv.boxcomic.adapter.ChapterAdapter;
import com.bccv.boxcomic.adapter.ComicFromAdapter;
import com.bccv.boxcomic.api.ComicApi;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.modal.OnlineFrom;
import com.bccv.boxcomic.net.NetUtil;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.FastBlur;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.ImageUtils;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.MyComparator;
import com.bccv.boxcomic.tool.SerializationUtil;
import com.bccv.boxcomic.tool.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComicInfoLocalActivity extends BaseActivity {
	private Comic comic;

	private List<Comic> collectList;
	private List<Comic> chapterList;

	private LinearLayout comicInfoLayout;

	private TextView readImageView;

	private TextView comicFromTextView;
	private PopupWindow comicFromPopupWindow;

	private GridView gridView;
	private ChapterAdapter adapter;
	private List<Chapter> list;

	private Activity activity;

	private String comicContentShortString;
	private String comicContentString;

	private boolean isShort = true;
	private boolean hasCollect = false;

	private ScrollView scrollView;

	private Comic mainItem;
	private String comicidString;

	private int historyChapter = -1;

	private List<OnlineFrom> onlineFroms = new ArrayList<OnlineFrom>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		GlobalParams.onlineFromNameIdString = "local";
		activity = this;
		super.onCreate(savedInstanceState);
		comicidString = getIntent().getStringExtra("mainitem");

		setContentView(R.layout.activity_comicinfo_local);

		setBack();
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		scrollView.setVisibility(View.INVISIBLE);
		comicInfoLayout = (LinearLayout) findViewById(R.id.comicInfo_localLayout);
		comicInfoLayout.setVisibility(View.INVISIBLE);
		fetchServiceData();
	}
	
	private void setData(boolean isAll) {
		list.clear();
		List<Chapter> getChapters = comic.getChapters();

		if (getChapters != null) {
//			if (isAll || getChapters.size() < 25) {
//				list.addAll(getChapters);
//			} else {
//				for (int i = 0; i < 25; i++) {
//					Chapter chapter = getChapters.get(i);
//					list.add(chapter);
//				}
//				Chapter chapter = new Chapter();
//				chapter.setChapter_title("...");
//				list.add(chapter);
//			}
			list.addAll(getChapters);
		}

		for (int i = 0; i < chapterList.size(); i++) {
			Comic historyComic = chapterList.get(i);
			if (historyComic.getComic_id().equals(comic.getComic_id())) {
				boolean has = false;
				for (int j = 0; j < getChapters.size(); j++) {
					if (j == historyComic.getHistoryChapter()) {
						historyChapter = j;
						has = true;
						break;
					}
				}
				if (has) {
					break;
				}
			}
		}

		if (historyChapter < list.size()) {
			for (int i = 0; i < list.size(); i++) {
				if (i == historyChapter) {
					list.get(historyChapter).setSelect(true);
				} else {
					list.get(i).setSelect(false);
				}
			}

		}

		adapter.notifyDataSetChanged();
		if (!isAll) {
			scrollView.smoothScrollTo(0, 0);
		}

		if (historyChapter == -1) {
			readImageView.setText("开始阅读");
		} else {
			readImageView.setText("续看"
					+ getChapters.get(historyChapter).getChapter_title());
		}

	}

	private List<Comic> newestChapterList;

	private void readData() {
		chapterList = SerializationUtil
				.readNewestHistoryCache(getApplicationContext());
		if (chapterList == null) {
			chapterList = new ArrayList<Comic>();
		}
		newestChapterList = SerializationUtil
				.readHistoryCache(getApplicationContext());
		if (newestChapterList == null) {
			newestChapterList = new ArrayList<Comic>();
		}
	}

	private void saveData() {
		SerializationUtil.wirteNewestHistorySerialization(
				getApplicationContext(), (Serializable) chapterList);
		SerializationUtil.wirteHistorySerialization(getApplicationContext(),
				(Serializable) newestChapterList);
	}

	private void fetchServiceData() {
		canShowWebDialog = false;
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (comic != null) {
					setComicInfo();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				comic = new Comic();
				comic.setComic_id(comicidString);
				comic.setComic_title(comicidString);
				comic.setComic_titlepic("");
				comic.setLocal(true);

				String localPathString = GlobalParams.localComicPathString
						+ "/" + comic.getComic_id() + "/";

				List<File> files = FileUtils.listPathDirs(localPathString);

				MyComparator myComparator = new MyComparator();
				Collections.sort(files, myComparator);
				
				List<Chapter> chapters = new ArrayList<Chapter>();
				for (int i = 0; i < files.size(); i++) {
					File file = files.get(i);
					Chapter chapter = new Chapter();
					chapter.setChapter_title(file.getName());
					chapter.setChapter_id(file.getName());
					List<File> chapterFiles = FileUtils.listPathFiles(localPathString + "/" + file.getName());
					chapter.setChapter_count(chapterFiles.size());
					chapters.add(chapter);
				}
				comic.setChapters(chapters);
				
				List<Comic> comics = SerializationUtil
						.readComicInfoCache(getApplicationContext());
				if (comics != null) {
					boolean hasComic = false;
					for (int i = 0; i < comics.size(); i++) {
						Comic comicInfo = comics.get(i);
						if (comicInfo.getComic_id().equals(comicidString)) {
							comicInfo = comic;
							hasComic = true;
							break;
						}
					}

					if (!hasComic) {
						comics.add(comic);
					}

				} else {
					comics = new ArrayList<Comic>();
					comics.add(comic);
				}
				SerializationUtil.wirteComicInfoSerialization(
						getApplicationContext(), (Serializable) comics);

				return null;
			}
		}.executeProxy("");

	}

	private void setBack() {

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@SuppressLint("NewApi")
	private void blur(Bitmap bkg, View view) {
        float scaleFactor = 10;
        float radius = 8;
        bkg = ImageUtils.scaleBitmap(bkg, (int) view.getMeasuredWidth(), (int) view.getMeasuredWidth() * bkg.getHeight() / bkg.getWidth());

        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()/scaleFactor),
                (int) (view.getMeasuredHeight()/scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft()/scaleFactor, -view.getTop()/scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int)radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }

	private void setComic() {
		final ImageView titleBgImageView = (ImageView) findViewById(R.id.titlebg_imageView);
		final ImageView imageView = (ImageView) findViewById(R.id.comicImage_imageView);
		List<File> files = FileUtils.listPathFiles(GlobalParams.localComicPathString + "/" + comicidString);
		if (files.size() > 0) {
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				Bitmap bitmap = ImageUtils.getBitmapByFile(file);
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
					blur(bitmap, titleBgImageView);
					break;
				}
			}
		}
		
		TextView titleTextView = (TextView) findViewById(R.id.comicTitle_textView);
		titleTextView.setText(comic.getComic_id());

		readImageView = (TextView) findViewById(R.id.read_button);
		readImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position = historyChapter == -1 ? comic.getChapters()
						.size() - 1 : historyChapter;
				historyChapter = position;

				comic.setHistoryChapter(position);
				comic.setHistoryChapterTitleString(comic.getChapters()
						.get(position).getChapter_title());
				boolean hasComic = true;
				int comicNum = -1;
				int bookmark = 0;
				Comic newestComic = null;
				for (int i = 0; i < chapterList.size(); i++) {
					Comic historyComic = chapterList.get(i);
					if (historyComic.getComic_id().equals(comic.getComic_id())) {
						comicNum = i;
						if (historyComic.getHistoryChapter() != position) {
							historyComic.setHistoryChapter(position);
							historyComic.setHistoryChapterPage(0);
							historyComic.setHistoryChapterTitleString(comic
									.getChapters().get(position)
									.getChapter_title());
						} else {
							bookmark = historyComic.getHistoryChapterPage();
						}
						newestComic = historyComic;
						hasComic = false;
						break;
					}
				}
				if (hasComic) {
					chapterList.add(0, comic);
					Comic tempComic = comic;
					boolean hasHistory = false;
					for (Comic comic : newestChapterList) {
						if (comic.getComic_id().equals(tempComic.getComic_id())) {
							hasHistory = true;
							Comic tempComic1 = comic;
							newestChapterList.remove(comic);
							tempComic1.setHistoryChapter(newestComic.getHistoryChapter());
							tempComic1.setHistoryChapterPage(newestComic.getHistoryChapterPage());
							tempComic1.setHistoryChapterTitleString(newestComic.getHistoryChapterTitleString());
							newestChapterList.add(0, tempComic1);
							break;
						}
					}
					if (!hasHistory) {
						newestChapterList.add(0, comic);
					}
				} else {
					if (comicNum != -1) {
						Comic tempComic = chapterList.get(comicNum);
						chapterList.remove(comicNum);
						chapterList.add(0, tempComic);

						boolean hasHistory = false;
						for (Comic comic : newestChapterList) {
							if (comic.getComic_id().equals(tempComic.getComic_id())) {
								hasHistory = true;
								Comic tempComic1 = comic;
								newestChapterList.remove(comic);
								tempComic1.setHistoryChapter(newestComic.getHistoryChapter());
								tempComic1.setHistoryChapterPage(newestComic.getHistoryChapterPage());
								tempComic1.setHistoryChapterTitleString(newestComic.getHistoryChapterTitleString());
								newestChapterList.add(0, tempComic1);
								break;
							}
						}
						if (!hasHistory) {
							newestChapterList.add(0, comic);
						}
					}
				}

				for (int i = 0; i < list.size(); i++) {
					if (i == position) {
						list.get(i).setSelect(true);
					} else {
						list.get(i).setSelect(false);
					}
				}

				adapter.notifyDataSetChanged();
				if (historyChapter == -1) {
					readImageView.setText("续看"
							+ comic.getChapters()
									.get(comic.getChapters().size() - 1)
									.getChapter_title());
				} else {
					readImageView.setText("续看"
							+ comic.getChapters().get(historyChapter)
									.getChapter_title());
				}

				Chapter rChapter = comic.getChapters().get(position);
				rChapter.setBookmarkNum(bookmark);
				Intent intent = new Intent(activity, ReadComicLocalActivity.class);
				intent.putExtra("comic", comic);
				intent.putExtra("chapter", rChapter);
				startActivityForResult(intent, 1);
			}
		});

		readData();
	}

	private void resetChapterData(final String menu_id) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (comic != null) {
					list.clear();
					List<Chapter> getChapters = comic.getChapters();

					if (getChapters != null) {
//						if (getChapters.size() < 25) {
//							list.addAll(getChapters);
//						} else {
//							for (int i = 0; i < 25; i++) {
//								Chapter chapter = getChapters.get(i);
//								list.add(chapter);
//							}
//							Chapter chapter = new Chapter();
//							chapter.setChapter_title("...");
//							list.add(chapter);
//						}
						list.addAll(getChapters);

					}

					historyChapter = -1;
					for (int i = 0; i < chapterList.size(); i++) {
						Comic historyComic = chapterList.get(i);
						if (historyComic.getComic_id().equals(
								comic.getComic_id())) {
							boolean has = false;
							for (int j = 0; j < getChapters.size(); j++) {
								if (j == historyComic.getHistoryChapter()) {
									historyChapter = j;
									has = true;
									break;
								}
							}
							if (has) {
								break;
							}
						}
					}

					if (historyChapter < list.size()) {
						for (int i = 0; i < list.size(); i++) {
							if (i == historyChapter) {
								list.get(historyChapter).setSelect(true);
							} else {
								list.get(i).setSelect(false);
							}
						}

					}

					adapter.notifyDataSetChanged();

					if (historyChapter == -1) {
						readImageView.setText("开始阅读");
					} else {
						readImageView.setText("续看"
								+ getChapters.get(historyChapter)
										.getChapter_title());
					}
				} else {
					Toast.makeText(getApplicationContext(), "获取章节失败", 1).show();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				ComicApi comicApi = new ComicApi();
				comic = comicApi
						.getComicOnlineInfo(comicidString + "", menu_id);
				return "";
			}
		}.executeProxy("");
	}
	

	private void setComicInfo() {
		scrollView.setVisibility(View.VISIBLE);
		comicInfoLayout.setVisibility(View.VISIBLE);
		if (comic != null) {
			setComic();
		}

		gridView = (GridView) findViewById(R.id.chapter_gridView);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		list = new ArrayList<Chapter>();

		OnChapterAll onChapterAll = new OnChapterAll() {

			@Override
			public void getAll(int position) {
				// TODO Auto-generated method stub
				if (position == -1) {
					setData(true);
				} else {
					historyChapter = position;

					comic.setHistoryChapter(position);
					comic.setHistoryChapterTitleString(list.get(position)
							.getChapter_title());
					boolean hasComic = true;
					int comicNum = -1;
					int bookmark = 0;
					Comic newestComic = comic;
					for (int i = 0; i < chapterList.size(); i++) {
						Comic historyComic = chapterList.get(i);
						if (historyComic.getComic_id().equals(
								comic.getComic_id())) {
							comicNum = i;
							if (historyComic.getHistoryChapter() != position) {
								historyComic.setHistoryChapter(position);
								historyComic.setHistoryChapterPage(0);
								historyComic.setHistoryChapterTitleString(list
										.get(position).getChapter_title());
							} else {
								bookmark = historyComic.getHistoryChapterPage();
							}
							newestComic = historyComic;
							hasComic = false;
							break;
						}
					}
					if (hasComic) {
						chapterList.add(0, comic);
						Comic tempComic = comic;
						boolean hasHistory = false;
						for (Comic comic : newestChapterList) {
							if (comic.getComic_id().equals(tempComic.getComic_id())) {
								hasHistory = true;
								Comic tempComic1 = comic;
								tempComic1.setHistoryChapter(newestComic.getHistoryChapter());
								tempComic1.setHistoryChapterPage(newestComic.getHistoryChapterPage());
								tempComic1.setHistoryChapterTitleString(newestComic.getHistoryChapterTitleString());
								newestChapterList.remove(comic);
								newestChapterList.add(0, tempComic1);
								break;
							}
						}
						if (!hasHistory) {
							newestChapterList.add(0, comic);
						}
					} else {
						if (comicNum != -1) {
							Comic tempComic = chapterList.get(comicNum);
							chapterList.remove(comicNum);
							chapterList.add(0, tempComic);

							boolean hasHistory = false;
							for (Comic comic : newestChapterList) {
								if (comic.getComic_id().equals(tempComic.getComic_id())) {
									hasHistory = true;
									Comic tempComic1 = comic;
									newestChapterList.remove(comic);
									tempComic1.setHistoryChapter(newestComic.getHistoryChapter());
									tempComic1.setHistoryChapterPage(newestComic.getHistoryChapterPage());
									tempComic1.setHistoryChapterTitleString(newestComic.getHistoryChapterTitleString());
									newestChapterList.add(0, tempComic1);
									break;
								}
							}
							if (!hasHistory) {
								newestChapterList.add(0, comic);
							}
						}
					}

					for (int i = 0; i < list.size(); i++) {
						if (i == position) {
							list.get(i).setSelect(true);
						} else {
							list.get(i).setSelect(false);
						}
					}

					adapter.notifyDataSetChanged();
					if (historyChapter == -1) {
						readImageView.setText("续看"
								+ list.get(list.size() - 1).getChapter_title());
					} else {
						readImageView.setText("续看"
								+ list.get(historyChapter).getChapter_title());
					}

					Chapter rChapter = list.get(position);
					rChapter.setBookmarkNum(bookmark);
					Intent intent = new Intent(activity,
							ReadComicLocalActivity.class);
					intent.putExtra("comic", comic);
					intent.putExtra("chapter", rChapter);
					startActivityForResult(intent, 1);
				}
			}
		};

		adapter = new ChapterAdapter(activity, list, onChapterAll, false);
		gridView.setAdapter(adapter);

		setData(false);
	}

	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		scrollView.smoothScrollTo(0, 0);

		readData();
		super.onResume();
	}

	public void resetData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			int position = 0;
			chapterList = SerializationUtil
					.readNewestHistoryCache(getApplicationContext());
			if (chapterList == null) {
				chapterList = new ArrayList<Comic>();
			}

			boolean hasComic = true;
			for (int i = 0; i < chapterList.size(); i++) {
				Comic historyComic = chapterList.get(i);
				if (historyComic.getComic_id().equals(comic.getComic_id())) {
					position = historyComic.getHistoryChapter();
					break;
				}
			}

			for (int i = 0; i < list.size(); i++) {
				if (i == position) {
					list.get(i).setSelect(true);
				} else {
					list.get(i).setSelect(false);
				}
			}

			historyChapter = position;

			if (historyChapter == -1) {
				readImageView.setText("续看"
						+ comic.getChapters().get(list.size() - 1)
								.getChapter_title());
			} else {
				readImageView.setText("续看"
						+ comic.getChapters().get(historyChapter)
								.getChapter_title());
			}

			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SerializationUtil.wirteNewestHistorySerialization(
				getApplicationContext(), (Serializable) chapterList);
		SerializationUtil.wirteHistorySerialization(getApplicationContext(),
				(Serializable) newestChapterList);

	}

}