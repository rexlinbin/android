package com.bccv.strategy.model;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class InstallAppInfo implements Parcelable{

	private String appName;
	private String packageName;
	private String version;
	private String iconUrl;
	private String introduce;	//简介 用于网络数据
	private Drawable icon;
	
	
	
	public InstallAppInfo(){}
	private InstallAppInfo(Parcel in){
//		private String appName;
//		private String packageName;
//		private String version;
//		private String iconUrl;
//		private String introduce;	//简介 用于网络数据
//		private Bitmap icon;
		
		appName = in.readString();
		packageName = in.readString();
		version = in.readString();
		iconUrl = in.readString();
		introduce = in.readString();
//		icon = in.readParcelable(null);
		
	}
	

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void setDrawable(Drawable icon){
		this.icon = icon;
	}
	
	public Drawable getIcon(){
		return icon;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Override
	public String toString() {
		return "InstallAppInfo [appName=" + appName + ", packageName="
				+ packageName + ", version=" + version + ", icon=" + icon
				+ ", iconUrl=" + iconUrl + ", introduce=" + introduce + "]";
	}
	
	
	public static final Parcelable.Creator<InstallAppInfo> CREATOR = new Parcelable.Creator<InstallAppInfo>() {
		@Override
		public InstallAppInfo createFromParcel(Parcel in) {
			return new InstallAppInfo(in);
		}

		@Override
		public InstallAppInfo[] newArray(int size) {
			return new InstallAppInfo[size];
		}
	};
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		
//		private String appName;
//		private String packageName;
//		private String version;
//		private String iconUrl;
//		private String introduce;	//简介 用于网络数据
//		private Bitmap icon;
		
		out.writeString(appName);
		out.writeString(packageName);
		out.writeString(version);
		out.writeString(iconUrl);
		out.writeString(introduce);
//		out.writeParcelable(icon, flags);
		
	}

	

}
