package com.bccv.zhuiyingzhihanju.activity;

import java.io.File;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.UserAPi;
import com.bccv.zhuiyingzhihanju.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.AppConfig;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.views.ui.CropImageUtil;
import com.utils.views.ui.CropImageUtil.CropImageListener;
import com.utils.views.ui.RoundImageUtil;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalDataActivity extends BaseActivity {
	private static int RESULT_UPDATE = -1;
	private ImageView icon;
	private ImageLoader iconLoader = ImageLoader.getInstance();;
	private EditText name;
	private RadioButton sexM, setWM;
	private EditText QM;
	private RadioGroup group;
	Bitmap bm;
	private PopupWindow mPopupWindow;
	private CropImageUtil corpImageUtil;
	String sex = "1";
	User user = null;
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "PersonalDataActivity");
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
tcStart();
		TextView textSure = (TextView) findViewById(R.id.titel_clear);
		textSure.setText("保存");
		textSure.setVisibility(View.VISIBLE);

		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		LinearLayout li = (LinearLayout) findViewById(R.id.title);
		li.setBackgroundColor(getResources().getColor(R.color.white));

		TextView text = (TextView) findViewById(R.id.titleName_textView);
		text.setText("个人资料修改");

		icon = (ImageView) findViewById(R.id.per_icon);

		name = (EditText) findViewById(R.id.per_Name);
		group = (RadioGroup) findViewById(R.id.radioGroup);
		sexM = (RadioButton) findViewById(R.id.rl_nan);
		setWM = (RadioButton) findViewById(R.id.rl_nv);
		QM = (EditText) findViewById(R.id.QM_edit);

		final TextView nameText = (TextView) findViewById(R.id.per_NameText);

		nameText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name.setVisibility(View.VISIBLE);
				nameText.setVisibility(View.INVISIBLE);
			}
		});

		final TextView QMText = (TextView) findViewById(R.id.QM_Text);
	
		QMText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QM.setVisibility(View.VISIBLE);
				QMText.setVisibility(View.INVISIBLE);
			}
		});

		if (GlobalParams.hasLogin) {
			nameText.setText(GlobalParams.user.getNick_name());
			name.setText(GlobalParams.user.getNick_name());
			iconLoader.displayImage(GlobalParams.user.getAvatars(), icon, GlobalParams.iconOptions);

			QM.setText(GlobalParams.user.getDes());

			QMText.setText(GlobalParams.user.getDes());
if(GlobalParams.user.getSex()!=null){
			if (GlobalParams.user.getSex().equals("1")) {

				sexM.setSelected(true);
				setWM.setSelected(false);
			}
			if (GlobalParams.user.getSex().equals("2")) {
				sexM.setSelected(false);
				setWM.setSelected(true);

			}
}
		}

		icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initPopuptWindow();

			}
		});
		corpImageUtil = CropImageUtil.getInstance(this);
		corpImageUtil.setCropImageListener(new CropImageListener() {

			@Override
			public void cropImageSuccess(Intent data, String path, boolean isHeadPic) {
				Log.e("图片流", isHeadPic + "");
				File mfile = new File(path);
				if (mfile.exists()) {
					bm = BitmapFactory.decodeFile(path);

					if (isHeadPic) {

						icon.setImageBitmap(RoundImageUtil.toRoundBitmap(bm));
					} else {

					}

				}
			}

			@Override
			public void cropImageFaild() {
				// 图片保存失败 提示
				Toast.makeText(PersonalDataActivity.this, "头像更换失败", Toast.LENGTH_SHORT).show();
			}
		});

		textSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendData(bm);
			}
		});

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				if (checkedId == sexM.getId()) {
					sexM.setSelected(true);
					setWM.setSelected(false);

					sex = "1";

				}
				if (checkedId == setWM.getId()) {
					setWM.setSelected(true);
					sexM.setSelected(false);
					sex = "2";
				}

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
		  Animation anim = AnimationUtils.loadAnimation(PersonalDataActivity.this, R.anim.slide_in_from_bottom);  
	  popupWindow.startAnimation(anim);  
		
		
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
		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(R.color.popuBg));
//		mPopupWindow.setAnimationStyle(R.style.anim_photo_select);
		mPopupWindow.showAtLocation(icon, Gravity.BOTTOM, 0, 0);
	}

	private void sendData(final Bitmap bm) {
		// TODO Auto-generated method stub

		Callback call = new Callback() {
			@Override
			public void handleResult(String result) {
				if (result.equals("true")) {
					finish();

				} else {
					showShortToast("修改失败");
				}
			}
		};

		new DataAsyncTask(call, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				if (GlobalParams.user != null) {
					Log.e("userID", GlobalParams.user.getUid() + "dfsdf");
				}
				
				try {

					UserAPi regi = new UserAPi(PersonalDataActivity.this);
					if (GlobalParams.hasLogin) {
						user = regi.LoninUpdateInfo(GlobalParams.user.getUid(), name.getText().toString(), bm, "","", QM.getText().toString(), sex, GlobalParams.user.getToken(), GlobalParams.user.getPhone());
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
			Logger.v("onActivityResult", "返回值到了UserInfoActivity,GALLERY_REQUEST_CODE");
		case CropImageUtil.CAMERA_REQUEST_CODE:
			Logger.v("onActivityResult", "返回值到了UserInfoActivity,CAMERA_REQUEST_CODE");
		case CropImageUtil.RESULT_REQUEST_CODE:
			Logger.v("onActivityResult", "返回值到了UserInfoActivity,RESULT_REQUEST_CODE");
			CropImageUtil.getInstance(this).handleMyActivityResult(requestCode, resultCode, data);
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
		TCAgent.onPageEnd(getApplicationContext(), "PersonalDataActivity");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		RESULT_UPDATE = -1;
		super.onResume();
	}
}
