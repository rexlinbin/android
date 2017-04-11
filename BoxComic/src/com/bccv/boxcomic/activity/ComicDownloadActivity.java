package com.bccv.boxcomic.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.ComicDownloadAdapter;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.BaseActivity;
import com.lidroid.xutils.http.HttpHandler.State;

public class ComicDownloadActivity extends BaseActivity {
	
	private ListView downloadListView;
	private ComicDownloadAdapter adapter;
	private List<Comic> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comicdownload);
	
		downloadListView = (ListView) findViewById(R.id.download_listView);
		downloadListView.setSelector(new ColorDrawable(android.R.color.transparent));
		list = new ArrayList<Comic>();
		adapter = new ComicDownloadAdapter(getApplicationContext(), list);
		downloadListView.setAdapter(adapter);
		downloadListView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteComic();
				return true;
			}
		});
		
		downloadListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ComicDownloadActivity.this, DownloadManagerActivity.class);
				intent.putExtra("comic", list.get(position));
				startActivity(intent);
			}
		});
		
		setData();
		
		setBack();
		
	}
	
	private void deleteComic(){
		
	}
	
	private void setData(){
		list.clear();
		DownloadManager downloadManager = DownloadService.getDownloadManager(getApplicationContext());
		for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
			DownloadInfo downloadInfo = downloadManager.getDownloadInfo(i);
			Comic comic = new Comic();
			comic.setComic_titlepic(downloadInfo.getApp_urlString());
			comic.setComic_id(downloadInfo.getApp_idString());
			comic.setComic_title(downloadInfo.getApp_titleString());
			
			boolean isSuccess = true;
			for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
				DownloadInfo downloadInfo1 = downloadManager.getDownloadInfo(j);
				if (downloadInfo1.getApp_idString().equals(comic.getComic_id())) {
					if (downloadInfo1.getState() != State.SUCCESS) {
						isSuccess = false;
						break;
					}
				}
			}
			
			if (isSuccess) {
				comic.setDownloadStateString("下载完成");
			} else {
				comic.setDownloadStateString("下载中");
			}
			
			boolean hasComic = false;
			for (int j = 0; j < list.size(); j++) {
				Comic listComic = list.get(j);
				if (listComic.getComic_id().equals(comic.getComic_id())) {
					hasComic = true;
				}
			}
			if (!hasComic) {
				list.add(comic);
			}
		}
		adapter.notifyDataSetChanged();
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
		setData();
	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}

}
