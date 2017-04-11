package com.bccv.boxcomic.ebook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Bookmarks implements Serializable{

	private static final long serialVersionUID = -8890984935500378702L;
	private HashMap<Integer,ArrayList<Bookmark>> bookmarks; //key 章节ID  value 章节所对应的书签集合
	
	@SuppressWarnings("unused")
	private static final String TAG = "Bookmarks";

	public HashMap<Integer, ArrayList<Bookmark>> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(HashMap<Integer, ArrayList<Bookmark>> bookmarks) {
		this.bookmarks = bookmarks;
	}
	
}
