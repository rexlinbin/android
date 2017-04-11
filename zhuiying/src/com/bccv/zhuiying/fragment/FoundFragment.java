package com.bccv.zhuiying.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.activity.CollectActivity;
import com.bccv.zhuiying.activity.HistoryActivity;
import com.bccv.zhuiying.activity.MovieListActivity;
import com.bccv.zhuiying.activity.MovieSearchActivity;
import com.bccv.zhuiying.activity.TVListActivity;
import com.bccv.zhuiying.adapter.FoundAdapter;
import com.bccv.zhuiying.api.FoundApi;
import com.bccv.zhuiying.model.Found;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class FoundFragment extends BaseActivity{

	private List<Found>data;
	private List<Found>list;
	private GridView gridView;
	private FoundAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_found);
		ImageButton backBtn=(ImageButton) findViewById(R.id.titel_back);
		backBtn.setVisibility(View.INVISIBLE);
		ImageView logoIm=(ImageView) findViewById(R.id.titel_logo);
		logoIm.setVisibility(View.VISIBLE);
		
		ImageButton searChBtn=(ImageButton) findViewById(R.id.titel_search);
		searChBtn.setVisibility(View.VISIBLE);
		searChBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent aIntent=new Intent(FoundFragment.this,MovieSearchActivity.class);
				
				startActivity(aIntent);
				
			}
		});
		ImageButton colletChBtn=(ImageButton) findViewById(R.id.titel_collect);
		colletChBtn.setVisibility(View.VISIBLE);
		
		
		
		colletChBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(CollectActivity.class);
			}
		});
		
		
		ImageButton hisBtn=(ImageButton) findViewById(R.id.titel_history);
		hisBtn.setVisibility(View.VISIBLE);
		
		
		
		hisBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityWithSlideAnimation(HistoryActivity.class);
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		gridView=(GridView) findViewById(R.id.found_grid);
		
		data=new ArrayList<Found>();
		adapter=new FoundAdapter(data, this);
		
		
		gridView.setAdapter(adapter);
		
		
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (data.get(position).getId().equals("12")) {
					Intent aIntent=new Intent(FoundFragment.this,TVListActivity.class);
					startActivity(aIntent);
				}else{
					Intent aIntent=new Intent(FoundFragment.this,MovieListActivity.class);
					
					
					
					aIntent.putExtra("type_id", data.get(position).getId());
					
					aIntent.putExtra("title", data.get(position).getTitle());
					
					startActivity(aIntent);
				}
				
				
			}
		});
		
		getData();
	}
	
	

	public void getData() {
	
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if(data!=null){
					data.clear();
					
				}
				if(list!=null){
					data.addAll(list);
					adapter.notifyDataSetChanged();
				}
		
				
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
			
				
				
				FoundApi api=new FoundApi();
				
				list=api.getTypeList();
				
				
				return "true";
			}
		}.execute("");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TCAgent.onPageStart(getApplicationContext(), "FoundFragment");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TCAgent.onPageEnd(getApplicationContext(), "FoundFragment");
	}
	
	

}
