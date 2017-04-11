package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.model.GetAuthorCommentResBean;
import com.bccv.meitu.model.GetAuthorinfoResBean;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.ui.fragment.ProAlbumFragment;
import com.bccv.meitu.ui.fragment.ProCommentFragment;
import com.bccv.meitu.utils.ScreenUtil;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.PullToZoomScrollView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;
/**
 * 个人空间
 * @author ZhaoHaiyang
 * @since 2014年11月12日
 */
public class ProZoneActivity extends BaseFragmentActivity implements OnClickListener{
	private PullToZoomScrollView mScrollView;
	private RelativeLayout waitting_layout;
	private ViewPager viewPager;
	private ProAlbumFragment view1;
	private ProCommentFragment view2;// 各个页卡
	private List<Fragment> views;// Tab页面列表
	private int currIndex = 0;// 当前页卡编号
	private int author_id;//个人空间id
	private int page = 1;//当前页
	private RelativeLayout pro_rl_root;
	/**************head view ******************/
	private ImageView pro_icon;
	private ImageView pro_back;
	/**************zoom view ******************/
	private ImageView iv_zoom;
	/*************content view*****************/
	private TextView tv_pro_name;
	private ImageView pro_guanzhu;
	private RelativeLayout ll_pro_left;
	private RelativeLayout ll_pro_right;
	private TextView pro_album;//专辑(200)
	private View pro_line_left;//左边线
	private TextView pro_comment;//评论(200)
	private View pro_line_right;//右边线
	private LinearLayout ll_title;
	private int careFlag = -1;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		SystemUtil.SetFullScreen(this);
		setContentView(R.layout.activity_promulgator_zone);
		author_id = getIntent().getExtras().getInt("author_id");
		if(author_id == -1 || author_id == 0){
			author_id = 1;
//			Toast.makeText(this, "author_id wrong", 0).show();
		}
		initView();
		getData(page,false,false);
		getComments(page);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private void initView(){
		pro_rl_root = (RelativeLayout) findViewById(R.id.pro_rl_root);
		waitting_layout = (RelativeLayout) findViewById(R.id.waitting_layout);
		waitting_layout.setVisibility(View.VISIBLE);
		mScrollView = (PullToZoomScrollView) findViewById(R.id.scroll_view);
		viewPager = (ViewPager) findViewById(R.id.pro_viewpager);
		pro_icon = (ImageView) findViewById(R.id.pro_icon);
		pro_back = (ImageView) findViewById(R.id.pro_back);
		iv_zoom = (ImageView) findViewById(R.id.iv_zoom);
		tv_pro_name = (TextView) findViewById(R.id.tv_pro_name);
		pro_guanzhu = (ImageView) findViewById(R.id.pro_guanzhu);
		ll_pro_left = (RelativeLayout) findViewById(R.id.ll_pro_left);
		ll_pro_right = (RelativeLayout) findViewById(R.id.ll_pro_right);
		pro_album = (TextView) findViewById(R.id.pro_album);
		pro_line_left = findViewById(R.id.pro_line_left);
		pro_comment = (TextView) findViewById(R.id.pro_comment);
		pro_line_right = findViewById(R.id.pro_line_right);
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		initViewPager();
//		getPagerHight();
		//左边专辑按钮
		ll_pro_left.setOnClickListener(this);
		//右边留言按钮
		ll_pro_right.setOnClickListener(this);
		//大图
		iv_zoom.setOnClickListener(this);
		pro_back.setOnClickListener(this);//返回键
		pro_guanzhu.setOnClickListener(this);//关注
//		ll_pro_left.setOnClickListener(this);//
//		ll_pro_right.setOnClickListener(this);
	}
	/**
	 * 获取viewpager的高度
	 */
	private void getPagerHight(){
		final int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		final int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		viewPager.measure(w, h);
		ViewTreeObserver vto = viewPager.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(
						this);
				View view = viewPager.getChildAt(viewPager.getCurrentItem());
				view.measure(w, h);
				LayoutParams params = new LayoutParams(
						android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				params.height = 
							( view.getMeasuredHeight() < ScreenUtil.dp2px(getApplicationContext(), 171) 
							? ScreenUtil.dp2px(getApplicationContext(), 171):view.getMeasuredHeight() ) + 10;
//				params.height = view.getMeasuredHeight()+10;
				params.addRule(RelativeLayout.BELOW, R.id.ll_title);
				viewPager.setLayoutParams(params);
			}
		});
	}
	
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		view1.dispatch(ev);
//		return super.dispatchTouchEvent(ev);
//	}
	
	// 初始化页卡内容
	private void initViewPager() {
		views = new ArrayList<Fragment>();
		view1 = new ProAlbumFragment();
		view2 = new ProCommentFragment();
		views.add(view1);
		views.add(view2);
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new PageChangeLisener());
	}
	
	public void getData(int page,final boolean refreshAlbum,final boolean refreshCom){
		waitting_layout.setVisibility(View.VISIBLE);
		if(SystemUtil.isNetOkWithToast(getApplicationContext())){
			NetWorkAPI.getAuthorinfo(getApplicationContext(), author_id, page, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof GetAuthorinfoResBean) {
						GetAuthorinfoResBean data = (GetAuthorinfoResBean) response;
//						System.out.println(data.toString());
						getAllData(data,refreshAlbum,refreshCom);
					}else{
						waitting_layout.setVisibility(View.GONE);
						showShortToast("获取数据失败");
					}
				}
				
				@Override
				public void onError(String errorMsg) {}
				
				@Override
				public void onCancel() {}
			});
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	
	public void getComments(int page){
		waitting_layout.setVisibility(View.VISIBLE);
		if(SystemUtil.isNetOkWithToast(getApplicationContext())){
			NetWorkAPI.getauthorcomment(mContext, author_id, page, new HttpCallback() {
				@Override
				public void onResult(NetResBean response) {
					if (response.success && response instanceof GetAuthorCommentResBean) {
						GetAuthorCommentResBean comBean = (GetAuthorCommentResBean) response;
						getAllComments(comBean);
					}else{
						waitting_layout.setVisibility(View.GONE);
						showShortToast("获取评论数据失败");
					}
				}
				@Override
				public void onError(String errorMsg) {}
				@Override
				public void onCancel() {}
			});
		}else{
			waitting_layout.setVisibility(View.GONE);
		}
	}
	private List<Comment> authorComments;//相关专辑
	private int initSize = 0;
	private int curCommentPage = -1;//当前评论页
	private int comment_page_total = -1;//评论总页数
	private void getAllComments(GetAuthorCommentResBean comBean){
		if (comBean != null) {
			authorComments = comBean.getComment();
			initSize += (authorComments == null ? 0 : authorComments.size()) ;
			curCommentPage = comBean.getPage();
			comment_page_total = comBean.getPage_total();
			setViewData(false, true);
		}else{
//			showShortToast("bean is null");
		}
	}
	/**
	 * 给view设置数据
	 */
	protected void setViewData(boolean refreshAlbum,boolean refreshCom) {
		waitting_layout.setVisibility(View.GONE);
		if(!refreshAlbum && !refreshCom){
			if (UserInfoManager.isLogin()) {
				if (is_care == 1) {
					pro_guanzhu.setBackgroundResource(R.drawable.per_yiguanzhu);
				}else{
					pro_guanzhu.setBackgroundResource(R.drawable.per_guanzhu);
				}
			}else{
				pro_guanzhu.setBackgroundResource(R.drawable.per_guanzhu);
			}
			if(author_icon != null && !author_icon.equals("")){
				ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(author_icon, pro_icon, 
						ImageLoaderUtil.getRoundedImageOptions());
			}
			if(author_name!=null && !author_name.equals("")){
				tv_pro_name.setText(author_name);
			}
			if (background != null && !background.equals("")) {
				ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(background, iv_zoom,R.drawable.user_bg);
			}else{
				iv_zoom.setBackgroundResource(R.drawable.user_bg);
			}
			if(specialnum != -1)
				pro_album.setText("专辑 ("+specialnum+")");
			if(comment_num != -1)
				pro_comment.setText("评论 ("+ (initSize+(authorComments == null ? 0 : authorComments.size()) +")") );
			if(authorspecial != null && authorspecial.size() > 0)
				view1.setList(authorspecial,curPage,page_total,this);
//			if(authorComments != null && authorComments.size() > 0)
//				view2.setList(authorComments,this);
		}else if(refreshAlbum){
			if(authorspecial != null && authorspecial.size() > 0) {
				view1.setList(authorspecial,curPage,page_total,this);
			}
		}else if(refreshCom){
			if(comment_num != -1){
//				pro_comment.setText("评论 ("+ (authorComments == null ? 0 : authorComments.size()) +")");
				pro_comment.setText("评论 ("+ initSize +")");
			}
			if(authorComments != null && authorComments.size() > 0)
				view2.setList(authorComments,curCommentPage,comment_page_total,this);
		}
	}
	/**
	 * Fragment的callback刷新数据
	 */
	public void callBack(){
		getPagerHight();
//		viewPager.invalidate();
	}
	/**********************************/
	private String author_icon;//作者头像
	private String author_name;//作者姓名
	private List<Special> authorspecial;//相关专辑
	private int comment_num = -1;//评论数量
	private int curPage = -1;//当前页
	private int page_total = -1;//总页数
	private int specialnum = -1;//相关专辑数
	private int is_care = -1;
	private String background = "";
	/**********************************/
	/**
	 * 解析bean,获取所有数据
	 * @param data
	 */
	private void getAllData(GetAuthorinfoResBean data,boolean refreshAlbum,boolean refreshCom) {
//		waitting_layout.setVisibility(View.GONE);
		if (data != null) {
			author_icon = data.getAuthor_icon();
			author_name = data.getAuthor_name();
			authorspecial = data.getAuthorspecial();
			comment_num = data.getComment_num();
			curPage = data.getPage();
			page_total = data.getPage_total();
			specialnum = data.getSpecialnum();
			is_care  = data.getIs_care();
			background = data.getBackdrop();
//			showShortToast("care > "+data.getIs_care());
			careFlag = is_care;
			setViewData(refreshAlbum ,refreshCom);
		}else{
//			Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_pro_left://左边专辑按钮
			if(currIndex != 0){
				viewPager.setCurrentItem(0);
			}
			break;
		case R.id.ll_pro_right://右边留言按钮
			if(currIndex != 1){
				viewPager.setCurrentItem(1);
			}
			break;
		case R.id.iv_zoom://换封面
			
			break;
		case R.id.pro_back://返回
			onBackPressed();
			break;
		case R.id.pro_guanzhu://关注
			if (UserInfoManager.isLogin()) {
				if (is_care == 1) {
					pro_guanzhu.setBackgroundResource(R.drawable.per_guanzhu);
					is_care = 2;
				}else{
					pro_guanzhu.setBackgroundResource(R.drawable.per_yiguanzhu);
					is_care = 1;
				}
			}else{
				Intent intent = new Intent(this,LoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}
	
	private void care() {
		if (author_id > 0) {
			if(SystemUtil.isNetOkWithToast(getApplicationContext())){
				NetWorkAPI.careauthor(getApplicationContext(), author_id, is_care,
					new HttpCallback() {

						@Override
						public void onResult(NetResBean response) {
							AlbumActivity act = AlbumActivity.getInstance();
							if (response.success && is_care == 1) {
//								showShortToast("已关注");
								if (act != null) {
									act.onRefresh();
								}
							} else if (response.success && is_care != 1) {
//								showShortToast("已取消关注");
								if (act != null) {
									act.onRefresh();
								}
							} else {
//								showShortToast("操作失败");
							}
						}

						@Override
						public void onError(String errorMsg) {
						}

						@Override
						public void onCancel() {}
					});
			}else{
				waitting_layout.setVisibility(View.GONE);
			}
		} else {
//			showShortToast("author id wrong");
		}
	}
	/**
	 * viewpager的适配器
	 * @author Administrator
	 *
	 */
	class ViewPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> list_views;

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
			list_views = views;
		}

		@Override
		public Fragment getItem(int arg0) {
			return list_views.get(arg0);
		}

		@Override
		public int getCount() {
			return list_views.size();
		}

	}
	/**
	 * 页卡切换监听
	 * @author Administrator
	 *
	 */
	class PageChangeLisener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
//			Animation animation = new TranslateAnimation(one * currIndex, one
//					* arg0, 0, 0);
			currIndex = arg0;
//			animation.setFillAfter(true);
//			animation.setDuration(300);
//			imageView.startAnimation(animation);
			if (currIndex == 1) {
				pro_line_right.setBackgroundColor(0xff333333);
				pro_line_left.setBackgroundColor(0xff999999);
			}else if(currIndex == 0){
				pro_line_right.setBackgroundColor(0xff999999);
				pro_line_left.setBackgroundColor(0xff333333);
			}
			getPagerHight();
//			viewPager.invalidate();
		}
	}
	
	@Override
	protected void onPause() {
		if (careFlag != is_care) {
			if (careFlag == 0) {//未关注时给服务器传2,服务器返回0.所以要区别判断.
				if (is_care != 0 && is_care != 2) {
					care();
				}
			}else{
				care();
			}
		}
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		pro_rl_root.removeAllViews();
		super.onDestroy();
	}
	
}
