package com.bccv.bangyangapp.ui.view;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.api.NetWorkAPI;
import com.bccv.bangyangapp.model.MyCommentItemBean;
import com.bccv.bangyangapp.model.QuestionInfoBean;
import com.bccv.bangyangapp.model.UserAnswer2QListResBean;
import com.bccv.bangyangapp.model.UserComment2appListResBean;
import com.bccv.bangyangapp.network.HttpCallback;
import com.bccv.bangyangapp.network.NetResBean;
import com.bccv.bangyangapp.ui.activity.Answer2PersonActivity;
import com.bccv.bangyangapp.ui.activity.AppDetailsActivity;
import com.bccv.bangyangapp.ui.activity.CommentListActivity;
import com.bccv.bangyangapp.ui.activity.MainActivity;
import com.bccv.bangyangapp.ui.activity.PersonalZoneActivity;
import com.bccv.bangyangapp.ui.adapter.MyAppCommentChildListAdapter;
import com.bccv.bangyangapp.ui.adapter.MyAppCommentChildListAdapter.OnBtnClickListener;
import com.bccv.bangyangapp.ui.adapter.MyQACommentChildListAdapter;
import com.bccv.bangyangapp.ui.view.XListView.IXListViewListener;
import com.bccv.bangyangapp.utils.L;
import com.bccv.bangyangapp.utils.SystemUtil;

public class CommentChildPage implements OnClickListener, IXListViewListener,
		OnItemClickListener {

	public static final String TAG = "CommentChildPage";

	public static final int APP_TYPE = 1;
	public static final int AQ_TYPE = 2;

	private View mView;
	private View loadingView, errorView;
	private XListView listView;

	private int cate = 1;

	private ArrayList<MyCommentItemBean> dataList = new ArrayList<MyCommentItemBean>();
	private BaseAdapter mAdapter;

	private MainActivity mActivity;

	private int currentPageId = 0;

	public CommentChildPage(final MainActivity mActivity, int cate) {
		this.mActivity = mActivity;
		this.cate = cate;
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		} else {
			mView = View.inflate(mActivity, R.layout.page_myapp_qa, null);
		}
		listView = (XListView) mView.findViewById(R.id.xlistview_include);
		loadingView = mView.findViewById(R.id.page_myapp_qa_loading);
		errorView = mView.findViewById(R.id.page_myapp_qa_error);
		errorView.setOnClickListener(this);

		// dataList.add("111111");
		// dataList.add("111111");
		// dataList.add("111111");
		// dataList.add("111111");
		// dataList.add("111111");
		// dataList.add("111111");
		// dataList.add("111111");

		// TODO adapter

		switch (cate) {
		case APP_TYPE:
			// TODO 应用评论列表
			mAdapter = new MyAppCommentChildListAdapter(mActivity, dataList);
			OnBtnClickListener appOnBtnClickListener = new OnBtnClickListener() {

				@Override
				public void onAppIconClick(String appid) {
					// TODO Auto-generated method stub
					Intent appDetailsIntent = new Intent(mActivity,
							AppDetailsActivity.class);
					appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
							mActivity.backGroundView.getGradientColor());
					appDetailsIntent.putExtra(AppDetailsActivity.APP_ID,appid);
					mActivity.startActivity(appDetailsIntent);
				}
			};
			((MyAppCommentChildListAdapter) mAdapter)
					.setOnBtnClickListener(appOnBtnClickListener);
			break;
		case AQ_TYPE:
			// TODO 疑问答复列表
			mAdapter = new MyQACommentChildListAdapter(mActivity, dataList);

			MyQACommentChildListAdapter.OnBtnClickListener qaOnBtnClickListener = new MyQACommentChildListAdapter.OnBtnClickListener() {

				@Override
				public void onUserIconClick(String userId) {
					// TODO Auto-generated method stub
					Intent personalZoneIntent = new Intent(mActivity,
							PersonalZoneActivity.class);
					personalZoneIntent.putExtra(
							BackGroundView.BACKGROUND_COLOR,
							mActivity.backGroundView.getGradientColor());
					personalZoneIntent.putExtra(
							PersonalZoneActivity.ZONE_ID_KEY,userId);
					mActivity.startActivity(personalZoneIntent);

				}
			};

			((MyQACommentChildListAdapter) mAdapter)
					.setOnBtnClickListener(qaOnBtnClickListener);

			break;

		default:
			break;
		}

		listView.setAdapter(mAdapter);

		listView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setEnableDragLoadMore(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);

	}

	public void onCreate() {
		dataList.clear();
		mAdapter.notifyDataSetChanged();
		if(dataList.size()==0){
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);
	}

	public View getView() {
		return mView;
	}

	@Override
	public void onRefresh() {
		// TODO 刷新数据
		if(dataList.size()==0){
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);
	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId+1);
	}

	private void requestData(int p) {
		if (SystemUtil.isNetOkWithToast(mActivity)) {
			switch (cate) {
			case APP_TYPE:
				// TODO 应用评论
				NetWorkAPI.user_comment(mActivity, p, new MyHttpCallBack());
				break;
			case AQ_TYPE:
				// TODO 问题解答
				NetWorkAPI.user_answer(mActivity, p, new MyHttpCallBack());
				
				break; 
				
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.fragment_hot_fresh_error:

			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (cate == APP_TYPE) {
			MyCommentItemBean myCommentItemBean = dataList.get(position-1);
			Intent commentListIntent = new Intent(mActivity,
					CommentListActivity.class);
			commentListIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					mActivity.backGroundView.getGradientColor());
			commentListIntent.putExtra(CommentListActivity.APP_ID_KEY,
					myCommentItemBean.getApp_id());
			commentListIntent.putExtra(CommentListActivity.APP_NAME_KEY,
					myCommentItemBean.getTitle());
			commentListIntent.putExtra(CommentListActivity.COMMENT_ID_KEY,
					myCommentItemBean.getId());
			commentListIntent.putExtra(CommentListActivity.COMMENT_TYPE_KEY,
					myCommentItemBean.getReply());
			mActivity.startActivity(commentListIntent);
		} else {
			MyCommentItemBean myCommentItemBean = dataList.get(position - 1);
			QuestionInfoBean questionInfoBean = new QuestionInfoBean();
			questionInfoBean.setId(myCommentItemBean.getId());
			questionInfoBean.setContent(myCommentItemBean.getQuestions());
			questionInfoBean.setUser_icon(myCommentItemBean.getUser_icon());
			questionInfoBean.setUser_id(myCommentItemBean.getUser_id());
			questionInfoBean.setUser_name(myCommentItemBean.getUser_name());
			
			Intent answer2PersonIntent = new Intent(mActivity,
					Answer2PersonActivity.class);
			answer2PersonIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
					mActivity.backGroundView.getGradientColor());
			answer2PersonIntent.putExtra(Answer2PersonActivity.QUESTION_INFO_KEY,
					questionInfoBean);
			mActivity.startActivity(answer2PersonIntent);

		}
	}

	private class MyHttpCallBack implements HttpCallback {

		@Override
		public void onResult(NetResBean response) {
			// TODO Auto-generated method stub
			L.i(TAG, "onResult", response.toString());
			if (response instanceof UserComment2appListResBean) {
				UserComment2appListResBean data = (UserComment2appListResBean) response;
				ArrayList<MyCommentItemBean> comments = data.getComments();
				int now_p = data.getNow_p();
				int total_p = data.getTotal_p();
				setData(comments, now_p, total_p);
			} else if (response instanceof UserAnswer2QListResBean) {
				UserAnswer2QListResBean data = (UserAnswer2QListResBean) response;
				ArrayList<MyCommentItemBean> comments = data.getComments();
				int now_p = data.getNow_p();
				int total_p = data.getTotal_p();
				setData(comments, now_p, total_p);
			} else {
				Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT).show();
				loadingView.setVisibility(View.GONE);
			}
		}

		@Override
		public void onCancel() {
		}

		@Override
		public void onError(String errorMsg) {
			// TODO Auto-generated method stub
			Toast.makeText(mActivity, "数据请求失败", Toast.LENGTH_SHORT).show();
			loadingView.setVisibility(View.GONE);
		}

		private void setData(ArrayList<MyCommentItemBean> comments, int now_p,
				int total_p) {

			if (comments != null && comments.size() > 0) {
				if (now_p == 1) {
					dataList.clear();
				}
				dataList.addAll(comments);
				currentPageId = now_p;
				mAdapter.notifyDataSetChanged();
			}
			listView.stopRefresh();
			listView.stopLoadMore();
			if (currentPageId >= total_p) {
				listView.setPullLoadEnable(false);
				listView.setEnableDragLoadMore(false);
			} else {
				listView.setPullLoadEnable(true);
				listView.setEnableDragLoadMore(true);
			}
			loadingView.setVisibility(View.GONE);
		}

	}

}
