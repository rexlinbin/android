package com.bccv.zhuiying.fragment;


import com.bccv.zhuiying.R;
import com.tendcloud.tenddata.TCAgent;
import com.utils.tools.BaseFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class IntroFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle args = getArguments();  
        String intro = args.getString("intro");
        String director = args.getString("director");
        String casts = args.getString("casts");
        View view = inflater.inflate(R.layout.fragment_intro, container, false);  
        TextView viewhello = (TextView) view.findViewById(R.id.intro_textView);
        viewhello.setText("\n" + director + "\n" + casts + "\n\n\n" + "简介：" + intro);  
        return view;  
	}
	
}
