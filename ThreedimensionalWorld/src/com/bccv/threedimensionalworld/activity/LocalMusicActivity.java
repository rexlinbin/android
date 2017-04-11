package com.bccv.threedimensionalworld.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.LocalMusicAdapter;
import com.bccv.threedimensionalworld.model.MusicBean;
import com.bccv.threedimensionalworld.tool.BaseActivity;

@SuppressLint("NewApi") public class LocalMusicActivity extends BaseActivity {

	private List<MusicBean> data;
	private LocalMusicAdapter adapter;
	private ListView list,list1;
private Button BackBtn,BackBtn1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_localmusic);

		data = new ArrayList<MusicBean>();
		BackBtn=(Button) findViewById(R.id.localmusic_back_button);
		BackBtn1=(Button) findViewById(R.id.localmusic_back_button1);
		list = (ListView) findViewById(R.id.localmusic_listView);
		list1 = (ListView) findViewById(R.id.localmusic_listView1);
		data=getMp3Infos(this);
		adapter = new LocalMusicAdapter(data, this);

		list.setAdapter(adapter);
	
		list.setOnItemClickListener(new ItemOnclickLisener());
		
		list1.setAdapter(adapter);

//		list1.setOnItemClickListener(new ItemOnclickLisener());
		
		BackBtn.setOnFocusChangeListener(new OnBackChangeListener());

		BackBtn1.setOnFocusChangeListener(new OnBackChangeListener());
		list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				list1.onTouchEvent(event);
				return false;
			}
		});
//		list1.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				list.onTouchEvent(event);
//				return false;
//			}
//		});
//		
		
		list.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				
				
				list1.onKeyDown(keyCode, event);
				
				
				
				
				
				
				return false;
			}
		});
		

		list.setOnGenericMotionListener(new OnGenericMotionListener() {
			
			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				list1.onGenericMotionEvent(arg1);
				return false;
			}
		});
		
		

		list1.setOnGenericMotionListener(new OnGenericMotionListener() {
			
			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				list.onGenericMotionEvent(arg1);
				return false;
			}
		});
		
		
	
		list1
		.setSelector(new ColorDrawable(android.R.color.transparent));
		
		list
		.setSelector(new ColorDrawable(android.R.color.transparent));
	
	}

	public static List getMp3Infos(Context context) {
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List mp3Infos = new ArrayList();
		for (int i = 0; i < cursor.getCount(); i++) {
			MusicBean mp3Info = new MusicBean();
			cursor.moveToNext();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
			String title = cursor.getString((cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐

			if (isMusic != 0) { // 只把音乐添加到集合当中mp3Info.setId(id);
				mp3Info.setName(title);
				mp3Info.setAuthor(artist);
				mp3Info.setPlay_url(url);
				
				
				mp3Infos.add(mp3Info);
			}
		}
		return mp3Infos;
	}

	
	class ItemOnclickLisener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			adapter.setSelect(arg2);
			
			Intent aIntent =new Intent(LocalMusicActivity.this,MusicActivity.class);
			aIntent.putExtra("isLocal", true);
			aIntent.putExtra("name", data.get(arg2).getName());
			aIntent.putExtra("url", data.get(arg2).getPlay_url());
			aIntent.putExtra("author", data.get(arg2).getAuthor());
			
			aIntent.putExtra("list",(Serializable)data);
			aIntent.putExtra("position",arg2);
			
			
			startActivity(aIntent);
			
		
			
			
			
			
		}
		
	}
	
	
	
	public void onBackClick(View view){
		
		
		finish();
		
		
		
		
		
		
		
		
	}
	
	
	class OnBackChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				BackBtn.setSelected(true);

				BackBtn1.setSelected(true);

			} else {

				BackBtn.setSelected(false);

				BackBtn1.setSelected(false);

			}

		}

	}

		
	}
	
	
	
	
	
	
	
	

