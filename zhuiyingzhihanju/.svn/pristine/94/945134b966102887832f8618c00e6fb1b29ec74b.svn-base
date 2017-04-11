package com.utils.share;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.bccv.zhuiyingzhihanju.R;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.utils.tools.BitmapUtil;
import com.utils.tools.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class ShareSDK {
	private static ShareCallback callback;
	private static String[] resultStrings;
	public static String titleString;
	public static String contentString;
	public static String picString;

	public void showShare(final Activity activity, View view) {
		Context context = activity.getApplicationContext();
		// ShareSDK.callback = callback;
		// ShareSDK.resultStrings = resultStrings;
		View shareView = View.inflate(context, R.layout.sharepop, null);
		PopupWindow popuWindow = new PopupWindow(shareView, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);
		popuWindow.setBackgroundDrawable(cd);
		popuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

		// shareQQ(shareView, activity);
		// shareQzone(shareView, activity);
		// shareWeiXin(shareView, activity);
		// shareWeiXinCircle(shareView, activity);
		// shareSina(shareView, activity);

		popuWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub

			}
		});

	}

	private Activity activity;
	private IWXAPI api;
	private Tencent mTencent;
	private AuthInfo mAuthInfo;
	private Oauth2AccessToken mAccessToken;
	private SsoHandler mSsoHandler;
	private IWeiboShareAPI mWeiboShareAPI;

	public ShareSDK(Activity activity) {
		this.activity = activity;
		String WX_APPID = "wx9d4ebb33c1e10bbd";
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(activity, WX_APPID, false);
		api.registerApp(WX_APPID);

		String QQ_APPID = "3102255791";
		mTencent = Tencent.createInstance(QQ_APPID, activity.getApplicationContext());

		String SINA_APP_KEY = "1605736755"; // 应用的APP_KEY
		String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页
		String SINA_SCOPE = "";
		mAuthInfo = new AuthInfo(activity, SINA_APP_KEY, SINA_REDIRECT_URL, SINA_SCOPE);
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, SINA_APP_KEY);
		mWeiboShareAPI.registerApp(); // 将应用注册到微博客户端

	}

	public void shareQQ(String title, String content, String thumb, String url) {
		Bundle bundle = new Bundle();
		// 这条分享消息被好友点击后的跳转URL。
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
		// 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		// 分享的图片URL
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumb);
		// 分享的消息摘要，最长50个字
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);

		// 标识该消息的来源应用，值为应用名称+AppId。 
		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
		mTencent.shareToQQ(activity, bundle, null);
	}

	public void shareQZone(String title, String content, String thumb, String url) {
		Bundle bundle = new Bundle();
		// 这条分享消息被好友点击后的跳转URL。
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
		// 分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_SUMMARY不能全为空，最少必须有一个是有值的。
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		// 分享的图片URL
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, thumb);
		// 分享的消息摘要，最长50个字
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);

		bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

		mTencent.shareToQQ(activity, bundle, null);
	}

	public void shareWeiXin(final String title, final String content, final String thumb, final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = url;

					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = title;
					msg.description = content;

					if (!StringUtils.isEmpty(thumb)) {
						Bitmap bmp = BitmapFactory.decodeStream(new URL(thumb).openStream());
						Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
						bmp.recycle();
						if (thumbBmp != null) {
							msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
						}
						
					}
					

					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneSession;
					api.sendReq(req);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void shareWeiXinCircle(final String title, final String content, final String thumb, final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					WXWebpageObject webpage = new WXWebpageObject();
					webpage.webpageUrl = url;

					WXMediaMessage msg = new WXMediaMessage(webpage);
					msg.title = title;
					msg.description = content;

					if (!StringUtils.isEmpty(thumb)) {
						Bitmap bmp = BitmapFactory.decodeStream(new URL(thumb).openStream());
						Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
						bmp.recycle();
						if (thumbBmp != null) {
							msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
						}
					}
					

					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;
					api.sendReq(req);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void shareSina(final String title, final String content, final String thumb, final String url) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				WeiboMultiMessage weiboMessage = new WeiboMultiMessage();// 初始化微博的分享消息

				weiboMessage.mediaObject = getWebpageObj(title, content, thumb, url);

				SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
				request.transaction = String.valueOf(System.currentTimeMillis());
				request.multiMessage = weiboMessage;

				mWeiboShareAPI.sendRequest(activity, request, mAuthInfo, "", new WeiboAuthListener() {

					@Override
					public void onWeiboException(WeiboException arg0) {
						Toast.makeText(activity.getApplicationContext(), "分享失败", 1).show();
					}

					@Override
					public void onComplete(Bundle bundle) {
						// TODO Auto-generated method stub
						Toast.makeText(activity.getApplicationContext(), "分享成功", 1).show();
					}

					@Override
					public void onCancel() {
						Toast.makeText(activity.getApplicationContext(), "分享取消", 1).show();
					}
				});
			}
		}).start();

	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj(String title, String content, String thumb, String url) {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.title = title;
		mediaObject.description = content;

		Bitmap bmp;
		try {
			bmp = BitmapFactory.decodeStream(new URL(thumb).openStream());
			Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
			bmp.recycle();
			// 设置 Bitmap 类型的图片到视频对象里 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
			mediaObject.thumbData = BitmapUtil.bmpToByteArray(thumbBmp, true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mediaObject.actionUrl = url;
		mediaObject.defaultText="韩剧迷";
		
		return mediaObject;
	}

	public void onResult(int requestCode, int resultCode, Intent data) {
		if (null != mTencent)
			mTencent.onActivityResult(requestCode, resultCode, data);

		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}

	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

}
