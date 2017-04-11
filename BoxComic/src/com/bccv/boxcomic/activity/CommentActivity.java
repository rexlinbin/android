package com.bccv.boxcomic.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.CommentAdapter;
import com.bccv.boxcomic.api.CommentApi;
import com.bccv.boxcomic.modal.Comment;
import com.bccv.boxcomic.net.NetUtil;
import com.bccv.boxcomic.pulltorefresh.FooterLoadingLayout;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshListView;
import com.bccv.boxcomic.sns.UserInfoManager;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.DimensionPixelUtil;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.PromptManager;
import com.bccv.boxcomic.tool.StringUtils;

public class CommentActivity extends BaseActivity {
	private String comicIdString;
	private String useridString;

	private boolean isUser;

	private PullToRefreshListView pullToRefreshListView;

	private boolean sendLayoutIsShowing = true;
	private LinearLayout sendLayout;
	private int lastVisibleItem = 0;

	private CommentAdapter adapter;
	private List<Comment> list;
	private List<Comment> getComments;

	private int pageNum = 1;

	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		activity = this;
		setContentView(R.layout.activity_comment);
	
		isUser = getIntent().getExtras().getBoolean("isUser");
		if (isUser) {
			useridString = getIntent().getExtras().getString("user_id");
		} else {
			comicIdString = getIntent().getExtras().getString("comic_id");
		}

		setBack();

		sendLayout = (LinearLayout) findViewById(R.id.send_layout);
		if (isUser) {
			sendLayout.setVisibility(View.GONE);
		}
		setSend();

		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.comment_pullToRefreshListView);
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
							// 提示网络不给力,直接完成刷新
							PromptManager.showToast(GlobalParams.context,
									"网络不给力");
							pullToRefreshListView.onPullDownRefreshComplete();
						} else {
							fetchServiceData(false, false);
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
							((FooterLoadingLayout) pullToRefreshListView
									.getFooterLoadingLayout()).getmHintView()
									.setText("数据加载中...");

							fetchServiceData(true, false);
						} else {
							PromptManager.showToast(GlobalParams.context,
									"网络不给力");
							pullToRefreshListView.onPullUpRefreshComplete();
						}
					}
				});

		list = new ArrayList<Comment>();
		adapter = new CommentAdapter(getApplicationContext(), list);
		pullToRefreshListView.getRefreshableView().setAdapter(adapter);

		pullToRefreshListView.getRefreshableView().setOnScrollListener(
				new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
						// TODO Auto-generated method stub
						if (!isUser) {
							if (firstVisibleItem > lastVisibleItem) {
								hideTypeLayout();
							} else if (firstVisibleItem < lastVisibleItem) {
								showTypeLayout();
							}

							lastVisibleItem = firstVisibleItem;
						}
						
					}
				});

		pullToRefreshListView.getRefreshableView().setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(activity,
								ReplyActivity.class);
						intent.putExtra("comment", list.get(position));
						intent.putExtra("isUser", isUser);
						startActivity(intent);
					}
				});

		fetchServiceData(false, false);
	}

	private void setSend() {
		final EditText editText = (EditText) findViewById(R.id.comment_editText);
		ImageView sendImageView = (ImageView) findViewById(R.id.send_imageView);
		sendImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(editText.getText().toString())) {
					Toast.makeText(getApplicationContext(), "评论不能为空", 1).show();
				} else {
					if (!UserInfoManager.isLogin()) {
						Intent aIntent=new Intent(activity,LoginActivity.class);
						aIntent.putExtra("type", "Comment");
						aIntent.putExtra("isUser", isUser);
						startActivity(aIntent);
						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						return;
					}
					Comment comment = new Comment();
					comment.setComment_id(comicIdString);
					comment.setComment_content(editText.getText().toString());
					comment.setUser_id(UserInfoManager.getUserId() + "");
					comment.setUser_icon(UserInfoManager.getUserIcon());
					comment.setUser_name(UserInfoManager.getUserName());
					comment.setComment_date(System.currentTimeMillis() / 1000L);
					sendComment(comment);
					list.add(0, comment);
					adapter.notifyDataSetChanged();
					editText.setText("");
					// 隐藏输入法
					InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					// 显示或者隐藏输入法
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});
	}

	private void sendComment(final Comment comment) {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("false")) {
					Toast.makeText(getApplicationContext(), "评论失败", 1).show();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				CommentApi commentApi = new CommentApi();
				boolean request = commentApi.sendComment(
						comment.getComment_id(), comment.getComment_content(),
						comment.getUser_id(), "1");
				if (request) {
					return "true";
				} else {
					return "false";
				}
			}
		}.executeProxy("");
	}

	public void showTypeLayout() {
		if (!sendLayoutIsShowing) {
			sendLayoutIsShowing = true;
			sendLayout.setVisibility(View.VISIBLE);
			sendLayout.clearAnimation();
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, DimensionPixelUtil.dip2px(getApplicationContext(), 45),
					0);
			translateAnimation.setDuration(300);
			translateAnimation.setFillAfter(true);

			sendLayout.startAnimation(translateAnimation);
		}
	}

	public void hideTypeLayout() {
		if (sendLayoutIsShowing) {
			sendLayoutIsShowing = false;
			sendLayout.clearAnimation();
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, 0,
					DimensionPixelUtil.dip2px(getApplicationContext(), 45));
			translateAnimation.setDuration(300);
			translateAnimation.setFillAfter(true);
			translateAnimation.setAnimationListener(new AnimationListener() {

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
					sendLayout.clearAnimation();
					sendLayout.setVisibility(View.GONE);
				}
			});
			sendLayout.startAnimation(translateAnimation);
		}
	}

	private void fetchServiceData(final boolean isLoadMore, boolean canProgress) {
		boolean isNetworkAvailable = NetUtil
				.isNetworkAvailable(GlobalParams.context);
		if (!isNetworkAvailable) {
			PromptManager.showToast(activity, "网络不给力");
			return;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("refresh")) {
					list.clear();
					if (getComments != null) {
						list.addAll(getComments);
					}

					pullToRefreshListView.onPullDownRefreshComplete();
				} else if (result.equals("more")) {
					if (getComments != null) {
						list.addAll(getComments);
					}
					pullToRefreshListView.onPullUpRefreshComplete();

				}

				adapter.notifyDataSetChanged();
			}
		};

		new DataAsyncTask(callback, canProgress) {

			@Override
			protected String doInBackground(String... params) {
				if (isLoadMore) {
					setData();
					return "more";
				} else {
					pageNum = 1;
					setData();
					return "refresh";
				}
			}
		}.executeProxy("");

	}

	private void setData() {
		CommentApi commentApi = new CommentApi();
		if (isUser) {
			getComments = commentApi.getMyCommentList(useridString, pageNum
					+ "", "10");
		} else {
			getComments = commentApi.getCommentList(comicIdString,
					pageNum + "", "10");
		}

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
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}

}
