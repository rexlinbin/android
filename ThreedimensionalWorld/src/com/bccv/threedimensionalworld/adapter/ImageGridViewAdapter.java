package com.bccv.threedimensionalworld.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Gallery.LayoutParams;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.activity.MusicActivity;
import com.bccv.threedimensionalworld.adapter.GralleryAdapter.Viewholder;
import com.bccv.threedimensionalworld.model.PictureBean;
import com.bccv.threedimensionalworld.tool.FileUtils;
import com.bccv.threedimensionalworld.tool.GlobalParams;
import com.bccv.threedimensionalworld.tool.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageGridViewAdapter extends BaseAdapter {
	private Context context;
	private List<PictureBean> data;
	private String path;
	private int selectNum = 0;

	public ImageGridViewAdapter(Context context, List<PictureBean> data) {
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
		Viewholder viewHolder = null;
		View view = null;
		PictureBean item = data.get(arg0);
		if (view == null) {
			viewHolder = new Viewholder();
			view = LayoutInflater.from(context).inflate(R.layout.item_image,
					null);
			viewHolder.image = (ImageView) view.findViewById(R.id.imageView);
			viewHolder.image1 = (ImageView) view.findViewById(R.id.imageView1);

		} else {
			viewHolder = (Viewholder) view.getTag();
		}

		ImageLoader imageLoader;

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		imageLoader.displayImage("file://" + item.getPicPath(),
				viewHolder.image);

		if (arg0 == selectNum) {
			viewHolder.image1.setSelected(true);

		} else {
			viewHolder.image1.setSelected(false);

		}

		return view;
	}

	class Viewholder {

		ImageView image;
		ImageView image1;
	}
}
