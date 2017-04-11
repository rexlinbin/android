package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.TypeInfoCommentAdapter;
import com.bccv.zhuiyingzhihanju.adapter.TypeInfoListAdapter;
import com.bccv.zhuiyingzhihanju.api.CommentApi;
import com.bccv.zhuiyingzhihanju.api.MovieListApi;
import com.bccv.zhuiyingzhihanju.model.Comment;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.Special;
import com.bccv.zhuiyingzhihanju.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tendcloud.tenddata.TCAgent;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.pulltorefresh.PullToRefreshListView;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.ImageUtils;
import com.utils.tools.PromptManager;
import com.utils.tools.StringUtils;
import com.utils.views.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TypeInfoActivity extends BaseActivity {

	private Special special;

	private void tcStart() {
		TCAgent.onPageStart(getApplicationContext(), "TypeInfoActivity");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "TypeInfoActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_typeinfo);
		tcStart();
		special = (Special) getIntent().getSerializableExtra("Special");
		if (special == null) {
			special = new Special();
		}
		setTitle();
		initView();
		getData(true);
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

		// TextView titleName = (TextView)
		// findViewById(R.id.titleName_textView);
		// titleName.setText(special.getTitle());
	}

	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private List<Movie> list, getList;
	private TypeInfoListAdapter adapter;
	private RelativeLayout layoutR;
	private View headView;
	TextView commentTextView;
	private String platform;
	private void initView() {
		initHead();
		initList();
		initCommentList();
	}

	private void initHead() {
		layoutR = (RelativeLayout) findViewById(R.id.typeinfo_layout);

		headView = View.inflate(getApplicationContext(), R.layout.view_typeinfo, null);
		ImageView headImageView = (ImageView) headView.findViewById(R.id.type_imageView);
		TextView titleTextView = (TextView) headView.findViewById(R.id.title_textView);
		commentTextView = (TextView) headView.findViewById(R.id.comment_textView);
		TextView userTextView = (TextView) headView.findViewById(R.id.user_textView);
		TextView numTextView = (TextView) headView.findViewById(R.id.num_textView);
		TextView hitTextView = (TextView) headView.findViewById(R.id.hit_textView);
		TextView infoTextView = (TextView) headView.findViewById(R.id.info_textView);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(special.getImages(), headImageView, GlobalParams.foundOptions,
				new ImageLoadingListener() {

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
						Bitmap newImage = ImageUtils.doBlur(loadedImage, 80, false);
						setImage(newImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub

					}
				});

		titleTextView.setText(special.getTitle());
		infoTextView.setText(special.getIntro());
		numTextView.setText("共" + special.getNum() + "部");
		hitTextView.setText("播放" + special.getHit() + "次");
		userTextView.setText(special.getUser_name());
		String commNum = "评论(<font color='#44b549'>" + special.getComment_num() + "</font>)";
		commentTextView.setText(Html.fromHtml(commNum));
		commentTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isCommentShow) {
					hideCommentList();
				} else {
					showCommentList();
				}
			}
		});
	}

	private void initList() {
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.typeinfo_pullToRefreshListView);
		listView = pullToRefreshListView.getRefreshableView();
		listView.addHeaderView(headView);
		listView.setVerticalScrollBarEnabled(false);
		listView.setDividerHeight(0);
		list = new ArrayList<Movie>();
		adapter = new TypeInfoListAdapter(getApplicationContext(), this, list);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(android.R.color.transparent));
		pullToRefreshListView.setPullLoadEnabled(true);
		pullToRefreshListView.setPullRefreshEnabled(false);
		pullToRefreshListView.getRefreshableView().setSelector(new ColorDrawable(android.R.color.transparent));
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");

					pullToRefreshListView.onPullDownRefreshComplete();
				} else {
					page = 1;
					getData(true);

				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) pullToRefreshListView.getFooterLoadingLayout()).getmHintView()
							.setText("数据加载中...");
					getData(false);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					pullToRefreshListView.onPullUpRefreshComplete();
				}
			}
		});
	}

	public void goInfo(int position) {
		if (isCommentShow) {
			return;
		}
		Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
		intent.putExtra("movie_id", list.get(position).getId());
		intent.putExtra("type_id", list.get(position).getType_id());
		intent.putExtra("episodes_id", "0");
		startActivity(intent);
	}

	private int page = 1, count = 21;

	private void getData(final boolean isRefresh) {
		if (isRefresh) {
			page = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null && getList.size() > 0) {
					if (isRefresh) {
						list.clear();
					}
					list.addAll(getList);
					adapter.notifyDataSetChanged();
					page++;
				} else {
//					if (getList == null) {
//						Toast.makeText(getApplicationContext(), "未获取到数据", 1).show();
//					}else{
//						Toast.makeText(getApplicationContext(), "已加载全部", 1).show();
//					}
					Toast.makeText(getApplicationContext(), "已加载全部", 1).show();
				}
				if (isRefresh) {
					pullToRefreshListView.onPullDownRefreshComplete();
				} else {
					pullToRefreshListView.onPullUpRefreshComplete();
				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieListApi movieListApi = new MovieListApi();
				getList = movieListApi.getTypeList(special.getTheme_id(), page + "", count + "");
				return null;
			}
		}.execute("");
	}

	public void setImage(Bitmap bitmap) {
		layoutR.setBackgroundDrawable(ImageUtils.bitmapToDrawable(bitmap));
	}

	// 评论
	private PullToRefreshListView commPullToRefreshListView;
	private ListView commListView;
	private List<Comment> commList, getCommList;
	private TypeInfoCommentAdapter commAdapter;
	private int commPage = 1, commCount = 10;
	private boolean isCommentShow = false, isFirst = true;
	private LinearLayout commLayout;
	private TextView textView, commTextView, commCloseTextView;
	private CircleImageView circleImageView;

	private void initCommentList() {
		commLayout = (LinearLayout) findViewById(R.id.comm_Layout);
		commPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.comm_pullToRefreshListView);
		commListView = commPullToRefreshListView.getRefreshableView();
		commListView.setVerticalScrollBarEnabled(false);
		commListView.setDividerHeight(0);
		commList = new ArrayList<Comment>();
		commAdapter = new TypeInfoCommentAdapter(getApplicationContext(), this, commList);
		commListView.setAdapter(commAdapter);
		commListView.setSelector(new ColorDrawable(android.R.color.transparent));
		commPullToRefreshListView.setPullLoadEnabled(true);
		commPullToRefreshListView.setPullRefreshEnabled(false);
		commPullToRefreshListView.getRefreshableView().setSelector(new ColorDrawable(android.R.color.transparent));
		commPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
					// 提示网络不给力,直接完成刷新
					PromptManager.showToast(GlobalParams.context, "网络不给力");

					commPullToRefreshListView.onPullDownRefreshComplete();
				} else {
					commPage = 1;
					getCommData(true);

				}
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
					((FooterLoadingLayout) commPullToRefreshListView.getFooterLoadingLayout()).getmHintView()
							.setText("数据加载中...");
					getCommData(false);
				} else {
					PromptManager.showToast(GlobalParams.context, "网络不给力");
					commPullToRefreshListView.onPullUpRefreshComplete();
				}
			}
		});
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
		
		commTextView = (TextView) findViewById(R.id.comm_textView);
		commCloseTextView = (TextView) findViewById(R.id.closeComm_textView);
		circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
		String commNum = "评论(<font color='#44b549'>" + special.getComment_num() + "</font>)";
		commTextView.setText(Html.fromHtml(commNum));
		
		commCloseTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideCommentList();
			}
		});
		
		if (GlobalParams.hasLogin) {
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(GlobalParams.user.getAvatars(), circleImageView, GlobalParams.iconOptions);
		}
		
		initComment();
		initDot();
		getCommData(true);
	}

	private void getCommData(final boolean isRefresh) {
		if (isRefresh) {
			commPage = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getCommList != null && getCommList.size() > 0) {
					if (isRefresh) {
						commList.clear();
					}
					commList.addAll(getCommList);
					commAdapter.notifyDataSetChanged();
					commPage++;
				} else {
					if (isFirst) {
						isFirst = false;
					}else{
//						if (getCommList == null) {
//							Toast.makeText(getApplicationContext(), "未获取到数据", 1).show();
//						}else{
//							Toast.makeText(getApplicationContext(), "已加载全部", 1).show();
//						}
						Toast.makeText(getApplicationContext(), "已加载全部", 1).show();
					}
					
					
				}
				if (isRefresh) {
					commPullToRefreshListView.onPullDownRefreshComplete();
				} else {
					commPullToRefreshListView.onPullUpRefreshComplete();
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
				getCommList = commentApi.getThemeCommentList(special.getTheme_id(), commPage + "", commCount + "");
				return null;
			}
		}.execute("");
	}

	private void showCommentList() {
		isCommentShow = true;
		listView.setEnabled(false);
		int height = commLayout.getMeasuredHeight();
		commLayout.setVisibility(View.VISIBLE);
		commLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, height, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);
		commLayout.startAnimation(bottomtranslateAnimation);
	}

	private void hideCommentList() {
		if (commLayout.getVisibility() == View.GONE) {
			// isDownloadShow = false;
			return;
		}
		int height = commLayout.getMeasuredHeight();
		commLayout.clearAnimation();
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
				listView.setEnabled(true);
				commLayout.clearAnimation();
				commLayout.setVisibility(View.GONE);
				isCommentShow = false;
			}
		});

		commLayout.startAnimation(bottomtranslateAnimation);
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
				}else{
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
					Comment comment = new Comment();
					comment.setContent(commentEditText.getText().toString());
					comment.setUser_info(GlobalParams.user);
					comment.setDigg(0);
					comment.setCtime(System.currentTimeMillis() / 1000);
					if (!isComment) {
						User user = new User();
						user.setNick_name(commentJsonObject.getString("from_nickname"));
						comment.setF_user_info(user);
						comment.setF_comment_info(commentJsonObject.getString("from_comment"));
					}
					commList.add(0, comment);
					commAdapter.notifyDataSetChanged();
					try {
						int num = Integer.parseInt(special.getComment_num());
						num++;
						String commNum = "评论(<font color='#44b549'>" + num + "</font>)";
						commTextView.setText(Html.fromHtml(commNum));
						commentTextView.setText(Html.fromHtml(commNum));
					} catch (Exception e) {
						// TODO: handle exception
					}
//					  textView.setText("Product Model: " + android.os.Build.MODEL + ","
//				                + android.os.Build.VERSION.SDK + ","
//				                + android.os.Build.VERSION.RELEASE);
					
					
					Log.e("手机型号", "Product Model: " + android.os.Build.MODEL );
					
					platform=android.os.Build.MODEL;
					if (isComment) {
						commentMovie(commentEditText.getText().toString(), "",platform);
					} else {
						commentMovie(commentEditText.getText().toString(), commentJsonObject.getString("from_id"),platform);
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
		Log.e("showDot", "showDot");
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

	private void commentMovie(final String comment, final String from_id,final String platform) {
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
				commentApi.commentTheme(comment, GlobalParams.user.getUid(), special.getTheme_id(), from_id, GlobalParams.user.getToken(),platform);
				return null;
			}
		}.execute("");
	}

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
}
