package com.utils.net;



import java.util.List;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class WifiUtils {//定义一个WifiManager对象  
    public static WifiManager mWifiManager;  
    //定义一个WifiInfo对象  
    private static WifiInfo mWifiInfo;  
    //扫描出的网络连接列表  
    private List<ScanResult> mWifiList;  
    //网络连接列表  
    private List<WifiConfiguration> mWifiConfigurations;  
    WifiLock mWifiLock;  
    private static Context context;
	
	public enum WifiCipherType {
		WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
	}
    
    
    
    public WifiUtils(Context context){ 
    	this.context=context;
        //取得WifiManager对象  
        mWifiManager=(WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        //取得WifiInfo对象  
        mWifiInfo=mWifiManager.getConnectionInfo();  
    }  
    
    
    
    
    //打开wifi  
    public void openWifi(){  
        if(!mWifiManager.isWifiEnabled()){  
            mWifiManager.setWifiEnabled(true);  
        }  
    }  
    //关闭wifi  
    public void closeWifi(){  
        if(mWifiManager.isWifiEnabled()){  
            mWifiManager.setWifiEnabled(false);  
        }  
    }  
     // 检查当前wifi状态    
    public int checkState() {    
        return mWifiManager.getWifiState();    
    }    
    //锁定wifiLock  
    public void acquireWifiLock(){  
        mWifiLock.acquire();  
    }  
    //解锁wifiLock  
    public void releaseWifiLock(){  
        //判断是否锁定  
        if(mWifiLock.isHeld()){  
            mWifiLock.acquire();  
        }  
    }  
    //创建一个wifiLock  
    public void createWifiLock(){  
        mWifiLock=mWifiManager.createWifiLock("test");  
    }  
    //得到配置好的网络  
    public List<WifiConfiguration> getConfiguration(){  
        return mWifiConfigurations;  
    }  
    //指定配置好的网络进行连接  
    public void connetionConfiguration(int index){  
        if(index>mWifiConfigurations.size()){  
            return ;  
        }  
        //连接配置好指定ID的网络  
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);  
    }  
    public void startScan(){  
        mWifiManager.startScan();  
        //得到扫描结果  
        mWifiList=mWifiManager.getScanResults();  
        //得到配置好的网络连接  
        mWifiConfigurations=mWifiManager.getConfiguredNetworks();  
    }  
    //得到网络列表  
    public List<ScanResult> getWifiList(){  
        return mWifiList;  
    }  
    //查看扫描结果  
    public StringBuffer lookUpScan(){  
        StringBuffer sb=new StringBuffer();  
        for(int i=0;i<mWifiList.size();i++){  
            sb.append("Index_" + new Integer(i + 1).toString() + ":");  
             // 将ScanResult信息转换成一个字符串包    
            // 其中把包括：BSSID、SSID、capabilities、frequency、level    
            sb.append((mWifiList.get(i)).toString()).append("\n");  
        }  
        return sb;    
    }  
    public String getMacAddress(){  
        return (mWifiInfo==null)?"NULL":mWifiInfo.getMacAddress();  
    }  
    public String getBSSID(){  
        return (mWifiInfo==null)?"NULL":mWifiInfo.getBSSID();  
    }  
    public int getIpAddress(){  
        return (mWifiInfo==null)?0:mWifiInfo.getIpAddress();  
    }  
    //得到连接的ID  
    public int getNetWordId(){  
        return (mWifiInfo==null)?0:mWifiInfo.getNetworkId(); 
    }  
    //得到wifiInfo的所有信息  
    public static WifiInfo getWifiInfo(){  
    	WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo();  
    }  
    //添加一个网络并连接  
    public  void addNetWork(WifiConfiguration configuration){  
        int wcgId=mWifiManager.addNetwork(configuration);  
        mWifiManager.enableNetwork(wcgId, true);  
    }  
    //断开指定ID的网络  
    public void disConnectionWifi(int netId){  
        mWifiManager.disableNetwork(netId);  
        mWifiManager.disconnect();  
    } 
    
	//判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
	public int IsConfiguration(String SSID){
		Log.i("IsConfiguration",String.valueOf(mWifiConfigurations.size()));
		for(int i = 0; i < mWifiConfigurations.size(); i++){
			Log.i(mWifiConfigurations.get(i).SSID,String.valueOf( mWifiConfigurations.get(i).networkId));
			if(mWifiConfigurations.get(i).SSID.equals(SSID)){//地址相同
				return mWifiConfigurations.get(i).networkId;
			}
		}
		return -1;
	}

	//连接指定Id的WIFI
	public boolean ConnectWifi(int wifiId){
		for(int i = 0; i < mWifiConfigurations.size(); i++){
			WifiConfiguration wifi = mWifiConfigurations.get(i);
			if(wifi.networkId == wifiId){
				while(!(mWifiManager.enableNetwork(wifiId, true))){//激活该Id，建立连接
					Log.e("ConnectWifi",String.valueOf(mWifiConfigurations.get(wifiId).status));//status:0--已经连接，1--不可连接，2--可以连接
					
				}
				return true;
			}
		}
		return false;
	}



	//添加指定WIFI的配置信息,原列表不存在此SSID
		public int AddWifiConfig(List<ScanResult> wifiList,String ssid,String pwd){
			int wifiId = -1;
			for(int i = 0;i < wifiList.size(); i++){
				ScanResult wifi = wifiList.get(i);
				if(wifi.SSID.equals(ssid)){
					Log.i("AddWifiConfig","equals");
					WifiConfiguration wifiCong = new WifiConfiguration();
					wifiCong.SSID = "\""+wifi.SSID+"\"";//\"转义字符，代表"
					wifiCong.preSharedKey = "\""+pwd+"\"";//WPA-PSK密码
					wifiCong.hiddenSSID = false;
					wifiCong.status = WifiConfiguration.Status.ENABLED;
					wifiId = mWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
					if(wifiId != -1){
						return wifiId;
					}
				}
			}
			return wifiId;
		}
		

		public static String long2ip(long ip){ 
			
			StringBuffer sb=new StringBuffer();  
			sb.append(String.valueOf((int)(ip&0xff)));  
			sb.append('.');  
			sb.append(String.valueOf((int)((ip>>8)&0xff)));  
			sb.append('.');  
			sb.append(String.valueOf((int)((ip>>16)&0xff)));  
			sb.append('.');  
			sb.append(String.valueOf((int)((ip>>24)&0xff)));  
			return sb.toString();  
		} 

		public static android.net.DhcpInfo getDhcpInfo(Context mContext){
			
		
			
			DhcpInfo di = mWifiManager.getDhcpInfo();  
			  
			return di;
		}
	
		
		public static WifiCipherType getWifiCipher(String capability){
			
			String cipher = getEncryptString(capability);
			
			if(cipher.contains("WEP")){
				
				return WifiCipherType.WIFICIPHER_WEP;
			}else if(cipher.contains("WPA") || cipher.contains("WPA2") || cipher.contains("WPS")){
				
				return WifiCipherType.WIFICIPHER_WPA;
			}else if(cipher.contains("unknow")){
				
				return WifiCipherType.WIFICIPHER_INVALID;
			}else{
				return WifiCipherType.WIFICIPHER_NOPASS;
			}
		}	
		
		public static String getEncryptString(String capability){
			
			
			StringBuilder sb = new StringBuilder();
			
			if(TextUtils.isEmpty(capability))
				return "unknow";
			
			if(capability.contains("WEP")){
				
				sb.append("WEP");
				
				return sb.toString();
			}
			
			if(capability.contains("WPA")){
				
				sb.append("WPA");
				
			}
			if(capability.contains("WPA2")){
				
				sb.append("/");
				
				sb.append("WPA2");
				
			}
			
			if(capability.contains("WPS")){
				
				sb.append("/");
				
				sb.append("WPS");
				
			}
			
			if(TextUtils.isEmpty(sb))
				return "OPEN";
			
			return sb.toString();
		}
		public static boolean removeWifi(Context mContext ,int networkId){
		
			
			WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			
			return wm.removeNetwork(networkId);
			
		}

		public static List<WifiConfiguration> getConfigurations(Context mContext){
			
		
			List<WifiConfiguration> mList = mWifiManager.getConfiguredNetworks();
			
			return mList;
		}
		
		
		
		public static boolean isWifiOpen(Context mContext){
			
			return  mWifiManager.isWifiEnabled() ;
				
		}	
		
		
		
		public static void openWifi(final Context mContext , final IWifiOpen mCallBack){
			
			new Thread(
					new Runnable(){

						@Override
						public void run() {
							
							WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
							
							wm.setWifiEnabled(true);
							
							while(wm.getWifiState() == WifiManager.WIFI_STATE_ENABLING){
								
							}
							
							Log.d(WifiUtils.class.getSimpleName(), "openWifi finish... " + wm.getWifiState());
							
							if(mCallBack != null){
								
								mCallBack.onWifiOpen(wm.getWifiState());
							}
						}
						
					}).start();
			
		}
		
		
		
		
		public interface IWifiOpen{
			
			public void onWifiOpen(int state);
		}

		
		public static WifiConfiguration createWifiConfig(String SSID, String Password,

				WifiCipherType Type) {

					WifiConfiguration config = new WifiConfiguration();

					config.allowedAuthAlgorithms.clear();

					config.allowedGroupCiphers.clear();

					config.allowedKeyManagement.clear();

					config.allowedPairwiseCiphers.clear();

					config.allowedProtocols.clear();

					if(!SSID.startsWith("\"")){
						
						SSID = "\"" + SSID + "\"";
					}
					config.SSID =  SSID ;
					
					Log.d(WifiUtils.class.getSimpleName(), config.SSID );

					// 无密码

					if (Type == WifiCipherType.WIFICIPHER_NOPASS) {

						config.wepKeys[0] = "\"" + "\"";

						config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

						config.wepTxKeyIndex = 0;

					}

					// WEP加密

					if (Type == WifiCipherType.WIFICIPHER_WEP) {

						config.preSharedKey = "\"" + Password + "\"";

						config.hiddenSSID = true;

						config.allowedAuthAlgorithms

						.set(WifiConfiguration.AuthAlgorithm.SHARED);

						config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);

						config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

						config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

						config.allowedGroupCiphers

						.set(WifiConfiguration.GroupCipher.WEP104);

						config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

						config.wepTxKeyIndex = 0;

					}

					// WPA加密

					if (Type == WifiCipherType.WIFICIPHER_WPA) {

						config.preSharedKey = "\"" + Password + "\"";

						config.hiddenSSID = true;

//						 config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

//						 config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

						 config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

//						 config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

//						 config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

						 config.status = WifiConfiguration.Status.ENABLED;

					}

					return config;

				}
		
		
		public static boolean addNetWork(WifiConfiguration cfg ,Context mContext){
			
			WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			
			WifiInfo mInfo = wm.getConnectionInfo();
			
			if(mInfo != null){
				
				wm.disableNetwork(mInfo.getNetworkId());
//				wm.disconnect();
			}
			
			boolean flag = false;
			
			
			if(cfg.networkId > 0){
				
				Log.d(WifiUtils.class.getSimpleName(), "cfg networkId = " + cfg.networkId);
				
				flag = wm.enableNetwork(cfg.networkId,true);
				
				wm.updateNetwork(cfg);
			}else{
				
				int netId = wm.addNetwork(cfg);
				
				
				Log.d(WifiUtils.class.getSimpleName(), "after adding netId = " + netId);
				
				if(netId > 0){
					wm.saveConfiguration();
					flag = wm.enableNetwork(netId, true);
				}
				else{
					
				Toast.makeText(mContext, "创建连接失败", Toast.LENGTH_SHORT).show();
				}
			}
			
			return flag;
		}
}














