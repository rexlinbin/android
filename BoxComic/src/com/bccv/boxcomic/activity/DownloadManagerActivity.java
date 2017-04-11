package com.bccv.boxcomic.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.ChapterDownloadAdapter;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.lidroid.xutils.exception.DbException;

public class DownloadManagerActivity extends BaseActivity {
	private Comic comic;
	private ListView listView;
	private ChapterDownloadAdapter adapter;
	private List<Chapter> list;
	
	private TextView titleTextView;
	
	private TextView editTextView;
	private TextView muluTextView;
	
	private boolean isEdit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		GlobalParams.isEditing = false;
		comic = (Comic) getIntent().getExtras().getSerializable("comic");
		setContentView(R.layout.activity_downloadmanager);
	
		setBack();
		titleTextView = (TextView) findViewById(R.id.downloadTitle_textView);
		titleTextView.setText(comic.getComic_title());
		
		listView = (ListView) findViewById(R.id.download_listView);
		listView.setSelector(new ColorDrawable(android.R.color.transparent));
		
		list = new ArrayList<Chapter>();
		adapter = new ChapterDownloadAdapter(getApplicationContext(), list, comic);
		listView.setAdapter(adapter);
		
		muluTextView = (TextView) findViewById(R.id.mulu_textView);
		muluTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ComicInfoActivity.class);
				intent.putExtra("mainitem", comic.getComic_id());
				startActivity(intent);
			}
		});
		
		editTextView = (TextView) findViewById(R.id.edit_textView);
		editTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isEdit = !isEdit;
				if (isEdit) {
					editTextView.setText("删除");
				}else {
					deleteData();
					editTextView.setText("编辑");
				}
				GlobalParams.isEditing = isEdit;
				adapter.notifyDataSetChanged();
			}
		});
		setData();
	}
	
	private void setData(){
		DownloadManager downloadManager = DownloadService.getDownloadManager(getApplicationContext());
		for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
			DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
			if (downloadInfo.getApp_idString().equals(comic.getComic_id())) {
				Chapter chapter = new Chapter();
				chapter.setChapter_id(downloadInfo.getChapter_id());
				chapter.setChapter_title(downloadInfo.getChapter_name());
				chapter.setChapter_size(downloadInfo.getChapter_size());
				boolean hasChapter = false;
				for (int j = 0; j < list.size(); j++) {
					Chapter listChapter = list.get(j);
					if (listChapter.getChapter_id().equals(chapter.getChapter_id())) {
						hasChapter = true;
						break;
					}
				}
				if (!hasChapter) {
					list.add(chapter);
				}
				
			}
		}
		adapter.notifyDataSetChanged();
	}
	
	private void deleteData(){
		DownloadManager downloadManager = DownloadService.getDownloadManager(getApplicationContext());
		List<Chapter> deleteChapters = new ArrayList<Chapter>();
		List<DownloadInfo> deleteDownloadInfos = new ArrayList<DownloadInfo>();
		for (Chapter chapter : list) {
			if (chapter.isSelect()) {
				deleteChapters.add(chapter);
				for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
					DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
					if (downloadInfo.getChapter_id().equals(chapter.getChapter_id())) {
						deleteDownloadInfos.add(downloadInfo);
					}
				}
			}
		}
		if (deleteDownloadInfos.size() > 0) {
			for (int i = 0; i < deleteDownloadInfos.size(); i++) {
				try {
					downloadManager.removeDownload(deleteDownloadInfos.get(i));
					if (FileUtils.checkDirPathExists(GlobalParams.downloadPathString + "/" + comic.getComic_id() + "/" + GlobalParams.onlineFromNameIdString + "/" + deleteDownloadInfos.get(i).getChapter_id())) {
						FileUtils.deleteDirectoryPath(GlobalParams.downloadPathString + "/" + comic.getComic_id() + "/" + GlobalParams.onlineFromNameIdString + "/" + deleteDownloadInfos.get(i).getChapter_id());
					}
				} catch (DbException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for (int i = 0; i < deleteChapters.size(); i++) {
				list.remove(deleteChapters.get(i));
			}
			
			
			adapter.notifyDataSetChanged();
		}
		
	}
	
	private void setBack(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}

}
