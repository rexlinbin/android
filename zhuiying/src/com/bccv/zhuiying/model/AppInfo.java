package com.bccv.zhuiying.model;

public class AppInfo {

	/**
	 * status 0 没有新升级，1有新版本 datadomain 内容接口url前缀 videodomain 视频播放地址解析url前缀
	 */
	int status;
	String datadomain;
	String videodomain;
	String tofriend;
	String msg;
	UpdateInfo data;
	int magnet;

	public UpdateInfo getData() {
		return data;
	}

	public void setData(UpdateInfo data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getMagnet() {
		return magnet;
	}

	public void setMagnet(int magnet) {
		this.magnet = magnet;
	}

	public String getTofriend() {
		return tofriend;
	}

	public void setTofriend(String tofriend) {
		this.tofriend = tofriend;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDatadomain() {
		return datadomain;
	}

	public void setDatadomain(String datadomain) {
		this.datadomain = datadomain;
	}

	public String getVideodomain() {
		return videodomain;
	}

	public void setVideodomain(String videodomain) {
		this.videodomain = videodomain;
	}

}
