package com.bccv.strategy.ui.activity;

import java.io.File;

import android.app.Dialog;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.test.PerformanceTestCase;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.api.DownLoadAPI;
import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.common.GlobalConstants;
import com.bccv.strategy.model.VersionBean;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.SNSShareManager;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.sns.SNSShareManager.ShareStateListener;
import com.bccv.strategy.sns.bean.SNSUserInfo;
import com.bccv.strategy.sns.bean.ShareInfo;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.SharePopwindow;
import com.bccv.strategy.utils.FileUtils;
import com.bccv.strategy.utils.PreferenceHelper;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.ApplicationManager;
import com.bccv.strategy.R;

public class SettingActivity extends BaseActivity {

	/***************** view *****************/
	private TextView tv_common_title;
	private ImageView iv_common_left;
	private LinearLayout common_right;
	private LinearLayout common_left;
	private RelativeLayout rl_friends;
	private RelativeLayout rl_score;
	private RelativeLayout rl_about;
	private RelativeLayout rl_feedback;
	private RelativeLayout rl_version;
	private RelativeLayout rl_clear;
	private RelativeLayout rl_logout;
	private BackGroundView setting_bg_view;
	private View setting_shadow;
	private LinearLayout setting_loading;
	private TextView logout_tv;

	private TextView cache_size;
	private int versionCode = -1;
	private SNSShareManager ssManager;
	/**************************************/

	private int[] backgrounds;
	private BroadcastReceiver infoChangeBroadcastReceiver;
	private IntentFilter infoChangeIntentFilter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_setting);
		backgrounds = getIntent().getIntArrayExtra(
				BackGroundView.BACKGROUND_COLOR);
		ssManager = SNSShareManager.getInstance();
		findView();
		setClick();
		initBroadcastRevicer();
		versionCode = SystemUtil.getVersionCode(mContext);
	}

	private void setClick() {
		common_left.setOnClickListener(this);
		common_right.setOnClickListener(this);
		rl_friends.setOnClickListener(this);
		rl_score.setOnClickListener(this);
		rl_about.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_version.setOnClickListener(this);
		rl_clear.setOnClickListener(this);
		rl_logout.setOnClickListener(this);
		
		if(!UserInfoManager.isLogin()){
			rl_logout.setClickable(false);
			logout_tv.setTextColor(0xffD3D1D2);
		}
		
	}

	private void findView() {
		tv_common_title = (TextView) findViewById(R.id.common_title_tv);
		iv_common_left = (ImageView) findViewById(R.id.iv_common_left);
		common_right = (LinearLayout) findViewById(R.id.common_title_right_btn);
		common_left = (LinearLayout) findViewById(R.id.common_title_menu_btn);
		rl_friends = (RelativeLayout) findViewById(R.id.rl_friends);
		rl_score = (RelativeLayout) findViewById(R.id.rl_score);
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
		rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
		rl_version = (RelativeLayout) findViewById(R.id.rl_version);
		rl_clear = (RelativeLayout) findViewById(R.id.rl_clear);
		rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
		logout_tv = (TextView) rl_logout.findViewById(R.id.logout_tv);
		setting_bg_view = (BackGroundView) findViewById(R.id.setting_bg_view);
		cache_size = (TextView) findViewById(R.id.cache_size);
		setting_shadow = findViewById(R.id.setting_shadow);
		setting_loading = (LinearLayout) findViewById(R.id.setting_loading);
		shareWindow = new SharePopwindow(SettingActivity.this, setting_shadow);

		setting_bg_view.setGradient(backgrounds[0], backgrounds[1]);

		// iv_common_left.setBackgroundResource(R.drawable.title_menu_left_selector);
		iv_common_left
				.setBackgroundResource(R.drawable.title_menu_left_down_selector);
		tv_common_title.setText("设置");

		getCacheSize();
	}

	private void initBroadcastRevicer(){
		//TODO 设置广播监听
		if(infoChangeIntentFilter == null){
			infoChangeBroadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					String action = intent.getAction();
					if(GlobalConstants.USER_INFO_CHANGE_BROADCAST.equals(action)){
						if(intent.getIntExtra(GlobalConstants.USER_INFO_CHANGE_TYPE, 0) 
								== GlobalConstants.LOGIN_SUCCESS){
							//TODO  更改退出按钮状态
							rl_logout.setClickable(true);
							logout_tv.setTextColor(0xffffffff);
						}
					}
				}
			};
			infoChangeIntentFilter = new IntentFilter(GlobalConstants.USER_INFO_CHANGE_BROADCAST);
			registerReceiver(infoChangeBroadcastReceiver, infoChangeIntentFilter);
		}
		
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
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_menu_btn:
			onBackPressed();
			this.finish();
			break;
		case R.id.common_title_right_btn:
			Intent appIntent = new Intent(SettingActivity.this,
					AppReleaseActivity.class);
			appIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
			startActivity(appIntent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;
		case R.id.rl_friends:
			if (!shareWindow.isShowing()) {
				ShareInfo info = new ShareInfo();
				info.title = getResources().getString(R.string.app_name);
				info.text = "同学~推荐一个好玩的应用给你,看看吧~o(∩_∩)o ";
				info.url = PreferenceHelper.getString("an_download", "");
				info.imagePath = GlobalConstants.launcher_path ;
//				info.imagePath = "file:///android_asset/ic_launcher.png" ; 
				info.titleUrl = "http://appsapi.bccv.com/res/bangyangApp.apk";
				ssManager.setShareStateListener(shareListener);
				shareWindow.show(setting_shadow, info);
			}
			break;
		case R.id.rl_score:
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.rl_about:
			Intent aboutIntent = new Intent(this, AboutUsActivity.class);
			aboutIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					this.setting_bg_view.getGradientColor());
			startActivity(aboutIntent);
			break;
		case R.id.rl_feedback:

			Intent feedbackIntent = new Intent(this, ReportMsgActivity.class);
			feedbackIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					this.setting_bg_view.getGradientColor());
			feedbackIntent.putExtra(ReportMsgActivity.REPORT_MSG_CATE,
					ReportMsgActivity.FEEDBACK_CATE);
			startActivity(feedbackIntent);

			break;
		case R.id.rl_version:
			setting_loading.setVisibility(View.VISIBLE);
			getVersion();
			break;
		case R.id.rl_clear:
			boolean success = FileUtils.deleteDirectory(cacheDir);
			if (success) {
				getCacheSize();
				showShortToast("已清理缓存");
				cache_size.setText("0KB");
			} else {
				showShortToast("已清理缓存");
				cache_size.setText("0KB");
			}
			break;
		case R.id.rl_logout:

			if (UserInfoManager.isLogin()) {
				UserInfoManager.logOut();
				Intent logoutIntent = new Intent();
				logoutIntent.setAction(GlobalConstants.USER_INFO_CHANGE_BROADCAST);
				logoutIntent.putExtra(GlobalConstants.USER_INFO_CHANGE_TYPE,GlobalConstants.LOGOUT_SUCCESS);
				sendBroadcast(logoutIntent);
				rl_logout.setClickable(false);
				logout_tv.setTextColor(0xffD3D1D2);
				showShortToast("已退出");
			}

			break;
		default:
			break;
		}
	}

	File cacheDir = null;
	private SharePopwindow shareWindow;

	private void getCacheSize() {
		cacheDir = com.bccv.strategy.utils.ImageLoaderUtil
				.getCacheDir(getApplicationContext());
		if (cacheDir == null) {
			showShortToast("未发现缓存目录");
		} else {
			long dirSize = FileUtils.getDirSize(cacheDir);
			if (dirSize == 0) {
				cache_size.setText("暂无缓存");
				return;
			}
			String fileSize = FileUtils.formatFileSize(dirSize);
			cache_size.setText(fileSize);
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.in_from_none, R.anim.out_to_bottom);
	}

	@Override
	public void onBackPressed() {
		if (shareWindow.isShowing()) {
			shareWindow.dismiss();
			return;
		}
		super.onBackPressed();
	}

	private void getVersion() {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.getVersion(mContext, new HttpCallback() {

				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof VersionBean) {
						VersionBean bean = (VersionBean) response;
						System.out.println("code : " + bean.getCode()
								+ " download : " + bean.getDownload());
						getVersionData(bean);
						showShortToast("succeed");
					} else {
						showShortToast("failed");
					}
					setting_loading.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onError(String errorMsg) {
					setting_loading.setVisibility(View.INVISIBLE);
				}

				@Override
				public void onCancel() {
					setting_loading.setVisibility(View.INVISIBLE);
				}
			});
		}else {
			setting_loading.setVisibility(View.INVISIBLE);
		}
	}
	
	
	protected void getVersionData(VersionBean data) {
		if (data != null) {
			final String downloadUrl = data.getDownload();
			final int new_version = data.getCode();
			String version_name = data.getName();
//			version_desS = data.getApp_des();
			if (versionCode > 0 && new_version > 0) {
				if (versionCode < new_version) {
					//此处直接new一个Dialog对象出来，在实例化的时候传入主题
					final Dialog dialog = new Dialog(this, R.style.MyDialog);
					//设置它的ContentView
					View view = LayoutInflater.from(this).inflate(R.layout.custome_dialog, null);
					TextView des = (TextView) view.findViewById(R.id.description);
					TextView tv = (TextView) view.findViewById(R.id.dialog_message);
					TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
					TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
					dialog_enter.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.cancel();
							if (downloadUrl != null && !downloadUrl.equals("")) {
								DownLoadAPI.downLoadApk(downloadUrl, getResources().getString(R.string.app_name), 
										getPackageName(), new_version, true, true);
								showShortToast("开始下载");
							}else{
								showShortToast("下载路径出错");
							}
						}
					});
					dialog_cancle.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.cancel();
						}
					});
					tv.setText("助手提示: 新版本: "+version_name);
					des.setText(Html.fromHtml("这里显示升级信息"));
					view.setMinimumWidth(600);
//						view.setMinimumHeight(400);
					dialog.setContentView(view);
					dialog.show();
				}else{
					showShortToast("当前是最新版本");
				}
			}else{
				showShortToast("当前是最新版本");
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		if(infoChangeIntentFilter!=null){
			unregisterReceiver(infoChangeBroadcastReceiver);
			infoChangeIntentFilter=null;
		}
		super.onDestroy();
	}
	
	private static final int SHARED_FAILED = 11;
	private static final int SHARED_SUCCEED = 12;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHARED_FAILED:
				Toast.makeText(SettingActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
				shareWindow.dismiss();
				break;
			case SHARED_SUCCEED:
				Toast.makeText(ApplicationManager.getGlobalContext(), "分享成功", Toast.LENGTH_SHORT).show();
//				NetWorkAPI.comment(getApplicationContext(), String.valueOf(id), String.valueOf(msg.obj),
//						new HttpCallback() {
//					@Override
//					public void onResult(NetResBean response) {
//						if (response.success) {
//						}
//					}
//					@Override
//					public void onError(String errorMsg) {}
//					@Override
//					public void onCancel() {}
//				});
				break;
			default:
				break;
			}
		};
	};
}
