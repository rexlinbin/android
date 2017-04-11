package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.model.GetAuthorCommentResBean;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.ui.adapter.CommentListAdapter;
import com.bccv.meitu.utils.SystemUtil;
import com.me.maxwin.view.NormalXListView;
import com.me.maxwin.view.NormalXListView.IXListViewListener;

public class CommentActivity extends BaseActivity implements IXListViewListener{
	
	private ImageView comment_back;
	private NormalXListView mListView;
	private LinearLayout ll_no_comment;
	private RelativeLayout waiting_layout;
	private CommentListAdapter mAdapter;
	private int author_id = -1;
	private String special_id = "";
	private int page = 1;
	private int curPage = -1;
	private int totalPage = -1;
	private List<Comment> commentList = null;
	private ArrayList<String> commentsList = new ArrayList<String>();
	private RelativeLayout rl_album_comment;
	private EditText album_et_comment;
	private Button album_send_btn;
	private InputMethodManager imm;
//	private ImageView pro_tv_get_more;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		//获取用户id
		author_id = getIntent().getIntExtra("author_id", -1);
		special_id = getIntent().getStringExtra("special_id");
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//初始化控件
		initView();
		//获取数据
		getComments(page,false);
	}
	private void initView() {
		comment_back = (ImageView) findViewById(R.id.comment_back);
		mListView = (NormalXListView) findViewById(R.id.comment_xlv);
		ll_no_comment = (LinearLayout) findViewById(R.id.ll_no_comment);
		waiting_layout = (RelativeLayout) findViewById(R.id.waitting_layout);
		rl_album_comment = (RelativeLayout) findViewById(R.id.rl_album_comment);
		album_et_comment = (EditText) findViewById(R.id.album_et_comment);
		album_send_btn = (Button) findViewById(R.id.album_send_btn);
		
		comment_back.setOnClickListener(this);
		album_send_btn.setOnClickListener(this);
		album_et_comment.setOnClickListener(this);
		mAdapter = new CommentListAdapter(this);
		
//		View viewFooter = LayoutInflater.from(this).inflate(R.layout.pro_album_list_footer, null);
//		pro_tv_get_more = (ImageView) viewFooter.findViewById(R.id.pro_tv_get_more);
//		pro_tv_get_more.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(totalPage - curPage > 0 && totalPage != -1 && curPage != -1){
//					getComments(curPage + 1);
//				}else{
//					showShortToast("暂无更多数据");
//				}
//			}
//		});
//		if(curPage == -1 || totalPage == -1){
//			pro_tv_get_more.setVisibility(View.GONE);
//		}
//		mListView.addFooterView(viewFooter);
//		
		mListView.setAdapter(mAdapter);
		
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		mListView.hideFooter();
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				showShortToast("点击回复评论");
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(totalPage != -1 && curPage != -1 && totalPage - curPage <= 0){
					if (isLoadMore && mListView.getLastVisiblePosition() == (totalItemCount-1)) {
						showShortToast("没有新数据了");
					}
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comment_back:
			onBackPressed();
			break;
		case R.id.album_et_comment:
			//由于存在两个edittext,其中一个还是能够隐藏,并在listview中的.所以软键盘弹出方式不能用压缩弹出.
			imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
			break;
		case R.id.album_send_btn:
			if (UserInfoManager.isLogin()) {
				String trim = album_et_comment.getText().toString().trim();
				if (trim == null || trim.equals("")) {
					showShortToast("评论内容不能为空");
				}else{
					commentsList.add(album_et_comment.getText().toString().trim());
					album_et_comment.setText("");
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//				showShortToast("收集评论");
				}
			}else{
				showShortToast("请先登录");
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			break;
		default:
			break;
		}
	}
	
	private void getComments(int page,final boolean isRefresh){
		displayLoading();
		if (author_id != -1) {
			if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
				NetWorkAPI.getauthorcomment(getApplicationContext(), author_id, page, new HttpCallback() {
					
					@Override
					public void onResult(NetResBean response) {
						disappearLoading();
						if (response.success && response instanceof GetAuthorCommentResBean) {
//							showShortToast("获取评论列表成功");
							GetAuthorCommentResBean data = (GetAuthorCommentResBean) response;
//							System.out.println("!!!!!!!!!!!!!!!!!!!!!"+data.toString());
							getAllData(data,isRefresh);
						}else{
//							showShortToast("获取列表失败");
						}
					}
					
					@Override
					public void onError(String errorMsg) {
						disappearLoading();
					}
					
					@Override
					public void onCancel() {
						disappearLoading();
					}
				});
			}else{
				disappearLoading();
			}
		}else{
			disappearLoading();
//			showShortToast("author id is wrong");
		}
	}
	
	private void comment(){
		if (SystemUtil.isNetOkWithToast(getApplicationContext())) {
			NetWorkAPI.comment(getApplicationContext(), Integer.valueOf(special_id), commentsList, 
				new HttpCallback() {
					@Override
					public void onResult(NetResBean response) {
						if (response.success) {
//							showShortToast("评论成功");
						}else{
//							showShortToast("评论失败");
						}
					}
					
					@Override
					public void onError(String errorMsg) {}
					
					@Override
					public void onCancel() {}
				});
		}else {
//			waitting_layout.setVisibility(View.GONE);
		}
	}
	protected void getAllData(GetAuthorCommentResBean data,boolean isRefresh) {
		commentList = data.getComment();
		curPage = data.getPage();
		totalPage = data.getPage_total();
		setView(isRefresh);
	}
	private void setView(boolean isRefresh) {
		if(totalPage != -1 && curPage != -1 && totalPage - curPage > 0){
			mListView.showFooter();
		}else{
			mListView.hideFooter();
		}
		if (commentList == null || commentList.size() == 0) {
			ll_no_comment.setVisibility(View.VISIBLE);
		}else{
			ll_no_comment.setVisibility(View.GONE);
			mAdapter.setList(commentList,isRefresh);
		}
//		if (curPage != -1 && totalPage != -1 && totalPage > curPage) {
//			pro_tv_get_more.setVisibility(View.VISIBLE);
//		}
	}
	
	private boolean fresh = false;
	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		getComments(page,true);
		fresh = true;
		mListView.stopRefresh();
	}
	
	@Override
	protected void onPause() {
		if (commentsList != null && commentsList.size()>0) {
			comment();
			commentsList.clear();
		}
		super.onPause();
	}
	boolean isLoadMore = false;
	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		isLoadMore = true;
		getComments(curPage + 1,false);
		curPage += 1;
		mListView.stopLoadMore();
	}
	
	private void displayLoading(){
		waiting_layout.setVisibility(View.VISIBLE);
	}
	
	private void disappearLoading(){
		waiting_layout.setVisibility(View.GONE);
	}
}
