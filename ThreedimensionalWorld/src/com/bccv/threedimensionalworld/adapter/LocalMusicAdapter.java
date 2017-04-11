package com.bccv.threedimensionalworld.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.model.MusicBean;

public class LocalMusicAdapter extends BaseAdapter{

	private List<MusicBean>data;
	private Context context;
	private int selectNum = 0;
	
	public LocalMusicAdapter(List<MusicBean> data, Context context) {
		super();
		this.data = data;
		this.context = context;
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
	public void setSelect(int selectNum) {
		if (selectNum != this.selectNum) {
			this.selectNum = selectNum;
			notifyDataSetChanged();
		}
	}
	@SuppressLint("NewApi") @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		View view=LayoutInflater.from(context).inflate(R.layout.item_localmusic, null);
		
		TextView nameText =(TextView) view.findViewById(R.id.localmusic_item_name);
		
		TextView autherText=(TextView) view.findViewById(R.id.localmusic_item_author);
		nameText.setScaleX(0.5f);
		autherText.setScaleX(0.5f);
		MusicBean item=data.get(arg0);
		
		nameText.setText(item.getName());
		
		autherText.setText(item.getAuthor());
		
		
//		if (arg0 == selectNum) {
//			re.setSelected(true);
//			
//		
//		} else {
//			re.setSelected(false);
//			
//		}
		
		
		
		return view;
	}

}
