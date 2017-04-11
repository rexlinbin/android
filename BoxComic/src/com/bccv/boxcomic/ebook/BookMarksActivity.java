package com.bccv.boxcomic.ebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.SerializationUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class BookMarksActivity extends BaseActivity implements OnClickListener{

	private ImageView title_leftbtn;
	private TextView title_rightbtn;
	private TextView tv_title;
	private ListView lv_bookmarks;
	private TextView no_marks;
	private BookMarksAdapter myAdapter;
	private boolean isShowDel = false;
	private String bookName;
	private Bookmarks bookmarks;
	private HashMap<Integer, ArrayList<Bookmark>> bookMap;
	private ArrayList<Bookmark> marksList = new ArrayList<Bookmark>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmarks);
		bookName = getIntent().getStringExtra(PageActivity.BOOKNAME_KEY);
		bookmarks = SerializationUtil.readSerializationBookmarks(this, bookName);
		if (bookmarks == null) {
			showShortToast("暂无书签");
		}else {
			bookMap = bookmarks.getBookmarks();
			getAllMarks(bookMap);
		}
		initViews();
	}

	private void getAllMarks(HashMap<Integer, ArrayList<Bookmark>> bookMap) {
		Set<Entry<Integer,ArrayList<Bookmark>>> entrySet = bookMap.entrySet();
		for (Entry<Integer, ArrayList<Bookmark>> entry : entrySet) {
			ArrayList<Bookmark> value = entry.getValue();
			marksList.addAll(value);
		}
	}
	
	public void removeMarks(Bookmark bookmark,int position){
		int id = bookmark.getChapterId();
		ArrayList<Bookmark> markList = bookMap.get(id);
		markList.remove(bookmark);
		marksList.remove(position);
		SerializationUtil.wirteSerializationBookmarks(this, bookmarks, bookName);
		showNoMarks();
	}

	private void initViews() {
		title_leftbtn = (ImageView) findViewById(R.id.title_leftbtn);
		title_rightbtn = (TextView) findViewById(R.id.title_rightbtn);
		tv_title = (TextView) findViewById(R.id.uncommon_tv_title);
		lv_bookmarks = (ListView) findViewById(R.id.lv_bookmarks);
		no_marks = (TextView) findViewById(R.id.tv_no_marks);

//		title_leftbtn.setBackgroundResource(R.drawable.back);
		title_rightbtn.setText("删除");
		tv_title.setText("书签列表");

		myAdapter = new BookMarksAdapter(this,marksList);
		//myAdapter.setDelBtn(isShowDel);
		lv_bookmarks.setAdapter(myAdapter);

		title_leftbtn.setOnClickListener(this);
		title_rightbtn.setOnClickListener(this);
		
		showNoMarks();
	}
	
	private void showNoMarks(){
		if (marksList == null || marksList.size() == 0) {
			no_marks.setVisibility(View.VISIBLE);
		}else{
			no_marks.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_leftbtn:
			onBackPressed();
			break;
		case R.id.title_rightbtn:
			if (!isShowDel) {
				isShowDel = true;
				myAdapter.setDelBtn(isShowDel);
				title_rightbtn.setText("取消");
			}else{
				isShowDel = false;
				myAdapter.setDelBtn(isShowDel);
				title_rightbtn.setText("删除");
			}
			break;
		default:
			break;
		}
	}
}
