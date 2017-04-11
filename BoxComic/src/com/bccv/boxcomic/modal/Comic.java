package com.bccv.boxcomic.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;


public class Comic implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String comic_id;
	private String comic_title;
	private String comic_titlepic;
	private String comic_author;
	private int comic_click;
	private int comic_comment;
	private int comic_good;
	private String comic_intro;
	private long comic_utime;
	private String comic_finish;
	private int comic_digg;
	
//	private int chapterNum;
	private int historyChapter;//阅读到哪
	private int historyChapterPage;
	private String historyChapterTitleString;
	
	private List<Chapter> chapters = new ArrayList<Chapter>();
	
	private int advices;
	
	private boolean isComic = true;
	
	private List<OnlineFrom> onlineFroms = new ArrayList<OnlineFrom>();
	
	private String comic_FromString = "";
	
	private boolean isLocal = false;
	
	
	
	public boolean isLocal() {
		return isLocal;
	}
	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}
	public String getComic_finish() {
		return comic_finish;
	}
	public void setComic_finish(String comic_finish) {
		this.comic_finish = comic_finish;
	}
	public String getComic_FromString() {
		return comic_FromString;
	}
	public void setComic_FromString(String comic_FromString) {
		this.comic_FromString = comic_FromString;
	}
	public List<OnlineFrom> getOnlineFroms() {
		return onlineFroms;
	}
	public void setOnlineFroms(List<OnlineFrom> onlineFroms) {
		this.onlineFroms = onlineFroms;
	}
	public boolean isComic() {
		return isComic;
	}
	public void setComic(boolean isComic) {
		this.isComic = isComic;
	}
	public String getHistoryChapterTitleString() {
		return historyChapterTitleString;
	}
	public void setHistoryChapterTitleString(String historyChapterTitleString) {
		this.historyChapterTitleString = historyChapterTitleString;
	}
	public int getComic_digg() {
		return comic_digg;
	}
	public void setComic_digg(int comic_digg) {
		this.comic_digg = comic_digg;
	}
	public int getHistoryChapterPage() {
		return historyChapterPage;
	}
	public void setHistoryChapterPage(int historyChapterPage) {
		this.historyChapterPage = historyChapterPage;
	}
	public int getAdvices() {
		return advices;
	}
	public void setAdvices(int advices) {
		this.advices = advices;
	}
	public List<Chapter> getChapters() {
		return chapters;
	}
	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}
	public String getComic_last_chaptitle() {
		return comic_last_chaptitle;
	}
	public void setComic_last_chaptitle(String comic_last_chaptitle) {
		this.comic_last_chaptitle = comic_last_chaptitle;
	}
	private boolean hasCollect;
	private String comic_last_chaptitle;
	private String downloadStateString;
	
	public String getDownloadStateString() {
		return downloadStateString;
	}
	public void setDownloadStateString(String downloadStateString) {
		this.downloadStateString = downloadStateString;
	}
	public boolean isHasCollect() {
		return hasCollect;
	}
	public void setHasCollect(boolean hasCollect) {
		this.hasCollect = hasCollect;
	}
	public int getHistoryChapter() {
		return historyChapter;
	}
	public void setHistoryChapter(int historyChapter) {
		this.historyChapter = historyChapter;
	}
//	public int getChapterNum() {
//		return chapterNum;
//	}
//	public void setChapterNum(int chapterNum) {
//		this.chapterNum = chapterNum;
//	}
	public String getComic_id() {
		return comic_id;
	}
	public void setComic_id(String comic_id) {
		this.comic_id = comic_id;
	}
	public String getComic_title() {
		return comic_title;
	}
	public void setComic_title(String comic_title) {
		this.comic_title = comic_title;
	}
	public String getComic_titlepic() {
		return comic_titlepic;
	}
	public void setComic_titlepic(String comic_titlepic) {
		this.comic_titlepic = comic_titlepic;
	}
	public String getComic_author() {
		return comic_author;
	}
	public void setComic_author(String comic_author) {
		this.comic_author = comic_author;
	}
	public int getComic_click() {
		return comic_click;
	}
	public void setComic_click(int comic_click) {
		this.comic_click = comic_click;
	}
	public int getComic_comment() {
		return comic_comment;
	}
	public void setComic_comment(int comic_comment) {
		this.comic_comment = comic_comment;
	}
	public int getComic_good() {
		return comic_good;
	}
	public void setComic_good(int comic_good) {
		this.comic_good = comic_good;
	}
	public String getComic_intro() {
		return comic_intro;
	}
	public void setComic_intro(String comic_intro) {
		this.comic_intro = comic_intro;
	}
	public long getComic_utime() {
		return comic_utime;
	}
	public void setComic_utime(long comic_utime) {
		this.comic_utime = comic_utime;
	}
	
}
