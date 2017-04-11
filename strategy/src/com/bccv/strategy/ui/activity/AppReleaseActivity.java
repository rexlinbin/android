package com.bccv.strategy.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.common.GlobalConstants;
import com.bccv.strategy.model.InstallAppInfo;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.ControlNumEditText;
import com.bccv.strategy.ui.view.ControlNumEditText.onTextEditListener;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class AppReleaseActivity extends BaseActivity {

	public static final String RESULT_STRING = "app_result";
	public static final String START_WITH_APP = "start_with_app";
	
	private TextView common_title_tv;
	private TextView tv_common_left;
	private TextView tv_common_right;
	private TextView tv_app_click;
	private LinearLayout common_title_right;
	private LinearLayout common_title_left;
	private ImageView iv_app_logo;//app logo
	private ImageView iv_app_add;//app add
	private ControlNumEditText app_edit;
	private TextView app_limit;
	private BackGroundView app_bg_view;
	private int MAX_NUM = 120;
	private int MIN_NUM = 20;
	private int[] backgrounds ;
	
	//接口数据
	private String packageName = null;
	private String comID = "";
	private String comment = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_share_app);
		backgrounds = getIntent().getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		findView();
		setClick();
		
		InstallAppInfo info = getIntent().getParcelableExtra(START_WITH_APP);
		getData(info);
	}

	private void findView() {
		common_title_tv = (TextView) findViewById(R.id.common_title_tv);
		tv_common_left = (TextView) findViewById(R.id.tv_common_left);
		tv_common_right = (TextView) findViewById(R.id.tv_common_right);
		common_title_left = (LinearLayout) findViewById(R.id.common_title_menu);
		common_title_right = (LinearLayout) findViewById(R.id.common_title_right);
		app_edit = (ControlNumEditText) findViewById(R.id.app_edit);
		app_limit = (TextView) findViewById(R.id.app_limit);
		app_bg_view = (BackGroundView) findViewById(R.id.app_bg_view);
		iv_app_add = (ImageView) findViewById(R.id.iv_app_add);
		iv_app_logo = (ImageView) findViewById(R.id.iv_app_logo);
		tv_app_click = (TextView) findViewById(R.id.tv_app_click);
		
		common_title_left.setOnClickListener(this);
		common_title_right.setOnClickListener(this);
		
		app_edit.setMaxNum(MAX_NUM);
		app_edit.setOnTextEditListener(new onTextEditListener() {
			@Override
			public void textChanged(int cur_num) {
				app_limit.setText("还可以输入"+ (MAX_NUM-cur_num) +"字");
			}
		});
		
		common_title_tv.setText("分享我的应用");
		app_bg_view.setGradient(backgrounds[0], backgrounds[1]);
	}
	
	private void setClick() {
		iv_app_add.setOnClickListener(this);
	}
	
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.in_from_none, R.anim.out_to_bottom);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_menu:
			onBackPressed();
			this.finish();
			break;
		case R.id.common_title_right:
			if (UserInfoManager.isLogin()) {
				send2Server();
			}else
				Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
			break;
		case R.id.iv_app_add:
			Intent shareIntent = new Intent(AppReleaseActivity.this,ShareAppSearchActivity.class);
			shareIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
			startActivityForResult(shareIntent, GlobalConstants.SEARCH4REQUEST);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;
		default:
			break;
		}
	}
	
	private void send2Server() {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			
			if (TextUtils.isEmpty(packageName)) {
				Toast.makeText(mContext, "请先选择应用", Toast.LENGTH_SHORT).show();
				return ;
			}
			
			String trim = app_edit.getText().toString().trim();
			if (!TextUtils.isEmpty(trim)) {
				comment = trim;
			}
			if (TextUtils.isEmpty(comment)) {
				Toast.makeText(mContext, "请评价应用", Toast.LENGTH_SHORT).show();
				return ;
			}else if (comment.length() < MIN_NUM) {
				Toast.makeText(mContext, "评价不应少于"+MIN_NUM+"字", Toast.LENGTH_SHORT).show();
				return ;
			}
					System.out.println("name : " + packageName);
			NetWorkAPI.appRelease(mContext,packageName, comID, comment,	new HttpCallback() {
				@Override
				public void onResult(NetResBean response) {
					if (response.success) {
						Toast.makeText(mContext, "应用上传成功", Toast.LENGTH_SHORT).show();
						closedSoftInput();
						AppReleaseActivity.this.finish();
					}else {
						Toast.makeText(mContext, "应用上传失败", Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					Toast.makeText(mContext, "应用上传失败", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onCancel() {
					Toast.makeText(mContext, "应用上传失败", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == GlobalConstants.SEARCH4RESULT) {
			if (data != null) {
				InstallAppInfo info = data.getParcelableExtra(RESULT_STRING);
				getData(info);
			}
		}
	}
	
	private void getData(InstallAppInfo info) {
		if (info != null) {
			tv_app_click.setText(info.getAppName());
			packageName = info.getPackageName();
			comment = info.getIntroduce();
			System.out.println("name : " + packageName);
			if ( !TextUtils.isEmpty(info.getIconUrl()) ) {
				String iconUrl = info.getIconUrl();
				ImageLoaderUtil.getInstance(mContext).displayImage(iconUrl, iv_app_logo);
			}else {
				iv_app_logo.setImageDrawable(SystemUtil.getCurAppIcon(mContext, packageName));
			}
		}
	}
	
	public void closedSoftInput(){
		/* 隐藏软键盘 */
		InputMethodManager imm1 = (InputMethodManager) app_edit.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm1.isActive()) {
			imm1.hideSoftInputFromWindow(
					app_edit.getApplicationWindowToken(), 0);
		}
	}
}
