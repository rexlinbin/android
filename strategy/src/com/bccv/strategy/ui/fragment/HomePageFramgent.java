package com.bccv.strategy.ui.fragment;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.AppInfoItemBean;
import com.bccv.strategy.model.HomeResBean;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.adapter.RoundTabAdapter;
import com.bccv.strategy.ui.adapter.VpAdapter;
import com.bccv.strategy.ui.utils.ViewPagerScroller;
import com.bccv.strategy.ui.view.HomePageViewPager;
import com.bccv.strategy.ui.view.RoundTab;
import com.bccv.strategy.ui.view.RoundTab.OnItemClickListener;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.PreferenceHelper;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

public class HomePageFramgent extends PrimaryFramgent implements
		OnClickListener {

	private static final String TAG = "HomePageFramgent";

	private View mView;
	private RoundTab round_tab;
	private HomePageViewPager round_vp;
	private View titleMenuBtn, titleRightBtn, title;
	private TextView titleTv;
	private View loadingView;
	private View errorView;

	private ViewPagerScroller viewPagerScroller;

	private ArrayList<AppInfoItemBean> homeItemList;

	private RoundTabAdapter roundTabAdapter;
	private VpAdapter vpAdapter;

	private int startPosition;

	private MainActivity mActivity;
	
	private boolean isDataSuccess = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) getActivity();
		// TODO 初始化视图
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		} else {
			mView = inflater.inflate(R.layout.fragment_homepage, null);
		}

		homeItemList = new ArrayList<AppInfoItemBean>();
		homeItemList.add(new AppInfoItemBean());
		homeItemList.add(new AppInfoItemBean());
		homeItemList.add(new AppInfoItemBean());
		homeItemList.add(new AppInfoItemBean());
		homeItemList.add(new AppInfoItemBean());
		

		round_tab = (RoundTab) mView.findViewById(R.id.round_tab);
		roundTabAdapter = new RoundTabAdapter(homeItemList, getActivity());
		round_tab.setAdapter(roundTabAdapter);
		round_tab.setOnItemClickListener(new MyOnItemClickListener());

		round_vp = (HomePageViewPager) mView.findViewById(R.id.round_vp);

		viewPagerScroller = new ViewPagerScroller(mActivity,
				ViewPagerScroller.sInterpolator);
		viewPagerScroller.initViewPagerScroll(round_vp);

		vpAdapter = new VpAdapter(homeItemList, getActivity());
		round_vp.setOffscreenPageLimit(2);
		startPosition = vpAdapter.getCount() / 2;
		round_vp.setAdapter(vpAdapter);
		round_vp.setOnPageChangeListener(new MyOnPageChangeListener());
		round_vp.setCurrentItem(startPosition);// 初始化ViewPager 开始位置

		title = mView.findViewById(R.id.homepage_title);
		titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
		titleRightBtn = title.findViewById(R.id.common_title_right_btn);
		titleTv = (TextView) title.findViewById(R.id.common_title_tv);
		titleTv.setText("助手精选");
		
		titleMenuBtn.setOnClickListener(mActivity);
		titleRightBtn.setOnClickListener(mActivity);

		loadingView = mView.findViewById(R.id.homepage_loading);
		errorView = mView.findViewById(R.id.homepage_error);

		errorView.setOnClickListener(this);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO 初始化数据
		if(!isDataSuccess || homeItemList.size()==0){
			requestData();
		}
	}

	/**
	 * 请求数据
	 */
	private void requestData() {
		errorView.setVisibility(View.GONE);
		loadingView.setVisibility(View.VISIBLE);
		if(SystemUtil.isNetOkWithToast(mActivity)){
			
			NetWorkAPI.app_index(mActivity, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					// TODO Auto-generated method stub
					L.i(TAG, "onResult", "response : " + response);
					if(response instanceof HomeResBean){
						HomeResBean data = (HomeResBean) response;
						if(data.success && data.getHomeItemList()!=null && data.getHomeItemList().size()>0){
							homeItemList.clear();
							homeItemList.addAll(data.getHomeItemList());
							PreferenceHelper.putString("an_download", data.getAn_download());
							vpAdapter.notifyDataSetChanged();
							round_tab.notifyDataSetChanged(round_vp.getCurrentItem()-startPosition);
							loadingView.setVisibility(View.GONE);
							
							isDataSuccess = true;
							
							loadingView.setVisibility(View.GONE);
							errorView.setVisibility(View.GONE);
							
						}else{
							loadingView.setVisibility(View.GONE);
							errorView.setVisibility(View.VISIBLE);
						}
					}else{
						loadingView.setVisibility(View.GONE);
						errorView.setVisibility(View.VISIBLE);
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					// TODO Auto-generated method stub
					L.i(TAG, "CategoryListCallBack  errorMsg", "errorMsg : " + errorMsg);
					loadingView.setVisibility(View.GONE);
					errorView.setVisibility(View.VISIBLE);
				}
				
				@Override
				public void onCancel() {}
			});
			
		}else{
			loadingView.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
		}

	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// // TODO Auto-generated method stub
			// ArgbEvaluator evaluator = new ArgbEvaluator();
			// ArgbEvaluator evaluator2 = new ArgbEvaluator();
			// int evaluate = (Integer) evaluator.evaluate(positionOffset,
			// 0XFF8080FF,0XFFFF8080);
			// int evaluate2 = (Integer) evaluator2.evaluate(positionOffset,
			// 0XFFFF8080, 0XFF8080FF);
			// if (position % 2 == 0) {
			// // view.setGradient(0XFF8080FF,0XFFFF8080);
			// mActivity.backGroundView.setGradient(evaluate, evaluate2);
			// }else {
			// // view.setGradient(0XFFFF8080,0XFF8080FF);
			// mActivity.backGroundView.setGradient(evaluate2, evaluate);
			// }

			if (!ischanging) {
				changeBackground(position, position + 1, positionOffset);
			}

		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			round_tab.moveToTarget(position - startPosition);// 转换为实际数据位置
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
		}

	}

	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(View v, int position) {
			// TODO Auto-generated method stub
			int vpPosition = position + startPosition;
			 round_vp.setCurrentItem(vpPosition);
			// round_vp.setCurrentItemByUser(vpPosition);
//			changePageByClick(vpPosition);
		}

	}

	// --------------------------------------------------------------------------------------------------------------

	boolean ischanging = false;  //正在进行item交替渐变动画

	private synchronized void changePageByClick(final int vpPosition) {
		final int currentItem = round_vp.getCurrentItem();
		if (currentItem == vpPosition) {
			return;
		}

		round_tab.moveToTarget(vpPosition - startPosition);// 转换为实际数据位置

		ObjectAnimator anim = ObjectAnimator//
				.ofFloat(round_vp, "zhy", 1.0F, 0.0F)//
				.setDuration(250);//
		anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				round_vp.setAlpha(cVal);
//				System.out.println("##########  cVal 11:: " + cVal);

				changeBackground(currentItem, vpPosition, (1.0f - cVal) / 2.0f);

			}
		});

		final ObjectAnimator anim2 = ObjectAnimator//
				.ofFloat(round_vp, "zhy", 0.0F, 1.0F)//
				.setDuration(250);//
		anim2.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				round_vp.setAlpha(cVal);
//				System.out.println("##########  cVal 222:: " + cVal);

				changeBackground(currentItem, vpPosition, cVal / 2.0f + 0.5f);

			}
		});

		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				ischanging = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				if (vpPosition >= 0) {
					round_vp.setCurrentItem(vpPosition, false);
				}
				anim2.start();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				ischanging = false;
			}
		});

		anim2.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				ischanging = true;
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// TODO Auto-generated method stub
				ischanging = false;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				ischanging = false;
			}
		});

		anim.start();
	}

	public void changeBackground(int currentposition, int targetposition,
			float positionOffset) {
		// TODO Auto-generated method stub

		
		int currenteTop, currentBottom;
		int targetTop, targetBottom;
		if (currentposition % 6 == 0) {
			currenteTop = 0XFFd35f53;
			currentBottom = 0XFFdb417a;
		} else if(currentposition%6 == 1){
			currenteTop = 0XFF385773;
			currentBottom = 0XFFab5046;
		}else if(currentposition %6 == 2) {
			currenteTop = 0XFF613c56;
			currentBottom = 0XFF385773;
		}else if(currentposition %6 == 3) {
			currenteTop = 0xFF734d38;
			currentBottom = 0XFF613c56;
		}else if(currentposition % 6 == 4) {
			currenteTop = 0XFF347a69;
			currentBottom = 0XFF734d38;
		}else{
			currenteTop = 0XFF386371;
			currentBottom = 0XFF347a69;
		}

		if (targetposition % 6 == 1) {
			targetTop = 0XFF385773;
			targetBottom = 0XFFab5046;
		} else if(targetposition % 6 == 2){
			targetTop = 0XFF613c56;
			targetBottom = 0XFF385773;
		}else if(targetposition %6 == 3) {
			targetTop = 0xFF734d38;
			targetBottom = 0XFF613c56;
		}else if (targetposition % 6 == 4) {
			targetTop = 0XFF347a69;
			targetBottom = 0XFF734d38;
		}else if (targetposition % 6 == 5) {
			targetTop = 0XFF386371;
			targetBottom = 0XFF347a69;
		}else {
			targetTop = 0XFFd35f53;
			targetBottom = 0XFFdb417a;
		}
		System.out.println("current:"+(currentposition%6)+"-"+currenteTop +" target:"+(targetposition%6)+"-"+targetTop);

//		System.out.println("######################   positionOffset  :::  "
//				+ positionOffset);

		ArgbEvaluator evaluator = new ArgbEvaluator();
		ArgbEvaluator evaluator2 = new ArgbEvaluator();

		int evaluate = (Integer) evaluator.evaluate(positionOffset,
				currenteTop, targetTop);
		int evaluate2 = (Integer) evaluator2.evaluate(positionOffset,
				currentBottom, targetBottom);
//
//		System.out.println("######################   currenteTop  :::  "
//				+ currenteTop + "   targetTop : " + targetTop);
//		System.out.println("######################   positionOffset  :::  "
//				+ positionOffset + "   evaluate : " + evaluate
//				+ "   evaluate2 : " + evaluate2);

		mActivity.backGroundView.setGradient(evaluate, evaluate2);

	}

	// --------------------------------------------------------------------------------------------------------------

	@Override
	public void onMenuTransform(float percentOpen) {
		// TODO Auto-generated method stub
		round_tab.setAlpha(percentOpen);
		title.setAlpha(percentOpen);
	}

	@Override
	public void onMenuOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMenuClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.homepage_error:
			// TODO 刷新数据
			requestData();
			break;

		default:
			break;
		}

	}

}
