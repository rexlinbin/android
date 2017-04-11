package com.bccv.meitu.download;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.Random;

import android.os.Handler;
import android.os.Message;

import com.bccv.meitu.model.Download;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.SystemUtil;

public class DownloadRunnable implements Runnable {

	public static final String TAG = "DownloadRunnable";
	
	/**
	 * 连接超时60秒
	 */
	private static final int mConnectTimeout = 60 * 1000;
	/**
	 * 读取超时30秒
	 */
	private static final int mReadTimeout = 30 * 1000;
	/**
	 * 重试次数
	 */
	private static final int RETRY_MAX_NUM = 3;
	
	// 当前重试次数
	private int mCurrentRetryNum = 0;
	
	private Download mDownload = null;
	private Handler mHandler;
	
	public DownloadRunnable(Download download, Handler handler){
		this.mDownload = download;
		this.mHandler = handler;
		setProgressTag();
	}
	
	// 是否停止
	private boolean mIsStop = false;
	
	public boolean isIsStop() {
		return mIsStop;
	}

	public void setIsStop(boolean isStop) {

		mIsStop = isStop;
	}
	
	private void setProgressTag(){
		if(mDownload.showProgress()){
			Random random = new Random();
			int tag = random.nextInt(Integer.MAX_VALUE);
			mDownload.setProgressTag(tag);
		}
	}
	
	@Override
	public void run() {
		mIsStop = false;
		//TODO 下载逻辑
		
		StringBuilder filePath = new StringBuilder();
		if(mDownload.isApk()){
			filePath.append(SystemUtil
					.getFoneCacheFolder(SystemUtil.DOWNLOAD_APK_TYPE));
		}else{
			filePath.append(SystemUtil
					.getFoneCacheFolder(SystemUtil.DOWNLOAD_IMAGE_TYPE));
		}
		filePath.append(File.separator);
		filePath.append(mDownload.getDownloadFileName());
		mDownload.setDownloadStoragePath(filePath.toString());
		
		Logger.v(TAG, "run", "filePath : " + filePath.toString());
		
		if (mDownload.isDownloadIsLimitSpeed()) {

			// 限速100KB/s
			limitSpeedDownloadFile(100);

		} else {
			// 全速下载
			limitSpeedDownloadFile(0);
		}
		
	}
	
	
	/**
	 * 下载apk
	 * @param speed
	 */
	private void limitSpeedDownloadFile(long speed){
		Logger.v(TAG, "limitSpeedDownloadFile",
				"start url=" + mDownload.getDownloadUrl());
		
		RandomAccessFile randomAccessFile = null;
		InputStream inputStream = null;
		long length = 0;
		
		try{
			URL url = new URL(mDownload.getDownloadUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置超时时间
			conn.setConnectTimeout(mConnectTimeout);
			conn.setReadTimeout(mReadTimeout);
			if (conn.getResponseCode() == 200) {
				length = conn.getContentLength();
				mDownload.setDownloadTotalSize(length);
			}
			
			File fileDir =null;
			if(mDownload.isApk()){
				fileDir = new File(SystemUtil
						.getFoneCacheFolder(SystemUtil.DOWNLOAD_APK_TYPE));
			}else{
				fileDir = new File(SystemUtil
						.getFoneCacheFolder(SystemUtil.DOWNLOAD_IMAGE_TYPE));
			}
			
			if(!fileDir.exists()){
				fileDir.mkdirs();
			}
			
			//TODO 判断是否有曾未下载完成的apk
			File file = new File(mDownload.getDownloadStoragePath());
			if (!file.exists()) { // TODO 如果文件不存在

				Logger.v(TAG, "limitSpeedDownloadFile", "create new file path="
						+ mDownload.getDownloadStoragePath());
				file.delete();
				file.createNewFile();
				mDownload.setDownloadAlreadySize(0);
				mDownload.setDownloadTotalSize(length);
			}else{
				if(file.length()==mDownload.getDownloadTotalSize()){
					Logger.v(TAG, "limitSpeedDownloadFile", "file.length()==mDownload.getDownloadTotalSize() : true");
					mDownload.setDownloadAlreadySize(file.length());
				}else{
					Logger.v(TAG, "limitSpeedDownloadFile", "file.length()==mDownload.getDownloadTotalSize() : false");
					file.delete();
					file.createNewFile();
					mDownload.setDownloadAlreadySize(0);
					mDownload.setDownloadTotalSize(length);
				}
			}
			
			// 开始位置
			long start = file.length();
			
			if(start==length){
				mDownload.setErrorCode(Download.DOWNLOAD_SUCCESS);
				return;
			}
			
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(mConnectTimeout);
			httpURLConnection.setReadTimeout(mReadTimeout);

			
			// 获取指定位置的数据，Range范围如果超出服务器上数据范围, 会以服务器数据末尾为准
			httpURLConnection.setRequestProperty("Range", "bytes=" + start
					+ "-" + length);
			randomAccessFile = new RandomAccessFile(file, "rws");

			randomAccessFile.seek(start);
			
			// 开始读写数据
			inputStream = httpURLConnection.getInputStream();
			int pack = 10240; // 10K bytes
			speed = speed * 1024;// 将速度换算成字节
			int sleep = 0;
			if (speed != 0) {
				sleep = (int) Math.floor(1000 * pack / speed) + 1;
				Logger.v(TAG, "limitSpeedDownloadFile", "speed=" + speed + " sleep="
						+ sleep + " url=" + mDownload.getDownloadUrl());
			}
			int maxCount = (int) Math.floor((length - start) / pack) + 1;
			Logger.v(TAG, "limitSpeedDownloadFile", "maxCount=" + maxCount + " url="
					+ mDownload.getDownloadUrl());
//			int oldPrecent = 0;
			byte[] buf = new byte[1024 * 10];
			int len;
			
			int currentProgress = (int)((((double)mDownload.getDownloadAlreadySize())/((double)mDownload.getDownloadTotalSize()))*100);
			if(mDownload.showProgress()){
				//TODO 发送消息修改UI
				mDownload.setDownloadProgress(currentProgress);
				Message msg = mHandler.obtainMessage();
				msg.obj = mDownload;
				msg.what = DownloadManager.SHOW_PROGRESS;
				msg.sendToTarget();	
			}
			
			int showProgress = (int)(100l/(mDownload.getDownloadTotalSize()/((double)(buf.length*50))));
			
			while ((len = inputStream.read(buf)) != -1) {
				
				// 判断是否停止
				if (mIsStop) {
					break;
				}
				if (file.canWrite()) {
					randomAccessFile.write(buf, 0, len);
				} else {
					
					Logger.e(TAG, "limitSpeedDownloadFile", "DOWNLOAD_IO_EXCEPTION"
							+ mDownload.getDownloadFileName()
							+ "下载失败,本地路径不能写数据");
					// 不能写数据,io异常(usb存储模式)
					throw new IOException(" write to file failed !!! ");
				}
				
				long currentLength = mDownload.getDownloadAlreadySize() + len;
				if (currentLength > mDownload.getDownloadTotalSize()) {
					//TODO 出问题了
					throw new IOException(" error : currentLength > TotalSize !!! ");
				}
				
				int progress =  (int)((((double)currentLength)/((double)mDownload.getDownloadTotalSize()))*100);
				Logger.i(TAG, "", progress + "%  showProgress :"+showProgress);
				
				if(showProgress<=100 && showProgress >= 1 && currentProgress!=progress && progress%showProgress==0 && mDownload.showProgress()){
					//TODO 发送消息修改UI
					Logger.i(TAG, "download progress", "updata progress !!! :" + progress);
					mDownload.setDownloadProgress(progress);
					Message msg = mHandler.obtainMessage();
					msg.obj = mDownload;
					msg.what = DownloadManager.SHOW_PROGRESS;
					msg.sendToTarget();	
						
				}
				currentProgress = (currentProgress!=progress) ? progress : currentProgress;
				
				mDownload.setDownloadAlreadySize(currentLength);
				
				if (sleep != 0) {
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {}
				}
			}
			
			mDownload.setDownloadAlreadySize(mDownload
					.getDownloadTotalSize());
			mDownload.setDownloadTotalSize(length);
			mDownload.setErrorCode(Download.DOWNLOAD_SUCCESS);
			
		} catch (SocketException e) {
			if (e != null) {
				e.printStackTrace();
			}
			mCurrentRetryNum++;
			if (mCurrentRetryNum <= RETRY_MAX_NUM) {
				Logger.w(TAG, "limitSpeedDownloadFile",
						"SocketException mCurrentRetryNum=" + mCurrentRetryNum);
				// 重试
				limitSpeedDownloadFile(speed);

			} else {
				//TODO 连接失败 下载失败
				Logger.e(TAG, "limitSpeedDownloadFile", "SocketException "
						+ mDownload.getDownloadFileName() + "下载失败,连接地址失效");
				mDownload.setErrorCode(Download.DOWNLOAD_IO_EXCEPTION);
				mDownload.setDownloadErrorMsg("连接失败");
			}

		}catch (MalformedURLException e) {
			if (e != null) {
				e.printStackTrace();
			}
			//TODO 无效的url 下载失败
			Logger.e(TAG, "limitSpeedDownloadFile", "MalformedURLException "
					+ mDownload.getDownloadFileName() + "下载失败,连接格式或路径错误");
			mDownload.setErrorCode(Download.DOWNLOAD_MALFORMED_URL_EXCEPTION);
			mDownload.setDownloadErrorMsg("url无效");
			
		} catch (IOException e) {
			if (e != null) {
				e.printStackTrace();
			}
			//TODO 文件写入失败  下载失败
			if (e instanceof FileNotFoundException) {
				Logger.e(TAG, "limitSpeedDownloadFile", "FileNotFoundException "
						+ mDownload.getDownloadFileName() + "下载失败,连接地址失效");
			} else {
				Logger.e(TAG, "limitSpeedDownloadFile", "DOWNLOAD_IO_EXCEPTION e="
						+ e + mDownload.getDownloadFileName()
						+ "升级包下载失败 downloadUrl=" + mDownload.getDownloadUrl());
			}
			mDownload.setErrorCode(Download.DOWNLOAD_IO_EXCEPTION);
			mDownload.setDownloadErrorMsg("写入文件失败");
			
		} catch(Exception e){
			if (e != null) {
				e.printStackTrace();
			}
			mDownload.setErrorCode(Download.DOWNLOAD_IO_EXCEPTION);
			mDownload.setDownloadErrorMsg("下载失败");
			
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					if (e != null) {
						e.printStackTrace();
					}
				}
			}
			if (randomAccessFile != null) {
				try {
					randomAccessFile.close();
				} catch (IOException e) {
					if (e != null) {
						e.printStackTrace();
					}
				}
			}
			
			Message msg = mHandler.obtainMessage();
			msg.obj = mDownload;
			if(mDownload.getErrorCode()==Download.DOWNLOAD_SUCCESS){
				msg.what = DownloadManager.DOWNLOAD_SUCCESS;
			}else{
				msg.what = DownloadManager.DOWNLOAD_FAILED;
			}
			msg.sendToTarget();
		}
	}
}
