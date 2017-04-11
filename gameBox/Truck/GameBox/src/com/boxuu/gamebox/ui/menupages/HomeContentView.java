package com.boxuu.gamebox.ui.menupages;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boxuu.gamebox.R;
import com.boxuu.gamebox.ui.activity.MainActivity;
import com.boxuu.gamebox.ui.fragment.OfflineFragment;
import com.boxuu.gamebox.ui.fragment.OnlineFragment;
import com.boxuu.gamebox.ui.view.CheackableButton;

public class HomeContentView extends MenuPage {

	private static final int ONLINE = 1;
	private static final int OUTLINE = 2;
	
	private static final int RIGHTBTN_NORMAL_STATE = 1;
	private static final int RIGHTBTN_EDIT_STATE = 2;

	private MainActivity mMainActivity;
	private Context mContext;
	private Handler mHandler;

	private View left_menu;
	private TextView right_btn;
	private CheackableButton online_btn, outline_btn;
//	private FrameLayout container;

	private FragmentManager fm;
	private Fragment online;
	private OfflineFragment outline;

	private int currentShowingPage;

	private int currentBtnState = RIGHTBTN_NORMAL_STATE;
	
	public HomeContentView(MainActivity mainActivity, int pageid) {
		super(mainActivity, pageid);
		this.mMainActivity = mainActivity;
		this.mContext = mMainActivity.getApplicationContext();
		initData();
		initView();
	}

	private void initView() {
		// home_container_page
		mView = LayoutInflater.from(mContext).inflate(
				R.layout.home_container_page, null);
		
		right_btn = (TextView) findViewById(R.id.right_btn);
		right_btn.setOnClickListener(this);

		left_menu = findViewById(R.id.left_menu);
		left_menu.setOnClickListener(mMainActivity);

		online_btn = (CheackableButton) findViewById(R.id.online_btn);
		outline_btn = (CheackableButton) findViewById(R.id.outline_btn);

		online_btn.setOnClickListener(this);
		outline_btn.setOnClickListener(this);

//		container = (FrameLayout) findViewById(R.id.fragment_container);
		
		// 初始化 显示在线页面
		changeView(ONLINE);
		online_btn.setChecked(true);
		outline_btn.setChecked(false);

	}

	private void initData() {
		fm = mMainActivity.getSupportFragmentManager();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.right_btn:
			//TODO  点击编辑按钮逻辑
			if(currentBtnState == RIGHTBTN_NORMAL_STATE){
				//TODO 改为编辑状态
				currentBtnState = RIGHTBTN_EDIT_STATE;
				right_btn.setText("完成");
				
				outline.notifyAdapter(true);
				
			}else{
				//TODO 编辑状态恢复到正常状态
				currentBtnState = RIGHTBTN_NORMAL_STATE;
				right_btn.setText("编辑");
				
				outline.notifyAdapter(false);
			}
			
			break;
		case R.id.online_btn:
			changeView(ONLINE);
			online_btn.setChecked(true);
			outline_btn.setChecked(false);
			break;
		case R.id.outline_btn:
			changeView(OUTLINE);
			outline_btn.setChecked(true);
			online_btn.setChecked(false);
			break;

		default:
			break;
		}

	}

	private void changeView(int showingID) {

		if (currentShowingPage != showingID) {
			FragmentTransaction transaction = fm.beginTransaction(); 
			hideFragements(transaction);
			switch (showingID) {
			case ONLINE:
				if(online==null){
					online = OnlineFragment.newInstance();
					transaction.add(R.id.fragment_container, online);
				}else{
					transaction.show(online);
				}
				if(currentBtnState == RIGHTBTN_EDIT_STATE){
					right_btn.setText("编辑");
					//TODO 改为正常状态
					outline.notifyAdapter(false);
				}
				
				
				right_btn.setVisibility(View.GONE);
				break;
			case OUTLINE:
				if(outline==null){
					outline = new OfflineFragment();
					transaction.add(R.id.fragment_container, outline);
				}else{
					transaction.show(outline);
				}
				right_btn.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
			transaction.commit();
		}
	}

	private void hideFragements(FragmentTransaction transaction){
		if(online!=null){
			transaction.hide(online);
		}
		if(outline!=null){
			transaction.hide(outline);
		}
	}
	
}
