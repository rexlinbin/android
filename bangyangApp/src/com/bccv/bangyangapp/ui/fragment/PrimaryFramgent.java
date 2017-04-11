package com.bccv.bangyangapp.ui.fragment;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;


public abstract class PrimaryFramgent extends Fragment {

	public abstract void onMenuTransform(float percentOpen);
	
	public abstract void onMenuOpened();
	
	public abstract void onMenuClosed();
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
}
