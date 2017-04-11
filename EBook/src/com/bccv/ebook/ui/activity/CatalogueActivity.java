package com.bccv.ebook.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bccv.ebook.model.BookInfo;
import com.bccv.ebook.model.ChapterInfo;
import com.bccv.ebook.ui.adapter.CatalogueAdapter;
import com.boxuu.ebookjy.R;
/**
 * 目录页
 */
public class CatalogueActivity extends BaseActivity {
	
	private ImageView common_title_leftbtn;
	private ImageView common_title_rightbtn;
	private TextView tv_title;
	private ListView lv_catalogue;
	private CatalogueAdapter myAdapter;
	private int curOrder;
	private ArrayList<String> pseList = new ArrayList<String>();
	private ArrayList<String> reverseList = new ArrayList<String>();
	private boolean isPse = true;
	private BookInfo bookInfo;
	private String bookName = "金庸小说全集";
	private ArrayList<ChapterInfo> chapterInfos; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalogue);
		curOrder = getIntent().getIntExtra(PageActivity.CURRENTPAGE_KEY, 1);
		bookInfo = (BookInfo) getIntent().getSerializableExtra(PageActivity.BOOKINFO_KEY);
		initData();
		initList();
		initView();
	}
	
	private void initView(){
		common_title_leftbtn = (ImageView) findViewById(R.id.common_title_leftbtn);
		common_title_rightbtn = (ImageView) findViewById(R.id.common_title_rightbtn);
		tv_title = (TextView) findViewById(R.id.tv_title);
		lv_catalogue = (ListView) findViewById(R.id.lv_catalogue);
		
		common_title_leftbtn.setImageResource(R.drawable.back);
		common_title_rightbtn.setImageResource(R.drawable.zhengxu);
		tv_title.setText(bookName);
		
		myAdapter = new CatalogueAdapter(this,curOrder);
		isPse = true;
		myAdapter.setList(pseList, chapterInfos,isPse);
		lv_catalogue.setAdapter(myAdapter);
		
		common_title_leftbtn.setOnClickListener(this);
		common_title_rightbtn.setOnClickListener(this);
	}
	
	private void initList(){
		for (int i = 1; i <= chapterInfos.size(); i++) {
			pseList.add("第"+i+"章");
		}
		for (int i = chapterInfos.size(); i > 0; i--) {
			reverseList.add("第"+i+"章");
		}
	}
	
	private void initData(){
		if (bookInfo != null) {
			bookName = bookInfo.getName();
			chapterInfos = bookInfo.getChapterInfos();
		}else{
			showShortToast("获取章节失败");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.common_title_leftbtn:
			onBackPressed();
			break;
		case R.id.common_title_rightbtn:
			// 右边按钮->正序倒序
			if (isPse && chapterInfos != null) {
				isPse = false;
				common_title_rightbtn.setImageResource(R.drawable.daoxu);
				myAdapter.setList(reverseList,chapterInfos,isPse);
				myAdapter.notifyDataSetChanged();
			}else if(!isPse && chapterInfos != null){
				isPse = true;
				common_title_rightbtn.setImageResource(R.drawable.zhengxu);
				myAdapter.setList(pseList,chapterInfos,isPse);
				myAdapter.notifyDataSetChanged();
			}else{
				showShortToast("获取章节失败");
			}
			break;
		}
	}
}
