package com.bccv.tianji.model;

public class Game {
	/**
	 * ----------系统状态码; total-----------总页数 data------------分类列表
	 * game_id-----------游戏ID icons---------游戏图标 title--------游戏名称
	 * point--------游戏评分 size---------游戏大小（单位：G） send_times---发布时间
	 * type_name----所属类别名称 down_num-----下载次数 user_downloadstatus----用户对该游戏下载状态 1
	 * normal 2 waiting 3 stop 4 download 5 complete
	 * 
	 * */

	String status;
	String total;
	String data;
	String game_id;
	String icons;
	String title;
	String point;
	String game_size;
	String send_times;
	String type_name;
	String down_num;
	String user_downloadstatus;
	float download_size;
	String task_id;
	boolean isSelect = false;
	long optTime = 0L;
	

	public long getOptTime() {
		return optTime;
	}

	public void setOptTime(long optTime) {
		this.optTime = optTime;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public float getDownload_size() {
		return download_size;
	}

	public void setDownload_size(float download_size) {
		this.download_size = download_size;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getIcons() {
		return icons;
	}

	public void setIcons(String icons) {
		this.icons = icons;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	

	public String getSend_times() {
		return send_times;
	}

	public void setSend_times(String send_times) {
		this.send_times = send_times;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getDown_num() {
		return down_num;
	}

	public void setDown_num(String down_num) {
		this.down_num = down_num;
	}

	public String getUser_downloadstatus() {
		return user_downloadstatus;
	}

	public void setUser_downloadstatus(String user_downloadstatus) {
		this.user_downloadstatus = user_downloadstatus;
	}

	public String getGame_size() {
		return game_size;
	}

	public void setGame_size(String game_size) {
		this.game_size = game_size;
	}


}
