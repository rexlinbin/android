package com.bccv.zhuiyingzhihanju.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.model.Special;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.DimensionPixelUtil;
import com.utils.tools.GlobalParams;

import android.R.integer;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecommendAdapter extends BaseAdapter{
	private List<Special>data;
	private Context context;

		int dw;
		
		
		
		public RecommendAdapter(List<Special> data, Context context) {
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
				
				view=LayoutInflater.from(context).inflate(R.layout.listview_recommended, null);
				
				
				viewHolder.image=(ImageView) view.findViewById(R.id.item_recommend_image);
				viewHolder.text=(TextView) view.findViewById(R.id.item_recommend_text);
				viewHolder.BoNum=(TextView) view.findViewById(R.id.item_recommend_bonum);
				viewHolder.num=(TextView) view.findViewById(R.id.item_recommend_num);
				viewHolder.up=(TextView) view.findViewById(R.id.item_recommend_up);
				view.setTag(viewHolder);
				
				
			}else{
				
				viewHolder=(ViewHolder) view.getTag();
				
				
			}
			Special item=data.get(position);
		
			viewHolder.text.setText(item.getTitle());
//			float w=dw/viewHolder.image.getHeight();
		
			viewHolder.BoNum.setText("播放"+getNum(Integer.parseInt(item.getHit()))+"次");
			
			viewHolder.num.setText("共"+item.getNum()+"部");
			viewHolder.up.setText(item.getUser_name());
			
			
			
			
			
			
			
			
			float hm=(float) (dw*0.5277777777777778);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,(int) hm);
			
//			params.setMargins((int)DimensionPixelUtil.dip2px(context, 0), (int)DimensionPixelUtil.dip2px(context, 0),(int) DimensionPixelUtil.dip2px(context,0), (int)DimensionPixelUtil.dip2px(context, 0));

			viewHolder.image.setLayoutParams(params);

			
			
			
			String images = (String) viewHolder.image.getTag();
			if (images != null && images.equals(item.getImages())) {
//				viewHolder.imageView.setTag(item.getImages());
//				ImageLoader imageLoader = ImageLoader.getInstance();
//				imageLoader.displayImage(item.getImages(), viewHolder.imageView, GlobalParams.movieOptions);
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
			TextView num;
			TextView up;
			TextView BoNum;
			
		}
		

			private String getNum(
					double num){
				
				
				if(num>9999&&num<10000000){
			
					DecimalFormat df=new DecimalFormat(".##");
				
					String st=df.format(num/10000D);	
					
					
//			double boN=(double)(Math.round(num*100)/100.0);
			return st+"万";
					
				}
				
				
				
				
				
				
				
				
				
				
				return (int)num+"";
				
				
				
				
				
			}
			
			
			
			
			
			
			
			
	
		
		
		
		
		
		
		
		
		
		
		
}
