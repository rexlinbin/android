package com.bccv.boxcomic.activity;

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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.api.UserApi;
import com.bccv.boxcomic.modal.User;
import com.bccv.boxcomic.sns.UserInfoManager;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.BitmapUtil;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.ImageUtils;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.view.CropImageUtil;
import com.bccv.boxcomic.view.CropImageUtil.CropImageListener;
import com.bccv.boxcomic.view.RoundImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserIconActivity extends BaseActivity implements OnClickListener {
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

	private View ll_user_icon;

	private String imageUrlString;
private String trim;
	private static int RESULT_UPDATE = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_icon);
	
		initView();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		userIcon = UserInfoManager.getUserIcon();
		userName = UserInfoManager.getUserName();
		// Backdrop = UserInfoManager.getUserBackdrop();

		ImageLoader.getInstance().displayImage(userIcon, user_icon_iv,
				GlobalParams.headOptions);
		user_icon_nick.setText(userName);
	}

	private void initView() {
		// TODO Auto-generated method stub

		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		user_icon_back = (ImageView) findViewById(R.id.user_icon_back);
		icon_tv = (TextView) findViewById(R.id.icon_tv);
		ll_user_icon = findViewById(R.id.ll_user_icon);

		user_icon_iv = (ImageView) findViewById(R.id.user_icon_iv);
		icon_nickname = (TextView) findViewById(R.id.icon_nickname);
		user_icon_nick = (TextView) findViewById(R.id.user_icon_nick);
		et_nick = (EditText) findViewById(R.id.et_nick);
		ll_other = (LinearLayout) findViewById(R.id.ll_other);

		user_icon_back.setOnClickListener(this);
		icon_nickname.setOnClickListener(this);
		ll_user_icon.setOnClickListener(this);

		ll_other.setOnClickListener(this);

		corpImageUtil = CropImageUtil.getInstance(this);

		corpImageUtil.setCropImageListener(new CropImageListener() {

			@Override
			public void cropImageSuccess(Intent data, String path,
					boolean isHeadPic) {
				imageUrlString = path;
				File mfile = new File(path);
				if (mfile.exists()) {
					Bitmap bm = BitmapFactory.decodeFile(path);
					if (isHeadPic) {
						sendIconData(bm);
						user_icon_iv.setImageBitmap(RoundImageUtil
								.toRoundBitmap(bm));
					} else {

					}
				}
			}

			@Override
			public void cropImageFaild() {
				// 图片保存失败 提示
				Toast.makeText(UserIconActivity.this, "头像更换失败",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 上传用户头像
	 * 
	 * @param bm
	 */
	private void sendIconData(final Bitmap bm) {
		// TODO Auto-generated method stub

		Callback localCallback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					Toast.makeText(getApplicationContext(), "修改成功", 1).show();
				}else {
					Toast.makeText(getApplicationContext(), "修改失败", 1).show();
				}
			}
		};

		new DataAsyncTask(localCallback, true) {

			protected String doInBackground(String... params) {
				UserApi userApi = new UserApi();
				String pic_type = ImageUtils.getImageType(new File(
						imageUrlString));
				String pic = BitmapUtil.bitmapToString(bm, pic_type);
				User userinfo = userApi.UpdateInfo(
						GlobalParams.user.getUser_name(),
						GlobalParams.user.getUser_icon(),
						GlobalParams.user.getUser_id() + "", pic_type, pic);
				saveInfoToSP(userinfo);
				if (userinfo != null) {
					GlobalParams.user = userinfo;
					return "true";
				}
				return "false";

			};

		}.executeProxy("");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.user_icon_back:
		
				if (et_nick.isShown()) {
					 trim = et_nick.getText().toString().trim();
					String nick = user_icon_nick.getText().toString().trim();
					if (trim == null || trim.equals("")) {
						user_icon_nick.setText(nick);
					} else {
						user_icon_nick.setText(trim);
						et_nick.setText("");
						sendNickNameData(trim);
					}
					user_icon_nick.setVisibility(View.VISIBLE);
					et_nick.setVisibility(View.GONE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
				
			
			
			onBackPressed();
			break;
		case R.id.ll_user_icon:
			initPopuptWindow();
			break;
		case R.id.icon_nickname:
			if (!et_nick.isShown()) {
				user_icon_nick.setVisibility(View.GONE);
				et_nick.setVisibility(View.VISIBLE);
				et_nick.requestFocus();
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		
			}
			break;
//		case R.id.ll_other:
//			if (et_nick.isShown()) {
//				 trim = et_nick.getText().toString().trim();
//				String nick = user_icon_nick.getText().toString().trim();
//				if (trim == null || trim.equals("")) {
//					user_icon_nick.setText(nick);
//				} else {
//					user_icon_nick.setText(trim);
//					et_nick.setText("");
//					sendNickNameData(trim);
//				}
//				user_icon_nick.setVisibility(View.VISIBLE);
//				et_nick.setVisibility(View.GONE);
//				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//			break;

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

	/**
	 * 修改用户名
	 * 
	 * @param nickName
	 */
	private void sendNickNameData(final String trim) {
		// TODO Auto-generated method stub
		Callback localCallback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (result.equals("true")) {
					Toast.makeText(getApplicationContext(), "修改成功", 1).show();
				}else {
					Toast.makeText(getApplicationContext(), "修改失败", 1).show();
				}
			}
		};

		new DataAsyncTask(localCallback, true) {

			protected String doInBackground(String... params) {
				UserApi userApi = new UserApi();
				String pic_type = "";
				String pic = "";
				User userinfo = userApi.UpdateInfo(
						trim,
						GlobalParams.user.getUser_icon(),
						GlobalParams.user.getUser_id() + "", pic_type, pic);
				saveInfoToSP(userinfo);
				if (userinfo != null) {
					GlobalParams.user = userinfo;
					return "true";
				}
				return "false";
			};

		}.executeProxy("");
	}

	/*
	 * 创建PopupWindow
	 */
	TextView photo_camera;
	TextView photo_album;
	TextView quit;
	private String userName;
	private String userIcon;

	// private String Backdrop;
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CropImageUtil.GALLERY_REQUEST_CODE:
		case CropImageUtil.CAMERA_REQUEST_CODE:
			Logger.v("onActivityResult",
					"返回值到了UserInfoActivity,CAMERA_REQUEST_CODE");
		case CropImageUtil.RESULT_REQUEST_CODE:
			Logger.v("onActivityResult",
					"返回值到了UserInfoActivity,RESULT_REQUEST_CODE");
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
			return;
		}
		super.onBackPressed();
	}

	/**
	 * 保存用户信息到本地
	 * 
	 * @param data
	 */
	protected void saveInfoToSP(User data) {
		
		User userInfo = new User();
		userInfo.setUser_name(data.getUser_name());
		userInfo.setUser_id(data.getUser_id());
		userInfo.setUser_icon(data.getUser_icon());
		Logger.e("saveInfoToSP", data.getUser_name());
		// System.out.println("old token : "+UserInfoManager.getUserToken());

		UserInfoManager.saveUserInfo(userInfo);
		// System.out.println("new token : "+UserInfoManager.getUserToken());

	}

	@Override
	protected void onDestroy() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("result", "Is user's data changed?");
		// 设置返回数据
		this.setResult(RESULT_UPDATE, intent);
		super.onDestroy();
	}

	@Override
	public void finish() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("result", "Is user's data changed?");
		// 设置返回数据
		this.setResult(RESULT_UPDATE, intent);
		super.finish();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

}
