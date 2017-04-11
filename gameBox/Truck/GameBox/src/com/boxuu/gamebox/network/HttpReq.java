package com.boxuu.gamebox.network;

import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.boxuu.gamebox.utils.L;

/**
 * 处理网络请求的抽象类，异步发出网络请求，返回网络数据后并响应回调函数
 */
public abstract class HttpReq extends AsyncTask<Void, Integer, HttpResult> {

	private static final String TAG = "HttpReq";

	protected Context mContext;
	protected ReqestMethod reqMethod = ReqestMethod.GET;
	protected Map<String, Object> params = null;// 请求参数
	protected String mUrl = null;// 网络请求地址
	protected HttpCallback mCallBack = null;// 回调函数
	private int mServiceTag = -1;

	public void setServiceTag(int nTag) {
		mServiceTag = nTag;
	}

	public int getServiceTag() {
		return mServiceTag;
	}

	public void setURL(String url) {
		mUrl = null;
	}

	/**
	 * 设置请求方式
	 * 
	 * @param method
	 */
	public void setReqMethod(ReqestMethod method) {
		reqMethod = method;
	}

	/**
	 * 设置请求参数
	 * 
	 * @param params
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	/**
	 * 获取请求参数
	 * 
	 * @return
	 */
	public Map<String, Object> getParams() {
		return this.params;
	}

	/**
	 * 设置callback
	 * 
	 * @param callBack
	 */
	public void setCallBack(HttpCallback callBack) {
		this.mCallBack = callBack;
	}

	/**
	 * 返回callback
	 */
	protected HttpCallback getCallBack() {
		return mCallBack;
	}

	/**
	 * 响应网络请求得抽象函数
	 * 
	 * @param response
	 *            网络请求返回的数据
	 * @return Object 返回的数据对象
	 * @throws Exception
	 */
	protected abstract NetResBean processResponse(String response);

	/**
	 * 网络请求函数，该函数中通过判断网络请求的方式（post 或者 get） 发出网络请求，并返回响应的数据结果
	 * 
	 * @return Object 返回的数据对象
	 * @throws Exception
	 */
	public String runReq() {
		String response = null;
		L.v(TAG, "runReq", " url : " + mUrl);
		if (reqMethod == ReqestMethod.GET) {
			L.v(TAG, "runReq", " do http_get ");
			response = HttpUtil.http_get(mContext, mUrl, params);
		} else {
			L.v(TAG, "runReq", " do http_post ");
			response = HttpUtil.http_post(mContext, mUrl, params, null);
		}
		L.v(TAG, "runReq", " response : " + response);
		return response;
	}

	/**
	 * AsyncTask方法重载 预执行
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * AsyncTask方法重载 后台异步执行
	 */
	@Override
	protected HttpResult doInBackground(Void... params) {

		HttpResult httpResult = new HttpResult();
		if (!TextUtils.isEmpty(mUrl)
				&& (mUrl.startsWith("http://") || mUrl.startsWith("https://"))) {
			String result = this.runReq();
			httpResult.dataOk = !TextUtils.isEmpty(result);
			if (httpResult.dataOk) {
				httpResult.result = result;
			} else {
				httpResult.result = " time out ";
			}
		} else {
			httpResult.dataOk = false;
			httpResult.result = " url :  " + mUrl + "  is error ";
		}

		return httpResult;
	}

	/**
	 * AsyncTask方法重载 在任务执行成功时回调
	 */
	@Override
	protected void onPostExecute(HttpResult httpResult) {
		if (mCallBack != null) {
			if (httpResult.dataOk) {
				mCallBack.onResult(processResponse(httpResult.result));
			} else {
				mCallBack.onError(httpResult.result);
			}
		}
		HttpService.getInstance().onReqFinish(this);
	}

	/**
	 * AsyncTask方法重载 在任务成功取消时回调
	 */
	@Override
	protected void onCancelled() {
		if (mCallBack != null) {
			mCallBack.onCancel();
		}
		HttpService.getInstance().onReqFinish(this);
	}

	/**
	 * 请求方式
	 * 
	 * @author kliu
	 */
	public static enum ReqestMethod {
		GET, POST
	}

}
