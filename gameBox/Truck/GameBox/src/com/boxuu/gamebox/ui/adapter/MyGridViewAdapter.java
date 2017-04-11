package com.boxuu.gamebox.ui.adapter;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boxuu.gamebox.R;
import com.boxuu.gamebox.common.GlobalConstants;
import com.boxuu.gamebox.download.DownloadInfo;
import com.boxuu.gamebox.download.DownloadManager;
import com.boxuu.gamebox.download.DownloadService;
import com.boxuu.gamebox.model.GameInfo;
import com.boxuu.gamebox.ui.view.MyGroup;
import com.boxuu.gamebox.utils.FileUtils;
import com.boxuu.gamebox.utils.ImageLoaderUtil;
import com.boxuu.gamebox.utils.SystemUtil;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class MyGridViewAdapter extends BaseAdapter {

	private Context context;
	private DownloadManager manager;
	private List<GameInfo> list ;
	private boolean isEdit = false;
	
	public MyGridViewAdapter(Context context , boolean edit) {
		this.context = context;
		this.isEdit = edit;
		this.manager = DownloadService.getDownloadManager(context);
	}
	
	public void setEdit(boolean edit) {
		this.isEdit = edit;
		notifyDataSetChanged();
	}
	
	public void setData(List<GameInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		DownloadInfo downloadInfo = null;
		boolean isDownload = false;
		
		if (list == null) {
			//没有数据
			return null;
		}
		
		final ViewHolder holder;
		MyGroup myGroup;
		
		if (convertView == null) {
			holder = new ViewHolder();
			myGroup = new MyGroup(context);
			myGroup.setTag(holder);
		}else {
			myGroup = (MyGroup) convertView;
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.myGroup = myGroup;
		holder.pb = myGroup.getBar();
		holder.tv_name = myGroup.getNameText();
		holder.tv_state = myGroup.getStateText();
		holder.iv_logo = myGroup.getLogoImage();
		
		GameInfo info = list.get(position);
		final String url = info.getDownload();
		final String fileName = info.getName();
		holder.tv_name.setText(info.getName());
		ImageLoaderUtil.getInstance(context).displayImage(info.getIcon(), holder.iv_logo);
		
		for (int j = 0; j < manager.getDownloadInfoListCount(); j++) {
			DownloadInfo aDownloadInfo = manager.getDownloadInfo(j);
			if (aDownloadInfo.getApp_idString() != null
					&& aDownloadInfo.getApp_idString().equals(String.valueOf(position))) {
				downloadInfo = manager.getDownloadInfo(j);
				if (downloadInfo.getState() != State.FAILURE
						&& downloadInfo.getState() != State.SUCCESS) {
					isDownload = true;
				} else if (downloadInfo.getState() == State.SUCCESS) {
					isDownload = false;
					if (!FileUtils.checkFileExists(GlobalConstants.GAME_PATH+fileName+position+".zip")) {
						try {
							manager.removeDownload(downloadInfo);
//							downloadInfo.setState(State.STARTED);
							downloadInfo = null;
						} catch (DbException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
		}
		
		if (downloadInfo!=null && downloadInfo.getState() == State.SUCCESS
				&& FileUtils.checkFileExists(GlobalConstants.GAME_PATH+fileName+position+".zip")) {
			holder.pb.setProgress(0);
			if (!isEdit) {
				holder.tv_state.setText("已下载");
				holder.tv_state.setBackgroundResource(R.drawable.down_finish);
			}else {
				holder.tv_state.setText("删除");
				holder.tv_state.setBackgroundResource(R.drawable.delete);
			}
		}else{
			if (downloadInfo!=null && downloadInfo.getState() == State.FAILURE
				&& FileUtils.checkFileExists(GlobalConstants.GAME_PATH+fileName+position+".zip")) {
				FileUtils.deleteFile(GlobalConstants.GAME_PATH+fileName+position+".zip");
			}
			holder.tv_state.setText("下载");
			holder.pb.setProgress(0);
			holder.tv_state.setBackgroundResource(R.drawable.down);
		}	
		
		if (isDownload) {
			holder.setDownLoadInfo(downloadInfo);
			holder.refresh();
		}else {
			holder.setDownLoadInfo(null);
		}
		
		if (isDownload) {
			HttpHandler<File> handler = downloadInfo.getHandler();
			if (handler != null) {
				RequestCallBack<File> callBack = handler.getRequestCallBack();
				if (callBack instanceof DownloadManager.ManagerCallBack) {
					DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
					managerCallBack.setBaseCallBack(new DownloadRequestCallBack());
				}
				callBack.setUserTag(new WeakReference<ViewHolder>(holder));
			}
		}
		
		final DownloadInfo cur_info = downloadInfo;
		holder.myGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String target = GlobalConstants.BASE_PATH+GlobalConstants.GAME_PATH+fileName+position+".zip";
				try {
					
					if (cur_info == null ) {
						Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
						holder.tv_state.setBackgroundResource(Color.TRANSPARENT);
						manager.addNewDownload(url, fileName+".zip", target, true, true, 
								null, String.valueOf(position), "111111");
					}else if (cur_info != null && cur_info.getState() == State.LOADING
							&& FileUtils.checkFileExists(GlobalConstants.GAME_PATH+fileName+position+".zip")) {
//						Toast.makeText(context, "正在下载", Toast.LENGTH_SHORT).show();
						manager.stopDownload(cur_info);
					}else if (cur_info != null && cur_info.getState() == State.SUCCESS && 
						FileUtils.checkFileExists(GlobalConstants.GAME_PATH+fileName+position+".zip")) {
						if (!isEdit) {
							FileUtils.startFile(context, fileName+position);
						}else {
							FileUtils.deleteFile(GlobalConstants.GAME_PATH+fileName+position+".zip");
							FileUtils.deleteDirectory(SystemUtil.getCacheFolder(
									SystemUtil.UNZIP_TYPE)+File.separator+fileName+position);
							manager.removeDownload(cur_info);
							holder.tv_state.setText("下载");
							holder.pb.setProgress(0);
							holder.tv_state.setBackgroundResource(Color.TRANSPARENT);
						}
					}else if (cur_info != null && cur_info.getState() == State.FAILURE) {
						manager.resumeDownload(cur_info,null);
					}else if (cur_info != null && cur_info.getState() == State.CANCELLED) {
						manager.resumeDownload(cur_info,null);
					}else if(cur_info.getState() == State.WAITING) { 
						Toast.makeText(context, "已加入下载列表", Toast.LENGTH_SHORT).show();
					}
//					else{
//						Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
//						holder.tv_state.setBackgroundResource(Color.TRANSPARENT);
//						manager.addNewDownload(url, fileName+".zip", target, true, true, 
//								null, String.valueOf(position), "111111");
//					}
				} catch (DbException e) {
					e.printStackTrace();
				}
				
				notifyDataSetChanged();
			}
		});
		
//		holder.iv_logo.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				notifyDataSetChanged();
//				if (cur_info != null && cur_info.getState()==State.SUCCESS && 
//					FileUtils.checkFileExists(GlobalConstants.GAME_PATH+fileName+position+".zip")) {
//					FileUtils.startFile(context, fileName+position);
//				}else {
//					Toast.makeText(context, "文件还未下载完成", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
		
		return myGroup;
	}
	
	public class ViewHolder {
		MyGroup myGroup;
		RelativeLayout rl;
		ProgressBar pb;
		TextView tv_state;
		TextView tv_name;
		ImageView iv_logo;
		
		public DownloadInfo downloadInfo;

		public void setDownLoadInfo(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}
		
		public void refresh() {
			if (downloadInfo == null) {
				return;
			}

			if (downloadInfo.getFileLength() > 0) {
				pb.setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength()));
			} else {
				pb.setProgress(0);
			}

			HttpHandler.State state = downloadInfo.getState();
			switch (state) {
			case WAITING:
				tv_state.setText("等待");
				tv_state.setBackgroundResource(Color.TRANSPARENT);
				break;
			case STARTED:
//				tv_state.setText("取消");
//				tv_state.setBackgroundResource(Color.TRANSPARENT);
				break;
			case CANCELLED:
				tv_state.setText("暂停");
				tv_state.setBackgroundResource(Color.TRANSPARENT);
				break;
			case LOADING:
				tv_state.setText("下载" + (int) (downloadInfo.getProgress() * 100 / downloadInfo.getFileLength()) + "%");
				tv_state.setBackgroundResource(Color.TRANSPARENT);
				break;
			case SUCCESS:
				tv_state.setText("已下载");
				tv_state.setBackgroundResource(R.drawable.progerss_done);
				break;
			case FAILURE:
				tv_state.setText("重试");
				tv_state.setBackgroundResource(Color.TRANSPARENT);
				break;
			default:
				break;
			}
		}
	}
	
	private class DownloadRequestCallBack extends RequestCallBack<File> {

		@SuppressWarnings("unchecked")
		private void refreshListItem() {
			if (userTag == null)
				return;
			WeakReference<ViewHolder> tag = (WeakReference<ViewHolder>) userTag;
			ViewHolder holder = tag.get();
			if (holder != null) {
				holder.refresh();
			}
		}
		
		@Override
		public void onStart() {
			refreshListItem();
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			refreshListItem();
		}

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			refreshListItem();
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			refreshListItem();
		}

		@Override
		public void onCancelled() {
			refreshListItem();
		}
	}
	
}
