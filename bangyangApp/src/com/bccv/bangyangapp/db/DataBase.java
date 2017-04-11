package com.bccv.bangyangapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.bccv.bangyangapp.ApplicationManager;
import com.bccv.bangyangapp.utils.L;

public class DataBase implements IDataBase{
	private static final String TAG = "DataBase";
	
	private static final String DATABASE_NAME = "app_share.db";
	
	private static SQLiteDatabase mSQLiteDatabase = null;
	private static DataBaseOpenHelper mDataBaseOpenHelper;
	private static DataBase mDataBase = new DataBase();
	
	private DataBase(){
		if(mDataBaseOpenHelper==null){
			mDataBaseOpenHelper = new DataBaseOpenHelper(ApplicationManager.getGlobalContext(), 
					DATABASE_NAME, null, DataBaseConfig.DATABASE_CURRENT_VERSION);
		}
	}
	
	public static DataBase getInstance(){
		
		if(mDataBase == null){
			mDataBase = new DataBase();
		}
		
		return mDataBase;
	}
	
	
	/**
	 * get SQLiteDatabase instance
	 * 
	 * @return SQLiteDatabase
	 */
	@SuppressWarnings("deprecation")
	public SQLiteDatabase openDatabase(boolean isIgnoreLocked) {
		if (mSQLiteDatabase == null) {
			mSQLiteDatabase = mDataBaseOpenHelper.getWritableDatabase();

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
	static class DataBaseOpenHelper extends SQLiteOpenHelper {

		public DataBaseOpenHelper(Context context, String name,
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
