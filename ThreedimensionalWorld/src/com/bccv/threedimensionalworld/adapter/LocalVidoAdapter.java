package com.bccv.threedimensionalworld.adapter;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.model.Movie;

public class LocalVidoAdapter extends BaseAdapter {
	private Context context;
	private List<Movie> data;
	private String path;
	private int selectNum = 0;
	Handler handler = new Handler();
	Viewholder viewHolder = null;

	public LocalVidoAdapter(Context context, List<Movie> data) {
		super();
		this.context = context;
		this.data = data;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
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

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

		View view = null;
		final Movie item = data.get(arg0);
		if (view == null) {
			viewHolder = new Viewholder();
			view = LayoutInflater.from(context).inflate(
					R.layout.item_localvideo, null);
			viewHolder.image = (ImageView) view
					.findViewById(R.id.localvideo_imageView);
			viewHolder.image1 = (ImageView) view
					.findViewById(R.id.localvideo_imageView1);
			viewHolder.name = (TextView) view
					.findViewById(R.id.localvideo_item_name);
		} else {
			viewHolder = (Viewholder) view.getTag();
		}

		viewHolder.name.setText(item.getTitle());
		if(item.getBit()!=null){
			viewHolder.image.setImageBitmap(item.getBit());
		}
		else{
			
			viewHolder.image.setImageResource(R.drawable.movielist_image);
			
				}
		if (arg0 == selectNum) {
			viewHolder.image1.setSelected(true);

		} else {
			viewHolder.image1.setSelected(false);

		}


		
		
		
		
		
		
		
		
		
		
		
		
		
		return view;
	}


}

class Viewholder {

	ImageView image;
	ImageView image1;
	TextView name;
}
