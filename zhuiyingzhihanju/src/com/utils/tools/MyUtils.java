package com.utils.tools;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class MyUtils {
	/**
	 * 给textview设置ttf字体
	 * @param context
	 * @param textView
	 * @param fileName "fonts/Georgia.ttf"
	 */
	public static void setTextFont(Context context, TextView textView, String fileName){
		AssetManager mgr = context.getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, fileName);// 根据路径得到Typeface
		textView.setTypeface(tf);// 设置字体
	}
}
