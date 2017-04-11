package com.bccv.meitu.sns;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bccv.meitu.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ShareSDK {
	
	public static void showShare(Activity activity, View view, ShareInfo shareInfo, shareCallback callback) {
		Context context = activity.getApplicationContext();
		View shareView = View.inflate(context, R.layout.sharepop, null);
		PopupWindow popuWindow = new PopupWindow(shareView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popuWindow.setOutsideTouchable(true);
		popuWindow.setFocusable(true);
		ColorDrawable cd = new ColorDrawable(-0000);
		popuWindow.setBackgroundDrawable(cd);
		popuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		// 首先在您的Activity中添加如下成员变量
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// 设置分享内容
//		mController
//				.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
//		// 设置分享图片, 参数2为图片的url地址
//		mController.setShareMedia(new UMImage(context,
//				"http://www.umeng.com/images/pic/banner_module_social.png"));

		shareQQ(shareView, activity, mController,shareInfo,callback);
		shareQzone(shareView, activity, mController,shareInfo,callback);
		shareWeiXin(shareView, activity, mController,shareInfo,callback);
		shareWeiXinCircle(shareView, activity, mController,shareInfo,callback);
		shareSina(shareView, activity, mController,shareInfo,callback);

	}

	private static void shareQzone(View shareView, final Activity activity,
			final UMSocialService mController, ShareInfo shareInfo,final shareCallback callback) {
		final Context context = activity.getApplicationContext();

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				"1103376300", "c03xvxnhzNR3Iidy");
		qZoneSsoHandler.addToSocialSDK();

		QZoneShareContent qzone = new QZoneShareContent();
		//设置分享文字
		qzone.setShareContent(shareInfo.getContentString());
		//设置点击消息的跳转URL
		qzone.setTargetUrl(shareInfo.getTagUrl());
		//设置分享内容的标题
		qzone.setTitle(shareInfo.getTitleString());
		
		if(!TextUtils.isEmpty(shareInfo.getImageUrl())){
			//设置分享图片
			qzone.setShareImage(new UMImage(activity, shareInfo.getImageUrl()));
		}
		
		mController.setShareMedia(qzone);
		
		ImageView im = (ImageView) shareView.findViewById(R.id.share_QQspace);
		im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Context mContext = context;

				mController.postShare(context, SHARE_MEDIA.QZONE,
						new SnsPostListener() {

							@Override
							public void onStart() {
								Toast.makeText(mContext, "开始分享.",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onComplete(SHARE_MEDIA platform,
									int eCode, SocializeEntity entity) {
								if (eCode == 200) {
									Toast.makeText(mContext, "分享成功.",
											Toast.LENGTH_SHORT).show();
									callback.callback(true);
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									callback.callback(false);
									Toast.makeText(mContext,
											"分享失败[" + eCode + "] " + eMsg,
											Toast.LENGTH_SHORT).show();
								}
							}
						});
			}
		});
	}

	private static void shareQQ(View shareView, final Activity activity,
			final UMSocialService mController, ShareInfo shareInfo, final shareCallback callback) {
		final Context context = activity.getApplicationContext();

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, "1103376300",
				"c03xvxnhzNR3Iidy");
		qqSsoHandler.addToSocialSDK();

		QQShareContent qqShareContent = new QQShareContent();
		//设置分享文字
		qqShareContent.setShareContent(shareInfo.getContentString());
		//设置分享title
		qqShareContent.setTitle(shareInfo.getTitleString());
		//设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(shareInfo.getTagUrl());
		//设置分享图片
		if(!TextUtils.isEmpty(shareInfo.getImageUrl())){
			qqShareContent.setShareImage(new UMImage(activity, shareInfo.getImageUrl()));
		}
		mController.setShareMedia(qqShareContent);
		
		
		ImageView im = (ImageView) shareView.findViewById(R.id.share_QQ);
		im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Context mContext = context;

				mController.postShare(context, SHARE_MEDIA.QQ,
						new SnsPostListener() {

							@Override
							public void onStart() {
								Toast.makeText(mContext, "开始分享.",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onComplete(SHARE_MEDIA platform,
									int eCode, SocializeEntity entity) {
								if (eCode == 200) {
									Toast.makeText(mContext, "分享成功.",
											Toast.LENGTH_SHORT).show();
									callback.callback(true);
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									callback.callback(false);
									Toast.makeText(mContext,
											"分享失败[" + eCode + "] " + eMsg,
											Toast.LENGTH_SHORT).show();
								}
							}
						});
			}
		});
	}

	private static void shareWeiXin(View shareView, Activity activity,
			final UMSocialService mController, ShareInfo shareInfo,final shareCallback callback) {
		final Context context = activity.getApplicationContext();

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxb6aa3591d186f9e8";
		String appSecret = "056f3a765fd2f4c23e07d40537cbf1b6";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
		wxHandler.addToSocialSDK();

		//设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		//设置分享文字
		weixinContent.setShareContent(shareInfo.getContentString());
		//设置title
		weixinContent.setTitle(shareInfo.getTitleString());
		//设置分享内容跳转URL
		weixinContent.setTargetUrl(shareInfo.getTagUrl());
		
		if(!TextUtils.isEmpty(shareInfo.getImageUrl())){
			weixinContent.setShareImage(new UMImage(activity, shareInfo.getImageUrl()));
		}
		
		//设置分享图片
		mController.setShareMedia(weixinContent);
		
		ImageView im = (ImageView) shareView.findViewById(R.id.share_weixin);
		im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Context mContext = context;
				mController.postShare(context, SHARE_MEDIA.WEIXIN,
						new SnsPostListener() {

							@Override
							public void onStart() {
								Toast.makeText(mContext, "开始分享.",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onComplete(SHARE_MEDIA platform,
									int eCode, SocializeEntity entity) {
								if (eCode == 200) {
									Toast.makeText(mContext, "分享成功.",
											Toast.LENGTH_SHORT).show();
									callback.callback(true);
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									callback.callback(false);
									Toast.makeText(mContext,
											"分享失败[" + eCode + "] " + eMsg,
											Toast.LENGTH_SHORT).show();
								}
							}
						});
			}
		});
	}

	private static void shareWeiXinCircle(View shareView, Activity activity,
			final UMSocialService mController, ShareInfo shareInfo,final shareCallback callback) {
		final Context context = activity.getApplicationContext();

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wxb6aa3591d186f9e8";
		String appSecret = "056f3a765fd2f4c23e07d40537cbf1b6";
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		//设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareInfo.getContentString());
		//设置朋友圈title
		circleMedia.setTitle(shareInfo.getTitleString());
		circleMedia.setTargetUrl(shareInfo.getTagUrl());
		
		if(!TextUtils.isEmpty(shareInfo.getImageUrl())){
			circleMedia.setShareImage(new UMImage(activity, shareInfo.getImageUrl()));
		}
		
		mController.setShareMedia(circleMedia);
		
		ImageView im = (ImageView) shareView
				.findViewById(R.id.share_winxinquan);
		im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Context mContext = context;
				mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE,
						new SnsPostListener() {

							@Override
							public void onStart() {
								Toast.makeText(mContext, "开始分享.",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onComplete(SHARE_MEDIA platform,
									int eCode, SocializeEntity entity) {
								if (eCode == 200) {
									Toast.makeText(mContext, "分享成功.",
											Toast.LENGTH_SHORT).show();
									callback.callback(true);
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									callback.callback(false);
									Toast.makeText(mContext,
											"分享失败[" + eCode + "] " + eMsg,
											Toast.LENGTH_SHORT).show();
								}
							}
						});
			}
		});
	}

	private static void shareSina(View shareView, final Activity activity,
			final UMSocialService mController, ShareInfo shareInfo,final shareCallback callback) {
		final Context context = activity.getApplicationContext();
		
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		
		SinaShareContent circleMedia = new SinaShareContent();
		circleMedia.setShareContent(shareInfo.getContentString());
		//设置朋友圈title
		circleMedia.setTitle(shareInfo.getTitleString());
		
		circleMedia.setTargetUrl(shareInfo.getTagUrl());
		
		if(!TextUtils.isEmpty(shareInfo.getImageUrl())){
			circleMedia.setShareImage(new UMImage(activity, shareInfo.getImageUrl()));
		}
		
		mController.setShareMedia(circleMedia);
		
		ImageView im = (ImageView) shareView.findViewById(R.id.share_sina);
		im.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Context mContext = context;
				mController.postShare(activity, SHARE_MEDIA.SINA,
						new SnsPostListener() {

							@Override
							public void onStart() {
								Toast.makeText(mContext, "开始分享.",
										Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onComplete(SHARE_MEDIA platform,
									int eCode, SocializeEntity entity) {
								if (eCode == 200) {
									Toast.makeText(mContext, "分享成功.",
											Toast.LENGTH_SHORT).show();
									callback.callback(true);
								} else {
									String eMsg = "";
									if (eCode == -101) {
										eMsg = "没有授权";
									}
									callback.callback(false);
									Toast.makeText(mContext,
											"分享失败[" + eCode + "] " + eMsg,
											Toast.LENGTH_SHORT).show();
								}
							}
						});
			}
		});
	}
	
	public static class ShareInfo{
		public ShareInfo(){}
		private String titleString;
		private String contentString;
		private String tagUrl;
		private String imageUrl;
		public String getContentString() {
			return contentString;
		}
		public void setContentString(String contentString) {
			this.contentString = contentString;
		}
		public String getTitleString() {
			return titleString;
		}
		public void setTitleString(String titleString) {
			this.titleString = titleString;
		}
		public String getTagUrl() {
			return tagUrl;
		}
		public void setTagUrl(String tagUrl) {
			this.tagUrl = tagUrl;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		
	}
	
	public interface shareCallback {
		public void callback(boolean success);
	}
	

}
