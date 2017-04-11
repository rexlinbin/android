package com.bccv.bangyangapp.ui.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.bccv.bangyangapp.ApplicationManager;
import com.bccv.bangyangapp.R;
import com.bccv.bangyangapp.common.GlobalConstants;
import com.bccv.bangyangapp.sns.UserInfoManager;
import com.bccv.bangyangapp.ui.fragment.ClassifcationFramgent;
import com.bccv.bangyangapp.ui.fragment.CollectionFramgent;
import com.bccv.bangyangapp.ui.fragment.CommentFramgent;
import com.bccv.bangyangapp.ui.fragment.HomePageFramgent;
import com.bccv.bangyangapp.ui.fragment.MyAppFramgent;
import com.bccv.bangyangapp.ui.fragment.PrimaryFramgent;
import com.bccv.bangyangapp.ui.fragment.QAFramgent;
import com.bccv.bangyangapp.ui.fragment.TopHotFramgent;
import com.bccv.bangyangapp.ui.view.BackGroundView;
import com.bccv.bangyangapp.ui.view.LeftMenuView;
import com.bccv.bangyangapp.utils.ExitUtils;
import com.bccv.bangyangapp.utils.L;
import com.igexin.sdk.PushManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class MainActivity extends BaseFragmentActivity {

	public static final int PAGE_ID_HOME = 1;			//首页
	public static final int PAGE_ID_CLASSIFICATION = 2;	//分类
	public static final int PAGE_ID_HOT = 3;			//最新最热
	public static final int PAGE_ID_QUESTION = 4;		//问答
	public static final int PAGE_ID_MYAPP = 5;			//我的应用
	public static final int PAGE_ID_COMMENT = 6;		//评论
	public static final int PAGE_ID_COLLECTION = 7;		//收藏
	
	public static final int SHOW_SEARCH_MSG = 1;	//打开搜索页面
	public static final int SHOW_SETTING_MSG = 2;	//打开设置页面 
	public static final int SHOW_PERSONAL_MSG = 3;	//打开个人页面 
	
	public static BackGroundView backGroundView;
	private LeftMenuView leftView;
	private FrameLayout conutView;
	private SlidingMenu menu;
	private PrimaryFramgent countFragment;
	
	private Handler mHandler;
	private FragmentManager fm;
	private PrimaryFramgent homePageFragment,classifcationFramgent,
							topHotFramgent,qaFramgent,myAppFramgent,
							commentFramgent,collectionFramgent;
	
	private int currentPageID;
	
	
	private BroadcastReceiver infoChangeBroadcastReceiver;
	private IntentFilter infoChangeIntentFilter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PushManager.getInstance().initialize(this.getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initHandler();
		initView();
		initBroadcastReceiver();
	}

	@SuppressLint("HandlerLeak")
	private void initHandler(){
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SHOW_PERSONAL_MSG:
//					Intent personalIntent = new Intent(MainActivity.this, PersonalActivity.class);
//					personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
//					startActivity(personalIntent);
//					overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
					Intent personalZoneIntent = new Intent(MainActivity.this, PersonalZoneActivity.class);
					personalZoneIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
					personalZoneIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY, String.valueOf(UserInfoManager.getUserId()));
					startActivity(personalZoneIntent);
//					overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
					break;
				case SHOW_SEARCH_MSG:
					Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
					searchIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
					startActivity(searchIntent);
					overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
					break;
				case SHOW_SETTING_MSG:
					Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
					settingIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
					startActivity(settingIntent);
					overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
					break;

				default:
					break;
				}
			}
			
		};
	}
	
	/**
	 * 初始化View
	 */
	private void initView() {

		backGroundView = (BackGroundView) findViewById(R.id.background_view);
		
		leftView = new LeftMenuView(this);
		conutView = (FrameLayout) View.inflate(mContext, R.layout.home_count_container, null);
		
		menu = (SlidingMenu) findViewById(R.id.slidingmenu_layout);

		// SlidingMenu.TOUCHMODE_FULLSCREEN 在屏幕任何地方滑动均可触发侧滑菜单
		// SlidingMenu.TOUCHMODE_MARGIN 在屏幕边缘地方（左边或者右边）滑动才可触发侧滑菜单
		// SlidingMenu.TOUCHMODE_NONE 在屏幕任何地方滑动均无法触发侧滑菜单
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setMode(SlidingMenu.LEFT);
		menu.setShadowWidth(10);
//		menu.setShadowDrawable(R.drawable.left_menu_shadow);//设置过度shadow
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setBehindWidth(GlobalConstants.screenPix.widthPixels/4*3);//设置SlidingMenu菜单的宽度
		menu.setBehindCanvasTransformer(new MyCanvasTransformer());
		menu.setBehindScrollScale(1);
		menu.setFadeEnabled(false); //渐隐渐显
		menu.setMenu(leftView.mView);
		menu.setContent(conutView);
		
		// menu.setSecondaryMenu(rightView.mView);
		// menu.setSecondaryShadowDrawable(R.drawable.left_menu_shadow);
		// menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		menu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				L.v(TAG, "onClosed()", " menu is closed !!! ");
				countFragment.onMenuClosed();
			}
		});

		menu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				L.v(TAG, "onOpened()", " menu is onOpened !!! ");
				countFragment.onMenuOpened();

			}
		});

		fm = getSupportFragmentManager();
		setContent(PAGE_ID_HOME);
		showContent();

	}
	
	private void initBroadcastReceiver(){
		
		if(infoChangeIntentFilter == null){
			infoChangeBroadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					String action = intent.getAction();
					if(GlobalConstants.USER_INFO_CHANGE_BROADCAST.equals(action)){
						int actionCode = intent.getIntExtra(GlobalConstants.USER_INFO_CHANGE_TYPE, 0);
						if(actionCode == GlobalConstants.LOGIN_SUCCESS){
							
							if(commentFramgent!=null){
								((CommentFramgent)commentFramgent).refreshData();
							}
							if(collectionFramgent!=null){
								((CollectionFramgent)collectionFramgent).refreshData();
							}
						}else if(actionCode == GlobalConstants.LOGOUT_SUCCESS){
							if(collectionFramgent!=null){
								((CollectionFramgent)collectionFramgent).refreshData();
							}
						}
						leftView.initData();
					}
				}
			};
			infoChangeIntentFilter = new IntentFilter(GlobalConstants.USER_INFO_CHANGE_BROADCAST);
			registerReceiver(infoChangeBroadcastReceiver, infoChangeIntentFilter);
		}
		
	}
	
	
	public void setContent(int pageID) {
		
		//TODO 回收View资源
		if(currentPageID == pageID){
			showContent();
			return;
		}
		currentPageID = pageID;
		countFragment = createCountPage(currentPageID);
		changeCountView();
		showContent();
	}
	
	private PrimaryFramgent createCountPage(int pageID) {
		PrimaryFramgent fragment = null;
		
		switch (pageID) {
		case PAGE_ID_HOME:	//首页
			if(homePageFragment==null){
				homePageFragment = new HomePageFramgent();
			}
			fragment = homePageFragment;
			break;
		case PAGE_ID_CLASSIFICATION:	//分类
			if(classifcationFramgent==null){
				classifcationFramgent = new ClassifcationFramgent();
			}
			fragment = classifcationFramgent;
			break;
		case PAGE_ID_HOT:	//最新最热
			if(topHotFramgent==null){
				topHotFramgent = new TopHotFramgent();
			}
			fragment = topHotFramgent;
			break;
		case PAGE_ID_QUESTION:	//疑问答疑
			if(qaFramgent==null){
				qaFramgent = new QAFramgent();
			}
			fragment = qaFramgent;
			break;
		case PAGE_ID_MYAPP:	//我的应用
			if(myAppFramgent==null){
				myAppFramgent = new MyAppFramgent();
			}
			fragment = myAppFramgent;
			break;
		case PAGE_ID_COMMENT:	//我的评论
			if(commentFramgent==null){
				commentFramgent = new CommentFramgent();
			}
			fragment = commentFramgent;
			break;
		case PAGE_ID_COLLECTION:	//我的收藏
			if(collectionFramgent==null){
				collectionFramgent = new CollectionFramgent();
			}
			fragment = collectionFramgent;
			break;

		default:
			break;
		}

		return fragment;
	}
	
	private FragmentTransaction fragmentTransaction;
	private void changeCountView(){
		fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container,countFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	
	public void showContent() {
		if (menu != null) {
			menu.showContent();
		}
	}

	public void showLeftMenu() {
		if (menu != null) {
			menu.showMenu();
		}
	}
	
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.common_title_menu_btn:
			//TODO 打开/关闭 menu
			showLeftMenu();
			break;
		case R.id.common_title_right_btn:
			//TODO 跳转上传页面
			Intent appReleaseIntent = new Intent(MainActivity.this, AppReleaseActivity.class);
			appReleaseIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
			startActivity(appReleaseIntent);
			overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_none);
			
			break;
		case R.id.menu_head:
			//TODO 跳转个人页
			if(UserInfoManager.isLogin()){
				Message personalMsg = mHandler.obtainMessage();
				personalMsg.what = SHOW_PERSONAL_MSG;
				mHandler.sendMessageDelayed(personalMsg, 0);
			}else{
				Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
				loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
				startActivity(loginIntent);
			}
			
			break;
		case R.id.menu_home:
			setContent(PAGE_ID_HOME);
			
			break;
		case R.id.menu_classification:
			setContent(PAGE_ID_CLASSIFICATION);
			
			break;
		case R.id.menu_hot:
			setContent(PAGE_ID_HOT);
			
			break;
		case R.id.menu_question:
			//TODO 疑问答疑
			setContent(PAGE_ID_QUESTION);
			
			break;
		case R.id.menu_myApp:
			//TODO 我的应用
			setContent(PAGE_ID_MYAPP);
			
			break;
		case R.id.menu_comment:
			//TODO 我的评论
			if(UserInfoManager.isLogin()){
				setContent(PAGE_ID_COMMENT);
			}else{
				Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
				loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
				startActivity(loginIntent);
			}
			
			break;
		case R.id.menu_collection:
			//TODO 我的收藏
//			if(UserInfoManager.isLogin()){
				setContent(PAGE_ID_COLLECTION);
//			}else{
//				Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//				loginIntent.putExtra(BackGroundView.BACKGROUND_COLOR, backGroundView.getGradientColor());
//				startActivity(loginIntent);
//			}
			
			break;
		case R.id.menu_search:
			//TODO 搜索
//			showContent();
			Message searchMsg = mHandler.obtainMessage();
			searchMsg.what = SHOW_SEARCH_MSG;
			mHandler.sendMessageDelayed(searchMsg, 0);
			break;
		case R.id.menu_setting:
			//TODO 设置
			Message settingMsg = mHandler.obtainMessage();
			settingMsg.what = SHOW_SETTING_MSG;
			mHandler.sendMessageDelayed(settingMsg, 0);
			
			break;

		default:
			break;
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(menu.isMenuShowing() && keyCode == KeyEvent.KEYCODE_BACK){
			showContent();
			return true;
		}else if(!menu.isMenuShowing() && keyCode == KeyEvent.KEYCODE_MENU){
			showLeftMenu();
			return true;
		}
		
		if(countFragment.onKeyDown(keyCode, event)){
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (ExitUtils.isExit(this)) {
				ApplicationManager.getInstance().exitSystem();
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		
		if(infoChangeIntentFilter!=null){
			unregisterReceiver(infoChangeBroadcastReceiver);
			infoChangeIntentFilter=null;
		}
		super.onDestroy();
	}
	
	
	private class MyCanvasTransformer implements CanvasTransformer {

		@Override
		public void transformCanvas(Canvas canvas, float percentOpen) {
			// TODO Auto-generated method stub
			float scale = (float) (percentOpen * 0.25 + 0.75);
			canvas.scale(scale, scale, canvas.getWidth() / 2,
					canvas.getHeight() / 2);

			if(percentOpen<0.01){
				percentOpen = 0;
			}
			countFragment.onMenuTransform(1.0f - percentOpen);
		}

	}
}
