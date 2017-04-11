package com.bccv.boxcomic.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.boxcomic.R;
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
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.ImageUtils;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.SerializationUtil;
import com.bccv.boxcomic.tool.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ComicInfoActivity extends BaseActivity {
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
		activity = this;
		super.onCreate(savedInstanceState);
		// mainItem = (Comic)
		// getIntent().getExtras().getSerializable("mainitem");
		comicidString = getIntent().getStringExtra("mainitem");
		
		setContentView(R.layout.activity_comicinfo);
	
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

	private void saveData(){
		SerializationUtil.wirteNewestHistorySerialization(
				getApplicationContext(), (Serializable) chapterList);
		SerializationUtil.wirteHistorySerialization(getApplicationContext(),
				(Serializable) newestChapterList);
	}
	
	private void fetchServiceData() {
		// boolean isNetworkAvailable = NetUtil.isNetworkAvailable(activity);
		// if (!isNetworkAvailable) {
		// PromptManager.showToast(activity, "网络不给力");
		// return;
		// }
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
				ComicApi comicApi = new ComicApi();
				Map<String, String> map = SerializationUtil
						.readOnlineFromCache(getApplicationContext());
				String defaultMenuId = "";
				if (map == null) {
					map = new HashMap<String, String>();
				} else {
					defaultMenuId = map.get(comicidString);
				}

				if (StringUtils.isEmpty(defaultMenuId)) {
					comic = comicApi.getComicOnlineInfo(comicidString + "", "");
				} else {
					comic = comicApi.getComicOnlineInfo(comicidString + "",
							defaultMenuId);
				}

				List<Comic> comics = SerializationUtil
						.readComicInfoCache(getApplicationContext());
				if (comic == null) {
					if (comics != null) {
						for (Comic comicInfo : comics) {
							if (comicInfo.getComic_id().equals(comicidString)) {
								comic = comicInfo;
								onlineFroms = comic.getOnlineFroms();
								break;
							}
						}
					}
				} else {
					onlineFroms = comic.getOnlineFroms();
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
				}
				return null;
			}
		}.executeProxy("");

	}

	private void setBack() {
		RelativeLayout downloadRelativeLayout = (RelativeLayout) findViewById(R.id.download_relativeLayout);
		downloadRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity,
						DownloadSelectActivity.class);
				intent.putExtra("comic", comic);
				startActivity(intent);
			}
		});

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
	private void blur(Bitmap bkg, ImageView view) {
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
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(
				GlobalParams.imageUrlString + comic.getComic_titlepic(),
				imageView, GlobalParams.options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						blur(loadedImage, titleBgImageView);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});
		
		TextView titleTextView = (TextView) findViewById(R.id.comicTitle_textView);
		titleTextView.setText(comic.getComic_title());

		TextView authorTextView = (TextView) findViewById(R.id.comicAuthor_textView);
		authorTextView.setText("作者：" + comic.getComic_author());

		TextView lookNumTextView = (TextView) findViewById(R.id.lookNum_textView);
		lookNumTextView.setText("浏览：" + comic.getComic_click());

		comicFromTextView = (TextView) findViewById(R.id.comicFrom_textView);
		Map<String, String> map = SerializationUtil
				.readOnlineFromCache(getApplicationContext());
		if (map == null) {
			map = new HashMap<String, String>();
		}
		String defaultMenuId = map.get(comic.getComic_id());
		if (StringUtils.isEmpty(defaultMenuId)) {
			comicFromTextView.setText(onlineFroms.get(0).getResource_name());
			GlobalParams.onlineFromMenuIdString = onlineFroms.get(0)
					.getMenu_id();
			GlobalParams.onlineFromNameIdString = onlineFroms.get(0)
					.getResource_id();
			map.put(comic.getComic_id(), onlineFroms.get(0).getMenu_id());
			SerializationUtil.wirteOnlineFromSerialization(
					getApplicationContext(), (Serializable) map);
		} else {
			boolean hasFrom = true;
			Logger.e("online", defaultMenuId);
			for (OnlineFrom onlineFrom : onlineFroms) {
				Logger.e("online", onlineFrom.getResource_id());
				if (onlineFrom.getResource_id().equals(defaultMenuId)) {
					comicFromTextView.setText(onlineFrom.getResource_name());
					GlobalParams.onlineFromMenuIdString = onlineFrom
							.getMenu_id();
					GlobalParams.onlineFromNameIdString = onlineFrom
							.getResource_id();
					hasFrom = false;
					break;
				}
			}
			if (hasFrom) {
				comicFromTextView
						.setText(onlineFroms.get(0).getResource_name());
				GlobalParams.onlineFromMenuIdString = onlineFroms.get(0)
						.getMenu_id();
				GlobalParams.onlineFromNameIdString = onlineFroms.get(0)
						.getResource_id();
				map.put(comic.getComic_id(), onlineFroms.get(0)
						.getResource_id());
				SerializationUtil.wirteOnlineFromSerialization(
						getApplicationContext(), (Serializable) map);
			}
		}

		comic.setComic_FromString(GlobalParams.onlineFromNameIdString);
		
		comicFromTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				comicFromTextView.setSelected(true);
				if (comicFromTextView.isSelected()) {
					if (comicFromPopupWindow != null
							&& !comicFromPopupWindow.isShowing()) {
						comicFromPopupWindow.showAsDropDown(v, 0, 0);
					} else {
						LinearLayout layout = (LinearLayout) View.inflate(
								getApplicationContext(), R.layout.list_dialog,
								null);
						ListView listView = (ListView) layout
								.findViewById(R.id.listView);
						// final List<String> list = getTestData();
						ComicFromAdapter adapter = new ComicFromAdapter(
								getApplicationContext(), onlineFroms,
								comicFromTextView);
						listView.setAdapter(adapter);
						listView.setSelector(new ColorDrawable(getResources()
								.getColor(android.R.color.transparent)));
						listView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
								// Intent intent = new Intent();
								// // 指定动作名称
								// intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
								// // 指定快捷方式的图标
								// // Parcelable icon =
								// Intent.ShortcutIconResource.fromContext(ComicInfoActivity.this,
								// R.drawable.a_small_select);
								// intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
								// imageView.getDrawingCache(true));
								// // 指定快捷方式的名称
								// intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
								// "YouTube");
								// // 指定快捷图标激活哪个activity
								// Intent i = new Intent();
								// i.setAction(Intent.ACTION_MAIN);
								// i.addCategory(Intent.CATEGORY_LAUNCHER);
								// ComponentName component = new
								// ComponentName(ComicInfoActivity.this,
								// ComicInfoActivity.class);
								// i.setComponent(component);
								// intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
								// i);
								// intent.putExtra("comic_id",
								// comic.getComic_id());
								// sendBroadcast(intent);
								if (!NetUtil
										.isNetworkAvailable(getApplicationContext())) {
									Toast.makeText(getApplicationContext(),
											"网络不给力，无法获取", 1).show();
									return;
								}
								comicFromTextView.setText(onlineFroms.get(
										position).getResource_name());
								comicFromPopupWindow.dismiss();
								saveData();
								GlobalParams.onlineFromMenuIdString = onlineFroms
										.get(position).getMenu_id();
								GlobalParams.onlineFromNameIdString = onlineFroms
										.get(position).getResource_id();
								Map<String, String> map = SerializationUtil
										.readOnlineFromCache(getApplicationContext());
								if (map == null) {
									map = new HashMap<String, String>();
								}
								map.put(comic.getComic_id(),
										onlineFroms.get(position).getMenu_id());
								SerializationUtil.wirteOnlineFromSerialization(
										getApplicationContext(),
										(Serializable) map);
								comic.setComic_FromString(GlobalParams.onlineFromNameIdString);
								readData();
								resetChapterData(onlineFroms.get(position)
										.getMenu_id());
							}
						});
						comicFromPopupWindow = new PopupWindow(layout,
								comicFromTextView.getWidth(),
								LayoutParams.WRAP_CONTENT);
						comicFromPopupWindow.setFocusable(true);
						comicFromPopupWindow.setTouchable(true);
						comicFromPopupWindow.setOutsideTouchable(true);
						comicFromPopupWindow
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.circular_corner_dialog));
						comicFromPopupWindow.showAsDropDown(v, 0, 0);
						comicFromPopupWindow
								.setOnDismissListener(new OnDismissListener() {

									@Override
									public void onDismiss() {
										// TODO Auto-generated method stub
										comicFromTextView.setSelected(false);
									}
								});
					}
				}
			}
		});

		readImageView = (TextView) findViewById(R.id.read_button);
		readImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (comic.getChapters().size() == 0) {
					return;
				}
				int position = historyChapter == -1 ? comic.getChapters()
						.size() - 1 : historyChapter;
				historyChapter = position;

				comic.setHistoryChapter(position);
				comic.setHistoryChapterTitleString(comic.getChapters()
						.get(position).getChapter_title());
				boolean hasComic = true;
				int comicNum = -1;
				int bookmark = 0;
				Comic newestComic = comic;
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
				saveData();
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
				Intent intent = new Intent(activity, ReadComicActivity.class);
				intent.putExtra("comic", comic);
				intent.putExtra("chapter", rChapter);
				startActivityForResult(intent, 1);
			}
		});

		// ImageView intelligenceImageView = (ImageView)
		// findViewById(R.id.intelligence_button);
		// if (comic.getAdvices() == 0) {
		// intelligenceImageView.setVisibility(View.INVISIBLE);
		// } else {
		// intelligenceImageView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(activity,
		// InfoAnalysisListActivity.class);
		// intent.putExtra("comic_id", comic.getComic_id());
		// startActivity(intent);
		// }
		// });
		// }
		// intelligenceImageView.setVisibility(View.INVISIBLE);
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
							ReadComicActivity.class);
					intent.putExtra("comic", comic);
					intent.putExtra("chapter", rChapter);
					startActivityForResult(intent, 1);
				}
			}
		};

		adapter = new ChapterAdapter(activity, list, onChapterAll, false);
		gridView.setAdapter(adapter);

		final TextView contentTextView = (TextView) findViewById(R.id.comicContent_textView);
		comicContentShortString = "";
		comicContentString = comic.getComic_intro();
		int maxlength = 60;
		if (comicContentString.length() > maxlength) {
			comicContentShortString = comicContentString
					.substring(0, maxlength) + "...";
		}

		final ImageView zhankaiImageView = (ImageView) findViewById(R.id.zhankai_imageView);
		if (StringUtils.isEmpty(comicContentShortString)) {
			contentTextView.setText(comicContentString);
			zhankaiImageView.setVisibility(View.GONE);
		} else {
			contentTextView.setText(comicContentShortString);
			zhankaiImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isShort) {
						contentTextView.setText(comicContentString);
						zhankaiImageView
								.setBackgroundResource(R.drawable.imageview_shousuo);
					} else {
						contentTextView.setText(comicContentShortString);
						zhankaiImageView
								.setBackgroundResource(R.drawable.imageview_zhankai);
					}
					isShort = !isShort;
				}
			});
		}

		final TextView collectTextView = (TextView) findViewById(R.id.collect_textView);
		collectList = SerializationUtil
				.readCollectCache(getApplicationContext());

		if (collectList != null) {
			for (Comic collectComic : collectList) {
				if (collectComic.getComic_id().equals(comic.getComic_id())) {
					hasCollect = collectComic.isHasCollect();
				}
			}

		} else {
			collectList = new ArrayList<Comic>();
		}

		collectTextView.setSelected(hasCollect);
		collectTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hasCollect = !hasCollect;
				collectTextView.setSelected(hasCollect);
				comic.setHasCollect(hasCollect);
				boolean hasComic = false;
				for (int i = 0; i < collectList.size(); i++) {
					Comic historyComic = collectList.get(i);
					if (historyComic.getComic_id() == comic.getComic_id()) {
						historyComic.setHasCollect(hasCollect);
						hasComic = true;
					}
				}
				if (!hasComic) {
					collectList.add(0, comic);
				}
				SerializationUtil.wirteCollectSerialization(
						getApplicationContext(), (Serializable) collectList);
			}
		});

		final TextView zanTextView = (TextView) findViewById(R.id.zan_textView);
		zanTextView.setText("顶(" + comic.getComic_digg() + ")");
		zanTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!zanTextView.isSelected()) {
					comic.setComic_digg(comic.getComic_digg() + 1);
					zanTextView.setText("顶(" + comic.getComic_digg() + ")");
					zanTextView.setSelected(!zanTextView.isSelected());
					setZan();
				}

			}
		});

		TextView commentTextView = (TextView) findViewById(R.id.comment_textView);
		commentTextView.setText("吐槽(" + comic.getComic_comment() + ")");
		commentTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, CommentActivity.class);
				intent.putExtra("isUser", false);
				intent.putExtra("comic_id", comic.getComic_id());
				startActivity(intent);
			}
		});

		TextView updateTextView = (TextView) findViewById(R.id.updateTime_textView);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String updateTimeString = simpleDateFormat.format(new Date(comic
				.getComic_utime() * 1000));
		updateTextView.setText("最近更新：" + updateTimeString);

		setData(false);
	}

	private void setZan() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				ComicApi comicApi = new ComicApi();
				comicApi.setZan(comic.getComic_id());

				return null;
			}
		}.executeProxy("");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		scrollView.smoothScrollTo(0, 0);
	
		readData();
		super.onResume();
	}

	public interface OnChapterAll {
		public void getAll(int position);
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
