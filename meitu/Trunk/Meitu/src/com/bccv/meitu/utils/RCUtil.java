package com.bccv.meitu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.ConnectionStatusListener;
import io.rong.imkit.RongIM.ConnectionStatusListener.ConnectionStatus;
import io.rong.imkit.RongIM.GetUserInfoProvider;
import io.rong.imkit.RongIM.LocationProvider.LocationCallback;
import io.rong.imkit.RongIM.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.Message;
import io.rong.imlib.RongIMClient.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.bccv.meitu.R;
import com.bccv.meitu.common.listener.RCConnectionStatusListener;
import com.bccv.meitu.sns.UserInfoManager;
import com.bccv.meitu.ui.activity.LocationActivity;

public class RCUtil {
	private static final String TAG = "RCUtil";
//	private static final String APP_KEY = "82hegw5uh73tx";
	private static final String APP_KEY = "e0x9wycfxjhkq";
	
	public static final String RECEIVE_NOREAD_MESSAGE = "com.bccv.meitu.noread_message";
	public static final String NOREAD_MESSAGE_NUM_KEY = "noread_message_num";
	
	private static Context mContext;
	private static boolean isConnected;
	private static LocationCallback locationCallback;
	
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, UserInfo> chatInfos = new HashMap<Integer, UserInfo>();
	
	//初始化
	public static void initRC(Context context){
		mContext = context;
		RongIM.init(mContext, APP_KEY, R.drawable.ic_launcher);
		setConversationBehaviorListener();
		setLocationProvider();
	}
	
	/**
	 * 设置聊天信息
	 * @param userId
	 * @param name
	 * @param picUrl
	 */
	public static void setCommunicationInfo(String userId,String name,String picUrl){
		
		
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		Set<Integer> keySet = chatInfos.keySet();
		for (Integer key : keySet) {
			if(UserInfoManager.getUserId() != key){
				arrayList.add(key);
			}
		}
		for (Integer key : arrayList) {
			chatInfos.remove(key);
		}
		UserInfo userInfo = new UserInfo(userId, name, picUrl);
		chatInfos.put(Integer.valueOf(userId), userInfo);
	}
	
	/**
	 * 设置融云会话界面 行为操作监听
	 */
	public static void setConversationBehaviorListener(){
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {

            @Override
            public boolean onClickUserPortrait(Context context, RongIMClient.ConversationType conversationType, RongIMClient.UserInfo user) {
            	//TODO 会话界面点击用户头像后执行。
            	
                Logger.v(TAG, "setConversationBehaviorListener onClickUserPortrait", conversationType.getName() + ":" + user.getName());
                return true;
            }

            @Override
            public boolean onClickMessage(Context context, RongIMClient.Message message) {
            	// 会话界面点击消息执行
            	
                if(message.getContent() instanceof LocationMessage){
                	//TODO如果消息为地理消息
                	Logger.v("Begavior", message.getObjectName() + ":" + message.getMessageId());
                    Intent intent = new Intent(context, LocationActivity.class);
                    intent.putExtra("location", message.getContent());
                    context.startActivity(intent);
                	
                }else if(message.getContent() instanceof ImageMessage){
                	// 如果是图像信息   融云已经给做处理
                	ImageMessage l = (ImageMessage)message.getContent();
                	Logger.v(TAG,"Begavior", "LocalUri : " + l.getLocalUri()
                						+ " RemoteUri : " + l.getRemoteUri()
                						+ " ThumUri : " + l.getThumUri());
                	
                }
                Logger.v("Begavior", message.getObjectName() + ":" + message.getMessageId());
                return false;
            }
        });
	}
	
	/**
	 * 设置融云地理位置提供者
	 */
	public static void setLocationProvider(){
		RongIM.setLocationProvider(new RongIM.LocationProvider() {
			@Override
			public void onStartLocation(final Context context, final LocationCallback callback) {
				// 点击发送地址 开始时的回调
				locationCallback = callback;
				context.startActivity(new Intent(context, LocationActivity.class));
			}
		});
	}
	
	/**
	 * 设置地图监听
	 * @param callback
	 */
	public static void setLocationCallBack(LocationCallback callback){
		locationCallback = callback;
	}
	
	/**
	 * 获取地图监听
	 * @return	locationCallback
	 */
	public static LocationCallback getLocationCallback(){
		return locationCallback;
	}
	
    /**
     * 用户信息的提供者。 如果在聊天中遇到的聊天对象是没有登录过的用户（即没有通过融云服务器鉴权过的），
     * RongIM 是不知道用户信息的，RongIM 将调用此 Provider 获取用户信息
     */
	public static void setGetUserInfoProvider(){
		RongIM.setGetUserInfoProvider(new GetUserInfoProvider() {
			@Override
			public UserInfo getUserInfo(String userId) {
				// 返回用户信息
				return chatInfos.get(Integer.valueOf(userId));
			}
		}, false);
	}
	
	
	/**
	 * 链接长连接
	 * @param token
	 * @param connectCallback
	 */
	public static void connect(String token,final RCConnectCallback rcConnectCallback){
		
		Logger.v(TAG, "connect", "token : " + token);
		try {
			RongIM.connect(token, new ConnectCallback(){
				
				@Override
				public void onSuccess(String userId) {
					Logger.v(TAG, "connect onSuccess", "userId : " + userId);
					setConnectionStatusListener(RCConnectionStatusListener.getInstance());
					setConversationBehaviorListener();
					setReceiveMessageListener();
					setGetUserInfoProvider();
					if(rcConnectCallback!=null){
						rcConnectCallback.onSuccess(userId);
					}
				}

				@Override
				public void onError(ErrorCode error) {
					
					Logger.v(TAG, "connect onError", "errorCode : " + error.getValue() + " error : " + error.getMessage());

					if(rcConnectCallback!=null){
						rcConnectCallback.onError(error.getMessage(), error.getValue());
					}
				}
				
			});
		} catch (Exception e) {
			if(rcConnectCallback!=null){
				rcConnectCallback.onError(e.getMessage(), 1011);
			}
			Logger.e(TAG, "connect", e.getClass().getSimpleName());
			
		}
	}
	
	/**
	 * 断开连接
	 * @param isReceivePush	是否接受消息
	 */
	public static void disconnect(boolean isReceivePush){
		
		if(RongIM.getInstance()!=null){
			RongIM.getInstance().disconnect(isReceivePush);
		}
	}
	
	
	/**
	 * 是否链接了
	 * @return
	 */
	public static boolean isConnected(){
		
		if(RongIM.getInstance()!=null){
			ConnectionStatus currentConnectionStatus = RongIM.getInstance().getCurrentConnectionStatus();
			if(currentConnectionStatus==ConnectionStatus.DISCONNECTED
					|| currentConnectionStatus==ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT
					|| currentConnectionStatus==ConnectionStatus.UNKNOWN){
				// 断开了链接  被T  未知错误
				isConnected = false;
			}
		}else{
			return false;
		}
		return isConnected;
	}
	
	/**
	 * 设置链接状态
	 * @param connecteState
	 */
	public static void setConnecteState(boolean connecteState){
		isConnected = connecteState;
	}
	
	
	public static void setConnectionStatusListener(final IRCConnectionStatusListener rcConnectionStatusListener){
		
		if(RongIM.getInstance()!=null){
			RongIM.getInstance().setConnectionStatusListener(new ConnectionStatusListener(){
				
				@Override
				public void onChanged(ConnectionStatus status) {
					if(status==ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT
							&& rcConnectionStatusListener!=null){
						//异地登陆被踢下线
						rcConnectionStatusListener.onKicked();
						isConnected = false;
					}else if(status==ConnectionStatus.CONNECTED){
						isConnected = true;
					}
				}
			});
		}
		
	}
	
	
	public static void setReceiveMessageListener(){

		if(RongIM.getInstance()!=null){
			RongIM.getInstance().setReceiveMessageListener(new OnReceiveMessageListener() {
				
				@Override
				public void onReceived(Message message, int left) {
					//message 消息     left 剩余未拉取的条目
					
					//TODO 获取到消息   获取未读信息条数
					int count = RongIM.getInstance().getTotalUnreadCount();
					Logger.v(TAG, "setReceiveMessageListener onReceived", "TotalUnreadCount : " + count);
					
                    Intent in = new Intent();
                    in.setAction(RECEIVE_NOREAD_MESSAGE);
                    in.putExtra(NOREAD_MESSAGE_NUM_KEY,count);
                    mContext.sendBroadcast(in);
					
				}
				
			});
		}
		
	}
	
	/**
	 * 没有读的消息数量
	 * @return
	 */
	public static int getTotalUnreadCount(){
		int count = 0;
		if(RongIM.getInstance()!=null){
			count = RongIM.getInstance().getTotalUnreadCount();
		}
		return count;
	}
	
	
	public interface RCConnectCallback{
		/**
		 * 登录成功
		 * @param userId
		 */
		void onSuccess(String userId);
		/**
		 * 登录失败
		 * @param msg
		 * @param errorCode
		 */
		void onError(String msg,int errorCode);
	}
	
	public interface IRCConnectionStatusListener{
		/**
		 * 异地登陆被踢
		 */
		void onKicked();
	}
	
	public enum RCConnectionStatus{
		CONNECTED,//链接成功
		CONNECTING,//链接中
		DISCONNECTED,//断开连接
		KICKED_OFFLINE_BY_OTHER_CLIENT,//被T
		LOGIN_ON_WEB,//web登录
		NETWORK_UNAVAILABLE,//网络不可用
		UNKNOWN//未知原因
	}
}
