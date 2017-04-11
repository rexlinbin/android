package com.utils.tools;

import com.utils.net.NetUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	protected boolean canShowWebDialog = true;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected abstract class DataAsyncTask extends
			AsyncTask<String, String, String> {

		protected Callback callback;
		@SuppressWarnings("unused")
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
			if (NetUtil.isNetworkAvailable(BaseFragment.this.getActivity())) {
				return super.execute(params);
			} else {
				if (canShowWebDialog) {
					PromptManager
							.showNoNetWork(BaseFragment.this.getActivity());
				} else {
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
