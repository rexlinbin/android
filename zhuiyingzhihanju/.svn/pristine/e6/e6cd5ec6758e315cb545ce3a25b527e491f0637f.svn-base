package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.apptalkingdata.push.app.a;
import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.StarNewsAdapter;
import com.bccv.zhuiyingzhihanju.adapter.StarWorksAdapter;
import com.bccv.zhuiyingzhihanju.api.StarApi;
import com.bccv.zhuiyingzhihanju.model.SearchInfo;
import com.bccv.zhuiyingzhihanju.model.StarInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.views.CircleImageView;

import android.annotation.SuppressLint;
import android.app.Activity;
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

@SuppressLint("ClickableViewAccessibility")
public class StarInfoActivity extends BaseActivity {
	private String star_id;
	private StarInfo starInfo;
	private CircleImageView circleImageView;
	private TextView nameTextView, typeTextView, birthTextView, conTextView, bornTextView, worksTextView, newsTextView;
	private GridView worksGridView, newsGridView;
	private List<SearchInfo> works, news;
	private StarWorksAdapter worksAdapter;
	private StarNewsAdapter newsAdapter;
	private Activity activity;
	private void tcStart(){
		TCAgent.onPageStart(getApplicationContext(), "StarInfoActivity");
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "StarInfoActivity");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		activity = this;
		setContentView(R.layout.activity_starinfo);
		star_id = getIntent().getStringExtra("star_id");

		ImageView backImageView = (ImageView) findViewById(R.id.back_imageView);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		initView();
		

		getData();
	}

	private void initView(){
		circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
		
		nameTextView = (TextView) findViewById(R.id.name_textView);
		typeTextView = (TextView) findViewById(R.id.type_textView);
		birthTextView = (TextView) findViewById(R.id.birth_textView);
		conTextView = (TextView) findViewById(R.id.con_textView);
		bornTextView = (TextView) findViewById(R.id.born_textView);
		worksTextView = (TextView) findViewById(R.id.works_textView);
		newsTextView = (TextView) findViewById(R.id.news_textView);
		worksTextView.setSelected(true);
		
		worksGridView = (GridView) findViewById(R.id.works_gridView);
		newsGridView = (GridView) findViewById(R.id.news_gridView);
		
		works = new ArrayList<>();
		worksAdapter = new StarWorksAdapter(this, works);
		worksGridView.setAdapter(worksAdapter);
		worksGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		worksGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
				intent.putExtra("movie_id", works.get(position).getId());
				intent.putExtra("type_id", works.get(position).getType_id());
				intent.putExtra("episodes_id", "0");
				startActivity(intent);
			}
		});
		
		news = new ArrayList<>();
		newsAdapter = new StarNewsAdapter(this, news);
		newsGridView.setAdapter(newsAdapter);
		newsGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		newsGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
				intent.putExtra("movie_id", news.get(position).getId());
				intent.putExtra("type_id", news.get(position).getType_id());
				intent.putExtra("episodes_id", "0");
				startActivity(intent);
			}
		});
		
	}
	
	private void setView(){
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(starInfo.getAvatars(), circleImageView, GlobalParams.iconOptions);
		
		nameTextView.setText(starInfo.getName());
		typeTextView.setText(starInfo.getProfessions());
		birthTextView.setText("生日" + "\n" + starInfo.getBirthday());
		conTextView.setText("星座" + "\n" + starInfo.getConstellation());
		bornTextView.setText("地区" + "\n" + starInfo.getBorn_place());
		
		if (starInfo.getWorks().getList() != null) {
			works.addAll(starInfo.getWorks().getList());
			worksAdapter.notifyDataSetChanged();
		}
		
		if (starInfo.getNews().getList() != null) {
			news.addAll(starInfo.getNews().getList());
			newsAdapter.notifyDataSetChanged();
		}
		
		
		worksTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newsTextView.setSelected(false);
				worksTextView.setSelected(true);
				newsGridView.setVisibility(View.GONE);
				worksGridView.setVisibility(View.VISIBLE);
				
			}
		});
		
		newsTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newsTextView.setSelected(true);
				worksTextView.setSelected(false);
				newsGridView.setVisibility(View.VISIBLE);
				worksGridView.setVisibility(View.GONE);
				
			}
		});
	}
	
	public void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				if (starInfo != null) {
					setView();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				StarApi starApi = new StarApi();
				starInfo = starApi.getStarInfo(star_id);
				return "true";
			}
		}.execute("");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

}
