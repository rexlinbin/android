package com.bccv.boxcomic.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.BookmarkActivity;
import com.bccv.boxcomic.activity.ComicInfoActivity;
import com.bccv.boxcomic.activity.ComicInfoLocalActivity;
import com.bccv.boxcomic.adapter.CollectChildAdapter;
import com.bccv.boxcomic.ebook.BookInfo;
import com.bccv.boxcomic.ebook.EbookActivity;
import com.bccv.boxcomic.ebook.PageActivity;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.DialogUtils;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.SerializationUtil;

@SuppressLint("NewApi")
public class CollectChildFramgent extends Fragment {

	// 数据List
	public static final int COLLECT = 3;
	public static final int PASS = 1;
	public static final int BOOKMARK = 2;
	private int cate;
	private View mView;
	private String TAg = "CollectChildFramgent";
	private ListView listView;
	private CollectChildAdapter adapter;
	private List<Comic> data;
	private Boolean isFirst = true;
	List<Comic> list;
	private Comic comic;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			return mView;
		} else {
			mView = inflater.inflate(R.layout.fragtment_collectchild, null);
		}
		listView = (ListView) mView.findViewById(R.id.collectchild_list_view);

		data = new ArrayList<Comic>();
		adapter = new CollectChildAdapter(data, getActivity(), cate);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
	
					if (data.get(arg2).isComic()) {

						 if (data.get(arg2).isLocal()){
							
							Intent aintent = new Intent(getActivity(),
									ComicInfoLocalActivity.class);

							aintent.putExtra("mainitem", data.get(arg2).getComic_id());

							startActivity(aintent);

							
							
						} else{
							
							Intent aintent = new Intent(getActivity(),
									ComicInfoActivity.class);

							aintent.putExtra("mainitem", data.get(arg2).getComic_id());

							startActivity(aintent);
						}
						
						
						

					}else {
						Comic comic = data.get(arg2);
						BookInfo info = new BookInfo();
						info.setBook_id(Integer.parseInt(comic.getComic_id()) );
						info.setBook_author(comic.getComic_author());
						info.setBook_title(comic.getComic_title());
						info.setBook_titlepic(comic.getComic_titlepic());

						info.setBook_last_chaptitle(comic
								.getComic_last_chaptitle());

						Intent aintent = new Intent(getActivity(),
								EbookActivity.class);

						aintent.putExtra(EbookActivity.BOOK_INFO_KEY,info);
						startActivity(aintent);

					}

				

			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				
				DialogUtils.showDeleteDialog(getActivity(),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								deleteData(arg2);

							}

						});
				
				return true;
			}
		});

		return mView;

	}

	private void setData() {
		// TODO Auto-generated method stub

		// list = new ArrayList<Comic>();

		if (cate == COLLECT) {
			Logger.e("error", COLLECT + "");
			list = SerializationUtil.readCollectCache(getActivity()
					.getApplicationContext());

		} else if (cate == BOOKMARK) {
			Logger.e("error", BOOKMARK + "");
			list = SerializationUtil.readBookmarkComicCache(getActivity()
					.getApplicationContext());

		} else {

			Logger.e("error", PASS + "");
			list = SerializationUtil.readHistoryCache(getActivity()
					.getApplicationContext());

		}

		try {
			data.clear();
			data.addAll(list);

			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void deleteData(int position) {
		// TODO Auto-generated method stub

		if (cate == COLLECT) {

			list = SerializationUtil.readCollectCache(getActivity()
					.getApplicationContext());

			data.remove(position);
			list.remove(position);

			SerializationUtil.wirteCollectSerialization(getActivity()
					.getApplicationContext(), (Serializable) list);

		} else if (cate == PASS) {

			list = SerializationUtil.readHistoryCache(getActivity()
					.getApplicationContext());
			data.remove(position);
			list.remove(position);
			SerializationUtil.wirteHistorySerialization(getActivity()
					.getApplicationContext(), (Serializable) list);
		} else if (cate == BOOKMARK) {

			list = SerializationUtil.readBookmarkComicCache(getActivity()
					.getApplicationContext());
			data.remove(position);
			list.remove(position);
			SerializationUtil.wirteBookmarkComicSerialization(getActivity()
					.getApplicationContext(), (Serializable) list);
		}

		adapter.notifyDataSetChanged();

	}

	/**
	 * 设置类型
	 * 
	 * @param cate
	 * 
	 */
	public void setCate(int cate) {
		this.cate = cate;
	}

	public int getCate() {
		return cate;
	}

	/**
	 * 重置
	 */
	public void reset() {

		if (data != null) {
			data.clear();
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setData();
	}

}
