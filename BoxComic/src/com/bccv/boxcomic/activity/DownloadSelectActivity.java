package com.bccv.boxcomic.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoActivity.OnChapterAll;
import com.bccv.boxcomic.adapter.ChapterAdapter;
import com.bccv.boxcomic.api.ComicApi;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.GlobalParams;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;

public class DownloadSelectActivity extends BaseActivity {

	private GridView gridView;
	private ChapterAdapter adapter;
	private List<Chapter> list;
	private Activity activity;
	private Comic comic;
	private TextView selectAllTextView;

	private boolean isSelectAll = false;

	private Map<String, Chapter> map = new HashMap<String, Chapter>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		comic = (Comic) getIntent().getExtras().getSerializable("comic");
		activity = this;
		setContentView(R.layout.activity_downloadselect);
	
		setBack();

		gridView = (GridView) findViewById(R.id.chapter_gridView);
		list = new ArrayList<Chapter>();

		OnChapterAll onChapterAll = new OnChapterAll() {

			@Override
			public void getAll(int position) {
				// TODO Auto-generated method stub
				checkSelect();
			}
		};

		adapter = new ChapterAdapter(activity, list, onChapterAll, true);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		if (comic != null) {
			TextView titleTextView = (TextView) findViewById(R.id.title_textView);
			titleTextView.setText(comic.getComic_title());
			setData(true);
		}

		setSelectAll();

		setDownload();
	}

	private void getDownloadData(final String downloadChapters) {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				downloadChapter(map.get(downloadChapters));
				map.remove(downloadChapters);
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				ComicApi comicApi = new ComicApi();
				Chapter chapter;
				try {
					chapter = comicApi
							.getComicDownloadChapterContent(downloadChapters, GlobalParams.onlineFromMenuIdString);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					chapter = new Chapter();
					e.printStackTrace();
				}
				map.put(downloadChapters, chapter);
				return null;
			}
		}.execute("");
	}

	private void setBack() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setDownload() {
		TextView downloadTextView = (TextView) findViewById(R.id.download_textView);
		downloadTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (Chapter chapter : list) {
					if (chapter.isSelect()) {
						// downloadChapter(chapter);
						getDownloadData(chapter.getChapter_id());
					}
				}

				finish();
			}
		});
	}

	private void downloadChapter(Chapter chapter) {

		DownloadManager downloadManager = DownloadService
				.getDownloadManager(getApplicationContext());

		File file = new File(GlobalParams.downloadPathString + "/"
				+ comic.getComic_id() + "/" + GlobalParams.onlineFromNameIdString + "/" + chapter.getChapter_id());
		if (file.exists()) {
			Toast.makeText(getApplicationContext(), "已下载", 1).show();
			return;
		} else {
			DownloadInfo downloadInfo = null;

			for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
				DownloadInfo aDownloadInfo = downloadManager.getDownloadInfo(j);
				if (aDownloadInfo.getApp_idString() != null
						&& aDownloadInfo.getApp_idString().equals(
								comic.getComic_id())) {
					downloadInfo = downloadManager.getDownloadInfo(j);
					if (downloadInfo.getChapter_id().equals(
							chapter.getChapter_id())) {
						Toast.makeText(getApplicationContext(), "已下载", 1)
								.show();
						return;
					}
				}
			}
		}

		for (int i = 0; i < chapter.getComicPics().size(); i++) {
			String url = chapter.getComicPics().get(i).getUrl();
			try {
				url = URLEncoder.encode(url,"utf-8").replaceAll("\\+", "%20");
				url = url.replaceAll("%3A", ":").replaceAll("%2F", "/");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int index = url.lastIndexOf("/");
			if (index > 0) {
				String fileName = url.substring(index + 1);
				try {
					downloadManager.addNewDownload(
							url,
							chapter.getChapter_title(),
							GlobalParams.downloadPathString + "/"
									+ comic.getComic_id() + "/" + GlobalParams.onlineFromNameIdString + "/"
									+ chapter.getChapter_id() + "/" + fileName,
							true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
							false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
							null, comic.getComic_id(),
							comic.getComic_titlepic(), comic.getComic_title(),
							chapter.getChapter_id(),
							chapter.getChapter_title(),
							chapter.getChapter_size(), i + "");
				} catch (DbException e) {
					LogUtils.e(e.getMessage(), e);
					Toast.makeText(getApplicationContext(), "下载失败", 1).show();
				}
			}
		}

	}

	private void setSelectAll() {
		selectAllTextView = (TextView) findViewById(R.id.selectAll_textView);
		selectAllTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSelectAll) {
					selectAllTextView.setText("全选");
				} else {
					selectAllTextView.setText("取消全选");
				}

				isSelectAll = !isSelectAll;
				for (Chapter chapter : list) {
					chapter.setSelect(isSelectAll);
				}
				adapter.notifyDataSetChanged();

			}
		});
	}

	private void checkSelect() {
		int selectNum = 0;
		for (Chapter chapter : list) {
			if (chapter.isSelect()) {
				selectNum++;
			}
		}
		if (selectNum == list.size()) {
			isSelectAll = true;
			selectAllTextView.setText("取消全选");
		} else {
			isSelectAll = false;
			selectAllTextView.setText("全选");
		}
	}

	private void setData(boolean isAll) {
		list.clear();
		List<Chapter> getChapters = comic.getChapters();

		if (getChapters != null) {
			list.addAll(getChapters);
		}

		adapter.notifyDataSetChanged();
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
