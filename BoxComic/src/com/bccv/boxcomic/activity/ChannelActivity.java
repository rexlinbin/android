package com.bccv.boxcomic.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.adapter.ChannelAdapter;
import com.bccv.boxcomic.api.ChannelApi;
import com.bccv.boxcomic.fragment.SearchFragment;
import com.bccv.boxcomic.modal.Channel;
import com.bccv.boxcomic.net.NetUtil;
import com.bccv.boxcomic.pulltorefresh.FooterLoadingLayout;
import com.bccv.boxcomic.pulltorefresh.ILoadingLayout.State;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.bccv.boxcomic.pulltorefresh.PullToRefreshGridView;
import com.bccv.boxcomic.tool.BaseActivity;
import com.bccv.boxcomic.tool.Callback;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.MyGridView;
import com.bccv.boxcomic.tool.PromptManager;
import com.bccv.boxcomic.tool.SerializationUtil;



public class ChannelActivity extends BaseActivity {
	protected static final String TAG = null;
	private MyGridView comicGridView;
	private MyGridView ebookGridView;
	private ChannelAdapter comicAdapter;
	private ChannelAdapter ebookAdapter;
	private List<Channel> comicList;
	private List<Channel> ebookList;
	private List<Channel> getComicList;
	private List<Channel> getEbookList;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		GlobalParams.context = getApplicationContext();
		activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel);
	
		setBack();
		
		comicList = new ArrayList<Channel>();
		ebookList = new ArrayList<Channel>();
		
		comicGridView = (MyGridView) findViewById(R.id.comic_gridView);
		comicAdapter = new ChannelAdapter(this, comicList);
		comicGridView.setAdapter(comicAdapter);
		comicGridView.setSelector(new ColorDrawable(android.R.color.transparent));
		
		comicGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent aIntent = new Intent(ChannelActivity.this,
						SearchListActivity.class);
				aIntent.putExtra("type", "Channel");
				aIntent.putExtra("key", getComicList.get(arg2).getCat_id()+"");
				aIntent.putExtra("textTitle", getComicList.get(arg2).getCat_title());
				startActivity(aIntent);

				
			}
		});
		
		
		
		ebookGridView = (MyGridView) findViewById(R.id.ebook_gridView);
		
		TextView textView = (TextView) findViewById(R.id.ebooktitle_textView);
		if (!GlobalParams.hasBook) {
			textView.setVisibility(View.GONE);
			ebookGridView.setVisibility(View.GONE);
		}else {
			ebookAdapter = new ChannelAdapter(this, ebookList);
			ebookGridView.setAdapter(ebookAdapter);
			ebookGridView.setSelector(new ColorDrawable(android.R.color.transparent));
			
			ebookGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					
					Intent aIntent = new Intent(ChannelActivity.this,
							SearchListActivity.class);
					aIntent.putExtra("type", "EbookChannel");
					aIntent.putExtra("key", getEbookList.get(arg2).getCat_id()+"");
					aIntent.putExtra("textTitle", getEbookList.get(arg2).getCat_title());
					startActivity(aIntent);

					
				}
			});
		}
		
		fetchServiceData(false, false);
	}
	
	private void setBack(){
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.back_relativeLayout);
		relativeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	/**
	 * 加载本地数据
	 */
	public void loadLocalData() {

		canShowWebDialog = false;
		
		Callback localCallback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				try {
					comicList.clear();
					comicList.addAll(getComicList);
					if (GlobalParams.hasBook) {
						ebookList.clear();
						ebookList.addAll(getEbookList);
						
						ebookAdapter.notifyDataSetChanged();
					}
					
					comicAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "获取数据失败", 1).show();
				}
				
			}
		};

		new DataAsyncTask(localCallback, false) {

			protected String doInBackground(String... params) {
				if (getComicList == null) {
					getComicList = SerializationUtil.readComicChannelCache(getApplicationContext());
				}
				if (GlobalParams.hasBook) {
					if (getEbookList == null) {
						getEbookList = SerializationUtil.readEbookChannelCache(getApplicationContext());
					}
				}
				
				return null;
			};

		}.executeProxy("");
	}

	private void fetchServiceData(final boolean isLoadMore, boolean canProgress) {
		boolean isNetworkAvailable = NetUtil.isNetworkAvailable(activity);
		if (!isNetworkAvailable) {
			loadLocalData();
			PromptManager.showToast(activity, "网络不给力");
			return;
		}
		canShowWebDialog = true;
		
		Callback callback = new Callback() {

			@Override
			public void handleResult(String result) {
				// TODO Auto-generated method stub
				
				try {
					if (result.equals("refresh")) {
						comicList.clear();
						comicList.addAll(getComicList);
						if (GlobalParams.hasBook) {
							ebookList.clear();
							ebookList.addAll(getEbookList);
						}
						
						
					} else if (result.equals("more")) {
						
					} else if (result.equals("local")) {
						loadLocalData();
						return;
					}
					if (GlobalParams.hasBook) {
						ebookAdapter.notifyDataSetChanged();
					}
					
					comicAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
					Logger.e("ChannelActivity", "获取数据为空");
				}
				
			}
		};

		new DataAsyncTask(callback, canProgress) {

			@Override
			protected String doInBackground(String... params) {
				ChannelApi channelApi = new ChannelApi();
				try {
					getComicList = channelApi.getChannelList();
					if (GlobalParams.hasBook) {
						getEbookList = channelApi.getEbookChannelList();
						if (getComicList == null && getEbookList == null) {
							return "local";
						}
						if (getEbookList != null) {
							SerializationUtil.wirteEbookChannelSerialization(getApplicationContext(), (Serializable) getEbookList);
						}
					}else {
						if (getComicList == null) {
							return "local";
						}
					}
					
					
					if (getComicList != null) {
						SerializationUtil.wirteComicChannelSerialization(getApplicationContext(), (Serializable) getComicList);
					}
					
					
					
					return "refresh";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "local";
			}
		}.executeProxy("");

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}

}
