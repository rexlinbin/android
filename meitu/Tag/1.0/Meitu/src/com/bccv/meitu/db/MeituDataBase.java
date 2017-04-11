package com.bccv.meitu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.bccv.meitu.ApplicationManager;
import com.bccv.meitu.utils.Logger;
import com.nostra13.universalimageloader.utils.L;

public class MeituDataBase implements IMeituDataBase{
	private static final String TAG = "MeituDataBase";
	
	private static final String DATABASE_NAME = "meitu.db";
	
	private static SQLiteDatabase mSQLiteDatabase = null;
	private static MeituOpenHelper sMeituOpenHelper;
	private static MeituDataBase mMeituDataBase = new MeituDataBase();
	
	private MeituDataBase(){
		if(sMeituOpenHelper==null){
			sMeituOpenHelper = new MeituOpenHelper(ApplicationManager.getGlobalContext(), 
					DATABASE_NAME, null, DataBaseConfig.DATABASE_CURRENT_VERSION);
		}
	}
	
	public static MeituDataBase getInstance(){
		
		if(mMeituDataBase == null){
			mMeituDataBase = new MeituDataBase();
		}
		
		return mMeituDataBase;
	}
	
	
	/**
	 * get SQLiteDatabase instance
	 * 
	 * @return SQLiteDatabase
	 */
	public SQLiteDatabase openDatabase(boolean isIgnoreLocked) {
		if (mSQLiteDatabase == null) {
			mSQLiteDatabase = sMeituOpenHelper.getWritableDatabase();

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
			Logger.v(TAG, "openDatabase", "mSQLiteDatabase=null");
		}

		return mSQLiteDatabase;
	}
	
	
	/**
	 * dbHelper
	 *
	 * @author liukai
	 * @date 2014-12-8
	 */
	static class MeituOpenHelper extends SQLiteOpenHelper {

		public MeituOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Logger.i(TAG, "onCreate", "start");

			// 创建数据库
			DataBaseConfig.createDatabase(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Logger.i(TAG, "onUpgrade", "oldVersion=" + oldVersion + " newVersion="
					+ newVersion);
			if (newVersion > oldVersion) { // 旧版本升到新版本

				// 选用合适的数据库升级方案,进行升级
				DataBaseConfig.upgradeDatabase(db, oldVersion);
			}
		}
	}
}
