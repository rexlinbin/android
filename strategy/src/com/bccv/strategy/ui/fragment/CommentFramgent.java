package com.bccv.strategy.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.adapter.MyCommentPagerAdapter;
import com.bccv.strategy.ui.view.CommentChildPage;
import com.bccv.strategy.R;

/**
 * 我的评论
 * @author liukai
 *
 * @version 2015-3-27  上午10:21:48
 */
public class CommentFramgent extends PrimaryFramgent implements OnClickListener, OnPageChangeListener {

	private static final String TAG = "CommentFramgent";

	private static final int APP_INDEX = 0;
	private static final int QA_INDEX = 1;

	private View mView;
	private View title, titleMenuBtn, titleRightBtn;
	private View appBtn, qaBtn;
	private TextView appTv, qaTv;
	private ViewPager viewPager;
	
	private MyCommentPagerAdapter mAdapter;
	private ArrayList<CommentChildPage> pageList;
	
	private MainActivity mActivity;
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
//			return mView;
		} else {
			mView = inflater.inflate(R.layout.fragment_mycomment, null);
			
			title = mView.findViewById(R.id.mycomment_title);
			titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
			titleRightBtn = title.findViewById(R.id.common_title_right_btn);
			appBtn = title.findViewById(R.id.app_rl);
			qaBtn = title.findViewById(R.id.qa_rl);
			appTv = (TextView) title.findViewById(R.id.app_tv);
			qaTv = (TextView) title.findViewById(R.id.qa_tv);
			
			titleMenuBtn.setOnClickListener(mActivity);
			titleRightBtn.setOnClickListener(mActivity);
			appBtn.setOnClickListener(this);
			qaBtn.setOnClickListener(this);
			
			viewPager = (ViewPager) mView.findViewById(R.id.mycomment_vp);
			
			initChilds();
			viewPager.setOnPageChangeListener(this);
			mAdapter = new MyCommentPagerAdapter(pageList);
			viewPager.setAdapter(mAdapter);
		}

		return mView;
	}
	
	private void initChilds() {
		// 初始化各个孩子fragment;
		// cate 查询分类id（0 最热 1 最新）
		pageList = new ArrayList<CommentChildPage>();
		CommentChildPage commentChildPage = null;
		for (int i = 0; i < 2; i++) {
			switch (i) {
			case 0:// 应用
				commentChildPage = new CommentChildPage(mActivity,CommentChildPage.APP_TYPE);
				break;
			case 1:// 答疑
				commentChildPage = new CommentChildPage(mActivity,CommentChildPage.AQ_TYPE);
				break;
			default:
				break;
			}
			pageList.add(commentChildPage);
		}
	}
	
	private void changePageTag(int page) {
		switch (page) {
		case APP_INDEX:
			appBtn.setBackgroundResource(R.drawable.change_left_select);
			appTv.setTextColor(0Xffffffff);
			qaBtn.setBackgroundResource(R.drawable.change_right);
			qaTv.setTextColor(0X4cffffff);
			break;
		case QA_INDEX:
			appBtn.setBackgroundResource(R.drawable.change_left);
			appTv.setTextColor(0X4cffffff);
			qaBtn.setBackgroundResource(R.drawable.change_right_select);
			qaTv.setTextColor(0Xffffffff);
			break;

		default:
			break;
		}
	}
	
	public void refreshData(){
		if(mAdapter!=null && pageList!=null){
			for (int i = 0; i < pageList.size(); i++) {
				pageList.get(i).onCreate();
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.app_rl:
			viewPager.setCurrentItem(APP_INDEX);
			break;
		case R.id.qa_rl:
			viewPager.setCurrentItem(QA_INDEX);
			break;

		default:
			break;
		}
	}

	@Override
	public void onPageSelected(int position) {
		changePageTag(position);
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}
	
	
	
	@Override
	public void onMenuTransform(float percentOpen) {
		// TODO Auto-generated method stub
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


}
