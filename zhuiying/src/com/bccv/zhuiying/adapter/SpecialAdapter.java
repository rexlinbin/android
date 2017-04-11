package com.bccv.zhuiying.adapter;

import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.model.Special;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.DimensionPixelUtil;
import com.utils.tools.GlobalParams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SpecialAdapter extends BaseAdapter{
private List<Special>data;
private Context context;

	int dw;
	
	
	
	public SpecialAdapter(List<Special> data, Context context) {
	super();
	this.data = data;
	this.context = context;
}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public int getDw() {
		return dw;
	}

	public void setDw(int dw) {
		this.dw = dw;
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
		
		ViewHolder viewHolder=null;
		
		if(view==null){
			
			viewHolder=new ViewHolder();
			
			view=LayoutInflater.from(context).inflate(R.layout.listitem_special, null);
			
			
			viewHolder.image=(ImageView) view.findViewById(R.id.item_special_image);
			viewHolder.text=(TextView) view.findViewById(R.id.item_special_text);
			
			view.setTag(viewHolder);
			
			
		}else{
			
			viewHolder=(ViewHolder) view.getTag();
			
			
		}
		Special item=data.get(position);
	
		viewHolder.text.setText(item.getName());
//		float w=dw/viewHolder.image.getHeight();
		float hm=(float) (dw*0.4705882352941176);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,(int) hm);
		
		params.setMargins((int)DimensionPixelUtil.dip2px(context, 10), (int)DimensionPixelUtil.dip2px(context, 10),(int) DimensionPixelUtil.dip2px(context, 10), (int)DimensionPixelUtil.dip2px(context, 10));

		viewHolder.image.setLayoutParams(params);

		
		
		
		String images = (String) viewHolder.image.getTag();
		if (images != null && images.equals(item.getImages())) {
//			viewHolder.imageView.setTag(item.getImages());
//			ImageLoader imageLoader = ImageLoader.getInstance();
//			imageLoader.displayImage(item.getImages(), viewHolder.imageView, GlobalParams.movieOptions);
		}else{
			viewHolder.image.setTag(item.getImages());
			
			ImageLoader imageLoder=ImageLoader.getInstance();
			imageLoder.displayImage(item.getImages(), viewHolder.image, GlobalParams.bannerOptions);
			
		}
		
		
		
		
		
		
		
		
		
		return view;
	}

	class ViewHolder{
		
		
		ImageView image;
		
		TextView text;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
