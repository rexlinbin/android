package com.bccv.strategy.sns.tencent.weixin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bccv.strategy.sns.SNSLoginManager.AuthorizingStateListener;
import com.bccv.strategy.sns.SNSShareManager;
import com.bccv.strategy.sns.SNSShareManager.ShareStateListener;
import com.bccv.strategy.sns.bean.ShareInfo;
import com.bccv.strategy.utils.L;
import com.bccv.strategy.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WeiXinShareUtil{

	private static final String TAG = "WeiXinShareUtil";
	public static final String APP_ID = "wxc646c34412d1989e";
	public static final String APP_SECRET = "11beb5ad39fce7cd25499b343632a262";
	
	private static final int THUMB_SIZE = 100;
	
	private IWXAPI wxApi;
	private Context mContext;
	
	private Handler mHandler;
	
	private ShareStateListener mShareStateListener;
	
	public AuthorizingStateListener mAuthorizingStateListener;
	
	private static WeiXinShareUtil mWeiXinShareUtil;
	
	private WeiXinShareUtil(Context context){
		mContext = context;
		wxApi = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
		wxApi.registerApp(APP_ID);
		iniHandler();
	}
	
	public static WeiXinShareUtil getInstance(Context context){
		if(mWeiXinShareUtil==null){
			mWeiXinShareUtil = new WeiXinShareUtil(context);
		}
		return mWeiXinShareUtil;
	}
	
	@SuppressLint("HandlerLeak")
	private void iniHandler(){
	 	   mHandler = new Handler(mContext.getMainLooper()){
	 		   
	 			@Override
	 			public void handleMessage(Message msg) {
	 				switch (msg.what) {
	 				case  ShareStateListener.SHARING:
	 					mShareStateListener.shareAction(ShareStateListener.SHARING, (String)msg.obj,SNSShareManager.TENCENT_WEIXIN_TYPE);
	 					break;
	 				case  ShareStateListener.SHARE_SUCCESS:
	 					mShareStateListener.shareAction(ShareStateListener.SHARE_SUCCESS, (String)msg.obj,SNSShareManager.TENCENT_WEIXIN_TYPE);
	 					break;
	 				case  ShareStateListener.SHARE_CANCEL:
	 					mShareStateListener.shareAction(ShareStateListener.SHARE_CANCEL, (String)msg.obj,SNSShareManager.TENCENT_WEIXIN_TYPE);
	 					break;
	 				case  ShareStateListener.SHARE_FAILED:
	 					mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, (String)msg.obj,SNSShareManager.TENCENT_WEIXIN_TYPE);
	 					break;
	 	
	 				default:
	 					break;
	 				}
	 			}
	 	   };
	    }
	
	public void shareMessage(final ShareInfo shareInfo,
			final ShareStateListener mShareStateListener) {
			wxApi = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
			wxApi.registerApp(APP_ID);
		
		this.mShareStateListener = mShareStateListener;
		
		L.v(TAG, "shareMessage start --- ");
		
		L.v(TAG, "shareMessage  shareInfo : >> " + shareInfo.toString());
		if(TextUtils.isEmpty(shareInfo.title)||TextUtils.isEmpty(shareInfo.titleUrl)){
			if(mShareStateListener!=null){
				mHandler.obtainMessage(ShareStateListener.SHARE_FAILED, "分享失败").sendToTarget();
			}
			return ;
		}
		
		if(!wxApi.isWXAppInstalled()){
			
			L.v(TAG, "shareMessage  wxApi.isWXAppInstalled() : >> " + wxApi.isWXAppInstalled());
			//TODO 
//			new WeixinDownloadDialog(mContext, ActivityQueue.popIndex(0));
			
			Toast.makeText(mContext, "微信版本过低或未安装微信客户端", Toast.LENGTH_SHORT).show();
			if(mShareStateListener!=null){
				mHandler.obtainMessage(ShareStateListener.SHARE_CANCEL, "微信版本过低或未安装微信客户端").sendToTarget();
			}
			return ;
		}

		new Thread(){
			
			@Override
			public void run() {
				
				try {
					WXWebpageObject webpage = new WXWebpageObject();
					WXMediaMessage msg = new WXMediaMessage();
					Bitmap bmp = null;
					
//					测试数据
//					shareInfo.imageUrl = "http://ww3.sinaimg.cn/bmiddle/64f9539ejw1ef56zm81pvj20qo0is75s.jpg";
					if(!TextUtils.isEmpty(shareInfo.imagePath)
							&&new File(shareInfo.imagePath).exists()){
						bmp = BitmapFactory.decodeFile(shareInfo.imagePath);
					}else if(!TextUtils.isEmpty(shareInfo.imageUrl)){
						if(!shareInfo.imageUrl.startsWith("http://")){
							shareInfo.imageUrl = "http://" + shareInfo.imageUrl;
						}
						bmp = BitmapFactory.decodeStream(new URL(shareInfo.imageUrl).openStream());
//						bmp = BitmapFactory.decodeStream(
//								new URL("http://ww3.sinaimg.cn/bmiddle/64f9539ejw1ef56zm81pvj20qo0is75s.jpg").openStream());
					}
					
					webpage.webpageUrl = shareInfo.titleUrl;
					//TODO 是否需要设置默认连接
//					测试数据
//					webpage.webpageUrl = "http://ww3.sinaimg.cn/bmiddle/64f9539ejw1ef56zm81pvj20qo0is75s.jpg";
					
					msg.mediaObject = webpage;
					msg.title = shareInfo.title; //分享标题
					
					msg.description = shareInfo.text; //分享内容
					
//					测试数据
//					msg.title = "微信测试title";
//					msg.description = "微信分享测试";
					
					SendMessageToWX.Req req = new SendMessageToWX.Req();
					if(bmp==null){
//						bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_weixin160);
					}
					if(bmp!=null){
						bmp = compressImage(bmp);
						Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
						bmp.recycle();
						msg.thumbData = bmpToByteArray(thumbBmp, true);//设置缩略图
						req.transaction = buildTransaction("img");
					}else{
						L.v(TAG, "shareMessage","bmp is null");
					}
					
					req.message = msg;
					
					if(shareInfo.SNSType == SNSShareManager.TENCENT_WEIXIN_TYPE){
						L.v(TAG, "shareMessage","SNSType : TENCENT_WEIXIN_TYPE");
						req.scene = SendMessageToWX.Req.WXSceneSession;	
					}else{
						L.v(TAG, "shareMessage","SNSType : TENCENT_WEIXIN_FRIEND_TYPE");
						req.scene = SendMessageToWX.Req.WXSceneTimeline;
					}
					wxApi.sendReq(req);
					WXEntryActivity.isLogin = false;
					mHandler.obtainMessage(ShareStateListener.SHARE_CANCEL, "微信cancel").sendToTarget();
				
				} catch (Exception e) {
					e.printStackTrace();
					L.e(TAG, "shareMessage", e.getMessage());
					if(mShareStateListener!=null){
						mHandler.obtainMessage(ShareStateListener.SHARE_FAILED, "分享失败").sendToTarget();
					}
				}
			};
			
		}.start();
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private Bitmap compressImage(Bitmap image) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>25) {//循环判断如果压缩后图片是否大于25kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        }  
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;  
    } 
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	/**
	 * 微信授权登陆
	 * @param authorizingStateListener
	 */
	public void login(AuthorizingStateListener authorizingStateListener){
		mAuthorizingStateListener = authorizingStateListener;
		
		wxApi = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
		wxApi.registerApp(APP_ID);
		if(!wxApi.isWXAppInstalled()||!wxApi.isWXAppSupportAPI()){
			
			Log.v(TAG, "login  wxApi.isWXAppInstalled() : >> " + wxApi.isWXAppInstalled());
			Toast.makeText(mContext, "微信版本过低或未安装微信客户端", Toast.LENGTH_SHORT).show();
			//TODO 
//			new WeixinDownloadDialog(mContext, ActivityQueue.popIndex(0));
			if(mAuthorizingStateListener!=null){
				mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_CANCEL, "cancel waitting");
			}
			
//			Toast.makeText(mContext, "微信版本过低或未安装微信客户端", Toast.LENGTH_SHORT).show();
			return ;
		}
		Log.v(TAG, " weixin logining ");
		
		//TODO 微信授权
		SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "bangyang_wechat_login";
		wxApi.sendReq(req);
		WXEntryActivity.isLogin = true;
		
		if(mAuthorizingStateListener!=null){
			mAuthorizingStateListener.authrizeAction(AuthorizingStateListener.AUTHORIZE_CANCEL, "cancel waitting");
		}
		
	}
	
}
