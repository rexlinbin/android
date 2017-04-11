package com.bccv.boxcomic.adapter;

import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.Chapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookmarkAdapter extends BaseAdapter {
	
	private Context context;
	private List<Chapter> list;
	
	public BookmarkAdapter(Context context, List<Chapter> list){
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			 convertView = View.inflate(context, R.layout.listitem_bookmark, null);
			 viewHolder.bookmarkTextView = (TextView) convertView.findViewById(R.id.bookmark_textView);
			 convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.bookmarkTextView.setText(list.get(position).getChapter_title() + "," + list.get(position).getBookmarkNum());
		return convertView;
	}

	class ViewHolder{
		TextView bookmarkTextView;
	}
	
}
