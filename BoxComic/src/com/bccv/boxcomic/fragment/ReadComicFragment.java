package com.bccv.boxcomic.fragment;

import com.bccv.boxcomic.R;
import com.bccv.boxcomic.activity.ReadComicActivity.OnFragmentClickListener;
import com.bccv.boxcomic.ebook.StringUtil;
import com.bccv.boxcomic.photoview.PhotoView;
import com.bccv.boxcomic.photoview.PhotoViewAttacher.OnViewTapListener;
import com.bccv.boxcomic.tool.GlobalParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class ReadComicFragment extends Fragment {
	private View view;
	private ImageView photoView;
	private OnFragmentClickListener onFragmentClickListener;

	public ReadComicFragment() {

	}

	@SuppressLint("ValidFragment")
	public ReadComicFragment(OnFragmentClickListener onFragmentClickListener) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_readcomic, null);
//		onFragmentClickListener = (OnFragmentClickListener) getArguments()
//				.getSerializable("onFragmentClickListener");
		photoView = (ImageView) view.findViewById(R.id.photoView);
		
		int num = getArguments().getInt("num");
		boolean isLocal = getArguments().getBoolean("isLocal");
		
		TextView textView = (TextView) view.findViewById(R.id.num_textView);
		textView.setText(num + "");
		
		String imageUrl = getArguments().getString("imageUrl");

		if (StringUtil.isEmpty(imageUrl)) {
			textView.setText("此页暂缺");
			if (isLocal) {
				GlobalParams.readComicLocalActivity.fragmentSend((num + 1) + "");
			}else {
				GlobalParams.readComicActivity.fragmentSend((num + 1) + "");
			}
			
		}
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader
				.displayImage(imageUrl, photoView, GlobalParams.comicOptions);
		setOnFragmentClick(onFragmentClickListener, isLocal);
		return view;
	}

	public void setOnFragmentClick(
			final OnFragmentClickListener onFragmentClickListener, final boolean isLocal) {
		photoView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isLocal) {
						GlobalParams.readComicLocalActivity.setClick(event.getX(), event.getY());
					}else {
						GlobalParams.readComicActivity.setClick(event.getX(), event.getY());
					}
						

				}
				
				return true;
			}
		});
	}
}
