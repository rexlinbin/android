package com.bccv.boxcomic.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ComicInfoLocalActivity;
import com.bccv.boxcomic.activity.LocalWebActivity;
import com.bccv.boxcomic.adapter.LocalAdapter;
import com.bccv.boxcomic.modal.Comic;
import com.bccv.boxcomic.tool.FileUtils;
import com.bccv.boxcomic.tool.GlobalParams;
import com.bccv.boxcomic.tool.MyComparator;
import com.bccv.boxcomic.tool.MyGridView;

public class LocalFramgent extends Fragment {

	private MyGridView gridView;
	private List<Comic> data;
	private LocalAdapter adapter;
	private RelativeLayout LocalRelative;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View mView = inflater.inflate(R.layout.frament_local, null);

		gridView = (MyGridView) mView.findViewById(R.id.Local_Gridview);
		LocalRelative = (RelativeLayout) mView
				.findViewById(R.id.Local_Relative);
		String localPathString = GlobalParams.localComicPathString + "/";

		List<File> files = FileUtils.listPathDirs(localPathString);

		MyComparator myComparator = new MyComparator();
		Collections.sort(files, myComparator);

		data = new ArrayList<Comic>();
		for (int i = 0; i < files.size(); i++) {
			File file = files.get(i);
			Comic comic = new Comic();
			comic.setComic_title(file.getName());

			comic.setComic_id(file.getName());

			data.add(comic);
		}

		adapter = new LocalAdapter(getActivity(), data);

		gridView.setAdapter(adapter);
		gridView.setSelector(new ColorDrawable(android.R.color.transparent));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				Intent aintent = new Intent(getActivity(),
						ComicInfoLocalActivity.class);

				aintent.putExtra("mainitem", data.get(arg2).getComic_id());

				startActivity(aintent);

			}
		});
		LocalRelative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent aintent = new Intent(getActivity(),
						LocalWebActivity.class);

			

				startActivity(aintent);
			}
		});
		return mView;
	}

}
