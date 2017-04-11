package com.bccv.tianji.adapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bccv.tianji.R;
import com.bccv.tianji.adapter.ClassifyListAdapter.ViewHolder;
import com.bccv.tianji.api.TcpApi;
import com.bccv.tianji.model.Game;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.utils.tools.GlobalParams;
import com.utils.tools.StringUtils;
import com.utils.views.TextProgressBar;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyDownloadAdapter extends BaseAdapter {
	private Context context;
	private List<Game> list;
	private boolean isEdit = false;

	public MyDownloadAdapter(Context context, List<Game> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			viewHolder = new ViewHolder();
			arg1 = View.inflate(context, R.layout.listitem_mydownload, null);
			viewHolder.iconImageView = (ImageView) arg1
					.findViewById(R.id.icon_imageView);
			viewHolder.selectImageView = (ImageView) arg1
					.findViewById(R.id.select_imageView);
			viewHolder.titleTextView = (TextView) arg1
					.findViewById(R.id.title_textView);
			viewHolder.scoreTextView = (TextView) arg1
					.findViewById(R.id.score_textView);
			viewHolder.typeTextView = (TextView) arg1
					.findViewById(R.id.type_textView);
			viewHolder.introTextView = (TextView) arg1
					.findViewById(R.id.intro_textView);
			viewHolder.downloadTextView = (TextView) arg1
					.findViewById(R.id.download_textView);
			viewHolder.progressBar = (ProgressBar) arg1
					.findViewById(R.id.progressBar);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}

		final Game game = list.get(arg0);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(game.getIcons(), viewHolder.iconImageView,
				GlobalParams.iconOptions);

		viewHolder.titleTextView.setText(game.getTitle());
		AssetManager mgr = context.getAssets();// 得到AssetManager
		Typeface tf = Typeface.createFromAsset(mgr, "fonts/Georgia.ttf");// 根据路径得到Typeface
		viewHolder.scoreTextView.setTypeface(tf);// 设置字体
		viewHolder.scoreTextView.setText(game.getPoint());
		viewHolder.typeTextView.setText(game.getType_name());
		viewHolder.introTextView.setText(game.getDown_num() + "次下载            "
				+ game.getGame_size() + "GB");

		if (game.getStatus().equals("normal")) {
			viewHolder.downloadTextView.setText("下载");
			viewHolder.downloadTextView.setBackgroundResource(R.drawable.frame);
			viewHolder.progressBar.setVisibility(View.GONE);
		} else if (game.getStatus().equals("waiting")) {
			viewHolder.downloadTextView.setText("等待中");
			viewHolder.downloadTextView
					.setBackgroundResource(R.drawable.download_frame);
			viewHolder.progressBar.setVisibility(View.GONE);
		} else if (game.getStatus().equals("stop")) {
			viewHolder.downloadTextView.setText("继续");
			viewHolder.downloadTextView
					.setBackgroundResource(R.drawable.download_frame);
			viewHolder.progressBar.setVisibility(View.VISIBLE);
		} else if (game.getStatus().equals("download")) {
			viewHolder.downloadTextView.setText("已下载"
					+ (int) game.getDownload_size() + "%");
			viewHolder.downloadTextView
					.setBackgroundResource(R.drawable.download_frame);
			viewHolder.progressBar.setVisibility(View.VISIBLE);
			viewHolder.progressBar.setProgress((int) game.getDownload_size());
		} else if (game.getStatus().equals("complete")) {
			viewHolder.downloadTextView.setText("已完成");
			viewHolder.downloadTextView.setBackgroundResource(R.drawable.frame);
			viewHolder.progressBar.setVisibility(View.GONE);
		}

		viewHolder.downloadTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				long currTime = System.currentTimeMillis();
				if (currTime - game.getOptTime() < 10000) {
					Toast.makeText(context, "操作太频繁", 1).show();
					return;
				}
				game.setOptTime(currTime);
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						TcpApi tcpApi = new TcpApi();
						if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
							if (game.getStatus().equals("normal")) {
								tcpApi.download(game.getTask_id(), GlobalParams.user.getUser_id(), game.getGame_id(), GlobalParams.auth_id);
							}else if (game.getStatus().equals("stop")) {
								tcpApi.startDownload(game.getTask_id(), GlobalParams.user.getUser_id(), game.getGame_id(), GlobalParams.auth_id);
							} else if (game.getStatus().equals("download")) {
								tcpApi.pauseDownload(game.getTask_id(), GlobalParams.user.getUser_id(), game.getGame_id(), GlobalParams.auth_id);
							}
						}
					}
				};

				if (GlobalParams.user != null
						&& !StringUtils.isEmpty(GlobalParams.auth_id)) {
					if (game.getStatus().equals("normal")) {
						timer.schedule(task, 100);
					}else if (game.getStatus().equals("stop")) {
						timer.schedule(task, 100);
					} else if (game.getStatus().equals("download")) {
						timer.schedule(task, 100);
					}
				}
			}
		});
//		final ImageView imageView = viewHolder.selectImageView;
		
		if (isEdit) {
			viewHolder.selectImageView.setSelected(game.isSelect());
			viewHolder.selectImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					v.setSelected(!v.isSelected());
					game.setSelect(v.isSelected());
				}
			});
			viewHolder.selectImageView.setVisibility(View.VISIBLE);
		}else{
			viewHolder.selectImageView.setVisibility(View.GONE);
		}
		
		return arg1;
	}

	public void isEdit(boolean isEdit){
		this.isEdit = isEdit;
	}
	
	class ViewHolder {
		ImageView iconImageView;
		ImageView selectImageView;
		TextView titleTextView;
		TextView scoreTextView;
		TextView typeTextView;
		TextView introTextView;
		TextView downloadTextView;
		ProgressBar progressBar;
	}

}
