package com.bccv.ebook.db;

import android.content.Context;

import com.bccv.ebook.ApplicationManager;

/**
 * 数据存储模块  
 *
 * @author liukai
 * @date 2014-12-8
 */
public class StorageModule {

	private static final String TAG = "StorageModule";
	
	private static StorageModule mStorageModule = new StorageModule();
	
	private static Context context;
	
	private StorageModule(){
		context = ApplicationManager.getGlobalContext();
	}
	
	public static StorageModule getInstance(){
		if(mStorageModule == null){
			mStorageModule = new StorageModule();
		}
		return mStorageModule;
	}
	
	
	//TODO 数据操作接口
	
	
}
