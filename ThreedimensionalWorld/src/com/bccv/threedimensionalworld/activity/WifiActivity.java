package com.bccv.threedimensionalworld.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.WifiAdapter;
import com.bccv.threedimensionalworld.model.WifiBeaN;
import com.bccv.threedimensionalworld.tool.BaseFragmentActivity;
import com.bccv.threedimensionalworld.wifi.ShowWifiInfoDialog;
import com.bccv.threedimensionalworld.wifi.ShowWifiInfoDialog.IRemoveWifi;
import com.bccv.threedimensionalworld.wifi.WifiPswDialog;
import com.bccv.threedimensionalworld.wifi.WifiPswDialog.OnCustomDialogListener;
import com.bccv.threedimensionalworld.wifi.WifiUtils;

public class WifiActivity extends BaseFragmentActivity implements IRemoveWifi {
	private WifiAdapter adapter;
	private List<WifiBeaN> data;
	private ListView list, list1;
	private Button isWifi, isWifi1;
	private WifiUtils localWifiUtils;
	private List<ScanResult> wifiResultList;
	private WifiBeaN wifiB;
	private int type;
	private TextView title, title1;
	// WifiManager wm = null;

	IntentFilter mFilter = new IntentFilter(
			WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

	WifiActionReceiver mWifiActionReceiver = new WifiActionReceiver();

	class WifiActionReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction() != null
					&& intent.getAction().equals(
							WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

				goScanResult();
			}

		}

	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		setContentView(R.layout.activity_wifi);

		list = (ListView) findViewById(R.id.wifi_listView);
		title = (TextView) findViewById(R.id.option_setscreen);
		title.setScaleX(0.5f);
		title1 = (TextView) findViewById(R.id.option_setscreen1);
		title1.setScaleX(0.5f);
		list1 = (ListView) findViewById(R.id.wifi_listView1);
		isWifi = (Button) findViewById(R.id.wifi_close);
		isWifi1 = (Button) findViewById(R.id.wifi_close1);
		data = new ArrayList<WifiBeaN>();
		adapter = new WifiAdapter(data, WifiActivity.this);
		localWifiUtils = new WifiUtils(WifiActivity.this);
		// wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		list.setAdapter(adapter);
		list1.setAdapter(adapter);
		list.setOnItemClickListener(new ItemClickListener());
		// list1.setOnItemClickListener(new ItemClickListener1());

		if (!WifiUtils.isWifiOpen(this)) {

			isWifi.setBackgroundResource(R.drawable.set_close);
			isWifi1.setBackgroundResource(R.drawable.set_close);
			data.clear();
			adapter.notifyDataSetChanged();
		} else {
			mHandler.post(mCallBack);
			isWifi.setBackgroundResource(R.drawable.set_open);
			isWifi1.setBackgroundResource(R.drawable.set_open);
		}
		// isWifi.setOnFocusChangeListener(new OnChangeListener());
		// isWifi1.setOnFocusChangeListener(new OnChangeListener());
		list.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				list1.onTouchEvent(event);
				return false;
			}
		});
		list1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				list.onTouchEvent(event);
				return false;
			}
		});
		list.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				list1.onKeyDown(keyCode, event);

				return false;
			}
		});
		list1.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub

				list.onKeyDown(keyCode, event);

				return false;
			}
		});

		list1.setSelector(new ColorDrawable(android.R.color.transparent));

		list.setSelector(new ColorDrawable(android.R.color.transparent));

		list.setOnGenericMotionListener(new OnGenericMotionListener() {

			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				list1.onGenericMotionEvent(arg1);
				return false;
			}
		});
		list1.setOnGenericMotionListener(new OnGenericMotionListener() {

			@Override
			public boolean onGenericMotion(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				list.onGenericMotionEvent(arg1);
				return false;
			}
		});

	}

	public void onClick(View view) {

		if (!localWifiUtils.mWifiManager.isWifiEnabled()) {

			isWifi.setBackgroundResource(R.drawable.set_open);
			isWifi1.setBackgroundResource(R.drawable.set_open);
			data.clear();
			localWifiUtils.openWifi();
			list.setVisibility(View.VISIBLE);
			list1.setVisibility(View.VISIBLE);
		} else {
			isWifi.setBackgroundResource(R.drawable.set_close);
			isWifi1.setBackgroundResource(R.drawable.set_close);
			localWifiUtils.closeWifi();
			data.clear();
			adapter.notifyDataSetChanged();
			list.setVisibility(View.INVISIBLE);
			list1.setVisibility(View.INVISIBLE);
			Log.e("wifi......", "Close");

		}

	}

	class ScanResultTask extends AsyncTask<Void, Void, List<ScanResult>> {

		@Override
		protected List<ScanResult> doInBackground(Void... params) {

			wifiResultList = localWifiUtils.getWifiList();
			return wifiResultList = localWifiUtils.getWifiList();

		}

		@Override
		protected void onPostExecute(List<ScanResult> result) {
			super.onPostExecute(result);
			data.clear();
			if (result != null) {

				for (int i = 0; i < result.size(); i++) {
					// 得到扫描结果
					ScanResult mScanResult = result.get(i);
					// (mScanResult.BSSID+"  ").append(mScanResult.SSID+"   ")
					// .append(mScanResult.capabilities+"   ").append(mScanResult.frequency+"   ")
					// .append(mScanResult.level+"\n\n");

					data.add(new WifiBeaN(mScanResult.SSID,
							mScanResult.capabilities, mScanResult.level));

				}
				adapter.notifyDataSetChanged();

			}

		}

	}

	// 清除配置信息
	@Override
	public void onRemoveClick(int networkId) {
		// TODO Auto-generated method stub
		WifiUtils.removeWifi(this, networkId);
		new ScanResultTask().execute((Void) null);
	}

	private void goScanResult() {

		mHandler.sendEmptyMessage(0);

	}

	private Runnable mCallBack = new Runnable() {

		@Override
		public void run() {

			Log.d(MainActivity.class.getSimpleName(), "mCallBack...");

			localWifiUtils.startScan();

			mHandler.postDelayed(this, 500);
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				new ScanResultTask().execute((Void) null);

				break;
			case 1:

				break;
			case 2:

				break;
			default:
				break;
			}
		};
	};

	// 列表item点击操作
	class ItemClickListener implements OnItemClickListener {
		String wifiItemSSID = null;
		private View selectedItem;
		private String wifiPassword = null;

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {

			// TODO Auto-generated method stub
			adapter.setSelect(arg2);
			wifiItemSSID = data.get(arg2).getWifiName();

			selectedItem = arg1;

			int wifiItemId = localWifiUtils.IsConfiguration("\"" + wifiItemSSID
					+ "\"");
			WifiInfo mInfo = WifiUtils.getWifiInfo();
			int Ip = mInfo.getIpAddress();

			final String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF)
					+ "." + ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);
			if (wifiItemId != -1) {

				if (mInfo.getSSID() != null
						&& (mInfo.getSSID().equals(wifiItemSSID) || mInfo
								.getSSID().equals("\"" + wifiItemSSID + "\""))) {
					// 显示信息详情

					ShowWifiInfoDialog.newInstance(WifiActivity.this,
							WifiActivity.this, WifiUtils.getEncryptString(data
									.get(arg2).getWifiInfo()), wifiItemSSID);

				}

				localWifiUtils.ConnectWifi(wifiItemId);
			} else {
				if (WifiUtils.getEncryptString(data.get(arg2).getWifiInfo())
						.equals("OPEN")) {

					localWifiUtils.addNetWork(WifiUtils.createWifiConfig(
							wifiItemSSID, "", WifiUtils.getWifiCipher(data.get(
									arg2).getWifiInfo())));
				} else {
					WifiPswDialog.showDialog(WifiActivity.this,
							new OnCustomDialogListener() {
								@Override
								public void back(String str) {
									// TODO Auto-generated method stub
									wifiPassword = str;
									if (wifiPassword != null) {
										int netId = localWifiUtils
												.AddWifiConfig(wifiResultList,
														wifiItemSSID,
														wifiPassword);
										Log.i("WifiPswDialog",
												String.valueOf(netId));
										if (netId != -1) {
											localWifiUtils.getConfiguration();// 添加了配置信息，要重新得到配置信息
											if (localWifiUtils
													.ConnectWifi(netId)) {

											}
										}
									} 
								}
							});

				}
			}

			// adapter.getDataList().clear();
			wifiB = data.get(arg2);
			data.remove(arg2);
			data.add(0, wifiB);

			adapter.getDataList().addAll(data);
			adapter.notifyDataSetChanged();

		}

	}

	// 列表item点击操作
	// class ItemClickListener1 implements OnItemClickListener {
	// String wifiItemSSID = null;
	// private View selectedItem;
	// private String wifiPassword = null;
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
	// long arg3) {
	//
	// // TODO Auto-generated method stub
	// adapter.setSelect(arg2);
	// wifiItemSSID = data.get(arg2).getWifiName();
	//
	// selectedItem = arg1;
	//
	// int wifiItemId = localWifiUtils.IsConfiguration("\"" + wifiItemSSID
	// + "\"");
	// WifiInfo mInfo = WifiUtils.getWifiInfo();
	// int Ip = mInfo.getIpAddress();
	//
	// final String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF)
	// + "." + ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);
	// if (wifiItemId != -1) {
	//
	// if (mInfo.getSSID() != null
	// && (mInfo.getSSID().equals(wifiItemSSID) || mInfo
	// .getSSID().equals("\"" + wifiItemSSID + "\""))) {
	// // 显示信息详情
	//
	// ShowWifiInfoDialog.newInstance(WifiActivity.this,
	// WifiActivity.this, WifiUtils.getEncryptString(data
	// .get(arg2).getWifiInfo()), wifiItemSSID);
	//
	// }
	//
	// localWifiUtils.ConnectWifi(wifiItemId);
	// } else {
	// if (WifiUtils.getEncryptString(data.get(arg2).getWifiInfo())
	// .equals("OPEN")) {
	//
	// localWifiUtils.addNetWork(WifiUtils.createWifiConfig(
	// wifiItemSSID, "", WifiUtils.getWifiCipher(data.get(
	// arg2).getWifiInfo())));
	// } else {
	// WifiPswDialog.showDialog(WifiActivity.this,
	// new OnCustomDialogListener() {
	// @Override
	// public void back(String str) {
	// // TODO Auto-generated method stub
	// wifiPassword = str;
	// if (wifiPassword != null) {
	// int netId = localWifiUtils
	// .AddWifiConfig(wifiResultList,
	// wifiItemSSID,
	// wifiPassword);
	// Log.i("WifiPswDialog",
	// String.valueOf(netId));
	// if (netId != -1) {
	// localWifiUtils.getConfiguration();// 添加了配置信息，要重新得到配置信息
	// if (localWifiUtils
	// .ConnectWifi(netId)) {
	//
	// }
	// }
	// if (strIp.equals("0.0.0.0")) {
	// Toast.makeText(WifiActivity.this,
	// "网络连接错误",
	// Toast.LENGTH_SHORT).show();
	//
	// ShowWifiInfoDialog
	// .newInstance(
	// WifiActivity.this,
	// WifiActivity.this,
	// WifiUtils
	// .getEncryptString(data
	// .get(arg2)
	// .getWifiInfo()),
	// wifiItemSSID);
	//
	// }
	// } else {
	//
	// }
	// }
	// });
	//
	// }
	// }
	//
	// // adapter.getDataList().clear();
	// wifiB = data.get(arg2);
	// data.remove(arg2);
	// data.add(0, wifiB);
	//
	// adapter.getDataList().addAll(data);
	// adapter.notifyDataSetChanged();
	//
	// }
	//
	// }

	class ScrollListener implements OnScrollListener {

		@SuppressLint("NewApi")
		@Override
		public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScrollStateChanged(AbsListView arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mWifiActionReceiver != null && mFilter != null)
			registerReceiver(mWifiActionReceiver, mFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mWifiActionReceiver != null)
			unregisterReceiver(mWifiActionReceiver);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (mCallBack != null) {
			mHandler.removeCallbacks(mCallBack);
		}
	}

	class OnChangeListener implements OnFocusChangeListener {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			// TODO Auto-generated method stub

			if (arg1) {

				LayoutParams params = (LayoutParams) isWifi.getLayoutParams();
				params.leftMargin = params.leftMargin + 1;
				isWifi.setLayoutParams(params);
				isWifi.setSelected(true);

				isWifi1.setSelected(true);

			} else {

				LayoutParams params = (LayoutParams) isWifi.getLayoutParams();
				params.leftMargin = params.leftMargin - 1;
				isWifi.setLayoutParams(params);
				isWifi.setSelected(false);

				isWifi1.setSelected(false);

			}

		}

	}

}
