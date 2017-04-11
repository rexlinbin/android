package com.bccv.tianji.adapter;

import java.util.List;

import com.bccv.tianji.R;
import com.bccv.tianji.adapter.MainAdapter.ViewHolder;
import com.bccv.tianji.model.Gameinfo;
import com.bccv.tianji.model.ImageIcon;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class IntroductionAdapter extends BaseAdapter{

	private List<String>data;
	private Context context;
	
	

	public IntroductionAdapter(List<String> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 4;
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

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		
		
		if (view == null) {
			viewHolder = new ViewHolder();
		view = LayoutInflater.from(context).inflate(R.layout.fragment_introduction_item,null);	
		
		
		viewHolder.image=(ImageView) view.findViewById(R.id.introduction_item_image);
		
		
		}else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		
	try {
		imageLoader.displayImage(data.get(arg0), viewHolder.image,GlobalParams.Intropic);

	} catch (Exception e) {
		// TODO: handle exception
	}
		
	
		
		return view;
	}

	
	class ViewHolder{
		
		ImageView image;
	
	}
	
	
	
	
	
	
	
	
	
	
}
