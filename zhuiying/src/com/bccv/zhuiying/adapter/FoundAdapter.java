package com.bccv.zhuiying.adapter;

import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.Found;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class FoundAdapter extends BaseAdapter {

	private List<Found> data;
	private Context context;

	public FoundAdapter(List<Found> data, Context context) {
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
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder viewHolder = null;

		if (view == null) {

			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.griditem_foundlist, null);

			viewHolder.image = (ImageView) view.findViewById(R.id.found_item_image);
			viewHolder.typeText = (TextView) view.findViewById(R.id.found_item_typeText);

			view.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) view.getTag();

		}

		try {
			Found item = data.get(position);

			viewHolder.typeText.setText(item.getTitle());

			String images = (String) viewHolder.image.getTag();
			if (images != null && images.equals(item.getImages())) {
				// viewHolder.imageView.setTag(item.getImages());
				// ImageLoader imageLoader = ImageLoader.getInstance();
				// imageLoader.displayImage(item.getImages(),
				// viewHolder.imageView, GlobalParams.movieOptions);
			} else {
				viewHolder.image.setTag(item.getImages());

				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(item.getImages(), viewHolder.image, GlobalParams.foundOptions);

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.toString();
		}

		return view;
	}

	class ViewHolder {

		TextView typeText;
		ImageView image;

	}

}
