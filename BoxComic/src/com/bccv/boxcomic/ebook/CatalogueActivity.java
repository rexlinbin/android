package com.bccv.boxcomic.ebook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.download.DownloadInfo;
import com.bccv.boxcomic.download.DownloadManager;
import com.bccv.boxcomic.download.DownloadService;

import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.HttpHandler.State;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

import android.graphics.drawable.Drawable.Callback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 目录页
 */
public class CatalogueActivity extends BaseActivity implements OnClickListener {

	private ImageView common_title_leftbtn;
	private ImageView common_title_rightbtn;
	private TextView tv_title;
	private ListView lv_catalogue;
	private CatalogueAdapter myAdapter;
	private int curOrder;
	private ArrayList<String> pseList = new ArrayList<String>();
	private ArrayList<String> reverseList = new ArrayList<String>();
	private boolean isPse = true;
	private BookInfo bookInfo;
	private String bookName = "金庸小说全集";
	private ArrayList<ChapterInfo> chapterInfos;

	private TextView downloadTextView;
	private ProgressBar downloadProgressBar;
	private boolean hasDownload;
	private boolean isDownloading = false;

	private Timer timer = new Timer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalogue);
		Logger.e("time", System.currentTimeMillis() + "");
		curOrder = getIntent().getIntExtra(EbookActivity.CURRENTPAGE_KEY, 1);
		Logger.e("time", System.currentTimeMillis() + "");
		bookInfo = (BookInfo) getIntent().getSerializableExtra(
				EbookActivity.BOOKINFO_KEY);
		Logger.e("time", System.currentTimeMillis() + "");
		initData();
		Logger.e("time", System.currentTimeMillis() + "");
		initList();
		Logger.e("time", System.currentTimeMillis() + "");
		initView();
		Logger.e("time", System.currentTimeMillis() + "");
		checkDownload();
		Logger.e("time", System.currentTimeMillis() + "");
		
	}

	private void initView() {
		common_title_leftbtn = (ImageView) findViewById(R.id.common_title_leftbtn);
		common_title_rightbtn = (ImageView) findViewById(R.id.common_title_rightbtn);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_catalogue = (ListView) findViewById(R.id.lv_catalogue);

		common_title_leftbtn.setImageResource(R.drawable.back);
		common_title_rightbtn.setImageResource(R.drawable.zhengxu);
		tv_title.setText(bookName);

		myAdapter = new CatalogueAdapter(this, curOrder);
		myAdapter.setBookInfo(bookInfo);
		isPse = true;
		myAdapter.setList(pseList, chapterInfos, isPse);
		lv_catalogue.setAdapter(myAdapter);

		common_title_leftbtn.setOnClickListener(this);
		common_title_rightbtn.setOnClickListener(this);
		int index = 0;
		for (ChapterInfo chapterInfo : chapterInfos) {
			if (chapterInfo.getId() == curOrder) {
				index = chapterInfos.indexOf(chapterInfo);
				break;
			}
		}
		lv_catalogue.setSelection(index);

		downloadTextView = (TextView) findViewById(R.id.download_textView);
		downloadTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isDownloading) {
					downloadTextView.setText("继续缓存");
					checkDownload();
					for (ChapterInfo chapterInfo : chapterInfos) {
						if (!chapterInfo.isDownload()) {
							pauseDownload(chapterInfo);
						}
						
					}
					isDownloading = false;
					return;
				}
				boolean hasDownload = false;
				for (ChapterInfo chapterInfo : chapterInfos) {
					if (!chapterInfo.isDownload()) {
						hasDownload = true;
						downloadChapter(chapterInfo);
						isDownloading = true;
					}
					
				}
				if (hasDownload) {
					downloadTextView.setText("缓存中。。。");
					startDownload();
				}else {
					downloadTextView.setText("已缓存");
					downloadTextView.setEnabled(false);
				}
				
			}
		});

		if (hasDownload) {
			downloadTextView.setText("缓存");
		}else {
			downloadTextView.setText("已缓存");
			downloadTextView.setEnabled(false);
		}
		
		downloadProgressBar = (ProgressBar) findViewById(R.id.download_progressBar);

	}

	//暂停下载
	private void pauseDownload(ChapterInfo chapterInfo){
		DownloadManager downloadManager = DownloadService
				.getDownloadManager(getApplicationContext());
		DownloadInfo downloadInfo = null;

		for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
			DownloadInfo aDownloadInfo = downloadManager
					.getDownloadInfo(j);
			if (aDownloadInfo.getApp_idString() != null
					&& aDownloadInfo.getApp_idString().equals(
							bookInfo.getBook_id() + "")) {
				downloadInfo = downloadManager.getDownloadInfo(j);
				if (downloadInfo.getChapter_id().equals(
						chapterInfo.getId())) {
					if (downloadInfo.getState() != State.SUCCESS) {
						try {
							downloadManager.stopDownload(downloadInfo);
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	// 检查章节下载状况
	private void checkDownload() {
		com.bccv.boxcomic.tool.Callback callback = new com.bccv.boxcomic.tool.Callback() {
			
			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				myAdapter.notifyDataSetChanged();
				
				if (hasDownload) {
					downloadTextView.setText("缓存");
				}else {
					downloadTextView.setText("已缓存");
					downloadTextView.setEnabled(false);
				}
				
				if (hasDownload) {
					startDownload();
				}
				
			}
		};
		
		new DataAsyncTask(callback, true) {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				for (ChapterInfo chapterInfo : chapterInfos) {
					String url = chapterInfo.getDownload();
					if (StringUtil.isEmpty(url)) {
						continue;
					}
					Uri uri = Uri.parse(url);
					
					String fileName = url.substring(url.lastIndexOf("/") + 1,
							url.lastIndexOf("txt") + 3);

					File file = new File(GlobalParams.ebookDownloadPathString + "/"
							+ bookInfo.getBook_id() + "/" + fileName);
					if (file.exists()) {
						chapterInfo.setDownload(true);
						DownloadManager downloadManager = DownloadService
								.getDownloadManager(getApplicationContext());
						DownloadInfo downloadInfo = null;

						for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
							DownloadInfo aDownloadInfo = downloadManager
									.getDownloadInfo(j);
							if (aDownloadInfo.getApp_idString() != null
									&& aDownloadInfo.getApp_idString().equals(
											bookInfo.getBook_id() + "")) {
								downloadInfo = downloadManager.getDownloadInfo(j);
								if (downloadInfo.getChapter_id().equals(
										chapterInfo.getId())) {
									if (downloadInfo.getState() != State.SUCCESS) {
										chapterInfo.setDownload(false);
									}
								}
							}
						}

					} else {
						chapterInfo.setDownload(false);
					}
					
					if (!chapterInfo.isDownload()) {
						hasDownload = true;
					}
				}
				return null;
			}
		};
		
		
	}

	// 更新进度条
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			DownloadManager downloadManager = DownloadService
					.getDownloadManager(getApplicationContext());
			String dictionaryPathString = GlobalParams.ebookDownloadPathString
					+ "/" + bookInfo.getBook_id();
			if (com.bccv.boxcomic.tool.FileUtils.checkDirPathExists(dictionaryPathString)) {
				List<File> list = com.bccv.boxcomic.tool.FileUtils
						.listPathFiles(dictionaryPathString);
				downloadProgressBar.setProgress(list.size() * 100
						/ chapterInfos.size());
				if (list.size() == chapterInfos.size() && downloadComplete()) {
					downloadTextView.setText("已缓存");
					downloadTextView.setEnabled(false);
					timer.cancel();
				}
			}else {
				if (FileUtils.createDirectory(dictionaryPathString)) {
					List<File> list = com.bccv.boxcomic.tool.FileUtils
							.listPathFiles(dictionaryPathString);
					downloadProgressBar.setProgress(list.size() * 100
							/ chapterInfos.size());
					if (list.size() == chapterInfos.size() && downloadComplete()) {
						downloadTextView.setText("已缓存");
						downloadTextView.setEnabled(false);
						timer.cancel();
					}
				}
			}

			super.handleMessage(msg);
		}
	};

	// 判断是否下载完成
	private boolean downloadComplete() {
		DownloadManager downloadManager = DownloadService
				.getDownloadManager(getApplicationContext());

		List<DownloadInfo> ebookDownloadInfos = new ArrayList<DownloadInfo>();

		for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
			DownloadInfo aDownloadInfo = downloadManager.getDownloadInfo(j);
			if (aDownloadInfo.getApp_idString() != null
					&& aDownloadInfo.getApp_idString().equals(
							bookInfo.getBook_id() + "")) {
				ebookDownloadInfos.add(aDownloadInfo);
			}
		}

		boolean isComplete = true;

		for (DownloadInfo downloadInfo : ebookDownloadInfos) {
			if (downloadInfo.getState() != State.SUCCESS) {
				isComplete = false;
				break;
			}
		}

		return isComplete;
	}

	// 开始下载
	private void startDownload() {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(0);
			}
		};
		timer.schedule(task, 0, 500);
	}

	private void initList() {
		for (int i = 1; i <= chapterInfos.size(); i++) {
			pseList.add("第" + i + "章");
		}
		for (int i = chapterInfos.size(); i > 0; i--) {
			reverseList.add("第" + i + "章");
		}
	}

	private void initData() {
		if (bookInfo != null) {
			bookName = bookInfo.getBook_title();
			chapterInfos = bookInfo.getChapterInfos();
		} else {
			showShortToast("获取章节失败");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_leftbtn:
			onBackPressed();
			break;
		case R.id.common_title_rightbtn:
			// 右边按钮->正序倒序
			if (isPse && chapterInfos != null) {
				isPse = false;
				common_title_rightbtn.setImageResource(R.drawable.daoxu);
				myAdapter.setList(reverseList, chapterInfos, isPse);
				myAdapter.notifyDataSetChanged();
			} else if (!isPse && chapterInfos != null) {
				isPse = true;
				common_title_rightbtn.setImageResource(R.drawable.zhengxu);
				myAdapter.setList(pseList, chapterInfos, isPse);
				myAdapter.notifyDataSetChanged();
			} else {
				showShortToast("获取章节失败");
			}
			break;
		}
	}

	// 下载章节
	private void downloadChapter(ChapterInfo chapter) {

		DownloadManager downloadManager = DownloadService
				.getDownloadManager(getApplicationContext());

		String url = chapter.getDownload();
		if (StringUtil.isEmpty(url)) {
			return;
		}
		
		Uri uri = Uri.parse(url);

		String fileName = url.substring(url.lastIndexOf("/") + 1,
				url.lastIndexOf("txt") + 3);

		File file = new File(GlobalParams.ebookDownloadPathString + "/"
				+ bookInfo.getBook_id() + "/" + fileName);
		if (file.exists()) {
			DownloadInfo downloadInfo = null;

			for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
				DownloadInfo aDownloadInfo = downloadManager.getDownloadInfo(j);
				if (aDownloadInfo.getApp_idString() != null
						&& aDownloadInfo.getApp_idString().equals(
								bookInfo.getBook_id() + "")) {
					downloadInfo = downloadManager.getDownloadInfo(j);
					if (downloadInfo.getChapter_id().equals(chapter.getId())) {
						if (downloadInfo.getState() != State.SUCCESS) {
							try {
								downloadManager.resumeDownload(downloadInfo, new RequestCallBack<File>() {
									
									@Override
									public void onSuccess(ResponseInfo<File> arg0) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onFailure(HttpException arg0, String arg1) {
										// TODO Auto-generated method stub
										Logger.e("aa", arg0.getMessage());
									}
								});
							} catch (DbException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							return;
						} else {
							Toast.makeText(getApplicationContext(), "已下载", 1)
									.show();
							return;
						}

					}
				}
			}
			Toast.makeText(getApplicationContext(), "已下载", 1).show();
			return;
		} else {
			DownloadInfo downloadInfo = null;

			for (int j = 0; j < downloadManager.getDownloadInfoListCount(); j++) {
				DownloadInfo aDownloadInfo = downloadManager.getDownloadInfo(j);
				if (aDownloadInfo.getApp_idString() != null
						&& aDownloadInfo.getApp_idString().equals(
								bookInfo.getBook_id() + "")) {
					downloadInfo = downloadManager.getDownloadInfo(j);
					if (downloadInfo.getChapter_id().equals(chapter.getId())) {
						try {
							downloadManager.resumeDownload(downloadInfo, new RequestCallBack<File>() {
								
								@Override
								public void onSuccess(ResponseInfo<File> arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onFailure(HttpException arg0, String arg1) {
									// TODO Auto-generated method stub
									Logger.e("aa", arg0.getMessage());
								}
							});
						} catch (DbException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Toast.makeText(getApplicationContext(), "已下载", 1)
								.show();
						return;
					}
				}
			}
		}

		try {
			downloadManager.addNewDownload(
					url,
					chapter.getTitle(),
					GlobalParams.ebookDownloadPathString + "/"
							+ bookInfo.getBook_id() + "/" + fileName,
					true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
					false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
					null, bookInfo.getBook_id() + "",
					"xs/" + bookInfo.getBook_titlepic(), bookInfo.getBook_title(),
					chapter.getId() + "", chapter.getTitle(), "", "0");
		} catch (DbException e) {
			LogUtils.e(e.getMessage(), e);
			Toast.makeText(getApplicationContext(), "下载失败", 1).show();
		}
	}

}
