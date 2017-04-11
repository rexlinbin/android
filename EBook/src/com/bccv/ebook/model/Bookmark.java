package com.bccv.ebook.model;

import java.io.Serializable;

public class Bookmark implements Serializable {

	private static final long serialVersionUID = -4696422972186945096L;
	private long time;
	private int charsetIndex;
	private int chapterId;
	private String chapterName;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getChapterId() {
		return chapterId;
	}

	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}

	public int getCharsetIndex() {
		return charsetIndex;
	}

	public void setCharsetIndex(int charsetIndex) {
		this.charsetIndex = charsetIndex;
	}

	public String getChapterName() {
		return chapterName;
	}

	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Bookmark [time=" + time + ", charsetIndex=" + charsetIndex
				+ ", chapterId=" + chapterId + ", chapterName=" + chapterName
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bookmark) {
			Bookmark bookmark = (Bookmark) obj;
			//如果章节id与字节下标相同 则认为是同一个书签
			return bookmark.chapterId == chapterId
					&& bookmark.charsetIndex == charsetIndex;
		}

		return false;
	}

}
