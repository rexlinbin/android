package com.bccv.threedimensionalworld.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.FileChooserAdapter;
import com.bccv.threedimensionalworld.adapter.FileChooserAdapter.FileInfo;
import com.bccv.threedimensionalworld.adapter.ImageGridViewAdapter;
import com.bccv.threedimensionalworld.model.MusicBean;
import com.bccv.threedimensionalworld.model.PictureBean;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.FileUtils;

public class UsbActivity extends BaseActivity {
	private static ArrayList<File> txtList = null;
	private ListView gridView, gridView1;

	private FileChooserAdapter adapter;
	private List<PictureBean> data;
	ArrayList<FileInfo> mFileLists;
	private List<MusicBean> data1;
	File path;
//	private String mSdcardRootPath; // sdcard 根路径
	private String mLastFilePath; // 当前显示的路径
private String usbPath;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usb);
		gridView = (ListView) findViewById(R.id.image_gridView);
		gridView1 = (ListView) findViewById(R.id.image_gridView1);
		mFileLists = new ArrayList<FileInfo>();
		data = new ArrayList<PictureBean>();
		data1 = new ArrayList<MusicBean>();
		TextView text=(TextView) findViewById(R.id.tvEmptyHint);
		TextView text1=(TextView) findViewById(R.id.tvEmptyHint1);
		text.setScaleX(0.5f);
		text1.setScaleX(0.5f);
		gridView.setEmptyView(text);
		gridView1.setEmptyView(text1);
		gridView.setOnItemClickListener(new ItemClicklistener());

		gridView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				gridView1.onTouchEvent(event);
				return false;
			}
		});

		gridView.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub

				gridView1.onKeyDown(arg1, arg2);
				return false;
			}
		});

		gridView1.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub

				gridView.onKeyDown(arg1, arg2);
				return false;
			}
		});
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));

		gridView1.setSelector(new ColorDrawable(android.R.color.transparent));

		gridView.setOnGenericMotionListener(new OnGenericMotionListener() {

			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				gridView1.onGenericMotionEvent(arg1);
				return false;
			}
		});
//		mSdcardRootPath = Environment.getExternalStorageDirectory()
//				.getAbsolutePath();
usbPath=getIntent().getStringExtra("usbPath");
		setData(usbPath);

	}

	private void setData(final String filePath) {
		// TODO Auto-generated method stub

		adapter = new FileChooserAdapter(this, mFileLists);

		gridView.setAdapter(adapter);
		gridView1.setAdapter(adapter);

		// TODO Auto-generated method stub
		mLastFilePath = filePath;

		if (mFileLists == null)
			mFileLists = new ArrayList<FileInfo>();
		if (!mFileLists.isEmpty())
			mFileLists.clear();

		File[] files = folderScan(filePath);
		if (files == null)
			return;

		for (int i = 0; i < files.length; i++) {
			if (files[i].isHidden()) // 不显示隐藏文件
				continue;

			String fileAbsolutePath = files[i].getAbsolutePath();
			String fileName = files[i].getName();
			boolean isDirectory = false;
			if (files[i].isDirectory()) {
				isDirectory = true;
			}
			FileInfo fileInfo = new FileInfo(fileAbsolutePath, fileName,
					isDirectory);
			mFileLists.add(fileInfo);
		}
		// When first enter , the object of mAdatper don't initialized
		if (adapter != null)
			adapter.notifyDataSetChanged(); // 重新刷新

	}

	// 获得当前路径的所有文件
	private File[] folderScan(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		return files;
	}

	class ItemClicklistener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

			FileInfo fileInfo = (FileInfo) (((FileChooserAdapter) arg0
					.getAdapter()).getItem(arg2));
			if (fileInfo.isDirectory()) // 点击项为文件夹, 显示该文件夹下所有文件
				setData(fileInfo.getFilePath());
			else if (fileInfo.isMovieFile()) { // 是视频文件 ， 则将该路径通知给调用者
				Intent intent = new Intent(UsbActivity.this,
						LocalVideoPlayerActivity.class);
				intent.putExtra("url", fileInfo.getFilePath());
				intent.putExtra("title", fileInfo.getFileName());
				startActivity(intent);
				finish();
			} else if (fileInfo.isMusicFile()) { // 是音乐文件 ， 则将该路径通知给调用者
				Intent aIntent = new Intent(UsbActivity.this,
						MusicActivity.class);

				MusicBean music = new MusicBean();
				music.setName(fileInfo.getFileName());
				music.setPlay_url(fileInfo.getFilePath());

				data1.add(music);

				aIntent.putExtra("isLocal", true);
				aIntent.putExtra("name", fileInfo.getFileName());
				aIntent.putExtra("url", fileInfo.getFilePath());
				aIntent.putExtra("author", "无");

				aIntent.putExtra("list", (Serializable) data1);
				aIntent.putExtra("position", 0);

				startActivity(aIntent);
				finish();
			} else if (fileInfo.isPicile()) { // 是图片文件 ， 则将该路径通知给调用者

				PictureBean pic = new PictureBean();

				pic.setTitle(fileInfo.getFileName());
				pic.setPicPath(fileInfo.getFilePath());
				data.add(pic);

				Intent intent = new Intent(UsbActivity.this,
						GalleryActivity.class);

				intent.putExtra("list", (Serializable) data);
				intent.putExtra("position", arg2);
				startActivity(intent);
				finish();
			} else { // 其他文件.....
				Toast.makeText(getApplication(), "不能识别此文件", Toast.LENGTH_SHORT)
						.show();
				
			}

		}

	}

	public void onBackClick(View view) {

		backProcess();

	}
	public boolean onKeyDown(int keyCode , KeyEvent event){
		if(  event.getKeyCode()==KeyEvent.KEYCODE_BUTTON_Y|| event.getKeyCode()==KeyEvent.KEYCODE_BUTTON_B){
			backProcess();   
			return true ;
		}
		return super.onKeyDown(keyCode, event);
	}
	// 返回上一层目录的操作
	public void backProcess() {
		// 判断当前路径是不是sdcard路径 ， 如果不是，则返回到上一层。
		if (!mLastFilePath.equals(usbPath)) {
			File thisFile = new File(mLastFilePath);
			String parentFilePath = thisFile.getParent();
			setData(parentFilePath);
		} else { // 是sdcard路径 ，直接结束
			setResult(RESULT_CANCELED);
			finish();
		}
	}

}
