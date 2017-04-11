package com.bccv.boxcomic.adapter;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.ZipUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ChapterDownloadAdapter extends BaseAdapter {

	private Context context;
	private List<Chapter> list;
	private Comic comic;
	private DownloadManager downloadManager;

	public ChapterDownloadAdapter(Context context, List<Chapter> list,
			Comic comic) {
		this.context = context;
		this.list = list;
		this.comic = comic;
		this.downloadManager = DownloadService.getDownloadManager(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		boolean isPause = false;
		boolean isSuccess = true;
		List<DownloadInfo> downloadlist = new ArrayList<DownloadInfo>();
		for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
			DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
			if (downloadInfo.getChapter_id().equals(
					list.get(position).getChapter_id())) {
				if (downloadInfo.getState() != State.SUCCESS) {
					isSuccess = false;
					if (downloadInfo.getState() == State.CANCELLED) {
						isPause = true;
					}
				}
				downloadlist.add(downloadInfo);
			}
		}

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.listitem_downloadmanager, null);

			ViewUtils.inject(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.setPosition(position);
		viewHolder.refresh();
		viewHolder.titleTextView.setText(list.get(position).getChapter_title());
		viewHolder.sizeTextView.setText(list.get(position).getChapter_size());
		for (int i = 0; i < downloadlist.size(); i++) {
			HttpHandler<File> handler = downloadlist.get(i).getHandler();
			if (handler != null) {
				RequestCallBack callBack = handler.getRequestCallBack();
				if (callBack instanceof DownloadManager.ManagerCallBack) {
					DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
					managerCallBack
							.setBaseCallBack(new DownloadRequestCallBack());

				}
				callBack.setUserTag(new WeakReference<ViewHolder>(viewHolder));
			}
		}

		if (GlobalParams.isEditing) {
			final ImageView imageView = viewHolder.imageView;
			if (list.get(position).isSelect()) {
				imageView
						.setBackgroundResource(R.drawable.check_box_select);
				viewHolder.stateLayout.setSelected(true);
			} else {
				imageView.setBackgroundResource(R.drawable.check_box);
				viewHolder.stateLayout.setSelected(false);
			}
			viewHolder.stateLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					v.setSelected(!v.isSelected());
					if (v.isSelected()) {
						imageView
								.setBackgroundResource(R.drawable.check_box_select);
					} else {
						imageView.setBackgroundResource(R.drawable.check_box);
					}
					list.get(position).setSelect(v.isSelected());
				}
			});
		} else {
			if (isSuccess) {
				viewHolder.imageView
						.setBackgroundResource(R.drawable.download_succes);
				viewHolder.progressBar.setProgress(100);
			} else if (isPause) {
				viewHolder.imageView
						.setBackgroundResource(R.drawable.check_box_start);
			} else {
				viewHolder.imageView
						.setBackgroundResource(R.drawable.check_box_stop);
			}
			viewHolder.stateLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
		}

		return convertView;
	}

	class ViewHolder {

		@ViewInject(R.id.size_textView)
		TextView sizeTextView;
		@ViewInject(R.id.downloadTitle_textView)
		TextView titleTextView;
		@ViewInject(R.id.download_stop_imageView)
		ImageView imageView;
		@ViewInject(R.id.download_progressBar)
		ProgressBar progressBar;
		@ViewInject(R.id.downloadState_layout)
		LinearLayout stateLayout;

		private int position;

		public void setPosition(int position) {
			this.position = position;
		}

		@OnClick(R.id.downloadState_layout)
		public void stop(View view) {
			if (GlobalParams.isEditing) {
				return;
			}
			boolean isPause = false;
			boolean isSuccess = true;
			List<DownloadInfo> downlist = new ArrayList<DownloadInfo>();
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
				if (downloadInfo.getChapter_id().equals(
						list.get(position).getChapter_id())) {
					if (downloadInfo.getState() != State.SUCCESS) {
						isSuccess = false;
						if (downloadInfo.getState() == State.CANCELLED) {
							isPause = true;
						}
					}
					downlist.add(downloadInfo);
				}
			}

			if (isPause) {
				imageView.setBackgroundResource(R.drawable.check_box_stop);
				try {
					for (int i = 0; i < downlist.size(); i++) {
						downloadManager.resumeDownload(downlist.get(i),
								new DownloadRequestCallBack());
					}

				} catch (DbException e) {
					LogUtils.e(e.getMessage(), e);
				}
				notifyDataSetChanged();
			} else if (!isSuccess) {
				imageView.setBackgroundResource(R.drawable.check_box_start);
				try {
					for (int i = 0; i < downlist.size(); i++) {
						downloadManager.stopDownload(downlist.get(i));
					}
				} catch (DbException e) {
					LogUtils.e(e.getMessage(), e);
				}
				notifyDataSetChanged();
			}

		}

		public void remove() {
			List<DownloadInfo> downlist = new ArrayList<DownloadInfo>();
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
				if (downloadInfo.getChapter_id().equals(
						list.get(position).getChapter_id())) {
					downlist.add(downloadInfo);
				}
			}
			try {
				for (int i = 0; i < downlist.size(); i++) {
					downloadManager.removeDownload(downlist.get(i));
					String downloadFileNameString = list.get(position)
							.getChapter_id();
					if (FileUtils
							.checkDirPathExists(GlobalParams.downloadPathString
									+ "/" + comic.getComic_id() + "/"
									+ GlobalParams.onlineFromNameIdString + "/"
									+ downloadFileNameString)) {
						FileUtils
								.deleteDirectoryPath(GlobalParams.downloadPathString
										+ "/"
										+ comic.getComic_id()
										+ "/"
										+ GlobalParams.onlineFromNameIdString
										+ "/" + downloadFileNameString);
					}
				}
				notifyDataSetChanged();

			} catch (DbException e) {
				LogUtils.e(e.getMessage(), e);
			}
		}

		public void refresh() {
			List<DownloadInfo> downlist = new ArrayList<DownloadInfo>();
			for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
				DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
				if (downloadInfo.getChapter_id().equals(
						list.get(position).getChapter_id())) {
					downlist.add(downloadInfo);
				}
			}
			String downloadFileNameString = list.get(position).getChapter_id();
			String path = GlobalParams.downloadPathString + "/"
					+ comic.getComic_id() + "/"
					+ GlobalParams.onlineFromNameIdString + "/"
					+ downloadFileNameString;
			long fileList = FileUtils.getFileCount(path);
			if (fileList == downlist.size()) {
				for (int i = 0; i < downlist.size(); i++) {
					if (downlist.get(i).getState() != State.SUCCESS) {
						return;
					}
				}
				progressBar
						.setProgress((int) (fileList * 100 / downlist.size()));
			} else {
				progressBar
						.setProgress((int) (fileList * 100 / downlist.size()));
			}
			notifyDataSetChanged();
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
