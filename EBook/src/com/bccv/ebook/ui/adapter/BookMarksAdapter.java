package com.bccv.ebook.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bccv.ebook.model.Bookmark;
import com.bccv.ebook.ui.activity.BookMarksActivity;
import com.bccv.ebook.ui.activity.PageActivity;
import com.boxuu.ebookjy.R;

public class BookMarksAdapter extends BaseAdapter {

	private BookMarksActivity context;
	private boolean isShowDel = false;
	private ArrayList<Bookmark> bookmarks;
	
	public BookMarksAdapter(BookMarksActivity context,ArrayList<Bookmark> marks){
		this.context = context;
		this.bookmarks = marks;
	}
	
	public void setDelBtn(boolean isShowBtn){
		this.isShowDel = isShowBtn;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return bookmarks == null ? 0 :bookmarks.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.bookmarks_list_item, null);
			holder = new ViewHolder();
			holder.rl_bookmarks = (RelativeLayout) convertView.findViewById(R.id.rl_bookmarks);
			holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
			holder.bookmarks_order = (TextView) convertView.findViewById(R.id.bookmarks_order);
			holder.bookmarks_name = (TextView) convertView.findViewById(R.id.bookmarks_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final Bookmark bookmark = bookmarks.get(position);
		String name = bookmark.getChapterName();
		long time = bookmark.getTime();
		if (isShowDel) {
			holder.iv_delete.setVisibility(View.VISIBLE);
		}else{
			holder.iv_delete.setVisibility(View.GONE);
		}
		
		holder.bookmarks_order.setText(name);
		holder.bookmarks_name.setText(dataFormat(time));
		
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.removeMarks(bookmark, position);
//				bookmarks.remove(position);
				notifyDataSetChanged();
			}
		});
		holder.rl_bookmarks.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent pageIntent = new Intent(context,PageActivity.class);
				pageIntent.putExtra(PageActivity.BOOKMARK_KEY, bookmark);
				context.startActivity(pageIntent);
				context.finish();
			}
		});
		return convertView;
	}
	
	
	class ViewHolder{
		RelativeLayout rl_bookmarks;
		ImageView iv_delete;
		TextView bookmarks_order;
		TextView bookmarks_name;
	}
	
	@SuppressLint("SimpleDateFormat")
	private String dataFormat(long date){
	    Date currentdate = new Date(date);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return sdf.format(currentdate);
	}
}
