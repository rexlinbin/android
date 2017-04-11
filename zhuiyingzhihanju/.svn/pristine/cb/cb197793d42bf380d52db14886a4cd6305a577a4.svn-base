package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.RecommendAdapter;
import com.bccv.zhuiyingzhihanju.adapter.SpecialAdapter;
import com.bccv.zhuiyingzhihanju.api.SpecialApi;
import com.bccv.zhuiyingzhihanju.fragment.SpecialFragment;
import com.bccv.zhuiyingzhihanju.model.Special;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;

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

public class RecommendedActivity extends BaseActivity{
	private ListView list;
	private List<Special> data;
	private List<Special> SList;

	private RecommendAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recommende);

		int dw = getWindowManager().getDefaultDisplay().getWidth();
		
		setTop();
	
		data = new ArrayList<Special>();

		list = (ListView) findViewById(R.id.special_listView);

		adapter = new RecommendAdapter(data, this);
		adapter.setDw(dw);
		list.setAdapter(adapter);

		list.setSelector(new ColorDrawable(android.R.color.transparent));

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				Intent aIntent = new Intent(RecommendedActivity.this, TypeInfoActivity.class);

				aIntent.putExtra("Special", data.get(position));
				startActivity(aIntent);

			}
		});

	}

	private void setTop() {
		
		
		ImageButton collectBtn = (ImageButton) findViewById(R.id.title_search_edit);

		ImageButton DownBtn = (ImageButton) findViewById(R.id.title_search_btnDown);

		DownBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(DownloadActivity.class);
				} else {

					Bundle bundle = new Bundle();
					bundle.putString("type", "D");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}
			}
		});
		collectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (GlobalParams.hasLogin) {
					startActivityWithSlideAnimation(CollectActivity.class);
				} else {

					Bundle bundle = new Bundle();
					bundle.putString("type", "C");
					startActivityForResultSlideAnimation(LoginActivity.class, bundle);
				}
			}
		});
		ImageButton hisBtn=(ImageButton) findViewById(R.id.title_search_btnhis);
			hisBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					startActivityWithSlideAnimation(HistoryActivity.class);

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

				SList = api.getThemList("1", "10");

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
