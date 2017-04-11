package com.bccv.strategy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

import com.bccv.strategy.utils.L;
import com.bccv.strategy.ApplicationManager;


/**
 * 数据库操作模板
 *
 * @author liukai
 * @date 2014-12-8
 */
public class OperateDataBaseTemplate {

	private static final String TAG = "CommonDataBaseTemplate";

	private static SQLiteDatabase sSQLiteDatabase;

	private static final OperateDataBaseTemplate mInstance = new OperateDataBaseTemplate();
	private static Context mContext;

	private OperateDataBaseTemplate() {
		mContext = ApplicationManager.getGlobalContext();
	}
	
	public static OperateDataBaseTemplate getInstance() {
		return mInstance;
	}

	/**
	 * 获取 SQLiteStatement
	 * @param sql
	 * @return
	 */
	public SQLiteStatement getSQLiteStatement(String sql) {
		SQLiteStatement sqliteStatement = null;
		if (sSQLiteDatabase != null) {
			sqliteStatement = sSQLiteDatabase.compileStatement(sql);
		} else {
			e("getSQLiteStatement", "sSQLiteDatabase=null");
		}
		return sqliteStatement;
	}

	/**
	 * 开启事物
	 */
	public void beginTransaction() {
		if (sSQLiteDatabase != null) {
			sSQLiteDatabase.beginTransaction();
		} else {
			e("beginTransaction", "sSQLiteDatabase=null");
		}
	}

	/**
	 * 事物执行成功
	 */
	public void setTransactionSuccessful() {
		if (sSQLiteDatabase != null) {
			sSQLiteDatabase.setTransactionSuccessful();
		} else {
			e("setTransactionSuccessful", "sSQLiteDatabase=null");
		}
	}

	/**
	 * 事物执行完毕
	 */
	public void endTransaction() {
		if (sSQLiteDatabase != null) {
			sSQLiteDatabase.endTransaction();
		} else {
			e("endTransaction", "sSQLiteDatabase=null");
		}
	}

	/**
	 * 打开数据库
	 */
	public void open() {
		sSQLiteDatabase = DataBase.getInstance().openDatabase(false);
		if (sSQLiteDatabase == null) {
			e("open", "sSQLiteDatabase=null");
		}
	}

	public void open(boolean isIgnoreLocked) {
		sSQLiteDatabase = DataBase.getInstance().openDatabase(
				isIgnoreLocked);
		if (sSQLiteDatabase == null) {
			e("open(boolean isReadable)", "sSQLiteDatabase=null");
		}
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (sSQLiteDatabase != null) {
			//TODO 关闭数据库
		}
	}

	public int delete(Uri uri, String where) {
		try {
			int count = mContext.getContentResolver().delete(uri, where, null);
			return count;
		} catch (Throwable e) {
			e.printStackTrace();
			return 0;
		}
	}

	public Uri insert(Uri uri, ContentValues cv) {
		try {
			Uri newUri = mContext.getContentResolver().insert(uri, cv);
			return newUri;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

	public int delete(Uri uri, String whereClause, String[] whereArgs) {
		try {
			return mContext.getContentResolver().delete(uri, whereClause,
					whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "delete", "CommonDataBaseTemplate delete" + e.getMessage()
					+ "");
		} finally {
		}
		return 0;
	}
	
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		try {
			return mContext.getContentResolver().update(uri, values,
					whereClause, whereArgs);

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "update", "CommonDataBaseTemplate update" + e.getMessage()
					+ "");
		} finally {

		}
		return 0;
	}
	
	protected boolean update(Uri uri, ContentValues cv, String where) {
		try {
			int count = mContext.getContentResolver().update(uri, cv, where,
					null);
			return count > 0 ? true : false;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
	}

	public Cursor select(Uri uri, String where) {
		return mContext.getContentResolver()
				.query(uri, null, where, null, null);
	}

	public Cursor select(Uri uri, String where, String[] selectionArgs) {
		return mContext.getContentResolver().query(uri, null, where,
				selectionArgs, null);
	}

	protected int getDataNum(Uri uri, String where) {
		int num = 0;
		Cursor cursor = mContext.getContentResolver().query(uri, null, where,
				null, null);
		if (cursor != null) {
			num = cursor.getCount();
			cursor.close();
		}
		return num;
	}

	/**
	 * Log wrapper
	 * 
	 * @param type
	 * @param msg
	 */
	public void v(String type, String msg) {
		L.v(TAG, type, msg);
	}

	/**
	 * Log wrapper
	 * 
	 * @param type
	 * @param msg
	 */
	public void e(String type, String msg) {
		L.e(TAG, type, msg);
	}

	/**
	 * 插入数据库
	 * @param statement
	 */
	protected void insert(SQLiteStatement statement) {
		try {
			statement.executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "insert",
					"CommonDataBaseTemplate insertBySql" + e.getMessage() + "");
		}
	}

	/**
	 * 插入数据库
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @return
	 */
	protected long insert(String table, String nullColumnHack,
			ContentValues values) {
		try {
			if (sSQLiteDatabase != null) {
				return sSQLiteDatabase.insert(table, nullColumnHack, values);
			} else {
				e("insert(String table, String nullColumnHack,ContentValues values)",
						"sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "insert",
					"CommonDataBaseTemplate insertBySql" + e.getMessage() + "");
		}
		return -1;
	}

	/**
	 * 插入数据库
	 * @param table
	 * @param nullColumnHack
	 * @param values
	 * @param conflictAlgorithm
	 * @return
	 */
	protected long insertWithOnConflict(String table, String nullColumnHack,
			ContentValues values, int conflictAlgorithm) {
		try {
			if (sSQLiteDatabase != null) {
				return sSQLiteDatabase.insertWithOnConflict(table,
						nullColumnHack, values, conflictAlgorithm);
			} else {
				e("insertWithOnConflict(String table, String nullColumnHack,ContentValues values)",
						"sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			L.w(TAG, "insertWithOnConflict",
					"CommonDataBaseTemplate insertBySql" + e.getMessage() + "");
		}
		return -1;
	}

	/**
	 * 删除
	 * @param sql
	 */
	protected void delete(String sql) {
		try {
			if (sSQLiteDatabase != null) {
				sSQLiteDatabase.execSQL(sql);
			} else {
				e("delete(String sql)", "sSQLiteDatabase=null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "delete", "CommonDataBaseTemplate delete" + e.getMessage()
					+ "");
		} finally {
		}
	}

	/**
	 * 删除
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		int result = 0;
		try {
			if (sSQLiteDatabase != null) {

				result = sSQLiteDatabase.delete(table, whereClause, whereArgs);

				v("delete", "result=" + result);

			} else {
				e("delete", "sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "delete", "CommonDataBaseTemplate delete" + e.getMessage()
					+ "");
		} finally {
		}
		return result;
	}

	/**
	 * 更新
	 * @param sql
	 */
	public void update(String sql) {
		try {
			if (sSQLiteDatabase != null) {
				sSQLiteDatabase.execSQL(sql);
			} else {
				e("update(String sql)", "sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "update", "CommonDataBaseTemplate update" + e.getMessage()
					+ "");
		} finally {
		}
	}

	/**
	 * 更新
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		try {
			if (sSQLiteDatabase != null) {
				return sSQLiteDatabase.update(table, values, whereClause,
						whereArgs);
			} else {
				e("update(String table, ContentValues values, String whereClause,String[] whereArgs)",
						"sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "update", "CommonDataBaseTemplate update" + e.getMessage()
					+ "");
		} finally {

		}
		return 0;
	}

	/**
	 * 查
	 * @param sql
	 * @return
	 */
	protected Cursor select(String sql) {
		try {
			if (sSQLiteDatabase != null) {
				return sSQLiteDatabase.rawQuery(sql, null);
			} else {
				e("select(String sql)", "sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "select", "CommonDataBaseTemplate select" + e.getMessage()
					+ "");
		}
		return null;
	}

	/**
	 * 查
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	protected Cursor select(String sql, String[] selectionArgs) {
		try {
			if (sSQLiteDatabase != null) {
				return sSQLiteDatabase.rawQuery(sql, selectionArgs);
			} else {
				e("select(String sql, String[] selectionArgs)",
						"sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "select", "CommonDataBaseTemplate select" + e.getMessage()
					+ "");
		}
		return null;
	}

	/**
	 * 执行sql
	 * @param sql
	 */
	protected void execSQL(String sql) {
		try {
			if (sSQLiteDatabase != null) {
				sSQLiteDatabase.execSQL(sql);
			} else {
				e("execSQL", "sSQLiteDatabase=null");
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "select",
					"CommonDataBaseTemplate execSQL" + e.getMessage() + "");
		}
	}
}
