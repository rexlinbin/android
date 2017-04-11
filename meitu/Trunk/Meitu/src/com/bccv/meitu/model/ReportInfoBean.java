package com.bccv.meitu.model;

/**
 * @Description: 上报信息
 * @author liukai
 * @date 2014-12-23
 */
public class ReportInfoBean {

	private String app_name;	//应用名
	private String app_pname;	//机型
	private String app_os;		//系统
	private String app_imei;	//udid
	private String app_net;		//网络类型
	private String app_ver;		//应用版本名
	private String app_upver;	//应用版本号
	private String app_channel;	//渠道
	private String app_type = "1";	//安卓 1 ios 2
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_pname() {
		return app_pname;
	}
	public void setApp_pname(String app_pname) {
		this.app_pname = app_pname;
	}
	public String getApp_os() {
		return app_os;
	}
	public void setApp_os(String app_os) {
		this.app_os = app_os;
	}
	public String getApp_imei() {
		return app_imei;
	}
	public void setApp_imei(String app_imei) {
		this.app_imei = app_imei;
	}
	public String getApp_net() {
		return app_net;
	}
	public void setApp_net(String app_net) {
		this.app_net = app_net;
	}
	public String getApp_ver() {
		return app_ver;
	}
	public void setApp_ver(String app_ver) {
		this.app_ver = app_ver;
	}
	public String getApp_upver() {
		return app_upver;
	}
	public void setApp_upver(String app_upver) {
		this.app_upver = app_upver;
	}
	public String getApp_channel() {
		return app_channel;
	}
	public void setApp_channel(String app_channel) {
		this.app_channel = app_channel;
	}
	public String getApp_type() {
		return app_type;
	}
	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}
	
	@Override
	public String toString() {
		return "ReportInfoBean [app_name=" + app_name + ", app_pname="
				+ app_pname + ", app_os=" + app_os + ", app_imei=" + app_imei
				+ ", app_net=" + app_net + ", app_ver=" + app_ver
				+ ", app_upver=" + app_upver + ", app_channel=" + app_channel
				+ ", app_type=" + app_type + "]";
	}

}
