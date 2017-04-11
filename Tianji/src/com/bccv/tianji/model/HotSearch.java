package com.bccv.tianji.model;

import java.io.Serializable;

public class HotSearch implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
String keyword;
String hit_num;
public String getKeyword() {
	return keyword;
}
public void setKeyword(String keyword) {
	this.keyword = keyword;
}
public String getHit_num() {
	return hit_num;
}
public void setHit_num(String hit_num) {
	this.hit_num = hit_num;
}

}
