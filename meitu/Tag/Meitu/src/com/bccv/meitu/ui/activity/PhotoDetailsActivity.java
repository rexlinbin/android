package com.bccv.meitu.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bccv.meitu.sns.ShareSDK.ShareInfo;
import com.bccv.meitu.sns.ShareSDK.shareCallback;

import com.bccv.meitu.R;
import com.bccv.meitu.api.DownLoadAPI;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.common.GlobalConstants;
import com.bccv.meitu.model.Comment;
import com.bccv.meitu.model.GetSpecialpicResBean;
import com.bccv.meitu.model.PicInfo;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.sns.ShareSDK;
import com.bccv.meitu.ui.adapter.GalleryAdapter;
import com.bccv.meitu.utils.ScreenUtil;
import com.bccv.meitu.utils.SystemUtil;
import com.bccv.meitu.view.MyImageView;
import com.bccv.meitu.view.PicGallery;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

/**
 * 显示高清照片
 * @author ZhaoHaiyang
 * @since 2014年11月10日12:52:57
 */
public class PhotoDetailsActivity extends BaseActivity {
	// 屏幕宽度
	public static int screenWidth;
	// 屏幕高度
	public static int screenHeight;
	private PicGallery gallery;
	private GalleryAdapter mAdapter;
	private ImageView photo_back;
	private ImageView photo_download;
	private ImageView photo_share;
	private ImageView photo_danmu;
	private LinearLayout photo_ll_danmu;
	private String special_id;//id
	private String special_name;//专辑名称
	private String special_names;//分享语句
	private FrameLayout photo_title;
	private RelativeLayout rl_first_help;
	private ImageView iv_first_help;
	/****************弹幕*****************/
	private RelativeLayout rl_danmu;
	private LinearLayout ll_danmu_first;
	private ImageView iv_danmu_first;
	private TextView tv_danmu_first;
	private LinearLayout ll_danmu_first_two;
	private ImageView iv_danmu_first_two;
	private TextView tv_danmu_first_two;
	private LinearLayout ll_danmu_second;
	private ImageView iv_danmu_second;
	private TextView tv_danmu_second;
	private LinearLayout ll_danmu_third;
	private ImageView iv_danmu_third;
	private TextView tv_danmu_third;
	private final int REFRESH_VIEW = 1;
	/***********************************/
	public Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REFRESH_VIEW:
				int i = (Integer)msg.obj;
				if(commentsList.size() == 1){
					if(i== 1){
						setThirdView(0);
					}else if(i == 2){
						setSecondView(0);
					}else if(i == 3){
						setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,0);
					}else if(i == 4){
						setFirstViewDisappear(ll_danmu_first);
					}
				}else if(commentsList.size() == 2){
					if(i== 1){
						setThirdView(0);
					}else if(i == 2){
						setSecondView(0);
						setThirdView(1);
					}else if(i == 3){
						setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,0);
						setSecondView(1);
					}else if(i == 4){
						setFirstViewDisappear(ll_danmu_first);
						setFirstViewShow(ll_danmu_first_two,tv_danmu_first_two,iv_danmu_first_two,1);
					}else if(i == 5){
						setFirstViewDisappear(ll_danmu_first_two);
					}
				}else if(commentsList.size() == 3){
					if(i== 1){
						setThirdView(0);
					}else if(i == 2){
						setSecondView(0);
						setThirdView(1);
					}else if(i == 3){
						setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,0);
						setSecondView(1);
						setThirdView(2);
					}else if(i == 4){
						setFirstViewDisappear(ll_danmu_first);
						setFirstViewShow(ll_danmu_first_two,tv_danmu_first_two,iv_danmu_first_two,1);
						setSecondView(2);
					}else if(i == 5){
						setFirstViewDisappear(ll_danmu_first_two);
						setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,2);
					}else if(i == 6){
						setFirstViewDisappear(ll_danmu_first);
					}
				}else{
					if(i == 1){
						setThirdView(i-1);
					}else if(i == 2){
						setSecondView(i-2);
						setThirdView(i-1);
					}else if(i == 3){
						setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,i-3);
						setSecondView(i-2);
						setThirdView(i-1);
					}else if(i == commentsList.size()+1){
						if(i%2 == 0){
							setFirstViewDisappear(ll_danmu_first);
							setFirstViewShow(ll_danmu_first_two,tv_danmu_first_two,iv_danmu_first_two,i-3);
						}else{
							setFirstViewDisappear(ll_danmu_first_two);
							setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,i-3);
						}
						setSecondView(i-2);
					}else if(i == commentsList.size()+2){
						if(i%2 == 0){
							setFirstViewDisappear(ll_danmu_first);
							setFirstViewShow(ll_danmu_first_two,tv_danmu_first_two,iv_danmu_first_two,i-3);
						}else{
							setFirstViewDisappear(ll_danmu_first_two);
							setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,i-3);
						}
					}else if(i == commentsList.size()+3){
						if(i%2 == 0){
							setFirstViewDisappear(ll_danmu_first);
						}else{
							setFirstViewDisappear(ll_danmu_first_two);
						}
					}else if(i >= 4 && i <= commentsList.size()){
						if(i%2 == 0){
							setFirstViewDisappear(ll_danmu_first);
							setFirstViewShow(ll_danmu_first_two,tv_danmu_first_two,iv_danmu_first_two,i-3);
						}else{
							setFirstViewDisappear(ll_danmu_first_two);
							setFirstViewShow(ll_danmu_first,tv_danmu_first,iv_danmu_first,i-3);
						}
						setSecondView(i-2);
						setThirdView(i-1);
					}
				}
				break;
			default:
				break;
			}
		};
	};
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_photo_details);
		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();
		special_id = getIntent().getExtras().getString("special_id");
		special_name = getIntent().getExtras().getString("special_name");
		special_names = getIntent().getExtras().getString("special_names");
		if(special_id == null || special_id.equals("-1") || special_id.equals("0")){
			special_id = "1";
			showShortToast("special_id wrong");
		}
		initView();
		getData();
	}
	private void initView() {
		/******************danmu*******************/
		rl_danmu = (RelativeLayout) findViewById(R.id.rl_danmu);
		ll_danmu_first = (LinearLayout) findViewById(R.id.ll_danmu_first);
		iv_danmu_first = (ImageView) findViewById(R.id.iv_danmu_first);
		tv_danmu_first = (TextView) findViewById(R.id.tv_danmu_first);
		ll_danmu_first_two = (LinearLayout) findViewById(R.id.ll_danmu_first_two);
		iv_danmu_first_two = (ImageView) findViewById(R.id.iv_danmu_first_two);
		tv_danmu_first_two = (TextView) findViewById(R.id.tv_danmu_first_two);
		ll_danmu_second = (LinearLayout) findViewById(R.id.ll_danmu_second);
		iv_danmu_second = (ImageView) findViewById(R.id.iv_danmu_second);
		tv_danmu_second = (TextView) findViewById(R.id.tv_danmu_second);
		ll_danmu_third = (LinearLayout) findViewById(R.id.ll_danmu_third);
		iv_danmu_third = (ImageView) findViewById(R.id.iv_danmu_third);
		tv_danmu_third = (TextView) findViewById(R.id.tv_danmu_third);
		/******************************************/
		rl_first_help = (RelativeLayout) findViewById(R.id.help_first);
		iv_first_help = (ImageView) findViewById(R.id.iv_first_help);
		photo_title = (FrameLayout) findViewById(R.id.photo_title);
		photo_back = (ImageView) findViewById(R.id.photo_back);
		photo_download = (ImageView) findViewById(R.id.photo_download);
		photo_danmu = (ImageView) findViewById(R.id.photo_danmu);
		photo_ll_danmu = (LinearLayout) findViewById(R.id.photo_ll_danmu);
		photo_share = (ImageView) findViewById(R.id.photo_share);
		gallery = (PicGallery) findViewById(R.id.pic_gallery);
		photo_back.setOnClickListener(this);
		photo_download.setOnClickListener(this);
//		photo_danmu.setOnClickListener(this);
		photo_ll_danmu.setOnClickListener(this);
		photo_share.setOnClickListener(this);
		iv_first_help.setOnClickListener(this);
		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		gallery.setDetector(new GestureDetector(this,
				new MySimpleGesture()));
		mAdapter = new GalleryAdapter(this);
		gallery.setAdapter(mAdapter);
		gallery.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
//				showShortToast("长点击事件");
				return false;
			}
		});
	}
	private class MySimpleGesture extends SimpleOnGestureListener {
		// 按两下的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent e) {

			View view = gallery.getSelectedView();
			if (view instanceof MyImageView) {
				MyImageView imageView = (MyImageView) view;
				if (imageView.getScale() > imageView.getMiniZoom()) {
					imageView.zoomTo(imageView.getMiniZoom());
				} else {
					imageView.zoomTo(imageView.getMaxZoom());
				}

			}
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if(photo_title.isShown()){
				photo_title.setVisibility(View.GONE);
			}else{
				photo_title.setVisibility(View.VISIBLE);
			}
			return true;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.photo_back:
			onBackPressed();
			break;
		case R.id.photo_download://下载
			if(SystemUtil.isNetOkWithToast(getApplicationContext())){
				int curItem = mAdapter.getCurItem();
				if(curItem > -1){
					String url = picList.get(curItem).getPic();
					String name = url.substring(url.lastIndexOf("/")+1);
					DownLoadAPI.downLoadFile(url, name, true);
					showShortToast("正在下载");
				}else{
					showShortToast("下载出错");
				}
			}
			break;
		case R.id.photo_ll_danmu://弹幕
			if(rl_danmu.isShown()){
				rl_danmu.setVisibility(View.GONE);
				photo_danmu.setBackgroundResource(R.drawable.guanbidanmu);
			}else {
				rl_danmu.setVisibility(View.VISIBLE);
				photo_danmu.setBackgroundResource(R.drawable.danmu);
			}
			break;
		case R.id.photo_share://分享
			ShareInfo shareInfo = new ShareInfo();
			shareInfo.setContentString(special_names);
			shareInfo.setTitleString(special_name);
			shareInfo.setTagUrl(GlobalConstants.SHARE_URL+special_id);
			ShareSDK.showShare(this, photo_share, shareInfo, new shareCallback() {
				@Override
				public void callback(boolean success) {
					if (success) {
						//XXX :
					}
				}
			});
			break;
		case R.id.iv_first_help://帮助
			rl_first_help.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	private void getData(){
		if(SystemUtil.isNetOkWithToast(getApplicationContext())){
			NetWorkAPI.getSpecialpic(getApplicationContext(), Integer.valueOf(special_id), new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					GetSpecialpicResBean data = (GetSpecialpicResBean) response;
					getAllData(data);
					setViewData();
				}
				
				@Override
				public void onError(String errorMsg) {
				}
				
				@Override
				public void onCancel() {
				}
			});
		}else {
//			waitting_layout.setVisibility(View.GONE);
		}
	}
	protected void setViewData() {
		if(picList == null || picList.size() == 0){
			showShortToast("该专辑暂无图片");
			photo_title.setVisibility(View.VISIBLE);
			return ;
		}
		mAdapter.setImageUrlList(picList);
		photo_title.setVisibility(View.GONE);
		if(commentsList == null || commentsList.size() == 0){
			showShortToast("该专辑暂无评论");
//			commentsList = new ArrayList<Comment>();
//			Comment com1 = new Comment();
//			Comment com2 = new Comment();
//			Comment com3 = new Comment();
//			Comment com4 = new Comment();
//			Comment com5 = new Comment();
//			com1.setComments_content("哈哈哈");
//			com2.setComments_content("哈哈哈哈哈哈哈");
//			com3.setComments_content("你猜我有多评论???");
//			com4.setComments_content("你猜少条评论???");
//			com5.setComments_content("heiheiehi");
//			commentsList.add(com1);
//			commentsList.add(com2);
//			commentsList.add(com3);
//			commentsList.add(com4);
//			commentsList.add(com5);
			return ;
		}
		setTime();
	}
	private int picNum;
	private List<PicInfo> picList;
	private List<Comment> commentsList;
	protected void getAllData(GetSpecialpicResBean data) {
		picNum = data.getPic_num();
		picList = data.getList();
		commentsList = data.getComment();
	}
	
	
	Timer time = new Timer(true);
	private void setTime() {
		if(time == null ){
			time = new Timer(true);
		}
		TimerTask tt = new TimerTask() {
			int countTime = 0;
			int size = 0;
			public void run() {
				if(commentsList.size() == 0){
					return ;
				}else {
					//if(commentsList.size() >= 1 || commentsList.size() <= 3)
					size = commentsList.size()+3;
				}
				if (countTime < size) {
					countTime++;
				} else{
					return;
				}
				Message msg = new Message();
				msg.obj = countTime;
				msg.what = REFRESH_VIEW;
				mHandler.sendMessage(msg);
			}
		};
		time.schedule(tt, 1000, 2000);
	}
	
	private void setThirdView(int i){
		String comment = commentsList.get(i).getComments_content();
		AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
		animAlpha.setDuration(500);
		tv_danmu_third.setText(comment);
		if(commentsList.get(i).getUser_icon() == null || commentsList.get(i).getUser_icon().equals("")){
			iv_danmu_third.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_img_user_default));
		}else{
			ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(
					commentsList.get(i).getUser_icon(), iv_danmu_third, ImageLoaderUtil.getRoundedImageOptions());
		}
		ll_danmu_third.startAnimation(animAlpha);
		ll_danmu_third.setVisibility(View.VISIBLE);
	}
	
	private void setSecondView(int i){
		String comment = commentsList.get(i).getComments_content();
		int[] location = new int[2];
		int[] location2 = new int[2];
		ll_danmu_third.getLocationOnScreen(location);
		ll_danmu_second.getLocationOnScreen(location2);
		float y = location[1];
		float y2 = location2[1];
		float yA = y - y2;
		if(i >= 1){
			String comment1 = commentsList.get(i-1).getComments_content();
			if(comment.length() > 15 && comment1.length() > 15){
				yA = yA - ScreenUtil.dp2px(getApplicationContext(), 12);
			}
		}
		TranslateAnimation animTrans = new TranslateAnimation(
				0, 0, yA, 0);
		animTrans.setDuration(500);
		tv_danmu_second.setText(comment);
		if(commentsList.get(i).getUser_icon() == null || commentsList.get(i).getUser_icon().equals("")){
//			iv_danmu_third.setBackgroundResource(R.drawable.ic_img_user_default);
			iv_danmu_second.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_img_user_default));
		}else{
			ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(
				commentsList.get(i).getUser_icon(), iv_danmu_second, ImageLoaderUtil.getRoundedImageOptions());		
		}
		ll_danmu_second.startAnimation(animTrans);
		ll_danmu_second.setVisibility(View.VISIBLE);
		ll_danmu_third.setVisibility(View.INVISIBLE);
	}
	
	private void setFirstViewShow(View view,TextView tv,ImageView iv,int i){
		String comment = commentsList.get(i).getComments_content();
		int[] location2 = new int[2];
		int[] location3 = new int[2];
		ll_danmu_second.getLocationOnScreen(location2);
		view.getLocationOnScreen(location3);
		float y2 = location2[1];
		float y3 = location3[1];
		float yB = y2 - y3;
		if(i >= 1){
			String comment1 = commentsList.get(i-1).getComments_content();
			if(comment.length() > 15 && comment1.length() > 15){
				yB = yB - ScreenUtil.dp2px(getApplicationContext(), 12);
			}
		}
		TranslateAnimation animTrans2 = new TranslateAnimation(
				0, 0, yB, 0);
		animTrans2.setDuration(500);
		tv.setText(comment);
		if(commentsList.get(i).getUser_icon() == null || commentsList.get(i).getUser_icon().equals("")){
//			iv_danmu_third.setBackgroundResource(R.drawable.ic_img_user_default);
			iv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_img_user_default));
		}else{
			ImageLoaderUtil.getInstance(getApplicationContext()).displayImage(
				commentsList.get(i).getUser_icon(), iv, ImageLoaderUtil.getRoundedImageOptions());
		}
		view.startAnimation(animTrans2);
		view.setVisibility(View.VISIBLE);
		ll_danmu_second.setVisibility(View.INVISIBLE);
	}
	
	private void setFirstViewDisappear(View view){
		AlphaAnimation animAlpha = new AlphaAnimation(1, 0);
		animAlpha.setDuration(500);
		view.startAnimation(animAlpha);
		view.setVisibility(View.INVISIBLE);
	}
}
