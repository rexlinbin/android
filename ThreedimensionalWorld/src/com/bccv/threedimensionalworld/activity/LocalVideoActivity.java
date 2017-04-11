package com.bccv.threedimensionalworld.activity;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.ImageGridViewAdapter;
import com.bccv.threedimensionalworld.adapter.LocalVidoAdapter;
import com.bccv.threedimensionalworld.model.Movie;
import com.bccv.threedimensionalworld.model.MusicBean;
import com.bccv.threedimensionalworld.model.PictureBean;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.FileUtils;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.MyComparator;

public class LocalVideoActivity extends BaseActivity {

		private GridView  gridView,gridView1;

		private LocalVidoAdapter adapter;
		private List<Movie> data;
		File path;

		@Override
		protected void onCreate(Bundle arg0) {
			// TODO Auto-generated method stub
			super.onCreate(arg0);
			setContentView(R.layout.activity_image);

			gridView = (GridView ) findViewById(R.id.image_gridView);
			gridView1 = (GridView ) findViewById(R.id.image_gridView1);
			
			
			data = new ArrayList<Movie>();
//			data=getMp3Infos(LocalVideoActivity.this);
			adapter = new LocalVidoAdapter(this, data);


			gridView.setAdapter(adapter);
			gridView1.setAdapter(adapter);
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
			
			gridView
			.setSelector(new ColorDrawable(android.R.color.transparent));
			
			gridView1
			.setSelector(new ColorDrawable(android.R.color.transparent));
//
//			gridView.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//				@Override
//				public void onItemSelected(AdapterView<?> parent, View view,
//						int position, long id) {
//					// TODO Auto-generated method stub
//					adapter.setSelect(position);
//				
//					
//				}
//
//				@Override
//				public void onNothingSelected(AdapterView<?> parent) {
//					// TODO Auto-generated method stub
//
//				}
//			});
			
			setData();
		}


		public static List getMp3Infos(Context context) {
			
			 Cursor cursor=null;
//			 ContentResolver contentResolver = context.getContentResolver();
		        String[] projection = new String[]{MediaStore.Video.Media.TITLE};
		        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
		    	List mp3Infos = new ArrayList();
		    	
		        try {
		        
		        	cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 
	                        null, null,null, null);
		        cursor.moveToFirst();
		        int fileNum = cursor.getCount();
		    
		        for(int counter = 0; counter < fileNum; counter++){        
		    
		       	Movie movie=new Movie();
		        String title= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
		        String data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
		        String url= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
		       
		    	
		        movie.setTitle(title);
				
				movie.setUrl(url);

				Bitmap bitmap = createVideoThumbnail(context,
						Uri.parse(url));
				movie.setBit(bitmap);
				mp3Infos.add(movie);
		        cursor.moveToNext();
		       
		      
		        }
		       
		        }catch (Exception e) {
					// TODO: handle exception
		        	e.printStackTrace();
				} finally {
		            if (cursor != null) {
		                cursor.close();
		            }
		        }
		
			return mp3Infos;
		}

		public void setData() {

			Callback callback = new Callback() {

				@Override
				public void handleResult(String result) {
					// TODO Auto-generated method stub
					adapter = new LocalVidoAdapter(LocalVideoActivity.this, data);


					gridView.setAdapter(adapter);
					gridView1.setAdapter(adapter);
					adapter.notifyDataSetChanged();

				}
			};

			new DataAsyncTask(callback, false) {

				@Override
				protected String doInBackground(String... params) {
					// TODO Auto-generated method stub

				
					data=getMp3Infos(LocalVideoActivity.this);
					
					
					return "true";
				}
			}.execute("");

		}

		class ItemClicklistener implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter.setSelect(arg2);
				Intent aIntent = new Intent(LocalVideoActivity.this,
						LocalVideoPlayerActivity.class);

				aIntent.putExtra("url",data.get(arg2).getUrl());
				aIntent.putExtra("title", data.get(arg2).getTitle());
				startActivity(aIntent);
			
			}

		}
		public static Bitmap createVideoThumbnail(Context context, Uri uri) {
			Bitmap bitmap = null;
			String className = "android.media.MediaMetadataRetriever";
			Object objectMediaMetadataRetriever = null;
			Method release = null;
			try {
				objectMediaMetadataRetriever = Class.forName(className)
						.newInstance();
				Method setDataSourceMethod = Class.forName(className).getMethod(
						"setDataSource", Context.class, Uri.class);
				setDataSourceMethod.invoke(objectMediaMetadataRetriever, context,
						uri);
				Method getFrameAtTimeMethod = Class.forName(className).getMethod(
						"getFrameAtTime");
				bitmap = (Bitmap) getFrameAtTimeMethod
						.invoke(objectMediaMetadataRetriever);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (release != null) {
						release.invoke(objectMediaMetadataRetriever);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return bitmap;
		}

}
