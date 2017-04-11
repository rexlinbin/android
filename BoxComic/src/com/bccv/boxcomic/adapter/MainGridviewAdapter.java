package com.bccv.boxcomic.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.DimensionPixelUtil;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainGridviewAdapter extends BaseAdapter {
	private List<Comic> data;
	private Context context;
	private int type;

	public void Sate(int type) {

		this.type = type;

	}

	public MainGridviewAdapter(List<Comic> data, Context context) {
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
		ViewHolder viewholder;

		if (view == null) {

			viewholder = new ViewHolder();

			view = LayoutInflater.from(context).inflate(
					R.layout.fragment_home_item, null);
			viewholder.title = (TextView) view
					.findViewById(R.id.frament_home_item_titleTag);
			viewholder.update = (TextView) view
					.findViewById(R.id.frament_home_item_updateTAg);
			viewholder.imge = (ImageView) view
					.findViewById(R.id.frament_home_item_image);
viewholder.tag=(View)view.findViewById(R.id.frament_home_item_tag);
			view.setTag(viewholder);
		

		} else {
			viewholder = (ViewHolder) view.getTag();

		}
		try {

			Comic item = data.get(arg0);
			viewholder.title.setText(item.getComic_title());
		
			if(Integer.parseInt(item.getComic_finish())==1){
				viewholder.update.setText("已完结");
			
				
			}else{
				viewholder.update.setText("更新至" + item.getComic_last_chaptitle());
			}
		
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(context));
			String url = GlobalParams.imageUrlString + item.getComic_titlepic();
			
			imageLoader.displayImage(url, viewholder.imge,
					GlobalParams.frameHomeoptions);
			if(type==1){
				viewholder.tag.setVisibility(View.GONE);
				
			}
		
			
			
			
			
			
			
			
			
			
//			WindowManager wm = (WindowManager) context
//					.getSystemService(Context.WINDOW_SERVICE);
//			float widthX = DimensionPixelUtil.dip2px(context, 60);
//			float titleH=DimensionPixelUtil.dip2px(context, 60);
//			int width = (int) ((wm.getDefaultDisplay().getWidth() - widthX) / 3);
//
//			int height = (int) (width * 4 /3 +titleH);
//
//			LayoutParams params = new LayoutParams(width, height);
//			view.setLayoutParams(params);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return view;
	}

	public static class ViewHolder {

		private TextView update, title;
		private ImageView imge;
private View tag;
	}

}
