package com.bccv.strategy.ui.activity;

import java.io.File;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.common.GlobalConstants;
import com.bccv.strategy.model.LoginResBean;
import com.bccv.strategy.model.UserInfo;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.utils.CropImageUtil;
import com.bccv.strategy.utils.CropImageUtil.CropImageListener;
import com.bccv.strategy.utils.ImageLoaderUtil;
import com.bccv.strategy.utils.RoundImageUtil;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class PersonalActivity extends BaseActivity {
	
	private RelativeLayout ll_root;
	private RelativeLayout rl_portrait;
	private ImageView iv_portrait;
	private RelativeLayout rl_nick;
	private TextView tv_nick;
	private EditText et_nick;
	private RelativeLayout rl_signature;
	private TextView tv_signature;
	private EditText et_signature;
	private View other;
	private TextView tv_common_title;
	private ImageView iv_common_left;
	private LinearLayout common_right;
	private LinearLayout common_left;
	private BackGroundView personal_bg_view;
	private PopupWindow mPopupWindow;
	private int[] backgrounds;
	private CropImageUtil corpImageUtil;
	private LinearLayout waitting_layout;
	private InputMethodManager imm;
	
	/****************popupwindow****************/
	private View v_other;
	private TextView photo_camera;
	private TextView photo_album;
	private TextView quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.second_personal);
		backgrounds = getIntent().getIntArrayExtra(BackGroundView.BACKGROUND_COLOR);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		findView();
		setClick();
		initPopuptWindow();
	}

	private void findView() {
		ll_root = (RelativeLayout) findViewById(R.id.ll_root);
		tv_common_title = (TextView) findViewById(R.id.common_title_tv);
		iv_common_left = (ImageView) findViewById(R.id.iv_common_left);
		common_right = (LinearLayout) findViewById(R.id.common_title_right_btn);
		common_left = (LinearLayout) findViewById(R.id.common_title_menu_btn);
		rl_portrait = (RelativeLayout) findViewById(R.id.rl_portrait);
		rl_nick = (RelativeLayout) findViewById(R.id.rl_nick);
		rl_signature = (RelativeLayout) findViewById(R.id.rl_signature);
		iv_portrait = (ImageView) findViewById(R.id.iv_portrait);
		tv_nick = (TextView) findViewById(R.id.tv_nick);
		et_nick = (EditText) findViewById(R.id.et_nick);
		tv_signature = (TextView) findViewById(R.id.tv_signature);
		et_signature = (EditText) findViewById(R.id.et_signature);
		personal_bg_view = (BackGroundView) findViewById(R.id.personal_bg_view);
		other = findViewById(R.id.other);
		waitting_layout = (LinearLayout) findViewById(R.id.waitting);
		
		personal_bg_view.setGradient(backgrounds[0], backgrounds[1]);
		
		iv_common_left.setBackgroundResource(R.drawable.title_menu_left_selector);
		tv_common_title.setText("应用设置");
		
		ImageLoaderUtil.getInstance(mContext).displayImage(UserInfoManager.getUserIcon(),iv_portrait,  
				ImageLoaderUtil.getUserIconImageOptions());
		tv_nick.setText(UserInfoManager.getUserName());
		tv_signature.setText(UserInfoManager.getUserIntroduce());
		
		corpImageUtil = CropImageUtil.getInstance(this);
		corpImageUtil.setHeadPic(true);
		
		corpImageUtil.setCropImageListener(new CropImageListener() {

			@Override
			public void cropImageSuccess(Intent data, String path,boolean isHeadPic) {
				File mfile = new File(path);
				if(mfile.exists()){
					Bitmap bm = BitmapFactory.decodeFile(path);
					waitting_layout.setVisibility(View.VISIBLE);
					if (isHeadPic) {
						update(UserInfoManager.getUserName(),UserInfoManager.getUserIntroduce(),bm);
						iv_portrait.setImageBitmap(RoundImageUtil.toRoundBitmap(bm));
					}
				}
			}
			@Override
			public void cropImageFaild() {
				// 图片保存失败 提示
				Toast.makeText(mContext, "头像更换失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void setClick() {
		other.setOnClickListener(this);
		common_left.setOnClickListener(this);
		common_right.setOnClickListener(this);
		rl_portrait.setOnClickListener(this);
		rl_nick.setOnClickListener(this);
		rl_signature.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_menu_btn:
			hideEditText();
			onBackPressed();
			this.finish();
			break;
		case R.id.common_title_right_btn:
			hideEditText();
			Intent intent = new Intent(PersonalActivity.this,AppReleaseActivity.class);
			intent.putExtra(BackGroundView.BACKGROUND_COLOR, backgrounds);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			break;
		case R.id.rl_portrait:
			hideEditText();
			if (mPopupWindow != null && !mPopupWindow.isShowing()) {
				mPopupWindow.showAtLocation(ll_root, Gravity.BOTTOM, 0, 0);
			}
			break;
		case R.id.quit:
		case R.id.v_other:
			if (mPopupWindow!= null && mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.photo_album:
			corpImageUtil.setHeadPic(true);
			corpImageUtil.getFromGallery();
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.photo_camera:
			corpImageUtil.setHeadPic(true);
			corpImageUtil.getFromCamera();
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		case R.id.rl_nick:
			if (et_signature.isShown()) {
				String sign_new = et_signature.getText().toString().trim();
				tv_signature.setText(sign_new);
				tv_signature.setVisibility(View.VISIBLE);
				et_signature.setVisibility(View.GONE);
			}
			
			if (!et_nick.isShown()) {
				String str_nick = tv_nick.getText().toString();
				tv_nick.setVisibility(View.INVISIBLE);
				et_nick.setText(str_nick);
				et_nick.setVisibility(View.VISIBLE);
				et_nick.requestFocus();
				imm.showSoftInput(et_nick,InputMethodManager.HIDE_NOT_ALWAYS);  
			}
			break;
		case R.id.rl_signature:
			if (et_nick.isShown()) {
				String nick_new = et_nick.getText().toString().trim();
				tv_nick.setText(nick_new);
				tv_nick.setVisibility(View.VISIBLE);
				et_nick.setVisibility(View.GONE);
			}
			if (!et_signature.isShown()) {
				tv_signature.setVisibility(View.INVISIBLE);
				String str_sign = tv_signature.getText().toString();
				et_signature.setText(str_sign);
				et_signature.setVisibility(View.VISIBLE);
				et_signature.requestFocus();
				imm.showSoftInput(et_signature,InputMethodManager.HIDE_NOT_ALWAYS);  
			}
			break;
		case R.id.other:
			hideEditText();
			break;
		default:
			break;
		}
	}
	
	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.user_icon, null);
		v_other = popupWindow.findViewById(R.id.v_other);
		photo_camera = (TextView) popupWindow.findViewById(R.id.photo_camera);
		photo_album = (TextView) popupWindow.findViewById(R.id.photo_album);
		quit = (TextView) popupWindow.findViewById(R.id.quit);
		v_other.setOnClickListener(this);
		photo_camera.setOnClickListener(this);
		photo_album.setOnClickListener(this);
		quit.setOnClickListener(this);
		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		mPopupWindow.setAnimationStyle(R.style.anim_photo_select);
//		mPopupWindow.showAtLocation(ll_root, Gravity.BOTTOM, 0, 0);
	}
	
	private void hideEditText() {
		if (et_nick.isShown()) {
			String nick_new = et_nick.getText().toString().trim();
			tv_nick.setText(nick_new);
			tv_nick.setVisibility(View.VISIBLE);
			et_nick.setVisibility(View.GONE);
			imm.hideSoftInputFromWindow(et_nick.getWindowToken(), 0); 
		}
		
		if (et_signature.isShown()) {
			String sign_new = et_signature.getText().toString().trim();
			tv_signature.setText(sign_new);
			tv_signature.setVisibility(View.VISIBLE);
			et_signature.setVisibility(View.GONE);
			imm.hideSoftInputFromWindow(et_signature.getWindowToken(), 0); 
		}
		if (!tv_nick.getText().toString().trim().equals(UserInfoManager.getUserName()) 
				|| !tv_signature.getText().toString().trim().equals(UserInfoManager.getUserIntroduce())) {
			update(tv_nick.getText().toString(), tv_signature.getText().toString(),null);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CropImageUtil.GALLERY_REQUEST_CODE:
		case CropImageUtil.CAMERA_REQUEST_CODE:
			Log.v("onActivityResult", "返回值到了UserInfoActivity,CAMERA_REQUEST_CODE");
		case CropImageUtil.RESULT_REQUEST_CODE:
			Log.v("onActivityResult", "返回值到了UserInfoActivity,RESULT_REQUEST_CODE");
			CropImageUtil.getInstance(this).handleMyActivityResult(requestCode,
					resultCode, data);
			break;
		default: 
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void update(String name,String introduce,Bitmap bm) {
		if (SystemUtil.isNetOkWithToast(mContext)) {
			NetWorkAPI.updateUserInfo(mContext, 
					name, introduce,"jpeg", bm, new HttpCallback() {
						
						@Override
						public void onResult(NetResBean response) {
							if (response.success && response instanceof LoginResBean) {
								LoginResBean bean = (LoginResBean) response;
								System.out.println("name :"+bean.getUser_name()+" sign："+bean.getIntroduce()
										+" url :"+bean.getUser_icon());
								saveInfoToSP(bean);
							}else {
								Toast.makeText(mContext, "修改资料失败", Toast.LENGTH_SHORT).show();
							}
							waitting_layout.setVisibility(View.GONE);
						}
						
						@Override
						public void onError(String errorMsg) {
							waitting_layout.setVisibility(View.GONE);
						}
						
						@Override
						public void onCancel() {
							waitting_layout.setVisibility(View.GONE);
						}
					});
		}
	}

	/**
	 * 保存用户信息到本地
	 * @param data
	 */
	protected void saveInfoToSP(LoginResBean data) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUser_id(data.getUser_id());
		userInfo.setUser_name(data.getUser_name());
		userInfo.setUser_icon(data.getUser_icon());
		userInfo.setUser_introduce(data.getIntroduce());
		UserInfoManager.saveUserInfo(userInfo);
		
		sendInfoAlterdBroadcast();
	}
	
	private void sendInfoAlterdBroadcast(){
	    Intent intent = new Intent();
        intent.setAction(GlobalConstants.USER_INFO_CHANGE_BROADCAST);
        sendBroadcast(intent);
	}
}
