package com.bccv.meitu.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.meitu.R;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.view.ScaleImageView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class PersonalRelatedListAdapter extends BaseAdapter {

	private List<Special> dataList;
	
	private Context mContext;
	
	public PersonalRelatedListAdapter(Context context,List<Special> dataList){
		this.mContext = context;
		this.dataList = dataList;
	}
	
	public List<Special> getDataList() {
		return dataList;
	}

	public void setDataList(List<Special> dataList) {
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
//		return height.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
//		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder=null;
		
		if(convertView!=null){
			holder = (ViewHolder) convertView.getTag();
			holder.special_pic.recycle();
			if(convertView instanceof ViewGroup){
				((ViewGroup) convertView).removeAllViews();
			}
			convertView = null;
		}
//		else{
			convertView = View.inflate(mContext, R.layout.home_list_item, null);
			holder = new ViewHolder();
			holder.special_pic = (ScaleImageView) convertView.findViewById(R.id.special_pic);
			holder.headPic = (ImageView) convertView.findViewById(R.id.home_headpic);
			holder.commentNum = (TextView) convertView.findViewById(R.id.home_comment_num);
			holder.likeNum = (TextView) convertView.findViewById(R.id.home_like_num);
			holder.iv = (ImageView) convertView.findViewById(R.id.personal_related_item_iv);
			convertView.setTag(holder);
//		}
		
		//TODO 设置数值
//		holder.special_pic.setImageWidth(50);
//		holder.special_pic.setImageHeight(height.get(position));
		
		final Special special = dataList.get(position);
		holder.special_pic.setImageWidth(special.getSpecial_pic_w());
		holder.special_pic.setImageHeight(special.getSpecial_pic_h());
		
		if(special.isIschecked()){
			holder.iv.setVisibility(View.VISIBLE);
		}else{
			holder.iv.setVisibility(View.GONE);
		}
		
		//TODO 设置图片
		ImageLoaderUtil.getInstance(mContext).displayImage(special.getSpecial_pic(), holder.special_pic);
		ImageLoaderUtil.getInstance(mContext).displayImage(special.getAuthor_icon(), holder.headPic,ImageLoaderUtil.getRoundedImageOptions());
		holder.commentNum.setText(String.valueOf(special.getComment_num()));
		holder.likeNum.setText(String.valueOf(special.getCare_num()));
		
		return convertView;
	}

	public class ViewHolder {
		// 图片封面
		ScaleImageView special_pic;
		ImageView headPic;
		TextView commentNum;
		TextView likeNum;
		public ImageView iv;
	}
	
	
}
