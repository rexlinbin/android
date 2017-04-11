package com.bccv.meitu.ui.activity;

import io.rong.imkit.RongIM;

import java.io.File;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.api.DownLoadAPI;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.model.GetuserinfoResBean;
import com.bccv.meitu.model.ReportInfoBean;
import com.bccv.meitu.model.UserInfo;
import com.bccv.meitu.model.Version;
import com.bccv.meitu.model.VersionInfo;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.utils.FileUtils;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.PreferenceHelper;
import com.bccv.meitu.utils.RCUtil;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.utils.RCUtil.RCConnectCallback;
import com.bccv.meitu.view.PullToZoomScrollView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class UserActivity extends BaseActivity {

	/************* activity_user ************/
	private PullToZoomScrollView user_scroll_view;
	private RelativeLayout waitting_layout;
	/************* head view **************/
	private ImageView pro_back;
	private ImageView iv_user_icon;
	/************* zoom view **************/
	private ImageView user_iv_zoom;
	/************* content view ************/
	private TextView tv_user_name;
	private TextView tv_user_score;
	private TextView tv_user_money;
	private TextView chat_list;
	private ImageView chat_num;
	private TextView get_score;
	private TextView my_album;
	private TextView my_attention;
	private TextView my_exclusive;
	private TextView my_favorite;
	private TextView clear_cache;
	private TextView cache_num;
	private TextView check_version;
	private TextView more_platform;
	private TextView upload_pic;
	private ImageView iv_check_version;
	private TextView comment_score;
	private TextView user_exit;
	private LinearLayout title_left;
	private LinearLayout title_right;
	private boolean isLogin = false;
	private int versionCode = -1;
	private int sp_versionCode = -1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		versionCode = SystemUtil.getVersionCode(mContext);
		sp_versionCode = PreferenceHelper.getInt(GlobalConstants.VERSION_CODE, -1);
		initView();
//		isLogin = UserInfoManager.isLogin();
//		user_scroll_view.setEnableZoom(true);
//		if (isLogin) {
//			getData();
//		}else{
//			title_left.setVisibility(View.INVISIBLE);
//			title_right.setVisibility(View.INVISIBLE);
//			tv_user_name.setText("未登录");
//		}
		onRefresh();
		getCacheSize();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(RCUtil.getTotalUnreadCount()!=0){
			chat_num.setVisibility(View.VISIBLE);
		}else{
			chat_num.setVisibility(View.GONE);
		}
		registBCReceiver();
//		Logger.e(TAG, "get message", "regist");
	}
	
	protected void onRefresh() {
		isLogin = UserInfoManager.isLogin();
		user_scroll_view.setEnableZoom(true);
		if (isLogin) {
			getData();
		}else{
			title_left.setVisibility(View.INVISIBLE);
			title_right.setVisibility(View.INVISIBLE);
			tv_user_name.setText("未登录");
		}
	}

	private void initView() {
		user_scroll_view = (PullToZoomScrollView) findViewById(R.id.user_scroll_view);
		waitting_layout = (RelativeLayout) findViewById(R.id.waitting_layout);

		pro_back = (ImageView) findViewById(R.id.user_back);
		iv_user_icon = (ImageView) findViewById(R.id.user_icon);

		user_iv_zoom = (ImageView) findViewById(R.id.user_iv_zoom);

		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_user_score = (TextView) findViewById(R.id.tv_user_score);
		tv_user_money = (TextView) findViewById(R.id.tv_user_money);
		chat_list = (TextView) findViewById(R.id.chat_list);
		chat_num = (ImageView) findViewById(R.id.chat_num);
		get_score = (TextView) findViewById(R.id.get_score);
		upload_pic = (TextView) findViewById(R.id.upload_pic);
		my_album = (TextView) findViewById(R.id.my_album);
		my_attention = (TextView) findViewById(R.id.my_attention);
		my_exclusive = (TextView) findViewById(R.id.my_exclusive);
		my_favorite = (TextView) findViewById(R.id.my_favorite);
		clear_cache = (TextView) findViewById(R.id.clear_cache);
		cache_num = (TextView) findViewById(R.id.cache_num);
		check_version = (TextView) findViewById(R.id.check_version);
		more_platform = (TextView) findViewById(R.id.more_platform);
		iv_check_version = (ImageView) findViewById(R.id.iv_check_version);
		comment_score = (TextView) findViewById(R.id.comment_score);
		user_exit = (TextView) findViewById(R.id.user_exit);
		title_left = (LinearLayout) findViewById(R.id.title_left);
		title_right = (LinearLayout) findViewById(R.id.title_right);

		chat_list.setOnClickListener(this);
		pro_back.setOnClickListener(this);
		user_iv_zoom.setOnClickListener(this);
		iv_user_icon.setOnClickListener(this);
		get_score.setOnClickListener(this);
		upload_pic.setOnClickListener(this);
		my_album.setOnClickListener(this);
		my_attention.setOnClickListener(this);
		my_exclusive.setOnClickListener(this);
		my_favorite.setOnClickListener(this);
		clear_cache.setOnClickListener(this);
		check_version.setOnClickListener(this);
		more_platform.setOnClickListener(this);
		comment_score.setOnClickListener(this);
		user_exit.setOnClickListener(this);
		
		if (versionCode > 0 && sp_versionCode > 0) {
			if (sp_versionCode > versionCode) {
				iv_check_version.setVisibility(View.VISIBLE);
			}else{
				iv_check_version.setVisibility(View.GONE);
			}
		}
	}

	private void getData() {
		waitting_layout.setVisibility(View.VISIBLE);
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
			NetWorkAPI.getuserinfo(getApplicationContext(), new HttpCallback() {
				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof GetuserinfoResBean) {
						GetuserinfoResBean data = (GetuserinfoResBean) response;
						getAllData(data);
					} else {
						waitting_layout.setVisibility(View.GONE);
//						showShortToast("failed");
					}
				}
	
				@Override
				public void onError(String errorMsg) {}
	
				@Override
				public void onCancel() {}
			});
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	private void getVersion(){
		waitting_layout.setVisibility(View.VISIBLE);
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
//			NetWorkAPI.getversion(getApplicationContext(), new HttpCallback() {
//				@Override
//				public void onResult(NetResBean response) {
//					waitting_layout.setVisibility(View.GONE);
//					if (response.success && response instanceof Version) {
//						Version data = (Version) response;
//						getVersionData(data);
//					} else {
//						waitting_layout.setVisibility(View.GONE);
////						showShortToast("failed");
//					}
//				}
//	
//				@Override
//				public void onError(String errorMsg) {}
//	
//				@Override
//				public void onCancel() {}
//			});
			NetWorkAPI.report(getApplicationContext(), new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					waitting_layout.setVisibility(View.GONE);
					if (response.success && response instanceof VersionInfo) {
						VersionInfo dataBean = (VersionInfo)response;
						getVersionData(dataBean);
					}else{
						waitting_layout.setVisibility(View.GONE);
						showShortToast("获取版本出错");
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					showShortToast("获取版本出错");
				}
				
				@Override
				public void onCancel() {}
			});
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}

	private String downloadUrl = "";
	private int new_version = -1;
	private String version_name = "";
	private String version_desS = "";
	protected void getVersionData(VersionInfo data) {
		if (data != null) {
			if (data.isHasNewVer()) {
				downloadUrl = data.getApp_url();
				new_version = data.getApp_upver();
				version_name = data.getApp_ver();
				version_desS = data.getApp_des();
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
						tv.setText("悦色提示: 新版本: "+version_name);
						des.setText(Html.fromHtml(version_desS));
						view.setMinimumWidth(600);
//						view.setMinimumHeight(400);
						dialog.setContentView(view);
						dialog.show();
					}else{
						showShortToast("当前是最新版本");
						//此处直接new一个Dialog对象出来，在实例化的时候传入主题
//					final Dialog dialog = new Dialog(this, R.style.MyDialog);
//					//设置它的ContentView
//					View view = LayoutInflater.from(this).inflate(R.layout.custome_dialog, null);
//					TextView tv = (TextView) view.findViewById(R.id.dialog_message);
//					TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
//					TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
//					dialog_enter.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							dialog.cancel();
//						}
//					});
//					dialog_cancle.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							dialog.cancel();
//						}
//					});
//					tv.setText("悦色提示: 新版本: "+version_name);
//					view.setMinimumWidth(600);
//					view.setMinimumHeight(400);
//					dialog.setContentView(view);
//					dialog.show();
					}
				}else{
					showShortToast("获取版本失败");
				}
			}else{
				showShortToast("当前是最新版本");
			}
		}
	}

	private String user_icon;
	private String user_name;
	private String binds;
	private int user_score = -1;
	private String user_group;
	private int user_money = -1;
	private int user_id = -1;
	private String backdrop = "";
	/**
	 * 保存用户信息到本地
	 * 
	 * @param data
	 */
	protected void getAllData(GetuserinfoResBean data) {
		user_icon = data.getUser_icon();
		user_name = data.getUser_name();
		binds = data.getBinds();
		user_score = data.getUser_score();
		user_group = data.getUser_group();
		user_money = data.getUser_money();
		user_id = data.getUser_id();
		backdrop = data.getBackdrop();
		UserInfo userInfo = new UserInfo();
		userInfo.setUser_name(user_name);
		userInfo.setUser_id(user_id);
		userInfo.setUser_icon(user_icon);
		userInfo.setUser_binds(binds);
		userInfo.setUser_group(user_group);
		userInfo.setUser_money(user_money);
		userInfo.setUser_score(user_score);
		userInfo.setBackdrop(backdrop);
		UserInfoManager.saveUserInfo(userInfo);
		setViewData();
	}

	/**
	 * 适配控件数据
	 */
	private void setViewData() {
//		if (actResult == 3) {
//			if (backdrop != null && !backdrop.equals("")) {
//				ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(
//						backdrop, user_iv_zoom,
//						R.drawable.user_bg);
//			}
//		}else if (actResult == 1) {
//			if (user_icon != null && !user_icon.equals("")) {
//				ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(
//						user_icon, iv_user_icon,
//						ImageLoaderUtil.getRoundedImageOptions());
//			}
//		}else if (actResult == 2) {
//			if (user_name != null && !user_name.equals("")) {
//				tv_user_name.setText(user_name);
//			}
//		}else{
			if (user_icon != null && !user_icon.equals("")) {
				ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(
						user_icon, iv_user_icon,
						ImageLoaderUtil.getRoundedImageOptions());
			}
			if (backdrop != null && !backdrop.equals("")) {
				ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(
						backdrop, user_iv_zoom,
						R.drawable.user_bg);
			}
			if (user_name != null && !user_name.equals("")) {
				tv_user_name.setText(user_name);
			}
			if (user_score != -1) {
				tv_user_score.setText(String.valueOf(user_score));
			}
			if (user_money != -1) {
				tv_user_money.setText(String.valueOf(user_money));
			}
//		}
		waitting_layout.setVisibility(View.GONE);
	}

	File cacheDir = null;

	private void getCacheSize() {
		cacheDir = ImageLoaderUtil.getCacheDir(getApplicationContext());
		if (cacheDir == null) {
			showShortToast("未发现缓存目录");
		} else {
//			System.out
//					.println("dir : " + cacheDir.getAbsolutePath().toString());
			long dirSize = FileUtils.getDirSize(cacheDir);
			if (dirSize == 0) {
				cache_num.setText("暂无缓存");
				return;
			}
			String fileSize = FileUtils.formatFileSize(dirSize);
			cache_num.setText(fileSize);
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_back:// 返回
			onBackPressed();
			break;
		case R.id.user_iv_zoom:
		case R.id.user_icon:// 头像
			if (isLogin) {
				Intent intent = new Intent(UserActivity.this,
						UserIconActivity.class);
				this.startActivityForResult(intent, 0);
			}else{
				Intent intent = new Intent(UserActivity.this,
						LoginActivity.class);
				this.startActivityForResult(intent,0);
			}
			break;
		case R.id.get_score:// 获取积分
			Intent scoreIntent = new Intent(UserActivity.this,
					ScoreActivity.class);
			scoreIntent.putExtra("title", "如何获取积分");
			scoreIntent.putExtra("url", GlobalConstants.TASK_URL+UserInfoManager.getUserId());
			this.startActivity(scoreIntent);
			break;
		case R.id.upload_pic:
			if (isLogin) {
				Intent upLoadIntent = new Intent(this,UploadActivity.class);
				startActivity(upLoadIntent);
			}else{
				Intent intent = new Intent(UserActivity.this,
						LoginActivity.class);
				this.startActivityForResult(intent,0);
			}
			break;
		case R.id.my_album:
			if (isLogin) {
				Intent relaIntent = new Intent(UserActivity.this,
						PersonalRelatedActivity.class);
				relaIntent.putExtra(PersonalRelatedActivity.TYPE_KEY,
						PersonalRelatedActivity.PSERSONAL);
				this.startActivity(relaIntent);
			}else{
				Intent intent = new Intent(UserActivity.this,
						LoginActivity.class);
				this.startActivityForResult(intent,0);
			}
			break;
		case R.id.my_attention:// 我的关注
//			if (isLogin) {
				Intent relaIntent0 = new Intent(UserActivity.this,PersonalAttentionActivity.class);
				this.startActivity(relaIntent0);
//			}else{
//				Intent intent = new Intent(UserActivity.this,
//						LoginActivity.class);
//				this.startActivityForResult(intent,0);
//			}
			break;
		case R.id.my_exclusive:// 我的专属
			if (isLogin) {
				Intent relaIntent = new Intent(UserActivity.this,
						PersonalRelatedActivity.class);
				relaIntent.putExtra(PersonalRelatedActivity.TYPE_KEY,
						PersonalRelatedActivity.EXCLUSIVE);
				this.startActivity(relaIntent);
			}else{
				Intent intent = new Intent(UserActivity.this,
						LoginActivity.class);
				this.startActivityForResult(intent,0);
			}
			break;
		case R.id.my_favorite:// 我的喜欢
//			if (isLogin) {
				Intent relaIntent2 = new Intent(UserActivity.this,
						PersonalRelatedActivity.class);
				relaIntent2.putExtra(PersonalRelatedActivity.TYPE_KEY,
						PersonalRelatedActivity.FAVORITE);
				this.startActivity(relaIntent2);
//			}else{
//				Intent intent = new Intent(UserActivity.this,
//						LoginActivity.class);
//				this.startActivityForResult(intent,0);
//			}
			break;
		case R.id.clear_cache:// 清理缓存
			boolean success = FileUtils.deleteDirectory(cacheDir);
			if (success) {
				getCacheSize();
				showShortToast("已清理缓存");
				cache_num.setText("0KB");
			} else {
				showShortToast("已清理缓存");
				cache_num.setText("0KB");
			}
       
			break;
		case R.id.more_platform:// 更多平台
			if (isLogin) {
				startActivity(new Intent(this, BindsActivity.class));
			}else{
				Intent intent = new Intent(UserActivity.this,
						LoginActivity.class);
				this.startActivityForResult(intent,0);
			}
			break;
		case R.id.chat_list:
			if (isLogin) {
				if (chat_num.isShown()) {
					chat_num.setVisibility(View.GONE);
				}
				RongIM im = RongIM.getInstance();
				if (im == null) {
					RCUtil.connect(UserInfoManager.getUserToken(), new RCConnectCallback() {
						@Override
						public void onSuccess(String userId) {
							if (RongIM.getInstance() != null) {
								Intent iiIntent = new Intent(UserActivity.this,ActionBarAddImageButtonFragmentActivity.class);
								startActivity(iiIntent);
							}
						}
						@Override
						public void onError(String msg, int errorCode) {
							showShortToast("网络连接失败");
						}
					});
				}else{
					Intent iiIntent = new Intent(UserActivity.this,ActionBarAddImageButtonFragmentActivity.class);
					startActivity(iiIntent);
				}
			}else{
				Intent intent = new Intent(UserActivity.this,LoginActivity.class);
				this.startActivityForResult(intent,0);
			}
			break;
		case R.id.check_version:// 检查版本
			getVersion();
			break;
		case R.id.comment_score:
			try {
				Uri uri = Uri.parse("market://details?id="+getPackageName());  
				Intent intent = new Intent(Intent.ACTION_VIEW,uri);  
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				startActivity(intent);
			} catch (Exception e) {
				showShortToast("手机未安装应用市场");
			}
			break;
		case R.id.user_exit:// 退出登录
			if (isLogin) {
				//此处直接new一个Dialog对象出来，在实例化的时候传入主题
				final Dialog dialog = new Dialog(this, R.style.MyDialog);
				//设置它的ContentView
				View view = LayoutInflater.from(this).inflate(R.layout.custome_dialog_quit, null);
				TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
				TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
				dialog_enter.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
						UserInfoManager.logOut();
						UserActivity.this.finish();
					}
				});
				dialog_cancle.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.cancel();
					}
				});
				view.setMinimumWidth(600);
//				view.setMinimumHeight(400);
				dialog.setContentView(view);
				dialog.show();
			}else{
				showShortToast("您还未登录哦!");
			}
			break;
		default:
			break;
		}
	}
	private int actResult = 0;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		 System.out.println("request : "+requestCode
//		 +"\r\n"+"result : "+resultCode+"data  :"+data.getExtras().getString("result"));
		if (resultCode == 1 || resultCode == 2 || resultCode == 3) {
			actResult = resultCode;
			waitting_layout.setVisibility(View.VISIBLE);
			getData();
		}else if (resultCode == 101) {
			onRefresh();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private IntentFilter rcBrocastIntentFilter = null;
	BroadcastReceiver rcBrocastReceiver = null;
	
	private void registBCReceiver() {
		Logger.e(TAG, "get message", "receiver   too");
		if(rcBrocastIntentFilter==null){
			rcBrocastIntentFilter = new IntentFilter();
			rcBrocastIntentFilter.addAction(RCUtil.RECEIVE_NOREAD_MESSAGE);
		}
		
		if (rcBrocastReceiver == null) {
			rcBrocastReceiver = new BroadcastReceiver(){
				@Override
				public void onReceive(Context context, Intent intent) {
					
					if(RCUtil.RECEIVE_NOREAD_MESSAGE.equals(intent.getAction())){
						//TODO 更新图标
//						Logger.e(TAG, "get message", "dian");
						chat_num.setVisibility(View.VISIBLE);
					}
				}
			};
			Logger.e(TAG, "get message", "receiver");
			mContext.registerReceiver(rcBrocastReceiver, rcBrocastIntentFilter);
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		Logger.e(TAG, "get message", "destroy");
		if(rcBrocastReceiver!=null){
			mContext.unregisterReceiver(rcBrocastReceiver);
			rcBrocastReceiver = null;
			rcBrocastIntentFilter = null;
		}
	};
}
