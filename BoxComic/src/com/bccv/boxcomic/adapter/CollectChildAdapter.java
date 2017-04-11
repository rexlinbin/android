package com.bccv.boxcomic.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.ebook.PreferenceHelper;
import com.bccv.boxcomic.fragment.CollectChildFramgent;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class CollectChildAdapter extends BaseAdapter {
	private List<Comic> data;
	private Context context;
private int type;
	public CollectChildAdapter(List<Comic> data, Context context,int type) {
		super();
		this.data = data;
		this.context = context;
		this.type=type;
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
		ViewHolder holder;
		String url;
		Comic item = data.get(arg0);
		if (view == null) {

			holder = new ViewHolder();

			view = LayoutInflater.from(context).inflate(R.layout.test_layout,
					null);
			holder.title = (TextView) view
					.findViewById(R.id.collect_item_title);
			holder.lookTag = (TextView) view
					.findViewById(R.id.collect_item_lookTag);
			holder.updateTag = (TextView) view
					.findViewById(R.id.collect_item_updateTag);
			holder.image = (ImageView) view
					.findViewById(R.id.collect_item_image);
			holder.fenleiIm = (ImageView) view.findViewById(R.id.fenlei_image);
			view.setTag(holder);

		} else {

			holder = (ViewHolder) view.getTag();

		}

		holder.title.setText(item.getComic_title());

		if (item.isComic()) {
			if( type!=1){
				holder.lookTag.setVisibility(View.INVISIBLE);
				
			}else{
				holder.lookTag.setVisibility(View.VISIBLE);
				holder.lookTag.setText(item.getHistoryChapterTitleString());
				
			}
			
			
		
			
			url = GlobalParams.imageUrlString + item.getComic_titlepic();
			holder.fenleiIm.setImageResource(R.drawable.mark_manhua);
			if (item.isLocal()) {
				holder.image.setBackgroundResource(R.drawable.localhome);
				holder.updateTag.setVisibility(View.INVISIBLE);
			} else {
				
				holder.updateTag.setVisibility(View.VISIBLE);
				if(Integer.parseInt(item.getComic_finish())==1){
					holder.updateTag.setText("已完结");
				
					
				}else{
					holder.updateTag.setText("更新至" + item.getComic_last_chaptitle());
				}
			}
		} else {
			String progressInfo = PreferenceHelper.getString(item.getComic_id()
					+ "", null);
			if (!TextUtils.isEmpty(progressInfo)) {
				// 格式 ： ChapterId_ChapterName_CharsetIndex
				String[] values = progressInfo.split("#");
				if (values.length == 3) {
					holder.lookTag.setText("上次浏览到" + values[1]);
				} else {
					holder.lookTag.setVisibility(View.INVISIBLE);
				}
			} else {
				holder.lookTag.setVisibility(View.INVISIBLE);
			}
			holder.fenleiIm.setImageResource(R.drawable.mark_xiaoshuo);
			url = GlobalParams.BookImagepic + item.getComic_titlepic();
		}
		
	
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));

		imageLoader.displayImage(url, holder.image, GlobalParams.options);
	
		
		
		
		
		
		
		return view;

	}

	public static class ViewHolder {

		TextView title, lookTag, updateTag;

		ImageView image, fenleiIm;

	}

}
