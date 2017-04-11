package com.bccv.boxcomic.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.bccv.boxcomic.net.NetUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

public class Statistics {
	private static String projectName;
	private static String appName;
	private static String urlString = "http://update.bccv.com/update/getup/index/";

	// 当前正在处于连接的APN信息
	private static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	private static HttpPost post;
	private static String PROXY_IP;
	private static int PROXY_PORT;

	private static HttpResponse response;

	private static HttpClient client;
	private static Header[] headers = new Header[13];

	private static Context context;
	private static Callback callback;

	public static void init(Context contexta, String projectNameString,
			String appNameString, Callback myCallback) {
		projectName = projectNameString;
		appName = appNameString;
		context = contexta;
		callback = myCallback;
		try {
			ContentResolver contentResolver = context.getContentResolver();
			Cursor query = contentResolver.query(PREFERRED_APN_URI, null, null,
					null, null);
			if (query != null && query.moveToFirst()) {
				// 读取ip和port
				PROXY_IP = query.getString(query.getColumnIndex("proxy"));
				PROXY_PORT = query.getInt(query.getColumnIndex("port"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		client = new DefaultHttpClient();

		// 判断是否读取到了ip信息（代理）
		if (!StringUtils.isEmpty(PROXY_IP)) {
			// wap方式上网
			HttpHost host = new HttpHost(PROXY_IP, PROXY_PORT);
			client.getParams()
					.setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
			
		}

		// 设置头的数据
		
		// 设置头的数据
				headers[0] = new BasicHeader("Appkey", "12343");
				headers[1] = new BasicHeader("Udid", "");// 手机串号
				headers[2] = new BasicHeader("Os", "android");//
				headers[3] = new BasicHeader("Osversion", "");//
				headers[4] = new BasicHeader("Appversion", "");// 1.0
				headers[5] = new BasicHeader("Sourceid", "");//
				headers[6] = new BasicHeader("Ver", "");

				headers[7] = new BasicHeader("Userid", "");
				headers[8] = new BasicHeader("Usersession", "");

				headers[9] = new BasicHeader("Unique", "");
				
				long time = System.currentTimeMillis();
				
				headers[10] = new BasicHeader("key", MD5Util.string2MD5(time + GlobalParams.md5String));
				headers[11] = new BasicHeader("secret", time + "");
				headers[12] = new BasicHeader("user-agent", "BCCV/" + projectNameString);
	}

	public static void getUpdate() {
		new DataAsyncTask(callback, false) {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				return sendPost();
			}
		}.executeProxy("");
	}

	public static String sendPost() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("app_type", "1" + "");
		params.put("app_name", getKeyCode() + "");
		params.put("app_pname", getPhoneBrand() + "");
		params.put("app_os", getSDKVersion() + "");
		params.put("app_imei", getPhoneUdid() + "");

		params.put("app_ver", getPackageInfo().versionName + "");
		params.put("app_upver", String.valueOf(getPackageInfo().versionCode)
				+ "");
		params.put("app_channel", getChannelCode() + "");

		int currentNetType = getCurrentNetType();
		switch (currentNetType) {
		case 1:// wifi
			params.put("app_net", "wifi" + "");
			break;
		case 2:// 2g
			params.put("app_net", "2g" + "");
			break;
		case 3:// 3g
			params.put("app_net", "3g" + "");
			break;
		case 0:// 无网络
				// TODO 没有网络 不做上报

			return null;
		default:
			break;
		}

		return sendPostNoSecret(urlString, params);
	}

	/**
	 * 发送post请求
	 * 
	 * @param uri
	 * @param params
	 *            以Map形式传递参数
	 * @return
	 */
	public static String sendPostNoSecret(String uri, Map<String, String> params) {
		post = new HttpPost(uri);

		post.setHeaders(headers);
		if (params != null && params.size() > 0) {

			String paramString = "{";

			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				paramString += "\"" + item.getKey() + "\":" + "\""
						+ item.getValue() + "\",";
			}
			paramString = paramString.substring(0, paramString.length() - 2)
					+ "\"}";

			BasicNameValuePair pair = new BasicNameValuePair("code",
					paramString);
			parameters.add(pair);

			try {
				HttpEntity entity = new UrlEncodedFormEntity(parameters,
						"UTF-8");
				post.setEntity(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			response = client.execute(post);
			// 判断是否成功处理
			if (response.getStatusLine().getStatusCode() == 200) {
				// EntityUtils//把服务器端给你的信息变成字符串
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取app的信息
	 * 
	 * @param packageName
	 * @return
	 */
	public static PackageInfo getPackageInfo() {

		try {
			PackageManager packageManager = context.getPackageManager();
			return packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * 获取手机厂商
	 * 
	 * @return
	 */
	public static String getPhoneBrand() {
		return android.os.Build.MANUFACTURER + "_" + android.os.Build.MODEL;
	}

	/**
	 * 获取系统版本
	 * 
	 * @return
	 */
	public static String getSDKVersion() {
		return "Andriod" + android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取渠道号
	 * 
	 * @return
	 */
	public static String getChannelCode() {
		String code = "own";
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			code = info.metaData.getString("InstallChannel");
			if (code == null) {
				code = info.metaData.getInt("InstallChannel") + "";
			}
		} catch (Exception e) {
			code = "own";
		}
		return code;
	}

	/**
	 * 获取唯一应用标识
	 * 
	 * @return
	 */
	public static String getKeyCode() {
		String code = "own";
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			code = info.metaData.getString("TA_APPKEY");
		} catch (Exception e) {
			code = "own";
		}
		return code;
	}
	
	/**
	 * 获取手机 udid
	 * 
	 * @return udid
	 */
	public static String getPhoneUdid() {

		String udidStr = "";
		udidStr = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		return udidStr;
	}

	/**
	 * 
	 * @param context
	 * @return 1 : wifi 2:gprs 3:3g 0: no net
	 */
	public static int getCurrentNetType() {
		if (null == context) {
			return 0;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);// 获取系统的连接服务
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();// 获取网络的连接情况
		if (null != activeNetInfo) {
			if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				// 判断WIFI网
				return 1;
			} else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager mTelephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				int type = mTelephonyManager.getNetworkType();
				if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN
						|| type == TelephonyManager.NETWORK_TYPE_GPRS
						|| type == TelephonyManager.NETWORK_TYPE_EDGE) {
					// 判断gprs网
					return 2;
				} else {
					// 判断3g网
					return 3;
				}
			}
		}
		return 0;
	}

	protected static abstract class DataAsyncTask extends
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

			if (NetUtil.isNetworkAvailable(context)) {
				return super.execute(params);
			} else {
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (isShowDialog) {
				PromptManager.closeProgressDialog();
			}

			if (callback != null) {
				callback.handleResult(result);
			}
		}
	}

}
