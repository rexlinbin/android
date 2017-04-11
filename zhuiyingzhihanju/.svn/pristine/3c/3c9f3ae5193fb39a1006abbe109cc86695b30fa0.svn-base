package com.utils.views;


import com.bccv.zhuiyingzhihanju.R;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MyBattery extends LinearLayout{

	private Context context;
	private ProgressBar progressBar;
	private TextView textView;
	private Handler handler;
	
	public MyBattery(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		this.context = context;
		init();
	}
	
	public MyBattery(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}
	
	private void init(){
		progressBar = new ProgressBar(context,null,android.R.attr.progressBarStyleHorizontal);
		progressBar.setMax(100);
		progressBar.setProgress(100);
		progressBar.setIndeterminate(false);
		progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.battery_progress));
		this.addView(progressBar);
	}

	
	public void setHandler(Handler handler){
		this.handler = handler;
	}
	
	public void registerBattery() {
		context.registerReceiver(batteryBroadcastReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	public void unregisterBattery() {
		context.unregisterReceiver(batteryBroadcastReceiver);
	}

	private BatteryBroadcastReceiver batteryBroadcastReceiver = new BatteryBroadcastReceiver();

	/** 接受电量改变广播 */
	class BatteryBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			 //电池电量，数字    
            //Log.d("Battery", "" + intent.getIntExtra("level", 0));                   
            //电池最大容量    
            //Log.d("Battery", "" + intent.getIntExtra("scale", 0));                   
            //电池伏数    
            //Log.d("Battery", "" + intent.getIntExtra("voltage", 0));                   
            //电池温度    
            //Log.d("Battery", "" + intent.getIntExtra("temperature", 0));    
            //电池状态，返回是一个数字    
            // BatteryManager.BATTERY_STATUS_CHARGING 表示是充电状态    
            // BatteryManager.BATTERY_STATUS_DISCHARGING 放电中    
            // BatteryManager.BATTERY_STATUS_NOT_CHARGING 未充电    
            // BatteryManager.BATTERY_STATUS_FULL 电池满    
            //Log.d("Battery", "ss" + intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_CHARGING));    
            //充电类型 BatteryManager.BATTERY_PLUGGED_AC 表示是充电器，不是这个值，表示是 USB    
            //Log.d("Battery", "" + intent.getIntExtra("plugged", 0));    
            //电池健康情况，返回也是一个数字    
            //BatteryManager.BATTERY_HEALTH_GOOD 良好    
            //BatteryManager.BATTERY_HEALTH_OVERHEAT 过热    
            //BatteryManager.BATTERY_HEALTH_DEAD 没电    
            //BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE 过电压    
            //BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE 未知错误    
            //Log.d("Battery", "" + intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN));    
			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

				int level = intent.getIntExtra("level", 0);
				progressBar.setProgress(level);
				if (level < 20) {
					progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.battery_progress_red));
					MyBattery.this.setSelected(true);
				}else{
					progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.battery_progress));
					MyBattery.this.setSelected(false);
				}
				if (handler != null) {
					Message msg = new Message();
					msg.what = 0;
					msg.arg1 = level;
					handler.sendMessage(msg);
				}
			}
		}
	}
}
