package com.bccv.meitu.ui.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.api.UpLoadAPI;
import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.localphoto.SelectPhotoActivity;
import com.bccv.meitu.localphoto.bean.PhotoInfo;
import com.bccv.meitu.localphoto.bean.UpdatePhotoInfo;
import com.bccv.meitu.model.DoSpecialResBean;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.ui.adapter.UploadGridViewAdapter;
import com.bccv.meitu.upload.UploadFileNetUtil.UploadAction;
import com.bccv.meitu.upload.UploadFileNetUtil.UploadListener;
import com.bccv.meitu.utils.Logger;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.CommonDialog;
import com.bccv.meitu.view.CommonDialog.onDialogBtnClickListener;

public class UploadActivity extends BaseActivity implements OnItemClickListener{

	private View rl_root;
	private View waitting_layout;
	private View leftBtn;
	private View RightBtn;
	private TextView titleText;
	private GridView pic_gv;
	private EditText description_et;
	private UploadGridViewAdapter adapter;
	private CheckBox upload_cb;
	private TextView upload_clause;
	
	private View upload_cb_parent;
	
	private ArrayList<UpdatePhotoInfo> photos;
	
	private CommonDialog dialog;
	
	private static final int OPENCAMERA_REQUESTCODE = 101;
	private static final int OPENLOCALALBUM_REQUESTCODE = 102;
	
	private File cameraPhotofile;
	private Uri cameraPhotoUri;
	
	private int special_id = -1;
	
	private boolean isUploading = false;
	private boolean interruptUpload = false; 
	
	private UpdatePhotoInfo defultPhotoInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadup);
		initView();
		initData();
	}
	
	private void initView(){
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		waitting_layout = findViewById(R.id.waitting_layout);
		leftBtn = findViewById(R.id.left_buton);
		RightBtn = findViewById(R.id.right_buton);
		titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText("我要上传");
		leftBtn.setOnClickListener(this);
		RightBtn.setOnClickListener(this);
		waitting_layout.setOnClickListener(this);
		waitting_layout.setVisibility(View.GONE);
		
		pic_gv = (GridView) findViewById(R.id.pic_gv);
		description_et = (EditText) findViewById(R.id.description_et);
		upload_cb = (CheckBox) findViewById(R.id.upload_cb);
		upload_clause = (TextView) findViewById(R.id.upload_clause);
		upload_cb_parent = findViewById(R.id.upload_cb_parent);
		upload_cb_parent.setOnClickListener(this);
		
		ColorStateList createColorStateList = createColorStateList(0xff999999, 0xffffffff);
		
		upload_clause.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG );
		upload_clause.getPaint().setAntiAlias(true);
		upload_clause.setTextColor(createColorStateList);
		upload_clause.setOnClickListener(this);
		
		photos = new ArrayList<UpdatePhotoInfo>();
		defultPhotoInfo = new UpdatePhotoInfo();
		defultPhotoInfo.setDefult(true);
		defultPhotoInfo.setCover(false);
		photos.add(defultPhotoInfo);
		adapter = new UploadGridViewAdapter(mContext, photos,defultPhotoInfo);
		pic_gv.setAdapter(adapter);
		
		pic_gv.setOnItemClickListener(this);
		
		dialog = new CommonDialog(this);
		
		ViewGroup decor = (ViewGroup) this.getWindow().getDecorView();
		ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
		decorChild.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return isUploading;
			}
		});
		
	}
	
	private void initData(){
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_buton:
			//返回界面
			if(isUploading){
				dialog.show(new InterruptUploadDialogListener(), "提示", 
						"图片正在上传中...", "是否中断退出?", false);
			}else{
				finish();
			}
			break;
		case R.id.right_buton:
				if(photos.size()<=1){
					showShortToast("请先选择要上传的照片");
					return;
				}
				if(upload_cb.isChecked()){
					if(SystemUtil.isNetOkWithToast(mContext)){
						uploadPhotos();
					}
				}else{
					showShortToast("请您确认已阅读悦色平台图片上传条款");
				}
			break;
		case R.id.photo_camera:
			long currentTimeMillis = System.currentTimeMillis();
			String path = SystemUtil.getMeiTuCacheFolder(SystemUtil.CACHE_IMAGE_TYPE)
		    		+ File.separator  + currentTimeMillis +".jpg";
			
		    cameraPhotofile = new File(path);
		    cameraPhotoUri = Uri.fromFile(cameraPhotofile);
			SystemUtil.getPhotoFromCamera(this, cameraPhotoUri, OPENCAMERA_REQUESTCODE);
			
			if(mPopupWindow!=null){
				mPopupWindow.dismiss();
			}
			break;
		case R.id.photo_album:
			Intent localAlbumIntent = new Intent(mContext,SelectPhotoActivity.class);
			localAlbumIntent.putExtra(SelectPhotoActivity.SELECTED_NUM, (photos.size()-1));
			startActivityForResult(localAlbumIntent, OPENLOCALALBUM_REQUESTCODE);
			if(mPopupWindow!=null){
				mPopupWindow.dismiss();
			}
			break;
		case R.id.quit:
			if(mPopupWindow!=null){
				mPopupWindow.dismiss();
			}
			break;
		case R.id.upload_clause:
			Intent intent = new Intent(this,ScoreActivity.class);
			intent.putExtra("title", "图片上传条款");
			intent.putExtra("url", GlobalConstants.UPLOAD_CLAUSE_URL);
			startActivity(intent);
			break;
		case R.id.upload_cb_parent:
			upload_cb.setChecked(!upload_cb.isChecked());
			break;

		default:
			break;
		} 
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position>=0&&position<photos.size()){
			
			UpdatePhotoInfo updatePhotoInfo = photos.get(position);
			if(updatePhotoInfo.isDefult()){
				initPopuptWindow();
			}else{
				clearCoverState();
				updatePhotoInfo.setCover(true);
				adapter.notifyDataSetChanged();
			}
		}
	}
	/**
	 * 清除封面数据
	 */
	private void clearCoverState(){
		for (UpdatePhotoInfo photoInfo : photos) {
			if(photoInfo.isCover()){
				photoInfo.setCover(false);
				break;
			}
		}
	}
	
	/**
	 * 上传图片
	 */
	private void uploadPhotos(){
		
		waitting_layout.setVisibility(View.VISIBLE);
		
		if(special_id==-1){ //创建专辑
			String special_names = description_et.getText().toString().trim();
			if(TextUtils.isEmpty(special_names)){
				showShortToast("给自己的专辑说点什么吧");
				waitting_layout.setVisibility(View.GONE);
				return;
//				special_names = "此作者很懒，什么都没有留下。";
			}
			
			NetWorkAPI.dospecial(mContext, special_names, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if(response.success && (response instanceof DoSpecialResBean) ){
						DoSpecialResBean data = (DoSpecialResBean) response;
						special_id = data.getSpecial_id();
						
						Logger.v(TAG, "uploadPhotos onResult", "special_id : " + special_id );
						if(special_id==-1){
							showShortToast("专辑创建失败,请重试");
							isUploading = false;
							RightBtn.setClickable(false);
							waitting_layout.setVisibility(View.GONE);
							return;
						}
						if(interruptUpload){//是否中断
							waitting_layout.setVisibility(View.GONE);
							return;
						}
						UpLoadAPI.getInstance().upLoadFile(mContext, photos.get(0), special_id, new MyUploadListener());
						
					}else{
						showShortToast("专辑创建失败,请重试");
						Logger.v(TAG, "uploadPhotos onResult", "response.success : " + response.success );
						isUploading = false;
						RightBtn.setClickable(false);
						waitting_layout.setVisibility(View.GONE);
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					showShortToast("专辑创建失败,请重试");
					Logger.v(TAG, "uploadPhotos onError", ""+errorMsg);
					isUploading = false;
					RightBtn.setClickable(false);
					waitting_layout.setVisibility(View.GONE);
				}
				
				@Override
				public void onCancel() {}
			});
			
		}else{ //在原有专辑上上传图片
			if(interruptUpload){//是否中断
				waitting_layout.setVisibility(View.GONE);
				return;
			}
			UpLoadAPI.getInstance().upLoadFile(mContext, photos.get(0), special_id, new MyUploadListener());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		if(resultCode!=0){
			switch (requestCode) {
			case OPENCAMERA_REQUESTCODE://打开相机返回
				
				if(cameraPhotofile!=null && cameraPhotoUri!=null){
					UpdatePhotoInfo updatePhotoInfo = new UpdatePhotoInfo();
					updatePhotoInfo.setPath_absolute(cameraPhotofile.getAbsolutePath());
					updatePhotoInfo.setPath_file(cameraPhotoUri.toString());
					updatePhotoInfo.setCover(false);
					updatePhotoInfo.setDefult(false);
					updatePhotoInfo.setUploadState(UpdatePhotoInfo.UNUPLOAD);
					photos.add(photos.size()-1, updatePhotoInfo);
					
					if(photos.size()>GlobalConstants.MAX_UPLOAD_NUM){
						photos.remove(photos.size()-1);
					}
				}
				adapter.resetWithNoneCover();
				adapter.notifyDataSetChanged();
				
				cameraPhotofile = null;
				cameraPhotoUri = null;
				
				break;
			case OPENLOCALALBUM_REQUESTCODE://打开相册返回
				Serializable serializableData = intent.getSerializableExtra(SelectPhotoActivity.SELECT_PHOTOS);
				if(serializableData!=null){
					@SuppressWarnings("unchecked")
					ArrayList<PhotoInfo> dataList = (ArrayList<PhotoInfo>) serializableData;
					for (PhotoInfo photoInfo : dataList) {
						UpdatePhotoInfo updatePhotoInfo = new UpdatePhotoInfo();
						updatePhotoInfo.setImage_id(photoInfo.getImage_id());
						updatePhotoInfo.setPath_absolute(photoInfo.getPath_absolute());
						updatePhotoInfo.setPath_file(photoInfo.getPath_file());
						updatePhotoInfo.setCover(false);
						updatePhotoInfo.setDefult(false);
						updatePhotoInfo.setUploadState(UpdatePhotoInfo.UNUPLOAD);
						photos.add(photos.size()-1, updatePhotoInfo);
						
						if(photos.size()>GlobalConstants.MAX_UPLOAD_NUM){//已到达最大数量
							photos.remove(photos.size()-1);
							break; 
						}
						
					}
					adapter.resetWithNoneCover();
					adapter.notifyDataSetChanged();
				}
				
				break;

			default:
				super.onActivityResult(requestCode, resultCode, intent);
				break;
			}
		}
	}
	
	/*
	 * 创建PopupWindow
	 */
	private PopupWindow mPopupWindow;
	TextView photo_camera;
	TextView photo_album;
	TextView quit;
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.user_icon, null);
		photo_camera = (TextView) popupWindow.findViewById(R.id.photo_camera);
		photo_album = (TextView) popupWindow.findViewById(R.id.photo_album);
		quit = (TextView) popupWindow.findViewById(R.id.quit);
		photo_camera.setOnClickListener(this);
		photo_album.setOnClickListener(this);
		quit.setOnClickListener(this);
		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(-000000));
		mPopupWindow.setAnimationStyle(R.style.anim_photo_select);
		mPopupWindow.showAtLocation(rl_root, Gravity.BOTTOM, 0, 0);
	}
	
	
	private class MyUploadListener implements UploadListener{

		@Override
		public void uploadAction(UploadAction action, UpdatePhotoInfo photoInfo) {
			
			Logger.v(TAG, "uploadPhotos uploadAction", "action : " + action.name());
			switch (action) {
			case PREEXECUTE://上传前action
				isUploading = true;
				RightBtn.setClickable(false);
				break;
			case CANCEL://上传取消action
				interruptUpload = true;
				break;
			case UPLOAD_SUCCESS://上传成功action
				if(interruptUpload){//是否中断
					waitting_layout.setVisibility(View.GONE);
					return;
				}
				photos.remove(photoInfo);
				if(!photos.get(photos.size()-1).isDefult()){
					photos.add(defultPhotoInfo);
				}
				
				adapter.notifyDataSetChanged();
				UpdatePhotoInfo nextUpdata1 = getNextUpdata();
				if(nextUpdata1!=null){
					UpLoadAPI.getInstance().upLoadFile(mContext, nextUpdata1, special_id, new MyUploadListener());
				}else{
					if(photos.size()==1){
						showShortToast("上传完毕");
						finish();
						waitting_layout.setVisibility(View.GONE);
//						dialog.show(new ContinueUploadDialogListener(), "提示", 
//								"有"+(photos.size()-1)+"图片未上传成功", "是否继续上传?", false);
						
					}else{
						waitting_layout.setVisibility(View.GONE);
						dialog.show(new ContinueUploadDialogListener(), "提示", 
								"有"+(photos.size()-1)+"图片未上传成功", "是否继续上传?", false);
					}
					isUploading = false;
					RightBtn.setClickable(true);
				}
				break;
			case UPLOAD_FAILED://上传失败action
				if(interruptUpload){//是否中断
					waitting_layout.setVisibility(View.GONE);
					return;
				}
				UpdatePhotoInfo nextUpdata2 = getNextUpdata();
				if(nextUpdata2!=null){
					UpLoadAPI.getInstance().upLoadFile(mContext, nextUpdata2, special_id, new MyUploadListener());
				}else{
					waitting_layout.setVisibility(View.GONE);
					dialog.show(new ContinueUploadDialogListener(), "提示", 
							"有"+(photos.size()-1)+"图片未上传成功", "是否继续上传?", false);
					isUploading = false;
					RightBtn.setClickable(true);
				}
				break;
			default:
				break;
			}
		}
		
		@Override
		public void updateUploadProgress(long total, long current) {
			Logger.v(TAG, "uploadPhotos updateUploadProgress", "total : " + total + " current : " + current);
		}
	}
	
	/**
	 * 获取下一个上传的对象
	 * @return
	 */
	private UpdatePhotoInfo getNextUpdata(){
		for (UpdatePhotoInfo info : photos) {
			if(info.getUploadState() == UpdatePhotoInfo.UNUPLOAD
					&& !info.isDefult()){
				return info;
			}
		}
		return null;
	}
	
	
	/**
	 *	继续上传 dialog 按钮监听
	 *
	 * @author liukai
	 * @date 2014-12-11
	 */
	private class ContinueUploadDialogListener implements onDialogBtnClickListener{

		@Override
		public void onCancelClick() {
			finish();
			dialog.cancel();
		}

		@Override
		public void onEnterClick() {
			if(SystemUtil.isNetOkWithToast(mContext)){
				for (UpdatePhotoInfo info : photos) {
					if(!info.isDefult()){
						info.setUploadState(UpdatePhotoInfo.UNUPLOAD);
					}
				}
				UpdatePhotoInfo nextUpdata = getNextUpdata();
				if(nextUpdata!=null){
					isUploading = true;
					RightBtn.setClickable(false);
					waitting_layout.setVisibility(View.VISIBLE);
					UpLoadAPI.getInstance().upLoadFile(mContext, nextUpdata, special_id, new MyUploadListener());
				}
				dialog.cancel();
			}
		}
	}
	/**
	 *	中断上传dialog 按钮监听
	 *
	 * @author liukai
	 * @date 2014-12-11
	 */
	private class InterruptUploadDialogListener implements onDialogBtnClickListener{
		
		@Override
		public void onCancelClick() {
			dialog.cancel();
		}
		
		@Override
		public void onEnterClick() {
			finish();
			dialog.cancel();
		}
	}
	
	@Override
	public void onBackPressed() {
		if(isUploading){
			dialog.show(new InterruptUploadDialogListener(), "提示", 
					"图片正在上传中...", "是否中断退出?", false);
			return;
		}
		super.onBackPressed();
	}
	
	
    /**
     * 创建text的ColorStateList
     * @param normal 	正常状态颜色  (16进制数字)
     * @param pressed	下按状态颜色	(16进制数字)
     * @return
     */
    private ColorStateList createColorStateList(int normal, int pressed) {  
        int[] colors = new int[] { pressed, normal, normal };  
        int[][] states = new int[3][];  
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };  
        states[1] = new int[] { android.R.attr.state_enabled };  
        states[2] = new int[] {};  
        ColorStateList colorList = new ColorStateList(states, colors);  
        return colorList;  
    }
	
	
}
