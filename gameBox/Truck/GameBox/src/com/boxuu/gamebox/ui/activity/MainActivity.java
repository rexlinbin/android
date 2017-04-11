package com.boxuu.gamebox.ui.activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;

import com.boxuu.gamebox.ApplicationManager;
import com.boxuu.gamebox.R;
import com.boxuu.gamebox.ui.menupages.HomeContentView;
import com.boxuu.gamebox.ui.menupages.LeftMenuView;
import com.boxuu.gamebox.ui.menupages.MenuPage;
import com.boxuu.gamebox.utils.ExitUtils;
import com.boxuu.gamebox.utils.L;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;

public class MainActivity extends BaseFragmentActivity {

	public static final int PAGE_ID_HOME = 1;

	// public RightMenuView rightView;
	public LeftMenuView leftView;
	public SlidingMenu menu;
	private MenuPage menuPage;
	
	public static int currentPage = PAGE_ID_HOME;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		

		leftView = new LeftMenuView(this);

		menu = (SlidingMenu) findViewById(R.id.slidingmenulayout);

		// SlidingMenu.TOUCHMODE_FULLSCREEN 在屏幕任何地方滑动均可触发侧滑菜单
		// SlidingMenu.TOUCHMODE_MARGIN 在屏幕边缘地方（左边或者右边）滑动才可触发侧滑菜单
		// SlidingMenu.TOUCHMODE_NONE 在屏幕任何地方滑动均无法触发侧滑菜单
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setMode(SlidingMenu.LEFT);
		menu.setShadowWidth(10);
		menu.setShadowDrawable(R.drawable.left_menu_shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.setMenu(leftView.mView);
//		menu.setBehindWidth(520);//设置SlidingMenu菜单的宽度
		menu.setBehindCanvasTransformer(new MyCanvasTransformer());
		
		
		// menu.setSecondaryMenu(rightView.mView);
		// menu.setSecondaryShadowDrawable(R.drawable.left_menu_shadow);
		// menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		menu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				L.v(TAG, "onClosed()", " menu is closed !!! ");

			}
		});
		
		menu.setOnOpenedListener(new OnOpenedListener() {

			@Override
			public void onOpened() {
				L.v(TAG, "onOpened()", " menu is onOpened !!! ");

			}
		});

		setContent(currentPage);
		showContent();
	}

	public void setContent(int pageID) {
		if (menuPage != null) {
			menuPage.onPause();
			menuPage.onStop();
			menuPage.onDestroy();
			menuPage.mView = null;
			menuPage = null;
			System.gc();
		}

		menuPage = createViewPage(pageID);
		if (menuPage != null) {
			menuPage.onStart();
			menuPage.onResume();
			menu.setContent(menuPage.mView);
		}
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

	private MenuPage createViewPage(int pageID) {
		MenuPage menuPage = null;
		switch (pageID) {
		case PAGE_ID_HOME:
			menuPage = new HomeContentView(this, pageID);
			break;

		default:
			break;
		}

		return menuPage;
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left_menu:
			if (menu.isMenuShowing()) {
				showContent();
			} else {
				showLeftMenu();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (menuPage != null) {
			menuPage.onStart();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (menuPage != null) {
			menuPage.onResume();
		}
	}

	@Override
	protected void onPause() {
		if (menuPage != null) {
			menuPage.onPause();
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		if (menuPage != null) {
			menuPage.onStop();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (menuPage != null) {
			menuPage.onDestroy();
		}
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		if(ExitUtils.isExit(this)){
			ApplicationManager.getInstance().exitSystem();
		}
	}

	private class MyCanvasTransformer implements CanvasTransformer{

		@Override
		public void transformCanvas(Canvas canvas, float percentOpen) {
			// TODO Auto-generated method stub
			float scale = (float) (percentOpen*0.25 + 0.75);
			canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);
			
			System.out.println( "  percentOpen :  " + percentOpen);
			
		}
		
	}
	
}
