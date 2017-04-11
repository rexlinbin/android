package com.bccv.zhuiying.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiying.R;
import com.bccv.zhuiying.activity.MovieInfoActivity;
import com.bccv.zhuiying.activity.VideoTVPlayerActivity;
import com.bccv.zhuiying.adapter.MovieListAdapter;
import com.bccv.zhuiying.adapter.TVListAdapter;
import com.bccv.zhuiying.model.Movie;
import com.bccv.zhuiying.model.TV;
import com.utils.tools.BaseFragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class TVListFragment extends BaseFragment {
	private View view;
	private List<TV> list;
	private TVListAdapter adapter;
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_tvlist, container, false);  
			listView = (ListView) view.findViewById(R.id.listView);
			list = new ArrayList<TV>();
			adapter = new TVListAdapter(getActivity().getApplicationContext(), list);
			listView.setAdapter(adapter);
			listView.setSelector(new ColorDrawable(android.R.color.transparent));
			listView.setDivider(new ColorDrawable(android.R.color.transparent));
			listView.setDividerHeight(2);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Intent aIntent = new Intent(getActivity().getApplicationContext(), VideoTVPlayerActivity.class);

					aIntent.putExtra("tv", (Serializable)list);
					aIntent.putExtra("currTV", position);

					startActivity(aIntent);
				}
			});
			getData();
		}else{
			ViewGroup viewGroup = (ViewGroup) view.getParent();
			viewGroup.removeView(view);
		}
		return view;
	}
	
	public void getData(){
		Bundle args = getArguments();  
		@SuppressWarnings("unchecked")
		List<TV> getList = (List<TV>) args.getSerializable("tvList");
		if (getList != null) {
			list.addAll(getList);
		}
		adapter.notifyDataSetChanged();
	}
}
