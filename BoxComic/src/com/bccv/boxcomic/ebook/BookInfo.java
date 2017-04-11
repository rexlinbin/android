package com.bccv.boxcomic.ebook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class BookInfo implements Serializable{
	
	private static final long serialVersionUID = 5294349855940807114L;

	private static final String TAG = "BookInfo";
	
	private int book_id;
	private String book_title;
	private String book_titlepic;
	private String ftitle;
	private ArrayList<ChapterInfo> chapterInfos;
	private String book_text;
	private String book_author;
	private String book_finish;
	private String book_last_chaptitle;
	
	
	public String getFtitle() {
		return ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	public int getBook_id() {
		return book_id;
	}

	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}

	public String getBook_title() {
		return book_title;
	}

	public void setBook_title(String book_title) {
		this.book_title = book_title;
	}

	public String getBook_titlepic() {
		return book_titlepic;
	}

	public void setBook_titlepic(String book_titlepic) {
		this.book_titlepic = book_titlepic;
	}

	public ArrayList<ChapterInfo> getChapterInfos() {
		return chapterInfos;
	}
	
	public void setChapterInfos(ArrayList<ChapterInfo> chapterInfos) {
		this.chapterInfos = chapterInfos;
	}
	
	

	public String getBook_text() {
		return book_text;
	}

	public void setBook_text(String book_text) {
		this.book_text = book_text;
	}

	public String getBook_author() {
		return book_author;
	}

	public void setBook_author(String book_author) {
		this.book_author = book_author;
	}

	public String getBook_finish() {
		return book_finish;
	}

	public void setBook_finish(String book_finish) {
		this.book_finish = book_finish;
	}

	public String getBook_last_chaptitle() {
		return book_last_chaptitle;
	}

	public void setBook_last_chaptitle(String book_last_chaptitle) {
		this.book_last_chaptitle = book_last_chaptitle;
	}

	

	@Override
	public String toString() {
		return "BookInfo [book_id=" + book_id + ", book_title=" + book_title
				+ ", book_titlepic=" + book_titlepic + ", chapterInfos="
				+ chapterInfos + ", book_text=" + book_text + ", book_author="
				+ book_author + ", book_finish=" + book_finish
				+ ", book_last_chaptitle=" + book_last_chaptitle + "]";
	}

	/**
	 * 解析章节信息
	 * 
	 * @param account	章节配置文件
	 */
//	public void parser(Context context,String account,int bookId){
//		InputStream inStream = null;
//		try {
//			inStream = context.getAssets().open(account);
//			String data = FileUtils.readInStream(inStream);
//			JSONObject jsonObject = new JSONObject(data);
//			JSONArray jsonArray = jsonObject.getJSONArray("chapters");
//			if(chapterInfos==null){
//				chapterInfos = new ArrayList<ChapterInfo>();
//			}
//			chapterInfos.clear();
//			
//			String eBookCacheFolder = SystemUtil.getEBookCacheFolder(SystemUtil.UNZIP_BOOK_TYPE);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject chapterJson = jsonArray.getJSONObject(i);
//				ChapterInfo chapterInfo = new ChapterInfo();
//				chapterInfo.setTitle(chapterJson.getString("name"));
//				chapterInfo.setFileName(chapterJson.getString("filename"));
//				chapterInfo.setId(chapterJson.getInt("id"));
//				chapterInfo.setIndex(i);
//				chapterInfo.setPath(eBookCacheFolder+File.separator+bookId+File.separator+chapterInfo.getFileName());
//				chapterInfos.add(chapterInfo);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			L.e(TAG, "parser", e.getMessage());
//		}finally{
//			if(inStream!=null){
//				try {
//					inStream.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
}
