package com.bccv.threedimensionalworld.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.model.WifiBeaN;
import com.bccv.threedimensionalworld.wifi.WifiUtils;

public class WifiAdapter extends BaseAdapter {

	List<WifiBeaN> data;
	Context context;
	private int selectNum = 0;
	public WifiAdapter(List<WifiBeaN> data, Context context) {
		super();
		this.data = data;
		this.context = context;
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
	public void setSelect(int selectNum) {
		if (selectNum != this.selectNum) {
			this.selectNum = selectNum;
			notifyDataSetChanged();
		}
	}
	@SuppressLint("NewApi") @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

		View view = LayoutInflater.from(context).inflate(R.layout.item_wifi,
				null);

		TextView name = (TextView) view.findViewById(R.id.wifi_item_name);
		TextView info = (TextView) view.findViewById(R.id.wifi_item_info);
		ImageView im = (ImageView) view.findViewById(R.id.wifi_item_image);
		info.setScaleX(0.5f);
		name.setScaleX(0.5f);
		WifiBeaN item = data.get(arg0);
		name.setText(item.getWifiName());

		int level = Math.abs(item.getLevel());

		if (!WifiUtils.getEncryptString(item.getWifiInfo()).equals("OPEN")) {
			im.setImageResource(R.drawable.wifi_sel);
			im.setImageLevel(level);

		} else {

			im.setImageResource(R.drawable.wifi_clock);
			im.setImageLevel(level);
	}

		WifiInfo mInfo = WifiUtils.getWifiInfo();
		if (mInfo != null) {

			if (mInfo.getSSID() != null
					&& mInfo.getSSID().equals("\"" + item.getWifiName() + "\"")) {
//				Log.e("wifiAdapter", mInfo.getSSID());

				int Ip = mInfo.getIpAddress();

				String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF)
						+ "." + ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);

				if (mInfo.getBSSID() != null && mInfo.getSSID() != null
						&& strIp != null && !strIp.equals("0.0.0.0")) {
//					Log.e("wifiAdaptermInfo.getBSSID()", mInfo.getBSSID());
					info.setText("已连接");
				} else {

					info.setText("连接中......");
				}
			} 
			
			else {
				info.setText(WifiUtils.getEncryptString(item.getWifiInfo()));
			}
		}else{
			
			info.setText(WifiUtils.getEncryptString("不在范围内"));
		}

		return view;
	}

	public List<WifiBeaN> getDataList() {
		// TODO Auto-generated method stub
		
		
		
		
		
		
		
		
		
		return data;
	}

	
}
