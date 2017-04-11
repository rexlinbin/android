package com.bccv.tianji.adapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.tianji.R;
import com.bccv.tianji.api.TcpApi;
import com.bccv.tianji.model.Game;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;

public class MainAdapter extends BaseAdapter {

	List<Game> data;
	Context context;
	Handler handler;

	public MainAdapter(List<Game> data, Context context, Handler handler) {
		super();
		this.data = data;
		this.context = context;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;

		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.list_mainitem,
					null);

			viewHolder.image = (ImageView) view
					.findViewById(R.id.main_list_image);
			viewHolder.title = (TextView) view
					.findViewById(R.id.main_list_title);
			viewHolder.content = (TextView) view
					.findViewById(R.id.main_list_content);
			viewHolder.downloadTextView = (TextView) view.findViewById(R.id.download_textView);
			viewHolder.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			viewHolder.icon = (ImageView) view
					.findViewById(R.id.main_list_icon);
			viewHolder.downNum = (TextView) view
					.findViewById(R.id.main_list_num);
			viewHolder.downSpeed = (TextView) view
					.findViewById(R.id.main_list_speed);
			viewHolder.soreBtn = (Button) view
					.findViewById(R.id.main_list_score);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		final Game item = data.get(arg0);

		viewHolder.title.setText(item.getTitle());
		viewHolder.content.setText(item.getType_name());

		viewHolder.downNum.setText(item.getDown_num()+"次下载");
		viewHolder.downSpeed.setText(item.getGame_size()+"GB");
		AssetManager mgr = context.getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		viewHolder.soreBtn.setTypeface(tf);// 设置字体
		viewHolder.soreBtn.setText(item.getPoint());
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(item.getIcons(), viewHolder.image,
				GlobalParams.iconOptions);

		if (item.getUser_downloadstatus().equals("normal")) {
			viewHolder.downloadTextView.setText("下载");
			viewHolder.downloadTextView.setBackgroundResource(R.drawable.frame);
			viewHolder.progressBar.setVisibility(View.GONE);
		}else if (item.getUser_downloadstatus().equals("waiting")) {
			viewHolder.downloadTextView.setText("等待中");
			viewHolder.downloadTextView.setBackgroundResource(R.drawable.download_frame);
			viewHolder.progressBar.setVisibility(View.GONE);
		}else if (item.getUser_downloadstatus().equals("stop")) {
			viewHolder.downloadTextView.setText("继续");
			viewHolder.downloadTextView.setBackgroundResource(R.drawable.download_frame);
			viewHolder.progressBar.setVisibility(View.VISIBLE);
		}else if (item.getUser_downloadstatus().equals("download")) {
			viewHolder.downloadTextView.setText("已下载" + (int)item.getDownload_size() + "%");
			viewHolder.downloadTextView.setBackgroundResource(R.drawable.download_frame);
			viewHolder.progressBar.setVisibility(View.VISIBLE);
			viewHolder.progressBar.setProgress((int) item.getDownload_size());
		}else if (item.getUser_downloadstatus().equals("complete")) {
			viewHolder.downloadTextView.setText("已完成");
			viewHolder.downloadTextView.setBackgroundResource(R.drawable.frame);
			viewHolder.progressBar.setVisibility(View.GONE);
		}
		
		
		
		
		
		

		viewHolder.downloadTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				long currTime = System.currentTimeMillis();
				if (currTime - item.getOptTime() < 10000) {
					handler.sendEmptyMessage(11);
					return;
				}
				item.setOptTime(currTime);
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TcpApi tcpApi = new TcpApi();
						if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
							if (item.getUser_downloadstatus().equals("normal")) {
								tcpApi.download(item.getTask_id(), GlobalParams.user.getUser_id(), item.getGame_id(), GlobalParams.auth_id);
							}else if (item.getUser_downloadstatus().equals("stop")) {
								tcpApi.startDownload(item.getTask_id(), GlobalParams.user.getUser_id(), item.getGame_id(), GlobalParams.auth_id);
							} else if (item.getUser_downloadstatus().equals("download")) {
								tcpApi.pauseDownload(item.getTask_id(), GlobalParams.user.getUser_id(), item.getGame_id(), GlobalParams.auth_id);
							}
							
						}

					}
				};
				
				if (GlobalParams.user != null
						&& !StringUtils.isEmpty(GlobalParams.auth_id)) {
					if (item.getUser_downloadstatus().equals("normal")) {
						timer.schedule(task, 100);
					}else if (item.getUser_downloadstatus().equals("stop")) {
						timer.schedule(task, 100);
					} else if (item.getUser_downloadstatus().equals("download")) {
						timer.schedule(task, 100);
					}
					
						handler.sendEmptyMessage(1);
					
				}else{
					handler.sendEmptyMessage(2);
				}
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return view;
	}

	class ViewHolder {
		ImageView image;
		TextView title;
		TextView content;
		ImageView icon;
		TextView downNum;
		TextView downSpeed;
		TextView downloadTextView;
		ProgressBar progressBar;
		Button soreBtn;
	}

}
