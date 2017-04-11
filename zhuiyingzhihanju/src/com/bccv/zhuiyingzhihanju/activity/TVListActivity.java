package com.bccv.zhuiyingzhihanju.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.api.TVApi;
import com.bccv.zhuiyingzhihanju.fragment.TVListFragment;
import com.bccv.zhuiyingzhihanju.model.TVType;
import com.utils.tools.BaseFragmentActivity;
import com.utils.tools.Callback;
import com.utils.views.HorizontalListView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TVListActivity extends BaseFragmentActivity {
	private List<TVType> list, getList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tvlist);
		setTitle();
		list = new ArrayList<>();
		getData();
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

	}

	private void getData() {
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				if (getList != null) {
					list.addAll(getList);
					InitTextView();
					InitViewPager();
				} else {
					Toast.makeText(getApplicationContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
				}
			}
		};

		new DataAsyncTask(callback, true) {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				TVApi tvApi = new TVApi();
				getList = tvApi.getTVList();

				return null;
			}
		}.execute("");
	}

	// viewpager+fragment
	private int currIndex;// 当前页卡编号
	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;

	private HorizontalListView horizontalListView;
	private MyTabAdapter adapter;

	/*
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<Fragment>();
		for (int i = 0; i < list.size(); i++) {
			TVListFragment introFragment = new TVListFragment();
			Bundle introBundle = new Bundle();
			introBundle.putSerializable("tvList", (Serializable) list.get(i).getList());
			introFragment.setArguments(introBundle);
			fragmentList.add(introFragment);
		}

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
		mPager.setOffscreenPageLimit(3);
	}

	/*
	 * 初始化标签名
	 */
	public void InitTextView() {
		horizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
		if (list.size() > 0) {
			list.get(0).setSelect(true);
		}
		adapter = new MyTabAdapter(list);
		horizontalListView.setAdapter(adapter);
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				list.get(currIndex).setSelect(false);
				currIndex = position;
				list.get(currIndex).setSelect(true);
				adapter.notifyDataSetChanged();
				mPager.setCurrentItem(currIndex);
			}
		});

	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mPager.setCurrentItem(index);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(final int arg0) {
			// TODO Auto-generated method stub
			list.get(currIndex).setSelect(false);
			currIndex = arg0;
			list.get(currIndex).setSelect(true);
			adapter.notifyDataSetChanged();
			
			new Handler().postDelayed(new Runnable() {

				public void run() {
					horizontalListView.setSelection(arg0);
				}
			}, 200);
		}
	}

	public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list) {
			super(fm);
			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Fragment getItem(int position) {
			return list.get(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return super.instantiateItem(container, position);
		}
	}

	public class MyTabAdapter extends BaseAdapter {
		private List<TVType> list;

		public MyTabAdapter(List<TVType> list) {
			// TODO Auto-generated constructor stub
			this.list = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(getApplicationContext(), R.layout.listitem_tvtab, null);
				viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.textView.setText(list.get(position).getTitle());

			viewHolder.textView.setSelected(list.get(position).isSelect());

			return convertView;
		}

		class ViewHolder {
			TextView textView;
		}

	}
}
