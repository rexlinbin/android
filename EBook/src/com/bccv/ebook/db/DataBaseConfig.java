package com.bccv.ebook.db;

import android.database.sqlite.SQLiteDatabase;


/**
 *	数据库配置文件
 *
 * @author liukai
 * @date 2014-12-8
 */
public class DataBaseConfig implements IEBookDataBase {

	private static final String TAG = "DataBaseConfig";
	
	/**
	 * 数据库升级时,该常量指向最新版本(注:此变量值不能低于已发布数据库版本的值,否则异常)
	 */
	public static final int DATABASE_CURRENT_VERSION = DataBaseConfig.EBOOK_DATABASE_VERSION_1_0;
	
	/**
	 * 数据库版本(1.0)
	 */
	public static final int EBOOK_DATABASE_VERSION_1_0 = 1;
	
	
	
	/**
	 * 配置数据库升级方案
	 * 
	 * @param sqliteDatabase
	 *            数据库操作对象
	 * @return void
	 */
	public static void upgradeDatabase(SQLiteDatabase sqliteDatabase,
			int oldVersion) {
		//TODO 根据不同版本的数据库 做不同的升级方案
	}
	
	
	/**
	 * 创建数据库
	 * 
	 * @param sqliteDatabase
	 *            数据库
	 * @return void
	 */
	public static void createDatabase(SQLiteDatabase sqliteDatabase) {
		//TODO 数据库表的创建
		
	}
}
