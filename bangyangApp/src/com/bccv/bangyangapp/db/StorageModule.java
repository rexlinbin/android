package com.bccv.bangyangapp.db;

import java.util.ArrayList;

import android.content.Context;

import com.bccv.bangyangapp.ApplicationManager;
import com.bccv.bangyangapp.utils.SerializationUtil;

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
	
	
	/*******************本地化存储喜欢和关注**************************/
	
//	public ArrayList<String> getLocalLikeList(){
//		return SerializationUtil.readLikeSerialization(context);
//	}
//	
//	public void addLocalLike(String albumID){
//		ArrayList<String> localLikeList = getLocalLikeList();
//		if(localLikeList==null){
//			localLikeList = new ArrayList<String>();
//		}
//		if(!localLikeList.contains(albumID)){
//			localLikeList.add(albumID);
//			SerializationUtil.wirteLikeSerialization(context, localLikeList);
//		}
//	}
//	
//	public void addLocalLike(ArrayList<String> albumIDs){
//		ArrayList<String> localLikeList = getLocalLikeList();
//		if(localLikeList==null){
//			localLikeList = albumIDs;
//		}else{
//			for (String albumID : albumIDs) {
//				if(!localLikeList.contains(albumID)){
//					localLikeList.add(albumID);
//				}
//			}
//		}
//		SerializationUtil.wirteLikeSerialization(context, localLikeList);
//	}
//	
//	public void removeLocalLike(ArrayList<String> albumIDs){
//		ArrayList<String> localLikeList = getLocalLikeList();
//		if(localLikeList!=null && localLikeList.size()!=0){
//			for (String albumID : albumIDs) {
//				if(localLikeList.contains(albumID)){
//					localLikeList.remove(albumID);
//				}
//			}
//			SerializationUtil.wirteLikeSerialization(context, localLikeList);
//		}
//	}
//	
//	public void removeLocalLike(String albumID){
//		ArrayList<String> localLikeList = getLocalLikeList();
//		if(localLikeList!=null&&localLikeList.contains(albumID)){
//			localLikeList.remove(albumID);
//			SerializationUtil.wirteLikeSerialization(context, localLikeList);
//		}
//	}
//	
//	public boolean isLiked(String albumID){
//		ArrayList<String> localLikeList = getLocalLikeList();
//		if(localLikeList!=null&&localLikeList.contains(albumID)){
//			return true;
//		}
//		return false;
//	}
	
	public ArrayList<String> getLocalCareList(){
		return SerializationUtil.readCareSerialization(context);
	}
	
	public void addLocalCare(String authorID){
		ArrayList<String> localCareList = getLocalCareList();
		if(localCareList==null){
			localCareList = new ArrayList<String>();
		}
		if(!localCareList.contains(authorID)){
			localCareList.add(authorID);
			SerializationUtil.wirteCareSerialization(context, localCareList);
		}
	}
	
	public void addLocalCare(ArrayList<String> authorIDs){
		ArrayList<String> localCareList = getLocalCareList();
		if(localCareList==null){
			localCareList = authorIDs;
		}else{
			for (String authorID : authorIDs) {
				if(!localCareList.contains(authorID)){
					localCareList.add(authorID);
				}
			}
		}
		SerializationUtil.wirteCareSerialization(context, localCareList);
	}
	
	public void removeLocalCare(ArrayList<String> authorIDs){
		ArrayList<String> localCareList = getLocalCareList();
		if(localCareList!=null&&localCareList.size()!=0){
			for (String authorID : authorIDs) {
				if(!localCareList.contains(authorID)){
					localCareList.remove(authorID);
				}
			}
			SerializationUtil.wirteCareSerialization(context, localCareList);
		}
	}
	
	public void removeLocalCare(String authorID){
		ArrayList<String> localCareList = getLocalCareList();
		if(localCareList!=null&&localCareList.contains(authorID)){
			localCareList.remove(authorID);
			SerializationUtil.wirteCareSerialization(context, localCareList);
		}
	}
	
	public boolean isCared(String authorID){
		ArrayList<String> localCareList = getLocalCareList();
		if(localCareList!=null&&localCareList.contains(authorID)){
			return true;
		}
		return false;
	}
	
	/*******************本地化存储喜欢和关注**************************/
}
