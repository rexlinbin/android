package com.utils.updatedownload;



public class Version{
	private String version;
	private int version_code;
	private String download;
	private String desString;
	
	public String getDesString() {
		return desString;
	}
	public void setDesString(String desString) {
		this.desString = desString;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public int getVersion_code() {
		return version_code;
	}
	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	@Override
	public String toString() {
		return "Version [version=" + version + ", version_code=" + version_code
				+ ", download=" + download + "]";
	}
	
}
