package com.bccv.boxcomic.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.net.NetUtil;

/**
 * API客户端接口：用于访问网络数据
 */
public class ApiClient {

	public static final String UTF_8 = "UTF-8";// 编码
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int REQUEST_TIMEOUT = 20000;// 请求超时时间
	private final static int SO_TIMEOUT = 30000;// 连接超时时间
	private final static int RETRY_TIME = 3;// 重试次数
	private static final String TAG = "ApiClient";

	private static HttpClient getHttpClient() {

		BasicHttpParams httpParams = new BasicHttpParams();
		// 请求超时
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		// 连接超时
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		// 设置编码
		httpClient.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		
		Context context = MyApplication.getGlobalContext();
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			String str = MyApplication.getGlobalContext().getString(R.string.tel_str);
			Uri uri = Uri.parse(str);
			Cursor mCursor = null;
			try {
				mCursor = context.getContentResolver().query(uri, null, null, null, null);
				if (mCursor != null && mCursor.moveToFirst()) {
					String proxyStr = mCursor.getString(mCursor
							.getColumnIndex("proxy"));
					if (proxyStr != null && proxyStr.trim().length() > 0) {
						HttpHost proxy = new HttpHost(proxyStr, 80);
						httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
					}
				}
			} catch (Exception e) {
//				NetUtil.checkNetWork(ApplicationManager.getGlobalContext());
				
//				if(!TextUtils.isEmpty(NetUtil.PROXY_IP)){
//					HttpHost host = new HttpHost(NetUtil.PROXY_IP, NetUtil.PROXY_PORT);
//					httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
//				}
			} finally {
				if(null != mCursor) {
					mCursor.close();
				}
			}
		}
		return httpClient;
	}

	/**
	 * 获取httpGet
	 * @param url
	 * @return
	 */
	private static HttpGet getHttpGet(String url) {
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Connection", "Keep-Alive");
		httpget.setHeader("Accept", "*/*");
		httpget.setHeader("User-Agent", "(C)NokiaE5-00/SymbianOS/9.1 Series60/3.0");
		httpget.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		httpget.addHeader("Accept-Encoding", "gzip,deflate");
		return httpget;
	}

	/**
	 * 获取httpPost
	 * @param url
	 * @param cookie
	 * @param userAgent
	 * @return
	 */
	private static HttpPost getHttpPost(String url) {
		
		HttpPost httpPost = new  HttpPost(url);	
		httpPost.setHeader("Connection","Keep-Alive");
		httpPost.setHeader("Accept", "*/*");
		httpPost.setHeader("User-Agent", "(C)NokiaE5-00/SymbianOS/9.1 Series60/3.0");
		httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		httpPost.addHeader("Accept-Encoding", "gzip,deflate");
		return httpPost;
	}

	/**
	 * get请求URL
	 * @param url
	 * @param params
	 * @return
	 */
	public static InputStream http_get(String url, Map<String, Object> params) { 

		HttpClient httpClient = null;
		HttpGet httpGet = null;
		byte[] responseBody = null;

		if (null != params)
			url = _MakeURL(url, params);

		InputStream is = null;
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url);
				HttpResponse response = httpClient.execute(httpGet);
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK) {
					Logger.v(TAG, "statusCode error : " + statusCode);
					httpGet.abort();
					return null;
				}
				is = response.getEntity().getContent();
				if(is==null){
					httpGet.abort();
					return null;
				}
				responseBody = IS2byteArray(is);
				break;
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					Logger.v(TAG, "connection failed !  reconnection ...  times : " + time);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {}
					continue;
				}
				Logger.e( TAG, e.getMessage());
				return null;
			} finally {
				if (null != httpClient){
					httpClient.getConnectionManager().closeExpiredConnections();
					httpClient.getConnectionManager().shutdown();
				}
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		if(null!=responseBody)
			return new ByteArrayInputStream(responseBody);
		else
			return null;
	}
	
	/**
	 * post请求URL
	 * @param url
	 * @param params
	 * @return
	 */
	public static InputStream http_post(String url, Map<String, Object> params,Map<String,File> files){
		
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		byte[] responseBody = null;
		
		InputStream is = null;
		int time = 0;
		do{
			try{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url);
				 if(params!=null&&params.size()>0){
					 List<NameValuePair> parameters = new ArrayList<NameValuePair>();
					 Set<Entry<String,Object>> entrySet = params.entrySet();
					 for (Entry<String, Object> entry : entrySet) {
						 parameters.add(new BasicNameValuePair(entry.getKey(),entry.getValue().toString()));
					}
					 UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,"UTF-8");
					 httpPost.setEntity(entity);
				 }
				 HttpResponse response = httpClient.execute(httpPost);		        
		        int statusCode = response.getStatusLine().getStatusCode();
		        if(statusCode != HttpStatus.SC_OK) 
		        {
		        	Logger.v(TAG, "statusCode error : " + statusCode);
		        	httpPost.abort();
		        	return null;
		        }
		        is = response.getEntity().getContent();
		        if(is==null){
		        	httpPost.abort();
		        	return null;
		        }
		        responseBody = IS2byteArray(is);
		     	break;
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					Logger.v(TAG, "connection failed !  reconnection ...  times : " + time);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				
				Logger.e(TAG, e.getMessage());
				return null;
			} finally {
				if(null!=httpClient){
					httpClient.getConnectionManager().closeExpiredConnections();
					httpClient.getConnectionManager().shutdown();
				}
				httpClient = null;
			}
		}while(time < RETRY_TIME); 
		if(null!=responseBody)
			return new ByteArrayInputStream(responseBody);
        return null;
	}


	/**
	 * 将输入流转化为byte数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private static byte[] IS2byteArray(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inputStream.close();
		buffer = null;
		return outSteam.toByteArray();
	}

	/**
	 * IS2String
	 * @param inputStream
	 * @return
	 */
	public static String IS2String(InputStream inputStream){
		String str = null;
		try {
			byte[] data = IS2byteArray(inputStream);
			str = new String(data,"UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 拼接带参的url
	 * 
	 * @param p_url
	 * @param params
	 * @return
	 */
	public static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		for (String name : params.keySet()) {
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
		}
		return url.toString().replace("?&", "?");
	}
}
