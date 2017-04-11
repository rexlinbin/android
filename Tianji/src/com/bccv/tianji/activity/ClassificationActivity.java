package com.bccv.tianji.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.tianji.R;
import com.bccv.tianji.adapter.ClassificationAdapter;
import com.bccv.tianji.api.ClassificationApi;
import com.bccv.tianji.model.Classification;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;

public class ClassificationActivity extends BaseActivity {
	private GridView gridView;
	private List<Classification> list, getList;
	private ClassificationAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classification);
		setBack();
		setView();
		getData();
	}
	
	private ImageView backTextView;

	private void setBack() {
		backTextView = (ImageView) findViewById(R.id.back_textView);
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();		
			}
		});
	}
	
	private void setView(){
		gridView = (GridView) findViewById(R.id.gridView);
		list = new ArrayList<Classification>();
		adapter = new ClassificationAdapter(getApplicationContext(), list);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), ClassifyListActivity.class);
				intent.putExtra("classify_id", list.get(position).getType_id());
				intent.putExtra("classify_name", list.get(position).getTitle());
				startActivity(intent);
			}
		});
	}
	
	private void getData(){
		Callback callback = new Callback() {
			
			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					list.addAll(getList);
					adapter.notifyDataSetChanged();
				}
			}
		};
		
		new DataAsyncTask(callback, true) {
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				ClassificationApi classificationApi = new ClassificationApi();
				getList = classificationApi.getClassificationList();
				return null;
			}
		}.execute("");
	}
}
