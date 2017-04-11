package com.bccv.tianji.activity;

import java.io.File;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.tianji.R;
import com.bccv.tianji.activity.ui.CropImageUtil;
import com.bccv.tianji.activity.ui.CropImageUtil.CropImageListener;
import com.bccv.tianji.activity.ui.RoundImageUtil;
import com.bccv.tianji.api.RegisterApi;
import com.bccv.tianji.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.views.CircleImageView;

public class UpdateUserInfoActivity extends BaseActivity {
	private EditText nameText;
	private Button btn;
	private CircleImageView image;
	private String name;
	private ImageLoader iconLoader = ImageLoader.getInstance();;
	private boolean isUpdate = false;
	private CropImageUtil corpImageUtil;
	Bitmap bm;
	private PopupWindow mPopupWindow;
	private static int RESULT_UPDATE = -1;
	ImageButton back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_userinfo);

		nameText = (EditText) findViewById(R.id.update_NameEdit);

		btn = (Button) findViewById(R.id.update_btn);
back=(ImageButton) findViewById(R.id.Back);
back.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finishActivityWithAnim();
	}
});
		image = (CircleImageView) findViewById(R.id.update_ImageIcon);

		if (GlobalParams.hasLogin) {
			nameText.setText(GlobalParams.user.getNick_name());

			iconLoader.displayImage(GlobalParams.user.getUser_icon(), image,
					GlobalParams.iconOptions);

		}
		nameText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});

		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initPopuptWindow();

			}
		});
		corpImageUtil = CropImageUtil.getInstance(this);
		corpImageUtil.setCropImageListener(new CropImageListener() {

			@Override
			public void cropImageSuccess(Intent data, String path,
					boolean isHeadPic) {
				Log.e("图片流", isHeadPic + "");
				File mfile = new File(path);
				if (mfile.exists()) {
					bm = BitmapFactory.decodeFile(path);

					if (isHeadPic) {

						image.setImageBitmap(RoundImageUtil.toRoundBitmap(bm));
					} else {

					}

				}
			}

			@Override
			public void cropImageFaild() {
				// 图片保存失败 提示
				Toast.makeText(UpdateUserInfoActivity.this, "头像更换失败",
						Toast.LENGTH_SHORT).show();
			}
		});
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				name = nameText.getText().toString().trim();
				setData(bm);
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

	// private String Backdrop;
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.user_icon, null);
		photo_camera = (TextView) popupWindow.findViewById(R.id.photo_camera);
		photo_album = (TextView) popupWindow.findViewById(R.id.photo_album);
		quit = (TextView) popupWindow.findViewById(R.id.quit);
		photo_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				corpImageUtil.setHeadPic(true);
				corpImageUtil.getFromCamera();
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
		photo_album.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				corpImageUtil.setHeadPic(true);
				corpImageUtil.getFromGallery();
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
		quit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
			}
		});
		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		mPopupWindow.setAnimationStyle(R.style.anim_photo_select);
		mPopupWindow.showAtLocation(image, Gravity.BOTTOM, 0, 0);
	}

	private void setData(final Bitmap bm) {
		// TODO Auto-generated method stub

		Callback call = new Callback() {
			@Override
			public void handleResult(String result) {
				if (result.equals("true")) {
					finish();

				}
			}
		};

		new DataAsyncTask(call, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				User user;
				try {

					RegisterApi regi = new RegisterApi(
							UpdateUserInfoActivity.this);
					if(GlobalParams.hasLogin){
						 user = regi.LoninUpdateInfo(GlobalParams.user.getUser_id(), name, bm);
					}else{
						 user = regi.LoninUpdateInfo("", name, bm);
					}
					
				
					if (user != null) {
						GlobalParams.user = user;
						AppConfig.setPrefUserInfo(user);
						RESULT_UPDATE = 1;
						return "true";
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				return "false";
			}
		}.executeProxy("");

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CropImageUtil.GALLERY_REQUEST_CODE:
			Logger.v("onActivityResult",
					"返回值到了UserInfoActivity,GALLERY_REQUEST_CODE");
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
	protected void onResume() {
		RESULT_UPDATE = -1;
		super.onResume();
	}
}
