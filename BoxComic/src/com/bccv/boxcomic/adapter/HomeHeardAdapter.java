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
import com.bccv.boxcomic.modal.Comic;

public class HomeHeardAdapter extends BaseAdapter {

	private List<Comic> data;
	private Context context;

	public HomeHeardAdapter(List<Comic> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size() < 1 ? data.size() : 1;
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
		View view = LayoutInflater.from(context).inflate(
				R.layout.fragment_mainchild_title, null);

		TextView title = (TextView) view
				.findViewById(R.id.frament_main_yuceText1);
		TextView seeTag = (TextView) view
				.findViewById(R.id.frament_main_readTag1);
		ImageView im = (ImageView) view.findViewById(R.id.frament_main_Image);
		Comic item = data.get(arg0);

		title.setText(item.getComic_title());
		if (item.getComic_id().equals("-1")) {
			seeTag.setText("");
			im.setVisibility(View.GONE);
		} else {
			seeTag.setText("上次浏览到" + item.getHistoryChapterTitleString());
			im.setVisibility(View.VISIBLE);
		}
		if (item.isComic()) {
			im.setBackgroundResource(R.drawable.mark_manhua);
			seeTag.setVisibility(View.VISIBLE);
		} else {
			im.setBackgroundResource(R.drawable.mark_xiaoshuo);
			String progressInfo = PreferenceHelper.getString(item.getComic_id()
					+ "", null);
			if (!TextUtils.isEmpty(progressInfo)) {
				// 格式 ： ChapterId_ChapterName_CharsetIndex
				String[] values = progressInfo.split("#");
				if (values.length == 3) {
					seeTag.setText("上次浏览到" + values[1]);
				}else {
					seeTag.setVisibility(View.INVISIBLE);
				}
			}else {
				seeTag.setVisibility(View.INVISIBLE);
			}
			
		}
		return view;
	}

}
