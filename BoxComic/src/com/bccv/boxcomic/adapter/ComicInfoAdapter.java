package com.bccv.boxcomic.adapter;

import java.util.List;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoActivity.OnChapterAll;
import com.bccv.boxcomic.tool.StringUtils;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComicInfoAdapter extends BaseAdapter {

	private Activity activity;
	private List<String> list;
	private OnChapterAll onChapterAll;

	public ComicInfoAdapter(Activity activity, List<String> list,
			OnChapterAll onChapterAll) {
		this.activity = activity;
		this.list = list;
		this.onChapterAll = onChapterAll;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(activity.getApplicationContext(),
					R.layout.griditem_comicinfo, null);
			viewHolder.chapterTextView = (TextView) convertView
					.findViewById(R.id.chapter_textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (!StringUtils.isEmpty(list.get(position))) {
			viewHolder.chapterTextView.setText(list.get(position));
		}
		if (list.get(position).equals("...")) {
			viewHolder.chapterTextView
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							onChapterAll.getAll(-1);
						}
					});
		} else {
			viewHolder.chapterTextView
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							onChapterAll.getAll(position);
						}
					});
		}

		return convertView;
	}

	class ViewHolder {
		TextView chapterTextView;
	}

}
