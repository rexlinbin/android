package com.bccv.boxcomic.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import com.bccv.boxcomic.activity.ReadComicActivity.OnFragmentClickListener;
import com.bccv.boxcomic.fragment.ReadComicFragment;
import com.bccv.boxcomic.modal.ComicPic;
import com.bccv.boxcomic.tool.GlobalParams;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;

public class ReadComicPagerAdapter extends FragmentPagerAdapter {

	private List<ComicPic> list;

	private OnFragmentClickListener onFragmentClickListener;

	private boolean isHigh = true;
	private boolean isLocal = true;

	public ReadComicPagerAdapter(FragmentManager fm, List<ComicPic> list,
			OnFragmentClickListener onFragmentClickListener, boolean isLocal) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.list = list;
		this.onFragmentClickListener = onFragmentClickListener;
		this.isLocal = isLocal;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		ReadComicFragment fragment = new ReadComicFragment();
		Bundle args = new Bundle();
		String imageUrlString = null;
		if (list.get(position).isLocal()) {
			imageUrlString = list.get(position).getUrl();
		}else {
			imageUrlString = list.get(position).getUrl();
		}
		
		if (list.get(position).isLocal()) {
			try {
				imageUrlString = URLDecoder.decode(imageUrlString, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				imageUrlString = URLEncoder.encode(imageUrlString,"utf-8").replaceAll("\\+", "%20");
				imageUrlString = imageUrlString.replaceAll("%3A", ":").replaceAll("%2F", "/");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		args.putString("imageUrl",
				imageUrlString);
		args.putInt("num", position + 1);
		args.putBoolean("isLocal", isLocal);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	public void setIsHigh(boolean isHigh) {
		this.isHigh = isHigh;
	}
}
