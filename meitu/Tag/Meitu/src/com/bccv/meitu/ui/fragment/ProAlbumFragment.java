package com.bccv.meitu.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.bccv.meitu.model.Special;
import com.bccv.meitu.ui.activity.AlbumActivity;
import com.bccv.meitu.ui.activity.ProZoneActivity;
import com.bccv.meitu.utils.StringUtil;
import com.bccv.meitu.view.ListViewForScrollView;
import com.nostra13.universalimageloader.utils.ImageLoaderUtil;

public class ProAlbumFragment extends Fragment {
	private ListViewForScrollView pro_album_listView;
	private MyAdapter myAdapter;
	private Activity context;
	private ProZoneActivity activity;
	private List<Special> specialList = new ArrayList<Special>();
	private List<Special> specialList2 = new ArrayList<Special>();
	private ImageView pro_tv_get_more;
	private int curPage = -1;
	private int totalPage = -1;
	@Override
	public void onAttach(Activity activity) {
		this.context = activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.promulgator_album_list, container, false);
		pro_album_listView = (ListViewForScrollView) view.findViewById(R.id.pro_album_lv);
		View viewFooter = inflater.inflate(R.layout.pro_album_list_footer, null);
		pro_tv_get_more = (ImageView) viewFooter.findViewById(R.id.pro_tv_get_more);
		pro_tv_get_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(totalPage - curPage > 0){
					activity.getData(curPage + 1,true,false);
				}else{
					Toast.makeText(activity, "暂无更多数据", Toast.LENGTH_SHORT).show();
				}
			}
		});
		if(curPage == -1 || totalPage == -1){
			pro_tv_get_more.setVisibility(View.GONE);
		}
		pro_album_listView.addFooterView(viewFooter);
		myAdapter = new MyAdapter();
		pro_album_listView.setAdapter(myAdapter);
		return view;
	}
	
	public void setList(List<Special> list,int cur,int total,ProZoneActivity act){
		if(list != null && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				this.specialList.add(list.get(i));
				this.specialList2.add(list.get(i));
			}
//			this.specialList = list;
			myAdapter.notifyDataSetChanged();
			activity = act;
			curPage = cur;
			totalPage = total;
			activity.callBack();
			if(curPage == -1 || totalPage == -1 || curPage == totalPage){
				pro_tv_get_more.setVisibility(View.GONE);
			}else{
				pro_tv_get_more.setVisibility(View.VISIBLE);
			}
		}
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return specialList == null?1:specialList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if(convertView != null){
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else{
				holder = new ViewHolder();
				view = View.inflate(context, R.layout.promulgator_album_list_item, null);
				holder.pro_iv_album = (ImageView) view.findViewById(R.id.pro_iv_album_item);
				holder.pro_tv_name = (TextView) view.findViewById(R.id.pro_tv_album_name);
				holder.pro_tv_num = (TextView) view.findViewById(R.id.pro_tv_album_num);
				holder.pro_tv_time = (TextView) view.findViewById(R.id.pro_tv_album_time);
				holder.pro_tv_no_album = (TextView) view.findViewById(R.id.pro_tv_no_album);
				holder.pro_rl_album = (RelativeLayout) view.findViewById(R.id.pro_rl_album);
				view.setTag(holder);
			}
			if(specialList != null && specialList.size()>0){
				holder.pro_rl_album.setVisibility(View.VISIBLE);
				holder.pro_tv_no_album.setVisibility(View.GONE);
				getAllData(holder,position);
				final int key = holder.id;
				ImageLoaderUtil.getInstance(getActivity()).displayImage(holder.url, holder.pro_iv_album);
				holder.pro_iv_album.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),AlbumActivity.class);
						intent.putExtra("special_id", String.valueOf(key));
						Log.e("ProAlbumFragment", "id :"+key);
						getActivity().startActivity(intent);
					}
				});
				holder.pro_tv_name.setText(holder.name);
				holder.pro_tv_num.setText(String.valueOf(holder.num));
				holder.pro_tv_time.setText(StringUtil.friendly_time(holder.time));
//				if(position == ( specialList.size() -1 )){
//					pro_tv_get_more.setVisibility(View.VISIBLE);
//					pro_tv_get_more.setOnClickListener(new OnClickListener() {
//						@Override
//						public void onClick(View v) {
//							for (int i = 0; i < specialList2.size(); i++) {
//								specialList.add(specialList2.get(i));
//							}
//							myAdapter.notifyDataSetChanged();
//							Toast.makeText(activity, "dianji", 0).show();
//							activity.callBack();
//						}
//					});
//				} 
			}else{
				holder.pro_rl_album.setVisibility(View.GONE);
				holder.pro_tv_no_album.setVisibility(View.VISIBLE);
			}
			return view;
		}
		
		private void getAllData(ViewHolder holder,int position) {
			holder.url = specialList.get(position).getSpecial_pic();
			holder.name = specialList.get(position).getSpecial_name();
			holder.time = specialList.get(position).getCtime();
			holder.num = specialList.get(position).getPic_num();
			holder.id = specialList.get(position).getSpecial_id();
		}
	}
	
	class ViewHolder{
		String url ;
		String name;
		int id;
		int num;
		String time;
		ImageView pro_iv_album;
		TextView pro_tv_name;
		TextView pro_tv_num;
		TextView pro_tv_time;
		RelativeLayout pro_rl_album;
		TextView pro_tv_no_album;
	}
	
}
