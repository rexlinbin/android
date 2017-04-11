package com.bccv.meitu.upload;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.mime.CustomMultiPartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.bccv.meitu.localphoto.bean.UpdatePhotoInfo;
import com.bccv.meitu.upload.UploadFileTask.UploadProgressListener;
import com.bccv.meitu.utils.Logger;

public class UploadFileNetUtil {

	public static final String UTF_8 = "UTF-8";// 编码
	public static final String DESC = "descend";
	public static final String ASC = "ascend";

	private final static int REQUEST_TIMEOUT = 20000;// 请求超时时间
	private final static int SO_TIMEOUT = 8000;// 连接超时时间
	private static final String TAG = "UploadFileNetUtil";
	
	private static HttpClient getHttpClient(Context mContext) {

		BasicHttpParams httpParams = new BasicHttpParams();
		// 请求超时
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		// 连接超时
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		// 设置编码
		httpClient.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		
		Context context = mContext;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			String str = "content://telephony/carriers/preferapn";
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
				NetUtil.checkNetWork(mContext);
				
				if(!TextUtils.isEmpty(NetUtil.PROXY_IP)){
					HttpHost host = new HttpHost(NetUtil.PROXY_IP, NetUtil.PROXY_PORT);
					httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
				}
			} finally {
				if(null != mCursor) {
					mCursor.close();
				}
			}
		}
		return httpClient;
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
//		httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
		httpPost.addHeader("Accept-Encoding", "gzip,deflate");
		return httpPost;
	}
	
	public static String uploadFile(Context context,String RequestURL,File file,Map<String,String> params,UploadProgressListener listener)  {
		
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		
		try {
			httpClient = getHttpClient(context);
			httpPost = getHttpPost(RequestURL);
			HttpContext httpContext = new BasicHttpContext();
			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(listener);
	
			//TODO 下载拼接下载参数
	        if(params!=null){
	        	Set<Entry<String,String>> entrySet = params.entrySet();
	        	for (Entry<String, String> entry : entrySet) {
	        		multipartContent.addPart(entry.getKey(), new StringBody(entry.getValue()));
				}
	        }
	        
			// We use FileBody to transfer an image
			multipartContent.addPart("photo", new FileBody(file));
			listener.setTotalSize(multipartContent.getContentLength());
			
			httpPost.setEntity(multipartContent);
			HttpResponse response = httpClient.execute(httpPost, httpContext);
			int statusCode = response.getStatusLine().getStatusCode();
			
			Logger.v(TAG, "uploadFile", "statusCode : " + statusCode);
			
			String serverResponse = EntityUtils.toString(response.getEntity());
			Logger.v(TAG, "uploadFile", "serverResponse : " + serverResponse);
			if(file.exists()){
				file.delete();
			}
			if(statusCode!=200){
				Logger.v(TAG, "uploadFile", "serverResponse : " + serverResponse);
				//TODO 上传失败 
	        	return serverResponse;
			}
			return serverResponse;
		} catch (Exception e) {
			Logger.v(TAG, "uploadFile", e.getClass().getSimpleName());
			e.printStackTrace();
		}finally{
			if(httpPost!=null){
				httpPost.abort();
			}
		}
		return null;
	}
	
    /**
     * 上传监听
     * 
     * @author liukai
     * @date 2014-12-3
     */
    public interface UploadListener{
    	void updateUploadProgress(long total,long current);
    	void uploadAction(UploadAction action,UpdatePhotoInfo photoInfo);
    }
    
    public enum UploadAction{
    	PREEXECUTE,		//上传前action
    	CANCEL,			//上传取消action
    	UPLOAD_SUCCESS,	//上传成功action
    	UPLOAD_FAILED	//上传失败action
    }
	
}
