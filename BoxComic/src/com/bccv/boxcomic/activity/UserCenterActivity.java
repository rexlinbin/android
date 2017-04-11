package com.bccv.boxcomic.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.sns.UserInfoManager;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.DataCleanManager;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.MyApplication;
import com.bccv.boxcomic.tool.RoundedImageView;
import com.bccv.boxcomic.tool.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserCenterActivity extends BaseActivity {
	private ImageView goImage;
	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usercenter);

		activity = this;
		setBack();

		RoundedImageView headImageView = (RoundedImageView) findViewById(R.id.head_circleImageView);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(UserInfoManager.getUserIcon(), headImageView,
				GlobalParams.headOptions);

		TextView userNameTextView = (TextView) findViewById(R.id.userName_textView);
		goImage = (ImageView) findViewById(R.id.head_imageView);
		if (!StringUtils.isEmpty(UserInfoManager.getUserName())) {
			userNameTextView.setText(UserInfoManager.getUserName());
		}

		LinearLayout userLayout = (LinearLayout) findViewById(R.id.user_layout);
		userLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!UserInfoManager.isLogin()) {
					Intent aIntent = new Intent(UserCenterActivity.this,
							LoginActivity.class);
					aIntent.putExtra("type", "UserCenter");
					startActivity(aIntent);
					overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
					finish();
				} else {
					startActivityWithSlideAnimation(UserIconActivity.class);
				}
			}
		});

		LinearLayout downloadLayout = (LinearLayout) findViewById(R.id.download_layout);
		downloadLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(ComicDownloadActivity.class);
			}
		});

		LinearLayout commentLayout = (LinearLayout) findViewById(R.id.comment_layout);
		commentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!UserInfoManager.isLogin()) {
					startActivityWithSlideAnimation(LoginActivity.class);
				} else {
					Intent intent = new Intent(UserCenterActivity.this,
							CommentActivity.class);
					intent.putExtra("user_id", UserInfoManager.getUserId() + "");
					intent.putExtra("isUser", true);
					startActivity(intent);
				}

			}
		});

		LinearLayout feedbackLayout = (LinearLayout) findViewById(R.id.feedback_layout);
		feedbackLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!UserInfoManager.isLogin()) {
					startActivityWithSlideAnimation(LoginActivity.class);
				} else {
					Intent intent = new Intent(UserCenterActivity.this,
							FeedbackActivity.class);
					startActivity(intent);
				}
			}
		});

		final TextView cacheSizeTextView = (TextView) findViewById(R.id.cacheSize_textView);

		cacheSizeTextView.setText(StringUtils.formatFileSize(FileUtils
				.getDirSize(getCacheDir())));

		LinearLayout clearCacheLayout = (LinearLayout) findViewById(R.id.clearCache_layout);
		clearCacheLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DataCleanManager.cleanApplicationData(getApplicationContext(),
						"");
				Toast.makeText(getApplicationContext(), "已清除", 1).show();
				cacheSizeTextView.setText("0B");
			}
		});

		LinearLayout updateLayout = (LinearLayout) findViewById(R.id.update_layout);
		updateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyApplication.setUpdate(getApplicationContext(), false);

			}
		});

		ImageView newImageView = (ImageView) findViewById(R.id.new_imageView);
		TextView updateTextView = (TextView) findViewById(R.id.update_textView);
		if (GlobalParams.canUpdate) {
			newImageView.setVisibility(View.VISIBLE);
			updateTextView.setText("有新版本可更新");
		} else {
			newImageView.setVisibility(View.GONE);
			updateTextView.setText("已是最新版本");
			updateLayout.setEnabled(false);
		}

		LinearLayout tuiChu = (LinearLayout) findViewById(R.id.logout_layout);

		if (!UserInfoManager.isLogin()) {

			tuiChu.setVisibility(View.GONE);

		} else {

			tuiChu.setVisibility(View.VISIBLE);

		}
		tuiChu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				UserInfoManager.logOut();
				finishActivityWithAnim();

			}
		});

	}

	private void setBack() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		RoundedImageView headImageView = (RoundedImageView) findViewById(R.id.head_circleImageView);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(UserInfoManager.getUserIcon(), headImageView,
				GlobalParams.headOptions);

		TextView userNameTextView = (TextView) findViewById(R.id.userName_textView);
		if (!StringUtils.isEmpty(UserInfoManager.getUserName())) {
			userNameTextView.setText(UserInfoManager.getUserName());
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

}
