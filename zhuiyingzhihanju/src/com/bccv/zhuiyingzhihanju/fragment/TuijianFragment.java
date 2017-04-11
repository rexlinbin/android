package com.bccv.zhuiyingzhihanju.fragment;

import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.MovieInfoActivity;
import com.bccv.zhuiyingzhihanju.adapter.MovieListAdapter;
import com.bccv.zhuiyingzhihanju.model.Movie;
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

public class TuijianFragment extends BaseFragment {
	private View view;
	private List<Movie> list;
	private MovieListAdapter adapter;
	private GridView gridView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_tuijian, container, false);  
			gridView = (GridView) view.findViewById(R.id.gridView);

			list = new ArrayList<Movie>();
			adapter = new MovieListAdapter(getActivity().getApplicationContext(), list);
			gridView.setAdapter(adapter);
			gridView.setSelector(new ColorDrawable(android.R.color.transparent));
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Intent aIntent = new Intent(getActivity().getApplicationContext(), MovieInfoActivity.class);

					aIntent.putExtra("movie", list.get(position));

					startActivity(aIntent);
					getActivity().finish();
				}
			});
			getData();
		}
		return view;
	}
	
	public void getData(){
		Bundle args = getArguments();  
		@SuppressWarnings("unchecked")
		List<Movie> getList = (List<Movie>) args.getSerializable("tuijian");
		if (getList != null) {
			list.addAll(getList);
		}
		adapter.notifyDataSetChanged();
	}
}
