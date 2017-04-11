package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
import com.bccv.zhuiyingzhihanju.adapter.InfoEpisodeAdapter;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;
import com.tendcloud.tenddata.ad;
import com.utils.tools.BaseFragment;
import com.utils.tools.StringUtils;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class InfoEpisodeFragment extends BaseFragment {
	View view;
	private List<MovieEpisode> list;
	private InfoEpisodeAdapter adapter;
	private GridView gridView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_info_episode, container, false);
			initView();
			getData();
		}
		return view;
	}
	
	private void initView(){
		list = new ArrayList<>();
		adapter = new InfoEpisodeAdapter(getActivity(), list);
		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView.setNumColumns(6);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setSelect(false);
				}
				list.get(position).setSelect(true);
				adapter.notifyDataSetChanged();
				((Video2DPlayerActivity)getActivity()).changeEpisode(list.get(position).getId());
			}
		});
	}
	
	public void getData(){
		Bundle args = getArguments();  
		@SuppressWarnings("unchecked")
		List<MovieEpisode> getList = (List<MovieEpisode>) args.getSerializable("list");
		if (getList != null) {
			list.addAll(getList);
		}
		adapter.notifyDataSetChanged();
	}
}
