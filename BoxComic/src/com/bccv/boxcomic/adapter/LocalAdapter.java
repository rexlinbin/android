package com.bccv.boxcomic.adapter;
import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.DimensionPixelUtil;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.ImageUtils;

public class LocalAdapter extends BaseAdapter {

	private Context context;
	private List<Comic>data;	
	
	public LocalAdapter(Context context, List<Comic> data) {
		super();
		this.context = context;
		this.data = data;
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		View view=LayoutInflater.from(context).inflate(R.layout.local_gridview_item, null);
		
		
		TextView title=(TextView) view.findViewById(R.id.frament_home_item_titleTag);
		
		
		TextView update = (TextView) view
				.findViewById(R.id.frament_home_item_updateTAg);
		ImageView imge = (ImageView) view
				.findViewById(R.id.frament_home_item_image);
		Comic item=data.get(arg0);
		
		title.setText(item.getComic_title());
		
		update.setVisibility(View.VISIBLE);
		
		
		
		List<File> files = FileUtils.listPathFiles(GlobalParams.localComicPathString + "/" + item.getComic_title());
		if (files.size() > 0) {
			for (int i = 0; i < files.size(); i++) {
				File file = files.get(i);
				Bitmap bitmap = ImageUtils.getBitmapByFile(file);
				if (bitmap != null) {
					imge.setImageBitmap(bitmap);
				
				}else{
					imge.setBackgroundResource(R.drawable.localhome);
					
				}
			}
		}
		
		
		
		return view;
	}

	
}
