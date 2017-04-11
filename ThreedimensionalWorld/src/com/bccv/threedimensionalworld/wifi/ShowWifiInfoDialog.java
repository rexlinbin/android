package com.bccv.threedimensionalworld.wifi;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.activity.MainActivity;

@SuppressLint("NewApi")
public class ShowWifiInfoDialog {

	public ShowWifiInfoDialog() {
		// TODO Auto-generated constructor stub
	};

	Activity mActivity;
	private static LinearLayout linear, linear1;

	public interface IRemoveWifi {

		public void onRemoveClick(int networkId);
	}

	IRemoveWifi mIRemoveWifi = null;

	static WifiInfo mConnectedInfo;
	private static AlertDialog dialog;
	String encrypt;
	public static Button okButton;
	public static Button okButton1;
	public static Button cancelButton1;
 static Button cancelButton;
	public static void newInstance(Context context,
			final IRemoveWifi mIRemoveWifi, String encrypt, String wifiItemSSID) {

		dialog = new AlertDialog.Builder(context).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
		dialog.show();
		mConnectedInfo = WifiUtils.getWifiInfo();

		Window view = dialog.getWindow();
		WindowManager.LayoutParams lp = view.getAttributes();
		view.setContentView(R.layout.dialog_wifi);
		view.setDimAmount(0.8f);
		view.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		view.setGravity(Gravity.CENTER);

		LayoutParams params = new LayoutParams();

		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 1); // 高度设置为屏幕的0.6
		lp.height = (int) (d.widthPixels * 0.5);

		view.setAttributes(lp);

		TextView title = (TextView) view.findViewById(R.id.wifi_dialog_title);
		title.setText(wifiItemSSID);
		TextView mStateTv = (TextView) view.findViewById(R.id.state_tv);
		TextView title1 = (TextView) view.findViewById(R.id.wifi_dialog_title1);
		title.setText(wifiItemSSID);
		title1.setText(wifiItemSSID);
		TextView mStateTv1 = (TextView) view.findViewById(R.id.state_tv1);
		String state = "";

		linear = (LinearLayout) view.findViewById(R.id.wifi_config_linear);
		linear1 = (LinearLayout) view.findViewById(R.id.wifi_config_linear1);
		linear.setScaleX(0.5f);
		linear1.setScaleX(0.5f);

		int Ip = mConnectedInfo.getIpAddress();

		Log.d(MainActivity.class.getSimpleName(), "ip = " + Ip);

		String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF) + "."
				+ ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);

		if (mConnectedInfo.getSSID() != null
				&& mConnectedInfo.getBSSID() != null
				&& !strIp.equals("0.0.0.0")) {

			mStateTv.setText("已连接");
			mStateTv1.setText("已连接");
		} else {
			mStateTv.setText("正在连接...");
			mStateTv1.setText("正在连接...");
		}

		TextView mSafetyTv = (TextView) view.findViewById(R.id.safety_tv);

		mSafetyTv.setText(encrypt);
		TextView mSafetyTv1 = (TextView) view.findViewById(R.id.safety_tv1);

		mSafetyTv1.setText(encrypt);
		TextView mLevelTv = (TextView) view.findViewById(R.id.level_tv);
		TextView mLevelTv1 = (TextView) view.findViewById(R.id.level_tv1);
		int level = Math.abs(mConnectedInfo.getRssi());

		if (level <= 50 && level >= 0) {

			mLevelTv.setText("强");
			mLevelTv1.setText("强");
		} else if (level <= 70 && level > 50) {

			mLevelTv.setText("较强");
			mLevelTv1.setText("较强");
		} else if (level <= 80 && level > 70) {

			mLevelTv.setText("一般");
			mLevelTv1.setText("一般");
		} else if (level <= 100 && level > 80) {

			mLevelTv.setText("弱");
			mLevelTv1.setText("一般");
		}

		TextView mSpeedTv = (TextView) view.findViewById(R.id.speed_tv);

		mSpeedTv.setText(mConnectedInfo.getLinkSpeed() + " Mbps");
		TextView mSpeedTv1 = (TextView) view.findViewById(R.id.speed_tv1);

		mSpeedTv1.setText(mConnectedInfo.getLinkSpeed() + " Mbps");

		TextView mIpTv = (TextView) view.findViewById(R.id.ip_tv);

		mIpTv.setText(WifiUtils.long2ip(mConnectedInfo.getIpAddress()));
		TextView mIpTv1 = (TextView) view.findViewById(R.id.ip_tv1);

		mIpTv1.setText(WifiUtils.long2ip(mConnectedInfo.getIpAddress()));

		try {

			Field mField = mConnectedInfo.getClass().getDeclaredField(
					"mIpAddress");
			mField.setAccessible(true);
			InetAddress mInetAddr = (InetAddress) mField.get(mConnectedInfo);

			// for (NetworkInterface networkInterface :
			// Collections.list(NetworkInterface.getNetworkInterfaces())) {
			// Log.d(ShowWifiInfoDialog.class.getSimpleName(),
			// networkInterface.toString());
			// }

			NetworkInterface mInterface = NetworkInterface
					.getByInetAddress(mInetAddr);

			byte[] mac = mInterface.getHardwareAddress();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {

				sb.append(String.format("%02X%s", mac[i],
						(i < mac.length - 1) ? ":" : ""));
			}

		} catch (SocketException e) {

			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		DhcpInfo mDhcpInfo = WifiUtils.getDhcpInfo(context);

		cancelButton = (Button) view
				.findViewById(R.id.wifi_Dialog_Cancel);
		okButton = (Button) view.findViewById(R.id.wifi_Dialog_Certain);

		cancelButton1 = (Button) view
				.findViewById(R.id.wifi_Dialog_Cancel1);
		 okButton1 = (Button) view
				.findViewById(R.id.wifi_Dialog_Certain1);

		 
		
		 
		 cancelButton1.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub

				if (arg1) {

					cancelButton1.setSelected(true);

					cancelButton.setSelected(true);

				} else {
					cancelButton1.setSelected(false);

					cancelButton.setSelected(false);

				}
			}
		});
		 cancelButton.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub

					if (arg1) {

						cancelButton1.setSelected(true);

						cancelButton.setSelected(true);

					} else {
						cancelButton1.setSelected(false);

						cancelButton.setSelected(false);

					}
				}
			});
		 
		cancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		
		
		okButton.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {

					okButton1.setSelected(true);

					okButton.setSelected(true);

				} else {
					okButton1.setSelected(false);

					okButton.setSelected(false);

				}
			}
		});
	okButton1.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {

					okButton1.setSelected(true);

					okButton.setSelected(true);

				} else {
					okButton1.setSelected(false);

					okButton.setSelected(false);

				}
			}
		});
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mIRemoveWifi != null) {
					mIRemoveWifi.onRemoveClick(mConnectedInfo.getNetworkId());
				}
				dialog.dismiss();
			}
		});

		cancelButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		okButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mIRemoveWifi != null) {
					mIRemoveWifi.onRemoveClick(mConnectedInfo.getNetworkId());
				}
				dialog.dismiss();
			}
		});

	}

	
}
