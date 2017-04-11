package com.bccv.threedimensionalworld.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.model.PictureBean;

public class GralleryAdapter extends PagerAdapter {

	private List<PictureBean> data;
	private Context context;

	public GralleryAdapter(List<PictureBean> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup arg, int position) {

	Viewholder viewHolder = null;
		View view = null;
	if (view == null) {

			view = LayoutInflater.from(context).inflate(R.layout.item_gallery, arg, false);
	viewHolder = new Viewholder();
	viewHolder. im = (ImageView) view
					.findViewById(R.id.gallery_item_image);

		view.setTag(viewHolder);

	} else {
		viewHolder = (Viewholder) view.getTag();
	}
		PictureBean item = data.get(position);
		Bitmap bit = BitmapFactory.decodeFile(item.getPicPath());
		Drawable drawable = new BitmapDrawable(bit);
		viewHolder.im.setBackgroundDrawable(drawable);
		
		
//		viewHolder.im.setBackgroundResource(R.drawable.general_bg);
//
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);

		LayoutParams params = new LayoutParams(wm.getDefaultDisplay()
				.getWidth(), wm.getDefaultDisplay().getHeight());

		// params.width = dm.widthPixels;
		view.setLayoutParams(params);
		 arg.addView(view, 0);
		return view;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub

		return view == object;

	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	class Viewholder {

		ImageView im;

	}

}
