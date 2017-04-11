package com.bccv.threedimensionalworld.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.adapter.BlueToothAdapter;
import com.bccv.threedimensionalworld.model.BlueToothBeaN;
import com.bccv.threedimensionalworld.tool.BleDefinedUUIDs;
import com.bccv.threedimensionalworld.tool.ClsUtils;
import com.bccv.threedimensionalworld.tool.MyListView;



public class BluetoothActivity extends Activity implements Comparable<BluetoothActivity>{
	static final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
	public static BluetoothSocket btSocket;
	private Button isOpenTooth, jiance, seachButton;
	private TextView DeviceName, time;
	private MyListView listView;
	private BlueToothAdapter adapter;
	private List<BlueToothBeaN> data;
	private BluetoothAdapter BlueTooth = BluetoothAdapter.getDefaultAdapter();
	RelativeLayout GoRe;
	/* 请求打开蓝牙 */
	private static final int REQUEST_ENABLE = 0x1;
	/* 请求能够被搜索 */
	private static final int REQUEST_DISCOVERABLE = 0x2;
	private boolean isEdiName;
	private EditText EditName;
	BluetoothDevice btDevice;
	private BlueToothBeaN bean;
	final static private UUID uuid =  UUID.fromString(SPP_UUID);
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth);

		isOpenTooth = (Button) findViewById(R.id.blueTooth_close);
		jiance = (Button) findViewById(R.id.buleTooth_jiance_close);
		EditName = (EditText) findViewById(R.id.bluetooth_Editname);
		seachButton = (Button) findViewById(R.id.bluetooth_search);
		DeviceName = (TextView) findViewById(R.id.bluetooth_name);
		DeviceName.setText(BlueTooth.getName());
		GoRe = (RelativeLayout) findViewById(R.id.blue_re3);

		listView = (MyListView) findViewById(R.id.buletooth_list);
		time = (TextView) findViewById(R.id.blueTooth_time);
		data = new ArrayList<BlueToothBeaN>();
		adapter = new BlueToothAdapter(data, this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClick());
		if (BlueTooth.isEnabled()) {

			isOpenTooth.setBackgroundResource(R.drawable.set_open);

		} else {
			isOpenTooth.setBackgroundResource(R.drawable.set_close);

		}

		// Register for broadcasts when a device is discovered
		IntentFilter discoveryFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, discoveryFilter);

		// Register for broadcasts when discovery has finished
		IntentFilter foundFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, foundFilter);

		// Get a set of currently paired devices
		Set<BluetoothDevice> pairedDevices = BlueTooth.getBondedDevices();

		// If there are paired devices, add each one to the ArrayAdapter

		if (pairedDevices.size() > 0) {

			for (BluetoothDevice device : pairedDevices) {
				data.add(new BlueToothBeaN(device.getName(), true, device
						.getType(), device.getAddress()));
				adapter.notifyDataSetChanged();
				listView.setSelection(data.size() - 1);
			}
		} else {

			data.add(new BlueToothBeaN("没有设备已经配对", false, 0, "0"));
			adapter.notifyDataSetChanged();
			listView.setSelection(data.size() - 1);
		}

	}

	public void onGoClick(View view) {
		if (!isEdiName) {

			EditName.setVisibility(View.VISIBLE);

			String name = EditName.getText().toString();
			EditName.setText(BlueTooth.getName());

			BlueTooth.setName(name);
			// DeviceName.setText(BlueTooth.getName());

			isEdiName = true;

		} else {

			DeviceName.setText(BlueTooth.getName());

			isEdiName = false;

			EditName.setVisibility(View.INVISIBLE);

		}

	}

	public void onClick(View view) {

		if (!BlueTooth.isEnabled()) {

			isOpenTooth.setBackgroundResource(R.drawable.set_open);
			BlueTooth.enable();

		} else {
			isOpenTooth.setBackgroundResource(R.drawable.set_close);
			BlueTooth.disable();
			data.clear();
			adapter.notifyDataSetChanged();
		}

	}

	public void onClickJian(View view) {

		Intent enabler = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		startActivityForResult(enabler, REQUEST_DISCOVERABLE);

		time.setText("对附近所有的蓝牙设备可见" + System.currentTimeMillis());

	}

	@SuppressLint("NewApi")
	public void onSearchClick(View view) {

		if (BlueTooth.isDiscovering()) {
			BlueTooth.cancelDiscovery();
			seachButton.setText("重新搜索");
		} else {
			data.clear();
			adapter.notifyDataSetChanged();

			Set<BluetoothDevice> pairedDevices = BlueTooth.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					data.add(new BlueToothBeaN(device.getName(), true, device
							.getType(), device.getAddress()));
					adapter.notifyDataSetChanged();
					listView.setSelection(data.size() - 1);
				}
			} else {
				data.add(new BlueToothBeaN("没有配对的设备", false, 0, "0"));
				adapter.notifyDataSetChanged();
				listView.setSelection(data.size() - 1);
			}
			/* 开始搜索 */
			BlueTooth.startDiscovery();
			seachButton.setText("停止搜索");
		}
	}

	class OnItemClick implements OnItemClickListener {

		@SuppressLint("NewApi")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			BlueTooth.cancelDiscovery();
			seachButton.setText("重新搜索");

			btDevice = BlueTooth.getRemoteDevice(data.get(arg2).getAdress());

			try {
				if (!data.get(arg2).isSiri()) {

					Toast.makeText(BluetoothActivity.this, "由未配对转为已配对", 500)
							.show();
					ClsUtils.createBond(btDevice.getClass(), btDevice);
			
			
			
					
				} else {
//					String uu=ClsUtils.getUUId(btDevice.getClass(), btDevice);
					
//					Log.e("UUId", uu);
					Log.e("BlueToothTestActivity", "开始连接...");
					  ConnectThread connectThread = new ConnectThread(btDevice);
	                    connectThread.start();//连接设备   
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			data.clear();
			adapter.notifyDataSetChanged();

			Set<BluetoothDevice> pairedDevices = BlueTooth.getBondedDevices();
			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {
					data.add(new BlueToothBeaN(device.getName(), true, device
							.getType(), device.getAddress()));
					adapter.notifyDataSetChanged();
					listView.setSelection(data.size() - 1);
				}
			}
		}

	}

	// The BroadcastReceiver that listens for discovered devices and
	// changes the title when discovery is finished
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			// When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				// Get the BluetoothDevice object from the Intent

				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// If it's already paired, skip it, because it's been listed
				// already
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

					data.add(new BlueToothBeaN(device.getName(), false, device
							.getType(), device.getAddress()));
					adapter.notifyDataSetChanged();
					listView.setSelection(data.size() - 1);
				}
				// When discovery is finished, change the Activity title
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				setProgressBarIndeterminateVisibility(false);
				if (listView.getCount() == 0) {
					data.add(new BlueToothBeaN("没有发现蓝牙设备", false, 0, "0"));
					adapter.notifyDataSetChanged();
					listView.setSelection(data.size() - 1);
				}
				seachButton.setText("重新搜索");
			}
			else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)){
				
				btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
				
				 switch (btDevice.getBondState()) {  
	                case BluetoothDevice.BOND_BONDING:  
	                    Log.e("BlueToothTestActivity", "正在配对......");  
	                    break;  
	                case BluetoothDevice.BOND_BONDED:  
	                    Log.e("BlueToothTestActivity", "完成配对");  
					ConnectThread connectThread = null;
					
						connectThread = new ConnectThread(btDevice);
						  connectThread.start();//连接设备 
					
	                   
	                    break;  
	                case BluetoothDevice.BOND_NONE:  
	                    Log.e("BlueToothTestActivity", "取消配对");  
	                default:  
	                    break;  
	                }  
				
				
				
				
				
			}
		}
	};

//	private void connect(BluetoothDevice btDev) {
//		UUID uuid = UUID.fromString(SPP_UUID);
//		try {
//			btSocket = btDev.createRfcommSocketToServiceRecord(uuid);
//			Log.d("BlueToothTestActivity", "开始连接...");
//			btSocket.connect();
//			btSocket.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	
	public class ConnectThread extends Thread {
	    private BluetoothSocket mmSocket;
	    private  BluetoothDevice mmDevice;
	     BluetoothSocket tmp = null;
	
	    public ConnectThread(BluetoothDevice device)  {
	    
	       this.mmDevice = device;

	    }

	    public void run() {
//	        setName("ConnectThread");
	        // Cancel discovery because it will slow down the connection
	        BlueTooth.cancelDiscovery();
			try {
				tmp= mmDevice.createRfcommSocketToServiceRecord(uuid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     
			 mmSocket = tmp;
			 
			
	         try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            Log.e("kent", "trying to connect to device");
	            mmSocket.connect();
	            Log.e("kent", "Connected!");
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	        	connectException.printStackTrace();
	            try {
	                Log.e("kent", "连接"+connectException.toString());

	                mmSocket.close();
	            } catch (IOException closeException) { 
//	            	 Log.e("kent", "ud"closeException.toString());
	            	
	            }
	            return;
	        }

	        Log.d("kent", "Connected!");
	    }
	
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
		
	
	}
	
	
	
	
	
	
	protected void onDestroy() {
		super.onDestroy();
//		try {
//			btSocket.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		unregisterReceiver(mReceiver);
	}

	@Override
	public int compareTo(BluetoothActivity arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	

}
