package com.bccv.meitu.upload;

import java.io.File;
import java.util.HashMap;

import org.apache.http.entity.mime.CustomMultiPartEntity.ProgressListener;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.bccv.meitu.localphoto.bean.UpdatePhotoInfo;
import com.bccv.meitu.upload.UploadFileNetUtil.UploadAction;
import com.bccv.meitu.upload.UploadFileNetUtil.UploadListener;
import com.bccv.meitu.utils.ImageZipUtil;
import com.bccv.meitu.utils.Logger;

public class UploadFileTask extends AsyncTask<Object, Long, UpdatePhotoInfo> {

	private static final String TAG = "UploadFileTask";
	
	private Context context;
	private UploadListener listener;
	private long total;
	
	public UploadFileTask(Context context,UploadListener listener){
		this.context = context;
		this.listener = listener;
	}
	
    @Override  
    protected void onPreExecute() {
		if(listener!=null){
			listener.uploadAction(UploadAction.PREEXECUTE,null);
		}
    }  
	
	@SuppressWarnings("unchecked")
	@Override
	protected UpdatePhotoInfo doInBackground(Object... params) {
		
		if(params.length>=3){
			UpdatePhotoInfo photoInfo = null;
			String url = null;
			HashMap<String,String> param = null;
			
			if(params[0] instanceof UpdatePhotoInfo){
				photoInfo = (UpdatePhotoInfo) params[0];
			}
			if(params[1] instanceof String){
				url = (String) params[1];
			}
			if(params[2] instanceof HashMap){
				param = (HashMap<String, String>) params[2];
			}
			
			File zipPic = null;
			if(photoInfo!=null&&url!=null){
				Uri uri = null;
				if(!TextUtils.isEmpty(photoInfo.getPath_file())){
					uri = Uri.parse(photoInfo.getPath_file());
				}
				
				zipPic = ImageZipUtil.zipPic(photoInfo.getPath_absolute(), 480, 800, uri, context);
			}
			
			if(zipPic==null||!zipPic.exists()){
				photoInfo.setUploadState(UpdatePhotoInfo.UPLOAD_FAILED);
				return photoInfo;
			}
			
			String response = UploadFileNetUtil.uploadFile(context,url,zipPic,param,new UploadProgressListener());
			
			boolean success = false;
			
			Logger.v(TAG, "doInBackground", " response : " + response);
			if(!TextUtils.isEmpty(response)){
				try {
					JSONObject jsonObject = new JSONObject(response);
						success = ("ok".equals(jsonObject.getString("request")));
				} catch (Exception e) {
					Logger.e(TAG, "doInBackground", e.getMessage()); 
					success = false;
				}
			}
			
			if(success){
				photoInfo.setUploadState(UpdatePhotoInfo.UPLOAD_SUCCESS);
			}else{
				photoInfo.setUploadState(UpdatePhotoInfo.UPLOAD_FAILED);
			}
			
			return	photoInfo;
		}else{
			Logger.v(TAG, "doInBackground", " params is error !!! ");
			return null;
		}
	}
	
	@Override  
	protected void onProgressUpdate(Long... progress) { 
		total = progress[0];
		//更新进度
		if(listener!=null && progress[0] != progress[1]){
			listener.updateUploadProgress(progress[0], progress[1]);
		}
	}  

    @Override  
    protected void onPostExecute(UpdatePhotoInfo finishedUpload) {  
    	
    	//TODO 解析返回报文
    	boolean success = (finishedUpload.getUploadState() == UpdatePhotoInfo.UPLOAD_SUCCESS);//TODO
    	// 上传完毕或上传失败  更新UI
		if(listener!=null){
			if(success){
				listener.updateUploadProgress(total, total);
				
				//TODO 返回解析后的数据
				listener.uploadAction(UploadAction.UPLOAD_SUCCESS,finishedUpload);
			}else{
				listener.uploadAction(UploadAction.UPLOAD_FAILED,finishedUpload);
			}
		}
    }

    @Override
    protected void onCancelled() {
    	if(listener!=null){
    		listener.uploadAction(UploadAction.CANCEL,null);
    	}
    }
    
    /**
     * 执行方法
     * 
     * @param file		需要上传的文件
     * @param url		上传的url地址
     * @param params	所需要的参数
     */
    public void dpoExecute(UpdatePhotoInfo photoInfo,String url,HashMap<String,String> params){
    	super.execute(photoInfo,url,params);
    }
    
    
	public class UploadProgressListener implements ProgressListener{
		
		private long totalSize;				//总大小
		
		public void setTotalSize(long totalSize){
			this.totalSize = totalSize;
		}
		
		public long getTotalSize(){
			return totalSize;
		}
		
		@Override
		public void transferred(long num) {
			publishProgress(totalSize, num);
		}
	}
    
}
