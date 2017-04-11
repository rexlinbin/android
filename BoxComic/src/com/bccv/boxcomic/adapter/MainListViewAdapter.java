package com.bccv.boxcomic.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoActivity;
import com.bccv.boxcomic.activity.ComicInfoLocalActivity;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.MyGridView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MainListViewAdapter extends BaseAdapter{
	private List<Comic> data;
	private Context context;
	Activity activity;
	

	public MainListViewAdapter(List<Comic> data, Context context,
			Activity activity) {
		super();
		this.data = data;
		this.context = context;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		 if (convertView == null) {
		      holder = new ViewHolder();			
		      convertView = LayoutInflater.from(context).inflate(R.layout.fragment_main_childgridview, null, false);  
		      holder.gridView = (MyGridView ) convertView.findViewById(R.id.frament_main_gridView);
		      convertView.setTag(holder);
		    } else {
		      holder = (ViewHolder) convertView.getTag();
		    }
		
		
		 if (this.data != null) {
		   
		      if (holder.gridView != null&& arg0==0) {
		    	
		    	  MainGridviewAdapter adapter = new MainGridviewAdapter(data, context);
		        holder.gridView.setAdapter(adapter);
		        holder.gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		        holder.gridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
						Intent aintent=new Intent(activity,ComicInfoActivity.class);
						
						aintent.putExtra("mainitem", data.get(arg2).getComic_id());

						activity.startActivity(aintent);
						
					}
				});
		      }

		    }
		return convertView;
	}

public static class ViewHolder{
	MyGridView  gridView;
		
		
		
		
		
	}
		
	
	
}
