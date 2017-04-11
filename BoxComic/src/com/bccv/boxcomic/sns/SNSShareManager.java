package com.bccv.boxcomic.sns;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.bccv.boxcomic.sns.bean.SNSUserInfo;
import com.bccv.boxcomic.sns.bean.ShareInfo;
import com.bccv.boxcomic.sns.sina.SinaShare;
import com.bccv.boxcomic.sns.tencent.qq.QQshare;
import com.bccv.boxcomic.sns.tencent.weixin.WeiXinShareUtil;
import com.bccv.boxcomic.tool.Logger;
import com.bccv.boxcomic.tool.MyApplication;


public class SNSShareManager {
	
	public static String TAG = "SNSShareManager";
	
	public static String SINA_WEIBO_LOCALSNSINFO = "sinalocal.txt";
	public static String TENCENT_WEIBO_LOCALSNSINFO = "tencentlocal.txt";
	
	public final static int DEFAULT_TYPE = 0;
	public final static int SINA_WEIBO_TYPE = 1;
	public final static int TENCENT_WEIBO_TYPE = 2;
	public final static int TENCENT_QZONE_TYPE = 3;
	public final static int TENCENT_QQ_TYPE = 4;
	public final static int TENCENT_WEIXIN_TYPE = 5;
	public final static int TENCENT_WEIXIN_FRIEND_TYPE = 6;
	
	private static Context mContext;
	
	private ShareStateListener mShareStateListener;
	
	private static SNSShareManager mSNSShareManager;
	
	
	public static void init(Context context){
		mContext = context;
		if(mSNSShareManager==null){
			mSNSShareManager = new SNSShareManager(context);
		}
	}
	
	private SNSShareManager(Context context){
		mContext = context;
	}
	
	
	public static SNSShareManager getInstance(){
		if(mSNSShareManager==null){
			throw new IllegalStateException(" SNSShareManager has not been initialized ");
		}
		return mSNSShareManager;
	}
	
	
	public void setShareStateListener(ShareStateListener shareStateListener) {
		mShareStateListener = shareStateListener;
	}

	public ShareStateListener getShareStateListener() {
		return mShareStateListener;
	}

	public void shareMessage(Activity activity, ShareInfo shareInfo) {
		
		Logger.v(TAG+"shareMessage", "shareInfo : " + shareInfo);
		if(mShareStateListener!=null){
			mShareStateListener.shareAction(ShareStateListener.SHARING, "sharing");
		}
		if(shareInfo==null||TextUtils.isEmpty(shareInfo.text)){
			if(mShareStateListener!=null){
				mShareStateListener.shareAction(ShareStateListener.SHARE_FAILED, "分享失败");
			}
			return;
		}
		
		
		switch (shareInfo.SNSType) {
			case SINA_WEIBO_TYPE:
				share2Sinawb(activity,shareInfo);
				break;
			case TENCENT_QZONE_TYPE:
				share2QZone(activity,shareInfo);
				break;
			case TENCENT_QQ_TYPE:
				share2QQ(activity,shareInfo);
				break;
			case TENCENT_WEIBO_TYPE:
				share2Tencentwb(activity,shareInfo);
				break;
			case TENCENT_WEIXIN_TYPE:
				share2Weixin(shareInfo);
				break;
			case TENCENT_WEIXIN_FRIEND_TYPE:
				share2WeixinFriends(shareInfo);
				break;
			default:
				break;
		}
	}

	
	/**
	 * 分享到新浪微博
	 * @param shareInfo
	 */
	private void share2Sinawb(Activity activity,ShareInfo shareInfo){
		
//		if(TextUtils.isEmpty(shareInfo.imagePath)&&TextUtils.isEmpty(shareInfo.imageUrl)&&FoneUtil.isDefaultShareimageExists()){
//			shareInfo.imagePath = MyApplication.getGlobalContext().getFilesDir()+ "/defaultImage.jpg";
//		}
		
		Bitmap bitmap = null;
		if((TextUtils.isEmpty(shareInfo.imageUrl)||(!shareInfo.imageUrl.startsWith("http://")))
				&&(!TextUtils.isEmpty(shareInfo.imagePath))){
			bitmap = BitmapFactory.decodeFile(shareInfo.imagePath);
		}
		
		SinaShare.getInstance(mContext).share2sina(activity, shareInfo.text+" " +shareInfo.url, shareInfo.imageUrl, bitmap, mShareStateListener);
	}
	
	/**
	 * 分享到QQ
	 * @param shareInfo
	 */
	private void share2QQ(Activity activity, ShareInfo shareInfo){
		
		Logger.v(TAG+ "share2QQ", shareInfo.toString());
		QQshare.getInstance(activity).share2QQ(activity, shareInfo.title, shareInfo.text, shareInfo.imageUrl, shareInfo.titleUrl, mShareStateListener);
		
	}

	/**
	 * 分享到QQ空间
	 * @param shareInfo
	 */
	private void share2QZone(Activity activity, ShareInfo shareInfo){
		
		Logger.v(TAG+ "share2QZone", shareInfo.toString());
		QQshare.getInstance(activity).share2Qzone(activity, shareInfo.title, shareInfo.text, shareInfo.imageUrl, shareInfo.titleUrl, mShareStateListener);

	}
	
	/**
	 * 分享到腾讯微博
	 * @param shareInfo
	 */
	private void share2Tencentwb(Activity activity, ShareInfo shareInfo){
		
//		if(TextUtils.isEmpty(shareInfo.imagePath)&&TextUtils.isEmpty(shareInfo.imageUrl)&&FoneUtil.isDefaultShareimageExists()){
//			shareInfo.imagePath = ApplicationManager.getGlobalContext().getFilesDir()+ "/defaultImage.jpg";
//		}
//		Bitmap bitmap = null;
//		if(TextUtils.isEmpty(shareInfo.imageUrl)||(!shareInfo.imageUrl.startsWith("http://"))){
//			bitmap = BitmapFactory.decodeFile(shareInfo.imagePath);
//		}
		
//		TencentWeiboShare.getInstance(mContext).share2tencentWB(activity, shareInfo.text, shareInfo.imageUrl, bitmap, mShareStateListener);
		
	}

	/**
	 * 分享到微信
	 * @param shareInfo
	 */
	private void share2Weixin(ShareInfo shareInfo){
		
		WeiXinShareUtil.getInstance(mContext).shareMessage(shareInfo, mShareStateListener);
		
	}
	

	/**
	 * 分享到微信朋友圈
	 * @param shareInfo
	 */
	private void share2WeixinFriends(ShareInfo shareInfo){

		WeiXinShareUtil.getInstance(mContext).shareMessage(shareInfo, mShareStateListener);
	}
	
	
	
	
	
	/**
	 * 分享监听
	 * @author user
	 *
	 */
	public interface ShareStateListener{
		
		public final static int  SHARING = 1;
		public final static int  SHARE_SUCCESS = 2;
		public final static int  SHARE_CANCEL = 3;
		public final static int  SHARE_FAILED = 4;
		
		/**
		 * 分享事件的回调
		 * @param action 事件类型
		 * 		ShareStateListener.SHARING					开始分享时调用
		 * 		ShareStateListener.SHARE_SUCCESS			分享成功后调用
		 * 		ShareStateListener.SHARE_CANCEL				取消分享时调用
		 * 		ShareStateListener.SHARE_FAILED				分享失败时调用
		 * @param info 事件信息
		 */
		public void shareAction(int action, String info);
		
		/**
		 * 分享后获取用户信息的事件回调
		 * @param user 用户信息类
		 */
		public void onUserInfoComplete(SNSUserInfo user);
	}
	
}
