package com.bccv.zhuiyingzhihanju.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.HistoryListAdapter;
import com.bccv.zhuiyingzhihanju.adapter.SearchRecomAdapter;
import com.bccv.zhuiyingzhihanju.api.SearchApi;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.SearchRecom;
import com.tendcloud.tenddata.TCAgent;
import com.utils.swipemenulistview.SwipeMenuListView;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.DimensionPixelUtil;
import com.utils.tools.SerializationUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HistoryActivity extends BaseActivity implements OnClickListener {

	private List<Movie> list;

	SwipeMenuListView pList;

	HistoryListAdapter adapter;
	private LinearLayout view1;
	private LinearLayout view2;
	ListView hotListView;
	private SearchRecomAdapter hotAdapter;
	private List<SearchRecom> hotList, getHotList;

	private void tcStart() {
		TCAgent.onPageStart(getApplicationContext(), "HistoryActivity");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "HistoryActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tcStart();
		setContentView(R.layout.activity_history);

		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		LinearLayout li = (LinearLayout) findViewById(R.id.title);
		li.setBackgroundColor(getResources().getColor(R.color.white));

		TextView text = (TextView) findViewById(R.id.titleName_textView);
		text.setVisibility(View.VISIBLE);
		text.setText("历史记录");

		pList = (SwipeMenuListView) findViewById(R.id.pList);
		view1 = (LinearLayout) findViewById(R.id.his_view1);
		view2 = (LinearLayout) findViewById(R.id.his_view2);
		hotListView = (ListView) findViewById(R.id.hot_listView);

		hotList = new ArrayList<>();
		hotAdapter = new SearchRecomAdapter(this, hotList);
		hotListView.setAdapter(hotAdapter);
		hotListView.setSelector(new ColorDrawable(android.R.color.transparent));
		hotListView.setDividerHeight(0);
		hotListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), Video2DPlayerActivity.class);
				intent.putExtra("movie_id", hotList.get(position).getId());
				intent.putExtra("type_id", hotList.get(position).getType_id());
				intent.putExtra("episodes_id", "0");
				startActivity(intent);
			}
		});

		pList.setSelector(new ColorDrawable(android.R.color.transparent));

		setEdit();
	}

	/**
	 * 长按删除
	 * 
	 * @param position
	 */
	private void DeleteDialog(final int position) {
		AlertDialog.Builder builder = new Builder(HistoryActivity.this);

		builder.setMessage("确定删除文件？");
		builder.setTitle("提示");

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				list.remove(position);
				SerializationUtil.wirteHistorySerialization(HistoryActivity.this, (Serializable) list);
				;

				adapter.notifyDataSetChanged();

			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}

	public void deleteData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (isEditShow) {
					editTextView.setText("编辑");
					adapter.notifyDataSetChanged();
					hideEdit();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {

				list.removeAll(selectList);
				selectList.clear();
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setEdit(false);
				}
				SerializationUtil.wirteHistorySerialization(HistoryActivity.this, (Serializable) list);

				return "true";
			}
		}.execute("");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		list = SerializationUtil.readHistoryCache(this.getApplicationContext());

		if (list == null || list.size() == 0) {

			list = new ArrayList<Movie>();
			view1.setVisibility(View.GONE);
			view2.setVisibility(View.VISIBLE);
			getTui();
		} else {
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.GONE);
		}

		adapter = new HistoryListAdapter(this, list);

		pList.setAdapter(adapter);

	}

	private void getTui() {
		// TODO Auto-generated method stub
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub

				if (getHotList != null) {
					hotList.addAll(getHotList);
					hotAdapter.notifyDataSetChanged();
				}
			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				SearchApi searchApi = new SearchApi();

				getHotList = searchApi.getRecomSearchList("5");

				return null;
			}
		}.execute("");

	}

	private LinearLayout editLayout;
	private boolean isEditShow, isSelectAll = false;
	private float editHeight = 0;
	private TextView editTextView, selectTextView, deleteTextView;
	private List<Movie> selectList;

	
	
	private void setEdit() {
		selectList = new ArrayList<>();
		editHeight = DimensionPixelUtil.dip2px(getApplicationContext(), 60);
		editTextView = (TextView) findViewById(R.id.titel_clear);
		editTextView.setText("编辑");
		editTextView.setVisibility(View.VISIBLE);
		editTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isEditShow) {
					editTextView.setText("编辑");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(false);
					}
					adapter.notifyDataSetChanged();
					hideEdit();
				} else {
					editTextView.setText("完成");
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setEdit(true);
						list.get(i).setSelect(false);
					}
					adapter.notifyDataSetChanged();
					showEdit();
				}
			}
		});

		editLayout = (LinearLayout) findViewById(R.id.edit_layout);
		selectTextView = (TextView) findViewById(R.id.select_textView);
		selectTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isSelectAll) {
					isSelectAll = false;
					for (int i = 0; i < list.size(); i++) {
						Movie downloadMovie = list.get(i);
						downloadMovie.setSelect(false);
					}
					selectList.clear();
					selectTextView.setText("全选");
				}else{
					isSelectAll = true;
					for (int i = 0; i < list.size(); i++) {
						Movie downloadMovie = list.get(i);
						downloadMovie.setSelect(true);
						if (!selectList.contains(downloadMovie)) {
							selectList.add(downloadMovie);
						}
					}
					selectTextView.setText("取消全选");
				}
				
				adapter.notifyDataSetChanged();
			}
		});
		deleteTextView = (TextView) findViewById(R.id.delete_textView);
		deleteTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteData();
			}
		});

		pList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Movie downloadMovie = list.get(position);
				if (isEditShow) {
					if (downloadMovie.isSelect()) {
						selectList.remove(downloadMovie);
					} else {
						selectList.add(downloadMovie);
					}
					downloadMovie.setSelect(!downloadMovie.isSelect());
					adapter.notifyDataSetChanged();
				} else {
					Movie movie = list.get(position);
					list.remove(movie);
					list.add(0, movie);
					SerializationUtil.wirteHistorySerialization(HistoryActivity.this, (Serializable) list);

					Intent aIntent = new Intent(HistoryActivity.this, Video2DPlayerActivity.class);

					aIntent.putExtra("movie_id", movie.getId());
					aIntent.putExtra("type_id", movie.getType_id());
					aIntent.putExtra("episodes_id", movie.getEpisode_id());

					startActivity(aIntent);
				}
			}
		});
	}

	private void showEdit() {
		isEditShow = true;
		editLayout.setVisibility(View.VISIBLE);

		editLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, editHeight, 0);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);

		editLayout.startAnimation(bottomtranslateAnimation);
	}

	private void hideEdit() {
		isEditShow = false;
		selectList.clear();
		editLayout.clearAnimation();
		TranslateAnimation bottomtranslateAnimation = new TranslateAnimation(0, 0, 0, editHeight);
		bottomtranslateAnimation.setDuration(300);
		bottomtranslateAnimation.setFillAfter(true);
		bottomtranslateAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				editLayout.clearAnimation();
				editLayout.setVisibility(View.GONE);
			}
		});

		editLayout.startAnimation(bottomtranslateAnimation);

	}

}
