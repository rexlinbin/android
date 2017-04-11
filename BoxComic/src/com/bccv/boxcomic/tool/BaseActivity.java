package com.bccv.boxcomic.tool;




import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.net.NetUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity通用操作，异步任务开启。
 * 
 * @author WuYelin
 * 
 */
public class BaseActivity extends Activity {

	protected boolean canShowWebDialog = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		AppManager.getAppManager().addActivity(this);
		GlobalParams.context = getApplicationContext();
		GlobalParams.activity = this;
	}

	public void startActivityWithSlideAnimation(Class<?> pClass) {
		startActivityWithSlideAnimation(pClass, null);
	}

	/**
	 * 带Slide效果开启新的Activity
	 * 
	 * @param pClass
	 * @param pBundle
	 *            activity之间传递的参数。
	 */
	protected void startActivityWithSlideAnimation(Class<?> pClass,
			Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	/**
	 * 带Slide效果退出Activity
	 */
	protected void finishActivityWithAnim() {
		finish();
		overridePendingTransition(anim.slide_in_left, anim.slide_out_right);
	}

	protected abstract class DataAsyncTask extends
			AsyncTask<String, String, String> {

		protected Callback callback;
		private boolean isShowDialog;

		/**
		 * 异步任务
		 * 
		 * @param callback
		 *            回调
		 * @param isShowDialog
		 *            执行异步任务时，是否开启提示对话框(如下拉刷新等是不需要开启的)。
		 */
		public DataAsyncTask(Callback callback, boolean isShowDialog) {
			this.callback = callback;
			this.isShowDialog = isShowDialog;
		}

		@Override
		protected void onPreExecute() {
			if (isShowDialog) {
				PromptManager.showCancelProgressDialog(BaseActivity.this, this);
			}
			super.onPreExecute();
		}

		/**
		 * 在这里执行异步任务。
		 */
		@Override
		protected abstract String doInBackground(String... params);

		/**
		 * 该方法替代execute方法，在执行前进行网络检测。
		 * 
		 * @param params
		 * @return
		 */
		public final AsyncTask<String, String, String> executeProxy(
				String... params) {
			if (NetUtil.isNetworkAvailable(BaseActivity.this)) {
				return super.execute(params);
			} else {
				if (canShowWebDialog) {
					PromptManager.showNoNetWork(BaseActivity.this);
				}else {
					return super.execute(params);
				}
				
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			PromptManager.closeCancelProgressDialog();
			if (callback != null) {
				callback.handleResult(result);
			}
		}
	}
	
	/**
	 * Toast短显示
	 * 
	 * @param int pResId
	 */
	protected void showShortToast(int pResId) {
		showShortToast(getApplicationContext().getString(pResId));
	}

	/**
	 * Toast短显示
	 * 
	 * @param String
	 *            pMsg
	 */
	protected void showShortToast(String pMsg) {
		Toast.makeText(getApplicationContext(), pMsg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toast长显示
	 * 
	 * @param int pResId
	 */
	protected void showLongToast(int pResId) {
		showLongToast(getApplicationContext().getString(pResId));
	}

	/**
	 * Toast长显示
	 * 
	 * @param String
	 *            pMsg
	 */
	protected void showLongToast(String pMsg) {
		Toast.makeText(getApplicationContext(), pMsg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
