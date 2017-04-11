package com.bccv.threedimensionalworld.view;

import com.bccv.threedimensionalworld.R;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MyTitleView extends LinearLayout {

	private Context context;
	private ImageView wifiImageView;

	public MyTitleView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public MyTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public MyTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(wifiReceiver, filter);

//		IntentFilter bluetoothFilter = new IntentFilter();
//		bluetoothFilter
//				.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//		context.registerReceiver(bluetoothReceiver, bluetoothFilter);

		View view = View.inflate(context, R.layout.view_mytitle, null);
		wifiImageView = (ImageView) view.findViewById(R.id.wifi_imageView);
		view.setScaleX(0.5f);
		addView(view);

	}

	
	public void setWifiState(boolean isConnect) {

	}

	public void setBluetoothState(boolean isConnect) {

	}

	private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {
				// signal strength changed
			} else if (intent.getAction().equals(
					WifiManager.NETWORK_STATE_CHANGED_ACTION)) {// wifi连接上与否
				System.out.println("网络状态改变");
				NetworkInfo info = intent
						.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
					System.out.println("wifi网络连接断开");
					wifiImageView.setVisibility(View.INVISIBLE);
				} else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {

					WifiManager wifiManager = (WifiManager) context
							.getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();

					// 获取当前wifi名称
					System.out.println("连接到网络 " + wifiInfo.getSSID());
					wifiImageView.setVisibility(View.VISIBLE);

				}

			} else if (intent.getAction().equals(
					WifiManager.WIFI_STATE_CHANGED_ACTION)) {// wifi打开与否
				int wifistate = intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_DISABLED);

				if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
					System.out.println("系统关闭wifi");
				} else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
					System.out.println("系统开启wifi");
				}
			}
		}
	};

	
	public void pauseView(){
		context.unregisterReceiver(wifiReceiver);
	}
	
	public void resumeView(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		context.registerReceiver(wifiReceiver, filter);
	}
	
}
