package com.bccv.boxcomic.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyPreferences {
	private static SharedPreferences getSharePreferences(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static boolean hasCollect(Context context){
		return getSharePreferences(context).getBoolean("hasCollect", false);
	}
	
	public static void setHasCollect(Context context, boolean hasCollect){
		getSharePreferences(context).edit().putBoolean("hasCollect", hasCollect);
	}
	
	public static boolean isDay(Context context){
		return getSharePreferences(context).getBoolean("isDay", true);
	}
	
	public static void setIsDay(Context context, boolean isDay){
		getSharePreferences(context).edit().putBoolean("isDay", isDay).commit();
	}
	
	public static void setBackColor(Context context, int backColor){
		getSharePreferences(context).edit().putInt("backColor", backColor).commit();
	}
	
	public static int getBackColor(Context context){
		return getSharePreferences(context).getInt("backColor", 0);
	}
	
	
}
