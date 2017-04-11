package com.bccv.boxcomic.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoActivity;
import com.bccv.boxcomic.adapter.MainListViewAdapter.ViewHolder;
import com.bccv.boxcomic.ebook.BookInfo;
import com.bccv.boxcomic.ebook.EbookActivity;
import com.bccv.boxcomic.ebook.PageActivity;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.MyGridView;
import com.bccv.boxcomic.tool.SerializationUtil;

public class MyListBookViewAdapter extends BaseAdapter {
	private List<BookInfo> data;
	private Context context;
	Activity activity;

	public MyListBookViewAdapter(List<BookInfo> data, Context context,
			Activity activity) {
		super();
		this.data = data;
		this.context = context;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.fragment_main_childgridview, null, false);
			holder.gridView = (MyGridView) convertView
					.findViewById(R.id.frament_main_gridView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (this.data != null) {

			if (holder.gridView != null && arg0 == 0) {

				MyBookAdapter adapter = new MyBookAdapter(data, context);
				holder.gridView.setAdapter(adapter);
				holder.gridView.setSelector(new ColorDrawable(
						android.R.color.transparent));
				holder.gridView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								BookInfo bookInfo = data.get(arg2);
								Comic comic = new Comic();
								comic.setComic(false);
								comic.setComic_id(bookInfo.getBook_id() + "");
								comic.setComic_author(bookInfo.getBook_author());
								comic.setComic_title(bookInfo.getBook_title());
								comic.setComic_titlepic(bookInfo.getBook_titlepic());
								comic.setComic_last_chaptitle(bookInfo.getBook_last_chaptitle());
								
								
								List<Comic> chapterList = SerializationUtil.readHistoryCache(context);
								if (chapterList == null) {
									chapterList = new ArrayList<Comic>();
								}
								boolean hasComic = true;
								int comicNum = -1;
								for (int i = 0; i < chapterList.size(); i++) {
									Comic historyComic = chapterList.get(i);
									if (historyComic.getComic_id().equals(comic.getComic_id())) {
										comicNum = i;
										hasComic = false;
										break;
									}
								}
								if (hasComic) {
									chapterList.add(0, comic);
								} else {
									if (comicNum != -1) {
										chapterList.remove(comicNum);
										chapterList.add(0, comic);
									}
								}

								SerializationUtil.wirteHistorySerialization(
										context, (Serializable) chapterList);
								
								
								Intent aintent = new Intent(activity,
										EbookActivity.class);

								aintent.putExtra(EbookActivity.BOOK_INFO_KEY,
										bookInfo);

								activity.startActivity(aintent);

							}
						});
			}

		}
		return convertView;
	}

	public static class ViewHolder {
		MyGridView gridView;

	}

}
