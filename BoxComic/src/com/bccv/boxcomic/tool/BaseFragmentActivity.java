package com.bccv.boxcomic.tool;

import com.bccv.boxcomic.net.NetUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class BaseFragmentActivity extends FragmentActivity {
	protected boolean canShowWebDialog = true;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		AppManager.getAppManager().addActivity(this);
		GlobalParams.activity = this;
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
			if (NetUtil.isNetworkAvailable(BaseFragmentActivity.this)) {
				return super.execute(params);
			} else {
				if (canShowWebDialog) {
					PromptManager.showNoNetWork(BaseFragmentActivity.this);
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
}
