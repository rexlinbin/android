package com.bccv.strategy.receiver;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.bccv.strategy.ui.activity.MainActivity;
import com.bccv.strategy.ui.activity.StrategyDetailsActivity;
import com.bccv.strategy.ui.view.BackGroundView;
import com.bccv.strategy.utils.L;
import com.igexin.sdk.PushConsts;

public class PushDemoReceiver extends BroadcastReceiver {
	private static final String TAG = "PushDemoReceiver";

	private static final int PUSH_TYPE_APPDETAIL = 1;

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
				
				L.v(TAG, "onReceive", data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					int type = jsonObject.optInt("type");
					switch (type) {
					case PUSH_TYPE_APPDETAIL:
						String appid = jsonObject.optString("app_id");
						gotoAppDetail(context,appid);
						break;

					default:
						break;
					}

				} catch (JSONException e) {
					e.printStackTrace();
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

	private void gotoAppDetail(Context context,String appid) {
		if(TextUtils.isEmpty(appid)){
			return;
		}
		
		int[] gradientColor;
		if(MainActivity.backGroundView!=null){
			gradientColor = MainActivity.backGroundView.getGradientColor();
		}else{
			gradientColor = new int[]{0xd35f53,0xdb417a};
		}
		
		 Intent appDetailsIntent = new Intent(context,
				 StrategyDetailsActivity.class);
		 appDetailsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 appDetailsIntent.putExtra(BackGroundView.BACKGROUND_COLOR,gradientColor);
		 appDetailsIntent.putExtra(StrategyDetailsActivity.APP_ID,appid);
		 context.startActivity(appDetailsIntent);
	}

}
