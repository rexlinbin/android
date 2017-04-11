package com.bccv.zhuiyingzhihanju.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.DownloadEpisodeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.DownloadSelectAdapter;
import com.bccv.zhuiyingzhihanju.adapter.SourceAdapter;
import com.bccv.zhuiyingzhihanju.api.MovieInfoApi;
import com.bccv.zhuiyingzhihanju.api.MovieUrlApi;
import com.bccv.zhuiyingzhihanju.fragment.EpisodeFragment;
import com.bccv.zhuiyingzhihanju.fragment.IntroFragment;
import com.bccv.zhuiyingzhihanju.fragment.TuijianFragment;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;
import com.bccv.zhuiyingzhihanju.model.MovieInfo;
import com.bccv.zhuiyingzhihanju.model.MovieSource;
import com.bccv.zhuiyingzhihanju.model.MovieUrl;
import com.bccv.zhuiyingzhihanju.model.RealUrl;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.BaseFragmentActivity;
import com.utils.tools.Callback;
import com.utils.tools.Function;
import com.utils.tools.GlobalParams;
import com.utils.tools.M3U8Utils;
import com.utils.tools.PromptManager;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;
import com.utils.tools.SystemUtils;
import com.utils.views.HorizontalListView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "InflateParams" })
public class MovieInfoActivity extends BaseFragmentActivity {
	private ImageView movieImageView;
	private ImageButton collectImageButton;
	private TextView typeTextView, scoreTextView, timeTextView, playTextView, downloadTextView, sourceTextView;
	private LinearLayout downloadLayout, sourceLayout;
	private GridView episodesGridView;
	private boolean isDownloadShow = false, isSourceShow = false;
	private MovieInfo movieInfo;
	private List<Movie> collectList, historyList;
	private Movie movie;
	private ListView sourceListView;
	private SourceAdapter sourceAdapter;
	private TextView sourceCancel;
	private HorizontalListView definitionListView;
	private TextView episodeTextView, episodeDownTextView, downloadcancel, downloadsure;
	private List<MovieEpisode> episodeList, episodeGridList, episodeGridList1;
	private List<MovieUrl> definitionList, sourceList, sourceGetList;
	private DownloadSelectAdapter definitionAdapter;
	private DownloadEpisodeAdapter episodeDownAdapter;
	private boolean isEpisode = false;
	private boolean isWifi = true;
	private boolean isStart = false;
	private int hd = 1, currSource = 0, episodeSelectNum = 1;
	private List<String> episodes;
	private List<MovieSource> movieSourceList = new ArrayList<>();
	private M3U8Utils m3u8Utils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movieinfo);

		movie = (Movie) getIntent().getSerializableExtra("movie");
		// movie = new Movie();
		// movie.setId("3053");
		// movie.setType_id("7");
		if (movie.getType_id().equals("7") || movie.getType_id().equals("8") || movie.getType_id().equals("9")
				|| movie.getType_id().equals("10")) {
			isEpisode = true;
		}
		if (GlobalParams.isWifi) {
			String netState = SystemUtils.getNetState(getApplicationContext());
			if (!netState.equals("WIFI")) {
				isWifi = false;
			}
		}

		setTitle();
		initView();
		getData();
	}

	private void setTitle() {
		ImageButton back = (ImageButton) findViewById(R.id.titel_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView titleName = (TextView) findViewById(R.id.titleName_textView);
		titleName.setText(movie.getTitle());
	}

	private void initView() {
		movieImageView = (ImageView) findViewById(R.id.movie_imageView);

		collectImageButton = (ImageButton) findViewById(R.id.title_collect);
		downloadTextView = (TextView) findViewById(R.id.download_textView);
		playTextView = (TextView) findViewById(R.id.play_textView);

		scoreTextView = (TextView) findViewById(R.id.score_textView);
		typeTextView = (TextView) findViewById(R.id.type_textView);
		timeTextView = (TextView) findViewById(R.id.time_textView);
		sourceTextView = (TextView) findViewById(R.id.source_textView);

		sourceLayout = (LinearLayout) findViewById(R.id.source_layout);
		sourceCancel = (TextView) findViewById(R.id.source_cancel_textView);
		sourceCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideSource();
			}
		});
		sourceList = new ArrayList<MovieUrl>();
		sourceAdapter = new SourceAdapter(getApplicationContext(), sourceList);
		sourceListView = (ListView) findViewById(R.id.source_listView);
		sourceListView.setAdapter(sourceAdapter);
		sourceListView.setSelector(new ColorDrawable(R.color.gray));
		sourceListView.setDivider(new ColorDrawable(R.color.gray));
		sourceListView.setDividerHeight(1);
		sourceListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				sourceList.get(currSource).setSelect(false);
				sourceList.get(position).setSelect(true);
				currSource = position;
				sourceAdapter.notifyDataSetChanged();
				sourceTextView.setText(sourceList.get(position).getWebsite_name());
				if (isEpisode) {
					setEpisodes();
					resetEpisode();
				}
				hideSource();
			}
		});

		downloadLayout = (LinearLayout) findViewById(R.id.download_layout);
		definitionListView = (HorizontalListView) findViewById(R.id.definition_horizontalListView);
		episodeDownTextView = (TextView) findViewById(R.id.episode_textView);
		episodesGridView = (GridView) findViewById(R.id.episode_gridView);

		definitionList = new ArrayList<MovieUrl>();
		episodeGridList = new ArrayList<MovieEpisode>();
		episodeGridList1 = new ArrayList<MovieEpisode>();
		definitionAdapter = new DownloadSelectAdapter(getApplicationContext(), definitionList);
		episodeDownAdapter = new DownloadEpisodeAdapter(getApplicationContext(), episodeGridList);

		definitionListView.setAdapter(definitionAdapter);
		definitionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				definitionList.get(hd - 1).setSelect(false);
				definitionList.get(position).setSelect(true);
				hd = position + 1;
				definitionAdapter.notifyDataSetChanged();
			}
		});

		episodesGridView.setAdapter(episodeDownAdapter);
		episodesGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		episodesGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				boolean isSelect = episodeGridList.get(position).isSelect();
				episodeGridList.get(position).setSelect(!isSelect);
				if (isSelect) {
					episodeSelectNum--;
				} else {
					episodeSelectNum++;
				}

				if (episodeSelectNum > 0) {
					downloadsure.setSelected(true);
				} else {
					downloadsure.setSelected(false);
				}
				episodeDownAdapter.notifyDataSetChanged();
			}
		});
		if (isEpisode) {
			episodesGridView.setVisibility(View.VISIBLE);
			episodeDownTextView.setVisibility(View.VISIBLE);
		}

		downloadcancel = (TextView) findViewById(R.id.download_cancel_textView);
		downloadcancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideDownload();
			}
		});
		downloadsure = (TextView) findViewById(R.id.download_sure_textView);
		downloadsure.setSelected(true);
		downloadsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (downloadsure.isSelected()) {
					startDownload();
				}
			}
		});
	}
	private void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (collectList == null) {
					collectList = new ArrayList<Movie>();
				}

				if (historyList == null) {
					historyList = new ArrayList<Movie>();
				}

				if (movieInfo != null) {
					movie.setImages(movieInfo.getImages());
					movie.setRating(movieInfo.getRating());
					movie.setTitle(movieInfo.getTitle());
					movie.setEpisode_num(movieInfo.getEpisodes_now());
					// if (isEpisode) {
					// episodes = new ArrayList<String>();
					// int total =
					// Integer.parseInt(movieInfo.getEpisodes_now());
					// int historyEpisodeid = 1;
					// boolean hasHistory = false;
					// for (int i = 0; i < historyList.size(); i++) {
					// Movie historyMovie = historyList.get(i);
					// if (historyMovie.getId().equals(movie.getId())
					// && historyMovie.getType_id().equals(movie.getType_id()))
					// {
					// if (!StringUtils.isEmpty(historyMovie.getEpisode_id())) {
					// historyEpisodeid =
					// Integer.parseInt(historyMovie.getEpisode_id());
					// hasHistory = true;
					// }
					//
					// break;
					// }
					//
					// }
					//
					// for (int i = 0; i < total; i++) {
					// MovieEpisode movieEpisode = new MovieEpisode();
					// movieEpisode.setId(i + 1 + "");
					// episodeList.add(movieEpisode);
					//
					// MovieEpisode movieEpisode2 = new MovieEpisode();
					// movieEpisode2.setId(i + 1 + "");
					// episodeGridList1.add(movieEpisode2);
					// }
					// if (hasHistory) {
					// if (historyEpisodeid - 1 < total) {
					// episodeList.get(historyEpisodeid - 1).setSelect(true);
					// }
					//
					// }
					// episodeAdapter.notifyDataSetChanged();
					// episodeTextView.setText("共" + total + "集 >");
					//
					// }

					if (sourceGetList != null && sourceGetList.size() > 0) {
						sourceList.addAll(sourceGetList);
						sourceList.get(0).setSelect(true);
						sourceAdapter.notifyDataSetChanged();
						
						sourceHeight = sourceCancel.getLayoutParams().height + Function.setListViewHeightBasedOnChildren(sourceListView);
						sourceTextView.setText(sourceList.get(0).getWebsite_name());
						sourceTextView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (!isSourceShow) {
									showSource();
								}					
							}
						});
					}
					String[] names = { "流畅", "标清", "高清" };
					for (int i = 1; i < 4; i++) {
						MovieUrl movieUrl = new MovieUrl();
						movieUrl.setId(i + "");
						movieUrl.setWebsite_name(names[i - 1]);
						definitionList.add(movieUrl);
					}
					definitionList.get(0).setSelect(true);
					definitionAdapter.notifyDataSetChanged();
					if (isEpisode) {
						setEpisodes();
					}

					setView();
				} else {
					Toast.makeText(getApplicationContext(), "获取信息失败", Toast.LENGTH_SHORT).show();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				MovieInfoApi movieInfoApi = new MovieInfoApi();
				movieInfo = movieInfoApi.getMovieInfo(movie.getId(), movie.getType_id());
				collectList = SerializationUtil.readCollectCache(getApplicationContext());
				historyList = SerializationUtil.readHistoryCache(getApplicationContext());
				MovieUrlApi movieUrlApi = new MovieUrlApi();
				if (isEpisode) {
					movieSourceList = movieUrlApi.getMovieEpidsodeUrlList(movie.getId(), movie.getType_id());
					if (movieSourceList != null && movieSourceList.size() > 0) {
						sourceGetList = movieSourceList.get(0).getSource_text();
						for (int i = 0; i < movieSourceList.size(); i++) {
							MovieEpisode movieEpisode2 = new MovieEpisode();
							movieEpisode2.setId(movieSourceList.get(i).getEpisodes_id());
							episodeGridList1.add(movieEpisode2);
						}
						for (int i = 1; i < movieSourceList.size(); i++) {
							List<MovieUrl> movieUrls = movieSourceList.get(i).getSource_text();
							if (!sourceGetList.containsAll(movieUrls)) {
								for (int j = 0; j < movieUrls.size(); j++) {
									if (!sourceGetList.contains(movieUrls.get(j))) {
										sourceGetList.add(movieUrls.get(j));
									}
								}
							}
						}
					}

				} else {
					sourceGetList = movieUrlApi.getMovieUrlList(movie.getId(), movie.getType_id());
				}

				return null;
			}
		}.execute("");
	}

	private void setEpisodes() {
		episodeGridList.clear();
		episodeGridList.addAll(episodeGridList1);
		int num = 0;
		for (int i = 0; i < movieSourceList.size(); i++) {
			List<MovieUrl> movieUrls = movieSourceList.get(i).getSource_text();
			MovieUrl movieUrl = sourceList.get(currSource);
			if (!movieUrls.contains(movieUrl)) {
				episodeGridList.remove(Integer.parseInt(movieSourceList.get(i).getEpisodes_id()) - 1 - num);
				num++;
			}
		}
		if (episodeGridList.size() > 0) {
			episodeGridList.get(0).setSelect(true);
		}

		episodeDownAdapter.notifyDataSetChanged();
	}

	private void setView() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(movieInfo.getImages(), movieImageView, GlobalParams.movieOptions);

		AssetManager mgr = getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		scoreTextView.setTypeface(tf);// 设置字体
		scoreTextView.setText(movieInfo.getRating() + "");
		typeTextView.setText(movieInfo.getGenres_name());
		timeTextView.setText(movieInfo.getYear());
		InitTextView();
		if (isEpisode) {
			InitImage(3);
		} else {
			InitImage(2);
		}

		InitViewPager();
		// introTextView.setText(movieInfo.getSummary());
		if (movieInfo.getMore() != null) {
			// list.addAll(movieInfo.getMore());
			// adapter.notifyDataSetChanged();
		}

		for (int i = 0; i < collectList.size(); i++) {
			Movie collectMovie = collectList.get(i);
			if (collectMovie.getId().equals(movie.getId()) && collectMovie.getType_id().equals(movie.getType_id())) {
				collectImageButton.setSelected(true);
			}
		}

		collectImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isSelect = collectImageButton.isSelected();
				collectImageButton.setSelected(!isSelect);
				if (isSelect) {
					for (int i = 0; i < collectList.size(); i++) {
						Movie co = collectList.get(i);
						if (movie.getId().equals(co.getId()) && movie.getType_id().equals(co.getType_id())) {
							collectList.remove(co);
							break;
						}
					}

				} else {
					collectList.add(movie);
				}
				SerializationUtil.wirteCollectSerialization(getApplicationContext(), (Serializable) collectList);
			}
		});

		downloadTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isDownloadShow) {
					hideDownload();
				} else {
					showDownload();
				}
			}
		});

		playTextView.setSelected(true);
		playTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				movie.setPlay_Date(System.currentTimeMillis());
				if (isEpisode) {
					movie.setEpisode_id(episodeGridList.get(0).getId());
					movie.setEpisode_num(movieInfo.getEpisodes_now());
				} else {
					movie.setEpisode_id("");
					movie.setEpisode_num("");
				}
				for (int i = 0; i < historyList.size(); i++) {
					Movie historyMovie = historyList.get(i);
					if (historyMovie.getId().equals(movie.getId())
							&& historyMovie.getType_id().equals(movie.getType_id())) {
						if (historyMovie.getEpisode_id().equals(movie.getEpisode_id())) {
							movie.setPlay_Time(historyMovie.getPlay_Time());
						} else {
							movie.setEpisode_id(historyMovie.getEpisode_id());
						}
						historyList.remove(historyMovie);
						break;
					}
				}
				int position = -1;
				for (int i = 0; i < episodeGridList.size(); i++) {
					if (episodeGridList.get(i).getId().equals(movie.getEpisode_id())) {
						position = i;
						break;
					}
				}
				setMovieSource(movie);
				historyList.add(0, movie);
				SerializationUtil.wirteHistorySerialization(getApplicationContext(), (Serializable) historyList);
				if (!isWifi) {
					String netState = SystemUtils.getNetState(getApplicationContext());
					if (!netState.equals("WIFI")) {
						isWifi = false;
					}
				}
				if (!isWifi) {
					showWifiDialog(MovieInfoActivity.this, movie, position);
					return;
				}
				startPlay(movie, position);
			}
		});
	}

	public void setMovieSource(Movie movie){
		movie.setCurrSourceNum(currSource);
		movie.setMovieSourceList(movieSourceList);
		movie.setSourceList(sourceList);
	}
	
	private int downloadNum = 0;

	private void startDownload() {
		downloadNum = 0;
		m3u8Utils = new M3U8Utils(getApplicationContext(), movie.getId(), movie.getType_id(), movie.getImages(),
				movie.getTitle(), isEpisode, sourceList.get(currSource).getWebsite_name(),
				sourceList.get(currSource).getSource_id(), hd);
		if (isEpisode) {
			PromptManager.showCancelProgressDialog(MovieInfoActivity.this);
			for (int i = 0; i < episodeGridList.size(); i++) {
				MovieEpisode movieEpisode = episodeGridList.get(i);
				if (movieEpisode.isSelect()) {
					List<MovieUrl> list = movieSourceList.get(Integer.parseInt(movieEpisode.getId()) - 1)
							.getSource_text();
					for (int j = 0; j < list.size(); j++) {
						MovieUrl movieUrl = list.get(j);
						if (movieUrl.getId().equals(sourceList.get(currSource).getId())) {
							downloadNum++;
							getUrl(movieUrl.getSource_id(), Integer.parseInt(movieEpisode.getId()));
						}
					}

				}
			}
		} else {
			downloadNum = 1;
			PromptManager.showCancelProgressDialog(MovieInfoActivity.this);
			getUrl(sourceList.get(currSource).getSource_id(), 0);
		}
	}

	private void getUrl(final String url, final int episode_id) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(result)) {
					Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
					;
				} else {
					m3u8Utils.download(episode_id);
				}
				downloadNum--;
				if (downloadNum <= 0) {
					// saveDb();
					hideDownload();
					PromptManager.closeCancelProgressDialog();
				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieUrlApi movieUrlApi = new MovieUrlApi();
				RealUrl realUrl = movieUrlApi.getUrl(url, hd + "", true);
				String downloadUrl = "";
				boolean isM3U8 = false;
				if (realUrl != null) {
					downloadUrl = realUrl.getUrl();
					if (realUrl.getFormat().equals("m3u8")) {
						isM3U8 = true;
					}
				}
				if (!StringUtils.isEmpty(downloadUrl)) {
					// downloadUrl =
					// "http://vapi.saaser.cn/v1/youtu/sm3u8/id/MDAwMDAwMDAwMJKruJKUptWsfZi82ZmNbGSaZ7rZumhtrYFsjJjEZZvXldyf0ouon4-Fu7W2gaFkeI6Iq7O3fZdpiWl3a7GGaNCVqpfVgZTAr5OXk52XsHioiHeV3q2Kga6Cim9v_3.m3u8";
//					m3u8Utils.initDownload(downloadUrl, episode_id, isM3U8);
				}
				return downloadUrl;
			}
		}.execute("");
	}

	private void showDownload() {
		isDownloadShow = true;
		downloadLayout.setVisibility(View.VISIBLE);

		downloadLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 1500, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		downloadLayout.startAnimation(bottomtranslateAnimation);
	}

	private void hideDownload() {
		if (downloadLayout.getVisibility() == View.GONE) {
			isDownloadShow = false;
			return;
		}
		isDownloadShow = false;
		downloadLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 0, 1500);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);
		bottomtranslateAnimation.setAnimationListener(new AnimationListener() {

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
				downloadLayout.clearAnimation();
				downloadLayout.setVisibility(View.GONE);
			}
		});

		downloadLayout.startAnimation(bottomtranslateAnimation);

	}

	private int sourceHeight = 1500;
	private void showSource() {
		isSourceShow = true;
		sourceLayout.setVisibility(View.VISIBLE);

		sourceLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, sourceHeight, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		sourceLayout.startAnimation(bottomtranslateAnimation);

	}

	private void hideSource() {
		if (sourceLayout.getVisibility() == View.GONE) {
			isSourceShow = false;
			return;
		}
		isSourceShow = false;
		sourceLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 0, sourceHeight);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);
		bottomtranslateAnimation.setAnimationListener(new AnimationListener() {

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
				sourceLayout.clearAnimation();
				sourceLayout.setVisibility(View.GONE);
			}
		});

		sourceLayout.startAnimation(bottomtranslateAnimation);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isStart) {
			isStart = true;
			return;
		} else {
			historyList = SerializationUtil.readHistoryCache(getApplicationContext());
			if (historyList == null) {
				historyList = new ArrayList<Movie>();
			}
			refreshEpisode();
		}
		// getData();
	}

	private void refreshEpisode() {
		if (isEpisode) {
			((EpisodeFragment) fragmentList.get(1)).refreshEpisode();
		}
	}

	private void resetEpisode(){
		if (isEpisode) {
			((EpisodeFragment) fragmentList.get(1)).resetData(episodeGridList);
		}
	}
	
	public void showWifiDialog(final FragmentActivity activity, final Movie playMovie, final int position) {

		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		// 设置它的ContentView
		View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.iswifi_dialog, null);
		TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
		TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
		dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				startPlay(playMovie, position);
			}
		});

		dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.cancel();
			}
		});

		view.setMinimumWidth(600);
		dialog.setContentView(view);
		dialog.show();

	}

	public void startPlay(Movie playMovie, int position) {
		Intent intent = new Intent(getApplicationContext(), Video2DPlayerActivity.class);
		intent.putExtra("movie", playMovie);
		startActivity(intent);
	}

	// viewpager+fragment
	private int currIndex;// 当前页卡编号
	private int bmpW;// 横线图片宽度
	private int offset;// 图片移动的偏移量
	private ImageView imageView;
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private TextView view1, view2, view3;
	private List<TextView> textList;

	/*
	 * 初始化ViewPager
	 * 在image后调用
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		IntroFragment introFragment = new IntroFragment();
		Bundle introBundle = new Bundle();
		introBundle.putString("intro", movieInfo.getSummary());
		introBundle.putString("director", movieInfo.getDirectors());
		introBundle.putString("casts", movieInfo.getCasts());
		introFragment.setArguments(introBundle);
		fragmentList.add(introFragment);

		if (isEpisode) {
			EpisodeFragment episodeFragment = new EpisodeFragment();
			Bundle episodeBundle = new Bundle();
			episodeBundle.putSerializable("episode", (Serializable) episodeGridList);
			episodeBundle.putSerializable("movie", (Serializable) movie);
			episodeBundle.putSerializable("historyList", (Serializable) historyList);
			episodeBundle.putBoolean("isWifi", isWifi);
			episodeFragment.setArguments(episodeBundle);
			fragmentList.add(episodeFragment);
		}

		TuijianFragment tuijianFragment = new TuijianFragment();
		if (movieInfo.getMore() != null) {
			Bundle tuijianBundle = new Bundle();
			tuijianBundle.putSerializable("tuijian", (Serializable) movieInfo.getMore());
			tuijianFragment.setArguments(tuijianBundle);
		}

		fragmentList.add(tuijianFragment);

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
		mPager.setOffscreenPageLimit(3);
	}

	/*
	 * 初始化标签名
	 * 
	 */
	public void InitTextView() {
		textList = new ArrayList<>();
		view1 = (TextView) findViewById(R.id.intro_textView);
		view2 = (TextView) findViewById(R.id.episodes_textView);
		view3 = (TextView) findViewById(R.id.tuijian_textView);
		view1.setSelected(true);
		view1.setVisibility(View.VISIBLE);
		view2.setVisibility(View.VISIBLE);
		view3.setVisibility(View.VISIBLE);
		view1.setOnClickListener(new txListener(0));
		textList.add(view1);
		if (isEpisode) {
			view2.setOnClickListener(new txListener(1));
			textList.add(view2);
			view3.setOnClickListener(new txListener(2));
			textList.add(view3);
		} else {
			view2.setVisibility(View.GONE);
			view3.setOnClickListener(new txListener(1));
			textList.add(view3);
		}

	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPager.setCurrentItem(index);
		}
	}

	/*
	 * 初始化图片的位移像素 num为页面数
	 * 最先调用
	 */
	public void InitImage(int num) {
		imageView = (ImageView) findViewById(R.id.cursor);
		imageView.setVisibility(View.VISIBLE);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.line).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / num - bmpW) / 2;

		// imgageview设置平移，使下划线平移到初始位置（平移一个offset）
		Animation animation = new TranslateAnimation(0, offset, 0, 0);// 平移动画
		animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
		animation.setDuration(0);// 动画持续时间0.2秒
		imageView.clearAnimation();
		imageView.startAnimation(animation);// 是用ImageView来显示动画的
		// Matrix matrix = new Matrix();
		// matrix.postTranslate(offset, 0);
		// imageView.setImageMatrix(matrix);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		private int one = offset * 2 + bmpW - 10;// 两个相邻页面的偏移量

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			textList.get(currIndex).setSelected(false);
			Animation animation = new TranslateAnimation(currIndex * one + offset, arg0 * one + offset, 0, 0);// 平移动画
			currIndex = arg0;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			imageView.clearAnimation();
			imageView.startAnimation(animation);// 是用ImageView来显示动画的
			textList.get(currIndex).setSelected(true);
		}
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}
	}
}
