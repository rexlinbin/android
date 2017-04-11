package com.bccv.zhuiyingzhihanju.activity;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.FeedbackApi;
import com.bccv.zhuiyingzhihanju.api.LoadingApi;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.model.AppInfo;
import com.utils.model.UpdateInfo;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.DataCleanManager;
import com.utils.tools.FileUtils;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.Statistics;
import com.utils.updatedownload.DownLoadAPI;
import com.utils.updatedownload.SystemUtil;
import com.utils.views.FeedBackDialog;
import com.utils.views.FeedBackDialog.FeedDialogcallback;
import com.utils.views.ShareDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SetActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout rl_logout;
	private RelativeLayout rl_Down;
	private RelativeLayout rl_Change;
	private RelativeLayout rl_feedback;
	private RelativeLayout rl_version;
	private RelativeLayout rl_clear;
	// private RelativeLayout rl_tui;
	
	private RelativeLayout rl_friends;
	private TextView cache_size;
	private ToggleButton wifiBtn, tuiBtn;
	ShareDialog shreDia;
	private Context context;
	FeedBackDialog FDialog;
	int RESULT;
	TextView cunText;

	Boolean isSD = false;
	private LinearLayout li;
	String request;
	private RelativeLayout rl_wifi;
	private RelativeLayout rl_tui;
	ImageView set_newdot;
	
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "SetActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "SetActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
tcStart();
		setContentView(R.layout.activity_set);
		FileUtils.init(SetActivity.this);
		isSD = AppConfig.getisSD();
		TextView text = (TextView) findViewById(R.id.titleName_textView);
		text.setVisibility(View.VISIBLE);
		text.setText("设置");
		li = (LinearLayout) findViewById(R.id.title);
		li.setBackgroundColor(getResources().getColor(R.color.white));
		TextView isNewesttext = (TextView) findViewById(R.id.rl_isNewest);
		set_newdot=(ImageView) findViewById(R.id.set_newdot);
//		String code = Statistics.getPackageInfo(getApplicationContext()).versionCode + "";
		String upver = Statistics.getPackageInfo(getApplicationContext()).versionName + "";
		
		if (GlobalParams.isNewest) {
		
			isNewesttext.setText("版本:V "+upver);
			set_newdot.setVisibility(View.GONE);	
			
			
		} else {
			isNewesttext.setText("版本:V "+upver);

			set_newdot.setVisibility(View.VISIBLE);
		}

		context = getApplicationContext();

		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		rl_wifi = (RelativeLayout) findViewById(R.id.rl_wifi);
		cunText = (TextView) findViewById(R.id.set_DownText);
		rl_logout = (RelativeLayout) findViewById(R.id.rl_logout);
		rl_Down = (RelativeLayout) findViewById(R.id.rl_Down);
		rl_Change = (RelativeLayout) findViewById(R.id.rl_Change);
		rl_feedback = (RelativeLayout) findViewById(R.id.rl_feedback);
		rl_version = (RelativeLayout) findViewById(R.id.rl_version);
		rl_clear = (RelativeLayout) findViewById(R.id.rl_clear);
	 rl_tui = (RelativeLayout) findViewById(R.id.rl_tuisong);
		rl_friends = (RelativeLayout) findViewById(R.id.rl_friends);
		cache_size = (TextView) findViewById(R.id.cache_size);
		rl_logout.setOnClickListener(this);
		rl_Down.setOnClickListener(this);

		rl_Change.setOnClickListener(this);
		rl_feedback.setOnClickListener(this);
		rl_version.setOnClickListener(this);
		rl_clear.setOnClickListener(this);
		rl_tui.setOnClickListener(this);
		rl_wifi.setOnClickListener(this);
		rl_friends.setOnClickListener(this);
		/// storage/sdcard0/Android/data/com.bccv.zhuiying/cache/uil-images/1318520130
		if (GlobalParams.hasLogin) {
			rl_logout.setVisibility(View.VISIBLE);
		}

		try {

			cache_size.setText(DataCleanManager.getCacheSize(context.getExternalCacheDir()));
			if (FileUtils.getSysTotalSize()!=0) {
				if (!isSD) {
					cunText.setText("手机存储 " + "剩余" + FileUtils.formatFileSize(FileUtils.getSDAvailableSize())
					+ "/共" + FileUtils.formatFileSize(FileUtils.getSDTotalSize()));
				} else {
					cunText.setText("SD卡 " + "剩余" + FileUtils.formatFileSize(FileUtils.getSystemAvailableSize())
							+ "/共" + FileUtils.formatFileSize(FileUtils.getSysTotalSize()));
				}
			} else {
				cunText.setText("手机存储 "+ "剩余" + FileUtils.formatFileSize(FileUtils.getSDAvailableSize())
				+ "/共" + FileUtils.formatFileSize(FileUtils.getSDTotalSize()));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cunText.setText("手机存储 "+ "剩余" + FileUtils.formatFileSize(FileUtils.getAvailableSize(GlobalParams.downloadPath))
			+ "/共" + FileUtils.formatFileSize(FileUtils.getTotalSize(GlobalParams.downloadPath)));
		}
	
		

		 wifiBtn = (ToggleButton) findViewById(R.id.set_wifi);
		 tuiBtn = (ToggleButton) findViewById(R.id.set_tui);
		
		 wifiBtn.setChecked(GlobalParams.isWifi);
		 wifiBtn.setBackgroundResource(GlobalParams.isWifi ? R.drawable.set_open : R.drawable.set_close);
		 wifiBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		 @Override
		 public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		 // TODO Auto-generated method stub
		 
		 if (!GlobalParams.isWifi) {
			showIsWifiDownloadDialog(SetActivity.this);
		}else{
			wifiBtn.setChecked(arg1);
			 wifiBtn.setBackgroundResource(arg1 ? R.drawable.set_open :
			 R.drawable.set_close);
			GlobalParams.isWifi = !GlobalParams.isWifi;
			 AppConfig.setWifi(GlobalParams.isWifi);
		}
		 
		 }
		 });
		
		 tuiBtn.setChecked(GlobalParams.isTui);
		 tuiBtn.setBackgroundResource(GlobalParams.isTui ? R.drawable.set_open
		 : R.drawable.set_close);
		 tuiBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		 @Override
		 public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		 // TODO Auto-generated method stub
		 tuiBtn.setChecked(arg1);
		 tuiBtn.setBackgroundResource(arg1 ? R.drawable.set_open :
		 R.drawable.set_close);
		 if (arg1) {
		 // 开启
		 PushManager.getInstance().turnOnPush(SetActivity.this.getApplicationContext());
		
		 } else {
		 // 关闭
		 PushManager.getInstance().turnOffPush(SetActivity.this.getApplicationContext());
		 }
		
		 GlobalParams.isTui = !GlobalParams.isTui;
		 AppConfig.setTui(GlobalParams.isTui);
		 }
		 });
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.rl_clear:

			ImageLoader.getInstance().clearDiscCache();
			ImageLoader.getInstance().clearMemoryCache();
			cache_size.setText("0KB");
			showShortToast("已清理缓存");

			break;
		case R.id.rl_version:
			if (!GlobalParams.isNewest) {
				checkUpdate();
			} else {
				Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.rl_feedback:
			FDialog = new FeedBackDialog(SetActivity.this);

			FDialog.setDialogcallback(new FeedDialogcallback() {

				@Override
				public void BackLog(String text) {
					// TODO Auto-generated method stub
					sendFeedback(text);
				}
			});

			break;

		case R.id.rl_Down:

			
			try {
				if (FileUtils.getSysTotalSize()!=0) {
					
					Checkdialog();

				}else{
					showShortToast("本机无Sd卡");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		

			break;

		case R.id.rl_Change:

			AppConfig.loginOut();

			GlobalParams.user = null;
			GlobalParams.hasLogin = false;

			RESULT = 3;
			finish();
			Bundle bundle = new Bundle();
			bundle.putString("type", "Y");
			startActivityWithSlideAnimation(LoginActivity.class,bundle);
			break;
		case R.id.rl_logout:

			AppConfig.loginOut();

			GlobalParams.user = null;
			GlobalParams.hasLogin = false;
			rl_logout.setVisibility(View.GONE);

			RESULT = 3;
showShortToast("已退出");
			break;
		case R.id.rl_friends:
			shreDia = new ShareDialog();
			shreDia.showUpdateDialog(GlobalParams.toFriendText , GlobalParams.toFriendUrl, getApplicationContext(),
					SetActivity.this);

			break;
		default:
			break;
		}

	}

	private void Checkdialog() {
int w;
		final String items[] = {
				"手机存储 " + "剩余" + FileUtils.formatFileSize(FileUtils.getSDAvailableSize())
				+ "/共" + FileUtils.formatFileSize(FileUtils.getSDTotalSize()),
				"SD卡" + "剩余" + FileUtils.formatFileSize(FileUtils.getSystemAvailableSize()) + "/共"
						+ FileUtils.formatFileSize(FileUtils.getSysTotalSize()) };

		AlertDialog.Builder builder = new AlertDialog.Builder(this); // 先得到构造器
		builder.setTitle("提示"); // 设置标题
		builder.setIcon(R.drawable.logo);// 设置图标，图片id即可
		if (isSD) {
			w=1;
		} else {
			w = 0;
		}
		builder.setSingleChoiceItems(items, w, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					isSD = false;
				} else {
					isSD = true;
				}

				AppConfig.setSD(isSD);
				GlobalParams.downloadPath = SystemUtil.getInstance().getRootPath() + "/HanJuMovie";
				dialog.dismiss();
				Toast.makeText(SetActivity.this, items[which], Toast.LENGTH_SHORT).show();

				cunText.setText(items[which]);
			}
		});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// cunText.setText("SD卡" + "剩余" +
				// FileUtils.formatFileSize(FileUtils.getAvailableSDCard2MemorySize())
				// + "/共" +FileUtils.formatFileSize(
				// FileUtils.getSDTotalSize()));
				dialog.dismiss();
				Toast.makeText(SetActivity.this, "确定", Toast.LENGTH_SHORT).show();

			}
		});
		builder.create().show();
	}

	private AppInfo appInfo;

	private void checkUpdate() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (appInfo == null) {
					Toast.makeText(getApplicationContext(), "网络连接错误！", Toast.LENGTH_SHORT).show();
				} else {
					if (appInfo.getStatus() == 0) {
						Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_SHORT).show();
					} else {
						showUpdateDialog(appInfo.getData(), getApplicationContext(), SetActivity.this);
					}

				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				LoadingApi loadingApi = new LoadingApi();
				String channel = Statistics.getChannelCode();
				
				String upver = Statistics.getPackageInfo(getApplicationContext()).versionCode + "";
				appInfo = loadingApi.getAppInfo(channel, upver);
				return null;
			}
		}.execute("");

	}

	/**
	 * 解析升级信息 显示升级对话框
	 * 
	 * @param data
	 *            升级信息
	 * @param context
	 */
	public void showUpdateDialog(final UpdateInfo data, final Context context, final Activity activity) {

		if (data != null) {
			final String downloadUrl = data.getDown_url();
			final String version_name = data.getDes_ver();
			String desString = data.getDes();
			final int new_version = data.getUpver();
			final Dialog dialog = new Dialog(activity, R.style.MyDialog);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			// 设置它的ContentView
			View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.custome_dialog, null);
			TextView tv = (TextView) view.findViewById(R.id.dialog_message);
			TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
			dialog_enter.setSelected(true);
			TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
			dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
					if (downloadUrl != null && !downloadUrl.equals("")) {
						DownLoadAPI.downLoadApk(downloadUrl,
								activity.getApplicationContext().getResources().getString(R.string.app_name),
								activity.getApplicationContext().getPackageName(), new_version, true, true);
					} else {
						Logger.e("showUpdateDialog", " 下载路径出错  downloadUrl : " + downloadUrl);
					}

				}
			});
			
			
			dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
			desString = desString.replace("\\n", "\n");

			tv.setText("新版本: " + version_name + "\n" + desString);
			view.setMinimumWidth(600);
			dialog.setContentView(view);
			dialog.show();
		}
	}

	private void sendFeedback(final String content) {
		Callback callback = new Callback() {

			@SuppressLint("ShowToast")
			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
//				if (result.equals("true")) {
					Toast.makeText(getApplicationContext(), request, 1).show();
					finish();
//				} else {
//					Toast.makeText(getApplicationContext(), "反馈失败", 1).show();
//				}

			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				FeedbackApi feedbackApi = new FeedbackApi();
			
				try {
					if(GlobalParams.hasLogin){
						request = feedbackApi.seedFeedback(getVersionName(), content,GlobalParams.user.getUid());
						
					}else{
						request = feedbackApi.seedFeedback(getVersionName(), content,"");
					}

				
						return request;
					
					
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return "false";
			}
		}.executeProxy("");
	}

	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}

	@Override
	public void finish() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("result", "Is user's data changed?");
		// 设置返回数据
		this.setResult(RESULT, intent);
		super.finish();
	}
	
	public void showIsWifiDownloadDialog(final Activity activity) {

		final Dialog dialog = new Dialog(activity, R.style.MyDialog);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		// 设置它的ContentView
		View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.isgoon_dialog, null);
		TextView tv = (TextView) view.findViewById(R.id.dialog_message);
		TextView dialog_enter = (TextView) view.findViewById(R.id.dialog_enter);
		dialog_enter.setText("开启");
		TextView dialog_cancle = (TextView) view.findViewById(R.id.dialog_cancle);
		dialog_enter.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				wifiBtn.setChecked(true);
				 wifiBtn.setBackgroundResource(R.drawable.set_open);
				GlobalParams.isWifi = true;
				AppConfig.setWifi(GlobalParams.isWifi);
				
				dialog.cancel();
			}
		});

		dialog_cancle.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				wifiBtn.setBackgroundResource(R.drawable.set_close);
				GlobalParams.isWifi = false;
				AppConfig.setWifi(GlobalParams.isWifi);
				dialog.cancel();
			}
		});
		tv.setText("非wifi环境下载可能导致超额流量，确定开启");
		view.setMinimumWidth(600);
		dialog.setContentView(view);
		dialog.show();

	}
}
