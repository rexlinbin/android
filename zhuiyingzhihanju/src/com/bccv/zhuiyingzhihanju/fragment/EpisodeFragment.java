package com.bccv.zhuiyingzhihanju.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bccv.zhuiyingzhihanju.R;
import com.bccv.zhuiyingzhihanju.activity.MovieInfoActivity;
import com.bccv.zhuiyingzhihanju.adapter.EpisodeAdapter;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.bccv.zhuiyingzhihanju.model.MovieEpisode;
import com.utils.tools.BaseFragment;
import com.utils.tools.SerializationUtil;
import com.utils.tools.StringUtils;
import com.utils.tools.SystemUtils;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class EpisodeFragment extends BaseFragment {
	private View view;
	private List<MovieEpisode> list;
	private List<Movie> historyList;
	private EpisodeAdapter adapter;
	private GridView gridView;
	private Movie movie;
	private boolean isWifi, isStart = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_episode, container, false);
			gridView = (GridView) view.findViewById(R.id.gridView);

			list = new ArrayList<MovieEpisode>();
			adapter = new EpisodeAdapter(getActivity().getApplicationContext(), list);
			gridView.setAdapter(adapter);
			gridView.setSelector(new ColorDrawable(android.R.color.transparent));
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					movie.setPlay_Date(System.currentTimeMillis());
					movie.setEpisode_id(list.get(position).getId());
					for (int i = 0; i < historyList.size(); i++) {
						Movie historyMovie = historyList.get(i);
						if (historyMovie.getId().equals(movie.getId())
								&& historyMovie.getType_id().equals(movie.getType_id())) {
							if (historyMovie.getEpisode_id().equals(movie.getEpisode_id())) {
								movie.setPlay_Time(historyMovie.getPlay_Time());
							}
							historyList.remove(historyMovie);
							break;
						}
					}
					((MovieInfoActivity) getActivity()).setMovieSource(movie);
					historyList.add(0, movie);
					SerializationUtil.wirteHistorySerialization(getActivity().getApplicationContext(),
							(Serializable) historyList);

					if (!isWifi) {
						String netState = SystemUtils.getNetState(getActivity().getApplicationContext());
						if (!netState.equals("WIFI")) {
							isWifi = false;
						}
					}

					if (!isWifi) {
						((MovieInfoActivity) getActivity()).showWifiDialog(getActivity(), movie, position);
						return;
					}
					((MovieInfoActivity) getActivity()).startPlay(movie, position);
				}
			});
			getData();
		}
		return view;
	}

	@SuppressWarnings("unchecked")
	public void getData() {
		Bundle args = getArguments();
		isWifi = args.getBoolean("isWifi", false);
		historyList = (List<Movie>) args.getSerializable("historyList");
		movie = (Movie) args.getSerializable("movie");
		List<MovieEpisode> getList = (List<MovieEpisode>) args.getSerializable("episode");
		if (getList != null) {
			list.addAll(getList);
		}

		if (historyList == null) {
			historyList = new ArrayList<Movie>();
		}

		String historyEpisodeid = "1";
		boolean hasHistory = false;
		for (int i = 0; i < historyList.size(); i++) {
			Movie historyMovie = historyList.get(i);
			if (historyMovie.getId().equals(movie.getId()) && historyMovie.getType_id().equals(movie.getType_id())) {
				if (!StringUtils.isEmpty(historyMovie.getEpisode_id())) {
					historyEpisodeid = historyMovie.getEpisode_id();
					hasHistory = true;
				}

				break;
			}

		}

		for (int i = 0; i < list.size(); i++) {
			if (hasHistory) {
				if (list.get(i).getId().equals(historyEpisodeid)) {
					list.get(i).setSelect(true);
				} else {
					list.get(i).setSelect(false);
				}

			}else{
				list.get(i).setSelect(false);
			}

		}

		adapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!isStart) {
			isStart = true;
			return;
		} else {
			refreshEpisode();
		}
		// getData();
	}

	public void refreshEpisode() {
		historyList = SerializationUtil.readHistoryCache(getActivity().getApplicationContext());
		if (historyList == null) {
			historyList = new ArrayList<Movie>();
		}

		int historyEpisodeid = 1;
		boolean hasHistory = false;
		for (int i = 0; i < historyList.size(); i++) {
			Movie historyMovie = historyList.get(i);
			if (historyMovie.getId().equals(movie.getId()) && historyMovie.getType_id().equals(movie.getType_id())) {
				if (!StringUtils.isEmpty(historyMovie.getEpisode_id())) {
					historyEpisodeid = Integer.parseInt(historyMovie.getEpisode_id());
					hasHistory = true;
				}

				break;
			}

		}

		if (hasHistory) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getId().equals(historyEpisodeid + "")) {
					list.get(i).setSelect(true);
				} else {
					list.get(i).setSelect(false);
				}

			}

		}
		adapter.notifyDataSetChanged();

	}
	
	public void resetData(List<MovieEpisode> newList){
		list.clear();
		list.addAll(newList);
		refreshEpisode();
	}
}
