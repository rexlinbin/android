package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.DownloadEpisodeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.InfoEpisodeAdapter;
import com.bccv.zhuiyingzhihanju.adapter.InfoEpisodeNumAdapter;
import com.bccv.zhuiyingzhihanju.adapter.TypeInfoCommentAdapter;
import com.bccv.zhuiyingzhihanju.adapter.VideoInfoLikeAdapter;
import com.bccv.zhuiyingzhihanju.api.CollectApi;
import com.bccv.zhuiyingzhihanju.api.CommentApi;
import com.bccv.zhuiyingzhihanju.api.MovieInfoApi;
import com.bccv.zhuiyingzhihanju.api.MovieUrlApi;
import com.bccv.zhuiyingzhihanju.model.Comment;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;
import com.bccv.zhuiyingzhihanju.model.MovieEpisodeNum;
import com.bccv.zhuiyingzhihanju.model.MovieInfo;
import com.bccv.zhuiyingzhihanju.model.MovieSource;
import com.bccv.zhuiyingzhihanju.model.MovieUrl;
import com.bccv.zhuiyingzhihanju.model.RealUrl;
import com.bccv.zhuiyingzhihanju.model.User;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tendcloud.tenddata.TCAgent;
import com.utils.net.NetUtil;
import com.utils.share.ShareSDK;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.ImageUtils;
import com.utils.tools.Logger;
import com.utils.tools.M3U8Utils;
import com.utils.tools.PromptManager;
import com.utils.tools.StringUtils;
import com.utils.views.CircleImageView;
import com.utils.views.HorizontalListView;
import com.utils.views.MyGridView;
import com.utils.views.MyScrollView;
import com.utils.views.MyScrollView.OnScrollListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnScrollChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class VideoInfoActivity extends BaseActivity {

	private ImageView img;
	private ImageView DropImage;
	private TextView infoTitle, des, score, type, Director, actor, up, textView;
	private TextView Startbtn;
	private TextView commTextView, context;
	private HorizontalListView infoEpisodeNumListView;
	private GridView infoEpisodeGridView;
	private List<MovieEpisode> infoEpisodeList;
	InfoEpisodeAdapter infoEpisodeAdapter;
	private List<Movie> infoLikeList;
	VideoInfoLikeAdapter movieLikeAdapter;
	private ListView infoLikeListView;
	private MovieInfo movieInfo;
	private String movie_id, type_id, episodes_id;
	private List<MovieEpisodeNum> infoEpisodeNumList;
	InfoEpisodeNumAdapter infoEpisodeNumAdapter;
	private int gridViewNum = 6;
	private TextView infoCollect, infoShare, infoDownload, cancelTextView;
	LinearLayout downloadLayout, shareLayout, shareAnimLayout, downloadAnimLayout;
	private TextView downloadSure, downloadCancel;
	MyGridView episodeGridView;
	private List<MovieEpisode> downloadList;
	DownloadEpisodeAdapter episodeDownAdapter;
	private int episodeSelectNum;
	private ImageView portFullImageView, shareQQ, shareQZ, shareWX, shareWXC, shareSina;
	private String image_url, movie_title;
	private String titleString = "";
	private List<Comment> getCommentList, commList;
	private boolean isEpisode = false;
	private String source_id;
	private int currEpisodeNum = 0;
	private int currSourceNum = 0;

	private int hd = 2;
	private ListView commListView;
	ListView commPullToRefreshListView;
	TypeInfoCommentAdapter commAdapter;
	private int commPage = 1, commCount = 10;
	boolean isFirst = true;
	private List<MovieEpisode> getEpisodeList = new ArrayList<>();
	private List<MovieSource> movieSourceList = new ArrayList<>();
	private DataAsyncTask getUrlTask;
	private CircleImageView circleImageView;
	private PullToRefreshScrollView scrollView;
	private String platform;
	private ImageView bgImageView;
	private TextView commTo;
	Bitmap newImage ;
	private void tcStart() {
		TCAgent.onPageStart(getApplicationContext(), "VideoInfoActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoinfo);
		tcStart();
		// mIsHwDecode = getIntent().getBooleanExtra("isHW", false);
		movie_id = getIntent().getStringExtra("movie_id");
		type_id = getIntent().getStringExtra("type_id");

		episodes_id = getIntent().getStringExtra("episodes_id");

		isEpisode = getIntent().getBooleanExtra("isEpisode", false);
		isEpisode = true;

		ImageButton BackBtn = (ImageButton) findViewById(R.id.titel_back);

		BackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		init();
	}

	private TextView type1;
	LinearLayout linearLayout;

	private void init() {
		// TODO Auto-generated method stub
		title = (TextView) findViewById(R.id.titleName_textView);
		linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
		type1 = (TextView) findViewById(R.id.videoninfo_type1);
		type1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scrollView.getRefreshableView().post(new Runnable() {
					@Override
					public void run() {
						// To change body of implemented methods use File |
						// Settings | File Templates.
						// mRootScrollView.fullScroll(ScrollView.FOCUS_DOWN);
						MyScrollView mRefreshableView = scrollView.getRefreshableView();
						int location = ((View) commTextView.getParent().getParent()).getTop()
								+ ((View) commTextView.getParent()).getTop();
						// int offset = linearLayout.getMeasuredHeight() -
						// location;
						// if (offset < 0) {
						// offset = 0;
						// }

						mRefreshableView.smoothScrollTo(0, location);
					}
				});
			}
		});
		bgImageView = (ImageView) findViewById(R.id.bg_imageView);
		scrollView = (PullToRefreshScrollView) findViewById(R.id.scrollView1);

		scrollView.getRefreshableView().setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(int scrollY) {
				// TODO Auto-generated method stub
				if (scrollY < 255) {
					bgImageView.setImageAlpha(255 - scrollY);
					title.setTextColor(Color.WHITE);
				} else {
					bgImageView.setImageAlpha(0);
					title.setTextColor(Color.DKGRAY);
				}
			}
		});
		img = (ImageView) findViewById(R.id.videoinfo_image);
		infoTitle = (TextView) findViewById(R.id.videoinfo_title);
		des = (TextView) findViewById(R.id.videoinfo_context);

		score = (TextView) findViewById(R.id.videoinfo_score);

		type = (TextView) findViewById(R.id.videoinfo_type);

		actor = (TextView) findViewById(R.id.videoinfo_Actor);
		Director = (TextView) findViewById(R.id.videoinfo_Director);
		up = (TextView) findViewById(R.id.videoinfo_Up);
		Startbtn = (TextView) findViewById(R.id.videoinfo_startBtn);
		commTextView = (TextView) findViewById(R.id.comm_textView);

		context = (TextView) findViewById(R.id.info_intro_textView);

		DropImage = (ImageView) findViewById(R.id.info_drop_imageView);

		infoEpisodeNumListView = (HorizontalListView) findViewById(R.id.info_episode_horizontalListView);

		infoEpisodeGridView = (GridView) findViewById(R.id.info_episode_gridView);
		infoLikeListView = (ListView) findViewById(R.id.vedioinfo_listView);

		infoLikeListView.setSelector(new ColorDrawable(android.R.color.transparent));

		infoCollect = (TextView) findViewById(R.id.info_collect_textView);
		infoShare = (TextView) findViewById(R.id.share_textView);

		infoShare.setVisibility(View.VISIBLE);
		infoDownload = (TextView) findViewById(R.id.info_download_textView);

		shareAnimLayout = (LinearLayout) findViewById(R.id.share_anim_layout);
		downloadAnimLayout = (LinearLayout) findViewById(R.id.download_anim_layout);
		commTo = (TextView) findViewById(R.id.videoninfo_type1);
		circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
		if (GlobalParams.hasLogin) {
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(GlobalParams.user.getAvatars(), circleImageView, GlobalParams.iconOptions);
		}
//		Startbtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				Intent aIntent = new Intent(VideoInfoActivity.this, Video2DPlayerActivity.class);
//
//				aIntent.putExtra("movie_id", movie_id);
//				aIntent.putExtra("type_id", type_id);
//				aIntent.putExtra("episodes_id", infoEpisodeList.size() + "");
//				aIntent.putExtra("movieInfo", movieInfo);
//				startActivity(aIntent);
//
//			}
//		});

		// 评论

		commListView = (ListView) findViewById(R.id.comm_pullToRefreshListView);

		commListView.setDividerHeight(0);

		commList = new ArrayList<Comment>();
		commAdapter = new TypeInfoCommentAdapter(getApplicationContext(), VideoInfoActivity.this, commList);
		commAdapter.setInfo(true);

		commListView.setAdapter(commAdapter);
		commListView.setSelector(new ColorDrawable(android.R.color.transparent));

		scrollView.setMode(Mode.BOTH);

		scrollView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

				getCommData(true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				getCommData(false);
			}
		});

		initComment();
		initDot();

		textView = (TextView) findViewById(R.id.textView1);
		textView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					showComment();
				} else {
					Toast.makeText(getApplicationContext(), "请先登录！", 1).show();
				}
			}
		});

		// 剧集

		infoEpisodeNumList = new ArrayList<>();
		infoEpisodeNumAdapter = new InfoEpisodeNumAdapter(getApplicationContext(), infoEpisodeNumList);
		infoEpisodeNumListView.setAdapter(infoEpisodeNumAdapter);
		infoEpisodeNumListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				for (int i = 0; i < infoEpisodeNumList.size(); i++) {
					infoEpisodeNumList.get(i).setSelect(false);
				}
				infoEpisodeNumList.get(position).setSelect(true);
				infoEpisodeNumAdapter.notifyDataSetChanged();

				getEpisodeData(position);
				infoEpisodeAdapter.notifyDataSetChanged();
			}
		});

		infoEpisodeList = new ArrayList<>();
		infoEpisodeAdapter = new InfoEpisodeAdapter(getApplicationContext(), infoEpisodeList);
		infoEpisodeGridView.setAdapter(infoEpisodeAdapter);
		infoEpisodeGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		infoEpisodeGridView.setNumColumns(6);
		infoEpisodeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				for (int i = 0; i < infoEpisodeList.size(); i++) {
					infoEpisodeList.get(i).setSelect(false);
				}
				infoEpisodeList.get(position).setSelect(true);
				infoEpisodeAdapter.notifyDataSetChanged();
				// changeEpisode(infoEpisodeList.get(position).getId());

				Intent aIntent = new Intent(VideoInfoActivity.this, Video2DPlayerActivity.class);

				aIntent.putExtra("movie_id", movie_id);
				aIntent.putExtra("type_id", type_id);
				aIntent.putExtra("episodes_id", infoEpisodeList.get(position).getId());
				aIntent.putExtra("variety", infoEpisodeList.get(position).getPeriods());
				aIntent.putExtra("movieInfo", movieInfo);

				startActivity(aIntent);

			}
		});

		// 下载
		downloadLayout = (LinearLayout) findViewById(R.id.download_layout);
		downloadLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideDownload();
			}
		});

		downloadSure = (TextView) findViewById(R.id.download_sure_textView);
		downloadCancel = (TextView) findViewById(R.id.download_cancel_textView);

		episodeGridView = (MyGridView) findViewById(R.id.episode_gridView);
		downloadList = new ArrayList<>();
		episodeDownAdapter = new DownloadEpisodeAdapter(getApplicationContext(), downloadList);
		episodeDownAdapter.setMovieInfo(movie_id, type_id);
		episodeGridView.setAdapter(episodeDownAdapter);
		episodeGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		episodeGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				boolean isSelect = downloadList.get(position).isSelect();
				downloadList.get(position).setSelect(!isSelect);
				if (isSelect) {
					episodeSelectNum--;
				} else {
					episodeSelectNum++;
				}

				if (episodeSelectNum > 0) {
					downloadSure.setSelected(true);
				} else {
					downloadSure.setSelected(false);
				}
				episodeDownAdapter.notifyDataSetChanged();
			}
		});

		downloadCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideDownload();
			}
		});

		downloadSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (downloadSure.isSelected()) {
					startDownload();
				}
			}
		});

		// 分享
		shareLayout = (LinearLayout) findViewById(R.id.share_layout);
		shareLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideShare();
			}
		});
		cancelTextView = (TextView) findViewById(R.id.cancel_textView);
		cancelTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareLayout.setVisibility(View.GONE);
			}
		});

		shareQQ = (ImageView) findViewById(R.id.share_QQ);
		shareQQ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareQQ(shareObject.getString("title"), shareObject.getString("content"),
							shareObject.getString("thumb"), shareObject.getString("url"));
				}
			}
		});
		shareQZ = (ImageView) findViewById(R.id.share_QQspace);
		shareQZ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareQZone(shareObject.getString("title"), shareObject.getString("content"),
							shareObject.getString("thumb"), shareObject.getString("url"));
				}
			}
		});
		shareWX = (ImageView) findViewById(R.id.share_weixin);
		shareWX.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareWeiXin(shareObject.getString("title"), shareObject.getString("content"),
							"http://img.zhuiying.me/hahjumi/108.png", shareObject.getString("url"));
					
					
					
				}
			}
		});
		shareWXC = (ImageView) findViewById(R.id.share_winxinquan);
		shareWXC.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareWeiXinCircle(shareObject.getString("title"), shareObject.getString("content"),
							"http://img.zhuiying.me/hahjumi/108.png", shareObject.getString("url"));
				}
			}
		});
		shareSina = (ImageView) findViewById(R.id.share_sina);
		shareSina.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (shareSDK != null) {
					shareSDK.shareSina(shareObject.getString("title"), shareObject.getString("content"),
							"http://img.zhuiying.me/hahjumi/108.png", shareObject.getString("url"));
				}
			}
		});

		// 猜你喜欢
		infoLikeList = new ArrayList<>();
		movieLikeAdapter = new VideoInfoLikeAdapter(this, infoLikeList);
		infoLikeListView.setAdapter(movieLikeAdapter);
		infoLikeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Movie movie = infoLikeList.get(position);
				Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
				intent.putExtra("movie_id", movie.getId());
				intent.putExtra("type_id", movie.getType_id());
				intent.putExtra("episodes_id", "0");
				intent.putExtra("isEpisode", true);
				startActivity(intent);
				finish();
			}
		});
		sourceList = new ArrayList<MovieUrl>();

		getInfo();
	}

	TextView title;

	// 设置详情数据
	private void setInfoData() {

		if (movieInfo.getTitle().length() > 8) {
			infoTitle.setText(movieInfo.getTitle().substring(0, 8) + "...");

		} else {
			infoTitle.setText(movieInfo.getTitle());

		}
		Startbtn.setText("更新：" + StringUtils.strToDateLong(movieInfo.getUtime() * 1000));
		if (type_id.equals("music") || type_id.equals("5")) {
			context.setText(movieInfo.getCasts());
		} else if (type_id.equals("news") || type_id.equals("13")) {
			context.setText(movieInfo.getDes());
		} else {
			context.setText(movieInfo.getDirectors() + "\n" + movieInfo.getCasts());
		}

		if (movieInfo.getSummary().length() > 51) {

			context.setText(movieInfo.getSummary().substring(0, 51) + "......");
			DropImage.setImageResource(R.drawable.expansion);
			DropImage.setVisibility(View.VISIBLE);

		} else {
			context.setText(movieInfo.getSummary());
			DropImage.setVisibility(View.GONE);

		}

		des.setText(movieInfo.getDes());

		score.setText(movieInfo.getRating());
		type.setText(movieInfo.getGenres_name());

		Director.setText(movieInfo.getDirectors());

		actor.setText(movieInfo.getCasts());

		up.setText("UP主:" + movieInfo.getUp_name());
		// 评论数量
		int num = Integer.parseInt(movieInfo.getComments_count());

		String commNum = "评论(<font color='#44b549'>" + num + "</font>)";
		commTextView.setText(Html.fromHtml(commNum));
		commTo.setText(Html.fromHtml(commNum));
		ImageLoader imageLoader = ImageLoader.getInstance();

		imageLoader.displayImage(movieInfo.getImages(), img, GlobalParams.foundOptions, new ImageLoadingListener() {

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub

				try {

					Bitmap newImage =ImageUtils.doBlur(loadedImage, 80, false);
					setImage(newImage);

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub

			}
		});
		
		title.setVisibility(View.VISIBLE);
		title.setText("详情");
		// 简介下拉
		DropImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DropImage.setSelected(!DropImage.isSelected());

				if (DropImage.isSelected()) {

					context.setText(movieInfo.getSummary());
					DropImage.setImageResource(R.drawable.contraction);
				} else {

					context.setText(movieInfo.getSummary().substring(0, 51) + "......");
					DropImage.setImageResource(R.drawable.expansion);

				}
			}
		});

		if (movieInfo.getMore().size() > 3) {
			infoLikeList.add(movieInfo.getMore().get(0));
			infoLikeList.add(movieInfo.getMore().get(1));
			infoLikeList.add(movieInfo.getMore().get(2));

		} else {
			infoLikeList.addAll(movieInfo.getMore());
		}

		movieLikeAdapter.notifyDataSetChanged();
		//
		// int currNum = 0;
		//
		// int gridPageNum = gridViewNum * 2;
		//
		// for (int i = 0; i < getEpisodeList.size(); i++) {
		// if (getEpisodeList.get(i).getId().equals(episodes_id)) {
		// currNum = i / gridPageNum;
		// }
		// }
		//
		// int num = (getEpisodeList.size() - 1) / gridPageNum;
		// for (int i = 0; i < num + 1; i++) {
		// MovieEpisodeNum movieEpisodeNum = new MovieEpisodeNum();
		// // if (i * gridPageNum + gridPageNum >= getEpisodeList.size()) {
		// // movieEpisodeNum.setId(getEpisodeList.get(i * gridPageNum).getId()
		// // + "-" +
		// // getEpisodeList.get(getEpisodeList.size() - 1).getId());
		// // } else {
		// // movieEpisodeNum.setId(getEpisodeList.get(i * gridPageNum).getId()
		// // + "-" + getEpisodeList.get(i * gridPageNum + gridPageNum -
		// // 1).getId());
		// // }
		// movieEpisodeNum.setId((i + 1) + "P");
		// if (i == currNum) {
		// movieEpisodeNum.setSelect(true);
		// } else {
		// movieEpisodeNum.setSelect(false);
		// }
		// infoEpisodeNumList.add(movieEpisodeNum);
		// }
		//
		// getEpisodeData(currNum);
		//
		// if (type_id.equals("movie")) {
		// infoEpisodeNumListView.setVisibility(View.INVISIBLE);
		// }
		//
		// infoEpisodeNumAdapter.notifyDataSetChanged();
		// infoEpisodeGridView.setNumColumns(gridViewNum);
		// infoEpisodeAdapter.setGridNum(gridViewNum);
		// infoEpisodeAdapter.notifyDataSetChanged();

		infoCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobalParams.hasLogin) {
					v.setSelected(!v.isSelected());
					if (v.isSelected()) {
						collectMovie();
					} else {
						discollectMovie();
					}
				} else {
					startLogin();
				}

			}
		});

		if (movieInfo.getLike() == 0) {
			infoCollect.setSelected(false);
		} else {
			infoCollect.setSelected(true);
		}

		infoDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!GlobalParams.isWifi) {
					if (!NetUtil.isWIFI(getApplicationContext())) {
						showIsWifiDownloadDialog(VideoInfoActivity.this);
						return;
					}
				}
				if (GlobalParams.hasLogin) {
					showDownload(movieInfo.getBimages(), movieInfo.getTitle());
				} else {
					startLogin();
				}

			}
		});

		infoShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("title", movieInfo.getTitle());
				jsonObject.put("thumb", movieInfo.getImages());
				jsonObject.put("content", movieInfo.getSummary());
				jsonObject.put("url", movieInfo.getShare());

				showShare(jsonObject);
			}
		});

	}

	private void getEpisodeData(int num) {
		infoEpisodeList.clear();
		for (int i = num * gridViewNum * 2; i < (num + 1) * gridViewNum * 2; i++) {
			if (i < getEpisodeList.size()) {
				MovieEpisode movieEpisode = new MovieEpisode();
				movieEpisode.setId(getEpisodeList.get(i).getId());
				movieEpisode.setDes(getEpisodeList.get(i).getDes());
				movieEpisode.setPeriods(getEpisodeList.get(i).getPeriods());

				if (episodes_id.equals("0")) {
					if (i == 0) {
						movieEpisode.setSelect(true);
					} else {
						movieEpisode.setSelect(false);
					}
				} else {
					if (movieEpisode.getId().equals(episodes_id)) {
						movieEpisode.setSelect(true);
					} else {
						movieEpisode.setSelect(false);
					}
				}

				infoEpisodeList.add(movieEpisode);
			}

		}
	}

	private void getEpisode() {
		getEpisodeList.clear();

		for (int i = 0; i < movieSourceList.size(); i++) {
			MovieSource movieSource = movieSourceList.get(i);
			MovieEpisode movieEpisode = new MovieEpisode();
			movieEpisode.setId(movieSource.getEpisodes_id());
			movieEpisode.setDes(movieSource.getDes());
			movieEpisode.setPeriods(movieSource.getPeriods());

			if (i == 0) {
				if (!StringUtils.isEmpty(movieSource.getPeriods())) {
					gridViewNum = 2;
				} else if (!StringUtils.isEmpty(movieSource.getDes())) {
					gridViewNum = 3;
				} else {
					gridViewNum = 6;
				}
			}

			if (!StringUtils.isEmpty(movieSource.getEpisodes_id())
					&& movieSource.getEpisodes_id().equals(episodes_id)) {
				movieEpisode.setSelect(true);
				currEpisodeNum = i;
			} else {
				movieEpisode.setSelect(false);
			}

			// getEpisodeList.add(movieEpisode);
			getEpisodeList.add(0, movieEpisode);
		}

		// if (currEpisodeNum == getEpisodeList.size() - 1) {
		// isEnd = true;
		// }
		currEpisodeNum = getEpisodeList.size() - 1 - currEpisodeNum;

		downloadList.clear();

		for (int i = 0; i < getEpisodeList.size(); i++) {

			MovieEpisode movieEpisode = new MovieEpisode();
			movieEpisode.setId(getEpisodeList.get(i).getId());
			movieEpisode.setDes(getEpisodeList.get(i).getDes());
			movieEpisode.setPeriods(getEpisodeList.get(i).getPeriods());
			movieEpisode.setSelect(false);
			downloadList.add(movieEpisode);
		}
		episodeGridView.setNumColumns(gridViewNum);
		episodeDownAdapter.setGridNum(gridViewNum);
		episodeDownAdapter.notifyDataSetChanged();
	}

	// 收藏
	private void collectMovie() {
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
				CollectApi collectApi = new CollectApi();
				collectApi.collect(GlobalParams.user.getUid(), movie_id, type_id, GlobalParams.user.getToken());
				return null;
			}
		}.execute("");
	}

	// 取消收藏
	private void discollectMovie() {
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
				CollectApi collectApi = new CollectApi();
				collectApi.deleteCollect(GlobalParams.user.getUid(), movie_id, type_id, GlobalParams.user.getToken());
				return null;
			}
		}.execute("");
	}

	// 点赞
	public void diggMovie(final String comment_id) {
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
				CommentApi commentApi = new CommentApi();
				commentApi.digg(comment_id);
				return null;
			}
		}.execute("");
	}

	public void setImage(Bitmap bitmap) {
		// RelativeLayout layout = (RelativeLayout)
		// findViewById(R.id.re_videolayout);
		//
		//

		// LinearLayout lin = (LinearLayout) findViewById(R.id.title);
		// lin.setBackgroundColor(getBaseContext().getResources().getColor(R.color.white));

		// bgImageView.setBackgroundDrawable(ImageUtils.bitmapToDrawable(bitmap));
		bgImageView.setImageBitmap(bitmap);
		// TextView title = (TextView) findViewById(R.id.titleName_textView);
		// title.setVisibility(View.VISIBLE);
		// title.setText("详情");
	}

	// 登陆
	private void startLogin() {
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.putExtra("type", "y");
		startActivity(intent);
	}

	private JSONObject shareObject;
	private ShareSDK shareSDK;

	// 显示分享
	public void showShare(JSONObject jsonObject) {

		shareObject = jsonObject;
		if (shareSDK == null) {
			shareSDK = new ShareSDK(this);
		}
		shareLayout.setVisibility(View.VISIBLE);
		shareAnimLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 1500, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		shareAnimLayout.startAnimation(bottomtranslateAnimation);
	}

	// 隐藏分享
	private void hideShare() {
		if (shareLayout.getVisibility() == View.GONE) {
			return;
		}

		shareAnimLayout.clearAnimation();
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
				shareAnimLayout.clearAnimation();
				shareLayout.setVisibility(View.GONE);
			}
		});

		shareAnimLayout.startAnimation(bottomtranslateAnimation);

	}

	private List<MovieUrl> sourceList, getSourceList;

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub

				if (getSourceList != null && getSourceList.size() > 0) {
					getSource();
					getEpisode();
					source_id = getSourceList.get(currSourceNum).getSource_url();

					int currNum = 0;

					int gridPageNum = gridViewNum * 2;

					for (int i = 0; i < getEpisodeList.size(); i++) {
						if (getEpisodeList.get(i).getId().equals(episodes_id)) {
							currNum = i / gridPageNum;
						}
					}

					int num = (getEpisodeList.size() - 1) / gridPageNum;
					for (int i = 0; i < num + 1; i++) {
						MovieEpisodeNum movieEpisodeNum = new MovieEpisodeNum();
						// if (i * gridPageNum + gridPageNum >=
						// getEpisodeList.size()) {
						// movieEpisodeNum.setId(getEpisodeList.get(i *
						// gridPageNum).getId()
						// + "-" +
						// getEpisodeList.get(getEpisodeList.size() -
						// 1).getId());
						// } else {
						// movieEpisodeNum.setId(getEpisodeList.get(i *
						// gridPageNum).getId()
						// + "-" + getEpisodeList.get(i * gridPageNum +
						// gridPageNum -
						// 1).getId());
						// }
						movieEpisodeNum.setId((i + 1) + "P");
						if (i == currNum) {
							movieEpisodeNum.setSelect(true);
						} else {
							movieEpisodeNum.setSelect(false);
						}
						infoEpisodeNumList.add(movieEpisodeNum);
					}

					getEpisodeData(currNum);

					if (type_id.equals("movie")) {
						infoEpisodeNumListView.setVisibility(View.INVISIBLE);
					}

					infoEpisodeNumAdapter.notifyDataSetChanged();
					infoEpisodeGridView.setNumColumns(gridViewNum);
					infoEpisodeAdapter.setGridNum(gridViewNum);
					infoEpisodeAdapter.notifyDataSetChanged();
					getCommData(true);
				} else {

					Toast.makeText(getApplicationContext(), "解析失败", Toast.LENGTH_SHORT).show();
					// if (currSourceNum == list.size() - 1) {
					// currSourceNum = 0;
					// } else {
					// currSourceNum++;
					// }
					// sourceHandler.sendEmptyMessage(0);

				}

			}
		};
		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieUrlApi movieUrlApi = new MovieUrlApi();
				currSourceNum = 0;
				if (isEpisode) {
					movieSourceList = movieUrlApi.getMovieEpidsodeUrlList(movie_id, type_id);

					if (movieSourceList != null && movieSourceList.size() > 0) {
						getSourceList = new ArrayList<>();
						getSourceList.addAll(movieSourceList.get(0).getSource_text());
						for (int i = 1; i < movieSourceList.size(); i++) {
							List<MovieUrl> movieUrls = movieSourceList.get(i).getSource_text();
							if (movieUrls != null && !getSourceList.containsAll(movieUrls)) {
								for (int j = 0; j < movieUrls.size(); j++) {
									if (!getSourceList.contains(movieUrls.get(j))) {
										getSourceList.add(movieUrls.get(j));
									}
								}
							}
						}
					}

				} else {
					getSourceList = movieUrlApi.getMovieUrlList(movie_id, type_id);
				}

				// MovieInfoApi movieInfoApi = new MovieInfoApi();
				// movieInfo = movieInfoApi.getMovieInfo(movie_id, type_id);
				//
				// CommentApi commentApi = new CommentApi();
				// getCommentList = commentApi.getCommentList(movie_id, type_id,
				// "1", "5");
				return null;
			}
		}.execute("");

	}

	private void getInfo() {
		// Logger.e("time", System.currentTimeMillis() + "");
		Callback infoCallBack = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (movieInfo != null) {
					titleString = movieInfo.getTitle();
					// Logger.e("time", System.currentTimeMillis() + "");
					setInfoData();
					getData();
					// Logger.e("time", System.currentTimeMillis() + "");

				} else {
					titleString = "";
					Toast.makeText(getApplicationContext(), "未获取到详情信息", 1).show();
				}
			}
		};

		new DataAsyncTask(infoCallBack, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				try {
					MovieInfoApi movieInfoApi = new MovieInfoApi();
					movieInfo = movieInfoApi.getMovieInfo(movie_id, type_id);
					return "true";
				} catch (Exception e) {
					// TODO: handle exception
				}

				return "false";
			}
		}.execute("");

	}

	private void getSource() {
		if (getSourceList == null) {
			return;
		}

		for (int i = 0; i < movieSourceList.size(); i++) {
			MovieSource movieSource = movieSourceList.get(i);
			if (episodes_id.equals("0")) {
				episodes_id = movieSourceList.get(movieSourceList.size() - 1).getEpisodes_id();
			}
			if (movieSource.getEpisodes_id().equals(episodes_id)) {
				List<MovieUrl> sourceTextList = movieSource.getSource_text();
				if (sourceTextList == null) {
					continue;
				}
				currSourceNum = 0;
				for (int j = 0; j < sourceTextList.size(); j++) {
					if (sourceTextList.get(j).getSource_id().equals(getSourceList.get(currSourceNum).getSource_id())) {
						currSourceNum = j;
						sourceTextList.get(j).setSelect(true);
					} else {
						sourceTextList.get(j).setSelect(false);
					}
				}
				sourceTextList.get(currSourceNum).setSelect(true);
				getSourceList = sourceTextList;
				break;
			}
		}

		if (getSourceList != null && getSourceList.size() > 0) {
			sourceList.clear();
			sourceList.addAll(getSourceList);

		}
	}

	public void showIsWifiDownloadDialog(final Activity activity) {

		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		// 设置它的ContentView
		View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.isgoon_dialog, null);
		TextView tv = (TextView) view.findViewById(R.id.dialog_message);
		TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
		dialog_enter.setText("开启");
		TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
		dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GlobalParams.isWifi = true;
				AppConfig.setWifi(GlobalParams.isWifi);
				Toast.makeText(getApplicationContext(), "已开启", 1).show();
				dialog.cancel();
			}
		});
		tv.setText("非wifi环境下载可能导致超额流量，确定开启");
		view.setMinimumWidth(600);
		dialog.setContentView(view);
		dialog.show();

	}

	// 显示下载
	public void showDownload(String image_url, String movie_title) {
		// isDownloadShow = true;
		this.image_url = image_url;
		this.movie_title = movie_title;
		downloadLayout.setVisibility(View.VISIBLE);

		downloadAnimLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 1500, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		downloadAnimLayout.startAnimation(bottomtranslateAnimation);
	}

	// 隐藏下载
	private void hideDownload() {
		if (downloadLayout.getVisibility() == View.GONE) {
			// isDownloadShow = false;
			return;
		}
		// isDownloadShow = false;
		downloadAnimLayout.clearAnimation();
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
				downloadAnimLayout.clearAnimation();
				downloadLayout.setVisibility(View.GONE);

			}
		});

		downloadAnimLayout.startAnimation(bottomtranslateAnimation);
		// downloadLayout.setVisibility(View.GONE);

	}

	private int downloadNum = 0;
	private M3U8Utils m3u8Utils;

	private void startDownload() {
		downloadNum = 0;
		m3u8Utils = new M3U8Utils(getApplicationContext(), movie_id, type_id, image_url, movie_title, isEpisode,
				sourceList.get(currSourceNum).getSource_name(), sourceList.get(currSourceNum).getSource_url(), hd);
		if (isEpisode) {
			PromptManager.showDownloadCancelProgressDialog(VideoInfoActivity.this);

			for (int i = 0; i < downloadList.size(); i++) {
				MovieEpisode movieEpisode = downloadList.get(i);
				if (movieEpisode.isSelect()) {
					List<MovieUrl> list = null;
					for (int j = 0; j < movieSourceList.size(); j++) {
						if (movieSourceList.get(j).getEpisodes_id().equals(movieEpisode.getId())) {
							list = movieSourceList.get(j).getSource_text();
						}
					}
					if (list != null) {
						for (int j = 0; j < list.size(); j++) {
							MovieUrl movieUrl = list.get(j);
							if (movieUrl.getSource_id().equals(sourceList.get(currSourceNum).getSource_id())) {
								downloadNum++;
								getDownloadUrl(movieUrl.getSource_url(), Integer.parseInt(movieEpisode.getId()));
							}
						}

						if (downloadNum == 0 && list.size() > 0) {
							downloadNum++;
							getDownloadUrl(list.get(0).getSource_url(), Integer.parseInt(movieEpisode.getId()));
						}
					}
				}
			}
		} else {
			downloadNum = 1;
			PromptManager.showDownloadCancelProgressDialog(VideoInfoActivity.this);
			getDownloadUrl(sourceList.get(currSourceNum).getSource_url(), 0);
		}
	}

	private void getDownloadUrl(final String url, final int episode_id) {
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
				String useragent = "";
				if (realUrl != null) {
					downloadUrl = realUrl.getUrl();
					if (realUrl.getFormat().equals("m3u8")) {
						isM3U8 = true;
					}
					useragent = realUrl.getUseragent();
				}
				if (!StringUtils.isEmpty(downloadUrl)) {
					// downloadUrl =
					// "http://vapi.saaser.cn/v1/youtu/sm3u8/id/MDAwMDAwMDAwMJKruJKUptWsfZi82ZmNbGSaZ7rZumhtrYFsjJjEZZvXldyf0ouon4-Fu7W2gaFkeI6Iq7O3fZdpiWl3a7GGaNCVqpfVgZTAr5OXk52XsHioiHeV3q2Kga6Cim9v_3.m3u8";
					m3u8Utils.initDownload(downloadUrl, episode_id, isM3U8, useragent);

				}
				return downloadUrl;
			}
		}.execute("");
	}

	private void getCommData(final boolean isRefresh) {
		if (isRefresh) {
			commPage = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				scrollView.onRefreshComplete();
				if (getCommentList != null && getCommentList.size() > 0) {
					if (isRefresh) {
						commList.clear();
					}
					commList.addAll(getCommentList);
					commAdapter.notifyDataSetChanged();
					commPage++;
				} else {

					if (isFirst) {
						isFirst = false;
					} else {
						// if (getCommList == null) {
						// Toast.makeText(getApplicationContext(), "未获取到数据",
						// 1).show();
						// }else{
						// Toast.makeText(getApplicationContext(), "已加载全部",
						// 1).show();
						// }
						showShortToast("已经加载完毕");

					}

				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				// getCommList = movieListApi.getTypeList(special.getTheme_id(),
				// commPage + "", commCount + "");
				CommentApi commentApi = new CommentApi();
				getCommentList = commentApi.getCommentList(movie_id, type_id, commPage + "", commCount + "");
				return null;
			}
		}.execute("");
	}

	private Button sendButton, sendcancelButton;
	private EditText commentEditText;
	private JSONObject commentJsonObject;
	private LinearLayout commentLayout;
	private InputMethodManager imm;
	private boolean isComment = true;

	private void initComment() {
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		commentLayout = (LinearLayout) findViewById(R.id.comment_layout);
		commentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commentLayout.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});

		commentEditText = (EditText) findViewById(R.id.comment_editText);
		commentEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(commentEditText.getText().toString())) {
					sendButton.setEnabled(false);
				} else {
					sendButton.setEnabled(true);
				}
			}
		});
		sendButton = (Button) findViewById(R.id.send_button);


		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!StringUtils.isEmpty(commentEditText.getText().toString())) {
					platform = android.os.Build.MODEL;
					Comment comment = new Comment();
					comment.setContent(commentEditText.getText().toString());
					comment.setUser_info(GlobalParams.user);
					comment.setDigg(0);
					comment.setCtime(System.currentTimeMillis() / 1000);
					comment.setPlatform(platform);
					comment.setLocaltion("");
					if (!isComment) {
						User user = new User();
						user.setNick_name(commentJsonObject.getString("from_nickname"));
						comment.setF_user_info(user);
						comment.setF_comment_info(commentJsonObject.getString("from_comment"));
					}
					commList.add(0, comment);
					commAdapter.notifyDataSetChanged();
					try {
						// 评论数量
						int num = Integer.parseInt(movieInfo.getComments_count());
						num++;
						String commNum = "评论(<font color='#44b549'>" + num + "</font>)";
						commTextView.setText(Html.fromHtml(commNum));

					} catch (Exception e) {
						// TODO: handle exception
					}

					if (isComment) {
						commentMovie(commentEditText.getText().toString(), "", platform);
					} else {
						commentMovie(commentEditText.getText().toString(), commentJsonObject.getString("from_id"),
								platform);
					}
				}
				commentLayout.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});


		sendcancelButton = (Button) findViewById(R.id.sendcancel_button);
		sendcancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commentLayout.setVisibility(View.GONE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
	}

	private void showComment() {
		isComment = true;
		commentJsonObject = new JSONObject();
		commentJsonObject.put("user_id", GlobalParams.user.getUid());
		commentEditText.setText("");
		sendButton.setEnabled(false);
		commentLayout.setVisibility(View.VISIBLE);
		commentEditText.requestFocus();
		imm.showSoftInput(commentEditText, 0);
	}

	private LinearLayout dotLayout, blackLayout;
	private Button reportButton, replyButton, cancelButton;

	private void initDot() {
		blackLayout = (LinearLayout) findViewById(R.id.black_layout);
		dotLayout = (LinearLayout) findViewById(R.id.dot_layout);
		reportButton = (Button) findViewById(R.id.report_button);
		replyButton = (Button) findViewById(R.id.reply_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);
		dotLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideDot();
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideDot();
			}
		});
	}

	public void showDot(final int position) {

		Log.e("showDot", "VishowDot");

		reportButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reportTheme(commList.get(position).getId());
				hideDot();
			}
		});

		replyButton.setText("回复" + commList.get(position).getUser_info().getNick_name());
		replyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reply(position);
				hideDot();
			}
		});
		int height = dotLayout.getMeasuredHeight();
		dotLayout.setVisibility(View.VISIBLE);
		blackLayout.setVisibility(View.VISIBLE);
		dotLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, height, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);
		dotLayout.startAnimation(bottomtranslateAnimation);
	}

	private void hideDot() {
		if (dotLayout.getVisibility() == View.GONE) {
			// isDownloadShow = false;
			return;
		}
		int height = dotLayout.getMeasuredHeight();
		dotLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 0, height);
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
				dotLayout.clearAnimation();
				dotLayout.setVisibility(View.GONE);
				blackLayout.setVisibility(View.GONE);
			}
		});

		dotLayout.startAnimation(bottomtranslateAnimation);
	}

	private void reply(int position) {
		if (GlobalParams.hasLogin) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("user_id", GlobalParams.user.getUid());
			jsonObject.put("from_id", commList.get(position).getId());
			jsonObject.put("from_nickname", commList.get(position).getUser_info().getNick_name());
			jsonObject.put("from_comment", commList.get(position).getContent());
			showReply(jsonObject);
		} else {
			Toast.makeText(getApplicationContext(), "请先登录！", 1).show();
		}
	}

	private void showReply(JSONObject jsonObject) {
		isComment = false;
		commentJsonObject = jsonObject;

		commentLayout.setVisibility(View.VISIBLE);
		commentEditText.setText("");
		sendButton.setEnabled(false);
		commentEditText.requestFocus();
		imm.showSoftInput(commentEditText, 0);
		commentEditText.setHint("回复" + jsonObject.getString("from_nickname"));
	}

	private void commentMovie(final String comment, final String from_id, final String platform) {
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
				CommentApi commentApi = new CommentApi();
				commentApi.comment(comment, GlobalParams.user.getUid(), movie_id, type_id, from_id,
						GlobalParams.user.getToken(), platform);
				return null;
			}
		}.execute("");
	}

	public void reportTheme(final String comment_id) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "举报成功", 1).show();
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				CommentApi commentApi = new CommentApi();
				commentApi.report(comment_id, GlobalParams.user.getUid());
				return null;
			}
		}.execute("");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		currSourceNum = 0;
		/**
		 * 结束后台事件处理线程
		 */

		// HeadSetUtil.getInstance().close(getApplicationContext());

		TCAgent.onPageEnd(getApplicationContext(), "VideoInfoActivity");
	}
}
