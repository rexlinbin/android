package com.bccv.strategy.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.strategy.R;
import com.bccv.strategy.model.AppInfoItemBean;
import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.activity.PersonalZoneActivity;
import com.bccv.strategy.ui.activity.StrategyDetailsActivity;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.utils.ImageLoaderUtil;

public class VpAdapter extends PagerAdapter {

	private ArrayList<AppInfoItemBean> data;
	private Context context;

	private SparseArray<View> showViewsList;
	private ArrayList<View> removeViewsList;
	
	public VpAdapter(ArrayList<AppInfoItemBean> data, Context context) {
		this.context = context;
		this.data = data;
		showViewsList = new SparseArray<View>();
		removeViewsList = new ArrayList<View>();
		
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(showViewsList.get(position));
		removeViewsList.add(showViewsList.get(position));
		showViewsList.remove(getActuallyPosition(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		
		View view;
		final Holder holder;
		if(removeViewsList.size()>0){
			view = removeViewsList.remove(0);
			holder = (Holder) view.getTag();
		}else{
			view = View.inflate(context, R.layout.homepage_item_vpager, null);
			holder = new Holder();
			holder.appname = (TextView) view.findViewById(R.id.homepage_item_appname);
//			holder.appintro = (TextView) view.findViewById(R.id.homepage_item_appintro);
			holder.usericon = (ImageView) view.findViewById(R.id.homepage_item_usericon);
			holder.app_poster = (ImageView) view.findViewById(R.id.homepage_item_app_poster);
			holder.app_introduce = (TextView) view.findViewById(R.id.homepage_item_app_introduce);
			holder.username = (TextView) view.findViewById(R.id.homepage_item_username);
			holder.app_introduce.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(holder.app_introduce.getLineCount() > 7){
						int lineEndIndex = holder.app_introduce.getLayout().getLineEnd(6); //设置第7行打省略号
						int linestartIndex = holder.app_introduce.getLayout().getLineStart(6); //设置第7行打省略号
						if(lineEndIndex-linestartIndex>15){
							String text = holder.app_introduce.getText().subSequence(0, lineEndIndex-3) +"..."; 
							holder.app_introduce.setText(text);
						}
					}
				}
			});
			
		}
		
		//TODO 设置数据
		setData(holder, getActuallyPosition(position));

		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent appDetailsIntent = new Intent(context, StrategyDetailsActivity.class);
				appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR, ((MainActivity)context).backGroundView.getGradientColor());
				appDetailsIntent.putExtra(StrategyDetailsActivity.RAID_ID, data.get(getActuallyPosition(position)).getId());
				appDetailsIntent.putExtra(StrategyDetailsActivity.GAME_ID, data.get(getActuallyPosition(position)).getGame_id());
				context.startActivity(appDetailsIntent);
			}
		});
		
		holder.usericon.setOnClickListener(new OnClickListener(){  

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent personalIntent = new Intent(context, PersonalZoneActivity.class);
				personalIntent.putExtra(BackGroundView.BACKGROUND_COLOR, ((MainActivity)context).backGroundView.getGradientColor());
				personalIntent.putExtra(PersonalZoneActivity.ZONE_ID_KEY,data.get(getActuallyPosition(position)).getCreator_id());
				context.startActivity(personalIntent);
			}
			
		});
		
		
		view.setTag(holder);
		showViewsList.put(position, view);
		((ViewPager) container).addView(view);
		return view;
	}

	private class Holder {
		TextView appname;
//		TextView appintro;// 副标题
		ImageView usericon;
		ImageView app_poster;
		TextView app_introduce;// app介绍
		TextView username;

	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();

		for (int i = 0; i < showViewsList.size(); i++) {
			int key = showViewsList.keyAt(i);
			View view = showViewsList.get(key);
			Holder holder = (Holder) view.getTag();
			// TODO 设置数据
			setData(holder, getActuallyPosition(key));
		}

	}

	private void setData(Holder holder, int actuallyPosition) {
		AppInfoItemBean homeItemBean = data.get(actuallyPosition);

		// TextView appname;
		// TextView appintro;//副标题
		// ImageView usericon;
		// ImageView app_poster;
		// TextView app_introduce;//app介绍
		// TextView username;

		ImageLoaderUtil.getInstance(context).displayImage(
				homeItemBean.getNews_titlepic(), holder.app_poster,
				ImageLoaderUtil.getAppHomeShowImageOptions());
		ImageLoaderUtil.getInstance(context).displayImage(
				homeItemBean.getCreator_icon(), holder.usericon,
				ImageLoaderUtil.getUserIconImageOptions());
		holder.appname.setText(homeItemBean.getNews_title() == null ? ""
				: homeItemBean.getNews_title());
//		holder.appintro.setText(homeItemBean.getNews_ftitle() == null ? ""
//				: homeItemBean.getNews_ftitle());
		holder.app_introduce.setText(homeItemBean.getNews_introduce() == null ? ""
				: homeItemBean.getNews_introduce());
		holder.username.setText(homeItemBean.getCreator_name() == null ? ""
				: "- " + homeItemBean.getCreator_name());

	}

	private int getActuallyPosition(int position) {
		if (data == null || data.size() == 0) {
			return position;
		}
		int temp = position - getCount() / 2;
		if (temp >= 0) {
			return temp % (data.size());
		} else {
			if (temp % data.size() == 0) {
				return 0;
			}
			return temp % (data.size()) + data.size();
		}
	}

}
