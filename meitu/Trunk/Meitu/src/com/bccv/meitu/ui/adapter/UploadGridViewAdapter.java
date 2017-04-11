package com.bccv.meitu.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bccv.meitu.R;
import com.bccv.meitu.localphoto.bean.UpdatePhotoInfo;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class UploadGridViewAdapter extends BaseAdapter {

	private List<UpdatePhotoInfo> infos;
	private Context mContext;
	private UpdatePhotoInfo defultUpdatePhotoInfo;
	
	public UploadGridViewAdapter(Context context,List<UpdatePhotoInfo> infos,UpdatePhotoInfo defultUpdatePhotoInfo){
		this.mContext = context;
		this.infos = infos;
		this.defultUpdatePhotoInfo = defultUpdatePhotoInfo;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos==null?0:infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos==null?null:infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final Holder holder;
		if(convertView==null){
			holder = new Holder();
			convertView = View.inflate(mContext, R.layout.upload_gv_item, null);
			holder.select_pic_iv = (ImageView) convertView.findViewById(R.id.select_pic_iv);
			holder.select_cover_iv = (ImageView) convertView.findViewById(R.id.select_cover_iv);
			holder.select_delete_iv = (ImageView) convertView.findViewById(R.id.select_delete_iv);
			holder.position = position;
		}else{
			holder = (Holder) convertView.getTag();
			holder.position = position;
		}
		
		//是否是默认添加条目
		if(infos.get(position).isDefult()){
			ImageLoaderUtil.getInstance(mContext).displayImage("",
					holder.select_pic_iv,ImageLoaderUtil.getUpLoadDefultImageOptions());
			holder.select_delete_iv.setVisibility(View.GONE);
		}else{
			ImageLoaderUtil.getInstance(mContext).displayImage(infos.get(position).getPath_file(),
									holder.select_pic_iv,ImageLoaderUtil.getUpLoadImageOptions());
			holder.select_delete_iv.setVisibility(View.VISIBLE);
		}
		
		//是否是封面
		if(infos.get(position).isCover()){
			holder.select_cover_iv.setVisibility(View.VISIBLE);
		}else{
			holder.select_cover_iv.setVisibility(View.GONE);
		}
		
		//设置删除按钮的点击事件
		holder.select_delete_iv.setOnClickListener(new OnClickListener() {
			int clickedPosition = position;
			@Override
			public void onClick(View v) {
				
				if(clickedPosition == holder.position){
					//TODO 删除这个图片
					infos.remove(clickedPosition);
					if(!infos.get(infos.size()-1).isDefult()){
						infos.add(defultUpdatePhotoInfo);
					}
					resetWithNoneCover();
					notifyDataSetChanged();
				}
			}
		});
		
		convertView.setTag(holder);
		return convertView;
	}

	public static class Holder{
		int position;
		ImageView select_pic_iv; //预览图
		ImageView select_cover_iv; //封面标记
		ImageView select_delete_iv; //删除按钮
	}
	
	/**
	 * 如果所选途中没有默认封面  则设置第一张为封面
	 */
	public void resetWithNoneCover(){
		if(infos.size()>1){
			for (UpdatePhotoInfo photoInfo : infos) {
				if(photoInfo.isCover()){
					return;
				}
			}
			infos.get(0).setCover(true);
		}
	}
	
}
