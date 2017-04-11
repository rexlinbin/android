package com.bccv.boxcomic.adapter;

import java.util.List;
import java.util.Map;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoActivity.OnChapterAll;
import com.bccv.boxcomic.adapter.ComicInfoAdapter.ViewHolder;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.tool.StringUtils;
import com.lidroid.xutils.http.HttpHandler.State;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChapterAdapter extends BaseAdapter {

	private Activity activity;
	private List<Chapter> list;
	private OnChapterAll onChapterAll;

	private boolean isSelect = false;
	
	private DownloadManager downloadManager;
	
	public ChapterAdapter(Activity activity, List<Chapter> list, OnChapterAll onChapterAll, boolean isSelect){
		this.activity = activity;
		this.list = list;
		this.onChapterAll = onChapterAll;
		this.isSelect = isSelect;
		this.downloadManager = DownloadService.getDownloadManager(activity.getApplicationContext());
		
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(activity.getApplicationContext(), R.layout.griditem_comicinfo, null);
			viewHolder.chapterTextView = (TextView) convertView.findViewById(R.id.chapter_textView);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.success_imageView);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (!StringUtils.isEmpty(list.get(position).getChapter_title())) {
			viewHolder.chapterTextView.setText(list.get(position).getChapter_title());
		}
		
		if (isSelect) {
			viewHolder.chapterTextView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					v.setSelected(!v.isSelected());
					list.get(position).setSelect(v.isSelected());
					onChapterAll.getAll(position);
				}
			});
		}else {
			if (list.get(position).getChapter_title().equals("...")) {
				viewHolder.chapterTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						onChapterAll.getAll(-1);
					}
				});
			}else {
				viewHolder.chapterTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						v.setSelected(!v.isSelected());
						list.get(position).setSelect(v.isSelected());
						onChapterAll.getAll(position);
					}
				});
			}
		}
		viewHolder.imageView.setVisibility(View.GONE);
		viewHolder.chapterTextView.setSelected(list.get(position).isSelect());
		
		for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
			DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
			if (downloadInfo.getChapter_id().equals(list.get(position).getChapter_id())) {
				if (downloadInfo.getState() == State.SUCCESS) {
					viewHolder.imageView.setVisibility(View.VISIBLE);
				}
				break;
			}
		}
		
		return convertView;
	}

	class ViewHolder{
		TextView chapterTextView;
		ImageView imageView;
	}
	
}
