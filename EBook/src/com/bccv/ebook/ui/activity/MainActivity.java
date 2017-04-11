package com.bccv.ebook.ui.activity;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bccv.ebook.ApplicationManager;
import com.bccv.ebook.api.NetWorkAPI;
import com.bccv.ebook.model.BookInfo;
import com.bccv.ebook.model.Bookshelf;
import com.bccv.ebook.model.VersionInfo;
import com.bccv.ebook.network.HttpCallback;
import com.bccv.ebook.network.NetResBean;
import com.bccv.ebook.ui.adapter.MyListViewAdapter;
import com.bccv.ebook.ui.adapter.MyListViewAdapter.MyOnItemClickListener;
import com.bccv.ebook.utils.EBookUtil;
import com.bccv.ebook.utils.ExitUtils;
import com.bccv.ebook.utils.FileUtils;
import com.bccv.ebook.utils.L;
import com.bccv.ebook.utils.SystemUtil;
import com.bccv.ebook.utils.ZipUtils;
import com.boxuu.ebookjy.R;

public class MainActivity extends BaseActivity {

//	private Button open_bt;
	private ImageView iv_all_app;//跳转到webview页面的按钮
	private ListView lv;//gridview
	private RelativeLayout waitting;
	private MyListViewAdapter myAdapter ;//gridview 的适配器
	private Bookshelf bookshelf;
	private ArrayList<BookInfo> bookInfos;
	
	private static final int GET_BOOK_INFO_SUCCESS = 1;
	private static final int CANCLE_WAITTING = 2;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_BOOK_INFO_SUCCESS:
				Intent intent = new Intent(MainActivity.this, PageActivity.class);
				intent.putExtra(PageActivity.BOOK_INFO_KEY, (BookInfo)msg.obj);
				startActivity(intent);
				mHandler.sendEmptyMessageDelayed(CANCLE_WAITTING, 500);
				break;
			case CANCLE_WAITTING:
				waitting.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		report();
		initData();
		initView();
	}

	private void initView() {
		iv_all_app = (ImageView) findViewById(R.id.iv_allApp);
		lv = (ListView) findViewById(R.id.lv_book);
		waitting = (RelativeLayout) findViewById(R.id.waitting);
		myAdapter = new MyListViewAdapter(this,bookInfos);
		myAdapter.setMyListener(new MyOnItemClickListener() {
			@Override
			public void OnItemClick(final int position) {
				waitting.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						BookInfo openBookInfo = getOpenBookInfo(position);
						Message msg = new Message();
						msg.obj = openBookInfo;
						msg.what = GET_BOOK_INFO_SUCCESS;
						mHandler.sendMessage(msg);
					}
				}).start();
				
			}
		});
		lv.setAdapter(myAdapter);
		iv_all_app.setOnClickListener(this);
		waitting.setOnClickListener(this);
//		open_bt = (Button) findViewById(R.id.open_bt);
//		open_bt.setOnClickListener(this);
	}
	
	private void initData(){
		bookshelf = Bookshelf.parser(mContext);
		bookInfos = bookshelf.getBookInfos();
	}
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
//		case R.id.open_bt:
//			
//			BookInfo openBookInfo = getOpenBookInfo(0);
//			Intent intent = new Intent(this, PageActivity.class);
//			intent.putExtra(PageActivity.BOOK_INFO_KEY, openBookInfo);
//			startActivity(intent);
			
			
//			final String path = Environment.getExternalStorageDirectory()
//					+ "/test.txt";
//			File file = new File(path);
//			if (file.isFile() && file.exists()) {
//				// TODO 打开图书
//				Intent intent = new Intent(this, PageActivity.class);
//				
//				BookInfo bookInfo = new BookInfo();
//				bookInfo.setName("啦啦啦");
//				bookInfo.setId(1);
//				ArrayList<ChapterInfo> chapterInfos = new ArrayList<ChapterInfo>();
//				for (int i = 0; i < 5; i++) {
//					ChapterInfo chapterInfo = new ChapterInfo();
//					chapterInfo.setFileName("test.txt");
//					chapterInfo.setPath(path);
//					chapterInfo.setId(i+1);
//					chapterInfo.setIndex(i);
//					chapterInfo.setName("第"+(i+1)+"章");
//					chapterInfos.add(chapterInfo);
//				}
//				bookInfo.setChapterInfos(chapterInfos);
//				
//				intent.putExtra(PageActivity.BOOK_INFO_KEY, openBookInfo);
//				startActivity(intent); 
//			} else {
//				showLongToast("图书不存在");
//			}
//			break;
		case R.id.iv_allApp:
			//TODO 跳转到web view的页面
			Intent in = new Intent(this,WebViewActivity.class);
//			in.putExtra("curOrder", 5);
			startActivity(in);
			break;
		default:
			break;
		}

	}
	
	/**
	 * 获取指定下标的信息
	 * 
	 * @param position
	 * @return
	 */
	private synchronized BookInfo getOpenBookInfo(int position){
		
		BookInfo bookInfo = null;
		try{
			bookInfo = bookInfos.get(position);
			if(bookInfo.getChapterInfos()==null||bookInfo.getChapterInfos().size()==0){
				bookInfo.parser(mContext, bookInfo.getAccount(), bookInfo.getId());
			}
			
			String cachePath = SystemUtil.getEBookCacheFolder(SystemUtil.UNZIP_BOOK_TYPE)
					+File.separator+bookInfo.getId();
			int fileCount = FileUtils.getFileCount(cachePath);
			if(fileCount!=bookInfo.getChapterInfos().size()){
				
				//TODO 解压
				FileUtils.deleteDirectory(cachePath);
				ZipUtils.unZip(mContext, bookInfo.getZippath(), SystemUtil.getEBookCacheFolder(SystemUtil.UNZIP_BOOK_TYPE));
			}
		}catch(Exception e){
			L.e(TAG, "getOpenBookInfo", e.getMessage());
			showShortToast("解压出错");
		}
		return bookInfo;
	}
	
	private void report(){
		NetWorkAPI.report(mContext, new HttpCallback() {
			
			@Override
			public void onResult(NetResBean response) {
				L.v(TAG, " report onResult ", response.success);
				if (response.success && response instanceof VersionInfo) {
					VersionInfo dataBean = (VersionInfo)response;
					L.v(TAG, " dataBean ", dataBean.toString());
					EBookUtil.showUpdateDialog(dataBean, MainActivity.this, false);
				}
			}
			
			@Override
			public void onError(String errorMsg) {
				L.v(TAG, " report onError ", errorMsg+"");
			}
			
			@Override
			public void onCancel() {}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (ExitUtils.isExit(this)) {
			super.onBackPressed();
			ApplicationManager.getInstance().exitSystem();
		}
	}
	
}
