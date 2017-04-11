package com.bccv.ebook.ui.adapter;

import java.util.ArrayList;

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
import com.bccv.ebook.model.ChapterInfo;
import com.bccv.ebook.ui.activity.CatalogueActivity;
import com.bccv.ebook.ui.activity.PageActivity;
import com.boxuu.ebookjy.R;

public class CatalogueAdapter extends BaseAdapter {

	private CatalogueActivity context;
	private int curOrder;
	private ArrayList<String> cata_list ;
	private ArrayList<ChapterInfo> chapterInfos;
	private boolean isPse = true;
	
	public CatalogueAdapter(CatalogueActivity context,int cur){
		this.context = context;
		this.curOrder = cur;
//		this.cata_list = list;
	}
	
	@Override
	public int getCount() {
		return cata_list == null ? 0 : cata_list.size();
	}
	
	public void setList(ArrayList<String> list , ArrayList<ChapterInfo> chapterInfos,boolean isPse){
		this.cata_list = list;
		this.chapterInfos = chapterInfos;
		this.isPse = isPse;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.catalogue_list_item, null);
			holder = new ViewHolder();
			holder.rl_catalogue = (RelativeLayout) convertView.findViewById(R.id.rl_catalogue);
			holder.iv_show = (ImageView) convertView.findViewById(R.id.iv_cur_show);
//			holder.tv_order = (TextView) convertView.findViewById(R.id.catalogue_order);
			holder.tv_name = (TextView) convertView.findViewById(R.id.catalogue_name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if ( isPse && position == curOrder) {
//			holder.tv_order.setTextColor(context.getResources().getColor(R.color.catalogue_text_cur));
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.catalogue_text_cur));
			holder.iv_show.setVisibility(View.VISIBLE);
		}else if( !isPse && position == chapterInfos.size()-1-curOrder ){
//			holder.tv_order.setTextColor(context.getResources().getColor(R.color.catalogue_text_cur));
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.catalogue_text_cur));
			holder.iv_show.setVisibility(View.VISIBLE);
		}else{
//			holder.tv_order.setTextColor(context.getResources().getColor(R.color.catalogue_text_normal));
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.catalogue_text_normal));
			holder.iv_show.setVisibility(View.GONE);
		}
//		holder.tv_order.setText(cata_list.get(position));
		if (isPse) {
			holder.tv_name.setText(chapterInfos.get(position).getName());
		}else{
			holder.tv_name.setText(chapterInfos.get( (chapterInfos.size()-position-1) ).getName());
		}
		holder.rl_catalogue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isPse && position == curOrder || !isPse && position == chapterInfos.size()-1-curOrder) {
					context.finish();
				}else if (isPse) {
					Bookmark bookmark = new Bookmark();
					bookmark.setChapterId(chapterInfos.get(position).getId());
					bookmark.setChapterName(chapterInfos.get(position).getName());
					bookmark.setCharsetIndex(0);
					Intent pageIntent = new Intent(context,PageActivity.class);
					pageIntent.putExtra(PageActivity.BOOKMARK_KEY, bookmark);
					context.startActivity(pageIntent);
					context.finish();
				}else if (!isPse) {
					Bookmark bookmark = new Bookmark();
					bookmark.setChapterId(chapterInfos.get(chapterInfos.size()-position-1).getId());
					bookmark.setChapterName(chapterInfos.get(chapterInfos.size()-position-1).getName());
					bookmark.setCharsetIndex(0);
					Intent pageIntent = new Intent(context,PageActivity.class);
					pageIntent.putExtra(PageActivity.BOOKMARK_KEY, bookmark);
					context.startActivity(pageIntent);
					context.finish();
				}
			}
		});
		return convertView;
	}
	
	
	class ViewHolder{
		RelativeLayout rl_catalogue;
		ImageView iv_show;
//		TextView tv_order;
		TextView tv_name;
	}
}
