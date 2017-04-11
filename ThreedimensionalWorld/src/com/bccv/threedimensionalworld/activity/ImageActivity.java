package com.bccv.threedimensionalworld.activity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.ImageGridViewAdapter;
import com.bccv.threedimensionalworld.model.PictureBean;
import com.bccv.threedimensionalworld.tool.BaseActivity;
import com.bccv.threedimensionalworld.tool.Callback;
import com.bccv.threedimensionalworld.tool.FileUtils;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.MyComparator;

public class ImageActivity extends BaseActivity {

	private GridView  gridView,gridView1;

	private ImageGridViewAdapter adapter;
	private List<PictureBean> data;
	File path;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_image);

		gridView = (GridView ) findViewById(R.id.image_gridView);
		gridView1 = (GridView ) findViewById(R.id.image_gridView1);
		
		
		data = new ArrayList<PictureBean>();

		adapter = new ImageGridViewAdapter(this, data);


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
		
		gridView1.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				
				gridView.onKeyDown(arg1, arg2);
				return false;
			}
		});
		gridView
		.setSelector(new ColorDrawable(android.R.color.transparent));
		
		gridView1
		.setSelector(new ColorDrawable(android.R.color.transparent));
		

		gridView.setOnGenericMotionListener(new OnGenericMotionListener() {
			
			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				gridView1.onGenericMotionEvent(arg1);
				return false;
			}
		});
		
		
		setData();
	}

	

	public void setData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				adapter.notifyDataSetChanged();

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub

				String localPathString =  GlobalParams.ImagePathString + "/";

				List<File> files = FileUtils.listPathFiles(localPathString);

				MyComparator myComparator = new MyComparator();
				Collections.sort(files, myComparator);

				for (int i = 0; i < files.size(); i++) {
					File file = files.get(i);
					PictureBean pic = new PictureBean();

					pic.setTitle(file.getName());
					pic.setPicPath(localPathString + file.getName());
					data.add(pic);
				}

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
			Intent aIntent = new Intent(ImageActivity.this,
					GalleryActivity.class);

			aIntent.putExtra("list", (Serializable) data);
			aIntent.putExtra("position", arg2);
			startActivity(aIntent);
			
		}

	}

}
