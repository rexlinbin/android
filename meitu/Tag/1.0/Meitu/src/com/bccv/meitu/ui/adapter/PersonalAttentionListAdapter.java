package com.bccv.meitu.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.api.NetWorkAPI;
import com.bccv.meitu.model.AuthorInfo;
import com.bccv.meitu.network.HttpCallback;
import com.bccv.meitu.network.NetResBean;
import com.bccv.meitu.utils.Logger;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class PersonalAttentionListAdapter extends BaseAdapter {

	private static final String TAG = "PersonalAttentionListAdapter";
	
	private List<AuthorInfo> dataList;
	
	private Context mContext;
	
	public PersonalAttentionListAdapter(Context context,List<AuthorInfo> dataList){
		this.mContext = context;
		this.dataList = dataList;
	}
	
	public List<AuthorInfo> getDataList() {
		return dataList;
	}

	public void setDataList(List<AuthorInfo> dataList) {
		this.dataList = dataList;
	}

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder=null;
		
		if(convertView!=null){
			holder = (ViewHolder) convertView.getTag();
		}else{
			holder = new ViewHolder();
			holder.position = position;
			convertView = View.inflate(mContext, R.layout.personal_attention_item, null);
			holder.author_pic = (ImageView) convertView.findViewById(R.id.author_pic);
			holder.author_name_tv = (TextView) convertView.findViewById(R.id.author_name_tv);
			holder.album_num_tv = (TextView) convertView.findViewById(R.id.album_num_tv);
			holder.fans_num_tv = (TextView) convertView.findViewById(R.id.fans_num_tv);
			holder.attention_btn = (ImageView) convertView.findViewById(R.id.attention_btn);
			holder.attention_btn.setOnClickListener(new MyOnClickListener(holder, position));
			
			convertView.setTag(holder);
		}
		
		if(dataList.get(position).isAttention()){
			holder.attention_btn.setBackgroundResource(R.drawable.personal_attention_item_btn_selector);
		}else{
			holder.attention_btn.setBackgroundResource(R.drawable.attention_guanzhu_visited);
		}
		
		ImageLoaderUtil.getInstance(mContext).displayImage(dataList.get(position).getAuthor_icon(),
				holder.author_pic, ImageLoaderUtil.getRoundedImageOptions());
		holder.author_name_tv.setText(dataList.get(position).getAuthor_name());
		holder.album_num_tv.setText("专辑:"+dataList.get(position).getSpecial_num());
		holder.fans_num_tv.setText("粉丝:"+dataList.get(position).getFans_num());
		
		return convertView;
	}

	public class ViewHolder {
		ImageView author_pic;
		TextView author_name_tv;
		TextView album_num_tv;
		TextView fans_num_tv;
		ImageView attention_btn;
		int position;
		
	}
	
	private class MyOnClickListener implements OnClickListener{
		
		private ViewHolder holder;
		private int position;
		
		public MyOnClickListener(ViewHolder holder,int position){
			this.holder = holder;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			final AuthorInfo authorInfo = dataList.get(position);
			
			final int type = authorInfo.isAttention()?2:1;
			
			NetWorkAPI.careauthor(mContext, authorInfo.getAuthor_id(), type, new HttpCallback() {
				
				@Override
				public void onResult(NetResBean response) {
					if(response.success){
						
						authorInfo.setAttention(!authorInfo.isAttention());
						if(authorInfo.isAttention()){
							Toast.makeText(mContext, "已关注", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(mContext, "已取消", Toast.LENGTH_SHORT).show();
						}
						
						Logger.v(TAG, "onResult",  " position : " + position + " holder.position : " + holder.position);
						
						if(position==holder.position){
							
							
							if(authorInfo.isAttention()){
								Logger.v(TAG, "onResult",  " authorInfo.isAttention : true ");
								holder.attention_btn.setBackgroundResource(R.drawable.personal_attention_item_btn_selector);
							}else{
								Logger.v(TAG, "onResult",  " authorInfo.isAttention : false ");
								holder.attention_btn.setBackgroundResource(R.drawable.attention_guanzhu_visited);
							}
						}
						
					}else{
						Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
					}
				}
				
				@Override
				public void onError(String errorMsg) {
					Toast.makeText(mContext, "操作失败，请重试", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onCancel() {}
			});

		}
	}
	
}
