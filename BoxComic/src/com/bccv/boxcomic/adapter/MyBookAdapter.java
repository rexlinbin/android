package com.bccv.boxcomic.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.ebook.BookInfo;
import com.bccv.boxcomic.tool.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyBookAdapter extends BaseAdapter {
	private List<BookInfo> data;
	private Context context;

	public MyBookAdapter(List<BookInfo> data, Context context) {
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
		ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(
					R.layout.fragment_home_item, null);

			viewHolder.title = (TextView) view
					.findViewById(R.id.frament_home_item_titleTag);
			viewHolder.update = (TextView) view
					.findViewById(R.id.frament_home_item_updateTAg);
			viewHolder.imge = (ImageView) view
					.findViewById(R.id.frament_home_item_image);

			view.setTag(viewHolder);

		} else {

			viewHolder = (ViewHolder) view.getTag();

		}
try {
	BookInfo item = data.get(arg0);

	viewHolder.title.setText(item.getBook_title());

	if(Integer.parseInt(item.getBook_finish())==1){
		
		viewHolder.update.setText("已完结");
	}else{
		
		viewHolder.update.setText("更新至" + item.getBook_last_chaptitle());
	}
	
	

	ImageLoader imageLoader = ImageLoader.getInstance();
	imageLoader.init(ImageLoaderConfiguration.createDefault(context));
	String url = GlobalParams.BookImagepic + item.getBook_titlepic();
	imageLoader.displayImage(url, viewHolder.imge,
			GlobalParams.frameHomeoptions);
} catch (Exception e) {
	// TODO: handle exception
}
	

		return view;
	}

	public static class ViewHolder {

		private TextView update, title;
		private ImageView imge;

	}
}
