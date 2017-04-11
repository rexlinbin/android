package com.bccv.zhuiyingzhihanju.activity;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.MSGApi;
import com.bccv.zhuiyingzhihanju.fragment.FancyFragment;
import com.bccv.zhuiyingzhihanju.fragment.JiaFancyFragment;
import com.bccv.zhuiyingzhihanju.fragment.SpecialFragment;
import com.bccv.zhuiyingzhihanju.fragment.YMyFrament;
import com.bccv.zhuiyingzhihanju.model.MyMsg;
import com.igexin.sdk.PushManager;
import com.tencent.stat.StatService;
import com.tendcloud.tenddata.TCAgent;
import com.utils.download.DownloadService;
import com.utils.tools.AppManager;
import com.utils.tools.GlobalParams;
import com.utils.tools.Statistics;

import android.app.TabActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnClickListener {
	private RelativeLayout fancyRe, foundRe, specialRe, myRe;
	private boolean isFancy, isFound, isSpecial, isMy;
	private ImageView fancyIm, foundIm, specialIm, myIm;
	private TabHost tabhost;
	private long exitTime = 0;
	ImageView msg_dot;
//	private MyMsg msg = new MyMsg();
	private int code;
	private int codeS;
	private boolean isTrue, isFalse;

	private void tcStart() {
		TCAgent.onPageStart(getApplicationContext(), "MainActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.activity_main);

		StatService.trackCustomEvent(this, "onCreate", "");

		PushManager.getInstance().initialize(this.getApplicationContext());

		GlobalParams.client_id = PushManager.getInstance().getClientid(getApplicationContext());

		// if (AppConfig.getPrefUserInfo() != null) {
		// GlobalParams.user = AppConfig.getPrefUserInfo();
		// GlobalParams.hasLogin = true;
		//
		// } else {
		//
		// User userInfo = new User();
		//
		// GlobalParams.user = userInfo;
		// GlobalParams.hasLogin = false;
		//
		// }

		msg_dot = (ImageView) findViewById(R.id.msg_dot);
		fancyRe = (RelativeLayout) findViewById(R.id.main_bottom_fancy);
		foundRe = (RelativeLayout) findViewById(R.id.main_bottom_found);

		specialRe = (RelativeLayout) findViewById(R.id.main_bottom_special);
		myRe = (RelativeLayout) findViewById(R.id.main_bottom_my);

		fancyIm = (ImageView) findViewById(R.id.main_bottom_fancyIm);
		foundIm = (ImageView) findViewById(R.id.main_bottom_foundIm);
		specialIm = (ImageView) findViewById(R.id.main_bottom_specialIm);
		myIm = (ImageView) findViewById(R.id.main_bottom_myIm);

		fancyRe.setOnClickListener(this);

		foundRe.setOnClickListener(this);
		specialRe.setOnClickListener(this);
		myRe.setOnClickListener(this);

		// if (GlobalParams.hasLogin) {
		new MainTask().execute();
		// }
	}

	private void setDefaultFragment(boolean isTrue2) {

		tabhost = getTabHost();
		if (isTrue2) {
			tabhost.addTab(tabhost.newTabSpec("fancy").setIndicator("fancy")
					.setContent(new Intent(this, FancyFragment.class)));
			tabhost.addTab(tabhost.newTabSpec("found").setIndicator("found")
					.setContent(new Intent(this, RecommendedActivity.class)));

			Intent aIntent = new Intent(this, SpecialFragment.class);

			aIntent.putExtra("isSelect", true);

			tabhost.addTab(tabhost.newTabSpec("special").setIndicator("special").setContent(aIntent));

			tabhost.addTab(tabhost.newTabSpec("my").setIndicator("my").setContent(new Intent(this, YMyFrament.class)));

		} else {
			tabhost.addTab(tabhost.newTabSpec("fancy").setIndicator("fancy")
					.setContent(new Intent(this, JiaFancyFragment.class)));
			tabhost.addTab(tabhost.newTabSpec("found").setIndicator("found")
					.setContent(new Intent(this, RecommendedActivity.class)));

			Intent aIntent = new Intent(this, JiaFancyFragment.class);

			aIntent.putExtra("isSelect", true);

			tabhost.addTab(tabhost.newTabSpec("special").setIndicator("special").setContent(aIntent));

			tabhost.addTab(
					tabhost.newTabSpec("my").setIndicator("my").setContent(new Intent(this, JiaFancyFragment.class)));
		}

		fancyIm.setImageResource(R.drawable.fancy_select);
		foundIm.setImageResource(R.drawable.found);
		specialIm.setImageResource(R.drawable.special);
		myIm.setImageResource(R.drawable.my);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.main_bottom_fancy:
			isFancy = true;
			isFound = false;
			isSpecial = false;
			isMy = false;
			tabhost.setCurrentTabByTag("fancy");
			break;

		case R.id.main_bottom_found:
			isFancy = false;
			isFound = true;
			isSpecial = false;
			isMy = false;
			tabhost.setCurrentTabByTag("found");
			break;
		case R.id.main_bottom_special:
			isFancy = false;
			isFound = false;
			isSpecial = true;
			isMy = false;
			tabhost.setCurrentTabByTag("special");
			break;

		case R.id.main_bottom_my:
			isFancy = false;
			isFound = false;
			isSpecial = false;
			isMy = true;
			tabhost.setCurrentTabByTag("my");
			break;

		}

		if (isFancy) {
			fancyIm.setImageResource(R.drawable.fancy_select);
		} else {
			fancyIm.setImageResource(R.drawable.fancy);
		}

		if (isFound) {
			foundIm.setImageResource(R.drawable.found_select);
		} else {
			foundIm.setImageResource(R.drawable.found);
		}

		if (isSpecial) {
			specialIm.setImageResource(R.drawable.special_select);
		} else {
			specialIm.setImageResource(R.drawable.special);
		}

		if (isMy) {
			myIm.setImageResource(R.drawable.my_select);
		} else {
			myIm.setImageResource(R.drawable.my);
		}

	}

	class MainTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			MSGApi api = new MSGApi();
			try {

				// msg = api.getCheckMSgList(GlobalParams.user.getUid());
				code = api.getCode();
				return "true";
			} catch (Exception e) {
				// TODO: handle exception
			}
			return "false";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equals("true")) {
				//
				// if (msg != null && (msg.getSys().equals("0") ||
				// msg.getUser().equals("0"))) {
				// msg_dot.setVisibility(View.INVISIBLE);
				// GlobalParams.hasMSG = false;
				// } else {
				// msg_dot.setVisibility(View.VISIBLE);
				// GlobalParams.hasMSG = true;
				// }
				String upver = Statistics.getPackageInfo(getApplicationContext()).versionCode + "";

				codeS = Integer.parseInt(upver);
//				Log.e("codeS ", codeS + "");
//				Log.e("code ", code + "");
//				isTrue = code > codeS;
//				Log.e("code > codeS ", isTrue + "");

				if (code > codeS) {

					setDefaultFragment(false);

				} else {
					
					setDefaultFragment(true);
				}

			} else {

			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				AppManager.getAppManager().finishAllActivity();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {

				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					DownloadService.unBindService(getApplicationContext());
					StatService.trackCustomEndEvent(this, " playTime", "app");
					finish();
					AppManager.getAppManager().finishAllActivity();
					// saveDownload();
					System.exit(0);
				}
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "MainActivity");
	}

}
