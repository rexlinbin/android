package com.bccv.zhuiying.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.activity.CollectActivity;
import com.bccv.zhuiying.activity.HistoryActivity;
import com.bccv.zhuiying.activity.MovieSearchActivity;
import com.bccv.zhuiying.activity.TypeInfoActivity;
import com.bccv.zhuiying.adapter.SpecialAdapter;
import com.bccv.zhuiying.api.SpecialApi;
import com.bccv.zhuiying.model.Special;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

public class SpecialFragment extends BaseActivity {
	private ListView list;
	private List<Special> data;
	private List<Special> SList;

	private SpecialAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_special);

		int dw = getWindowManager().getDefaultDisplay().getWidth();
		
		
		
		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setVisibility(View.INVISIBLE);
		ImageView logoIm = (ImageView) findViewById(R.id.titel_logo);
		logoIm.setVisibility(View.VISIBLE);

		ImageButton searChBtn = (ImageButton) findViewById(R.id.titel_search);
		searChBtn.setVisibility(View.VISIBLE);
		searChBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent aIntent = new Intent(SpecialFragment.this, MovieSearchActivity.class);

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
		
		data = new ArrayList<Special>();

		list = (ListView) findViewById(R.id.special_listView);

		adapter = new SpecialAdapter(data, this);
		adapter.setDw(dw);
		list.setAdapter(adapter);

		list.setSelector(new ColorDrawable(android.R.color.transparent));

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				Intent aIntent = new Intent(SpecialFragment.this, TypeInfoActivity.class);

				aIntent.putExtra("Special", data.get(position));
				startActivity(aIntent);

			}
		});

	}

	public void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (SList != null) {

					data.clear();

					data.addAll(SList);
					adapter.notifyDataSetChanged();

				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {

				SpecialApi api = new SpecialApi();

				SList = api.getThemList();

				return "true";
			}
		}.execute("");
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
		TCAgent.onPageStart(getApplicationContext(), "SpecialFragment");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TCAgent.onPageEnd(getApplicationContext(), "SpecialFragment");
	}
}
