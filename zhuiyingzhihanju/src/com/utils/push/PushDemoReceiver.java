package com.utils.push;

import java.util.List;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bccv.zhuiyingzhihanju.activity.MainActivity;
import com.bccv.zhuiyingzhihanju.activity.Video2DPlayerActivity;
//import com.bccv.zhuiying.activity.MainActivity;
//import com.bccv.zhuiying.activity.MovieInfoActivity;
import com.bccv.zhuiyingzhihanju.model.Movie;
import com.igexin.sdk.PushConsts;

public class PushDemoReceiver extends BroadcastReceiver {

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			byte[] payload = bundle.getByteArray("payload");
			if (payload != null) {
				String data = new String(payload);
				JSONObject jsonObject = JSON.parseObject(data);
				boolean isStart = jsonObject.getBooleanValue("isStart");
				boolean hasStart = false;
				if (isStart) {
					
					ActivityManager activityManager = (ActivityManager) context
							.getSystemService(Context.ACTIVITY_SERVICE);
					List<RunningTaskInfo> task_info = activityManager
							.getRunningTasks(20);

					String className = "";
					for (int i = 0; i < task_info.size(); i++) {
						if ("com.bccv.zhuiyingzhihanju".equals(task_info.get(i).topActivity
								.getPackageName())) {
							hasStart = true;
							className = task_info.get(i).topActivity
									.getClassName();
							// 这里是指从后台返回到前台 前两个的是关键
							intent.setAction(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_LAUNCHER);
							try {
								Log.d("GetuiSdk", className);
								intent.setComponent(new ComponentName(context,
										Class.forName(className)));
								intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
										| Intent.FLAG_ACTIVITY_NEW_TASK
										| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								context.startActivity(intent);
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								hasStart = false;
							}
							
						}
					}
					if (!hasStart) {
						Intent intent2 = new Intent(context,
								MainActivity.class);
						intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent2);
					}

				} else {
					
					String movie_id = jsonObject.getString("movie_id");
					String type_id = jsonObject.getString("type_id");
					String episodes_id = jsonObject.getString("episode_id");

					Intent intent2 = new Intent(context,
							Video2DPlayerActivity.class);
					intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent2.putExtra("movie_id", movie_id);
					intent2.putExtra("type_id", type_id);
					intent2.putExtra("episodes_id", episodes_id);
					context.startActivity(intent2);
				}

			}

			break;
		case PushConsts.GET_CLIENTID:

			break;
		case PushConsts.THIRDPART_FEEDBACK:

			break;
		default:
			break;
		}
	}
}
