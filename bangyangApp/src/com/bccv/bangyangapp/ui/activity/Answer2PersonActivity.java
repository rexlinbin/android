package com.bccv.bangyangapp.ui.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.model.AnswerListResBean;
import com.bccv.bangyangapp.model.QuestionInfoBean;
import com.bccv.bangyangapp.model.ReplyBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.ui.view.XListView;
import com.bccv.bangyangapp.ui.view.XListView.IXListViewListener;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;
import com.bccv.bangyangapp.utils.L;
import com.bccv.bangyangapp.utils.StringUtil;
import com.bccv.bangyangapp.utils.SystemUtil;

public class Answer2PersonActivity extends BaseActivity implements
		IXListViewListener {

	public static final int REFRESH_CANCLE_LOADDING = 1;

	public static final String QUESTION_INFO_KEY = "question_info_key";

	private BackGroundView background_view;
	private View errorView, loadingView;
	private View common_title_back_btn, common_title_right_btn;
	private TextView common_title_tv;
	private XListView xListView;
	private EditText reply_edit_et;
	private View import_rl, send_btn;
	private ImageView user_icon;
	private TextView question_tv;

	private int[] background;

	private Handler mHandler;

	private ArrayList<ReplyBean> commentList = new ArrayList<ReplyBean>();

	private CommentAdapter mCommentAdapter;

	private QuestionInfoBean question;

	private int currentPageId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_2_person);

		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		question = intent.getParcelableExtra(QUESTION_INFO_KEY);
		initHandler();
		initView();
		initData();
	}

	private void initView() {
		import_rl = findViewById(R.id.import_rl);
		background_view = (BackGroundView) findViewById(R.id.background_view);
		common_title_back_btn = findViewById(R.id.common_title_back_btn);
		common_title_right_btn = findViewById(R.id.common_title_right_btn);
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		user_icon = (ImageView) findViewById(R.id.user_icon);
		question_tv = (TextView) findViewById(R.id.question_tv);
		xListView = (XListView) findViewById(R.id.xlistview_include);
		send_btn = findViewById(R.id.send_btn);
		loadingView = findViewById(R.id.reply_2_person_loading);
		errorView = findViewById(R.id.reply_2_person_error);
		reply_edit_et = (EditText) findViewById(R.id.reply_edit_et);

		common_title_tv.setText(question.getUser_name());

		xListView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setEnableDragLoadMore(false);
		xListView.setXListViewListener(this);

		background_view.setGradient(background[0], background[1]);

		// common_title_tv.setText(question.getUser_name());

		ImageLoaderUtil.getInstance(mContext).displayImage(
				question.getUser_icon(), user_icon,
				ImageLoaderUtil.getUserIconImageOptions());

		question_tv.setText(question.getContent());

		user_icon.setOnClickListener(this);
		common_title_back_btn.setOnClickListener(this);
		common_title_right_btn.setOnClickListener(this);
		send_btn.setOnClickListener(this);

		mCommentAdapter = new CommentAdapter();
		xListView.setAdapter(mCommentAdapter);

		// xListView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	private void initData() {
		requestData(1);
		// mHandler.sendEmptyMessage(REFRESH_CANCLE_LOADDING);
	}

	private void requestData(int p) {

		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
			if (commentList.size() == 0) {
				loadingView.setVisibility(View.VISIBLE);
			}
			NetWorkAPI.app_answer(mContext, p, question.getId(),
					new HttpCallback() {

						@Override
						public void onResult(NetResBean response) {
							// TODO Auto-generated method stub
							L.i(TAG, "onResult", response.toString());
							if (response instanceof AnswerListResBean) {
								AnswerListResBean data = (AnswerListResBean) response;
								int now_p = data.getNow_p();
								int total_p = data.getTotal_p();
								ArrayList<ReplyBean> answerList = data
										.getAnswerList();
								if (answerList != null && answerList.size() > 0) {
									if (now_p == 1) {
										commentList.clear();
									}
									commentList.addAll(answerList);
									currentPageId = now_p;
									mCommentAdapter.notifyDataSetChanged();
								}
								if (currentPageId >= total_p) {
									xListView.setPullLoadEnable(false);
									xListView.setEnableDragLoadMore(false);
								} else {
									xListView.setPullLoadEnable(true);
									xListView.setEnableDragLoadMore(true);
								}
								loadingView.setVisibility(View.GONE);
							} else {
								showShortToast("数据请求失败");
								loadingView.setVisibility(View.GONE);
							}
							xListView.stopRefresh();
							xListView.stopLoadMore();

						}

						@Override
						public void onError(String errorMsg) {
							// TODO Auto-generated method stub
							showShortToast("数据请求失败");
							xListView.stopRefresh();
							xListView.stopLoadMore();
							loadingView.setVisibility(View.GONE);

						}

						@Override
						public void onCancel() {
						}
					});
		}

	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case REFRESH_CANCLE_LOADDING:
					mCommentAdapter.notifyDataSetChanged();
					loadingView.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}

		};
	}

	/**
	 * 评论
	 */
	private void doComment() {

		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {

			String answer = reply_edit_et.getText().toString().trim();
			if (TextUtils.isEmpty(answer)) {
				showShortToast("请输入内容");
				return;
			}

			hideSoftInput();
			if(UserInfoManager.isLogin()){
				
				NetWorkAPI.answer(this.getApplicationContext(), answer,
						question.getId(), new HttpCallback() {
					
					@Override
					public void onResult(NetResBean response) {
						// TODO Auto-generated method stub
						if (response.success) {
							reply_edit_et.setText("");
							showShortToast("回复成功");
							requestData(1);
						} else {
							showShortToast("回复失败");
						}
					}
					
					@Override
					public void onError(String errorMsg) {
						// TODO Auto-generated method stub
						showShortToast("回复失败");
					}
					
					@Override
					public void onCancel() {
					}
				});
				
			}else{
				Intent loginIntent = new Intent(Answer2PersonActivity.this, LoginActivity.class);
				loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
				startActivity(loginIntent);
			}
		}
	}

	private void hideSoftInput() {
		/* 隐藏软键盘 */
		InputMethodManager imm = (InputMethodManager) reply_edit_et
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(
					reply_edit_et.getApplicationWindowToken(), 0);
		}
	}

	private void reportZan() {

		StringBuilder stringBuilder = new StringBuilder();
		boolean hasZan = false;
		for (int i = 0; i < commentList.size(); i++) {
			ReplyBean replyBean = commentList.get(i);
			if (replyBean.isZan()) {
				hasZan = true;
				stringBuilder.append(replyBean.getId());
				stringBuilder.append(",");
			}
		}
		if (hasZan && stringBuilder.length() >= 2) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			NetWorkAPI.digg_answer(mContext, stringBuilder.toString(),
					new HttpCallback() {

						@Override
						public void onResult(NetResBean response) {
							L.i(TAG, "onResult", response.toString());
						}

						@Override
						public void onError(String errorMsg) {
							L.e(TAG, "onError", errorMsg);
						}

						@Override
						public void onCancel() {
						}
					});
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.user_icon:
			// TODO Auto-generated method stub
			Intent personalZoneIntent = new Intent(this,
					PersonalZoneActivity.class);
			personalZoneIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					background_view.getGradientColor());
			startActivity(personalZoneIntent);
			break;

		case R.id.common_title_back_btn:
			finish();
			break;

		case R.id.common_title_right_btn:
			Intent appReleaseIntent = new Intent(this, AppReleaseActivity.class);
			appReleaseIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					background_view.getGradientColor());
			startActivity(appReleaseIntent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;

		case R.id.send_btn:
			doComment();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		reportZan();
		super.onDestroy();
	}

	private class CommentAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return commentList == null ? 0 : commentList.size();
		}

		@Override
		public Object getItem(int position) {
			return commentList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			final CommentHolder holder;
			if (convertView == null) {
				holder = new CommentHolder();
				convertView = View.inflate(mContext,
						R.layout.comment_item_view, null);
				holder.comment_item_icon = (ImageView) convertView
						.findViewById(R.id.comment_item_icon);
				holder.comment_item_name = (TextView) convertView
						.findViewById(R.id.comment_item_name);
				holder.comment_item_time = (TextView) convertView
						.findViewById(R.id.comment_item_time);
				holder.comment_item_zan_num = (TextView) convertView
						.findViewById(R.id.comment_item_zan_num);
				holder.comment_item_zan = convertView
						.findViewById(R.id.comment_item_zan);
				holder.comment_item_content = (TextView) convertView
						.findViewById(R.id.comment_item_content);

			} else {
				holder = (CommentHolder) convertView.getTag();
			}

			// TODO 设置数据
			final ReplyBean replyBean = commentList.get(position);

			ImageLoaderUtil.getInstance(mContext).displayImage(
					replyBean.getUser_icon(), holder.comment_item_icon,
					ImageLoaderUtil.getUserIconImageOptions());

			if (replyBean.isZan()) {
				holder.comment_item_zan
						.setBackgroundResource(R.drawable.zan_press);
			} else {
				holder.comment_item_zan.setBackgroundResource(R.drawable.zan);
			}
			holder.comment_item_name.setText(replyBean.getUser_name());
			holder.comment_item_time.setText(StringUtil.formatLongDate(
					replyBean.getTimes(), "MM/dd HH:mm"));
			holder.comment_item_content.setText(replyBean.getComment());
			holder.comment_item_zan_num.setText(replyBean.getDigg() + "");

			OnClickListener onClickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.comment_item_icon:
						Intent personalIntent = new Intent(
								Answer2PersonActivity.this,
								PersonalZoneActivity.class);
						personalIntent.putExtra(
								BackGroundView.BACKGROUND_COLOR,
								background_view.getGradientColor());
						startActivity(personalIntent);
						break;
					case R.id.comment_item_zan:
						if (!replyBean.isZan()) {
							replyBean.setZan(true);
							holder.comment_item_zan
									.setBackgroundResource(R.drawable.zan_press);
							replyBean.setDigg(replyBean.getDigg() + 1);
						} else {
							replyBean.setZan(false);
							holder.comment_item_zan
									.setBackgroundResource(R.drawable.zan);
							replyBean.setDigg(replyBean.getDigg() - 1);
						}
						holder.comment_item_zan_num.setText(replyBean.getDigg()
								+ "");
						break;

					default:
						break;
					}
				}
			};

			holder.comment_item_icon.setOnClickListener(onClickListener);
			holder.comment_item_zan.setOnClickListener(onClickListener);

			convertView.setTag(holder);
			return convertView;
		}

		private class CommentHolder {
			ImageView comment_item_icon;
			TextView comment_item_name;
			TextView comment_item_time;
			TextView comment_item_zan_num;
			TextView comment_item_content;
			View comment_item_zan;
		}

	}
	
	@Override
	public void finish() {
		hideSoftInput();
		super.finish();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (commentList.size() == 0) {
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		requestData(currentPageId + 1);
	}

}
