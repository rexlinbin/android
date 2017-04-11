package com.bccv.strategy.ui.activity;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.CommentInfoResBean;
import com.bccv.strategy.model.CommentInfoResBean.CommentInfo;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.ui.view.XListView.IXListViewListener;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.JsonObjectUitl;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.StringUtil;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class CommentListActivity extends BaseActivity implements
		IXListViewListener {

	public static final int DATA_UPDATE = 1;
	public static final int UI_ROLL_MSG = 3;

	public static final String APP_ID_KEY = "app_id_key";
	public static final String APP_NAME_KEY = "app_name_key";
	public static final String COMMENT_ID_KEY = "comment_id_key";
	public static final String COMMENT_TYPE_KEY = "comment_type_key";

	private BackGroundView background_view;
	private View errorView, loadingView;
	private View common_title_back_btn, common_title_right_btn;
	private TextView common_title_tv;
	private XListView xListView;
	private EditText comment_edit_et;
	private View comment_send_btn;

	private int[] background;

	private Handler mHandler;
	private CommentInfoResBean data;
	private ArrayList<CommentInfo> commentList = new ArrayList<CommentInfo>();
	private CommentAdapter mCommentAdapter;

	private String app_id;
	private String app_name;
	private String comment_id;
	private int comment_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_list);

		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		// private String app_name;
		// private String comment_id;
		// private int comment_type;
		app_id = intent.getStringExtra(APP_ID_KEY);
		app_name = intent.getStringExtra(APP_NAME_KEY);
		comment_id = intent.getStringExtra(COMMENT_ID_KEY);
		comment_type = intent.getIntExtra(COMMENT_TYPE_KEY, 0);
		initHandler();
		initView();
		initData();
	}

	private void initView() {
		background_view = (BackGroundView) findViewById(R.id.background_view);
		common_title_back_btn = findViewById(R.id.common_title_back_btn);
		common_title_right_btn = findViewById(R.id.common_title_right_btn);
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		xListView = (XListView) findViewById(R.id.xlistview_include);
		comment_edit_et = (EditText) findViewById(R.id.comment_edit_et);
		comment_send_btn = findViewById(R.id.comment_send_btn);
		loadingView = findViewById(R.id.comment_list_loading);
		errorView = findViewById(R.id.comment_list_error);

		common_title_tv.setText("评论:"+app_name);
		background_view.setGradient(background[0], background[1]);

		common_title_back_btn.setOnClickListener(this);
		common_title_right_btn.setOnClickListener(this);
		comment_send_btn.setOnClickListener(this);

		xListView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setEnableDragLoadMore(false);
		xListView.setXListViewListener(this);

		mCommentAdapter = new CommentAdapter();
		xListView.setAdapter(mCommentAdapter);
	}

	private void initData() {
		requestData();
	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO
				switch (msg.what) {
				case DATA_UPDATE:
					mCommentAdapter.notifyDataSetChanged();
					this.sendEmptyMessageDelayed(UI_ROLL_MSG, 0l);

					break;
				case UI_ROLL_MSG:
					xListView.setSelection(10);
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

			final String comment = comment_edit_et.getText().toString().trim();
			if (TextUtils.isEmpty(comment)) {
				showShortToast("请输入内容");
				return;
			}

			hideSoftInput();
			//TODO 
			NetWorkAPI.add_comment(mContext, app_id, comment, comment_id, new HttpCallback() {
				@Override
				public void onResult(NetResBean response) {
					if (response.success) {
						Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
						String dataString = response.toString();
						JSONObject object;
						try {
							object = new JSONObject(dataString);
							String str = JsonObjectUitl.getString(object, "data");
							JSONObject object2 = new JSONObject(str);
							String comment_id = JsonObjectUitl.getString(object2, "comment_id");
							
							 CommentInfo commentInfo = data.new CommentInfo();
							 commentInfo.id = comment_id;
							 commentInfo.user_id = String.valueOf(UserInfoManager.getUserId());
							 commentInfo.comment = comment;
							 commentInfo.user_icon = UserInfoManager.getUserIcon();
							 commentInfo.user_name = UserInfoManager.getUserName();
							 commentInfo.is_digg = false;
							 commentInfo.digg = 0;
							 commentInfo.times = System.currentTimeMillis();
							 commentInfo.isBasic = false;
							 commentList.add(1, commentInfo);
							 comment_edit_et.setText("");
							mCommentAdapter.notifyDataSetChanged();
							
						} catch (JSONException e) {
							e.printStackTrace();
							return ;
						}
					}else {
						Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					L.e(TAG, "onError", errorMsg);
					}
				
				@Override
				public void onCancel() {}
			});
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

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

		case R.id.comment_send_btn:
			doComment();
			break;

		default:
			break;
		}
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
						R.layout.app_comment_list_item, null);
				holder.first_comment_item = convertView
						.findViewById(R.id.first_comment_item);
				holder.second_comment_item = convertView
						.findViewById(R.id.second_comment_item);
				holder.first_iv_icon = (ImageView) convertView
						.findViewById(R.id.first_iv_icon);
				holder.comment_item_icon = (ImageView) convertView
						.findViewById(R.id.comment_item_icon);
				holder.first_tv_name = (TextView) convertView
						.findViewById(R.id.first_tv_name);
				holder.comment_item_name = (TextView) convertView
						.findViewById(R.id.comment_item_name);
				holder.first_tv_time = (TextView) convertView
						.findViewById(R.id.first_tv_time);
				holder.comment_item_time = (TextView) convertView
						.findViewById(R.id.comment_item_time);
				holder.first_iv_like = (ImageView) convertView
						.findViewById(R.id.first_iv_like);
				holder.comment_item_zan = (ImageView) convertView
						.findViewById(R.id.comment_item_zan);
				holder.first_tv_like_num = (TextView) convertView
						.findViewById(R.id.first_tv_like_num);
				holder.comment_item_zan_num = (TextView) convertView
						.findViewById(R.id.comment_item_zan_num);
				holder.first_tv_comments_content = (TextView) convertView
						.findViewById(R.id.first_tv_comments_content);
				holder.comment_item_content = (TextView) convertView
						.findViewById(R.id.comment_item_content);
				holder.first_iv_divider_line = (ImageView) convertView
						.findViewById(R.id.first_iv_divider_line);
				holder.first_item_divider = convertView.findViewById(R.id.first_item_divider);

			} else {
				holder = (CommentHolder) convertView.getTag();
			}
			
			final CommentInfo commentInfo = commentList.get(position);
			if (commentInfo.isBasic) {
				holder.first_comment_item.setVisibility(View.VISIBLE);
				holder.second_comment_item.setVisibility(View.GONE);

				ImageLoaderUtil.getInstance(mContext).displayImage(
						commentInfo.user_icon, holder.first_iv_icon,
						ImageLoaderUtil.getUserIconImageOptions());
				
				holder.first_tv_name.setText(commentInfo.user_name);
				holder.first_tv_time.setText(StringUtil.formatLongDate(commentInfo.times, "MM/dd HH:mm"));
				
				if(commentInfo.is_digg){
					holder.first_iv_like.setBackgroundResource(R.drawable.zan_press);
				}else{
					holder.first_iv_like.setBackgroundResource(R.drawable.zan);
				}
				holder.first_tv_like_num.setText("" + commentInfo.digg);
				holder.first_tv_comments_content.setText(commentInfo.comment);
				if(commentList.size()<=1){
					holder.first_iv_divider_line.setVisibility(View.GONE);
					holder.first_item_divider.setVisibility(View.VISIBLE);
				}else{
					holder.first_item_divider.setVisibility(View.GONE);
					holder.first_iv_divider_line.setVisibility(View.VISIBLE);
				}
				
			} else {
				holder.first_comment_item.setVisibility(View.GONE);
				holder.second_comment_item.setVisibility(View.VISIBLE);

				ImageLoaderUtil.getInstance(mContext).displayImage(
						commentInfo.user_icon, holder.comment_item_icon,
						ImageLoaderUtil.getUserIconImageOptions());
				
				holder.comment_item_name.setText(commentInfo.user_name);
				holder.comment_item_time.setText(StringUtil.formatLongDate(commentInfo.times, "MM/dd HH:mm"));
				
				if(commentInfo.is_digg){
					holder.comment_item_zan.setBackgroundResource(R.drawable.zan_press);
				}else{
					holder.comment_item_zan.setBackgroundResource(R.drawable.zan);
				}
				holder.comment_item_zan_num.setText("" + commentInfo.digg);
				holder.comment_item_content.setText(commentInfo.comment);
			}

			// TODO 设置数据

			OnClickListener onClickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.first_iv_icon:
					case R.id.comment_item_icon:

						Intent personalIntent = new Intent(
								CommentListActivity.this,
								PersonalZoneActivity.class);
						personalIntent.putExtra(
								BackGroundView.BACKGROUND_COLOR,
								background_view.getGradientColor());
						personalIntent.putExtra(
								PersonalZoneActivity.ZONE_ID_KEY,
								commentInfo.user_id);
						CommentListActivity.this.startActivity(personalIntent);

						break;
					case R.id.first_iv_like:
							if(!commentInfo.is_digg && SystemUtil.isNetOkWithToast(CommentListActivity.this.getApplicationContext())){
								commentInfo.is_digg = true;
								holder.first_iv_like.setBackgroundResource(R.drawable.zan_press);
								holder.first_tv_like_num.setText(String.valueOf(commentInfo.digg+1));
								NetWorkAPI.digg_c(mContext, commentInfo.id, new MyHttpCallback());
							}
						break;
					case R.id.comment_item_zan:
						// TODO 更改UI 增加赞
						if(!commentInfo.is_digg && SystemUtil.isNetOkWithToast(CommentListActivity.this.getApplicationContext())){
							commentInfo.is_digg = true;
							holder.comment_item_zan.setBackgroundResource(R.drawable.zan_press);
							holder.comment_item_zan_num.setText(String.valueOf(commentInfo.digg+1));
							NetWorkAPI.digg_c(mContext, commentInfo.id, new MyHttpCallback());
						}
						break;
					default:
						break;
					}

				}
			};

			holder.first_iv_icon.setOnClickListener(onClickListener);
			holder.comment_item_icon.setOnClickListener(onClickListener);
			holder.first_iv_like.setOnClickListener(onClickListener);
			holder.comment_item_zan.setOnClickListener(onClickListener);

			convertView.setTag(holder);
			return convertView;
		}

		private class CommentHolder {

			View first_comment_item;
			View second_comment_item;
			ImageView first_iv_icon;
			ImageView comment_item_icon;
			TextView first_tv_name;
			TextView comment_item_name;
			TextView first_tv_time;
			TextView comment_item_time;
			ImageView first_iv_like;
			ImageView comment_item_zan;
			TextView first_tv_like_num;
			TextView comment_item_zan_num;
			TextView first_tv_comments_content;
			TextView comment_item_content;
			ImageView first_iv_divider_line;
			View first_item_divider;

		}

	}

	private void requestData(){
		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
			
			if(commentList.size()==0){
				loadingView.setVisibility(View.VISIBLE);
			}
			//TODO 
			NetWorkAPI.find_comment(mContext, comment_id, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					L.i(TAG, "onResult", response.toString());
					if(response instanceof CommentInfoResBean){
						data = (CommentInfoResBean) response;
						CommentInfo comment_info = data.getComment_info();
						if(comment_info!=null){
							commentList.clear();
							commentList.add(comment_info);
							commentList.addAll(comment_info.reply);
							mCommentAdapter.notifyDataSetChanged();
							
							if(comment_type != 0){
								xListView.setSelection(data.getNow_situation()+1);
							}
						}else{
							showShortToast("数据获取失败");
						}
					}else{
						showShortToast("数据获取失败");
					}
					xListView.stopRefresh();
					xListView.stopLoadMore();
					loadingView.setVisibility(View.GONE);
				}
				
				@Override
				public void onError(String errorMsg) {
					L.e(TAG, "onError", "errorMsg : " + errorMsg);
					showShortToast("数据获取失败");
					xListView.stopRefresh();
					xListView.stopLoadMore();
					loadingView.setVisibility(View.GONE);
				}
				
				@Override
				public void onCancel() {}
			});
			
		}
	}
	
	
	@Override
	public void onRefresh() {
		// TODO 刷新数据
		requestData();
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		xListView.stopRefresh();
		xListView.stopLoadMore();

	}
	
	private void hideSoftInput() {
		/* 隐藏软键盘 */
		InputMethodManager imm = (InputMethodManager) comment_edit_et
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(
					comment_edit_et.getApplicationWindowToken(), 0);
		}
	}
	
	@Override
	public void finish() {
		hideSoftInput();
		super.finish();
	}
	
	private class MyHttpCallback implements HttpCallback{

		@Override
		public void onResult(NetResBean response) {
			L.i(TAG, "onResult", response.toString());
		}

		@Override
		public void onCancel() {}

		@Override
		public void onError(String errorMsg) {
			L.e(TAG, "onError", errorMsg);
		}
		
	}

}
