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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.CommentInfoResBean;
import com.bccv.strategy.model.CommentInfoResBean.CommentInfo;
import com.bccv.strategy.model.CommentInfoResBean.Digg;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.JsonObjectUitl;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.ScreenUtil;
import com.bccv.strategy.utils.StringUtil;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class Reply2PersonActivity extends BaseActivity {

	public static final int REFRESH_CANCLE_LOADDING = 1;
	
	public static final String COMMENT_USERNAME_KEY = "comment_username_key";
	public static final String APP_ID_KEY = "app_id_key";
	public static final String COMMENT_ID_KEY = "comment_id_key";
	public static final String COMMENT_TEXT_KEY = "comment_text_key";
	public static final String COMMENT_TYPE_KEY = "comment_type_key";
	
	private BackGroundView background_view;
	private View errorView,loadingView;
	private View common_title_back_btn,common_title_right_btn;
	private TextView common_title_tv;
	private GridView comment_zan_gv;
	private TextView comment_content_tv,comment_content_zan_count;
	private XListView xListView;
	private EditText reply_edit_et;
	private View send_btn;

	private int[] background;
	
	private Handler mHandler;
	
	private ArrayList<Digg> zanList = new ArrayList<Digg>();
	private ArrayList<CommentInfo> commentList = new ArrayList<CommentInfo>();
	private CommentInfo baseCommentInfo;
	
	private CommentAdapter mCommentAdapter;
	private ZanAdapter mZanAdapter;
	
	private String app_id;
	private String comment_id;
	private String username;
	private String comment_text;
	
	private CommentInfoResBean data;
	
	private int zanItemViewSize = 50;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reply_2_person);
		
		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		app_id = intent.getStringExtra(APP_ID_KEY);
		comment_id = intent.getStringExtra(COMMENT_ID_KEY);
		username = intent.getStringExtra(COMMENT_USERNAME_KEY);
		comment_text = intent.getStringExtra(COMMENT_TEXT_KEY);
		
		zanItemViewSize = ScreenUtil.dp2px(mContext, 25);
		
		initHandler();
		initView();
		initData();
	}
	
	private void initView(){
		background_view = (BackGroundView) findViewById(R.id.background_view);
		common_title_back_btn = findViewById(R.id.common_title_back_btn);
		common_title_right_btn = findViewById(R.id.common_title_right_btn);
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		comment_zan_gv = (GridView) findViewById(R.id.comment_zan_gv);
		comment_content_tv = (TextView) findViewById(R.id.comment_content_tv);
		comment_content_zan_count = (TextView) findViewById(R.id.comment_content_zan_count);
		xListView = (XListView) findViewById(R.id.xlistview_include);
		reply_edit_et = (EditText) findViewById(R.id.reply_edit_et);
		send_btn = findViewById(R.id.send_btn);
		loadingView = findViewById(R.id.reply_2_person_loading);
		errorView = findViewById(R.id.reply_2_person_error);
		
		common_title_tv.setText(username);
		if(comment_text.length()>35){
			comment_text = comment_text.substring(0, 35)+"…";
		}
		comment_content_tv.setText(comment_text);
		
		comment_zan_gv.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		xListView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		xListView.setPullRefreshEnable(false);
		xListView.setPullLoadEnable(false);
		xListView.setEnableDragLoadMore(false);
//		xListView.setXListViewListener(this);
		
		background_view.setGradient(background[0], background[1]);
		
		common_title_back_btn.setOnClickListener(this);
		common_title_right_btn.setOnClickListener(this);
		send_btn.setOnClickListener(this);
		
		mZanAdapter = new ZanAdapter();
		comment_zan_gv.setAdapter(mZanAdapter);
		
		mCommentAdapter = new CommentAdapter();
		xListView.setAdapter(mCommentAdapter);
		
		comment_zan_gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//TODO 如果是 ... 按钮 列出所有赞的人的列表
				if(position==7){
					Intent moreZanIntent = new Intent(Reply2PersonActivity.this, MoreZanListActivity.class);
					moreZanIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
					moreZanIntent.putExtra(MoreZanListActivity.COMMENT_ID_KEY, comment_id);
					startActivity(moreZanIntent);
				}else if(position==0){
					//TODO 点赞
					
					if(UserInfoManager.isLogin()){
						if(baseCommentInfo!=null && !baseCommentInfo.is_digg 
								&& SystemUtil.isNetOkWithToast(Reply2PersonActivity.this.getApplicationContext())){
							baseCommentInfo.is_digg = true;
							Digg digg = data.new Digg(); 
							digg.user_icon = UserInfoManager.getUserIcon();
							digg.user_id = String.valueOf(UserInfoManager.getUserId());
							zanList.add(0, digg);
							mZanAdapter.notifyDataSetChanged();
							baseCommentInfo.digg+=1;
							comment_content_zan_count.setText(baseCommentInfo.digg+"人赞过");
							NetWorkAPI.digg_c(mContext, baseCommentInfo.id, new MyHttpCallback());
						}
					}else{
						Intent loginIntent = new Intent(Reply2PersonActivity.this, LoginActivity.class);
						loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
						startActivity(loginIntent);
					}

					
				}else{
					//TODO 跳转对应点赞人的主页
					Intent personalIntent = new Intent(Reply2PersonActivity.this, PersonalZoneActivity.class);
					personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
					personalIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY, zanList.get(position-1).user_id);
					Reply2PersonActivity.this.startActivity(personalIntent);
//					Intent moreZanIntent = new Intent(Reply2PersonActivity.this, MoreZanListActivity.class);
//					moreZanIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
//					moreZanIntent.putExtra(MoreZanListActivity.COMMENT_ID_KEY, comment_id);
//					startActivity(moreZanIntent);
					
				}
			}
		});
		
		
		xListView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
	
	private void initData(){
		requestData();		
	}
	
	
	private void requestData(){
		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {
			
			//TODO 
			errorView.setVisibility(View.GONE);
			loadingView.setVisibility(View.VISIBLE);
			NetWorkAPI.find_comment(mContext, comment_id, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					L.i(TAG, "onResult", response.toString());
					if(response instanceof CommentInfoResBean){
						data = (CommentInfoResBean) response;
						baseCommentInfo = data.getComment_info();
						if(baseCommentInfo.digg_list!=null){
							zanList.clear();
							zanList.addAll(baseCommentInfo.digg_list);
						}
						if(baseCommentInfo.reply!=null){
							commentList.clear();
							commentList.addAll(baseCommentInfo.reply);
						}
						refreshView();
					}else{
						showShortToast("数据获取失败");
						errorView.setVisibility(View.VISIBLE);
					}
					loadingView.setVisibility(View.GONE);
				}
				
				@Override
				public void onError(String errorMsg) {
					L.e(TAG, "onError", "errorMsg : " + errorMsg);
					showShortToast("数据获取失败");
					errorView.setVisibility(View.VISIBLE);
					loadingView.setVisibility(View.GONE);
				}
				
				@Override
				public void onCancel() {}
			});
			
		}
	}
	
	private void refreshView(){
		comment_content_tv.setText(baseCommentInfo.comment);
		if(baseCommentInfo.comment.length()>35){
			baseCommentInfo.comment = baseCommentInfo.comment.substring(0, 35)+"…";
		}
		comment_content_tv.setText(baseCommentInfo.comment);
		comment_content_zan_count.setText(baseCommentInfo.digg+"人赞过");
		mZanAdapter.notifyDataSetChanged();
		mCommentAdapter.notifyDataSetChanged();
	}

	@SuppressLint("HandlerLeak")
	private void initHandler(){
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				
				switch (msg.what) {
				case REFRESH_CANCLE_LOADDING:
					mZanAdapter.notifyDataSetChanged();
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
	private void doComment(){
		
		if (SystemUtil.isNetOkWithToast(this.getApplicationContext())) {

			final String comment = reply_edit_et.getText().toString().trim();
			if (TextUtils.isEmpty(comment)) {
				showShortToast("请输入内容");
				return;
			}

			hideSoftInput();
			//TODO 
			
			if(UserInfoManager.isLogin()){
				
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
								commentList.add(0, commentInfo);
								reply_edit_et.setText("");
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
			}else{
				Intent loginIntent = new Intent(Reply2PersonActivity.this, LoginActivity.class);
				loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
				startActivity(loginIntent);
			}
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
			appReleaseIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
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
	public void finish() {
		hideSoftInput();
		super.finish();
	}
	
	private class CommentAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return commentList==null?0:commentList.size();
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
			if(convertView == null){
				holder = new CommentHolder();
				convertView = View.inflate(mContext, R.layout.comment_item_view, null);
				holder.comment_item_icon = (ImageView) convertView.findViewById(R.id.comment_item_icon);
				holder.comment_item_name = (TextView) convertView.findViewById(R.id.comment_item_name);
				holder.comment_item_time = (TextView) convertView.findViewById(R.id.comment_item_time);
				holder.comment_item_zan_num = (TextView) convertView.findViewById(R.id.comment_item_zan_num);
				holder.comment_item_zan = (ImageView) convertView.findViewById(R.id.comment_item_zan);
				holder.comment_item_content = (TextView) convertView.findViewById(R.id.comment_item_content);
				
			}else{
				holder = (CommentHolder) convertView.getTag();
			}
			
			//TODO 设置数据
			final CommentInfo commentInfo = commentList.get(position);
			ImageLoaderUtil.getInstance(mContext).displayImage(commentInfo.user_icon,
					holder.comment_item_icon, ImageLoaderUtil.getUserIconImageOptions());
			holder.comment_item_name.setText(commentInfo.user_name);
			holder.comment_item_time.setText(StringUtil.formatLongDate(commentInfo.times, "MM/dd HH:mm"));
			holder.comment_item_zan_num.setText(commentInfo.digg+"");
			if(commentInfo.is_digg){
				holder.comment_item_zan.setBackgroundResource(R.drawable.zan_press);
			}else{
				holder.comment_item_zan.setBackgroundResource(R.drawable.zan);
			}
			holder.comment_item_content.setText(commentInfo.comment);
			
			OnClickListener onClickListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.comment_item_zan:
						
						if(UserInfoManager.isLogin()){
							if(!commentInfo.is_digg && SystemUtil.isNetOkWithToast(Reply2PersonActivity.this.getApplicationContext())){
								commentInfo.is_digg = true;
								holder.comment_item_zan.setBackgroundResource(R.drawable.zan_press);
								holder.comment_item_zan_num.setText(String.valueOf(commentInfo.digg+1));
								NetWorkAPI.digg_c(mContext, commentInfo.id, new MyHttpCallback());
							}
						}else{
							Intent loginIntent = new Intent(Reply2PersonActivity.this, LoginActivity.class);
							loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
							startActivity(loginIntent);
						}
						break;
					case R.id.comment_item_icon:
						Intent personalIntent = new Intent(Reply2PersonActivity.this, PersonalZoneActivity.class);
						personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR, background_view.getGradientColor());
						personalIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY, commentInfo.user_id);
						Reply2PersonActivity.this.startActivity(personalIntent);
						
						break;
					default:
						break;
					}
				}
			};
			
			holder.comment_item_zan.setOnClickListener(onClickListener);
			holder.comment_item_icon.setOnClickListener(onClickListener);
			
			
			convertView.setTag(holder);
			return convertView;
		}
		
		
		private class CommentHolder{
			ImageView comment_item_icon;
			TextView comment_item_name;
			TextView comment_item_time;
			TextView comment_item_content;
			TextView comment_item_zan_num;
			ImageView comment_item_zan;
		}
		
	}
	
	
	private class ZanAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			
			if(zanList==null){
				return 1;
			}else if(zanList.size()>8){
				return 8;
			}
			return zanList.size()+1;
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
			
			ImageView imageView = new ImageView(mContext);
			LayoutParams layoutParams = new LayoutParams(zanItemViewSize, zanItemViewSize);
			imageView.setLayoutParams(layoutParams);
			
			if(position==0){
				
				if(baseCommentInfo!=null&&baseCommentInfo.is_digg){
					imageView.setBackgroundResource(R.drawable.zan_press);
				}else{
					imageView.setBackgroundResource(R.drawable.zan);
				}
			}else if(position==7){
				imageView.setBackgroundResource(R.drawable.more_zan_btn_selector);
			}else{
				ImageLoaderUtil.getInstance(mContext).displayImage(zanList.get(position-1).user_icon,
						imageView, ImageLoaderUtil.getUserIconImageOptions());
			}
			
			return imageView;
		}
		
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
