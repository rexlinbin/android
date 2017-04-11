package com.boxuu.gamebox.ui.menupages;

import android.view.View;
import android.view.View.OnClickListener;

import com.boxuu.gamebox.ui.activity.MainActivity;
import com.boxuu.gamebox.utils.L;

public abstract class MenuPage implements OnClickListener{

	public static final String TAG = "MenuPage";
	public View mView;
	
	public void onStart() {
	}

	public void onPause() {
	}

	public void onResume() {
	}

	public void onStop() {
	};

	public void onDestroy() {
	};
	
	
	public MenuPage(MainActivity mainActivity, int pageid) {
		L.v(TAG, "ViewControl", " pageid : >>" + pageid);
		
		
	}
	
	
	public final View findViewById(int id) {
		if (mView == null)
			return null;
		return mView.findViewById(id);
	}
	
}
