package com.bccv.threedimensionalworld.activity;

import com.bccv.threedimensionalworld.R;
import com.bccv.threedimensionalworld.tool.ClsUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BlueQuXiaoActivity extends Activity{
	
	private String Adress;

	  BluetoothDevice btDevice; 
	  private boolean isEdiName;
	  private TextView DeviceName;
		private EditText EditName;
	private BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blue_quxiao);
		
		Adress=getIntent().getStringExtra("adress");
		btDevice=adapter.getRemoteDevice(Adress);
	}

	
	public void onClick(View view){
		
	
		
		 try {
			ClsUtils.removeBond(btDevice.getClass(), btDevice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
		
		
		
		
		finish();
		
	}
	
	
	
	
}
