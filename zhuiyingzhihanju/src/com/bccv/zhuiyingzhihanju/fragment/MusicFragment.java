package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
import com.bccv.zhuiyingzhihanju.adapter.SpecialAdapter;
import com.bccv.zhuiyingzhihanju.api.SpecialApi;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieNews;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.net.NetUtil;
import com.utils.pulltorefresh.FooterLoadingLayout;
import com.utils.pulltorefresh.PullToRefreshBase;
import com.utils.pulltorefresh.PullToRefreshGridView;
import com.utils.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.utils.tools.BaseFragment;
import com.utils.tools.Callback;
import com.utils.tools.GlobalParams;
import com.utils.tools.PromptManager;
import com.utils.tools.ScreenUtil;
import com.utils.views.ChildViewPager;
import com.utils.views.ChildViewPager.OnSingleTouchListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MusicFragment extends BaseFragment
{
//
//	View view;
//	private PullToRefreshGridView gridView;
//	private GridView list;
//	private List<Movie> data;
//	private List<Movie> SList;
//
//	private SpecialAdapter adapter;
//	MovieNews sp;
//	private List<Movie> topList;
//	
//	
//	boolean isStop = true;
//	@SuppressLint("NewApi")
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		if (view == null) {
//			view = inflater.inflate(R.layout.special_item, container, false);
//
//			data = new ArrayList<Movie>();
//
//			gridView = (PullToRefreshGridView) view.findViewById(R.id.special_listView);
//			list = gridView.getRefreshableView();
//
//
//
//			adapter = new SpecialAdapter(data, getActivity());
//
//			list.setAdapter(adapter);
//
//			list.setSelector(new ColorDrawable(android.R.color.transparent));
//
//			list.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					// TODO Auto-generated method stub
//					Intent aIntent = new Intent(getActivity(), Video2DPlayerActivity.class);
//
//					aIntent.putExtra("movie_id", data.get(position).getId());
//					aIntent.putExtra("type_id", data.get(position).getType_id());
//					aIntent.putExtra("episodes_id", "1");
//
//					startActivity(aIntent);
//
//				}
//			});
//			gridView.setPullLoadEnabled(true);
//			gridView.setPullRefreshEnabled(true);
//			gridView.getRefreshableView().setSelector(
//					new ColorDrawable(android.R.color.transparent));
//			gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {
//
//				@Override
//				public void onPullDownToRefresh(
//						PullToRefreshBase<GridView> refreshView) {
//					// TODO Auto-generated method stub
//					if (!NetUtil.isNetworkAvailable(GlobalParams.context)) {
//						// 提示网络不给力,直接完成刷新
//						PromptManager.showToast(GlobalParams.context, "网络不给力");
//
//						gridView.onPullDownRefreshComplete();
//					} else {
//						getData(true);
//
//					}
//				}
//
//				@Override
//				public void onPullUpToRefresh(
//						PullToRefreshBase<GridView> refreshView) {
//					// TODO Auto-generated method stub
//					if (NetUtil.isNetworkAvailable(GlobalParams.context)) {
//						((FooterLoadingLayout) gridView.getFooterLoadingLayout())
//								.getmHintView().setText("数据加载中...");
//						getData(false);
//					} else {
//						PromptManager.showToast(GlobalParams.context, "网络不给力");
//						gridView.onPullUpRefreshComplete();
//					}
//				}
//
//			});	
//			
//			Bundle args = getArguments();
//			Boolean isSelect = args.getBoolean("ifSelect", false);
//
//			if (isSelect) {
//
//			} else {
//
//			}
//
//		}
//		ll_dots = (LinearLayout) view.findViewById(R.id.ll_point_group);
//		headerViewPager = (ViewPager) view.findViewById(R.id.fancy_viewPager);
//		headerViewPager.setOnPageChangeListener(new MyPageChangeListener());
//		Ptext = (TextView) view.findViewById(R.id.fancy_viewPager_text);
//		getData(true);
//		return view;
//
//	}
//	private int page = 1, count = 6;
//	public void getData(final boolean isRefresh) {
//		if (isRefresh) {
//			page = 1;
//		}
//		Callback callback = new Callback() {
//
//			@Override
//			public void handleResult(String result) {
//				// TODO Auto-generated method stub
//				if (sp != null) {
//					SList = sp.getList();
//					topList=sp.getBig();
//					if (isRefresh) {
//						data.clear();
//					}
//
//					data.addAll(SList);
//					adapter.notifyDataSetChanged();
//
//				}
//
//				if (topList != null) {
//					setTopView(topList);
//				}
//				if (isRefresh) {
//					gridView.onPullDownRefreshComplete();
//				} else {
//					gridView.onPullUpRefreshComplete();
//				}
//
//			}
//		};
//
//		new DataAsyncTask(callback, false) {
//
//			@Override
//			protected String doInBackground(String... params) {
//
//				SpecialApi api = new SpecialApi();
//
//				sp = api.getThemList("music", page + "", count + "");
//				page++;
//				return "true";
//			}
//		}.execute("");
//	}
//
//	@SuppressWarnings("unused")
//	private static final int CHANGE_VIEWPAGER = 1;
//	private List<ImageView> dots; // 图片标题正文的那些点
//	private List<String> headpicUrls;// 图片地址
//	private LinearLayout ll_dots;
//
//	private ViewPager headerViewPager;
//	private int currentItem;
//	private MyAdapter myAdapter;
//	private TextView Ptext;
//	private List<String> titleText;
//	private void setTopView(List<Movie> topData2) {
//		// TODO Auto-generated method stub
//
//		dots = new ArrayList<ImageView>();
//
//		headpicUrls = new ArrayList<String>();
//		titleText=new ArrayList<String>();
//		myAdapter = new MyAdapter(topData2);
//		int temp = topData2.size() > 0 ? (myAdapter.getCount()) / 2 % (topData2.size()) : 0;
//		currentItem = (myAdapter.getCount()) / 2 - temp;
//		initHeadData(topData2);
//		headerViewPager.setCurrentItem(currentItem);
//		headerViewPager.setAdapter(myAdapter);
//		headerViewPager.setCurrentItem(0);
//
//	}
//
//	/**
//	 * 初始化头部viewPager
//	 * 
//	 * @param freshList
//	 */
//	private void initHeadData(List<Movie> modelList) {
//
//		ll_dots.removeAllViews();
//		dots.clear();
//
//		headpicUrls.clear();
//
//		for (Movie model : modelList) {
//			ImageView dot = addDot(ll_dots);
//			dots.add(dot);
//
//			// 初始化图片url
//			headpicUrls.add(model.getImages());
//			titleText.add(model.getTitle());
//		}
//		dots.get(currentItem % (dots.size())).setImageResource(R.drawable.dot_select);
//
//	}
//
//	/**
//	 * 添加滑动点
//	 * 
//	 * @param ll_dots
//	 */
//	private ImageView addDot(LinearLayout ll_dots) {
//		// 初始化圆点
//
//		ImageView view = new ImageView(getActivity());
//		view.setImageResource(R.drawable.dot);
//		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		mLayoutParams.rightMargin = ScreenUtil.dp2px(4);
//		mLayoutParams.leftMargin = ScreenUtil.dp2px(4);
//		mLayoutParams.topMargin = ScreenUtil.dp2px(12);
//		mLayoutParams.bottomMargin = ScreenUtil.dp2px(7);
//		ll_dots.addView(view, mLayoutParams);
//		return view;
//	}
//
//	/**
//	 * 当ViewPager中页面的状态发生改变时调用
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	private class MyPageChangeListener implements OnPageChangeListener {
//		private int oldPosition = currentItem;
//
//		/**
//		 * This method will be invoked when a new page becomes selected.
//		 * position: Position index of the new selected page.
//		 */
//		public void onPageSelected(int position) {
//
//			currentItem = position;
//
//			dots.get(oldPosition % (dots.size())).setImageResource(R.drawable.dot);
//			dots.get(position % (dots.size())).setImageResource(R.drawable.dot_select);
//			oldPosition = position;
//
//		}
//
//		public void onPageScrollStateChanged(int position) {
//		}
//
//		public void onPageScrolled(int arg0, float arg1, int arg2) {
//
//		}
//	}
//
//	/**
//	 * 填充ViewPager页面的适配器
//	 * 
//	 * @author Administrator
//	 * 
//	 */
//	private class MyAdapter extends PagerAdapter {
//		private List<Movie> topData;
//
//		public MyAdapter(List<Movie> topData) {
//			// TODO Auto-generated constructor stub
//			this.topData = topData;
//		}
//
//		@Override
//		public int getCount() {
//			if (topData.size() == 1) {
//				return 1;
//			}
//			return Integer.MAX_VALUE;
//		}
//
//		@Override
//		public Object instantiateItem(View container, int progress) {
//			int curretIndex = (progress) % topData.size();
//			ImageView imageView = new ImageView(getActivity());
//			imageView.setScaleType(ScaleType.CENTER_CROP);
//
//			if (topData != null && topData.size() > curretIndex) {
//				setOnClick4headPic(imageView, topData.get(curretIndex));
//			}
//
//			((ViewPager) container).addView(imageView);
//
//			ImageLoader imageLoader = ImageLoader.getInstance();
//			imageLoader.displayImage(headpicUrls.get(curretIndex), imageView, GlobalParams.bannerOptions);
//			Ptext.setText(titleText.get(curretIndex));
//			return imageView;
//		}
//
//		@Override
//		public void destroyItem(View container, int arg1, Object arg2) {
//			((ViewPager) container).removeView((View) arg2);
//		}
//
//		@Override
//		public boolean isViewFromObject(View arg0, Object arg1) {
//			return arg0 == arg1;
//		}
//
//		@Override
//		public void restoreState(Parcelable arg0, ClassLoader arg1) {
//
//		}
//
//		@Override
//		public Parcelable saveState() {
//			return null;
//		}
//
//		@Override
//		public void startUpdate(View arg0) {
//
//		}
//
//		@Override
//		public void finishUpdate(View arg0) {
//
//		}
//	}
//
//	/**
//	 * 
//	 * /** 为头部viewPager设置点击事件
//	 * 
//	 * @param view
//	 * @param fresh
//	 */
//	private void setOnClick4headPic(View view, final Movie model) {
//		view.setOnClickListener(new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			int curretIndex = (currentItem) % topList.size();
//			Intent aIntent = new Intent(getActivity(), Video2DPlayerActivity.class);
//
//			aIntent.putExtra("movie_id", topList.get(curretIndex).getId());
//			aIntent.putExtra("type_id", topList.get(curretIndex).getType_id());
//			aIntent.putExtra("episodes_id", "1");
//
//			startActivity(aIntent);
//		}
//	});}
}
