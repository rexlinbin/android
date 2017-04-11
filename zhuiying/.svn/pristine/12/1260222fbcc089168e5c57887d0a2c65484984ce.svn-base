package com.bccv.zhuiying.activity;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.adapter.MovieListAdapter;
import com.bccv.zhuiying.api.FancyApi;
import com.bccv.zhuiying.model.Movie;
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
import android.widget.TextView;

public class MoreListActivity extends BaseActivity {

	private GridView gridView;
	private MovieListAdapter adapter;
	private List<Movie> data;
	private List<Movie> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_morelist);
		TCAgent.onPageStart(getApplicationContext(), "MoreListActivity");
		gridView = (GridView) findViewById(R.id.more_grid);

		data = new ArrayList<Movie>();

		adapter = new MovieListAdapter(this, data);
		gridView.setAdapter(adapter);

		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		TextView text = (TextView) findViewById(R.id.titleName_textView);
		text.setVisibility(View.VISIBLE);
		text.setText("大家都在看");
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
	
				
				Intent aIntent=new Intent(MoreListActivity.this,MovieInfoActivity.class);
				
				
				aIntent.putExtra("movie", data.get(position));
				
				
				
				startActivity(aIntent);
				
				
				
			}
		});

		getData();
	}

	public void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
			if (list != null) {
				data.addAll(list);

				adapter.notifyDataSetChanged();
			}
				

			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {

				FancyApi api = new FancyApi();

				list = api.getMoreList();

				return "true";
			}
		}.execute("");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "MoreListActivity");
	}
	
}
