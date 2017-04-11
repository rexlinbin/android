package com.bccv.threedimensionalworld.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;



import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.model.Picture;

public class AppAdapter extends BaseAdapter {

	private List<Picture> data;
	private Context context;
	private int selectNum = 0;
	public AppAdapter(List<Picture> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	public void setSelect(int selectNum) {
		if (selectNum != this.selectNum) {
			this.selectNum = selectNum;
			notifyDataSetChanged();
		}
	}
	@SuppressLint("NewApi") @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

		View view = LayoutInflater.from(context).inflate(R.layout.item_app,
				null);

		ImageView im = (ImageView) view.findViewById(R.id.app_item_image);
		
	LinearLayout layout=(LinearLayout) view.findViewById(R.id.app_item_linear);

		if (arg0 == selectNum) {
			
			LayoutParams params = (LayoutParams) layout.getLayoutParams();
			params.leftMargin = params.leftMargin + 1;
			layout.setLayoutParams(params);
		} else {
			LayoutParams params = (LayoutParams) layout.getLayoutParams();
			params.leftMargin = params.leftMargin - 1;
			layout.setLayoutParams(params);
		}

		
		if (arg0 == 0) {

			im.setBackgroundResource(R.drawable.app_video);

		} else if (arg0 == 1) {
			im.setBackgroundResource(R.drawable.app_music);

		} else if (arg0 == 2) {
			im.setBackgroundResource(R.drawable.app_life);
		} else if (arg0 == 3) {
			im.setBackgroundResource(R.drawable.app_child);
		} else if (arg0 == 4) {
			im.setBackgroundResource(R.drawable.app_ebook);
		} else if (arg0 == 5) {
			im.setBackgroundResource(R.drawable.app_education);
		} else if (arg0 == 6) {
			im.setBackgroundResource(R.drawable.app_health);
		} else if (arg0 == 7) {
			im.setBackgroundResource(R.drawable.app_news);
		}

		return view;
	}

}
