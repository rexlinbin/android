package com.bccv.bangyangapp.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.bccv.bangyangapp.ApplicationManager;
import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.db.StorageModule;
import com.bccv.bangyangapp.model.AppDetailsBean;
import com.bccv.bangyangapp.model.CommentBean;
import com.bccv.bangyangapp.model.DownloadBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.sns.SNSShareManager;
import com.bccv.bangyangapp.sns.SNSShareManager.ShareStateListener;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.sns.bean.SNSUserInfo;
import com.bccv.bangyangapp.sns.bean.ShareInfo;
import com.bccv.bangyangapp.ui.adapter.AppDetailsAdapter;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.ui.view.DownloadPopwindow;
import com.bccv.bangyangapp.ui.view.MyScrollView;
import com.bccv.bangyangapp.ui.view.MyScrollView.OnScrollChangedListener;
import com.bccv.bangyangapp.ui.view.SharePopwindow;
import com.bccv.bangyangapp.ui.view.XListView;
import com.bccv.bangyangapp.ui.view.XListView.IXListViewListener;
import com.bccv.bangyangapp.utils.ImageLoaderUtil;
import com.bccv.bangyangapp.utils.JsonObjectUitl;
import com.bccv.bangyangapp.utils.SystemUtil;

public class AppDetailsActivity extends BaseActivity implements 
								IXListViewListener ,OnScrollChangedListener,OnScrollListener,OnItemClickListener{

	private static int TOTAL_COUNT = 2;
	public static final String APP_ID = "id";
	private XListView listView;
	private LinearLayout loading;
	private ImageView iv_surface;
	private ImageView iv_bottom;
	private TextView tv_show;
	private ImageView iv_more;
	private ImageView iv_back;
	private ImageView iv_like;
	private ImageView iv_down;
	private ImageView iv_share;
	private ImageView iv_collect;
	private AppDetailsAdapter adapter;
	private boolean more = false;
	private boolean isLoadMore = false;
	private MyScrollView scroll;
	private ImageView app_logo;
	private TextView app_name;
	private TextView app_des;
	private TextView app_category;
	private TextView app_category2;
	private View bg_gray;
	private BackGroundView details_app_bg_view;
	private int[] backgrounds;
	private int cur_position = 0;
	
	/**************评论条***************/
	private boolean commentlayoutIsShowing = false;
	private RelativeLayout details_import_rl;
	private EditText details_edit_et;
	private ImageView details_send_btn;

	/*************界面数据**************/
	private String description;
	private int choice;
	private List<CommentBean> comments;
	private String creator_icon;
	private int creator_id;
	private String creator_name;
	private String ftitle;
	private String icon;
	private int id;
	private List<String> pic;
	private String times;
	private String title;
	private boolean is_digg;
	private boolean is_like;
	private int now_p;
	private int total_p;
	private int price;
	private String type_name;
	private String url;
	private List<DownloadBean> downloads;
	private int app_id;
	private int versionCode;
	private String pkgName;
	
	private DownloadPopwindow window ; //下载的提示框
	private SharePopwindow windowShare ; //分享的提示框
	private SNSShareManager ssManager;
	private StorageModule sModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_app_details);
		backgrounds = getIntent().getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		String appId = getIntent().getStringExtra(APP_ID);
		sModule = StorageModule.getInstance(); 
		setShare();
		if(TextUtils.isEmpty(appId)){
			showShortToast("数据有误");
			finish();
		}else{
			app_id = Integer.valueOf(appId);
			findView();
			initWindow();
			getData(app_id,1,5);
		}
		
	}
	
	private void setShare() {
		ssManager = SNSShareManager.getInstance();
//		ssManager.setShareStateListener(shareListener);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		backgrounds = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		app_id = Integer.valueOf(intent.getStringExtra(APP_ID));
		getData(app_id,1,5);
		super.onNewIntent(intent);
	}
	
	private void findView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_like = (ImageView) findViewById(R.id.iv_like);
		iv_down = (ImageView) findViewById(R.id.iv_down);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		
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
		
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_down.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		iv_like.setOnClickListener(this);
		details_send_btn.setOnClickListener(this);

		View view = LayoutInflater.from(mContext).inflate(R.layout.second_app_details_head, null);
		initHead(view);

		listView.addHeaderView(view);
		adapter = new AppDetailsAdapter(this);
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

	private void initHead(View view) {
		scroll = (MyScrollView) view.findViewById(R.id.scroll);
		iv_surface = (ImageView) view.findViewById(R.id.iv_surface);
		iv_bottom = (ImageView) view.findViewById(R.id.iv_bottom);
		tv_show = (TextView) view.findViewById(R.id.app_details_des);
		iv_more = (ImageView) view.findViewById(R.id.details_more);
		app_logo = (ImageView) view.findViewById(R.id.app_logo);
		app_name = (TextView) view.findViewById(R.id.app_name);
		app_des = (TextView) view.findViewById(R.id.app_des);
		app_category = (TextView) view.findViewById(R.id.app_category);
		app_category2 = (TextView) view.findViewById(R.id.app_category2);
		scroll.setOnScrollChangedListener(this);
		scroll.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		
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
				getData(app_id,++now_p,5);
			}else
				Toast.makeText(mContext, "没有新数据了", Toast.LENGTH_SHORT).show();
			listView.stopRefresh();
			listView.stopLoadMore();
		}
	}

	@Override
	public void onBackPressed() {
		if (window != null && window.isShowing()) {
			window.dismiss();
			return ;
		}
		super.onBackPressed();
		this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.details_more:
			if (!more) {
				tv_show.setText(description);
				iv_more.setBackgroundResource(R.drawable.app_details_less_selector);
				more = true;
			} else {
				tv_show.setText(description.substring(0, 50) + "...");
				iv_more.setBackgroundResource(R.drawable.app_details_more_selector);
				more = false;
			}
			break;
		case R.id.iv_like:
			if (UserInfoManager.isLogin()) {
				if (!is_digg) {
					iv_like.setImageResource(R.drawable.like_press);
					is_digg = true;
					digg_app(id);
				}else {
					Toast.makeText(mContext, "已赞", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.iv_collect:
			if (UserInfoManager.isLogin()) {
				if (!is_like) {
					iv_collect.setImageResource(R.drawable.collect_2_press);
					is_like = true;
					like(id);
				}else {
					Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
				}
			}else {
				if (!sModule.isCared(String.valueOf(id))) {
					iv_collect.setImageResource(R.drawable.collect_2_press);
					is_like = true;
					sModule.addLocalCare(String.valueOf(id));
				}else{
					Toast.makeText(mContext, "已收藏", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.iv_down:
			if (window != null && !window.isShowing()) {
				window.show(bg_gray, downloads,versionCode,pkgName,title);
			}
			break;
		case R.id.iv_share:
			ShareInfo sInfo = new ShareInfo();
			sInfo.text = description;
			sInfo.title = title;
			sInfo.imageUrl = icon;
			sInfo.titleUrl = url;
			sInfo.url = sInfo.titleUrl;
			ssManager.setShareStateListener(shareListener);
			windowShare.show(bg_gray, sInfo);
			break;
		case R.id.iv_back:
			onBackPressed();
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
	
	private void initWindow() {
		window = new DownloadPopwindow(this, bg_gray);
		windowShare = new SharePopwindow(this, bg_gray);
	}

	private void addImage() {
		
		scroll.removeAllViews();
		
		LayoutInflater inflater = LayoutInflater.from(AppDetailsActivity.this);
		LinearLayout mLinearLayout = new LinearLayout(AppDetailsActivity.this);
		mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

		for (int i = 0; i < TOTAL_COUNT; i++) {
			View scroView = inflater.inflate(R.layout.gallery_item, null);

			final ImageView imageView1 = (ImageView) scroView
					.findViewById(R.id.gallery_item_image);
			imageView1.setBackgroundResource(R.drawable.default_320568);
			if (pic != null && pic.size() > 0) {
				ImageLoaderUtil.getInstance(mContext).displayImage(
						pic.get(i)
						, imageView1,ImageLoaderUtil.getAutoRotateImageOptions());
			}

			final int position = i;
			imageView1.getParent().requestDisallowInterceptTouchEvent(true);
			imageView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(AppDetailsActivity.this,
							ScreenShotActivity.class);

					intent.putExtra("images", (ArrayList<String>) pic);// 非必须
					intent.putExtra("position", position);

					int[] location = new int[2];
					imageView1.getLocationOnScreen(location);
					intent.putExtra("locationX", location[0]);// 必须
					intent.putExtra("locationY", location[1]);// 必须

					intent.putExtra("width", imageView1.getWidth());// 必须
					intent.putExtra("height", imageView1.getHeight());// 必须
					startActivity(intent);
					overridePendingTransition(0, 0);
				}
			});
			mLinearLayout.addView(scroView);
		}
		scroll.addView(mLinearLayout);
	}

	@SuppressLint("NewApi")
	@Override
	public void onScrollChanged(int l, int t, int oldl, int oldt) {
		int margin = getResources().getDimensionPixelSize(R.dimen.test_margin);
		int pixelSize = getResources().getDimensionPixelSize(R.dimen.test_width);
		int width = scroll.getChildAt(0).getWidth()-pixelSize - margin*4;
		int width2 = iv_bottom.getWidth()-iv_surface.getWidth();
		
		float scale = (float)width2/(float)width;
		ObjectAnimator.ofFloat(iv_surface, "translationX", oldl*scale,l*scale)
		.setDuration(10).start();
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
		Intent intent = new Intent(AppDetailsActivity.this,Reply2PersonActivity.class);
		intent.putExtra(Reply2PersonActivity.APP_ID_KEY, String.valueOf(app_id));
		intent.putExtra(Reply2PersonActivity.COMMENT_ID_KEY, comments == null ? "0" :String.valueOf(
				comments.get(position-2).getId()));
		intent.putExtra(Reply2PersonActivity.COMMENT_USERNAME_KEY, comments == null ? 0 :comments.get(position-2).getUser_name());
		intent.putExtra(Reply2PersonActivity.COMMENT_TEXT_KEY, comments == null ? 0 :comments.get(position-2).getComment());
		intent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
		startActivity(intent);
	}
	
	private void getData(int id,int p,int num) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			showDialog(true);
			NetWorkAPI.getAppDetails(mContext,id,p,num,new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof AppDetailsBean) {
						AppDetailsBean bean = (AppDetailsBean) response;
						setData(bean);
					}
					showDialog(false);
				}

				@Override
				public void onError(String errorMsg) {
					showDialog(false);
				}
				
				@Override
				public void onCancel() {
					showDialog(false);
				}
			});
		}
	}
	
	private void setData(AppDetailsBean bean) {
		if (bean != null) {
			if (!isLoadMore) {
				choice = bean.getChoice();
				creator_icon = bean.getCreator_icon();
				creator_id = bean.getCreator_id();
				creator_name = bean.getCreator_name();
				ftitle = bean.getFtitle();
				icon = bean.getIcon();
				id = bean.getId();
				description = bean.getIntroduce();
				pic = bean.getPics();
				times = bean.getTimes();
				title = bean.getTitle();
				is_digg = bean.isIs_digg();
				is_like = bean.isIs_like();
				now_p = bean.getNow_p();
				total_p = bean.getTotal_p();
				price = bean.getPrice();
				type_name = bean.getType_name();
				url = bean.getUrl();
				downloads = bean.getDownload();
				pkgName = bean.getPkgname();
				versionCode = bean.getVersioncode();
				comments = bean.getComment();
			}else {
				comments.addAll(bean.getComment());
			}
			setView();
		}
	}

	private void setView() {
		
		if (!isLoadMore) {
			ImageLoaderUtil.getInstance(mContext).displayImage(icon, app_logo);
			app_name.setText(title);
			app_des.setText(ftitle);
			
			if (price == 0) {
				app_category2.setText("免费");
				app_category2.setBackgroundResource(R.drawable.mark_mianfei);
			}else {
				app_category2.setText("收费");
				app_category2.setBackgroundResource(R.drawable.mark_shoufei);
			}
			
			app_category.setText(type_name);
			
			if (UserInfoManager.isLogin()) {
				if (is_like) {
					iv_collect.setImageResource(R.drawable.collect_2_press);
				}else {
					iv_collect.setImageResource(R.drawable.collect_2);
				}
			}else {
				if (sModule.isCared(String.valueOf(id))) {
					iv_collect.setImageResource(R.drawable.collect_2_press);
				}else{
					iv_collect.setImageResource(R.drawable.collect_2);
				}
			}
			
			if (is_digg) {
				iv_like.setImageResource(R.drawable.like_press);
			}else {
				iv_like.setImageResource(R.drawable.like);
			}
			//描述
			if (description == null) {
				description = getResources().getString(R.string.details_des);
			}
			if (description.length() > 50) {
				tv_show.setText(description.substring(0, 50) + "...");
				iv_more.setVisibility(View.VISIBLE);
				iv_more.setOnClickListener(this);
			} else {
				iv_more.setVisibility(View.GONE);
			}
			//截图
			if (pic != null && pic.size()>0) {
				TOTAL_COUNT = pic.size();
			}else {
				TOTAL_COUNT = 2;
			}
			addImage();
		}
		
		adapter.setList(comments);
		isLoadMore = false;
	}
	
	private void showDialog(boolean isShow) {
		if (isShow) {
			loading.setVisibility(View.VISIBLE);
		}else {
			loading.setVisibility(View.GONE);
		}
	}
	
	public int getTextWidth(String text) {
		Paint paint = new Paint();
    	Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int width = bounds.left + bounds.width();
		return width;
	}
	
	public int getTextHeight(String text) {
		Paint paint = new Paint();
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		int height = bounds.bottom + bounds.height();
		return height;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private void digg_app(int app_id) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.digg_app(mContext, String.valueOf(app_id), new HttpCallback() {
				@Override
				public void onResult(NetResBean response) {
					if (response.success) {
						Toast.makeText(mContext, "Ding succeed!", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(mContext, "Ding failed!", Toast.LENGTH_SHORT).show();
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
	
	private void like(int app_id) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.like(mContext, String.valueOf(app_id), new HttpCallback() {
				@Override
				public void onResult(NetResBean response) {
					if (response.success) {
						Toast.makeText(mContext, "Collect succeed!", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(mContext, "Collect failed!", Toast.LENGTH_SHORT).show();
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
	
	private void comment(int app_id,String comment,String reply) {
		NetWorkAPI.comment(mContext, String.valueOf(app_id), comment, reply, new HttpCallback() {
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
						CommentBean com = new CommentBean();
						com.setComment(details_edit_et.getText().toString().trim());
						com.setDigg(0);
						com.setId(Integer.valueOf(comment_id));
						com.setIs_digg(0);
						com.setIs_more_comment(0);
						com.setTimes(String.valueOf(System.currentTimeMillis()/1000));
						com.setUser_name(UserInfoManager.getUserName());
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
	
	
	ShareStateListener shareListener = new ShareStateListener() {
		@Override
		public void onUserInfoComplete(SNSUserInfo user) {}
		@Override
		public void shareAction(int action, String info, int platformID) {
			switch (action) {
			case ShareStateListener.SHARING :
				Log.v(TAG, "iniShareManager"+" shareAction : SHARING");
				break;
			case ShareStateListener.SHARE_SUCCESS :
				Log.v(TAG, "iniShareManager"+ " shareAction : SHARE_SUCCESS");
				handler.sendMessage(handler.obtainMessage(SHARED_SUCCEED,platformID));
				break;
			case ShareStateListener.SHARE_FAILED :
				Log.e(TAG, "iniShareManager"+ " shareAction : SHARE_FAILED"+" info :" +info);
				handler.sendMessage(handler.obtainMessage(SHARED_FAILED));
				break;
			case ShareStateListener.SHARE_CANCEL :
//				handler.sendMessage(handler.obtainMessage(SHARED_FAILED));
				Log.v(TAG, "iniShareManager"+" shareAction : SHARE_CANCEL");
				break;
			default:
				break;
			}
		}
	};
	private static final int SHARED_FAILED = 11;
	private static final int SHARED_SUCCEED = 12;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHARED_FAILED:
				Toast.makeText(AppDetailsActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
				windowShare.dismiss();
				break;
			case SHARED_SUCCEED:
				Toast.makeText(ApplicationManager.getGlobalContext(), "分享成功", Toast.LENGTH_SHORT).show();
				NetWorkAPI.comment(getApplicationContext(), String.valueOf(id), String.valueOf(msg.obj),
						new HttpCallback() {
					@Override
					public void onResult(NetResBean response) {
						if (response.success) {
						}
					}
					@Override
					public void onError(String errorMsg) {}
					@Override
					public void onCancel() {}
				});
				break;
			default:
				break;
			}
		};
	};
}
