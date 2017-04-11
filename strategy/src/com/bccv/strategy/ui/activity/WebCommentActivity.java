package com.bccv.strategy.ui.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.R;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.CommentInfoResBean2;
import com.bccv.strategy.model.CommentInfoResBean2.CommentInfo;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.adapter.WebCommentAdapter;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.ui.view.XListView.IXListViewListener;
import com.bccv.strategy.utils.JsonObjectUitl;
import com.bccv.strategy.utils.SystemUtil;

public class WebCommentActivity extends BaseActivity implements 
								IXListViewListener ,OnScrollListener,OnItemClickListener{

	public static final String APP_ID = "id";
	public static final String RAID_TITLE = "title";
	
	private XListView listView;
	public LinearLayout loading;
	private WebCommentAdapter adapter;
	private boolean isLoadMore = false;
	private View bg_gray;
	private BackGroundView details_app_bg_view;
	private int[] backgrounds;
	private int cur_position = 0;
	private ImageView iv_back;
	private TextView tv_title;
	
	/**************评论条***************/
	private boolean commentlayoutIsShowing = false;
	private RelativeLayout details_import_rl;
	private EditText details_edit_et;
	private ImageView details_send_btn;

	/*************界面数据**************/
	private List<CommentInfo> comments;
	private int id;
	private int now_p = -1;
	private int total_p = -1;
	private String raid_id;
	private String title;
	
	private static final int FIRST_PAGE = 1;
	private static final int NUM_PER_PAGE = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_app_details);
		backgrounds = getIntent().getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		raid_id = getIntent().getStringExtra(APP_ID);
		title = getIntent().getStringExtra(RAID_TITLE);
		findView();
		if(TextUtils.isEmpty(raid_id)){
			showShortToast("数据有误");
			finish();
		}else{
			showDialog(true);
			getData(raid_id,1);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		backgrounds = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		raid_id = intent.getStringExtra(APP_ID);
//		getData(app_id,1,5);
		super.onNewIntent(intent);
	}
	
	private void findView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		details_app_bg_view = (BackGroundView) findViewById(R.id.details_app_bg_view);
		details_import_rl = (RelativeLayout) findViewById(R.id.details_import_rl);
		details_edit_et = (EditText) findViewById(R.id.details_edit_et);
		details_send_btn = (ImageView) findViewById(R.id.details_send_btn);
		listView = (XListView) findViewById(R.id.app_details_listview);
		loading = (LinearLayout) findViewById(R.id.details_loading);
		bg_gray = findViewById(R.id.bg_gray);
		
		if (backgrounds!=null && backgrounds.length == 2) {
			details_app_bg_view.setGradient(backgrounds[0], backgrounds[1]);
		}
		
		details_send_btn.setOnClickListener(this);
		iv_back.setOnClickListener(this);

		adapter = new WebCommentAdapter(this);
		listView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		listView.setPullRefreshEnable(false);
		listView.setOverScrollMode(SCROLL_STATE_IDLE);
		listView.setPullLoadEnable(true);
		listView.setEnableDragLoadMore(true);
		listView.setXListViewListener(this);
		
		listView.setAdapter(adapter);
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onRefresh() {
		listView.stopRefresh();
		listView.stopLoadMore();
	}

	@Override
	public void onLoadMore() {
		
		if (!isLoadMore) {
			if (total_p > 1 && total_p > now_p) {
				isLoadMore = true;
//				getData(app_id,++now_p,5);
			}else
				Toast.makeText(mContext, "没有新数据了", Toast.LENGTH_SHORT).show();
			listView.stopRefresh();
			listView.stopLoadMore();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.app_logo:
//			Intent personalZoneIntent = new Intent(WebCommentActivity.this, StrategyListActivity.class);
//			personalZoneIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
//			startActivity(personalZoneIntent);
//			break;
		case R.id.iv_back:
			System.out.println("2222222222");
			this.finish();
			break;
		case R.id.details_send_btn:
			if (UserInfoManager.isLogin()) {
				String trim = details_edit_et.getText().toString().trim();
				if (TextUtils.isEmpty(trim)) {
					Toast.makeText(mContext, "评论不能为空", Toast.LENGTH_SHORT).show();
					break;
				}
				showDialog(true);
				comment(id, trim, "");
			}else {
				Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if (firstVisibleItem > cur_position) {
			hideTypeLayout();
		}else if (firstVisibleItem < cur_position) {
			showTypeLayout();
		}
		
		cur_position = firstVisibleItem;
	}
	
	public void showTypeLayout() {
		if (!commentlayoutIsShowing) {
			commentlayoutIsShowing = true;
			details_import_rl.clearAnimation();
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, getResources().getDimensionPixelSize(R.dimen.comments_height), 0);
			translateAnimation.setDuration(100);
			translateAnimation.setFillAfter(true);
			
			details_import_rl.startAnimation(translateAnimation);
		}
	}

	public void hideTypeLayout() {
		if (commentlayoutIsShowing) {
			commentlayoutIsShowing = false;
			details_import_rl.clearAnimation();
			TranslateAnimation translateAnimation = new TranslateAnimation(0,
					0, 0, getResources().getDimensionPixelSize(R.dimen.comments_height));
			translateAnimation.setDuration(100);
			
			translateAnimation.setFillAfter(true);
			details_import_rl.startAnimation(translateAnimation);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(WebCommentActivity.this,Reply2PersonActivity.class);
		intent.putExtra(Reply2PersonActivity.APP_ID_KEY, String.valueOf(raid_id));
		intent.putExtra(Reply2PersonActivity.COMMENT_ID_KEY, comments == null ? "0" :String.valueOf(
				comments.get(position-1).id));
		intent.putExtra(Reply2PersonActivity.COMMENT_USERNAME_KEY, comments == null ? 0 :comments.get(position-1).user_name);
		intent.putExtra(Reply2PersonActivity.COMMENT_TEXT_KEY, comments == null ? 0 :comments.get(position-1).comment);
		intent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
		startActivity(intent);
	}
	
	private void getData(String raid_id,int p) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.getRaidComment(mContext, raid_id,p , new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					CommentInfoResBean2 bean = null;
					if (response.success && response instanceof CommentInfoResBean2) {
						bean = (CommentInfoResBean2) response;
					}
					setData(bean);
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
	
	private void setData(CommentInfoResBean2 bean) {
		if (bean != null) {
			if (!isLoadMore) {
//				id = bean.getId();
				now_p = bean.getNow_p();
				total_p = bean.getTotal_p();
				comments = bean.getComment_info().reply;
			}else {
				comments.addAll(bean.getComment_info().reply);
			}
		}
		setView();
	}

	private void setView() {
		
//		if (!isLoadMore) {
//			ImageLoaderUtil.getInstance(mContext).displayImage(icon, app_logo);
//			app_name.setText(title);
//		}
		if (now_p == total_p || total_p == 1) {
			listView.setPullLoadEnable(false);
		}
		if (comments == null || comments.size() <= 5) {
			details_import_rl.setVisibility(View.VISIBLE);
		}
		
		if (comments != null) {
			tv_title.setText(title);
			adapter.setList(comments);
			isLoadMore = false;
		}
		showDialog(false);
	}
	
	private void showDialog(boolean isShow) {
		if (isShow) {
			loading.setVisibility(View.VISIBLE);
		}else {
			loading.setVisibility(View.GONE);
		}
	}
	
	
	private void comment(int raid_id,String comment,String reply) {
		NetWorkAPI.add_comment(mContext, String.valueOf(raid_id), comment, reply, new HttpCallback() {
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
						CommentInfoResBean2 bean2 = new CommentInfoResBean2();
						CommentInfo com = bean2.new CommentInfo();
						com.comment = details_edit_et.getText().toString().trim();
//						com.setDigg(0);
//						com.setId(Integer.valueOf(comment_id));
//						com.setIs_digg(0);
//						com.setIs_more_comment(0);
//						com.setTimes(String.valueOf(System.currentTimeMillis()/1000));
						com.user_name = UserInfoManager.getUserName();
						comments.add(0, com);
						adapter.setList(comments);
						details_edit_et.setText("");
					} catch (JSONException e) {
						e.printStackTrace();
						return ;
					}
				}else {
					Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT).show();
				}
				showDialog(false);
			}
			
			@Override
			public void onError(String errorMsg) {showDialog(false);}
			
			@Override
			public void onCancel() {showDialog(false);}
		});
	}
	
	public void intent2Person(String userid) {
		Intent intent = new Intent(this,PersonalZoneActivity.class);
		intent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
		intent.putExtra(PersonalZoneActivity.ZONE_ID_KEY, userid);
		startActivity(intent);
	}
	
}
