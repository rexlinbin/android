package com.bccv.strategy.db;

import java.util.ArrayList;

import android.content.Context;

import com.bccv.strategy.utils.SerializationUtil;
import com.bccv.strategy.ApplicationManager;

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
	
	public ArrayList<String> getLocalLikeList(){
		return SerializationUtil.readLikeSerialization(context);
	}
	
	public void addLocalLike(String albumID){
		ArrayList<String> localLikeList = getLocalLikeList();
		if(localLikeList==null){
			localLikeList = new ArrayList<String>();
		}
		if(!localLikeList.contains(albumID)){
			localLikeList.add(albumID);
			SerializationUtil.wirteLikeSerialization(context, localLikeList);
		}
	}
	
	public void addLocalLike(ArrayList<String> albumIDs){
		ArrayList<String> localLikeList = getLocalLikeList();
		if(localLikeList==null){
			localLikeList = albumIDs;
		}else{
			for (String albumID : albumIDs) {
				if(!localLikeList.contains(albumID)){
					localLikeList.add(albumID);
				}
			}
		}
		SerializationUtil.wirteLikeSerialization(context, localLikeList);
	}
	
	public void removeLocalLike(ArrayList<String> albumIDs){
		ArrayList<String> localLikeList = getLocalLikeList();
		if(localLikeList!=null && localLikeList.size()!=0){
			for (String albumID : albumIDs) {
				if(localLikeList.contains(albumID)){
					localLikeList.remove(albumID);
				}
			}
			SerializationUtil.wirteLikeSerialization(context, localLikeList);
		}
	}
	public void removeAllLocalLike(){
		ArrayList<String> localLikeList = getLocalLikeList();
		if(localLikeList!=null&&localLikeList.size()!=0){
			for (int i = 0; i < localLikeList.size(); i++) {
				localLikeList.remove(i);
			}
			SerializationUtil.wirteLikeSerialization(context, localLikeList);
		}
	}
	
	public void removeLocalLike(String albumID){
		ArrayList<String> localLikeList = getLocalLikeList();
		if(localLikeList!=null&&localLikeList.contains(albumID)){
			localLikeList.remove(albumID);
			SerializationUtil.wirteLikeSerialization(context, localLikeList);
		}
	}
	
	public boolean isLiked(String albumID){
		ArrayList<String> localLikeList = getLocalLikeList();
		if(localLikeList!=null&&localLikeList.contains(albumID)){
			return true;
		}
		return false;
	}
	
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
			if (authorIDs != null) {
				for (String authorID : authorIDs) {
					if(!localCareList.contains(authorID)){
						localCareList.remove(authorID);
					}
				}
			}else {
				for (int i = 0; i < localCareList.size(); i++) {
					localCareList.remove(i);
				}
			}
			SerializationUtil.wirteCareSerialization(context, localCareList);
		}
	}
	
	public void removeAllLocalCare(){
		ArrayList<String> localCareList = getLocalCareList();
		if(localCareList!=null&&localCareList.size()!=0){
			for (int i = 0; i < localCareList.size(); i++) {
				localCareList.remove(i);
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
	
	public boolean isDigg(String id) {
		ArrayList<String> localCareList = getLocalDiggList();
		if(localCareList!=null&&localCareList.contains(id)){
			return true;
		}
		return false;
	}
	
	public ArrayList<String> getLocalDiggList(){
		return SerializationUtil.readDiggSerialization(context);
	}
	
	public void addLocalDigg(String albumID){
		ArrayList<String> localLikeList = getLocalDiggList();
		if(localLikeList==null){
			localLikeList = new ArrayList<String>();
		}
		if(!localLikeList.contains(albumID)){
			localLikeList.add(albumID);
			SerializationUtil.wirteDiggSerialization(context, localLikeList);
		}
	}
	
	public void addLocalDigg(ArrayList<String> albumIDs){
		ArrayList<String> localLikeList = getLocalDiggList();
		if(localLikeList==null){
			localLikeList = albumIDs;
		}else{
			for (String albumID : albumIDs) {
				if(!localLikeList.contains(albumID)){
					localLikeList.add(albumID);
				}
			}
		}
		SerializationUtil.wirteDiggSerialization(context, localLikeList);
	}
	
	public void removeLocalDigg(ArrayList<String> albumIDs){
		ArrayList<String> localLikeList = getLocalDiggList();
		if(localLikeList!=null && localLikeList.size()!=0){
			for (String albumID : albumIDs) {
				if(localLikeList.contains(albumID)){
					localLikeList.remove(albumID);
				}
			}
			SerializationUtil.wirteDiggSerialization(context, localLikeList);
		}
	}
	public void removeAllLocalDigg(){
		ArrayList<String> localLikeList = getLocalDiggList();
		if(localLikeList!=null&&localLikeList.size()!=0){
			for (int i = 0; i < localLikeList.size(); i++) {
				localLikeList.remove(i);
			}
			SerializationUtil.wirteDiggSerialization(context, localLikeList);
		}
	}
	
	public void removeLocalDigg(String albumID){
		ArrayList<String> localLikeList = getLocalDiggList();
		if(localLikeList!=null&&localLikeList.contains(albumID)){
			localLikeList.remove(albumID);
			SerializationUtil.wirteDiggSerialization(context, localLikeList);
		}
	}
}
