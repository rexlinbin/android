package com.bccv.zhuiyingzhihanju.activity;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.adapter.CollectListAdapter;
import com.bccv.zhuiyingzhihanju.adapter.SearchRecomAdapter;
import com.bccv.zhuiyingzhihanju.api.CollectApi;
import com.bccv.zhuiyingzhihanju.api.SearchApi;
import com.bccv.zhuiyingzhihanju.model.Collect;
import com.bccv.zhuiyingzhihanju.model.HotSearch;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.SearchRecom;
import com.tendcloud.tenddata.TCAgent;
import com.utils.swipemenulistview.SwipeMenuListView;
import com.utils.tools.BaseActivity;
import com.utils.tools.Callback;
import com.utils.tools.DimensionPixelUtil;
import com.utils.tools.GlobalParams;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.TextView;

public class CollectActivity extends BaseActivity {
	private ListView listView;
	private CollectListAdapter adapter;
	private List<Collect> data;
	private List<Collect> list;

	private void tcStart() {
		TCAgent.onPageStart(getApplicationContext(), "CollectActivity");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TCAgent.onPageEnd(getApplicationContext(), "CollectActivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		tcStart();
		listView = (ListView) findViewById(R.id.swipeMenuListView);

		data = new ArrayList<Collect>();

		adapter = new CollectListAdapter(this, data);
		listView.setAdapter(adapter);

		// SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
		//
		// @Override
		// public void create(SwipeMenu menu) {
		// // TODO Auto-generated method stub
		// SwipeMenuItem item = new SwipeMenuItem(
		// getApplicationContext());
		// item.setBackground(new ColorDrawable(Color.parseColor("#ff3b30")));
		// item.setWidth((int)
		// DimensionPixelUtil.dip2px(getApplicationContext(), 90));
		// item.setTitle("删除");
		// item.setTitleColor(Color.WHITE);
		// item.setTitleSize(15);
		// menu.addMenuItem(item);
		// }
		// };
		// listView.setMenuCreator(swipeMenuCreator);
		//
		// listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		//
		// @Override
		// public boolean onMenuItemClick(int position, SwipeMenu menu, int
		// index) {
		// // TODO Auto-generated method stub
		// deleteData(data.get(position));
		// data.remove(position);
		// adapter.notifyDataSetChanged();
		// return false;
		// }
		// });

		TextView titleName = (TextView) findViewById(R.id.titleName_textView);
		titleName.setText("我的收藏");
		titleName.setVisibility(View.VISIBLE);

		ImageButton backBtn = (ImageButton) findViewById(R.id.titel_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		listView.setSelector(new ColorDrawable(android.R.color.transparent));

		setEdit();

	}

	private ListView hotListView;
	private List<SearchRecom> hotList, getHotList;
	private SearchRecomAdapter hotAdapter;

	private void setTuijian() {
		LinearLayout tuijian = (LinearLayout) findViewById(R.id.tuijian_layout);
		tuijian.setVisibility(View.VISIBLE);
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
				Intent intent = new Intent(getApplicationContext(), VideoInfoActivity.class);
				intent.putExtra("movie_id", hotList.get(position).getId());
				intent.putExtra("type_id", hotList.get(position).getType_id());
				intent.putExtra("episodes_id", "0");
				startActivity(intent);
			}
		});
		getTuijianData();
	}

	private void getTuijianData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stu

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

	public void getData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				data.clear();
				if (list != null) {
					LinearLayout tuijian = (LinearLayout) findViewById(R.id.tuijian_layout);
					tuijian.setVisibility(View.GONE);
					editTextView.setEnabled(true);
					data.addAll(list);

					adapter.notifyDataSetChanged();
				} else {
					setTuijian();
					editTextView.setEnabled(false);
				}

			}
		};

		new DataAsyncTask(callback, false) {

			@Override
			protected String doInBackground(String... params) {
				CollectApi collectApi = new CollectApi();
				if (GlobalParams.user != null) {
					list = collectApi.getCollectList(GlobalParams.user.getUid(), "1", "100000",
							GlobalParams.user.getToken());
				}

				return "true";
			}
		}.execute("");
	}

	public void deleteData() {

		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (isEditShow) {
					editTextView.setText("编辑");
					data.removeAll(selectList);
					selectList.clear();
					for (int i = 0; i < data.size(); i++) {
						data.get(i).setEdit(false);
					}
					adapter.notifyDataSetChanged();
					hideEdit();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				CollectApi collectApi = new CollectApi();
				for (int i = 0; i < selectList.size(); i++) {
					Collect collect = selectList.get(i);
					collectApi.deleteCollect(GlobalParams.user.getUid(), collect.getId(), collect.getType(),
							GlobalParams.user.getToken());
				}

				return "true";
			}
		}.execute("");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		getData();

	}

	private LinearLayout editLayout;
	private boolean isEditShow, isSelectAll = false;
	private float editHeight = 0;
	private TextView editTextView, selectTextView, deleteTextView;
	private List<Collect> selectList;

	private void setEdit() {
		selectList = new ArrayList<>();
		editHeight = DimensionPixelUtil.dip2px(getApplicationContext(), 60);
		editTextView = (TextView) findViewById(R.id.titleEdit_textView);
		editTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isEditShow) {
					editTextView.setText("编辑");
					for (int i = 0; i < data.size(); i++) {
						data.get(i).setEdit(false);
					}
					adapter.notifyDataSetChanged();
					hideEdit();
				} else {
					editTextView.setText("完成");
					for (int i = 0; i < data.size(); i++) {
						data.get(i).setEdit(true);
						data.get(i).setSelect(false);
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
					for (int i = 0; i < data.size(); i++) {
						Collect downloadMovie = data.get(i);
						downloadMovie.setSelect(false);
					}
					selectList.clear();
					selectTextView.setText("全选");
				}else{
					isSelectAll = true;
					for (int i = 0; i < data.size(); i++) {
						Collect downloadMovie = data.get(i);
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

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Collect downloadMovie = data.get(position);
				if (isEditShow) {
					if (downloadMovie.isSelect()) {
						selectList.remove(downloadMovie);
					} else {
						selectList.add(downloadMovie);
					}
					downloadMovie.setSelect(!downloadMovie.isSelect());
					adapter.notifyDataSetChanged();
				} else {
					Intent aIntent = new Intent(CollectActivity.this, VideoInfoActivity.class);

					aIntent.putExtra("movie_id", list.get(position).getId());
					aIntent.putExtra("type_id", list.get(position).getType());
					if (list.get(position).getType().equals("7") || list.get(position).getType().equals("8")
							|| list.get(position).getType().equals("9") || list.get(position).getType().equals("10")) {
						aIntent.putExtra("isEpisode", true);
					} else {
						aIntent.putExtra("isEpisode", false);
					}
					aIntent.putExtra("episodes_id", "0");
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
