package com.bccv.bangyangapp.ui.activity;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.common.GlobalConstants;
import com.bccv.bangyangapp.model.AppInfoItemBean;
import com.bccv.bangyangapp.model.CollectListResBean;
import com.bccv.bangyangapp.model.QuestionInfoBean;
import com.bccv.bangyangapp.model.QuestionInfoListResBean;
import com.bccv.bangyangapp.model.UserInfoBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.ui.adapter.MyQuestionAdapter;
import com.bccv.bangyangapp.ui.adapter.PersonalZoneAdapter;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.ui.view.XListView;
import com.bccv.bangyangapp.ui.view.XListView.IXListViewListener;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;
import com.bccv.bangyangapp.utils.SystemUtil;

public class PersonalZoneActivity extends BaseActivity implements 
					IXListViewListener,OnScrollListener,OnItemClickListener{

	public static final String ZONE_ID_KEY = "user_id";
	private String user_id;
	private boolean isSelf;
	private boolean isLeftLoadMore;
	private boolean isRightLoadMore;
	private XListView listView;
	private PersonalZoneAdapter sAdapter;
	private MyQuestionAdapter qAdapter;
	private RelativeLayout rl_show;
	private RelativeLayout zone_hot_rl;
	private TextView zone_hot_tv;
	private RelativeLayout zone_fresh_rl;
	private TextView zone_fresh_tv;
	private LinearLayout loading;
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private int cur = 1;
	private int cur_share_position = 0;
	private int cur_question_position = 0;
	private boolean First ;
	
	/*************head one*************/
	private RelativeLayout one_rl;
	private ImageView one_icon;
	private TextView one_tv_name;
	private TextView one_tv_sign;
	private ImageView zone_head_one_iv_arrow;
	
	/*************head two************/
	private RelativeLayout two_hot_rl;
	private TextView two_hot_tv;
	private RelativeLayout two_fresh_rl;
	private TextView two_fresh_tv;
	private RelativeLayout two_rl;
	
	/************title****************/
	private LinearLayout common_title_menu_btn;
	private LinearLayout common_title_right_btn;
	private ImageView iv_common_left;
	private ImageView iv_common_right;
	private TextView common_title_tv;
	
	/************edit text**************/
	private RelativeLayout zone_import_rl;
	
	private int[] backgrounds;
	private int right_now_p;
	private int right_total_p;
	private int left_now_p;
	private int left_total_p;
	private ArrayList<QuestionInfoBean> questions;
	private ArrayList<AppInfoItemBean> sharedApps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_personal_zone);
		user_id = getIntent().getStringExtra(ZONE_ID_KEY);
		if (user_id == null) {
			this.finish();
		}
		if (!user_id.equals(String.valueOf(UserInfoManager.getUserId()))) {
			isSelf = false;
		}else {
			isSelf = true;
		}
		backgrounds = getIntent().getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		First = true;
		findView();
		initHeads();
		setClick();
		isLeftLoadMore = false;
		isRightLoadMore = false;
		getSharedAppSet(user_id, 1, 10);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalConstants.USER_INFO_CHANGE_BROADCAST);
		registerReceiver(picReceiver, filter);
	}

	private void setClick() {
		if (isSelf) {
			one_rl.setOnClickListener(this);
		}
		two_hot_rl.setOnClickListener(this);
		two_fresh_rl.setOnClickListener(this);
		zone_hot_rl.setOnClickListener(this);
		zone_fresh_rl.setOnClickListener(this);
		common_title_menu_btn.setOnClickListener(this);
		common_title_right_btn.setOnClickListener(this);
	}

	private void initHeads() {
		LayoutInflater inflater = LayoutInflater.from(PersonalZoneActivity.this);
		View head1 = inflater.inflate(R.layout.personal_zone_head_one, null);
		View head2 = inflater.inflate(R.layout.personal_zone_head_two, null);
		
		one_rl = (RelativeLayout) head1.findViewById(R.id.zone_head_one_rl);
		one_icon = (ImageView) head1.findViewById(R.id.zone_head_one_icon);
		one_tv_name = (TextView) head1.findViewById(R.id.zone_head_one_tv_name);
		one_tv_sign = (TextView) head1.findViewById(R.id.zone_head_one_tv_sign);
		zone_head_one_iv_arrow = (ImageView) head1.findViewById(R.id.zone_head_one_iv_arrow);
		
		if (!isSelf) {
			zone_head_one_iv_arrow.setVisibility(View.INVISIBLE);
			getUserInfo(user_id);
		}else {
			ImageLoaderUtil.getInstance(mContext).displayImage(UserInfoManager.getUserIcon(), one_icon,
					ImageLoaderUtil.getUserIconImageOptions());
			one_tv_name.setText(UserInfoManager.getUserName());
			one_tv_sign.setText(UserInfoManager.getUserIntroduce());
		}
		
		two_hot_rl = (RelativeLayout) head2.findViewById(R.id.zone_head_two_hot_rl);
		two_hot_tv = (TextView) head2.findViewById(R.id.zone_head_two_hot_tv);
		two_fresh_rl = (RelativeLayout) head2.findViewById(R.id.zone_head_two_fresh_rl);
		two_fresh_tv = (TextView) head2.findViewById(R.id.zone_head_two_fresh_tv);
		two_rl = (RelativeLayout) head2.findViewById(R.id.zone_two_rl);
		
		listView.addHeaderView(head1);
		listView.addHeaderView(head2);
		
		sAdapter = new PersonalZoneAdapter(PersonalZoneActivity.this, null);
		qAdapter = new MyQuestionAdapter(PersonalZoneActivity.this, null);
		listView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		listView.setAdapter(sAdapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
	}

	private void findView() {
		zone_import_rl = (RelativeLayout) findViewById(R.id.zone_import_rl);
		rl_show = (RelativeLayout) findViewById(R.id.rl_show);
		listView = (XListView) findViewById(R.id.zone_lv);
		zone_hot_rl = (RelativeLayout) findViewById(R.id.zone_hot_rl);
		zone_hot_tv = (TextView) findViewById(R.id.zone_hot_tv);
		zone_fresh_rl = (RelativeLayout) findViewById(R.id.zone_fresh_rl);
		zone_fresh_tv = (TextView) findViewById(R.id.zone_fresh_tv);
		common_title_menu_btn = (LinearLayout) findViewById(R.id.common_title_menu_btn);
		common_title_right_btn = (LinearLayout) findViewById(R.id.common_title_right_btn);
		iv_common_left = (ImageView) findViewById(R.id.iv_common_left);
		iv_common_right = (ImageView) findViewById(R.id.iv_common_right);
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		loading = (LinearLayout) findViewById(R.id.personal_zone_loading);
		
		listView.setPullRefreshEnable(false);
		listView.setPullLoadEnable(false);
		listView.setEnableDragLoadMore(false);
		listView.setXListViewListener(this);
		
		iv_common_left.setBackgroundResource(R.drawable.title_menu_left_selector);
		common_title_tv.setText("个人中心");
	}

	@Override
	public void onRefresh() {}

	@Override
	public void onLoadMore() {
		if (cur == LEFT) {
			if (left_total_p>1 && left_total_p > left_now_p) {
				isLeftLoadMore = true;
				getSharedAppSet(user_id, ++left_now_p, 10);
			}else
				Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
		}else {
			if (right_total_p>1 && right_total_p > right_now_p) {
				isRightLoadMore = true;
				getQuestionSet(user_id, ++right_now_p, 10);
			}else {
				Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
			}
		}
		listView.stopLoadMore();
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (cur == RIGHT) {
			if (scrollState != SCROLL_STATE_IDLE) {
				zone_import_rl.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
	        int visibleItemCount, int totalItemCount) {
	    int top2 = two_rl.getTop();
	    if (top2 <= 0 || firstVisibleItem > 1 ) {
	    	rl_show.setVisibility(View.VISIBLE);
	    }else {
	    	rl_show.setVisibility(View.INVISIBLE);
	    }
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.zone_hot_rl:
		case R.id.zone_head_two_hot_rl:
			if (cur != LEFT) {
				zone_import_rl.setVisibility(View.GONE);
				setChangeBg(LEFT);
				cur_share_position = listView.getFirstVisiblePosition();
				if (sharedApps != null && sharedApps.size() > 0) {
					setSharedView();
				}else {
					getSharedAppSet(user_id,1,10);
				}
			}
			break;
		case R.id.zone_fresh_rl:
		case R.id.zone_head_two_fresh_rl:
			if (cur != RIGHT) {
				setChangeBg(RIGHT);
				cur_question_position = listView.getFirstVisiblePosition();
				if (questions != null && questions.size() > 0) {
					setView();
				}else {
					getQuestionSet(user_id,1,10);
				}
			}
			break;
		case R.id.common_title_menu_btn:
			onBackPressed();
			this.finish();
			break;
		case R.id.common_title_right_btn:
			Intent appIntent = new Intent(PersonalZoneActivity.this,AppReleaseActivity.class);
			appIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
			startActivity(appIntent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;
		case R.id.zone_head_one_rl:
			zone_import_rl.setVisibility(View.GONE);
			Intent intent = new Intent(PersonalZoneActivity.this,PersonalActivity.class);
			intent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void setChangeBg(int cur_item) {
		if (cur_item == LEFT) {
			zone_hot_rl.setBackgroundResource(R.drawable.change_left_select);
			two_hot_rl.setBackgroundResource(R.drawable.change_left_select);
			zone_hot_tv.setTextColor(0xffffffff);
			two_hot_tv.setTextColor(0xffffffff);
			
			zone_fresh_rl.setBackgroundResource(R.drawable.change_right);
			two_fresh_rl.setBackgroundResource(R.drawable.change_right);
			zone_fresh_tv.setTextColor(0x4dffffff);
			two_fresh_tv.setTextColor(0x4dffffff);
		}else {
			zone_hot_rl.setBackgroundResource(R.drawable.change_left);
			two_hot_rl.setBackgroundResource(R.drawable.change_left);
			zone_hot_tv.setTextColor(0x4dffffff);
			two_hot_tv.setTextColor(0x4dffffff);
			
			zone_fresh_rl.setBackgroundResource(R.drawable.change_right_select);
			two_fresh_rl.setBackgroundResource(R.drawable.change_right_select);
			zone_fresh_tv.setTextColor(0xffffffff);
			two_fresh_tv.setTextColor(0xffffffff);
		}
	}
	
	public RelativeLayout getLayout() {
		if (zone_import_rl != null) {
			return zone_import_rl;
		}
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (cur == LEFT && sharedApps != null && position > 2 &&
				Integer.valueOf(sharedApps.get(position-3).getId()) > 0) {
			Intent intent = new Intent(PersonalZoneActivity.this,AppDetailsActivity.class);
			intent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
			intent.putExtra(AppDetailsActivity.APP_ID, sharedApps.get(position-3).getId());
			startActivity(intent);
		}else if (cur == RIGHT && questions != null && position > 2 && 
				Integer.valueOf(questions.get(position-3).getId()) > 0) {
			Intent answer2PersonIntent = new Intent(PersonalZoneActivity.this,Answer2PersonActivity.class);
			answer2PersonIntent.putExtra(BackGroundView.BACKGROUND_COLOR,backgrounds);
			answer2PersonIntent.putExtra(Answer2PersonActivity.QUESTION_INFO_KEY,
					questions.get(position - 3));
			startActivity(answer2PersonIntent);
		}
	}
	//RIGHT page
	private void getQuestionSet(String user_id,int p,int num) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			showDialog(true);
			NetWorkAPI.getUserQuestion(mContext, user_id, p, num, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof QuestionInfoListResBean) {
						QuestionInfoListResBean bean = (QuestionInfoListResBean) response;
						getData(bean);
					}else {
						setView();
					}
					showDialog(false);
				}
				
				@Override
				public void onError(String errorMsg) {
					setView();
					showDialog(false);
				}
				
				@Override
				public void onCancel() {
					setView();
					showDialog(false);
				}
			});
		}
		
	}
	
	protected void getData(QuestionInfoListResBean bean) {
		if (bean !=null) {
			right_now_p = bean.getNow_p();
			right_total_p = bean.getTotal_p();
//			questions = bean.getQuestionInfoBeans();
			if (!isRightLoadMore) {
				questions = bean.getQuestionInfoBeans();
			}else {
				questions.addAll(bean.getQuestionInfoBeans());
//				isRightLoadMore = false;
			}
			if (questions == null || questions.size() < 10) {
				listView.setPullLoadEnable(false);
				listView.setEnableDragLoadMore(false);
			}else {
				listView.setPullLoadEnable(true);
				listView.setEnableDragLoadMore(true);
			}
			setView();
		}
	}
	
	private void setView() {
		cur = RIGHT;
		qAdapter.setList(questions);
		if (isRightLoadMore) {
			qAdapter.notifyDataSetChanged();
		}else {
			listView.setAdapter(qAdapter);
			if (cur_share_position >= 2) {
				listView.setSelection(cur_share_position);
			}else {
				listView.setSelection(2);
			}
		}
		isRightLoadMore = false;
	}

	private void showDialog(boolean isShow) {
		if (isShow) {
			loading.setVisibility(View.VISIBLE);
		}else {
			loading.setVisibility(View.GONE);
		}
	}
	//LEFT page
	private void getSharedAppSet(String user_id,int p,int num) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			showDialog(true);
			NetWorkAPI.getUserSharedApp(mContext, user_id, p, num, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof CollectListResBean) {
						CollectListResBean bean = (CollectListResBean) response;
						getSharedData(bean);
					}else {
						setSharedView();
					}
					showDialog(false);
				}
				
				@Override
				public void onError(String errorMsg) {
					setSharedView();
					showDialog(false);
				}
				
				@Override
				public void onCancel() {
					setSharedView();
					showDialog(false);
				}
			});
		}
		
	}

	protected void getSharedData(CollectListResBean bean) {
		if (bean != null) {
			left_total_p = bean.getTotal_p();
			left_now_p = bean.getNow_p();
//			sharedApps = bean.getAppInfoItemBeans();
			if (!isLeftLoadMore) {
				sharedApps = bean.getAppInfoItemBeans();
			}else {
				sharedApps.addAll(bean.getAppInfoItemBeans());
//				isLeftLoadMore = false;
			}
			
			if (sharedApps == null || sharedApps.size() < 10) {
				listView.setPullLoadEnable(false);
				listView.setEnableDragLoadMore(false);
			}else {
				listView.setPullLoadEnable(true);
				listView.setEnableDragLoadMore(true);
			}
			setSharedView();
		}
	}
	
	private void setSharedView() {
		cur = LEFT;
		sAdapter.setList(sharedApps);
		if (isLeftLoadMore) {
			sAdapter.notifyDataSetChanged();
		}else {
			listView.setAdapter(sAdapter);
			if (!First) {
				if (cur_question_position >= 2 ) {
					listView.setSelection(cur_question_position);
				}else {
					listView.setSelection(2);
				}
			}
			First = false;
		}
		isLeftLoadMore = false;
	}
	
	private void getUserInfo(String user_ids) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.getUserInfo(mContext, user_id, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof UserInfoBean) {
						UserInfoBean bean = (UserInfoBean) response;
						if (bean != null) {
							ImageLoaderUtil.getInstance(mContext).displayImage(
									bean.getUser_icon(), one_icon,ImageLoaderUtil.getUserIconImageOptions());
							one_tv_name.setText(bean.getUser_name());
							one_tv_sign.setText(bean.getIntroduce());
						}
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					
				}
				
				@Override
				public void onCancel() {
					
				}
			});
		}
	}
	
	BroadcastReceiver picReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ImageLoaderUtil.getInstance(mContext).displayImage(UserInfoManager.getUserIcon(), one_icon,
					ImageLoaderUtil.getUserIconImageOptions());
			one_tv_name.setText(UserInfoManager.getUserName());
			one_tv_sign.setText(UserInfoManager.getUserIntroduce());
		}
	};
	
	protected void onDestroy() {
		unregisterReceiver(picReceiver);
		super.onDestroy();
	};
}
