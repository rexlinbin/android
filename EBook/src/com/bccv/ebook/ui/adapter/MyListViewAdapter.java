package com.bccv.ebook.ui.adapter;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bccv.ebook.model.BookInfo;
import com.boxuu.ebookjy.R;

public class MyListViewAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<BookInfo> bookInfos;
	private int count = 0;
	
	public MyListViewAdapter(Context context,ArrayList<BookInfo> bookInfos) {
		this.context = context;
		this.bookInfos = bookInfos;
	}
	
	@Override
	public int getCount() {
		if(bookInfos==null){
			count = 0;
		}
		count = bookInfos.size()% 3 == 0 ? (bookInfos.size()/3):(bookInfos.size()/3+1);
		return count;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.list_view_item, null);
			holder.view = convertView.findViewById(R.id.view);
			holder.ll_first = (LinearLayout) convertView.findViewById(R.id.ll_first);
			holder.ll_second = (LinearLayout) convertView.findViewById(R.id.ll_second);
			holder.ll_third = (LinearLayout) convertView.findViewById(R.id.ll_third);
			holder.grid_iv_pic_first = (ImageView) convertView.findViewById(R.id.list_iv_book_pic_first);
			holder.grid_iv_pic_second = (ImageView) convertView.findViewById(R.id.list_iv_book_pic_second);
			holder.grid_iv_pic_third = (ImageView) convertView.findViewById(R.id.list_iv_book_pic_third);
			holder.grid_tv_name_first = (TextView) convertView.findViewById(R.id.list_tv_book_name_first);
			holder.grid_tv_name_second = (TextView) convertView.findViewById(R.id.list_tv_book_name_second);
			holder.grid_tv_name_third = (TextView) convertView.findViewById(R.id.list_tv_book_name_third);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (position * 3 < bookInfos.size()) {
			holder.grid_tv_name_first.setText(bookInfos.get(position*3).getName());
			holder.ll_first.setVisibility(View.VISIBLE);
			String coverPath = bookInfos.get(position*3).getCover();
			setCover(holder.grid_iv_pic_first, coverPath);
			holder.grid_iv_pic_first.setOnClickListener(new MyOnClickListener(position*3));
		}else{
			holder.ll_first.setVisibility(View.GONE);
		}
		if (position*3+1 < bookInfos.size()) {
			holder.grid_tv_name_second.setText(bookInfos.get(position*3+1).getName());
			holder.ll_second.setVisibility(View.VISIBLE);
			String coverPath = bookInfos.get(position*3+1).getCover();
			setCover(holder.grid_iv_pic_second, coverPath);
			holder.grid_iv_pic_second.setOnClickListener(new MyOnClickListener(position*3+1));
		}else{
			holder.ll_second.setVisibility(View.GONE);
		}
		if (position*3+2 < bookInfos.size()) {
			holder.grid_tv_name_third.setText(bookInfos.get(position*3+2).getName());
			holder.ll_third.setVisibility(View.VISIBLE);
			String coverPath = bookInfos.get(position*3+2).getCover();
			setCover(holder.grid_iv_pic_third, coverPath);
			holder.grid_iv_pic_third.setOnClickListener(new MyOnClickListener(position*3+2));
		}else{
			holder.ll_third.setVisibility(View.GONE);
		}
		
		if (position == count+1) {
			holder.view.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	@SuppressWarnings("deprecation")
	private void setCover(ImageView view,String coverPath){
		try {
			InputStream is = context.getAssets().open(coverPath);
			BitmapDrawable coverDrawable= new BitmapDrawable(BitmapFactory.decodeStream(is));
			view.setBackgroundDrawable(coverDrawable);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			view.setBackgroundResource(R.drawable.bookshotcut);
		}
	}
	
	class ViewHolder{
		View view;
		LinearLayout ll_first;
		LinearLayout ll_second;
		LinearLayout ll_third;
		ImageView grid_iv_pic_first;
		ImageView grid_iv_pic_second;
		ImageView grid_iv_pic_third;
		TextView grid_tv_name_first;
		TextView grid_tv_name_second;
		TextView grid_tv_name_third;
	}
	
	class MyOnClickListener implements OnClickListener{
		
		private int bookId;

		public MyOnClickListener(int bookId){
			this.bookId = bookId;
		}
		
		@Override
		public void onClick(View v) {
//			BookInfo bookInfo = bookInfos.get(bookId);
//			Intent intent = new Intent(context,CatalogueActivity.class);
//			context.startActivity(intent);
			if (myListener != null) {
				myListener.OnItemClick(bookId);
			}
		}
		
	}
	public MyOnItemClickListener myListener;
	public interface MyOnItemClickListener{
		public void OnItemClick(int position);
	}
	
	public void setMyListener(MyOnItemClickListener myListener){
		this.myListener = myListener;
	}
	
	public MyOnItemClickListener getMyListener(){
		if (myListener != null) {
			return myListener;
		}
		return null;
	}
}
