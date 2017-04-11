package com.bccv.strategy.ui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.strategy.api.NetWorkAPI;
import com.bccv.strategy.model.QuestionInfoBean;
import com.bccv.strategy.model.QuestionInfoListResBean;
import com.bccv.strategy.network.HttpCallback;
import com.bccv.strategy.network.NetResBean;
import com.bccv.strategy.sns.UserInfoManager;
import com.bccv.strategy.ui.activity.Answer2PersonActivity;
import com.bccv.strategy.ui.activity.LoginActivity;
import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.activity.PersonalZoneActivity;
import com.bccv.strategy.ui.activity.ReportMsgActivity;
import com.bccv.strategy.ui.adapter.QAListAdapter;
import com.bccv.strategy.ui.adapter.QAListAdapter.OnBtnClickListener;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.ui.view.XListView;
import com.bccv.strategy.ui.view.XListView.IXListViewListener;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.utils.SystemUtil;
import com.bccv.strategy.R;

/**
 * 疑问解答
 * 
 * @author liukai
 * 
 * @version 2015-3-27 上午10:22:45
 */
public class QAFramgent extends PrimaryFramgent implements IXListViewListener,
		OnClickListener, OnTouchListener, OnScrollListener, OnItemClickListener {

	private static final String TAG = "QAFramgent";

	private View mView;
	private View titleMenuBtn, titleRightBtn, title;
	private TextView titleTv;
	private View loadingView;
	private View errorView;
	private XListView listView;
	private View qa_import_rl, qa_send_btn;
	private EditText qa_reply_edit_et;

	private OnBtnClickListener onItemBtnClickListener;

	private ArrayList<QuestionInfoBean> dataList = new ArrayList<QuestionInfoBean>();
	private QAListAdapter mAdapter;

	private MainActivity mActivity;

	private int currentPageId = 0;
	private boolean isDataSuccess = false;

	private int currentClickPosition = -1;

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
			mView = inflater.inflate(R.layout.fragment_qa, null);
		}

		title = mView.findViewById(R.id.qa_title);
		titleMenuBtn = title.findViewById(R.id.common_title_menu_btn);
		titleRightBtn = title.findViewById(R.id.common_title_right_btn);
		titleTv = (TextView) title.findViewById(R.id.common_title_tv);
		titleTv.setText("疑问答疑");

		titleMenuBtn.setOnClickListener(this);
		titleRightBtn.setOnClickListener(this);

		loadingView = mView.findViewById(R.id.qa_loading);
		errorView = mView.findViewById(R.id.qa_error);

		listView = (XListView) mView.findViewById(R.id.xlistview_include);
		qa_import_rl = mView.findViewById(R.id.qa_import_rl);
		qa_send_btn = mView.findViewById(R.id.qa_send_btn);
		qa_reply_edit_et = (EditText) mView.findViewById(R.id.qa_reply_edit_et);
		qa_send_btn.setOnClickListener(this);

		mView.setOnTouchListener(this);
		qa_import_rl.setOnClickListener(this);
		qa_import_rl.setOnTouchListener(this);
		listView.setOnScrollListener(this);

		onItemBtnClickListener = new OnBtnClickListener() {

			@Override
			public void onUserIconClick(String userId) {
				Intent personalZoneIntent = new Intent(mActivity,
						PersonalZoneActivity.class);
				personalZoneIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY,
						userId);
				personalZoneIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
						mActivity.backGroundView.getGradientColor());
				mActivity.startActivity(personalZoneIntent);
			}

			@Override
			public void onReplyBtnClick(int position) {
				// TODO Auto-generated method stub
				currentClickPosition = position;

				if (qa_import_rl.getVisibility() == View.GONE) {
					qa_import_rl.setVisibility(View.VISIBLE);
					showSoftInput();
				} else {
					qa_reply_edit_et.requestFocusFromTouch();
				}
			}
		};

		mAdapter = new QAListAdapter(mActivity, dataList,
				onItemBtnClickListener);
		listView.setAdapter(mAdapter);

		listView.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(false);
		listView.setEnableDragLoadMore(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// TODO 初始化数据
		if (!isDataSuccess || dataList.size() == 0) {
			requestData(1);
		}

	}

	private void requestData(int p) {
		if (SystemUtil.isNetOkWithToast(mActivity)) {

			NetWorkAPI.questions(mActivity.getApplicationContext(), p,
					new HttpCallback() {

						@Override
						public void onResult(NetResBean response) {
							// TODO Auto-generated method stub
							L.i(TAG, "onResult", response.toString());
							if (response instanceof QuestionInfoListResBean) {
								QuestionInfoListResBean data = (QuestionInfoListResBean) response;
								int now_p = data.getNow_p();
								int total_p = data.getTotal_p();
								ArrayList<QuestionInfoBean> questionInfoBeans = data
										.getQuestionInfoBeans();
								if (questionInfoBeans != null
										&& questionInfoBeans.size() > 0) {
									if (now_p == 1) {
										dataList.clear();
									}
									dataList.addAll(questionInfoBeans);
									currentPageId = now_p;
									mAdapter.notifyDataSetChanged();
									isDataSuccess = true;
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
							} else {
								listView.stopRefresh();
								listView.stopLoadMore();
								Toast.makeText(mActivity, "数据请求失败",
										Toast.LENGTH_SHORT).show();
								loadingView.setVisibility(View.GONE);
							}

						}

						@Override
						public void onError(String errorMsg) {
							// TODO Auto-generated method stub
							listView.stopRefresh();
							listView.stopLoadMore();
							Toast.makeText(mActivity, "数据请求失败",
									Toast.LENGTH_SHORT).show();
							loadingView.setVisibility(View.GONE);

						}

						@Override
						public void onCancel() {
						}
					});

		}
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

	@Override
	public void onRefresh() {
		// TODO 刷新数据
		if (dataList.size() == 0) {
			loadingView.setVisibility(View.VISIBLE);
		}
		requestData(1);

		// listView.stopRefresh();
		// listView.stopLoadMore();

	}

	@Override
	public void onLoadMore() {
		// TODO 加载更多
		requestData(currentPageId + 1);

		// listView.stopRefresh();
		// listView.stopLoadMore();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qa_send_btn:
			// TODO 发送回复
			if (SystemUtil.isNetOkWithToast(mActivity)) {
				String answer = qa_reply_edit_et.getText().toString().trim();
				if (TextUtils.isEmpty(answer)) {
					Toast.makeText(mActivity, "请输入内容", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				hideSoftInput();
				if (currentClickPosition == -1) {
					Toast.makeText(mActivity, "回复失败", Toast.LENGTH_SHORT)
							.show();
				}
				
				if(UserInfoManager.isLogin()){
					QuestionInfoBean questionInfoBean = dataList
							.get(currentClickPosition);
					
					NetWorkAPI.add_answer(mActivity, answer, questionInfoBean.getId(),
							new HttpCallback() {
						
						@Override
						public void onResult(NetResBean response) {
							// TODO Auto-generated method stub
							if (response.success) {
								qa_reply_edit_et.setText("");
								Toast.makeText(mActivity, "回复成功",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(mActivity, "回复失败",
										Toast.LENGTH_SHORT).show();
							}
							
						}
						
						@Override
						public void onError(String errorMsg) {
							// TODO Auto-generated method stub
							Toast.makeText(mActivity, "回复失败",
									Toast.LENGTH_SHORT).show();
						}
						
						@Override
						public void onCancel() {
						}
					});
				}else{
					Intent loginIntent = new Intent(mActivity, LoginActivity.class);
					loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, mActivity.backGroundView.getGradientColor());
					mActivity.startActivity(loginIntent);
				}

			}
			break;
		case R.id.common_title_menu_btn:
			hideEditTextState();
			mActivity.onClick(titleMenuBtn);
			break;
		case R.id.common_title_right_btn:
			hideEditTextState();
			Intent intent = new Intent(mActivity, ReportMsgActivity.class);
			intent.putExtra(BackGroundView.BACKGROUND_COLOR,
					mActivity.backGroundView.getGradientColor());
			intent.putExtra(ReportMsgActivity.REPORT_MSG_CATE,
					ReportMsgActivity.QUESTION_CATE);
			startActivity(intent);
			mActivity.overridePendingTransition(R.anim.in_from_bottom,
					R.anim.out_to_none);
			break;

		default:
			break;
		}

	}

	private void hideEditTextState() {
		hideSoftInput();
		if (qa_import_rl.getVisibility() == View.VISIBLE) {
			currentClickPosition = -1;
			qa_reply_edit_et.setText("");
			qa_import_rl.setVisibility(View.GONE);
		}
	}

	private void hideSoftInput() {
		/* 隐藏软键盘 */
		InputMethodManager imm = (InputMethodManager) qa_reply_edit_et
				.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(
					qa_reply_edit_et.getApplicationWindowToken(), 0);
		}
	}

	private void showSoftInput() {
		/* 隐藏软键盘 */
		InputMethodManager imm = (InputMethodManager) mActivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		}
		qa_reply_edit_et.requestFocusFromTouch();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		if (v.getId() != R.id.qa_import_rl) {
			hideEditTextState();
		}
		return false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		hideEditTextState();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		hideEditTextState();
		Intent answer2PersonIntent = new Intent(mActivity,
				Answer2PersonActivity.class);
		answer2PersonIntent.putExtra(BackGroundView.BACKGROUND_COLOR,
				mActivity.backGroundView.getGradientColor());
		answer2PersonIntent.putExtra(Answer2PersonActivity.QUESTION_INFO_KEY,
				dataList.get(position - 1));
		mActivity.startActivity(answer2PersonIntent);
	}

}
