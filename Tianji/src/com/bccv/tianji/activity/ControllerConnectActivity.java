package com.bccv.tianji.activity;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bccv.tianji.R;
import com.bccv.tianji.adapter.MachineAdapter;
import com.utils.net.TcpClientHelper;
import com.utils.net.TcpServerHelper;
import com.utils.net.TcpServerHelper.Machine;
import com.utils.net.UdpHelper;
import com.utils.net.WifiUtils;
import com.utils.tools.AnimationManager;
import com.utils.tools.BaseActivity;
import com.utils.tools.GlobalParams;

public class ControllerConnectActivity extends BaseActivity {
	private Button connectButton, settingButton;
	private TextView logTextView, wifiTextView, textView, textView2;
	private boolean isConnecting = false;
	private WifiUtils wifiUtils;
	private Timer timer;
	private ListView listView;
	private List<Machine> list;
	private MachineAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controllerconnect);
		setBack();
		setView();
	}

	private ImageView backTextView;

	private void setBack() {
		backTextView = (ImageView) findViewById(R.id.back_textView);
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();		
			}
		});
	}
	
	TcpClientHelper tcpHelper;

	private void setView() {
		wifiUtils = new WifiUtils(getApplicationContext());

		
		listView = (ListView) findViewById(R.id.listView);
		list = new ArrayList<Machine>();
		adapter = new MachineAdapter(getApplicationContext(), list);
		listView.setAdapter(adapter);
		listView.setSelector(new ColorDrawable(android.R.color.transparent));
		listView.setVisibility(View.GONE);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				for (int i = 0; i < list.size(); i++) {
					if (i != position) {
						try {
							list.get(i).getSocket().close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				GlobalParams.controllerSocket = list.get(position).getSocket();
				Intent intent = new Intent(getApplicationContext(), ControllerActivity.class);
				startActivity(intent);
				
				
			}
		});
		
		logTextView = (TextView) findViewById(R.id.log_textView);
		wifiTextView = (TextView) findViewById(R.id.wifi_textView);
		textView = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		settingButton = (Button) findViewById(R.id.setting_button);
		settingButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
				startActivity(intent);
			}
		});

		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(2);
			}
		};

		timer.schedule(task, 100, 5000);

		connectButton = (Button) findViewById(R.id.connect_button);
		connectButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!isConnecting) {
					isConnecting = true;
					connectButton.setSelected(true);
					if (!GlobalParams.tcpServerHelper.getIsStarting()) {
						GlobalParams.tcpServerHelper.startServer(GlobalParams.ControllerServerPort, handler);
					}
					// 传递WifiManager对象，以便在UDPHelper类里面使用MulticastLock
					// udpHelper.addObserver(ControllerConnectActivity.this);
					Thread tReceived = new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							GlobalParams.udpHelper.send("aaaa");
						}
					});
					tReceived.start();
					AnimationManager.setRotateAnimation(connectButton, 0,
							360 * 5, 5000, 0, new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									// TODO Auto-generated method stub
									isConnecting = false;
									connectButton.setSelected(false);
								}
							});
				}
			}
		});

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				List<Machine> getList = GlobalParams.tcpServerHelper.getClientSockets();
				if (getList != null) {
					list.clear();
					list.addAll(getList);
					adapter.notifyDataSetChanged();
					setMachine();
				}
				break;
			case 2:
				setWifi();
				break;
			case 3:
				adapter.notifyDataSetChanged();
			default:
				break;
			}
		};
	};

	private void setMachine(){
		if (list.size() > 0) {
			listView.setVisibility(View.VISIBLE);
			textView.setVisibility(View.GONE);
			textView2.setVisibility(View.GONE);
		}else {
			listView.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
			textView2.setVisibility(View.VISIBLE);
		}
	}
	
	private void setWifi() {

		WifiInfo wifiInfo = wifiUtils.getWifiInfo();
		int Ip = wifiInfo.getIpAddress();

		if (Ip == 0) {
			logTextView.setVisibility(View.GONE);
			wifiTextView.setText("当前无网络");
			textView.setVisibility(View.GONE);
			textView2.setVisibility(View.GONE);
			settingButton.setVisibility(View.VISIBLE);
		} else {
			logTextView.setVisibility(View.VISIBLE);
			wifiTextView.setText(wifiInfo.getSSID());
			setMachine();
			settingButton.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (wifiUtils != null) {
			if (timer != null) {
				timer.cancel();
			}
			timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(2);
				}
			};

			timer.schedule(task, 100, 5000);
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			if (GlobalParams.controllerSocket != null) {
				GlobalParams.controllerSocket.close();
				GlobalParams.controllerSocket = null;
				GlobalParams.tcpServerHelper.closeServer();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
