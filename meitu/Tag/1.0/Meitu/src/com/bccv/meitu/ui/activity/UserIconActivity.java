package com.bccv.meitu.ui.activity;

import java.io.File;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.UpdateuserinfoResBean;
import com.bccv.meitu.model.UserInfo;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.utils.CropImageUtil;
import com.bccv.meitu.utils.CropImageUtil.CropImageListener;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.RoundImageUtil;
import com.bccv.meitu.utils.SystemUtil;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class UserIconActivity extends BaseActivity {
	private RelativeLayout rl_root;
	private ImageView user_icon_back;
	private TextView icon_tv;
	private ImageView user_icon_iv;
	private TextView icon_nickname;
	private TextView user_icon_nick;
	private EditText et_nick;
	private LinearLayout ll_other;
	private InputMethodManager imm;
	private PopupWindow mPopupWindow;
	private CropImageUtil corpImageUtil;
	private RelativeLayout waitting_layout;
	
	private View ll_user_icon;
	private View ll_user_background;
	
	private static int RESULT_UPDATE = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_icon);
		initView();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		userIcon = UserInfoManager.getUserIcon();
		userName = UserInfoManager.getUserName();
//		Backdrop = UserInfoManager.getUserBackdrop();
		ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(userIcon, user_icon_iv, 
				ImageLoaderUtil.getRoundedImageOptions());
//		if (Backdrop == null || Backdrop.equals("")) {
//			background_iv.setBackgroundResource(R.drawable.user_bg);
//		}else{
//			ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(Backdrop, background_iv, 
//					R.drawable.user_bg);
//		}
		user_icon_nick.setText(userName);
	}
	@Override
	protected void onResume() {
		RESULT_UPDATE = -1;
		super.onResume();
	}
	private void initView() {
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		user_icon_back = (ImageView) findViewById(R.id.user_icon_back);
		icon_tv = (TextView) findViewById(R.id.icon_tv);
		ll_user_icon = findViewById(R.id.ll_user_icon);
		ll_user_background = findViewById(R.id.ll_user_background);
		user_icon_iv = (ImageView) findViewById(R.id.user_icon_iv);
		icon_nickname = (TextView) findViewById(R.id.icon_nickname);
		user_icon_nick = (TextView) findViewById(R.id.user_icon_nick);
		et_nick = (EditText) findViewById(R.id.et_nick);
		ll_other = (LinearLayout) findViewById(R.id.ll_other);
		waitting_layout = (RelativeLayout) findViewById(R.id.waitting_layout);
		
		user_icon_back.setOnClickListener(this);
		icon_nickname.setOnClickListener(this);
		ll_user_icon.setOnClickListener(this);
		ll_user_background.setOnClickListener(this);
		ll_other.setOnClickListener(this);
		
		corpImageUtil = CropImageUtil.getInstance(this);
		
		
		corpImageUtil.setCropImageListener(new CropImageListener() {

			@Override
			public void cropImageSuccess(Intent data, String path,boolean isHeadPic) {
				File mfile = new File(path);
				if(mfile.exists()){
					Bitmap bm = BitmapFactory.decodeFile(path);
					waitting_layout.setVisibility(View.VISIBLE);
					if (isHeadPic) {
						sendIconData(bm);
						user_icon_iv.setImageBitmap(RoundImageUtil.toRoundBitmap(bm));
					}else{
						sendBackDropData(bm);
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
	
	/*
	 * 创建PopupWindow
	 */
	TextView photo_camera;
	TextView photo_album;
	TextView quit;
	private String userName;
	private String userIcon;
//	private String Backdrop;
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.user_icon, null);
		photo_camera = (TextView) popupWindow.findViewById(R.id.photo_camera);
		photo_album = (TextView) popupWindow.findViewById(R.id.photo_album);
		quit = (TextView) popupWindow.findViewById(R.id.quit);
		photo_camera.setOnClickListener(this);
		photo_album.setOnClickListener(this);
		quit.setOnClickListener(this);
		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		mPopupWindow.setAnimationStyle(R.style.anim_photo_select);
		mPopupWindow.showAtLocation(rl_root, Gravity.BOTTOM, 0, 0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_icon_back:
			onBackPressed();
			break;
		case R.id.ll_user_icon:
			initPopuptWindow();
			break;
		case R.id.icon_nickname:
			if(!et_nick.isShown()){
				user_icon_nick.setVisibility(View.GONE);
				et_nick.setVisibility(View.VISIBLE);
				et_nick.requestFocus();
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
			}
			break;
		case R.id.ll_other:
			if(et_nick.isShown()){
				String trim = et_nick.getText().toString().trim();
				String nick = user_icon_nick.getText().toString().trim();
				if(trim == null || trim.equals("")){
					user_icon_nick.setText(nick);
				}else{
					user_icon_nick.setText(trim);
					et_nick.setText("");
					waitting_layout.setVisibility(View.VISIBLE);
					sendNickNameData(trim);
				}
				user_icon_nick.setVisibility(View.VISIBLE);
				et_nick.setVisibility(View.GONE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
	 		}
			break;
		case R.id.ll_user_background:
			corpImageUtil.setHeadPic(false);
			corpImageUtil.getFromGallery();
			break;
		case R.id.photo_camera:
			corpImageUtil.setHeadPic(true);
			corpImageUtil.getFromCamera();
			if (mPopupWindow.isShowing()) {
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
		case R.id.quit:
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CropImageUtil.GALLERY_REQUEST_CODE:
		case CropImageUtil.CAMERA_REQUEST_CODE:
			Logger.v("onActivityResult", "返回值到了UserInfoActivity,CAMERA_REQUEST_CODE");
		case CropImageUtil.RESULT_REQUEST_CODE:
			Logger.v("onActivityResult", "返回值到了UserInfoActivity,RESULT_REQUEST_CODE");
			CropImageUtil.getInstance(this).handleMyActivityResult(requestCode,
					resultCode, data);
			break;
		default: 
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	@Override
	public void onBackPressed() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			return ;
		}
		super.onBackPressed();
	}
	/**
	 * 上传用户头像
	 * @param bm
	 */
	private void sendIconData(Bitmap bm){
		waitting_layout.setVisibility(View.VISIBLE);
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
			NetWorkAPI.updateUserPic(getApplicationContext(), bm,null, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					waitting_layout.setVisibility(View.GONE);
					if (response.success && response instanceof UpdateuserinfoResBean) {
						UpdateuserinfoResBean dataBean = (UpdateuserinfoResBean) response;
						saveInfoToSP(dataBean);
						showShortToast("上传头像成功");
						RESULT_UPDATE = 1;
					}else{
						showShortToast("上传头像失败");
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					if (errorMsg != null && errorMsg.equals("")) {
						showShortToast(errorMsg);
					}
				}
				
				@Override
				public void onCancel() {}
			});
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	private void sendBackDropData(Bitmap bm){
		waitting_layout.setVisibility(View.VISIBLE);
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
			NetWorkAPI.updateUserPic(getApplicationContext(), null, bm, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					waitting_layout.setVisibility(View.GONE);
					if (response.success && response instanceof UpdateuserinfoResBean) {
						UpdateuserinfoResBean dataBean = (UpdateuserinfoResBean) response;
						saveInfoToSP(dataBean);
						showShortToast("上传背景成功");
						RESULT_UPDATE = 3;
					}else{
						showShortToast("上传背景失败");
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
	/**
	 * 修改用户名
	 * @param nickName
	 */
	private void sendNickNameData(String nickName){
		waitting_layout.setVisibility(View.VISIBLE);
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
			NetWorkAPI.updateUserName(getApplicationContext(), nickName, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					waitting_layout.setVisibility(View.GONE);
					if (response.success && response instanceof UpdateuserinfoResBean) {
						UpdateuserinfoResBean dataBean = (UpdateuserinfoResBean) response;
						saveInfoToSP(dataBean);
						showShortToast("修改昵称成功");
						RESULT_UPDATE = 2;
					}else{
						showShortToast("修改昵称失败");
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					if (errorMsg != null && errorMsg.equals("")) {
						showShortToast(errorMsg);
					}
				}
				
				@Override
				public void onCancel() {}
			});
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 保存用户信息到本地
	 * @param data
	 */
	protected void saveInfoToSP(UpdateuserinfoResBean data) {
		UserInfo userInfo = new UserInfo();
		userInfo.setUser_name(data.getUser_name());
		userInfo.setUser_id(data.getUser_id());
		userInfo.setUser_icon(data.getUser_icon());
		userInfo.setBackdrop(data.getBackdrop());
//		System.out.println(userInfo.toString());
		UserInfoManager.saveUserInfo(userInfo);
	}
	
	@Override
	protected void onDestroy() {
		//数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("result", "Is user's data changed?");
        //设置返回数据
        this.setResult(RESULT_UPDATE, intent);
		super.onDestroy();
	}
	
	@Override
	public void finish() {
		//数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("result", "Is user's data changed?");
        //设置返回数据
        this.setResult(RESULT_UPDATE, intent);
		super.finish();
	}
}
