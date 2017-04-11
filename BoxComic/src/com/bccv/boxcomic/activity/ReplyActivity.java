package com.bccv.boxcomic.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.bccv.boxcomic.tool.CircleImageView;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.PromptManager;
import com.bccv.boxcomic.tool.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ReplyActivity extends BaseActivity {
private PullToRefreshListView pullToRefreshListView;
	
	private boolean sendLayoutIsShowing = true;
	private LinearLayout sendLayout;
	private int lastVisibleItem = 0;
	
	private CommentAdapter adapter;
	private List<Comment> list;
	private List<Comment> getComments;
	
	private Activity activity;
	private Comment comment;
	
	private int pageNum = 1;
	
	private boolean isUser = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply);
	
		isUser = getIntent().getExtras().getBoolean("isUser");
		setBack();
		
		setComment();
		
		sendLayout = (LinearLayout) findViewById(R.id.send_layout);
		if (isUser) {
			sendLayout.setVisibility(View.GONE);
		}
		setSend();
		
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.reply_pullToRefreshListView);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

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
					((FooterLoadingLayout)pullToRefreshListView.getFooterLoadingLayout()).getmHintView()
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
		
		pullToRefreshListView.getRefreshableView().setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (!isUser) {
					if (firstVisibleItem > lastVisibleItem) {
						hideTypeLayout();
					}else if (firstVisibleItem < lastVisibleItem) {
						showTypeLayout();
					}
					
					lastVisibleItem = firstVisibleItem;
				}
				
			}
		});
		fetchServiceData(false, false);
	}
	
	private void setComment(){
		comment = (Comment) getIntent().getSerializableExtra("comment");
		if (comment != null) {
			
			TextView titleTextView = (TextView) findViewById(R.id.replyTitle_textView);
			titleTextView.setText(comment.getUser_name());
			
			TextView nameTextView = (TextView) findViewById(R.id.name_textView);
			nameTextView.setText(comment.getUser_name());
			TextView timeTextView = (TextView) findViewById(R.id.time_textView);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String timeString = simpleDateFormat.format(new Date(comment.getComment_date() * 1000L));
			timeTextView.setText(timeString);
			TextView replyTextView = (TextView) findViewById(R.id.reply_textView);
			replyTextView.setText(comment.getReport_num());
			TextView contentTextView = (TextView) findViewById(R.id.comment_textView);
			contentTextView.setText(comment.getComment_content());
			
			ImageLoader imageLoader = ImageLoader.getInstance();
			CircleImageView headCircleImageView = (CircleImageView) findViewById(R.id.head_circleImageView);
			imageLoader.displayImage(comment.getUser_icon(), headCircleImageView, GlobalParams.headOptions);
		}
		
	}
	
	private void setSend(){
		final EditText editText = (EditText) findViewById(R.id.comment_editText);
		ImageView sendImageView = (ImageView) findViewById(R.id.send_imageView);
		sendImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(editText.getText().toString())) {
					Toast.makeText(getApplicationContext(), "回复不能为空", 1).show();
				}else {
					if (!UserInfoManager.isLogin()) {
						Intent intent = new Intent(activity, LoginActivity.class);
						startActivity(intent);
						return;
					}
					Comment reply = new Comment();
					reply.setComment_id(comment.getComment_id());
					reply.setComment_content(editText.getText().toString());
					reply.setUser_id(UserInfoManager.getUserId() + "");
					reply.setUser_icon(UserInfoManager.getUserIcon());
					reply.setUser_name(UserInfoManager.getUserName());
					reply.setComment_date(System.currentTimeMillis() / 1000L);
					sendComment(reply);
					list.add(0, reply);
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
	
	private void sendComment(final Comment comment){
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("false")) {
					Toast.makeText(getApplicationContext(), "回复失败", 1).show();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				CommentApi commentApi = new CommentApi();
				boolean request = commentApi.sendComment(comment.getComment_id(), comment.getComment_content(), comment.getUser_id(), "2");
				if (request) {
					return "true";
				}else {
					return "false";
				}
			}
		}.executeProxy("");
	}
	
	private void showTypeLayout() {
		if (!sendLayoutIsShowing) {
			sendLayoutIsShowing = true;
			sendLayout.setVisibility(View.VISIBLE);
			sendLayout.clearAnimation();
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, 80, 0);
			translateAnimation.setDuration(300);
			translateAnimation.setFillAfter(true);
			
			sendLayout.startAnimation(translateAnimation);
		}
	}

	private void hideTypeLayout() {
		if (sendLayoutIsShowing) {
			sendLayoutIsShowing = false;
			sendLayout.clearAnimation();
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, 0, 80);
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
		boolean isNetworkAvailable = NetUtil.isNetworkAvailable(GlobalParams.context);
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
				}else {
					pageNum = 1;
					setData();
					return "refresh";
				}
			}
		}.executeProxy("");

	}
	
	private void setData(){
		CommentApi commentApi = new CommentApi();
		getComments = commentApi.getReplyList(comment.getComment_id(), pageNum + "", "10");
	}
	
	private void setBack(){
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
