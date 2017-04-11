package com.bccv.zhuiyingzhihanju.model;

public class Search {
	private SearchType movie;
	private SearchType tv;
	private SearchType variety;
	private SearchType music;
	private SearchType news;
	private SearchStar star;
	public SearchType getMovie() {
		return movie;
	}
	public void setMovie(SearchType movie) {
		this.movie = movie;
	}
	public SearchType getTv() {
		return tv;
	}
	public void setTv(SearchType tv) {
		this.tv = tv;
	}
	public SearchType getVariety() {
		return variety;
	}
	public void setVariety(SearchType variety) {
		this.variety = variety;
	}
	public SearchType getMusic() {
		return music;
	}
	public void setMusic(SearchType music) {
		this.music = music;
	}
	public SearchType getNews() {
		return news;
	}
	public void setNews(SearchType news) {
		this.news = news;
	}
	public SearchStar getStar() {
		return star;
	}
	public void setStar(SearchStar star) {
		this.star = star;
	}
	
	
}
