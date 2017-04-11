package com.bccv.ebook.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.Xml;

public class Bookshelf implements Serializable {
	
	private static final long serialVersionUID = -8125884877518789792L;

	@SuppressWarnings("unused")
	private static final String TAG = "Bookshelf";

	private String title;
	private ArrayList<BookInfo> bookInfos;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ArrayList<BookInfo> getBookInfos() {
		return bookInfos;
	}
	public void setBookInfos(ArrayList<BookInfo> bookInfos) {
		this.bookInfos = bookInfos;
	}
	
	@Override
	public String toString() {
		return "Bookshelf [title=" + title + ", bookInfos=" + bookInfos + "]";
	}
	
public static Bookshelf parser(Context context){
		
		Bookshelf bookshelf = new Bookshelf();
		try{
			//Pull解析  1. 获得解析器
			XmlPullParser parser = Xml.newPullParser();
			//2.初始化参数,并指定解析的文件
			parser.setInput(context.getAssets().open("bookshelf.xml"),"utf-8");
			//获取当前解析事件的标签指针
			BookInfo bookInfo = null;
			int type = parser.getEventType();
			while(type!=XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_TAG:
					if("title".equals(parser.getName())){
						bookshelf.setTitle(parser.nextText());
					}else if("books".equals(parser.getName())){
						ArrayList<BookInfo> bookInfos = new ArrayList<BookInfo>();
						bookshelf.setBookInfos(bookInfos);
					}else if("book".equals(parser.getName())){
						bookInfo = new BookInfo();
						bookshelf.getBookInfos().add(bookInfo);
					}else if("id".equals(parser.getName())){
						bookshelf.getBookInfos().get(bookshelf.getBookInfos().size()-1)
						.setId(Integer.valueOf(parser.nextText()));
					}else if("name".equals(parser.getName())){
						bookshelf.getBookInfos().get(bookshelf.getBookInfos().size()-1)
						.setName(parser.nextText());
					}else if("zippath".equals(parser.getName())){
						bookshelf.getBookInfos().get(bookshelf.getBookInfos().size()-1)
						.setZippath(parser.nextText());
					}else if("account".equals(parser.getName())){
						bookshelf.getBookInfos().get(bookshelf.getBookInfos().size()-1)
						.setAccount(parser.nextText());
					}else if("cover".equals(parser.getName())){
						bookshelf.getBookInfos().get(bookshelf.getBookInfos().size()-1)
						.setCover(parser.nextText());
					}
					break;
				}
				//将指针下移
				type = parser.next();
			}
		}catch(Exception e){
			
		}
		return bookshelf;
	}
	
	
}
