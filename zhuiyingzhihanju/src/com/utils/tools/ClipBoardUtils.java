package com.utils.tools;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;

public class ClipBoardUtils {
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public static String getClipBoardString(Context context) {
		if (android.os.Build.VERSION.SDK_INT > 11) {
			android.content.ClipboardManager c = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
//			ClipData clipData = c.getPrimaryClip();
//			if (clipData.getItemAt(0) != null) {
//				clipData.getItemAt(0).getText().toString();
//			}

			if (c.getText() == null) {
				return "";
			}
			
			return c.getText().toString();
		} else {
			android.text.ClipboardManager c = (android.text.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			return c.getText().toString();
		}
	}
}
