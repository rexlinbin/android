package com.bccv.bangyangapp.download;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RemoteViews;

import com.bccv.bangyangapp.R;

public class Download implements Parcelable{

	/**
	 * 无效url
	 */
	public final static int DOWNLOAD_MALFORMED_URL_EXCEPTION = 1;
	/**
	 * IO异常
	 */
	public final static int DOWNLOAD_IO_EXCEPTION = 2;
	/**
	 * 数据正常
	 */
	public final static int DOWNLOAD_SUCCESS = 3;
	public final static int DOWNLOAD_CANCEL = 4;
	
	// 下载URL
	private String mDownloadUrl;
	// 下载文件名称
	private String mDownloadFileName;
	// 下载文件名称
	private String mDownloadFilePackageName;
	// 下载apk版本
	private int mDownloadFileVer;
	// 下载文件存储路径
	private String mDownloadStoragePath;
	// 是否安装
	private boolean mDownloadIsInstall;
	// 总长度
	private long mDownloadTotalSize;
	// 已经下载的长度
	private long mDownloadAlreadySize;
	// 错误信息
	private int mErrorCode;
	// 是否限速
	private boolean mDownloadIsLimitSpeed;
	// 异常信息                       
	private String mDownloadErrorMsg;
	//是否是apk
	private boolean isApk;
	//是否显示下载进度
	private boolean showProgress;
	//下载进度Tag  外部不用设置
	private int progressTag;
	//下载百分比  （百分之 分子部分）
	private int downloadProgress;
	// 通知栏显示下载进度
	private Notification mDownloadRunningNotification;
	public Download() {}
	
	public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>() {
		@Override
		public Download createFromParcel(Parcel in) {
			return new Download(in);
		}

		@Override
		public Download[] newArray(int size) {
			return new Download[size];
		}
	};
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mDownloadUrl);
		out.writeString(mDownloadFileName);
		out.writeString(mDownloadFilePackageName);
		out.writeInt(mDownloadFileVer);
		out.writeString(mDownloadStoragePath);
		out.writeInt(mDownloadIsInstall?1:0);
		out.writeLong(mDownloadTotalSize);
		out.writeLong(mDownloadAlreadySize);
		out.writeInt(mErrorCode);
		out.writeInt(mDownloadIsLimitSpeed?1:0);
		out.writeString(mDownloadErrorMsg);
		out.writeInt(isApk?1:0);
		out.writeInt(showProgress?1:0);
		out.writeInt(progressTag);
		out.writeInt(downloadProgress);
		out.writeParcelable(mDownloadRunningNotification, flags);
	}
	
	
	private Download(Parcel in) {
		mDownloadUrl = in.readString();
		mDownloadFileName = in.readString();
		mDownloadFilePackageName = in.readString();
		mDownloadFileVer = in.readInt();
		mDownloadStoragePath = in.readString();
		mDownloadIsInstall = in.readInt() == 1;
		mDownloadTotalSize = in.readLong();
		mDownloadAlreadySize = in.readLong();
		mErrorCode = in.readInt();
		mDownloadIsLimitSpeed = in.readInt() == 1;
		mDownloadErrorMsg = in.readString();
		isApk = in.readInt() == 1;
		showProgress = in.readInt() == 1;
		progressTag = in.readInt();
		downloadProgress = in.readInt();
		mDownloadRunningNotification = in.readParcelable(Notification.class
				.getClassLoader());
	}
	
	
	
	public String getDownloadFileName() {
		return mDownloadFileName;
	}


	public void setDownloadFileName(String mDownloadFileName) {
		this.mDownloadFileName = mDownloadFileName;
	}
	
	public String getDownloadFilePackageName() {
		return mDownloadFilePackageName;
	}
	
	public void setDownloadFilePackageName(String mDownloadFilePackageName) {
		this.mDownloadFilePackageName = mDownloadFilePackageName;
	}
	
	public int getDownloadFileVer() {
		return mDownloadFileVer;
	}
	
	
	public void setDownloadFileVer(int mDownloadFileVer) {
		this.mDownloadFileVer = mDownloadFileVer;
	}


	public String getDownloadUrl() {
		return mDownloadUrl == null ? "" : mDownloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {

		mDownloadUrl = downloadUrl;
	}
	
	public boolean getDownloadIsInstall() {
		return mDownloadIsInstall;
	}

	public void setDownloadIsInstall(boolean downloadIsInstall) {

		mDownloadIsInstall = downloadIsInstall;
	}
	
	public void setDownloadAlreadySize(long downloadAlreadySize) {

		mDownloadAlreadySize = downloadAlreadySize;
	}
	
	public long getDownloadAlreadySize(){
		
		return mDownloadAlreadySize;
	}
	
	public long getDownloadTotalSize() {
		return mDownloadTotalSize;
	}

	public void setDownloadTotalSize(long downloadTotalSize) {

		mDownloadTotalSize = downloadTotalSize;
	}

	public String getmDownloadUrl() {
		return mDownloadUrl;
	}

	public void setmDownloadUrl(String mDownloadUrl) {
		this.mDownloadUrl = mDownloadUrl;
	}

	public String getDownloadStoragePath() {
		return mDownloadStoragePath;
	}

	public void setDownloadStoragePath(String mDownloadStoragePath) {
		this.mDownloadStoragePath = mDownloadStoragePath;
	}

	public boolean isDownloadIsInstall() {
		return mDownloadIsInstall;
	}
	
	public int getErrorCode() {
		return mErrorCode;
	}

	public void setErrorCode(int mErrorCode) {
		this.mErrorCode = mErrorCode;
	}

	public boolean isDownloadIsLimitSpeed() {
		return mDownloadIsLimitSpeed;
	}

	public void setDownloadIsLimitSpeed(boolean mDownloadIsLimitSpeed) {
		this.mDownloadIsLimitSpeed = mDownloadIsLimitSpeed;
	}
	
	public boolean isApk() {
		return isApk;
	}
	
	public void setIsApk(boolean isApk) {
		this.isApk = isApk;
	}
	public boolean showProgress() {
		return showProgress;
	}
	
	public void setShowProgress(boolean showProgress) {
		this.showProgress = showProgress;
	}
	
	public int getProgressTag() {
		return progressTag;
	}
	
	public void setProgressTag(int progressTag) {
		this.progressTag = progressTag;
	}
	
	public int getDownloadProgress() {
		return downloadProgress;
	}
	
	public void setDownloadProgress(int downloadProgress) {
		this.downloadProgress = downloadProgress;
	}

	public String getDownloadErrorMsg() {
		return mDownloadErrorMsg;
	}

	public void setDownloadErrorMsg(String mDownloadErrorMsg) {
		this.mDownloadErrorMsg = mDownloadErrorMsg;
	}


	public void setDownloadNotification(Context context) {
		if (mDownloadRunningNotification == null) {
			mDownloadRunningNotification = getRunningNotification(context);

		}
	}
	
	/**
	 * 创建运行通知
	 */
	private Notification getRunningNotification(Context context) {

		Notification notification = new Notification();
		notification.icon = android.R.drawable.stat_sys_download;
		notification.when = System.currentTimeMillis();
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.flags = Notification.FLAG_NO_CLEAR;
		Intent intent = new Intent(context, context.getClass());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.contentIntent = contentIntent;
		notification.tickerText = mDownloadFileName;
		RemoteViews removeViews = new RemoteViews(context.getPackageName(),
				R.layout.notification_update);
		notification.contentView = removeViews;
		return notification;
	}
	
	public Notification getDownloadRunningNotification() {
		// L.v("download", "getDownloadNotification", "mDownloadNotification="
		// + mDownloadNotification);
		return mDownloadRunningNotification;
	}
	

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
