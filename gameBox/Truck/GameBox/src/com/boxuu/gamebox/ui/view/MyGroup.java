package com.boxuu.gamebox.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boxuu.gamebox.R;
import com.boxuu.gamebox.download.DownloadManager;

public class MyGroup extends RelativeLayout {
	
	private ProgressBar pb_download;
	private TextView tv_name;
	private ImageView iv_logo;
	private TextView tv_state;
	private RelativeLayout rl;
	
	public MyGroup(Context context , DownloadManager manager){
		this(context);
	}

	public MyGroup(Context context) {
		super(context);
		initView(context);
	}

	public MyGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private void initView(Context context){
		View inflate = LayoutInflater.from(context).inflate(R.layout.gridview_item, this);
		iv_logo = (ImageView) inflate.findViewById(R.id.game_logo);
		tv_name = (TextView) inflate.findViewById(R.id.game_name);
		tv_state = (TextView) inflate.findViewById(R.id.game_state);
		pb_download = (ProgressBar) inflate.findViewById(R.id.game_pro);
		rl = (RelativeLayout) inflate.findViewById(R.id.rl_root);
	}

	public ProgressBar getBar(){
		return pb_download == null ? null : pb_download;
	}
	
	public RelativeLayout getLayout(){
		return rl;
	}
	
	public TextView getStateText(){
		return tv_state;
	}
	
	public TextView getNameText(){
		return tv_name;
	}
	
	public ImageView getLogoImage() {
		return iv_logo;
	}

}
