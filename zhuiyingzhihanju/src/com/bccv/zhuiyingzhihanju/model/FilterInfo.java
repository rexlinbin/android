package com.bccv.zhuiyingzhihanju.model;

import java.util.List;

public class FilterInfo {
List<Filter> order;
List<Filter> is_finish;
List<Filter> type;
List<Filter> year;
public List<Filter> getOrder() {
	return order;
}
public void setOrder(List<Filter> order) {
	this.order = order;
}
public List<Filter> getIs_finish() {
	return is_finish;
}
public void setIs_finish(List<Filter> is_finish) {
	this.is_finish = is_finish;
}
public List<Filter> getType() {
	return type;
}
public void setType(List<Filter> type) {
	this.type = type;
}
public List<Filter> getYear() {
	return year;
}
public void setYear(List<Filter> year) {
	this.year = year;
}


}
