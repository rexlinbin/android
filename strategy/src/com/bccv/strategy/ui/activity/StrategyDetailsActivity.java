package com.bccv.strategy.ui.activity;

import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.R;
import com.bccv.strategy.api.DownLoadAPI;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.db.StorageModule;
import com.bccv.strategy.model.AppDetailsBean;
import com.bccv.strategy.model.CommentBean;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.SNSShareManager;
import com.bccv.strategy.sns.SNSShareManager.ShareStateListener;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.sns.bean.SNSUserInfo;
import com.bccv.strategy.sns.bean.ShareInfo;
import com.bccv.strategy.ui.utils.WebViewSetting;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.DownloadPopwindow;
import com.bccv.strategy.ui.view.SharePopwindow;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.StringUtil;
import com.bccv.strategy.utils.SystemUtil;

public class StrategyDetailsActivity extends BaseActivity {

	public static final String APP_ID = "APP_ID";
	public static final String RAID_ID = "RAID_ID";
	public static final String GAME_ID = "GAME_ID";

	private BackGroundView background_view;
	private View loadingView;
	private View bg_gray;
	private ImageView app_logo;
	private TextView app_name, app_time, comment_num;
	private ImageView iv_back, iv_down, iv_share, iv_collect, iv_like;
	private WebView mWebView;
	private View comment_btn, comment_rl;
	private ScrollView strategy_details_sv;
	private LinearLayout strategy_details_ll;
	private View strategy_details_head;
	private RelativeLayout app_details_name;

	private int[] background;

	private DownloadPopwindow downloadWindow; // 下载的提示框
	private SharePopwindow windowShare; // 分享的提示框
	private SNSShareManager ssManager;

	private String raid_id = "2", game_id = "2";
	private List<CommentBean> comments;
	private String icon;
//	private int game_id;
	private String title;
	private long news_ctime;
	private boolean is_digg;
	private boolean is_focus;
	private String news_title;
	private String news_ftitle;
	private String news_titlepic;
	private String news_comment;
	private String url;
	private String game_an_down;
	private StorageModule sModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//		    Window window = getWindow();
//		    window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//		    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//		}
		setContentView(R.layout.activity_strategy_details);
		raid_id = getIntent().getStringExtra(RAID_ID);
		game_id = getIntent().getStringExtra(GAME_ID);
		ssManager = SNSShareManager.getInstance();
		if (TextUtils.isEmpty(raid_id) || TextUtils.isEmpty(game_id)) {
			this.finish();
		}
		sModule = StorageModule.getInstance(); 
		initView();
		initWindow();
		getData(raid_id, game_id);
	}
	
	private void initView() {
		// iv_back,iv_down,iv_share,iv_collect,iv_like;
		// app_name,app_time;
		// app_logo;
		background_view = (BackGroundView) findViewById(R.id.background_view);
		loadingView = findViewById(R.id.strategy_details_loading);
		strategy_details_ll = (LinearLayout) findViewById(R.id.strategy_details_ll);
		strategy_details_head = findViewById(R.id.strategy_details_head);
		bg_gray = findViewById(R.id.bg_gray);
		Intent intent = getIntent();
		background = intent.getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);

		background_view.setGradient(background[0], background[1]);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_down = (ImageView) findViewById(R.id.iv_down);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_collect = (ImageView) findViewById(R.id.iv_collect);
		iv_like = (ImageView) findViewById(R.id.iv_like);

		// iv_down.setVisibility(View.GONE);
		app_details_name = (RelativeLayout) findViewById(R.id.app_details_name);
		app_logo = (ImageView) findViewById(R.id.app_logo);
		app_name = (TextView) findViewById(R.id.app_name);
		app_time = (TextView) findViewById(R.id.app_time);
		comment_num = (TextView) findViewById(R.id.comment_num);
		comment_btn = findViewById(R.id.comment_btn);
		comment_rl = findViewById(R.id.comment_rl);
		strategy_details_sv = (ScrollView) findViewById(R.id.strategy_details_sv);

		strategy_details_sv
				.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);

		app_details_name.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_down.setOnClickListener(this);
		iv_collect.setOnClickListener(this);
		iv_like.setOnClickListener(this);
		comment_btn.setOnClickListener(this);

		mWebView = new WebView(mContext);
		WebViewSetting.setting(mWebView, "AppDetailsActivity", loadingView,this);
		mWebView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		LayoutParams.WRAP_CONTENT));
		strategy_details_ll.addView(mWebView);
		// mWebView.loadUrl("http://www.baidu.com");

	}

	private void initWindow() {
		downloadWindow = new DownloadPopwindow(this, bg_gray);
		windowShare = new SharePopwindow(this, bg_gray);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO 刷新数据
		raid_id = intent.getStringExtra(RAID_ID);
		game_id = intent.getStringExtra(GAME_ID);
		if (TextUtils.isEmpty(raid_id) || TextUtils.isEmpty(game_id)) {
			this.finish();
		}
		System.out.println("raid_id : " + raid_id);
		getData(raid_id, game_id);
		if (mWebView != null) {
			strategy_details_ll.removeViewAt(1);
			mWebView = new WebView(mContext);
			WebViewSetting.setting(mWebView, "AppDetailsActivity", loadingView,this);
			mWebView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			strategy_details_ll.addView(mWebView);
		}
	}

	private static final int SHARED_FAILED = 11;
	private static final int SHARED_SUCCEED = 12;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHARED_FAILED:
				showShortToast("分享失败");
				if (windowShare != null && windowShare.isShowing()) {
					windowShare.dismiss();
				}
				break;
			case SHARED_SUCCEED:
				showShortToast("分享成功");
				if (windowShare != null && windowShare.isShowing()) {
					windowShare.dismiss();
				}
//				 NetWorkAPI.comment(getApplicationContext(),
//				 String.valueOf(id), String.valueOf(msg.obj),
//				 new HttpCallback() {
//				 @Override
//				 public void onResult(NetResBean response) {
//				 if (response.success) {
//				 }
//				 }
//				 @Override
//				 public void onError(String errorMsg) {}
//				 @Override
//				 public void onCancel() {}
//				 });
				break;
			default:
				break;
			}
		};
	};

	ShareStateListener shareListener = new ShareStateListener() {
		@Override
		public void onUserInfoComplete(SNSUserInfo user) {
		}

		@Override
		public void shareAction(int action, String info, int platformID) {
			switch (action) {
			case ShareStateListener.SHARING:
				Log.v(TAG, "iniShareManager" + " shareAction : SHARING");
				break;
			case ShareStateListener.SHARE_SUCCESS:
				Log.v(TAG, "iniShareManager" + " shareAction : SHARE_SUCCESS");
				handler.sendMessage(handler.obtainMessage(SHARED_SUCCEED,
						platformID));
				break;
			case ShareStateListener.SHARE_FAILED:
				Log.e(TAG, "iniShareManager" + " shareAction : SHARE_FAILED"
						+ " info :" + info);
				handler.sendMessage(handler.obtainMessage(SHARED_FAILED));
				break;
			case ShareStateListener.SHARE_CANCEL:
				Log.v(TAG, "iniShareManager" + " shareAction : SHARE_CANCEL");
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_btn:
			Intent webCommentIntent = new Intent(this, WebCommentActivity.class);
			webCommentIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					background);
			webCommentIntent.putExtra(WebCommentActivity.APP_ID, raid_id);
			webCommentIntent.putExtra(WebCommentActivity.RAID_TITLE, title);
			startActivity(webCommentIntent);
			break;
		case R.id.iv_down:
			// TODO 下载"http://appsapi.bccv.com/res/bangyangApp.apk"
			if (TextUtils.isEmpty(game_an_down)) {
				Toast.makeText(mContext, "该游戏暂无下载资源", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				DownLoadAPI.downLoadApk(game_an_down, title, "", 0, true, true);
			}
			// if (downloadWindow != null && !downloadWindow.isShowing()) {
			// downloadWindow.show(strategy_details_sv,
			// downloads,versionCode,pkgName,title);
			// }
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_share:
			// TODO share
			 ShareInfo sInfo = new ShareInfo();
			 sInfo.text = news_ftitle;
			 sInfo.title = title;
			 sInfo.imageUrl = icon;
			 sInfo.titleUrl = news_titlepic;
			 sInfo.url = url;
			 ssManager.setShareStateListener(shareListener);
			 windowShare.show(bg_gray, sInfo);

			break;
		case R.id.iv_collect:
			if (UserInfoManager.isLogin()) {
				if (!is_focus) {
					showDialog(true);
					focus_r();
				}else {
					Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();
				}
			}else {
				if (!sModule.isCared(String.valueOf(raid_id))) {
					iv_collect.setImageResource(R.drawable.collect_2_press);
					is_focus = true;
					sModule.addLocalCare(String.valueOf(raid_id));
				}else{
					Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.iv_like:
//			if (!UserInfoManager.isLogin()) {
//				Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
//				return ;
//			}
			if (UserInfoManager.isLogin()) {
				if (!is_digg) {
					showDialog(true);
					digg_r();
				}else {
					Toast.makeText(mContext, "已顶", Toast.LENGTH_SHORT).show();
				}
			}else {
				if (!sModule.isDigg(String.valueOf(raid_id))) {
					sModule.addLocalDigg(String.valueOf(raid_id));
					iv_like.setImageResource(R.drawable.like_press);
					Toast.makeText(mContext, "已顶", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(mContext, "已顶", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.app_details_name:
			Intent strategyListIntent = new Intent(this,StrategyListActivity.class);
			strategyListIntent.putExtra(StrategyListActivity.GAME_ID, game_id);
			strategyListIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					background);
			startActivity(strategyListIntent);
			break;
		default:
			break;
		}
	}

	private void digg_r() {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.digg_r(mContext, raid_id, new HttpCallback() {

				@Override
				public void onResult(NetResBean response) {
					if (response.success) {
						is_digg = true;
						iv_like.setImageResource(R.drawable.like_press);
						Toast.makeText(mContext, "已顶", Toast.LENGTH_SHORT).show();
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
		showDialog(false);
	}

	private void focus_r() {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.focus_r(mContext, raid_id, new HttpCallback() {

				@Override
				public void onResult(NetResBean response) {
					if (response.success) {
						is_focus = true;
						iv_collect.setImageResource(R.drawable.collect_2_press);
						if (!sModule.isCared(String.valueOf(raid_id))) {
							sModule.addLocalCare(String.valueOf(raid_id));
						}
						Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();
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
		showDialog(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mWebView != null && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			}
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		mWebView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mWebView.onPause();
		super.onPause();
	}

	private void getData(String raid_id, String game_id) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			showDialog(true);
			NetWorkAPI.getAppDetails(mContext, raid_id, game_id,
					new HttpCallback() {

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
			comments = bean.getComment();
			icon = bean.getGame_icon();
//			game_id = bean.getGame_id();
			title = bean.getGame_title();
			news_ctime = bean.getNews_ctime();
			is_digg = bean.isIs_raid_digg();
			is_focus = bean.isIs_raid_focus();
			news_comment = bean.getNews_comment();
			news_title = bean.getNews_title();
			news_ftitle = bean.getNews_ftitle();
			news_titlepic = bean.getNews_titlepic();
			url = bean.getUrl();
			game_an_down = bean.getGame_an_down();
		}
		setView();
	}

	private void setView() {
		if (TextUtils.isEmpty(game_an_down)) {
			iv_down.setVisibility(View.GONE);
		}
		mWebView.loadUrl(url);
		ImageLoaderUtil.getInstance(mContext).displayImage(icon, app_logo,
				ImageLoaderUtil.getAppIconImageOptions());
		app_name.setText(title);
		app_time.setText("发布于："
				+ StringUtil.formatLongDate(news_ctime * 1000l, "yyyy-mm-dd"));
		comment_num.setText((news_comment != null ? news_comment : "0"));
		
		if(UserInfoManager.isLogin()){
			if (is_focus) {
				iv_collect.setImageResource(R.drawable.collect_2_press);
			} else {
				iv_collect.setImageResource(R.drawable.collect_2);
			}
		}else {
			if (sModule.isCared(String.valueOf(raid_id))) {
				iv_collect.setImageResource(R.drawable.collect_2_press);
			}else{
				iv_collect.setImageResource(R.drawable.collect_2);
			}
		}
		
		if (UserInfoManager.isLogin()) {
			if (is_digg) {
				iv_like.setImageResource(R.drawable.like_press);
			} else {
				iv_like.setImageResource(R.drawable.like);
			}
		}else {
			if (sModule.isDigg(String.valueOf(raid_id))) {
//				sModule.addLocalDigg(String.valueOf(raid_id));
				iv_like.setImageResource(R.drawable.like_press);
//				Toast.makeText(mContext, "已顶", Toast.LENGTH_SHORT).show();
			}else{
				iv_like.setImageResource(R.drawable.like);
			}
		}
	}

	private void showDialog(boolean isShow) {
		if (isShow) {
			loadingView.setVisibility(View.VISIBLE);
		} else {
			loadingView.setVisibility(View.GONE);
		}
	}
}
