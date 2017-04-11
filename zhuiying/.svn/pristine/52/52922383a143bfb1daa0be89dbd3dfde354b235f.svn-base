package com.bccv.zhuiying.activity;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.adapter.TypeInfoListAdapter;
import com.bccv.zhuiying.api.MovieListApi;
import com.bccv.zhuiying.model.Movie;
import com.bccv.zhuiying.model.Special;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.views.MyGridView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TypeInfoActivity extends BaseActivity {
	private MyGridView gridView;
	
	private List<Movie> list, getList;
	private TypeInfoListAdapter adapter;
	
	private Special special;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TCAgent.onPageStart(getApplicationContext(), "TypeInfoActivity");
		setContentView(R.layout.activity_typeinfo);
		special = (Special) getIntent().getSerializableExtra("Special");
		setTitle();
		initView();
		setView();
		getData(true);
	}
	
	private void setTitle() {
		ImageButton back = (ImageButton) findViewById(R.id.titel_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		ImageButton search = (ImageButton) findViewById(R.id.titel_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private void initView(){
		gridView = (MyGridView) findViewById(R.id.gridView);
		gridView.setNumColumns(3);
		list = new ArrayList<Movie>();
		adapter = new TypeInfoListAdapter(getApplicationContext(), list, special);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent aIntent = new Intent(getApplicationContext(), MovieInfoActivity.class);

				aIntent.putExtra("movie", list.get(position));

				startActivity(aIntent);
			}
		});
		
	}
	
	private int page = 1, count = 1000;
	private void getData(final boolean isRefresh) {
		if (isRefresh) {
			page = 1;
		}
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					if (isRefresh) {
						list.clear();
					}
					list.addAll(getList);
					adapter.notifyDataSetChanged();
				}
				
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				MovieListApi movieListApi = new MovieListApi();
				getList = movieListApi.getTypeList(special.getId(), page + "", count + "");
				page++;
				return null;
			}
		}.execute("");
	}
	
	private void setView(){

		ImageView headImageView = (ImageView) findViewById(R.id.type_imageView);
		TextView titleTextView = (TextView) findViewById(R.id.title_textView);
		TextView numTextView = (TextView) findViewById(R.id.num_textView);
		TextView hitTextView = (TextView) findViewById(R.id.hit_textView);
		TextView infoTextView = (TextView) findViewById(R.id.info_textView);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(special.getImages(), headImageView, GlobalParams.foundOptions);
		
		titleTextView.setText(special.getName());
		infoTextView.setText(special.getIntro());
		numTextView.setText(special.getMovie_num()+"部");
		hitTextView.setText(special.getHit()+"次");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "TypeInfoActivity");
	}
}
