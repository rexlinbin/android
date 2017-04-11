package com.bccv.meitu.model;

public class InstallAppInfo {

	private String packageName;
	private String version;
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
	
	@Override
	public String toString() {
		return "{package:" + packageName + ", version:"
				+ version + "}";
	}
	
}
