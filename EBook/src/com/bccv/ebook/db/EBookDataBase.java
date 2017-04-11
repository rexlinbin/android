package com.bccv.ebook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.bccv.ebook.ApplicationManager;
import com.bccv.ebook.utils.L;

public class EBookDataBase implements IEBookDataBase{
	private static final String TAG = "EBookDataBase";
	
	private static final String DATABASE_NAME = "ebook.db";
	
	private static SQLiteDatabase mSQLiteDatabase = null;
	private static EBookOpenHelper sEBookOpenHelper;
	private static EBookDataBase mEBookDataBase = new EBookDataBase();
	
	private EBookDataBase(){
		if(sEBookOpenHelper==null){
			sEBookOpenHelper = new EBookOpenHelper(ApplicationManager.getGlobalContext(), 
					DATABASE_NAME, null, DataBaseConfig.DATABASE_CURRENT_VERSION);
		}
	}
	
	public static EBookDataBase getInstance(){
		
		if(mEBookDataBase == null){
			mEBookDataBase = new EBookDataBase();
		}
		
		return mEBookDataBase;
	}
	
	
	/**
	 * get SQLiteDatabase instance
	 * 
	 * @return SQLiteDatabase
	 */
	@SuppressWarnings("deprecation")
	public SQLiteDatabase openDatabase(boolean isIgnoreLocked) {
		if (mSQLiteDatabase == null) {
			mSQLiteDatabase = sEBookOpenHelper.getWritableDatabase();

		}

		if (mSQLiteDatabase != null) {

			while (mSQLiteDatabase.isDbLockedByCurrentThread()
					|| mSQLiteDatabase.isDbLockedByOtherThreads()) {
				// db is locked, keep looping
				if (isIgnoreLocked) {
					L.w(TAG,
							"openDatabase",
							"mSQLiteDatabase locked isIgnoreLocked="
									+ isIgnoreLocked
									+ " currentThread="
									+ mSQLiteDatabase
											.isDbLockedByCurrentThread()
									+ " otherThreads="
									+ mSQLiteDatabase
											.isDbLockedByOtherThreads());
					break;
				} else {
					L.w(TAG,
							"openDatabase",
							"mSQLiteDatabase locked isIgnoreLocked="
									+ isIgnoreLocked
									+ " currentThread="
									+ mSQLiteDatabase
											.isDbLockedByCurrentThread()
									+ " otherThreads="
									+ mSQLiteDatabase
											.isDbLockedByOtherThreads());
				}
				DbUtil.writeStackTrace();
			}
			// L.v(TAG, "openDatabase", "mSQLiteDatabase unlocked.");
		} else {
			L.v(TAG, "openDatabase", "mSQLiteDatabase=null");
		}

		return mSQLiteDatabase;
	}
	
	
	/**
	 * dbHelper
	 *
	 * @author liukai
	 * @date 2014-12-8
	 */
	static class EBookOpenHelper extends SQLiteOpenHelper {

		public EBookOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			L.i(TAG, "onCreate", "start");

			// 创建数据库
			DataBaseConfig.createDatabase(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			L.i(TAG, "onUpgrade", "oldVersion=" + oldVersion + " newVersion="
					+ newVersion);
			if (newVersion > oldVersion) { // 旧版本升到新版本

				// 选用合适的数据库升级方案,进行升级
				DataBaseConfig.upgradeDatabase(db, oldVersion);
			}
		}
	}
}
