package com.bccv.boxcomic.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.BookmarkAdapter;
import com.bccv.boxcomic.modal.Chapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.DialogUtils;
import com.bccv.boxcomic.tool.SerializationUtil;


public class BookmarkActivity extends BaseActivity {
	private Comic comic;
	private List<Chapter> list;
	private ListView listView;
	private BookmarkAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark);
				setBack();
		comic = (Comic) getIntent().getExtras().getSerializable("comic");
		list = new ArrayList<Chapter>();
		adapter = new BookmarkAdapter(getApplicationContext(), list);
		listView = (ListView) findViewById(R.id.bookmark_listView);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("bookmark", list.get(position).getBookmarkNum());
				intent.putExtra("pageCount", list.get(position)
						.getChapter_count());
				setResult(1, intent);
				finish();
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				DialogUtils.showDeleteDialog(BookmarkActivity.this,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								deleteData(position);
							}
						});
				return false;
			}
		});

		setData();
	}

	private void setBack() {
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(1);
				finish();
			}
		});
	}

	private void setData() {
		List<Chapter> listChapters = SerializationUtil.readBookmarkCache(
				getApplicationContext(), comic.getComic_id());
		if (listChapters != null) {
			list.addAll(listChapters);
			adapter.notifyDataSetChanged();
		}
	}

	private void deleteData(int position) {

		list.remove(position);
		if (list.size() == 0) {
			List<Comic> bookmarkComicList = SerializationUtil
					.readBookmarkComicCache(getApplicationContext());
			if (bookmarkComicList == null) {
				bookmarkComicList = new ArrayList<Comic>();
			}

			for (Comic bComic : bookmarkComicList) {
				if (bComic.getComic_id().equals(comic.getComic_id())) {
					bookmarkComicList.remove(bComic);
					break;
				}
			}

			SerializationUtil.wirteBookmarkComicSerialization(
					getApplicationContext(), (Serializable) bookmarkComicList);
		}

		SerializationUtil.wirteBookmarkSerialization(getApplicationContext(),
				(Serializable) list, comic.getComic_id());

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			setResult(2);
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

}
