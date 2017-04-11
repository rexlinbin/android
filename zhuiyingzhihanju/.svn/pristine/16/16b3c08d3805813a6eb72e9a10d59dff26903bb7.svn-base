package com.bccv.zhuiyingzhihanju.wxapi;

import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bccv.zhuiyingzhihanju.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 提供给微信回调的Activity
 * 
 * @author Administrator
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXEntryActivity";

	private IWXAPI api;// IWXAPI 是第三方app和微信通信的openapi接口

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		String WX_APPID = "wx9d4ebb33c1e10bbd";
		api = WXAPIFactory.createWXAPI(this, WX_APPID, false);
		api.registerApp(WX_APPID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		String result;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "禁止";
			break;
		default:
			result = "其他";
			break;
		}
		Toast.makeText(getApplicationContext(), result, 1).show();

	}

}
