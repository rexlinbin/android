package com.bccv.zhuiyingzhihanju.model;

import java.io.Serializable;
import java.util.List;

public class MovieInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *     id-----------------电影ID
           title--------------名称
           original_title-----原名
           directors----------导演
           casts--------------演员
           genres-------------分类ID(可能属于多个分类)
           genres_name--------分类名称
           year---------------影片上映时间
           countries----------所属国家
           rating-------------豆瓣评分
           summary------------简介
           images-------------封面图
           letter-------------字幕地址
           hit----------------播放次数
           episodes-----------总集数（当type=7（动画）  8（电视剧）  9（综艺）时 返回次字段  ）
           episodes_now-----------当前集数（当type=7（动画）  8（电视剧）  9（综艺）时 返回次字段  ）
           type_id------------分类id
           more---------------相同类别电影推荐
               id-----------------电影ID
               title--------------名称
               images-------------封面图
               rating-------------评分
               type_id------------所属分类ID
	 */
	
	String id;
	String title;
	String original_title;
	String directors;
	String casts;
	String genres;
	String genres_name;
	
	String year;
	String countries;
	String rating;
	String summary;
	String images;
	String bimages;
	String letter;
	String hit;
	String episodes;
	String episodes_now;
	String type_id;
	List<Movie> more;
	String share;
	String des;
	int like;
String up_name;
 String comments_count;
 Long utime;
 
 

	public Long getUtime() {
	return utime;
}
public void setUtime(Long utime) {
	this.utime = utime;
}
	public String getUp_name() {
	return up_name;
}
public void setUp_name(String up_name) {
	this.up_name = up_name;
}
	public String getBimages() {
		return bimages;
	}
	public void setBimages(String bimages) {
		this.bimages = bimages;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getShare() {
		return share;
	}
	public void setShare(String share) {
		this.share = share;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOriginal_title() {
		return original_title;
	}
	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public String getCasts() {
		return casts;
	}
	public void setCasts(String casts) {
		this.casts = casts;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getGenres_name() {
		return genres_name;
	}
	public void setGenres_name(String genres_name) {
		this.genres_name = genres_name;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCountries() {
		return countries;
	}
	public void setCountries(String countries) {
		this.countries = countries;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
	}
	public String getLetter() {
		return letter;
	}
	public void setLetter(String letter) {
		this.letter = letter;
	}
	public String getHit() {
		return hit;
	}
	public void setHit(String hit) {
		this.hit = hit;
	}
	public String getEpisodes() {
		return episodes;
	}
	public void setEpisodes(String episodes) {
		this.episodes = episodes;
	}
	public String getEpisodes_now() {
		return episodes_now;
	}
	public void setEpisodes_now(String episodes_now) {
		this.episodes_now = episodes_now;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public List<Movie> getMore() {
		return more;
	}
	public void setMore(List<Movie> more) {
		this.more = more;
	}
	public String getComments_count() {
		return comments_count;
	}
	public void setComments_count(String comments_count) {
		this.comments_count = comments_count;
	}
	@Override
	public String toString() {
		return "MovieInfo [id=" + id + ", title=" + title + ", original_title=" + original_title + ", directors="
				+ directors + ", casts=" + casts + ", genres=" + genres + ", genres_name=" + genres_name + ", year="
				+ year + ", countries=" + countries + ", rating=" + rating + ", summary=" + summary + ", images="
				+ images + ", letter=" + letter + ", hit=" + hit + ", episodes=" + episodes + ", episodes_now="
				+ episodes_now + ", type_id=" + type_id + ", more=" + more + ", share=" + share + "]";
	}
	
}
