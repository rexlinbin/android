package com.bccv.strategy.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

import com.bccv.strategy.utils.L;
import com.bccv.strategy.ApplicationManager;

public class DbUtil {

	private static final String TAG = "DbUtil";
	
	/**
	 * 打印栈信息
	 * 
	 * @return void
	 */
	public static void writeStackTrace() {
		StackTraceElement[] stackElements = new Throwable().getStackTrace();
		if (stackElements != null) {
			for (int i = 0; i < stackElements.length; i++) {
				L.v(TAG, "writeStackTrace" ,""+ stackElements[i]);
			}
		}
	}

	/**
	 * 文件拷贝
	 * 
	 * @param sourceFile
	 * @param targetFile
	 * @throws IOException
	 */
	public void copyDataBaseToSdCard() {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			FileInputStream source = new FileInputStream(
					ApplicationManager.getGlobalContext().getDatabasePath("app_share.db"));
			String folderPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "AppShare";
			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
	
			File filepath = new File(folderPath + "/app_share.db");
			FileOutputStream target = new FileOutputStream(filepath);
	
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(source);
			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(target);
			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
	
		} catch (IOException e) {
			e.printStackTrace();
	
		} finally {
			// 关闭流
			if (inBuff != null){
				try {
					inBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outBuff != null){
				try {
					outBuff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
