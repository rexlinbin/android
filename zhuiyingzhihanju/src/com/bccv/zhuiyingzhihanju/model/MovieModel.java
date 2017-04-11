package com.bccv.zhuiyingzhihanju.model;

import java.util.List;

public class MovieModel {

	
	List<Movie>slide;
	List<Movie>hot;
	MovieNews coming;
	List<Star> star;
	MovieNews sidelight;
	MovieNews high;
	public List<Movie> getSlide() {
		return slide;
	}
	public void setSlide(List<Movie> slide) {
		this.slide = slide;
	}
	public List<Movie> getHot() {
		return hot;
	}
	public void setHot(List<Movie> hot) {
		this.hot = hot;
	}
	public MovieNews getComing() {
		return coming;
	}
	public void setComing(MovieNews coming) {
		this.coming = coming;
	}
	public List<Star> getStar() {
		return star;
	}
	public void setStar(List<Star> star) {
		this.star = star;
	}
	public MovieNews getSidelight() {
		return sidelight;
	}
	public void setSidelight(MovieNews sidelight) {
		this.sidelight = sidelight;
	}
	public MovieNews getHigh() {
		return high;
	}
	public void setHigh(MovieNews high) {
		this.high = high;
	}
	@Override
	public String toString() {
		return "MovieModel [slide=" + slide + ", hot=" + hot + ", coming=" + coming + ", star=" + star + ", sidelight="
				+ sidelight + ", high=" + high + "]";
	}



	
	
	
}
