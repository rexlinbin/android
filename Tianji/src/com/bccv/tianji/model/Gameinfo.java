package com.bccv.tianji.model;

import java.util.List;

public class Gameinfo {

	/**
	 * status----------系统状态码 
	 * data------------分类列表 
	 * game_id--------游戏ID
	 * title----------游戏名称 
	 * icons-----------游戏图标 
	 * point----------游戏评分
	 * pics-----------游戏截图 **数组形式**
	 * intro----------游戏简介
	 * down_num-------下载次数
	 * system_demand--系统要求 
	 * size-----------游戏大小 **G为单位** 
	 * send_times-----发布时间
	 * type_name------游戏类型 
	 * user_downloadstatus----用户对该游戏下载状态 
	 * 1 normal 2 waiting 3 stop 4 download 5complete 
	 * star_info------ 评星详情
	 * total_star-----总评星数量
	 * star1----------一星数量 star2----------二星数量 star3----------三星数量
	 * star4----------四星数量 star5----------五星数量
	 **/

	
	
	private String status;
	private String data;
	private String game_id;
	private String title;
	private String icons;
	private String point;
	private List<String >pics;
	private String intro;
	private String down_num;
	private String system_demand;
	private String game_size;
	private String send_times;
	private String type_name;
	private String user_downloadstatus;
	private String star_info;
	private String total_star;
	private String star1;
	private String star2;
	private String star3;
	private String star4;
	private String star5;
	private String task_id;
	float download_size;
	long optTime = 0L;

	public long getOptTime() {
		return optTime;
	}

	public void setOptTime(long optTime) {
		this.optTime = optTime;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIcons() {
		return icons;
	}
	public void setIcons(String icons) {
		this.icons = icons;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public List<String> getPics() {
		return pics;
	}
	public void setPics(List<String> pics) {
		this.pics = pics;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getDown_num() {
		return down_num;
	}
	public void setDown_num(String down_num) {
		this.down_num = down_num;
	}
	public String getSystem_demand() {
		return system_demand;
	}
	public void setSystem_demand(String system_demand) {
		this.system_demand = system_demand;
	}
	
	public String getGame_size() {
		return game_size;
	}

	public void setGame_size(String game_size) {
		this.game_size = game_size;
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
	public String getUser_downloadstatus() {
		return user_downloadstatus;
	}
	public void setUser_downloadstatus(String user_downloadstatus) {
		this.user_downloadstatus = user_downloadstatus;
	}
	public String getStar_info() {
		return star_info;
	}
	public void setStar_info(String star_info) {
		this.star_info = star_info;
	}
	public String getTotal_star() {
		return total_star;
	}
	public void setTotal_star(String total_star) {
		this.total_star = total_star;
	}
	public String getStar1() {
		return star1;
	}
	public void setStar1(String star1) {
		this.star1 = star1;
	}
	public String getStar2() {
		return star2;
	}
	public void setStar2(String star2) {
		this.star2 = star2;
	}
	public String getStar3() {
		return star3;
	}
	public void setStar3(String star3) {
		this.star3 = star3;
	}
	public String getStar4() {
		return star4;
	}
	public void setStar4(String star4) {
		this.star4 = star4;
	}
	public String getStar5() {
		return star5;
	}
	public void setStar5(String star5) {
		this.star5 = star5;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
