package com.bccv.tianji.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bccv.tianji.R;
import com.bccv.tianji.activity.ScreenShotActivity;
import com.bccv.tianji.adapter.IntroductionAdapter;
import com.bccv.tianji.api.GameApi;
import com.bccv.tianji.api.TcpApi;
import com.bccv.tianji.model.Game;
import com.bccv.tianji.model.Gameinfo;
import com.utils.tools.AppConfig;
import com.utils.tools.GlobalParams;
import com.utils.tools.Logger;
import com.utils.tools.StringUtils;

@SuppressLint("NewApi")
public class IntroductionFragment extends Fragment {
	private GridView gridView;
	private TextView describe;
	private ImageView image;
	private Gameinfo data;
	private IntroductionAdapter adapter;
	private List<String> listData = new ArrayList<String>();;
	private String GameId;
	private String introduce;
	ProgressBar downImage;
	private TextView downText;
	private List<Game> downloadingList;
	private Timer timer;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_introduction, null);
		gridView = (GridView) view.findViewById(R.id.introduction_gridView);

		describe = (TextView) view.findViewById(R.id.introduction_describe);
		downText = (TextView) view.findViewById(R.id.downText);
		downText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			
				if (GlobalParams.user != null
						&& !StringUtils.isEmpty(GlobalParams.auth_id)) {
					long currTime = System.currentTimeMillis();
					if (currTime - data.getOptTime() < 10000) {
						Toast.makeText(getActivity().getApplicationContext()
								, "操作太频繁", 1).show();
						return;
					}
				
					data.setOptTime(currTime);
					Timer timer = new Timer();
					TimerTask task = new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							TcpApi tcpApi = new TcpApi();
							if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
								if (data.getUser_downloadstatus().equals(
										"normal")) {
									tcpApi.download(data.getTask_id(),
											GlobalParams.user.getUser_id(),
											data.getGame_id(),
											GlobalParams.auth_id);
								} else if (data.getUser_downloadstatus()
										.equals("stop")) {
									tcpApi.startDownload(data.getTask_id(),
											GlobalParams.user.getUser_id(),
											data.getGame_id(),
											GlobalParams.auth_id);
								} else if (data.getUser_downloadstatus()
										.equals("download")) {
									tcpApi.pauseDownload(data.getTask_id(),
											GlobalParams.user.getUser_id(),
											data.getGame_id(),
											GlobalParams.auth_id);
								}

							}

						}
					};

					if (GlobalParams.user != null
							&& !StringUtils.isEmpty(GlobalParams.auth_id)) {
						if (data.getUser_downloadstatus().equals("normal")) {
							timer.schedule(task, 100);
						} else if (data.getUser_downloadstatus().equals("stop")) {
							timer.schedule(task, 100);
						} else if (data.getUser_downloadstatus().equals(
								"download")) {
							timer.schedule(task, 100);
						}
						else if (data.getUser_downloadstatus().equals(
								"download")) {
							timer.schedule(task, 100);
						}
						Toast.makeText(getActivity(), AppConfig.getDown(), Toast.LENGTH_SHORT).show();
					}
						
						
					
				
					
				}else{
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
				}
			}
		});

		downImage = (ProgressBar) view.findViewById(R.id.progressBar);
		GameId = getGameId();

		new IntroductionTask().execute();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getActivity(),
						ScreenShotActivity.class);

				intent.putExtra("images", (ArrayList<String>) listData);// 非必须
				intent.putExtra("position", position);

				// int[] location = new int[2];
				// imageView1.getLocationOnScreen(location);
				// intent.putExtra("locationX", location[0]);// 必须
				// intent.putExtra("locationY", location[1]);// 必须
				//
				// intent.putExtra("width", imageView1.getWidth());// 必须
				// intent.putExtra("height", imageView1.getHeight());// 必须
				startActivity(intent);

			}
		});

		return view;
	}

	class IntroductionTask extends AsyncTask<String, Void, Gameinfo> {

		@Override
		protected Gameinfo doInBackground(String... params) {
			GlobalParams.tcpClientHelper.setHandler(handler);
			GameApi gameApi = new GameApi();
			String useridString = "";
			if (GlobalParams.hasLogin) {
				useridString = GlobalParams.user.getUser_id();
			}
			data = gameApi.getGameInfoList(useridString, GameId);
			return data;
		}

		@Override
		protected void onPostExecute(Gameinfo list) {
			super.onPostExecute(list);
			if(list!=null){
				introduce = list.getIntro();
				listData = list.getPics();

				adapter = new IntroductionAdapter(listData, getActivity());
				gridView.setAdapter(adapter);
				describe.setText(introduce);
				refreshDownload();
			}
		

			if (timer != null) {
				timer.cancel();
				timer = new Timer();
			} else {
				timer = new Timer();
			}
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					TcpApi tcpApi = new TcpApi();
					if (!StringUtils.isEmpty(GlobalParams.auth_id)) {
						tcpApi.getDownloadGameState(data.getTask_id(),
								GlobalParams.user.getUser_id(),
								GlobalParams.auth_id);
					}
				}
			};
			if (GlobalParams.user != null
					&& !StringUtils.isEmpty(GlobalParams.auth_id)) {
				timer.schedule(task, 1000, 10000);
			}
		}
	}

	public String getGameId() {
		return GameId;
	}

	public void setGameId(String gameId) {
		GameId = gameId;
	}

	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				String rdata = (String) msg.obj;
				Logger.e("data111", rdata);
				if (!StringUtils.isEmpty(rdata)) {
					try {
						JSONObject jsonObject = JSON.parseObject(rdata);
						String flag = jsonObject.getString("flag");
						if (flag.equals("task_list")) {
							JSONArray jsonArray = jsonObject
									.getJSONArray("info");

							for (int j = 0; j < jsonArray.size(); j++) {
								JSONObject jsonObject2 = jsonArray
										.getJSONObject(j);
								if (data.getGame_id().equals(
										jsonObject2.getString("game_id"))) {
									data.setUser_downloadstatus(jsonObject2
											.getString("download_status"));
									data.setDownload_size(jsonObject2
											.getFloatValue("download_size"));
									refreshDownload();
									break;
								}
							}
						}else if(flag.equals("pc")){
							String eerText = jsonObject.getString("message");
//						downText.setText(eerText);
							AppConfig.isDownLoad(eerText);
						}
						
						
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;

			default:
				break;
			}
		}
	};

	private void refreshDownload() {
		if (data.getUser_downloadstatus().equals("normal")) {
			downText.setText("下载");
			downImage.setProgress(0);
		} else if (data.getUser_downloadstatus().equals("waiting")) {
			downText.setText("等待中");
			downImage.setProgress(0);
		} else if (data.getUser_downloadstatus().equals("stop")) {
			downText.setText("继续");
			downImage.setProgress((int) data.getDownload_size());
		} else if (data.getUser_downloadstatus().equals("download")) {
			downText.setText("已下载" + (int) data.getDownload_size() + "%");
			downImage.setProgress((int) data.getDownload_size());
		} else if (data.getUser_downloadstatus().equals("complete")) {
			downText.setText("已完成");
			downImage.setProgress(100);
		}
	}

}
